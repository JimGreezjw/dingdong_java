package com.dingdong.register.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.conf.PageUtils;
import com.dingdong.register.model.Schedule;
import com.dingdong.register.service.ScheduleService;
import com.dingdong.register.vo.request.ScheduleRequest;
import com.dingdong.register.vo.response.ScheduleDateResponse;
import com.dingdong.register.vo.response.ScheduleResponse;

/**
 * 用户预约服务；医生根据预约情况，确定出诊日程
 * 
 * @author chenliang
 * @version 1.0
 */
@Controller
@Api("医生日程信息")
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ScheduleController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ScheduleController.class);
	@Autowired
	private ScheduleService scheduleService;

	@ApiOperation(value = "查询医生的日程信息", notes = "status=1  而且 issueNum》>registeredNum 时表示可以预约 ，issueNum==registeredNum表示已约满")
	@RequestMapping(value = "/doctors/{doctorId}/schedules", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ScheduleResponse> getDoctorSchedule(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") Long doctorId,
			@ApiParam(value = "开始日期", required = false) @RequestParam(value = "beginDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate,
			@ApiParam(value = "结束日期", required = false) @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		ScheduleResponse response = scheduleService.getDoctorSchedule(doctorId,
				beginDate, endDate);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "查询医生的日程信息（包含草稿状态）", notes = "status<=1  而且 issueNum》>registeredNum 时表示可以预约 ，issueNum==registeredNum表示已约满")
	@RequestMapping(value = "/doctors/{doctorId}/schedulesWithDraft", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ScheduleResponse> getDoctorScheduleWithDraft(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") Long doctorId,
			@ApiParam(value = "开始日期", required = false) @RequestParam(value = "beginDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate,
			@ApiParam(value = "结束日期", required = false) @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

		ScheduleResponse response = scheduleService.getDoctorScheduleWithDraft(
				doctorId, beginDate, endDate);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "查询医生在某一医院的日程信息", notes = "status=1  而且 issueNum》>registeredNum 时表示可以预约 ，issueNum==registeredNum表示已约满")
	@RequestMapping(value = "/doctors/{doctorId}/hospitals/{hospitalId}/schedules", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ScheduleResponse> findByDoctorIdHospitalIdBeginDateEndDate(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") Long doctorId,
			@ApiParam(value = "医院id", required = true, defaultValue = "0") @PathVariable(value = "hospitalId") Long hospitalId,
			@ApiParam(value = "开始日期", required = false) @RequestParam(value = "beginDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate,
			@ApiParam(value = "结束日期", required = false) @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		ScheduleResponse response = scheduleService
				.findByDoctorIdHospitalIdBeginDateEndDate(doctorId, hospitalId,
						beginDate, endDate);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "查询医生的每日可预约信息信息", notes = "按天查询。	状态值： 1:可预约, 2:预约满,3: 不出诊,4:不可预约")
	@RequestMapping(value = "/doctors/{doctorId}/scheduleDates", method = RequestMethod.GET)
	public ResponseEntity<ScheduleDateResponse> getDoctorScheduleDate(
			@ApiParam(value = "名医id") @PathVariable(value = "doctorId") Long doctorId,
			@ApiParam(value = "开始日期", required = false) @RequestParam(value = "beginDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate,
			@ApiParam(value = "结束日期", required = false) @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		ScheduleDateResponse response = scheduleService.getDoctorDateSchedule(
				doctorId, beginDate, endDate);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "查询医生在某一医院的每日可预约信息信息", notes = "按天查询。	状态值： 1:可预约, 2:预约满,3: 不出诊,4:不可预约")
	@RequestMapping(value = "/doctors/{doctorId}/hospitals/{hospitalId}/scheduleDates", method = RequestMethod.GET)
	public ResponseEntity<ScheduleDateResponse> getDoctorScheduleDate(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") Long doctorId,
			@ApiParam(value = "医院id", required = true, defaultValue = "0") @PathVariable(value = "hospitalId") Long hospitalId,
			@ApiParam(value = "开始日期", required = false) @RequestParam(value = "beginDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate,
			@ApiParam(value = "结束日期", required = false) @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		ScheduleDateResponse response = scheduleService
				.getDoctorHospitalDateSchedule(doctorId, hospitalId, beginDate,
						endDate);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// @ApiOperation(value = "创建日程", notes = "名医出诊-名医创建出诊日程", response =
	// ResponseBody.class)
	// @RequestMapping(value = "/schedules", produces = {
	// MediaType.APPLICATION_JSON_VALUE }, consumes = {
	// MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	// public ResponseEntity<ScheduleResponse> createSchedule(
	// @RequestBody @Valid ScheduleRequest request) {
	// ScheduleResponse response = scheduleService.createSchedule(request);
	// return new ResponseEntity<>(response, HttpStatus.OK);
	//
	// }

	@ApiOperation(value = "创建日程", notes = "名医出诊-名医创建出诊日程", response = ResponseBody.class)
	@RequestMapping(value = "/schedules", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<ResponseBody> createSchedule(
			@ApiParam(value = "医生id", required = true) @RequestParam(value = "doctorId") Long doctorId,
			@ApiParam(value = "医院id", required = true) @RequestParam(value = "hospitalId") Long hospitalId,
			@ApiParam(value = "出诊日期", required = true) @RequestParam(value = "scheduleDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date scheduleDate,
			@ApiParam(value = "时段", required = true) @RequestParam(value = "timeSlot", required = true) Integer timeSlot,
			@ApiParam(value = "开始时间", required = false) @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "HH:mm") Date startTime,
			@ApiParam(value = "结束时间", required = false) @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "HH:mm") Date endTime,
			@ApiParam(value = "放号数", required = true) @RequestParam(value = "issueNum", required = true) Integer issueNum,
			@ApiParam(value = "创建者id", required = true) @RequestParam(value = "creatorId", required = false) Long creatorId) {
		ResponseBody response = scheduleService.createSchedule(doctorId,
				hospitalId, scheduleDate, timeSlot, startTime, endTime,
				issueNum, creatorId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "激活日程", notes = "激活日程", response = ScheduleDateResponse.class)
	@RequestMapping(value = "/schedules/{id}/activeSpecificSchedule", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<ScheduleResponse> activeSchedule(
			@ApiParam(value = "日程id", required = true) @PathVariable(value = "id") Long id) {
		ScheduleResponse response = scheduleService.activeSpecificSchedule(id);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "更新出诊日程中的具体挂号信息", notes = "更新出诊日期，时段，", response = ResponseBody.class)
	@RequestMapping(value = "/schedules/{id}/scheduleInfo", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	public ResponseEntity<ResponseBody> updateScheduleInfo(
			@ApiParam(value = "日程id", required = true) @PathVariable(value = "id") Long id,
			@ApiParam(value = "出诊日期", required = true) @RequestParam(value = "scheduleDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date scheduleDate,
			@ApiParam(value = "时段", required = true) @RequestParam(value = "timeSlot", required = true) Integer timeSlot,
			@ApiParam(value = "开始时间", required = false) @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "HH:mm") Date startTime,
			@ApiParam(value = "结束时间", required = false) @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "HH:mm") Date endTime,
			@ApiParam(value = "放号数", required = true) @RequestParam(value = "issueNum", required = true) Integer issueNum,
			@ApiParam(value = "创建者id", required = false) @RequestParam(value = "creatorId", required = false) Long creatorId) {
		ResponseBody response = scheduleService
				.updateScheduleInfo(id, scheduleDate, timeSlot, startTime,
						endTime, issueNum, creatorId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "删除日程", notes = "删除日程信息", response = ResponseBody.class)
	@RequestMapping(value = "/schedules/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> deleteSchedule(
			@ApiParam(value = "日程id", required = true) @PathVariable(value = "id") Long id) {
		ResponseBody response = scheduleService.deleteSchedule(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新出诊日程", notes = "名医更新出诊日程", response = ResponseBody.class)
	@RequestMapping(value = "/schedules", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	public ResponseEntity<ResponseBody> updateSchedule(
			@RequestBody @Valid ScheduleRequest request) {
		ResponseBody response = scheduleService.updateSchedule(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "将出诊日程延迟到固定日期", notes = "将出诊日程延迟到固定日期", response = ResponseBody.class)
	@RequestMapping(value = "/schedules/{id}/postponeSchedule", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> postponeSchedule(
			@ApiParam(value = "日程id", required = true) @PathVariable(value = "id") Long id,
			@ApiParam(value = "出诊日期", required = true) @RequestParam(value = "scheduleDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date scheduleDate) {
		ResponseBody response = scheduleService.postponeSchedule(id,
				scheduleDate);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "根据id延迟出诊日程", notes = "根据id延迟出诊日程", response = ResponseBody.class)
	@RequestMapping(value = "/schedules/{id}/postponeScheduleById", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> postponeScheduleById(
			@ApiParam(value = "日程id", required = true) @PathVariable(value = "id") Long id) {
		// 待检验
		ResponseBody response = scheduleService.postponeSchedule(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "查询医生在某一医院的日程信息(带分页)", notes = "status=1  而且 issueNum》>registeredNum 时表示可以预约 ，issueNum==registeredNum表示已约满")
	@RequestMapping(value = "/doctors/{doctorId}/hospitals/{hospitalId}/schedulesWithPage", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ScheduleResponse> getSchedulesWithPage(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") Long doctorId,
			@ApiParam(value = "医院id", required = true, defaultValue = "0") @PathVariable(value = "hospitalId") Long hospitalId,
			@ApiParam(value = "开始日期", required = false) @RequestParam(value = "beginDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date beginDate,
			@ApiParam(value = "结束日期", required = false) @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page) {
		DDPageInfo<Schedule> pageInfo = new DDPageInfo<Schedule>(page, size,
				"", "");
		ScheduleResponse response = scheduleService.getSchedulesWithPage(
				doctorId, hospitalId, beginDate, endDate, pageInfo);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
