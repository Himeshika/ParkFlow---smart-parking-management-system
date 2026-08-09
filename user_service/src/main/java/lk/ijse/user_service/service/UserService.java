package lk.ijse.user_service.service;

import lk.ijse.user_service.dto.req.LoginRequest;
import lk.ijse.user_service.dto.req.UserSaveRequest;
import lk.ijse.user_service.dto.req.UserUpdateRequest;
import lk.ijse.user_service.dto.resp.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse saveUser(UserSaveRequest request);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    UserResponse getUser(Long id);

    List<UserResponse> getAllUsers();

    void deleteUser(Long id);

    UserResponse login(LoginRequest request);

}