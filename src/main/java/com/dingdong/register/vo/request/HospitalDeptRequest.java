package com.dingdong.register.vo.request;

import java.util.List;

import com.dingdong.common.vo.RequestBody;
import com.dingdong.register.model.HospitalDept;

public class HospitalDeptRequest extends RequestBody {

	private List<HospitalDept> hospitalDepts;

	public List<HospitalDept> getHospitalDepts() {
		return hospitalDepts;
	}

	public HospitalDeptRequest setHospitalDepts(List<HospitalDept> hospitalDepts) {
		this.hospitalDepts = hospitalDepts;
		return this;
	}
}
