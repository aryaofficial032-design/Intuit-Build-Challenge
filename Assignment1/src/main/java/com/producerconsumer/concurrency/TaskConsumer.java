package com.producerconsumer.concurrency;

public class TaskConsumer implements Runnable {

    private final AbstractBuffer<String> sharedBuffer;
    private final int consumptionTarget;
    private final long processingTimeMs;
    private final String consumerId;

    public TaskConsumer(AbstractBuffer<?> buffer, int target, long timeMs, String id) {
        @SuppressWarnings("unchecked")
        AbstractBuffer<String> castedBuffer = (AbstractBuffer<String>) buffer;
        this.sharedBuffer = castedBuffer;
        this.consumptionTarget = target;
        this.processingTimeMs = timeMs;
        this.consumerId = id;
    }

    @Override
    public void run() {
        System.out.println(">> [" + consumerId + "] Ready to process.");

        for (int i = 0; i < consumptionTarget; i++) {
            try {
                String data = sharedBuffer.consume();

                System.out.println("   [" + consumerId + "] - Processed: " + data);

                if (processingTimeMs > 0) {
                    Thread.sleep(processingTimeMs);
                }

            } catch (InterruptedException e) {
                System.err.println("!! [" + consumerId + "] Interrupted. Stopping.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("<< [" + consumerId + "] All tasks completed.");
    }
}
