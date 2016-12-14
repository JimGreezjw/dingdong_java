package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.DoctorEval;

/**
 * 执业医院信息响应对象
 * 
 * @author chenliang
 * 
 */
public class DoctorEvalResponse extends ResponseBody {
	private int evalScore;

	public int getEvalScore() {
		return evalScore;
	}

	public void setEvalScore(int evalScore) {
		this.evalScore = evalScore;
	}

	private List<DoctorEval> doctorEvals;

	public List<DoctorEval> getDoctorEvals() {
		return doctorEvals;
	}

	public void setDoctorEvals(List<DoctorEval> doctorEvals) {
		this.doctorEvals = doctorEvals;
	}

}
