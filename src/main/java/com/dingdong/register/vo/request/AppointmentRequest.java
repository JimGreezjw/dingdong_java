package com.dingdong.register.vo.request;

/**
 * dingdong预约请求
 * @author chenliang
 * @version 2015年12月13日 上午12:06:11
 */
public class AppointmentRequest {

	/** 日程id */
	private long scheduleId;
	
	public long getScheduleId() {
        return scheduleId;
    }
    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
