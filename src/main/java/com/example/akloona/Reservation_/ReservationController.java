//package com.example.akloona.Reservation_;
//
//
//import com.example.akloona.Service.MainPage;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/reservation")
//public class ReservationController {
//
//    @Autowired
//    private final ReservationService reservationService;
//
//    @Autowired
//    private final MainPage mainPage;
//
//    public ReservationController(ReservationService reservationService, MainPage mainPage) {
//        this.reservationService = reservationService;
//        this.mainPage = mainPage;
//    }
//
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
