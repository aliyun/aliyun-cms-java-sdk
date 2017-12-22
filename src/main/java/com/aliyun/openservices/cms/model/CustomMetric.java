package com.aliyun.openservices.cms.model;

import com.aliyun.openservices.cms.builder.metric.CustomMetricBuilder;

import java.util.Date;
import java.util.Map;

/**
 * Created by eli.lyj on 2017/9/30.
 */
public class CustomMetric {
    /**
     * field type value set
     */
    public static final int TYPE_VALUE = 0;
    public static final int TYPE_AGG = 1;

    /**
     * field period value set
     */
    public static final int PERIOD_15S = 15;
    public static final int PERIOD_1M = 60;
    public static final int PERIOD_5M = 300;


    /**
     * app groupId
     * you can find it on aliyun monitor console
     */
    private Long groupId;

    /**
     * key value pair
     * which defined one time series
     * the key must [_0-9a-zA-Z]
     */
    private Map<String, String> dimensions;
    /**
     * metric name
     * the value must be [_0-9a-zA-Z]
     */
    private String metricName;
    /**
     * the time of the value set
     */
    private Date time;
    /**
     * 0 original value
     * 1 pre agg value
     */
    private Integer type;
    /**
     * only when type = 1
     * only set the values in
     * 15
     * 60
     * 300
     * 900
     * 1800
     */
    private Integer period;
    /**
     * if type is 0
     * key: 'value'
     * value: original value
     *
     * if type is 1
     * key set is
     * Average,Minimum,Maximum,Sum,SampleCount
     * LastValue: the last of the original value
     * SumPerSecond: Sum/period or the ewma(or other algorithm) value of the original value
     * CountPerSecond: SampleCount/period or the ewma(or other algorithm) value of the original value
     *
     * PXX value: xx% is less then the PXX
     * P10: 10% of the  original value is less then the P10
     * P20
     * P30
     * P40
     * P50
     * P60
     * P70
     * P75
     * P80
     * P90
     * P95
     * P98
     * P99
     * the value is the pre agg value in the period
     */
    private Map<String, Number> values;

    public Map<String, String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(Map<String, String> dimensions) {
        this.dimensions = dimensions;
    }

    public Map<String, Number> getValues() {
        return values;
    }

    public void setValues(Map<String, Number> values) {
        this.values = values;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    public static CustomMetricBuilder builder() {
        return new CustomMetricBuilder();
    }

}
