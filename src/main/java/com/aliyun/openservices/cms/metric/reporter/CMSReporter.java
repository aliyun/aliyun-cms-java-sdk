package com.aliyun.openservices.cms.metric.reporter;

import com.aliyun.openservices.cms.CMSClient;
import com.aliyun.openservices.cms.CMSClientInit;
import com.aliyun.openservices.cms.builder.metric.CustomMetricBuilder;
import com.aliyun.openservices.cms.exception.CMSException;
import com.aliyun.openservices.cms.metric.impl.Metric;
import com.aliyun.openservices.cms.metric.registry.MetricName;
import com.aliyun.openservices.cms.metric.registry.MetricRegistry;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.registry.wrapper.MetricWrapper;
import com.aliyun.openservices.cms.model.CustomMetric;
import com.aliyun.openservices.cms.request.CustomMetricUploadRequest;
import com.aliyun.openservices.cms.response.CustomMetricUploadResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by eli.lyj on 2017/10/31.
 */
public class CMSReporter extends ScheduledReporter{

    private final CMSClient cmsClient;

    private int batchNum = 100;
    private int sendTimeDis = 1;
    private Long groupId;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    private LinkedBlockingQueue<CustomMetric> buff = new LinkedBlockingQueue<CustomMetric>();

    public CMSReporter(ScheduledExecutorService service, MetricRegistry registry, CMSClient cmsClient) {
        super(service, registry);
        this.cmsClient = cmsClient;
    }

    public void start() {
        super.start();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(buff.isEmpty()) {
                    return;
                }
                List<CustomMetric> metrics = new ArrayList<CustomMetric>();
                buff.drainTo(metrics, batchNum);
                sendCustomMetric(metrics);
            }
        }, 100, 500, TimeUnit.MILLISECONDS);
    }

    private void sendCustomMetric(List<CustomMetric> metrics) {
        CustomMetricUploadRequest request = new CustomMetricUploadRequest();
        request.setList(metrics);
        Exception ex = null;
        boolean flag = false;
        for(int i = 0; i < 3; i++) {
            try {
                CustomMetricUploadResponse response = cmsClient.putCustomMetric(request);
                flag = true;
                if(response.getCode().equals("200")) {//发送成功
                    return;
                }
                if(response.getMessage().contains("rate limit")) {//被限速 重试
                    continue;
                }
            } catch (Exception e) {
                ex = e;
            }
        }
        if(!flag && ex != null) {
            ex.printStackTrace();
        }
    }


    protected CustomMetric covert(MetricName name, MetricWrapper wrapper, RecordLevel level, Date date) {
        Metric metric = wrapper.getMetricByRecordLevel(level);
        if(metric == null) {
            return null;
        }
        Map<String, Number> values =  metric.resultMap();

        CustomMetric customMetric = CustomMetric.builder()
        .setMetricName(name.getName())
        .setDimensions(name.getDimensions())
        .setPeriod(level.getInterval())
        .setTime(date)
        .setType(CustomMetric.TYPE_AGG)
        .setValues(values)
        .setGroupId(groupId == null ? 0L : groupId).build();
        return customMetric;
    }

    @Override
    protected void reportCustomMetric(MetricName name, MetricWrapper wrapper, RecordLevel level, Date date) {
        CustomMetric metric = covert(name, wrapper, level, date);
        if(metric == null) {
            return;
        }
        buff.add(metric);
    }
}
