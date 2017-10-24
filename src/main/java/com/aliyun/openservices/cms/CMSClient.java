package com.aliyun.openservices.cms;

import com.aliyun.openservices.cms.common.Constants;
import com.aliyun.openservices.cms.exception.CMSException;
import com.aliyun.openservices.cms.http.*;
import com.aliyun.openservices.cms.http.impl.AsyncInvoker;
import com.aliyun.openservices.cms.request.CustomEventUploadRequest;
import com.aliyun.openservices.cms.response.CustomEventUploadResponse;
import com.aliyun.openservices.cms.support.DateUtil;
import com.aliyun.openservices.cms.support.StringSupport;
import org.apache.commons.validator.routines.InetAddressValidator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by eli.lyj on 2017/8/18.
 */
public class CMSClient {

    private final String endPoint;
    private final String accessKeyId;
    private final String accessSecret;
    private final String sourceIp;
    private HttpInvoker client;
    private final String host;

    public CMSClient(String endPoint, String accessKeyId, String accessSecret) {
        this(endPoint, accessKeyId, accessSecret, null);
    }


    public CMSClient(String endPoint, String accessKeyId, String accessSecret, String sourceIp) {
        if(endPoint.endsWith("/")) {
            endPoint = endPoint.substring(0, endPoint.length() - 1);
        }
        if(endPoint.startsWith("http://")) {
            host = endPoint.substring(7, endPoint.length());
        }else if(endPoint.startsWith("https://")) {
            host = endPoint.substring(8, endPoint.length());
        }else {
            throw new IllegalArgumentException("endpoint must start with 'http' or 'https', only hostname");
        }
        this.endPoint = endPoint;
        this.accessKeyId = accessKeyId;
        this.accessSecret = accessSecret;
        AsyncInvoker invoker = new AsyncInvoker();
        invoker.init();
        client = invoker;
        if(StringSupport.isNullOrEmpty(sourceIp, true)) {
            sourceIp = localMachineIp();
        }
        this.sourceIp = sourceIp;
    }

    public void setClient(HttpInvoker client) {
        this.client = client;
    }

    public CustomEventUploadResponse putCustomEvent(CustomEventUploadRequest request) throws CMSException {
        Response response = invokeHttp(request.httpMethod(), request.uri(), request.queryParam(), request.body());
        CustomEventUploadResponse ret = new CustomEventUploadResponse();
        ret.setRequestId(response.getRequestId());
        ret.setCode(response.getCode());
        ret.setMessage(response.getMsg());
        return ret;
    }

