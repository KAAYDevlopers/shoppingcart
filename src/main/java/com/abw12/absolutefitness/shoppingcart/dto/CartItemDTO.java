package com.abw12.absolutefitness.shoppingcart.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    @Id
    private String cartItemId;
    private String productId;
    private Long cartItemQuantity;
    private String cartItemCreatedAt;
    private String cartItemModifiedAt;
}
