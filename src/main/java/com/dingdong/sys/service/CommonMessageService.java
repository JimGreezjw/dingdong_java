package com.dingdong.sys.service;

import java.util.Map;

import com.dingdong.register.model.Hospital;
import com.dingdong.register.model.Patient;
import com.dingdong.register.model.Register;
import com.dingdong.sys.model.User;

/**
 * 信息接口的一般api
 * 
 * @author niukai
 * 
 */
public interface CommonMessageService {

	/**
	 * 旧版发送成功进行排队的短信，已经不建议使用
	 * 
	 * @param mobileNo
	 *            手机号码
	 * @param patientName
	 *            病人称呼
	 * @param doctorName
	 *            医生称呼
	 * @param queueNum
	 *            排队号码
	 * @param minArrageNum
	 *            叮咚门诊与医生沟通需要达到的人数
	 * @param prepareDay
	 *            提前期
	 * @return
	 */
	@Deprecated
	public Map<String, Object> sendQueueMessage(String mobileNo,
			String patientName, String doctorName, int queueNum,
			int minArrageNum, int prepareDay);

	/**
	 * 新版发送成功进行排队的短信，加入了地点
	 * 
	 * @param User
	 *            用户对象
	 * @param doctorName
	 *            医生称呼
	 * @param hospitalName
	 *            地点
	 * @param queueNum
	 *            排队号码
	 * @param minArrageNum
	 *            叮咚门诊与医生沟通需要达到的人数
	 * @param prepareDay
	 *            提前期
	 * @return
	 */
	public Map<String, Object> sendQueueMessage(User user, String doctorName,
			String hospitalName, int queueNum, int minArrageNum, int prepareDay);

	/**
	 * 发送进行挂号确认的短信
	 * 
	 * @param mobileNo
	 *            手机号码
	 * @param patientName
	 *            病人姓名
	 * @param doctorName
	 *            医生姓名
	 * @param timeStr
	 *            时间
	 * @param hospitalName
	 *            地点
	 * @param validMinute
	 *            有效时间
	 * @return
	 */
	public Map<String, Object> sendConfirmRegisterMessage(String mobileNo,
			String patientName, String doctorName, String timeStr,
			String hospitalName, int validMinute);

	// /**
	// * 发送预约短信
	// *
	// * @param mobileNo
	// * 手机号码
	// * @param patientName
	// * 病患称谓
	// * @param doctorName
	// * 医生称谓
	// * @param strTime
	// * 时间
	// * @param hospitalName
	// * 就诊地点
	// * @param prepareDay
	// * 提前期
	// * @param hospitalPhone
	// * 医院电话
	// *
	// * @return
	// */
	// public Map<String, Object> sendOrderMessage(String mobileNo,
	// String patientName, String doctorName, String strTime,
	// String hospitalName, int prepareDay, String hospitalPhone);

	/**
	 * 达到预先设置的出诊数量，提醒医生确定出诊时间
	 * 
	 * @param mobileNo
	 *            医生手机号码
	 * @param doctorName
	 *            医生姓名
	 * @param hospitalName
	 *            出诊地点
	 * @param currNum
	 *            当前排队人数
	 * @param goalNum
	 *            预设排队人数
	 * @return
	 */
	public Map<String, Object> sendReachPresetNumMessage(String mobileNo,
			String doctorName, String hospitalName, int currNum, int goalNum);

	/**
	 * 提醒医生出诊
	 * 
	 * @param mobileNo
	 *            医生手机号码
	 * @param doctorName
	 *            医生称呼
	 * @param strTime
	 *            出诊时间
	 * @param hospitalName
	 *            出诊地点
	 * @return
	 */
	public Map<String, Object> alarmWorkMessage(String mobileNo,
			String doctorName, String strTime, String hospitalName);

	/**
	 * 给用户发送完成诊疗消息
	 * 
	 * @param register
	 * @param user
	 * @return
	 */
	public Map<String, Object> sendPatientFinishTreatmentMessage(
			Register register, User user);

	/**
	 * 进行测试的时候使用
	 * 
	 * @param mobileNo
	 * @return
	 */
	public Map<String, Object> testSendMessage(String mobileNo);

	Map<String, Object> sendOrderMessage(Register register, User user,
			Patient patient, Hospital hospital);
}
