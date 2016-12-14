package com.dingdong.register.vo.request;

import java.util.List;

import com.dingdong.common.vo.RequestBody;
import com.dingdong.register.model.Doctor;

public class DoctorRequest extends RequestBody {

	private List<Doctor> doctors;

	public List<Doctor> getDoctors() {
		return doctors;
	}

	public DoctorRequest setDoctors(List<Doctor> doctors) {
		this.doctors = doctors;
		return this;
	}
}
