package com.example.akloona.Database;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.akloona.Enums.ReservationStatus;

import java.util.List;
import java.util.Optional;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

    @Query("SELECT r FROM Reservation r WHERE r.user.ID = :userId AND r.date = :date AND r.time = :time AND r.tableStatus.ID = :tableId")
    Optional<Reservation> findByUser_IDAndDateAndTimeAndTableStatus_ID(int userId, String date, String time, int tableId);


    Optional<List<Reservation>> findAllByDateAndRestaurantName(String date, String name);

    Optional<List<Reservation>> findAllByRestaurantNameAndDate(String restaurantName, String date);

    Optional<List<Reservation>> findAllByUser_Username(String username);

    List<Reservation> findAllByUser_(User_ userNotFound);


    List<Reservation> findAllByUser_ID(int userNotFound);

    List<Reservation> findByStatus(ReservationStatus status);
}