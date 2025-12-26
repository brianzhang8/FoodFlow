package com.brian.foodapp.payment.service;

import com.brian.foodapp.payment.dtos.PaymentDTO;
import com.brian.foodapp.response.Response;
import java.util.List;

public interface PaymentService {

    Response<?> initializePayment(PaymentDTO paymentDTO);

    void updatePaymentForOrder(PaymentDTO paymentDTO);

    Response<List<PaymentDTO>> getAllPayments();

    Response<PaymentDTO> getPaymentById(Long paymentId);
}
