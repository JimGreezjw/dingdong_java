package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.conf.PageInfo;
import com.dingdong.register.model.DoctorEval;

/**
 * 
 * @author lqm
 * 
 */
@Repository
public interface DoctorEvalMapper {

	public DoctorEval findById(@Param(value = "id") long id);

	public List<DoctorEval> findByUserId(@Param(value = "userId") long userId);

	public List<DoctorEval> findByRegisterId(
			@Param(value = "registerId") long registerId);

	public List<DoctorEval> findByDoctorId(
			@Param(value = "doctorId") long doctorId,
			@Param(value = "pageInfo") PageInfo pageInfo);

	public void addDoctorEval(@Param(value = "doctorEval") DoctorEval doctorEval);

	public void deleteById(@Param(value = "id") long id);

}
