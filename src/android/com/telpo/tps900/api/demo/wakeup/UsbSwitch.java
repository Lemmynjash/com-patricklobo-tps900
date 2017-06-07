package com.telpo.tps900.api.demo.wakeup;

public class UsbSwitch {

	private static native int sleep_3255();
	private static native int wakeup_3255();
	
	public static int sleep() {
		return sleep_3255();
	}
	
	public static int wakeup() {
		return wakeup_3255();
	}
	
	static {
		System.loadLibrary("sleepWakeup");
	}
}
