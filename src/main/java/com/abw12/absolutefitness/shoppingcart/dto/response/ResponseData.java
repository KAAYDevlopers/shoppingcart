package com.abw12.absolutefitness.shoppingcart.dto.response;

import com.abw12.absolutefitness.shoppingcart.dto.CartDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData {

    private String success;
    private String message;
    private CartDTO cartData;
}
