package com.dingdong.register.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.mapper.AccountMapper;
import com.dingdong.register.model.Account;
import com.dingdong.register.service.AccountService;
import com.dingdong.register.vo.request.AccountRequest;
import com.dingdong.register.vo.response.AccountResponse;

/**
 * 
 * @author yushansoft
 * 
 */
@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(AccountServiceImpl.class);

	@Autowired
	private AccountMapper accountMapper;

	@Override
	public AccountResponse findAccountById(long id) {
		AccountResponse response = new AccountResponse();
		List<Account> accounts = new ArrayList<>();
		accounts.add(this.accountMapper.findById(id));
		response.setAccounts(accounts);
		return response;
	}

	@Override
	public AccountResponse addAccount(AccountRequest request) {
		AccountResponse response = new AccountResponse();
		List<Account> accounts = request.getAccounts();
		for (Account account : accounts) {
			accountMapper.addAccount(account);
		}
		response.setAccounts(accounts);
		return response;
	}

	@Override
	public ResponseBody deleteById(long id) {
		ResponseBody response = new ResponseBody();
		accountMapper.deleteById(id);
		return response;
	}

	@Override
	public ResponseBody updateAccount(AccountRequest request) {
		ResponseBody response = new ResponseBody();
		List<Account> accounts = request.getAccounts();
		for (Account account : accounts) {
			accountMapper.updateAccount(account);
		}
		return response;
	}

	@Override
	public AccountResponse findAccountByUserId(Long userId) {
		AccountResponse response = new AccountResponse();
		List<Account> accounts = this.accountMapper.findByUserId(userId);
		response.setAccounts(accounts);
		return response;
	}
}
