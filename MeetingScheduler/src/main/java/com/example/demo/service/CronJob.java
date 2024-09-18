package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJob {
    // this cron job would send notifications to employees whose meeting is approaching.

    @Autowired
    private Service service;

    @Scheduled(fixedRate = 10000) // Runs every 10sec = 10,000 miliseconds
    public void sendNotificationsForUpcomingMeetings() {
        System.out.println("Scheduled task executed at " + System.currentTimeMillis());
        System.out.println("sendNotificationsForUpcomingMeetings Function called by cron job.");
        // TODO: implement to check if any meeting time is approaching, if yes send notification to
        //  all the invitees of that meeting.
        //  use send_meeting_reminder(meeting) to send notification.
        service.send_meeting_reminder();
    }

    @Scheduled(cron = "0 0 0 * * SUN") // Runs every sun at night 12AM.
    public void deleteOldMeetings() {
        System.out.println("Scheduled task executed at " + System.currentTimeMillis());
        System.out.println("deleteOldMeetings Function called by cron job.");
        // implemented to delete old meetings that have passed. this would delete 1 week old meetings data.
        service.delete_old_meetings();
    }





}
