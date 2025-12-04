package com.producerconsumer.concurrency;

public class App {
    public static void main(String[] args) {
        System.out.println("=== Producer-Consumer Application Started ===");
        System.out.println(">> Mode: Visual Demonstration");

        // 1. Create a fixed-size buffer (Capacity 5)
        AbstractBuffer<String> demoBuffer = new CircularBuffer<>(5);

        // 2. Create Workers
        // Producer: Generates 10 items, fast (50ms delay)
        // Consumer: Consumes 10 items, slow (100ms delay) to force the producer to wait
        Thread producer = new Thread(new TaskProducer(demoBuffer, 10, 50, "Demo-Producer"));
        Thread consumer = new Thread(new TaskConsumer(demoBuffer, 10, 100, "Demo-Consumer"));

        // 3. Start Threads
        producer.start();
        consumer.start();

        // 4. Wait for them to finish
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted.");
        }

        System.out.println("=== Demonstration Complete ===");
    }

    private static void testFixedBuffer() {
        System.out.println(">>> Scenario 1: Fixed Circular Buffer (Size 3) <<<");
        // Small size to force blocking quickly
        AbstractBuffer<String> buffer = new CircularBuffer<>(3);

        // Producer creates 5 items, Consumer takes 5 items
        Thread p1 = new Thread(new TaskProducer(buffer, 5, 100, "Prod-Fixed"));
        Thread c1 = new Thread(new TaskConsumer(buffer, 5, 300, "Cons-Fixed"));
        // Note: Consumer is slower (300ms) than Producer (100ms) to force "Queue Full" wait

        p1.start();
        c1.start();

        joinThreads(p1, c1);
    }

    private static void testDynamicBuffer() {
        System.out.println(">>> Scenario 2: Dynamic Elastic Buffer (Initial Size 2) <<<");
        // Start very small to force resize
        AbstractBuffer<String> buffer = new ElasticBuffer<>(2);

        // Producer creates 10 items very fast (50ms)
        // Consumer is slow, causing accumulation and forcing resize
        Thread p1 = new Thread(new TaskProducer(buffer, 10, 50, "Prod-Dynamic"));
        Thread c1 = new Thread(new TaskConsumer(buffer, 10, 200, "Cons-Dynamic"));

        p1.start();
        c1.start();

        joinThreads(p1, c1);
    }

    private static void joinThreads(Thread... threads) {
        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
