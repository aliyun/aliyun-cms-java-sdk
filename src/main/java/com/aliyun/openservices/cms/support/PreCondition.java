package com.aliyun.openservices.cms.support;

/**
 * Created by eli.lyj on 2017/8/22.
 */
public class PreCondition {


    public static void checkIsTrue(boolean value, String exception) {
        if(value) {
            throw new RuntimeException(exception);
        }
    }

}
