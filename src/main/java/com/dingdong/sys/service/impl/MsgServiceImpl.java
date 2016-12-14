package com.dingdong.sys.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.PageInfo;
import com.dingdong.sys.mapper.MsgMapper;
import com.dingdong.sys.model.Msg;
import com.dingdong.sys.service.MsgService;
import com.dingdong.sys.vo.response.MsgResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class MsgServiceImpl implements MsgService {
	private static final Logger LOG = LoggerFactory
			.getLogger(MsgServiceImpl.class);
	@Autowired
	private MsgMapper msgMapper;

	@Override
	public MsgResponse addMsg(Msg msg) {
		LOG.info(msg.toString());
		MsgResponse response = new MsgResponse();
		this.msgMapper.addMsg(msg);
		List<Msg> msgList = new ArrayList<Msg>();
		msgList.add(msg);
		response.setMsgs(msgList);
		return response;

	}

	@Override
	public MsgResponse findChatsByFromUserIdAndToUserId(long fromUserId,
			long toUserId, PageInfo page) {
		// TODO Auto-generated method stub
		MsgResponse response = new MsgResponse();
		response.setMsgs(this.msgMapper.findChatsByFromUserIdAndToUserId(
				fromUserId, toUserId, page));
		return response;
	}

	@Override
	public ResponseBody updateMsgRead(long toId) {
		MsgResponse response = new MsgResponse();
		this.msgMapper.updateMsgRead(toId);

		return response;
	}

	@Override
	public MsgResponse findUnReadMsgs(long userId, PageInfo page) {
		MsgResponse response = new MsgResponse();
		response.setMsgs(this.msgMapper.findUnReadMsgs(userId, page));
		return response;
	}

	@Override
	public MsgResponse findUserMsgs(long userId, String endDate, PageInfo page) {
		MsgResponse response = new MsgResponse();
		response.setMsgs(this.msgMapper.findUserMsgs(userId, endDate, page));
		return response;
	}

}
