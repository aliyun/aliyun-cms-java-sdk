package com.aliyun.openservices.cms.builder.event;

import com.aliyun.openservices.cms.CMSClientInit;
import com.aliyun.openservices.cms.model.Event;
import com.aliyun.openservices.cms.support.PreCondition;
import com.aliyun.openservices.cms.support.StringSupport;

import java.util.Date;
import java.util.UUID;

/**
 * Created by eli.lyj on 2017/8/21.
 */
public abstract class AbstractEventBuilder<T extends Event> {
    protected T event;

    protected void verify() {
        PreCondition.alertWhenTrue(StringSupport.isNullOrEmpty(event.getName(), true), "'name' must not be empty");

    }



    protected void setDefaultValue() {
        if(event.getRegionId() == null) {
            event.setRegionId("N/A");
        }
        if(event.getTrace() == null) {
            event.setTrace(UUID.randomUUID().toString());
        }
        if(event.getTime() == null) {
            event.setTime(new Date());
        }
        if(event.getStatus() == null) {
            event.setStatus("N/A");
        }
    }

    public T build() {
        verify();
        setDefaultValue();
        return event;
    }
}
