package com.dingdong.core.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class EnvVariableInterceptor implements HandlerInterceptor {

	protected PropertySourcesPlaceholderConfigurer configurer;
	@SuppressWarnings("unused")
	private boolean debug;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		for (PropertySource<?> propertySource : configurer.getAppliedPropertySources()) {
			if (propertySource.getProperty("host") != null) {
				request.setAttribute("host", propertySource.getProperty("host"));
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

	public void setConfigurer(PropertySourcesPlaceholderConfigurer configurer) {
		this.configurer = configurer;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
