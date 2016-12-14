package com.dingdong.register.vo.request;

import com.dingdong.common.vo.RequestBody;

public class DoctorHospitalRequest extends RequestBody {
	private long doctorId;
	private long hospitalId;
	private String hospitalName;
	private long deptId;
	private String deptName;
	private int mainFlag = 0;
	private int minQueue;
	private int registerFee;
	private int deposit;

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

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public long getDeptId() {
		return deptId;
	}

	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getMainFlag() {
		return mainFlag;
	}

	public void setMainFlag(int mainFlag) {
		this.mainFlag = mainFlag;
	}

	public int getMinQueue() {
		return minQueue;
	}

	public void setMinQueue(int minQueue) {
		this.minQueue = minQueue;
	}

	public int getRegisterFee() {
		return registerFee;
	}

	public void setRegisterFee(int registerFee) {
		this.registerFee = registerFee;
	}

	public int getDeposit() {
		return deposit;
	}

	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}

}
