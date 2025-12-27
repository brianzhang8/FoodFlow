package com.brian.foodapp.auth_users.service;

import com.brian.foodapp.auth_users.dtos.UserDTO;
import com.brian.foodapp.auth_users.entity.User;
import com.brian.foodapp.auth_users.repository.UserRepository;
import com.brian.foodapp.aws.AWSS3Service;
import com.brian.foodapp.email_notification.dtos.NotificationDTO;
import com.brian.foodapp.email_notification.service.NotificationService;
import com.brian.foodapp.exceptions.NotFoundException;
import com.brian.foodapp.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;
    private final AWSS3Service awsS3Service;

    @Override
    public User getCurrentLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Response<List<UserDTO>> getAllUsers() {
        log.info("Inside getAllUsers()");

        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<UserDTO> userDTOS = modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());

        return Response.<List<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("All users retrieved successfully")
                .data(userDTOS)
                .build();
    }

    @Override
    public Response<UserDTO> getOwnAccountDetail() {
        log.info("Inside getOwnAccountDetail()");

        User user = getCurrentLoggedInUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Retrieving own account detail successfully")
                .data(userDTO)
                .build();
    }

    @Override
    public Response<?> updateOwnAccount(UserDTO userDTO) {
        log.info("Inside updateOwnAccount()");

        // fetch current logged in user
        User user = getCurrentLoggedInUser();

        String profileUrl = user.getProfileUrl();
        MultipartFile imageFile = userDTO.getImageFile();

        //check if new imageFile was provided, if provided, update
        if(imageFile != null && !imageFile.isEmpty()) {
            // delete old image in cloud if it exists
            if(profileUrl != null && !profileUrl.isEmpty()) {
                String keyName = profileUrl.substring(profileUrl.lastIndexOf('/') + 1);
                awsS3Service.deleteFile("profile/" + keyName);
                log.info("Deleted old profile image from s3");
            }
            // upload new image
            String imageName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            URL newImageUrl = awsS3Service.uploadFile("profile/" + imageName, imageFile);
            user.setProfileUrl(newImageUrl.toString());
        }
        // update user details
        if(userDTO.getName() != null) user.setName(userDTO.getName());
        if(userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
        if(userDTO.getAddress() != null) user.setAddress(userDTO.getAddress());
        if (StringUtils.hasText(userDTO.getPassword())) user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // save the user
        userRepository.save(user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account updated successfully")
                .build();
    }

    @Override
    public Response<?> deactivateOwnAccount() {
        log.info("Inside deactivateOwnAccount()");

        User user = getCurrentLoggedInUser();

        // deactivate the user
        user.setActive(false);
        userRepository.save(user);

        // send email after deactivation
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Account Deactivated")
                .body("Your account has been deactivated. If this was a mistake, please contact customer support.")
                .build();

        notificationService.sendEmail(notificationDTO);

        // return a success response
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account deactivated successfully")
                .build();
    }
}
