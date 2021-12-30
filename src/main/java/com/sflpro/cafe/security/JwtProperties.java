package com.sflpro.cafe.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties
{

	private String expirationTimePattern;

	public long getExpirationTime()
	{
		return Long.parseLong(expirationTimePattern);
	}
}
