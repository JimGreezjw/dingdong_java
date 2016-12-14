package com.dingdong.register.model;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

/**
 * 医生的标签汇总
 * 
 * @author niukai
 * @created on January 26th, 2016
 * 
 */
@Alias("doctorTag")
public class DoctorTag extends IdEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3077111433948292135L;

	private long doctorId;
	private String doctorName;
	private long tagId;
	private String tagName;
	private int times;

	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}
