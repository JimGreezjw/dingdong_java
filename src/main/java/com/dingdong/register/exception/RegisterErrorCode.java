package com.dingdong.register.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 挂号异常信息
 * 
 * @author chenliang
 * 
 */
public enum RegisterErrorCode implements ErrorCode {
	REGISTER_ID_NOT_EXIST(1, "该预约号不存在"), QUEUE_NOT_EXIST(2, "没有排队"), REGISTER_FAILED(
			3, "预约失败"), PATIENT_ID_NOT_EXIST(4, "病人id号不存在"), HOSPITAL_ID_NOT_EXIST(
			5, "医院id号不存在"), DOCTOR_HOSPITAL_NOT_EXIST(6, "医生执业医院信息不存在，无法排队或挂号"), QUEQUE_NOT_ALLOWED(
			7, "医生不允许挂号，或者缺少人数限制信息"), REGISTERNUM_EXCEED(8, "超出了医生允许的挂号数量"), MONEY_NOT_ENOUGH(
			9, "账户余额不足，请及时充值"), ALREADY_FINISH_TREAT(10, "该用户已经完成诊疗！"), UNFINISH_REGISTER_EXIST(
			11, "您还存在未完成诊疗！"), REGISTER_NOT_QUERE(12, "该排队已完成确认"), REGISTER_SCHEDULE_NOT_FOUND(
			13, "挂号确认时日程不存在"), ALREADY_EVALUATED(14, "您已经对本次诊疗评价过了！"), CANCEL_SCHEDULE_OVER_TIME_LITMIT(
			15, "该预约已经超过了取消时限！！");

	private int code;
	private String message;

	RegisterErrorCode(int code, String message) {
		this.code = ErrorCode.SUBCODE_REGISTER + code;
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
