package com.dingdong.sys.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;
import com.dingdong.register.model.Hospital;

@Alias("user")
public class User extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8633117192993098446L;

	private String Name = "";

	// 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知

	private int gender = 0;
	private Date birthday = new Date(0);
	private String address = "";
	private String phone = "";

	// 推荐用户加入的微信二维码场景编号
	private String qrsceneId = "";
	// 0--表示患者，1--表示医生
	private int role = Role.PATIENT.getId();
	private Date createTime = new Date();

	private String openId = "";
	private String nickName = "";// 微信昵称
	/**
	 * 账户余额
	 */
	private float balance = 0f;
	/**
	 * 账户积分
	 */
	private int score = 0;

	private String country = "";
	private String province = "";
	private String city = "";
	private String headImgUrl = "";

	private int status = Status.ACTIVATED.getValue();
	private long opHospitalId = 0;
	private String password = "";
	private Hospital hospital;

	public long getOpHospitalId() {
		return opHospitalId;
	}

	public void setOpHospitalId(long opHospitalId) {
		this.opHospitalId = opHospitalId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getQrsceneId() {
		return qrsceneId;
	}

	public void setQrsceneId(String qrsceneId) {
		this.qrsceneId = qrsceneId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

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

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public static enum Role {
		PATIENT(0, "患者"), DOCTOR(1, "医生"), DDADMIN(2, "叮咚运维"), HOSPITALADMIN(3,
				"医院运维");
		private int id;
		private String desc;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		Role(int id, String desc) {
			this.id = id;
			this.desc = desc;
		}

	}

	public static enum Status {
		CREATED(0, "创建"), ACTIVATED(1, "已激活"), CANCEL(2, "已取消");

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
