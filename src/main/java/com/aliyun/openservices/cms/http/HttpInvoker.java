package com.aliyun.openservices.cms.http;

import java.io.IOException;

/**
 * Created by eli.lyj on 2017/8/22.
 */
public interface HttpInvoker {
    Response doHttpClient(Request request) throws IOException;
}
