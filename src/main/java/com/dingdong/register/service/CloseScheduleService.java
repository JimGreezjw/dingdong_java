package com.dingdong.register.service;

import java.util.Date;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.vo.response.CloseScheduleResponse;

/**
 * 预约挂号服务
 * 
 * @author chenliang
 * @version 2015年12月13日 上午1:35:01
 */
public interface CloseScheduleService {
	/**
	 * 通过id获得schedule
	 * 
	 * @param id
	 * @return
	 */
	public CloseScheduleResponse findById(long id);

	/**
	 * 查找某一指定医院的调度停诊记录
	 * 
	 * @param weekScheduleId
	 * @return
	 */
	public CloseScheduleResponse findByWeekScheduleId(long weekScheduleId);

	/**
	 * 
	 * @param weekScheduleId
	 * @param status
	 * @param fromDate
	 * @param toDate
	 * @param reason
	 * @param creatorId
	 * @return
	 */
	public CloseScheduleResponse add(long weekScheduleId, Integer status,
			Date fromDate, Date toDate, String reason, Long creatorId);

	/**
	 * 
	 * @param id
	 * @param status
	 * @param fromDate
	 * @param toDate
	 * @param reason
	 * @param creatorId
	 * @return
	 */
	public CloseScheduleResponse update(long id, Integer status, Date fromDate,
			Date toDate, String reason, Long creatorId);

	/**
	 * 删除停诊信息
	 * 
	 * @param request
	 * @return
	 */
	public ResponseBody deleteById(long id);

}
