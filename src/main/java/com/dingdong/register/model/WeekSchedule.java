package com.dingdong.register.model;

import java.sql.Time;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

@Alias("weekSchedule")
public class WeekSchedule extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2475726124632641964L;
	// 0-草稿 ，1-上架, 2-已完成， 3-延迟，4-删除
	private int status = Status.EFFECTIVE.getValue();// 缺省生效
	// 医生编号
	private long doctorId;
	private String doctorName;
	// 医院编号
	private long hospitalId;
	private String hospitalName;

	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date startTime = Time.valueOf("00:00:00");

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

	@JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
	private Date endTime = Time.valueOf("23:59:00");

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

	// 周几，0表示星期 天
	private int day = 0;

	public String getDayDesc() {
		for (Day d : Day.values()) {
			if (d.getValue() == day)
				return d.desc;
		}
		return null;
	}

	// 时段 ，0-全天，1-上午，2-下午，3-晚上
	private int timeSlot = TimeSlot.ALL.getValue();

	public String getTimeSlotDesc() {
		for (TimeSlot ts : TimeSlot.values()) {
			if (ts.getValue() == timeSlot)
				return ts.desc;
		}
		return null;
	}

	// 发布允许挂号数量
	private int issueNum = 0;
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
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

	public static enum Day {
		SUN(0, "周日"), MON(1, "周一"), TUES(2, "周二"), WED(3, "周三"), THU(4, "周四"), FRI(
				5, "周五"), SAT(6, "周六");

		private int value;
		private String desc;

		Day(int value, String desc) {
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
