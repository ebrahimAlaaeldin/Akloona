package com.example.akloona.Controller;


import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateRestaurantRequest {

    private String name;
    private String address;
    private String phoneNumber;
    private int numberOfTables;

}
