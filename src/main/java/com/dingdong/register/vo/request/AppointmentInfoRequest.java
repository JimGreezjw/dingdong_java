package com.dingdong.register.vo.request;

/**
 *
 * @author chenliang
 * @version 2015年12月13日 上午12:06:11
 */
public class AppointmentInfoRequest {

	/** 医生id */
	private long doctorId;
	/** 医院id */
	private long hospitalId;
	
	public long getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}
	
    public long getHospitalId() {
        return hospitalId;
    }
    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }
}
