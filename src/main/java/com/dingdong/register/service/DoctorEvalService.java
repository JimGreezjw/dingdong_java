package com.dingdong.register.service;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.PageInfo;
import com.dingdong.register.vo.response.DoctorEvalResponse;

public interface DoctorEvalService {

	/**
	 * 按照医生搜索评价
	 * 
	 * @param doctorId
	 * @return
	 */
	public DoctorEvalResponse findByDoctorId(long doctorId, PageInfo pageInfo);

	/**
	 * 按照用户搜索
	 * 
	 * @param userId
	 * @return
	 */
	public DoctorEvalResponse findByUserId(long userId);

	/**
	 * 按照挂号编号查找
	 * 
	 * @param registerId
	 * @return
	 */
	public DoctorEvalResponse findByRegisterId(long registerId);

	public DoctorEvalResponse findById(long id);

	public ResponseBody deleteById(long id);

	/**
	 * 用户对某次诊疗的评价
	 * 
	 * @param userId
	 * @param registerId
	 * @param treatmentEffect
	 * @param serviceAttitude
	 * @param evalDesc
	 * @return
	 */
	public DoctorEvalResponse addDoctorEval(long userId, long registerId,
			int treatmentEffect, int serviceAttitude, String evalDesc);

	/**
	 * 用户对某次诊疗的评价，附带标签
	 * 
	 * @param userId
	 * @param registerId
	 * @param treatmentEffect
	 * @param serviceAttitude
	 * @param evalDesc
	 * @param tags
	 * @return
	 */
	public DoctorEvalResponse addDoctorEvalWithTag(long userId,
			long registerId, int treatmentEffect, int serviceAttitude,
			String evalDesc, String tags);
}
