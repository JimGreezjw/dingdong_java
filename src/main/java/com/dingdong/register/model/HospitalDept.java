package com.dingdong.register.model;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

@Alias("hospitalDept")
public class HospitalDept extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3935452045891383084L;
	private long hospitalId = 0l;
	private String hospitalName = "";
	private long deptId = 0l;
	private String deptName = "";
	private long managerId = 0l;
	private long creatorId = 0l;
	private Date createTime = new Date();
	private int status = 1;
	private int total = 0;
	private List<DoctorHospital> doctorHospitals;

	public List<DoctorHospital> getDoctorHospitals() {
		return doctorHospitals;
	}

	public void setDoctorHospitals(List<DoctorHospital> doctorHospitals) {
		this.doctorHospitals = doctorHospitals;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
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

	public long getManagerId() {
		return managerId;
	}

	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
