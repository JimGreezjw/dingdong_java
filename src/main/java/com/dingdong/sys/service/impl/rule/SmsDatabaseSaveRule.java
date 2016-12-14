package com.dingdong.sys.service.impl.rule;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.dingdong.sys.mapper.SmsValidateMessageMapper;
import com.dingdong.sys.model.SmsValidateMessageVO;
import com.dingdong.sys.rule.IAfterRule;

/**
 * 向数据库中写入的验证码的规则
 * 
 * @author niukai
 * 
 */
public class SmsDatabaseSaveRule implements IAfterRule {

	private static SmsValidateMessageMapper smsValidateMessageMapper;

	@Override
	public void process(Object obj) {

	}

	@Override
	public void afterProcess(Object obj) {

		if (obj instanceof SmsValidateMessageVO) {
			SmsValidateMessageVO messageVO = (SmsValidateMessageVO) obj;
			getMessageMapper().insertMessageVO(messageVO);
		}
	}

	private static SmsValidateMessageMapper getMessageMapper() {
		if (smsValidateMessageMapper == null) {
			WebApplicationContext wac = ContextLoader
					.getCurrentWebApplicationContext();
			smsValidateMessageMapper = (SmsValidateMessageMapper) wac
					.getBean("smsValidateMessageMapper");
		}

		return smsValidateMessageMapper;
	}
}
