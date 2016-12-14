package com.dingdong.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dingdong.register.mapper.RegisterMapper;
import com.dingdong.register.model.Register;
import com.dingdong.sys.service.CommonMessageService;
import com.dingdong.sys.service.SmsValidateMessageService;
import com.dingdong.sys.service.UserValidateService;
import com.dingdong.sys.vo.request.ValidateMsgResponse;
import com.dingdong.sys.vo.util.SmsConst;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "/sms", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "短信")
public class SmsController {

	@Autowired
	private SmsValidateMessageService smsValidateMessageService;
	@Autowired
	private UserValidateService userValidateService;
	@Autowired
	private CommonMessageService commonMessageService;

	@Autowired
	private RegisterMapper registerMapper;

	@ApiOperation(value = "获得短信验证码", notes = "获得短信验证码")
	@RequestMapping(value = "/validNum", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ValidateMsgResponse> getValidNum(
			@ApiParam(value = "手机号") @RequestParam(value = "mobileNo") String mobileNo) {

		// 发送信息
		Map<String, Object> retMap = smsValidateMessageService.sendSmsMessage(
				mobileNo, SmsConst.TEMPLATE_VALIDATION, 10);
		ValidateMsgResponse responce = new ValidateMsgResponse();
		responce.setRetMap(retMap);

		return new ResponseEntity<ValidateMsgResponse>(responce, HttpStatus.OK);
	}

	@ApiOperation(value = "用户验证", notes = "用户验证,通过验证后才写入电话号码")
	@RequestMapping(value = "/validation", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	public ResponseEntity<ValidateMsgResponse> doValidate(
			@ApiParam(value = "用户编号", required = true) @RequestParam(value = "userId", required = true) long userId,
			@ApiParam(value = "手机号", required = true) @RequestParam(value = "mobileNo", required = true) String mobileNo,
			@ApiParam(value = "短信验证码", required = true) @RequestParam(value = "msgCode", required = true) String msgCode,
			@ApiParam(value = "图片验证码", required = false) @RequestParam(value = "imgCode", required = false) String imgCode,
			@ApiParam(value = "模式", required = false, defaultValue = "0") @RequestParam(value = "mode", required = false, defaultValue = "0") int mode) {

		// 短信验证
		Map<String, Object> retMap = userValidateService.doValidate(userId,
				mobileNo, msgCode, imgCode, mode);
		ValidateMsgResponse responce = new ValidateMsgResponse();
		responce.setRetMap(retMap);

		return new ResponseEntity<ValidateMsgResponse>(responce, HttpStatus.OK);
	}

	@SuppressWarnings("deprecation")
	@ApiOperation(value = "发送成功排队的短信", notes = "发送成功排队的短信")
	@RequestMapping(value = "/sendSuccessQueueMessageOld", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ValidateMsgResponse> sendSuccessQueueMessageOld(
			@ApiParam(value = "手机号", required = true) @RequestParam(value = "mobileNo", required = true) String mobileNo) {
		// 发送信息
		Map<String, Object> retMap = commonMessageService.sendQueueMessage(
				mobileNo, "何先生", "孙坚", 6, 10, 6);
		ValidateMsgResponse responce = new ValidateMsgResponse();
		responce.setRetMap(retMap);

		return new ResponseEntity<ValidateMsgResponse>(responce, HttpStatus.OK);
	}

	// @ApiOperation(value = "发送成功排队的短信", notes = "发送成功排队的短信")
	// @RequestMapping(value = "/sendSuccessQueueMessage", produces = {
	// MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	// public ResponseEntity<ValidateMsgResponse> sendSuccessQueueMessage(
	// @ApiParam(value = "手机号", required = true) @RequestParam(value =
	// "mobileNo", required = true) String mobileNo) {
	// // 发送信息
	// // Map<String, Object> retMap = commonMessageService.sendQueueMessage(
	// // mobileNo, "李先生", "孙坚", "德济医院", 6, 10, 6);
	// // ValidateMsgResponse responce = new ValidateMsgResponse();
	// // responce.setRetMap(retMap);
	//
	// return new ResponseEntity<ValidateMsgResponse>(responce, HttpStatus.OK);
	// }

	@ApiOperation(value = "发送成功挂号的短信", notes = "发送成功挂号的短信")
	@RequestMapping(value = "/sendSuccessOrderMessage", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ValidateMsgResponse> sendSuccessOrderMessage(
			@ApiParam(value = "手机号", required = true) @RequestParam(value = "mobileNo", required = true) String mobileNo) {
		// 发送信息
		Register register = this.registerMapper.findById(201);
		Map<String, Object> retMap = commonMessageService.sendOrderMessage(
				register, null, null, null);
		ValidateMsgResponse responce = new ValidateMsgResponse();
		responce.setRetMap(retMap);

		return new ResponseEntity<ValidateMsgResponse>(responce, HttpStatus.OK);
	}

	@ApiOperation(value = "发送人数达到预设值的短信", notes = "发送人数达到预设值的短信")
	@RequestMapping(value = "/sendReachPresetNumMessage", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ValidateMsgResponse> sendReachPresetNumMessage(
			@ApiParam(value = "手机号", required = true) @RequestParam(value = "mobileNo", required = true) String mobileNo) {
		// 发送信息
		Map<String, Object> retMap = commonMessageService
				.sendReachPresetNumMessage(mobileNo, "孙坚", "北医三院", 11, 10);
		ValidateMsgResponse responce = new ValidateMsgResponse();
		responce.setRetMap(retMap);

		return new ResponseEntity<ValidateMsgResponse>(responce, HttpStatus.OK);
	}

	@ApiOperation(value = "发送提醒出诊的短信", notes = "发送提醒出诊的短信")
	@RequestMapping(value = "/alarmWorkMessage", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<ValidateMsgResponse> alarmWorkMessage(
			@ApiParam(value = "手机号", required = true) @RequestParam(value = "mobileNo", required = true) String mobileNo) {
		// 发送信息
		Map<String, Object> retMap = commonMessageService.alarmWorkMessage(
				mobileNo, "孙坚", "2016-01-01上午", "北医三院");
		ValidateMsgResponse responce = new ValidateMsgResponse();
		responce.setRetMap(retMap);

		return new ResponseEntity<ValidateMsgResponse>(responce, HttpStatus.OK);
	}
}
