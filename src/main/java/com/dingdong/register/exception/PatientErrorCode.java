package com.dingdong.register.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 评价异常信息
 * 
 * @author lqm
 * 
 */
public enum PatientErrorCode implements ErrorCode {
	PATIENT_ID_NOT_EXIST(1, "患者id号不存在");

	private int code;
	private String message;

	PatientErrorCode(int code, String message) {
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
