package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.DoctorFan;

/**
 * 执业医院信息响应对象
 * 
 * @author chenliang
 * 
 */
public class DoctorFanResponse extends ResponseBody {

	private List<DoctorFan> doctorFans;

	public List<DoctorFan> getDoctorFans() {
		return doctorFans;
	}

	public DoctorFanResponse setDoctorFans(List<DoctorFan> doctorFans) {
		this.doctorFans = doctorFans;
		return this;
	}
}
