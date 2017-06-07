/**
 * Copyright (c) 2014 广东天波信息技术股份有限公司
 * @author linhx
 */
package com.common.sdk;

/**
 * IC卡/PSAM卡接收缓存不足时抛出
 * @author linhx
 * @since 1.0
 */
public class NotEnoughBufferException extends TelpoException {

	private static final long serialVersionUID = -2405024364586250998L;

	public NotEnoughBufferException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotEnoughBufferException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughBufferException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughBufferException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
	
	public String getDescription(){
		return "The given buffer is not enough for data to receive!";
	}
}
