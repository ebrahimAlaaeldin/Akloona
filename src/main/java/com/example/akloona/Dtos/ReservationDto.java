package com.example.akloona.Dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReservationDto {
    private int ReservationID;
    private String userName;
    private String date;
    private String time;
    private int guestCount;
    private int tableID;
    private String restaurantName;


}
