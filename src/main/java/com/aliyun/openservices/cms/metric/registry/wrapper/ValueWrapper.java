package com.aliyun.openservices.cms.metric.registry.wrapper;

import com.aliyun.openservices.cms.metric.Reservoir;
import com.aliyun.openservices.cms.metric.impl.DoubleValue;
import com.aliyun.openservices.cms.metric.registry.MetricWrapperBuilder;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public class ValueWrapper extends MetricWrapper<DoubleValue> {

    public static final MetricWrapperBuilder<DoubleValue> DEFAULT = new MetricWrapperBuilder<DoubleValue>() {
        @Override
        public DoubleValue newMetric(RecordLevel level) {
            Reservoir reservoir = (Reservoir) LOCAL_RESERVOIR.get();
            if(reservoir == null) {
                return new DoubleValue(level);
            }else {
                return new DoubleValue(level, reservoir);
            }
        }

        @Override
        public MetricWrapper<DoubleValue> newWrapper(RecordLevel... level) {
            ValueWrapper wrapper =  new ValueWrapper(this, level);
            return wrapper;
        }
    };

    public ValueWrapper(MetricWrapperBuilder<DoubleValue> builder, RecordLevel... levels) {
        super(builder, levels);
    }

    public void update(double value) {
        for(DoubleValue doubleValue : metricList) {
            if(doubleValue != null) {
                doubleValue.update(value);
            }
        }

    }
}
