package com.dingdong.sys.service;

import com.dingdong.sys.model.User;
import com.dingdong.sys.vo.response.PayTokenResponse;

public interface DDMZService extends WxCommonService {

	public String payPro(String eventXml);

	// 取得带参数二微码ticket
	String getInviteQrUrl(String employeeUid, String qrSceneId);

	/*
	 * 取得微信的前置url
	 */
	String getMediaPreUrl();

	public User getUser(String openId);

	/**
	 * 获取微信支付预订单号
	 * 
	 * @param wxpayParam
	 * @return
	 */
	// public String getPrePayId(WxpayParam wxpayParam);

	/**
	 * 在获取预支付编号后创建支付口令
	 * 
	 * @return
	 */
	// public PayTokenResponse createPayToken(String prePayId);

	/**
	 * 取得为用户充值的支付口令
	 * 
	 * @param userId
	 * @param totalFee
	 * @param remoteIp
	 * @return
	 */
	public PayTokenResponse createPayToken(long userId, float totalFee,
			String remoteIp);

}
