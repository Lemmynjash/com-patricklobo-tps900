/**
 * Copyright (c) 2014 广东天波信息技术股份有限公司
 * @author linhx
 */
package com.common.sdk;

/**
 * 设备重复打开异常
 * @author linhx
 * @since 1.0
 */
public class DeviceAlreadyOpenException extends TelpoException{
	
	private static final long serialVersionUID = 775254919822242857L;

	public DeviceAlreadyOpenException() {
		super();
	}

	public DeviceAlreadyOpenException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DeviceAlreadyOpenException(String detailMessage) {
		super(detailMessage);
	}

	public DeviceAlreadyOpenException(Throwable throwable) {
		super(throwable);
	}
	
	public String getDescription(){
		return "Device already opened!";
	}
}
