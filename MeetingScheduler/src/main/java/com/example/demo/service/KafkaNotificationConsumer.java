package com.example.demo.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaNotificationConsumer {

    private static final String TOPIC = "meeting_notifications";
    private static final String GROUPID = "my-group";

    @KafkaListener(topics = TOPIC, groupId = GROUPID)
    public void consume(String empId,String notification) {
        System.out.println("Received notification at kafka consumer for empId: " + empId + " -> " + notification);
        // Process the notification as needed
    }
}

