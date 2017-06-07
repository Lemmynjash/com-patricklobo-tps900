/**
 * Copyright (c) 2016 广东天波信息技术股份有限公司
 * @author linhx
 */
package com.common.sdk;

/**
 * 无权限访问异常
 * @author linhx
 * @version 1.0
 * @since 1.0
 */
public class PermissionDenyException extends TelpoException{

	private static final long serialVersionUID = 1149990948139378861L;

	public PermissionDenyException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PermissionDenyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public PermissionDenyException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public PermissionDenyException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}
	
	public String getDescription(){
		return "Permission Deny!";
	}
}
