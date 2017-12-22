package com.aliyun.openservices.cms.metric;

/**
 * Created by eli.lyj on 2017/10/31.
 */
public interface Reservoir {
    /**
     * Returns the number of values recorded.
     *
     * @return the number of values recorded
     */
    int size();

    /**
     * Adds a new recorded value to the reservoir.
     *
     * @param value a new recorded value
     */
    void update(double value);

    /**
     * Returns a snapshot of the reservoir's values.
     *
     * @return a snapshot of the reservoir's values
     */
    Snapshot getSnapshot();

    /**
     * Returns a snapshot of the reservoir's values.
     * then reset the reservoir
     * @return
     */
    Snapshot getAndResetSnapshot();

    /**
     * reset the reservoir
     */
    void reset();
}
