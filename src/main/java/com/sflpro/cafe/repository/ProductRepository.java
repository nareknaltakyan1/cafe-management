package com.sflpro.cafe.repository;

import com.sflpro.cafe.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long>
{
}
