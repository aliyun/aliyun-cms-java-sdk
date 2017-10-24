package com.aliyun.openservices.cms.exception;

/**
 * Created by eli.lyj on 2017/8/18.
 */
public class CMSException extends Exception{
    private static final long serialVersionUID = -9085101696734008965L;
    private final String requestId;
    private final String errorCode;

    public CMSException(String requestId, String errorCode, String errorMsg) {
        super(errorMsg);
        this.requestId = requestId;
        this.errorCode = errorCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getErrorCode() {
        return errorCode;
    }


    public String toString() {
        return String.format("%s,%s,%s", requestId, errorCode, getMessage());
    }
}
