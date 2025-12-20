package com.brian.foodapp.email_notification.service;

import com.brian.foodapp.email_notification.dtos.NotificationDTO;

public interface NotificationService {

    void sendEmail(NotificationDTO notificationDTO);

}
