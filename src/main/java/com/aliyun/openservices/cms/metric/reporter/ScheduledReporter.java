package com.aliyun.openservices.cms.metric.reporter;

import com.aliyun.openservices.cms.metric.registry.MetricName;
import com.aliyun.openservices.cms.metric.registry.MetricRegistry;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.registry.wrapper.MetricWrapper;

import java.util.*;
import java.util.concurrent.*;


public abstract class ScheduledReporter implements Reporter{
    protected final ScheduledExecutorService service;
    protected final MetricRegistry registry;
    protected Long groupId;

    protected ScheduledReporter(ScheduledExecutorService service, MetricRegistry registry) {
        this.service = service;
        this.registry = registry;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void start() {
        for(final RecordLevel level : RecordLevel.values()) {
            service.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    final Date date = new Date();
                    Map<MetricName, MetricWrapper> metricMap = registry.getMetrics();
                    for(Map.Entry<MetricName, MetricWrapper> entry : metricMap.entrySet()) {
                        reportCustomMetric(entry.getKey(), entry.getValue(), level, date);
                    }
                }
            }, level.getInterval(), level.getInterval(), TimeUnit.SECONDS);
        }
    }

    protected abstract void reportCustomMetric(MetricName name, MetricWrapper wrapper, RecordLevel level, Date date);

}
