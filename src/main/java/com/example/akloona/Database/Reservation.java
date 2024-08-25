package com.example.akloona.Database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@Entity(name = "Reservation")
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;

    @Column(name = "date", nullable = false)// eg. 2021-12-31
    private String date;

    @Column(name = "time", nullable = false) //eg. 14:30:00
    private String  time;

    @Column(name = "guestCount", nullable = false)
    private int guestCount;




//    @Column(name = "startTime", nullable = false)
//    private LocalTime startTime;
//
//    @Column(name = "endTime", nullable = false)
//    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableStatus tableStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User_ user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

}

