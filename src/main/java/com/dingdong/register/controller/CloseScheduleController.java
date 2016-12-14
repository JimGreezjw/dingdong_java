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
import com.dingdong.register.service.CloseScheduleService;
import com.dingdong.register.vo.response.CloseScheduleResponse;

/**
 * 医生停诊信息
 * 
 * @author yushansoft
 * 
 */
@Controller
@Api("医生停诊信息")
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class CloseScheduleController {

	private static final Logger LOG = LoggerFactory
			.getLogger(CloseScheduleController.class);
	@Autowired
	private CloseScheduleService closeScheduleService;

	@ApiOperation(value = "查询医生在某一医院的停诊信息", notes = "0 表示星期 ")
	@RequestMapping(value = "/weekSchedules/{weekScheduleId}/closeSchedules", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<CloseScheduleResponse> findByWeekScheduleId(
			@ApiParam(value = "周行程编号", required = true, defaultValue = "0") @PathVariable(value = "weekScheduleId") long weekScheduleId

	) {
		CloseScheduleResponse response = closeScheduleService
				.findByWeekScheduleId(weekScheduleId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "创建停诊", notes = "名医停诊", response = ResponseBody.class)
	@RequestMapping(value = "/closeSchedules", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<CloseScheduleResponse> add(
			@ApiParam(value = "周日程id", required = true, defaultValue = "0") @RequestParam(value = "weekScheduleId") long weekScheduleId,

			@ApiParam(value = "开始时间", required = true, defaultValue = "2016-03-1") @RequestParam(value = "fromDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@ApiParam(value = "结束时间", required = true, defaultValue = "2016-03-1") @RequestParam(value = "toDate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@ApiParam(value = "创建者id", required = true) @RequestParam(value = "creatorId") long creatorId,
			@ApiParam(value = "停诊原因", required = false) @RequestParam(value = "reason", required = false) String reason,
			@ApiParam(value = "状态", required = false) @RequestParam(value = "status", required = false) Integer status) {
		CloseScheduleResponse response = closeScheduleService.add(
				weekScheduleId, status, fromDate, toDate, reason, creatorId);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "更新停诊", notes = "", response = ResponseBody.class)
	@RequestMapping(value = "/closeSchedules/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	public ResponseEntity<CloseScheduleResponse> update(
			@ApiParam(value = "停诊id", required = true) @PathVariable(value = "id") long id,
			@ApiParam(value = "开始时间", required = false) @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@ApiParam(value = "结束时间", required = false) @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
			@ApiParam(value = "创建者id", required = false) @RequestParam(value = "creatorId", required = false) Long creatorId,
			@ApiParam(value = "停诊原因", required = false) @RequestParam(value = "reason", required = false) String reason,
			@ApiParam(value = "状态", required = false) @RequestParam(value = "status", required = false) Integer status) {
		CloseScheduleResponse response = closeScheduleService.update(id,
				status, fromDate, toDate, reason, creatorId);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "删除停诊", notes = "删除停诊信息", response = ResponseBody.class)
	@RequestMapping(value = "/closeSchedules/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> deleteCloseSchedule(
			@ApiParam(value = "停诊id", required = true) @PathVariable(value = "id") long id) {
		LOG.info("the id is {}", id);
		ResponseBody response = closeScheduleService.deleteById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "按编号获取停诊", notes = "", response = ResponseBody.class)
	@RequestMapping(value = "/closeSchedules/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseBody> findById(
			@ApiParam(value = "停诊id", required = true) @PathVariable(value = "id") long id) {
		ResponseBody response = closeScheduleService.findById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
