package com.aliyun.openservices.cms.builder.metric.registry;

import com.aliyun.openservices.cms.CMSClientInit;
import com.aliyun.openservices.cms.metric.registry.MetricRegistry;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.reporter.CMSReporter;
import com.aliyun.openservices.cms.metric.reporter.ConsoleReporter;
import com.aliyun.openservices.cms.support.NamedThreadFactory;

import java.util.concurrent.Executors;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public class ConsoleMetricRegistryBuilder extends MetricRegistryBuilder{
    @Override
    public MetricRegistry build() {
        return build(MetricRegistry.getDEFAULT());
    }

    @Override
    public MetricRegistry build(RecordLevel... levels) {
        MetricRegistry registry = new MetricRegistry(levels);
        if(service == null) {
            service = Executors.newScheduledThreadPool(CMSClientInit.metricScheduleUploadThread, new NamedThreadFactory(name));
        }
        ConsoleReporter reporter = new ConsoleReporter(service, registry);
        reporter.setGroupId(groupId == null ? CMSClientInit.groupId : groupId);
        reporter.start();
        return registry;
    }
}
