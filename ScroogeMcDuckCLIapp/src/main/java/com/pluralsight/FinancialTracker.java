package com.pluralsight;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class FinancialTracker {
    private static final String FILE_NAME = "transactions.csv";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("Home Screen");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "D":
                    addTransaction("deposit");
                    break;
                case "P":
                    addTransaction("payment");
                    break;
                case "L":
                    viewLedger();
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    public static void addTransaction(String type) {
        String description, vendor, date, time;
        double amount;

        System.out.print("Enter the description of the transaction: ");
        description = scanner.nextLine();

        // If the vendor is Scrooge McDuck, set it automatically
        if (description.toLowerCase().contains("scrooge mcduck")) {
            vendor = "Scrooge McDuck";
        } else {
            System.out.print("Enter the vendor: ");
            vendor = scanner.nextLine();
        }

        System.out.print("Enter the amount: ");
        amount = Double.parseDouble(scanner.nextLine());

        // If it's a deposit, amount is positive, otherwise negative for payments
        if (type.equals("deposit")) {
            amount = Math.abs(amount);
        } else if (type.equals("payment")) {
            amount = -Math.abs(amount);
        }

        // Generate current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
        date = sdf.format(new Date());

        time = date.split("\\|")[1]; // Time is the part after the "|"

        // Create the CSV line
        String transactionLine = String.format("%s|%s|%s|%s|%.2f", date, time, description, vendor, amount);

        // Write transaction to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(transactionLine);
            writer.newLine();
            System.out.println("Transaction added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    public static void viewLedger() {
        System.out.println("Ledger Screen");
        System.out.println("A) All Transactions");
        System.out.println("D) Deposits");
        System.out.println("P) Payments");
        System.out.println("H) Home");

        String choice = scanner.nextLine().toUpperCase();

        switch (choice) {
            case "A":
                displayTransactions("all");
                break;
            case "D":
                displayTransactions("deposit");
                break;
            case "P":
                displayTransactions("payment");
                break;
            case "H":
                return;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

    public static void displayTransactions(String type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            List<String> transactions = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                transactions.add(line);
            }

            // Reverse the order to show newest first
            Collections.reverse(transactions);

            for (String transaction : transactions) {
                String[] parts = transaction.split("\\|");
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                // Filter based on the type if needed
                if ("all".equals(type) ||
                        ("deposit".equals(type) && amount > 0) ||
                        ("payment".equals(type) && amount < 0)) {
                    System.out.println(transaction);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading the file.");
        }
    }
}