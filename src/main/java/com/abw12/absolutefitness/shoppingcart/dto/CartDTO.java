package com.abw12.absolutefitness.shoppingcart.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    @Id
    private String cartId;
    private String userId;
    private List<CartItemDTO> cartItem;
    private String cartCreatedAt;
    private String cartModifiedAt;
}
