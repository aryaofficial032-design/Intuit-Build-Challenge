package com.sales_analytics;

import com.sales_analytics.model.Sale;
import com.sales_analytics.service.SalesAnalysisService;
import com.sales_analytics.util.CsvLoader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class App {
    public static void main(String[] args) {
        try {
            System.out.println("Loading data...");
            List<Sale> sales = CsvLoader.loadSalesFromResource("sales_data.csv");
            SalesAnalysisService service = new SalesAnalysisService(sales);

            System.out.println("\n--- Sales Analysis Report ---");

            // 1. Total Revenue
            double totalRevenue = service.calculateTotalRevenue();
            System.out.printf("Total Revenue: $%.2f%n", totalRevenue);

            // 2. Revenue by Region
            System.out.println("\nRevenue by Region:");
            Map<String, Double> regionRevenue = service.getRevenueByRegion();
            regionRevenue.forEach((region, revenue) ->
                    System.out.printf("  %s: $%.2f%n", region, revenue));

            // 3. Unique Products
            System.out.println("\nUnique Products List: " + service.getUniqueProducts());

            // 4. Average Quantity for Electronics
            double avgElec = service.getAverageQuantityByCategory("Electronics");
            System.out.printf("\nAvg Quantity per 'Electronics' Transaction: %.2f%n", avgElec);

            // 5. Highest Value Sale
            Optional<Sale> topSale = service.findHighestValueSale();
            topSale.ifPresent(s -> System.out.println("\nHighest Value Sale: " + s));
            //6. Max Sale By Category
            Map<String, Optional<Sale>> maxByCategory = service.getMaxSaleByCategory();
            maxByCategory.forEach((category, saleOpt) -> {
                String saleInfo = saleOpt
                        .map(s -> String.format("%s ($%.2f)", s.getProduct(), s.getTotalRevenue()))
                        .orElse("No Sales");
                System.out.printf("  %s: %s%n", category, saleInfo);
            });

        } catch (IOException e) {
            System.err.println("Error loading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}