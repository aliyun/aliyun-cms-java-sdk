package com.aliyun.openservices.cms.metric;

import com.aliyun.openservices.cms.metric.impl.Metric;

/**
 * An object which maintains mean and exponentially-weighted rate.
 */
public interface Metered extends Metric, Counting {
    /**
     * Returns the number of events which have been marked.
     *
     * @return the number of events which have been marked
     */
    long getCount();


    /**
     * Returns the mean rate at which events have occurred since the meter was created.
     *
     * @return the mean rate at which events have occurred since the meter was created
     */
    double getRate();
}

