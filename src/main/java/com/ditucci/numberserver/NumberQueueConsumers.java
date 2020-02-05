package com.ditucci.numberserver;

import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Singleton
public class NumberQueueConsumers {
    private NumberQueue queue;
    private NumberRepository repository;
    private ExecutorService executorService;

    public NumberQueueConsumers(NumberQueue queue, NumberRepository repository) {
        this.queue = queue;
        this.repository = repository;
        this.executorService = Executors.newFixedThreadPool(10);

        startConsumingQueue();
    }

    private void startConsumingQueue() {
        executorService.submit(() -> {
            while (true) {
                try {
                    List<String> numbers = queue.blockingGet();
                    repository.save(numbers);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public void shutdownGracefully() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
