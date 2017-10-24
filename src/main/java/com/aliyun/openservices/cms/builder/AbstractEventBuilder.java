package com.aliyun.openservices.cms.builder;

import com.aliyun.openservices.cms.CMSClientInit;
import com.aliyun.openservices.cms.common.annotation.Nullable;
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
        PreCondition.checkIsTrue(StringSupport.isNullOrEmpty(event.getName(), true), "'name' must not be empty");
        PreCondition.checkIsTrue(event.getGroupId() == null && CMSClientInit.groupId == null, "'groupId' must not be empty ");
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

        if(event.getGroupId() == null) {
            event.setGroupId(CMSClientInit.groupId);
        }
    }

    public T build() {
        verify();
        setDefaultValue();
        return event;
    }
}
