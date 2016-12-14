package com.dingdong.sys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.util.HttpUtils;
import com.dingdong.register.exception.DoctorErrorCode;
import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.service.DoctorFanService;
import com.dingdong.sys.exception.WxErrorCode;
import com.dingdong.sys.mapper.TransferMapper;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.Transfer;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.DDMZService;
import com.dingdong.sys.service.TransferService;
import com.dingdong.sys.vo.response.PayTokenResponse;
import com.dingdong.sys.vo.response.WxResponse;

@Service("ddmzService")
@Transactional
public class DDMZServiceImpl extends WxCommonServiceImpl implements DDMZService {
	private static final Logger LOG = LoggerFactory
			.getLogger(DDMZServiceImpl.class);

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DoctorMapper doctorMapper;

	@Autowired
	private DoctorFanService doctorFanService;

	@Autowired
	private TransferService transferService;

	@Autowired
	private TransferMapper transferMapper;
	@Value("#{config['score.newUserScore']}")
	private int newUserScore;

	// private static final int MAX_LIMIT_SCENEID = 100000;
	// 以内属于医生编码
	private static final int MAX_LIMIT_SCENEID_DOCTOR = 50000;

	protected String mch_id = "1300465101"; // 微信支付分配的商户号
	protected String paySerectId = "liaoqingmiao19780yushansoft90233";

	@Value("#{config['versionPath']}")
	protected String versionPath;// 带版本号的根目录

	/**
     * 
     */

	/**
	 * 扫描带参数二维码事件
	 */

	public WxResponse scanPro(Element eventElement) {
		LOG.info("begin scanPro");
		WxResponse wxResponse = new WxResponse();

		String fromUserName = eventElement.element("FromUserName").getText();
		String toUserName = eventElement.element("ToUserName").getText();
		String eventKey = eventElement.element("EventKey").getText();
		LOG.info("**********,eventElement={}", eventElement.getText());
		LOG.info("**********,fromUserName={},toUserName={},eventKey={}",
				fromUserName, toUserName, eventKey);
		// 注册新的用户
		eventKey = eventKey.replace("qrscene_", "");

		User user = this.userMapper.findUserByOpenId(fromUserName,
				User.Role.PATIENT.getId());
		boolean isNewUser = false;
		if (user == null) {
			user = newUser(this.getAccessToken(), fromUserName, eventKey,
					User.Role.PATIENT.getId());
			isNewUser = true;
		}

		// 参数值
		if (StringUtils.isNotBlank(eventKey) && StringUtils.isNumeric(eventKey)) {
			long lEventKey = Long.valueOf(eventKey);
			// 属于扫描医生的二维码
			if (lEventKey <= MAX_LIMIT_SCENEID_DOCTOR) {
				return this.doctorQrPro(fromUserName, toUserName, user,
						lEventKey, isNewUser);
			}
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
				+ "https://mmbiz.qlogo.cn/mmbiz/d7iahysxQQ9nVr3UbRjudk47KXD88ZasICwPib11qWXHTLlnQib2gXOVVw2Yr09EVX6iajv216Kgx116NrmS6Ydvug/0?wx_fmt=jpeg"
				+ "]]></PicUrl>");
		buffer.append("<Url><![CDATA["
				+ "http://www.yushansoft.com/dingdong/v1/index.html?appId=wxb031b2ffa1d3900a"
				+ "]]></Url>");
		buffer.append("</item>");
		buffer.append("</Articles>");
		buffer.append("</xml>");

		LOG.info(buffer.toString());

		return buffer.toString();
	}

