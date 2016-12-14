package com.dingdong.sys.service;

import com.dingdong.sys.model.SmsValidateMessageVO;

/**
 * 短信验证公共接口
 * 
 * @author niukai
 * 
 */
public interface IPubMessageValidateService {

	/**
	 * 添加执行前规则
	 */
	public void addBeforeRuleInner();

	/**
	 * 执行后规则
	 */
	public void addAfterRuleInner();

	/**
	 * 执行前规则
	 */
	public void execBeforeRule(SmsValidateMessageVO messageVO);

	/**
	 * 执行后规则
	 */
	public void execAfterRules(SmsValidateMessageVO messageVO);
}
