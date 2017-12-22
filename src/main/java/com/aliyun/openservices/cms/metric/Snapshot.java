package com.aliyun.openservices.cms.metric;

/**
 * A statistical snapshot of a {@link Snapshot}.
 */
public abstract class Snapshot {

    /**
     * Returns the value at the given quantile.
     *
     * @param quantile    a given quantile, in {@code [0..1]}
     * @return the value in the distribution at {@code quantile}
     */
    public abstract double getValue(double quantile);

    /**
     * Returns the entire set of values in the snapshot.
     *
     * @return the entire set of values
     */
    public abstract double[] getValues();

    /**
     * Returns the number of values in the snapshot.
     *
     * @return the number of values
     */
    public abstract int size();

    /**
     * Returns the median value in the distribution.
     *
     * @return the median value
     */
    public double getP50() {
        return getValue(0.5);
    }

    /**
     * Returns the value at the 75th percentile in the distribution.
     *
     * @return the value at the 75th percentile
     */
    public double getP75() {
        return getValue(0.75);
    }

    /**
     * Returns the value at the 95th percentile in the distribution.
     *
     * @return the value at the 95th percentile
     */
    public double getP95() {
        return getValue(0.95);
    }

    /**
     * Returns the value at the 98th percentile in the distribution.
     *
     * @return the value at the 98th percentile
     */
    public double getP98() {
        return getValue(0.98);
    }

    /**
     * Returns the value at the 99th percentile in the distribution.
     *
     * @return the value at the 99th percentile
     */
    public double getP99() {
        return getValue(0.99);
    }


    /**
     * Returns the highest value in the snapshot.
     *
     * @return the highest value
     */
    public abstract double getMax();

    /**
     * Returns the arithmetic mean of the values in the snapshot.
     *
     * @return the arithmetic mean
     */
    public abstract double getAvg();

    /**
     * Returns the lowest value in the snapshot.
     *
     * @return the lowest value
     */
    public abstract double getMin();

}
