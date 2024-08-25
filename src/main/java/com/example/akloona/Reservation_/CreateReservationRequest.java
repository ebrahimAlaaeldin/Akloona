package com.example.akloona.Reservation_;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateReservationRequest {

    private String username;//only for Creating Reservation by Manager

    private String restaurantName;
    private String date;
    private String time;
    private int tableID;
    private int guestCount;

}
