package com.aliyun.openservices.cms.builder.metric.registry;

import com.aliyun.openservices.cms.CMSClient;
import com.aliyun.openservices.cms.metric.registry.MetricRegistry;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.reporter.CMSReporter;
import com.aliyun.openservices.cms.metric.reporter.Reporter;
import com.aliyun.openservices.cms.support.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public abstract class MetricRegistryBuilder {

    protected String name = "cms-client";

    protected Long groupId;

    protected ScheduledExecutorService service;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScheduled(ScheduledExecutorService scheduled) {
        this.service = scheduled;
    }

    public abstract MetricRegistry build();

    public abstract MetricRegistry build(RecordLevel... levels);


}
