package com.sales_analytics.model;

import java.time.LocalDate;


public class Sale {
    private int id;
    private LocalDate date;
    private String region;
    private String category;
    private String product;
    private int quantity;
    private double unitPrice;

    public Sale(int id, LocalDate date, String region, String category, String product, int quantity, double unitPrice) {
        this.id = id;
        this.date = date;
        this.region = region;
        this.category = category;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters
    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getRegion() { return region; }
    public String getCategory() { return category; }
    public String getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }

    // Computed property
    public double getTotalRevenue() {
        return quantity * unitPrice;
    }

    @Override
    public String toString() {
        return String.format("Sale{id=%d, product='%s', qty=%d, region='%s', total=%.2f}",
                id, product, quantity, region, getTotalRevenue());
    }
}