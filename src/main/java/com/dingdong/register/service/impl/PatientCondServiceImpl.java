package com.dingdong.register.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.register.mapper.PatientCondMapper;
import com.dingdong.register.model.PatientCond;
import com.dingdong.register.service.PatientCondService;
import com.dingdong.register.vo.request.PatientCondRequest;
import com.dingdong.register.vo.response.PatientCondResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Service
@Transactional
public class PatientCondServiceImpl implements PatientCondService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(PatientCondServiceImpl.class);
	@Autowired
	private PatientCondMapper patientCondMapper;

	@Override
	public PatientCondResponse findAllPatientConds() {
		PatientCondResponse response = new PatientCondResponse();
		response.setPatientConds(this.patientCondMapper.findAllPatientConds());
		return response;
	}

	@Override
	public PatientCondResponse findPatientCondByRegisterId(long id) {
		PatientCondResponse response = new PatientCondResponse();
		response.setPatientConds(this.patientCondMapper
				.findPatientCondByRegisterId(id));
		return response;
	}

	@Override
	public PatientCondResponse addPatientCond(PatientCondRequest request) {
		PatientCondResponse response = new PatientCondResponse();
		List<PatientCond> patientConds = request.getPatientConds();

		PatientCond patientCond = patientConds.get(0);

		this.patientCondMapper.addPatientCond(patientCond);
		response.setResponseStatus(0);
		return response;
	}
}
