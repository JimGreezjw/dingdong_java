package com.dingdong.register.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 粉丝异常信息,包括各种粉丝异常
 * 
 * @author lqm
 * 
 */
public enum FansErrorCode implements ErrorCode {
	ALREADY_FOCUS_DOCTOR(1, "患者已经关注该医生");

	private int code;
	private String message;

	FansErrorCode(int code, String message) {
		this.code = ErrorCode.SUBCODE_FANS + code;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public Enum<? extends ErrorCode> getName() {
		return this;
	}

}