	// 扫描医生二维码的处理
	private WxResponse doctorQrPro(String fromUserName, String toUserName,
			User user, long doctorId, boolean isNewUser) {
		WxResponse response = new WxResponse();
		// String content = null;
		Doctor doctor = this.doctorMapper.findById(doctorId);
		if (doctor == null) {
			response.setErrorCode(DoctorErrorCode.DOCTOR_ID_NOT_EXIST);
			return response;
		}

		// 自动添加对医生的关注
		this.doctorFanService.addDoctorFan(user.getId(), doctorId);
		// 吸引新用户为医生积分
		if (doctor.getUserId() > 0)
			this.transferService.executeTransferScore(doctor.getUserId(),
					this.newUserScore, Transfer.Reason.YSXFJF.getId());

		// String homeUrl = versionPath + "/ddpatient/index.html#/tab/doctors/"
		// + doctor.getId() + "/doctorInforShow";
		// LOG.info(" THE HOMEURL IS {}", homeUrl);
		// content = "你好，我是" + doctor.getName() + "大夫 \n。欢迎访问我的<a href=\""
		// + homeUrl + "\">叮咚小站</a>！我会在站中发布我的最新资讯和行程，方便大家及时预约就诊。";

		// response.setMsg(this.generateTextMsg(fromUserName, toUserName,
		// content));

		response.setMsg(this.generateDoctorWelcomeMsg(fromUserName, toUserName,
				doctor));

		return response;
	}

	public String generateDoctorWelcomeMsg(String fromUserName,
			String toUserName, Doctor doctor) {
		StringBuffer buffer = new StringBuffer();

		String homeUrl = versionPath + "/ddpatient/index.html#/doctors/"
				+ doctor.getId() + "/doctorInforShow";

		buffer.append("<xml>");
		buffer.append("<ToUserName><![CDATA[" + fromUserName
				+ "]]></ToUserName>");
		buffer.append("<FromUserName><![CDATA[" + toUserName
				+ "]]></FromUserName>");
		buffer.append("<CreateTime>"
				+ String.valueOf(System.currentTimeMillis()) + "</CreateTime>");
		buffer.append("<MsgType><![CDATA[news]]></MsgType>");
		buffer.append("<ArticleCount>2</ArticleCount>");
		buffer.append("<Articles>");
		buffer.append("<item>");
		buffer.append("<Title>" + this.welcomeTitle + "</Title>");
		buffer.append("<Description><![CDATA[" + this.welcomeTitle
				+ "]]></Description>");
		buffer.append("<PicUrl><![CDATA["
				+ "https://mmbiz.qlogo.cn/mmbiz/d7iahysxQQ9nVr3UbRjudk47KXD88ZasICwPib11qWXHTLlnQib2gXOVVw2Yr09EVX6iajv216Kgx116NrmS6Ydvug/0?wx_fmt=jpeg"
				+ "]]></PicUrl>");
		// buffer.append("<Url><![CDATA["
		// +
		// "http://www.yushansoft.com/dingdong/v1/index.html?appId=wxb031b2ffa1d3900a"
		// + "]]></Url>");
		buffer.append("</item>");
		buffer.append("<item>");
		buffer.append("<Title>" + doctor.getName() + "大夫欢迎你！" + "</Title>");
		buffer.append("<Description><![CDATA[" + "你好，我是" + doctor.getName()
				+ "大夫 \n。欢迎访问我的叮咚小站！我会在站中发布我的最新资讯和行程，方便大家及时预约就诊。"
				+ "]]></Description>");
		buffer.append("<PicUrl><![CDATA[" + doctor.getHeadImgUrl()
				+ "]]></PicUrl>");
		buffer.append("<Url><![CDATA[" + homeUrl + "]]></Url>");
		buffer.append("</item>");

		buffer.append("</Articles>");
		buffer.append("</xml>");

		LOG.info(buffer.toString());

		return buffer.toString();
	}

