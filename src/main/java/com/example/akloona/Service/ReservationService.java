package com.example.akloona.Service;

import com.example.akloona.Authentication.JwtService;
import com.example.akloona.Database.*;

import com.example.akloona.Dtos.ReservationDto;
import com.example.akloona.Dtos.TableStatusDto;
import com.example.akloona.Enums.ReservationStatus;

import com.example.akloona.Reservation_.AvailableTablesRequest;
import com.example.akloona.Reservation_.CancelReservationRequest;
import com.example.akloona.Reservation_.MakeReservationRequest;
import com.example.akloona.Reservation_.UserReservationsRequest;
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



    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");


    @Transactional
    public boolean checkWithinRange (MakeReservationRequest request) {
        Restaurant restaurant = restaurantRepo.findByName(request.getRestaurantName())
                .orElseThrow(() -> new IllegalStateException("Restaurant not found"));

        LocalDate now = LocalDate.now();
        LocalDate reservationDate = LocalDate.parse(request.getDate(), formatter);
        if(now.isAfter(reservationDate)) {
            return false;
        }

        String[] timeArray = request.getTime().split(":");
        int hour = Integer.parseInt(timeArray[0]);
        int minute = Integer.parseInt(timeArray[1]);
        int openingHour = Integer.parseInt(restaurant.getOpeningTime().split(":")[0]);
        int openingMinute = Integer.parseInt(restaurant.getOpeningTime().split(":")[1]);
        int closingHour = Integer.parseInt(restaurant.getClosingTime().split(":")[0]);
        int closingMinute = Integer.parseInt(restaurant.getClosingTime().split(":")[1]);

        if (hour >= openingHour && hour <= closingHour) {
            return true;
        } else return minute >= openingMinute && minute <= closingMinute;
    }
    @Transactional
    public String makeReservation(MakeReservationRequest request, HttpServletRequest httpServletRequest) {
        try{
                Restaurant restaurant = restaurantRepo.findByName(request.getRestaurantName())
                        .orElseThrow(() -> new IllegalStateException("Restaurant not found"));


                if(!checkWithinRange(request)) {
                throw new IllegalStateException("Reservation time is out of range");
                }
                if (isTableReserved(request.getRestaurantName(), request.getDate(), request.getTableID())) {
                throw new IllegalStateException("Table is already reserved");
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
                        ).status(ReservationStatus.BOOKED)
                        .build();

                reservationRepo.save(reservation);

                return "Reservation created successfully\n your Reservation ID is " + reservation.getID();

        }catch (IllegalStateException e) {
                return "Error: " + e.getMessage();}
        catch(Exception e){
                return "Error: An unexpected error occurred - " +e.getMessage();
        }
    }
    @Transactional
    public String makeReservation(MakeReservationRequest request, String username) {
        try{
            Restaurant restaurant = restaurantRepo.findByName(request.getRestaurantName())
                    .orElseThrow(() -> new IllegalStateException("Restaurant not found"));


            if(!checkWithinRange(request)) {
                throw new IllegalStateException("Reservation time is out of range");
            }
            if (isTableReserved(request.getRestaurantName(), request.getDate(), request.getTableID())) {
                throw new IllegalStateException("Table is already reserved");
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
                    .user(userRepo.findByUsername(username)
                            .orElseThrow(() -> new IllegalStateException("User not found"))
                    ).status(ReservationStatus.BOOKED)
                    .build();

            reservationRepo.save(reservation);

            return "Reservation created successfully\n your Reservation ID is " + reservation.getID();

        }catch (IllegalStateException e) {
            return "Error: " + e.getMessage();}
        catch(Exception e){
            return "Error: An unexpected error occurred - " +e.getMessage();
        }
    }

    @Transactional
    public String makeReservationByManager(MakeReservationRequest request, HttpServletRequest httpServletRequest) {

            String token = httpServletRequest.getHeader("Authorization").substring(7);
            String username = jwtService.extractUsername(token);
            User_ user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            return makeReservation(request, username);

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

        try {
                return tableStatusRepo.findAllByRestaurantName(request.getRestaurantName())
                .orElseThrow(() -> new IllegalStateException("Restaurant not found"))
                .stream()
                .filter(tableStatus -> tableStatus.getReservations().stream().noneMatch(reservation1 -> reservation1.getDate().equals(request.getDate())))
                .map(tableStatus -> TableStatusDto.builder()
                        .ID(tableStatus.getID())
                        .capacity(tableStatus.getCapacity())
                        .build())
                .toList();
        }catch (IllegalStateException e) {
                return "Error: " + e.getMessage();}
        catch(Exception e){
                return "Error: An unexpected error occurred - " +e.getMessage();
        }
    }

    @Transactional
    public Object getUserReservationsByManager(UserReservationsRequest request) {
        try{
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

        }catch (IllegalStateException e) {
                return "Error: " + e.getMessage();}
        catch(Exception e){
                return "Error: An unexpected error occurred - " +e.getMessage();
        }
    }
    @Transactional
    public Object getMyReservations(HttpServletRequest httpServletRequest) {
        try{
                String token = httpServletRequest.getHeader("Authorization").substring(7);
                String username = jwtService.extractUsername(token);
                User_ user = userRepo.findByUsername(username)
                        .orElseThrow(() -> new IllegalStateException("User not found"));

                List<Reservation> reservations = reservationRepo.findAllByUser_IDAndStatusNot(user.getID(), ReservationStatus.CANCELED);
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
        }catch (IllegalStateException e) {
                return "Error: " + e.getMessage();}
        catch(Exception e){
                return "Error: An unexpected error occurred - " +e.getMessage();
        }
    }

    public Object cancelReservation(CancelReservationRequest request, HttpServletRequest httpServletRequest) {
        try{
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
                reservation.setStatus(ReservationStatus.CANCELED);
                reservationRepo.save(reservation); // Save the updated reservation
                return "Reservation cancelled successfully";
                } else {
                return "Reservation cannot be cancelled";
                }
        }catch (IllegalStateException e) {
                return "Error: " + e.getMessage();}
        catch(Exception e){
                return "Error: An unexpected error occurred - " +e.getMessage();
        }        
    }
}

