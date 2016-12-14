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
import com.dingdong.register.service.DoctorFanService;
import com.dingdong.register.vo.response.DoctorFanResponse;
import com.dingdong.register.vo.response.DoctorResponse;
import com.dingdong.sys.vo.response.UserResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "医生粉丝")
public class DoctorFanController {
	private static final Logger LOG = LoggerFactory
			.getLogger(DoctorFanController.class);
	private static final int LIMITED = 100;
	@Autowired
	private DoctorFanService doctorFanService;

	// @ApiOperation(value = "获得所有医生粉丝信息", notes = "通用查询")
	// @RequestMapping(value = "/docotorFans", produces = {
	// MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	// public ResponseEntity<DoctorFanResponse> getAllDoctorFans() {
	// DoctorFanResponse response = this.doctorFanService.findAllDoctorFans();
	// return new ResponseEntity<DoctorFanResponse>(response, HttpStatus.OK);
	// }

	@ApiOperation(value = "获得医生的所有粉丝信息", notes = "按医生查询医生的粉丝信息")
	@RequestMapping(value = "/docotors/{doctorId}/doctorFans", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorFanResponse> findByDoctor(
			@ApiParam(value = "医生编号") @PathVariable(value = "doctorId") long doctorId,
			@ApiParam(value = "过滤条件", required = false) @RequestParam(value = "filterText", required = false) String filterText,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page,
			@ApiParam(value = "排序字段", required = false, defaultValue = "userName") @RequestParam(value = "order", required = false, defaultValue = "userName") String orderBy,
			@ApiParam(value = "顺序", required = false, defaultValue = PageUtils.ORDER_ASC) @RequestParam(value = "orderBy", required = false, defaultValue = PageUtils.ORDER_ASC) String order) {
		PageInfo pageInfo = new PageInfo(page, size, orderBy, order);
		DoctorFanResponse response = this.doctorFanService.findByDoctorId(
				doctorId, filterText, pageInfo);
		return new ResponseEntity<DoctorFanResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得用户关注的医生列表", notes = "医生列表")
	@RequestMapping(value = "/users/{userId}/doctorFans", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorResponse> findByUser(
			@ApiParam(value = "用户编号") @PathVariable(value = "userId") long userId) {
		DoctorResponse response = this.doctorFanService.findByUserId(userId);
		return new ResponseEntity<DoctorResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得用户关注的医生列表", notes = "医生列表")
	@RequestMapping(value = "/doctors/{doctorId}/fanUsers", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<UserResponse> findUsersByDoctor(
			@ApiParam(value = "医生编号") @PathVariable(value = "doctorId") long doctorId,
			@ApiParam(value = "过滤条件", required = false) @RequestParam(value = "filterText", required = false) String filterText,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page,
			@ApiParam(value = "排序字段", required = false, defaultValue = "userName") @RequestParam(value = "order", required = false, defaultValue = "userName") String orderBy,
			@ApiParam(value = "顺序", required = false, defaultValue = PageUtils.ORDER_ASC) @RequestParam(value = "orderBy", required = false, defaultValue = PageUtils.ORDER_ASC) String order) {
		PageInfo pageInfo = new PageInfo(page, size, orderBy, order);
		UserResponse response = this.doctorFanService.findUsersByDoctor(
				doctorId, filterText, pageInfo);
		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "按照粉丝编号获得医生粉丝信息", notes = "按照粉丝编号获得一条记录")
	@RequestMapping(value = "/doctorFans/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorFanResponse> getDoctorFanById(
			@PathVariable(value = "id") Long id) {
		DoctorFanResponse response = this.doctorFanService
				.findDoctorFanById(id);
		return new ResponseEntity<DoctorFanResponse>(response, HttpStatus.OK);
	}

	// @ApiOperation(value = "更新医生粉丝信息", notes = "")
	// @RequestMapping(value = "/doctorFans/{id}", produces = {
	// MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	// public ResponseEntity<ResponseBody> updateDoctorFan(
	// @RequestBody @Valid DoctorFanRequest request) {
	// ResponseBody response = this.doctorFanService.updateDoctorFan(request);
	// return new ResponseEntity<>(response, HttpStatus.OK);
	// }

	@ApiOperation(value = "用户取消本医生粉丝信息", notes = "按医生编号与患者用户编号")
	@RequestMapping(value = "/users/{userId}/doctorFans", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> cancelDoctorFan(
			@PathVariable @Valid long userId, @RequestParam @Valid long doctorId) {

		ResponseBody response = new ResponseBody();
		for (int i = 0; i++ < LIMITED;) {
			try {
				response = this.doctorFanService.cancelDoctorFan(userId,
						doctorId);
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage());
			}
		}

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "用户取消本医生粉丝信息", notes = "按医生粉丝编号")
	@RequestMapping(value = "/doctorFans/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> cancelDoctorFan(
			@PathVariable(value = "id") Long id) {
		ResponseBody response = this.doctorFanService.cancelDoctorFan(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "用户添加对医生的关注", notes = "添加医生粉丝信息-新增医生粉丝个人信息")
	@RequestMapping(value = "/users/{userId}/doctorFans", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<ResponseBody> addDoctorFan(
			@PathVariable @Valid long userId, @RequestParam @Valid long doctorId) {
		ResponseBody response = new ResponseBody();
		for (int i = 0; i++ < LIMITED;) {
			try {
				response = this.doctorFanService.addDoctorFan(userId, doctorId);
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage());
			}
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
