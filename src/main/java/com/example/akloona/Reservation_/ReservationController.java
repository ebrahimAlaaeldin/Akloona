package com.example.akloona.Reservation_;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {


    private final ReservationService reservationService;

    @GetMapping("/get-available-tables")
    public ResponseEntity<?> getAvailableTables(@RequestBody AvailableTablesRequest request) {
        return ResponseEntity.ok(reservationService.getAvailableTables(request));
    }

    @PostMapping("/create")
    public ResponseEntity<String> makeReservation(@RequestBody CreateReservationRequest request, HttpServletRequest httpServletRequest) {

        return ResponseEntity.ok(reservationService.makeReservation(request, httpServletRequest));

    }

    @GetMapping("/get-User-Reservations")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    public ResponseEntity<?> getReservationsForLoggedInUser(@RequestBody UserReservationsRequest Name) {
        return ResponseEntity.ok(reservationService.getUserReservationsByManager(Name));
    }

    @GetMapping("/get-My-Reservations")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<?> getReservationsForLoggedInUser(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(reservationService.getMyReservations(httpServletRequest));
    }

    @PostMapping("/make-reservation-by-manager")
    @PreAuthorize("hasAnyRole('MANAGER','STAFF')")
    public ResponseEntity<String> makeReservationByManager(@RequestBody CreateReservationRequest request,HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(reservationService.makeReservationByManager(request,httpServletRequest));
    }

    @DeleteMapping("/cancel-reservation")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<String> cancelReservation(@RequestBody CancelReservationRequest request,HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(reservationService.cancelReservation(request,httpServletRequest).toString());
    }
}

