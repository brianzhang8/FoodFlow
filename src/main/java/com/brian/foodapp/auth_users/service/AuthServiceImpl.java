package com.brian.foodapp.auth_users.service;

import com.brian.foodapp.auth_users.dtos.LoginRequest;
import com.brian.foodapp.auth_users.dtos.LoginResponse;
import com.brian.foodapp.auth_users.dtos.RegistrationRequest;
import com.brian.foodapp.auth_users.entity.User;
import com.brian.foodapp.auth_users.repository.UserRepository;
import com.brian.foodapp.exceptions.BadRequestException;
import com.brian.foodapp.exceptions.NotFoundException;
import com.brian.foodapp.response.Response;
import com.brian.foodapp.role.entity.Role;
import com.brian.foodapp.role.repository.RoleRepository;
import com.brian.foodapp.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public Response<?> registerUser(RegistrationRequest registrationRequest) {
        log.info("Inside registerUser()");

        // register but the email is taken
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        // find all roles from the request
        List<Role> userRoles;

        // if provide roles in registrationRequest
        if(registrationRequest.getRoles() != null && !registrationRequest.getRoles().isEmpty()) {
            userRoles = registrationRequest.getRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName.toUpperCase())
                            .orElseThrow(() -> new NotFoundException("Role with name: " + roleName + "Not found")))
                    .toList();
        }else {
            // if no roles provided, default to customer
            Role defaultRole = roleRepository.findByName("CUSTOMER")
                    .orElseThrow(() -> new NotFoundException("Default CUSTOMER role not found"));
            userRoles = List.of(defaultRole);
        }

        // build the user object
        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .phoneNumber(registrationRequest.getPhoneNumber())
                .address(registrationRequest.getAddress())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(userRoles)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        // save to repo
        userRepository.save(user);

        log.info("User registered successfully");

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("User registered successfully")
                .build();
    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {
        log.info("Inside login()");

        // if not found the user
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Invalid Email"));

        // if user not active
        if(!user.isActive()){
            throw new NotFoundException("Account not active, Please contact the customer support");
        }

        // verify the password
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new BadRequestException("Invalid Password");
        }

        // generate the token
        String token = jwtUtils.generateToken(user.getEmail());

        // extract roles names as a list
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRoles(roleNames);

        return Response.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Successfully")
                .data(loginResponse)
                .build();
    }
}
