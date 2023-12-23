package com.abw12.absolutefitness.shoppingcart.service;

import com.abw12.absolutefitness.shoppingcart.constants.CommonConstants;
import com.abw12.absolutefitness.shoppingcart.dto.CartDTO;
import com.abw12.absolutefitness.shoppingcart.dto.CartItemDTO;
import com.abw12.absolutefitness.shoppingcart.dto.request.AddToCartReq;
import com.abw12.absolutefitness.shoppingcart.dto.request.InventoryValidationReq;
import com.abw12.absolutefitness.shoppingcart.dto.response.AddToCartRes;
import com.abw12.absolutefitness.shoppingcart.dto.response.InventoryValidationRes;
import com.abw12.absolutefitness.shoppingcart.entity.CartDAO;
import com.abw12.absolutefitness.shoppingcart.entity.CartItemDAO;
import com.abw12.absolutefitness.shoppingcart.gateway.interfaces.ProductCatalogInventoryClient;
import com.abw12.absolutefitness.shoppingcart.mappers.CartItemMapper;
import com.abw12.absolutefitness.shoppingcart.mappers.CartMapper;
import com.abw12.absolutefitness.shoppingcart.repository.CartItemRepository;
import com.abw12.absolutefitness.shoppingcart.repository.CartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ShoppingCartService {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private ProductCatalogInventoryClient inventoryClient;

    /**
     * @param requestDTO will contain userId,cartId and list of cartItem (usually this list will always contain one cartItem only) with its updated data
     * @return  success/failure msg
     */
    @Transactional
    public AddToCartRes addToCart(AddToCartReq requestDTO) {
        //todo decide on how to hanlde userId for guest and logged-in users
        logger.info("Inside addToCart method :: inserting cart data");
        if(requestDTO == null) throw new RuntimeException("invalid data provided");
        AddToCartRes response = new AddToCartRes();
        //check if variant is available or not(quantity level in inventory)
        //based on that return inStock or outOfStock response
        String variantId = requestDTO.getVariantId();
        InventoryValidationRes inventoryValidationRes;
        logger.info("validate inventory for variant with variantId:: {}",variantId);
        ResponseEntity<Map<String, Objects>> validationRes = inventoryClient.cartValidation(new InventoryValidationReq(variantId, requestDTO.getRequestQuantity()));
        if(validationRes.getStatusCode().is2xxSuccessful() && validationRes.hasBody()){
            inventoryValidationRes = objectMapper.convertValue(validationRes.getBody(), InventoryValidationRes.class);
            logger.info("response from validate product variant inventory call with variantId {} =>  {}",variantId,inventoryValidationRes);
            if(inventoryValidationRes.getStockStatus().equals(CommonConstants.OUT_OF_STOCK)) {
                logger.error("Product Variant with variantId {} is {}, cannot add to cart",variantId,CommonConstants.OUT_OF_STOCK);
                AddToCartRes errorRes = new AddToCartRes();
                errorRes.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
                errorRes.setMessage(String.format("Product Variant with variantId %s is %s for current %s requested quantity, cannot add to cart",
                        variantId,CommonConstants.OUT_OF_STOCK,requestDTO.getRequestQuantity()));
                return errorRes;
            }
        }else{
            throw new RuntimeException(String.format("Error while calling the product inventory checkStockStatus API :: %s",validationRes.getStatusCode()));
        }
        String cartId;
        //check if cart already exist - if yes add the cartItem to same cart else create a new cart and add to it
        if(!StringUtils.isEmpty(requestDTO.getCartId())) {
            logger.info("Fetching the existing cart details from DB with cartId :: {}",requestDTO.getCartId());
            CartDAO storedCartData = cartRepository.findById(requestDTO.getCartId())
                    .orElseThrow(() -> new RuntimeException(String.format("Error while fetching the Cart with cartId :: %s",requestDTO.getCartId())));
            cartId=storedCartData.getCartId();
            CartItemDAO cartItemToStore = generateCartItemDataToSaveDB(storedCartData,requestDTO);
            //store the cartItem into existing cart
            cartItemRepository.save(cartItemToStore);
            logger.info("New cart item data updated into existing cart with cartId :: {}",cartId);
        }
        else{
            logger.info("CartId is empty/null creating a new cart and adding cartItem data to it.");
            //create new cart to store in db
            CartDAO cartToSave= generateCartDataToSaveDB(requestDTO);
            CartDAO storedCartData = cartRepository.save(cartToSave);
            cartId=storedCartData.getCartId();
            //create new cartItem to store in db
            CartItemDAO cartItemToStore = generateCartItemDataToSaveDB(storedCartData,requestDTO);
            cartItemRepository.save(cartItemToStore);
            logger.info("Created new cart and added the cartItem into it with cartId :: {}",cartId);
        }
        response.setStatus(HttpStatus.OK.getReasonPhrase());
        response.setMessage(String.format("Successfully added the item into cart with cartId :: %s",cartId));
        return response;
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
        logger.info("Fetched cart details by userId :: {} => {}",userId,response);
        return response;
    }

    @Transactional
    public String clearCartData(String cartId){
        logger.info("Starting to clear the cart data with cartId.. :: {}",cartId);
        logger.info("Removing all the cartItem with cartId :: {}",cartId);
        Integer rowsDeleted = cartItemRepository.deleteCartItemByCartId(cartId).orElseThrow(() ->
                new RuntimeException(String.format("Error while deleting the cart item with carId :: %s", cartId)));
        logger.debug("Total {} number of cartItem deleted with cartId = {} ",rowsDeleted,cartId);
        if(rowsDeleted != 0){
            cartRepository.deleteById(cartId);
            logger.info("Deleted the cart data with cartId :: {}",cartId);
        }else {
            logger.info("Cart with cartId :: {} does not had any cartItem to delete",cartId);
        }
        return String.format("Successfully deleted the cart data with cartId %s",cartId);
    }

    public String removeCartItem(String cartItemId){
        logger.info("Removing the cartItem  with cartItemId :: {}",cartItemId);
        cartItemRepository.deleteById(cartItemId);
        logger.info("Removed the cartItem with cartItemId {}",cartItemId);
        return String.format("Successfully removed the cartItem with cartItemId :: %s",cartItemId);
    }

    private CartDAO generateCartDataToSaveDB(AddToCartReq requestDTO) {
        CartDAO cartDBdata = new CartDAO();
        if(!StringUtils.isEmpty(requestDTO.getCartId()))
            cartDBdata.setUserId(requestDTO.getCartId());
        if(!StringUtils.isEmpty(requestDTO.getUserId()))
            cartDBdata.setUserId(requestDTO.getUserId());

        cartDBdata.setCartCreatedAt(OffsetDateTime.now());
        cartDBdata.setCartModifiedAt(OffsetDateTime.now());
        return cartDBdata;
    }

    private CartItemDAO generateCartItemDataToSaveDB(CartDAO storedCartData, AddToCartReq requestDTO) {
        CartItemDAO cartItemDAO = new CartItemDAO();
        if(storedCartData!=null && !StringUtils.isEmpty(storedCartData.getCartId()))
            cartItemDAO.setCartId(storedCartData.getCartId());
        if(requestDTO!=null && requestDTO.getRequestQuantity()!=null)
            cartItemDAO.setCartItemQuantity(requestDTO.getRequestQuantity());
        if(requestDTO!=null && !StringUtils.isEmpty(requestDTO.getVariantId()))
            cartItemDAO.setVariantId(requestDTO.getVariantId());

        cartItemDAO.setCartItemCreatedAt(OffsetDateTime.now());
        cartItemDAO.setCartItemModifiedAt(OffsetDateTime.now());
        return cartItemDAO;
    }
}
