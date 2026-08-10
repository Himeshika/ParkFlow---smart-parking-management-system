package lk.ijse.payment_service.service.impl;

import lk.ijse.payment_service.dto.req.PaymentRequest;
import lk.ijse.payment_service.dto.resp.PaymentResponse;
import lk.ijse.payment_service.dto.resp.ReceiptResponse;
import lk.ijse.payment_service.entity.Payment;
import lk.ijse.payment_service.entity.PaymentStatus;
import lk.ijse.payment_service.exception.BadRequestException;
import lk.ijse.payment_service.exception.PaymentNotFoundException;
import lk.ijse.payment_service.repository.PaymentRepository;
import lk.ijse.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        validateRequest(request);
        verifyReferenceExists(
                "http://user-service/user_service/api/v1/users/" + request.getUserId(),
                "User");
        verifyReferenceExists(
                "http://parking-service/parking_service/api/v1/parking/" + request.getParkingId(),
                "Parking session");
        verifyReferenceExists(
                "http://vehicle-service/api/v1/vehicles/" + request.getVehicleId(),
                "Vehicle");

        String maskedCardNumber = maskCardNumber(request.getCardNumber());
        boolean declined = request.getCardNumber().endsWith("0000");

        Payment payment = Payment.builder()
                .userId(request.getUserId())
                .vehicleId(request.getVehicleId())
                .parkingId(request.getParkingId())
                .amount(request.getAmount())
                .cardHolderName(request.getCardHolderName())
                .maskedCardNumber(maskedCardNumber)
                .expiryMonth(request.getExpiryMonth())
                .expiryYear(request.getExpiryYear())
                .transactionId(UUID.randomUUID().toString())
                .build();

        if (declined) {
            payment.setStatus(PaymentStatus.FAILED);
        } else {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
            payment.setReceiptNumber("RCPT-" + System.currentTimeMillis());
        }

        return mapToResponse(paymentRepository.save(payment));
    }

    private void verifyReferenceExists(String url, String label) {
        try {
            restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new BadRequestException(label + " not found — cannot process payment");
        } catch (RestClientException ex) {
            throw new BadRequestException(label + " service is unavailable — cannot verify reference");
        }
    }

    @Override
    public PaymentResponse getPayment(Long id) {
        return mapToResponse(findPayment(id));
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByUser(Long userId) {
        return paymentRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public ReceiptResponse getReceipt(Long id) {
        Payment payment = findPayment(id);

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BadRequestException("Receipt not available for a transaction that was not successful");
        }

        return ReceiptResponse.builder()
                .receiptNumber(payment.getReceiptNumber())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .cardHolderName(payment.getCardHolderName())
                .maskedCardNumber(payment.getMaskedCardNumber())
                .paidAt(payment.getPaidAt())
                .build();
    }

    private Payment findPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
    }

    private void validateRequest(PaymentRequest request) {
        if (request.getUserId() == null || request.getUserId() <= 0)
            throw new BadRequestException("Valid user ID is required");
        if (request.getVehicleId() == null || request.getVehicleId().trim().isEmpty())
            throw new BadRequestException("Valid vehicle ID is required");
        if (request.getParkingId() == null || request.getParkingId() <= 0)
            throw new BadRequestException("Valid parking session ID is required");
        if (request.getAmount() == null || request.getAmount() <= 0)
            throw new BadRequestException("Amount must be greater than zero");
        if (request.getCardHolderName() == null || request.getCardHolderName().trim().isEmpty())
            throw new BadRequestException("Card holder name is required");
        if (request.getCardNumber() == null || !request.getCardNumber().matches("\\d{16}"))
            throw new BadRequestException("Card number must be exactly 16 digits");
        if (request.getCvv() == null || !request.getCvv().matches("\\d{3}"))
            throw new BadRequestException("CVV must be exactly 3 digits");
        if (request.getExpiryMonth() == null || request.getExpiryMonth() < 1 || request.getExpiryMonth() > 12)
            throw new BadRequestException("Expiry month must be between 1 and 12");
        if (request.getExpiryYear() == null)
            throw new BadRequestException("Expiry year is required");

        LocalDateTime now = LocalDateTime.now();
        if (request.getExpiryYear() < now.getYear()
                || (request.getExpiryYear() == now.getYear() && request.getExpiryMonth() < now.getMonthValue())) {
            throw new BadRequestException("Card has expired");
        }
    }

    private String maskCardNumber(String cardNumber) {
        String last4 = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + last4;
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .userId(payment.getUserId())
                .vehicleId(payment.getVehicleId())
                .parkingId(payment.getParkingId())
                .amount(payment.getAmount())
                .cardHolderName(payment.getCardHolderName())
                .maskedCardNumber(payment.getMaskedCardNumber())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .receiptNumber(payment.getReceiptNumber())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}