package com.dingdong.register.service;

import java.util.Date;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.vo.request.WeekScheduleRequest;
import com.dingdong.register.vo.response.WeekScheduleResponse;

/**
 * 预约挂号服务
 * 
 * @author chenliang
 * @version 2015年12月13日 上午1:35:01
 */
public interface WeekScheduleService {
	/**
	 * 通过id获得schedule
	 * 
	 * @param id
	 * @return
	 */
	public WeekScheduleResponse findById(long id);

	/**
	 * 查找某一指定医院的调度日程记录
	 * 
	 * @param doctorId
	 * @param hospitalId
	 * @return
	 */
	public WeekScheduleResponse findByDoctorIdHospitalId(long doctorId,
			Long hospitalId);

	/**
	 * 医生每周行程
	 * 
	 * @param doctorId
	 * @param hospitalId
	 * @param day
	 * @param timeSlot
	 * @param startTime
	 * @param endTime
	 * @param issueNum
	 * @param creatorId
	 * @return
	 */
	public WeekScheduleResponse add(long doctorId, long hospitalId, int day,
			int timeSlot, Date startTime, Date endTime, int issueNum,
			long creatorId);

	/**
	 * 更新日程
	 * 
	 * @param id
	 * @param day
	 * @param timeSlot
	 * @param startTime
	 * @param endTime
	 * @param issueNum
	 * @param creatorId
	 * @return
	 */
	public WeekScheduleResponse update(long id, Integer day, Integer timeSlot,
			Date startTime, Date endTime, Integer issueNum, Long creatorId);

	/**
	 * 删除日程信息
	 * 
	 * @param request
	 * @return
	 */
	public ResponseBody deleteById(long id);

	/**
	 * 定时产生日程
	 */
	public void autoCreateSchedule();

	public WeekScheduleResponse addWeekSchedule(WeekScheduleRequest request);

}
