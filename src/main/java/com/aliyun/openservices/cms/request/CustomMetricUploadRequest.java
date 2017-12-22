package com.aliyun.openservices.cms.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyun.openservices.cms.builder.request.CustomMetricUploadRequestBuilder;
import com.aliyun.openservices.cms.model.CustomMetric;
import com.aliyun.openservices.cms.response.CustomEventUploadResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eli.lyj on 2017/9/29.
 */
public class CustomMetricUploadRequest extends CMSRequest<CustomEventUploadResponse>{
    private static final long serialVersionUID = -1328626694523973935L;
    private List<CustomMetric> list;

    public void setList(List<CustomMetric> list) {
        this.list = list;
    }

    @Override
    public String uri() {
        return "/metric/custom/upload";
    }

    public void append(CustomMetric metric) {
        if(list == null) {
            list = new ArrayList<CustomMetric>();
        }
        list.add(metric);
    }

    @Override
    public byte[] body() {
        JSONArray.DEFFAULT_DATE_FORMAT = "yyyyMMdd'T'HHmmss.SSSZ";
        return JSONArray.toJSONBytes(list, SerializerFeature.WriteDateUseDateFormat);
    }

    public static CustomMetricUploadRequestBuilder builder() {
        return CustomMetricUploadRequestBuilder.create();
    }
}
