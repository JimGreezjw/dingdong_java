package com.dingdong.register.vo.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 医生的日期预约信息，每一天的日程信息
 * 
 * @author lqm
 * @version 2015年12月27日 下午1:41:34
 */
public class ScheduleDateResponse extends ResponseBody {
	public static enum DATE_STATUS {
		YES(1, "可预约"), FULL(2, "预约满"), TEMP_NO(3, "不出诊"), NO(4, "不可预约");
		private int status;
		private String desc;

		DATE_STATUS(int status, String desc) {
			this.status = status;
			this.desc = desc;
		}

		public int getStatus() {
			return status;
		}

		public String getDesc() {
			return desc;
		}
	}

	public class ScheduleDate implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8261848313636263604L;
		// 挂号日期
		@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
		Date scheduleDate;

		public Date getScheduleDate() {
			return scheduleDate;
		}

		public void setScheduleDate(Date scheduleDate) {
			this.scheduleDate = scheduleDate;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		Integer status = 4;

		public ScheduleDate(Date scheduleDate, int status) {
			this.scheduleDate = scheduleDate;
			this.status = status;

		}

	}

	/* 预约信息 */
	private List<ScheduleDate> schduleDateList;

	public List<ScheduleDate> getSchduleDateList() {
		return schduleDateList;
	}

	public void setSchduleDateList(List<ScheduleDate> schduleDateList) {
		this.schduleDateList = schduleDateList;
	}

}
