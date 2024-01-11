package com.abw12.absolutefitness.shoppingcart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRes {

    private String status;
    private String message;
    private String cartId;
}
