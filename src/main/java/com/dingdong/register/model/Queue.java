//package com.dingdong.register.model;
//
//import java.util.Date;
//
//import org.apache.ibatis.type.Alias;
//
//import com.dingdong.common.model.IdEntity;
//import com.fasterxml.jackson.annotation.JsonFormat;
//
///**
// * 排队实体类
// * 
// * @author chenliang
// * @version 2015年12月14日 下午10:50:44
// */
//@Alias("queue")
//public class Queue extends IdEntity {
//
//	private static final long serialVersionUID = -4437136463158172899L;
//	/**
//	 * 客户id
//	 */
//	private long userId;
//	/**
//	 * 客户名称
//	 */
//	private String userName;
//	/**
//	 * 名医id
//	 */
//	private long doctorId;
//
//	private String doctorName;
//	/**
//	 * 医院id
//	 */
//	private long hospitalId;
//
//	private String hospitalName;
//
//	private int queueNum;
//
//	public String getDoctorName() {
//		return doctorName;
//	}
//
//	public int getQueueNum() {
//		return queueNum;
//	}
//
//	public void setQueueNum(int queueNum) {
//		this.queueNum = queueNum;
//	}
//
//	public void setDoctorName(String doctorName) {
//		this.doctorName = doctorName;
//	}
//
//	public String getHospitalName() {
//		return hospitalName;
//	}
//
//	public void setHospitalName(String hospitalName) {
//		this.hospitalName = hospitalName;
//	}
//
//	/**
//	 * 可接受预约的时间
//	 */
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
//	private Date appointmentTime = new Date();
//	/**
//	 * 排队状态，0-排队中，1-已完成，2-失败，3-取消
//	 */
//	private int status;
//
//	/**
//	 * 排队信息添加时间
//	 */
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GTM+08:00")
//	private Date createTime = new Date();
//
//	public long getUserId() {
//		return userId;
//	}
//
//	public Queue setUserId(long userId) {
//		this.userId = userId;
//		return this;
//	}
//
//	public String getUserName() {
//		return userName;
//	}
//
//	public Queue setUserName(String userName) {
//		this.userName = userName;
//		return this;
//	}
//
//	public long getDoctorId() {
//		return doctorId;
//	}
//
//	public Queue setDoctorId(long doctorId) {
//		this.doctorId = doctorId;
//		return this;
//	}
//
//	public long getHospitalId() {
//		return hospitalId;
//	}
//
//	public Queue setHospitalId(long hospitalId) {
//		this.hospitalId = hospitalId;
//		return this;
//	}
//
//	public Date getAppointmentTime() {
//		return appointmentTime;
//	}
//
//	public Queue setAppointmentTime(Date appointmentTime) {
//		this.appointmentTime = appointmentTime;
//		return this;
//	}
//
//	public int getStatus() {
//		return status;
//	}
//
//	public Queue setStatus(int status) {
//		this.status = status;
//		return this;
//	}
//
//	public Date getCreateTime() {
//		return createTime;
//	}
//
//	public Queue setCreateTime(Date createTime) {
//		this.createTime = createTime;
//		return this;
//
//	}
//
//	public static enum Status {
//		WAITING(0, "排队中"), SUCCESS(1, "排队成功，顺利预约"), FAIL(2, "排队失败"), CANCEL(3,
//				"取消排队"), UNPAYED_POSTPONE(4, "未付费延迟"), PAYED_POSTPONE(5, "付费延迟");
//		
//		private int status;
//		private String desc;
//
//		Status(int status, String desc) {
//			this.status = status;
//			this.desc = desc;
//		}
//
//		public int getStatus() {
//			return status;
//		}
//
//		public String getDesc() {
//			return desc;
//		}
//	}
// }
