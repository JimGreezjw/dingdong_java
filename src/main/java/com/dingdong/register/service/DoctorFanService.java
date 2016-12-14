package com.dingdong.register.service;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.PageInfo;
import com.dingdong.register.vo.response.DoctorFanResponse;
import com.dingdong.register.vo.response.DoctorResponse;
import com.dingdong.sys.vo.response.UserResponse;

public interface DoctorFanService {

	public DoctorFanResponse findAllDoctorFans();

	/**
	 * 按照医生搜索粉丝
	 * 
	 * @param doctorId
	 * @return
	 */
	public DoctorFanResponse findByDoctorId(long doctorId, String filterText,
			PageInfo pageInfo);

	/**
	 * 按照用户搜索，直接返回医生列表
	 * 
	 * @param userId
	 * @return
	 */
	public DoctorResponse findByUserId(long userId);

	/**
	 * 查找医生的所有粉丝用户
	 * 
	 * @param doctorId
	 * @return
	 */
	public UserResponse findUsersByDoctor(long doctorId, String filterText,
			PageInfo pageInfo);

	public DoctorFanResponse findDoctorFanById(long id);

	/**
	 * 用户添加对医生的关注
	 * 
	 * @param userId
	 * @param doctorId
	 * @return
	 */
	public ResponseBody addDoctorFan(long userId, long doctorId);

	/**
	 * 用户取消对医生的关注
	 * 
	 * @param userId
	 * @param doctorId
	 * @return
	 */
	public ResponseBody cancelDoctorFan(long userId, long doctorId);

	/**
	 * 用户取消对医生的关注
	 * 
	 * @param id
	 * @return
	 */
	public ResponseBody cancelDoctorFan(long id);

	public ResponseBody deleteById(long id);
}
