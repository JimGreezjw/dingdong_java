package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.Hospital;

/**
 * 医院信息响应对象
 * 
 * @author chenliang
 * 
 */
public class HospitalResponse extends ResponseBody {

	private List<Hospital> hospitals;

	public List<Hospital> getHospitals() {
		return hospitals;
	}

	public HospitalResponse setHospitals(List<Hospital> hospitals) {
		this.hospitals = hospitals;
		return this;
	}

}
