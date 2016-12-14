package com.dingdong.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dingdong.common.vo.ResponseBody;
import com.dingdong.sys.model.User;
import com.dingdong.sys.service.UserService;
import com.dingdong.sys.vo.request.UserRequest;
import com.dingdong.sys.vo.response.UserAllMsgResponse;
import com.dingdong.sys.vo.response.UserResponse;

/**
 * 
 * @author ChanLueng
 * 
 */
@Controller
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
@Api(value = "系统用户信息")
public class UserController {

	@Autowired
	private UserService userService;

	private static final int maxAge = 365 * 24 * 60 * 60;

	@ApiOperation(value = "获得所有患者信息", notes = "获得患者信息-获得所有患者信息")
	@RequestMapping(value = "/users", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<UserResponse> getAllUsers() {
		UserResponse response = this.userService.findAllUsers();
		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得患者信息", notes = "获得患者信息-获得指定id患者信息")
	@RequestMapping(value = "/users/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<UserResponse> getUserById(
			@PathVariable(value = "id") Long id) {
		UserResponse response = this.userService.findUserById(id);
		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "获得患者全部信息", notes = "获得患者全部信息-获得指定id患者的全部信息")
	@RequestMapping(value = "/users/{id}/allmsg", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<UserAllMsgResponse> getUserAllMsgById(
			@PathVariable(value = "id") Long id) {
		UserAllMsgResponse response = this.userService.findUserAllMsgById(id);
		return new ResponseEntity<UserAllMsgResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "通过微信号获得患者信息", notes = "获得患者信息-获得指定微信号获得患者信息")
	@RequestMapping(value = "/users/openId", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public ResponseEntity<UserResponse> getUserById(
			@RequestParam(value = "openId") String openId) {
		UserResponse response = this.userService.findUserByOpenId(openId);
		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "添加患者信息", notes = "添加患者信息-新增患者个人信息")
	@RequestMapping(value = "/users", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<UserResponse> insertUser(
			@RequestBody @Valid UserRequest request) {
		UserResponse response = this.userService.addUser(request);
		return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新用户信息", notes = "更新用户的基本信息")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseBody> updateUser(
			@PathVariable(value = "id") long id,
			@ApiParam(value = "名称", required = false) @RequestParam(value = "name", required = false) String name,
			@ApiParam(value = "性别", required = false) @RequestParam(value = "gender", required = false) Integer gender,
			@ApiParam(value = "地址", required = false) @RequestParam(value = "address", required = false) String address,
			@ApiParam(value = "身份证号", required = false) @RequestParam(value = "certificateId", required = false) String certificateId,
			@ApiParam(value = "头像地址", required = false) @RequestParam(value = "headImgUrl", required = false) String headImgUrl) {

		ResponseBody response = this.userService.updateUser(id, name, gender,
				address, certificateId, headImgUrl);
		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "更新用户的角色信息", notes = " 0--表示患者，1--表示医生")
	@RequestMapping(value = "/users/{id}/role", method = RequestMethod.PATCH)
	public ResponseEntity<ResponseBody> updateDoctor(
			@PathVariable(value = "id") long id,
			@ApiParam(value = "角色", required = false) @RequestParam(value = "role", required = false) int role) {

		ResponseBody response = this.userService.updateUserRole(id, role);
		return new ResponseEntity<ResponseBody>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "管理员登录", notes = "管理员通过手机号登录")
	@RequestMapping(value = "/users/login", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<UserResponse> userLogin(
			@ApiParam(value = "手机号", required = true) @RequestParam(value = "phone", required = true) String phone,
			@ApiParam(value = "密码", required = true) @RequestParam(value = "password", required = true) String password,
			HttpServletRequest request, HttpServletResponse response) {
		String userAgent = request.getHeader("user-agent");
		UserResponse userResponse = this.userService.userLogin(phone, password,
				userAgent);
		if (CollectionUtils.isNotEmpty(userResponse.getUsers())) {
			User user = userResponse.getUsers().get(0);
			Cookie cookie;
			cookie = new Cookie("userId", String.valueOf(user.getId()));
			cookie.setMaxAge(maxAge);
			cookie.setPath("/");
			response.addCookie(cookie);
			cookie = new Cookie("tokenId", userResponse.getTokenId());
			cookie.setMaxAge(maxAge);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "管理员密码验证", notes = "管理员通过手机号设置登录密码")
	@RequestMapping(value = "/users/passwordReset", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<UserResponse> userPasswordReset(
			@ApiParam(value = "手机号", required = true) @RequestParam(value = "phone", required = true) String phone,
			@ApiParam(value = "角色", required = true) @RequestParam(value = "role", required = true) int role,
			@ApiParam(value = "验证码", required = true) @RequestParam(value = "msgCode", required = true) String msgCode,
			@ApiParam(value = "密码", required = true) @RequestParam(value = "password", required = true) String password,
			HttpServletRequest request, HttpServletResponse response) {
		String userAgent = request.getHeader("user-agent");
		UserResponse userResponse = this.userService.userPasswordReset(phone,
				role, msgCode, password, userAgent);
		if (CollectionUtils.isNotEmpty(userResponse.getUsers())) {
			User user = userResponse.getUsers().get(0);
			Cookie cookie;
			cookie = new Cookie("userId", String.valueOf(user.getId()));
			cookie.setMaxAge(maxAge);
			cookie.setPath("/");
			response.addCookie(cookie);
			cookie = new Cookie("tokenId", userResponse.getTokenId());
			cookie.setMaxAge(maxAge);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "清除用户ID和令牌缓存", notes = "清除用户ID和令牌缓存")
	@RequestMapping(value = "/users/cookiesRemove", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.POST)
	public ResponseEntity<UserResponse> userCookieRemove(
			HttpServletRequest request, HttpServletResponse response) {
		UserResponse userResponse = new UserResponse();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
	}
}
