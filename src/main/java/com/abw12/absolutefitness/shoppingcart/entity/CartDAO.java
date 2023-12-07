package com.abw12.absolutefitness.shoppingcart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart",schema = "shoppingcart")
public class CartDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_id")
    private String cartId;
    @Column(name = "user_id")
    private String userId;
//    @Column(name = "cart_total")
//    private BigDecimal cartTotal;
    @Column(name = "cart_created_at")
    private OffsetDateTime cartCreatedAt;
    @Column(name = "cart_modified_at")
    private OffsetDateTime cartModifiedAt;
}
