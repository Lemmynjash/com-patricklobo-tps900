/**
 * Copyright (c) 2014 广东天波信息技术股份有限公司
 * @author linhx
 */
package com.common.sdk;


/**
 * 设备未打开/设备资源未申请异常
 * @author linhx
 * @version 1.0
 * @since 1.0
 */
public class DeviceNotOpenException extends TelpoException {

	private static final long serialVersionUID = -3693748800173552918L;

	public DeviceNotOpenException() {
		super();
	}

	public DeviceNotOpenException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DeviceNotOpenException(String detailMessage) {
		super(detailMessage);
	}

	public DeviceNotOpenException(Throwable throwable) {
		super(throwable);
	}
	
	public String getDescription(){
		return "Device not open!";
	}
}
