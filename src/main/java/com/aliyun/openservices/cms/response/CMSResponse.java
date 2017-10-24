package com.aliyun.openservices.cms.response;

import java.io.Serializable;

/**
 * Created by eli.lyj on 2017/8/18.
 */
public abstract class CMSResponse implements Serializable{
    private static final long serialVersionUID = 2341763749192614730L;
    protected String requestId;
    protected String code;
    protected String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
