package com.brian.foodapp.cart.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartDTO { // request+response dto

    private Long id;

    private List<CartItemDTO> cartItems;

    private Long menuId; // add item to cart

    private int quantity; // how many will add

    private BigDecimal totalAmount;
}
