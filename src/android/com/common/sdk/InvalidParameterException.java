package com.common.sdk;

public class InvalidParameterException extends TelpoException{
	private static final long serialVersionUID = 4961548828475264132L;
	
	public InvalidParameterException() {
		super();
	}

	public InvalidParameterException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InvalidParameterException(String detailMessage) {
		super(detailMessage);
	}

	public InvalidParameterException(Throwable throwable) {
		super(throwable);
	}
	
	public String getDescription(){
		return "Some parameter for the method is invalid!";
	}
}
