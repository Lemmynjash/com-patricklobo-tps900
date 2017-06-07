/**
 * Copyright (c) 2014 广东天波信息技术股份有限公司
 * @author linhx
 */
package com.common.sdk;

/**
 * 超时
 * @author linhx
 * @since 1.0
 */
public class TimeoutException extends TelpoException {

	private static final long serialVersionUID = 8323068427519122516L;

	public TimeoutException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TimeoutException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public TimeoutException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public TimeoutException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
	
	public String getDescription(){
		return "Operation timeout!";
	}
}
