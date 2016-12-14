package com.dingdong.inquire;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.dingdong.sys.model.Msg;
import com.google.gson.GsonBuilder;

/**
 * Created by zjw on 2016/2/17.
 */
@Controller
@Api(value = "在线聊天接口")
public class ChatController {

	static Logger logger = LoggerFactory.getLogger(ChatController.class);

	@Resource
	SystemWebSocketHandler handler;

	@Bean
	public SystemWebSocketHandler systemWebSocketHandler() {
		return new SystemWebSocketHandler();
	}

	@ApiOperation(value = "系统广播", notes = "系统广播-向所有用户推送消息")
	@RequestMapping("/userChat/{fromId}/auditing/{content}")
	@ResponseBody
	public void auditing(@PathVariable(value = "fromId") Long fromId,
			@PathVariable(value = "content") String content,
			HttpServletRequest request) {
		Msg msg = new Msg();
		msg.setCreateTime(new Date());
		msg.setFromUserId(fromId);
		msg.setContent(content);
		try {
			handler.broadcast(new TextMessage(new GsonBuilder()
					.setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ApiOperation(value = "用户之间的交流", notes = "用户之间的交流-发起用户/接收用户")
	@RequestMapping("/userChat/{fromId}")
	@ResponseBody
	public void chatController(@PathVariable(value = "fromId") Long fromId,
			HttpServletRequest request) {
		request.getSession().setAttribute("uid", fromId);
	}
}