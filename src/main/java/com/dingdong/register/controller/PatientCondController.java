package com.dingdong.register.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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

import com.dingdong.register.service.PatientCondService;
import com.dingdong.register.vo.request.PatientCondRequest;
import com.dingdong.register.vo.response.PatientCondResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "/patientConds", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "病情信息")
public class PatientCondController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(PatientCondController.class);
	@Autowired
	private PatientCondService patientCondService;

	@ApiOperation(value = "添加患者病况信息", notes = "添加患者病况信息-新增患者病况信息")
	@RequestMapping(value = "/add", produces = { MediaType.APPLICATION_JSON_VALUE }, method =

	RequestMethod.POST)
	public ResponseEntity<PatientCondResponse> insertPatientCond(
			@RequestBody @Valid PatientCondRequest request) {
		PatientCondResponse response = this.patientCondService
				.addPatientCond(request);
		return new ResponseEntity<PatientCondResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得患者病情信息", notes = "获得患者病情信息-获得指定id患者病情信息")
	@RequestMapping(value = "/patientConds/{registerId}", produces = { MediaType.APPLICATION_JSON_VALUE }, method =

	RequestMethod.GET)
	public ResponseEntity<PatientCondResponse> getPatientCondByRegisterId(
			@PathVariable(value = "registerId") Long

			registerId) {
		PatientCondResponse response = this.patientCondService
				.findPatientCondByRegisterId(registerId);
		return new ResponseEntity<PatientCondResponse>(response, HttpStatus.OK);
	}

}
