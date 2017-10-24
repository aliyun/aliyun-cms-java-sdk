package com.aliyun.openservices.cms.http.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.cms.http.HttpInvoker;
import com.aliyun.openservices.cms.http.HttpMethod;
import com.aliyun.openservices.cms.http.Request;
import com.aliyun.openservices.cms.http.Response;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.ListenableFuture;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by eli.lyj on 2017/8/22.
 */
public class AsyncInvoker implements HttpInvoker{
    protected AsyncHttpClient client;

    private int maxConnections = 10;
    private int connectTimeoutInMs = 5000;
    private int requestTimeoutInMs = 3000;
    private int maxConnectionsPerHost = 10;
    private int maxRetryNum = 2;

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public void setConnectTimeoutInMs(int connectTimeoutInMs) {
        this.connectTimeoutInMs = connectTimeoutInMs;
    }

    public void setRequestTimeoutInMs(int requestTimeoutInMs) {
        this.requestTimeoutInMs = requestTimeoutInMs;
    }

    public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
    }

    public void init() {
        client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder()
                .setMaximumConnectionsTotal(maxConnections)
                .setConnectionTimeoutInMs(connectTimeoutInMs)
                .setMaximumConnectionsPerHost(maxConnectionsPerHost)
                .setMaxRequestRetry(maxRetryNum)
                .setRequestTimeoutInMs(requestTimeoutInMs).build());
    }

    @Override
    public Response doHttpClient(Request request) throws IOException {
        AsyncHttpClient.BoundRequestBuilder builder = null;
        if(request.getHttpMethod() == HttpMethod.GET) {
            builder = client.prepareGet(request.getUri());
        }else if(request.getHttpMethod() == HttpMethod.POST) {
            builder = client.preparePost(request.getUri());
        }else {
            throw new IllegalArgumentException("unsupport method " + request.getHttpMethod().toString());
        }
        if(request.getBody() != null) {
            builder.setBody(request.getBody());
        }
        addHeader(builder, request);
        addQueryParam(builder, request);
        ListenableFuture<Response> future = client.executeRequest(builder.build(), new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(com.ning.http.client.Response response) throws Exception {
                return new Response(response.getStatusCode(),
                        JSONObject.parseObject(response.getResponseBody()));
            }
        });
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addHeader(AsyncHttpClient.BoundRequestBuilder builder, Request request) {
        for(Map.Entry<String, String> e : request.getHeaders().entrySet()) {
            builder.setHeader(e.getKey(), e.getValue());
        }
    }

    private void addQueryParam(AsyncHttpClient.BoundRequestBuilder builder, Request request) {
        for(Map.Entry<String, String> e : request.getQueryParams().entrySet()) {
            builder.addQueryParameter(e.getKey(), e.getValue());
        }
    }

}
