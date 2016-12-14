package com.dingdong.register.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
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
import com.dingdong.register.model.Patient;
import com.dingdong.register.service.PatientService;
import com.dingdong.register.vo.response.PatientResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "患者信息")
public class PatientController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(PatientController.class);
	@Autowired
	private PatientService patientService;

	@ApiOperation(value = "获得所有患者信息", notes = "获得患者信息-获得所有患者信息")
	@RequestMapping(value = "/patients", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<PatientResponse> getAllPatients() {
		PatientResponse response = this.patientService.findAllPatients();
		return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得指定患者信息", notes = "获得患者信息-获得指定id患者信息")
	@RequestMapping(value = "/patients/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<PatientResponse> getPatientById(
			@PathVariable(value = "id") Long id) {
		PatientResponse response = this.patientService.findPatientById(id);
		return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "取得某个用户的所有患者信息", notes = "获得患者信息-获得指定微信号获得患者信息")
	@RequestMapping(value = "/users/{userId}/patients", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<PatientResponse> getPatientById(
			@PathVariable(value = "userId") int userId) {
		PatientResponse response = this.patientService
				.findPatientByUserId(userId);
		return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "删除患者信息，指定id", notes = "")
	@RequestMapping(value = "/patients/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> deleteById(
			@PathVariable(value = "id") long id) {
		ResponseBody response = this.patientService.deleteById(id);
		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "某用户添加患者信息", notes = "userRelation:与患者关系,0-本人,1-家人,2-亲戚,3-朋友,4-其他")
	@RequestMapping(value = "/users/{userId}/patients", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<PatientResponse> addPatient(
			@ApiParam(value = "用户id", required = true) @PathVariable("userId") long userId,
			@ApiParam(value = "患者姓名", required = true) @RequestParam("patientName") String patientName,
			@ApiParam(value = "患者身份证号", required = true) @RequestParam("certificateId") String certificateId,
			@ApiParam(value = "患者与用户的关系", required = true, defaultValue = "0") @RequestParam(value = "userRelation", required = true, defaultValue = "0") int userRelation,
			@ApiParam(value = "性别", required = false) @RequestParam(value = "gender", required = false) Integer gender,
			@ApiParam(value = "生日", required = false) @RequestParam(value = "birthday", required = false) Date birthday,
			@ApiParam(value = "联系电话", required = false) @RequestParam(value = "phone", required = false) String phone,
			@ApiParam(value = "家庭住址", required = false) @RequestParam(value = "address", required = false) String address) {

		Patient patient = new Patient();

		patient.setUserId(userId);
		patient.setName(patientName);
		patient.setCertificateId(certificateId);
		patient.setUserRelation(userRelation);
		if (null != gender)
			patient.setGender(gender);
		if (null != birthday)
			patient.setBirthday(birthday);
		if (StringUtils.isNotBlank(phone))
			patient.setPhone(phone);
		if (StringUtils.isNotBlank(address))
			patient.setAddress(address);

		PatientResponse response = this.patientService.addPatient(patient);
		return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新患者信息", notes = "更新患者的基本信息")
	@RequestMapping(value = "/patients/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseBody> updatePatients(
			@ApiParam(value = "id", required = true) @PathVariable("id") long id,
			@ApiParam(value = "用户id", required = false) @RequestParam(value = "userId", required = false) long userId,
			@ApiParam(value = "患者姓名", required = false) @RequestParam(value = "name", required = false) String name,
			@ApiParam(value = "患者身份证号", required = false) @RequestParam(value = "certificateId", required = false) String certificateId,
			@ApiParam(value = "患者与用户的关系", required = false, defaultValue = "0") @RequestParam(value = "userRelation", required = false, defaultValue = "0") Integer userRelation,
			@ApiParam(value = "性别", required = false) @RequestParam(value = "gender", required = false) Integer gender,
			@ApiParam(value = "生日", required = false) @RequestParam(value = "birthday", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday,
			@ApiParam(value = "联系电话", required = false) @RequestParam(value = "phone", required = false) String phone,
			@ApiParam(value = "家庭住址", required = false) @RequestParam(value = "address", required = false) String address) {

		ResponseBody response = patientService.updatePatients(id, name, gender,
				birthday, address, phone, userRelation, certificateId, userId);
		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}
}
