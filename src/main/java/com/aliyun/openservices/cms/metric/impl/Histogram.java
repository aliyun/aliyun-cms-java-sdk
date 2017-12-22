package com.aliyun.openservices.cms.metric.impl;

import com.aliyun.openservices.cms.metric.*;
import com.aliyun.openservices.cms.metric.internal.LongAdderAdapter;
import com.aliyun.openservices.cms.metric.internal.LongAdderProxy;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.reservoir.ExponentiallyDecayingReservoir;

import java.util.HashMap;
import java.util.Map;

import static com.aliyun.openservices.cms.metric.MetricAttribute.*;
import static com.aliyun.openservices.cms.metric.MetricAttribute.P90;

/**
 * Created by eli.lyj on 2017/10/31.
 */
public class Histogram implements Metric, Counting, Sampling {

    private final Reservoir reservoir;
    private final LongAdderAdapter count;


    /**
     * Creates a new {@link Histogram} with the given reservoir.
     */
    public Histogram(RecordLevel level) {
        this(new ExponentiallyDecayingReservoir(level));
    }

    /**
     * Creates a new {@link Histogram} with the given reservoir.
     *
     * @param reservoir the reservoir to create a histogram from
     */
    public Histogram(Reservoir reservoir) {
        this.reservoir = reservoir;
        this.count = LongAdderProxy.create();
    }

    /**
     * Adds a recorded value.
     *
     * @param value the length of the value
     */
    public void update(long value) {
        count.increment();
        reservoir.update(value);
    }

    /**
     * Returns the number of values recorded.
     *
     * @return the number of values recorded
     */
    @Override
    public long getCount() {
        return count.sumThenReset();
    }

    @Override
    public Snapshot getSnapshot() {
        return reservoir.getSnapshot();
    }

    @Override
    public Snapshot getAndResetSnapshot() {
        return reservoir.getAndResetSnapshot();
    }

    @Override
    public Map<String, Number> resultMap() {
        Map<String, Number> ret = new HashMap<String, Number>();
        ret.put(COUNT.getCode(), getCount());
        Snapshot snapshot = reservoir.getAndResetSnapshot();
        ret.put(MAX.getCode(), snapshot.getMax());
        ret.put(MIN.getCode(), snapshot.getMin());
        ret.put(AVG.getCode(), snapshot.getAvg());
        ret.put(P10.getCode(), snapshot.getValue(0.1));
        ret.put(P20.getCode(), snapshot.getValue(0.2));
        ret.put(P30.getCode(), snapshot.getValue(0.3));
        ret.put(P40.getCode(), snapshot.getValue(0.4));
        ret.put(P50.getCode(), snapshot.getValue(0.5));
        ret.put(P60.getCode(), snapshot.getValue(0.6));
        ret.put(P70.getCode(), snapshot.getValue(0.7));
        ret.put(P75.getCode(), snapshot.getValue(0.75));
        ret.put(P80.getCode(), snapshot.getValue(0.8));
        ret.put(P90.getCode(), snapshot.getValue(0.9));
        ret.put(P95.getCode(), snapshot.getValue(0.95));
        ret.put(P98.getCode(), snapshot.getValue(0.98));
        ret.put(P99.getCode(), snapshot.getValue(0.99));
        return ret;
    }
}
