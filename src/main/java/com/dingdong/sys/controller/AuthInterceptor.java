package com.dingdong.sys.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.dingdong.common.exception.CommonErrorCode;
import com.dingdong.sys.service.TokenService;
import com.dingdong.sys.vo.response.TokenResponse;
import com.dingdong.sys.vo.util.CookieUtil;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOG = LoggerFactory
			.getLogger(AuthInterceptor.class);

	// private static final String loginUrl = "/dingdong/ddpc/login.html";

	public String[] allowUrls;// 还没发现可以直接配置不拦截的资源，所以在代码里面来排除

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}

	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String host = request.getServerName();
		if ("127.0.0.1".equals(host) || "localhost".equalsIgnoreCase(host))
			return true;

		String uri = request.getRequestURI();
		// tokenUid应该是变化的，现在已经加入一天缓存
		// 验证通过就重新生成新的tokenUid

		String userAgent = request.getHeader("user-agent");
		// 属于微信浏览器
		// lqm todo 暂时不需要验证
		if (userAgent.indexOf("MicroMessenger") > 0) {
			LOG.debug("来自微信浏览器！");
			return true;
		}

		// 判断为安卓平台，则直接通过，niukai
		if (userAgent != null && userAgent.indexOf("HttpClient") > -1) {
			return true;
		}

		if (RequestMethod.GET.toString().equals(request.getMethod())) {
			return true;
		}

		// String[] allowUrls = new String[] { "login", "index" }; //
		// 对登录本身的页面以及业务不拦截
		for (String s : allowUrls) {
			if (uri.indexOf(s) != -1) {
				return true;
			}
		}

		Cookie[] cookies = request.getCookies();

		String userId = CookieUtil.getValue("userId", cookies);
		String tokenId = CookieUtil.getValue("tokenId", cookies);

		// 可能正在进行初始化 添加用户
		// 加上userUid保证即使被盗访，也是无意义数据
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(tokenId)) {
			// response.sendRedirect(loginUrl);
			return false;
		}

		TokenResponse tokenResponse = tokenService.validateToken(
				Long.valueOf(userId), tokenId, userAgent);
		if (tokenResponse.getResponseStatus() == CommonErrorCode.OK.getCode())// 大多数情况下走这段逻辑，所以放在最前
		{
			return true;
		} else {
			// response.sendRedirect(loginUrl);
			return false;
		}
	}

}
