package com.dingdong.register.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 
 * @author chenliang
 * @version 2015年12月14日 下午11:55:55
 */
public enum QueueErrorCode implements ErrorCode {
	QUEUE_NOT_FOUND(1, "找不到排队信息"), QUEUE_CANNOT_CANCEL(2, "排队已完成，无法取消"), QUEUE_ALREADY_EXIST(
			3, "您已经已排过队了！"), QUEUE_EXCEPTION(4, "排队异常或者排队人数过多");

	private int code;
	private String desc;

	QueueErrorCode(int code, String desc) {
		this.code = ErrorCode.SUBCODE_QUEUE + code;
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
