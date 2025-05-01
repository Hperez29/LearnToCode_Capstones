package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    private static final String FILE_NAME = "transactions.csv";

    public void addTransaction(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(transaction.toCsv());
            writer.newLine();
            System.out.println("Transaction added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void addTransaction(String type) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();
        String vendor = detectVendor(description);

        System.out.print("Enter amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
            return;
        }

        if (type.equalsIgnoreCase("payment")) {
            amount *= -1;
        }

        Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
        addTransaction(transaction);
    }

    private String detectVendor(String description) {
        String descLower = description.toLowerCase();
        if (descLower.contains("mcduck")) return "Scrooge McDuck";
        if (descLower.contains("donald")) return "Donald Duck";
        if (descLower.contains("goofy")) return "Goofy";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter vendor: ");
        return scanner.nextLine().trim();
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(FILE_NAME);

        // ✅ Ensure file exists
        if (!file.exists()) {
            try {
                file.createNewFile();
                return transactions;
            } catch (IOException e) {
                System.out.println("Could not create transactions file.");
                return transactions;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction transaction = Transaction.fromCsv(line);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return transactions;
    }

    public List<Transaction> getFilteredTransactions(String type) {
        return getAllTransactions().stream()
                .filter(t -> type.equals("all") ||
                             (type.equals("deposit") && t.getAmount() > 0) ||
                             (type.equals("payment") && t.getAmount() < 0))
                .collect(Collectors.toList());
    }

    public List<Transaction> customSearch(LocalDate start, LocalDate end, String desc, String vendor, Double amount) {
        return getAllTransactions().stream()
                .filter(t -> (start == null || !t.getDate().isBefore(start)) &&
                             (end == null || !t.getDate().isAfter(end)) &&
                             (desc == null || t.getDescription().toLowerCase().contains(desc.toLowerCase())) &&
                             (vendor == null || t.getVendor().toLowerCase().contains(vendor.toLowerCase())) &&
                             (amount == null || t.getAmount() == amount))
                .collect(Collectors.toList());
    }}