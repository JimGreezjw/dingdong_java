package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.HospitalDept;

/**
 * 医院科室信息响应对象
 * 
 * @author chenliang
 * 
 */
public class HospitalDeptResponse extends ResponseBody {

	private List<HospitalDept> hospitalDepts;

	public List<HospitalDept> getHospitalDepts() {
		return hospitalDepts;
	}

	public HospitalDeptResponse setHospitalDepts(
			List<HospitalDept> hospitalDepts) {
		this.hospitalDepts = hospitalDepts;
		return this;
	}

}
