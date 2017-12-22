package com.aliyun.openservices.cms.metric.registry.wrapper;

import com.aliyun.openservices.cms.metric.impl.Counter;
import com.aliyun.openservices.cms.metric.impl.Gauge;
import com.aliyun.openservices.cms.metric.registry.MetricWrapperBuilder;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public class GaugeWrapper extends MetricWrapper<Gauge>{

    public static final MetricWrapperBuilder<Gauge> DEFAULT = new MetricWrapperBuilder<Gauge>() {
        @Override
        public Gauge newMetric(RecordLevel level) {
            return (Gauge) LOCAL_RESERVOIR.get();
        }

        @Override
        public MetricWrapper<Gauge> newWrapper(RecordLevel... level) {
            GaugeWrapper wrapper = new GaugeWrapper(this, level);
            LOCAL_RESERVOIR.remove();
            return wrapper;
        }
    };


    protected GaugeWrapper(MetricWrapperBuilder<Gauge> builder, RecordLevel... levels) {
        super(builder, levels);
    }
}
