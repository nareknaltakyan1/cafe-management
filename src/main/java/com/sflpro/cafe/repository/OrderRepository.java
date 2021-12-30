package com.sflpro.cafe.repository;

import com.sflpro.cafe.enumeration.OrderStatus;
import com.sflpro.cafe.entity.Order;
import com.sflpro.cafe.entity.Table;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long>
{

	List<Order> findOrdersByTableAndStatus(Table table, OrderStatus status);
}
