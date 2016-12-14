package com.dingdong.sys.service.impl;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.sys.model.User;
import com.dingdong.sys.service.DDYBService;
import com.dingdong.sys.vo.response.WxResponse;

@Service("ddybService")
@Transactional
public class DDYBServiceImpl extends WxCommonServiceImpl implements DDYBService {
	private static final Logger LOG = LoggerFactory
			.getLogger(DDYBServiceImpl.class);

	@Value("#{config['ddyb.welcome.title']}")
	public void setWelcomeTitle(String welcomeTitle) {
		this.welcomeTitle = welcomeTitle;
	}

	@Value("#{config['ddyb.welcome.content']}")
	public void setWelcomeContent(String welcomeContent) {
		this.welcomeContent = welcomeContent;
	}

	// lqm todo ,为什么属性读不进来
	// @Value(value = "wxb031b2ffa1d3900a")
	// @Value(value = "${wx.appId}")
	@Value("#{config['ddyb.wx.appId']}")
	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Value("#{config['ddyb.wx.appSecretId']}")
	public void setAppSecretId(String appSecretId) {
		this.appSecretId = appSecretId;
	}

	/**
	 * 扫描带参数二维码事件
	 */
	public WxResponse scanPro(Element eventElement) {
		LOG.info("begin scanPro");
		WxResponse wxResponse = new WxResponse();

		String fromUserName = eventElement.element("FromUserName").getText();
		String toUserName = eventElement.element("ToUserName").getText();
		String eventKey = eventElement.element("EventKey").getText();

		LOG.info("**********,eventElement={}", eventElement.toString());
		// 注册新的用户
		eventKey = eventKey.replace("qrscene_", "");

		User user = this.userMapper.findUserByOpenId(fromUserName,
				User.Role.DOCTOR.getId());

		if (user == null) {
			user = newUser(this.getAccessToken(), fromUserName, eventKey,
					User.Role.DOCTOR.getId());
		}

		wxResponse.setMsg(generateWelcomeMsg(fromUserName, toUserName));

		return wxResponse;
	}

	public String generateWelcomeMsg(String fromUserName, String toUserName) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<xml>");
		buffer.append("<ToUserName><![CDATA[" + fromUserName
				+ "]]></ToUserName>");
		buffer.append("<FromUserName><![CDATA[" + toUserName
				+ "]]></FromUserName>");
		buffer.append("<CreateTime>"
				+ String.valueOf(System.currentTimeMillis()) + "</CreateTime>");
		buffer.append("<MsgType><![CDATA[news]]></MsgType>");
		buffer.append("<ArticleCount>1</ArticleCount>");
		buffer.append("<Articles>");
		buffer.append("<item>");
		buffer.append("<Title>" + this.welcomeTitle + "</Title>");
		buffer.append("<Description><![CDATA[" + this.welcomeContent
				+ "]]></Description>");
		buffer.append("<PicUrl><![CDATA["
				+ "https://mmbiz.qlogo.cn/mmbiz/n5mfDXWXlehImFI0LjIGmqaJhZNk8P0mGNKEsFjR1H1DSfc7Kr8rxjBs3Jqq2BV6BCaRicJ4eann4AjRBTd65pw/0?wx_fmt=jpeg"
				+ "]]></PicUrl>");
		// buffer.append("<Url><![CDATA["
		// +
		// "http://www.yushansoft.com/dingdong/v1/index.html?appId=wxb031b2ffa1d3900a"
		// + "]]></Url>");
		buffer.append("</item>");
		buffer.append("</Articles>");
		buffer.append("</xml>");

		LOG.info(buffer.toString());

		return buffer.toString();
	}

	@Override
	public User getUser(String openId) {
		User user = null;

		try {
			user = this.userMapper.findUserByOpenId(openId,
					User.Role.DOCTOR.getId());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		return user;
	}

	@Override
	public WxResponse textPro(Element eventElement) {
		// TODO Auto-generated method stub
		return null;
	}

}
