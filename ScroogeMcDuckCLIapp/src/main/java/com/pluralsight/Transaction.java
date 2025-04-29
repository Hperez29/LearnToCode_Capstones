package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public static Transaction fromCsv(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 5) return null;

        LocalDate date = LocalDate.parse(parts[0]);
        LocalTime time = LocalTime.parse(parts[1]);
        String description = parts[2];
        String vendor = parts[3];
        double amount = Double.parseDouble(parts[4]);

        return new Transaction(date, time, description, vendor, amount);
    }

    public String toCsv() {
        return String.format("%s|%s|%s|%s|%.2f", date, time, description, vendor, amount);
    }

    @Override
    public String toString() {
        return String.format("%s %s | %-20s | %-15s | %.2f", date, time, description, vendor, amount);
    }

    // Getters
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public String getVendor() { return vendor; }
    public double getAmount() { return amount; }
}
