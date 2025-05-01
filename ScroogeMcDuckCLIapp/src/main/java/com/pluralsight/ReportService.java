package com.pluralsight;

import java.time.LocalDate;
import java.util.List;

public class ReportService {
    private final TransactionService transactionService;

    public ReportService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void displayMonthToDate() {
        LocalDate now = LocalDate.now();
        LocalDate firstOfMonth = now.withDayOfMonth(1);
        displayResults(transactionService.customSearch(firstOfMonth, now, null, null, null));
    }

    public void displayPreviousMonth() {
        LocalDate now = LocalDate.now();
        LocalDate firstOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = firstOfLastMonth.withDayOfMonth(firstOfLastMonth.lengthOfMonth());
        displayResults(transactionService.customSearch(firstOfLastMonth, endOfLastMonth, null, null, null));
    }

    public void displayYearToDate() {
        LocalDate now = LocalDate.now();
        LocalDate firstOfYear = now.withDayOfYear(1);
        displayResults(transactionService.customSearch(firstOfYear, now, null, null, null));
    }

    public void displayPreviousYear() {
        LocalDate lastYear = LocalDate.now().minusYears(1);
        LocalDate start = lastYear.withDayOfYear(1);
        LocalDate end = lastYear.withMonth(12).withDayOfMonth(31);
        displayResults(transactionService.customSearch(start, end, null, null, null));
    }

    private void displayResults(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for the selected period.");
        } else {
            transactions.forEach(System.out::println);
        }
    }
}