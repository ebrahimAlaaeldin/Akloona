package com.example.akloona.Service;

import com.example.akloona.Database.Reservation;
import com.example.akloona.Database.ReservationRepo;
import com.example.akloona.Database.UserRepo;
import com.example.akloona.Database.User_;
import com.example.akloona.Util.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Transactional
    public void sendUpcomingReservationNotifications() {
        LocalDateTime now = LocalDateTime.now();
        log.info("sendUpcomingReservationNotifications.Current time: " + now);

        LocalDateTime oneDayLater = now.plusDays(1);
        log.info("sendUpcomingReservationNotifications.Notification window: " + now + " to " + oneDayLater);

        List<Reservation> reservations = reservationRepo.findAll();
        log.info("Number of reservations fetched: " + reservations.size());

        for (Reservation reservation : reservations) {
            LocalDateTime reservationDateTime = DateTimeUtil.convertStringToDateTime(reservation.getDate(), reservation.getTime().trim());
            log.info("Checking reservation: ID=" + reservation.getUser().getID() + ", DateTime=" + reservationDateTime);
            if (reservationDateTime.isAfter(now) && reservationDateTime.isBefore(oneDayLater)) {

                log.info("INSIDE IF");

                try{
                    // Access the User_ entity directly through the reservation entity
                    User_ user = reservation.getUser();
                    log.info("User found with ID: " + user.getID());

                    String userEmail = user.getEmail();
                    log.info("User email found: " + userEmail);
                    
                    String message = "Reminder: You have a reservation at " + reservation.getTime() + " on " + reservation.getDate();
                    emailService.sendSimpleEmail(userEmail, "Upcoming Restaurant Reservation", message);
                    log.info("Sent email to: " + userEmail);
                }catch (Exception e)
                {
                    log.info("Eror: " + e.getMessage());
                }
                
            } else {
                log.info("Reservation ID=" + reservation.getUser().getID()+ " is not within the notification window.");
            }
        }
    }
}