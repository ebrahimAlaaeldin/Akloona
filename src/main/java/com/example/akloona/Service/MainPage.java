package com.example.akloona.Service;


import com.example.akloona.Database.UserRepo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.akloona.Database.User_;
import com.example.akloona.Database.Restaurant;
import com.example.akloona.Database.RestaurantRepo;

import java.security.PrivateKey;
import java.util.PrimitiveIterator;


@Getter
@Component
public class MainPage {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder; // for password hashing
    private final UserService userService;
    private User_ activeUser; // for storing the active user
    private final RestaurantRepo restaurantRepo;


    // for checking if user is logged in and also check the account type for later use it is encapsulated in the class where it can only be modified here
    private  boolean loggedIn ;
    private String accountType ;

    @Autowired
    public MainPage(UserRepo userRepo, PasswordEncoder passwordEncoder, UserService userService, RestaurantRepo restaurantRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.restaurantRepo = restaurantRepo;
        this.loggedIn = false; // default value
        this.accountType = "";
    }


    public void addRestaurant(String name, String address, String phoneNumber) {
        if (isLoggedIn() && accountType.equals("admin")) {
            Restaurant restaurant = new Restaurant(name, address, phoneNumber);
            restaurantRepo.save(restaurant);
        } else {
            throw new IllegalStateException("Only admins can add restaurants");
        }
    }
    public void deleteRestaurant(int restaurantId) {
        if (isLoggedIn() && accountType.equals("admin")) {
            restaurantRepo.deleteById(restaurantId);
        } else {
            throw new IllegalStateException("Only admins can delete restaurants");
        }
    }
}