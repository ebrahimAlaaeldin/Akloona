package com.example.akloona.Database;

import jakarta.persistence.*;


@Entity(name = "TableStatus")
public class TableStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    @Column(name = "isReserved", nullable = false)
    private boolean isReserved;

    @Column(name = "capacity", nullable = false)
    private int capacity;


    public TableStatus() {
        this( false, 4);
    }

    public TableStatus(boolean isOccupied, int capacity) {
        this.isReserved = isReserved;//I (omar) have an error here
        this.capacity = capacity;
    }
}
