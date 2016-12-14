package com.dingdong.register.model;

import java.sql.Time;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

@Alias("schedule")
public class Schedule extends IdEntity {
	private static final long serialVersionUID = 8694982276937977681L;

	// 0-草稿 ，1-上架, 2-已完成， 3-延迟，4-删除
	private int status = Status.EFFECTIVE.getValue();
	// 医生编号
	private long doctorId;
	private String doctorName;
	// 医院编号
	private long hospitalId;
	private String hospitalName;

	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date startTime = Time.valueOf("00:00:00");

	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date endTime = Time.valueOf("23:59:00");

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	// 挂号日期
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	private Date scheduleDate;

	// 时段 ，0-全天，1-上午，2-下午，3-晚上
	private int timeSlot = -1;// 缺省不设置值

	public String getTimeSlotDesc() {
		for (TimeSlot ts : TimeSlot.values()) {
			if (ts.getValue() == timeSlot)
				return ts.desc;
		}
		return null;
	}

	// 发布允许挂号数量
	private int issueNum = 0;
	// 已挂号数量
	private int registeredNum = 0;
	/**
	 * 日程创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime = new Date();

	private long creatorId = 0;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getIssueNum() {
		return issueNum;
	}

	public void setIssueNum(int issueNum) {
		this.issueNum = issueNum;
	}

	public int getRegisteredNum() {
		return registeredNum;
	}

	public void setRegisteredNum(int registeredNum) {
		this.registeredNum = registeredNum;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public static enum Status {
		DRAFT(0, "草稿"), EFFECTIVE(1, "上架生效"), COMPLETE(2, "完成"), POSTPONE(3,
				"延迟"), CANCEL(4, "下架");

		private int value;
		private String desc;

		Status(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}
	}

	public static enum TimeSlot {
		ALL(0, "全天"), AM(1, "上午"), PM(2, "下午"), NIGHT(3, "晚上");

		private int value;
		private String desc;

		TimeSlot(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public String getDesc() {
			return desc;
		}

	}
}
