package com.producerconsumer.concurrency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Integration Tests: Multi-threaded Scenarios")
class SimulationTest {

    @Test
    @DisplayName("Scenario: 1 Producer -> 1 Consumer")
    void testOneToOne() throws InterruptedException {
        AbstractBuffer<String> buffer = new CircularBuffer<>(5);

        Thread p = new Thread(new TaskProducer(buffer, 10, 0, "P1"));
        Thread c = new Thread(new TaskConsumer(buffer, 10, 0, "C1"));

        p.start();
        c.start();

        p.join(5000);
        c.join(5000);

        assertTrue(buffer.isEmpty(), "Buffer should be empty after processing");
    }

    @Test
    @DisplayName("Scenario: 3 Producers -> 1 Consumer (High Contention)")
    void testManyProducers() throws InterruptedException {
        // Small buffer to force frequent blocking
        AbstractBuffer<String> buffer = new CircularBuffer<>(3);

        int itemsPerProd = 5;
        // 3 producers * 5 items = 15 total items
        Thread p1 = new Thread(new TaskProducer(buffer, itemsPerProd, 10, "P1"));
        Thread p2 = new Thread(new TaskProducer(buffer, itemsPerProd, 10, "P2"));
        Thread p3 = new Thread(new TaskProducer(buffer, itemsPerProd, 10, "P3"));

        // Consumer must consume 15 items total
        Thread c1 = new Thread(new TaskConsumer(buffer, itemsPerProd * 3, 10, "C1"));

        startAll(p1, p2, p3, c1);
        joinAll(p1, p2, p3, c1);

        assertTrue(buffer.isEmpty());
    }

    @Test
    @DisplayName("Scenario: 1 Producer -> 3 Consumers (Starvation Check)")
    void testManyConsumers() throws InterruptedException {
        AbstractBuffer<String> buffer = new CircularBuffer<>(5);

        int totalItems = 15;
        // 1 Producer makes 15 items
        Thread p1 = new Thread(new TaskProducer(buffer, totalItems, 5, "P1"));

        // 3 Consumers take 5 each
        Thread c1 = new Thread(new TaskConsumer(buffer, 5, 10, "C1"));
        Thread c2 = new Thread(new TaskConsumer(buffer, 5, 10, "C2"));
        Thread c3 = new Thread(new TaskConsumer(buffer, 5, 10, "C3"));

        startAll(p1, c1, c2, c3);
        joinAll(p1, c1, c2, c3);

        assertTrue(buffer.isEmpty());
    }

    @Test
    @DisplayName("Scenario: Fast Producer -> Slow Consumer")
    void testBackpressure() throws InterruptedException {
        AbstractBuffer<String> buffer = new CircularBuffer<>(2);

        // Producer runs with 0 delay (fast)
        // Consumer runs with 50ms delay (slow)
        Thread p = new Thread(new TaskProducer(buffer, 10, 0, "FastP"));
        Thread c = new Thread(new TaskConsumer(buffer, 10, 50, "SlowC"));

        long start = System.currentTimeMillis();

        startAll(p, c);
        joinAll(p, c);

        long duration = System.currentTimeMillis() - start;

        // Since consumer sleeps 50ms * 10 items = 500ms, total time must be > 500ms
        assertTrue(duration >= 500, "Simulation finished too fast, backpressure failed?");
        assertTrue(buffer.isEmpty());
    }

    @Test
    @DisplayName("Scenario: Deadlock Safety Check (Capacity 1)")
    void testDeadlockSafety() throws InterruptedException {
        // Capacity 1 is the highest risk for deadlocks
        AbstractBuffer<String> buffer = new CircularBuffer<>(1);

        Thread p1 = new Thread(new TaskProducer(buffer, 20, 2, "P1"));
        Thread c1 = new Thread(new TaskConsumer(buffer, 20, 2, "C1"));

        startAll(p1, c1);

        // If they don't finish in 2 seconds, we likely have a deadlock
        p1.join(2000);
        c1.join(2000);

        assertFalse(p1.isAlive(), "Producer stuck (Deadlock)");
        assertFalse(c1.isAlive(), "Consumer stuck (Deadlock)");
    }

    @Test
    @DisplayName("Scenario: Dynamic Buffer resizing under load")
    void testDynamicLoad() throws InterruptedException {
        AbstractBuffer<String> buffer = new ElasticBuffer<>(2);

        // Produce 20 items. Initial size 2 will force multiple resizes.
        Thread p = new Thread(new TaskProducer(buffer, 20, 0, "DynP"));

        // This forces items to accumulate in the buffer, triggering the resize.
        Thread c = new Thread(new TaskConsumer(buffer, 20, 100, "DynC"));

        startAll(p, c);
        joinAll(p, c);

        // If we reach here without exceptions, resizing worked in a threaded context
        assertTrue(buffer.isEmpty());
        // Verify it actually grew (Start 2 -> ... -> >2)
        assertTrue(buffer.getBufferLimit() > 2, "Buffer did not resize! Consumer might have been too fast.");
    }

    // Helper methods to reduce boilerplate
    private void startAll(Thread... threads) {
        for (Thread t : threads) t.start();
    }

    private void joinAll(Thread... threads) throws InterruptedException {
        for (Thread t : threads) t.join(10000); // 10s global timeout safety
    }
}