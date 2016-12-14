package com.dingdong.register.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 挂号异常信息
 * 
 * @author chenliang
 * 
 */
public enum DoctorErrorCode implements ErrorCode {
	DOCTOR_ID_NOT_EXIST(1, "医生id号不存在"), DOCTOR_HOSPITAL_ALREADY_EXIST(2,
			"医生执业医院信息已存在，无法添加"), DOCTOR_HOSPITAL_NOT_EXIST(3,
			"医生执业医院信息不存在，无法排队或挂号"), HOSTPITAL_ID_NOT_EXIST(4, "不正确的医院编号"), DOCTOR_HOSTPITAL_CANNOT_DELETE(
			5, "存在未完成排班日程，不能删除此执业医院"), DOCTOR_SUBMIT_VALID(6,
			"您已经提交了核对申请，请耐心等待！"), DOCTOR_USER_ALREADY_SIGNED(7,
			"您已经完成了医生核对。如有误，请与客服联系！"), DOCTOR_ALREADY_EXISTS(8,
			"该医生已存在，请勿重复录入。如有误，请与客服联系！"), DOCTOR_INTRODUCE_EXCEED(9,
			"医生简介超过允许长度！");

	private int code;
	private String message;

	DoctorErrorCode(int code, String message) {
		this.code = ErrorCode.SUBCODE_DOCTOR + code;
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
