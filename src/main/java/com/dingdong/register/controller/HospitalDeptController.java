package com.dingdong.register.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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

import com.dingdong.conf.DDPageInfo;
import com.dingdong.conf.PageUtils;
import com.dingdong.register.model.HospitalDept;
import com.dingdong.register.service.HospitalDeptService;
import com.dingdong.register.vo.response.DeptResponse;
import com.dingdong.register.vo.response.HospitalDeptResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "医院科室信息")
public class HospitalDeptController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(HospitalDeptController.class);
	@Autowired
	private HospitalDeptService hospitalDeptService;

	@ApiOperation(value = "获得指定医院科室信息", notes = "获得医院科室信息-获得指定id医院科室信息")
	@RequestMapping(value = "/hospitals/{hospitalId}/hospitalDepts", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<HospitalDeptResponse> getHospitalDeptsByHospitalId(
			@PathVariable(value = "hospitalId") Long hospitalId,
			@ApiParam(value = "过滤条件", required = false) @RequestParam(value = "filterText", required = false) String filterText,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page) {
		DDPageInfo<HospitalDept> pageInfo = new DDPageInfo<HospitalDept>(page,
				size, "", "");
		HospitalDeptResponse response = hospitalDeptService.findByHospitalId(
				hospitalId, pageInfo);
		return new ResponseEntity<HospitalDeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "添加医院科室信息", notes = "添加医院科室信息-新增医院科室信息")
	@RequestMapping(value = "/hospitalDepts/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<HospitalDeptResponse> addHospitalDept(
			@PathVariable(value = "id") Long id,
			@ApiParam(value = "科室id，使用 , 分隔", required = true) @RequestParam("deptStr") String deptStr) {
		HospitalDeptResponse response = hospitalDeptService.addHospitalDept(id,
				deptStr);
		return new ResponseEntity<HospitalDeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "删除医院科室信息", notes = "删除医院科室信息-删除医院科室信息")
	@RequestMapping(value = "/hospitalDepts/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<HospitalDeptResponse> delHospitalDept(
			@PathVariable(value = "id") Long id,
			@ApiParam(value = "医院科室id，使用 , 分隔", required = true) @RequestParam("hospitalDeptStr") String hospitalDeptStr) {

		hospitalDeptStr = hospitalDeptStr.trim();
		String[] hosStrs = hospitalDeptStr.split(",");
		HospitalDeptResponse response = new HospitalDeptResponse();
		for (String str : hosStrs) {
			Long deptId = Long.parseLong(str);
			response = hospitalDeptService.deleteById(deptId);
		}
		return new ResponseEntity<HospitalDeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得指定医院未添加科室信息", notes = "获得医院未添加科室信息-获得指定id医院未添加科室信息")
	@RequestMapping(value = "/hospitalDepts/{hospitalId}/remain", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DeptResponse> getHospitalDeptsRemainById(
			@PathVariable(value = "hospitalId") Long hospitalId,
			@ApiParam(value = "过滤条件", required = false) @RequestParam(value = "filterText", required = false) String filterText) {
		DeptResponse response = hospitalDeptService.getHospitalDeptRemain(
				hospitalId, filterText);
		return new ResponseEntity<DeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得指定医院树形科室信息", notes = "获得医院科室信息-获得指定id医院科室信息")
	@RequestMapping(value = "/hospitalDepts/{hospitalId}/tree", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<HospitalDeptResponse> getHospitalDeptsById(
			@PathVariable(value = "hospitalId") Long hospitalId) {
		HospitalDeptResponse response = hospitalDeptService
				.findDeptTotalByHospitalId(hospitalId);
		return new ResponseEntity<HospitalDeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得科室信息", notes = "获得医院科室信息-获得指定id医院科室信息")
	@RequestMapping(value = "/hospitalDepts/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<HospitalDeptResponse> getHospitalDeptById(
			@PathVariable(value = "id") Long id) {
		HospitalDeptResponse response = this.hospitalDeptService.findById(id);
		return new ResponseEntity<HospitalDeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "根据医院和科室唯一号获得科室信息", notes = "获得医院科室信息-获得指定医院科室信息")
	@RequestMapping(value = "/hospitals/{hospitalId}/dept/{deptId}/hospitalDepts", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<HospitalDeptResponse> getHospitalDeptByHospitalIdAndDeptId(
			@PathVariable(value = "hospitalId") Long hospitalId,
			@PathVariable(value = "deptId") Long deptId) {
		HospitalDeptResponse response = this.hospitalDeptService
				.findByHospitalIdAndDeptId(hospitalId, deptId);
		return new ResponseEntity<HospitalDeptResponse>(response, HttpStatus.OK);
	}
}