    protected Response invokeHttp(HttpMethod method,
                                  String resourceUri, Map<String, String> parameters,
                                  byte[] body) throws CMSException {
        Map<String, String> headers = headers();
        if (body.length > 0) {
            headers.put(Constants.CONST_CONTENT_MD5, md5(body));
        }
        headers.put(Constants.CONST_CONTENT_LENGTH, String.valueOf(body.length));
        signature(method.toString(), headers, resourceUri, parameters);
        Request request = new Request();
        request.setBody(body);
        request.setHeaders(headers);
        request.setQueryParams(parameters);
        request.setHttpMethod(method);
        request.setUri(endPoint + resourceUri);
        Response response = null;
        try {
            response = this.client.doHttpClient(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CMSException("", "500", e.getMessage());
        }
        if(response.getHttpStatusCode() != 200
                || !"200".equals(response.getCode())) {
            throw new CMSException(response.getRequestId(), response.getCode(), response.getMsg());
        }
        return response;
    }

    /**
     * 计算body的MD5
     * @param bytes
     * @return
     */
    private String md5(byte[] bytes) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance(Constants.CONST_MD5);
            return StringSupport.base16(md.digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Not Supported signature method "
                    + Constants.CONST_MD5, e);
        }
    }

    private void signature(String verb,
                           Map<String, String> headers, String resourceUri,
                           Map<String, String> urlParams) {
        StringBuilder builder = new StringBuilder();
        builder.append(verb).append("\n");
        builder.append(getMapValue(headers, Constants.CONST_CONTENT_MD5)).append(
                "\n");
        builder.append(getMapValue(headers, Constants.CONST_CONTENT_TYPE)).append(
                "\n");
        builder.append(getMapValue(headers, Constants.CONST_DATE)).append("\n");
        builder.append(getCanonicalizedHeaders(headers)).append("\n");
        builder.append(resourceUri);
        if (urlParams.isEmpty() == false) {
            builder.append("?");
            builder.append(buildUrlParameter(urlParams));
        }
        String signature = caluSignature(accessSecret, builder.toString());
        headers.put(Constants.CONST_AUTHORIZATION, accessKeyId + ":" + signature);
    }

    private String getMapValue(Map<String, String> map, String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return "";
        }
    }

    private String buildUrlParameter(Map<String, String> paras) {
        Map<String, String> treeMap = new TreeMap<String, String>(paras);
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            if (isFirst == true) {
                isFirst = false;
            } else {
                builder.append("&");
            }
            builder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return builder.toString();
    }

    private String getCanonicalizedHeaders(Map<String, String> headers) {
        Map<String, String> treeMap = new TreeMap<String, String>(headers);
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            if (!entry.getKey().startsWith(Constants.CONST_X_CMS_PREFIX)
                    && !entry.getKey().startsWith(Constants.CONST_X_ACS_PREFIX)) {
                continue;
            }
            if (isFirst == true) {
                isFirst = false;
            } else {
                builder.append("\n");
            }
            builder.append(entry.getKey()).append(":").append(entry.getValue());
        }
        return builder.toString();
    }

    private String caluSignature(String accesskey, String data) {
        try {
            byte[] keyBytes = accesskey.getBytes(Constants.UTF_8_ENCODING);
            byte[] dataBytes = data.getBytes(Constants.UTF_8_ENCODING);
            Mac mac = Mac.getInstance(Constants.HMAC_SHA1_JAVA);
            mac.init(new SecretKeySpec(keyBytes, Constants.HMAC_SHA1_JAVA));
            String sig = new String(StringSupport.base16(mac.doFinal(dataBytes)));
            return sig;
        } catch (UnsupportedEncodingException e) { // actually these exceptions
            // should never happened
            throw new RuntimeException("Not Supported encoding method "
                    + Constants.UTF_8_ENCODING, e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Not Supported signature method "
                    + Constants.HMAC_SHA1, e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Failed to calcuate the signature", e);
        }
    }

    private Map<String, String> headers() {
        HashMap<String, String> headParameter = new HashMap<String, String>();
        headParameter.put(Constants.CONST_USER_AGENT, Constants.CONST_USER_AGENT_VALUE);
        headParameter.put(Constants.CONST_CONTENT_LENGTH, "0");
        headParameter.put(Constants.CONST_CONTENT_TYPE, Constants.CONST_FORMAT_JSON);
        headParameter.put(Constants.CONST_DATE, DateUtil.formatRfc822Date(new Date()));
        headParameter.put(Constants.CONST_HOST, this.host);
        headParameter.put(Constants.CONST_X_CMS_APIVERSION, Constants.DEFAULT_API_VESION);
        headParameter.put(Constants.CONST_X_CMS_SIGNATURE_METHOD, Constants.HMAC_SHA1);
        headParameter.put(Constants.CONST_X_CMS_IP, sourceIp);
        return headParameter;
    }





    private String localMachineIp() {
        InetAddressValidator validator = new InetAddressValidator();
        String candidate = "N/A";
        try {
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface
                    .getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = ifaces.nextElement();

                if (iface.isUp()) {
                    for (Enumeration<InetAddress> addresses = iface
                            .getInetAddresses(); addresses.hasMoreElements();) {

                        InetAddress address = addresses.nextElement();

                        if (address.isLinkLocalAddress() == false
                                && address.getHostAddress() != null) {
                            String ipAddress = address.getHostAddress();
                            if (ipAddress.equals(Constants.CONST_LOCAL_IP)) {
                                continue;
                            }
                            if (validator.isValidInet4Address(ipAddress)) {
                                return ipAddress;
                            }
                            if (validator.isValid(ipAddress)) {
                                candidate = ipAddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {

        }
        return candidate;
    }
}
