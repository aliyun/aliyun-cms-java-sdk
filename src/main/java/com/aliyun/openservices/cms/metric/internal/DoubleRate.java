package com.aliyun.openservices.cms.metric.internal;

import java.util.concurrent.TimeUnit;

/**
 * Created by eli.lyj on 2017/11/6.
 */
public class DoubleRate {

    private final AtomicDouble atomicDouble = new AtomicDouble();
    private volatile long lastSample;

    private volatile double rate = 0.0;



    /**
     * Update the moving average with a new value.
     *
     * @param value the new value
     */
    public void update(double value) {
        atomicDouble.addAndGet(value);
    }

    public void sample() {
        double count = atomicDouble.getAndSet(0);
        long dis = System.currentTimeMillis() - lastSample;
        lastSample = lastSample + dis;
        rate = count /dis/ TimeUnit.MILLISECONDS.toNanos(1);
    }


    /**
     * Returns the rate in the given units of time.
     *
     * @param rateUnit the unit of time
     * @return the rate
     */
    public double getRate(TimeUnit rateUnit) {
        return rate * (double) rateUnit.toNanos(1);
    }

}
