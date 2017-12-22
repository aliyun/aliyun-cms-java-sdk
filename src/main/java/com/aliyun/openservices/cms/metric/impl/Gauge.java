package com.aliyun.openservices.cms.metric.impl;


import com.aliyun.openservices.cms.metric.MetricAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * A gauge metric is an instantaneous reading of a particular value. To instrument a queue's depth,
 * for example:<br>
 * <pre><code>
 * final Queue&lt;String&gt; queue = new ConcurrentLinkedQueue&lt;String&gt;();
 * final Gauge&lt;Integer&gt; queueDepth = new Gauge&lt;Integer&gt;() {
 *     public Integer getValue() {
 *         return queue.size();
 *     }
 * };
 * </code></pre>
 *
 * @param <T> the type of the metric's value
 */
public abstract class Gauge<T extends Number> implements Metric {
    /**
     * Returns the metric's current value.
     *
     * @return the metric's current value
     */
    abstract public T getValue();

    public Map<String, Number> resultMap() {
        Map<String, Number> ret = new HashMap<String, Number>();
        ret.put(MetricAttribute.LAST.getCode(), getValue());
        return ret;
    }
}
