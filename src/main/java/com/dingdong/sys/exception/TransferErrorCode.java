package com.dingdong.sys.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 转账异常信息
 * 
 * @author chenliang
 * 
 */
public enum TransferErrorCode implements ErrorCode {
	TRANSFER_BALANCE_INSUFFICIENT(1, "账户余额不足"), TRANSFER_GETCASH_TOO_MUCH(2,
			"取现超过指定金额"), TRANSFER_GETCASH_ALREADY_TODAY(3, "当日取现次数已达上限");

	private int code;
	private String message;

	TransferErrorCode(int code, String message) {
		this.code = ErrorCode.SUBCODE_TRANSFER + code;
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
