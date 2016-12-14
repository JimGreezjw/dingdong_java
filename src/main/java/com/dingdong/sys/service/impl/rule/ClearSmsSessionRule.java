package com.dingdong.sys.service.impl.rule;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.dingdong.sys.model.SmsValidateMessageVO;
import com.dingdong.sys.rule.IAfterRule;
import com.dingdong.sys.service.SmsValidateSessionSaveMap;

/**
 * 清除叮咚医生缓存中的信息
 * 
 * @author niukai
 * 
 */
public class ClearSmsSessionRule implements IAfterRule {

	private static SmsValidateSessionSaveMap smsValidateSessionSaveMap;

	@Override
	public void process(Object obj) {

	}

	@Override
	public void afterProcess(Object obj) {
		if (obj instanceof SmsValidateMessageVO) {
			getSmsSessionSaveMap().removeMessage((SmsValidateMessageVO) obj);
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
