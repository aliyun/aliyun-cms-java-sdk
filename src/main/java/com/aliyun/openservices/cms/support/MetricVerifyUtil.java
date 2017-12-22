package com.aliyun.openservices.cms.support;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by eli.lyj on 2017/9/30.
 */
public class MetricVerifyUtil {



    /**
     * 合法字符
     * 0-9
     * a-z
     * A-z
     * _-
     */
    static byte[] cc = new byte[128];

    static final int MAX_METRIC_NAME = 64;

    static final int MAX_TAG_KEY = 32;

    static final int MAX_TAG_VALUE = 64;


    static {
        init();
    }

    private static void init() {
        //添加0-9
        for(int i = '0'; i <= '9'; i++) {
            cc[i] = 1;
        }
        //添加a-z
        for(int i = 'a'; i <= 'z'; i++) {
            cc[i] = 1;
        }

        //添加A-Z
        for(int i = 'A'; i <= 'Z'; i++) {
            cc[i] = 1;
        }
        cc['_'] = 1;
        cc['-'] = 1;
    }

    public static void checkMetricName(String name) {
        check0(name, MAX_METRIC_NAME, "metric name");
    }

    public static void checkMetricTagKey(String tag) {
        check0(tag, MAX_TAG_KEY, "metric tag");
    }

    public static void checkMetricTagValue(String value) {
        check0(value, MAX_TAG_VALUE, "metric tag value");
    }

    private static void check0(String name, int maxLength, String errorType) {
        if(StringSupport.isNullOrEmpty(name, true)) {
            throw new IllegalArgumentException(String.format("%s must not null", errorType));
        }
        if(name.length() > maxLength) {
            throw new IllegalArgumentException(String.format("%s too long, max length is %s", errorType, maxLength));
        }
        for(int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if(c >= cc.length || cc[c] != 1) {
                throw new IllegalArgumentException(String.format("%s %s contain illegal symbol %s", errorType, name, c));
            }
        }
    }

}
