package com.brian.foodapp.cart.repository;

import com.brian.foodapp.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
