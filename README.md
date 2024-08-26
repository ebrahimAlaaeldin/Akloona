# Akloona Restaurant Reservation System

## Overview

Akloona is a restaurant reservation system built using Spring Boot. It allows users to make reservations, manage their profiles, and for staff and managers to manage restaurants and reservations. The system includes features for user authentication, profile management, reservation handling, and restaurant management.

## Features

- **User Authentication**: Register, login, and refresh tokens.
- **Profile Management**: Update user details such as password, email, address, and phone number.
- **Reservation Management**: Create, view, and cancel reservations.
- **Restaurant Management**: Add and delete restaurants, and retrieve a list of all restaurants.
- **Role-Based Access Control**: Different functionalities are accessible based on the user's role (Customer, Staff, Manager).

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- A relational database (e.g., MySQL, PostgreSQL)

 
 ## API Documentation

### AuthenticationController

- **POST** `/api/authenticate/register`
  - Registers a new user.
  - **Request Body:** `RegisterRequest`
  - **Response:** `AuthenticationResponse`

- **POST** `/api/authenticate/login`
   - Authenticates a user and provides a JWT token.
   - **Request Body:** `AuthenticationRequest`
   - **Response:** `AuthenticationResponse`

- **POST** `/api/authenticate/refresh-token`
   - Refreshes the JWT token for an authenticated user.
   - **Request Params:** `HttpServletRequest`, `HttpServletResponse`

### ProfileController

- **PUT** `/api/profile/change-password`
   - Updates the user's password.
   - **Request Body:** `ChangePasswordRequest`
   - **Response:** `String`

- **PUT** `/api/profile/change-email`
   - Updates the user's email.
   - **Request Body:** `ChangeEmailRequest`
   - **Response:** `String`

- **PUT** `/api/profile/change-address`
   - Updates the user's address.
   - **Request Body:** `ChangeAddressRequest`
   - **Response:** `String`

- **PUT** `/api/profile/change-phoneNumber`
   - Updates the user's phone number.
   - **Request Body:** `ChangePhoneNumberRequest`
   - **Response:** `String`

- **DELETE** `/api/profile/delete-account`
   - Deletes the user's account.
   - **Response:** `String`

- **DELETE** `/api/profile/block-user`
   - Blocks a user (accessible to managers and staff).
   - **Request Body:** `BlockUserRequest`
   - **Response:** `String`

- **PUT** `/api/profile/unblock-user`
   - Unblocks a user (accessible to managers and staff).
   - **Request Body:** `UnblockUserRequest`
   - **Response:** `String`

### ReservationController

- **GET** `/api/reservation/get-available-tables`
   - Retrieves available tables based on a given request.
   - **Request Body:** `AvailableTablesRequest`
   - **Response:** `List<Table>`

- **POST** `/api/reservation/create`
   - Creates a reservation (accessible to customers).
   - **Request Body:** `MakeReservationRequest`
   - **Response:** `String`

- **GET** `/api/reservation/get-User-Reservations`
   - Retrieves reservations for a user by the manager or staff.
   - **Request Body:** `UserReservationsRequest`
   - **Response:** `List<Reservation>`

- **GET** `/api/reservation/get-My-Reservations`
   - Retrieves the logged-in user's reservations (accessible to customers).
   - **Response:** `List<Reservation>`

- **POST** `/api/reservation/make-reservation-by-manager`
   - Creates a reservation on behalf of a user by the manager or staff.
   - **Request Body:** `MakeReservationRequest`
   - **Response:** `String`

- **POST** `/api/reservation/cancel-reservation`
   - Cancels a reservation (accessible to customers).
   - **Request Body:** `CancelReservationRequest`
   - **Response:** `String`

### RestaurantController

- **POST** `/api/restaurant/add`
   - Adds a new restaurant (accessible to managers).
   - **Request Body:** `CreateRestaurantRequest`
   - **Response:** `String`

- **DELETE** `/api/restaurant/delete`
   - Deletes a restaurant (accessible to managers).
   - **Request Body:** `DeleteRestaurantRequest`
   - **Response:** `String`

- **GET** `/api/restaurant/get-all`
   - Retrieves a list of all restaurants (accessible to managers).
   - **Response:** `List<RestaurantDTO>`

## Project Structure

- `src/main/java/com/example/akloona/Controllers/`: Contains all the controller classes.
- `src/main/java/com/example/akloona/Service/`: Contains service classes with business logic.
- `src/main/java/com/example/akloona/Models/`: Contains entity classes representing the database tables.
- `src/main/resources/`: Contains configuration files, including `application.properties`.

## Contributing
- Tareq Mahfouz
- Ebrahim Alaa
- Omar El-Ansary

