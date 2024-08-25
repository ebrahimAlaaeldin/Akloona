package com.example.akloona.Database;

import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableStatusRepo extends JpaRepository<TableStatus, Integer> {

    Optional<List<TableStatus>> findAllByRestaurantName(String restaurantName);


    Optional<TableStatus> findAllByRestaurantNameAndID(String restaurantName, int tableID);
}
