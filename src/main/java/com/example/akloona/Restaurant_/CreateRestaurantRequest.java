package com.example.akloona.Restaurant_;


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
    private String openingTime;
    private String closingTime;

}
