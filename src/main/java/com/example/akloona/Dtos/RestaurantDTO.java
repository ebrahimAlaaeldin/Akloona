package com.example.akloona.Dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RestaurantDTO {
    private String name;
    private String address;
    private String phoneNumber;
    private int numberOfTables;

}
