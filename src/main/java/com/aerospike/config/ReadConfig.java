package com.aerospike.config;

import com.aerospike.policies.Policy;
import com.aerospike.policies.ReadPolicy;

public class ReadConfig implements Config {
    public Integer totalTime;
    public Integer timeoutDelay;

    public ReadConfig(int totalTime, int timeoutDelay) {
        this.totalTime = totalTime;
        this.timeoutDelay = timeoutDelay;
    }

    public ReadConfig(){}

    public ReadConfig(ReadConfig other){
        this.totalTime = other.totalTime;
        this.timeoutDelay = other.timeoutDelay;
    }

    @Override
    public Policy merge(Config config, Policy policy) {
        ReadPolicy copyPolicy = null;
        if (config instanceof ReadConfig && policy instanceof ReadPolicy) {
            var readConfig = (ReadConfig) config;
            copyPolicy = new ReadPolicy((ReadPolicy)policy);

            copyPolicy.totalTimeout = readConfig.totalTime;
            copyPolicy.timeoutDelay = readConfig.timeoutDelay;

        }

        return copyPolicy;
    }
}
