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

import com.dingdong.conf.PageInfo;
import com.dingdong.conf.PageUtils;
import com.dingdong.register.service.DeptService;
import com.dingdong.register.vo.response.DeptResponse;
import com.dingdong.register.vo.response.DoctorResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "科室信息")
public class DeptController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DeptController.class);
	@Autowired
	private DeptService deptService;

	@ApiOperation(value = "获得所有科室信息", notes = "获得科室信息-获得所有科室信息")
	@RequestMapping(value = "/depts", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DeptResponse> getAllDepts(
			@ApiParam(value = "过滤条件", required = false) @RequestParam(value = "filterText", required = false) String filterText,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page,
			@ApiParam(value = "排序字段", required = false, defaultValue = "name") @RequestParam(value = "order", required = false, defaultValue = "name") String orderBy,
			@ApiParam(value = "排序顺序", required = false, defaultValue = PageUtils.ORDER_ASC) @RequestParam(value = "orderBy", required = false, defaultValue = PageUtils.ORDER_ASC) String order) {
		PageInfo pageInfo = new PageInfo(page, size, orderBy, order);
		DeptResponse response = this.deptService.findAllDepts(filterText,
				pageInfo);
		return new ResponseEntity<DeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得科室信息", notes = "获得科室信息-获得指定id科室信息")
	@RequestMapping(value = "/depts/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DeptResponse> getDeptById(
			@PathVariable(value = "id") Long id) {
		DeptResponse response = this.deptService.findDeptById(id);
		return new ResponseEntity<DeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得科室子类信息", notes = "获得科室子类信息-获得指定父类科室的子类信息")
	@RequestMapping(value = "/depts/{parentId}/subDepts", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DeptResponse> getDeptByParentId(
			@PathVariable(value = "parentId") Long parentId) {
		DeptResponse response = this.deptService.findDeptByParentId(parentId);
		return new ResponseEntity<DeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得大科室（顶层科室）信息", notes = "获得大科室（顶层科室）信息")
	@RequestMapping(value = "/depts/topDepts", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DeptResponse> getTopDepts() {
		DeptResponse response = this.deptService.findTopDepts();
		return new ResponseEntity<DeptResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得父科室的全层级子科室信息", notes = "获得父科室的全层级子科室信息")
	@RequestMapping(value = "/depts/{parentId}/recursiveSubDepts", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DeptResponse> getRecursiveSubDepts(
			@PathVariable(value = "parentId") Long parentId) {
		DeptResponse response = this.deptService
				.findRecursiveSubDepts(parentId);
		return new ResponseEntity<DeptResponse>(response, HttpStatus.OK);
	}

}
