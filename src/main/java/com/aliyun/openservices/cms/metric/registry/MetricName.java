package com.aliyun.openservices.cms.metric.registry;

import com.aliyun.openservices.cms.metric.impl.Metric;

import java.util.*;

/**
 * A metric name with the ability to include semantic tags.
 *
 * This replaces the previous style where metric names where strictly
 * dot-separated strings.
 *
 * @author udoprog
 */
public class MetricName implements Comparable<MetricName> {
    public static final Map<String, String> EMPTY_DIMENSIONS = Collections.emptyMap();

    private final String name;
    private final Map<String, String> dimensions;

    private int hashCode = 0;

    private boolean hashCodeCached = false;


    public static MetricName build(String name) {
        return new MetricName(name);
    }

    public static MetricName build(String name, String... dimensions) {
        Map<String, String> map = new HashMap<String, String>();
        for(int i = 0; i < dimensions.length;) {
            map.put(dimensions[i], dimensions[i + 1]);
            i+=2;
        }
        return new MetricName(name, map);
    }

    public static MetricName build(String name, Map<String, String> dimensions) {
        return new MetricName(name, dimensions);
    }

    public MetricName(String name) {
        this(name, null);
    }

    private MetricName(String name, Map<String, String> dimensions) {
        this.name = name;
        this.dimensions = checkDimensions(dimensions);
    }

    private Map<String, String> checkDimensions(Map<String, String> dimensions) {
        if (dimensions == null || dimensions.isEmpty()) {
            return EMPTY_DIMENSIONS;
        }
        return Collections.unmodifiableMap(dimensions);
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getDimensions() {
        return dimensions;
    }

    @Override
    public String toString() {
        if (dimensions.isEmpty()) {
            return name;
        }

        return name + dimensions;
    }

    @Override
    public int hashCode() {

        if (!hashCodeCached){

            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((dimensions == null) ? 0 : dimensions.hashCode());

            hashCode = result;
            hashCodeCached = true;
        }

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        MetricName other = (MetricName) obj;

        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;

        if (!dimensions.equals(other.dimensions))
            return false;

        return true;
    }

    @Override
    public int compareTo(MetricName o) {
        if (o == null)
            return -1;

        int c = compareName(name, o.getName());

        if (c != 0)
            return c;

        return compareTags(dimensions, o.getDimensions());
    }

    private int compareName(String left, String right) {
        if (left == null && right == null)
            return 0;

        if (left == null)
            return 1;

        if (right == null)
            return -1;

        return left.compareTo(right);
    }

    private int compareTags(Map<String, String> left, Map<String, String> right) {
        if (left == null && right == null)
            return 0;

        if (left == null)
            return 1;

        if (right == null)
            return -1;

        final Iterable<String> keys = uniqueSortedKeys(left, right);

        for (final String key : keys) {
            final String a = left.get(key);
            final String b = right.get(key);

            if (a == null && b == null)
                continue;

            if (a == null)
                return -1;

            if (b == null)
                return 1;

            int c = a.compareTo(b);

            if (c != 0)
                return c;
        }

        return 0;
    }

    private Iterable<String> uniqueSortedKeys(Map<String, String> left, Map<String, String> right) {
        final Set<String> set = new TreeSet<String>(left.keySet());
        set.addAll(right.keySet());
        return set;
    }
}
