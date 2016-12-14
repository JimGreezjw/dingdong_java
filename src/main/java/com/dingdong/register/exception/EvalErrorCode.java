package com.dingdong.register.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 评价异常信息
 * 
 * @author lqm
 * 
 */
public enum EvalErrorCode implements ErrorCode {
	ALREADY_FOCUS_DOCTOR(1, "您无法评价");

	private int code;
	private String message;

	EvalErrorCode(int code, String message) {
		this.code = ErrorCode.SUBCODE_EVAL + code;
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
