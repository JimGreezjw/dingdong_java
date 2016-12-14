package com.dingdong.sys.vo.util;

/**
 * 消息的一些常量，主要是记录消息模板的类型
 * 
 * @author niukai
 * @created on December 25th, 2015
 * 
 */
public class SmsConst {

	/**
	 * 成功码
	 */
	public static String SUCCESS_CODE = "000000";

	/**
	 * 验证模板号
	 */
	public static String TEMPLATE_VALIDATION = "58196";

	/**
	 * 旧的排队模板号
	 */
	public static String TEMPLATE_QUEUE_OLD = "58230";

	/**
	 * 新的排队模板号
	 */
	public static String TEMPLATE_QUEUE = "58990";

	/**
	 * 通知用户进行预约挂号的确认
	 */
	public static String TEMPLATE_INFORM_USER_CONFIRM = "59266";

	/**
	 * 排队人数达到预设值模板号
	 */
	public static String TEMPLATE_REACH_PRESET_NUM = "58982";

	/**
	 * 提醒医生外出问诊模板号
	 */
	public static String TEMPLATE_ALARM_WORK = "58983";

	/**
	 * 挂号模板号
	 */

	public static String TEMPLATE_ORDER = "75459";// "71069";// "58236";

	/**
	 * 测试模板号
	 */
	public static String TEMPLATE_TEST = "1";

	/**
	 * 短信服务器，正式环境的主机
	 */
	public static String FORMAL_IP = "app.cloopen.com";

	/**
	 * 短信服务器，测试环境的主机
	 */
	public static String TEST_IP = "sandboxapp.cloopen.com";

	/**
	 * 短信服务器的端口
	 */
	public static String MESSAGE_PORT = "8883";

	/**
	 * 短信服务器的account SID
	 */
	public static String ACCOUNT_SID = "aaf98f89512446e201512d7922fd1a29";

	/**
	 * 短信服务器的
	 */
	public static String AUTH_TOKEN = "7df11d73e3c04e7a81aa13ed78838a2e";

	/**
	 * appID
	 */
	public static String APP_ID = "8a48b55151d688bc0151d744a61f0267";

	/**
	 * 提前几天通知进行挂号
	 */
	public static int DOCTOR_PREPARE_DAY = 7;

	/**
	 * 患者提前几天取消挂号
	 */
	public static int PATIENT_PREPARE_DAY = 3;

	/**
	 * 患者要在多长时间内完成确认
	 */
	public static int PATIENT_CONFIRM_MINUTE = 60;

	/**
	 * 验证类型的消息
	 */
	public static String MESSAGE_TYPE_VALIDATION = "validation";

	/**
	 * 排队类型的消息
	 */
	public static String MESSAGE_TYPE_QUEUE = "queue";

	/**
	 * 挂号类型的消息
	 */
	public static String MESSAGE_TYPE_REGISTER = "register";

	/**
	 * 人数达到预设值类型的消息
	 */
	public static String MESSAGE_TYPE_REACH_PRESET_NUM = "reachnum";

	/**
	 * 通知用户确认类型的短信
	 */
	public static String MESSAGE_TYPE_INFORM_USER_CONFIRM = "userconfirm";

	/**
	 * 提醒出诊类型的消息
	 */
	public static String MESSAGE_TYPE_ALARM_WORK = "alarmwork";

	/**
	 * 消息状态——成功
	 */
	public static int MESSAGE_STATE_SUCCESS = 1;

	/**
	 * 消息状态——失败
	 */
	public static int MESSAGE_STATE_FAIL = 0;

	/**
	 * 账户最小金额
	 */
	public static int ACCOUNT_MIN_MONEY = 100;

	/**
	 * 默认挂号费用
	 */
	public static int DEFAULT_REGISTER_MONEY = 100;

	/**
	 * 取消挂号费需要扣的钱数
	 */
	public static int DEFAULT_DISCARD_MONEY = 15;

	/**
	 * 失败后重新执行的次数
	 */
	public static int FAILURE_REPEAT_TIME = 10;
}
