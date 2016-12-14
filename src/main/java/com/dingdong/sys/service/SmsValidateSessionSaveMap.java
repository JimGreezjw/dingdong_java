package com.dingdong.sys.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dingdong.sys.model.SmsValidateMessageVO;

@Component("smsValidateSessionSaveMap")
public class SmsValidateSessionSaveMap {
	private Map<String, SmsValidateMessageVO> map;
	private static int MAX_SIZE = 1000;

	public SmsValidateSessionSaveMap() {
		map = new HashMap<String, SmsValidateMessageVO>(MAX_SIZE);
	}

	/**
	 * 获取MessageVO
	 * 
	 * @param mobileNo
	 * @return
	 */
	public SmsValidateMessageVO getMessage(String mobileNo) {
		return map.get(mobileNo);
	}

	/**
	 * 添加Message
	 * 
	 * @param messageVO
	 */
	public synchronized void addMessage(SmsValidateMessageVO messageVO) {
		String mobileNo = messageVO.getMobileNo();

		if (map.get(mobileNo) != null) {
			map.put(mobileNo, messageVO);
			return;
		}

		// 检查大小，以免浪费时间
		if (map.size() >= MAX_SIZE) {
			return;
		}

		map.put(mobileNo, messageVO);
	}

	/**
	 * 移出Message，验证通过后在内存中移出
	 * 
	 * @param messageVO
	 */
	public void removeMessage(SmsValidateMessageVO messageVO) {
		String mobileNo = messageVO.getMobileNo();
		map.remove(mobileNo);
	}
}
