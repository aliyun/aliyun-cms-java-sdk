package com.aliyun.openservices.cms.support;

/**
 * Created by eli.lyj on 2017/8/18.
 */
public class StringSupport {

    


    public static String null2empty(String str) {
        return str == null ? "" : str;
    }



    static char[] cc = "0123456789ABCDEF".toCharArray();
    public static String base16(byte[] arr) {
        StringBuilder sb = new StringBuilder(arr.length << 1);
        for(byte b : arr) {
            sb.append(cc[(b >> 4) & 0x0f]);
            sb.append(cc[b & 0x0f]);
        }
        return sb.toString();
    }

    public static boolean isNullOrEmpty(String src, boolean trim) {
        if(src == null) {
            return true;
        }
        return trim ? src.trim().length() == 0 : src.length() == 0;
    }
}
