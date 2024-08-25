package com.example.akloona.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {

    @Query("SELECT r FROM Reservation r WHERE r.user_.ID = :userId AND r.date = :date AND r.time = :time AND r.tableStatus.ID = :tableId")
    Optional<Reservation> findByUser_IDAndDateAndTimeAndTableStatus_ID(int userId, String date, String time, int tableId);
}