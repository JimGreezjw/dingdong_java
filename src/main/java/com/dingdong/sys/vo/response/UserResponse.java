package com.dingdong.sys.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.sys.model.User;

/**
 * 医生信息响应对象
 * 
 * @author chenliang
 * 
 */
public class UserResponse extends ResponseBody {

	private List<User> users;

	private String tokenId;

	public List<User> getUsers() {
		return users;
	}

	public UserResponse setUsers(List<User> users) {
		this.users = users;
		return this;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

}
