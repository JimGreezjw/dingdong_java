package com.dingdong.sys.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;
import com.dingdong.register.model.Account;
import com.fasterxml.jackson.annotation.JsonFormat;

@Alias("transfer")
public class Transfer extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 481377697449749388L;
	private long fromUserId;
	// 默认系统用 "叮咚门诊"
	private String fromUserName = "微信支付";

	private long userId;
	private String userName;

	public long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	private float amount;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	private Date createTime = new Date();

	private int type = Type.BALANCE.getId();
	// 0--草稿， 1—成功
	private int status = Status.DRAFT.getId();

	// 支付系统返回的流水号
	private String transactionId = "";

	// 转账原因
	private int reason = Reason.YHCZ.getId();

	// 提现账户
	private long accountId = 0;

	private Account account;

	public static final float MAX_CASH = 1000;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getReasonDesc() {

		for (Reason reason : Reason.values()) {
			if (reason.id == this.reason)
				return reason.getDesc();
		}
		return null;
	}

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}

	// 交易完成时间
	private Date transactionTime = new Date();

	public Date getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public static enum Type {
		BALANCE(0, "余额转账"), SCORE(1, "积分转账"), CASH(2, "余额提现");
		private int id;
		private String desc;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		Type(int id, String desc) {
			this.id = id;
			this.desc = desc;
		}

	}

	public static enum Reason {
		YHCZ(0, "用户充值"), GHCYZ(1, "挂号诚意金"), ZHTK(2, "诊疗后退款"), ZLJF(3, "医生诊疗后积分"), YFPJJF(
				4, "用户评价后积分"), YSXFJF(5, "医生吸收新粉积分"), YHTX(6, "用户提现"), QXPDTK(
				7, "取消排队退款"), QXGHTK(8, "取消挂号退款"), QXGHKCJF(9, "取消挂号扣除");
		private int id;
		private String desc;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		Reason(int id, String desc) {
			this.id = id;
			this.desc = desc;
		}

	}

	public static enum Status {
		DRAFT(0, "申请转账"), SUCCESS(1, "转账成功");
		private int id;
		private String desc;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		Status(int id, String desc) {
			this.id = id;
			this.desc = desc;
		}

	}
}