	@Override
	public PayTokenResponse createPayToken(long userId, float totalFee,
			String remoteIp) {
		PayTokenResponse payTokenResponse = new PayTokenResponse();

		WxpayParam wxpayParam = new WxpayParam();

		User user = this.userMapper.findById(userId);
		if (user == null || StringUtils.isBlank(user.getOpenId())) {
			payTokenResponse.setErrorCode(WxErrorCode.OPEN_ID_NOT_EXIST);
			return payTokenResponse;
		}

		wxpayParam.setOpenId(user.getOpenId());

		// 客户端IP
		LOG.info(" ther remote ip is " + remoteIp);

		String notify_url = "http://www.yushansoft.com/dingdong/ddmz/payPro";

		wxpayParam.setNotify_url(notify_url);

		// 创建充值订单
		Transfer transfer = new Transfer();
		transfer.setUserId(user.getId());
		transfer.setUserName(user.getName());
		transfer.setAmount(totalFee);

		this.transferMapper.addTransfer(transfer);

		// 微信采用分来表示
		long total_fee = (long) (totalFee * 100);//
		wxpayParam.setTotal_fee(total_fee);

		wxpayParam.setOut_trade_no("DDPAY" + (new Date().getTime()) + "-"
				+ String.valueOf(transfer.getId()));

		// out_trade_no, notify_url

		wxpayParam.setSpbill_create_ip(remoteIp);
		String prePayId = getPrePayId(wxpayParam);
		if (StringUtils.isBlank(prePayId)) {
			payTokenResponse.setErrorCode(WxErrorCode.EMPTY_PREPAY_ID);
			return payTokenResponse;
		}

		payTokenResponse = createPayToken(prePayId);
		return payTokenResponse;

	}

	private PayTokenResponse createPayToken(String prepay_id) {

		PayTokenResponse payTokenResponse = new PayTokenResponse();

		try {
			Map<String, String> params = new HashMap<String, String>();
			String timeStamp = String.valueOf((new Date()).getTime());// ：" 1395712654",//时间戳，自1970年以来的秒数
			String nonce_str = UUID.randomUUID().toString().replace("-", "")
					.substring(0, 30);// 随机串
			String pack = "prepay_id=" + prepay_id;

			LOG.info(" pay sign");
			params.put("appId", appId);
			params.put("timeStamp", timeStamp);
			params.put("nonceStr", nonce_str);
			params.put("package", pack);
			params.put("signType", "MD5");

			String paySign = Md5Sign.CreateMd5Sign(params, "key", paySerectId);

			params.put("paySign", paySign);

			payTokenResponse.setTokenConfig(params);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("createPayToken error " + e);

		}

		return payTokenResponse;

	}

