package com.abw12.absolutefitness.shoppingcart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cartitem" ,schema = "shoppingcart")
public class CartItemDAO  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_item_id")
    private String cartItemId;
    @Column(name = "cart_id")
    private String cartId;
    @Column(name = "variant_id")
    private String variantId;
    @Column(name = "cart_item_quantity")
    private Long cartItemQuantity;
    @Column(name="cart_item_created_at")
    private OffsetDateTime cartItemCreatedAt;
    @Column(name="cart_item_modified_at")
    private OffsetDateTime cartItemModifiedAt;
}
