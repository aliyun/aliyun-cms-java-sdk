package com.aliyun.openservices.cms.metric.impl;

import com.aliyun.openservices.cms.metric.*;
import com.aliyun.openservices.cms.metric.internal.Clock;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.reservoir.ExponentiallyDecayingReservoir;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.aliyun.openservices.cms.metric.MetricAttribute.*;

/**
 * Created by eli.lyj on 2017/10/31.
 */
public class Timer implements Metered, Sampling {


    private final Meter meter;
    private final Reservoir reservoir;
    private final Clock clock;

    private static final double factor = 1.0d/TimeUnit.MILLISECONDS.toNanos(1);

    /**
     * Creates a new {@link Timer} using an {@link ExponentiallyDecayingReservoir} and the default
     * {@link Clock}.
     * RecordLevel level
     */
    public Timer(RecordLevel level) {
        this(level.getInterval(), TimeUnit.MILLISECONDS, new ExponentiallyDecayingReservoir(level));
    }

    /**
     * Creates a new {@link Timer} that uses the given {@link Reservoir}.
     */
    public Timer(long time, TimeUnit unit) {
        this(time, unit, new ExponentiallyDecayingReservoir());
    }

    /**
     * Creates a new {@link Timer} that uses the given {@link Reservoir}.
     *
     * @param reservoir the {@link Reservoir} implementation the timer should use
     */
    public Timer(long time, TimeUnit unit, Reservoir reservoir) {
        this(time, unit, reservoir, Clock.defaultClock());
    }

    /**
     * Creates a new {@link Timer} that uses the given {@link Reservoir} and {@link Clock}.
     * {@link Clock}.
     * @param time emwa time for pre-agg
     * @param unit
     * @param reservoir the {@link Reservoir} implementation the timer should use
     * @param clock  the {@link Clock} implementation the timer should use
     */
    public Timer(long time, TimeUnit unit, Reservoir reservoir, Clock clock) {
        this.meter = new Meter(clock, time, unit);
        this.clock = clock;
        this.reservoir = reservoir;
    }

    /**
     * Adds a recorded duration.
     *
     * @param duration the length of the duration
     * @param unit     the scale unit of {@code duration}
     */
    public void update(long duration, TimeUnit unit) {
        update(unit.toNanos(duration));
    }



    /**
     * Times and records the duration of event.
     *
     * @param event a {@link Callable} whose {@link Callable#call()} method implements a process
     *              whose duration should be timed
     * @param <T>   the type of the value returned by {@code event}
     * @return the value returned by {@code event}
     * @throws Exception if {@code event} throws an {@link Exception}
     */
    public <T> T time(Callable<T> event) throws Exception {
        final long startTime = clock.getTick();
        try {
            return event.call();
        } finally {
            update(clock.getTick() - startTime);
        }
    }

    /**
     * Times and records the duration of event.
     *
     * @param event a {@link Runnable} whose {@link Runnable#run()} method implements a process
     *              whose duration should be timed
     */
    public void time(Runnable event) {
        final long startTime = clock.getTick();
        try {
            event.run();
        } finally {
            update(clock.getTick() - startTime);
        }
    }

    @Override
    public long getCount() {
        return meter.getCount();
    }

    @Override
    public double getRate() {
        return meter.getRate();
    }

    @Override
    public Snapshot getSnapshot() {
        return reservoir.getSnapshot();
    }

    @Override
    public Snapshot getAndResetSnapshot() {
        return reservoir.getAndResetSnapshot();
    }

    private void update(long duration) {
        if (duration >= 0) {
            reservoir.update(duration);
            meter.mark();
        }
    }

    @Override
    public Map<String, Number> resultMap() {
        Map<String, Number> ret = new HashMap<String, Number>();
        ret.put(COUNT.getCode(), meter.getCount());
        ret.put(COUNT_PS.getCode(), meter.getRate());
        Snapshot snapshot = getAndResetSnapshot();
        ret.put(MAX.getCode(), snapshot.getMax() * factor);
        ret.put(MIN.getCode(), snapshot.getMin() * factor);
        ret.put(AVG.getCode(), snapshot.getAvg() * factor);
        ret.put(P10.getCode(), snapshot.getValue(0.1) * factor);
        ret.put(P20.getCode(), snapshot.getValue(0.2) * factor);
        ret.put(P30.getCode(), snapshot.getValue(0.3) * factor);
        ret.put(P40.getCode(), snapshot.getValue(0.4) * factor);
        ret.put(P50.getCode(), snapshot.getValue(0.5) * factor);
        ret.put(P60.getCode(), snapshot.getValue(0.6) * factor);
        ret.put(P70.getCode(), snapshot.getValue(0.7) * factor);
        ret.put(P75.getCode(), snapshot.getValue(0.75) * factor);
        ret.put(P80.getCode(), snapshot.getValue(0.8) * factor);
        ret.put(P90.getCode(), snapshot.getValue(0.9) * factor);
        ret.put(P95.getCode(), snapshot.getValue(0.95) * factor);
        ret.put(P98.getCode(), snapshot.getValue(0.98) * factor);
        ret.put(P99.getCode(), snapshot.getValue(0.99) * factor);
        return ret;
    }
}
