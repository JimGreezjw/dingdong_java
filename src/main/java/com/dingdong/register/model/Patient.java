package com.dingdong.register.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

@Alias("patient")
public class Patient extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3716363770341962315L;

	private String name = "";

	// 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private int gender = 0;
	private Date birthday = new Date();
	private String address = "";
	private String phone = "";
	// 0-本人，1-家人，2-亲戚，3-朋友，4-其他
	private int userRelation = 0;

	private int status = Status.EFFECTIVE.getValue();

	public int getUserRelation() {
		return userRelation;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setUserRelation(int userRelation) {
		this.userRelation = userRelation;
	}

	private String certificateId;// 身份证号码

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Date createTime = new Date();

	private long userId = 0;

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public static enum Status {
		CREATED(0, "创建"), EFFECTIVE(1, "已生效"), CANCEL(2, "已取消");

		private int value;
		private String desc;

		Status(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

}
