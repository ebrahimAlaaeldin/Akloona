package com.example.akloona.Controller;

import com.example.akloona.Authentication.JwtService;
import com.example.akloona.Database.*;
import com.example.akloona.Token.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class RestaurantService {

    private final RestaurantRepo restaurantRepo;
    private final TokenRepo tokenRepo;
    private final JwtService jwtService;
    private final UserRepo userRepo;

    @Transactional
    public String addRestaurant(CreateRestaurantRequest createRestaurantRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        User_ user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = Restaurant.builder()
                .name(createRestaurantRequest.getName())
                .address(createRestaurantRequest.getAddress())
                .phoneNumber(createRestaurantRequest.getPhoneNumber())
                .numberOfTables(createRestaurantRequest.getNumberOfTables())
                .user(user)
                .build();

        restaurant.addTable(createRestaurantRequest.getNumberOfTables());

        restaurantRepo.save(restaurant);
        user.getRestaurants().add(restaurant);

        userRepo.save(user);

        return "Restaurant added successfully";
    }

    @Transactional
    public List<RestaurantDTO> getAllRestaurants(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        User_ user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));


        // Map the Restaurant entities to RestaurantDTOs to avoid circular references
        List<RestaurantDTO> restaurantDTOs = user.getRestaurants().stream()
                .map(restaurant -> RestaurantDTO.builder()
                        .address(restaurant.getAddress())
                        .numberOfTables(restaurant.getNumberOfTables())
                        .name(restaurant.getName())
                        .phoneNumber(restaurant.getPhoneNumber())
                        .build())
                .collect(Collectors.toList());

        return restaurantDTOs;
    }


    @Transactional
    public void deleteRestaurant(int id) {
        if (!restaurantRepo.existsById(id)) {
            throw new RuntimeException("Restaurant with ID " + id + " not found");
        }
        restaurantRepo.deleteById(id);
    }
}