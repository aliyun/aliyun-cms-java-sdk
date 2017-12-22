package com.aliyun.openservices.cms.metric.impl;

import java.util.Map;

/**
 * A tag interface to indicate that a class is a metric.
 */
public interface Metric {
    Map<String, Number> resultMap();
}
