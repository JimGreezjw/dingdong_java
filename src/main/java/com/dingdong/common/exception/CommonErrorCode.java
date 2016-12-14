package com.dingdong.common.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 
 * @author chenliang
 * @version 2015年12月13日 上午2:45:48
 */
public enum CommonErrorCode implements ErrorCode {
	OK(1, "OK"), UNKNOWN_EXCEPTION(2, "异常错误，请稍后重试！");

	private int code;
	private String desc;

	CommonErrorCode(int code, String desc) {
		this.code = ErrorCode.SUBCODE_COMMON + code;
		this.desc = desc;
	}

	@Override
	public String getMessage() {
		return this.desc;
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
