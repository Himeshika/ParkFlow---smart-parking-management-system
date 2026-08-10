package lk.ijse.payment_service.service;

import lk.ijse.payment_service.dto.req.PaymentRequest;
import lk.ijse.payment_service.dto.resp.PaymentResponse;
import lk.ijse.payment_service.dto.resp.ReceiptResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request);

    PaymentResponse getPayment(Long id);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByUser(Long userId);

    ReceiptResponse getReceipt(Long id);

}