package com.dingdong.sys.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.util.HttpUtils;
import com.dingdong.sys.exception.WxErrorCode;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.mapper.WechatTokenMapper;
import com.dingdong.sys.model.User;
import com.dingdong.sys.model.WechatToken;
import com.dingdong.sys.service.WxCommonService;
import com.dingdong.sys.service.WxJobService;
import com.dingdong.sys.vo.response.WxResponse;

@Service("wxCommonService")
@Transactional
public abstract class WxCommonServiceImpl implements WxCommonService {
	protected static final Logger LOG = LoggerFactory
			.getLogger(WxCommonServiceImpl.class);

	@Autowired
	protected UserMapper userMapper;

	@Autowired
	protected WxJobService wxJobService;

	@Autowired
	protected WechatTokenMapper wechatTokenMapper;

	protected static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

	protected static final String QR_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";

	// 微信统一下单url
	protected static final String ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	protected static final String CUSTOM_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

	protected String appId;

	protected String appSecretId;

	protected String welcomeTitle;
	protected String welcomeContent;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecretId() {
		return appSecretId;
	}

	public void setAppSecretId(String appSecretId) {
		this.appSecretId = appSecretId;
	}

	public String getWelcomeTitle() {
		return welcomeTitle;
	}

	public void setWelcomeTitle(String welcomeTitle) {
		this.welcomeTitle = welcomeTitle;
	}

	public String getWelcomeContent() {
		return welcomeContent;
	}

	public void setWelcomeContent(String welcomeContent) {
		this.welcomeContent = welcomeContent;
	}

	@Override
	public User newUser(String accessToken, String openId, String qrsceneId,
			Integer role) {
		LOG.info("begin new User:" + openId + " qrsceneId is " + qrsceneId);
		User user = null;
		try {

			LOG.info("begin new User:" + openId);
			user = new User();
			user.setOpenId(openId);
			if (StringUtils.isNotEmpty(qrsceneId))
				user.setQrsceneId(qrsceneId);
			if (role != null)
				user.setRole(role);
			//
			// 还应该根据条码制订角色
			// 这一部分应该异步化
			// lqm todo

			this.userMapper.addUser(user);
			LOG.info("call getWxUserInfo method start  , for  async test ");
			this.wxJobService.getWxUserInfo(user, accessToken, openId, this);
			LOG.info("call getWxUserInfo end  , for  async test ");
		} catch (Exception e) {
			LOG.error(e.getMessage());
			// e.printStackTrace();
		}
		LOG.info("newUser method  end, for  async test ");
		return user;
	}

	/**
	 * 发送客户服务文本消息
	 * 
	 * @param openId
	 * @param content
	 */
	@Override
	public WxResponse sendCustomTextMsg(String openId, String content) {
		LOG.info("sendCustomTextMsg openId={},content={}", openId, content);

		String msg = "{\"touser\":\"" + openId
				+ "\",\"msgtype\":\"text\",\"text\":{\"content\":\"" + content
				+ "\"}}";

		return this.sendCustomMsg(msg);

	}

	@Override
	public String getAccessToken() {
		// 时刻要注意将来应用服务器是多个,所以要写入数据库
		WechatToken wechatToken = this.wechatTokenMapper.findById(this.appId);
		if (wechatToken == null) {
			this.autoFetchAccessToken();// 自动获取jsticket
			wechatToken = this.wechatTokenMapper.findById(this.appId);
			if (wechatToken != null)
				return wechatToken.getAccessToken();
		}
		return wechatToken.getAccessToken();

	}

