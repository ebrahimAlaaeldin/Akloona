package com.example.akloona.Controller;

import com.example.akloona.Dtos.RestaurantDTO;
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



    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteRestaurant(@PathVariable("name") String name) {
        try {
            restaurantService.deleteRestaurant(name);
            return ResponseEntity.ok("Restaurant Deleted Successfully");
        } catch (Exception e) {
           return ResponseEntity.ok("Restaurant not found");
        }
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants(HttpServletRequest httpServletRequest) {
        List<RestaurantDTO> restaurants = restaurantService.getAllRestaurants(httpServletRequest);
        return ResponseEntity.ok(restaurants);
    }
}