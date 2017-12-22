package com.aliyun.openservices.cms.test;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.cms.CMSClient;
import com.aliyun.openservices.cms.builder.request.CustomEventUploadRequestBuilder;
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
    String endpoint = "http://pre-metrichub-cms.aliyuncs.com/";
//    String endpoint = "http://localhost:7001/";
//    private String accKey = "";
//    private String secret = "";

    String accKey = "";
    String secret = "";





    @Test
    public void upload() throws Exception{
        CMSClient cmsClient = new CMSClient(endpoint, accKey, secret);
        CustomEventUploadRequest request =
                CustomEventUploadRequest.builder()
                .append(CustomEvent.builder()
                .setGroupId(11L)
                .setName("testtest")
                .setContent("testste")
                .setRegionId("cn-hangzhou")
                .build()).build();
        cmsClient.putCustomEvent(request);
    }


}
