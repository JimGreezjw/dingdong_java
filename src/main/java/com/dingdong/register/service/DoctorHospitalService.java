package com.dingdong.register.service;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.conf.DDPageInfo;
import com.dingdong.register.model.DoctorHospital;
import com.dingdong.register.vo.request.DoctorHospitalRequest;
import com.dingdong.register.vo.response.DoctorHospitalResponse;

public interface DoctorHospitalService {

	// public DoctorHospitalResponse findAllDoctorHospitals();

	public DoctorHospitalResponse findByDoctorId(long doctorId);

	/**
	 * 获取执业医院， 含挂号信息
	 * 
	 * @param doctorId
	 * @param userId
	 * @return
	 */
	public DoctorHospitalResponse findByDoctorIdWithRegisterInfo(long doctorId,
			long userId);

	/**
	 * 按医院查找
	 * 
	 * @param hospitalId
	 * @return
	 */
	public DoctorHospitalResponse findByHospitalId(long hospitalId);

	public DoctorHospitalResponse findByDoctorIdStatus(long doctorId,
			Integer status);

	public DoctorHospitalResponse findDoctorHospitalById(long id);

	public ResponseBody addDoctorHospital(DoctorHospitalRequest request);

	public ResponseBody updateDoctorHospital(long id,
			DoctorHospitalRequest request);

	/**
	 * 更新执业医院信息
	 * 
	 * @param id
	 * @param minQueue
	 * @param registerFee
	 * @param deptName
	 * @param contactName
	 * @param contactTele
	 * @param status
	 *            状态
	 * @return
	 */
	public ResponseBody updateDoctorHospital(long id, Integer minQueue,
			Integer registerFee, Integer deposit, Long deptId, String deptName,
			String contactName, String contactTele, Integer status);

	public ResponseBody deleteById(long id);

	public ResponseBody updateStatus(long id, int status);

	public DoctorHospitalResponse findByHospitalAndDept(long hospitalId,
			long deptId, DDPageInfo<DoctorHospital> pageInfo);

	public DoctorHospitalResponse findByHospitalAndDeptAndDoctor(
			long hospitalId, long deptId, long doctorId);

	public ResponseBody findByHospitalWithPage(long hospitalId,
			String filterText, DDPageInfo<DoctorHospital> pageInfo);
}
