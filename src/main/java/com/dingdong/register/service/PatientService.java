package com.dingdong.register.service;

import java.util.Date;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.Patient;
import com.dingdong.register.vo.response.PatientResponse;

public interface PatientService {

	public PatientResponse findAllPatients();

	public PatientResponse findPatientById(long id);

	public PatientResponse addPatient(Patient patient);

	PatientResponse findPatientByUserId(long userId);

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	ResponseBody deleteById(long id);

	public ResponseBody updatePatients(long id, String name, Integer gender,
			Date birthday, String address, String phone, Integer userRelation,
			String certificateId, Long userId);
}
