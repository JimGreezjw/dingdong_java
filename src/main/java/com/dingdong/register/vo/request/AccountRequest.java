package com.dingdong.register.vo.request;

import java.util.List;

import com.dingdong.common.vo.RequestBody;
import com.dingdong.register.model.Account;

public class AccountRequest extends RequestBody {

	private List<Account> accounts;

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
}
