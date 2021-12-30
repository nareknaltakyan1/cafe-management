package com.sflpro.cafe.dto;

import com.sflpro.cafe.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO
{

	private Long id;

	@NotNull
	private OrderStatus status;

	private TableDTO tableDTO;
}
