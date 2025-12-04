package com.producerconsumer.concurrency;

public abstract class AbstractBuffer<T> {

    // Standard default size if none provided
    protected static final int DEFAULT_BUFFER_SIZE = 10;

    // Current maximum items the buffer can hold
    protected volatile int bufferLimit;

    // Current number of items in the buffer
    protected volatile int currentCount;

    public AbstractBuffer(int bufferLimit) {
        this.bufferLimit = bufferLimit;
        this.currentCount = 0;
    }


     // @throws InterruptedException if the thread is interrupted while waiting.

    public abstract void produce(T item) throws InterruptedException;

     // @throws InterruptedException if the thread is interrupted while waiting.

    public abstract T consume() throws InterruptedException;

    // Status checks
    public abstract boolean isFull();
    public abstract boolean isEmpty();

    // Getters for monitoring
    public int getCurrentCount() {
        return currentCount;
    }

    public int getBufferLimit() {
        return bufferLimit;
    }
}