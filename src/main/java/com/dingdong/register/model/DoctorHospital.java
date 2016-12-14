package com.dingdong.register.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

@Alias("doctorHospital")
public class DoctorHospital extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6706839412335875334L;
	private long doctorId;
	private String doctorName = "";
	private long hospitalId = 0;
	private String hospitalName = "";
	private String headImgUrl = "";
	// 在本医院的诚意金
	private int deposit = 100;

	// 挂号级别，缺省为顶级门诊
	private int registerLevel = RegisterLevel.LEVEL2.getValue();

	public String getRegisterLevelDesc() {
		return getLevelDesc(this.registerLevel);
	}

	public static String getLevelDesc(int level) {
		for (RegisterLevel ts : RegisterLevel.values()) {
			if (ts.getValue() == level)
				return ts.getDesc();
		}
		return null;
	}

	public static enum RegisterLevel {

		LEVEL0(0, "普通门诊"), LEVEL1(1, "专家门诊"), LEVEL2(2, "特需门诊");

		private int value;
		private String desc;

		RegisterLevel(int value, String desc) {
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

	public int getDeposit() {
		return deposit;
	}

	public int getRegisterLevel() {
		return registerLevel;
	}

	public void setRegisterLevel(int registerLevel) {
		this.registerLevel = registerLevel;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	private Doctor doctor;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	private long deptId;
	private String deptName = "";
	private int mainFlag = 0;
	private int minQueue;
	private int registerFee;
	// 联系人名字，方便医生与医院联系
	// 联系电话，方便医生与医院联系
	private String contactName = "";
	private String contactTele = "";

	// 该医院正在排队的数量人数
	// 此数量暂时动态统计，没有存入数据库中
	// 以后说不定要改成实时填写
	private int queueSize = 0;
	private int status = Status.SIGNED.getValue();
	private long creatorId = 0;// 录入人
	private Date createTime = new Date();
	/*
	 * 该医生在该医院的余票数量,非持久态，临时算出
	 */
	private int availableCount = 0;
	/**
	 * 在该医院的挂号状态,非持久态，临时算出 包括，0-未排队 ，1-已排队 ，2-可挂号 3-已挂号
	 */
	private int registerStatus = RegisterStatus.UNQUEUE.getValue();

	public int getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(int registerStatus) {
		this.registerStatus = registerStatus;
	}

	public int getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(int availableCount) {
		this.availableCount = availableCount;
	}

	public String getContactName() {
		return contactName;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTele() {
		return contactTele;
	}

	public void setContactTele(String contactTele) {
		this.contactTele = contactTele;
	}

	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}

	public long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(long hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public long getDeptId() {
		return deptId;
	}

	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getMainFlag() {
		return mainFlag;
	}

	public void setMainFlag(int mainFlag) {
		this.mainFlag = mainFlag;
	}

	public int getMinQueue() {
		return minQueue;
	}

	public void setMinQueue(int minQueue) {
		this.minQueue = minQueue;
	}

	public int getRegisterFee() {
		return registerFee;
	}

	public void setRegisterFee(int registerFee) {
		this.registerFee = registerFee;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static enum Status {
		CREATED(0, "创建"), SIGNED(1, "已生效"), CANCEL(2, "已取消");

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

	public static enum RegisterStatus {
		UNQUEUE(0, "未排队"), QUEUED(1, "已排队"), CANREGISTER(2, "可挂号"), REGISTERED(
				3, "已挂号");

		private int value;
		private String desc;

		RegisterStatus(int value, String desc) {
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
