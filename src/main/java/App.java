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
    // v3: Maintain Accurate Availability Status
    public List<Integer> getAvailableRooms() {
        List<Integer> available = new ArrayList<>();
        for (int i = 1; i <= TOTAL_ROOMS; i++) {
            if (!bookedRooms.contains(i)) {
                available.add(i);
            }
        }
        return available;
    }

    public static void main(String[] args) {
        System.out.println("Room Booking Service is running on Localhost...");
        
        // This loop keeps the application alive inside your Docker container/K8s pod
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Service Interrupted");
                break;
            }
        }
    }
}

    
    