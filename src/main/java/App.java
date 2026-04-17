package com.example.booking;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;

public class App {
    private final Set<Integer> bookedRooms = new HashSet<>();
    private final int TOTAL_ROOMS = 10;

    public String bookRoom(int roomNumber) {
        if (roomNumber < 1 || roomNumber > TOTAL_ROOMS) return "Invalid Room";
        if (bookedRooms.contains(roomNumber)) return "Already Booked";
        bookedRooms.add(roomNumber);
        return "Success";
    }

    public String cancelBooking(int roomNumber) {
        if (bookedRooms.remove(roomNumber)) return "Cancelled";
        return "Not Booked";
    }

    public static void main(String[] args) throws IOException {
        App app = new App();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            StringBuilder gridHtml = new StringBuilder();
            for (int i = 1; i <= 10; i++) {
                boolean isBooked = app.bookedRooms.contains(i);
                String statusClass = isBooked ? "booked" : "available";
                String statusText = isBooked ? "OCCUPIED" : "AVAILABLE";
                gridHtml.append("<div class='room-card ").append(statusClass).append("'>")
                        .append("<div class='room-no'>").append(i).append("</div>")
                        .append("<div class='status'>").append(statusText).append("</div>")
                        .append("</div>");
            }

            String response = "<!DOCTYPE html><html><head>" +
                "<link href='https://googleapis.com' rel='stylesheet'>" +
                "<style>" +
                "  * { box-sizing: border-box; font-family: 'Poppins', sans-serif; }" +
                "  body { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; display: flex; justify-content: center; align-items: center; margin: 0; }" +
                "  .container { background: rgba(255, 255, 255, 0.95); padding: 40px; border-radius: 20px; box-shadow: 0 20px 40px rgba(0,0,0,0.2); width: 500px; text-align: center; }" +
                "  h2 { color: #2d3436; margin-bottom: 30px; font-weight: 600; font-size: 28px; }" +
                "  .grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 12px; margin-bottom: 30px; }" +
                "  .room-card { padding: 10px; border-radius: 12px; border: 2px solid transparent; transition: all 0.3s ease; cursor: default; }" +
                "  .room-no { font-size: 18px; font-weight: 600; }" +
                "  .status { font-size: 8px; font-weight: 400; opacity: 0.8; }" +
                "  .available { background: #e3f9e5; color: #1f9d55; border-color: #71d08d; }" +
                "  .booked { background: #ffe3e3; color: #e12d39; border-color: #f6969b; }" +
                "  .room-card:hover { transform: translateY(-5px); box-shadow: 0 5px 15px rgba(0,0,0,0.1); }" +
                "  .form-group { display: flex; flex-direction: column; gap: 15px; }" +
                "  input { padding: 12px; border: 2px solid #eee; border-radius: 10px; outline: none; transition: 0.3s; font-size: 16px; text-align: center; }" +
                "  input:focus { border-color: #667eea; }" +
                "  .actions { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }" +
                "  button { padding: 12px; border: none; border-radius: 10px; color: white; font-weight: 600; cursor: pointer; transition: 0.3s; font-size: 14px; text-transform: uppercase; letter-spacing: 1px; }" +
                "  .btn-book { background: #6c5ce7; }" +
                "  .btn-book:hover { background: #a29bfe; box-shadow: 0 4px 12px rgba(108, 92, 231, 0.4); }" +
                "  .btn-cancel { background: #636e72; }" +
                "  .btn-cancel:hover { background: #b2bec3; }" +
                "</style></head><body>" +
                "<div class='container'>" +
                "  <h2>🏨 Grand Hotel Manager</h2>" +
                "  <div class='grid'>" + gridHtml + "</div>" +
                "  <div class='form-group'>" +
                "    <input type='number' id='num' placeholder='Enter Room No (1-10)' min='1' max='10'>" +
                "    <div class='actions'>" +
                "      <button class='btn-book' onclick='handle(\"book\")'>Book Now</button>" +
                "      <button class='btn-cancel' onclick='handle(\"cancel\")'>Release</button>" +
                "    </div>" +
                "  </div>" +
                "</div>" +
                "<script>function handle(type){ " +
                "  const val = document.getElementById(\"num\").value;" +
                "  if(!val || val < 1 || val > 10) { alert(\"Please enter a valid room (1-10)\"); return; }" +
                "  location.href=\"/action?type=\"+type+\"&num=\"+val;" +
                "}</script>" +
                "</body></html>";

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.createContext("/action", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            if (query != null) {
                String[] params = query.split("&");
                String type = params[0].split("=")[1];
                int num = Integer.parseInt(params[1].split("=")[1]);
                if (type.equals("book")) app.bookRoom(num);
                else app.cancelBooking(num);
            }
            exchange.getResponseHeaders().set("Location", "/");
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        });

        System.out.println("Enhanced Service Running at http://localhost:8080");
        server.start();
    }
}
