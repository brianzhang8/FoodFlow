package com.brian.foodapp.order.service;

import com.brian.foodapp.enums.OrderStatus;
import com.brian.foodapp.order.dtos.OrderDTO;
import com.brian.foodapp.order.dtos.OrderItemDTO;
import com.brian.foodapp.response.Response;
import org.springframework.data.domain.Page;
import java.util.List;

public interface OrderService {

    Response<?> placeOrderFromCart();

    Response<OrderDTO> getOrderById(Long id);

    Response<Page<OrderDTO>> getAllOrders(OrderStatus orderStatus, int page, int size);

    Response<List<OrderDTO>> getOrderOfUser();

    Response<OrderItemDTO> getOrderItemById(Long orderItemId);

    Response<OrderDTO> updateOrderStatus(OrderDTO orderDTO);

    Response<Long> countUniqueCustomers();

}
