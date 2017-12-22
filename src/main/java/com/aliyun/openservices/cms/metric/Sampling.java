package com.aliyun.openservices.cms.metric;

/**
 * An object which samples values.
 */
public interface Sampling {
    /**
     * Returns a snapshot of the values.
     *
     * @return a snapshot of the values
     */
    Snapshot getSnapshot();

    Snapshot getAndResetSnapshot();
}
