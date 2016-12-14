package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.DoctorHospital;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface DoctorHospitalMapper {

	// public List<DoctorHospital> findAllDoctorHospitals();

	public DoctorHospital findById(@Param(value = "id") long id);

	public List<DoctorHospital> findByDoctorId(
			@Param(value = "doctorId") long doctorId);

	/**
	 * 按照医院查找
	 * 
	 * @param hospitalId
	 * @return
	 */
	public List<DoctorHospital> findByHospitalId(
			@Param(value = "hospitalId") long hospitalId,
			@Param(value = "filterText") String filterText);

	/**
	 * 查找医生的主医院信息
	 * 
	 * @param doctorId
	 * @return
	 */
	public List<DoctorHospital> findMainDoctorHospital(
			@Param(value = "doctorId") long doctorId);

	public List<DoctorHospital> findByDoctorIdStatus(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "status") Integer status);

	public DoctorHospital findByDoctorIdAndHospitalId(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "hospitalId") long hospitalId);

	public void addDoctorHospital(
			@Param(value = "doctorHospital") DoctorHospital doctorHospital);

	public void addDoctorHospitals(
			@Param(value = "doctorHospitals") List<DoctorHospital> doctorHospitals);

	public void updateDoctorHospital(
			@Param(value = "doctorHospital") DoctorHospital doctorHospital);

	/**
	 * 更新doctor_hospital状态
	 * 
	 * @param id
	 * @param status
	 */
	@Update(" update doctor_hospital set status = #{status} where id = #{id} ")
	public void updateStatus(@Param(value = "id") long id,
			@Param(value = "status") int status);

	public void updateDoctorHospitals(
			@Param(value = "doctorHospitals") List<DoctorHospital> doctorHospitals);

	public void deleteById(@Param(value = "id") long id);

	public List<DoctorHospital> findByHospitalAndDept(
			@Param(value = "hospitalId") long hospitalId,
			@Param(value = "deptId") long deptId);

	public DoctorHospital findByHospitalAndDeptAndDoctor(
			@Param(value = "hospitalId") long hospitalId,
			@Param(value = "deptId") long deptId,
			@Param(value = "doctorId") long doctorId);
}
