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
import com.dingdong.register.model.Doctor;
import com.dingdong.register.service.DeptService;
import com.dingdong.register.service.DoctorService;
import com.dingdong.register.vo.request.DoctorRequest;
import com.dingdong.register.vo.response.DoctorResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "医生信息")
public class DoctorController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DoctorController.class);
	@Autowired
	private DoctorService doctorService;

	@Autowired
	private DeptService deptService;

	@ApiOperation(value = "获得所有医生信息", notes = "获得医生信息-获得所有医生信息")
	@RequestMapping(value = "/doctors", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorResponse> getAllDoctors(
			@ApiParam(value = "过滤条件", required = false) @RequestParam(value = "filterText", required = false) String filterText,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page,
			@ApiParam(value = "排序字段", required = false, defaultValue = "name") @RequestParam(value = "orderBy", required = false, defaultValue = "name") String orderBy,
			@ApiParam(value = "顺序", required = false, defaultValue = PageUtils.ORDER_ASC) @RequestParam(value = "order", required = false, defaultValue = PageUtils.ORDER_ASC) String order) {
		DDPageInfo<Doctor> pageInfo = new DDPageInfo<Doctor>(page, size,
				orderBy, order);
		DoctorResponse response = this.doctorService.findAllDoctors(filterText,
				pageInfo);
		return new ResponseEntity<DoctorResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得新入驻医生信息", notes = "获得医生信息-获得新入驻医生信息")
	@RequestMapping(value = "/doctors/newJoinDoctors", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorResponse> getNewJoinDoctors(
			@ApiParam(value = "获取的数量", required = true, defaultValue = "5") @RequestParam(value = "requireNum", required = true) int requireNum) {
		DoctorResponse response = new DoctorResponse();
		// 获取最新添加的医生
		response = doctorService.getNewJoinDoctors(requireNum);

		return new ResponseEntity<DoctorResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "按状态获得医生列表", notes = "状态码 0——创建, 1——已签约, 2——已取消")
	@RequestMapping(value = "/doctors/status", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorResponse> findByStatus(
			@ApiParam(value = "状态", required = false) @RequestParam(value = "status", required = false) int status) {
		DoctorResponse response = this.doctorService.findByStatus(status);
		return new ResponseEntity<DoctorResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得未签约医生列表", notes = "")
	@RequestMapping(value = "/doctors/unsigned", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorResponse> findUnsignedDoctors(
			@ApiParam(value = "医生名字", required = false) @RequestParam(value = "doctorName", required = false) String doctorName) {
		DoctorResponse response = this.doctorService
				.findUnsignedDoctors(doctorName);
		return new ResponseEntity<DoctorResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得未签约医生申请列表", notes = "")
	@RequestMapping(value = "/doctors/unsignedApply", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorResponse> findUnsignedDoctorsApply(
			@ApiParam(value = "医生名字", required = false) @RequestParam(value = "doctorName", required = false) String doctorName,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = "0") @RequestParam(value = "size", required = false, defaultValue = "0") Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = "1") @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
		DDPageInfo<Doctor> pageInfo = new DDPageInfo<Doctor>(page, size, null,
				null);
		DoctorResponse response = this.doctorService.findUnsignedDoctorsApply(
				doctorName, pageInfo);
		return new ResponseEntity<DoctorResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新医生信息", notes = "更新医生的基本信息")
	@RequestMapping(value = "/doctors/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseBody> updateDoctor(
			@PathVariable(value = "id") long id,
			@ApiParam(value = "名称", required = false) @RequestParam(value = "name", required = false) String name,
			@ApiParam(value = "性别", required = false) @RequestParam(value = "gender", required = false) Integer gender,
			@ApiParam(value = "主医院", required = false) @RequestParam(value = "hospitalName", required = false) String hospitalName,
			@ApiParam(value = "医生级别", required = false) @RequestParam(value = "level", required = false) String level,
			@ApiParam(value = "医生特长", required = false) @RequestParam(value = "specialty", required = false) String specialty,
			@ApiParam(value = "医生介绍", required = false) @RequestParam(value = "introduction", required = false) String introduction) {

		ResponseBody response = this.doctorService.updateDoctor(id, name,
				gender, hospitalName, level, specialty, introduction);
		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "签约医生，与userId绑定")
	@RequestMapping(value = "/doctors/{id}/submitSignDoctor", method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> submitSignDoctor(
			@PathVariable(value = "id") long id,
			@ApiParam(value = "用户编号", required = true) @RequestParam(value = "userId", required = true) Long userId,
			@ApiParam(value = "医生办公电话", required = true) @RequestParam(value = "officeTele", required = true) String officeTele) {

		ResponseBody response = this.doctorService.submitSignDoctor(id, userId,
				officeTele);

		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "运维设置医生签约认证状态")
	@RequestMapping(value = "/doctors/{id}/signDoctor", method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> signDoctor(
			@PathVariable(value = "id") long id) {

		ResponseBody response = this.doctorService.signDoctor(id);

		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "取消签约医生,或者认证未通过")
	@RequestMapping(value = "/doctors/{id}/cancelSignDoctor", method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> cancelSignDoctor(
			@PathVariable(value = "id") long id) {

		ResponseBody response = this.doctorService.cancelSignDoctor(id);

		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得医生信息", notes = "获得医生信息-获得指定id医生信息")
	@RequestMapping(value = "/doctors/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorResponse> getDoctorById(
			@PathVariable(value = "id") Long id) {
		DoctorResponse response = this.doctorService.findDoctorById(id);
		return new ResponseEntity<DoctorResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "添加医生信息", notes = "添加医生信息-新增医生个人信息")
	@RequestMapping(value = "/doctors", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<ResponseBody> addDoctor(
			@RequestBody @Valid DoctorRequest request) {
		ResponseBody response = this.doctorService.addDoctor(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新医生信息", notes = "更新医生信息-更新医生个人信息")
	@RequestMapping(value = "/doctors/{id}/bySelective", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	public ResponseEntity<ResponseBody> updateDoctor(
			@RequestBody @Valid DoctorRequest request) {
		ResponseBody response = this.doctorService
				.updateDoctorBySelective(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "删除医生信息", notes = "删除医生信息-删除医生个人信息")
	@RequestMapping(value = "/doctors/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> delDoctor(
			@PathVariable(value = "id") Long id) {
		ResponseBody response = this.doctorService.delDoctorById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得科室下面的全部医生信息", notes = "获得科室下面的全部医生信息")
	@RequestMapping(value = "/depts/{id}/recursiveDoctors", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<DoctorResponse> getRecursiveDoctors(
			@PathVariable(value = "id") Long id) {
		DoctorResponse response = this.deptService.findRecursiveDoctors(id);
		return new ResponseEntity<DoctorResponse>(response, HttpStatus.OK);
	}

}
