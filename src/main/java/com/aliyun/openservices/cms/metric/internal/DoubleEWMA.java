package com.aliyun.openservices.cms.metric.internal;

import com.aliyun.openservices.cms.metric.internal.AtomicDouble;

import java.util.concurrent.TimeUnit;

/**
 * An exponentially-weighted moving average.
 *
 * @see <a href="http://www.teamquest.com/pdfs/whitepaper/ldavg1.pdf">UNIX Load Average Part 1: How
 *      It Works</a>
 * @see <a href="http://www.teamquest.com/pdfs/whitepaper/ldavg2.pdf">UNIX Load Average Part 2: Not
 *      Your Average Average</a>
 * @see <a href="http://en.wikipedia.org/wiki/Moving_average#Exponential_moving_average">EMA</a>
 */
public class DoubleEWMA {

    private volatile boolean initialized = false;
    private volatile double rate = 0.0;

    private final AtomicDouble uncounted = new AtomicDouble();
    private final double alpha, interval;
    /**
     * Create a new EWMA with a specific smoothing constant.
     *
     * @param alpha        the smoothing constant
     * @param interval     the expected tick interval
     * @param intervalUnit the time unit of the tick interval
     */
    public DoubleEWMA(double alpha, long interval, TimeUnit intervalUnit) {
        this.interval = intervalUnit.toNanos(interval);
        this.alpha = alpha;
    }

    /**
     * Update the moving average with a new value.
     *
     * @param n the new value
     */
    public void update(double n) {
        uncounted.addAndGet(n);
    }

    /**
     * Mark the passage of time and decay the current rate accordingly.
     */
    public void tick() {
        final double sum = uncounted.getAndSet(0);
        final double instantRate = sum / interval;
        if (initialized) {
            rate += (alpha * (instantRate - rate));
        } else {
            rate = instantRate;
            initialized = true;
        }
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
