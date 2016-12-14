package com.dingdong.register.vo.response;

import java.util.List;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.model.Account;

/**
 * 预约信息
 * 
 * @author chenliang
 * @version 2015年12月13日 下午1:41:34
 */
public class AccountResponse extends ResponseBody {

	/* 用户账户信息 */
	private List<Account> accounts;

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

}
