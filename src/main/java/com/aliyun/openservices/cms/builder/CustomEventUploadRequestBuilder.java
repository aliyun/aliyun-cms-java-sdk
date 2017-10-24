package com.aliyun.openservices.cms.builder;

import com.aliyun.openservices.cms.model.impl.CustomEvent;
import com.aliyun.openservices.cms.request.CustomEventUploadRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eli.lyj on 2017/8/21.
 */
public class CustomEventUploadRequestBuilder extends RequestBuilder<CustomEventUploadRequest>{


    public static CustomEventUploadRequestBuilder create() {
        CustomEventUploadRequestBuilder builder = new CustomEventUploadRequestBuilder();
        builder.request = new CustomEventUploadRequest();
        return builder;
    }

    public CustomEventUploadRequestBuilder append(CustomEvent event) {
        request.appendEvent(event);
        return this;
    }

    public CustomEventUploadRequestBuilder setEventList(List<CustomEvent> list) {
        request.setEvents(new ArrayList<CustomEvent>(list));
        return this;
    }

}
