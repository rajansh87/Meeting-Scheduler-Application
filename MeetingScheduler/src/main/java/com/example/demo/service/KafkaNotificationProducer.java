package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaNotificationProducer {

    private static final String TOPIC = "meeting_notifications";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNotification(String empId, String notification) {
        System.out.println("Sending notification from kafka producer for empId: " + empId + " -> " + notification);
        kafkaTemplate.send(TOPIC, empId, notification);
    }
}


