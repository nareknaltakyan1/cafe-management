package com.sflpro.cafe.util;

import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class CookieUtil
{

	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value)
	{
		Cookie cookie = WebUtils.getCookie(request, name);

		if (cookie != null)
		{
			cookie.setValue(value);
		}
		else
		{
			cookie = new Cookie(name, value);
		}

		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public static String getValue(ServletRequest servletRequest, String name)
	{
		Cookie cookie = WebUtils.getCookie(((HttpServletRequest) servletRequest), name);
		return cookie != null ? cookie.getValue() : null;
	}
}