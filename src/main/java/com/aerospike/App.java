package com.aerospike;

import com.aerospike.config.ClientConfig;
import com.aerospike.config.Config;
import com.aerospike.config.ReadConfig;
import com.aerospike.config.WriteConfig;
import com.aerospike.policies.ClientPolicy;
import com.aerospike.policies.Policy;
import com.aerospike.policies.ReadPolicy;
import com.aerospike.policies.WritePolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


class MergedCache {
    Map<Integer, Policy> configCache;
    Policy sourcePolicy;

    public MergedCache(Policy sourcePolicy) {
        this.configCache = new HashMap<>();
        this.sourcePolicy = sourcePolicy;
    }
}

/*
The driving idea behind split cache is that conifg + user + default = hash. We build and generate policy
for config + default. Will merge with user provided policy if user passes in policy on operation.
If the user does pass in the policy we are going to compute hash for passed in policy. This will have to be
computed on every operation since we can't save/store the cache. This is because users are making changes to
class attributes directly and are not using setters. The second part of the hash config + default will be looked up
since in the first cache.
 */
public class App {
    Map<String, MergedCache> configCache = new HashMap<>();
    // Cache to store config objects found in the config file. Things like Write, Read, Query, .etc
    // This cache is finite in size and will never exceed number of configuration defined in the file

    public void put(Policy policy) {
        MergedCache cache = configCache.get(WriteConfig.class.getName());
        Policy mergedDefaultPolicy = cache.sourcePolicy;
        Map<Integer, Policy> mergedPolicy = cache.configCache;

        if (policy == null) {
            var key = Objects.hash(mergedDefaultPolicy.hashCode());
            if (mergedPolicy.containsKey(key)) {
                System.out.println("Found key from previous put operation" + mergedPolicy.get(key));
            } else {
                System.out.println("Hash not found using default + config ");
                mergedPolicy.put(Objects.hash(mergedDefaultPolicy.hashCode()), mergedDefaultPolicy);
            }
        } else {
            var hash = mergedDefaultPolicy.hashCode();
            var inputHash = policy.hashCode();

            if (mergedPolicy.containsKey(Objects.hash(hash, inputHash))) {
                System.out.println("Found key from previous put operation" + mergedPolicy.get(Objects.hash(hash, inputHash)));
            } else {
                System.out.println("Hash not found using default + config + user policy");
                mergedPolicy.put(Objects.hash(hash, inputHash), policy.fuse(mergedDefaultPolicy));
            }
        }
    }
    public int get(Policy policy) {
        MergedCache cache = configCache.get(ReadConfig.class.getName());
        Policy mergedDefaultPolicy = cache.sourcePolicy;
        Map<Integer, Policy> mergedPolicy = cache.configCache;

        if (policy == null) {
            var key = Objects.hash(mergedDefaultPolicy.hashCode());
            if (mergedPolicy.containsKey(key)) {
                System.out.println("Found key from previous get operation" + mergedPolicy.get(key));
            } else {
                System.out.println("Hash not found using default + config ");
                mergedPolicy.put(Objects.hash(mergedDefaultPolicy.hashCode()), mergedDefaultPolicy);
            }
        } else {
            var hash = mergedDefaultPolicy.hashCode();
            var inputHash = policy.hashCode();

            if (mergedPolicy.containsKey(Objects.hash(hash, inputHash))) {
                System.out.println("Found key from previous get operation" + mergedPolicy.get(Objects.hash(hash, inputHash)));
            } else {
                System.out.println("Hash not found using default + config + user policy");
                mergedPolicy.put(Objects.hash(hash, inputHash), policy.fuse(mergedDefaultPolicy));
            }
        }

        return 0;
    }

    public void onLoad(List<Config> serailized) {
        for (Config config : serailized) {
            String operationType = config.getClass().getName();
            configCache.put(operationType, new MergedCache(fetchPolicy(config)));
        }
    }

    public Policy fetchPolicy(Config config) {
        if (config instanceof WriteConfig) {
            WriteConfig writeConfig = (WriteConfig) config;
            var socketTimeout = writeConfig.socketTimeout == null ? 0 : writeConfig.socketTimeout;
            var generation = writeConfig.generation == null ? 0 : writeConfig.generation;
            return new WritePolicy(socketTimeout, generation);
        }
        if (config instanceof ClientConfig) {
            ClientConfig clientConfig = (ClientConfig) config;
            return new ClientPolicy(clientConfig.closeTimeout,clientConfig.maxConnsPerNode,clientConfig.asyncMinConnsPerNode,clientConfig.macSocketIdle);
        }
        if (config instanceof ReadConfig) {
            ReadConfig readConfig = (ReadConfig) config;
            var totalTimeout = readConfig.totalTime == null ? 0 : readConfig.totalTime;
            var timeoutDelay = readConfig.timeoutDelay == null ? 0 : readConfig.timeoutDelay;

            return new ReadPolicy(timeoutDelay, totalTimeout);
        }

        else
            return null;
    }


    public static void main(String[] args) {
        App app = new App();
        // --- Code section that is representing configuration serialized from file or a service aka config objects
        var writeConfig = new WriteConfig();
        var readConfig = new ReadConfig();
        writeConfig.socketTimeout = 55;
        readConfig.timeoutDelay = 5;

        var userWritePolicy = new WritePolicy(45, 11);
        var userReadPolicy = new ReadPolicy();
        userReadPolicy.totalTimeout = 15;
        List<Config> serializedConfigs = List.of(writeConfig, readConfig);
        app.onLoad(serializedConfigs);
        // ---

        // --- Performing operation
        app.put(null);
        app.put(null);
        app.put(userWritePolicy);
        app.put(userWritePolicy);

        app.get(null);
        app.get(null);
        app.get(null);
        app.get(userReadPolicy);
        app.get(userReadPolicy);
        userReadPolicy.totalTimeout = 12;
        app.get(userReadPolicy);
        app.get(userReadPolicy);
        // ---
    }
}