package com.example.akloona.Controller;

import com.example.akloona.Database.Restaurant;
import com.example.akloona.Service.MainPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    private final MainPage mainPage;

    public RestaurantController(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    @PostMapping("/add")
    public String addRestaurant(@RequestParam("name") String name,
                                @RequestParam("address") String address,
                                @RequestParam("phoneNumber") String phoneNumber) {
        try {
            mainPage.addRestaurant(name, address, phoneNumber);
            return "Restaurant Added Successfully";
        } catch (Exception e) {
            return "Failed to Add Restaurant: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable("id") int id) {
        try {
            mainPage.deleteRestaurant(id);
            return "Restaurant Deleted Successfully";
        } catch (Exception e) {
            return "Failed to Delete Restaurant: " + e.getMessage();
        }
    }
}
