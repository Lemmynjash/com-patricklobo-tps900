/**
 * Copyright (c) 2016 广东天波信息技术股份有限公司
 * @author yuhs
 */

package com.common.sdk.nfc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.R.integer;
import android.content.Context;

import com.common.sdk.InternalErrorException;
import com.common.sdk.TelpoException;

/**
 * 天波NFC控制类
 * 
 * @author yuhs
 * @version 1.0
 * @since 1.0
 */

public class Nfc {
	private Context mContext = null;
	
	public Nfc(Context context) {
		mContext = context;
	}
	
	public synchronized void open() throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("open");
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
	
	public synchronized void close() throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("close");
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
	
	public synchronized byte[] activate(int timeOut) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		byte[] result = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("activate", int.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			result = (byte[]) method.invoke(obj, timeOut);
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
	
	public synchronized byte[] cpu_get_ats() throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		byte[] result = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("cpu_get_ats");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			result = (byte[]) method.invoke(obj);
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
	
	public synchronized byte[] transmit(byte[] sendBuffer, 
			int sendBufferLength) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		byte[] result = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("cpu_transmit", new Class[]{byte[].class, int.class});
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

	public synchronized void m1_authenticate(byte noBlock, byte passwd) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("m1_authenticate", new Class[] {byte.class, byte.class});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, noBlock, passwd);
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
	
	
	public synchronized byte[] m1_read_block(byte noBlock) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		byte[] result = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("m1_read_block", byte.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			result = (byte[]) method.invoke(obj, noBlock);
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
	
	public synchronized void m1_write_block(byte noBlock, byte[] inBuf,
			int inLen) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("m1_write_block", new Class[] {byte.class, byte[].class, int.class});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, noBlock, inBuf, inLen);
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
	
	public synchronized byte[] m1_read_value(byte noBlock) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		byte[] result = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("m1_read_value", byte.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			result = (byte[]) method.invoke(obj, noBlock);
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
	
	public synchronized void m1_write_value(byte noBlock, byte[] inBuf,
			int inLen) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("m1_write_value", new Class[] {byte.class, byte[].class, int.class});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, noBlock, inBuf, inLen);
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
	
	public synchronized void m1_increment(byte srcAddr, byte destAddr,
			byte[] inBuf, int inLen) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("m1_increment", new Class[] {byte.class, byte.class, byte[].class, int.class});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, srcAddr, destAddr, inBuf, inLen);
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
	
	public synchronized void m1_decrement(byte srcAddr, byte destAddr,
			byte[] inBuf, int inLen) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("m1_decrement", new Class[] {byte.class, byte.class, byte[].class, int.class});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, srcAddr, destAddr, inBuf, inLen);
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
	
	public synchronized void halt() throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("halt");
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
	
	public synchronized void remove(int timeOut) throws TelpoException {
		Class<?> nfccard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			nfccard = Class.forName("com.common.sdk.nfc.NFCServiceManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("NFC");
		try {
			method = nfccard.getMethod("remove", int.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			method.invoke(obj, timeOut);
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
