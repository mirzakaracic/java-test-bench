package com.aerospike.policies;

public class WritePolicy  implements Policy {
    public int socketTimeout;
    public int generation;

    public WritePolicy(){}

    public WritePolicy(int socketTimeout, int generation) {
        this.socketTimeout = socketTimeout;
        this.generation = generation;
    }

    public WritePolicy(WritePolicy other)  {
        this.socketTimeout = other.socketTimeout;
        this.generation = other.generation;
    }

    @Override
    public Policy fuse(Policy other) {
        var right = (WritePolicy) other;

        WritePolicy merge = new WritePolicy();

        if (right.socketTimeout != this.socketTimeout && this.socketTimeout == 0) {
            merge.socketTimeout = right.socketTimeout;
        } else if (right.socketTimeout != this.socketTimeout && right.socketTimeout == 0) {
            merge.socketTimeout = this.socketTimeout;
        } else {
            merge.socketTimeout = right.socketTimeout;
        }
        if (right.generation != this.generation && this.generation == 0) {
            merge.generation = right.generation;
        } else if (right.generation != this.generation && right.generation == 0) {
            merge.generation = this.generation;
        } else {
            merge.generation = right.generation;
        }
        return merge;
    }

    @Override
    public String toString() {
        return "WritePolicy{" +
                "socketTimeout=" + socketTimeout +
                ", generation=" + generation +
                '}';
    }
}
