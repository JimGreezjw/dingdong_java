package com.dingdong.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dingdong.sys.service.YusFileService;
import com.dingdong.sys.vo.response.YusFileResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "文件管理操作")
public class YusFileController {

	@Autowired
	private YusFileService yusFileService;

	@ApiOperation(value = "添加文件", notes = "")
	@RequestMapping(value = "/yusFiles", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<YusFileResponse> addYusFile(
			@ApiParam(value = "用户号", required = true) @RequestParam(value = "userId", required = true) long userId,
			@ApiParam(value = "微信服务器编号", required = true) @RequestParam(value = "wxServerId", required = true) String wxServerId) {
		YusFileResponse response = this.yusFileService.addYusFileForWx(userId,
				wxServerId);
		return new ResponseEntity<YusFileResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "通过id查找文件", notes = "")
	@RequestMapping(value = "/yusFiles/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<YusFileResponse> getYusFileById(
			@PathVariable(value = "id") Long id) {
		YusFileResponse response = this.yusFileService.findYusFileById(id);
		return new ResponseEntity<YusFileResponse>(response, HttpStatus.OK);
	}

}
