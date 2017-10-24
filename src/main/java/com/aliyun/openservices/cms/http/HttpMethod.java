/**
 * Copyright (C) Alibaba Cloud Computing
 * All rights reserved.
 * 
 * 版权所有 （C）阿里云计算有限公司
 */

package com.aliyun.openservices.cms.http;

/**
 * 表示HTTP的请求方法。
 * 
 * @author xiaoming.yin
 * 
 */
public enum HttpMethod {
	/**
	 * DELETE方法。
	 */
	DELETE("DELETE"),

	/**
	 * GET方法。
	 */
	GET("GET"),

	/**
	 * HEAD方法。
	 */
	HEAD("HEAD"),

	/**
	 * POST方法。
	 */
	POST("POST"),

	/**
	 * PUT方法。
	 */
	PUT("PUT"),

	/**
	 * OPTION方法。
	 */
	OPTIONS("OPTIONS");

	private final String text;

	private HttpMethod(final String text) {
		this.text = text;
	}

	public String toString() {
		return this.text;
	}
}
