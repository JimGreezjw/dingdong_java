package com.dingdong.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.conf.PageInfo;
import com.dingdong.sys.model.Msg;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface MsgMapper {

	public Msg findById(@Param(value = "id") long id);

	public void addMsg(@Param(value = "msg") Msg msg);

	/**
	 * 取得两人的聊天记录
	 * 
	 * @param fromUserId
	 * @param toUserId
	 * @param page
	 * @return
	 */
	public List<Msg> findChatsByFromUserIdAndToUserId(
			@Param(value = "fromUserId") long fromUserId,
			@Param(value = "toUserId") long toUserId,
			@Param(value = "page") PageInfo page);

	/**
	 * 获取未读消息
	 * 
	 * @param userId
	 * @param page
	 * @return
	 */
	public List<Msg> findUnReadMsgs(@Param(value = "userId") long userId,
			@Param(value = "page") PageInfo page);
	
	/**
	 * 获取用户消息
	 * 
	 * @param userId
	 * @param endDate
	 * @param page
	 * @return
	 */
	public List<Msg> findUserMsgs(@Param(value = "userId") long userId,
			@Param(value = "endDate") String endDate,@Param(value = "page") PageInfo page);

	/**
	 * 设置消息已读
	 * 
	 * @param id
	 */
	public void updateMsgRead(@Param(value = "toId") long toId);

}
