package lk.ijse.parking_service.dto.req;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingUpdateRequest {

    private String parkingName;
    private String location;
    private String address;
    private Integer totalSlots;
    private Double hourlyRate;
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