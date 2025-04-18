package com.aerospike.policies;

import java.util.Objects;

public class ReadPolicy implements Policy {
    public int timeoutDelay;
    public int totalTimeout;


    public ReadPolicy(int timeoutDelay, int totalTimeout) {
        this.timeoutDelay = timeoutDelay;
        this.totalTimeout = totalTimeout;
    }

    public ReadPolicy(ReadPolicy other) {
        this.timeoutDelay = other.timeoutDelay;
        this.totalTimeout = other.totalTimeout;
    }

    public ReadPolicy() {}

    @Override
    public int hashCode() {
        return Objects.hash(timeoutDelay, totalTimeout);
    }

    @Override
    public Policy fuse(Policy other) {
        var right = (ReadPolicy) other;

        ReadPolicy merge = new ReadPolicy();

        if (right.totalTimeout != this.totalTimeout && this.totalTimeout == 0) {
            merge.totalTimeout = right.totalTimeout;
        } else if (right.totalTimeout != this.totalTimeout && right.totalTimeout == 0) {
            merge.totalTimeout = this.totalTimeout;
        } else {
            merge.totalTimeout = right.totalTimeout;
        }
        if (right.timeoutDelay != this.timeoutDelay && this.timeoutDelay == 0) {
            merge.timeoutDelay = right.timeoutDelay;
        } else if (right.timeoutDelay != this.timeoutDelay && right.timeoutDelay == 0) {
            merge.timeoutDelay = this.timeoutDelay;
        } else {
            merge.timeoutDelay = right.timeoutDelay;
        }
        return merge;
    }

    @Override
    public String toString() {
        return "ReadPolicy{" +
                "timeoutDelay=" + timeoutDelay +
                ", totalTimeout=" + totalTimeout +
                '}';
    }
}
