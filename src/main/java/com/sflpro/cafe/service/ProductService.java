package com.sflpro.cafe.service;

import com.sflpro.cafe.dto.ProductDTO;
import com.sflpro.cafe.entity.Product;
import com.sflpro.cafe.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class ProductService
{

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository)
	{
		this.productRepository = productRepository;
	}

	@Transactional
	public ProductDTO createProduct(ProductDTO productDTO)
	{

		Product entity = new Product();

		entity.setName(productDTO.getName());
		entity.setPrice(productDTO.getPrice());

		productRepository.save(entity);

		return productDtoFromEntity(entity);
	}

	public List<ProductDTO> getAllProducts()
	{

		return StreamSupport.stream(productRepository.findAll().spliterator(), false).map(this::productDtoFromEntity).collect(Collectors.toList());
	}

	private ProductDTO productDtoFromEntity(Product entity)
	{
		ProductDTO productDTO = new ProductDTO();

		productDTO.setId(entity.getId());
		productDTO.setName(entity.getName());
		productDTO.setPrice(entity.getPrice());

		return productDTO;
	}
}
