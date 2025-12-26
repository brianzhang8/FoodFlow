package com.brian.foodapp.cart.service;

import com.brian.foodapp.cart.dtos.CartDTO;
import com.brian.foodapp.response.Response;

public interface CartService {

    Response<?> addItemToCart(CartDTO cartDTO);

    Response<?> incrementItem(Long menuId);

    Response<?> decrementItem(Long menuId);

    Response<?> removeItem(Long cartItemId);

    Response<CartDTO> getShoppingCart();

    Response<?> clearShoppingCart();
}
