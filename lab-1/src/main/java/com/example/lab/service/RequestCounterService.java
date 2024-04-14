package com.example.lab.service;

import java.util.concurrent.atomic.AtomicLong;

public class RequestCounterService {
    private static AtomicLong counter = new AtomicLong(0);

    public static void incrementCountr() {
        counter.incrementAndGet();
    }

    public static long getRequestsCount() {
        return counter.get();
    }
}
