package com.dingdong.register.exception;

import com.dingdong.core.exception.ErrorCode;

/**
 * 日程错误信息
 * 
 * @author chenliang
 * @version 2015年12月16日 上午12:33:30
 */
public enum ScheduleErrorCode implements ErrorCode {
	SCHEDULE_NOT_FOUND(0, "未找到日程"), NOT_HAS_REGISTER(1, "未找到挂号信息"), ALREADY_FINISH_REGISTER(
			2, "已经完成挂号"), ALREADY_HAS_REGISTERMSG(3, "已经存在挂号信息,请及时确认"), DATE_ALREADY_SCHEDULE(
			4, "该时间段已存在预约日程"), UNABLE_CANCEL(5, "当前挂号状态无法取消"), UNABLE_ACTIVE(6,
			"当前状态无法激活"), UNREACH_TIME(7, "未到可激活时间"), UNABLE_FINISH(8,
			"当前日程无法设置为完成"), UNABLE_POSTPONE(9, "当前状态无法延迟"), SCHEDULE_TOO_EARLY(
			10, "日程设置的时间过早"), WEEK_SCHEDULE_NOT_FOUND(11, "未找到相关周日程"), CLOSE_SCHEDULE_NOT_FOUND(
			12, "未找到相关停诊信息"), SCHEDULE_DATE_EMPTY(13, "未找到排班的日期信息"), SCHEDULE_CANNOT_DELETE(
			14, "无法删除医生日程，可能存在未完成预约挂号！");

	private int code;
	private String desc;

	ScheduleErrorCode(int code, String desc) {
		this.code = ErrorCode.SUBCODE_SCHEDULE + code;
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
