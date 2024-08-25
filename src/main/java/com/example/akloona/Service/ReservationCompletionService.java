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
import com.example.akloona.Enums.ReservationStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service

public class ReservationCompletionService {
    @Autowired
    private ReservationRepo reservationRepo;
    
    @Transactional
    public void updateCompletedReservations() {
        LocalDateTime now = LocalDateTime.now();
        log.info("updateCompletedReservations.Current time: " + now);

        List<Reservation> notifiedReservations = reservationRepo.findByStatus(ReservationStatus.NOTIFIED);
        log.info("Number of notified reservations fetched: " + notifiedReservations.size());

        for (Reservation reservation : notifiedReservations) {
            LocalDateTime reservationDateTime = DateTimeUtil.convertStringToDateTime(reservation.getDate(), reservation.getTime().trim());
            log.info("Checking reservation: ID=" + reservation.getUser().getID() + ", DateTime=" + reservationDateTime);

            if (reservationDateTime.isBefore(now)) {
                // If reservation time is in the past and it's still notified, mark it as completed
                reservation.setStatus(ReservationStatus.COMPLETED);
                reservationRepo.save(reservation);

                log.info("Reservation ID=" + reservation.getUser().getID() + " status updated to COMPLETED.");
            } else {
                log.info("Reservation ID=" + reservation.getUser().getID() + " is not yet completed.");
            }
        }
    }

}
