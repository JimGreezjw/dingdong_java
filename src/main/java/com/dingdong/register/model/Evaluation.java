package com.dingdong.register.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

@Alias("evaluation")
public class Evaluation extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2825866355095608846L;
	private int evaluationType;
	private long evaluationPerson;
	private long registorId;
	private int evaluationScore;
	private String evaluationDesc;
	private long creatorId;
	private Date createTime;
	private long modifierId;
	private Date modifyTime;
	private String notes;

	public long getEvaluationPerson() {
		return evaluationPerson;
	}

	public Evaluation setEvaluationPerson(long evaluationPerson) {
		this.evaluationPerson = evaluationPerson;
		return this;
	}

	public long getRegistorId() {
		return registorId;
	}

	public Evaluation setRegistorId(long registorId) {
		this.registorId = registorId;
		return this;
	}

	public int getEvaluationScore() {
		return evaluationScore;
	}

	public Evaluation setEvaluationScore(int evaluationScore) {
		this.evaluationScore = evaluationScore;
		return this;
	}

	public int getEvaluationType() {
		return evaluationType;
	}

	public Evaluation setEvaluationType(int evaluationType) {
		this.evaluationType = evaluationType;
		return this;
	}

	public String getEvaluationDesc() {
		return evaluationDesc;
	}

	public Evaluation setEvaluationDesc(String evaluationDesc) {
		this.evaluationDesc = evaluationDesc;
		return this;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public Evaluation setCreatorId(long creatorId) {
		this.creatorId = creatorId;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Evaluation setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public long getModifierId() {
		return modifierId;
	}

	public Evaluation setModifierId(long modifierId) {
		this.modifierId = modifierId;
		return this;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public Evaluation setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
		return this;
	}

	public String getNotes() {
		return notes;
	}

	public Evaluation setNotes(String notes) {
		this.notes = notes;
		return this;
	}

}
