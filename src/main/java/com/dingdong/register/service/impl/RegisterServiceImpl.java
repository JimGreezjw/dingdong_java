package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.exception.LockFailureException;
import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.exception.DoctorErrorCode;
import com.dingdong.register.exception.PatientErrorCode;
import com.dingdong.register.exception.RegisterErrorCode;
import com.dingdong.register.exception.ScheduleErrorCode;
import com.dingdong.register.mapper.DoctorHospitalMapper;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.mapper.PatientMapper;
import com.dingdong.register.mapper.RegisterMapper;
import com.dingdong.register.mapper.ScheduleMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.model.DoctorHospital;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.model.Patient;
import com.dingdong.register.model.Register;
import com.dingdong.register.model.Register.Status;
import com.dingdong.register.model.Schedule;
import com.dingdong.register.service.RegisterService;
import com.dingdong.register.vo.response.RegisterResponse;
import com.dingdong.sys.exception.SysErrorCode;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.Transfer;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.CommonMessageService;
import com.dingdong.sys.service.TransferService;
import com.dingdong.sys.service.YusFileService;

/**
 * 预约服务
 * 
 * @author chenliang
 * @version 2015年12月13日 上午1:34:49
 */
@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

	private static final Logger LOG = LoggerFactory
			.getLogger(RegisterServiceImpl.class);

	// 取消挂号提前通知的天数
	// @Value("#{config['register.cancelAdvanceDays']}")
	// private int cancelAdvanceDays;

	// 成功诊疗后对医生用户的奖励
	@Value("#{config['score.successTreatDoctorScore']}")
	private int successTreatDoctorScore = 0;

	// @Value("#{config['register.deposit']}")
	// private int registerDeposit;

	// 调度按照周期自动提前分钟数，默认是一天的时间量
	@Value("#{config['register.canCancelAdvanceMinutes']}")
	private int canCancelAdvanceMinutes = 1440;
	// 用户取消挂号的积分评价
	@Value("#{config['register.cancelFineScore']}")
	private int cancelFineScore = 10;
	// 取消挂号的罚金百分比
	@Value("#{config['register.cancelfinePercent']}")
	private int cancelfinePercent = 50;

	// @Value("#{config['register.sendOrderMsgImmediately']}")
	// private int sendOrderMsgImmediately = 0;

	// 延期未就诊的罚金百分比
	// private int cancelAllPercent = 100;

	// @Value("#{config['register.deal']}")
	// private int dealMoney;

	@Autowired
	private RegisterMapper registerMapper;
	@Autowired
	private ScheduleMapper scheduleMapper;

	@Autowired
	private PatientMapper patientMapper;

	@Autowired
	private DoctorMapper doctorMapper;

	@Autowired
	private HospitalMapper hospitalMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DoctorHospitalMapper doctorHospitalMapper;

	@Autowired
	private CommonMessageService commonMessageService;
	@Autowired
	private TransferService transferService;

	@Autowired
	private YusFileService yusFileService;

	// 提前通知的天数
	public static int INFORM_DAYS_IN_ADVANCE = 7;

	// 是否采用模糊排队号
	public static boolean USE_FUZZY_QUEUENUM = true;

	@Override
	public ResponseBody validateUserAccount(long userId, long doctorId,
			long hospitalId) {
		ResponseBody response = new ResponseBody();

		// 用户
		User user = this.userMapper.findById(userId);
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		DoctorHospital doctorHospital = this.doctorHospitalMapper
				.findByDoctorIdAndHospitalId(doctorId, hospitalId);

		if (null == doctorHospital) {
			response.setErrorCode(RegisterErrorCode.DOCTOR_HOSPITAL_NOT_EXIST);
			return response;
		}

		// 挂号诚意金
		int registerDeposit = doctorHospital.getDeposit();

		// 检查金额是否足够
		if (user.getBalance() < registerDeposit) {
			response.setErrorCode(RegisterErrorCode.MONEY_NOT_ENOUGH);
			return response;
		}

		return response;
	}

	// @Override
	// @Deprecated
	// public RegisterResponse confirmRegister(long registerId, long patientId,
	// String revisit, String phenomenon, String attachNo) {
	// RegisterResponse response = new RegisterResponse();
	//
	// // 是否复诊
	// if ("Y".equalsIgnoreCase(revisit)) {
	// revisit = "Y";
	// } else {
	// revisit = "N";
	// }
	//
	// // 再一次进行验证,主要考虑并发的影响
	// Register register = registerMapper.findById(registerId);
	// if (register == null) {
	// response.setErrorCode(ScheduleErrorCode.NOT_HAS_REGISTER);
	// return response;
	// }
	//
	// if (register.getStatus() >= Register.Status.SUCCESS.getValue()) {
	// response.setErrorCode(ScheduleErrorCode.ALREADY_FINISH_REGISTER);
	// return response;
	// }
	//
	// // 用户
	// User user = this.userMapper.findById(register.getUserId());
	// if (user == null) {
	// response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
	// return response;
	// }
	//
	// DoctorHospital doctorHospital = this.doctorHospitalMapper
	// .findByDoctorIdAndHospitalId(register.getDoctorId(),
	// register.getHospitalId());
	//
	// if (null == doctorHospital) {
	// response.setErrorCode(RegisterErrorCode.DOCTOR_HOSPITAL_NOT_EXIST);
	// return response;
	// }
	//
	// // 挂号诚意金
	// int registerDeposit = doctorHospital.getDeposit();
	//
	// // 检查金额是否足够
	// // 检查金额是否足够
	// if (user.getBalance() < registerDeposit) {
	// response.setErrorCode(RegisterErrorCode.MONEY_NOT_ENOUGH);
	// return response;
	// }
	// // end
	//
	// // 添加信息
	// register.setStatus(Register.Status.SUCCESS.getValue());
	//
	// Patient patient = this.patientMapper.findById(patientId);
	//
	// if (patient == null) {
	// response.setErrorCode(RegisterErrorCode.PATIENT_ID_NOT_EXIST);
	// return response;
	// }
	//
	// register.setPatientId(patientId);
	// register.setPatientName(patient.getName());
	//
	// // register.setCertificateId(patient.getCertificateId());
	// // register.setUserRelation(patient.getUserRelation());
	//
	// register.setRevisit(revisit);
	// if (StringUtils.isNotBlank(phenomenon))
	// register.setPhenomenon(phenomenon);
	// if (StringUtils.isNotBlank(attachNo))
	// register.setAttachNo(attachNo);
	//
	// registerMapper.confirmRegister(register);
	//
	// 扣除诚意金
	// transferService.executeTransferBalance(user.getId(),
	// (-1 * registerDeposit), Transfer.Reason.GHYZ.getId());
	//
	// this.successRegisterPro(register, patient);
	// return response;
	// }

	/**
	 * FIXME 排队状态下的取消，由于现在是一旦日程生成了直接将患者设置成为已预约成功，所以排队时预约实际上可以
	 */
	@Override
	public RegisterResponse cancelQueue(long registerId) {
		RegisterResponse response = new RegisterResponse();

		Register register = registerMapper.findById(registerId);
		if (register == null) {
			response.setErrorCode(ScheduleErrorCode.NOT_HAS_REGISTER);
			return response;
		}
		// 只有排队中这个状态能够取消
		int status = register.getStatus();
		if (Register.Status.QUEUE.getValue() != status) {
			// 当前状态无法取消
			response.setErrorCode(ScheduleErrorCode.UNABLE_CANCEL);
			return response;
		}

		// 进行用户的检测
		long userId = register.getUserId();
		User user = this.userMapper.findById(userId);
		if (null == user) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		int rows = 0;
		// 在还未进行预约时，退还的是全部的诚意金
		transferService.executeTransferBalance(register.getUserId(),
				register.getDeposit(), Transfer.Reason.QXPDTK.getId());

		// 将这次排队设置为取消状态
		rows = registerMapper.updateStatusById(registerId,
				Register.Status.CANCEL.getValue());
		System.out.println(rows);

		// 直接对游离数据进行更新
		register.setStatus(Register.Status.CANCEL.getValue());
		List<Register> registers = new ArrayList<Register>();
		registers.add(register);

		response.setTotal(1L);
		response.setRegisters(registers);
		return response;
	}

	/**
	 * FIXME 预约状态下的取消
	 */
	@Override
	public RegisterResponse cancelRegister(long registerId) {
		RegisterResponse response = new RegisterResponse();

		Register register = registerMapper.findById(registerId);
		if (register == null) {
			response.setErrorCode(ScheduleErrorCode.NOT_HAS_REGISTER);
			return response;
		}

		// 只有这两种状态的挂号能够取消
		int status = register.getStatus();
		if (!(status == Register.Status.SUCCESS.getValue() || status == Register.Status.SIGNED
				.getValue())) {
			// 当前状态无法取消
			response.setErrorCode(ScheduleErrorCode.UNABLE_CANCEL);
			return response;
		}

		// 取消时间判断
		// 当前时间位于 挂号日期 和 挂号取消提前期 之间
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(register.getScheduleDate());
		c.add(Calendar.MINUTE, -this.canCancelAdvanceMinutes);
		if (now.before(register.getScheduleDate()) && now.after(c.getTime())) {
			// 当前时间在挂号取消提前期和挂号日期之间，则无法取消
			response.setErrorCode(RegisterErrorCode.CANCEL_SCHEDULE_OVER_TIME_LITMIT);
			return response;
		}

		// 获取日程
		long scheduleId = register.getScheduleId();
		Schedule schedule = scheduleMapper.findById(scheduleId);
		if (schedule == null) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
			return response;
		}

		// 最后，根据用户的状态更新用户
		long userId = register.getUserId();
		User user = this.userMapper.findById(userId);
		if (null == user) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		// 减去扣的钱
		int tk = Math.round(register.getDeposit()
				* (100 - this.cancelfinePercent) / 100);
		transferService.executeTransferBalance(register.getUserId(), tk,
				Transfer.Reason.QXGHTK.getId());

		// 扣除积分
		transferService.executeTransferScore(register.getUserId(),
				-this.cancelFineScore, Transfer.Reason.QXGHKCJF.getId());

		// 将预约状态设置为取消状态
		int rows = registerMapper.updateStatusById(registerId,
				Register.Status.CANCEL.getValue());

		// 将已挂号的数量减1，这里会遇到并发问题
		int registerNum = schedule.getRegisteredNum();
		int newNum = registerNum - 1;
		rows = scheduleMapper.acquireScheduleLock(scheduleId, newNum,
				registerNum);
		// 最后的检查
		if (rows < 1)
			throw new LockFailureException("无法锁定日程的已挂号数量！");

		return response;
	}

	// @Override
	// @Scheduled(cron = "0 0/10 * * * ?", zone = "GMT+08:00")
	// public void removeExpireRegister() {
	// int status = Register.Status.DRAFT.getValue();
	// Date expireTime = new Date();
	//
	// // FIXME 修改一下这里的逻辑，直接去检查需要设置为失效状态的挂号
	// List<Register> registerList = registerMapper.findExpireRegister(status,
	// expireTime);
	// // end
	// if (CollectionUtils.isEmpty(registerList)) {
	// return;
	// }
	//
	// for (Register register : registerList) {
	// // 先进行更新，再计算总量
	// long id = register.getId();
	//
	// int rows = registerMapper.updateStateById(id,
	// Register.Status.CANCEL.getValue(),
	// Register.Status.DRAFT.getValue());
	//
	// if (rows < 1) {
	// registerList.remove(register);
	// }
	// }
	//
	// // 开始进行更新
	// Map<Long, Integer> expireMap = new HashMap<Long, Integer>();
	// for (Register register : registerList) {
	//
	// long key = register.getScheduleId();
	// Integer value = expireMap.get(key);
	//
	// if (value == null) {
	// value = 1;
	// } else {
	// value++;
	// }
	//
	// expireMap.put(key, value);
	// }
	//
	// // 最后，根据实际情况设置Schedule
	// for (Entry<Long, Integer> entry : expireMap.entrySet()) {
	// long scheduleId = entry.getKey();
	// int minusNum = entry.getValue();
	//
	// int rows = 0;
	// int flag = 0;
	// while (rows < 1 && flag < SmsConst.FAILURE_REPEAT_TIME) {
	// flag++;
	// Schedule schedule = scheduleMapper.findById(scheduleId);
	//
	// if (schedule == null) {
	// continue;
	// }
	//
	// int newNum = schedule.getRegisteredNum() - minusNum;
	// rows = scheduleMapper.acquireScheduleLock(scheduleId, newNum,
	// schedule.getRegisteredNum());
	// }
	// }
	// }

	@Override
	public RegisterResponse queueUp(long userId, long patientId, long doctorId,
			long hospitalId, String revisit, String phenomenon, String attachNo) {
		LOG.info(
				"****makeAppointment, userId={},patientId={},doctorId={},revisit={},phenomenon={},attachNo={}",
				userId, patientId, doctorId, revisit, phenomenon, attachNo);

		RegisterResponse response = new RegisterResponse();
		Register register = new Register();

		// 用户
		User user = this.userMapper.findById(userId);
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		Patient patient = this.patientMapper.findById(patientId);

		if (patient == null) {
			response.setErrorCode(RegisterErrorCode.PATIENT_ID_NOT_EXIST);
			return response;
		}

		if ("Y".equals(revisit) || "y".equals(revisit)) {
			revisit = "Y";
		} else {
			revisit = "N";
		}

		// 查看日程是否存在
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

		DoctorHospital doctorHospital = this.doctorHospitalMapper
				.findByDoctorIdAndHospitalId(doctor.getId(), hospital.getId());

		if (null == doctorHospital) {
			response.setErrorCode(RegisterErrorCode.DOCTOR_HOSPITAL_NOT_EXIST);
			return response;
		}

		if (1 != doctorHospital.getStatus()) {
			response.setErrorCode(RegisterErrorCode.QUEQUE_NOT_ALLOWED);
			return response;
		}

		// 检查金额是否足够
		// 医生在每家医院要求的押金可能会不一样
		// 挂号诚意金
		int registerDeposit = doctorHospital.getDeposit();

		// 检查金额是否足够
		if (user.getBalance() < registerDeposit) {
			response.setErrorCode(RegisterErrorCode.MONEY_NOT_ENOUGH);
			return response;
		}
		// end

		// 按照queue_num由大到小排队
		List<Register> queued = registerMapper
				.findUnFinishedByUserIdDoctorIdHospitalId(userId, doctorId,
						hospitalId);

		if (!CollectionUtils.isEmpty(queued)) {
			response.setErrorCode(RegisterErrorCode.UNFINISH_REGISTER_EXIST);
			return response;
		}
		// end

		register.setUserId(userId);
		register.setUserName(user.getName());

		register.setPatientId(patientId);
		register.setPatientName(patient.getName());
		register.setDoctorId(doctor.getId());
		register.setDoctorName(doctor.getName());
		register.setHospitalId(hospital.getId());
		register.setHospitalName(hospital.getName());

		register.setDeposit(registerDeposit);

		register.setRevisit(revisit);
		if (StringUtils.isNotBlank(phenomenon))
			register.setPhenomenon(phenomenon);
		if (StringUtils.isNotBlank(attachNo))
			register.setAttachNo(attachNo);

		// 直接设置为挂号成功状态
		register.setStatus(Register.Status.QUEUE.getValue());

		int queueNum = 1;

		// 获取最后一个queueNum, 由于不清零，对于取消也不处理，所以这个queueNum基本对用户无用，
		// 只用于并发处理
		int status = Register.Status.QUEUE.getValue();
		List<Register> queueList = this.registerMapper
				.findByDoctorIdHospitalIdStatusOrderByCreateTime(doctorId,
						hospitalId, status);
		if (CollectionUtils.isNotEmpty(queueList)) {
			queueNum += queueList.size();
			// 更换算法
			// queueNum += queueList.get(queueList.size() - 1).getQueueNum();
		}

		// 之后保存register,排队数
		register.setSeq(queueNum);

		registerMapper.add(register);

		// FIXME 扣除诚意金
		transferService.executeTransferBalance(user.getId(),
				(-1 * registerDeposit), Transfer.Reason.GHCYZ.getId());
		// 患者排队消息
		commonMessageService.sendQueueMessage(user, doctor.getName(),
				hospital.getName(), queueNum, doctorHospital.getMinQueue(),
				INFORM_DAYS_IN_ADVANCE);

		LOG.info("***********************queueMessage sended!");

		// 队伍达到人数后通知医生
		if (queueNum >= doctorHospital.getMinQueue()) {

			User doctorUser = userMapper.findById(doctorId);
			if (doctorUser != null
					&& !StringUtils.isEmpty(doctorUser.getPhone())) {
				commonMessageService.sendReachPresetNumMessage(
						doctorUser.getPhone(), doctor.getName(),
						doctor.getHospitalName(), queueNum,
						doctorHospital.getMinQueue());
			}
		}
		// end

		List<Register> registers = new ArrayList<Register>();
		registers.add(register);

		response.setTotal(1L);
		response.setRegisters(registers);
		return response;
	}

	@Override
	public ResponseBody makeAppointment(long userId, long patientId,
			long scheduleId, String revisit, String phenomenon, String attachNo) {
		LOG.info(
				"****makeAppointment, userId={},patientId={},scheduleId={},revisit={},phenomenon={},attachNo={}",
				userId, patientId, scheduleId, revisit, phenomenon, attachNo);

		RegisterResponse response = new RegisterResponse();
		Register register = new Register();

		if ("Y".equals(revisit) || "y".equals(revisit)) {
			revisit = "Y";
		} else {
			revisit = "N";
		}

		// 查看日程是否存在
		Schedule schedule = scheduleMapper.findById(scheduleId);
		if (null == schedule) {
			response.setErrorCode(ScheduleErrorCode.SCHEDULE_NOT_FOUND);
			return response;
		}

		// FIXME 查看用户是否已经进行了挂号
		List<Register> existList = registerMapper.findByUserIdAndScheduleId(
				userId, scheduleId);
		if (!CollectionUtils.isEmpty(existList)) {
			response.setErrorCode(ScheduleErrorCode.ALREADY_HAS_REGISTERMSG);
			return response;
		}
		// end

		DoctorHospital doctorHospital = this.doctorHospitalMapper
				.findByDoctorIdAndHospitalId(schedule.getDoctorId(),
						schedule.getHospitalId());

		if (null == doctorHospital) {
			response.setErrorCode(RegisterErrorCode.DOCTOR_HOSPITAL_NOT_EXIST);
			return response;
		}

		// end

		// 对日程满的优先判断
		if (schedule.getRegisteredNum() >= schedule.getIssueNum()) {
			response.setErrorCode(RegisterErrorCode.REGISTERNUM_EXCEED);
			return response;
		}

		// 用户
		User user = this.userMapper.findById(userId);
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		// 挂号诚意金
		int registerDeposit = doctorHospital.getDeposit();

		// 检查金额是否足够
		if (user.getBalance() < registerDeposit) {
			response.setErrorCode(RegisterErrorCode.MONEY_NOT_ENOUGH);
			return response;
		}

		Patient patient = this.patientMapper.findById(patientId);

		if (patient == null) {
			LOG.warn(
					"-------------> patient is null, patientId={}, scheduleId={}",
					patientId, scheduleId);
			response.setErrorCode(RegisterErrorCode.PATIENT_ID_NOT_EXIST);
			return response;
		}

		register.setUserId(userId);
		register.setUserName(user.getName());

		register.setPatientId(patientId);
		register.setPatientName(patient.getName());
		// register.setCertificateId(patient.getCertificateId());
		// register.setUserRelation(patient.getUserRelation());
		register.setDeposit(registerDeposit);

		register.setRevisit(revisit);
		if (StringUtils.isNotBlank(phenomenon))
			register.setPhenomenon(phenomenon);
		if (StringUtils.isNotBlank(attachNo))
			register.setAttachNo(attachNo);

		register.setDoctorId(schedule.getDoctorId());
		register.setDoctorName(schedule.getDoctorName());
		register.setHospitalId(schedule.getHospitalId());
		register.setHospitalName(schedule.getHospitalName());
		register.setScheduleId(scheduleId);
		register.setScheduleDate(schedule.getScheduleDate());
		register.setTimeSlot(schedule.getTimeSlot());
		// 直接设置为挂号成功状态
		register.setStatus(Register.Status.SUCCESS.getValue());
		// FIXME 需要修改这里的生成规则
		register.setSeq(0);

		int oldRegisterNum = schedule.getRegisteredNum();
		int registerNum = oldRegisterNum + 1;
		int rows = scheduleMapper.acquireScheduleLock(scheduleId, registerNum,
				oldRegisterNum);
		// 无法 取得锁定
		if (rows < 1) {// 乐观锁未锁定住
			throw new LockFailureException("无法锁定已挂号数！");
		}

		// 之后保存register
		register.setSeq(registerNum);
		registerMapper.add(register);

		// FIXME 扣除诚意金
		transferService.executeTransferBalance(user.getId(),
				(-1 * registerDeposit), Transfer.Reason.GHCYZ.getId());

		commonMessageService.sendOrderMessage(register, user, patient, null);

		List<Register> registers = new ArrayList<Register>();
		registers.add(register);

		response.setTotal(1L);
		response.setRegisters(registers);

		return response;
	}

	@Override
	public RegisterResponse getSuccessRegisters(long doctorId, long hospitalId,
			Date scheduleDate) {
		RegisterResponse response = new RegisterResponse();
		int status = Register.Status.SUCCESS.getValue();

		List<Register> registers = registerMapper
				.findByDoctorIdAndHospitalIdAndScheduleDateAndStatus(doctorId,
						hospitalId, scheduleDate, status);
		// 转换文件地址
		transFileUrls(registers);
		response.setRegisters(registers);
		return response;
	}

	@Override
	public RegisterResponse getUnFinishedRegistersByDoctorId(long doctorId) {
		RegisterResponse response = new RegisterResponse();

		List<Register> registers = registerMapper
				.findUnFinishedRegistersByDoctorId(doctorId);
		// 转换文件地址
		transFileUrls(registers);
		response.setRegisters(registers);

		return response;
	}

	@Override
	public RegisterResponse findByDoctorIdScheduleDateOrderByUserName(
			long doctorId, Date scheduleDate) {
		RegisterResponse response = new RegisterResponse();
		response.setRegisters(registerMapper
				.findByDoctorIdScheduleDateOrderByUserName(doctorId,
						scheduleDate));
		return response;
	}

	@Override
	public RegisterResponse getRegistersByDoctorIdAndStatus(long doctorId,
			int status) {
		RegisterResponse response = new RegisterResponse();
		List<Register> registers = registerMapper.findByDoctorIdAndStatus(
				doctorId, status);
		transFileUrls(registers);
		response.setRegisters(registers);
		return response;
	}

	@Override
	public RegisterResponse finishTreatment(long registerId) {
		LOG.info("finishTreatment the registerId={}", registerId);

		RegisterResponse response = new RegisterResponse();
		Register register = new Register();
		register = registerMapper.findById(registerId);

		if (null == register) {
			response.setErrorCode(RegisterErrorCode.REGISTER_ID_NOT_EXIST);
			return response;
		}

		if (Status.TREATED.getValue() == register.getStatus()) {
			response.setErrorCode(RegisterErrorCode.ALREADY_FINISH_TREAT);
			return response;
		}

		register.setStatus(Register.Status.TREATED.getValue());
		registerMapper.updateRegister(register);

		// FIXME 归还诚意金金额
		transferService.executeTransferBalance(register.getUserId(),
				register.getDeposit(), Transfer.Reason.ZHTK.getId());

		Doctor doctor = this.doctorMapper.findById(register.getDoctorId());
		if (doctor == null) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
			return response;
		}
		// 诊疗完后为医生加分
		if (doctor.getUserId() > 0)
			this.transferService.executeTransferScore(doctor.getUserId(),
					this.successTreatDoctorScore, Transfer.Reason.ZLJF.getId());

		// 用户
		User user = this.userMapper.findById(register.getUserId());
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}

		this.commonMessageService.sendPatientFinishTreatmentMessage(register,
				user);

		// 设置医生奖励积分
		response.setBonusScore(this.successTreatDoctorScore);

		return response;
	}

	@Override
	public RegisterResponse getRegistersByUserIdAndStatus(long userId,
			int status) {
		RegisterResponse response = new RegisterResponse();
		List<Register> registers = registerMapper.findByUserIdAndStatus(userId,
				status);
		transFileUrls(registers);
		response.setRegisters(registers);

		return response;
	}

	@Override
	public RegisterResponse getUnFinishedRegistersByUserId(long userId) {
		RegisterResponse response = new RegisterResponse();

		LOG.info("userId={}", userId);
		response.setRegisters(registerMapper
				.findUnFinishedRegistersByUserId(userId));
		return response;
	}

	@Override
	public RegisterResponse getRegistersByUserIdScheduleId(long userId,
			long scheduleId) {
		RegisterResponse response = new RegisterResponse();

		LOG.info("userId={},scheduleId={}", userId, scheduleId);
		List<Register> registers = registerMapper.findByUserIdAndScheduleId(
				userId, scheduleId);
		// 转换文件地址
		transFileUrls(registers);
		response.setRegisters(registers);
		return response;
	}

	private void transFileUrls(List<Register> registers) {
		for (Register register : registers) {
			transFileUrls(register);
		}
	}

	private void transFileUrls(Register register) {
		if (null == register)
			return;
		String urls = register.getAttachNo();
		register.setAttachUrls(yusFileService.getTransYusFileUrl(urls));
	}

	@Override
	public RegisterResponse getRegisterById(long id) {
		RegisterResponse response = new RegisterResponse();

		LOG.info("id={} ", id);
		List<Register> registers = new ArrayList<Register>();
		Register register = registerMapper.findById(id);

		if (register == null) {
			response.setErrorCode(RegisterErrorCode.REGISTER_ID_NOT_EXIST);
			return response;
		}
		// 转换文件地址
		transFileUrls(register);

		Patient patient = this.patientMapper.findById(register.getPatientId());

		register.setPatient(patient);

		registers.add(registerMapper.findById(id));
		response.setRegisters(registers);
		return response;
	}

	@Override
	public RegisterResponse confirmQueue(long registerId) {
		RegisterResponse response = new RegisterResponse();

		Register register = this.registerMapper.findById(registerId);

		if (null == register) {
			response.setErrorCode(RegisterErrorCode.REGISTER_ID_NOT_EXIST);
			return response;
		}

		if (Register.Status.DRAFT.getValue() != register.getStatus()) {
			response.setErrorCode(RegisterErrorCode.REGISTER_NOT_QUERE);
			return response;
		}

		if (register.getScheduleId() == 0) {
			response.setErrorCode(RegisterErrorCode.REGISTER_SCHEDULE_NOT_FOUND);
			return response;
		}

		// 更改状态
		register.setStatus(Register.Status.SUCCESS.getValue());
		// this.registerMapper.updateStatusById(registerId,
		// Register.Status.SUCCESS.getValue());
		registerMapper.confirmRegister(register);

		Patient patient = this.patientMapper.findById(register.getPatientId());
		if (null == patient) {
			response.setErrorCode(PatientErrorCode.PATIENT_ID_NOT_EXIST);
			return response;
		}
		commonMessageService.sendOrderMessage(register, null, patient, null);

		return response;
	}

	// 医院处理并返回预约信息
	@Override
	public RegisterResponse updateHospitalRegister(long registerId,
			String hospitalRegisterId) {
		RegisterResponse response = new RegisterResponse();
		Register register = registerMapper.findById(registerId);
		if (register == null) {
			response.setErrorCode(RegisterErrorCode.REGISTER_ID_NOT_EXIST);
			return response;
		}

		if (StringUtils.isNotEmpty(hospitalRegisterId))
			register.setHospitalRegisterId(hospitalRegisterId);

		this.registerMapper.updateRegister(register);
		commonMessageService.sendOrderMessage(register, null, null, null);

		return response;
	}

	public RegisterResponse updateRegister(long registerId, String phenomenon,
			String attachNo) {
		RegisterResponse response = new RegisterResponse();
		Register register = registerMapper.findById(registerId);
		if (register == null) {
			response.setErrorCode(RegisterErrorCode.REGISTER_ID_NOT_EXIST);
			return response;
		}
		// 部位空的不允许上传
		if (StringUtils.isNotEmpty(phenomenon))
			register.setPhenomenon(phenomenon);

		if (StringUtils.isNotEmpty(attachNo))
			register.setAttachNo(attachNo);

		int rows = registerMapper.updateRegister(register);
		if (rows < 1) {
			throw new LockFailureException("无法更新挂号信息");
		}

		List<Register> registers = new ArrayList<Register>();
		registers.add(register);

		response.setTotal(1L);
		response.setRegisters(registers);
		return response;
	}

	@Override
	public RegisterResponse getRegistersByDoctorIdAndHospitalIdAndStatus(
			long doctorId, long hospitalId, int status) {
		RegisterResponse response = new RegisterResponse();
		List<Register> registers = registerMapper
				.findByDoctorIdAndHospitalIdAndStatus(doctorId, hospitalId,
						status);
		transFileUrls(registers);

		response.setRegisters(registers);
		return response;
	}

	@Override
	public RegisterResponse getUnFinishedRegistersByHospitalId(long hospitalId) {
		RegisterResponse response = new RegisterResponse();

		List<Register> registers = registerMapper
				.findUnFinishedRegistersByHospitalId(hospitalId);
		// 转换文件地址
		transFileUrls(registers);
		response.setRegisters(registers);

		return response;
	}
}
