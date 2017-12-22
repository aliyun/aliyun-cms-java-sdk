package com.aliyun.openservices.cms.metric.registry;

import com.aliyun.openservices.cms.metric.impl.Metric;
import com.aliyun.openservices.cms.metric.registry.wrapper.MetricWrapper;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public interface MetricWrapperBuilder<T extends Metric> {

    public final static ThreadLocal LOCAL_RESERVOIR = new ThreadLocal();

    T newMetric(RecordLevel level);

    MetricWrapper<T> newWrapper(RecordLevel... levels);
}
