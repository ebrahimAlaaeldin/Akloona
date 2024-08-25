package com.example.akloona.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ScheduledTasks {

    @Autowired
    private ReservationNotificationService notificationService;

    @Autowired
    private ReservationCompletionService completionService;

    @Scheduled(fixedRate = 60000) // Run every hour
    public void scheduleReservationNotifications() {
        try {
            notificationService.sendUpcomingReservationNotifications();
        } catch (Exception e) {
            log.error("Error occurred while sending reservation notifications: " + e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 60000) // Run every minute for testing
    public void scheduleReservationCompletion() {
        try {
            completionService.updateCompletedReservations();
        } catch (Exception e) {
            log.error("Error occurred while updating completed reservations: " + e.getMessage(), e);
        }
    }
}
