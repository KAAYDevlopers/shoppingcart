package com.abw12.absolutefitness.shoppingcart.controller;

import com.abw12.absolutefitness.shoppingcart.dto.request.AddToCartReq;
import com.abw12.absolutefitness.shoppingcart.service.ShoppingCartService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);
    @Autowired
    private ShoppingCartService cartService;
    @PostMapping("/addToCart")
    private ResponseEntity<?> addToCart(@RequestBody AddToCartReq requestDTO){
        logger.info("Inside addToCart rest call: {} " , requestDTO);
        try{
            return new ResponseEntity<>(cartService.addToCart(requestDTO), HttpStatus.OK);
        }catch (Exception e){
            logger.error("Exception while add item into cart: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/getCartDataByUserId/{userId}")
    public ResponseEntity<?> getCartDataByUserId(@PathVariable String userId){
        logger.info("Inside getCartDataByUserId rest call: {} " , userId);
        try{
            if(StringUtils.isEmpty(userId)) throw new RuntimeException("userId cannot be null/empty...");
            return new ResponseEntity<>(cartService.getCartDetailsByUserId(userId),HttpStatus.OK);
        }catch (Exception e){
            logger.error("Exception while fetching cart data  by userId : {} => {}",userId,e.getMessage(),e.getCause());
            throw e;
        }
    }

    @DeleteMapping("/clearCartData/{cartId}")
    public ResponseEntity<?> clearCart(@PathVariable String cartId){
        logger.info("Inside clearCart method by cartId rest call: {} " , cartId);
        try{
            if(StringUtils.isEmpty(cartId)) throw new RuntimeException("cartId cannot be null/empty...");
            return new ResponseEntity<>(cartService.clearCartData(cartId),HttpStatus.OK);
        }catch (Exception e){
            logger.error("Exception while deleting cart data  by cartId : {} => {}",cartId,e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/removeCartItem/{cartItemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable String cartItemId){
        logger.info("Inside removeCartItem method by cartItemId rest call: {} " , cartItemId);
        try{
            if(StringUtils.isEmpty(cartItemId)) throw new RuntimeException("cartItemId cannot be null/empty...");
            return new ResponseEntity<>(cartService.removeCartItem(cartItemId),HttpStatus.OK);
        }catch (Exception e){
            logger.error("Exception while deleting cartItem by cartItemId : {} => {}",cartItemId,e.getMessage());
            throw e;
        }
    }

}
