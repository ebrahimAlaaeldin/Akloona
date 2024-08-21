package com.example.akloona.Service;

import com.example.akloona.Database.TableStatus;
import com.example.akloona.Database.TableStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableStatusService {

    @Autowired
    private TableStatusRepo TableStatusRepo;

    // Create or Update a TableStatus
    public TableStatus saveTableStatus(TableStatus tableStatus) {
        return TableStatusRepo.save(tableStatus);
    }

    // Get a TableStatus by ID
    public Optional<TableStatus> getTableStatusById(int id) {
        return TableStatusRepo.findById(id);
    }

    // Get all TableStatus records
    public List<TableStatus> getAllTableStatuses() {
        return TableStatusRepo.findAll();
    }

    // Delete a TableStatus by ID
    public void deleteTableStatusById(int id) {
        TableStatusRepo.deleteById(id);
    }
}
