package com.brian.foodapp.auth_users.service;

import com.brian.foodapp.auth_users.dtos.LoginRequest;
import com.brian.foodapp.auth_users.dtos.LoginResponse;
import com.brian.foodapp.auth_users.dtos.RegistrationRequest;
import com.brian.foodapp.response.Response;

public interface AuthService {

    Response<?> registerUser(RegistrationRequest registrationRequest);

    Response<LoginResponse> login(LoginRequest loginRequest);
}
