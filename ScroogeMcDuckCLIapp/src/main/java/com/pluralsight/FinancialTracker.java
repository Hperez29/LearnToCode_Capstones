package com.pluralsight;

import java.io.*;
import java.text.*;
import java.util.*;

public class FinancialTracker {
    private static final String FILE_NAME = "transactions.csv";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showHomeScreen();
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "D": addTransaction("deposit"); break;
                case "P": addTransaction("payment"); break;
                case "L": ledgerScreen(); break;
                case "X": running = false; break;
                default: System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    private static void showHomeScreen() {
        System.out.println("Home Screen");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
    }

    public static void addTransaction(String type) {
        String description, vendor;
        double amount;

        System.out.print("Enter the description of the transaction: ");
        description = scanner.nextLine();
        vendor = getVendor(description);

        System.out.print("Enter the amount: ");
        amount = Double.parseDouble(scanner.nextLine());

        // Set amount sign based on transaction type
        amount = type.equals("deposit") ? Math.abs(amount) : -Math.abs(amount);

        // Get current date and time
        String dateTime = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss").format(new Date());
        String time = dateTime.split("\\|")[1];

        // Prepare transaction line
        String transactionLine = String.format("%s|%s|%s|%s|%.2f", dateTime.split("\\|")[0], time, description, vendor, amount);

        writeToFile(transactionLine);
    }

    private static String getVendor(String description) {
        if (description.toLowerCase().contains("scrooge mcduck")) return "Scrooge McDuck";
        if (description.toLowerCase().contains("donald duck")) return "Donald Duck";
        if (description.toLowerCase().contains("goofy")) return "Goofy";
        System.out.print("Enter the vendor: ");
        return scanner.nextLine();
    }

    private static void writeToFile(String transactionLine) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(transactionLine);
            writer.newLine();
            System.out.println("Transaction added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    public static void ledgerScreen() {
        boolean running = true;
        while (running) {
            showLedgerScreen();
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "A": displayTransactions("all"); break;
                case "D": displayTransactions("deposit"); break;
                case "P": displayTransactions("payment"); break;
                case "R": reportsScreen(); break;
                case "H": running = false; break;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void showLedgerScreen() {
        System.out.println("Ledger Screen");
        System.out.println("A) All Transactions");
        System.out.println("D) Deposits");
        System.out.println("P) Payments");
        System.out.println("R) Reports");
        System.out.println("H) Home");
    }

    public static void displayTransactions(String type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            List<String> transactions = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) transactions.add(line);

            Collections.reverse(transactions);

            transactions.stream()
                    .filter(transaction -> filterTransaction(transaction, type))
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.out.println("Error reading the file.");
        }
    }

    private static boolean filterTransaction(String transaction, String type) {
        String[] parts = transaction.split("\\|");
        double amount = Double.parseDouble(parts[4]);

        return "all".equals(type) ||
                ("deposit".equals(type) && amount > 0) ||
                ("payment".equals(type) && amount < 0);
    }

    public static void reportsScreen() {
        boolean running = true;
        while (running) {
            showReportsScreen();
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "1": displayMonthToDate(); break;
                case "2": displayPreviousMonth(); break;
                case "3": displayYearToDate(); break;
                case "4": displayPreviousYear(); break;
                case "5": searchByVendor(); break;
                case "0": running = false; break;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void showReportsScreen() {
        System.out.println("Reports Screen");
        System.out.println("1) Month To Date");
        System.out.println("2) Previous Month");
        System.out.println("3) Year To Date");
        System.out.println("4) Previous Year");
        System.out.println("5) Search by Vendor");
        System.out.println("0) Back");
    }

    public static void displayMonthToDate() {
        System.out.println("Displaying Month to Date transactions");
        // Add logic for month-to-date transactions
    }

    public static void displayPreviousMonth() {
        System.out.println("Displaying Previous Month transactions");
        // Add logic for previous month transactions
    }

    public static void displayYearToDate() {
        System.out.println("Displaying Year to Date transactions");
        // Add logic for year-to-date transactions
    }

    public static void displayPreviousYear() {
        System.out.println("Displaying Previous Year transactions");
        // Add logic for previous year transactions
    }

    public static void searchByVendor() {
        System.out.print("Enter vendor name to search: ");
        String vendor = scanner.nextLine();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            System.out.println("Transactions for vendor: " + vendor);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[3].equalsIgnoreCase(vendor)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file.");
        }
    }
}