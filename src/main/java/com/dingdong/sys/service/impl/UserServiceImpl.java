package com.dingdong.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.util.Md5Util;
import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.mapper.HospitalMapper;
import com.dingdong.register.mapper.RegisterMapper;
import com.dingdong.register.model.Hospital;
import com.dingdong.register.model.Register;
import com.dingdong.sys.exception.SysErrorCode;
import com.dingdong.sys.mapper.UserMapper;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.TokenService;
import com.dingdong.sys.service.UserService;
import com.dingdong.sys.service.UserValidateService;
import com.dingdong.sys.vo.request.UserRequest;
import com.dingdong.sys.vo.response.UserAllMsgResponse;
import com.dingdong.sys.vo.response.UserResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
	private static final Logger LOG = LoggerFactory
			.getLogger(UserServiceImpl.class);
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private HospitalMapper hospitalMapper;

	@Autowired
	private UserValidateService userValidateService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private RegisterMapper registerMapper;

	@Override
	public UserResponse findAllUsers() {
		UserResponse response = new UserResponse();
		response.setUsers(this.userMapper.findAllUsers());
		return response;
	}

	@Override
	public UserResponse findUserById(long id) {
		UserResponse response = new UserResponse();
		List<User> userList = new ArrayList<User>();
		userList.add(this.userMapper.findById(id));
		response.setUsers(userList);
		return response;
	}

	@Override
	public UserAllMsgResponse findUserAllMsgById(long id) {
		UserAllMsgResponse response = new UserAllMsgResponse();
		List<User> userList = new ArrayList<User>();

		// 获取用户
		User user = this.userMapper.findById(id);
		if (user == null) {
			response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
			return response;
		}
		userList.add(user);
		response.setUsers(userList);

		// 获取用户的各种信息
		long userId = id;
		// 是否只统计数量
		boolean isJustStatNum = true;
		if (!isJustStatNum) {
			// 排队状态
			int status = Register.Status.QUEUE.getValue();
			List<Register> queueRegisterList = registerMapper
					.findByUserIdAndStatus(userId, status);
			int queueNum = queueRegisterList == null ? 0 : queueRegisterList
					.size();
			response.setQueueNum(queueNum);
			response.setQueueRegisterList(queueRegisterList);

			// 草稿状态
			status = Register.Status.DRAFT.getValue();
			List<Register> draftRegisterList = registerMapper
					.findByUserIdAndStatus(userId, status);
			int draftNum = draftRegisterList == null ? 0 : draftRegisterList
					.size();
			response.setDraftNum(draftNum);
			response.setDraftRegisterList(draftRegisterList);

			// 预约成功状态
			status = Register.Status.SUCCESS.getValue();
			List<Register> dataRegisterList = registerMapper
					.findByUserIdAndStatus(userId, status);
			int dataNum = dataRegisterList == null ? 0 : dataRegisterList
					.size();
			response.setDataNum(dataNum);
			response.setDataRegisterList(dataRegisterList);

			// 诊疗完成
			status = Register.Status.TREATED.getValue();
			List<Register> treatRegisterList = registerMapper
					.findByUserIdAndStatus(userId, status);
			int treatNum = treatRegisterList == null ? 0 : treatRegisterList
					.size();
			response.setTreatNum(treatNum);
			response.setTreatRegisterList(treatRegisterList);

			// 已评价
			status = Register.Status.EVALUATED.getValue();
			List<Register> assessRegisterList = registerMapper
					.findByUserIdAndStatus(userId, status);
			int assessNum = assessRegisterList == null ? 0 : assessRegisterList
					.size();
			response.setAssessNum(assessNum);
			response.setAssessRegisterList(assessRegisterList);
		} else {
			// 只统计数量
			List<Register> registers = registerMapper.statUserMsg(userId);

			if (registers != null) {
				for (Register register : registers) {
					int status = register.getStatus();
					int num = register.getSeq();

					if (status == Register.Status.QUEUE.getValue()) {
						response.setQueueNum(num);
					} else if (status == Register.Status.DRAFT.getValue()) {
						response.setDraftNum(num);
					} else if (status == Register.Status.SUCCESS.getValue()) {
						response.setDataNum(num);
					} else if (status == Register.Status.TREATED.getValue()) {
						response.setTreatNum(num);
					} else if (status == Register.Status.EVALUATED.getValue()) {
						response.setAssessNum(num);
					}
				}
			}
		}

		return response;
	}

	@Override
	public UserResponse findUserByOpenId(String openId) {
		LOG.info("the openId is " + openId);
		UserResponse response = new UserResponse();
		List<User> userList = new ArrayList<User>();
		response.setUsers(userList);
		return response;
	}

	@Override
	public UserResponse addUser(UserRequest request) {
		UserResponse response = new UserResponse();
		List<User> users = request.getUsers();
		if (users.size() == 1) {
			User user = users.get(0);
			this.userMapper.addUser(user);
		} else
			this.userMapper.addUsers(users);
		response.setResponseStatus(0);
		return response;
	}

	@Override
	public ResponseBody updateUser(long id, String name, Integer gender,
			String address, String certificateId, String headImgUrl) {
		ResponseBody response = new ResponseBody();

		User user = this.userMapper.findById(id);
		if (user == null) {
			return response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
		}

		if (name != null)
			user.setName(name);
		if (gender != null)
			user.setGender(gender);
		if (address != null)
			user.setAddress(address);
		if (headImgUrl != null)
			user.setHeadImgUrl(headImgUrl);

		this.userMapper.updateUser(user);
		return response;
	}

	@Override
	public ResponseBody updateUserRole(long id, int role) {
		ResponseBody response = new ResponseBody();

		User user = this.userMapper.findById(id);
		if (user == null) {
			return response.setErrorCode(SysErrorCode.USER_ID_NOT_EXIST);
		}

		user.setRole(role);

		this.userMapper.updateUser(user);
		return response;
	}

	@Override
	public UserResponse userLogin(String phone, String password,
			String userAgent) {
		UserResponse response = new UserResponse();
		List<User> userList = new ArrayList<User>();
		password = Md5Util.encode2hex(password);
		User user = userMapper.findByPhoneAndPassword(phone, password);
		if (user != null) {
			if (User.Role.HOSPITALADMIN.getId() == user.getRole()) {// 如果是医院运维
				long hospitalId = user.getOpHospitalId();
				if (hospitalId != 0) {
					Hospital hospital = hospitalMapper.findById(hospitalId);
					if (hospital != null) {
						user.setHospital(hospital);
					} else {
						response.setErrorCode(SysErrorCode.OP_HOSPITAL_NOT_EXIST);
					}
				} else {
					response.setErrorCode(SysErrorCode.OP_HOSPITAL_NOT_EXIST);
				}
			}
			userList.add(user);
			String tokenId = tokenService
					.generateToken(user.getId(), userAgent);
			response.setTokenId(tokenId);
		} else {
			response.setErrorCode(SysErrorCode.USER_PASSWORD_WRONG);
		}

		response.setUsers(userList);
		return response;
	}

	@Override
	public UserResponse userPasswordReset(String phone, int role,
			String msgCode, String password, String userAgent) {
		UserResponse response = new UserResponse();
		List<User> userList = new ArrayList<User>();
		Map<String, Object> map = userValidateService.doValidate(0, phone,
				msgCode, "", 0);
		if (map.get("ok").equals(1)) {
			User user = userMapper.findByPhoneAndRole(phone, role);
			Hospital hospital = hospitalMapper.findById(user.getOpHospitalId());
			user.setHospital(hospital);
			user.setPassword(Md5Util.encode2hex(password));
			userMapper.updateUser(user);
			userList.add(user);
			String tokenId = tokenService
					.generateToken(user.getId(), userAgent);
			response.setTokenId(tokenId);
		} else {
			response.setResponseStatus(0);
			response.setResponseDesc(map.get("msg").toString());
		}
		response.setUsers(userList);
		return response;
	}
}
