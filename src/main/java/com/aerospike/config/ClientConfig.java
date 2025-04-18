package com.aerospike.config;


import com.aerospike.policies.ClientPolicy;
import com.aerospike.policies.Policy;

import java.util.Objects;

public class ClientConfig implements Config {
    public Integer closeTimeout;
    public Integer maxConnsPerNode = 1000;
    public Integer asyncMinConnsPerNode;
    public Integer macSocketIdle;

    public ClientConfig(){}
    public ClientConfig(int closeTimeout, int maxConnsPerNode, int asyncMinConnsPerNode, int macSocketIdle) {
        this.closeTimeout = closeTimeout;
        this.maxConnsPerNode = maxConnsPerNode;
        this.asyncMinConnsPerNode = asyncMinConnsPerNode;
        this.macSocketIdle = macSocketIdle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(closeTimeout, maxConnsPerNode, asyncMinConnsPerNode, macSocketIdle);
    }

    @Override
    public Policy merge(Config config, Policy policy) {
        ClientPolicy copyClientPolicy = null;
        if (config instanceof WriteConfig && policy instanceof ClientPolicy) {
            var clientConfig = (ClientConfig) config;
            copyClientPolicy = new ClientPolicy((ClientPolicy)policy);

            copyClientPolicy.macSocketIdle = clientConfig.macSocketIdle;
            copyClientPolicy.maxConnsPerNode = clientConfig.maxConnsPerNode;
            copyClientPolicy.closeTimeout = clientConfig.closeTimeout;
            copyClientPolicy.asyncMinConnsPerNode = clientConfig.asyncMinConnsPerNode;
        }

        return copyClientPolicy;
    }
}
