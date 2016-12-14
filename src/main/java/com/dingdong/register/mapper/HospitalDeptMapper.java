package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.HospitalDept;

/**
 * 
 * 
 */
@Repository
public interface HospitalDeptMapper {

	public void addHospitalDepts(
			@Param(value = "hospitalDepts") List<HospitalDept> hospitalDepts);

	public void addHospitalDept(
			@Param(value = "hospitalDept") HospitalDept hospitalDept);

	public void deleteById(@Param(value = "id") long id);

	public List<HospitalDept> findByHospitalId(
			@Param(value = "hospitalId") long hospitalId);

	public List<HospitalDept> findDeptTotalByHospitalId(
			@Param(value = "hospitalId") long hospitalId);

	public HospitalDept findById(@Param(value = "id") long id);

	public HospitalDept findByHospitalIdAndDeptId(
			@Param(value = "hospitalId") long hospitalId,
			@Param(value = "deptId") long deptId);
}
