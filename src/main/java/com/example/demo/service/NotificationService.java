package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final String NOTIFICATION_KEY_PREFIX = "notifications:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void sendNotification(String empId, String notification) {
        String key = NOTIFICATION_KEY_PREFIX + empId;
        redisTemplate.opsForList().rightPush(key, notification);
    }

    public List<Object> getNotificationsByEmpId(String empId) {
        String key = NOTIFICATION_KEY_PREFIX + empId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public List<Object> getAllNotifications() {
        return redisTemplate.keys(NOTIFICATION_KEY_PREFIX + "*")
                .stream()
                .flatMap(key -> redisTemplate.opsForList().range(key, 0, -1).stream())
                .collect(Collectors.toList());
    }

    public void flushCurrentDatabase() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}

