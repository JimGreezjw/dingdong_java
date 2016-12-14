package com.dingdong.sys.service.impl;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.util.HttpUtils;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.WxCommonService;
import com.dingdong.sys.service.WxJobService;

@Service("wxJobService")
@Transactional
public class WxJobServiceImpl implements WxJobService {
	protected static final Logger LOG = LoggerFactory
			.getLogger(WxJobServiceImpl.class);
	protected static final String USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=";
	@Autowired
	protected UserMapper userMapper;

	@Async
	@Override
	public void getWxUserInfo(User user, String accessToken, String openId,
			WxCommonService wxCommonService) {

		LOG.info("getWxUserInfo,the openid={},accesstoken={}", openId,
				accessToken);

		try {
			String url = USERINFO_URL + accessToken + "&openid=" + openId
					+ "&lang=zh_CN";
			String result = HttpUtils.sendGet(url);
			LOG.info("the result id is" + result);
			JSONObject userInfo = JSONObject.fromObject(result);

			if (userInfo.containsKey("errcode")
					&& "40001".equals(userInfo.getString("errcode"))) {// 如果属于验证码错误
				wxCommonService.autoFetchAccessToken();
				accessToken = wxCommonService.getAccessToken();
				url = USERINFO_URL + accessToken + "&openid=" + openId
						+ "&lang=zh_CN";
				result = HttpUtils.sendGet(url);
				LOG.info("the refetch result id is" + result);
				userInfo = JSONObject.fromObject(result);
			}
			if (userInfo != null && !userInfo.isEmpty()
					&& userInfo.containsKey("nickname")) {
				user.setNickName(userInfo.getString("nickname"));
				user.setCountry(userInfo.getString("country"));
				user.setProvince(userInfo.getString("province"));
				user.setCity(userInfo.getString("city"));
				user.setHeadImgUrl(userInfo.getString("headimgurl"));
				user.setGender(Integer.valueOf(userInfo.getString("sex")));
				if (StringUtils.isBlank(user.getName()))
					user.setName(user.getNickName());

				this.userMapper.updateUser(user);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("获取微信用户信息时发生错误", e);
		}
		return;

	}

}
