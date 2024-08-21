package com.example.akloona.Controller;

import com.example.akloona.Database.TableStatus;
import com.example.akloona.Service.TableStatusService;
import com.example.akloona.Controller.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
public class TableStatusController {

    @Autowired
    private final TableStatusService tableStatusService;

    public TableStatusController(TableStatusService tableStatusService) {
        this.tableStatusService = tableStatusService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TableStatus>> createTableStatus(@RequestParam("isReserved") boolean isReserved,
                                                                       @RequestParam("capacity") int capacity) {
        try {
            TableStatus tableStatus = new TableStatus(isReserved, capacity);
            TableStatus savedTableStatus = tableStatusService.saveTableStatus(tableStatus);
            return ResponseEntity.ok(new ApiResponse<>(true, savedTableStatus, "Table Created Successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse<>(false, null, "Error Creating Table: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TableStatus>> getTableStatusById(@PathVariable int id) {
        Optional<TableStatus> tableStatus = tableStatusService.getTableStatusById(id);
        if (tableStatus.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(true, tableStatus.get(), "Table Found"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse<>(false, null, "Table Not Found"));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TableStatus>>> getAllTableStatuses() {
        List<TableStatus> tableStatuses = tableStatusService.getAllTableStatuses();
        return ResponseEntity.ok(new ApiResponse<>(true, tableStatuses, "Table Statuses Retrieved"));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<TableStatus>> updateTableStatus(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "isReserved", required = false) Boolean isReserved,
            @RequestParam(value = "capacity", required = false) Integer capacity) {
        String message="";
        Optional<TableStatus> existingTableStatus = tableStatusService.getTableStatusById(id);
        if (existingTableStatus.isPresent()) {
            TableStatus tableStatus = existingTableStatus.get();
            if (isReserved != null) {
                tableStatus.setIsReserved(isReserved);
                message=message+"reservation, ";
            }
            if (capacity != null) {
                tableStatus.setCapacity(capacity);
                message=message+"capacity ";
            }
            TableStatus updatedTableStatus = tableStatusService.saveTableStatus(tableStatus);
            return ResponseEntity.ok(new ApiResponse<>(true, updatedTableStatus, "Table "+message+ "Updated Successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(false, null, "Table Not Found"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteTableStatus(@RequestParam("id") int id) {
        Optional<TableStatus> existingTableStatus = tableStatusService.getTableStatusById(id);
        if (existingTableStatus.isPresent()) {
            tableStatusService.deleteTableStatusById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, null, "Table Deleted Successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse<>(false, null, "Table Not Found"));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiResponse<>(false, null, "An error occurred: " + e.getMessage()));
    }
}
