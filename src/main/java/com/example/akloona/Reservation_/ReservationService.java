package com.example.akloona.Reservation_;

import com.example.akloona.Authentication.JwtService;
import com.example.akloona.Database.*;

import com.example.akloona.Dtos.ReservationDto;
import com.example.akloona.Dtos.TableStatusDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Service
public class ReservationService {

    private final ReservationRepo reservationRepo;
    private final UserRepo userRepo;
    private final TableStatusRepo tableStatusRepo;
    private final JwtService jwtService;
    private final RestaurantRepo restaurantRepo;


    @Transactional
    public String makeReservation(CreateReservationRequest request, HttpServletRequest httpServletRequest) {
        Restaurant restaurant = restaurantRepo.findByName(request.getRestaurantName())
                .orElseThrow(() -> new IllegalStateException("Restaurant not found"));

        if (request.getTableID() == 0) {
            return "Table ID cannot be 0";
        }
        if (isTableReserved(request.getRestaurantName(), request.getDate(), request.getTableID())) {
            return "Table is already reserved for the given date";
        }
        // Fetch the specific table for the given restaurant name and table ID
        TableStatus tableStatus = tableStatusRepo.findAllByRestaurantNameAndID(request.getRestaurantName(), request.getTableID())
                .orElseThrow(() -> new IllegalStateException("Table not found"));

        // Check if the guest count exceeds the table's capacity
        if (request.getGuestCount() > tableStatus.getCapacity()) {
            return "Guest count exceeds table capacity";
        }


        Reservation reservation = Reservation.builder()
                .date(request.getDate())
                .time(request.getTime())
                .tableStatus(tableStatusRepo.findById(request.getTableID())
                        .orElseThrow(() -> new IllegalStateException("Table not found")))
                .guestCount(request.getGuestCount())
                .restaurant(restaurant)
                .guestCount(request.getGuestCount())
                .user(userRepo.findByUsername(jwtService.extractUsername(httpServletRequest.getHeader("Authorization").substring(7)))
                        .orElseThrow(() -> new IllegalStateException("User not found"))
                )
                .build();

        reservationRepo.save(reservation);

        return "Reservation created successfully\n your Reservation ID is " + reservation.getID();

    }
    @Transactional
    public String makeReservationByManager(CreateReservationRequest request, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        User_ user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Fetch the restaurant once and reuse it
        Restaurant restaurant = restaurantRepo.findByName(request.getRestaurantName())
                .orElseThrow(() -> new IllegalStateException("Restaurant not found"));

        // Check if the manager is the owner of the restaurant
        if (user.getID() == restaurant.getUser().getID()) {

            if (request.getTableID() == 0) {
                return "Table ID cannot be 0";
            }

            if (isTableReserved(request.getRestaurantName(), request.getDate(), request.getTableID())) {
                return "Table is already reserved for the given date";
            }

            // Fetch the specific table for the given restaurant name and table ID
            TableStatus tableStatus = tableStatusRepo.findAllByRestaurantNameAndID(request.getRestaurantName(), request.getTableID())
                    .orElseThrow(() -> new IllegalStateException("Table not found"));

            // Check if the guest count exceeds the table's capacity
            if (request.getGuestCount() > tableStatus.getCapacity()) {
                return "Guest count exceeds table capacity";
            }

            // Build and save the reservation
            Reservation reservation = Reservation.builder()
                    .date(request.getDate())
                    .time(request.getTime())
                    .tableStatus(tableStatus)
                    .guestCount(request.getGuestCount())
                    .restaurant(restaurant)
                    .user(userRepo.findByUsername(request.getUsername())
                            .orElseThrow(() -> new IllegalStateException("User not found"))
                    )
                    .build();

            reservationRepo.save(reservation);

            return "Reservation created successfully\n your Reservation ID is " + reservation.getID();
        } else {
            return "You are not the owner of the restaurant";
        }
    }


    @Transactional
    public boolean isTableReserved(String restaurantName, String date, int tableID) {
        // Fetch all tables for the specified restaurant
        List<TableStatus> tableStatuses = tableStatusRepo.findAllByRestaurantName(restaurantName)
                .orElseThrow(() -> new IllegalStateException("Restaurant not found"));

        // Check if the specific table with tableID has a reservation on the given date
        return tableStatuses.stream()
                .filter(tableStatus -> tableStatus.getID() == tableID) // Filter for the specific tableID
                .flatMap(tableStatus -> tableStatus.getReservations().stream()) // Stream over reservations of the filtered table
                .anyMatch(reservation -> reservation.getDate().equals(date)); // Check if any reservation matches the given date
    }

    @Transactional
    public Object getAvailableTables(AvailableTablesRequest request) {


        return tableStatusRepo.findAllByRestaurantName(request.getRestaurantName())
                .orElseThrow(() -> new IllegalStateException("Restaurant not found"))
                .stream()
                .filter(tableStatus -> tableStatus.getReservations().stream().noneMatch(reservation1 -> reservation1.getDate().equals(request.getDate())))
                .map(tableStatus -> TableStatusDto.builder()
                        .ID(tableStatus.getID())
                        .capacity(tableStatus.getCapacity())
                        .isReserved(tableStatus.isReserved())
                        .build())
                .toList();
    }

    @Transactional
    public Object getUserReservationsByManager(UserReservationsRequest request) {

        User_ user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<Reservation> reservations = reservationRepo.findAllByUser_ID(user.getID());

        return reservations.stream().map(
                reservation -> ReservationDto.builder()
                        .ReservationID(reservation.getID())
                        .userName(reservation.getUser().getUsername())
                        .date(reservation.getDate())
                        .time(reservation.getTime())
                        .guestCount(reservation.getGuestCount())
                        .restaurantName(reservation.getRestaurant().getName())
                        .tableID(reservation.getTableStatus().getID())
                        .build()
        ).toList();
    }
    @Transactional
    public Object getMyReservations(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        User_ user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<Reservation> reservations = reservationRepo.findAllByUser_ID(user.getID());


        return reservations.stream().map(
                reservation -> ReservationDto.builder()
                        .ReservationID(reservation.getID())
                        .userName(reservation.getUser().getUsername())
                        .date(reservation.getDate())
                        .time(reservation.getTime())
                        .guestCount(reservation.getGuestCount())
                        .restaurantName(reservation.getRestaurant().getName())
                        .tableID(reservation.getTableStatus().getID())
                        .build()
        ).toList();
    }

    public Object cancelReservation(CancelReservationRequest request, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        User_ user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        Reservation reservation = reservationRepo.findById(request.getReservationID())
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        // Define the date format that matches your string (e.g., "2021-8-24")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        // Parse the string date into a LocalDate object
        LocalDate reservationDate = LocalDate.parse(reservation.getDate(), formatter);
        // Get the current date
        LocalDate now = LocalDate.now();
        // Compare the dates
        if (now.isBefore(reservationDate.minusDays(1))) {
            reservationRepo.deleteById(request.getReservationID());
            return "Reservation cancelled successfully";
        } else {
            return "Reservation cannot be cancelled";
        }
    }
}
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
