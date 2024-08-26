package com.example.akloona.Service;

import com.example.akloona.Authentication.JwtService;
import com.example.akloona.Database.*;
import com.example.akloona.Dtos.RestaurantDTO;
import com.example.akloona.Restaurant_.CreateRestaurantRequest;
import com.example.akloona.Restaurant_.DeleteRestaurantRequest;
import com.example.akloona.Token.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepo restaurantRepo;
    private final TokenRepo tokenRepo;
    private final JwtService jwtService;
    private final UserRepo userRepo;

    @Transactional
    public String addRestaurant(CreateRestaurantRequest createRestaurantRequest, HttpServletRequest httpServletRequest) {
        try {
            String token = httpServletRequest.getHeader("Authorization").substring(7);
            String username = jwtService.extractUsername(token);
            User_ user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));


            Restaurant restaurant = Restaurant.builder()
                    .name(createRestaurantRequest.getName())
                    .address(createRestaurantRequest.getAddress())
                    .phoneNumber(createRestaurantRequest.getPhoneNumber())
                    .numberOfTables(createRestaurantRequest.getNumberOfTables())
                    .user(user)
                    .openingTime(createRestaurantRequest.getOpeningTime())
                    .closingTime(createRestaurantRequest.getClosingTime())
                    .build();

            restaurant.addTable(createRestaurantRequest.getNumberOfTables());

            restaurantRepo.save(restaurant);
            user.getRestaurants().add(restaurant);

            userRepo.save(user);

            return "Restaurant added successfully";
        }catch (RuntimeException e) {
            return "Error: " + e.getMessage();}
        catch(Exception e){
                return "Error: An unexpected error occurred - " +e.getMessage();
        }
    }

    @Transactional
    public Object getAllRestaurants(HttpServletRequest httpServletRequest) {
        try{
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
        }catch (RuntimeException e) {
            return "Error: " + e.getMessage();}
        catch(Exception e){
                return "Error: An unexpected error occurred - " +e.getMessage();
        }    
    }


    @Transactional
    public String deleteRestaurant(DeleteRestaurantRequest request) {
        try{
            log.info("Deleting restaurant with ID " + request.getRestaurantName());

            restaurantRepo.deleteByName(request.getRestaurantName());
            return "Restaurant Deleted Successfully";
        }catch(Exception e)
        {
            return "Error: An unexpected error occurred - " + e.getMessage();
        }    
    }
}