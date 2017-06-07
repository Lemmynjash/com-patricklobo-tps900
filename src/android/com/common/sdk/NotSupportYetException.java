/**
 * Copyright (c) 2014 广东天波信息技术股份有限公司
 * @author linhx
 */
package com.common.sdk;

/**
 * 当前软件/硬件暂不支持异常
 * @author linhx
 * @since 1.0
 */
public class NotSupportYetException extends TelpoException{

	private static final long serialVersionUID = 2975865503741011861L;

	public NotSupportYetException() {
		super();
	}

	public NotSupportYetException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NotSupportYetException(String detailMessage) {
		super(detailMessage);
	}

	public NotSupportYetException(Throwable throwable) {
		super(throwable);
	}
	
	public String getDescription(){
		return "The operation or the function is not supported for current SDK/Hardware !";
	}
}