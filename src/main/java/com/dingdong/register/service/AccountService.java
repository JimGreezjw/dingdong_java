package com.dingdong.register.service;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.vo.request.AccountRequest;
import com.dingdong.register.vo.response.AccountResponse;

public interface AccountService {

	public AccountResponse findAccountById(long id);

	public AccountResponse addAccount(AccountRequest request);

	public ResponseBody deleteById(long id);

	public ResponseBody updateAccount(AccountRequest request);

	public AccountResponse findAccountByUserId(Long userId);

}
