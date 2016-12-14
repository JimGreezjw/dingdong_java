package com.dingdong.sys.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.mapper.PatientMapper;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.model.Patient;
import com.dingdong.register.model.Register;
import com.dingdong.register.service.impl.DoctorServiceImpl;
import com.dingdong.sys.mapper.CommonMessageMapper;
import com.dingdong.sys.model.CommonMessageVO;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.CommonMessageService;
import com.dingdong.sys.service.DDMZService;
import com.dingdong.sys.vo.util.DDCollectionUtils;
import com.dingdong.sys.vo.util.SmsConst;

/**
 * 通用的消息发送
 * 
 * @author niukai
 * 
 */
@Async
@Service("commonMessageService")
public class CommonMessageServiceImpl implements CommonMessageService {
	private static final Logger LOG = LoggerFactory
			.getLogger(DoctorServiceImpl.class);
	/**
	 * 是否是正式环境
	 */
	private boolean isFormal = true;
	private CCPRestSmsSDK restAPI;

	// #排队用户通知方法 ，WX-微信通知， SMS-短信通知
	@Value("#{config['msg.queueNotifyMethod']}")
	private String queueNotifyMethod = "SMS";

	@Autowired
	private DDMZService ddmzService;

	@Value("#{config['dd.servicePhone']}")
	private String ddServicePhone;

	@Autowired
	private PatientMapper patientMapper;

	// @Autowired
	// private DoctorMapper doctorMapper;

	@Autowired
	private HospitalMapper hospitalMapper;

	// @Autowired
	// private UserMapper userMapper;

	@Autowired
	private CommonMessageMapper commonMessageMapper;

	SimpleDateFormat myDateFmt = new SimpleDateFormat("M月d日");

	private final String EVAL_URL = "http://www.yushansoft.com/dingdong/v1/ddpatient/index.html#/user/addEvaluation/";
	private final String EVAL_IMGAGE_URL = "https://mmbiz.qlogo.cn/mmbiz/d7iahysxQQ9nuRNticSGRG5hPgau7rUs9iadicicWicTmxaia4jibiaSWzBgOabgT7lFyGicAY58T6rVDAAYYtLHeSLqt0pg/0?wx_fmt=jpeg";

	public CommonMessageServiceImpl() {
		restAPI = new CCPRestSmsSDK();

		// 是否正式环境
		if (isFormal) {
			restAPI.init(SmsConst.FORMAL_IP, SmsConst.MESSAGE_PORT);
		} else {
			restAPI.init(SmsConst.TEST_IP, SmsConst.MESSAGE_PORT);
		}

		restAPI.setAccount(SmsConst.ACCOUNT_SID, SmsConst.AUTH_TOKEN);
		restAPI.setAppId(SmsConst.APP_ID);
	}

	@Override
	public Map<String, Object> sendQueueMessage(String mobileNo,
			String patientName, String doctorName, int queueNum,
			int minArrageNum, int prepareDay) {

		ArrayList<String> list = new ArrayList<String>();
		list.add(patientName);
		list.add(doctorName);
		list.add("" + queueNum);
		list.add("" + minArrageNum);
		list.add(doctorName);
		list.add("" + prepareDay);

		return this.sendMessage(mobileNo, SmsConst.TEMPLATE_QUEUE_OLD,
				SmsConst.MESSAGE_TYPE_QUEUE,
				list.toArray(new String[list.size()]));
	}

