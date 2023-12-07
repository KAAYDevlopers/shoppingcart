package com.abw12.absolutefitness.shoppingcart.repository;

import com.abw12.absolutefitness.shoppingcart.entity.CartDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartDAO,String> {

    @Query("SELECT c FROM CartDAO c WHERE c.userId =:userId")
    Optional<CartDAO> getCartDetails(String userId);
}
