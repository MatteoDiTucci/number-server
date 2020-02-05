package com.ditucci.numberserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class NumberQueueTest {

    LinkedBlockingQueue<List<String>> linkedBlockingQueue;
    private NumberQueue numberQueue;


    @BeforeEach
    void setUp() {
        linkedBlockingQueue = Mockito.mock(LinkedBlockingQueue.class);
        numberQueue = new NumberQueue(linkedBlockingQueue);
    }

    @Test
    void returnsNumbersWhenQueueHasElements() throws InterruptedException {
        List<String> listOfNumbers = List.of("123456789");
        when(linkedBlockingQueue.take()).thenReturn(listOfNumbers);

        assertEquals(listOfNumbers, numberQueue.blockingGet());
    }

    @Test
    void forwardsInterruptedExceptionFromLinkedBlockingQueue() throws InterruptedException {
        when(linkedBlockingQueue.take()).thenThrow(new InterruptedException());
        numberQueue = new NumberQueue(linkedBlockingQueue);

        assertThrows(InterruptedException.class,
                () -> numberQueue.blockingGet());
    }

    @Test
    void returnsTrueWhenLinkedBlockingQueueAcceptsElements() {
        List<String> listOfNumbers = List.of("123456789");
        when(linkedBlockingQueue.add(listOfNumbers)).thenReturn(true);

        assertTrue(numberQueue.add(listOfNumbers));
    }

    @Test
    void returnsFalseWhenLinkedBlockingQueueDoesNotAcceptElements() {
        List<String> listOfNumbers = List.of("123456789");
        when(linkedBlockingQueue.add(listOfNumbers)).thenThrow(new IllegalStateException());

        assertFalse(numberQueue.add(listOfNumbers));
    }
}