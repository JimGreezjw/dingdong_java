package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.Doctor;

/**
 * 医生信息响应对象
 * @author chenliang
 *
 */
public class DoctorResponse extends ResponseBody {

	private List<Doctor> doctors;

	public List<Doctor> getDoctors() {
		return doctors;
	}

	public DoctorResponse setDoctors(List<Doctor> doctors) {
		this.doctors = doctors;
		return this;
	}
}
