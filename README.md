# Build Challenge -- Combined Assignments Repository

This repository contains **two complete Java-based Build Challenge
assignments**, each demonstrating different core programming
competencies: **concurrent programming** and **functional data analytics
using Streams**.

Both projects are fully implemented with: - Production-ready source
code - Unit tests - Console output demonstrations - Individual project
READMEs inside each assignment folder

------------------------------------------------------------------------

## 📁 Repository Structure

    Build-Challenge-Repo/
    ├── Assignment1/   # Producer-Consumer Concurrency Project
    │   └── README.md
    ├── Assignment2/   # Sales Data Stream Analytics Project
    │   └── README.md
    └── README.md      # (This file)

------------------------------------------------------------------------

# ✅ Assignment 1 -- Producer Consumer with Thread Synchronization

### 🔹 Objective

Implement a **classic Producer-Consumer pattern** using **manual thread
synchronization** without relying on built-in `BlockingQueue`.

### 🔹 Key Concepts Demonstrated

-   Thread synchronization using `synchronized`, `wait()`, and
    `notifyAll()`
-   Custom blocking buffers
-   Circular queue implementation
-   Dynamic buffer resizing
-   Multi-threaded integration testing

### 🔹 Core Features

-   `CircularBuffer` -- Fixed-size blocking queue
-   `ElasticBuffer` -- Auto-resizing queue when load exceeds 75%
-   `TaskProducer` -- Generates tasks
-   `TaskConsumer` -- Processes tasks
-   `App` -- Runs live producer-consumer simulation

### 🔹 Testing

-   Full JUnit 5 coverage
-   Thread safety validation
-   Resizing and blocking behavior tests

### 🔹 Run Instructions

``` bash
cd Assignment1
mvn clean package
mvn exec:java -Dexec.mainClass="com.producerconsumer.concurrency.App"
```

------------------------------------------------------------------------

# ✅ Assignment 2 -- Sales Data Analytics Using Streams

### 🔹 Objective

Perform **CSV-based data analysis** using **Java Streams and functional
programming**.

### 🔹 Key Concepts Demonstrated

-   Stream pipelines
-   Lambda expressions
-   Filtering, grouping, reduction
-   Aggregation analytics
-   CSV parsing

### 🔹 Core Features

-   Reads sales data from `sales_data.csv`
-   Calculates:
    -   Total revenue
    -   Revenue by region
    -   Unique products
    -   Average category quantity
    -   Highest value transactions
    -   Max sale per category

### 🔹 Testing

-   Full unit test coverage for analytics methods

### 🔹 Run Instructions

``` bash
cd Assignment2
mvn clean install
mvn exec:java -Dexec.mainClass="com.sales_analytics.App"
```

------------------------------------------------------------------------

## 🛠 Prerequisites (For Both Projects)

-   Java 8+ (Java 17 recommended)
-   Maven 3.6+
-   Git

------------------------------------------------------------------------

## 📌 Deliverables Checklist

✅ Public GitHub Repository\
✅ Complete Source Code\
✅ Unit Tests for All Logic\
✅ Console Output Demonstrations\
✅ Individual Assignment READMEs\
✅ Combined Repository README

------------------------------------------------------------------------

## 📊 Sample Output Summary

### Assignment 1

    [Producer] Generated: Data-1
    [Consumer] Processed: Data-1
    [Buffer] Queue empty. Waiting...

### Assignment 2

    Total Revenue: $4875.00
    Revenue by Region:
    North: $1600.00
    East: $1700.00
    Highest Sale: Laptop - $1250.00

------------------------------------------------------------------------

## 🏁 Final Notes

This repository showcases: - **Low-level thread synchronization** -
**Enterprise-grade Stream-based analytics** - **Clean architecture** -
**Fully test-driven development** - **Professional GitHub-ready
structure**

------------------------------------------------------------------------

✅ Ready for evaluation and enterprise code review.
