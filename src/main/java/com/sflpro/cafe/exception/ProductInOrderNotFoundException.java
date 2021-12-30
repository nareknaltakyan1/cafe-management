package com.sflpro.cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductInOrderNotFoundException extends RuntimeException
{

	public ProductInOrderNotFoundException()
	{
		super("ProductInOrder not found");
	}
}
