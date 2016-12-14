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

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.service.WeekScheduleService;
import com.dingdong.register.vo.response.WeekScheduleResponse;

/**
 * 医生每周日程信息
 * 
 * @author yushansoft
 * 
 */
@Controller
@Api("医生每周周日程信息")
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WeekScheduleController {

	private static final Logger LOG = LoggerFactory
			.getLogger(WeekScheduleController.class);
	@Autowired
	private WeekScheduleService weekScheduleService;

	@ApiOperation(value = "查询医生在某一医院的周日程信息", notes = "0 表示星期 ")
	@RequestMapping(value = "/doctors/{doctorId}/hospitals/{hospitalId}/weekSchedules", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<WeekScheduleResponse> findByDoctorIdHospitalId(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @PathVariable(value = "doctorId") Long doctorId,
			@ApiParam(value = "医院id", required = true, defaultValue = "0") @PathVariable(value = "hospitalId") Long hospitalId

	) {
		WeekScheduleResponse response = weekScheduleService
				.findByDoctorIdHospitalId(doctorId, hospitalId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "创建周日程", notes = "名医出诊-名医创建出诊周日程", response = ResponseBody.class)
	@RequestMapping(value = "/weekSchedules", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<WeekScheduleResponse> add(
			@ApiParam(value = "名医id", required = true, defaultValue = "0") @RequestParam(value = "doctorId") Long doctorId,
			@ApiParam(value = "医院id", required = true, defaultValue = "0") @RequestParam(value = "hospitalId") Long hospitalId,
			@ApiParam(value = "星期几", required = true, defaultValue = "0") @RequestParam(value = "day") Integer day,
			@ApiParam(value = "上午下午", required = true, defaultValue = "0") @RequestParam(value = "timeSlot") Integer timeSlot,
			@ApiParam(value = "开始时间", required = false) @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "HH:mm") Date startTime,
			@ApiParam(value = "结束时间", required = false) @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "HH:mm") Date endTime,
			@ApiParam(value = "放号数量", required = true, defaultValue = "5") @RequestParam(value = "issueNum") Integer issueNum,
			@ApiParam(value = "创建者id", required = true, defaultValue = "0") @RequestParam(value = "creatorId") Long creatorId) {
		WeekScheduleResponse response = weekScheduleService.add(doctorId,
				hospitalId, day, timeSlot, startTime, endTime, issueNum,
				creatorId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新周日程", notes = "", response = ResponseBody.class)
	@RequestMapping(value = "/weekSchedules/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	public ResponseEntity<WeekScheduleResponse> update(
			@ApiParam(value = "周日程id", required = true) @PathVariable(value = "id") Long id,
			@ApiParam(value = "星期几", required = false) @RequestParam(value = "day", required = false) Integer day,
			@ApiParam(value = "上午下午", required = false) @RequestParam(value = "timeSlot", required = false) Integer timeSlot,
			@ApiParam(value = "开始时间", required = false) @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "HH:mm") Date startTime,
			@ApiParam(value = "结束时间", required = false) @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "HH:mm") Date endTime,
			@ApiParam(value = "放号数量", required = false) @RequestParam(value = "issueNum", required = false) Integer issueNum,
			@ApiParam(value = "创建者id", required = false) @RequestParam(value = "creatorId", required = false) Long creatorId) {
		WeekScheduleResponse response = weekScheduleService.update(id, day,
				timeSlot, startTime, endTime, issueNum, creatorId);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "删除周日程", notes = "删除周日程信息", response = ResponseBody.class)
	@RequestMapping(value = "/weekSchedules/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> deleteWeekSchedule(
			@ApiParam(value = "周日程id", required = true) @PathVariable(value = "id") Long id) {
		LOG.info("the id is {}", id);
		ResponseBody response = weekScheduleService.deleteById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "按编号获取每周日程", notes = "", response = ResponseBody.class)
	@RequestMapping(value = "/weekSchedules/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseBody> findById(
			@ApiParam(value = "周日程id", required = true) @PathVariable(value = "id") Long id) {
		ResponseBody response = weekScheduleService.findById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
