package com.dingdong.register.vo.request;

import java.util.List;

import com.dingdong.common.vo.RequestBody;
import com.dingdong.register.model.WeekSchedule;

public class WeekScheduleRequest extends RequestBody {

	private List<WeekSchedule> weekSchedules;

	public List<WeekSchedule> getWeekSchedules() {
		return weekSchedules;
	}

	public WeekScheduleRequest setWeekSchedules(List<WeekSchedule> weekSchedules) {
		this.weekSchedules = weekSchedules;
		return this;
	}
}
