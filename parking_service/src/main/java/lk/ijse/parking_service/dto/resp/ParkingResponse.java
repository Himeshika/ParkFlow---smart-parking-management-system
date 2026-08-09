package lk.ijse.parking_service.dto.resp;

import lk.ijse.parking_service.entity.ParkingStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingResponse {

    private Long id;
    private String parkingName;
    private String location;
    private String address;
    private Integer totalSlots;
    private Integer availableSlots;
    private Double hourlyRate;
    private ParkingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String city;
    private String zone;
    private Double latitude;
    private Double longitude;
    private Long ownerId;
    private String description;
    private String contactNumber;
    private LocalTime openingTime;
    private LocalTime closingTime;
}