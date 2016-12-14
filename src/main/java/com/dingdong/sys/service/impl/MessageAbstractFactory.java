package com.dingdong.sys.service.impl;

import java.util.Map;

import com.dingdong.sys.service.SmsValidateMessageService;

/**
 * 消息的工厂类
 * 
 * @author niukai
 * @created on November 28th, 2015
 * 
 */
public final class MessageAbstractFactory {

    private SmsValidateMessageService msgService;
    public static int DEFAULT_TIME = 5;

    public MessageAbstractFactory(String msgCenter) {
        msgService = new DefaultMessageServiceImpl();
    }

    public Map<String, Object> sendSmsMessage(String mobileNo, String templateID, int minites) {
        return msgService.sendSmsMessage(mobileNo, templateID, minites);
    }

    public String getSuccessCode() {
        return msgService.getSuccessCode();
    }
}
