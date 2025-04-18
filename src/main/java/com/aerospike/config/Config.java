package com.aerospike.config;


import com.aerospike.policies.Policy;

public interface Config {
    Policy merge(Config config, Policy defaultPolicy);
}
