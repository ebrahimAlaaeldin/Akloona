package com.example.akloona.Reservation_;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvailableTablesRequest {
    private String date;
    private String restaurantName;
}
