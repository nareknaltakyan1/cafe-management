package com.sflpro.cafe.entity;

import com.sflpro.cafe.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Table(name = "APP_ORDER")
public class Order extends BaseEntity {

    @Column(name = "STATUS")
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "TABLE_ID")
    private Table table;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductInOrder> productsInOrder;
}
