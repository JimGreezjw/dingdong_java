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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.conf.PageUtils;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.service.HospitalService;
import com.dingdong.register.vo.request.HospitalRequest;
import com.dingdong.register.vo.response.HospitalResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "医院信息")
public class HospitalController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(HospitalController.class);
	@Autowired
	private HospitalService hospitalService;

	@ApiOperation(value = "获得所有医院信息", notes = "获得医院信息-获得所有医院信息")
	@RequestMapping(value = "/hospitals", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<HospitalResponse> getAllHospitals(
			@ApiParam(value = "过滤条件", required = false) @RequestParam(value = "filterText", required = false) String filterText,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page,
			@ApiParam(value = "排序字段", required = false, defaultValue = "name") @RequestParam(value = "orderBy", required = false, defaultValue = "name") String orderBy,
			@ApiParam(value = "排序顺序", required = false, defaultValue = PageUtils.ORDER_ASC) @RequestParam(value = "order", required = false, defaultValue = PageUtils.ORDER_ASC) String order) {
		// PageInfo pageInfo = new PageInfo(page, size, orderBy, order);
		DDPageInfo<Hospital> pageInfo = new DDPageInfo<Hospital>(page, size,
				orderBy, order);

		HospitalResponse response = this.hospitalService.findAllHospitals(
				filterText, pageInfo);
		return new ResponseEntity<HospitalResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得医院信息", notes = "获得医院信息-获得指定id医院信息")
	@RequestMapping(value = "/hospitals/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<HospitalResponse> getHospitalById(
			@PathVariable(value = "id") Long id) {
		HospitalResponse response = this.hospitalService.findHospitalById(id);
		return new ResponseEntity<HospitalResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "添加医院信息", notes = "添加医院信息-新增医院信息")
	@RequestMapping(value = "/hospitals", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<HospitalResponse> addHospital(
			@RequestBody @Valid HospitalRequest request) {
		HospitalResponse response = this.hospitalService.addHospital(request);
		return new ResponseEntity<HospitalResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得所有医院科室信息", notes = "获得医院科室信息-获得所有医院科室信息")
	@RequestMapping(value = "/hospitals/hospitalDepts", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<HospitalResponse> getAllHospitalDepts() {

		HospitalResponse response = this.hospitalService.findAllHospitalDepts();
		return new ResponseEntity<HospitalResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新医院信息", notes = "更新医院信息-更新医院基本信息")
	@RequestMapping(value = "/hospitals/{id}/bySelective", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	public ResponseEntity<ResponseBody> updateHospital(
			@RequestBody @Valid HospitalRequest request) {
		ResponseBody response = this.hospitalService
				.updateHospitalBySelective(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "删除医院信息", notes = "删除医院信息-删除医院基本信息")
	@RequestMapping(value = "/hospitals/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> delHospital(
			@PathVariable(value = "id") Long id) {
		ResponseBody response = this.hospitalService.delHospitalById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "根据运维电话获得医院信息", notes = "获得医院信息-获得指定id医院信息")
	@RequestMapping(value = "/hospitals/opTeleValid", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<HospitalResponse> getHospitalByOpTele(
			@ApiParam(value = "运维电话", required = false) @RequestParam(value = "opTele", required = true) String opTele) {
		HospitalResponse response = this.hospitalService
				.findHospitalByOpTele(opTele);
		return new ResponseEntity<HospitalResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "根据状态获得医院信息", notes = "0——草稿，1——已签约，2——已取消")
	@RequestMapping(value = "/hospitals/status", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<HospitalResponse> findByStatus(
			@ApiParam(value = "状态", required = false) @RequestParam(value = "status", required = true) int status) {
		HospitalResponse response = this.hospitalService.findByStatus(status);
		return new ResponseEntity<HospitalResponse>(response, HttpStatus.OK);
	}

}
