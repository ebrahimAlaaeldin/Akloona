package com.example.akloona.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation,Integer> {
    Reservation findByUserIDAndDateAndTimeAndTableID(int userID, String date, String time, int tableID);
    List<Reservation> findByDateAndTime(String date, String time);
}
