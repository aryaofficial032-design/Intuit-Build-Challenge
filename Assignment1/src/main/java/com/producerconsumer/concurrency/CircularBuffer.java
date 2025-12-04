package com.producerconsumer.concurrency;

public class CircularBuffer<T> extends AbstractBuffer<T> {

    protected Object[] bufferArray;
    protected int readCursor;  // Points to the head (removal end)
    protected int writeCursor; // Points to the tail (insertion end)

    public CircularBuffer(int size) {
        super(size > 0 ? size : DEFAULT_BUFFER_SIZE);
        this.bufferArray = new Object[this.bufferLimit];
        this.readCursor = 0;
        this.writeCursor = -1;
    }

    public CircularBuffer() {
        this(DEFAULT_BUFFER_SIZE);
    }

    @Override
    public synchronized void produce(T item) throws InterruptedException {
        if (item == null) {
            throw new IllegalArgumentException("Cannot produce null items.");
        }

        // Critical Section: Wait while the buffer is at capacity
        while (isFull()) {
            System.out.println("[Buffer Monitor] Queue full. Producer is waiting...");
            wait(); // Releases lock and waits for notification
        }

        // Circular increment logic
        writeCursor = (writeCursor + 1) % bufferLimit;
        bufferArray[writeCursor] = item;
        currentCount++;

        // Notify potential consumers that data is available
        notifyAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized T consume() throws InterruptedException {
        // Critical Section: Wait while the buffer is empty
        while (isEmpty()) {
            System.out.println("[Buffer Monitor] Queue empty. Consumer is waiting...");
            wait(); // Releases lock and waits for notification
        }

        T item = (T) bufferArray[readCursor];
        bufferArray[readCursor] = null; // Clear reference for GC

        // Circular increment logic
        readCursor = (readCursor + 1) % bufferLimit;
        currentCount--;

        // Notify potential producers that space is available
        notifyAll();
        return item;
    }

    @Override
    public synchronized boolean isFull() {
        // If bufferLimit is positive, check if count equals it
        return bufferLimit > 0 && currentCount == bufferLimit;
    }

    @Override
    public synchronized boolean isEmpty() {
        return currentCount == 0;
    }
}
