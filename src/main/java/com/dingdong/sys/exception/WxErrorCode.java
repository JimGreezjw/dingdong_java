package com.dingdong.sys.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 挂号异常信息
 * 
 * @author chenliang
 * 
 */
public enum WxErrorCode implements ErrorCode {
	OPEN_ID_NOT_EXIST(1, "用户公众号OPENID不存在"), EMPTY_PREPAY_ID(2, "无法获取PrePayId"), INVALID_XML(
			3, "不正确的XML格式，无法读取XML节点");

	private int code;
	private String message;

	WxErrorCode(int code, String message) {
		this.code = ErrorCode.SUBCODE_WX + code;
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
