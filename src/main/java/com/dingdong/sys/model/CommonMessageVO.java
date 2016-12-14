package com.dingdong.sys.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * 一般的短信
 * 
 * @author niukai
 * @created on December 27th, 2015
 * 
 */
@Alias("commonmessage")
public class CommonMessageVO {

	// 唯一号，自增长
	private long uid;
	// 信息类型
	private String msgType = "";
	// 信息状态
	private int msgState;
	// 手机号码
	private String mobileNo = "";
	// 用户id
	private long userId;
	// 病人的id
	private long patientId;
	// 医生id
	private long doctorId;
	// 信息内容
	private String content = "";
	// 发送时间
	private Date sendTime = new Date();

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public int getMsgState() {
		return msgState;
	}

	public void setMsgState(int msgState) {
		this.msgState = msgState;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
}
