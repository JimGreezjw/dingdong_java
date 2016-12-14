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
public class DoctorHospitalResponse extends ResponseBody {

	private List<DoctorHospital> doctorHospitals;

	public List<DoctorHospital> getDoctorHospitals() {
		return doctorHospitals;
	}

	public DoctorHospitalResponse setDoctorHospitals(
			List<DoctorHospital> doctorHospitals) {
		this.doctorHospitals = doctorHospitals;
		return this;
	}
}
