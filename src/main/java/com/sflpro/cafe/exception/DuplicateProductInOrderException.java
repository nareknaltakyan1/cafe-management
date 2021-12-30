package com.sflpro.cafe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateProductInOrderException extends RuntimeException
{

	public DuplicateProductInOrderException()
	{
		super("ProductInOrder with such orderId + productId couple is already added");
	}
}
