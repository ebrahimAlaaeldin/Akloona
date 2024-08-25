package com.example.akloona.Database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "Restaurant")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Slf4j
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phoneNumber", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "number_of_tables", nullable = false)  // Updated column name
    private int numberOfTables;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TableStatus> tables;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private User_ user;
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public void addTable(int noOfTables) {

        log.info("Adding {} tables to the restaurant", noOfTables);
        // Initialize the list of TableStatus based on the number of tables
        List<TableStatus> tableStatuses = new ArrayList<>();
        for (int i = 1; i <= noOfTables; i++) {
            TableStatus tableStatus = TableStatus.builder()
                    .isReserved(false)
                    .restaurant(this)  // Associate each TableStatus with the Restaurant
                    .capacity(6) //max capacity of the table
                    .build();
            tableStatuses.add(tableStatus);
        }

        this.tables = tableStatuses;
    }
}
