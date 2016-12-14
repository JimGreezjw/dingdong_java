package com.dingdong.register.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dingdong.common.exception.CommonErrorCode;
import com.dingdong.common.exception.LockFailureException;
import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.Schedule;
import com.dingdong.register.service.DoctorFanService;
import com.dingdong.register.service.RegisterService;
import com.dingdong.register.service.ScheduleService;
import com.dingdong.register.vo.response.RegisterResponse;

/**
 * 用户预约服务；医生根据预约情况，确定出诊日程
 * 
 * @author chenliang
 * @version 1.0
 */
@Controller
@Api("挂号信息")
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class RegisterController {
	private static final Logger LOG = LoggerFactory
			.getLogger(RegisterController.class);

	private static final int LIMITED = 100;

	@Autowired
	private RegisterService registerService;
	@Autowired
	private DoctorFanService doctorFanService;

	@Autowired
	private ScheduleService scheduleService;

	@ApiOperation(value = "验证账户金额", notes = "验证账户金额")
	@RequestMapping(value = "/users/{userId}/validateUserAccount", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ResponseBody> validateUserAccount(
			@ApiParam(value = "用户id", required = true) @PathVariable("userId") long userId,
			@ApiParam(value = "医生id", required = true) @RequestParam(value = "doctorId", required = true) long doctorId,
			@ApiParam(value = "医院id", required = true) @RequestParam(value = "hospitalId", required = true) long hospitalId) {
		ResponseBody response = registerService.validateUserAccount(userId,
				doctorId, hospitalId);
		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	// @ApiOperation(value = "用户确认预约", notes =
	// "用户排队成功后，在叮咚中确认预约挂号，输入患者的姓名和身份证号码,病情描述等信息。"
	// + "附件编号为微信上传后" + " userRelation:与患者关系,0-本人,1-家人,2-亲戚,3-朋友,4-其他", response
	// = ResponseBody.class)
	// @RequestMapping(value = "/users/{userId}/confirmRegister", produces = {
	// MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	// public ResponseEntity<RegisterResponse> confirmRegister(
	// @ApiParam(value = "预约挂号id", required = true) @RequestParam("registerId")
	// long registerId,
	// @ApiParam(value = "患者编号", required = true) @RequestParam("patientId")
	// long patientId,
	// // @ApiParam(value = "患者姓名", required = true) @RequestParam(value =
	// // "patientName", required = true) String patientName,
	// // @ApiParam(value = "患者身份证号", required = true) @RequestParam(value
	// // = "certiNo", required = true) String certiNo,
	// // @ApiParam(value = "与用户关系", required = true, defaultValue = "0")
	// // @RequestParam(value = "userRelation", required = true,
	// // defaultValue = "0") int userRelation,
	// @ApiParam(value = "是否复诊，输入Y或者N", defaultValue = "N") @RequestParam(value
	// = "revisit", defaultValue = "N") String revisit,
	// @ApiParam(value = "病症描述") @RequestParam(value = "phenomenon", required =
	// false) String phenomenon,
	// @ApiParam(value = "附件编号") @RequestParam(value = "attachNo", required =
	// false) String attachNo) {
	// RegisterResponse response = null;
	// for (int i = 0; i++ < LIMITED;) {
	// try {
	// response = registerService.confirmRegister(registerId,
	// patientId, revisit, phenomenon, attachNo);
	// break;
	// } catch (LockFailureException e) {// 如果有无法锁定的错误，则直接跳入重试
	// LOG.warn(e.getLocalizedMessage());
	// } catch (Exception e) {// 其它运行时错误
	// LOG.warn(e.getLocalizedMessage());
	// response = new RegisterResponse();
	// response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
	// break;
	// }
	// }
	//
	// return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	// }

	@ApiOperation(value = "用户确认预约", notes = "用户排队成功后，在叮咚中确认预约挂号", response = ResponseBody.class)
	@RequestMapping(value = "/registers/{id}/confirmQueue", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<RegisterResponse> confirmQueue(
			@ApiParam(value = "预约挂号id", required = true) @PathVariable("id") long registerId) {
		RegisterResponse response = null;
		for (int i = 0; i++ < LIMITED;) {
			try {
				response = registerService.confirmQueue(registerId);
				break;
			} catch (LockFailureException e) {// 如果有无法锁定的错误，则直接跳入重试
				LOG.warn(e.getLocalizedMessage());
			} catch (Exception e) {// 其它运行时错误
				LOG.warn(e.getLocalizedMessage());
				response = new RegisterResponse();
				response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
				break;
			}
		}

		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新挂医院的真正预约号号、信息", notes = "更新挂号信息")
	@RequestMapping(value = "/registers/{id}/hospitalRegister", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<RegisterResponse> updateHospitalRegister(
			@ApiParam(value = "预约挂号id", required = true) @PathVariable("id") long registerId,
			@ApiParam(value = "医院预约号") @RequestParam(value = "hospitalRegisterId", required = true) String hospitalRegisterId) {
		RegisterResponse response = new RegisterResponse();

		try {
			response = registerService.updateHospitalRegister(registerId,
					hospitalRegisterId);

		} catch (Exception e) {// 其它运行时错误
			LOG.warn(e.getLocalizedMessage());

		}

		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新挂号信息", notes = "更新挂号信息")
	@RequestMapping(value = "/registers/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<RegisterResponse> updateRegister(
			@ApiParam(value = "预约挂号id", required = true) @PathVariable("id") long registerId,
			@ApiParam(value = "病症描述") @RequestParam(value = "phenomenon", required = false) String phenomenon,
			@ApiParam(value = "附件编号") @RequestParam(value = "attachNo", required = false) String attachNo) {
		RegisterResponse response = new RegisterResponse();
		for (int i = 0; i++ < LIMITED;) {
			try {
				response = registerService.updateRegister(registerId,
						phenomenon, attachNo);
				break;
			} catch (LockFailureException e) {// 如果有无法锁定的错误，则直接跳入重试
				LOG.warn(e.getLocalizedMessage());
			} catch (Exception e) {// 其它运行时错误
				LOG.warn(e.getLocalizedMessage());
				response = new RegisterResponse();
				response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
				break;
			}

		}

		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "取消挂号", notes = "取消挂号")
	@RequestMapping(value = "/registers/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<RegisterResponse> cancelRegister(
			@ApiParam(value = "预约挂号id", required = true) @PathVariable("id") long registerId) {
		RegisterResponse response = new RegisterResponse();
		for (int i = 0; i++ < LIMITED;) {
			try {
				response = registerService.cancelRegister(registerId);
				break;
			} catch (LockFailureException e) {// 如果有无法锁定的错误，则直接跳入重试
				LOG.warn(e.getLocalizedMessage());
			} catch (Exception e) {// 其它运行时错误
				LOG.warn(e.getLocalizedMessage());
				response = new RegisterResponse();
				response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
				break;
			}

		}

		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "取消排队", notes = "取消排队")
	@RequestMapping(value = "/registers/queues/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<RegisterResponse> cancelQueue(
			@ApiParam(value = "预约挂号id", required = true) @PathVariable("id") long id) {
		RegisterResponse response = new RegisterResponse();
		for (int i = 0; i++ < LIMITED;) {
			try {
				response = registerService.cancelQueue(id);
				break;
			} catch (LockFailureException e) {
				// 如果有无法锁定的错误，则直接跳入重试
				LOG.warn(e.getLocalizedMessage());
			} catch (Exception e) {// 其它运行时错误
				LOG.warn(e.getLocalizedMessage());
				response = new RegisterResponse();
				response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
				break;
			}

		}

		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	// @Deprecated
	// @ApiOperation(value = "用户预约挂号", notes = "用户预约名医,预约挂号", response =
	// ResponseBody.class)
	// @RequestMapping(value = "/users/{userId}/registers", produces = {
	// MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	// public ResponseEntity<ResponseBody> makeAppointment(
	// @ApiParam(value = "用户id", required = true) @PathVariable("userId") long
	// userId,
	// @ApiParam(value = "病患id", required = true) @RequestParam("patientId")
	// long patientId,
	// @ApiParam(value = "日程id", required = true) @RequestParam(value =
	// "scheduleId", required = true) long scheduleId) {
	// ResponseBody response = null;
	// for (int i = 0; i++ < LIMITED;) {
	// response = registerService.makeAppointment(userId, patientId,
	// scheduleId);
	// if (response.getResponseStatus() == CommonErrorCode.OK.getCode()) {
	// return new ResponseEntity<>(response, HttpStatus.OK);
	// } else if (response.getResponseStatus() !=
	// RegisterErrorCode.REGISTER_FAILED
	// .getCode()) {
	// return new ResponseEntity<>(response, HttpStatus.OK);
	// }
	// }
	// response = new ResponseBody();
	// response.setErrorCode(RegisterErrorCode.REGISTER_FAILED);
	// return new ResponseEntity<>(response, HttpStatus.OK);
	// }

	@ApiOperation(value = "用户直接排队", notes = "", response = ResponseBody.class)
	@RequestMapping(value = "/registers/queueUp", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<RegisterResponse> queueUp(
			@ApiParam(value = "用户id", required = true) @RequestParam("userId") long userId,
			@ApiParam(value = "患者编号", required = true) @RequestParam("patientId") long patientId,
			@ApiParam(value = "医生id", required = true) @RequestParam(value = "doctorId", required = true) long doctorId,
			@ApiParam(value = "医院id", required = true) @RequestParam(value = "hospitalId", required = true) long hospitalId,
			@ApiParam(value = "是否复诊", defaultValue = "N") @RequestParam(value = "revisit", defaultValue = "N", required = false) String revisit,
			@ApiParam(value = "病症描述") @RequestParam(value = "phenomenon", required = false) String phenomenon,
			@ApiParam(value = "附件编号") @RequestParam(value = "attachNo", required = false) String attachNo) {
		RegisterResponse response = new RegisterResponse();
		for (int i = 0; i++ < LIMITED;) {
			try {
				response = registerService.queueUp(userId, patientId, doctorId,
						hospitalId, revisit, phenomenon, attachNo);
				break;
			} catch (LockFailureException e) {// 如果有无法锁定的错误，则直接跳入重试
				LOG.warn(e.getLocalizedMessage());
			} catch (Exception e) {// 其它运行时错误直接退出
				LOG.warn(e.getLocalizedMessage());
				response = new RegisterResponse();
				response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
				break;
			}

		}

		// 排队候自动成为医生的粉丝,独立事务，不影响核心逻辑
		try {
			this.doctorFanService.addDoctorFan(userId, doctorId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.warn("添加粉丝失败。\n" + e.getMessage());
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "用户直接预约", notes = "用户预约挂号,输入患者的姓名和身份证号码,病情描述等信息。"
			+ "userRelation:与患者关系,0-本人,1-家人,2-亲戚,3-朋友,4-其他", response = ResponseBody.class)
	@RequestMapping(value = "/users/{userId}/directRegister", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<ResponseBody> directMakeAppointment(
			@ApiParam(value = "用户id", required = true) @PathVariable("userId") long userId,
			@ApiParam(value = "患者编号", required = true) @RequestParam("patientId") long patientId,
			// @ApiParam(value = "患者姓名", required = true)
			// @RequestParam("patientName") String patientName,
			// @ApiParam(value = "患者身份证号", required = true)
			// @RequestParam("certiNo") String certiNo,
			// @ApiParam(value = "患者与用户的关系", required = true, defaultValue =
			// "0") @RequestParam(value = "userRelation", required = true,
			// defaultValue = "0") int userRelation,
			@ApiParam(value = "日程id", required = true) @RequestParam(value = "scheduleId", required = true) long scheduleId,
			@ApiParam(value = "是否复诊", defaultValue = "N") @RequestParam(value = "revisit", defaultValue = "N", required = false) String revisit,
			@ApiParam(value = "病症描述") @RequestParam(value = "phenomenon", required = false) String phenomenon,
			@ApiParam(value = "附件编号") @RequestParam(value = "attachNo", required = false) String attachNo) {
		ResponseBody response = new ResponseBody();
		for (int i = 0; i++ < LIMITED;) {
			try {
				response = registerService.makeAppointment(userId, patientId,
						scheduleId, revisit, phenomenon, attachNo);
				break;
			} catch (LockFailureException e) {// 如果有无法锁定的错误，则直接跳入重试
				LOG.warn(e.getLocalizedMessage());
			} catch (Exception e) {// 其它运行时错误直接退出
				LOG.warn(e.getLocalizedMessage());
				response = new RegisterResponse();
				response.setErrorCode(CommonErrorCode.UNKNOWN_EXCEPTION);
				break;
			}

		}

		// 排队候自动成为医生的粉丝,独立事务，不影响核心逻辑
		try {
			Schedule schedule = this.scheduleService.findById(scheduleId);
			if (schedule != null && schedule.getDoctorId() > 0)
				this.doctorFanService.addDoctorFan(userId,
						schedule.getDoctorId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.warn("添加粉丝失败。\n" + e.getMessage());
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/*
	 * @ApiOperation(value = "医生查询成功挂号信息", notes = "医生查询-查询某一天已成功挂号的病患信息")
	 * 
	 * @RequestMapping(value = "/doctors/{doctorId}/registers/success", produces
	 * = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	 * public ResponseEntity<RegisterResponse> getSuccessSchedule(
	 * 
	 * @ApiParam(value = "名医id", required = true, defaultValue = "0")
	 * 
	 * @PathVariable(value = "doctorId") long doctorId,
	 * 
	 * @ApiParam(value = "医院id", required = true, defaultValue = "0")
	 * 
	 * @RequestParam(value = "hospitalId", required = true, defaultValue = "0")
	 * long hospitalId,
	 * 
	 * @ApiParam(value = "就医日期", required = true) @RequestParam(value =
	 * "scheduleDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd")
	 * Date scheduleDate) { RegisterResponse response =
	 * registerService.getSuccessRegisters( doctorId, hospitalId, scheduleDate);
	 * return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK); }
	 */
	@ApiOperation(value = "医生查询待诊患者", notes = "查询已经预约和成功挂号的病患信息，状态位 1-已预约，2-已挂号")
	@RequestMapping(value = "/doctors/{doctorId}/registers/unfinished", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<RegisterResponse> getDoctorRegisters(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") long doctorId) {
		RegisterResponse response = registerService
				.getUnFinishedRegistersByDoctorId(doctorId);
		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "查询某一医院待诊患者", notes = "查询某一医院已经预约和成功挂号的病患信息")
	@RequestMapping(value = "/hospitals/{hospitalId}/registers/unfinished", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<RegisterResponse> getHospitalUnFinishedRegisters(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "hospitalId") long hospitalId) {
		RegisterResponse response = registerService
				.getUnFinishedRegistersByHospitalId(hospitalId);
		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "医生查询某日患者", notes = "包括状态，状态位 1-已预约，2-已挂号，3——已诊疗完成")
	@RequestMapping(value = "/doctors/{doctorId}/registers/scheduleDate", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<RegisterResponse> findByDoctorIdScheduleDateOrderByUserName(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") long doctorId,
			@ApiParam(value = "就诊日期", required = true, defaultValue = "2016-01-16") @RequestParam(value = "scheduleDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date scheduleDate) {
		RegisterResponse response = registerService
				.findByDoctorIdScheduleDateOrderByUserName(doctorId,
						scheduleDate);
		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "医生查询已经某个状态下的预约信息", notes = "医生查询-查询某个状态下的预约信息，挂号状态    "
			+ "-1--排队中 ，0—草稿，1-已预约，2-已挂号，3-已诊疗，4-取消，5-已评价")
	@RequestMapping(value = "/doctors/{doctorId}/registers/status", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<RegisterResponse> getRegistersByDoctorIdAndStatus(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") long doctorId,
			@ApiParam(value = "预约状态", required = true, defaultValue = "0") @RequestParam(value = "status") int status) {
		RegisterResponse response = registerService
				.getRegistersByDoctorIdAndStatus(doctorId, status);
		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "医生查询已经在某个医院某个状态下的预约信息", notes = "医生查询-查询某个状态下的预约信息，挂号状态    "
			+ "-1--排队中 ，0—草稿，1-已预约，2-已挂号，3-已诊疗，4-取消，5-已评价")
	@RequestMapping(value = "/doctors/{doctorId}/hospitals/{hospitalId}/registers/status", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<RegisterResponse> getRegistersByDoctorIdAndStatus(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") long doctorId,
			@ApiParam(value = "医院id", required = true, defaultValue = "0") @PathVariable(value = "hospitalId") long hospitalId,
			@ApiParam(value = "预约状态", required = true, defaultValue = "0") @RequestParam(value = "status") int status) {
		RegisterResponse response = registerService
				.getRegistersByDoctorIdAndHospitalIdAndStatus(doctorId,
						hospitalId, status);
		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "病人查询已经某个状态下的预约信息", notes = "病患查询-查询某个状态下的预约信息，挂号状态   "
			+ "状态参数 ,-1--排队中，0—草稿，1-已预约，2-已挂号，3-已诊疗，4-取消，5-已评价")
	@RequestMapping(value = "/users/{userId}/registers/status", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<RegisterResponse> getRegistersByUserIdAndStatus(
			@ApiParam(value = "病人id", required = true, defaultValue = "0") @PathVariable(value = "userId") long userId,
			@ApiParam(value = "预约状态", required = true, defaultValue = "0") @RequestParam(value = "status") int status) {
		RegisterResponse response = registerService
				.getRegistersByUserIdAndStatus(userId, status);
		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "病人查询未完成的预约信息", notes = "包括挂号状态    //"
			+ " 状态 ,-1--排队中，0—草稿，1-已预约，2-已挂号")
	@RequestMapping(value = "/users/{userId}/registers/unfinished", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<RegisterResponse> getUnFinishedRegistersByUserId(
			@ApiParam(value = "病人id", required = true, defaultValue = "0") @PathVariable(value = "userId") long userId) {
		RegisterResponse response = registerService
				.getUnFinishedRegistersByUserId(userId);
		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "查询某一用户某一日程的预约信息", notes = "用于检验用户是否重复点击预约")
	@RequestMapping(value = "/users/{userId}/schedules/{scheduleId}/registers", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<RegisterResponse> getRegistersByUserIdScheduleId(
			@ApiParam(value = "病人id", required = true, defaultValue = "0") @PathVariable(value = "userId") long userId,
			@ApiParam(value = "日程id", required = true, defaultValue = "0") @PathVariable(value = "scheduleId") long scheduleId) {
		RegisterResponse response = registerService
				.getRegistersByUserIdScheduleId(userId, scheduleId);
		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "医生确认患者已完成诊疗", notes = "", response = ResponseBody.class)
	@RequestMapping(value = "/registers/{registerId}/finish", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> finishTreatment(
			@ApiParam(value = "预约id", required = true) @PathVariable(value = "registerId") long registerId) {
		ResponseBody response = new ResponseBody();
		response = registerService.finishTreatment(registerId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "查询某一指定编号的挂号信息", notes = "")
	@RequestMapping(value = "registers/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<RegisterResponse> getRegistersById(
			@ApiParam(value = "预约挂号id", required = true) @PathVariable(value = "id") long id) {
		RegisterResponse response = registerService.getRegisterById(id);
		return new ResponseEntity<RegisterResponse>(response, HttpStatus.OK);
	}

}
