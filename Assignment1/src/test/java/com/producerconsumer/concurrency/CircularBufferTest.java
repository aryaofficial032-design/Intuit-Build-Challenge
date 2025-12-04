package com.producerconsumer.concurrency;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Tests: CircularBuffer (Fixed)")
class CircularBufferTest {

    private CircularBuffer<String> buffer;
    private static final int INITIAL_CAPACITY = 5;

    @BeforeEach
    void init() {
        buffer = new CircularBuffer<>(INITIAL_CAPACITY);
    }

    @Test
    @DisplayName("Verify initialization state")
    void verifyInitialization() {
        assertEquals(INITIAL_CAPACITY, buffer.getBufferLimit());
        assertEquals(0, buffer.getCurrentCount());
        assertTrue(buffer.isEmpty());
        assertFalse(buffer.isFull());
    }

    @Test
    @DisplayName("Verify single produce and consume cycle")
    void verifyBasicCycle() throws InterruptedException {
        buffer.produce("Alpha");
        assertEquals(1, buffer.getCurrentCount());
        assertFalse(buffer.isEmpty());

        String result = buffer.consume();
        assertEquals("Alpha", result);
        assertEquals(0, buffer.getCurrentCount());
        assertTrue(buffer.isEmpty());
    }

    @Test
    @DisplayName("Verify FIFO (First-In-First-Out) ordering")
    void verifyFifoLogic() throws InterruptedException {
        buffer.produce("One");
        buffer.produce("Two");
        buffer.produce("Three");

        assertEquals("One", buffer.consume());
        assertEquals("Two", buffer.consume());
        assertEquals("Three", buffer.consume());
    }

    @Test
    @DisplayName("Verify isFull() status")
    void verifyFullState() throws InterruptedException {
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            buffer.produce("Data-" + i);
        }
        assertTrue(buffer.isFull());
        assertEquals(INITIAL_CAPACITY, buffer.getCurrentCount());
    }

    @Test
    @DisplayName("Verify Null rejection")
    void verifyNullSafety() {
        assertThrows(IllegalArgumentException.class, () -> buffer.produce(null));
    }

    @Test
    @DisplayName("Verify circular index wrapping")
    void verifyCircularWrapping() throws InterruptedException {
        // Fill buffer completely
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            buffer.produce("Old-" + i);
        }

        // Consume 2 items to advance read cursor
        buffer.consume(); // Old-0
        buffer.consume(); // Old-1

        // Produce 2 new items (should wrap around to beginning of array)
        buffer.produce("New-A");
        buffer.produce("New-B");

        // Verify the sequence remains correct
        assertEquals("Old-2", buffer.consume());
        assertEquals("Old-3", buffer.consume());
        assertEquals("Old-4", buffer.consume());
        assertEquals("New-A", buffer.consume());
        assertEquals("New-B", buffer.consume());
    }

    @Test
    @Timeout(2) // Fail if deadlocked > 2 seconds
    @DisplayName("Verify producer blocks when full")
    void verifyProducerBlocking() throws InterruptedException {
        // Fill the buffer
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            buffer.produce("Filler");
        }

        Thread producerThread = new Thread(() -> {
            try {
                buffer.produce("BlockedItem");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        Thread.sleep(100); // Allow thread to start and hit the block

        // It should be blocked (alive but waiting)
        assertTrue(producerThread.isAlive());

        // Consume one to free space
        buffer.consume();

        // Now producer should finish
        producerThread.join(1000);
        assertFalse(producerThread.isAlive());
        assertTrue(buffer.isFull()); // Should be full again
    }

    @Test
    @Timeout(2)
    @DisplayName("Verify consumer blocks when empty")
    void verifyConsumerBlocking() throws InterruptedException {
        Thread consumerThread = new Thread(() -> {
            try {
                buffer.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumerThread.start();
        Thread.sleep(100);

        // Should be blocked waiting for data
        assertTrue(consumerThread.isAlive());

        // Produce data
        buffer.produce("Unblocker");

        // Consumer should finish now
        consumerThread.join(1000);
        assertFalse(consumerThread.isAlive());
    }
}
