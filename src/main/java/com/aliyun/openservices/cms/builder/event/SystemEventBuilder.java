package com.aliyun.openservices.cms.builder.event;

import com.aliyun.openservices.cms.CMSClientInit;
import com.aliyun.openservices.cms.model.impl.SystemEvent;
import com.aliyun.openservices.cms.support.PreCondition;
import com.aliyun.openservices.cms.support.StringSupport;

import java.util.Date;
import java.util.UUID;

/**
 * Created by eli.lyj on 2017/8/21.
 */
public class SystemEventBuilder extends AbstractEventBuilder<SystemEvent> {

    public static SystemEventBuilder create() {
        return new SystemEventBuilder();
    }

    @Override
    protected void verify() {
        PreCondition.alertWhenTrue(StringSupport.isNullOrEmpty(event.getProduct(), false)
                , "'product' must not be empty");
        PreCondition.alertWhenTrue(StringSupport.isNullOrEmpty(event.getLevel(), false)
                , "'level' must not be empty");
        PreCondition.alertWhenTrue(StringSupport.isNullOrEmpty(event.getStatus(), false)
                , "'status' must not be empty");
        PreCondition.alertWhenTrue(StringSupport.isNullOrEmpty(event.getResourceId(), false)
                , "'resourceId' must not be empty");

        PreCondition.alertWhenTrue(StringSupport.isNullOrEmpty(event.getName(), true)
                , "'name' must not be empty");
    }



    protected void setDefaultValue() {
        if(event.getTrace() == null) {
            event.setTrace(UUID.randomUUID().toString());
        }
        if(event.getTime() == null) {
            event.setTime(new Date());
        }
        if(event.getInstanceName() == null) {
            event.setInstanceName("");
        }
        if(event.getRegionId() == null) {
            event.setRegionId("N/A");
        }
    }


    private SystemEventBuilder() {
        event = new SystemEvent();
    }


    public SystemEventBuilder setName(String name) {
        event.setName(name);
        return this;
    }

    public SystemEventBuilder setRegionId(String region) {
        event.setRegionId(region);
        return this;
    }

    public SystemEventBuilder setResourceId(String resourceId) {
        event.setResourceId(resourceId);

        return this;
    }

    public SystemEventBuilder setInstanceName(String instanceName) {
        event.setInstanceName(instanceName);

        return this;
    }

    public SystemEventBuilder setTime(Date time) {
        event.setTime(time);
        return this;
    }

    public SystemEventBuilder setUserId(String userId) {
        event.setUserId(userId);
        return this;
    }

    public SystemEventBuilder setContent(String content) {
        event.setContent(content);
        return this;
    }

    public SystemEventBuilder setProduct(String product) {
        event.setProduct(product);

        return this;
    }

    public SystemEventBuilder setLevel(String level) {
        event.setLevel(level);
        return this;
    }

    public SystemEventBuilder setStatus(String status) {
        event.setStatus(status);
        return this;
    }
}
