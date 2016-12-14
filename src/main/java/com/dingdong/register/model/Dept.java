package com.dingdong.register.model;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

@Alias("dept")
public class Dept extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3935452045891383084L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	private String name = "";
	private Long parentId = 0l;

	/**
	 * 部门大纲级别
	 */
	private String deptOutline = "";

	public String getDeptOutline() {
		return deptOutline;
	}

	public void setDeptOutline(String deptOutline) {
		this.deptOutline = deptOutline;
	}

	private int doctorCount;

	public int getDoctorCount() {
		return doctorCount;
	}

	public void setDoctorCount(int doctorCount) {
		this.doctorCount = doctorCount;
	}

}
