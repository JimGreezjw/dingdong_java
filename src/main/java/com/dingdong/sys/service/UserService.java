package com.dingdong.sys.service;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.sys.vo.request.UserRequest;
import com.dingdong.sys.vo.response.UserAllMsgResponse;
import com.dingdong.sys.vo.response.UserResponse;

public interface UserService {

	public UserResponse findAllUsers();

	public UserResponse findUserById(long id);

	/**
	 * 获取用户的全部信息
	 * 
	 * @param id
	 * @return
	 */
	public UserAllMsgResponse findUserAllMsgById(long id);

	public UserResponse addUser(UserRequest request);

	UserResponse findUserByOpenId(String openId);

	ResponseBody updateUser(long id, String name, Integer gender,
			String address, String certificateId, String headImgUrl);

	/**
	 * 更新用户角色
	 * 
	 * @param id
	 * @param role
	 * @return
	 */
	ResponseBody updateUserRole(long id, int role);

	public UserResponse userLogin(String phone, String password,
			String userAgent);

	public UserResponse userPasswordReset(String phone, int role,
			String msgCode, String password, String userAgent);
}
