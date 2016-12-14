package com.dingdong.register.service;

import java.util.Date;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.vo.response.RegisterResponse;

/**
 * 预约挂号服务
 * 
 * @author chenliang
 * @version 2015年12月13日 上午1:35:01
 */
public interface RegisterService {

	/**
	 * 验证用户的账户余额是否足够
	 * 
	 * @param userId
	 * @param doctorId
	 * @param hospitalId
	 * @return
	 */
	public ResponseBody validateUserAccount(long userId, long doctorId,
			long hospitalId);

	// /**
	// * niukai 确认挂号
	// *
	// * @param registerId
	// * 主键
	// * @param patientId
	// * 患者编号
	// * @param certiNo
	// * 身份证号
	// * @param userRelation
	// * 与患者的关系
	// * @param revisit
	// * 是否复诊
	// * @param phenomenon
	// * 症状
	// * @param attachNo
	// * 附件编号
	// * @return
	// */
	// @Deprecated
	// public RegisterResponse confirmRegister(long registerId, long patientId,
	// String revisit, String phenomenon, String attachNo);

	/**
	 * 确认排队
	 * 
	 * @param registerId
	 *            主键
	 * @return
	 */
	public RegisterResponse confirmQueue(long registerId);

	/**
	 * 取消排队
	 * 
	 * @param id
	 * @return
	 */
	public RegisterResponse cancelQueue(long id);

	/**
	 * 取消挂号
	 * 
	 * @param registerId
	 * @return
	 */
	public RegisterResponse cancelRegister(long registerId);

	/**
	 * 清空已经失效的挂号
	 */
	// public void removeExpireRegister();

	/**
	 * 获得已成功的挂号信息
	 * 
	 * @param request
	 *            可预约条件请求
	 * @return
	 */
	public RegisterResponse getSuccessRegisters(long doctorId, long hospitalId,
			Date scheduleDate);

	/**
	 * 获得某一医生未完成的挂号信息
	 * 
	 * @param doctorId
	 * @return
	 */
	public RegisterResponse getUnFinishedRegistersByDoctorId(long doctorId);

	/**
	 * 获得某一医院未完成的挂号信息
	 * 
	 * @param hospitalId
	 * @return
	 */
	public RegisterResponse getUnFinishedRegistersByHospitalId(long hospitalId);

	/**
	 * 医生查询已经完成的诊疗记录的病患信息
	 * 
	 * @param doctorId
	 *            医生
	 * @param status
	 *            状态
	 * @return
	 */
	public RegisterResponse getRegistersByDoctorIdAndStatus(long doctorId,
			int status);

	/**
	 * 病患查询已经完成的诊疗记录的病患信息
	 * 
	 * @param userId
	 *            病患
	 * @param status
	 *            状态
	 * @return
	 */
	public RegisterResponse getRegistersByUserIdAndStatus(long userId,
			int status);

	/**
	 * 病患查询未经完成的诊疗记录的病患信息
	 * 
	 * @param userId
	 *            病患
	 * @return
	 */
	public RegisterResponse getUnFinishedRegistersByUserId(long userId);

	/**
	 * 用户在在名医处挂号,此处不再使用patientId,直接记录姓名和身份证号
	 * 
	 * @param userId
	 *            用户id
	 * @param patientId
	 *            患者编号
	 * 
	 * @param scheduleId
	 *            名医的日程
	 * @param revisit
	 *            是否复诊
	 * @param phenomenon
	 *            症状
	 * @param attachNo
	 *            附件编号
	 * @param registerMoney
	 *            挂号费
	 * @param minAccountMoney
	 *            账户最小余额
	 * @return
	 */
	public ResponseBody makeAppointment(long userId, long patientId,
			long scheduleId, String revisit, String phenomenon, String attachNo);

	/**
	 * 用户在在名医处挂号,此处不再使用patientId,直接记录姓名和身份证号
	 * 
	 * @param userId
	 *            用户id
	 * @param patientId
	 *            患者编号
	 * 
	 * @param doctorId
	 *            名医编号
	 * @param revisit
	 *            是否复诊
	 * @param phenomenon
	 *            症状
	 * @param attachNo
	 *            附件编号
	 * @param registerMoney
	 *            挂号费
	 * @param minAccountMoney
	 *            账户最小余额
	 * @return
	 */
	public RegisterResponse queueUp(long userId, long patientId, long doctorId,
			long hospitalId, String revisit, String phenomenon, String attachNo);

	/**
	 * 医生完成诊疗
	 * 
	 * @param registerId
	 * @return
	 */
	public RegisterResponse finishTreatment(long registerId);

	/**
	 * 获取用户在某一个日程的挂号列表，用于判断重复预约
	 * 
	 * @param userId
	 * @param scheduleId
	 * @return
	 */
	public RegisterResponse getRegistersByUserIdScheduleId(long userId,
			long scheduleId);

	/**
	 * 获取某一指定编号的挂号信息
	 * 
	 * @param id
	 * @return
	 */
	public RegisterResponse getRegisterById(long id);

	/**
	 * 按照医生病号调度日期编号查询挂号列表
	 * 
	 * @param doctorId
	 * @param scheduleDate
	 * @return
	 */
	public RegisterResponse findByDoctorIdScheduleDateOrderByUserName(
			long doctorId, Date scheduleDate);

	/**
	 * 更新排队或者挂号信息
	 * 
	 * @param registerId
	 * @param phenomenon
	 * @param attachNo
	 * @return
	 */
	public RegisterResponse updateRegister(long registerId, String phenomenon,
			String attachNo);

	public RegisterResponse getRegistersByDoctorIdAndHospitalIdAndStatus(
			long doctorId, long hospitalId, int status);

	// 处理医院预约号信息
	public RegisterResponse updateHospitalRegister(long registerId,
			String hospitalRegisterId);

}
