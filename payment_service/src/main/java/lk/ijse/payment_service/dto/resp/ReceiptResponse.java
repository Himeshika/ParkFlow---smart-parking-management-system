package lk.ijse.payment_service.dto.resp;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptResponse {

    private String receiptNumber;
    private String transactionId;
    private Double amount;
    private String cardHolderName;
    private String maskedCardNumber;
    private LocalDateTime paidAt;
}