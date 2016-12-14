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

import com.dingdong.register.service.DoctorTagService;
import com.dingdong.register.vo.response.DoctorTagResponse;

/**
 * 医生评价标签
 * 
 * @author niukai
 * 
 */
@Controller
@Api("医生标签")
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class DoctorTagController {

	private static final Logger logger = LoggerFactory
			.getLogger(RegisterController.class);

	@Autowired
	private DoctorTagService doctorTagService;

	@ApiOperation(value = "为医生创建标签", notes = "为医生创建标签", response = DoctorTagResponse.class)
	@RequestMapping(value = "/doctorTags/{doctorId}/addDoctorTag", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	public ResponseEntity<DoctorTagResponse> addDoctorTag(
			@ApiParam(value = "医生id", required = true) @PathVariable("doctorId") long doctorId,
			@ApiParam(value = "医生name", required = true) @RequestParam("doctorName") String doctorName,
			@ApiParam(value = "标签id", required = true) @RequestParam("tagId") long tagId,
			@ApiParam(value = "标签name", required = true) @RequestParam("tagName") String tagName) {
		DoctorTagResponse response = new DoctorTagResponse();
		response = doctorTagService.insertDoctorTag(doctorId, doctorName,
				tagId, tagName);

		return new ResponseEntity<DoctorTagResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新医生标签的被评价次数", notes = "更新医生标签的被评价次数", response = DoctorTagResponse.class)
	@RequestMapping(value = "/doctorTags/{doctorId}/updateDoctorTag", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<DoctorTagResponse> updateDoctorTag(
			@ApiParam(value = "医生id", required = true) @PathVariable("doctorId") long doctorId,
			@ApiParam(value = "标签id", required = true) @RequestParam("tagId") long tagId) {
		DoctorTagResponse response = new DoctorTagResponse();
		for (int i = 0; i++ < 10;) {
			try {
				response = doctorTagService.updateDoctorTag(doctorId, tagId);
				break;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		return new ResponseEntity<DoctorTagResponse>(response, HttpStatus.OK);
	}
}
