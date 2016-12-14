package com.dingdong.register.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.PageInfo;
import com.dingdong.conf.PageUtils;
import com.dingdong.register.service.DoctorEvalService;
import com.dingdong.register.vo.response.DoctorEvalResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "医生点评信息")
public class DoctorEvalController {

	private static final Logger LOG = LoggerFactory
			.getLogger(DoctorEvalController.class);
	@Autowired
	private DoctorEvalService doctorEvalService;

	@ApiOperation(value = "获得医生的所有评价信息", notes = "按医生查询医生的评价信息")
	@RequestMapping(value = "/docotors/{doctorId}/doctorEvals", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorEvalResponse> findByDoctor(
			@ApiParam(value = "医生编号") @PathVariable(value = "doctorId") long doctorId,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page,
			@ApiParam(value = "排序字段", required = false, defaultValue = "create_time") @RequestParam(value = "order", required = false, defaultValue = "create_time") String orderBy,
			@ApiParam(value = "排序", required = false, defaultValue = PageUtils.ORDER_DESC) @RequestParam(value = "orderBy", required = false, defaultValue = PageUtils.ORDER_DESC) String order) {
		PageInfo pageInfo = new PageInfo(page, size, orderBy, order);
		DoctorEvalResponse response = this.doctorEvalService.findByDoctorId(
				doctorId, pageInfo);
		return new ResponseEntity<DoctorEvalResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得用户评价的评价列表", notes = "评价列表")
	@RequestMapping(value = "/users/{userId}/doctorEvals", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorEvalResponse> findByUser(
			@ApiParam(value = "用户编号") @PathVariable(value = "userId") long userId) {
		DoctorEvalResponse response = this.doctorEvalService
				.findByUserId(userId);
		return new ResponseEntity<DoctorEvalResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得某次诊疗的评价列表", notes = "评价列表")
	@RequestMapping(value = "/registers/{registerId}/doctorEvals", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorEvalResponse> findByRegister(
			@ApiParam(value = "挂号编号") @PathVariable(value = "registerId") long registerId) {
		DoctorEvalResponse response = this.doctorEvalService
				.findByRegisterId(registerId);

		return new ResponseEntity<DoctorEvalResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "按照评价编号获得医生评价信息", notes = "按照评价编号获得一条记录")
	@RequestMapping(value = "/doctorEvals/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorEvalResponse> getDoctorEvalById(
			@PathVariable(value = "id") Long id) {
		DoctorEvalResponse response = this.doctorEvalService.findById(id);
		return new ResponseEntity<DoctorEvalResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "用户取消本医生评价信息", notes = "按评价编号")
	@RequestMapping(value = "/doctorEvals/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> cancelDoctorEval(
			@PathVariable(value = "id") Long id) {
		ResponseBody response = this.doctorEvalService.deleteById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "用户添加对医生的评价，附带标签", notes = "治疗效果、服务态度均按1-5星级从低到高设计，标签用英文的逗号隔开")
	@RequestMapping(value = "/users/{userId}/doctorEval", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<ResponseBody> addDoctorEval(
			@PathVariable @Valid long userId,
			@ApiParam(value = "挂号Id", required = true) @RequestParam(value = "registerId", required = true) long registerId,
			@ApiParam(value = "治疗效果", required = true) @RequestParam(value = "treatmentEffect", required = true) int treatmentEffect,
			@ApiParam(value = "服务态度", required = true) @RequestParam(value = "serviceAttitude", required = true) int serviceAttitude,
			@ApiParam(value = "评价内容", required = false) @RequestParam(value = "evalDesc", required = false) String evalDesc,
			@ApiParam(value = "评价标签", required = false) @RequestParam(value = "tags", required = false) String tags) {
		// FIXME niukai 需要将次数也给添加上
		ResponseBody response = null;
		for (int i = 0; i < 100; i++) {
			try {
				response = this.doctorEvalService.addDoctorEvalWithTag(userId,
						registerId, treatmentEffect, serviceAttitude, evalDesc,
						tags);
				break;
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
