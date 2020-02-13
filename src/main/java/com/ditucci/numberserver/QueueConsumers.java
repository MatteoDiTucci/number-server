package com.ditucci.numberserver;

import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.ditucci.numberserver.Queue.POISON_PILL;

@Singleton
public class QueueConsumers {
    private Queue queue;
    private Repository repository;
    private ExecutorService executorService;

    public QueueConsumers(Queue queue, Repository repository) {
        this.queue = queue;
        this.repository = repository;
        this.executorService = Executors.newFixedThreadPool(10); // 10 is an arbitrary number

        startConsumingQueue();
    }

    private void startConsumingQueue() {
        executorService.submit(() -> {
            while (true) {
                try {
                    List<String> numbers = queue.blockingGet();

                    if (POISON_PILL == numbers) {
                        queue.add(POISON_PILL);
                        break;
                    }

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
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) { // 60 seconds seem a safe timeout before terminating the threads
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
