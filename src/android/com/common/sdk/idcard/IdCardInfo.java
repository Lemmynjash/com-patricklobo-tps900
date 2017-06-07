/**
 * Copyright (c) 2016, 广东天波信息技术股份有限公司
 *  二代身份证信息类的实现
 */

package com.common.sdk.idcard;

import android.os.Parcel;
import android.os.Parcelable;

public class IdCardInfo implements Parcelable{
	private String name;
	private String sex;
	private String nation;
	private String born;
	private String adress;
	private String number;
	private String office;
	private String term;
	byte[]img = new byte[1024];
	

	public IdCardInfo() {
		
	}
	public IdCardInfo(Parcel in) {
        this.name = in.readString();
        this.sex = in.readString();
        this.nation = in.readString();
        this.born = in.readString();
        this.adress = in.readString();
        this.number = in.readString();
        this.office = in.readString();
        this.term = in.readString();
        in.readByteArray(this.img);
	}

	public String getName(){
		return name;
	}

	public String getSex(){
		return sex;
	}

	public String getNation(){
		return nation;
	}

	public String getBorn(){
		return born;
	}

	public String getAdress(){
		return adress;
	}

	public String getNumber(){
		return number;
	}

	public String getOffice(){
		return office;
	}

	public String getTerm(){
		return term;
	}
	
	
	public byte[] getImg(){
		
		return img;
	}
	
	

	
	public void setName(String name){
         this.name = name;
	}
	public void setSex(String sex){
         this.sex = sex;
	}
	public void setNation(String nation){
         this.nation = nation;
	}public void setBorn(String born){
         this.born = born;
	}
	public void setAdress(String adress){
         this.adress = adress;
	}
	public void setNumber(String number){
         this.number = number;
	}
	public void setOffice(String office){
         this.office = office;
	}
	public void setTerm(String term){
         this.term = term;
	}
	
	public void setImg(byte[] img){
         this.img = img;
	}
	



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(nation);
        dest.writeString(born);
		dest.writeString(adress);
		dest.writeString(number);
		dest.writeString(office);
		dest.writeString(term);
		dest.writeByteArray(img);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<IdCardInfo> CREATOR = new Creator<IdCardInfo>() {
        @Override
        public IdCardInfo createFromParcel(Parcel source) {
            return new IdCardInfo(source);
        }

        @Override
        public IdCardInfo[] newArray(int size) {
            return new IdCardInfo[size];
        }
    };

}
