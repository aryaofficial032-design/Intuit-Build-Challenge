# Producer-Consumer Pattern with Custom Blocking Buffers

A robust Java implementation of the producer-consumer pattern featuring
custom thread synchronization, circular buffering, and dynamic array
resizing. This project demonstrates core concurrency concepts without
relying on `java.util.concurrent.BlockingQueue`.

## 📌 Project Overview

This project satisfies the **Assignment 1** requirements by
implementing:

-   **Thread Synchronization:** Manual control using `synchronized`,
    `wait()`, and `notifyAll()`.
-   **Custom Data Structures:**
    -   `CircularBuffer`: A fixed-size, memory-efficient circular queue.
    -   `ElasticBuffer`: A dynamic queue that auto-resizes when load
        exceeds 75%.
-   **Concurrent Actors:** Configurable Producer and Consumer threads
    implementing `Runnable`.
-   **Testing:** A comprehensive JUnit 5 test suite covering edge cases,
    resizing logic, and multi-threaded integration scenarios.

## 📂 Project Structure

    Java-Concurrency-Assignments/
    ├── Assignment1/
    │   ├── pom.xml
    │   ├── src/
    │   │   ├── main/java/com/producerconsumer/concurrency/
    │   │   │   ├── AbstractBuffer.java
    │   │   │   ├── CircularBuffer.java
    │   │   │   ├── ElasticBuffer.java
    │   │   │   ├── TaskProducer.java
    │   │   │   ├── TaskConsumer.java
    │   │   │   └── App.java
    │   │   └── test/java/com/producerconsumer/concurrency/
    │   │       ├── CircularBufferTest.java
    │   │       ├── ElasticBufferTest.java
    │   │       └── SimulationTest.java
    │   └── TESTING_INSTRUCTIONS.md
    ├── Assignment2/
    ├── .gitignore
    └── README.md

## 🧱 Core Components

### 1. Data Structures

-   **AbstractBuffer.java**
    -   Defines contract (`produce`, `consume`, `isFull`, `isEmpty`).
    -   Shared state with `volatile` variables.
-   **CircularBuffer.java**
    -   Implements fixed-size circular queue with O(1) operations.
    -   Blocking behavior with `wait()` and `notifyAll()`.
-   **ElasticBuffer.java**
    -   Extends `CircularBuffer`, doubling capacity when usage ≥ 75%.
    -   Maintains FIFO order upon resizing.

### 2. Workers

-   **TaskProducer.java** − Generates data packets.
-   **TaskConsumer.java** − Processes packets.

### 3. Entry Point

-   **App.java** − Visual demo of 1 Producer ↔ 1 Consumer.

## 🚀 Installation & Setup

### Prerequisites

-   Java 8+
-   Maven 3.6+

### Steps

``` bash
git clone https://github.com/aryaofficial032-design/Intuit-Build-Challenge.git
cd Intuit-Build-Challenge/Assignment1
mvn clean package
```

## 🏃 Running the Application

### Option 1: Visual Demo

``` bash
mvn exec:java -Dexec.mainClass="com.producerconsumer.concurrency.App"
```

### Option 2: Run Tests

``` bash
mvn test
```

Specific tests:

``` bash
mvn -Dtest=CircularBufferTest test
mvn -Dtest=ElasticBufferTest test
mvn -Dtest=SimulationTest test
```

## 📊 Sample Output

    === Producer-Consumer Application Started ===
    >> Mode: Visual Demonstration
    >> [Demo-Producer] Started.
    >> [Demo-Consumer] Ready to process.
    [Demo-Producer] + Generated: Demo-Producer-Data-1
    [Demo-Consumer] - Processed: Demo-Producer-Data-1
    [Demo-Producer] + Generated: Demo-Producer-Data-2
    [Demo-Consumer] - Processed: Demo-Producer-Data-2
    [Demo-Producer] + Generated: Demo-Producer-Data-3
    [Demo-Producer] + Generated: Demo-Producer-Data-4
    [Demo-Consumer] - Processed: Demo-Producer-Data-3
    [Demo-Producer] + Generated: Demo-Producer-Data-5
    [Demo-Producer] + Generated: Demo-Producer-Data-6
    [Demo-Consumer] - Processed: Demo-Producer-Data-4
    [Demo-Producer] + Generated: Demo-Producer-Data-7
    [Demo-Producer] + Generated: Demo-Producer-Data-8
    [Demo-Consumer] - Processed: Demo-Producer-Data-5
    [Demo-Producer] + Generated: Demo-Producer-Data-9
    [Demo-Producer] + Generated: Demo-Producer-Data-10
    [Demo-Consumer] - Processed: Demo-Producer-Data-6
    << [Demo-Producer] Finished work.
    [Demo-Consumer] - Processed: Demo-Producer-Data-7
    [Demo-Consumer] - Processed: Demo-Producer-Data-8
    [Demo-Consumer] - Processed: Demo-Producer-Data-9
    [Demo-Consumer] - Processed: Demo-Producer-Data-10
    << [Demo-Consumer] All tasks completed.
    === Demonstration Complete ===



    
