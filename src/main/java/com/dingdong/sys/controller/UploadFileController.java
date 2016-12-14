package com.dingdong.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dingdong.sys.service.UploadFileService;
import com.dingdong.sys.vo.response.UploadFileResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "文件上传操作")
public class UploadFileController {

	@Autowired
	private UploadFileService uploadFileService;

	@ApiOperation(value = "添加医生图片文件", notes = "")
	@RequestMapping(value = "/uploadDoctorFile", method = RequestMethod.POST)
	public ResponseEntity<UploadFileResponse> addDoctorFile(
			@ApiParam(value = "文件", required = true) @RequestParam(value = "uploadFile", required = true) MultipartFile uploadFile,
			@ApiParam(value = "文件Id", required = true) @RequestParam(value = "id", required = false) Long id,
			HttpServletRequest request) {
		UploadFileResponse response = uploadFileService.addDoctorFile(
				uploadFile, id, request);
		return new ResponseEntity<UploadFileResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "添加医院图片文件", notes = "")
	@RequestMapping(value = "/uploadHospitalFile", method = RequestMethod.POST)
	public ResponseEntity<UploadFileResponse> addHospitalFile(
			@ApiParam(value = "文件", required = true) @RequestParam(value = "uploadFile", required = true) MultipartFile uploadFile,
			@ApiParam(value = "文件Id", required = true) @RequestParam(value = "id", required = false) Long id,
			HttpServletRequest request) {
		UploadFileResponse response = uploadFileService.addHospitalFile(
				uploadFile, id, request);
		return new ResponseEntity<UploadFileResponse>(response, HttpStatus.OK);
	}
}
