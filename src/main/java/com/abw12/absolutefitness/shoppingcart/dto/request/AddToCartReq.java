package com.abw12.absolutefitness.shoppingcart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartReq {

    private String cartId;
    private String userId;
    private String variantId;
    private Long requestQuantity;
}
