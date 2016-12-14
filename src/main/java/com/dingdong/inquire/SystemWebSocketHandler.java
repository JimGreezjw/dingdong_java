package com.dingdong.inquire;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.dingdong.sys.model.Msg;
import com.dingdong.sys.service.MsgService;
import com.google.gson.Gson;

@Component
public class SystemWebSocketHandler implements WebSocketHandler {
	private static final Log log = LogFactory
			.getLog(SystemWebSocketHandler.class);
	public static final Map<Long, WebSocketSession> userSocketSessionMap;

	static {
		userSocketSessionMap = new HashMap<Long, WebSocketSession>();
	}

	@Autowired
	private MsgService msgService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		Long uid = (Long) session.getAttributes().get("uid");
		if (userSocketSessionMap.get(uid) == null) {
			userSocketSessionMap.put(uid, session);
		}
	}

	/**
	 * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
	 */
	@Override
	public void handleMessage(WebSocketSession session,
			WebSocketMessage<?> message) throws Exception {
		try {
			if (message.getPayloadLength() == 0)
				return;
			Long uid = (Long) session.getAttributes().get("uid");
			Msg msg = new Gson().fromJson(message.getPayload().toString(),
					Msg.class);
			msg.setCreateTime(new Date());
			sendMessageToUser(uid, msg.getToUserId(), new TextMessage(
					JSONObject.fromObject(msg).toString()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		// new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
		// .toJson(msg)));
	}

	/**
	 * 消息传输错误处理
	 */
	@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		Iterator<Map.Entry<Long, WebSocketSession>> it = userSocketSessionMap
				.entrySet().iterator();
		// 移除Socket会话
		while (it.hasNext()) {
			Map.Entry<Long, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
				break;
			}
		}
	}

	/**
	 * 关闭连接后
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus closeStatus) throws Exception {
		System.out.println("Websocket:" + session.getId() + "已经关闭");
		Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap
				.entrySet().iterator();
		// 移除Socket会话
		while (it.hasNext()) {
			Entry<Long, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
				break;
			}
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void broadcast(final TextMessage message) throws IOException {
		Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap
				.entrySet().iterator();

		// 多线程群发
		while (it.hasNext()) {

			final Entry<Long, WebSocketSession> entry = it.next();

			if (entry.getValue().isOpen()) {
				// entry.getValue().sendMessage(message);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							if (entry.getValue().isOpen()) {
								entry.getValue().sendMessage(message);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}).start();
			}

		}
	}

	/**
	 * 给某个用户发送消息
	 */
	public void sendMessageToUser(Long uid, Long toId, TextMessage message)
			throws IOException {
		WebSocketSession session = userSocketSessionMap.get(toId);
		if (session != null && session.isOpen()) {
			session.sendMessage(message);
			addMessage(message, uid, toId);
		} else {
			// 用户不在线，向用户反馈在线消息
			log.info(message);
			try {
				addMessage(message, uid, toId);
				// 告知用户等待
				// Msg msgBySys = new Gson().fromJson(message.getPayload()
				// .toString(), Msg.class);
				Msg msgBySys = new Msg();
				msgBySys.setToUserId(uid);
				msgBySys.setFromUserId(toId);
				msgBySys.setContent("医生当前未在线，你的信息已发送，请稍后 ！");
				String str = JSONObject.fromObject(msgBySys).toString();
				sendMessageBySys(uid, new TextMessage(str));
			} catch (Exception e) {
				log.error(e.getStackTrace());
			}
		}
	}

	/**
	 * 系统返回消息给用户
	 */
	public void sendMessageBySys(Long uid, TextMessage message)
			throws IOException {
		WebSocketSession session = userSocketSessionMap.get(uid);
		if (session != null && session.isOpen()) {
			session.sendMessage(message);
		}
	}

	/**
	 * 存放信息至数据库
	 */
	public void addMessage(TextMessage message, Long uid, Long toId)
			throws IOException {
		Msg msg = new Gson().fromJson(message.getPayload().toString(),
				Msg.class);
		msg.setFromUserId(uid);
		msg.setToUserId(toId);
		msg.setStatus(Msg.Status.RECEIVE.getId());
		this.msgService.addMsg(msg);

	}

}
