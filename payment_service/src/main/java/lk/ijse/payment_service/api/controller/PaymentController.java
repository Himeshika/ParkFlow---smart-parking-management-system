package lk.ijse.payment_service.api.controller;

import lk.ijse.payment_service.dto.req.PaymentRequest;
import lk.ijse.payment_service.dto.resp.PaymentResponse;
import lk.ijse.payment_service.dto.resp.ReceiptResponse;
import lk.ijse.payment_service.service.PaymentService;
import lk.ijse.payment_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @RequestBody PaymentRequest request) {

        PaymentResponse response = paymentService.processPayment(request);

        // 201 regardless of SUCCESS/FAILED — a declined card is a valid
        // business outcome, not a client error.
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Payment processed", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Payment retrieved successfully", paymentService.getPayment(id))
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Payment list retrieved successfully", paymentService.getAllPayments())
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Payment history retrieved successfully", paymentService.getPaymentsByUser(userId))
        );
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<ApiResponse<ReceiptResponse>> getReceipt(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Receipt retrieved successfully", paymentService.getReceipt(id))
        );
    }

}