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
 * Created by eli.lyj on 2017/11/1.
 */
public class DoubleMeter implements Metric{

    private static final long TICK_INTERVAL = TimeUnit.SECONDS.toNanos(5);

    private final DoubleRate rate;

    private final AtomicDouble count = new AtomicDouble();
    private final long startTime;
    private final AtomicLong lastTick;
    private final Clock clock;

    public DoubleMeter(RecordLevel level) {
        this(level.getInterval(), TimeUnit.SECONDS);
    }

    /**
     * Creates a new {@link Meter}.
     */
    public DoubleMeter(long time, TimeUnit unit) {
        this(Clock.defaultClock(), time, unit);
    }

    /**
     * Creates a new {@link Meter}.
     *
     * @param clock      the clock to use for the meter ticks
     */
    public DoubleMeter(Clock clock, long time, TimeUnit unit) {
        this.rate = new DoubleRate();
        this.clock = clock;
        this.startTime = this.clock.getTick();
        this.lastTick = new AtomicLong(startTime);
    }

    /**
     * Mark the occurrence of a given number of events.
     *
     * @param value the number of events
     */
    public void mark(double value) {
        count.addAndGet(value);
        rate.update(value);
    }

    public double getCount() {
        return count.getAndSet(0);
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
