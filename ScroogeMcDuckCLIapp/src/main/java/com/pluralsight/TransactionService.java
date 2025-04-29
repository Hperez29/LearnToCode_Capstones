package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
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
            System.out.println("Error writing to file.");
        }
    }

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
    }
}