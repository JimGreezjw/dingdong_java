package com.dingdong.common.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringUtils extends HttpServlet {

	private static final long serialVersionUID = 4108552567099829807L;
	private static WebApplicationContext wac = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		wac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(config.getServletContext());
	}

	// public static Object getBean(String beanName) {
	// return wac.getBean(beanName);
	// }

	public static <T> T getBean(Class<T> clazz) {
		return getBean(clazz.getName());
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return (T) wac.getBean(beanName);
	}

	public static ApplicationContext getApplicationContext() {
		return wac;
	}

	public void publishEvent(ApplicationEvent applicationEvent) {
		wac.publishEvent(applicationEvent);
	}
}
