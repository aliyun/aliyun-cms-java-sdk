package com.aliyun.openservices.cms.metric.impl;


import com.aliyun.openservices.cms.metric.MetricAttribute;
import com.aliyun.openservices.cms.metric.internal.*;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Math.exp;

/**
 * A meter metric which measures mean throughput and one-, five-, and fifteen-minute
 * exponentially-weighted moving average throughputs.
 *
 * @see EWMA
 */
public class Meter implements Metric{

    private static final long TICK_INTERVAL = TimeUnit.SECONDS.toNanos(5);

    private final Rate rate;

    private final LongAdderAdapter count = LongAdderProxy.create();
    private final long startTime;
    private final AtomicLong lastTick;
    private final Clock clock;


    public Meter(RecordLevel level) {
        this(level.getInterval(), TimeUnit.SECONDS);
    }

    /**
     * Creates a new {@link Meter}.
     */
    public Meter(long time, TimeUnit unit) {
        this(Clock.defaultClock(), time, unit);
    }

    /**
     * Creates a new {@link Meter}.
     *
     * @param clock      the clock to use for the meter ticks
     */
    public Meter(Clock clock, long time, TimeUnit unit) {
        this.rate = new Rate();
        this.clock = clock;
        this.startTime = this.clock.getTick();
        this.lastTick = new AtomicLong(startTime);
    }

    /**
     * Mark the occurrence of an event.
     */
    public void mark() {
        mark(1);
    }

    /**
     * Mark the occurrence of a given number of events.
     *
     * @param n the number of events
     */
    public void mark(long n) {
        count.add(n);
        rate.update(n);
    }

    public long getCount() {
        return count.sumThenReset();
    }

    public double getRate() {
        rate.sample();
        return rate.getRate(TimeUnit.SECONDS);
    }


    @Override
    public Map<String, Number> resultMap() {
        Map<String, Number> ret = new HashMap<String, Number>();
        ret.put(MetricAttribute.SUM_PS.getCode(), getRate());
        ret.put(MetricAttribute.SUM.getCode(), getCount());
        return ret;
    }
}
