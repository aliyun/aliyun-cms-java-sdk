package com.aliyun.openservices.cms.metric.registry;

import com.aliyun.openservices.cms.metric.impl.*;
import com.aliyun.openservices.cms.metric.registry.wrapper.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 使用这个注册的 使用cms上报模型
 * Created by eli.lyj on 2017/9/30.
 */
public class MetricRegistry {
    private final static RecordLevel[] DEFAULT =
            new RecordLevel[]{
                    RecordLevel._15S,
                    RecordLevel._60S,
                    RecordLevel._300S
            };


    private final RecordLevel[] levels;
    private final ConcurrentMap<MetricName, MetricWrapper> metrics;

    public MetricRegistry() {
        this(DEFAULT);
    }


    public MetricRegistry(RecordLevel... levels) {
        this.levels = levels;
        metrics = new ConcurrentHashMap<MetricName, MetricWrapper>();
    }

    public static RecordLevel[] getDEFAULT() {
        return DEFAULT;
    }

    public <T extends Metric, R extends MetricWrapper> R register(MetricName name, MetricWrapper<T> wrapper) throws IllegalArgumentException {
        final MetricWrapper<T> existing = metrics.putIfAbsent(name, wrapper);
        if (existing != null) {
            throw new IllegalArgumentException("A metric named " + name + " already exists");
        }
        return (R) wrapper;
    }

    private <T extends Metric, R extends MetricWrapper<T>> R getOrAdd(MetricName name, MetricWrapperBuilder<T> builder, RecordLevel... levels) {
        final R wrapper = (R) metrics.get(name);
        if(wrapper != null) {
            return wrapper;
        }
        try {
            return (R) register(name, builder.newWrapper(levels == null ? levels : this.levels));
        } catch (IllegalArgumentException e) {
            return (R) metrics.get(name);
        }
    }

    public ValueWrapper value(MetricName name) {
        return getOrAdd(name, ValueWrapper.DEFAULT);
    }

    public MeterWrapper meter(MetricName name) {
        return getOrAdd(name,MeterWrapper.BUILDER);
    }

    public TimerWrapper timer(MetricName name) {
        return getOrAdd(name, TimerWrapper.DEFAULT);
    }

    public HistogramWrapper histogram(MetricName name) {
        return getOrAdd(name, HistogramWrapper.DEFAULT);
    }

    public CounterWrapper counter(MetricName name) {
        return getOrAdd(name, CounterWrapper.DEFAULT);
    }

    public <T extends Metric, R extends MetricWrapper<T>> R register(MetricName name, MetricWrapperBuilder<T> builder) {
        return getOrAdd(name, builder);
    }

    public GaugeWrapper gauge(MetricName name, Gauge gauge) {
        MetricWrapperBuilder.LOCAL_RESERVOIR.set(gauge);
        GaugeWrapper ret = getOrAdd(name, GaugeWrapper.DEFAULT);
        MetricWrapperBuilder.LOCAL_RESERVOIR.remove();
        return ret;
    }

    public Map<MetricName, MetricWrapper> getMetrics() {
        Map<MetricName, MetricWrapper> map = new HashMap<MetricName, MetricWrapper>();
        map.putAll(metrics);
        return Collections.unmodifiableMap(map);
    }


}
