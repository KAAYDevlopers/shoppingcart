package com.abw12.absolutefitness.shoppingcart.controller;

import com.abw12.absolutefitness.shoppingcart.dto.CartDTO;
import com.abw12.absolutefitness.shoppingcart.service.ShoppingCartService;
import jakarta.validation.Valid;
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
    private ResponseEntity<?> addToCart(@RequestBody @Valid CartDTO requestDTO){
        logger.info("Inside addToCart rest call: {} " , requestDTO);
        try{
            return new ResponseEntity<>(cartService.addToCart(requestDTO), HttpStatus.OK);
        }catch (Exception e){
            logger.error("Exception while add item into cart: {}", e.getMessage());
            return new ResponseEntity<>("Exception while Inserting/updating variant inventory data with variantId",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCartDataByUserId/{userId}")
    public ResponseEntity<?> getCartDataByUserId(@PathVariable String userId){
        logger.info("Inside getCartDataByUserId rest call: {} " , userId);
        try{
            if(StringUtils.isEmpty(userId)) throw new RuntimeException("userId cannot be null/empty...");
            return new ResponseEntity<>(cartService.getCartDetailsByUserId(userId),HttpStatus.OK);
        }catch (Exception e){
            logger.error("Exception while fetching cart data  by userId : {} => {}",userId,e.getMessage());
            return new ResponseEntity<>("Exception while fetching cart data by userId ",HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>("Exception while deleting cart data by cartId ",HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>("Exception while deleting cartItem  by cartItemId ",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
