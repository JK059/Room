package com.example.booking;

import java.util.*;

public class App {
    private final Set<Integer> bookedRooms = new HashSet<>();
    private final int TOTAL_ROOMS = 10;
    // v1: Develop Room Booking Logic
    public String bookRoom(int roomNumber) {
        if (roomNumber < 1 || roomNumber > TOTAL_ROOMS) {
            return "Invalid Room Number";
        }
        if (bookedRooms.contains(roomNumber)) {
            return "Room already booked! Double-booking prevented.";
        }
        bookedRooms.add(roomNumber);
        return "Booking Successful for room: " + roomNumber;
    }
     // v2: Develop Cancellation Logic
    public String cancelBooking(int roomNumber) {
        if (bookedRooms.remove(roomNumber)) {
            return "Cancellation Successful for room: " + roomNumber;
        }
        return "Cancellation Failed: Room was not booked.";
    }

    
    