package com.dingdong.sys.service;

import com.dingdong.sys.model.User;

public interface WxJobService {

	void getWxUserInfo(User user, String accessToken, String openId,
			WxCommonService wxCommonService);

}
