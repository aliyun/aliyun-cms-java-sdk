package com.aliyun.openservices.cms.test;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.cms.CMSClient;
import com.aliyun.openservices.cms.builder.CustomEventBuilder;
import com.aliyun.openservices.cms.builder.CustomEventUploadRequestBuilder;
import com.aliyun.openservices.cms.exception.CMSException;
import com.aliyun.openservices.cms.model.Event;
import com.aliyun.openservices.cms.model.impl.CustomEvent;
import com.aliyun.openservices.cms.request.CustomEventUploadRequest;
import com.aliyun.openservices.cms.response.CustomEventUploadResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by eli.lyj on 2017/8/18.
 */
@RunWith(JUnit4.class)
public class CustomEventTest {
    String endpoint = "http://127.0.0.1:7001";
    private String accKey = "";
    private String secret = "";





    @Test
    public void uploadEvent() throws CMSException, InterruptedException {
        CMSClient cmsClient = new CMSClient(endpoint, accKey, secret);
        String cls = getClass().getName() + "\n" + Map.class.getName();
        Random random = new Random();
        List<String> content = new ArrayList<String>();
        content.add("123,abc");
        content.add("4567, efgh");
        content.add(exp2Str(new Exception("TestEventLoad")));
        for(;;) {

            Date date = new Date();

            CustomEventUploadRequestBuilder builder = CustomEventUploadRequest.builder();
            for(long i = 0; i < 100; i++) {
                CustomEvent event = CustomEvent.builder()
                        .setContent(content.get(random.nextInt(content.size())))
                        .setGroupId(100l + i)
                        .setName("Event_" + i)
                        .setTime(date).build();
                builder.append(event);


            }
            long t = System.currentTimeMillis();
            CustomEventUploadResponse response = cmsClient.putCustomEvent(builder.build());
            System.out.println((System.currentTimeMillis() - t));
            System.out.println(response.getCode());
            Thread.sleep(150);
        }
    }

    private String exp2Str(Throwable e) {
        StringWriter ret = new StringWriter();
        PrintWriter writer = new PrintWriter(ret);
        e.printStackTrace(writer);
        return ret.toString();
    }

}
