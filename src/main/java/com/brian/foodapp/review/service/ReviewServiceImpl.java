package com.brian.foodapp.review.service;


import com.brian.foodapp.auth_users.entity.User;
import com.brian.foodapp.auth_users.service.UserService;
import com.brian.foodapp.enums.OrderStatus;
import com.brian.foodapp.exceptions.BadRequestException;
import com.brian.foodapp.exceptions.NotFoundException;
import com.brian.foodapp.menu.entity.Menu;
import com.brian.foodapp.menu.repository.MenuRepository;
import com.brian.foodapp.order.entity.Order;
import com.brian.foodapp.order.repository.OrderItemRepository;
import com.brian.foodapp.order.repository.OrderRepository;
import com.brian.foodapp.response.Response;
import com.brian.foodapp.review.dtos.ReviewDTO;
import com.brian.foodapp.review.entity.Review;
import com.brian.foodapp.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public Response<ReviewDTO> createReview(ReviewDTO reviewDTO) {
        log.info("Inside createReview()");

        // get current user
        User user = userService.getCurrentLoggedInUser();

        // validate required fields
        if(reviewDTO.getOrderId() == null || reviewDTO.getOrderId() == null) {
            throw  new BadRequestException("Order id and menu item id are required");
        }

        // validate menu item exists
        Menu menu = menuRepository.findById(reviewDTO.getMenuId())
                .orElseThrow(() -> new NotFoundException("Menu item found"));

        // validate order exists and belongs to user
        Order order = orderRepository.findById(reviewDTO.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        // make sure the order belongs to you
        if(!order.getUser().getId().equals(user.getId())) {
            throw  new BadRequestException("This order doesn't belong to you");
        }

        // validate order status is delivered
        if(order.getOrderStatus() != OrderStatus.DELIVERED){
            throw  new BadRequestException("You can only review items from delivered orders");
        }

        // validate that menu item was part of this order
        boolean itemInOrder = orderItemRepository.existsByOrderIdAndMenuId(
                reviewDTO.getOrderId(), reviewDTO.getMenuId());

        if(!itemInOrder){
            throw new BadRequestException("This menu item was not part of the specified order");
        }

        // check if user already wrote review for the item
        if(reviewRepository.existsByUserIdAndMenuIdAndOrderId(
                user.getId(), reviewDTO.getMenuId(), reviewDTO.getOrderId())){
            throw new BadRequestException("You've already reviewed this item from this order");
        }

        // create and save review
        Review review = Review.builder()
                .user(user)
                .menu(menu)
                .orderId(reviewDTO.getOrderId())
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        // return response with review data
        ReviewDTO responseDto = modelMapper.map(savedReview, ReviewDTO.class);
        responseDto.setUserName(user.getName());
        responseDto.setMenuName(menu.getName());

        return Response.<ReviewDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Review added successfully")
                .data(responseDto)
                .build();
    }

    @Override
    public Response<List<ReviewDTO>> getReviewsForMenu(Long menuId) {
        log.info("Inside getReviewsForMenu()");

        List<Review> reviews = reviewRepository.findByMenuIdOrderByIdDesc(menuId);

        List<ReviewDTO> reviewDTOS = reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .toList();

        return Response.<List<ReviewDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Reviews retrieved successfully")
                .data(reviewDTOS)
                .build();
    }

    @Override
    public Response<Double> getAverageRating(Long menuId) {
        log.info("Inside getAverageRating()");

        Double averageRating = reviewRepository.calculateAverageRatingByMenuId(menuId);

        return Response.<Double>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Average rating retrieved successfully")
                .data(averageRating != null ? averageRating : 0.0)
                .build();
    }
}
