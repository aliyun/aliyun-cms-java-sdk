package com.aliyun.openservices.cms.metric.registry.wrapper;

import com.aliyun.openservices.cms.metric.impl.Metric;
import com.aliyun.openservices.cms.metric.registry.MetricWrapperBuilder;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eli.lyj on 2017/10/31.
 */
public class MetricWrapper<T extends Metric> {
    protected final List<T> metricList;

    public MetricWrapper(MetricWrapperBuilder<T> builder, RecordLevel... levels) {
        metricList = new ArrayList<T>(RecordLevel.values().length);
        for(int i = 0; i < RecordLevel.values().length; i++) {
            metricList.add(null);
        }
        for(RecordLevel level : levels) {
            metricList.set(level.ordinal(), builder.newMetric(level));
        }
    }


    protected List<T> getMetricList() {
        return metricList;
    }

    public T getMetricByRecordLevel(RecordLevel level) {
        return metricList.get(level.ordinal());
    }

    public void appendLevel(MetricWrapperBuilder<T> builder, RecordLevel... levels) {
        for(RecordLevel level : levels) {
            if(metricList.get(level.ordinal()) == null) {
                synchronized (this) {
                    if(metricList.get(level.ordinal()) != null) {
                        metricList.set(level.ordinal(), builder.newMetric(level));
                    }
                }
            }
        }
    }
}
