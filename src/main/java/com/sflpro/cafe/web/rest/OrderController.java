package com.sflpro.cafe.web.rest;

import com.sflpro.cafe.dto.OrderDTO;
import com.sflpro.cafe.dto.ProductInOrderDTO;
import com.sflpro.cafe.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/order")
public class OrderController
{

	private final OrderService orderService;

	public OrderController(OrderService orderService)
	{
		this.orderService = orderService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_WAITER')")
	public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO)
	{

		return new ResponseEntity<>(orderService.createOrder(orderDTO), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{orderId}")
	@PreAuthorize("hasRole('ROLE_WAITER')")
	public ResponseEntity<?> editOrder(@PathVariable Long orderId, @Valid @RequestBody OrderDTO orderDTO)
	{

		orderDTO.setId(orderId);

		return new ResponseEntity<>(orderService.editOrder(orderDTO), HttpStatus.OK);
	}

	@PutMapping(value = "/{orderId}/product")
	@PreAuthorize("hasRole('ROLE_WAITER')")
	public ResponseEntity<?> addProductToOrder(@PathVariable Long orderId, @RequestBody ProductInOrderDTO productInOrderDTO)
	{

		productInOrderDTO.setOrderId(orderId);

		return ResponseEntity.ok(orderService.addProductToOrder(productInOrderDTO));
	}

	@GetMapping(value = "/{orderId}/product/{productId}")
	@PreAuthorize("hasRole('ROLE_WAITER')")
	public ResponseEntity<?> getProductInOrder(@PathVariable Long orderId, @PathVariable Long productId)
	{

		Optional<ProductInOrderDTO> productInOrderOpt = orderService.getProductInOrder(orderId, productId);

		if (!productInOrderOpt.isPresent())
		{
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(productInOrderOpt.get());
	}

	@PutMapping(value = "/{orderId}/product/{productId}")
	@PreAuthorize("hasRole('ROLE_WAITER')")
	public ResponseEntity<?> editProductInOrder(@PathVariable Long orderId, @PathVariable Long productId,
		@RequestBody ProductInOrderDTO productInOrderDTO)
	{

		productInOrderDTO.setOrderId(orderId);
		productInOrderDTO.setProductId(productId);

		ProductInOrderDTO result = orderService.editProductInOrder(productInOrderDTO);

		return ResponseEntity.ok(result);
	}
}
