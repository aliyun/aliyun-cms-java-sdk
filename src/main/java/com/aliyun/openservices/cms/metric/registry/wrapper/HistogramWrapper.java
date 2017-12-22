package com.aliyun.openservices.cms.metric.registry.wrapper;

import com.aliyun.openservices.cms.metric.Reservoir;
import com.aliyun.openservices.cms.metric.impl.Histogram;
import com.aliyun.openservices.cms.metric.registry.MetricWrapperBuilder;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public class HistogramWrapper extends MetricWrapper<Histogram> {

    public static final MetricWrapperBuilder<Histogram> DEFAULT = new MetricWrapperBuilder<Histogram>() {
        @Override
        public Histogram newMetric(RecordLevel level) {
            Reservoir reservoir = (Reservoir) LOCAL_RESERVOIR.get();
            if(reservoir != null) {
                return new Histogram(reservoir);
            }else {
                return new Histogram(level);
            }
        }

        @Override
        public MetricWrapper<Histogram> newWrapper(RecordLevel... level) {
            HistogramWrapper wrapper = new HistogramWrapper(this, level);
            return wrapper;
        }
    };

    public HistogramWrapper(MetricWrapperBuilder<Histogram> builder, RecordLevel... levels) {
        super(builder, levels);
    }

    public void update(long value) {
        for(Histogram metric : metricList) {
            if(metric != null) {
                metric.update(value);
            }
        }
    }
}
