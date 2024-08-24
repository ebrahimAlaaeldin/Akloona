package com.example.akloona.Database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
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
    @ManyToOne
    @JoinColumn(name = "tableID", nullable = false)
    private TableStatus tableStatus;
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User_ user;
    @Column(name = "isConfirmed", nullable = false)
    private boolean isConfirmed;
    @Column(name = "isCancelled", nullable = false)
    private boolean isCancelled;
    @Column(name = "guestCount", nullable = false)
    private  int guestCount;

//    public int getTableId() {
//        return tableStatus.getID();
//    }

}
