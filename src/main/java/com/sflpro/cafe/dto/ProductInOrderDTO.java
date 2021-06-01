package com.sflpro.cafe.dto;

import com.sflpro.cafe.enumeration.ProductInOrderStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class ProductInOrderDTO {

    private Long id;

    @NotNull
    @Positive
    private Long orderId;

    @NotNull
    @Positive
    private Long productId;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    private ProductInOrderStatus status;
}
