package com.dingdong.register.service;

import java.util.Date;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.model.Schedule;
import com.dingdong.register.vo.request.ScheduleRequest;
import com.dingdong.register.vo.response.ScheduleDateResponse;
import com.dingdong.register.vo.response.ScheduleResponse;

/**
 * 预约挂号服务
 * 
 * @author chenliang
 * @version 2015年12月13日 上午1:35:01
 */
public interface ScheduleService {
	/**
	 * 通过id获得schedule
	 * 
	 * @param id
	 * @return
	 */
	public Schedule findById(long id);

	/**
	 * 获得医生的日程信息，包含草稿状态
	 * <p>
	 * niukai 2016-02-21
	 * </p>
	 * 
	 * @param doctorId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public ScheduleResponse getDoctorSchedule(long doctorId, Date beginDate,
			Date endDate);

	/**
	 * 获得医生的日程信息
	 * 
	 * @param doctorId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public ScheduleResponse getDoctorScheduleWithDraft(long doctorId,
			Date beginDate, Date endDate);

	/**
	 * 查找某一指定医院一段时间的调度日程记录
	 * 
	 * @param doctorId
	 * @param hospitalId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public ScheduleResponse findByDoctorIdHospitalIdBeginDateEndDate(
			long doctorId, Long hospitalId, Date beginDate, Date endDate);

	/**
	 * 医生确定出诊日程，确定日程后，客户可以查看日程信息直接预约
	 * 
	 * @param request
	 *            出诊日程请求
	 * @return
	 */
	public ScheduleResponse createSchedule(ScheduleRequest request);

	/**
	 * 医生创建出诊日程
	 * 
	 * @param doctorId
	 * @param hospitalId
	 * @param scheduleDate
	 * @param timeSlot
	 * @param startTime
	 * @param endTime
	 * @param issueNum
	 * @param creatorId
	 * @return
	 */
	public ScheduleResponse createSchedule(long doctorId, long hospitalId,
			Date scheduleDate, int timeSlot, Date startTime, Date endTime,
			int issueNum, long creatorId);

	// /**
	// * 根据提前期激活医生创建的日程
	// *
	// * @return
	// */
	// public void activeSchedule();

	/**
	 * 激活一个特定的日程
	 * 
	 * @param scheduleId
	 * @return
	 */
	public ScheduleResponse activeSpecificSchedule(long scheduleId);

	/**
	 * 扫描未预约满的日程，并通知排队者
	 */
	// public void scanUnFullSchedule();

	/**
	 * 更新日程信息
	 * 
	 * @param request
	 *            更新日程信息
	 * @return
	 */
	public ResponseBody updateSchedule(ScheduleRequest request);

	/**
	 * 删除日程信息
	 * 
	 * @param creatorId
	 * @param endTime
	 * @param startTime
	 * 
	 * @param request
	 * @return
	 */
	public ResponseBody updateScheduleInfo(long id, Date scheduleDate,
			Integer timeSlot, Date startTime, Date endTime, Integer issueNum,
			Long creatorId);

	/**
	 * 将日程设置为完成
	 * 
	 * @param id
	 * @return
	 */
	public ResponseBody finishSchedule(long id);

	/**
	 * 延迟日程
	 * 
	 * @param id
	 * @return
	 */
	public ResponseBody postponeSchedule(long id);

	/**
	 * 延迟日程到特定的日期
	 * 
	 * @param id
	 * @param newScheduleDate
	 * @return
	 */
	public ResponseBody postponeSchedule(long id, Date newScheduleDate);

	/**
	 * 删除日程信息
	 * 
	 * @param request
	 * @return
	 */
	public ResponseBody deleteSchedule(long id);

	/**
	 * 获得医生的日程信息,显示每天的日程信息
	 * 
	 * @param doctorId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public ScheduleDateResponse getDoctorDateSchedule(long doctorId,
			Date beginDate, Date endDate);

	/**
	 * 取得医生在某医院按日统计的日程信息
	 * 
	 * @param doctorId
	 * @param hospitalId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	ScheduleDateResponse getDoctorHospitalDateSchedule(long doctorId,
			long hospitalId, Date beginDate, Date endDate);

	/**
	 * 创建一个日程
	 * 
	 * @param schedule
	 * @return
	 */
	void createSchedule(Schedule schedule);

	public ScheduleResponse getSchedulesWithPage(long doctorId,
			long hospitalId, Date beginDate, Date endDate,
			DDPageInfo<Schedule> pageInfo);
}
