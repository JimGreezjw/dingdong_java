package com.dingdong.register.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户账户信息
 * 
 * @author yushansoft
 * 
 */
@Alias("account")
public class Account extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2924866319161150745L;
	// 0-草稿 ，1-上架, 2-已删除
	private int status = Status.EFFECTIVE.getValue();// 缺省生效
	// 用户编号
	private long userId;

	private String bankName = "";
	private String bankBranchName = "";
	private String bankAddress = "";
	private String cardNumber = "";
	private String cardName = "";

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime = new Date();

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
