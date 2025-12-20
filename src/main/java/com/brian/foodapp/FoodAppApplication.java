package com.brian.foodapp;

import com.brian.foodapp.email_notification.dtos.NotificationDTO;
import com.brian.foodapp.email_notification.service.NotificationService;
import com.brian.foodapp.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class FoodAppApplication {

    private final NotificationService notificationService;



    public static void main(String[] args) {
        SpringApplication.run(FoodAppApplication.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
            NotificationDTO notificationDTO = NotificationDTO
                    .builder()
                    .recipient("ericzhang64@yahoo.com")
                    .subject("hello brian")
                    .body("Thi is a test email")
                    .type(NotificationType.EMAIL)
                    .build();

            notificationService.sendEmail(notificationDTO);
        };
    }

}
