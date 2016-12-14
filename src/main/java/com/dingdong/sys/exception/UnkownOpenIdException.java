package com.dingdong.sys.exception;

/**
 * 
 * 系统权限验证异常
 * 
 * @author yushansoft
 * 
 */
public class UnkownOpenIdException {

    public final static String exceptionId = "YS-WX-00001"; // 异常编号
    public final static String message = "无法通过微信OpenId获取用户信息";// 异常消息

    public UnkownOpenIdException() {
        // super("错误编号：" + exceptionId + "," + message);

    }

}
