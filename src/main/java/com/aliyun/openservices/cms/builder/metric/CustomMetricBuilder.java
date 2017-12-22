package com.aliyun.openservices.cms.builder.metric;

import com.aliyun.openservices.cms.CMSClientInit;
import com.aliyun.openservices.cms.metric.MetricAttribute;
import com.aliyun.openservices.cms.model.CustomMetric;
import com.aliyun.openservices.cms.support.PreCondition;
import com.aliyun.openservices.cms.support.StringSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eli.lyj on 2017/10/31.
 */
public class CustomMetricBuilder {
    private CustomMetric metric;

    public CustomMetricBuilder() {
        metric = new CustomMetric();
    }

    public CustomMetricBuilder setDimensions(Map<String, String> dimensions) {
        metric.setDimensions(dimensions);
        return this;
    }

    public CustomMetricBuilder setValues(Map<String, Number> values) {
        metric.setValues(values);
        return this;
    }


    public CustomMetricBuilder appendDimension(String key, String value) {
        Map<String, String> dimensions = metric.getDimensions();
        if(dimensions == null) {
            dimensions = new HashMap<String, String>();
            metric.setDimensions(dimensions);
        }
        dimensions.put(key, value);
        return this;
    }

    public CustomMetricBuilder appendValue(MetricAttribute valueKey, Number value) {
        Map<String, Number> values = metric.getValues();
        if(values == null) {
            values = new HashMap<String, Number>();
            metric.setValues(values);
        }
        values.put(valueKey.getCode(), value);
        return this;
    }

    public CustomMetricBuilder setMetricName(String metricName) {
        metric.setMetricName(metricName);
        return this;
    }

    public CustomMetricBuilder setPeriod(Integer period) {
        metric.setPeriod(period);
        return this;
    }

    public CustomMetricBuilder setGroupId(Long groupId) {
        metric.setGroupId(groupId);
        return this;
    }

    public CustomMetricBuilder setType(Integer type) {
        metric.setType(type);
        return this;
    }

    public CustomMetricBuilder setTime(Date time) {
        metric.setTime(time);
        return this;
    }

    public CustomMetric build() {
        setDefaultValue();
        verify();
        return metric;
    }

    private void verify() {
        PreCondition.alertWhenTrue(StringSupport.isNullOrEmpty(metric.getMetricName(), true), "'name' must not be empty");
        PreCondition.alertWhenTrue(metric.getGroupId() == null && CMSClientInit.groupId == null, "'groupId' must not be empty");
        checkType();
        checkPeriod();
        checkDimension();
        checkValue();
    }

    private void setDefaultValue(){
        if(metric.getGroupId() == null) {
            metric.setGroupId(CMSClientInit.groupId);
        }
        if(metric.getTime() == null) {
            metric.setTime(new Date());
        }
    }

    private void checkType() {
        if(metric.getType() == null || metric.getType() > 1 || metric.getType() < 0) {
            throw new IllegalArgumentException("type must set 0 or 1");
        }
    }

    private void checkDimension() {
        if(metric.getDimensions() != null && metric.getDimensions().size() > 10) {
            throw new IllegalArgumentException("dimensions max size is 10");
        }
    }

    private void checkPeriod() {
        if(metric.getType() == CustomMetric.TYPE_AGG) {
            if(metric.getPeriod() == null ||
                    (metric.getPeriod() != CustomMetric.PERIOD_15S
                            && metric.getPeriod() != CustomMetric.PERIOD_1M
                            && metric.getPeriod() != CustomMetric.PERIOD_5M)) {
                throw new IllegalArgumentException("period must set 15 60 300");
            }
        }
    }

    private void checkValue() {
        if(metric.getValues() == null) {
            throw new IllegalArgumentException("values must be set value");
        }
        if(metric.getType() == CustomMetric.TYPE_VALUE) {
            if(metric.getValues().size() != 1
                    || !metric.getValues().containsKey(MetricAttribute.VALUE.getCode())) {
                throw new IllegalArgumentException("when type = 0, values key can only have 'value'");
            }
        }else {
            for(Map.Entry<String, Number> entry : metric.getValues().entrySet()) {
                MetricAttribute m = MetricAttribute.findByCode(entry.getKey());
                if(m == null || m == MetricAttribute.VALUE) {
                    throw new IllegalArgumentException("when type = 1, values key is invalid:" + entry.getKey());
                }
                if(entry.getValue() == null) {
                    throw new IllegalArgumentException(String.format("values key:%s, value is null", entry.getKey()));
                }
            }
        }

    }
}
