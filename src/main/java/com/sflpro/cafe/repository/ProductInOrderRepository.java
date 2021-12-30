package com.sflpro.cafe.repository;

import com.sflpro.cafe.entity.Order;
import com.sflpro.cafe.entity.Product;
import com.sflpro.cafe.entity.ProductInOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductInOrderRepository extends CrudRepository<ProductInOrder, Long>
{

	Optional<ProductInOrder> findByOrderAndProduct(Order order, Product product);

	Optional<ProductInOrder> findByOrderIdAndProductId(Long orderId, Long productId);
}
