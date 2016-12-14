package com.dingdong.sys.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.sys.model.Msg;

/**
 * 信息响应对象
 * 
 * @author chenliang
 * 
 */
public class MsgResponse extends ResponseBody {
	/**
	 * 返回的消息
	 */
	private List<Msg> msgs;

	public List<Msg> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<Msg> msgs) {
		this.msgs = msgs;
	}

}
