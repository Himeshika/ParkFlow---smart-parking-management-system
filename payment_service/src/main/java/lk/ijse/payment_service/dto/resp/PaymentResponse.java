package lk.ijse.payment_service.dto.resp;

import lk.ijse.payment_service.entity.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Long userId;
    private String vehicleId;
    private Long parkingId;
    private Double amount;
    private String cardHolderName;
    private String maskedCardNumber;
    private PaymentStatus status;
    private String transactionId;
    private String receiptNumber;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}