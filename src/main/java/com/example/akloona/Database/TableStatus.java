package com.example.akloona.Database;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
@Entity(name = "TableStatus")
@NoArgsConstructor
public class TableStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableStatus")
    private List<Reservation> reservations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantID")
    private Restaurant restaurant;

}