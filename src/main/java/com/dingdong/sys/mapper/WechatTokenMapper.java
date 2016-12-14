package com.dingdong.sys.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.sys.model.WechatToken;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface WechatTokenMapper {

	public List<WechatToken> findAllWechatTokens();

	public WechatToken findById(@Param(value = "appId") String appId);

	// 更新口令
	public void addWechatToken(@Param(value = "appId") String appId,
			@Param(value = "accessToken") String accessToken,
			@Param(value = "accessTokenLastUpdate") Date accessTokenLastUpdate);

	// 更新口令
	public void updateAccessToken(@Param(value = "appId") String appId,
			@Param(value = "accessToken") String accessToken,
			@Param(value = "accessTokenLastUpdate") Date accessTokenLastUpdate);

	// 更新jsApiTicket
	public void updateJsapiTicket(@Param(value = "appId") String appId,
			@Param(value = "jsapiTicket") String jsapiTicket,
			@Param(value = "jsapiTicketLastUpdate") Date jsapiTicketLastUpdate);

}