	@Override
	public User getUser(String openId) {
		User user = null;

		try {
			user = this.userMapper.findUserByOpenId(openId,
					User.Role.PATIENT.getId());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		return user;
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

	// 获取微信预支付号
	private String getPrePayId(WxpayParam wxpayParam) {
		LOG.info("begin to geprepay******************************************************************");

		String nonce_str = UUID.randomUUID().toString().replace("-", "")
				.substring(0, 30);

		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", this.appId);
		params.put("body", wxpayParam.getBody());
		params.put("device_info", wxpayParam.getDevice_info());
		params.put("mch_id", this.mch_id);
		params.put("notify_url", wxpayParam.getNotify_url());
		params.put("nonce_str", nonce_str);
		params.put("openid", wxpayParam.getOpenId());
		params.put("out_trade_no", wxpayParam.getOut_trade_no());
		params.put("spbill_create_ip", wxpayParam.getSpbill_create_ip());
		params.put("total_fee", String.valueOf(wxpayParam.getTotal_fee()));
		params.put("trade_type", wxpayParam.getTrade_type());
		String sign = Md5Sign.CreateMd5Sign(params, "key", paySerectId);

		String content = "<xml>" + "<appid>" + this.appId + "</appid>"
				+ "<body>" + wxpayParam.getBody() + "</body>" + "<device_info>"
				+ wxpayParam.getDevice_info() + "</device_info>" + "<mch_id>"
				+ mch_id + "</mch_id>" + " <nonce_str>" + nonce_str
				+ "</nonce_str>" + "<notify_url>" + wxpayParam.getNotify_url()
				+ "</notify_url>" + "<openid>" + wxpayParam.getOpenId()
				+ "</openid>" + "<out_trade_no>" + wxpayParam.getOut_trade_no()
				+ "</out_trade_no>" + "<spbill_create_ip>"
				+ wxpayParam.getSpbill_create_ip() + "</spbill_create_ip>"
				+ "<total_fee>" + wxpayParam.getTotal_fee() + "</total_fee>"
				+ "<trade_type>" + wxpayParam.getTrade_type() + "</trade_type>"
				+ "<sign>" + sign + "</sign></xml>";

		try {
			String strContent = HttpUtils.sendPost(ORDER_URL, content);
			LOG.info(" the content is " + strContent);
			Document document = DocumentHelper.parseText(strContent);
			Element root = document.getRootElement();

			String prepay_id = root.element("prepay_id").getText();
			if (StringUtils.isBlank(prepay_id)) {
				LOG.error("getPrePayId null error" + root.asXML());
			}

			LOG.info("the prepay_id  are " + prepay_id);

			return prepay_id;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("getPrepayid error" + e);
		}

		return null;
	}

	@Override
	public String payPro(String eventXml) {
		LOG.info("begin payPro the event xml is \n  " + eventXml);
		try {
			Document document = DocumentHelper.parseText(eventXml.trim());
			Element root = document.getRootElement();

			// if (StringUtils.isBlank(openId))
			// throw new ServletException("无法取到微信OpenId !");

			//
			// 推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
			// 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。

			String return_code = root.element("return_code").getText();
			LOG.info(" the wechat return_code is " + return_code);
			if ("SUCCESS".equals(return_code)) {
				String result_code = root.element("result_code").getText();
				LOG.info(" the wechat result_code is" + result_code);
				if ("SUCCESS".equals(result_code)) {
					String out_trade_no = root.element("out_trade_no")
							.getText();
					String transaction_id = root.element("transaction_id")
							.getText();

					out_trade_no = out_trade_no.replaceFirst("DDPAY\\d*-", "");
					LOG.info(" the wechat out_trade_no is" + out_trade_no);
					this.transferService.updateTransferSuccess(
							Long.valueOf(out_trade_no), transaction_id);

				}

			}

			String content = "<xml>  <return_code><![CDATA[SUCCESS]]></return_code>  <return_msg><![CDATA[OK]]></return_msg></xml>";

			return content;

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return "";
	}

	@Value("#{config['welcome.title']}")
	public void setWelcomeTitle(String welcomeTitle) {
		this.welcomeTitle = welcomeTitle;
	}

	@Value("#{config['welcome.content']}")
	public void setWelcomeContent(String welcomeContent) {
		this.welcomeContent = welcomeContent;
	}

	// lqm todo ,为什么属性读不进来
	// @Value(value = "wxb031b2ffa1d3900a")
	// @Value(value = "${wx.appId}")
	@Value("#{config['wx.appId']}")
	public void setAppId(String appId) {
		this.appId = appId;
	}

	// @Value(value = "de8864ef9d64975c93c520e0c73053c1")
	// @Value(value = "${wx.appSecretId}")
	@Value("#{config['wx.appSecretId']}")
	public void setAppSecretId(String appSecretId) {
		this.appSecretId = appSecretId;
	}

	@Override
	public WxResponse textPro(Element eventElement) {

		LOG.info("begin textPro");
		WxResponse wxResponse = new WxResponse();

		String fromUserName = eventElement.element("FromUserName").getText();
		String toUserName = eventElement.element("ToUserName").getText();
		String content = eventElement.element("Content").getText();
		LOG.info("**********,eventElement={}", eventElement.getText());
		LOG.info("**********,fromUserName={},toUserName={},content={}",
				fromUserName, toUserName, content);
		if (StringUtils.isBlank(content))
			return null;
		if (content.startsWith("@"))// 如果用@开头
		{

			List<Doctor> doctors = this.doctorMapper.findByName(content
					.substring(1).trim());
			if (CollectionUtils.isNotEmpty(doctors)) {
				wxResponse.setMsg(this.generateDoctorWelcomeMsg(fromUserName,
						toUserName, doctors.get(0)));
			} else {
				content = ("该医生暂时还未入驻，我们会尽快与该医生联系。");

				wxResponse.setMsg(this.generateTextMsg(fromUserName,
						toUserName, content));
			}
		} else {
			wxResponse.setMsg(this.transferToCS(fromUserName, toUserName));
		}

		return wxResponse;
	}
}
