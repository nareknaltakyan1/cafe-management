package com.sflpro.cafe.entity;

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
@javax.persistence.Table(name = "APP_TABLE")
public class Table extends BaseEntity {

    @Column(name = "CHAIRS")
    private Integer chairsAmount;

    @ManyToOne
    @JoinColumn(name = "WAITER_ID")
    private User waiter;

    @OneToMany(mappedBy = "table", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders;
}
