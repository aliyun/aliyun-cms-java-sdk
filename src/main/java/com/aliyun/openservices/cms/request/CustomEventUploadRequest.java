package com.aliyun.openservices.cms.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyun.openservices.cms.builder.CustomEventUploadRequestBuilder;
import com.aliyun.openservices.cms.model.impl.CustomEvent;
import com.aliyun.openservices.cms.response.CustomEventUploadResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eli.lyj on 2017/8/18.
 */
public class CustomEventUploadRequest extends CMSRequest<CustomEventUploadResponse>{
    private static final long serialVersionUID = 7183213188799830070L;
    private List<CustomEvent> events = new ArrayList<CustomEvent>();

    public CustomEventUploadRequest() {

    }

    public CustomEventUploadRequest(List<CustomEvent> events) {
        this.events = new ArrayList<CustomEvent>(events.size());
        this.events.addAll(events);
    }

    public void setEvents(List<CustomEvent> events) {
        this.events = events;
    }

    public List<CustomEvent> getEvents() {
        return events;
    }

    public void appendEvent(CustomEvent event) {
        events.add(event);
    }

    @Override
    public String uri() {
        return "/event/custom/upload";
    }

    @Override
    public byte[] body() {
        JSONArray.DEFFAULT_DATE_FORMAT = "yyyyMMdd'T'HHmmss.SSSZ";
        return JSONArray.toJSONBytes(events, SerializerFeature.WriteDateUseDateFormat);
    }

    public static CustomEventUploadRequestBuilder builder() {
        return CustomEventUploadRequestBuilder.create();
    }
}
