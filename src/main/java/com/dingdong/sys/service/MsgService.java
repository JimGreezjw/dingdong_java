package com.dingdong.sys.service;

import org.apache.ibatis.annotations.Param;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.PageInfo;
import com.dingdong.sys.model.Msg;
import com.dingdong.sys.vo.response.MsgResponse;

public interface MsgService {

	public MsgResponse addMsg(@Param(value = "msg") Msg msg);

	/**
	 * 设置消息已读
	 * 
	 * @param id
	 */
	public ResponseBody updateMsgRead(long id);

	/**
	 * 
	 * @param fromUserId
	 * @param toUserId
	 * @param page
	 * @return
	 */
	public MsgResponse findUnReadMsgs(long userId, PageInfo page);

	/**
	 * 取得两人的聊天记录
	 * 
	 * @param fromUserId
	 * @param toUserId
	 * @param page
	 * @return
	 */
	public MsgResponse findChatsByFromUserIdAndToUserId(long fromUserId,
			long toUserId, PageInfo page);
	
	/**
	 * 取得用户的聊天记录
	 * 
	 * @param userId
	 * @param endDate
	 * @param page
	 * @return
	 */
	public MsgResponse findUserMsgs(long userId,String endDate,PageInfo page);
}
