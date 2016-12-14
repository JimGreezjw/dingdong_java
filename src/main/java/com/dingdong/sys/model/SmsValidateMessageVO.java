package com.dingdong.sys.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * 验证码消息，以手机号码作为检索条件
 * <p>
 * 如果给手机号码发送过验证码，则直接进行更新，不再创建一条新的记录
 * </p>
 * 
 * @author niukai
 * @created on December 13rd, 2015
 * 
 */
@Alias("valimessage")
public class SmsValidateMessageVO {
	// 唯一号
	private long uid;
	// 手机号码
	private String mobileNo;
	// 验证码
	private String validateCode;
	// 发送时间
	private Date sendTime;
	// 失效事件
	private Date expireTime;
	// 创建时间
	private Date creationTime;
	// 修改时间
	private Date modifyTime;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}
