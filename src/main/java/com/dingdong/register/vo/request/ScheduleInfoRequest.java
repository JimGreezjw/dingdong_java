package com.dingdong.register.vo.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author chenliang
 * @version 2015年12月20日 上午11:22:20
 */
public class ScheduleInfoRequest {

	/**
	 * 医院id
	 */
	private long hospitalId;
	/**
	 * 开始日期
	 */

	private Date scheduleBeginDate;

	/**
	 * 结束日期
	 */

	private Date scheduleEndDate;

	public Date getScheduleBeginDate() {
		return scheduleBeginDate;
	}

	public void setScheduleBeginDate(Date scheduleBeginDate) {
		this.scheduleBeginDate = scheduleBeginDate;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	public Date getScheduleEndDate() {
		return scheduleEndDate;
	}

	public void setScheduleEndDate(Date scheduleEndDate) {
		this.scheduleEndDate = scheduleEndDate;
	}

	public long getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(long hospitalId) {
		this.hospitalId = hospitalId;
	}

}
