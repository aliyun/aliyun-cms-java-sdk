package com.aliyun.openservices.cms.http;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by eli.lyj on 2017/8/18.
 */
public class Response {
    private Map<String, String> headers;
    private int httpStatusCode;
    private String requestId;
    private String code;
    private String msg;
    private JSONObject data;

    public Response() {

    }

    public Response(int httpCode, JSONObject src) {
        requestId = src.getString("requestId");
        code = src.getString("code");
        msg = src.getString("msg");
        data = src.getJSONObject("data");
        this.httpStatusCode = httpCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
