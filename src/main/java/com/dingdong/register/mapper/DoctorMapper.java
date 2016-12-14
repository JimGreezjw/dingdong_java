package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.Doctor;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface DoctorMapper {

	public List<Doctor> findAllDoctors(
			@Param(value = "filterText") String filterText);

	/**
	 * 获取最新加入的医生
	 * 
	 * @param requireNum
	 * @return
	 */
	public List<Doctor> getNewJoinDoctors(
			@Param(value = "requireNum") int requireNum);

	/**
	 * 获取还未签约使用的医生列表
	 * 
	 * @param name
	 * @return
	 */
	public List<Doctor> findUnsignedDoctors(@Param(value = "name") String name);

	/**
	 * 按照状态获取医生列表
	 * 
	 * @param status
	 * @return
	 */
	public List<Doctor> findByStatus(@Param(value = "status") int status);

	public List<Doctor> findUnsignedDoctorsApply(
			@Param(value = "name") String name);

	public Doctor findById(@Param(value = "id") long id);

	/**
	 * 获取科室下的全部医生
	 * 
	 * @param deptOutline
	 * @return
	 */
	public List<Doctor> findByDeptOutline(
			@Param(value = "deptOutline") String deptOutline);

	/**
	 * 查询是否已经有预定号码的医生
	 * 
	 * @param mobileNo
	 * @return
	 */
	public Doctor findByMobileNo(@Param(value = "mobileNo") String mobileNo);

	/**
	 * 通过系统用户编号查找医生编号
	 * 
	 * @param userId
	 * @return
	 */
	public List<Doctor> findByUserId(@Param(value = "userId") long userId);

	/**
	 * 通过医生名称查找医生
	 * 
	 * @param userId
	 * @return
	 */
	public List<Doctor> findByName(@Param(value = "name") String name);

	/**
	 * 通过医生名称,医院，科室查找医生
	 * 
	 * @param userId
	 * @return
	 */
	public List<Doctor> findByNameHospitalDept(
			@Param(value = "name") String name,
			@Param(value = "hospitalName") String hospitalName,
			@Param(value = "deptName") String deptName);

	/**
	 * 按照id查询
	 * 
	 * @param ids
	 * @return
	 */
	public List<Doctor> findByIds(@Param(value = "ids") List<Long> ids);

	public void addDoctors(@Param(value = "doctors") List<Doctor> doctors);

	public void addDoctor(@Param(value = "doctor") Doctor doctor);

	public void updateDoctor(@Param(value = "doctor") Doctor doctor);

	public void deleteById(@Param(value = "id") long id);

	public void delectDoctors(List<Integer> idList);

	//
	/**
	 * 添加积分
	 * 
	 * @param id
	 * @param add_count
	 *            --增加的数量
	 * @param expect_count
	 *            -期待更新记录的原数量 一般为+1，-1 积分增量
	 */
	public int updateFansCount(@Param(value = "id") long id,
			@Param(value = "add_count") int add_count,
			@Param(value = "expect_count") long expect_count);

	public void updateBySelective(@Param(value = "doctor") Doctor doctor);
}
