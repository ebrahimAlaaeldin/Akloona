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


    private LocalDate date;
    private int tableID;
    private int GuestCount;

}
