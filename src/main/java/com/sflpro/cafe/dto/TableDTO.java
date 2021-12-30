package com.sflpro.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDTO
{

	private Long id;

	@Positive
	private Integer chairsAmount;

	private UserDTO waiter;
}
