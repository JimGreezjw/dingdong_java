package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.exception.PatientErrorCode;
import com.dingdong.register.mapper.PatientMapper;
import com.dingdong.register.model.Patient;
import com.dingdong.register.service.PatientService;
import com.dingdong.register.vo.response.PatientResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class PatientServiceImpl implements PatientService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(PatientServiceImpl.class);
	@Autowired
	private PatientMapper patientMapper;

	@Override
	public PatientResponse findAllPatients() {
		PatientResponse response = new PatientResponse();
		response.setPatients(this.patientMapper.findAllPatients());
		return response;
	}

	@Override
	public PatientResponse findPatientById(long id) {
		PatientResponse response = new PatientResponse();
		List<Patient> patientList = new ArrayList<>();
		patientList.add(this.patientMapper.findById(id));
		response.setPatients(patientList);
		return response;
	}

	@Override
	public PatientResponse findPatientByUserId(long userId) {
		PatientResponse response = new PatientResponse();
		response.setPatients(this.patientMapper.findPatientByUserId(userId));
		return response;
	}

	@Override
	public PatientResponse addPatient(Patient patient) {
		PatientResponse response = new PatientResponse();

		this.patientMapper.addPatient(patient);
		List<Patient> patients = new ArrayList<Patient>();
		patients.add(patient);
		response.setPatients(patients);// 返回整个patients,便于客户端取得id

		return response;
	}

	@Override
	public ResponseBody deleteById(long id) {
		ResponseBody response = new ResponseBody();
		this.patientMapper.deleteById(id);
		return response;
	}

	@Override
	public ResponseBody updatePatients(long id, String name, Integer gender,
			Date birthday, String address, String phone, Integer userRelation,
			String certificateId, Long userId) {
		ResponseBody response = new ResponseBody();

		Patient patient = patientMapper.findById(id);
		if (patient == null) {
			response.setErrorCode(PatientErrorCode.PATIENT_ID_NOT_EXIST);
			return response;
		}
		if (name != null)
			patient.setName(name);
		if (gender != null)
			patient.setGender(gender);
		if (birthday != null)
			patient.setBirthday(birthday);
		if (address != null)
			patient.setAddress(address);
		if (phone != null)
			patient.setPhone(phone);
		if (userRelation != null)
			patient.setUserRelation(userRelation);
		if (certificateId != null)
			patient.setCertificateId(certificateId);
		if (userId != null)
			patient.setUserId(userId);

		patient.setCreateTime(new Date());

		patientMapper.updatePatients(patient);
		return response;
	}
}
