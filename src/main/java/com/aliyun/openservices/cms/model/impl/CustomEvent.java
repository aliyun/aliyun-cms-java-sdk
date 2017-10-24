package com.aliyun.openservices.cms.model.impl;

import com.aliyun.openservices.cms.builder.CustomEventBuilder;
import com.aliyun.openservices.cms.model.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eli.lyj on 2017/8/18.
 */
public class CustomEvent extends Event<String> {

    public CustomEvent() {

    }


    public static CustomEventBuilder builder() {
        return CustomEventBuilder.create();
    }

}
