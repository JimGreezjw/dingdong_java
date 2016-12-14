package com.dingdong.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dingdong.sys.service.TokenService;
import com.dingdong.sys.vo.response.TokenResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "用户令牌")
public class TokenController {

	@Autowired
	private TokenService tokenService;

	@ApiOperation(value = "验证令牌信息", notes = "验证令牌信息")
	@RequestMapping(value = "/tokens/validateToken", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<TokenResponse> validateToken(
			HttpServletRequest request,
			@CookieValue(value = "userId", defaultValue = "0") long userId,
			@CookieValue(value = "tokenId", defaultValue = "0") String tokenId) {
		String userAgent = request.getHeader("user-agent");
		TokenResponse response = this.tokenService.validateToken(
				Long.valueOf(userId), tokenId, userAgent);
		return new ResponseEntity<TokenResponse>(response, HttpStatus.OK);
	}
}
