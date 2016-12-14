package com.dingdong.register.model;

import org.apache.ibatis.type.Alias;

import com.dingdong.common.model.IdEntity;

@Alias("patient_cond")
public class PatientCond extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7699091735508253716L;
	private long registerId;
	private int condSeq;
	private String condDesc;

	public long getRegisterId() {
		return registerId;
	}

	public PatientCond setRegisterId(long registerId) {
		this.registerId = registerId;
		return this;
	}

	public int getCondSeq() {
		return condSeq;
	}

	public PatientCond setCondSeq(int condSeq) {
		this.condSeq = condSeq;
		return this;
	}

	public String getCondDesc() {
		return condDesc;
	}

	public PatientCond setCondDesc(String condDesc) {
		this.condDesc = condDesc;
		return this;
	}

}
