package com.sales_analytics.service;

import com.sales_analytics.model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SalesAnalysisServiceTest {

    private SalesAnalysisService service;

    @BeforeEach
    void setUp() {
        // Create mock data for testing to avoid file I/O dependency
        List<Sale> mockSales = Arrays.asList(
                new Sale(1, LocalDate.now(), "North", "Electronics", "Laptop", 1, 1000.00), // Total: 1000
                new Sale(2, LocalDate.now(), "North", "Furniture", "Chair", 4, 50.00),      // Total: 200
                new Sale(3, LocalDate.now(), "South", "Electronics", "Mouse", 2, 25.00),    // Total: 50
                new Sale(4, LocalDate.now(), "South", "Furniture", "Table", 1, 200.00),     // Total: 200
                new Sale(5, LocalDate.now(), "West", "Clothing", "Shirt", 10, 10.00)        // Total: 100
        );
        service = new SalesAnalysisService(mockSales);
    }

    @Test
    @DisplayName("Should calculate total revenue correctly")
    void testCalculateTotalRevenue() {
        // 1000 + 200 + 50 + 200 + 100 = 1550
        assertEquals(1550.00, service.calculateTotalRevenue(), 0.01);
    }

    @Test
    @DisplayName("Should return unique products sorted alphabetically")
    void testGetUniqueProducts() {
        List<String> products = service.getUniqueProducts();
        assertEquals(5, products.size());
        assertEquals("Chair", products.get(0));
        assertEquals("Table", products.get(4));
    }

    @Test
    @DisplayName("Should group revenue by region")
    void testGetRevenueByRegion() {
        Map<String, Double> result = service.getRevenueByRegion();

        assertEquals(1200.00, result.get("North"), 0.01); // 1000 + 200
        assertEquals(250.00, result.get("South"), 0.01);  // 50 + 200
        assertEquals(100.00, result.get("West"), 0.01);   // 100
    }

    @Test
    @DisplayName("Should find the highest value sale transaction")
    void testFindHighestValueSale() {
        Optional<Sale> highest = service.findHighestValueSale();

        assertTrue(highest.isPresent());
        assertEquals("Laptop", highest.get().getProduct());
        assertEquals(1000.00, highest.get().getTotalRevenue(), 0.01);
    }

    @Test
    @DisplayName("Should calculate average quantity for specific category")
    void testGetAverageQuantityByCategory() {
        // Electronics: Laptop (1) + Mouse (2) = 3 total qty / 2 transactions = 1.5
        double avg = service.getAverageQuantityByCategory("Electronics");
        assertEquals(1.5, avg, 0.01);
    }

    @Test
    @DisplayName("Should return 0 average for non-existent category")
    void testGetAverageQuantityByInvalidCategory() {
        double avg = service.getAverageQuantityByCategory("Toys");
        assertEquals(0.0, avg, 0.01);
    }
}