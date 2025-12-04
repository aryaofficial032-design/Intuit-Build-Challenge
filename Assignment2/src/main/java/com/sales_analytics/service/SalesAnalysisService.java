package com.sales_analytics.service;

import com.sales_analytics.model.Sale;

import java.util.*;
import java.util.stream.Collectors;


public class SalesAnalysisService {

    private final List<Sale> salesData;

    public SalesAnalysisService(List<Sale> salesData) {
        this.salesData = salesData;
    }


    public double calculateTotalRevenue() {
        return salesData.stream()
                .mapToDouble(Sale::getTotalRevenue)
                .sum();
    }

    public List<String> getUniqueProducts() {
        return salesData.stream()
                .map(Sale::getProduct)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }


    public Map<String, Double> getRevenueByRegion() {
        return salesData.stream()
                .collect(Collectors.groupingBy(
                        Sale::getRegion,
                        Collectors.summingDouble(Sale::getTotalRevenue)
                ));
    }


    public Optional<Sale> findHighestValueSale() {
        return salesData.stream()
                .max(Comparator.comparingDouble(Sale::getTotalRevenue));
    }

    public double getAverageQuantityByCategory(String category) {
        return salesData.stream()
                .filter(s -> s.getCategory().equalsIgnoreCase(category))
                .mapToInt(Sale::getQuantity)
                .average()
                .orElse(0.0);
    }

    public Map<String, Optional<Sale>> getTopSaleByCategory() {
        return salesData.stream()
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.maxBy(Comparator.comparingDouble(Sale::getTotalRevenue))
                ));
    }

    public Map<String, Optional<Sale>> getMaxSaleByCategory() {
        return salesData.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Sale::getCategory,
                        Collectors.maxBy(Comparator.comparingDouble(Sale::getTotalRevenue))
                ));
    }
}