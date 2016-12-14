package com.dingdong.sys.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 系统错误异常信息
 * 
 * @author chenliang
 * 
 */
public enum SysErrorCode implements ErrorCode {
	USER_ID_NOT_EXIST(1, "用户编号不存在"), PHONE_ID_NOT_EXIST(2, "电话号码未验证"), USER_PASSWORD_WRONG(
			3, "用户名或者密码错误"), OP_HOSPITAL_NOT_EXIST(4, "医院不存在"), ACCOUNT_NOT_EXIST(
			5, "账户不存在");
	;

	private int code;
	private String message;

	SysErrorCode(int code, String message) {
		this.code = ErrorCode.SUBCODE_SYS + code;
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