	@Override
	public Map<String, Object> sendQueueMessage(User user, String doctorName,
			String hospitalName, int queueNum, int minArrageNum, int prepareDay) {
		LOG.info(
				"sendQueueMessage user={},doctorName={},hospitalName={},queueNum={},minArrageNum={},prepareDay={}",
				user, doctorName, hospitalName, queueNum, minArrageNum,
				prepareDay);
		String strUser = "用户";
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (queueNotifyMethod.equalsIgnoreCase("WX")
				&& StringUtils.isNotBlank(user.getOpenId())) {
			String content = String.format(
					"【叮咚门诊】尊敬的%s，您已成功加入%s大夫的约诊队列，就诊地点为%s，"
							+ "排队号码是%d。当排队人数达到%d人时，叮咚门诊会与%s大夫确定出诊日期，"
							+ "并在出诊前%d天通知您进行正式预约，请您耐心等待。", strUser, doctorName,
					hospitalName, queueNum, minArrageNum, doctorName,
					prepareDay);

			this.ddmzService.sendCustomTextMsg(user.getOpenId(), content);
			return null;
		}

		ArrayList<String> list = new ArrayList<String>();
		list.add(strUser);
		list.add(doctorName);
		list.add(hospitalName);
		list.add("" + queueNum);
		list.add("" + minArrageNum);
		list.add(doctorName);
		list.add("" + prepareDay);

		return this.sendMessage(user.getPhone(), SmsConst.TEMPLATE_QUEUE,
				SmsConst.MESSAGE_TYPE_QUEUE,
				list.toArray(new String[list.size()]));
	}

	@Override
	public Map<String, Object> sendConfirmRegisterMessage(String mobileNo,
			String patientName, String doctorName, String timeStr,
			String hospitalName, int validMinute) {
		if (StringUtils.isEmpty(mobileNo))
			return null;
		ArrayList<String> list = new ArrayList<String>();
		list.add(patientName);
		list.add(timeStr);
		list.add(doctorName);
		list.add(hospitalName);
		list.add(validMinute + "分钟");

		return this.sendMessage(mobileNo,
				SmsConst.TEMPLATE_INFORM_USER_CONFIRM,
				SmsConst.MESSAGE_TYPE_INFORM_USER_CONFIRM,
				list.toArray(new String[list.size()]));
	}

	// @Override
	// public Map<String, Object> sendOrderMessage(String mobileNo,
	// String patientName, String doctorName, String strTime,
	// String hospitalName, int prepareDay, String hospitalPhone) {
	// if (StringUtils.isEmpty(mobileNo))
	// return null;
	//
	// ArrayList<String> list = new ArrayList<String>();
	// list.add(patientName);
	// list.add(doctorName);
	// list.add(strTime);
	// list.add(hospitalName);
	// list.add("" + prepareDay);
	// list.add(hospitalPhone);
	// list.add(this.ddServicePhone);
	//
	// return this.sendMessage(mobileNo, SmsConst.TEMPLATE_ORDER,
	// SmsConst.MESSAGE_TYPE_REGISTER,
	// list.toArray(new String[list.size()]));
	// }

	// 【叮咚门诊】尊敬的{1}，叮咚门诊已成功为您预约了{2}大夫，就诊时间为{3}、地点为{4}、预约号为{5}。请您携带预约时指定的患者身份证按时就诊。医院电话：{6}，叮咚服务电话：{7}。祝早日康复！
	@Override
	public Map<String, Object> sendOrderMessage(Register register, User user,
			Patient patient, Hospital hospital) {

		if (StringUtils.isBlank(register.getHospitalRegisterId())) {
			LOG.warn("医院预约号为空，无法发送！");
			return null;
		}
		if (null == patient)
			patient = this.patientMapper.findById(register.getPatientId());

		if (null == hospital)
			hospital = this.hospitalMapper.findById(register.getHospitalId());

		if (StringUtils.isEmpty(patient.getPhone()))
			return null;

		ArrayList<String> list = new ArrayList<String>();
		list.add(patient.getName());
		list.add(register.getDoctorName());

		String strDate = this.myDateFmt.format(register.getScheduleDate())
				+ register.getTimeSlotDesc();
		list.add(strDate);
		list.add(hospital.getName());
		list.add(register.getHospitalRegisterId());
		list.add(hospital.getTele());
		list.add(this.ddServicePhone);

		return this.sendMessage(patient.getPhone(), SmsConst.TEMPLATE_ORDER,
				SmsConst.MESSAGE_TYPE_REGISTER,
				list.toArray(new String[list.size()]));
	}

	@Override
	public Map<String, Object> sendReachPresetNumMessage(String mobileNo,
			String doctorName, String hospitalName, int currNum, int goalNum) {

		ArrayList<String> list = new ArrayList<String>();
		list.add(doctorName);
		list.add(hospitalName);
		list.add("" + currNum);
		list.add("" + goalNum);

		return this.sendMessage(mobileNo, SmsConst.TEMPLATE_REACH_PRESET_NUM,
				SmsConst.MESSAGE_TYPE_REACH_PRESET_NUM,
				list.toArray(new String[list.size()]));
	}

