package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.Register;

/**
 * 医生信息响应对象
 * 
 * @author chenliang
 * 
 */
public class RegisterResponse extends ResponseBody {

	private List<Register> registers;
	// 本次操作积分奖励
	private int bonusScore;

	public int getBonusScore() {
		return bonusScore;
	}

	public void setBonusScore(int bonusScore) {
		this.bonusScore = bonusScore;
	}

	public List<Register> getRegisters() {
		return registers;
	}

	public RegisterResponse setRegisters(List<Register> registers) {
		this.registers = registers;
		return this;
	}
}
