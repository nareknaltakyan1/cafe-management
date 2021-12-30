package com.sflpro.cafe.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.cafe.util.CookieUtil;
import com.sflpro.cafe.security.JWTAuthenticationService;
import com.sflpro.cafe.dto.payload.LoginDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter
{

	private final JWTAuthenticationService jwtAuthenticationService;

	public JWTLoginFilter(String url, AuthenticationManager authManager, JWTAuthenticationService jwtAuthenticationService)
	{
		super(new AntPathRequestMatcher(url));
		this.jwtAuthenticationService = jwtAuthenticationService;
		this.setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException, IOException
	{

		LoginDTO loginDTO = new ObjectMapper().readValue(req.getInputStream(), LoginDTO.class);

		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, Authentication auth)
	{
		String token = jwtAuthenticationService.generateAuthHeader(auth);
		CookieUtil.setCookie(req, resp, JWTAuthenticationService.TOKEN_NAME, token);
	}
}
