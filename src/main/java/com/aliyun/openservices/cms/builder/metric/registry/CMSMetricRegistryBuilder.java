package com.aliyun.openservices.cms.builder.metric.registry;

import com.aliyun.openservices.cms.CMSClient;
import com.aliyun.openservices.cms.CMSClientInit;
import com.aliyun.openservices.cms.metric.registry.MetricRegistry;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.reporter.CMSReporter;
import com.aliyun.openservices.cms.metric.reporter.Reporter;
import com.aliyun.openservices.cms.support.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public class CMSMetricRegistryBuilder extends MetricRegistryBuilder{
    protected CMSClient cmsClient;

    public void setCmsClient(CMSClient cmsClient) {
        this.cmsClient = cmsClient;
    }

    public MetricRegistry build() {
        return build(MetricRegistry.getDEFAULT());
    }

    @Override
    public MetricRegistry build(RecordLevel... levels) {
        if(cmsClient == null) {
            throw new NullPointerException("cms client is null");
        }
        MetricRegistry registry = new MetricRegistry(levels);
        if(service == null) {
            service = Executors.newScheduledThreadPool(5, new NamedThreadFactory(name));
        }
        CMSReporter reporter = new CMSReporter(service, registry, cmsClient);
        reporter.setGroupId(groupId == null ? CMSClientInit.groupId : groupId);
        reporter.start();
        return registry;
    }
}
