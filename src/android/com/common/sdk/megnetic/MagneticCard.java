/**
 * Copyright (c) 2016 广东天波信息技术股份有限公司
 * @author yuhs
 */

package com.common.sdk.megnetic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;

import com.common.sdk.InternalErrorException;
import com.common.sdk.TelpoException;

/**
 * 天波磁条卡操作类
 * 
 * @author yuhs
 * @version 1.0
 * @since 1.0
 */
public class MagneticCard {
	private Context mContext = null;
	
	public MagneticCard(Context context) {
		mContext = context;
	}
	
	public synchronized void open() throws TelpoException{
		Class<?> msr = null;
		Method method = null;
		Object obj    = null;
		
		try {
			msr = Class.forName("com.common.sdk.magneticcard.MagneticCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("MagneticCard");
		try {
			method = msr.getMethod("open");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj);
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
	
	public synchronized void close() throws TelpoException{
		Class<?> msr = null;
		Method method = null;
		Object obj    = null;
		
		try {
			msr = Class.forName("com.common.sdk.magneticcard.MagneticCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("MagneticCard");
		try {
			method = msr.getMethod("close");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj);
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
	
	/**
	 * 检测是否刷到卡(阻塞的)
	 * @throws InternalErrorException
	 *            内部错误异常
	 * @param 	timeout: 超时时间（以ms为单位）
	 * @return  int 0: 刷到卡 ，非0：超时没有刷到卡
	 * @author yuhs
	 * @version 1.0
	 * @since 1.0
	 */
	public synchronized int check(int timeout) throws TelpoException{
		Class<?> msr = null;
		Method method = null;
		Object obj    = null;
		int result = 0;
		
		try {
			msr = Class.forName("com.common.sdk.magneticcard.MagneticCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("MagneticCard");
		try {
			method = msr.getMethod("check", int.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			result = (Integer) method.invoke(obj, timeout);
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
		return result;
	}
	
	/**
	 * 读取3个磁道的数据
	 * @throws InternalErrorException
	 *            内部错误异常
	 * @return 	String[]： 3个磁道的数据
	 * @author yuhs
	 * @version 1.0
	 * @since 1.0
	 */
	public synchronized String[] read() throws TelpoException{
		Class<?> msr = null;
		Method method = null;
		String[] result = new String[3];
		Object obj    = null;
		
		try {
			msr = Class.forName("com.common.sdk.magneticcard.MagneticCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("MagneticCard");
		try {
			method = msr.getMethod("read");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			result = (String[]) method.invoke(obj);
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
		return result;
	}
}
