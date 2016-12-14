package com.dingdong.sys.vo.request;

import java.util.List;

import com.dingdong.common.vo.RequestBody;
import com.dingdong.sys.model.User;

public class UserRequest extends RequestBody {

	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public UserRequest setUsers(List<User> users) {
		this.users = users;
		return this;
	}
}
