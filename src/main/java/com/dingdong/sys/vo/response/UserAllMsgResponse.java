package com.dingdong.sys.vo.response;

import java.util.List;

import com.dingdong.register.model.Register;

/**
 * 返回患者的全部信息
 * 
 * @author niukai
 * @created on March 26th, 2016
 * 
 */
public class UserAllMsgResponse extends UserResponse {

	// 排队中
	private int queueNum;
	private List<Register> queueRegisterList;

	// 草稿
	private int draftNum;
	private List<Register> draftRegisterList;

	// 已预约数量
	private int dataNum;
	private List<Register> dataRegisterList;

	// 已诊疗
	private int treatNum;
	private List<Register> treatRegisterList;

	// 已评价
	private int assessNum;
	private List<Register> assessRegisterList;

	public int getQueueNum() {
		return queueNum;
	}

	public void setQueueNum(int queueNum) {
		this.queueNum = queueNum;
	}

	public List<Register> getQueueRegisterList() {
		return queueRegisterList;
	}

	public void setQueueRegisterList(List<Register> queueRegisterList) {
		this.queueRegisterList = queueRegisterList;
	}

	public int getDraftNum() {
		return draftNum;
	}

	public void setDraftNum(int draftNum) {
		this.draftNum = draftNum;
	}

	public List<Register> getDraftRegisterList() {
		return draftRegisterList;
	}

	public void setDraftRegisterList(List<Register> draftRegisterList) {
		this.draftRegisterList = draftRegisterList;
	}

	public int getDataNum() {
		return dataNum;
	}

	public void setDataNum(int dataNum) {
		this.dataNum = dataNum;
	}

	public List<Register> getDataRegisterList() {
		return dataRegisterList;
	}

	public void setDataRegisterList(List<Register> dataRegisterList) {
		this.dataRegisterList = dataRegisterList;
	}

	public int getTreatNum() {
		return treatNum;
	}

	public void setTreatNum(int treatNum) {
		this.treatNum = treatNum;
	}

	public List<Register> getTreatRegisterList() {
		return treatRegisterList;
	}

	public void setTreatRegisterList(List<Register> treatRegisterList) {
		this.treatRegisterList = treatRegisterList;
	}

	public int getAssessNum() {
		return assessNum;
	}

	public void setAssessNum(int assessNum) {
		this.assessNum = assessNum;
	}

	public List<Register> getAssessRegisterList() {
		return assessRegisterList;
	}

	public void setAssessRegisterList(List<Register> assessRegisterList) {
		this.assessRegisterList = assessRegisterList;
	}
}
