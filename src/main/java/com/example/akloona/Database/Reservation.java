package com.example.akloona.Database;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Entity(name = "Reservation")
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    @Column(name = "date", nullable = false)
    private String date;
    @Column(name = "time", nullable = false)
    private String time;
    @Column(name = "tableID", nullable = false)
    private int tableID;
    @Column(name = "userID", nullable = false)
    private int userID;
    @Column(name = "isConfirmed", nullable = false)
    private boolean isConfirmed;
    @Column(name = "isCancelled", nullable = false)
    private boolean isCancelled;
    @Column(name = "guestCount", nullable = false)
    private  int guestCount;

    public Reservation(String date, String time, int tableID, int userID, int guestCount) {
        this.date = date;
        this.time = time;
        this.tableID = tableID;
        this.userID = userID;
        this.guestCount = guestCount;
        this.isConfirmed = false;
        this.isCancelled = false;

    }





}
