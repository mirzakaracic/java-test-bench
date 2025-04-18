package com.aerospike.policies;

public interface Policy {
    Policy fuse(Policy rightInput);
}
