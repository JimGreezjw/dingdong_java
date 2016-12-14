package com.dingdong.sys.service;

import java.util.Map;

/**
 * 用户认证
 * 
 * @author niukai
 * 
 */
public interface UserValidateService extends IPubMessageValidateService {

	/**
	 * 用户进行验证
	 * 
	 * @param userId
	 *            用户号
	 * @param mobileNo
	 *            手机号码
	 * @param msgCode
	 *            短信验证码
	 * @param imgCode
	 *            图片验证码
	 * @param mode
	 *            模式
	 * @return
	 */
	public Map<String, Object> doValidate(long userUid, String mobileNo,
			String msgCode, String imgCode, int mode);
}
