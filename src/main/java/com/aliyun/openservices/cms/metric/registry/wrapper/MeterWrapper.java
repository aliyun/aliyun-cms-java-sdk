package com.aliyun.openservices.cms.metric.registry.wrapper;

import com.aliyun.openservices.cms.metric.impl.Meter;
import com.aliyun.openservices.cms.metric.registry.MetricWrapperBuilder;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;

import java.util.concurrent.TimeUnit;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public class MeterWrapper extends MetricWrapper<Meter> {
    public static final MetricWrapperBuilder<Meter> BUILDER = new MetricWrapperBuilder<Meter>() {
        @Override
        public Meter newMetric(RecordLevel level) {
            return new Meter(level.getInterval(), TimeUnit.SECONDS);
        }

        @Override
        public MetricWrapper<Meter> newWrapper(RecordLevel... level) {
            return new MeterWrapper(this, level);
        }
    };
    protected MeterWrapper(MetricWrapperBuilder<Meter> builder, RecordLevel... levels) {
        super(builder, levels);
    }


    public void update(long value) {
        for(Meter metric : metricList) {
            if(metric != null) {
                metric.mark(value);
            }
        }
    }
}
