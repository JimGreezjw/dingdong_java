//package com.dingdong.register.service.impl;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.dingdong.common.exception.CommonErrorCode;
//import com.dingdong.common.exception.LockFailureException;
//import com.dingdong.common.vo.ResponseBody;
//import com.dingdong.register.exception.DoctorErrorCode;
//import com.dingdong.register.exception.QueueErrorCode;
//import com.dingdong.register.exception.RegisterErrorCode;
//import com.dingdong.register.mapper.DoctorHospitalMapper;
//import com.dingdong.register.mapper.DoctorMapper;
//import com.dingdong.register.mapper.HospitalMapper;
//import com.dingdong.register.mapper.QueueMapper;
//import com.dingdong.register.model.Doctor;
//import com.dingdong.register.model.DoctorHospital;
//import com.dingdong.register.model.Hospital;
//import com.dingdong.register.model.Queue;
//import com.dingdong.register.service.QueueService;
//import com.dingdong.register.vo.response.QueueMutiResponse;
//import com.dingdong.register.vo.response.QueueResponse;
//import com.dingdong.register.vo.response.QueueSingleResponse;
//import com.dingdong.sys.exception.SysErrorCode;
//import com.dingdong.sys.mapper.UserMapper;
//import com.dingdong.sys.model.User;
//import com.dingdong.sys.service.CommonMessageService;
//import com.dingdong.sys.service.DDMZService;
//
///**
// * 预约服务
// * 
// * @author chenliang
// * @version 2015年12月13日 上午1:34:49
// */
//@Service
//@Transactional
//public class QueueServiceImpl implements QueueService {
//
//	private static final Logger LOG = LoggerFactory
//			.getLogger(QueueServiceImpl.class);
//
//	@Autowired
//	private QueueMapper queueMapper;
//	@Autowired
//	private HospitalMapper hospitalMapper;
//
//	@Autowired
//	private DoctorHospitalMapper doctorHospitalMapper;
//
//	@Autowired
//	private DoctorMapper doctorMapper;
//
//	@Autowired
//	private UserMapper userMapper;
//
//	@Autowired
//	private CommonMessageService commonMessageService;
//
//	@Autowired
//	private DDMZService wechatService;
//
//	// 提前通知的天数
//	public static int INFORM_DAYS_IN_ADVANCE = 7;
//
//	// 是否采用模糊排队号
//	public static boolean USE_FUZZY_QUEUENUM = true;
//
//	// #排队用户通知方法 ，WX-微信通知， SMS-短信通知
//	@Value("#{config['msg.queueNotifyMethod']}")
//	private String queueNotifyMethod = "SMS";
//
//	@Override
//	public QueueSingleResponse queueUp(long userId, long doctorId,
//			long hospitalId) {
//		LOG.info(
//				"************************** userId={},doctorId={},hospitalId={}",
//				userId, doctorId, hospitalId);
//		QueueSingleResponse response = new QueueSingleResponse();
//		int status = Queue.Status.WAITING.getStatus();
//		User user = userMapper.findById(userId);
//		if (null == user) {
//			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
//			return response;
//		}
//
//		if (StringUtils.isBlank(user.getPhone())) {
//			response.setErrorCode(SysErrorCode.PHONE_ID_NOT_EXIST);
//			return response;
//		}
//
//		Doctor doctor = doctorMapper.findById(doctorId);
//		if (null == doctor) {
//			response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
//			return response;
//		}
//
//		Hospital hospital = hospitalMapper.findById(hospitalId);
//
//		if (null == hospital) {
//			response.setErrorCode(RegisterErrorCode.HOSPITAL_ID_NOT_EXIST);
//			return response;
//		}
//
//		DoctorHospital doctorHospital = this.doctorHospitalMapper
//				.findByDoctorIdAndHospitalId(doctor.getId(), hospital.getId());
//
//		if (null == doctorHospital) {
//			response.setErrorCode(RegisterErrorCode.DOCTOR_HOSPITAL_NOT_EXIST);
//			return response;
//		}
//
//		if (1 != doctorHospital.getStatus()
//				|| 0 == doctorHospital.getMinQueue()) {
//			response.setErrorCode(RegisterErrorCode.QUEQUE_NOT_ALLOWED);
//			return response;
//		}
//
//		// 按照queue_num由大到小排队
//		List<Queue> queued = queueMapper
//				.findByUserIdAndDoctorIdAndHospitalIdAndStatus(userId,
//						doctorId, hospitalId, status);
//
//		if (!CollectionUtils.isEmpty(queued)) {
//			response.setErrorCode(QueueErrorCode.QUEUE_ALREADY_EXIST);
//			return response;
//		}
//
//		int queueNum = 1;
//		Queue selfQueue = null;
//
//		// 获取最后一个queueNum, 由于不清零，对于取消也不处理，所以这个queueNum基本对用户无用，
//		// 只用于并发处理
//		List<Queue> queueList = this.queueMapper
//				.findByDoctotrIdAndHospitalIdAndStatusOrderByCreateTime(
//						doctorId, hospitalId, status);
//		if (CollectionUtils.isNotEmpty(queueList)) {
//			// queueNum += queueList.size();
//			// 更换算法
//			queueNum += queueList.get(queueList.size() - 1).getQueueNum();
//		}
//
//		// queueNum首先设置为0
//		selfQueue = this.geneQueue(user, doctor, hospital, 0);
//		// 采用先添加,再更新排队号的策略
//		queueMapper.add(selfQueue);
//
//		int rows = queueMapper.updateQueueNum(selfQueue.getId(),
//				selfQueue.getDoctorId(), selfQueue.getHospitalId(), queueNum,
//				queueNum);
//
//		if (rows < 1) {
//			// response.setErrorCode(QueueErrorCode.QUEUE_EXCEPTION);
//			// return response;
//			throw new LockFailureException("排队锁定发生异常！");
//		}
//
//		String strUser = "用户";
//		if (queueNotifyMethod.equalsIgnoreCase("WX")
//				&& StringUtils.isNotBlank(user.getOpenId())) {
//			String content = String.format(
//					"【叮咚门诊】尊敬的%s，您已成功加入%s大夫的约诊队列，就诊地点为%s，"
//							+ "排队号码是%d。当排队人数达到%d人时，叮咚门诊会与%s大夫确定出诊日期，"
//							+ "并在出诊前%d天通知您进行正式预约，请您耐心等待。", strUser,
//					doctor.getName(), hospital.getName(), queueNum,
//					doctorHospital.getMinQueue(), doctor.getName(),
//					INFORM_DAYS_IN_ADVANCE);
//
//			this.wechatService.sendCustomTextMsg(user.getOpenId(), content);
//		} else
//			commonMessageService.sendQueueMessage(user.getPhone(), strUser,
//					doctor.getName(), hospital.getName(), queueNum,
//					doctorHospital.getMinQueue(), INFORM_DAYS_IN_ADVANCE);
//
//		// 队伍达到人数后通知医生
//		if (queueNum >= doctorHospital.getMinQueue()) {
//
//			User doctorUser = userMapper.findById(doctorId);
//			if (doctorUser != null
//					&& !StringUtils.isEmpty(doctorUser.getPhone())) {
//				commonMessageService.sendReachPresetNumMessage(
//						doctorUser.getPhone(), doctor.getName(),
//						doctor.getHospitalName(), queueNum,
//						doctorHospital.getMinQueue());
//			}
//		}
//		// end
//
//		// 返回排队信息
//		response.setQueue(selfQueue);
//		// end
//		return response;
//	}
//
//	private Queue geneQueue(User user, Doctor doctor, Hospital hospital,
//			int queueNum) {
//		// long doctorId = request.getDoctorId();
//		// long hospitalId = request.getHospitalId();
//		// Date appointmentTime = request.getAppointmentTime();
//
//		Queue queue = new Queue();
//		// queue.setAppointmentTime(appointmentTime);
//		queue.setDoctorId(doctor.getId());
//		queue.setDoctorName(doctor.getName());
//
//		queue.setHospitalId(hospital.getId());
//		queue.setHospitalName(hospital.getName());
//
//		queue.setUserId(user.getId());
//		queue.setUserName(user.getName());
//		queue.setStatus(Queue.Status.WAITING.getStatus());
//
//		queue.setQueueNum(queueNum);
//
//		return queue;
//	}
//
//	@Override
//	public QueueMutiResponse queueUp(long userId, long doctorId,
//			Set<Long> hospitalIds) {
//		QueueMutiResponse response = new QueueMutiResponse();
//		response.setSuccessQueues(new ArrayList<Queue>());
//
//		StringBuffer msg = new StringBuffer();
//
//		if (!CollectionUtils.isEmpty(hospitalIds)) {
//			for (Long hospitalId : hospitalIds) {
//				if (hospitalId == null || hospitalId == 0) {
//					continue;
//				}
//
//				Hospital hospital = this.hospitalMapper.findById(hospitalId);
//
//				QueueSingleResponse singleResponse = this.queueUp(userId,
//						doctorId, hospitalId);
//				if (singleResponse.getResponseStatus() == CommonErrorCode.OK
//						.getCode()) {
//					// 添加排队信息
//					msg.append(hospital.getName() + "排队成功。\n");
//					response.getSuccessQueues().add(singleResponse.getQueue());
//				} else {
//					msg.append(hospital.getName() + "排队失败，原因："
//							+ singleResponse.getResponseDesc() + "\n");
//					// // 失败数量
//					// int oldFailNum = response.getFailureNum();
//					// response.setFailureNum(oldFailNum + 1);
//				}
//			}
//		}
//
//		response.setResponseDesc(msg.toString());
//		return response;
//	}
//
//	@Override
//	public ResponseBody cancelQueue(long queueId) {
//		ResponseBody response = new ResponseBody();
//		Queue queue = queueMapper.findById(queueId);
//		if (null == queue) {
//			response.setErrorCode(QueueErrorCode.QUEUE_NOT_FOUND);
//			return response;
//		}
//		int status = queue.getStatus();
//		if (status == Queue.Status.SUCCESS.getStatus()
//				|| status == Queue.Status.CANCEL.getStatus()) {
//			response.setErrorCode(QueueErrorCode.QUEUE_CANNOT_CANCEL);
//			return response;
//		}
//		queue.setStatus(Queue.Status.CANCEL.getStatus());
//		queueMapper.save(queue);
//		return response;
//	}
//
//	@Override
//	public QueueResponse statQueueNumByHospital(long doctorId) {
//		// 直接调用等待状态
//		return this.statQueueNumByHospital(doctorId,
//				Queue.Status.WAITING.getStatus());
//	}
//
//	@Override
//	public QueueResponse statQueueNumByHospital(long doctorId, int status) {
//		QueueResponse response = new QueueResponse();
//		List<Queue> queues = queueMapper.statQueueNumByHospital(doctorId,
//				status);
//
//		// 开始统计数量
//		if (!CollectionUtils.isEmpty(queues)) {
//			int totalNum = 0;
//			for (Queue queue : queues) {
//				totalNum += queue.getQueueNum();
//			}
//
//			// 设置总数量
//			response.setQueueNum(totalNum);
//		}
//
//		// 只做查询使用
//		response.setQueues(queues);
//		return response;
//	}
//
//	@Override
//	public QueueResponse getByDoctorIdHospitalId(long doctorId, long hospitalId) {
//		QueueResponse response = new QueueResponse();
//		int status = Queue.Status.WAITING.getStatus();
//		List<Queue> queues = queueMapper
//				.findByDoctotrIdAndHospitalIdAndStatusOrderByCreateTime(
//						doctorId, hospitalId, status);
//		if (null == queues) {
//			response.setErrorCode(QueueErrorCode.QUEUE_NOT_FOUND);
//			return response;
//		}
//		Set<Long> userIds = new HashSet<>();
//		List<Queue> distinctQueues = new ArrayList<>();
//		for (Queue queue : queues) {
//			long userId = queue.getUserId();
//			if (!userIds.contains(userId)) {
//				userIds.add(userId);
//				distinctQueues.add(queue);
//			}
//		}
//		response.setQueues(distinctQueues);
//		response.setQueueNum(distinctQueues.size());
//		return response;
//	}
//
//	@Override
//	public QueueResponse getByUserId(long userId) {
//		QueueResponse response = new QueueResponse();
//		int status = Queue.Status.WAITING.getStatus();
//		List<Queue> queues = queueMapper
//				.findByUserIdAndStatusOrderByCreateTime(userId, status);
//		if (null == queues) {
//			response.setErrorCode(QueueErrorCode.QUEUE_NOT_FOUND);
//			return response;
//		}
//
//		response.setQueues(queues);
//		response.setQueueNum(queues.size());
//		return response;
//	}
//
//	@Override
//	public QueueResponse statQueueNumByDoctor(long hospitalId, int status) {
//		QueueResponse response = new QueueResponse();
//		List<Queue> queues = queueMapper.statQueueNumByDoctor(hospitalId,
//				status);
//
//		// 开始统计数量
//		if (!CollectionUtils.isEmpty(queues)) {
//			int totalNum = 0;
//			for (Queue queue : queues) {
//				totalNum += queue.getQueueNum();
//			}
//
//			// 设置总数量
//			response.setQueueNum(totalNum);
//		}
//
//		// 只做查询使用
//		response.setQueues(queues);
//		return response;
//	}
// }
