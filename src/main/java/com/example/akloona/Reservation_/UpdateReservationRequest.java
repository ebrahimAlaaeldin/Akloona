package com.example.akloona.Reservation_;


import com.example.akloona.Database.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateReservationRequest {

    private  String date;
    private  String time;
    private  int tableID;
    private  int guestCount;
    private int reservationID;

}
