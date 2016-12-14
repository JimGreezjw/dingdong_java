package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.DoctorHospital;

/**
 * 执业医院信息响应对象
 * 
 * @author chenliang
 * 
 */
public class DoctorHospitalWithRegisterResponse extends ResponseBody {

	private List<DoctorHospital> doctorHospitals;

	public List<DoctorHospital> getDoctorHospitals() {
		return doctorHospitals;
	}

	public DoctorHospitalWithRegisterResponse setDoctorHospitals(
			List<DoctorHospital> doctorHospitals) {
		this.doctorHospitals = doctorHospitals;
		return this;
	}
}
