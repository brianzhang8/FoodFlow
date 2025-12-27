package com.brian.foodapp.auth_users.service;

import com.brian.foodapp.auth_users.dtos.UserDTO;
import com.brian.foodapp.auth_users.entity.User;
import com.brian.foodapp.response.Response;
import java.util.List;

public interface UserService {

    User getCurrentLoggedInUser();

    Response<List<UserDTO>> getAllUsers();

    Response<UserDTO> getOwnAccountDetail();

    Response<?> updateOwnAccount(UserDTO userDTO);

    Response<?> deactivateOwnAccount();
}
