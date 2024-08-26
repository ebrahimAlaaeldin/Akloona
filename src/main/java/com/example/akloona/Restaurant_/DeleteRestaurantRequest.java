package com.example.akloona.Restaurant_;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class DeleteRestaurantRequest {
    private String restaurantName;
}
