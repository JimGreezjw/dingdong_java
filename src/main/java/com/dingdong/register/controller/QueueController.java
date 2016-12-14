//package com.dingdong.register.controller;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.dingdong.common.exception.CommonErrorCode;
//import com.dingdong.common.exception.LockFailureException;
//import com.dingdong.common.vo.ResponseBody;
//import com.dingdong.register.model.Queue;
//import com.dingdong.register.service.DoctorFanService;
//import com.dingdong.register.service.QueueService;
//import com.dingdong.register.vo.response.QueueMutiResponse;
//import com.dingdong.register.vo.response.QueueResponse;
//
///**
// * 用户预约服务；医生根据预约情况，确定出诊日程
// * 
// * @author chenliang
// * @version 1.0
// */
//@Controller
//@Api("排队信息")
//@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
//public class QueueController {
//	private static final Logger LOG = LoggerFactory
//			.getLogger(QueueController.class);
//	@Autowired
//	private QueueService queueService;
//	@Autowired
//	private DoctorFanService doctorFanService;
//
//	@ApiOperation(value = "用户排队", notes = "用户通过排队约出名医，需要指定医院", response = ResponseBody.class)
//	@RequestMapping(value = "/users/{userId}/queues", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
//	public ResponseEntity<ResponseBody> queueUp(
//			@ApiParam(value = "用户id", required = true) @PathVariable("userId") long userId,
//			@ApiParam(value = "医生id", required = true) @RequestParam("doctorId") long doctorId,
//			@ApiParam(value = "医院id", required = true) @RequestParam("hospitalId") long hospitalId) {
//		ResponseBody response = new ResponseBody();
//		try {
//			response = queueService.queueUp(userId, doctorId, hospitalId);
//			// 排队候自动成为医生的粉丝
//			this.doctorFanService.addDoctorFan(userId, doctorId);
//		} catch (Exception e) {
//			LOG.warn(e.getLocalizedMessage());
//			response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
//		}
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
//	@Deprecated
//	@ApiOperation(value = "用户在多个医院排队", notes = "用户在多个医院排队，医院id使用逗号分隔", response = ResponseBody.class)
//	@RequestMapping(value = "/users/{userId}/mutiQueueUp", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
//	public ResponseEntity<QueueMutiResponse> mutiQueueUp(
//			@ApiParam(value = "用户id", required = true) @PathVariable("userId") long userId,
//			@ApiParam(value = "医生id", required = true) @RequestParam("doctorId") long doctorId,
//			@ApiParam(value = "医院id，使用 , 分隔", required = true) @RequestParam("hospitalStr") String hospitalStr) {
//		LOG.info("userId={},doctorId={},hospitalStr={}", userId, doctorId,
//				hospitalStr);
//		QueueMutiResponse response = new QueueMutiResponse();
//		hospitalStr = hospitalStr.trim();
//		String[] hosStrs = hospitalStr.split(",");
//		Set<Long> hospitalIds = new HashSet<Long>();
//
//		for (String str : hosStrs) {
//			Long id = Long.parseLong(str);
//			hospitalIds.add(id);
//		}
//
//		try {
//			response = queueService.queueUp(userId, doctorId, hospitalIds);
//			// 排队候自动成为医生的粉丝
//			this.doctorFanService.addDoctorFan(userId, doctorId);
//		}
//
//		catch (LockFailureException e) {
//			LOG.warn(e.getLocalizedMessage());
//			response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
//		} catch (Exception e) {
//			LOG.warn(e.getLocalizedMessage());
//			response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
//		}
//
//		return new ResponseEntity<QueueMutiResponse>(response, HttpStatus.OK);
//	}
//
//	@ApiOperation(value = "获得某一用户的排队信息", notes = "")
//	@RequestMapping(value = "/users/{userId}/queues", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
//	public ResponseEntity<QueueResponse> getQueues(
//			@ApiParam(value = "用户id", required = true) @PathVariable("userId") long userId) {
//		QueueResponse response = queueService.getByUserId(userId);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
//	@ApiOperation(value = "取消排队", notes = "按照排队号", response = ResponseBody.class)
//	@RequestMapping(value = "/queues/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
//	public ResponseEntity<ResponseBody> cancelQueue(
//			@ApiParam(value = "排队编号", required = true) @PathVariable(value = "id") long id) {
//		ResponseBody response = queueService.cancelQueue(id);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
//	@ApiOperation(value = "获得名医的排队信息", notes = "")
//	@RequestMapping(value = "/doctors/{doctorId}/queues", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
//	public ResponseEntity<QueueResponse> getQueues(
//			@ApiParam(value = "名医id", required = true) @PathVariable("doctorId") long doctorId,
//			@ApiParam(value = "医院id", required = true) @RequestParam(value = "hospitalId", required = true) long hospitalId) {
//		QueueResponse response = queueService.getByDoctorIdHospitalId(doctorId,
//				hospitalId);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
//	@ApiOperation(value = "统计某医生在各医院的排队信息", notes = "统计名医的排队信息，包括总数量和各个医院的分数量")
//	@RequestMapping(value = "/doctors/{doctorId}/statQueue", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
//	public ResponseEntity<QueueResponse> statQueueNumByHospital(
//			@ApiParam(value = "名医id", required = true) @PathVariable("doctorId") long doctorId) {
//		QueueResponse response = queueService.statQueueNumByHospital(doctorId);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
//
//	@ApiOperation(value = "统计某医院各医生的排队信息", notes = "包括总数量和各个医生的分数量")
//	@RequestMapping(value = "/hospitals/{hospitalId}/statQueue", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
//	public ResponseEntity<QueueResponse> statQueueNumByDoctor(
//			@ApiParam(value = "医院id", required = true) @PathVariable("hospitalId") long hospitalId) {
//		QueueResponse response = queueService.statQueueNumByDoctor(hospitalId,
//				Queue.Status.WAITING.getStatus());
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
// }
