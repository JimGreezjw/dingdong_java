package com.dingdong.core.exception;

/**
 * 
 * @author chenliang
 * 
 */
public interface ErrorCode {

	/* 通用业务代码 */
	public int SUBCODE_COMMON = 0;
	/* 医生业务错误代码 */
	public int SUBCODE_SYS = 100;
	/* 服务内部错误 */
	public int SUBCODE_INTERNAL_ERROR = 200;
	/* 预约错误代码 */
	public int SUBCODE_REGISTER = 300;
	/* 日程错误代码 */
	public int SUBCODE_SCHEDULE = 400;
	/* 排队错误代码 */
	public int SUBCODE_QUEUE = 500;

	/* 微信错误代码 */
	public int SUBCODE_WX = 600;

	/* 各种粉丝关注的错误代码 */
	public int SUBCODE_FANS = 700;

	/* 医生业务错误代码 */
	public int SUBCODE_DOCTOR = 800;

	/**
	 * 评价系统
	 */
	public int SUBCODE_EVAL = 900;

	/* 患者业务错误代码 */
	public int SUBCODE_PATIENT = 1000;

	/* 账户业务错误代码 */
	public int SUBCODE_TRANSFER = 2000;

	/* 医院错误代码 */
	public int SUBCODE_HOSPITAL = 3000;

	/* 医院错误代码 */
	public int SUBCODE_TOKEN = 4000;

	/**
	 * 获得异常信息描述
	 * 
	 * @return
	 */
	public String getMessage();

	/**
	 * 获得异常代码
	 * 
	 * @return
	 */
	public int getCode();

	/**
	 * 获得异常枚举类值
	 * 
	 * @return
	 */
	public Enum<? extends ErrorCode> getName();
}
