package com.dingdong.register.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;
import com.dingdong.register.model.Schedule.TimeSlot;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author baidu
 * 
 */
@Alias("register")
public class Register extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7225992738034723245L;
	// 序号，正式确认的打印出的挂号编号
	private int seq = 0;

	private int status = Status.DRAFT.getValue();

	public String getTimeSlotDesc() {
		for (TimeSlot ts : TimeSlot.values()) {
			if (ts.getValue() == timeSlot)
				return ts.getDesc();
		}
		return null;
	}

	public String getStatustDesc() {
		for (Status ts : Status.values()) {
			if (ts.getValue() == timeSlot)
				return ts.getDesc();
		}
		return null;
	}

	// 用户唯一号
	private long userId = 0;
	// 用户名称
	private String userName = "";

	// 病人唯一号
	private long patientId = 0;
	// 病人名称
	private String patientName = "";
	/**
	 * 患者信息
	 */
	private Patient patient;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	// 日程唯一号
	private long scheduleId = 0;

	// 医生编号
	private long doctorId = 0;
	private String doctorName = "";
	// 医院编号
	private long hospitalId = 0;
	private String hospitalName = "";
	// 挂号日期
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	private Date scheduleDate = new Date();
	// 时段 ，0-全天，1-上午，2-下午，3-晚上
	private int timeSlot = TimeSlot.ALL.getValue();

	// 加入病情描述等信息
	// 是否复查
	private String revisit = "N";
	// 症状描述
	private String phenomenon = "";
	// 附件编号
	private String attachNo = "";

	// 显示的图像链接地址
	private String attachUrls = "";

	// 医院预约号
	private String hospitalRegisterId = "";

	public String getHospitalRegisterId() {
		return hospitalRegisterId;
	}

	public void setHospitalRegisterId(String hospitalRegisterId) {
		this.hospitalRegisterId = hospitalRegisterId;
	}

	public String getAttachUrls() {
		return attachUrls;
	}

	public void setAttachUrls(String attachUrls) {
		this.attachUrls = attachUrls;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date registerTime = new Date();// 医院正式挂号时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime = new Date();// 记录添加时间，网上预约成功时间
	// 记录超时时间，超时后自动更改状态
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date expireTime = new Date();
	// 本次挂号的诚意金
	private int deposit = 0;

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRevisit() {
		return revisit;
	}

	public void setRevisit(String revisit) {
		this.revisit = revisit;
	}

	public String getPhenomenon() {
		return phenomenon;
	}

	public void setPhenomenon(String phenomenon) {
		this.phenomenon = phenomenon;
	}

	public String getAttachNo() {
		return attachNo;
	}

	public void setAttachNo(String attachNo) {
		this.attachNo = attachNo;
	}

	// end

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
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

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public int getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public static enum Status {
		QUEUE(-1, "排队中"), // 由排队刚转成的状态
		DRAFT(0, "待确认"), // 由排队刚转成的状态,基本废除了
		SUCCESS(1, "已预约"), // 在叮咚中完成签约挂号
		SIGNED(2, "已挂号"), // 在落地医院现场挂号
		TREATED(3, "已诊疗"), // 在落地医院完成诊疗
		CANCEL(4, "已取消"), EVALUATED(5, "已评价");// 已评价

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
