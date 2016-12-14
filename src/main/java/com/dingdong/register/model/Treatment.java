package com.dingdong.register.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

@Alias("treatment")
public class Treatment extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5733514017359830976L;
	private int treatmentType;
	private int treatmentState;
	private long patientId;
	private long hospitalId;
	private long doctorId;
	private long scheduleId;
	private Date recommendTime;
	private long creatorId;
	private Date createTime;
	private long modifierId;
	private Date modifyTime;
	private String notes;

	public long getScheduleId() {
		return scheduleId;
	}

	public Treatment setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
		return this;
	}

	public long getDoctorId() {
		return doctorId;
	}

	public Treatment setDoctorId(long doctorId) {
		this.doctorId = doctorId;
		return this;
	}

	public long getHospitalId() {
		return hospitalId;
	}

	public Treatment setHospitalId(long hospitalId) {
		this.hospitalId = hospitalId;
		return this;
	}

	public long getPatientId() {
		return patientId;
	}

	public Treatment setPatientId(long patientId) {
		this.patientId = patientId;
		return this;
	}

	public int getTreatmentType() {
		return treatmentType;
	}

	public Treatment setTreatmentType(int treatmentType) {
		this.treatmentType = treatmentType;
		return this;
	}

	public int getTreatmentState() {
		return treatmentState;
	}

	public Treatment setTreatmentState(int treatmentState) {
		this.treatmentState = treatmentState;
		return this;
	}

	public Date getRecommendTime() {
		return recommendTime;
	}

	public Treatment setRecommendTime(Date recommendTime) {
		this.recommendTime = recommendTime;
		return this;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public Treatment setCreatorId(long creatorId) {
		this.creatorId = creatorId;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Treatment setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public long getModifierId() {
		return modifierId;
	}

	public Treatment setModifierId(long modifierId) {
		this.modifierId = modifierId;
		return this;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public Treatment setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
		return this;
	}

	public String getNotes() {
		return notes;
	}

	public Treatment setNotes(String notes) {
		this.notes = notes;
		return this;
	}

}
