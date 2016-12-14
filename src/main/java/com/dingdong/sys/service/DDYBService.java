package com.dingdong.sys.service;

import com.dingdong.sys.model.User;
import com.dingdong.sys.service.impl.JsSDKConfig;
import com.dingdong.sys.vo.response.WxResponse;

public interface DDYBService extends WxCommonService {

	public String getOpenId(String codeId);

	// 返送要求codeId的请求
	public void sendRequestWxCodeId(String userUid, String hostInfo);

	public WxResponse eventPro(String eventXml);

	/**
	 * 产生js签名
	 * 
	 * @param url
	 * @return
	 */
	JsSDKConfig createJsSign(String url);

	/*
	 * 取得微信的前置url
	 */
	String getMediaPreUrl();

	public User getUser(String openId);

}
