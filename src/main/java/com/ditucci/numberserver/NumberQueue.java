package com.ditucci.numberserver;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class NumberQueue {
    public static List<String> POISON_PILL = List.of();
    private BlockingQueue<List<String>> queue;

    public NumberQueue(BlockingQueue<List<String>> queue) {
        this.queue = queue;
    }

    public List<String> blockingGet() throws InterruptedException {
        return queue.take();
    }

    public boolean add(List<String> numbers) {
        try {
            return queue.add(numbers);
        } catch (IllegalStateException exception) {
            return false;
        }
    }
}
