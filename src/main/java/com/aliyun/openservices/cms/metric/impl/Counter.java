package com.aliyun.openservices.cms.metric.impl;

import com.aliyun.openservices.cms.metric.Counting;
import com.aliyun.openservices.cms.metric.MetricAttribute;
import com.aliyun.openservices.cms.metric.internal.LongAdderAdapter;
import com.aliyun.openservices.cms.metric.internal.LongAdderProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * An incrementing and decrementing counter metric.
 */
public class Counter implements Metric, Counting {

    private final LongAdderAdapter count;

    public Counter() {
        this.count = LongAdderProxy.create();
    }

    /**
     * Increment the counter by {@code n}.
     *
     * @param n the amount by which the counter will be increased
     */
    public void inc(long n) {
        count.add(n);
    }

    /**
     * Decrement the counter by {@code n}.
     *
     * @param n the amount by which the counter will be decreased
     */
    public void dec(long n) {
        count.add(-n);
    }

    /**
     * Returns the counter's current value.
     *
     * @return the counter's current value
     */
    @Override
    public long getCount() {
        return count.sumThenReset();
    }

    @Override
    public Map<String, Number> resultMap() {
        Map<String, Number> ret = new HashMap<String, Number>();
        ret.put(MetricAttribute.COUNT.getCode(), getCount());
        return ret;
    }
}
