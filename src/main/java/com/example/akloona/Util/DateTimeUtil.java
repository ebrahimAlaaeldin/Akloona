package com.example.akloona.Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static LocalDateTime convertStringToDateTime(String date, String time) {
        String dateTimeString = date + " " + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // Adjust the pattern as needed
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}