	/**
	 * 最后应该写到数据库中去，供多处应用使用 cron = "0 24 * * * ?", zone = "GMT+08:00",
	 * initialDelay = 120 initialDelay会报错
	 */
	@Override
	@Scheduled(cron = "0 55 * * * ?", zone = "GMT+08:00")
	public void autoFetchJsTicket() {
		String strTicket = null;
		LOG.info("the appi is" + this.appId);
		WechatToken wechatToken = this.wechatTokenMapper.findById(this.appId);
		LOG.info("the wechatToken is " + wechatToken);
		LOG.info("the wechatToken is " + wechatToken.getAccessToken()
				+ "  appId is " + wechatToken.getAccessToken()
				+ "  the createTime is "
				+ wechatToken.getAccessTokenLastUpdate());
		if (null == wechatToken
				|| StringUtils.isBlank(wechatToken.getAccessToken())) {
			LOG.error("autoFetchJsTicket error 无法获取微信getAccessToken信息!");

		}
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
				+ wechatToken.getAccessToken() + "&type=jsapi";
		try {
			String ticket = HttpUtils.sendGet(url);
			LOG.info("the jsticket is " + ticket);
			JSONObject jo = JSONObject.fromObject(ticket); // 转换成JSONObject对象
			strTicket = (String) jo.get("ticket");
			this.wechatTokenMapper.updateJsapiTicket(appId, strTicket,
					new Date());
			// return strTicket;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}

	}

	/**
	 * 最后应该写到数据库中去，供多处应用使用
	 */
	@Override
	@Scheduled(cron = "0 54 * * * ?", zone = "GMT+08:00")
	public void autoFetchAccessToken() {
		String url = ACCESS_TOKEN_URL + "&appid=" + this.appId + "&secret="
				+ this.appSecretId;
		String accessToken = null;
		// log.info(url);
		try {
			String ticket = HttpUtils.sendGet(url);
			JSONObject jo = JSONObject.fromObject(ticket); // 转换成JSONObject对象

			accessToken = (String) jo.get("access_token");
			LOG.info("the accessToken is " + accessToken);
			WechatToken wechatToken = this.wechatTokenMapper
					.findById(this.appId);
			if (wechatToken == null) {
				this.wechatTokenMapper.addWechatToken(this.appId, accessToken,
						new Date());
			} else {
				this.wechatTokenMapper.updateAccessToken(this.appId,
						accessToken, new Date());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}

	}

	//
	// 微信基础访问令牌
	public String getJsapiTicket() {
		// 时刻要注意将来应用服务器是多个,所以要写入数据库
		// String ticket = null;
		WechatToken wechatToken = this.wechatTokenMapper.findById(this.appId);
		if (wechatToken == null) {
			this.autoFetchAccessToken();
			this.autoFetchJsTicket();// 自动获取jsticket;
		} else if (StringUtils.isBlank(wechatToken.getJsapiTicket())) {
			this.autoFetchJsTicket();// 自动获取jsticket;
		}
		wechatToken = this.wechatTokenMapper.findById(this.appId);
		if (wechatToken != null)
			return wechatToken.getJsapiTicket();

		return null;

	}

	@Override
	public JsSDKConfig createJsSign(String url) {
		// TODO Auto-generated method stub
		Map<String, String> ret = JsSDKSign.sign(this.getJsapiTicket(), url);
		JsSDKConfig jsSDKConfig = new JsSDKConfig();
		jsSDKConfig.setAppId(this.appId);
		jsSDKConfig.setNonceStr(ret.get("nonceStr"));
		jsSDKConfig.setTimestamp(Long.valueOf(ret.get("timestamp")));
		jsSDKConfig.setSignature(ret.get("signature"));
		LOG.info(jsSDKConfig.toString());
		return jsSDKConfig;

	}

	@Override
	public String getOpenId(String codeId) {
		String openId = null;

		try {
			String strAccessTocken = HttpUtils
					.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid="
							+ this.appId
							+ "&secret="
							+ this.appSecretId
							+ "&code="
							+ codeId
							+ "&grant_type=authorization_code");

			LOG.info("the AccessTocken id is" + strAccessTocken);

			JSONObject jsonObject = JSONObject.fromObject(strAccessTocken);
			if (jsonObject.containsKey("openid"))
				openId = (String) jsonObject.get("openid");
			// String unionid = (String) jsonObject.get("unionid");

			LOG.info("the  openId fectched is " + openId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		return openId;
	}

	@Override
	public String getMediaPreUrl() {
		return "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
				+ this.getAccessToken() + "&media_id=";

	}

	@Override
	public String getInviteQrUrl(String employeeUid, String qrSceneId) {
		// TODO Auto-generated method stub
		return null;
	}

	public WxResponse subsribePro(Element eventElement) {
		LOG.info("开始进行微信公众号订阅事件处理");
		return this.scanPro(eventElement);
	}

	/**
	 * 事件处理xml
	 * 
	 * @param eventXml
	 * @return
	 */
	@Override
	public WxResponse eventPro(String eventXml) {
		WxResponse response = new WxResponse();

		// log.error("the eventXml is " + eventXml);
		Element root = null;
		try {
			Document document = DocumentHelper.parseText(eventXml);
			root = document.getRootElement();
		} catch (DocumentException e) {
			LOG.error(e.getMessage());
		}

		if (null == root) {
			response.setErrorCode(WxErrorCode.INVALID_XML);
			return response;
		}

		// if (StringUtils.isBlank(openId))
		// throw new ServletException("无法取到微信OpenId !");
		String msgType = root.element("MsgType").getText();
		LOG.info("the msg type  are " + msgType);
		if ("event".equals(msgType)) {

			String event = root.element("Event").getText();

			LOG.info("the event is " + event);

			// lqm todo
			// 应该怎样提示他开始使用呢
			if ("subscribe".equals(event))// 如果订阅
			{
				return this.subsribePro(root);
				// fromUserName为 openId
			} else if ("unsubscribe".equals(event)) {// 如果取消订阅
				return this.unsubsribePro(root);
			} else if ("SCAN".equals(event)) {// 如果是扫码
				return this.scanPro(root);

			} else if ("CLICK".equals(event))// 如果订阅
			{
				String eventKey = root.element("EventKey").getText();
				LOG.info("the event key is  " + eventKey);
				// this.userCookierPro(root, request, response);

				if ("SUGGESTION".equals(eventKey))// 如果订阅我的建议
				{
					return this.suggestPro(root);
				}
			}
		} else if ("text".equals(msgType)) {
			return this.textPro(root);
		}

		return response;

	}

	/**
	 * 扫描带参数二维码事件
	 */

	public WxResponse suggestPro(Element eventElement) {
		LOG.info("开始进行微信公众号扫码事件处理");
		WxResponse wxResponse = new WxResponse();
		String fromUserName = eventElement.element("FromUserName").getText();
		String toUserName = eventElement.element("ToUserName").getText();

		String content = ("欢迎反馈建议！\n请点击左下角图标直接发消息即可。");

		wxResponse.setMsg(this.generateTextMsg(fromUserName, toUserName,
				content));

		return wxResponse;
	}

	/**
	 * 扫描带参数二维码事件
	 */

	public abstract WxResponse scanPro(Element eventElement);

	/**
	 * 文本消息处理事件
	 */

	public abstract WxResponse textPro(Element eventElement);

	/**
    *
    */
	public WxResponse unsubsribePro(Element eventElement) {
		// try {
		// String fromUserName = eventElement.element("FromUserName").getText();
		// String toUserName = eventElement.element("ToUserName").getText();
		// String eventKey = eventElement.element("EventKey").getText();
		//
		// log.info("开始进行微信公众号订阅事件处理。openId=" + fromUserName + ",场景号：" +
		// eventKey);
		//
		// User user = this.userDAO.getUserByWxOpenId(fromUserName);
		//
		// // 激活新用户
		// if (user != null) {
		// log.info("暂时休眠用户！");
		// user.setStatus(User.STATUS_UNACTIVATED);
		// this.userManager.update(user);
		// }
		//
		// } catch (BaseBusinessException e) {
		// log.error(e.getMessage());
		// } catch (Exception e) {
		// log.error(e.getMessage());
		// }

		return null;

	}

	@Override
	public void sendRequestWxCodeId(String userUid, String hostInfo) {

		try {

			String redirectUrl = hostInfo
					+ "/ysproject/WechatServlet?type=wxbindCallBack&userUid="
					+ userUid;

			String wxUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
					+ this.appId
					+ "&redirect_uri="
					+ URLEncoder.encode(redirectUrl, "utf-8")
					+ "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
			LOG.info("the wx code request url is " + wxUrl);
			URL url = new URL(wxUrl);
			url.openConnection();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}

	}

	/**
	 * 扫描带参数二维码事件
	 */

	public WxResponse commonMsgPro(Element eventElement) {
		WxResponse wxResponse = new WxResponse();
		LOG.info("开始进行微信公众号扫码事件处理");
		try {
			String fromUserName = eventElement.element("FromUserName")
					.getText();
			String toUserName = eventElement.element("ToUserName").getText();

			String content = ("叮咚门诊欢迎您！");

			wxResponse.setMsg(this.generateTextMsg(fromUserName, toUserName,
					content));

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		LOG.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		return wxResponse;
	}

	// 转发给客服
	protected String transferToCS(String fromUserName, String toUserName) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<xml>");
		buffer.append("<ToUserName><![CDATA[" + fromUserName
				+ "]]></ToUserName>");
		buffer.append("<FromUserName><![CDATA[" + toUserName
				+ "]]></FromUserName>");
		buffer.append("<CreateTime>"
				+ String.valueOf(System.currentTimeMillis()) + "</CreateTime>");
		buffer.append("<MsgType><![CDATA[transfer_customer_service]]></MsgType>");

		buffer.append("</xml>");

		LOG.info(buffer.toString());

		return buffer.toString();
	}

	protected String generateTextMsg(String fromUserName, String toUserName,
			String content) {
		StringBuffer buffer = new StringBuffer();
		LOG.info("the content is " + content);
		buffer.append("<xml>");
		buffer.append("<ToUserName><![CDATA[" + fromUserName
				+ "]]></ToUserName>");
		buffer.append("<FromUserName><![CDATA[" + toUserName
				+ "]]></FromUserName>");
		buffer.append("<CreateTime>"
				+ String.valueOf(System.currentTimeMillis()) + "</CreateTime>");
		buffer.append("<MsgType><![CDATA[text]]></MsgType>");

		buffer.append("<Content><![CDATA[");
		buffer.append(content);
		buffer.append("]]></Content>");

		buffer.append("</xml>");

		LOG.info(buffer.toString());

		return buffer.toString();
	}

	/**
	 * 取得带参数二维码
	 * 
	 * @param codeId
	 * @return
	 */
	@Override
	public String getPersistQrUrl(long id) {

		String params = "{\"action_name\":\"QR_LIMIT_SCENE\",\"action_info\": {\"scene\": {\"scene_id\": "
				+ id + "}}}";

		String result = HttpUtils.sendPost(QR_URL + getAccessToken(), params);
		if (result != null) {
			LOG.info("the result id is" + result);

			if (result.contains("errcode")
					&& (result.contains("40001") || result.contains("42001") || result
							.contains("40014"))) {
				this.autoFetchAccessToken();
				String token = this.getAccessToken();
				if (null != token) {
					result = HttpUtils.sendPost(QR_URL + token, params);
					LOG.info(result);
				}
			}

			JSONObject jsonObject = JSONObject.fromObject(result);
			String ticket = "";
			if (jsonObject.containsKey("ticket"))
				ticket = (String) jsonObject.get("ticket");
			// String unionid = (String) jsonObject.get("unionid");
			if (StringUtils.isNotBlank(ticket))
				return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="
						+ ticket;
		}
		return null;
	}

	@Override
	public String getTempQrUrl(long id) {
		// long sceneId = MAX_LIMIT_SCENEID + id;
		long sceneId = id;

		LOG.info("the qr sceneId is " + sceneId);

		String params = "{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\":"
				+ sceneId + "}}}";

		String result = HttpUtils.sendPost(QR_URL + getAccessToken(), params);

		if (result.contains("errcode")
				&& (result.contains("40001") || result.contains("42001") || result
						.contains("40014"))) {
			this.autoFetchAccessToken();
			String token = this.getAccessToken();
			if (null != token) {
				result = HttpUtils.sendPost(QR_URL + token, params);
				LOG.info(result);
			}
		}

		LOG.info("the result id is" + result);
		if (StringUtils.isNotBlank(result)) {
			JSONObject jsonObject = JSONObject.fromObject(result);
			String ticket = "";
			if (jsonObject.containsKey("ticket"))
				ticket = (String) jsonObject.get("ticket");
			if (StringUtils.isNotBlank(ticket))
				return "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="
						+ ticket;
		}
		return null;
	}

	public WxResponse sendCustomMsg(String msg) {
		LOG.info("sendCustomTextMsg msg={}", msg);
		WxResponse response = new WxResponse();

		String url = CUSTOM_SEND_URL + this.getAccessToken();
		String result = HttpUtils.sendPost(url, msg);

		LOG.info(result);

		if (result.contains("errcode")
				&& (result.contains("40001") || result.contains("42001") || result
						.contains("40014"))) {
			// 重新自动获取accessToken
			this.autoFetchAccessToken();
			String token = this.getAccessToken();
			if (null != token) {
				url = CUSTOM_SEND_URL + token;
				result = HttpUtils.sendPost(url, msg);
				LOG.info(result);
			}
		}
		response.setMsg(result);
		return response;

	}

}
