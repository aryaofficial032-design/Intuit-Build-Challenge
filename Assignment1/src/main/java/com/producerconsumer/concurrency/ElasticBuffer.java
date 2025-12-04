package com.producerconsumer.concurrency;

public class ElasticBuffer<T> extends CircularBuffer<T> {

    // Threshold changed to 75% for better performance
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    // Growth factor changed to 2.0 (Double size)
    private static final double EXPANSION_MULTIPLIER = 2.0;

    public ElasticBuffer(int initialSize) {
        super(initialSize);
    }

    public ElasticBuffer() {
        super(DEFAULT_BUFFER_SIZE);
    }

    @Override
    public synchronized void produce(T item) throws InterruptedException {
        // Check load factor before producing
        double currentLoad = (double) currentCount / bufferLimit;

        if (currentLoad >= LOAD_FACTOR_THRESHOLD) {
            expandBuffer();
        }

        // Delegate to parent for actual insertion logic
        super.produce(item);
    }

    /**
     * Expands the internal array and realigns the circular indices.
     */
    private void expandBuffer() {
        int oldLimit = bufferLimit;
        int newLimit = (int) (oldLimit * EXPANSION_MULTIPLIER);

        Object[] newArray = new Object[newLimit];

        // Unwrap the circular array into the new linear array
        for (int i = 0; i < currentCount; i++) {
            newArray[i] = bufferArray[(readCursor + i) % oldLimit];
        }

        this.bufferArray = newArray;
        this.bufferLimit = newLimit;

        // Reset cursors to linear positions
        this.readCursor = 0;
        this.writeCursor = currentCount - 1;

        System.out.printf("[Buffer Monitor] ** Resizing ** Capacity increased from %d to %d%n", oldLimit, newLimit);
    }
}
