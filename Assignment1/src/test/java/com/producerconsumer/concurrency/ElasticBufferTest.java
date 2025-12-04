package com.producerconsumer.concurrency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Tests: ElasticBuffer (Dynamic)")
class ElasticBufferTest {

    private ElasticBuffer<String> elasticBuffer;
    private static final int START_SIZE = 4;

    @BeforeEach
    void setup() {
        elasticBuffer = new ElasticBuffer<>(START_SIZE);
    }

    @Test
    @DisplayName("Verify initial capacity settings")
    void verifyStartUp() {
        assertEquals(START_SIZE, elasticBuffer.getBufferLimit());
        assertTrue(elasticBuffer.isEmpty());
    }

    @Test
    @DisplayName("Verify auto-resize trigger at 75% load")
    void verifyResizeLogic() throws InterruptedException {
        // Initial Size: 4.
        // Threshold: 0.75 * 4 = 3 elements.
        // This means when we have 3 elements (75%), the NEXT insert triggers resize.

        elasticBuffer.produce("A"); // 1/4 = 0.25
        elasticBuffer.produce("B"); // 2/4 = 0.50
        elasticBuffer.produce("C"); // 3/4 = 0.75 -> Threshold hit!

        // Capacity should still be 4 right now (resize happens ON next produce)
        assertEquals(START_SIZE, elasticBuffer.getBufferLimit());

        // This produce call should trigger resize before inserting
        elasticBuffer.produce("D_Trigger");

        // New Capacity calculation: 4 * 2.0 = 8
        int expectedNewCapacity = 8;

        assertEquals(expectedNewCapacity, elasticBuffer.getBufferLimit());
        assertEquals(4, elasticBuffer.getCurrentCount());
    }

    @Test
    @DisplayName("Verify data integrity during resize")
    void verifyDataIntegrity() throws InterruptedException {
        // Force a wrap-around scenario before resize
        // Fill 4
        elasticBuffer.produce("1");
        elasticBuffer.produce("2");
        elasticBuffer.produce("3");
        elasticBuffer.produce("4-Trigger"); // Resizes to 8 here

        // Current: [1, 2, 3, 4-Trigger, null, null, null, null]

        // Consume 2
        assertEquals("1", elasticBuffer.consume());
        assertEquals("2", elasticBuffer.consume());

        // Add more to verify order persists
        elasticBuffer.produce("5");
        elasticBuffer.produce("6");

        // Check remaining order
        assertEquals("3", elasticBuffer.consume());
        assertEquals("4-Trigger", elasticBuffer.consume());
        assertEquals("5", elasticBuffer.consume());
        assertEquals("6", elasticBuffer.consume());
    }

    @Test
    @DisplayName("Verify multiple consecutive resizes")
    void verifyMultipleExpansions() throws InterruptedException {
        // Start size 4.
        // 1. Resize to 8.
        // 2. Resize to 16.
        // 3. Resize to 32.

        int itemsToAdd = 30;

        for (int i = 0; i < itemsToAdd; i++) {
            elasticBuffer.produce("Item-" + i);
        }

        // Capacity should be at least 32 to hold 30 items
        assertTrue(elasticBuffer.getBufferLimit() >= 30);
        assertEquals(30, elasticBuffer.getCurrentCount());

        // Drain and check
        for (int i = 0; i < itemsToAdd; i++) {
            assertEquals("Item-" + i, elasticBuffer.consume());
        }
    }
}
