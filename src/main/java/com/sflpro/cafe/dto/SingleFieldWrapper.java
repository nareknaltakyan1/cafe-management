package com.sflpro.cafe.dto;

public class SingleFieldWrapper<T>
{

	private T value;

	public SingleFieldWrapper()
	{
	}

	public SingleFieldWrapper(T value)
	{
		this.value = value;
	}

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

	public static <T> SingleFieldWrapper<T> of(T obj)
	{
		return new SingleFieldWrapper<>(obj);
	}

	@Override
	public String toString()
	{
		return "SingleFieldWrapper{" + "value=" + getValue() + "}";
	}
}
