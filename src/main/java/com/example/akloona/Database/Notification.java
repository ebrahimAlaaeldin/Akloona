package com.example.akloona.Database;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
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

    @Column(name = "tableID", nullable = false)
    private int tableID;

    @Column(name = "userID", nullable = false)
    private int userID;

    @Column(name = "reservationID", nullable = false)
    private  int reservationID;

    public Notification(String message, String date, String time, int tableID, int userID, int reservationID) {
        this.message = message;
        this.date = date;
        this.time = time;
        this.tableID = tableID;
        this.userID = userID;
        this.reservationID = reservationID;
    }

}
