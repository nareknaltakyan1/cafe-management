package com.sflpro.cafe.entity;

import com.sflpro.cafe.enumeration.ProductInOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Table(name = "PRODUCT_IN_ORDER")
public class ProductInOrder extends BaseEntity
{

	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;

	@Column(name = "QUANTITY")
	private Integer quantity;

	@Column(name = "STATUS")
	@Enumerated(EnumType.ORDINAL)
	private ProductInOrderStatus status;
}
