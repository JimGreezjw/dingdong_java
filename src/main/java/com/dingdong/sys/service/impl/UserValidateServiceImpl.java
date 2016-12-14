package com.dingdong.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dingdong.register.mapper.DoctorMapper;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.service.impl.RegisterServiceImpl;
import com.dingdong.sys.mapper.SmsValidateMessageMapper;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.SmsValidateMessageVO;
import com.dingdong.sys.rule.IAfterRule;
import com.dingdong.sys.rule.IBeforeRule;
import com.dingdong.sys.service.SmsValidateSessionSaveMap;
import com.dingdong.sys.service.UserValidateService;
import com.dingdong.sys.service.impl.rule.ClearSmsSessionRule;

@Service
@Transactional
public class UserValidateServiceImpl implements UserValidateService {
	private static final Logger LOG = LoggerFactory
			.getLogger(RegisterServiceImpl.class);
	// 消息的前后规则
	List<IBeforeRule> beforeRuleList = new ArrayList<IBeforeRule>();
	List<IAfterRule> afterRuleList = new ArrayList<IAfterRule>();

	@Autowired
	private SmsValidateSessionSaveMap smsValidateSessionSaveMap;
	@Autowired
	private SmsValidateMessageMapper smsValidateMessageMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DoctorMapper doctorMapper;

	public UserValidateServiceImpl() {
		// FIXME 添加前后规则
		this.addBeforeRuleInner();
		this.addAfterRuleInner();
		// end
	}

	@Override
	public Map<String, Object> doValidate(long userId, String mobileNo,
			String msgCode, String imgCode, int mode) {
		HashMap<String, Object> retMap = new HashMap<String, Object>();
		LOG.info(
				"sms valid data userId={},mobileNo={},msgCode={},imgCode={},mode={}",
				userId, mobileNo, msgCode, imgCode, mode);
		if (StringUtils.isEmpty(mobileNo)) {
			retMap.put("ok", 0);
			retMap.put("msg", "手机号码为空");
			return retMap;
		}

		// 开始进行验证
		SmsValidateMessageVO messageVO = null;
		// 服务端查找
		List<SmsValidateMessageVO> list = smsValidateMessageMapper
				.findMessageVoByMobileNo(mobileNo);

		if (list == null || list.size() < 1) {
			retMap.put("ok", 0);
			retMap.put("msg", "请重新获取短信验证码");
			return retMap;
		}

		// 获取最新一条
		messageVO = list.get(0);

		// 验证码的时效性
		Date expireTime = messageVO.getExpireTime();
		Date nows = new Date();

		if (nows.after(expireTime)) {
			retMap.put("ok", 0);
			retMap.put("msg", "验证码已经失效,请重新获取短信验证码");
			return retMap;
		}

		String serverCode = messageVO.getValidateCode();
		if (serverCode.equals(msgCode)) {
			retMap.put("ok", 1);
			retMap.put("msg", "短信验证成功！");
			if (0 == mode)// 用户
			{
				this.userMapper.updateUserPhone(userId, mobileNo);
			} else if (1 == mode) {// 医生登录时校验号码
				// 与医生绑定
				this.userMapper.updateUserPhone(userId, mobileNo);
				Doctor doctor = doctorMapper.findByMobileNo(mobileNo);
				if (doctor != null) {
					// 与用户关联
					doctor.setUserId(userId);
					doctor.setStatus(Doctor.Status.SIGNED.getValue());
					doctorMapper.updateDoctor(doctor);
					// 返回doctorId
					retMap.put("doctorId", doctor.getId());
				}
			}

			// 先不要执行规则
			// this.execAfterRules(messageVO);
		} else {
			retMap.put("ok", 0);
			retMap.put("msg", "验证码错误");
		}

		return retMap;
	}

	@Override
	public void addBeforeRuleInner() {

	}

	@Override
	public void addAfterRuleInner() {
		afterRuleList.add(new ClearSmsSessionRule());
	}

	@Override
	public void execBeforeRule(SmsValidateMessageVO messageVO) {

	}

	@Override
	public void execAfterRules(SmsValidateMessageVO messageVO) {
		// 执行规则
		for (IAfterRule rule : afterRuleList) {
			rule.afterProcess(messageVO);
		}
	}

	public SmsValidateSessionSaveMap getSmsValidateSessionSaveMap() {
		return smsValidateSessionSaveMap;
	}

	public void setSmsValidateSessionSaveMap(
			SmsValidateSessionSaveMap smsValidateSessionSaveMap) {
		this.smsValidateSessionSaveMap = smsValidateSessionSaveMap;
	}

	public SmsValidateMessageMapper getSmsValidateMessageMapper() {
		return smsValidateMessageMapper;
	}

	public void setSmsValidateMessageMapper(
			SmsValidateMessageMapper smsValidateMessageMapper) {
		this.smsValidateMessageMapper = smsValidateMessageMapper;
	}
}
