package com.dingdong.sys.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.sys.model.Token;

/**
 * 医生信息响应对象
 * 
 * @author chenliang
 * 
 */
public class TokenResponse extends ResponseBody {

	private List<Token> tokens;

	public List<Token> getTokens() {
		return tokens;
	}

	public TokenResponse setTokens(List<Token> tokens) {
		this.tokens = tokens;
		return this;
	}
}
