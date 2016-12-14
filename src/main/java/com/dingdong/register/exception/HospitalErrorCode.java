package com.dingdong.register.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 医院异常信息
 * 
 * @author lqm
 * 
 */
public enum HospitalErrorCode implements ErrorCode {
	OP_TELE_ALREADY_EXIST(1, "运维电话号码已存在！"), HOSPITAL_NOT_EXIST(2, "未找到相关医院！"), HOSPITAL_INTRODUCE_EXCEED(
			3, "医院简介超过允许长度！");

	private int code;
	private String message;

	HospitalErrorCode(int code, String message) {
		this.code = ErrorCode.SUBCODE_HOSPITAL + code;
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
