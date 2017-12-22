package com.aliyun.openservices.cms.builder.request;

import com.aliyun.openservices.cms.model.impl.SystemEvent;
import com.aliyun.openservices.cms.request.SystemEventUploadRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eli.lyj on 2017/8/21.
 */
public class SystemEventUploadRequestBuilder extends RequestBuilder<SystemEventUploadRequest>{


    public static SystemEventUploadRequestBuilder create() {
        SystemEventUploadRequestBuilder builder = new SystemEventUploadRequestBuilder();
        builder.request = new SystemEventUploadRequest();
        return builder;
    }

    public SystemEventUploadRequestBuilder append(SystemEvent event) {
        request.appendEvent(event);
        return this;
    }

    public SystemEventUploadRequestBuilder setEventList(List<SystemEvent> list) {
        request.setEvents(new ArrayList<SystemEvent>(list));
        return this;
    }

}