	@Override
	public Map<String, Object> alarmWorkMessage(String mobileNo,
			String doctorName, String strTime, String hospitalName) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(doctorName);
		list.add(strTime);
		list.add(hospitalName);

		return this.sendMessage(mobileNo, SmsConst.TEMPLATE_ALARM_WORK,
				SmsConst.MESSAGE_TYPE_ALARM_WORK,
				list.toArray(new String[list.size()]));
	}

	@Override
	public Map<String, Object> testSendMessage(String mobileNo) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("689566");
		list.add("10");

		return this.sendMessage(mobileNo, SmsConst.TEMPLATE_TEST,
				SmsConst.MESSAGE_TYPE_VALIDATION,
				list.toArray(new String[list.size()]));
	}

	/**
	 * 所有的接口，其实最终都是通过这个来实现
	 * 
	 * @param mobileNo
	 * @param templateID
	 * @param msgType
	 * @param messageObjs
	 * @return
	 */
	private Map<String, Object> sendMessage(String mobileNo, String templateID,
			String msgType, String[] messageObjs) {

		HashMap<String, Object> result = null;
		HashMap<String, Object> retMap = new HashMap<String, Object>();

		// 是否要发送短信
		boolean isSendCenter = true;
		if (isSendCenter) {
			result = restAPI.sendTemplateSMS(mobileNo, templateID, messageObjs);
		} else {
			result = new HashMap<String, Object>();
			result.put("statusCode", SmsConst.SUCCESS_CODE);
		}
		// end

		String statusCode = (String) result.get("statusCode");
		if (SmsConst.SUCCESS_CODE.equals(statusCode)) {
			retMap.put("ok", 1);
			retMap.put("msg", "信息发送成功");

			String[] mobileNoArrays = mobileNo.split(",");
			if (!DDCollectionUtils.isEmpty(mobileNoArrays)) {
				for (String singleMobileNo : mobileNoArrays) {
					// 数据库的持久化
					CommonMessageVO messageVO = new CommonMessageVO();
					messageVO.setMobileNo(singleMobileNo);
					messageVO.setMsgType(msgType);
					messageVO.setMsgState(SmsConst.MESSAGE_STATE_SUCCESS);
					messageVO.setSendTime(new Date());

					commonMessageMapper.insertMessageVO(messageVO);
				}
			}
		} else {
			// 错误信息
			String errMsg = (String) result.get("statusMsg");

			retMap.put("ok", 0);
			retMap.put("msg", statusCode + "————>" + errMsg);
		}

		return retMap;
	}

	@Override
	public Map<String, Object> sendPatientFinishTreatmentMessage(
			Register register, User user) {
		LOG.info("the openId is {}", user.getOpenId());
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("ok", 1);
		retMap.put("msg", "信息发送成功");

		try {
			if (StringUtils.isNotBlank(user.getOpenId())) {
				// 这个消息以后换成图文链接，可以直接评价
				String userName = "用户";
				String title = "写一下您对" + register.getDoctorName() + "大夫的印象吧";
				String content = String.format(
						"【叮咚门诊】尊敬的%s，" + register.getDoctorName()
								+ "已为您完成诊疗，祝您早日康复。请您对本次就诊服务进行评价，有叮咚积分好礼相送。",
						userName);
				String url = EVAL_URL + String.valueOf(register.getId());
				String msg = "{\"touser\":\""
						+ user.getOpenId()
						+ "\",\"msgtype\":\"news\",\"news\":{\"articles\":[{\"title\":\""
						+ title + "\",\"description\":\"" + content
						+ "\",\"url\":\"" + url + "\",\"picurl\":\""
						+ EVAL_IMGAGE_URL + "\"}]}}";

				this.ddmzService.sendCustomMsg(msg);
			}
		} catch (Exception e) {
			retMap.put("ok", 0);
			retMap.put("msg", " 消息发送错误————>" + e.getMessage());
		}

		return retMap;
	}
}
