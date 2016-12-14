package com.dingdong.register.vo.request;

import java.util.List;

import com.dingdong.common.vo.RequestBody;
import com.dingdong.register.model.Hospital;

public class HospitalRequest extends RequestBody {

	private List<Hospital> hospitals;

	public List<Hospital> getHospitals() {
		return hospitals;
	}

	public HospitalRequest setHospitals(List<Hospital> hospitals) {
		this.hospitals = hospitals;
		return this;
	}
}
