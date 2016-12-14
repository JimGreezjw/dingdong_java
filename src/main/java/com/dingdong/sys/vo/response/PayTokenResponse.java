package com.dingdong.sys.vo.response;

import java.util.Map;

import com.dingdong.common.vo.ResponseBody;

/**
 * 医生信息响应对象
 * 
 * @author lqm
 * 
 */
public class PayTokenResponse extends ResponseBody {

	private Map<String, String> tokenConfig;

	public Map<String, String> getTokenConfig() {
		return tokenConfig;
	}

	public void setTokenConfig(Map<String, String> tokenConfig) {
		this.tokenConfig = tokenConfig;
	}

}
