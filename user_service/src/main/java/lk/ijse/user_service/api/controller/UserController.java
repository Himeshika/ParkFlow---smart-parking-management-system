package lk.ijse.user_service.api.controller;

import lk.ijse.user_service.dto.req.LoginRequest;
import lk.ijse.user_service.dto.req.UserSaveRequest;
import lk.ijse.user_service.dto.req.UserUpdateRequest;
import lk.ijse.user_service.dto.resp.UserResponse;
import lk.ijse.user_service.service.UserService;
import lk.ijse.user_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> saveUser(
            @RequestBody UserSaveRequest request) {

        UserResponse response = userService.saveUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        201,
                        "User created successfully",
                        response
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Login successful",
                        userService.login(request)
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "User retrieved successfully",
                        userService.getUser(id)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Users retrieved successfully",
                        userService.getAllUsers()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "User updated successfully",
                        userService.updateUser(id, request)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "User deleted successfully",
                        null
                )
        );
    }
}