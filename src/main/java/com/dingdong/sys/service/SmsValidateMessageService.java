package com.dingdong.sys.service;

import java.util.Map;

/**
 * 短信接口
 * 
 * @author niukai
 * @created on November 28th, 2015
 * 
 */
public interface SmsValidateMessageService extends IPubMessageValidateService {

	/**
	 * 发送短信
	 * 
	 * @param mobileNo
	 *            手机号码，以逗号隔开
	 * @param templateID
	 *            短信模板编号
	 * @param minutes
	 *            有效时间
	 * @return 短信发送情况
	 */
	public Map<String, Object> sendSmsMessage(String mobileNo,
			String templateID, int minutes);

	/**
	 * 信息成功的提示信息
	 * 
	 * @return
	 */
	public String getSuccessCode();

	/**
	 * 在服务器端保存验证码
	 * 
	 * @param mobileNo
	 *            电话号码
	 * @param validateCode
	 *            验证码
	 * @param enableMinutes
	 *            有效时间
	 * @return
	 */
	public boolean saveRecord(String mobileNo, String validateCode,
			int enableMinutes);

	/**
	 * 在数据库中清除已经失效的信息记录，默认保存一天的信息
	 */
	public void removeExpireMessage();
}
