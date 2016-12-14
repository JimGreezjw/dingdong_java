package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.exception.DoctorErrorCode;
import com.dingdong.register.exception.RegisterErrorCode;
import com.dingdong.register.exception.ScheduleErrorCode;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.mapper.WeekScheduleMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.model.Schedule;
import com.dingdong.register.model.WeekSchedule;
import com.dingdong.register.service.ScheduleService;
import com.dingdong.register.service.WeekScheduleService;
import com.dingdong.register.vo.request.WeekScheduleRequest;
import com.dingdong.register.vo.response.WeekScheduleResponse;

/**
 * 
 * @author yushansoft
 * 
 */
@Service("weekWeekScheduleService")
@Transactional
public class WeekScheduleServiceImpl implements WeekScheduleService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(WeekScheduleServiceImpl.class);

	@Autowired
	private WeekScheduleMapper weekScheduleMapper;

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private HospitalMapper hospitalMapper;
	@Autowired
	private DoctorMapper doctorMapper;

	// 成功诊疗后对医生用户的奖励
	@Value("#{config['schedule.autoGenerateAdvanceDays']}")
	private int autoGenerateAdvanceDays = 14;

	@Override
	public WeekScheduleResponse add(long doctorId, long hospitalId, int day,
			int timeSlot, Date startTime, Date endTime, int issueNum,
			long creatorId) {
		WeekScheduleResponse response = new WeekScheduleResponse();

		Doctor doctor = doctorMapper.findById(doctorId);
		if (null == doctor) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
			return response;
		}

		Hospital hospital = hospitalMapper.findById(hospitalId);
		if (null == hospital) {
			response.setErrorCode(RegisterErrorCode.HOSPITAL_ID_NOT_EXIST);
			return response;
		}

		// end

		WeekSchedule weekSchedule = new WeekSchedule();

		weekSchedule.setHospitalId(hospitalId);
		weekSchedule.setHospitalName(hospital.getName());
		weekSchedule.setDoctorId(doctorId);
		weekSchedule.setDoctorName(doctor.getName());
		weekSchedule.setIssueNum(issueNum);
		weekSchedule.setStatus(WeekSchedule.Status.EFFECTIVE.getValue());
		weekSchedule.setDay(day);
		weekSchedule.setTimeSlot(timeSlot);
		weekSchedule.setStartTime(startTime);
		weekSchedule.setEndTime(endTime);

		this.weekScheduleMapper.add(weekSchedule);

		// 如果已经到了预约的时间，则通知患者进行预约

		List<WeekSchedule> scheduleList = new ArrayList<WeekSchedule>();
		scheduleList.add(weekSchedule);
		response.setWeekSchedules(scheduleList);
		return response;
	}

	// 每天早上8点开始放号
	@Override
	@Scheduled(cron = "0 0 8 * * ?", zone = "GMT+08:00")
	public void autoCreateSchedule() {

		Calendar calendar = Calendar.getInstance();

		// 自动提前
		calendar.add(Calendar.DAY_OF_YEAR, this.autoGenerateAdvanceDays);

		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date scheduleDate = calendar.getTime();

		int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		List<WeekSchedule> weekSchedules = this.weekScheduleMapper
				.findByDay(day);
		if (CollectionUtils.isEmpty(weekSchedules))
			return;
		int status = Schedule.Status.EFFECTIVE.getValue();

		for (WeekSchedule weekSchedule : weekSchedules) {
			if (weekSchedule.getDay() != day)
				continue;
			Schedule schedule = new Schedule();
			schedule.setDoctorId(weekSchedule.getDoctorId());
			schedule.setDoctorName(weekSchedule.getDoctorName());
			schedule.setHospitalId(weekSchedule.getHospitalId());
			schedule.setHospitalName(weekSchedule.getHospitalName());
			schedule.setIssueNum(weekSchedule.getIssueNum());
			schedule.setScheduleDate(scheduleDate);
			schedule.setTimeSlot(weekSchedule.getTimeSlot());
			schedule.setStartTime(weekSchedule.getStartTime());
			schedule.setEndTime(weekSchedule.getEndTime());
			schedule.setStatus(status);
			this.scheduleService.createSchedule(schedule);

		}

		// // 后台任务，写入程序
		// DDDate now = new DDDate();
		// DDDate sevenDays = now.after(SmsConst.DOCTOR_PREPARE_DAY);
		// Date queryDate = sevenDays.getDate();
		// int status = WeekSchedule.Status.DRAFT.getValue();
		//
		// List<WeekSchedule> scheduleList = scheduleMapper
		// .findUnactiveWeekScheduleList(status, queryDate);
		// if (!CollectionUtils.isEmpty(scheduleList)) {
		// for (WeekSchedule schedule : scheduleList) {
		// long scheduleId = schedule.getId();
		// this.confirmWeekSchedule(scheduleId);
		// }
		// }
	}

	@Override
	public ResponseBody deleteById(long id) {
		ResponseBody response = new ResponseBody();
		// 删除前首先进行校验

		weekScheduleMapper.deleteById(id);
		return response;
	}

	@Override
	public WeekScheduleResponse findById(long id) {
		WeekScheduleResponse response = new WeekScheduleResponse();
		List<WeekSchedule> scheduleList = new ArrayList<WeekSchedule>();
		scheduleList.add(this.weekScheduleMapper.findById(id));
		response.setWeekSchedules(scheduleList);
		return response;

	}

	@Override
	public WeekScheduleResponse findByDoctorIdHospitalId(long doctorId,
			Long hospitalId) {
		WeekScheduleResponse response = new WeekScheduleResponse();
		int status = WeekSchedule.Status.EFFECTIVE.getValue();
		List<WeekSchedule> weekSchedules = weekScheduleMapper
				.findByDoctorIdHospitalIdStatus(doctorId, hospitalId, status);
		response.setWeekSchedules(weekSchedules);
		return response;
	}

	@Override
	public WeekScheduleResponse addWeekSchedule(WeekScheduleRequest request) {
		WeekScheduleResponse response = new WeekScheduleResponse();
		List<WeekSchedule> weekSchedules = request.getWeekSchedules();
		for (WeekSchedule weekSchedule : weekSchedules) {
			weekScheduleMapper.add(weekSchedule);
		}
		response.setWeekSchedules(weekSchedules);
		return response;
	}

	@Override
	public WeekScheduleResponse update(long id, Integer day, Integer timeSlot,
			Date startTime, Date endTime, Integer issueNum, Long creatorId) {
		// TODO Auto-generated method stub
		WeekScheduleResponse response = new WeekScheduleResponse();

		WeekSchedule weekSchedule = this.weekScheduleMapper.findById(id);
		if (weekSchedule == null) {
			response.setErrorCode(ScheduleErrorCode.WEEK_SCHEDULE_NOT_FOUND);
			return response;
		}
		if (day != null)
			weekSchedule.setDay(day);
		if (timeSlot != null)
			weekSchedule.setTimeSlot(timeSlot);
		if (startTime != null)
			weekSchedule.setStartTime(startTime);
		if (endTime != null)
			weekSchedule.setEndTime(endTime);
		if (issueNum != null)
			weekSchedule.setIssueNum(issueNum);

		weekSchedule.setCreateTime(new Date());

		this.weekScheduleMapper.update(weekSchedule);
		return response;
	}

}
