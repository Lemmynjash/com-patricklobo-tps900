/**
 * Copyright (c) 2014 广东天波信息技术股份有限公司
 * @author linhx
 */
package com.common.sdk;


/**
 * 系统/设备缓存不足异常
 * @author linhx
 * @version 1.0
 * @since 1.0
 */
public class DeviceOverFlowException extends TelpoException{

	private static final long serialVersionUID = -2720559549044825415L;

	public DeviceOverFlowException() {
		super();
	}

	public DeviceOverFlowException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DeviceOverFlowException(String detailMessage) {
		super(detailMessage);
	}

	public DeviceOverFlowException(Throwable throwable) {
		super(throwable);
	}
	
	public String getDescription(){
		return "Memory or buffer over flow!";
	}
}
