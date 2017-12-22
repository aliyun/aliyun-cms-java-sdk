package com.aliyun.openservices.cms.builder.request;

import com.aliyun.openservices.cms.request.CMSRequest;

/**
 * Created by eli.lyj on 2017/8/21.
 */
abstract class RequestBuilder<T extends CMSRequest> {

    protected T request;

    public T build() {
        return request;
    }
}
