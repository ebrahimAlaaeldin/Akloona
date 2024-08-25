package com.example.akloona.Database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Notification")
@Table(name = "Notification", uniqueConstraints = {
        @UniqueConstraint(name = "notificationID_unique", columnNames = "ID"),
})
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "time", nullable = false)
    private String time;

    @ManyToOne
    @JoinColumn(name = "reservationID", nullable = false)
    private Reservation reservation;
}

