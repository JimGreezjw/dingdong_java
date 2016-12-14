package com.dingdong.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.DDMZService;
import com.dingdong.sys.service.impl.JsSDKConfig;
import com.dingdong.sys.vo.response.PayTokenResponse;
import com.dingdong.sys.vo.response.WxResponse;

/**
 * 
 * @author lqm
 * 
 */
@Controller
@RequestMapping(value = "/ddmz", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "叮咚门诊接口")
public class DDMZController {

	private static final Logger LOG = LoggerFactory
			.getLogger(DDMZController.class);

	private static final int LIMITED = 100;
	@Autowired
	private DDMZService ddmzService;

	@ApiOperation(value = "获得所有患者信息", notes = "获得患者信息-获得所有患者信息")
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
			String openId = ddmzService.getOpenId(code);

			LOG.info("url获取的   openId为：" + openId);
			if (StringUtils.isBlank(openId) || "null".equals(openId))
				throw new ServletException("无法获取到微信用户号信息，请稍后重试!");

			User user = ddmzService.getUser(openId);

			// if (user == null) {
			// return;
			// // user = ddmzService.newUser(openId, null,
			// // User.Role.PATIENT.getId());
			// }

			// jsonResult.put("openId", openId);
			if (user != null) {
				destUrl += "&userId=" + user.getId();

				LOG.info("the destUrl is" + destUrl);
				response.sendRedirect(destUrl);
			}

		} catch (Exception e) {
			LOG.error(e.getMessage());

		}

	}

	@ApiOperation(value = "微信消息处理", notes = "")
	@RequestMapping(value = "/eventPro", produces = { "application/text;charset=UTF-8" }, method = RequestMethod.POST)
	public ResponseEntity<WxResponse> eventPro(
			@ApiParam(value = "事件xml", required = false) @RequestParam(value = "eventXml", required = false) String eventXml) {
		LOG.info("begin wxPayPro");
		// String
		// eventXml="<xml><ToUserName><![CDATA[gh_b12ae814c346]]></ToUserName><FromUserName><![CDATA[oOqRWvwbu8U_tRm8Nv1KzxOB8Jq8]]></FromUserName><CreateTime>1458384904</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[subscribe]]></Event><EventKey><![CDATA[]]></EventKey></xml>";

		WxResponse response = this.ddmzService.eventPro(eventXml);
		// returnMsg = "成功了！";

		return new ResponseEntity<WxResponse>(response, HttpStatus.OK);// wxAuth(request,
	}

	@ApiOperation(value = "微信返回消息处理", notes = "")
	@RequestMapping(value = "/payPro", produces = { "application/text;charset=UTF-8" }, method = RequestMethod.POST)
	public ResponseEntity<String> wxPayPro(HttpServletRequest request,
			HttpServletResponse response) {
		LOG.info("begin wxPayPro");
		String returnMsg = "success";
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
			String xml = sb.toString(); // 此即为接收到微信端发送过来的xml数据

			LOG.info("*****************the input xml is " + xml);
			LOG.info("*****************StringUtils.isNotBlank(xml)  "
					+ StringUtils.isNotBlank(xml));
			if (StringUtils.isNotBlank(xml)) {
				LOG.info("begin to wxpro " + ddmzService.toString());
				returnMsg = ddmzService.payPro(xml);
				LOG.info("the wx return xml is " + returnMsg);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("wechat msg error******************888！", e);

		}
		// returnMsg = "成功了！";
		LOG.info("the return xml is " + returnMsg);
		return new ResponseEntity<String>(returnMsg, HttpStatus.OK);// wxAuth(request,
	}

	@ApiOperation(value = "微信消息处理", notes = "")
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
					response = ddmzService.eventPro(xml);
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

			JsSDKConfig jsSDKConfig = ddmzService.createJsSign(url);
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

			response = this.ddmzService.sendCustomTextMsg(openId, content);

		} catch (Exception e) {
			LOG.error(e.getMessage());

		}

		return new ResponseEntity<WxResponse>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "获得用户充值的支付口令信息", notes = "充值金额单位为元")
	@RequestMapping(value = "/payToken", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<PayTokenResponse> getJsPayToken(
			@ApiParam(value = "用户号", required = true) @RequestParam(value = "userId", required = true) long userId,
			@ApiParam(value = "充值金额（单位为元）", required = true) @RequestParam(value = "totalFee", required = true) float totalFee,
			HttpServletRequest request, HttpServletResponse response) {
		String remoteIp = request.getRemoteAddr();// "192.168.1.1";//
		PayTokenResponse payTokenResponse = ddmzService.createPayToken(userId,
				totalFee, remoteIp);
		return new ResponseEntity<PayTokenResponse>(payTokenResponse,
				HttpStatus.OK);
	}

	@ApiOperation(value = "获得某一编号的二维码", notes = "")
	@RequestMapping(value = "/qr/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> getPersistQr(
			@ApiParam(value = "编号", required = true) @PathVariable(value = "id") long id,
			HttpServletResponse response) {

		String url = ddmzService.getPersistQrUrl(id);
		if (StringUtils.isNotBlank(url))
			try {
				response.sendRedirect(url);
			} catch (Exception e) {
				LOG.error(e.getMessage());
			}

		return new ResponseEntity<String>("", HttpStatus.OK);

	}

}
