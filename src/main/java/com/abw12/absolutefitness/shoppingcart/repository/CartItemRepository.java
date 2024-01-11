package com.abw12.absolutefitness.shoppingcart.repository;

import com.abw12.absolutefitness.shoppingcart.entity.CartItemDAO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemDAO,String> {

    @Query("SELECT i FROM CartItemDAO i WHERE i.cartId =:cartId")
    Optional<List<CartItemDAO>> getCartItemDetails(String cartId);

    @Modifying
    @Query("DELETE FROM CartItemDAO i WHERE i.cartId =:cartId")
    Optional<Integer> deleteCartItemByCartId(String cartId);
    @Query("SELECT i FROM CartItemDAO i WHERE i.variantId =:variantId")
    Optional<CartItemDAO> findByVariantId(String variantId);

    @Modifying
    @Transactional
    @Query("UPDATE CartItemDAO c SET c.cartItemQuantity = :newCartItemQuantity, c.cartItemModifiedAt = :newCartItemModifiedAt WHERE c.cartItemId = :cartItemId")
    int updateCartItem(
            @Param("cartItemId") String cartItemId,
            @Param("newCartItemQuantity") Long newCartItemQuantity,
            @Param("newCartItemModifiedAt") OffsetDateTime newCartItemModifiedAt
    );
}
