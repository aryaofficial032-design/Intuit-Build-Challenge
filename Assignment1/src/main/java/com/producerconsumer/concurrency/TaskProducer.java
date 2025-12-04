package com.producerconsumer.concurrency;

import java.util.concurrent.ThreadLocalRandom;

public class TaskProducer implements Runnable {

    private final AbstractBuffer<String> sharedBuffer;
    private final int productionTarget;
    private final long waitTimeMs;
    private final String producerId;

    // Use wildcard '?' to allow any buffer type, then cast internally if needed
    public TaskProducer(AbstractBuffer<?> buffer, int target, long waitTimeMs, String id) {
        // Safe cast as we will be putting Strings into it
        @SuppressWarnings("unchecked")
        AbstractBuffer<String> castedBuffer = (AbstractBuffer<String>) buffer;
        this.sharedBuffer = castedBuffer;
        this.productionTarget = target;
        this.waitTimeMs = waitTimeMs;
        this.producerId = id;
    }

    @Override
    public void run() {
        System.out.println(">> [" + producerId + "] Started.");

        for (int i = 1; i <= productionTarget; i++) {
            try {
                String dataPacket = producerId + "-Data-" + i;

                sharedBuffer.produce(dataPacket);

                System.out.println("   [" + producerId + "] + Generated: " + dataPacket);

                // Simulate processing time
                if (waitTimeMs > 0) {
                    Thread.sleep(waitTimeMs);
                } else {
                    // Small random jitter if no specific wait time set
                    Thread.sleep(ThreadLocalRandom.current().nextInt(10, 50));
                }

            } catch (InterruptedException e) {
                System.err.println("!! [" + producerId + "] Interrupted. Stopping.");
                Thread.currentThread().interrupt(); // Restore interrupted status
                break;
            }
        }
        System.out.println("<< [" + producerId + "] Finished work.");
    }
}
