package com.dingdong.sys.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 令牌异常信息
 * 
 * @author chenliang
 * 
 */
public enum TokenErrorCode implements ErrorCode {
	TOKEN_INFORMATION_INCOMPLETE(1, "令牌信息不完整！"), TOKEN_INFORMATION_ERROR(2,
			"令牌信息错误！");

	private int code;
	private String message;

	TokenErrorCode(int code, String message) {
		this.code = ErrorCode.SUBCODE_TOKEN + code;
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
