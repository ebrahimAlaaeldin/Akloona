package com.example.akloona.Reservation_;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {


    private final ReservationService reservationService;

    @GetMapping("/get-available-tables")
    public ResponseEntity<?> getAvailableTables(AvailableTablesRequest request) {
        return ResponseEntity.ok(reservationService.getAvailableTables(request));
    }
}
//    @PostMapping("/create")
//    @PreAuthorize("hasAnyRole('CUSTOMER')")
//    public ResponseEntity<String> createReservation(CreateReservationRequest request,HttpServletRequest httpServletRequest) {
//
//        return ResponseEntity.ok(reservationService.createReservation(request,httpServletRequest));
//
//    }
//
//    @PutMapping("/update")
//    @PreAuthorize("hasAnyRole('CUSTOMER')")
//    public ResponseEntity<String> updateReservation(UpdateReservationRequest request,HttpServletRequest httpServletRequest) {
//        return ResponseEntity.ok(reservationService.updateReservation(request,httpServletRequest));
//    }
//
//    @PutMapping("/cancel/{id}")
//    @PreAuthorize("hasAnyRole('CUSTOMER')")
//    //Note check if the reservation is deleted in the reservation list of user or not
//    public ResponseEntity<String> cancelReservation(@PathVariable ("id") int id,HttpServletRequest httpServletRequest) {
//        return ResponseEntity.ok(reservationService.cancelReservation(id));
//
//
//
//    }
//
//    @DeleteMapping("/delete")
//    @PreAuthorize("hasAnyRole('CUSTOMER')")
//    public ResponseEntity<String> deleteReservation(@PathVariable ("id")int id,HttpServletRequest httpServletRequest) {
//
//        return ResponseEntity.ok(reservationService.deleteReservation(id,httpServletRequest));
//
//
//    }
//
//    @GetMapping("/get/{username}/{id}")
//    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
//    public ResponseEntity<?> getReservationsForLoggedInUser(HttpServletRequest httpServletRequest) {
//        return ResponseEntity.ok(reservationService.getReservationsForLoggedInUser(httpServletRequest));
//
//    }
//
//}
