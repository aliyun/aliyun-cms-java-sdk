package com.aliyun.openservices.cms.metric.registry.wrapper;

import com.aliyun.openservices.cms.metric.Reservoir;
import com.aliyun.openservices.cms.metric.impl.Timer;
import com.aliyun.openservices.cms.metric.registry.MetricWrapperBuilder;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;

import java.util.concurrent.TimeUnit;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public class TimerWrapper extends MetricWrapper<Timer> {
    public static final MetricWrapperBuilder<Timer> DEFAULT = new MetricWrapperBuilder<Timer>() {
        @Override
        public Timer newMetric(RecordLevel level) {
            Reservoir reservoir = (Reservoir) LOCAL_RESERVOIR.get();
            if(reservoir == null) {
                return new Timer(level);
            }
            return new Timer(level.getInterval(), TimeUnit.SECONDS, reservoir);
        }

        @Override
        public MetricWrapper<Timer> newWrapper(RecordLevel... level) {
            TimerWrapper ret = new TimerWrapper(this, level);
            return ret;
        }
    };
    public TimerWrapper(MetricWrapperBuilder<Timer> builder, RecordLevel... levels) {
        super(builder, levels);
    }


    public void update(long duration, TimeUnit unit) {
        for(Timer timer : metricList) {
            if(timer != null) {
                timer.update(duration, unit);
            }
        }
    }

}
