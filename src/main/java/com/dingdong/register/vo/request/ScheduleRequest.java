package com.dingdong.register.vo.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 出诊日程安排
 * 
 * @author chenliang
 * @version 2015年12月13日 上午12:58:11
 */
public class ScheduleRequest {
	/**
	 * 医生编号
	 */
	private long doctorId;

	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}

	/** 出诊接受预约的预估数量 */
	private int issueNum;
	/** 出诊时间 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	private Date scheduleDate;
	/** 时间段 */
	private int timeSlot;
	/** 出诊医院id */
	private long hospitalId;

	public int getIssueNum() {
		return issueNum;
	}

	public void setIssueNum(int issueNum) {
		this.issueNum = issueNum;
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

	public long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(long hospitalId) {
		this.hospitalId = hospitalId;
	}
}
