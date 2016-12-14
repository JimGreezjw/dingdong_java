package com.dingdong.sys.vo.request;

import java.util.Map;

import com.dingdong.common.vo.ResponseBody;

/**
 * 信息返回Body
 * 
 * @author niukai
 * @created on December 13rd, 2015
 * 
 */
public class ValidateMsgResponse extends ResponseBody {
	public Map<String, Object> retMap;

	public Map<String, Object> getRetMap() {
		return retMap;
	}

	public ValidateMsgResponse setRetMap(Map<String, Object> retMap) {
		this.retMap = retMap;
		return this;
	}
}
