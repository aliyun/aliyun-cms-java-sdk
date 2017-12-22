package com.aliyun.openservices.cms.metric.registry.wrapper;

import com.aliyun.openservices.cms.metric.impl.Counter;
import com.aliyun.openservices.cms.metric.registry.MetricWrapperBuilder;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public class CounterWrapper extends MetricWrapper<Counter> {

    public static final MetricWrapperBuilder<Counter> DEFAULT = new MetricWrapperBuilder<Counter>() {
        @Override
        public Counter newMetric(RecordLevel level) {
            return new Counter();
        }

        @Override
        public MetricWrapper<Counter> newWrapper(RecordLevel... level) {
            return new CounterWrapper(this, level);
        }
    };


    public CounterWrapper(MetricWrapperBuilder<Counter> builder, RecordLevel... levels) {
        super(builder, levels);
    }

    public void inc(long value) {
        for(Counter metric : metricList) {
            if(metric != null) {
                metric.inc(value);
            }
        }
    }

    public void dec(long value) {
        for(Counter metric : metricList) {
            if(metric != null) {
                metric.dec(value);
            }
        }
    }
}
