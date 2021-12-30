package com.sflpro.cafe.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.stream.Stream;

public final class SecurityUtils
{

	private SecurityUtils()
	{
	}

	public static Optional<String> getCurrentUserLogin()
	{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
	}

	private static String extractPrincipal(Authentication authentication)
	{
		if (authentication == null)
		{
			return null;
		}
		else
			if (authentication.getPrincipal() instanceof UserDetails)
			{
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				return springSecurityUser.getUsername();
			}
			else
				if (authentication.getPrincipal() instanceof String)
				{
					return (String) authentication.getPrincipal();
				}
		return null;
	}

	private static Stream<String> getAuthorities(Authentication authentication)
	{
		return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
	}
}