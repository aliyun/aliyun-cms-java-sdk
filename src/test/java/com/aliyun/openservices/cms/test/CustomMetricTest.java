package com.aliyun.openservices.cms.test;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.cms.CMSClient;
import com.aliyun.openservices.cms.CMSClientInit;
import com.aliyun.openservices.cms.builder.metric.registry.CMSMetricRegistryBuilder;
import com.aliyun.openservices.cms.builder.metric.registry.ConsoleMetricRegistryBuilder;
import com.aliyun.openservices.cms.exception.CMSException;
import com.aliyun.openservices.cms.metric.MetricAttribute;
import com.aliyun.openservices.cms.metric.Snapshot;
import com.aliyun.openservices.cms.metric.impl.*;
import com.aliyun.openservices.cms.metric.registry.MetricName;
import com.aliyun.openservices.cms.metric.registry.MetricRegistry;
import com.aliyun.openservices.cms.metric.registry.wrapper.*;
import com.aliyun.openservices.cms.metric.reservoir.ExponentiallyDecayingReservoir;
import com.aliyun.openservices.cms.model.CustomMetric;
import com.aliyun.openservices.cms.request.CustomMetricUploadRequest;
import com.aliyun.openservices.cms.response.CustomMetricUploadResponse;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by eli.lyj on 2017/10/30.
 */
@RunWith(JUnit4.class)
public class CustomMetricTest {
    String endpoint = "https://metrichub-cms-cn-hangzhou.aliyuncs.com/";

    private String accKey = "";
    private String secret = "";

//    String accKey = "";
//    String secret = "";





    MetricRegistry registry;

    private AsyncHttpClient client = new AsyncHttpClient();

    private CMSClient cmsClient;

    @Before
    public void init() {
        CMSClientInit.groupId = 1L;
        cmsClient = new CMSClient(endpoint, accKey, secret);
        CMSMetricRegistryBuilder builder = new CMSMetricRegistryBuilder();
        builder.setCmsClient(cmsClient);
        registry = builder.build();
    }

    @Test
    public void uploadOriginalValue() throws CMSException {
        CMSClientInit.groupId = 1L;
        final CMSClient cmsClient = new CMSClient(endpoint, accKey, secret);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while(true) {
                    CustomMetricUploadRequest request = CustomMetricUploadRequest.builder()
                            .append(CustomMetric.builder()
                                    .setMetricName("originalValue")
                                    .setTime(new Date())
                                    .setType(CustomMetric.TYPE_VALUE)
                                    .appendValue(MetricAttribute.VALUE, random.nextInt(100))
                                    .appendDimension("source", "java-sdk")
                                    .appendDimension("ip", "127.0.0.1")
                                    .build())
                            .build();
                    try {
                        CustomMetricUploadResponse response = cmsClient.putCustomMetric(request);
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        LockSupport.park();

    }

    @Test
    public void uploadPreAggValue() throws CMSException {
        CMSClient cmsClient = new CMSClient(endpoint, accKey, secret);
        CustomMetricUploadRequest request = CustomMetricUploadRequest.builder()
                .append(CustomMetric.builder()
                        .setMetricName("customTest")
                        .setTime(new Date())
                        .setType(CustomMetric.TYPE_AGG)
                        .setPeriod(CustomMetric.PERIOD_1M)
                        .appendDimension("source", "70")
                        .appendValue(MetricAttribute.COUNT, 20)
                        .build())
                .append(CustomMetric.builder()
                        .setMetricName("customTest")
                        .setTime(new Date())
                        .setType(CustomMetric.TYPE_AGG)
                        .setPeriod(CustomMetric.PERIOD_5M)
                        .appendDimension("test", "testValue")
                        .appendDimension("dimension", "dimensionValue")
                        .appendValue(MetricAttribute.SUM, 500)
                        .appendValue(MetricAttribute.MAX, 100)
                        .appendValue(MetricAttribute.MIN, 0.1)
                        .appendValue(MetricAttribute.COUNT, 90)
                        .appendValue(MetricAttribute.AVG, 5)
                        .appendValue(MetricAttribute.LAST, 10)
                        .appendValue(MetricAttribute.P50, 10)
                        .appendValue(MetricAttribute.P90, 17)
                        .appendValue(MetricAttribute.P99, 19).build())
                .build();
        CustomMetricUploadResponse response = cmsClient.putCustomMetric(request);
        System.out.println(JSONObject.toJSONString(response));
    }

    @Test
    public void testDoubleValue() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(int i = 1; i <= 100000; i++) {
                        registry.value(MetricName.build("mergeValueTest", "source", "java-sdk", "ip", "127.0.0.1"))
                                .update(i);
                        registry.value(MetricName.build("mergeValueTest", "source", "python-sdk", "ip", "192.168.0.1"))
                                .update(i);
                        registry.value(MetricName.build("mergeValueTest", "source", "c-sdk", "ip", "172.0.0.1"))
                                .update(i);
                        registry.value(MetricName.build("mergeValueTest", "source", "go-sdk", "ip", "localhost"))
                                .update(i);
                        registry.value(MetricName.build("mergeValueTest", "source", "java-sdk", "ip", "10.0.0.1"))
                                .update(i);
                        registry.value(MetricName.build("mergeValueTest", "source", "java-sdk", "ip", "255.255.255.0"))
                                .update(i);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        LockSupport.park();
    }

    @Test
    public void testMeter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(int i = 0; i < 1000; i++) {
                        registry.meter(MetricName.build("meter")).update(2);
                    }
                }
            }
        }).start();
        LockSupport.park();
    }

    @Test
    public void testCounter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(int i = 0; i < 100; i++) {
                        registry.counter(MetricName.build("counter")).inc(i);
                    }
                }
            }
        }).start();
        LockSupport.park();
    }



    @Test
    public void testHistogram() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(int i = 0; i < 100000; i++) {
                        registry.histogram(MetricName.build("histogram")).update(i);
                    }
                }
            }
        }).start();
        LockSupport.park();
    }

    @Test
    public void testTimer() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for(int i = 1; i <= 1000000; i++) {
                        registry.timer(MetricName.build("timer")).update(i, TimeUnit.MILLISECONDS);
                    }
                }
            }
        }).start();
        LockSupport.park();
    }

    @Test
    public void testGauge() {
        final Random random = new Random();
        registry.gauge(MetricName.build("gauge", "ip","127.0.0.1", "app", "testApp"), new Gauge() {
            @Override
            public Number getValue() {
                return random.nextInt(1000);
            }
        });
        LockSupport.park();
    }

}
