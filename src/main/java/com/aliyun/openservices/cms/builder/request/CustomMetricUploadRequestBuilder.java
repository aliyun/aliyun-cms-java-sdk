package com.aliyun.openservices.cms.builder.request;

import com.aliyun.openservices.cms.model.CustomMetric;
import com.aliyun.openservices.cms.model.impl.CustomEvent;
import com.aliyun.openservices.cms.request.CustomEventUploadRequest;
import com.aliyun.openservices.cms.request.CustomMetricUploadRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eli.lyj on 2017/8/21.
 */
public class CustomMetricUploadRequestBuilder extends RequestBuilder<CustomMetricUploadRequest>{


    private CustomMetricUploadRequestBuilder() {

    }

    public static CustomMetricUploadRequestBuilder create() {
        CustomMetricUploadRequestBuilder builder = new CustomMetricUploadRequestBuilder();
        builder.request = new CustomMetricUploadRequest();
        return builder;
    }

    public CustomMetricUploadRequestBuilder append(CustomMetric metric) {
        request.append(metric);
        return this;
    }

    public CustomMetricUploadRequestBuilder setEventList(List<CustomMetric> list) {
        request.setList(new ArrayList<CustomMetric>(list));
        return this;
    }

}
