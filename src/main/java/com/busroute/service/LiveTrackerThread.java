package com.busroute.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LiveTrackerThread extends Thread {
    private volatile boolean running = true;
    
    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    
    @Override
    public void run() {
        String[] mockUpdates = {
            "Bus TR-1 arriving at Habibganj in 2 mins",
            "Bus SR-5 delayed by traffic near Bairagarh",
            "Bus SR-8 now departing from Bhopal Station",
            "Bus TR-4 has reached its final terminus"
        };
        int counter = 0;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("live_tracker.log", true))) {
            while (running) {
                try {
                    Thread.sleep(15000); // 15 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                
                if (running) {
                    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    writer.write("[" + timestamp + "] " + mockUpdates[counter % mockUpdates.length]);
                    writer.newLine();
                    writer.flush();
                    counter++;
                }
            }
        } catch (IOException e) {
            System.err.println("Live tracker background error: " + e.getMessage());
        }
    }
    
    public void stopTracker() {
        this.running = false;
        this.interrupt();
    }
}
