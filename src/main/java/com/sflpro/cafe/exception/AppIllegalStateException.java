package com.sflpro.cafe.exception;

public class AppIllegalStateException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public AppIllegalStateException(String message)
	{
		super(message);
	}
}
