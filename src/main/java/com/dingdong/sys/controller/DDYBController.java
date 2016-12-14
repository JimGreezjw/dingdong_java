package com.dingdong.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

import com.dingdong.common.exception.LockFailureException;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.DDYBService;
import com.dingdong.sys.service.impl.JsSDKConfig;
import com.dingdong.sys.vo.response.WxResponse;

/**
 * 
 * @author lqm
 * 
 */
@Controller
@RequestMapping(value = "/ddyb", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "叮咚医邦接口")
public class DDYBController {

	private static final Logger LOG = LoggerFactory
			.getLogger(DDYBController.class);

	private static final int LIMITED = 100;
	@Autowired
	private DDYBService ddybService;

	@Autowired
	private DoctorMapper doctorMapper;

	@ApiOperation(value = "叮咚医邦消息处理", notes = "")
	@RequestMapping(value = "/event", produces = { "application/text;charset=UTF-8" }, method = RequestMethod.GET)
	public ResponseEntity<String> firstValid(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			LOG.info("begn firstValid！");
			/** 判断是否是微信接入激活验证，只有首次接入验证时才会收到echostr参数，此时需要把它直接返回 */
			String echostr = request.getParameter("echostr");
			if (!StringUtils.isEmpty(echostr)) {
				return new ResponseEntity<String>(echostr, HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());

		}

		return new ResponseEntity<String>("验证失败", HttpStatus.OK);
	}

	@ApiOperation(value = "公众号首页登录页面的跳转处理", notes = "会自动在url后串接上userId与role值")
	@RequestMapping(value = "/loginCallback", method = RequestMethod.GET)
	public void wxLoginCallback(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");

		try {
			String code = request.getParameter("code");
			String state = request.getParameter("state");
			String destUrl = request.getParameter("destUrl");
			LOG.info("WXCALLBACK：the code is " + code + " the state is "
					+ state);
			LOG.info("WXCALLBACK：the destUrl is " + destUrl);
			response.setContentType("text/html;charset=utf-8");
			String openId = ddybService.getOpenId(code);

			LOG.info("url获取的   openId为：" + openId);
			if (StringUtils.isBlank(openId) || "null".equals(openId))
				throw new ServletException("无法获取到微信用户号信息，请稍后重试!");

			User user = ddybService.getUser(openId);

			// if (user == null) {
			// return;
			// // user = ddybService.newUser(openId, null,
			// // User.Role.DOCTOR.getId());
			// }

			// jsonResult.put("openId", openId);
			if (user != null) {
				destUrl += "&userId=" + user.getId();
				// 判断是请求进入医生还是员工界面
				// String role = request.getParameter("role");
				// LOG.info("the request role is {} ", role);
				// if ("doctor".equals(role)) {
				List<Doctor> doctors = this.doctorMapper.findByUserId(user
						.getId());
				if (CollectionUtils.isNotEmpty(doctors)) {
					Doctor doctor = doctors.get(0);
					// 医生的状态位必须有效才能写入
					if (doctor.getStatus().equals(
							Doctor.Status.SIGNED.getValue()))
						destUrl += "&doctorId=" + doctors.get(0).getId();
				}
				// }

				LOG.info("the destUrl is" + destUrl);
				response.sendRedirect(destUrl);
			}

		} catch (Exception e) {
			LOG.error(e.getMessage());

		}

	}

