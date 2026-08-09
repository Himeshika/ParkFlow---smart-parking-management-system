package lk.ijse.parking_service.api.controller;

import lk.ijse.parking_service.dto.req.ParkingSaveRequest;
import lk.ijse.parking_service.dto.req.ParkingUpdateRequest;
import lk.ijse.parking_service.dto.resp.ParkingResponse;
import lk.ijse.parking_service.service.ParkingService;
import lk.ijse.parking_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/parking")
@RequiredArgsConstructor
@CrossOrigin
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping
    public ResponseEntity<ApiResponse<ParkingResponse>> saveParking(
            @RequestBody ParkingSaveRequest request) {

        ParkingResponse response = parkingService.saveParking(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        201,
                        "Parking created successfully",
                        response
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingResponse>> getParking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Parking retrieved successfully",
                        parkingService.getParking(id)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ParkingResponse>>> getAllParking() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Parking list retrieved successfully",
                        parkingService.getAllParking()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ParkingResponse>> updateParking(
            @PathVariable Long id,
            @RequestBody ParkingUpdateRequest request) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Parking updated successfully",
                        parkingService.updateParking(id, request)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteParking(
            @PathVariable Long id) {

        parkingService.deleteParking(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Parking deleted successfully",
                        null
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ParkingResponse>>> filterByLocation(
            @RequestParam String location) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Parking filtered successfully",
                        parkingService.filterByLocation(location)
                )
        );
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ApiResponse<ParkingResponse>> reserveParking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Parking reserved successfully",
                        parkingService.reserveParking(id)
                )
        );
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<ApiResponse<ParkingResponse>> releaseParking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Parking released successfully",
                        parkingService.releaseParking(id)
                )
        );
    }

}