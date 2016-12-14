package com.dingdong.sys.vo.response;

import com.dingdong.common.vo.ResponseBody;

/**
 * 医生信息响应对象
 * 
 * @author chenliang
 * 
 */
public class WxResponse extends ResponseBody {
	/**
	 * 返回的消息
	 */
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
