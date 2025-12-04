package com.producerconsumer.concurrency;

public class App {
    public static void main(String[] args) {
        System.out.println("=== Starting Concurrency Assignment 1 ===\n");

        // Scenario 1: Fixed Buffer blocking logic
        testFixedBuffer();

        System.out.println("\n----------------------------------\n");

        // Scenario 2: Dynamic Buffer resizing logic
        testDynamicBuffer();
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
