package com.dingdong.sys.service;

import org.dom4j.Element;

import com.dingdong.sys.model.User;
import com.dingdong.sys.service.impl.JsSDKConfig;
import com.dingdong.sys.vo.response.WxResponse;

public interface WxCommonService {

	public abstract User newUser(String accessToken, String openId,
			String qrsceneId, Integer role);

	public void autoFetchAccessToken();

	public void autoFetchJsTicket();

	public WxResponse sendCustomTextMsg(String openId, String content);

	/**
	 * 发送各类客户服务消息
	 * 
	 * @param content
	 * @return
	 */
	public WxResponse sendCustomMsg(String content);

	String getInviteQrUrl(String employeeUid, String qrSceneId);

	String getMediaPreUrl();

	public abstract WxResponse scanPro(Element eventElement);

	public String getOpenId(String codeId);

	// 返送要求codeId的请求
	public void sendRequestWxCodeId(String userUid, String hostInfo);

	public WxResponse eventPro(String eventXml);

	/**
	 * 获取永久二维码地址，用id作为参数
	 * 
	 * @param id
	 * @return
	 */
	String getPersistQrUrl(long id);

	/**
	 * 获取临时二维码的信息
	 * 
	 * @param id
	 * @return
	 */
	String getTempQrUrl(long id);

	/**
	 * 产生js签名
	 * 
	 * @param url
	 * @return
	 */
	JsSDKConfig createJsSign(String url);

	public abstract String getAccessToken();

}
