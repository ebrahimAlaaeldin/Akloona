package com.example.akloona.Controllers;

import com.example.akloona.Dtos.RestaurantDTO;
import com.example.akloona.Restaurant_.CreateRestaurantRequest;
import com.example.akloona.Restaurant_.DeleteRestaurantRequest;
import com.example.akloona.Service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    @Autowired

    private final RestaurantService restaurantService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> addRestaurant(@RequestBody CreateRestaurantRequest request, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(restaurantService.addRestaurant(request, httpServletRequest));
    }



    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteRestaurant(@RequestBody DeleteRestaurantRequest request) {
            return ResponseEntity.ok(restaurantService.deleteRestaurant(request));
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Object> getAllRestaurants(HttpServletRequest httpServletRequest) {
        Object restaurants = restaurantService.getAllRestaurants(httpServletRequest);
        return ResponseEntity.ok(restaurants);
    }
}