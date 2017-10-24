package com.aliyun.openservices.cms.builder;

import com.aliyun.openservices.cms.model.impl.CustomEvent;

import java.util.Date;

/**
 * Created by eli.lyj on 2017/8/21.
 */
public class CustomEventBuilder extends AbstractEventBuilder<CustomEvent> {

    public static CustomEventBuilder create() {
        return new CustomEventBuilder();
    }

    private CustomEventBuilder() {
        event = new CustomEvent();
    }


    public CustomEventBuilder setName(String name) {
        event.setName(name);
        return this;
    }

    public CustomEventBuilder setRegionId(String region) {
        event.setRegionId(region);
        return this;
    }

    public CustomEventBuilder setTime(Date time) {
        event.setTime(time);
        return this;
    }


    public CustomEventBuilder setContent(String content) {
        event.setContent(content);
        return this;
    }

    public CustomEventBuilder setGroupId(Long groupId) {
        event.setGroupId(groupId);
        return this;
    }



}
