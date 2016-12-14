package com.dingdong.sys.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("wechatToken")
public class WechatToken {

	private String appId;
	private String accessToken;
	private String jsapiTicket;
	private Date accessTokenLastUpdate;
	private Date jsapiTicketLastUpdate;

	public String getAppId() {
		return appId;
	}

	public Date getAccessTokenLastUpdate() {
		return accessTokenLastUpdate;
	}

	public void setAccessTokenLastUpdate(Date accessTokenLastUpdate) {
		this.accessTokenLastUpdate = accessTokenLastUpdate;
	}

	public Date getJsapiTicketLastUpdate() {
		return jsapiTicketLastUpdate;
	}

	public void setJsapiTicketLastUpdate(Date jsapiTicketLastUpdate) {
		this.jsapiTicketLastUpdate = jsapiTicketLastUpdate;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getJsapiTicket() {
		return jsapiTicket;
	}

	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}

}