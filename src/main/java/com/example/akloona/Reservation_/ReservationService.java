//package com.example.akloona.Reservation_;
//
//import com.example.akloona.Authentication.JwtService;
//import com.example.akloona.Database.*;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//
//@RequiredArgsConstructor
//@Slf4j
//@Service
//public class ReservationService {
//
//    private final ReservationRepo reservationRepo;
//    private final UserRepo userRepo;
//    private final TableStatusRepo tableStatusRepo;
//    private final JwtService jwtService;
//
//
//    @Transactional
//    public String createReservation(CreateReservationRequest request, HttpServletRequest httpServletRequest) {
//
//
//            String token = httpServletRequest.getHeader("Authorization").substring(7);
//            String username = jwtService.extractUsername(token);
//            User_ user = userRepo.findByUsername(username)
//                    .orElseThrow(() -> new IllegalStateException("Reservation not found"));
//            var reservation = Reservation.builder().date(request.getDate())
//                    .time(request.getTime())
//                    .tableStatus(tableStatusRepo.findById(request.getTableID())
//                            .orElseThrow(() -> new IllegalStateException("Table not found")))
//                    .user_(user)
//                    .guestCount(request.getGuestCount())
//                    .isConfirmed(true)
//                    .isCancelled(false)
//                    .build();
//
//
//            reservationRepo.save(reservation);
//            reservation.getTableStatus().setReserved(true); // Set table as reserved when reservation is created
//            user.addReservation(reservation);// Add reservation to user
//            userRepo.save(user); // Save user with reservation
//            return "Reservation created successfully\n your Reservation ID is " + reservation.getID();
//
//    }
//
//    @Transactional
//    public String updateReservation(UpdateReservationRequest request, HttpServletRequest httpServletRequest) {
//        var token = httpServletRequest.getHeader("Authorization").substring(7);
//        var username = jwtService.extractUsername(token);
//        var user = userRepo.findByUsername(username);
//        Reservation reservation = reservationRepo.findById(request.getReservationID())
//                .orElseThrow(() -> new IllegalStateException("Reservation not found"));
//
//        if (reservation.getUser_().getUsername().equals(username)) {
//            reservation.setDate(request.getDate());
//            reservation.setTime(request.getTime());
//            reservation.setTableStatus(tableStatusRepo.findById(request.getTableID())
//                    .orElseThrow(() -> new IllegalStateException("Table not found")));
//            reservation.setGuestCount(request.getGuestCount());
//            reservationRepo.save(reservation);
//            return "Reservation updated successfully";
//
//        }
//        else {
//            throw new IllegalStateException("Reservation not found");
//        }
//        //if the reservation is not found
//
//    }
//
//    @Transactional
//    public String cancelReservation(int id) {
//        Reservation reservation = reservationRepo.findById(id)
//                .orElseThrow(() -> new IllegalStateException("Reservation not found"));
//        reservation.setCancelled(true);
//        reservation.setConfirmed(false); // Set reservation as not confirmed when cancelled
//        reservation.getTableStatus().setReserved(false); // Set table as not reserved when reservation is cancelled
//        reservationRepo.save(reservation);
//        return "Reservation cancelled successfully";
//
//    }
//
//    @Transactional
//    public String deleteReservation(int id, HttpServletRequest httpServletRequest) {
//        if (!reservationRepo.existsById(id)) {
//            throw new IllegalStateException("Reservation not found");
//        }
//        reservationRepo.findById(id).get().getTableStatus().setReserved(false); // Set table as not reserved when reservation is deleted
//        reservationRepo.deleteById(id);
//
//        return "Reservation deleted successfully";
//    }
//
//
//    public Object getReservationsForLoggedInUser(HttpServletRequest httpServletRequest) {
//        String token = httpServletRequest.getHeader("Authorization").substring(7);
//        String username = jwtService.extractUsername(token);
//        User_ user = userRepo.findByUsername(username)
//                .orElseThrow(() -> new IllegalStateException("User not found"));
//        List<Reservation> reservations = user.getReservations();
//        return reservations;
//    }
//}
