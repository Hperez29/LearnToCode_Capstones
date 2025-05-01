package com.pluralsight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class FinancialTracker {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TransactionService transactionService = new TransactionService();
    private static final ReportService reportService = new ReportService(transactionService);

    public static void main(String[] args) {
        showSplashScreen();     // Megaman splash banner
        showMegamanIntro();     // Megaman loading animation

        boolean running = true;
        while (running) {
            System.out.println("\nHome Screen");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            switch (scanner.nextLine().toUpperCase()) {
                case "D" -> transactionService.addTransaction("deposit");
                case "P" -> transactionService.addTransaction("payment");
                case "L" -> ledgerScreen();
                case "X" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void ledgerScreen() {
        boolean inLedger = true;
        while (inLedger) {
            System.out.println("\nLedger Screen");
            System.out.println("A) All Transactions");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            switch (scanner.nextLine().toUpperCase()) {
                case "A" -> transactionService.getFilteredTransactions("all").forEach(System.out::println);
                case "D" -> transactionService.getFilteredTransactions("deposit").forEach(System.out::println);
                case "P" -> transactionService.getFilteredTransactions("payment").forEach(System.out::println);
                case "R" -> reportsScreen();
                case "H" -> inLedger = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void reportsScreen() {
        boolean inReports = true;
        while (inReports) {
            System.out.println("\nReports Screen");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            switch (scanner.nextLine()) {
                case "1" -> reportService.displayMonthToDate();
                case "2" -> reportService.displayPreviousMonth();
                case "3" -> reportService.displayYearToDate();
                case "4" -> reportService.displayPreviousYear();
                case "5" -> {
                    System.out.print("Enter vendor: ");
                    String vendor = scanner.nextLine();
                    transactionService.customSearch(null, null, null, vendor, null).forEach(System.out::println);
                }
                case "6" -> runCustomSearch();
                case "0" -> inReports = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void runCustomSearch() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate start = parseDate(prompt("Start Date (yyyy-MM-dd or blank): "), fmt);
        LocalDate end = parseDate(prompt("End Date (yyyy-MM-dd or blank): "), fmt);
        String desc = prompt("Description (or blank): ");
        String vendor = prompt("Vendor (or blank): ");
        String amountStr = prompt("Amount (or blank): ");
        Double amount = amountStr.isEmpty() ? null : Double.parseDouble(amountStr);

        List<Transaction> results = transactionService.customSearch(
                start,
                end,
                desc.isEmpty() ? null : desc,
                vendor.isEmpty() ? null : vendor,
                amount
        );

        if (results.isEmpty()) System.out.println("No transactions found.");
        else results.forEach(System.out::println);
    }

    private static String prompt(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }

    private static LocalDate parseDate(String input, DateTimeFormatter fmt) {
        if (input.isEmpty()) return null;
        try {
            return LocalDate.parse(input, fmt);
        } catch (Exception e) {
            System.out.println("Invalid date. Skipping.");
            return null;
        }
    }

    // 🎮 Megaman ASCII Splash
    private static void showSplashScreen() {
        System.out.println("""
             ________  ________  ________  ________  ________  ________     
            |\\   __  \\|\\   __  \\|\\   __  \\|\\   __  \\|\\   __  \\|\\   ___  \\   
            \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\\\ \\  \\  
             \\ \\   ____\\ \\   __  \\ \\   ____\\ \\  \\\\\\  \\ \\   __  \\ \\  \\\\ \\  \\ 
              \\ \\  \\___|\\ \\  \\ \\  \\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\ \\  \\ \\  \\\\ \\  \\
               \\ \\__\\    \\ \\__\\ \\__\\ \\__\\    \\ \\_______\\ \\__\\ \\__\\ \\__\\\\ \\__\\
                \\|__|     \\|__|\\|__|\\|__|     \\|_______|\\|__|\\|__|\\|__| \\|__|

              MEGAMAN.EXE - Financial Tracker System Booting...

              Loading modules:
              [█] TransactionService.EXE
              [█] ReportService.EXE
              [█] UI Modules
              [█] Virus Scan... 0 threats found ✅

              All systems GO. Jacking in!

        """);
    }

    // 💥 Megaman loading animation
    private static void showMegamanIntro() {
        System.out.println("Initializing PET System...");
        pause(500);
        System.out.println("🔷 MEGAMAN.EXE - ONLINE 🔷");
        pause(500);
        System.out.println("📡 Connecting to Financial Tracker Mainframe...");
        pause(700);
        System.out.println("💻 Booting Transaction Modules...");
        pause(600);
        System.out.println("🧾 Loading Report Interfaces...");
        pause(600);
        System.out.println("🛡️ Running Virus Scan...");
        pause(1000);
        System.out.println("✅ All Clear! No threats found.");
        pause(500);
        System.out.println("\n🔌 Jacking In...");
        pause(800);
        System.out.println("💥 MEGAMAN.EXE Ready to bust bugs and balance books!\n");
        pause(600);
    }

    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }}