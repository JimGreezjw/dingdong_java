package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.WeekSchedule;

/**
 * 预约信息
 * 
 * @author chenliang
 * @version 2015年12月13日 下午1:41:34
 */
public class WeekScheduleResponse extends ResponseBody {

	/* 预约信息 */
	private List<WeekSchedule> weekSchedules;

	public List<WeekSchedule> getWeekSchedules() {
		return weekSchedules;
	}

	public void setWeekSchedules(List<WeekSchedule> weekSchedules) {
		this.weekSchedules = weekSchedules;
	}
}
