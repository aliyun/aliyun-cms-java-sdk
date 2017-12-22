package com.aliyun.openservices.cms.metric.internal;

import java.util.concurrent.TimeUnit;

import static java.lang.Math.exp;

/**
 * Created by eli.lyj on 2017/11/6.
 */
public class Rate {

    private final LongAdderAdapter uncounted = LongAdderProxy.create();
    private volatile long lastSample;

    private volatile double rate = 0.0;



    /**
     * Update the moving average with a new value.
     *
     * @param n the new value
     */
    public void update(long n) {
        uncounted.add(n);
    }

    public void sample() {
        long count = uncounted.sumThenReset();
        long dis = System.currentTimeMillis() - lastSample;
        lastSample = lastSample + dis;
        rate = count * 1.0/dis/TimeUnit.MILLISECONDS.toNanos(1);
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
