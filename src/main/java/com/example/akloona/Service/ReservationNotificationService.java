package com.example.akloona.Service;

import com.example.akloona.Database.Reservation;
import com.example.akloona.Database.ReservationRepo;
import com.example.akloona.Database.UserRepo;
import com.example.akloona.Database.User_;
import com.example.akloona.Util.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
public class ReservationNotificationService {

    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    public void sendUpcomingReservationNotifications() {
        LocalDateTime now = LocalDateTime.now();
        log.info("sendUpcomingReservationNotifications.Current time: " + now);

        LocalDateTime oneDayLater = now.plusDays(1);
        log.info("sendUpcomingReservationNotifications.Notification window: " + now + " to " + oneDayLater);

        List<Reservation> reservations = reservationRepo.findAll();
        log.info("Number of reservations fetched: " + reservations.size());

        for (Reservation reservation : reservations) {
            LocalDateTime reservationDateTime = DateTimeUtil.convertStringToDateTime(reservation.getDate(), reservation.getTime().trim());
            log.info("Checking reservation: ID=" + reservation.getUserID() + ", DateTime=" + reservationDateTime);
            if (reservationDateTime.isAfter(now) && reservationDateTime.isBefore(oneDayLater)) {

                log.info("INSIDE IF");
                User_ user = userRepo.findById(reservation.getUserID())
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + reservation.getUserID()));
                log.info("Befire email");
                String userEmail = user.getEmail();
                log.info("User email found: " + userEmail);

                String message = "Reminder: You have a reservation at " + reservation.getTime() + " on " + reservation.getDate();
                emailService.sendSimpleEmail(userEmail, "Upcoming Resturant Reservation", message);

            } else {
                log.info("Reservation ID=" + reservation.getUserID() + " is not within the notification window.");
            }
        }
    }
}