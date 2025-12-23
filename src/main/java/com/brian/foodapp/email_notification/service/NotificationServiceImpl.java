package com.brian.foodapp.email_notification.service;

import com.brian.foodapp.email_notification.dtos.NotificationDTO;
import com.brian.foodapp.email_notification.entity.Notification;
import com.brian.foodapp.email_notification.repository.NotificationRepository;
import com.brian.foodapp.enums.NotificationType;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;

    private final NotificationRepository notificationRepository;

    @Override
    @Async
    public void sendEmail(NotificationDTO notificationDTO) {
        log.info("Inside sendEmail()"); //tell us this function is invoking
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_RELATED,
                    StandardCharsets.UTF_8.name());

            mimeMessageHelper.setTo(notificationDTO.getRecipient());
            mimeMessageHelper.setSubject(notificationDTO.getSubject());
            mimeMessageHelper.setText(notificationDTO.getBody(), notificationDTO.isHtml());

            javaMailSender.send(mimeMessage);

            //Save to database
            Notification notificationToSave = Notification.builder()
                    .recipient(notificationDTO.getRecipient())
                    .subject(notificationDTO.getSubject())
                    .body(notificationDTO.getBody())
                    .type(NotificationType.EMAIL)
                    .isHtml(notificationDTO.isHtml())
                    .build();

            notificationRepository.save(notificationToSave);
            log.info("Notification saved to table successfully");

        }catch (Exception e){
            log.error("Failed to send email", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
