package com.aliyun.openservices.cms.metric;

/**
 * Represents attributes of metrics which can be reported.
 */
public enum MetricAttribute {
    VALUE("value"),
    AVG("Average"),
    MAX("Maximum"),
    MIN("Minimum"),
    SUM("Sum"),
    LAST("LastValue"),
    COUNT("SampleCount"),
    SUM_PS("SumPerSecond"),
    COUNT_PS("CountPerSecond"),
    P10("P10"),
    P20("P20"),
    P30("P30"),
    P40("P40"),
    P50("P50"),
    P60("P60"),
    P70("P70"),
    P75("P75"),
    P80("P80"),
    P90("P90"),
    P95("P95"),
    P98("P98"),
    P99("P99");

    private final String code;

    MetricAttribute(String code) {
        this.code = code;
    }

    public static MetricAttribute findByCode(String code) {
        for(MetricAttribute m : values()) {
            if(m.getCode().equals(code)) {
                return m;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
}
