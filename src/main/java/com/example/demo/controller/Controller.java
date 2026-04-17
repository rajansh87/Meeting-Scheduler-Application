package com.example.demo.controller;

import com.example.demo.model.MeetingHistory;
import com.example.demo.model.NotificationRequestBody;
import com.example.demo.model.RequestedBody;
import com.example.demo.service.GoogleAiStudioService;
import com.example.demo.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/meeting_scheduler/")
public class Controller {

    // Inject the feedback service
    @Autowired
    private Service service;

    @Autowired
    private GoogleAiStudioService aiService;

    // API endpoint for retrieving all Data with pagination
    @CrossOrigin(origins = "http://localhost:63342") // Change this to match your frontend URL
    @GetMapping
    public ResponseEntity<List<Long>> findAvailableRooms(@RequestBody RequestedBody requestedBody) {
        List<Long> available_rooms = service.find_available_rooms(requestedBody.getStartTime(), requestedBody.getEndTime());
        return ResponseEntity.ok(available_rooms);
    }

    // API endpoint for creating new data
    @PostMapping
    public ResponseEntity<String> bookMeeting(@RequestBody RequestedBody requestedBody,
                                              @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return service.book_meeting(requestedBody, idempotencyKey);
    }

    // API endpoint for creating new data
    @PostMapping("book_room/{roomId}")
    public ResponseEntity<String> bookMeetingInSpecificRoom(@RequestBody RequestedBody requestedBody,@PathVariable("roomId")int roomId) {
        return service.bookMeetingInSpecificRoom(requestedBody,(long)roomId);
    }

    @GetMapping("history/")
    public List<MeetingHistory> getMeetingHistory(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return service.get_meeting_history(page,size);
    }

    @GetMapping("meetings_on_day/")
    public List<MeetingHistory> getAllMeetingsOnDate(@RequestParam(defaultValue = "0") String date, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return service.getAllMeetingsOnDate(date,page,size);
    }

    @GetMapping("notifications/")
    public ResponseEntity<ArrayList<String>> view_notifications(@RequestParam Optional<Long> empId) {
        return service.view_notifications1(empId);
    }

    @GetMapping("notifications/v2/")
    public ResponseEntity<List<Object>> view_notifications_v2(@RequestParam Optional<Long> empId) {
        return service.view_notifications2(empId);
    }

    @PostMapping("send_notification/")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequestBody notificationRequestBody) {
        return service.send_notificationToEmployee(notificationRequestBody.getEmpId(),notificationRequestBody.getNotification());
    }

    @PostMapping("book_room_via_ai/")
    public ResponseEntity<String> bookRoomViaGoogleAiStudio(@RequestBody Map<String, String> payload) throws Exception {
        String userInput = payload.get("input_text"); // e.g. "Schedule a meeting with Anshraj at 5pm today"
        String geminiRawResponse = aiService.callGeminiAPI(userInput);
        RequestedBody bookingDetails = aiService.parseGeminiResponse(geminiRawResponse);
        System.out.println("Gemini AI Response converted to rb " + bookingDetails.toString());
        return service.book_meeting(bookingDetails, "");
    }
}
