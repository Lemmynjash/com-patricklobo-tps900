/**
 * Copyright (c) 2014 广东天波信息技术股份有限公司
 * @author linhx
 */
package com.telpo.tps900.api.demo.idcard;

import java.io.Serializable;

/**
 * 身份证信息类
 * @author linhx
 * @since 1.0
 */
public class IdentityInfo implements Serializable{
	
	private static final long serialVersionUID = -7696282392790000305L;
	
	private String name;
	private String sex;
	private String nation;
	private String born;
	private String address;
	private String apartment;
	private String period;
	private String no;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getBorn() {
		return born;
	}
	public void setBorn(String born) {
		this.born = born;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
}