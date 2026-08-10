package lk.ijse.payment_service.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    private Long userId;
    private String vehicleId;
    private Long parkingId;
    private Double amount;
    private String cardHolderName;
    private String cardNumber;   // raw input only — never saved to DB
    private String cvv;          // raw input only — never saved to DB
    private Integer expiryMonth;
    private Integer expiryYear;
}