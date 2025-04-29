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
        List<Transaction> results = transactionService.customSearch(firstOfMonth, now, null, null, null);
        results.forEach(System.out::println);
    }

    public void displayPreviousMonth() {
        LocalDate now = LocalDate.now();
        LocalDate firstOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = firstOfLastMonth.withDayOfMonth(firstOfLastMonth.lengthOfMonth());
        List<Transaction> results = transactionService.customSearch(firstOfLastMonth, endOfLastMonth, null, null, null);
        results.forEach(System.out::println);
    }

    public void displayYearToDate() {
        LocalDate now = LocalDate.now();
        LocalDate firstOfYear = now.withDayOfYear(1);
        List<Transaction> results = transactionService.customSearch(firstOfYear, now, null, null, null);
        results.forEach(System.out::println);
    }

    public void displayPreviousYear() {
        LocalDate lastYear = LocalDate.now().minusYears(1);
        LocalDate start = lastYear.withDayOfYear(1);
        LocalDate end = lastYear.withMonth(12).withDayOfMonth(31);
        List<Transaction> results = transactionService.customSearch(start, end, null, null, null);
        results.forEach(System.out::println);
    }
}
