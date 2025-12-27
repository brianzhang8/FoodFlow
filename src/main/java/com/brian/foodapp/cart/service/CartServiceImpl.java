package com.brian.foodapp.cart.service;

import com.brian.foodapp.auth_users.entity.User;
import com.brian.foodapp.auth_users.service.UserService;
import com.brian.foodapp.cart.dtos.CartDTO;
import com.brian.foodapp.cart.entity.Cart;
import com.brian.foodapp.cart.entity.CartItem;
import com.brian.foodapp.cart.repository.CartItemRepository;
import com.brian.foodapp.cart.repository.CartRepository;
import com.brian.foodapp.exceptions.NotFoundException;
import com.brian.foodapp.menu.entity.Menu;
import com.brian.foodapp.menu.repository.MenuRepository;
import com.brian.foodapp.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuRepository menuRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;


    // Add a specified quantity of a Menu to the currently logged-in user's shopping cart.
    @Override
    public Response<?> addItemToCart(CartDTO cartDTO) {
        log.info("Inside addItemToCart()");

        Long menuId = cartDTO.getMenuId(); // the menu that will add to cart
        int quantity = cartDTO.getQuantity(); // how many will add to

        User user = userService.getCurrentLoggedInUser();

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu Item not found"));

        // find user's cart, if not find, create a cart for current user
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        // check if the item is already in the cart
        Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getMenu().getId().equals(menuId))
                .findFirst();

        // if present, increment item
        if(optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setSubtotal(cartItem.getPricePerUnit().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            cartItemRepository.save(cartItem);
        }else {
            // if not present
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .menu(menu)
                    .quantity(quantity)
                    .pricePerUnit(menu.getPrice())
                    .subtotal(menu.getPrice().multiply(BigDecimal.valueOf(quantity)))
                    .build();
            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        //cartItemRepository.save(cart); //not needed, it will auto save and persists in the cart table

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item added to cart successfully")
                .build();
    }

    @Override
    public Response<?> incrementItem(Long menuId) {
        log.info("Inside incrementItem()");

        User user = userService.getCurrentLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        // check if the item is already in the cart
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getMenu().getId().equals(menuId))
                .findFirst().orElseThrow(() -> new NotFoundException("Menu not found in cart"));

        int newQuantity = cartItem.getQuantity() + 1; // increment the item
        cartItem.setQuantity(newQuantity);
        cartItem.setSubtotal(cartItem.getPricePerUnit().multiply(BigDecimal.valueOf(newQuantity)));

        cartItemRepository.save(cartItem);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item quantity incremented successfully")
                .build();
    }

    @Override
    public Response<?> decrementItem(Long menuId) {
        log.info("Inside decrementItem()");

        User user = userService.getCurrentLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        // check if the item is already in the cart
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getMenu().getId().equals(menuId))
                .findFirst().orElseThrow(() -> new NotFoundException("Menu not found in cart"));

        int newQuantity = cartItem.getQuantity() - 1; // decrement the item

        if(newQuantity > 0) {
            cartItem.setQuantity(newQuantity);
            cartItem.setSubtotal(cartItem.getPricePerUnit().multiply(BigDecimal.valueOf(newQuantity)));
            cartItemRepository.save(cartItem);
        }else {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item quantity decremented successfully")
                .build();
    }

    @Override
    public Response<?> removeItem(Long cartItemId) {
        log.info("Inside removeItem()");

        User user = userService.getCurrentLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        if(!cart.getCartItems().contains(cartItem)) {
            throw new NotFoundException("Cart item does not belong to this user's cart");
        }
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item removed from cart successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response<CartDTO> getShoppingCart() {
        log.info("Inside getShoppingCart()");

        User user = userService.getCurrentLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart not found for user"));

        List<CartItem> cartItems = cart.getCartItems();

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        // calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        if(cartItems != null){ //add null check here
            for(CartItem cartItem : cartItems) {
                totalAmount = totalAmount.add(cartItem.getSubtotal());
            }
        }
        cartDTO.setTotalAmount(totalAmount);

        // remove the review from the response
        if(cartDTO.getCartItems() != null){
            cartDTO.getCartItems()
                    .forEach(item -> item.getMenu().setReviews(null));
        }

        return Response.<CartDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Shopping cart retrieved successfully")
                .data(cartDTO)
                .build();
    }

    @Override
    public Response<?> clearShoppingCart() {
        log.info("Inside clearShoppingCart()");

        User user = userService.getCurrentLoggedInUser();

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart not found for user"));

        // Delete cart items from the database first
        cartItemRepository.deleteAll(cart.getCartItems());

        // clear the cart's items collection
        cart.getCartItems().clear();

        // update the db
        cartRepository.save(cart);

        return Response.<CartDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Shopping cart cleared successfully")
                .build();
    }
}
