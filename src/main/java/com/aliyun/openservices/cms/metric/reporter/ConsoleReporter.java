package com.aliyun.openservices.cms.metric.reporter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyun.openservices.cms.metric.impl.*;
import com.aliyun.openservices.cms.metric.registry.MetricName;
import com.aliyun.openservices.cms.metric.registry.MetricRegistry;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.registry.wrapper.MetricWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by eli.lyj on 2017/11/1.
 */
public class ConsoleReporter extends ScheduledReporter{

    public ConsoleReporter(ScheduledExecutorService service, MetricRegistry registry) {
        super(service, registry);
    }

    @Override
    protected void reportCustomMetric(MetricName name, MetricWrapper wrapper, RecordLevel level, Date date) {
        Metric metric = wrapper.getMetricByRecordLevel(level);
        if(metric == null) {
            return;
        }
        System.out.println(JSONObject.toJSONString(covert(name, metric, date, level), SerializerFeature.PrettyFormat));
    }

    protected Map<String, Object> covert(MetricName name, Metric metric, Date date, RecordLevel level) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(metric.resultMap());
        map.put("metric", name.getName());
        map.put("metricType", getMetricType(metric));
        map.put("tags", name.getDimensions());
        map.put("timestamp", date.getTime());
        map.put("period", level.getInterval());
        map.put("groupId", groupId == null ? "0":groupId);
        return map;
    }

    private String getMetricType(Metric metric) {
        if(metric instanceof Gauge) {
            return "GAUGE";
        }
        if(metric instanceof Counter) {
            return "COUNTER";
        }
        if(metric instanceof DoubleMeter || metric instanceof Meter) {
            return "METER";
        }
        if(metric instanceof Histogram) {
            return "HISTOGRAM";
        }
        if(metric instanceof Timer) {
            return "TIMER";
        }
        return "VALUE";
    }
}
