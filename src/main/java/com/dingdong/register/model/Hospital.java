package com.dingdong.register.model;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

@Alias("hospital")
public class Hospital extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5510994129052636191L;

	public void setName(String name) {
		this.name = name;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setServiceAttitude(int serviceAttitude) {
		this.serviceAttitude = serviceAttitude;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private List<HospitalDept> hospitalDepts;

	public List<HospitalDept> getHospitalDepts() {
		return hospitalDepts;
	}

	public void setHospitalDepts(List<HospitalDept> hospitalDepts) {
		this.hospitalDepts = hospitalDepts;
	}

	private String name = "";
	private String fullName = "";
	private String level = "";
	private int status = 0;
	private String introduction = "";
	private String country = "";
	private String province = "";
	private String city = "";
	private String street = "";
	private int serviceAttitude = 0;
	private String logo = "";
	// 医院办公电话
	private String tele = "";
	// 运维手机号码
	private String opTele = "";
	// 医院图标
	private String iconUrl = "";

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getOpTele() {
		return opTele;
	}

	public void setOpTele(String opTele) {
		this.opTele = opTele;
	}

	public String getTele() {
		return tele;
	}

	public void setTele(String tele) {
		this.tele = tele;
	}

	private Date createTime = new Date();

	public String getName() {
		return name;
	}

	public String getFullName() {
		return fullName;
	}

	public String getLevel() {
		return level;
	}

	public int getStatus() {
		return status;
	}

	public String getCountry() {
		return country;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public int getServiceAttitude() {
		return serviceAttitude;
	}

	public String getLogo() {
		return logo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public static enum Status {
		CREATED(0, "创建"), SIGNED(1, "已签约"), CANCEL(2, "已取消");

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
