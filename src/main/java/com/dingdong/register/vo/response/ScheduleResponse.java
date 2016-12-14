package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.Schedule;

/**
 * 预约信息
 * 
 * @author chenliang
 * @version 2015年12月13日 下午1:41:34
 */
public class ScheduleResponse extends ResponseBody {

	/* 预约信息 */
	private List<Schedule> schedules;

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

}
