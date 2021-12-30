package com.sflpro.cafe.web.rest;

import com.sflpro.cafe.dto.ProductDTO;
import com.sflpro.cafe.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/product")
public class ProductController
{

	private final ProductService productService;

	public ProductController(ProductService productService)
	{
		this.productService = productService;
	}

	@PostMapping
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO)
	{

		return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_MANAGER')")
	public ResponseEntity<?> getAllProducts()
	{

		return ResponseEntity.ok(productService.getAllProducts());
	}
}
