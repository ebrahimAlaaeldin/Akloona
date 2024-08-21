package com.example.akloona.Service;

import com.example.akloona.Database.Reservation;
import com.example.akloona.Database.ReservationRepo;
import com.example.akloona.Database.UserRepo;
import com.example.akloona.Database.TableStatusRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final ReservationRepo reservationRepo;
    private final UserRepo userRepo;
    private final TableStatusRepo tableStatusRepo;

    public ReservationService(ReservationRepo reservationRepo, UserRepo userRepo, TableStatusRepo tableStatusRepo) {
        this.reservationRepo = reservationRepo;
        this.userRepo = userRepo;
        this.tableStatusRepo = tableStatusRepo;
    }

    @Transactional
    public Reservation createReservation(String date, String time, int tableID, int userID, int guestCount) {
        if (!userRepo.existsById(userID)) {
            throw new IllegalStateException("User does not exist");
        }

        if (!tableStatusRepo.existsById(tableID)) {
            throw new IllegalStateException("Table does not exist");
        }

        // Check for existing reservation
        if (reservationRepo.findByUserIDAndDateAndTimeAndTableID(userID, date, time, tableID).isConfirmed()) {
            throw new IllegalStateException("A reservation already exists for this table and time");
        }

        Reservation reservation = new Reservation(date, time, tableID, userID, guestCount);
        reservation.setConfirmed(true); // Set reservation as confirmed when created
        return reservationRepo.save(reservation);
    }

    @Transactional
    public Reservation updateReservation(int id, String date, String time, int tableID, int guestCount) {
        Reservation reservation = reservationRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));
        reservation.setDate(date);
        reservation.setTime(time);
        reservation.setTableID(tableID);
        reservation.setGuestCount(guestCount);
        return reservationRepo.save(reservation);
    }

    @Transactional
    public void cancelReservation(int id) {
        Reservation reservation = reservationRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));
        reservation.setCancelled(true);
        reservation.setConfirmed(false); // Set reservation as not confirmed when cancelled
        reservationRepo.save(reservation);
    }

    @Transactional
    public void deleteReservation(int id) {
        if (!reservationRepo.existsById(id)) {
            throw new IllegalStateException("Reservation not found");
        }
        reservationRepo.deleteById(id);
    }

    public Reservation getReservationById(int id) {
        return reservationRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Reservation not found"));
    }
}
