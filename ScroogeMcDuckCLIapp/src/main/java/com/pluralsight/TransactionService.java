package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    private static final String FILE_NAME = "transactions.csv";

    // Existing method to add a transaction directly
    public void addTransaction(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(transaction.toCsv());
            writer.newLine();
            System.out.println("Transaction added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    // ✅ New method to add a transaction via user input (type = "deposit" or "payment")
    public void addTransaction(String type) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        String vendor = detectVendor(description);

        System.out.print("Enter amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
            return;
        }

        // Payments are negative amounts
        if (type.equalsIgnoreCase("payment")) {
            amount *= -1;
        }

        Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
        addTransaction(transaction);
    }

    // ✅ Helper method to auto-detect vendor or ask user
    private String detectVendor(String description) {
        String descLower = description.toLowerCase();
        if (descLower.contains("mcduck")) return "Scrooge McDuck";
        if (descLower.contains("donald")) return "Donald Duck";
        if (descLower.contains("goofy")) return "Goofy";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter vendor: ");
        return scanner.nextLine();
    }

    // Reads all transactions from the file
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction transaction = Transaction.fromCsv(line);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file.");
        }
        return transactions;
    }

    // Filters by type: all, deposit, or payment
    public List<Transaction> getFilteredTransactions(String type) {
        return getAllTransactions().stream()
                .filter(t -> type.equals("all") ||
                        (type.equals("deposit") && t.getAmount() > 0) ||
                        (type.equals("payment") && t.getAmount() < 0))
                .collect(Collectors.toList());
    }

    // Custom search by start/end date, description, vendor, and amount
    public List<Transaction> customSearch(LocalDate start, LocalDate end, String desc, String vendor, Double amount) {
        return getAllTransactions().stream()
                .filter(t -> (start == null || !t.getDate().isBefore(start)) &&
                        (end == null || !t.getDate().isAfter(end)) &&
                        (desc == null || t.getDescription().toLowerCase().contains(desc.toLowerCase())) &&
                        (vendor == null || t.getVendor().toLowerCase().contains(vendor.toLowerCase())) &&
                        (amount == null || t.getAmount() == amount))
                .collect(Collectors.toList());
    }
}