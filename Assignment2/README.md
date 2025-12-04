
# Sales Analytics Stream Processor

A Java application that performs **functional data analysis** on CSV-based sales records using **Streams**, **Lambdas**, and **aggregation operations**. This project demonstrates core Stream API concepts through real-world sales analytics.

---

## 📌 Project Overview

This application reads sales data from a CSV file and performs:

- Functional-style data processing
- Aggregation and grouping
- Stream pipelines
- Lambda-based transformations
- Analytical reporting (revenue, categories, highest sales, averages)

---

## 📂 Project Structure

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── sales_analytics
│   │           ├── App.java                     # Entry point
│   │           ├── model/
│   │           │   └── Sale.java                # POJO representing a transaction
│   │           ├── service/
│   │           │   └── SalesAnalysisService.java # Stream-based business logic
│   │           └── util/
│   │               └── CsvLoader.java           # CSV reader utility
│   └── resources/
│       └── sales_data.csv                       # Input dataset
└── test
    └── java
        └── com
            └── sales_analytics
                └── service/
                    └── SalesAnalysisServiceTest.java  # Unit tests
```

---

## 🛠️ Prerequisites

- **Java 17+**
- **Maven 3.6+**

---

## 🔧 Building the Project

Run:

```bash
mvn clean install
```

---

## ▶️ Running the Application

```bash
mvn exec:java -Dexec.mainClass="com.sales_analytics.App"
```

---

## 📊 Expected Output

```
Loading data...

--- Sales Analysis Report ---


Revenue by Region:
  West: $1400.00
  South: $1250.00
  North: $3050.00
  East: $1950.00

Unique Products List: [Chair, Desk, Headphones, Jeans, Laptop, Monitor, Mouse, T-Shirt, Table]

Avg Quantity per 'Electronics' Transaction: 4.20

Highest Value Sale: Sale{id=1, product='Laptop', qty=2, region='North', total=2400.00}
  Clothing: Jeans ($400.00)
  Electronics: Laptop ($2400.00)
  Furniture: Chair ($750.00)
  
Max Sale per Category:
  Electronics: Laptop ($1250.00)
  Furniture: Table ($500.00)
  Clothing: Jeans ($400.00)
```

---

### Option 1: Visual Demo

``` bash
mvn exec:java -Dexec.mainClass="com.sales_analytics.App"
```

## 🧪 Running Tests

```bash
mvn test
```

---

## 📁 CSV Format

```
id,date,region,category,product,quantity,unit_price
```

---

## ✅ Summary

This project showcases:

- Stream-based data manipulation
- Aggregation and grouping
- Clean and functional Java coding patterns
- Realistic analytical reporting over CSV data  
