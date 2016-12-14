package com.dingdong.register.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 医生停诊信息
 * 
 * @author yushansoft
 * 
 */
@Alias("closeSchedule")
public class CloseSchedule extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2924866319161150745L;
	// 0-草稿 ，1-上架, 2-已删除
	private int status = Status.EFFECTIVE.getValue();// 缺省生效
	// 医生编号
	private long weekScheduleId;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	private Date fromDate = new Date();
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	private Date toDate = new Date();
	/**
	 * 原因
	 */
	private String reason = "";

	private WeekSchedule weekSchedule;

	public WeekSchedule getWeekSchedule() {
		return weekSchedule;
	}

	public void setWeekSchedule(WeekSchedule weekSchedule) {
		this.weekSchedule = weekSchedule;
	}

	public long getWeekScheduleId() {
		return weekScheduleId;
	}

	public void setWeekScheduleId(long weekScheduleId) {
		this.weekScheduleId = weekScheduleId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	// 创建时间
	private long creatorId = 0;
	/**
	 * 日程创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime = new Date();

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static enum Status {
		DRAFT(0, "草稿"), EFFECTIVE(1, "生效"), CANCEL(2, "失效");

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

}
