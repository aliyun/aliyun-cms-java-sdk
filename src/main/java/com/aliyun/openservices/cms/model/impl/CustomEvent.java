package com.aliyun.openservices.cms.model.impl;

import com.aliyun.openservices.cms.builder.event.CustomEventBuilder;
import com.aliyun.openservices.cms.model.Event;

/**
 * Created by eli.lyj on 2017/8/18.
 */
public class CustomEvent extends Event<String> {

    private static final long serialVersionUID = -8673720805995877220L;


    protected Long groupId;//默认组名

    public CustomEvent() {

    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public static CustomEventBuilder builder() {
        return CustomEventBuilder.create();
    }

}
