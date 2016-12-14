package com.dingdong.register.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

@Alias("doctor")
public class Doctor extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3799756507349833388L;
	private String name = "";
	private int gender;
	private long hospitalId = 0;
	private String hospitalName = "";
	private String level = "";
	private Integer status = Status.CREATED.getValue();
	private String specialty = "";
	private String introduction = "";

	private int treatmentEffect = 500;
	private int serviceAttitude = 500;
	private String headImgUrl = "";
	// 二维码地址
	private String qrImgUrl = "";
	// 挂号日期
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	private Date createTime = new Date();

	private long userId = 0;
	/**
	 * 医生的粉丝数量
	 */
	private long fansCount = 0;

	/**
	 * 医生执业资格编号
	 */
	private String qualificationId = "";

	private String officeTele = "";

	public long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public long getDeptId() {
		return deptId;
	}

	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}

	private String mobilePhone = "";

	private long deptId = 0;
	private String deptName = "";

	private String teacherLevel = "";
	// 医生的用户评级，从0-500
	private int grade = 400;

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getTeacherLevel() {
		return teacherLevel;
	}

	public void setTeacherLevel(String teacherLevel) {
		this.teacherLevel = teacherLevel;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOfficeTele() {
		return officeTele;
	}

	public void setOfficeTele(String officeTele) {
		this.officeTele = officeTele;
	}

	public String getQualificationId() {
		return qualificationId;
	}

	public void setQualificationId(String qualificationId) {
		this.qualificationId = qualificationId;
	}

	public long getFansCount() {
		return fansCount;
	}

	public void setFansCount(long fansCount) {
		this.fansCount = fansCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getTreatmentEffect() {
		return treatmentEffect;
	}

	public void setTreatmentEffect(int treatmentEffect) {
		this.treatmentEffect = treatmentEffect;
	}

	public int getServiceAttitude() {
		return serviceAttitude;
	}

	public void setServiceAttitude(int serviceAttitude) {
		this.serviceAttitude = serviceAttitude;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getQrImgUrl() {
		return qrImgUrl;
	}

	public void setQrImgUrl(String qrImgUrl) {
		this.qrImgUrl = qrImgUrl;
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
