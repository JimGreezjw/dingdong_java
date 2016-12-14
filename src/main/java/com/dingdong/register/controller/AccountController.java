package com.dingdong.register.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.register.service.AccountService;
import com.dingdong.register.vo.request.AccountRequest;
import com.dingdong.register.vo.response.AccountResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "用户账户信息")
public class AccountController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(AccountController.class);
	@Autowired
	private AccountService accountService;

	@ApiOperation(value = "获得账户信息", notes = "获得指定账户信息")
	@RequestMapping(value = "/accounts/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<AccountResponse> getAccountById(
			@PathVariable(value = "id") Long id) {
		AccountResponse response = this.accountService.findAccountById(id);
		return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "添加账户信息", notes = "添加账户信息-新增账户信息")
	@RequestMapping(value = "/accounts", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<ResponseBody> addAccount(
			@RequestBody @Valid AccountRequest request) {
		ResponseBody response = this.accountService.addAccount(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新账户信息", notes = "更新账户信息")
	@RequestMapping(value = "/accounts/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.PUT)
	public ResponseEntity<ResponseBody> updateAccount(
			@RequestBody @Valid AccountRequest request) {
		ResponseBody response = this.accountService.updateAccount(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "删除账户信息", notes = "删除账户信息")
	@RequestMapping(value = "/accounts/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.DELETE)
	public ResponseEntity<ResponseBody> delAccount(
			@PathVariable(value = "id") Long id) {
		ResponseBody response = this.accountService.deleteById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "根据用户userId获得账户信息", notes = "根据用户userId获得指定账户信息")
	@RequestMapping(value = "/accounts/{userId}/userId", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<AccountResponse> getAccountByUserId(
			@PathVariable(value = "userId") Long userId) {
		AccountResponse response = this.accountService
				.findAccountByUserId(userId);
		return new ResponseEntity<AccountResponse>(response, HttpStatus.OK);
	}
}
