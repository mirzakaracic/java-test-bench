package com.aerospike.config;


import com.aerospike.policies.Policy;
import com.aerospike.policies.WritePolicy;

import java.util.Objects;

public class WriteConfig implements Config {
    public Integer socketTimeout;
    public Integer generation;

    public WriteConfig(){}

    public WriteConfig(int socketTimeout, int generation) {
        this.socketTimeout = socketTimeout;
        this.generation = generation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(socketTimeout, generation);
    }

    @Override
    public Policy merge(Config config, Policy policy) {
        WritePolicy copyWritePolicy = null;
        if (config instanceof WriteConfig && policy instanceof WritePolicy) {
            var writeConfig = (WriteConfig) config;
            copyWritePolicy = new WritePolicy((WritePolicy)policy);

            copyWritePolicy.socketTimeout = writeConfig.socketTimeout;
            copyWritePolicy.generation = writeConfig.generation;

        }

        return copyWritePolicy;
    }
}
