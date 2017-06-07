/**
 * Copyright (c) 2016 广东天波信息技术股份有限公司
 * @author yuhs
 */

package com.common.sdk.idcard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Bitmap;

import com.common.sdk.InternalErrorException;
import com.common.sdk.TelpoException;
import com.telpo.tps900.api.demo.idcard.IdentityInfo;

/**
 * 天波IC卡操作类
 * 
 * @author yuhs
 * @version 1.0
 * @since 1.0
 */
public class IDCard {
	private Context mContext = null;
	
	IdentityInfo identityinfo = new IdentityInfo();
	IdCardInfo idcardinfo = null;
	byte[]imgae = new byte[1024];
	public String name = null;
	public String sex = null;
	public String nation = null;
	public String born = null;
	public String address = null;
	public String number = null;
	public String office = null;
	public String term = null;
	
	public IDCard(Context context) {
		mContext = context;
	}
	
	public synchronized void open(){
		
	}
	
	public synchronized static void open(int type,int pautRate) {
		
	}
	
	public synchronized static void open(int type) {
		
	}

	public synchronized void close() {
		
	}
	
	/**
	 * 搜索并读取二代身份证信息
	 * 
	 * @author linhx
	 * @since 1.0
	 * @param timeoutInMs
	 *            超时时间，单位ms
	 * @return 返回二代身份证信息
	 * @throws DeviceNotOpenException
	 *             二代证读卡器未打开
	 * @throws TimeoutException
	 *             超时时间内读不到二代证信息时产生
	 * 
	 */
	public synchronized IdentityInfo checkIdCard(int timeout)
			throws TelpoException {
		Class<?> idcard = null;
		Method method = null;
		Object obj    = null;
		
		try {
			idcard = Class.forName("com.common.sdk.idcard.IdCardManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("IDCard");
		try {
			method = idcard.getMethod("getIdcardInfo", int.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			idcardinfo = (IdCardInfo) method.invoke(obj, timeout);
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
		name = idcardinfo.getName();
		sex = idcardinfo.getSex();
		nation = idcardinfo.getNation();
		born = idcardinfo.getBorn();
		address = idcardinfo.getAdress();
		number = idcardinfo.getNumber();
		office = idcardinfo.getOffice();
		term = idcardinfo.getTerm();
		
		identityinfo.setName(name);
		identityinfo.setSex(sex);
		identityinfo.setNation(nation);
		identityinfo.setBorn(born);
		identityinfo.setAddress(address);
		identityinfo.setNo(number);
		identityinfo.setPeriod(term);
		identityinfo.setApartment(office);
		
		return identityinfo;
	}
	
	public synchronized byte[] getIdCardImage() throws TelpoException {
		
		imgae = idcardinfo.getImg();
		if (imgae != null){
			return imgae;
		}else{
			return null;
		}
		
		
	}
	
	public Bitmap decodeIdCardImage(byte[] image) throws TelpoException {

		Class<?> idcard = null;
		Method method = null;
		Object obj    = null;
		Bitmap picture = null;
		try {
			idcard = Class.forName("com.common.sdk.idcard.IdCardManager");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		obj =  mContext.getSystemService("IDCard");
		try {
			method = idcard.getMethod("getBitmap", byte[].class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new InternalErrorException();
		}
		
		try {
			picture = (Bitmap) method.invoke(obj, image);
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
		return picture;
	}
}
	