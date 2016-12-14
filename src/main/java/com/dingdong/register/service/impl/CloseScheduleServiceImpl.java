package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.exception.ScheduleErrorCode;
import com.dingdong.register.mapper.CloseScheduleMapper;
import com.dingdong.register.mapper.WeekScheduleMapper;
import com.dingdong.register.model.CloseSchedule;
import com.dingdong.register.model.WeekSchedule;
import com.dingdong.register.service.CloseScheduleService;
import com.dingdong.register.vo.response.CloseScheduleResponse;

/**
 * 
 * @author yushansoft
 * 
 */
@Service("closeCloseScheduleService")
@Transactional
public class CloseScheduleServiceImpl implements CloseScheduleService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(CloseScheduleServiceImpl.class);

	@Autowired
	private CloseScheduleMapper closeScheduleMapper;

	@Autowired
	private WeekScheduleMapper weekScheduleMapper;

	@Override
	public CloseScheduleResponse add(long weekScheduleId, Integer status,
			Date fromDate, Date toDate, String reason, Long creatorId) {
		CloseScheduleResponse response = new CloseScheduleResponse();

		// end

		CloseSchedule closeSchedule = new CloseSchedule();

		closeSchedule.setWeekScheduleId(weekScheduleId);
		if (status != null)
			closeSchedule.setStatus(status);
		closeSchedule.setFromDate(fromDate);
		closeSchedule.setToDate(toDate);
		if (reason != null)
			closeSchedule.setReason(reason);
		closeSchedule.setCreatorId(creatorId);
		this.closeScheduleMapper.add(closeSchedule);

		// 如果已经到了预约的时间，则通知患者进行预约

		List<CloseSchedule> scheduleList = new ArrayList<CloseSchedule>();
		scheduleList.add(closeSchedule);
		response.setCloseSchedules(scheduleList);
		return response;
	}

	@Override
	public ResponseBody deleteById(long id) {
		ResponseBody response = new ResponseBody();
		// 删除前首先进行校验

		closeScheduleMapper.deleteById(id);
		return response;
	}

	@Override
	public CloseScheduleResponse findById(long id) {
		CloseScheduleResponse response = new CloseScheduleResponse();
		List<CloseSchedule> scheduleList = new ArrayList<CloseSchedule>();
		scheduleList.add(this.closeScheduleMapper.findById(id));
		response.setCloseSchedules(scheduleList);
		return response;

	}

	@Override
	public CloseScheduleResponse findByWeekScheduleId(long weekScheduleId) {
		CloseScheduleResponse response = new CloseScheduleResponse();
		int status = CloseSchedule.Status.EFFECTIVE.getValue();
		List<CloseSchedule> closeSchedules = closeScheduleMapper
				.findByWeekScheduleIdStatus(weekScheduleId, status);
		for (CloseSchedule closeSchedule : closeSchedules) {
			WeekSchedule weekSchedule = weekScheduleMapper
					.findById(weekScheduleId);
			closeSchedule.setWeekSchedule(weekSchedule);
		}
		response.setCloseSchedules(closeSchedules);
		return response;
	}

	@Override
	public CloseScheduleResponse update(long id, Integer Status, Date fromDate,
			Date toDate, String reason, Long creatorId) {
		// TODO Auto-generated method stub
		CloseScheduleResponse response = new CloseScheduleResponse();

		CloseSchedule closeSchedule = this.closeScheduleMapper.findById(id);
		if (closeSchedule == null) {
			response.setErrorCode(ScheduleErrorCode.CLOSE_SCHEDULE_NOT_FOUND);
			return response;
		}
		if (Status != null)
			closeSchedule.setStatus(Status);
		if (fromDate != null)
			closeSchedule.setFromDate(fromDate);
		if (toDate != null)
			closeSchedule.setToDate(toDate);
		if (reason != null)
			closeSchedule.setReason(reason);
		if (creatorId != null)
			closeSchedule.setCreatorId(creatorId);
		closeSchedule.setCreateTime(new Date());

		this.closeScheduleMapper.update(closeSchedule);
		return response;
	}
}
