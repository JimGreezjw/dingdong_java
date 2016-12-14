package com.dingdong.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.dingdong.sys.mapper.SmsValidateMessageMapper;
import com.dingdong.sys.model.SmsValidateMessageVO;
import com.dingdong.sys.rule.IAfterRule;
import com.dingdong.sys.rule.IBeforeRule;
import com.dingdong.sys.service.SmsValidateMessageService;
import com.dingdong.sys.service.impl.rule.SmsDatabaseSaveRule;
import com.dingdong.sys.service.impl.rule.SmsSessionSaveRule;
import com.dingdong.sys.vo.util.DDDate;

/**
 * 默认的消息服务中心
 * 
 * @author niukai
 * @created on November 28th, 2015
 * 
 */
@Service("smsValidateMessageService")
public class DefaultMessageServiceImpl implements SmsValidateMessageService {

	private final String formalIP = "app.cloopen.com";
	private final String testIP = "sandboxapp.cloopen.com";
	private final String serverPort = "8883";
	private final String accountSID = "aaf98f89512446e201512d7922fd1a29";
	private final String authToken = "7df11d73e3c04e7a81aa13ed78838a2e";
	/**
	 * 测试appid
	 */
	private final String TEST_APP_ID = "aaf98f89512446e201512d795b821a2c";
	/**
	 * 正式appid
	 */
	private final String FORMAL_APP_ID = "8a48b55151d688bc0151d744a61f0267";

	/**
	 * 是否是正式环境
	 */
	private boolean isFormal = true;

	/**
	 * 是否在内存中保留消息信息
	 */
	public static boolean USE_MEMORY_RECORD_MESSAGE = false;

	private CCPRestSmsSDK restAPI;

	// 消息的前后规则
	List<IBeforeRule> beforeRuleList = new ArrayList<IBeforeRule>();
	List<IAfterRule> afterRuleList = new ArrayList<IAfterRule>();

	@Autowired
	private SmsValidateMessageMapper smsValidateMessageMapper;

	public DefaultMessageServiceImpl() {
		// FIXME 添加前后规则
		this.addBeforeRuleInner();
		this.addAfterRuleInner();
		// end

		restAPI = new CCPRestSmsSDK();

		// 是否正式环境
		if (isFormal) {
			restAPI.init(formalIP, serverPort);
		} else {
			restAPI.init(testIP, serverPort);
		}

		restAPI.setAccount(accountSID, authToken);
		if (isFormal) {
			restAPI.setAppId(FORMAL_APP_ID);
		} else {
			restAPI.setAppId(TEST_APP_ID);
		}
	}

	@Override
	public Map<String, Object> sendSmsMessage(String mobileNo,
			String templateID, int minutes) {
		HashMap<String, Object> result = null;
		HashMap<String, Object> retMap = new HashMap<String, Object>();

		// 首先检测这个手机号码是否已经在用户列表和医生列表中了
		// List<User> userList = userMapper.findByMobileNo(mobileNo);
		// if (userList != null && userList.size() > 0) {
		// retMap.put("ok", 0);
		// retMap.put("msg", "该手机号码已被用户注册");
		//
		// return retMap;
		// }

		// Doctor doctor = doctorMapper.findByMobileNo(mobileNo);
		// if (doctor == null) {
		// retMap.put("ok", 0);
		// retMap.put("msg", "该手机号码已被医生注册");
		// return retMap;
		// }
		// end

		String useMinute = minutes + "";
		String validateCode = CodeGenerator.genCode();

		// 是否要发送短信
		boolean isSendCenter = true;
		if (isSendCenter) {
			result = restAPI.sendTemplateSMS(mobileNo, templateID,
					new String[] { validateCode, useMinute });
		} else {
			result = new HashMap<String, Object>();
			result.put("statusCode", this.getSuccessCode());
		}
		// end

		String statusCode = (String) result.get("statusCode");
		if (this.getSuccessCode().equals(statusCode)) {
			retMap.put("ok", 1);
			retMap.put("msg", "验证码成功发送");

			// 构建MessageVO对象
			SmsValidateMessageVO messageVO = new SmsValidateMessageVO();
			messageVO.setMobileNo(mobileNo);
			messageVO.setValidateCode(validateCode);

			Date nows = new Date();
			messageVO.setSendTime(nows);
			messageVO.setExpireTime(new Date(nows.getTime() + minutes * 60
					* 1000));

			this.execAfterRules(messageVO);
		} else {
			String errMsg = (String) result.get("statusMsg");

			retMap.put("ok", 0);
			retMap.put("msg", errMsg);
		}

		return retMap;
	}

	@Override
	public String getSuccessCode() {
		return "000000";
	}

	@Override
	public boolean saveRecord(String mobileNo, String validateCode,
			int enableMinutes) {
		// 用于本地的持久化
		return true;
	}

	@Override
	@Scheduled(cron = "0 0 20 * * ?", zone = "GMT+08:00")
	public void removeExpireMessage() {
		DDDate now = new DDDate();

		// 保留一天的数据
		smsValidateMessageMapper.removeExpireMessage(now.before(1).getDate());
	}

	@Override
	public void addBeforeRuleInner() {

	}

	@Override
	public void addAfterRuleInner() {
		if (USE_MEMORY_RECORD_MESSAGE) {
			afterRuleList.add(new SmsSessionSaveRule());
		}

		afterRuleList.add(new SmsDatabaseSaveRule());
	}

	@Override
	public void execBeforeRule(SmsValidateMessageVO messageVO) {

	}

	@Override
	public void execAfterRules(SmsValidateMessageVO messageVO) {
		// 执行规则
		for (IAfterRule rule : afterRuleList) {
			rule.afterProcess(messageVO);
		}
	}
}
