package com.sflpro.cafe.service;

import com.sflpro.cafe.dto.OrderDTO;
import com.sflpro.cafe.enumeration.OrderStatus;
import com.sflpro.cafe.dto.ProductInOrderDTO;
import com.sflpro.cafe.dto.TableDTO;
import com.sflpro.cafe.entity.Order;
import com.sflpro.cafe.entity.Product;
import com.sflpro.cafe.entity.ProductInOrder;
import com.sflpro.cafe.entity.Table;
import com.sflpro.cafe.exception.*;
import com.sflpro.cafe.repository.OrderRepository;
import com.sflpro.cafe.repository.ProductInOrderRepository;
import com.sflpro.cafe.repository.ProductRepository;
import com.sflpro.cafe.repository.TableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService
{

	private final TableRepository tableRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ProductInOrderRepository productInOrderRepository;

	public OrderService(TableRepository tableRepository, OrderRepository orderRepository, ProductRepository productRepository,
		ProductInOrderRepository productInOrderRepository)
	{
		this.tableRepository = tableRepository;
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.productInOrderRepository = productInOrderRepository;
	}

	@Transactional
	public OrderDTO createOrder(OrderDTO orderDTO)
	{

		TableDTO tableDTO = orderDTO.getTableDTO();
		if (tableDTO == null || tableDTO.getId() == null)
		{
			final String errorMsg = "Invalid table details";
			log.error(errorMsg);
			throw new GeneralBadRequestException(errorMsg);
		}

		Optional<Table> tableOpt = tableRepository.findById(tableDTO.getId());

		if (!tableOpt.isPresent())
		{
			final String errorMsg = "No table has been found for id: " + tableDTO.getId();
			log.error(errorMsg);
			throw new TableNotFoundException();
		}

		Table table = tableOpt.get();
		List<Order> openOrders = orderRepository.findOrdersByTableAndStatus(table, OrderStatus.OPEN);

		if (!openOrders.isEmpty())
		{
			final String errorMsg = "There already are open orders assigned to table: " + tableDTO.getId();
			log.error(errorMsg);
			throw new BusyTableException();
		}

		Order order = new Order();
		order.setStatus(orderDTO.getStatus());
		order.setTable(table);

		return orderEntityToDto(orderRepository.save(order));
	}

	public ProductInOrderDTO addProductToOrder(ProductInOrderDTO productInOrderDTO)
	{

		Long orderId = productInOrderDTO.getOrderId();
		Long productId = productInOrderDTO.getProductId();

		Optional<Order> orderOpt = orderRepository.findById(orderId);
		if (!orderOpt.isPresent())
		{
			final String errorMsg = "No order has been found for id: " + orderId;
			log.error(errorMsg);
			throw new OrderNotFoundException();
		}

		Optional<Product> productOpt = productRepository.findById(productId);
		if (!productOpt.isPresent())
		{
			final String errorMsg = "No product has been found for id: " + productId;
			log.error(errorMsg);
			throw new ProductNotFoundException();
		}

		Product product = productOpt.get();
		Order order = orderOpt.get();

		Optional<ProductInOrder> productInOrderOpt = productInOrderRepository.findByOrderAndProduct(order, product);
		if (productInOrderOpt.isPresent())
		{
			final String errorMsg = MessageFormat.format("Another Product in Order with orderId {0} and productId {1} already exists", orderId,
				productId);
			log.error(errorMsg);
			throw new DuplicateProductInOrderException();
		}

		ProductInOrder productInOrder = new ProductInOrder();
		productInOrder.setOrder(order);
		productInOrder.setProduct(product);
		productInOrder.setQuantity(productInOrderDTO.getQuantity());
		productInOrder.setStatus(productInOrderDTO.getStatus());

		order.getProductsInOrder().add(productInOrder);

		productInOrderRepository.save(productInOrder);
		orderRepository.save(order);

		return productInOrderEntityToDto(productInOrder);
	}

	public Optional<ProductInOrderDTO> getProductInOrder(Long orderId, Long productId)
	{

		return productInOrderRepository.findByOrderIdAndProductId(orderId, productId).map(this::productInOrderEntityToDto);
	}

	public ProductInOrderDTO editProductInOrder(ProductInOrderDTO productInOrderDTO)
	{

		Long orderId = productInOrderDTO.getOrderId();
		Long productId = productInOrderDTO.getProductId();

		Optional<ProductInOrder> productInOrderOpt = productInOrderRepository.findByOrderIdAndProductId(orderId, productId);

		if (!productInOrderOpt.isPresent())
		{
			final String errorMsg = MessageFormat.format("ProductInOrder not found by orderId: {0} and productId {1}", orderId, productId);
			log.error(errorMsg);
			throw new ProductInOrderNotFoundException();
		}

		ProductInOrder existing = productInOrderOpt.get();
		existing.setStatus(productInOrderDTO.getStatus());
		existing.setQuantity(productInOrderDTO.getQuantity());

		return productInOrderEntityToDto(productInOrderRepository.save(existing));
	}

	public OrderDTO editOrder(OrderDTO orderDTO)
	{

		Optional<Order> orderOpt = orderRepository.findById(orderDTO.getId());
		if (!orderOpt.isPresent())
		{
			final String errorMsg = "No order has been found for id: " + orderDTO.getId();
			log.error(errorMsg);
			throw new OrderNotFoundException();
		}

		Order order = orderOpt.get();
		order.setStatus(orderDTO.getStatus());

		return orderEntityToDto(orderRepository.save(order));
	}

	private ProductInOrderDTO productInOrderEntityToDto(ProductInOrder entity)
	{
		ProductInOrderDTO productInOrderDTO = new ProductInOrderDTO();

		productInOrderDTO.setId(entity.getId());
		productInOrderDTO.setOrderId(entity.getOrder().getId());
		productInOrderDTO.setProductId(entity.getProduct().getId());
		productInOrderDTO.setQuantity(entity.getQuantity());
		productInOrderDTO.setStatus(entity.getStatus());

		return productInOrderDTO;
	}

	private OrderDTO orderEntityToDto(Order entity)
	{

		OrderDTO orderDTO = new OrderDTO();

		orderDTO.setId(entity.getId());
		orderDTO.setStatus(entity.getStatus());
		orderDTO.setTableDTO(TableService.tableEntityToDto(entity.getTable()));

		return orderDTO;
	}
}
