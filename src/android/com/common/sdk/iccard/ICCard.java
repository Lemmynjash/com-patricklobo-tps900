/**
 * Copyright (c) 2016 广东天波信息技术股份有限公司
 * @author yuhs
 */

package com.common.sdk.iccard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;

import com.common.sdk.InternalErrorException;
import com.common.sdk.TelpoException;

/**
 * 天波IC卡操作类
 * 
 * @author yuhs
 * @version 1.0
 * @since 1.0
 */
public class ICCard {
	private Context mContext = null;
	
	public ICCard(Context context) {
		mContext = context;
	}
	
	public synchronized void open(int slot) throws TelpoException {
		Class<?> iccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			iccard = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("ICCard");
		try {
			method = iccard.getMethod("open", int.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, slot);
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
	
	public synchronized void close() throws TelpoException {
		Class<?> iccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			iccard = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("ICCard");
		try {
			method = iccard.getMethod("close");
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
	 * 检测是否有IC卡插入（阻塞的）
	 * @throws InternalErrorException
	 *            内部错误异常
	 * @param  timeout: 超时时间（以ms为单位）
	 * @return int: 0(检测到IC卡) 非0（没有检测到IC卡）
	 * @author yuhs
	 * @version 1.0
	 * @since 1.0
	 */
	public synchronized int detect(int timeout) throws TelpoException {
		Class<?> iccard = null;
		Method method = null;
		Object obj    = null;
		int result    = 0;
		
		try {
			iccard = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("ICCard");
		try {
			method = iccard.getMethod("detect", int.class);
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
	 * IC卡上电操作
	 * @throws InternalErrorException
	 *            内部错误异常
	 * @author yuhs
	 * @version 1.0
	 * @since 1.0
	 */
	public synchronized void power_on() throws TelpoException {
		Class<?> iccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			iccard = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("ICCard");
		try {
			method = iccard.getMethod("power_on");
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
	 * IC卡获取复位应答数据
	 * @throws InternalErrorException
	 *            内部错误异常
	 * @return String: 返回的应答数据
	 * @author yuhs
	 * @version 1.0
	 * @since 1.0
	 */
	public synchronized String getAtr() throws TelpoException {
		Class<?> iccard = null;
		Method method = null;
		Object obj    = null;
		String result = null;
		
		try {
			iccard = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("ICCard");
		try {
			method = iccard.getMethod("get_atr");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			result = (String) method.invoke(obj);
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
	 * IC卡传输APDU命令
	 * @throws InternalErrorException
	 *            内部错误异常
	 * @param  sendBuffer: 发送的数据缓冲区， sendBufferLength： 发送数据长度
	 * @return String: 返回的数据
	 * @author yuhs
	 * @version 1.0
	 * @since 1.0
	 */
	public synchronized byte[] transmit(byte[] sendBuffer, int sendBufferLength) throws TelpoException {
		Class<?> iccard = null;
		Method method = null;
		Object obj    = null;
		byte[] result = null;
		
		try {
			iccard = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("ICCard");
		try {
			method = iccard.getMethod("transmit", new Class[]{byte[].class, int.class});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			result = (byte[]) method.invoke(obj, sendBuffer, sendBufferLength);
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
	 * IC卡下电操作
	 * @throws InternalErrorException
	 *            内部错误异常
	 * @author yuhs
	 * @version 1.0
	 * @since 1.0
	 */
	public synchronized void power_off() throws TelpoException {
		Class<?> iccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			iccard = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("ICCard");
		try {
			method = iccard.getMethod("power_off");
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
}
