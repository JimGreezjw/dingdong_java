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
import com.dingdong.register.model.DoctorHospital;
import com.dingdong.register.service.DoctorHospitalService;
import com.dingdong.register.vo.request.DoctorHospitalRequest;
import com.dingdong.register.vo.response.DoctorHospitalResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "执业医院信息")
public class DoctorHospitalController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(DoctorHospitalController.class);
	@Autowired
	private DoctorHospitalService doctorHospitalService;

	// @ApiOperation(value = "获得所有执业医院信息", notes = "通用查询")
	// @RequestMapping(value = "/docotorHospitals", produces = {
	// MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	// public ResponseEntity<DoctorHospitalResponse> getAllDoctorHospitals() {
	// DoctorHospitalResponse response = this.doctorHospitalService
	// .findAllDoctorHospitals();
	// return new ResponseEntity<DoctorHospitalResponse>(response,
	// HttpStatus.OK);
	// }

	@ApiOperation(value = "获得医生的所有执业信息、按照状态查询", notes = "")
	@RequestMapping(value = "/docotors/{doctorId}/doctorHospitals/status", method = RequestMethod.GET)
	public ResponseEntity<DoctorHospitalResponse> findByDoctorIdStatus(
			@ApiParam(value = "医生编号") @PathVariable(value = "doctorId") long doctorId,
			@ApiParam(value = "状态", required = false) @RequestParam(value = "status", required = false) Integer status) {
		DoctorHospitalResponse response = this.doctorHospitalService
				.findByDoctorIdStatus(doctorId, status);
		return new ResponseEntity<DoctorHospitalResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "获得医生的所有执业信息", notes = "")
	@RequestMapping(value = "/docotors/{doctorId}/doctorHospitals", method = RequestMethod.GET)
	public ResponseEntity<DoctorHospitalResponse> findByDoctor(
			@ApiParam(value = "医生编号") @PathVariable(value = "doctorId") long doctorId) {
		DoctorHospitalResponse response = this.doctorHospitalService
				.findByDoctorId(doctorId);
		return new ResponseEntity<DoctorHospitalResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "获得医生的所有执业信息,含该患者用户的挂号信息", notes = "  registerStatus 表示在该医院的挂号状态,非持久态，临时算出 包括，0-未排队 ，1-已排队 ，2-可挂号 3-已挂号"
			+ " ，availableCount 表示医生在该医院的余票数量 ")
	@RequestMapping(value = "/docotors/{doctorId}/doctorHospitalsWithRegisterInfo", method = RequestMethod.GET)
	public ResponseEntity<DoctorHospitalResponse> findByDoctorWithRegisterInfo(
			@ApiParam(value = "医生编号") @PathVariable(value = "doctorId") long doctorId,
			@ApiParam(value = "患者用户编号", required = true) @RequestParam(value = "userId", required = true) long userId) {
		DoctorHospitalResponse response = this.doctorHospitalService
				.findByDoctorIdWithRegisterInfo(doctorId, userId);

		return new ResponseEntity<DoctorHospitalResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "获得医院的所有执业医生信息", notes = "按医院查询")
	@RequestMapping(value = "/hospitals/{hospitalId}/doctorHospitals", method = RequestMethod.GET)
	public ResponseEntity<DoctorHospitalResponse> findByHospital(
			@ApiParam(value = "医院编号") @PathVariable(value = "hospitalId") long hospitalId) {
		DoctorHospitalResponse response = this.doctorHospitalService
				.findByHospitalId(hospitalId);
		return new ResponseEntity<DoctorHospitalResponse>(response,
				HttpStatus.OK);
	}

	@ApiOperation(value = "查询医院所有的执业医生信息（带分页）", notes = "按医院查询")
	@RequestMapping(value = "/hospitals/{hospitalId}/doctorHospitals/pageQuery", method = RequestMethod.GET)
	public ResponseEntity<ResponseBody> findByHospitalWithPage(
			@PathVariable(value = "hospitalId") long hospitalId,
			@ApiParam(value = "过滤条件", required = false) @RequestParam(value = "filterText", required = false) String filterText,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page) {
		DDPageInfo<DoctorHospital> pageInfo = new DDPageInfo<DoctorHospital>(
				page, size, null, null);

		ResponseBody response = this.doctorHospitalService
				.findByHospitalWithPage(hospitalId, filterText, pageInfo);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "按照执业编号获得执业医院信息", notes = "按照医生执业编号")
	@RequestMapping(value = "/doctorHospitals/{id}", method = RequestMethod.GET)
	public ResponseEntity<DoctorHospitalResponse> getDoctorHospitalById(
			@PathVariable(value = "id") Long id) {
		DoctorHospitalResponse response = this.doctorHospitalService
				.findDoctorHospitalById(id);
		return new ResponseEntity<DoctorHospitalResponse>(response,
				HttpStatus.OK);
	}

	// @ApiOperation(value = "更新医生执业医院信息", notes = "更新执业医院信息")
	// @RequestMapping(value = "/doctorHospitals/{id}", method =
	// RequestMethod.PUT)
	// public ResponseEntity<ResponseBody> updateDoctorHospital(
	// @PathVariable(value = "id") long id,
	// @RequestBody @Valid DoctorHospitalRequest request) {
	// ResponseBody response = this.doctorHospitalService
	// .updateDoctorHospital(id, request);
	// return new ResponseEntity<>(response, HttpStatus.OK);
	// }

	@ApiOperation(value = "更新医生执业医院信息", notes = "更新执业医院信息，主要包括最低排队人数，挂号费、状态等信息，其中状态 0————表示创建,1--表示有效 ，2————表示 已取消")
	@RequestMapping(value = "/doctorHospitals/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> updateDoctorHospital(
			@PathVariable(value = "id") long id,
			@ApiParam(value = "最小排队人数", required = false) @RequestParam(value = "minQueue", required = false) Integer minQueue,
			@ApiParam(value = "挂号费", required = false) @RequestParam(value = "registerFee", required = false) Integer registerFee,
			@ApiParam(value = "诚意金", required = false) @RequestParam(value = "deposit", required = false) Integer deposit,
			@ApiParam(value = "部门ID", required = false) @RequestParam(value = "deptId", required = false) Long deptId,
			@ApiParam(value = "部门名称", required = false) @RequestParam(value = "deptName", required = false) String deptName,
			@ApiParam(value = "联系人名称", required = false) @RequestParam(value = "contactName", required = false) String contactName,
			@ApiParam(value = "联系人电话", required = false) @RequestParam(value = "contactTele", required = false) String contactTele,
			@ApiParam(value = "状态", required = false) @RequestParam(value = "status", required = false) Integer status

	) {
		ResponseBody response = this.doctorHospitalService
				.updateDoctorHospital(id, minQueue, registerFee, deposit,
						deptId, deptName, contactName, contactTele, status);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新医生执业医院状态信息", notes = "0————表示创建,1--表示有效 ，2————表示 已取消")
	@RequestMapping(value = "/doctorHospitals/{id}/status", method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> updateDoctorHospital(
			@PathVariable(value = "id") long id,
			@RequestParam(value = "status") int status) {
		ResponseBody response = this.doctorHospitalService.updateStatus(id,
				status);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "取消本执业信息", notes = "按执业编号")
	@RequestMapping(value = "/doctorHospitals/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> cancelDoctorHospital(
			@PathVariable(value = "id") long id) {
		ResponseBody response = this.doctorHospitalService.deleteById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "添加执业医院信息", notes = "添加执业医院信息-新增执业医院个人信息")
	@RequestMapping(value = "/doctorHospitals", method = RequestMethod.POST)
	public ResponseEntity<ResponseBody> addDoctorHospital(
			@RequestBody @Valid DoctorHospitalRequest request) {
		ResponseBody response = this.doctorHospitalService
				.addDoctorHospital(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得某医院某科室所有的执业医生信息", notes = "按科室查询")
	@RequestMapping(value = "/hospitals/{hospitalId}/depts/{deptId}/doctorHospitals", method = RequestMethod.GET)
	public ResponseEntity<ResponseBody> findByHospitalAndDept(
			@PathVariable(value = "hospitalId") long hospitalId,
			@PathVariable(value = "deptId") long deptId,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page,
			@ApiParam(value = "排序字段", required = false, defaultValue = "doctor_name") @RequestParam(value = "orderBy", required = false, defaultValue = "doctor_name") String orderBy,
			@ApiParam(value = "排序顺序", required = false, defaultValue = PageUtils.ORDER_ASC) @RequestParam(value = "order", required = false, defaultValue = PageUtils.ORDER_ASC) String order) {
		DDPageInfo<DoctorHospital> pageInfo = new DDPageInfo<DoctorHospital>(
				page, size, orderBy, order);

		ResponseBody response = this.doctorHospitalService
				.findByHospitalAndDept(hospitalId, deptId, pageInfo);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得医生的所有执业信息、按照状态查询", notes = "按照医院科室和医生")
	@RequestMapping(value = "/doctorHospitals/{hospitalId}/{deptId}/{doctorId}", method = RequestMethod.GET)
	public ResponseEntity<DoctorHospitalResponse> findByHospitalAndDeptAndDoctor(
			@ApiParam(value = "医院编号") @PathVariable(value = "hospitalId") long hospitalId,
			@ApiParam(value = "科室编号") @PathVariable(value = "deptId") long deptId,
			@ApiParam(value = "医生编号") @PathVariable(value = "doctorId") long doctorId) {
		DoctorHospitalResponse response = this.doctorHospitalService
				.findByHospitalAndDeptAndDoctor(hospitalId, deptId, doctorId);
		return new ResponseEntity<DoctorHospitalResponse>(response,
				HttpStatus.OK);
	}
}
