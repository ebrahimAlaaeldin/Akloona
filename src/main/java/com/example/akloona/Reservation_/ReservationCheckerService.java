package com.example.akloona.Reservation_;

import com.example.akloona.Database.Reservation;
import com.example.akloona.Database.ReservationRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ReservationCheckerService {

    @Autowired
    private ReservationRepo reservationRepo;

    // Define the date format that matches your string (e.g., "2021-8-24")
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

    // @Scheduled(fixedRate = 30000)
    // public void checkReservations() {
    //     // Get the current date
    //     LocalDate now = LocalDate.now();

    //     // Fetch all reservations (or you can filter by date if your repository supports it)
    //     List<Reservation> reservations = reservationRepo.findAll();

    //     for (Reservation reservation : reservations) {
    //         // Parse the string date into a LocalDate object
    //         LocalDate reservationDate = LocalDate.parse(reservation.getDate(), formatter);

    //         // Check if the reservation date is after the current date
    //         if (now.isAfter(reservationDate)) {
    //             log.info("Reservation ID " + reservation.getID() + " has a past date: " + reservation.getDate());
    //             reservationRepo.delete(reservation);

    //             // Perform any action, like updating the status, notifying the user, etc.
    //         }
    //     }
    // }
}
