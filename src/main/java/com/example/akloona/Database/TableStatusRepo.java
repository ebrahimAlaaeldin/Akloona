package com.example.akloona.Database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TableStatusRepo extends JpaRepository<TableStatus,Integer> {
}
