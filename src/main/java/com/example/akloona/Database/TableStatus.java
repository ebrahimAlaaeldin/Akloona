package com.example.akloona.Database;

import jakarta.persistence.*;
import lombok.*;


@Builder

@AllArgsConstructor
@Data
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
        this( false, 2);
    }

    public TableStatus(boolean isOccupied, int capacity) {
        this.isReserved = isOccupied;
        this.capacity = capacity;
    }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}
