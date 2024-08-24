package com.example.akloona.Reservation_;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateReservationRequest {

    private String date;
    private String time;
    private int tableID;
    private int GuestCount;

}
