package com.aerospike.policies;

public class ClientPolicy implements Policy {
    public int closeTimeout;
    public int maxConnsPerNode = 1000;
    public int asyncMinConnsPerNode;
    public int macSocketIdle;

    public ClientPolicy(){}
    public ClientPolicy(int closeTimeout, int maxConnsPerNode, int asyncMinConnsPerNode, int macSocketIdle) {
        this.closeTimeout = closeTimeout;
        this.maxConnsPerNode = maxConnsPerNode;
        this.asyncMinConnsPerNode = asyncMinConnsPerNode;
        this.macSocketIdle = macSocketIdle;
    }

    public ClientPolicy(ClientPolicy other) {
        this.closeTimeout = other.closeTimeout;
        this.maxConnsPerNode = other.maxConnsPerNode;
        this.asyncMinConnsPerNode = other.asyncMinConnsPerNode;
        this.macSocketIdle = other.macSocketIdle;
    }

    @Override
    public Policy fuse(Policy other) {
        var right = (ClientPolicy) other;

        ClientPolicy merge = new ClientPolicy();

        if (this.asyncMinConnsPerNode != right.asyncMinConnsPerNode && right.asyncMinConnsPerNode != 0){
            merge.asyncMinConnsPerNode = right.asyncMinConnsPerNode;
        } else if (this.asyncMinConnsPerNode != right.asyncMinConnsPerNode && this.asyncMinConnsPerNode != 0) {
            merge.asyncMinConnsPerNode = this.asyncMinConnsPerNode;
        }
        if (this.closeTimeout != right.closeTimeout && right.closeTimeout != 0) {
            merge.closeTimeout = right.closeTimeout;
        }else if (this.closeTimeout != right.closeTimeout && this.closeTimeout != 0) {
            merge.closeTimeout = this.closeTimeout;
        }
        if (this.macSocketIdle != right.macSocketIdle && right.macSocketIdle != 0) {
            merge.macSocketIdle = right.macSocketIdle;
        }else if (this.macSocketIdle != right.macSocketIdle && this.macSocketIdle != 0) {
            merge.macSocketIdle = this.macSocketIdle;
        }
        if (this.maxConnsPerNode != right.maxConnsPerNode && right.maxConnsPerNode != 0) {
            merge.maxConnsPerNode = right.maxConnsPerNode;
        }else if (this.maxConnsPerNode != right.maxConnsPerNode && this.maxConnsPerNode != 0) {
            merge.maxConnsPerNode = this.maxConnsPerNode;
        }
        return merge;
    }

    @Override
    public String toString() {
        return "ClientPolicy{" +
                "closeTimeout=" + closeTimeout +
                ", maxConnsPerNode=" + maxConnsPerNode +
                ", asyncMinConnsPerNode=" + asyncMinConnsPerNode +
                ", macSocketIdle=" + macSocketIdle +
                '}';
    }
}
