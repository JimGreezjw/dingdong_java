package com.dingdong.register.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.dingdong.register.model.Patient;

/**
 * 
 * @author ChanLueng
 * 
 */
@Repository
public interface PatientMapper {

	public List<Patient> findAllPatients();

	public Patient findById(@Param(value = "id") long id);

	/*
	 * 通过微信UserId搜索
	 */
	public List<Patient> findPatientByUserId(
			@Param(value = "userId") long userId);

	public void addPatient(@Param(value = "patient") Patient patient);

	public void addPatients(@Param(value = "patients") List<Patient> patients);

	public void updatePatient(@Param(value = "id") long id,
			@Param(value = "name") String name,
			@Param(value = "gender") int gender);

	public void deleteById(@Param(value = "id") long id);

	public void updatePatients(@Param(value = "patient") Patient patient);
}
