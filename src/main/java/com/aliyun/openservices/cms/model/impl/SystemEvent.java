package com.aliyun.openservices.cms.model.impl;

import com.aliyun.openservices.cms.builder.event.SystemEventBuilder;
import com.aliyun.openservices.cms.model.Event;

import java.util.Date;

/**
 * system upload model
 * Created by eli.lyj on 2017/11/27.
 */
public class SystemEvent extends Event<String> {
    private static final long serialVersionUID = 82240774811400410L;

    /**
     * 事件id
     */
    private String id;

    /**
     * eg:
     * ecs/rds ...
     */
    private String product;
    /**
     * aliUid
     */
    private String userId;
    /**
     * eg:
     * warn\info...
     */
    private String level;
    /**
     * eg:
     * ecsId: i-xxxxxxxxx
     * eipId: eip-xxxxxx
     */
    private String resourceId;
    /**
     * eg:
     * hostname
     * alias
     */
    private String instanceName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public static SystemEventBuilder builder() {
        return SystemEventBuilder.create();
    }
}
