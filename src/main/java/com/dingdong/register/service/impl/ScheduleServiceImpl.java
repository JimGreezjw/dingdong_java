package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.exception.CommonErrorCode;
import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.exception.DoctorErrorCode;
import com.dingdong.register.exception.RegisterErrorCode;
import com.dingdong.register.exception.ScheduleErrorCode;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.mapper.RegisterMapper;
import com.dingdong.register.mapper.ScheduleMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.model.Register;
import com.dingdong.register.model.Schedule;
import com.dingdong.register.model.Schedule.TimeSlot;
import com.dingdong.register.service.ScheduleService;
import com.dingdong.register.vo.request.ScheduleRequest;
import com.dingdong.register.vo.response.ScheduleDateResponse;
import com.dingdong.register.vo.response.ScheduleResponse;
import com.dingdong.sys.service.CommonMessageService;
import com.dingdong.sys.vo.util.DDCollectionUtils;
import com.dingdong.sys.vo.util.DDDate;
import com.dingdong.sys.vo.util.SmsConst;
import com.github.pagehelper.PageHelper;

/**
 * 预约服务
 * 
 * @author chenliang
 * @version 2015年12月13日 上午1:34:49
 */
@Service("scheduleService")
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

	private static final Logger LOG = LoggerFactory
			.getLogger(ScheduleServiceImpl.class);

	@Autowired
	private RegisterMapper registerMapper;
	@Autowired
	private ScheduleMapper scheduleMapper;

	@Autowired
	private HospitalMapper hospitalMapper;
	@Autowired
	private DoctorMapper doctorMapper;
	@Autowired
	private CommonMessageService commonMessageService;

	@Override
	public ScheduleResponse getDoctorSchedule(long doctorId, Date beginDate,
			Date endDate) {
		ScheduleResponse response = new ScheduleResponse();

		// FIXME niukai 设置时间，此处应该有一个时间转换的过程
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date now = calendar.getTime();

		if (beginDate == null || beginDate.before(now)) {
			beginDate = now;
		}

		if (endDate == null || endDate.before(now)) {
			endDate = now;
		}

		// 开始时间
		calendar.setTime(beginDate);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		beginDate = calendar.getTime();

		// 结束时间
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		endDate = calendar.getTime();

		response.setSchedules(scheduleMapper.findDoctorScheduleList(doctorId,
				beginDate, endDate));
		return response;
	}

	@Override
	public ScheduleResponse getDoctorScheduleWithDraft(long doctorId,
			Date beginDate, Date endDate) {
		ScheduleResponse response = new ScheduleResponse();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date d = calendar.getTime();

		if (beginDate == null || beginDate.before(d))
			beginDate = d;
		response.setSchedules(scheduleMapper.findDoctorScheduleListWithDraft(
				doctorId, beginDate, endDate));
		return response;
	}

	@Override
	public ScheduleResponse createSchedule(ScheduleRequest request) {
		ScheduleResponse response = new ScheduleResponse();

		long doctorId = request.getDoctorId();
		Doctor doctor = doctorMapper.findById(doctorId);
		if (null == doctor) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
			return response;
		}

		Hospital hospital = hospitalMapper.findById(request.getHospitalId());
		if (null == hospital) {
			response.setErrorCode(RegisterErrorCode.HOSPITAL_ID_NOT_EXIST);
			return response;
		}

		// 检验医生是否已经生成过日程,如果已经生成了日程,则无法再次生成
		Date scheduleDate = request.getScheduleDate();
		if (scheduleDate == null) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_DATE_EMPTY);
			return response;
		}

		// 进行时段的检验
		int timeSlot = request.getTimeSlot();
		List<Schedule> existScheduleList = scheduleMapper
				.findDoctorScheduleList(doctorId, scheduleDate, scheduleDate);

		if (!DDCollectionUtils.isEmpty(existScheduleList)) {
			if (timeSlot == TimeSlot.ALL.getValue()) {
				// 如果是全天的话,直接返回错误即可
				response.setErrorCode(ScheduleErrorCode.DATE_ALREADY_SCHEDULE);
				return response;
			}

			Set<Integer> existTimeSlot = new HashSet<Integer>();
			for (Schedule obj : existScheduleList) {
				existTimeSlot.add(obj.getTimeSlot());
			}

			if (existTimeSlot.contains(TimeSlot.ALL.getValue())
					|| existTimeSlot.contains(timeSlot)) {
				response.setErrorCode(ScheduleErrorCode.DATE_ALREADY_SCHEDULE);
				return response;
			}
		}
		// end 进行时段的检验

		// FIXME 确定register的状态
		int status = Schedule.Status.DRAFT.getValue();
		DDDate nowDate = new DDDate();
		DDDate shouldActiveDate = new DDDate(scheduleDate).before(7);

		// 看看是否到了应该发出通知的时候
		if (nowDate.getDate().after(shouldActiveDate.getDate())) {
			status = Schedule.Status.EFFECTIVE.getValue();
		}
		// end

		Schedule schedule = new Schedule();
		schedule.setIssueNum(request.getIssueNum());
		schedule.setStatus(status);
		schedule.setScheduleDate(request.getScheduleDate());
		schedule.setTimeSlot(request.getTimeSlot());
		schedule.setHospitalId(request.getHospitalId());
		schedule.setHospitalName(hospital.getName());
		schedule.setDoctorId(doctorId);
		schedule.setDoctorName(doctor.getName());
		schedule.setCreateTime(nowDate.getDate());

		this.createSchedule(schedule);
		// end

		List<Schedule> scheduleList = new ArrayList<Schedule>();
		scheduleList.add(schedule);
		response.setSchedules(scheduleList);
		return response;
	}

	@Override
	public void createSchedule(Schedule schedule) {
		LOG.info("createSchedule, status ={},schedule={}",
				schedule.getStatus(), schedule);
		scheduleMapper.addSchedule(schedule);

		// 如果已经到了预约的时间，则通知患者进行预约
		if (schedule.getStatus() == Schedule.Status.EFFECTIVE.getValue()) {
			this.confirmSchedule(schedule);
		}

		// niukai 状态逻辑
		// 通过前端的日期实现
		// 莫名其妙啊 lqm todo
		// if (schedule.getStatus() == Schedule.Status.DRAFT.getValue()) {
		// scheduleMapper.updateScheduleState(schedule.getId(),
		// Schedule.Status.EFFECTIVE.getValue());
		// }

	}

	// 日程确认后对排队用户进行处理
	private ResponseBody confirmSchedule(Schedule schedule) {
		// Schedule schedule = scheduleMapper.findById(scheduleId);
		ResponseBody response = new ResponseBody();
		// if (null == schedule) {
		// response.setErrorCode(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
		// return response;
		// }

		// long hospitalId = schedule.getHospitalId();

		// // 医院电话
		// String hospitalTele = "";
		// Hospital hospital = this.hospitalMapper.findById(hospitalId);
		// if (hospital != null)
		// hospitalTele = hospital.getTele();
		// FIXME niukai 进行数量的限定，还需考虑延迟的情况
		int issueNum = schedule.getIssueNum();
		int registerNum = schedule.getRegisteredNum();
		if (registerNum >= issueNum) {
			return response;
		}

		// // 查看已经存在的预约信息
		// List<Register> existRegisters = registerMapper
		// .findByScheduleId(scheduleId);
		// if (!CollectionUtils.isEmpty(existRegisters)) {
		// for (Register register : existRegisters) {
		// if (register.getStatus() == Register.Status.SUCCESS.getValue()) {
		// // no operation
		// } else {
		// existRegisters.remove(register);
		// }
		// }
		//
		// // 发送短信
		// StringBuffer mobileStr = new StringBuffer();
		// for (Register register : existRegisters) {
		// long userId = register.getUserId();
		// User user = userMapper.findById(userId);
		//
		// if (user == null) {
		// continue;
		// }
		//
		// String mobileNo = user.getPhone();
		// if (StringUtils.isEmpty(mobileStr)) {
		// continue;
		// }
		//
		// mobileStr.append(mobileNo);
		// mobileStr.append(",");
		// }
		//
		// if (mobileStr.length() > 0) {
		// mobileStr.deleteCharAt(mobileStr.length() - 1);
		//
		// String patientName = "叮咚用户";
		// String doctorName = schedule.getDoctorName();
		// String strTime = new DDDate(schedule.getScheduleDate())
		// .toChinaDate() + schedule.getTimeSlotDesc();
		// String placeStr = schedule.getHospitalName();
		//
		// commonMessageService.sendOrderMessage(mobileStr.toString(),
		// patientName, doctorName, strTime, placeStr,
		// SmsConst.PATIENT_PREPARE_DAY, hospitalTele);
		// }
		// }

		List<Register> registerList = registerMapper.getArrivedQueue(schedule);
		// end
		// 一下流程将排队转成预约
		if (null == registerList || registerList.isEmpty()) {
			// response.setErrorCode(QueueErrorCode.QUEUE_NOT_FOUND);
			return response;
		}

		// int seq = 0;
		// Date createTime = new Date();
		// Date expireTime = new Date(createTime.getTime() + 60L * 60L * 1000L);
		for (Register register : registerList) {

			// register.setSeq(++seq);
			// 这里的seq不再使用自然号,而是使用排队号

			register.setScheduleDate(schedule.getScheduleDate());
			register.setScheduleId(schedule.getId());
			register.setTimeSlot(schedule.getTimeSlot());
			register.setStatus(Register.Status.SUCCESS.getValue());// 直接挂号成功

			// FIXME 设置失效时间
			// register.setCreateTime(createTime);
			// register.setExpireTime(expireTime);
			// end

		}
		this.registerMapper.updateList(registerList);
		schedule.setStatus(Schedule.Status.EFFECTIVE.getValue());
		schedule.setRegisteredNum(registerList.size());
		scheduleMapper.save(schedule);

		// FIXME 通知进行挂号的确认
		// StringBuffer mobileArray = new StringBuffer();
		for (Register register : registerList) {
			commonMessageService.sendOrderMessage(register, null, null, null);

		}
		// LOG.info("the mobile array is " + mobileArray.toString());

		return response;
	}

	// @Override
	// @Scheduled(cron = "0 0/10 8-20 * * ?", zone = "GMT+08:00")
	// public void activeSchedule() {
	// // 后台任务，写入程序
	// DDDate now = new DDDate();
	// DDDate sevenDays = now.after(SmsConst.DOCTOR_PREPARE_DAY);
	// Date queryDate = sevenDays.getDate();
	// int status = Schedule.Status.DRAFT.getValue();
	//
	// List<Schedule> scheduleList = scheduleMapper.findUnactiveScheduleList(
	// status, queryDate);
	// if (!CollectionUtils.isEmpty(scheduleList)) {
	// for (Schedule schedule : scheduleList) {
	// long scheduleId = schedule.getId();
	// this.confirmSchedule(scheduleId);
	// }
	// }
	// }

	@Override
	public ScheduleResponse activeSpecificSchedule(long scheduleId) {
		ScheduleResponse response = new ScheduleResponse();
		int status = Schedule.Status.DRAFT.getValue();

		Schedule schedule = scheduleMapper.findById(scheduleId);
		if (schedule == null) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
			return response;
		}

		// 检验状态
		if (status != Schedule.Status.DRAFT.getValue()) {
			response.setErrorCode(ScheduleErrorCode.UNABLE_ACTIVE);
			return response;
		}

		// 检验是否到可激活时间
		DDDate now = new DDDate();
		DDDate shouldActiveDate = new DDDate(schedule.getScheduleDate())
				.before(SmsConst.DOCTOR_PREPARE_DAY);
		if (now.getDate().before(shouldActiveDate.getDate())) {
			response.setErrorCode(ScheduleErrorCode.UNREACH_TIME);
			return response;
		}

		// 取得返回结果
		ResponseBody tempResponse = this.confirmSchedule(schedule);
		if (tempResponse.getResponseStatus() != CommonErrorCode.OK.getCode()) {
			response.setResponseStatus(tempResponse.getResponseStatus());
			response.setResponseDesc(tempResponse.getResponseDesc());

			return response;
		}

		List<Schedule> scheduleList = new ArrayList<Schedule>();
		scheduleList.add(schedule);
		response.setSchedules(scheduleList);
		return response;
	}

	// @Override
	// @Scheduled(cron = "0 5/10 8-20 * * ?", zone = "GMT+08:00")
	// public void scanUnFullSchedule() {
	// // 首先获得未预约满的日程，获取的日程应该在今天以后，并在设定的日子之前
	// int status = Schedule.Status.EFFECTIVE.getValue();
	// DDDate beginDate = new DDDate();
	// DDDate endDate = beginDate.after(SmsConst.DOCTOR_PREPARE_DAY);
	//
	// List<Schedule> scheduleList = scheduleMapper.findUnFullScheduleList(
	// status, beginDate.getDate(), endDate.getDate());
	// if (CollectionUtils.isEmpty(scheduleList)) {
	// return;
	// }
	//
	// // 排队的status
	// int queueStatus = Queue.Status.WAITING.getStatus();
	//
	// // FIXME niukai 先使用简单的逻辑实现，之后考虑使用多线程
	// for (Schedule schedule : scheduleList) {
	// // 先更新schedule，之后再继续
	// long scheduleId = schedule.getId();
	// int rows = 0;
	// int flag = 0;
	// List<Queue> queueList = null;
	//
	// while (rows < 1 && flag < SmsConst.FAILURE_REPEAT_TIME) {
	// flag++;
	//
	// int issueNum = schedule.getIssueNum();
	// int registerNum = schedule.getRegisteredNum();
	//
	// int presetNum = issueNum - registerNum;
	// if (presetNum < 1) {
	// continue;
	// }
	//
	// long doctorId = schedule.getDoctorId();
	// long hospitalId = schedule.getHospitalId();
	// // 查询Queue
	// queueList = queueMapper
	// .findByDoctotrIdAndHospitalIdAndStatusOrderByCreateTimeLimitNum(
	// doctorId, hospitalId, queueStatus, presetNum);
	//
	// if (CollectionUtils.isEmpty(queueList)) {
	// break;
	// }
	//
	// int queueSize = queueList.size();
	// // 确定通知的人数
	// presetNum = presetNum <= queueSize ? presetNum : queueSize;
	//
	// rows = scheduleMapper.acquireScheduleLock(scheduleId,
	// registerNum + presetNum, registerNum);
	//
	// if (rows < 1) {
	// schedule = scheduleMapper.findById(scheduleId);
	// continue;
	// } else {
	// break;
	// }
	// }
	//
	// if (rows < 1) {
	// continue;
	// }
	//
	// // 证明数量正确，开始设置排队信息
	// // FIXME 设置创建时间、失效时间
	// Date createTime = new Date();
	// Date expireTime = new Date(createTime.getTime() + 60L * 60L * 1000L);
	// List<Register> registers = new ArrayList<Register>();
	// for (Queue queue : queueList) {
	// queue.setStatus(Queue.Status.SUCCESS.getStatus());
	//
	// Register register = new Register();
	// register.setSeq(queue.getQueueNum());
	// register.setDoctorId(queue.getDoctorId());
	// register.setDoctorName(schedule.getDoctorName());
	// register.setHospitalId(schedule.getHospitalId());
	// register.setHospitalName(schedule.getHospitalName());
	// register.setUserId(queue.getUserId());
	// register.setUserName(queue.getUserName());
	// register.setScheduleDate(schedule.getScheduleDate());
	// register.setScheduleId(scheduleId);
	// register.setTimeSlot(schedule.getTimeSlot());
	// register.setStatus(Register.Status.DRAFT.getValue());
	//
	// register.setCreateTime(createTime);
	// register.setExpireTime(expireTime);
	// registers.add(register);
	// }
	//
	// registerMapper.addList(registers);
	// queueMapper.updatequeueListStatus(queueList);
	//
	// // 发送短信通知
	// StringBuffer mobileStr = new StringBuffer();
	// for (Queue queue : queueList) {
	// long userId = queue.getUserId();
	// User user = userMapper.findById(userId);
	//
	// if (user == null) {
	// continue;
	// }
	//
	// String mobileNo = user.getPhone();
	// if (!StringUtils.isEmpty(mobileNo)) {
	// mobileStr.append(mobileNo);
	// mobileStr.append(",");
	// }
	// }
	//
	// if (mobileStr.length() > 0) {
	// mobileStr.deleteCharAt(mobileStr.length() - 1);
	// String patientName = "叮咚门诊用户";
	// String doctorName = schedule.getDoctorName();
	// String placeStr = schedule.getHospitalName();
	// DDDate scheduleDate = new DDDate(schedule.getScheduleDate());
	// String timeStr = scheduleDate.toChinaDate()
	// + schedule.getTimeSlotDesc();
	// int validMinute = SmsConst.PATIENT_CONFIRM_MINUTE;
	// commonMessageService.sendConfirmRegisterMessage(
	// mobileStr.toString(), patientName, doctorName, timeStr,
	// placeStr, validMinute);
	// }
	// }
	// }

	private ScheduleDateResponse getDateSchedule(List<Schedule> scheduleList) {
		ScheduleDateResponse response = new ScheduleDateResponse();
		Map<Date, Integer> dateMap = new HashMap<Date, Integer>();
		for (Schedule schedule : scheduleList) {
			int status = (schedule.getRegisteredNum() >= schedule.getIssueNum()) ? 2
					: 1;

			if (dateMap.containsKey(schedule.getScheduleDate())) {
				if (dateMap.get(schedule.getScheduleDate()) > status)
					dateMap.put(schedule.getScheduleDate(), status);
			} else {
				dateMap.put(schedule.getScheduleDate(), status);
			}
		}
		List<ScheduleDateResponse.ScheduleDate> scheduleDateList = new ArrayList<ScheduleDateResponse.ScheduleDate>();
		response.setSchduleDateList(scheduleDateList);
		for (Map.Entry<Date, Integer> entry : dateMap.entrySet()) {
			ScheduleDateResponse.ScheduleDate scheduleDate = response.new ScheduleDate(
					entry.getKey(), entry.getValue());
			scheduleDateList.add(scheduleDate);
		}
		return response;
	}

	@Override
	public ScheduleDateResponse getDoctorDateSchedule(long doctorId,
			Date beginDate, Date endDate) {

		List<Schedule> scheduleList = scheduleMapper.findDoctorScheduleList(
				doctorId, beginDate, endDate);
		if (scheduleList != null && !scheduleList.isEmpty()) {

			return getDateSchedule(scheduleList);
		}

		return null;
	}

	@Override
	public ResponseBody updateSchedule(ScheduleRequest request) {
		ResponseBody response = new ResponseBody();

		long doctorId = request.getDoctorId();
		Doctor doctor = doctorMapper.findById(doctorId);
		if (null == doctor) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
			return response;
		}
		Hospital hospital = hospitalMapper.findById(request.getHospitalId());
		if (null == hospital) {
			response.setErrorCode(RegisterErrorCode.HOSPITAL_ID_NOT_EXIST);
			return response;
		}
		Schedule schedule = new Schedule();
		schedule.setIssueNum(request.getIssueNum());
		schedule.setStatus(Schedule.Status.EFFECTIVE.getValue());
		schedule.setScheduleDate(request.getScheduleDate());
		schedule.setTimeSlot(request.getTimeSlot());
		schedule.setHospitalId(request.getHospitalId());
		schedule.setHospitalName(hospital.getName());
		schedule.setDoctorId(doctorId);
		schedule.setDoctorName(doctor.getName());
		scheduleMapper.addSchedule(schedule);
		this.confirmSchedule(schedule);
		return response;
	}

	@Override
	public ResponseBody updateScheduleInfo(long id, Date scheduleDate,
			Integer timeSlot, Date startTime, Date endTime, Integer issueNum,
			Long creatorId) {
		ResponseBody response = new ResponseBody();

		Schedule schedule = scheduleMapper.findById(id);
		if (schedule == null) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
			return response;
		}
		if (scheduleDate != null)
			schedule.setScheduleDate(scheduleDate);
		if (timeSlot != null)
			schedule.setTimeSlot(timeSlot);
		if (startTime != null)
			schedule.setStartTime(startTime);
		if (endTime != null)
			schedule.setEndTime(endTime);
		if (issueNum != null)
			schedule.setIssueNum(issueNum);
		if (creatorId != null)
			schedule.setCreatorId(creatorId);

		scheduleMapper.save(schedule);

		return response;
	}

	@Override
	public ResponseBody finishSchedule(long id) {
		ResponseBody response = new ResponseBody();

		Schedule schedule = scheduleMapper.findById(id);
		if (schedule == null) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
			return response;
		}

		int status = schedule.getStatus();
		if (status == Schedule.Status.EFFECTIVE.getValue()) {
			// 仅有这个状态可以设置为完成
			schedule.setStatus(Schedule.Status.COMPLETE.getValue());

			// 只修改状态
			status = Schedule.Status.COMPLETE.getValue();
			scheduleMapper.updateScheduleState(id, status);
		} else {
			response.setErrorCode(ScheduleErrorCode.UNABLE_FINISH);
		}

		return response;
	}

	@Override
	public ResponseBody postponeSchedule(long id) {
		ResponseBody response = new ResponseBody();

		Schedule schedule = this.isScheduleExist(id);
		if (schedule == null) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
			return response;
		}

		// 只有未激活、已创建的状态可以延迟
		int status = schedule.getStatus();
		if (status == Schedule.Status.DRAFT.getValue()
				|| status == Schedule.Status.EFFECTIVE.getValue()) {
			// 可以延迟
			status = Schedule.Status.POSTPONE.getValue();
			int rows = scheduleMapper.updateScheduleState(id, status);

			if (rows < 1) {
				response.setErrorCode(ScheduleErrorCode.UNABLE_POSTPONE);
			}
		} else {
			response.setErrorCode(ScheduleErrorCode.UNABLE_POSTPONE);
		}

		return response;
	}

	@Override
	public ResponseBody postponeSchedule(long id, Date newScheduleDate) {
		ResponseBody response = new ResponseBody();

		Schedule schedule = this.isScheduleExist(id);
		if (schedule == null) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
			return response;
		}

		int status = schedule.getStatus();
		if (status == Schedule.Status.COMPLETE.getValue()
				|| status == Schedule.Status.CANCEL.getValue()) {
			response.setErrorCode(ScheduleErrorCode.UNABLE_POSTPONE);
			return response;
		}

		// 医院电话

		Hospital hospital = this.hospitalMapper.findById(schedule
				.getHospitalId());

		// FIXME niukai 这是要重点写的方法了
		DDDate scheduleDate = new DDDate(newScheduleDate);
		DDDate now = new DDDate();
		if (scheduleDate.getDate().before(now.getDate())) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_TOO_EARLY);
			return response;
		}

		DDDate canActiveDate = now.after(SmsConst.DOCTOR_PREPARE_DAY);
		if (scheduleDate.getDate().after(canActiveDate.getDate())) {
			// 设置为草稿状态，就是未激活
			status = Schedule.Status.DRAFT.getValue();
		} else {
			// 证明到了可以预约的时间
			status = Schedule.Status.EFFECTIVE.getValue();
		}

		schedule.setStatus(status);
		schedule.setScheduleDate(newScheduleDate);

		int rows = scheduleMapper.updateScheduleInfo(schedule);
		if (rows < 1) {
			return response;
		}

		// 更新一下register的信息
		long scheduleId = schedule.getId();
		List<Register> registerList = registerMapper
				.findByScheduleId(scheduleId);
		if (!CollectionUtils.isEmpty(registerList)) {
			for (Register register : registerList) {
				if (register.getStatus() == Register.Status.SUCCESS.getValue()) {
					register.setScheduleDate(newScheduleDate);
				} else {
					registerList.remove(register);
				}
			}

			// 保存
			registerMapper.updateList(registerList);
			// 如果没到激活时间，暂时不发送短信，等激活时再统一发送
		} else {
			return response;
		}

		if (status == Schedule.Status.EFFECTIVE.getValue()) {

			for (Register register : registerList) {
				this.commonMessageService.sendOrderMessage(register, null,
						null, hospital);
			}

		}

		return response;
	}

	@Override
	public ResponseBody deleteSchedule(long id) {
		ResponseBody response = new ResponseBody();
		// 删除前首先进行校验
		List<Register> registers = this.registerMapper
				.findUnFinishedByScheduleId(id);
		if (CollectionUtils.isNotEmpty(registers)) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_CANNOT_DELETE);
			return response;
		}

		scheduleMapper.deleteById(id);
		return response;
	}

	private Schedule isScheduleExist(long id) {
		Schedule schedule = scheduleMapper.findById(id);
		return schedule;
	}

	@Override
	public Schedule findById(long id) {
		// TODO Auto-generated method stub
		return this.scheduleMapper.findById(id);
	}

	@Override
	public ScheduleResponse findByDoctorIdHospitalIdBeginDateEndDate(
			long doctorId, Long hospitalId, Date beginDate, Date endDate) {
		LOG.info(
				"***findByDoctorIdHospitalIdBeginDateEndDate the input param is  doctorId={},hospitalId={},beginDate={},endDate={}",
				doctorId, hospitalId, beginDate, endDate);
		ScheduleResponse response = new ScheduleResponse();
		int status = Schedule.Status.EFFECTIVE.getValue();
		Date d = new Date();
		if (beginDate == null || beginDate.before(d))
			beginDate = d;

		response.setSchedules(scheduleMapper
				.findByDoctorIdHospitalIdBeginDateEndDateStatus(doctorId,
						hospitalId, beginDate, endDate, status));
		return response;
	}

	@Override
	public ScheduleDateResponse getDoctorHospitalDateSchedule(long doctorId,
			long hosptalId, Date beginDate, Date endDate) {
		LOG.info(
				"***getDoctorHospitalDateSchedule the input param is  doctorId={},beginDate={},endDate={}",
				doctorId, beginDate, endDate);
		// ScheduleResponse response = new ScheduleResponse();
		int status = Schedule.Status.EFFECTIVE.getValue();

		// FIXME niukai 设置时间，此处应该有一个时间转换的过程
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date now = calendar.getTime();

		if (beginDate == null || beginDate.before(now)) {
			beginDate = now;
		}

		if (endDate == null || endDate.before(now)) {
			endDate = now;
		}

		// 开始时间
		calendar.setTime(beginDate);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		beginDate = calendar.getTime();

		// 结束时间
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		endDate = calendar.getTime();

		List<Schedule> scheduleList = scheduleMapper
				.findByDoctorIdHospitalIdBeginDateEndDateStatus(doctorId,
						hosptalId, beginDate, endDate, status);

		if (scheduleList != null && !scheduleList.isEmpty()) {

			return getDateSchedule(scheduleList);
		}

		return null;
	}

	@Override
	public ScheduleResponse getSchedulesWithPage(long doctorId,
			long hospitalId, Date beginDate, Date endDate,
			DDPageInfo<Schedule> pageInfo) {
		LOG.info(
				"***getSchedulesWithPage the input param is  doctorId={},hospitalId={},beginDate={},endDate={}",
				doctorId, hospitalId, beginDate, endDate);
		ScheduleResponse response = new ScheduleResponse();
		int status = Schedule.Status.EFFECTIVE.getValue();
		PageHelper.startPage(pageInfo.getPage(), pageInfo.getSize(), true);
		pageInfo.setPageInfo(scheduleMapper
				.findByDoctorIdHospitalIdBeginDateEndDateStatus(doctorId,
						hospitalId, beginDate, endDate, status));

		response.setSchedules(pageInfo.getPageInfo().getResult());
		response.setPages(pageInfo.getPages());
		response.setTotal(pageInfo.getTotal());
		return response;
	}

	@Override
	public ScheduleResponse createSchedule(long doctorId, long hospitalId,
			Date scheduleDate, int timeSlot, Date startTime, Date endTime,
			int issueNum, long creatorId) {
		LOG.info(
				"***createSchedule  the input param is  doctorId={},hospitalId={},scheduleDate={},timeSlo={}  startTime={},endTime={},issueNum={}",
				doctorId, hospitalId, scheduleDate, timeSlot, startTime,
				endTime, issueNum);
		ScheduleResponse response = new ScheduleResponse();

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

		// 检验医生是否已经生成过日程,如果已经生成了日程,则无法再次生成
		if (scheduleDate == null) {
			// errMsg--未设置出诊日期
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_DATE_EMPTY);
			return response;
		}

		// 进行时段的检验
		List<Schedule> existScheduleList = scheduleMapper
				.findDoctorScheduleList(doctorId, scheduleDate, scheduleDate);

		if (!DDCollectionUtils.isEmpty(existScheduleList)) {
			if (timeSlot == TimeSlot.ALL.getValue()) {
				// 如果是全天的话,直接返回错误即可
				response.setErrorCode(ScheduleErrorCode.DATE_ALREADY_SCHEDULE);
				return response;
			}

			Set<Integer> existTimeSlot = new HashSet<Integer>();
			for (Schedule obj : existScheduleList) {
				existTimeSlot.add(obj.getTimeSlot());
			}

			if (existTimeSlot.contains(TimeSlot.ALL.getValue())
					|| existTimeSlot.contains(timeSlot)) {
				response.setErrorCode(ScheduleErrorCode.DATE_ALREADY_SCHEDULE);
				return response;
			}
		}
		// end 进行时段的检验

		// FIXME 确定register的状态

		// DDDate nowDate = new DDDate();
		// DDDate shouldActiveDate = new DDDate(scheduleDate).before(7);

		// 看看是否到了应该发出通知的时候
		// lqm todo
		// 这个还没有特别理解
		// if (nowDate.getDate().after(shouldActiveDate.getDate())) {
		// status = Schedule.Status.EFFECTIVE.getValue();
		// }
		// end

		Schedule schedule = new Schedule();
		schedule.setDoctorId(doctorId);
		schedule.setDoctorName(doctor.getName());
		schedule.setHospitalId(hospitalId);
		schedule.setHospitalName(hospital.getName());

		schedule.setScheduleDate(scheduleDate);
		schedule.setTimeSlot(timeSlot);
		schedule.setIssueNum(issueNum);

		int status = Schedule.Status.EFFECTIVE.getValue();
		schedule.setStatus(status);
		if (null != startTime)
			schedule.setStartTime(startTime);
		if (null != endTime)
			schedule.setEndTime(endTime);

		schedule.setCreatorId(creatorId);

		this.createSchedule(schedule);
		// end

		List<Schedule> scheduleList = new ArrayList<Schedule>();
		scheduleList.add(schedule);
		response.setSchedules(scheduleList);
		return response;
	}
}
