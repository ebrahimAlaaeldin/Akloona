package com.example.akloona.Controller;

import com.example.akloona.Database.Reservation;
import com.example.akloona.Service.ReservationService;
import com.example.akloona.Service.MainPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private final ReservationService reservationService;

    @Autowired
    private final MainPage mainPage;

    public ReservationController(ReservationService reservationService, MainPage mainPage) {
        this.reservationService = reservationService;
        this.mainPage = mainPage;
    }

    @PostMapping("/create/{username}")
    public ResponseEntity<String> createReservation(@PathVariable("username") String username,
                                                    @RequestParam("date") String date,
                                                    @RequestParam("time") String time,
                                                    @RequestParam("tableID") int tableID,
                                                    @RequestParam("guestCount") int guestCount) {
        if (!mainPage.isLoggedIn() || !mainPage.getActiveUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized or user not logged in");
        }
        try {
            int userID = mainPage.getActiveUser().getId(); // Get user ID from active user
            reservationService.createReservation(date, time, tableID, userID, guestCount);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reservation Created Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to Create Reservation: " + e.getMessage());
        }
    }

    @PutMapping("/update/{username}/{id}")
    public ResponseEntity<String> updateReservation(@PathVariable("username") String username,
                                                    @PathVariable("id") int id,
                                                    @RequestParam("date") String date,
                                                    @RequestParam("time") String time,
                                                    @RequestParam("tableID") int tableID,
                                                    @RequestParam("guestCount") int guestCount) {
        if (!mainPage.isLoggedIn() || !mainPage.getActiveUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized or user not logged in");
        }
        try {
            reservationService.updateReservation(id, date, time, tableID, guestCount);
            return ResponseEntity.status(HttpStatus.OK).body("Reservation Updated Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to Update Reservation: " + e.getMessage());
        }
    }

    @PutMapping("/cancel/{username}/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable("username") String username,
                                                    @PathVariable("id") int id) {
        if (!mainPage.isLoggedIn() || !mainPage.getActiveUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized or user not logged in");
        }
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.status(HttpStatus.OK).body("Reservation Cancelled Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to Cancel Reservation: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{username}/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable("username") String username,
                                                    @PathVariable("id") int id) {
        if (!mainPage.isLoggedIn() || !mainPage.getActiveUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized or user not logged in");
        }
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Reservation Deleted Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to Delete Reservation: " + e.getMessage());
        }
    }

    @GetMapping("/get/{username}/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable("username") String username,
                                                @PathVariable("id") int id) {
        if (!mainPage.isLoggedIn() || !mainPage.getActiveUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized or user not logged in");
        }

        try {
            Reservation reservation = reservationService.getReservationById(id);
            return ResponseEntity.ok(reservation);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
