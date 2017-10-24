package com.aliyun.openservices.cms.request;

import com.aliyun.openservices.cms.response.CMSResponse;
import com.aliyun.openservices.cms.exception.CMSException;
import com.aliyun.openservices.cms.http.HttpMethod;
import com.aliyun.openservices.cms.http.Response;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * Created by eli.lyj on 2017/8/18.
 */
public abstract class CMSRequest<T extends CMSResponse> implements Serializable{

    private static final long serialVersionUID = 725096851508886375L;

    public HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    public Map<String, String> queryParam() {
        return Collections.EMPTY_MAP;
    }

    public T encode(Response response, Class<T> clazz) throws CMSException {
        if(response.getHttpStatusCode() == 200
                || !"200".equals(response.getCode())) {
            return response.getData().toJavaObject(clazz);
        }else {
            throw new CMSException(response.getRequestId(), response.getCode(), response.getMsg());
        }
    }
    public abstract String uri();
    public abstract byte[] body();
}
