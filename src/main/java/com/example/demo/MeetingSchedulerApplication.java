package com.example.demo;


import com.example.demo.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;
import java.util.TimeZone;

@EnableScheduling // from cron job
@EnableCaching
@EnableAutoConfiguration
@SpringBootApplication
public class MeetingSchedulerApplication implements CommandLineRunner {
    @Autowired
    private Service service;

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(MeetingSchedulerApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of rooms to create: ");
        int n = scanner.nextInt();
        service.createRooms(n);
        System.out.println(n + " rooms created.");

        System.out.print("Enter number of employees to create: ");
        int m = scanner.nextInt();
        service.createEmployees(m);
        System.out.println(m + " employees created.");
    }
}