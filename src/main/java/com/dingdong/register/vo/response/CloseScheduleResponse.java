package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.CloseSchedule;

/**
 * 预约信息
 * 
 * @author chenliang
 * @version 2015年12月13日 下午1:41:34
 */
public class CloseScheduleResponse extends ResponseBody {

	/* 预约信息 */
	private List<CloseSchedule> closeSchedules;

	public List<CloseSchedule> getCloseSchedules() {
		return closeSchedules;
	}

	public void setCloseSchedules(List<CloseSchedule> closeSchedules) {
		this.closeSchedules = closeSchedules;
	}

}
