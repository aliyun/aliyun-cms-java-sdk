package com.aliyun.openservices.cms.metric.registry;

/**
 * 指标记录级别
 * 影响指标的同步速率
 */
public enum RecordLevel {

    _300S(300),

    _60S(60),

    _15S(15); // 每秒上报一次

    private final int interval;

    public int getInterval() {
        return interval;
    }

    static {
        for (RecordLevel level : RecordLevel.values()) {
            if (level.ordinal() < 0) {
                throw new RuntimeException("RecordLevel can not < 0");
            }
        }
    }

    RecordLevel(int interval) {
        this.interval = interval;
    }
}
