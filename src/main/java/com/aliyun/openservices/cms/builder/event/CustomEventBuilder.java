package com.aliyun.openservices.cms.builder.event;

import com.aliyun.openservices.cms.CMSClientInit;
import com.aliyun.openservices.cms.model.impl.CustomEvent;
import com.aliyun.openservices.cms.support.PreCondition;
import com.aliyun.openservices.cms.support.StringSupport;

import java.util.Date;
import java.util.UUID;

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

    protected void verify() {
        super.verify();
        PreCondition.alertWhenTrue(event.getGroupId() == null && CMSClientInit.groupId == null, "'groupId' must not be empty ");
    }



    protected void setDefaultValue() {
        super.setDefaultValue();

        if(event.getGroupId() == null) {
            event.setGroupId(CMSClientInit.groupId);
        }
    }



}
