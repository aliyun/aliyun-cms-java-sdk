package com.aliyun.openservices.cms.metric.internal;

/**
 * Interface which exposes the LongAdder functionality. Allows different
 * LongAdder implementations to coexist together.
 */
public interface LongAdderAdapter {

    void add(long x);

    long sum();

    void increment();

    void decrement();

    long sumThenReset();
}
