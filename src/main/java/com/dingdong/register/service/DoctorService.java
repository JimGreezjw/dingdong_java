package com.dingdong.register.service;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.model.Doctor;
import com.dingdong.register.vo.request.DoctorRequest;
import com.dingdong.register.vo.response.DoctorResponse;

public interface DoctorService {

	public DoctorResponse findAllDoctors(String filterText,
			DDPageInfo<Doctor> pageInfo);

	/**
	 * 获取最新加入的医生
	 * 
	 * @param requireNum
	 * @return
	 */
	public DoctorResponse getNewJoinDoctors(int requireNum);

	/**
	 * 获取未认证的医生列表
	 * 
	 * @param name
	 * @param pageInfo
	 * @return
	 */
	public DoctorResponse findUnsignedDoctors(String name);

	public DoctorResponse findDoctorById(long id);

	public ResponseBody addDoctor(DoctorRequest request);

	ResponseBody updateDoctor(long id, String name, Integer gender,
			String hospitalName, String level, String specialty,
			String introduction);

	/**
	 * 医生与平台签约
	 * 
	 * @param id
	 * @param userId
	 * @param officeTele
	 * @return
	 */
	ResponseBody submitSignDoctor(long id, Long userId, String officeTele);

	/**
	 * 医生与平台签约
	 * 
	 * @param id
	 * @return
	 */
	ResponseBody signDoctor(long id);

	/**
	 * 医生取消与平台签约，或者无法通过认证
	 * 
	 * @param id
	 * @return
	 */
	ResponseBody cancelSignDoctor(long id);

	DoctorResponse updateDoctorBySelective(DoctorRequest request);

	public ResponseBody delDoctorById(long id);

	public DoctorResponse findUnsignedDoctorsApply(String name,
			DDPageInfo<Doctor> pageInfo);

	DoctorResponse findByStatus(int status);
}
