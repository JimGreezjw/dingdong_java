package com.dingdong.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dingdong.sys.service.SysTagService;
import com.dingdong.sys.vo.response.SysTagResponse;

/**
 * 
 * @author niukai
 * 
 */
@Controller
@RequestMapping(value = "/systag", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "系统标签")
public class SysTagController {

	@Autowired
	private SysTagService sysTagService;

	@ApiOperation(value = "获得所有系统标签", notes = "获得所有系统标签")
	@RequestMapping(value = "/tags", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<SysTagResponse> getAllUsers() {
		SysTagResponse response = sysTagService.findAllTags();
		return new ResponseEntity<SysTagResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "添加系统标签", notes = "添加系统标签")
	@RequestMapping(value = "/tags/addTag", method = RequestMethod.PATCH)
	public ResponseEntity<SysTagResponse> addTag(
			@ApiParam(value = "tagName", required = true) @RequestParam(value = "tagName", required = true) String tagName) {

		SysTagResponse response = sysTagService.addTag(tagName);
		return new ResponseEntity<SysTagResponse>(response, HttpStatus.OK);
	}
}
