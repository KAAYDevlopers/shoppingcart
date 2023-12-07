package com.abw12.absolutefitness.shoppingcart.service;

import com.abw12.absolutefitness.shoppingcart.constants.CommonConstants;
import com.abw12.absolutefitness.shoppingcart.dto.CartDTO;
import com.abw12.absolutefitness.shoppingcart.dto.CartItemDTO;
import com.abw12.absolutefitness.shoppingcart.entity.CartDAO;
import com.abw12.absolutefitness.shoppingcart.entity.CartItemDAO;
import com.abw12.absolutefitness.shoppingcart.mappers.CartItemMapper;
import com.abw12.absolutefitness.shoppingcart.mappers.CartMapper;
import com.abw12.absolutefitness.shoppingcart.repository.CartItemRepository;
import com.abw12.absolutefitness.shoppingcart.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartItemMapper cartItemMapper;

    @Transactional
    public String addToCart(CartDTO requestDTO) {
        logger.info("Inside addToCart method :: inserting cart data");
        if(requestDTO == null) throw new RuntimeException("invalid data provided");

        CartDAO cartData = cartMapper.dtoToEntity(requestDTO);
        CartDAO storedCartData = cartRepository.save(cartData);
        String cartId = storedCartData.getCartId();
        logger.debug("cart data stored in db : {} ",storedCartData);
        if(!StringUtils.isEmpty(storedCartData.getCartId())){
            List<CartItemDAO> cartItemData = requestDTO.getCartItem().stream()
                    .map(cartItemDTO -> {
                        CartItemDAO itemData = cartItemMapper.dtoToEntity(cartItemDTO);
                        itemData.setCartId(cartId);
                        return itemData;
                    })
                    .toList();
            List<CartItemDAO> cartItemStored = cartItemRepository.saveAll(cartItemData);
            logger.debug("cartItems list stored in db : {}  with cartId = {}",cartItemStored,cartId);
        }
        logger.info(CommonConstants.SUCCESS_MSG);
        return CommonConstants.SUCCESS_MSG;
    }

    public CartDTO getCartDetailsByUserId(String userId){
        logger.info("Inside getCartDetailsByUserId to fetch cart details");
        logger.debug("Fetching car details by userId :: {}",userId);
        CartDAO cartData = cartRepository.getCartDetails(userId).orElseThrow(() ->
                new RuntimeException(String.format("Cannot find cart details by userId : %s", userId)));
        List<CartItemDAO> cartItemsList = cartItemRepository.getCartItemDetails(cartData.getCartId()).orElseThrow(() ->
                new RuntimeException(String.format("Cannot find cart items details for cartId : %s", cartData.getCartId())));
        CartDTO response = cartMapper.entityToDto(cartData);
        List<CartItemDTO> itemList = cartItemsList.stream().map(item -> cartItemMapper.entityToDto(item)).toList();
        response.setCartItem(itemList);
        logger.debug("Fetched cart details by userId :: {} => {}",userId,response);
        return response;
    }
}
