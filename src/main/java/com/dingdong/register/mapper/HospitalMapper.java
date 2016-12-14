package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.Hospital;

/**
 * 
 * 
 */
@Repository
public interface HospitalMapper {

	public List<Hospital> findAllHospitals(
			@Param(value = "filterText") String filterText);

	@Results(value = { @Result(id = true, property = "id", column = "id"),
			@Result() })
	public Hospital findById(@Param(value = "id") long id);

	public Hospital findByName(@Param(value = "name") String name);

	public List<Hospital> findByOpTele(@Param(value = "opTele") String opTele);

	public List<Hospital> findByStatus(@Param(value = "status") int status);

	public void addHospital(@Param(value = "hospital") Hospital hospital);

	public void addHospitals(
			@Param(value = "hospitals") List<Hospital> hospitals);

	public List<Hospital> getAllHospitalDepts();

	public void updateBySelective(@Param(value = "hospital") Hospital hospital);

	public void deleteById(long id);
}
