package com.sales_analytics.util;

import com.sales_analytics.model.Sale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvLoader {

    public static List<Sale> loadSalesFromResource(String fileName) throws IOException {
        List<Sale> sales = new ArrayList<>();

        InputStream is = CsvLoader.class.getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new IOException("File not found: " + fileName);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            // Skip header and map lines to Sale objects
            sales = br.lines()
                    .skip(1) // Skip CSV header
                    .map(mapToSale)
                    .collect(Collectors.toList());
        }
        return sales;
    }

    // Lambda to map a CSV line to a Sale object
    private static Function<String, Sale> mapToSale = (line) -> {
        String[] p = line.split(",");
        return new Sale(
                Integer.parseInt(p[0]),       // id
                LocalDate.parse(p[1]),        // date
                p[2],                         // region
                p[3],                         // category
                p[4],                         // product
                Integer.parseInt(p[5]),       // quantity
                Double.parseDouble(p[6])      // unitPrice
        );
    };
}