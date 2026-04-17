package com.example.demo.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    private final int maxRequests;
    private final long windowSizeInMillis;

    private final ConcurrentHashMap<String, Deque<Long>> requestMap = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeInMillis = windowSizeInMillis;
    }

    public boolean allowRequest(String key) {

        long currentTime = System.currentTimeMillis();

        Deque<Long> timestamps = requestMap.computeIfAbsent(key, k -> new LinkedList<>());

        synchronized (timestamps) {

            // remove old timestamps
            while (!timestamps.isEmpty() &&
                    currentTime - timestamps.peekFirst() > windowSizeInMillis) {
                timestamps.pollFirst();
            }

            if (timestamps.size() < maxRequests) {
                timestamps.addLast(currentTime);
                return true;
            }

            return false;
        }
    }
}
