package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.PatientCond;

/**
 * 
 * 
 */
@Repository
public interface PatientCondMapper {

	@Results(value = { @Result(id = true, property = "id", column = "id"),
			@Result(property = "registerId", column = "register_id"),
			@Result(property = "condSeq", column = "cond_seq"),
			@Result(property = "condDesc", column = "cond_desc"), })
	public List<PatientCond> findAllPatientConds();

	@Results(value = { @Result(id = true, property = "id", column = "id"),
			@Result(property = "registerId", column = "register_id"),
			@Result(property = "condSeq", column = "cond_seq"),
			@Result(property = "condDesc", column = "cond_desc"), })
	@Select("select * from patient_cond where register_id = #{registerId}")
	public List<PatientCond> findPatientCondByRegisterId(
			@Param(value = "registerId") long registerId);

	@Results(value = { @Result(id = true, property = "id", column = "id"),
			@Result(property = "registerId", column = "register_id"),
			@Result(property = "condSeq", column = "cond_seq"),
			@Result(property = "condDesc", column = "cond_desc"), })
	@Insert("insert into patient_cond (register_id, cond_seq, cond_desc) values  (#{patientCond.registerId}, #{patientCond.condSeq}, #{patientCond.condDesc})")
	public int addPatientCond(
			@Param(value = "patientCond") PatientCond patientCond);

	public void deletePatientCond(@Param(value = "id") long id);

}
