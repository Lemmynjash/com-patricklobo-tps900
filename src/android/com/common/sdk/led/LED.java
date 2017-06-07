/**
 * Copyright (c) 2016 广东天波信息技术股份有限公司
 * @author yuhs
 */

package com.common.sdk.led;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;

import com.common.sdk.InternalErrorException;
import com.common.sdk.TelpoException;

/**
 * LED操作类
 * 
 * @author yuhs
 * @version 1.0
 * @since 1.0
 */
public class LED {
	private Context mContext = null;
	
	public LED(Context context) {
		mContext = context;
	}
	
	public synchronized void on(int num) throws TelpoException {
		Class<?> led = null;
		Method method = null;
		Object obj    = null;
		
		try {
			led = Class.forName("com.common.sdk.led.LEDServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("LED");
		try {
			method = led.getMethod("on", int.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, num);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Exception targetExp = (Exception) e.getTargetException();
			if (targetExp instanceof TelpoException)
			{
				throw (TelpoException)targetExp;
			}
		}
	}
	
	public synchronized void off(int num) throws TelpoException {
		Class<?> led = null;
		Method method = null;
		Object obj    = null;
		
		try {
			led = Class.forName("com.common.sdk.led.LEDServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("LED");
		try {
			method = led.getMethod("off", int.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, num);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Exception targetExp = (Exception) e.getTargetException();
			if (targetExp instanceof TelpoException)
			{
				throw (TelpoException)targetExp;
			}
		}
	}
	
	public synchronized void blink(int num, int period) throws TelpoException {
		Class<?> led = null;
		Method method = null;
		Object obj    = null;
		
		try {
			led = Class.forName("com.common.sdk.led.LEDServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("LED");
		try {
			method = led.getMethod("blink", new Class[] {int.class, int.class});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, num, period);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			Exception targetExp = (Exception) e.getTargetException();
			if (targetExp instanceof TelpoException)
			{
				throw (TelpoException)targetExp;
			}
		}
	}
	
}
