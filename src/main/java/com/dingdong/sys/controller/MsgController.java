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

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.PageInfo;
import com.dingdong.conf.PageUtils;
import com.dingdong.sys.model.Msg;
import com.dingdong.sys.service.MsgService;
import com.dingdong.sys.vo.response.MsgResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "消息管理操作")
public class MsgController {

	@Autowired
	private MsgService msgService;

	@ApiOperation(value = "添加消息", notes = "")
	@RequestMapping(value = "/msgs", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<MsgResponse> addMsg(
			@ApiParam(value = "发送用户号", required = true) @RequestParam(value = "fromUserId", required = true) long fromUserId,
			@ApiParam(value = "接收用户号", required = true) @RequestParam(value = "toUserId", required = true) long toUserId,
			@ApiParam(value = "消息内容", required = true) @RequestParam(value = "content", required = true) String content,
			@ApiParam(value = "类型，缺省为文本", required = false, defaultValue = "0") @RequestParam(value = "type", required = true, defaultValue = "0") int type) {
		Msg msg = new Msg();
		msg.setFromUserId(fromUserId);
		msg.setToUserId(toUserId);

		msg.setStatus(Msg.Status.RECEIVE.getId());
		msg.setContent(content);
		msg.setType(type);
		MsgResponse response = this.msgService.addMsg(msg);
		return new ResponseEntity<MsgResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "添加消息", notes = "")
	@RequestMapping(value = "/msgs/{toId}/read", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> addMsg(
			@ApiParam(value = "接收方用户编号", required = true) @PathVariable(value = "toId") long toId) {
		ResponseBody response = this.msgService.updateMsgRead(toId);

		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "通过fromUserId,toUserId查找两人的聊天记录", notes = "")
	@RequestMapping(value = "/msgs", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<MsgResponse> findChatsByFromUserIdAndToUserId(
			@ApiParam(value = "发送用户号", required = true) @RequestParam(value = "fromUserId", required = true) long fromUserId,
			@ApiParam(value = "接收用户号", required = true) @RequestParam(value = "toUserId", required = true) long toUserId,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page

	) {
		PageInfo pageInfo = new PageInfo(page, size, null, null);
		MsgResponse response = this.msgService
				.findChatsByFromUserIdAndToUserId(fromUserId, toUserId,
						pageInfo);
		return new ResponseEntity<MsgResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "通过某人未读的聊天消息", notes = "")
	@RequestMapping(value = "/users/{userId}/msgs/unread", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<MsgResponse> findUnReadMsgs(
			@ApiParam(value = "用户号") @PathVariable(value = "userId") long userId,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page

	) {
		PageInfo pageInfo = new PageInfo(page, size, null, null);
		MsgResponse response = this.msgService.findUnReadMsgs(userId, pageInfo);

		return new ResponseEntity<MsgResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "通过某人的所有聊天消息", notes = "")
	@RequestMapping(value = "/users/{userId}/msgs/{endDate}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<MsgResponse> findUserReadMsgs(
			@ApiParam(value = "用户号") @PathVariable(value = "userId") long userId,
			@ApiParam(value = "最早日期") @PathVariable(value = "endDate") String endDate,
			@ApiParam(value = "分页-每页大小", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) @RequestParam(value = "size", required = false, defaultValue = PageUtils.PAGE_SIZE_STR) Integer size,
			@ApiParam(value = "分页-当前页数", required = false, defaultValue = PageUtils.PAGE_NUM_STR) @RequestParam(value = "page", required = false, defaultValue = PageUtils.PAGE_NUM_STR) Integer page) {
		PageInfo pageInfo = new PageInfo(page, size, null, null);
		MsgResponse response = this.msgService.findUserMsgs(userId, endDate,
				pageInfo);
		return new ResponseEntity<MsgResponse>(response, HttpStatus.OK);
	}
}
