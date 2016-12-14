package com.dingdong.sys.service.impl.rule;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.dingdong.sys.model.SmsValidateMessageVO;
import com.dingdong.sys.rule.IAfterRule;
import com.dingdong.sys.service.SmsValidateSessionSaveMap;

/**
 * 发送验证码后，服务器内部的存储
 * 
 * @author niukai
 * 
 */
public class SmsSessionSaveRule implements IAfterRule {

	private static SmsValidateSessionSaveMap smsValidateSessionSaveMap;

	@Override
	public void process(Object obj) {

	}

	@Override
	public void afterProcess(Object obj) {

		if (obj instanceof SmsValidateMessageVO) {
			getSmsSessionSaveMap().addMessage((SmsValidateMessageVO) obj);
		}
	}

	private static SmsValidateSessionSaveMap getSmsSessionSaveMap() {

		if (smsValidateSessionSaveMap == null) {
			WebApplicationContext wac = ContextLoader
					.getCurrentWebApplicationContext();
			smsValidateSessionSaveMap = (SmsValidateSessionSaveMap) wac
					.getBean("smsValidateSessionSaveMap");
		}

		return smsValidateSessionSaveMap;
	}
}
