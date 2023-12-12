package com.abw12.absolutefitness.shoppingcart.repository;

import com.abw12.absolutefitness.shoppingcart.entity.CartItemDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemDAO,String> {

    @Query("SELECT i FROM CartItemDAO i WHERE i.cartId =:cartId")
    Optional<List<CartItemDAO>> getCartItemDetails(String cartId);

    @Query("DELETE FROM CartItemDAO i WHERE i.cartId =:cartId")
    Optional<Long> deleteCartItemByCartId(String cartId);
}
