package com.example.akloona.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasks {

    @Autowired
    private ReservationNotificationService notificationService;

    @Scheduled(fixedRate = 3600000) // Run every hour
    //@Scheduled(fixedRate = 60000) // Run every minute for testing
    public void scheduleReservationNotifications() {
        notificationService.sendUpcomingReservationNotifications();
    }
}