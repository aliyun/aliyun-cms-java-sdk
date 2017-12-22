package com.aliyun.openservices.cms.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyun.openservices.cms.model.impl.SystemEvent;
import com.aliyun.openservices.cms.response.SystemEventUploadResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eli.lyj on 2017/11/27.
 */
public class SystemEventUploadRequest extends CMSRequest<SystemEventUploadResponse>{

    private static final long serialVersionUID = -934527333035958301L;

    private List<SystemEvent> events = new ArrayList<SystemEvent>();

    public SystemEventUploadRequest() {

    }

    public SystemEventUploadRequest(List<SystemEvent> events) {
        this.events = new ArrayList<SystemEvent>(events.size());
        this.events.addAll(events);
    }

    public void setEvents(List<SystemEvent> events) {
        this.events = events;
    }

    public List<SystemEvent> getEvents() {
        return events;
    }

    public void appendEvent(SystemEvent event) {
        events.add(event);
    }


    @Override
    public String uri() {
        return "/event/system/upload";
    }

    @Override
    public byte[] body() {
        JSONArray.DEFFAULT_DATE_FORMAT = "yyyyMMdd'T'HHmmss.SSSZ";
        return JSONArray.toJSONBytes(events, SerializerFeature.WriteDateUseDateFormat);
    }
}