	@ApiOperation(value = "叮咚医邦消息处理", notes = "")
	@RequestMapping(value = "/event", produces = { "application/text;charset=UTF-8" }, method = RequestMethod.POST)
	public ResponseEntity<String> wxEventPro(HttpServletRequest request) {

		LOG.info("begin wxEventPro");
		String returnMsg = "success";
		String xml = "";
		try {
			/** 读取接收到的xml消息 */
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			xml = sb.toString(); // 此即为接收到微信端发送过来的xml数据
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("微信xml消息处理时发生错误！", e);
		}

		// xml =
		// "<xml><ToUserName><![CDATA[gh_b12ae814c346]]></ToUserName><FromUserName><![CDATA[oOqRWvwbu8U_tRm8Nv1KzxOB8Jq8]]></FromUserName><CreateTime>1452899569</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[subscribe]]></Event><EventKey><![CDATA[]]></EventKey></xml>";

		LOG.info("微信输入xml 为" + xml);
		if (StringUtils.isNotEmpty(xml)) {
			WxResponse response = new WxResponse();
			for (int i = 0; i++ < LIMITED;) {
				try {
					response = ddybService.eventPro(xml);
					break;
				} catch (LockFailureException e) {
					// TODO Auto-generated catch block
					LOG.warn(e.getMessage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LOG.error(e.getMessage());
					break;
				}
			}

			if (StringUtils.isNotBlank(response.getMsg()))
				returnMsg = response.getMsg();

		}

		// returnMsg = "成功了！";
		LOG.info("the return xml is " + returnMsg);
		return new ResponseEntity<String>(returnMsg, HttpStatus.OK);// wxAuth(request,

	}

	// response);

	@ApiOperation(value = "获得jsapi的config信息", notes = "")
	@RequestMapping(value = "/jsConfig", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<JsSDKConfig> getJsConfig(
			@ApiParam(value = "网址", required = true) @RequestParam(value = "url", required = true) String url) {

		try {

			JsSDKConfig jsSDKConfig = ddybService.createJsSign(url);
			return new ResponseEntity<JsSDKConfig>(jsSDKConfig, HttpStatus.OK);

		} catch (Exception e) {
			LOG.error(e.getMessage());

		}
		LOG.info("error ！");

		return null;

	}

	@ApiOperation(value = "发送客户维护消息", notes = "")
	@RequestMapping(value = "/customMsgSender", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<WxResponse> customMsgSender(
			@ApiParam(value = "openId", required = true) @RequestParam(value = "openId", required = true) String openId,
			@ApiParam(value = "content", required = true) @RequestParam(value = "content", required = true) String content) {

		WxResponse response = new WxResponse();
		try {

			response = this.ddybService.sendCustomTextMsg(openId, content);

		} catch (Exception e) {
			LOG.error(e.getMessage());

		}

		return new ResponseEntity<WxResponse>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "微信消息处理", notes = "")
	@RequestMapping(value = "/eventPro", produces = { "application/text;charset=UTF-8" }, method = RequestMethod.POST)
	public ResponseEntity<WxResponse> eventPro(
			@ApiParam(value = "事件xml", required = false) @RequestParam(value = "eventXml", required = false) String eventXml) {
		LOG.info("begin wxPayPro");
		// String
		// eventXml="<xml><ToUserName><![CDATA[gh_b12ae814c346]]></ToUserName><FromUserName><![CDATA[oOqRWvwbu8U_tRm8Nv1KzxOB8Jq8]]></FromUserName><CreateTime>1458384904</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[subscribe]]></Event><EventKey><![CDATA[]]></EventKey></xml>";

		WxResponse response = this.ddybService.eventPro(eventXml);
		// returnMsg = "成功了！";

		return new ResponseEntity<WxResponse>(response, HttpStatus.OK);// wxAuth(request,
	}

	@ApiOperation(value = "获得某一医生的邀请其他医生的二维码", notes = "")
	@RequestMapping(value = "/inviteDoctorQr/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> getInviteDoctorQrUrl(
			@ApiParam(value = "医生编号", required = true) @PathVariable(value = "id") long id,
			HttpServletResponse response) {
		// 叮咚医邦在10万到20万之间是邀请其他医生的二维码
		long sceneId = 100000 + id;
		String url = this.ddybService.getTempQrUrl(sceneId);
		if (StringUtils.isNotBlank(url))
			try {
				response.sendRedirect(url);
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}

		return new ResponseEntity<String>("", HttpStatus.OK);

	}

}
