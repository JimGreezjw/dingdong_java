package com.dingdong.sys.model;

import java.util.Date;

import com.dingdong.common.model.IdEntity;

public class Token extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3192980354101041812L;

	private String tokenId;
	private long userId;
	private String userAgent;
	private Date deadline;
	private Date createTime;
	// 1为有效，2为失效
	private Integer status = 1;

	private User user;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
