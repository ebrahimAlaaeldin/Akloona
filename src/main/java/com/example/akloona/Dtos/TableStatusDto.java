package com.example.akloona.Dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TableStatusDto {
    private int ID;
    private boolean isReserved;
    private int capacity;



}
