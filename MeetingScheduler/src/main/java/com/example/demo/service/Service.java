package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.MeetingHistoryRepository;
import com.example.demo.repository.MeetingsRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class Service {

    // Inject the feedback repository
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MeetingsRepository meetingsRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MeetingHistoryRepository meetingHistoryRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private KafkaNotificationProducer kafkaNotificationProducer;
    @Autowired
    private KafkaNotificationConsumer kafkaNotificationConsumer;
    @Autowired
    private WebClientService webClientService;

    public HashMap<Long,ArrayList<String>> meetingNotifications = new HashMap<Long,ArrayList<String>>();
    public Integer notificationQueueSize = 10;

    @Transactional
    @PostConstruct
    public void init() {
        try {
            meetingsRepository.deleteAll();
            meetingHistoryRepository.deleteAll();
            roomRepository.deleteAll();
            employeeRepository.deleteAll();
            roomRepository.flush();
            employeeRepository.flush();
            // Adding some initial room records
            roomRepository.save(new Room(11L,"Meeting Room A"));
            roomRepository.save(new Room(12L,"Meeting Room B"));
            roomRepository.save(new Room(13L,"Meeting Room C"));
            roomRepository.save(new Room(14L,"Meeting Room D"));
            employeeRepository.save(new Employee(101L,"Employee A","EmailA","ContactA"));
            employeeRepository.save(new Employee(102L,"Employee B","EmailB","ContactB"));
            employeeRepository.save(new Employee(103L,"Employee C","EmailC","ContactC"));
            employeeRepository.save(new Employee(104L,"Employee D","EmailD","ContactD"));

            // Flush changes to the database
            roomRepository.flush();
            employeeRepository.flush();

            // Clear the current redis database cache to ensure fresh data
            notificationService.flushCurrentDatabase();
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
        }
    }


    public void createRooms(int n) {
        for (int i = 1; i <= n; i++) {
            roomRepository.save(new Room((long)i,"Meeting Room "+ i));
        }
    }

    public void createEmployees(int n) {
        for (int i = 1; i <= n; i++) {
            employeeRepository.save(new Employee((long)i+100,"Employee "+i,"Email "+i,"Contact "+i));
        }
    }

    public ArrayList<Room> get_overlapping_meetings(Timestamp start, Timestamp end){
        ArrayList<Meetings> meetings = meetingsRepository.findAllOverlappingMeetings(start,end);
        return meetings.stream().map(Meetings::getRoom).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Long> find_available_rooms( Timestamp startTime, Timestamp endTime) {
        ArrayList<Long> roomsIds = roomRepository.findAll()
                .stream()
                .map(Room::getId) // Assuming Room has a method getId()
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Rooms: " + roomsIds.toString());

        ArrayList<Room> overlapping_meeting_rooms = get_overlapping_meetings(startTime,endTime);
        ArrayList<Long> overlapping_meeting_roomsIds = getRoomIds(overlapping_meeting_rooms);


        ArrayList<Long> available_rooms = roomsIds.stream()
                .filter(roomId -> !overlapping_meeting_roomsIds.contains(roomId))
                .collect(Collectors.toCollection(ArrayList::new));
        return available_rooms;
    }

    public static ArrayList<Long> getRoomIds(List<Room> overlappingMeetingRooms) {
        ArrayList<Long> roomIds = new ArrayList<>();
        for (Room room : overlappingMeetingRooms) {
            roomIds.add(room.getId());
        }
        return roomIds;
    }

    public Date extractDateFromTimeStamp(Date timeStamp) {
        return new Date(timeStamp.getTime());
    }
    public Timestamp convertTimeToIST(Timestamp inputTimestamp){

        // Step 1: Convert the Timestamp to ZonedDateTime
        ZonedDateTime zonedDateTime = inputTimestamp.toInstant().atZone(ZoneId.systemDefault());

        // Step 2: Convert to desired time zone (e.g., IST)
        ZonedDateTime utcZonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());

        // Step 3: Convert the ZonedDateTime back to a Timestamp & Return the resulting Timestamp
        return Timestamp.from(utcZonedDateTime.toInstant());
    }

    public Meetings book_room(Long roomId, Timestamp startTime, Timestamp endTime, Set<Employee> invitees){
        Room room = roomRepository.getById(roomId);
        Timestamp newStartTime = convertTimeToIST(startTime);
        Timestamp newEndTime = convertTimeToIST(endTime);
        Meetings meetings = new Meetings();
        meetings.setRoom(room);
        meetings.setStartTime(newStartTime);
        meetings.setEndTime(newEndTime);
        meetings.setInvitees(invitees);
        meetings.setDate(extractDateFromTimeStamp(newStartTime));
        Meetings newMeetings = meetingsRepository.save(meetings);

        MeetingHistory meetingHistory = new MeetingHistory();
        meetingHistory.setMeetingId(newMeetings.getId());
        meetingHistory.setStartTime(startTime);
        meetingHistory.setEndTime(endTime);
        meetingHistoryRepository.save(meetingHistory);
        send_invitation(newMeetings);
        return newMeetings;
    }

    public ResponseEntity<String> book_meeting(RequestedBody requestedBody){
        ArrayList<Long> available_rooms= find_available_rooms(requestedBody.getStartTime(),requestedBody.getEndTime());
        if(available_rooms.isEmpty()){
            return ResponseEntity.ok("No Room available for Meeting");
        }
        ArrayList<Long> invitees = requestedBody.getInvitees();
        Set<Employee> inviteesSet = new HashSet<>();

        for (Long empId : invitees) {
            Employee invitee = employeeRepository.getById(empId);
            inviteesSet.add(invitee);
        }
        book_room(available_rooms.get(0),requestedBody.getStartTime(),requestedBody.getEndTime(),inviteesSet);
        return ResponseEntity.ok(String.format("Meeting Booked successfully in Room %d for Timeslot %s to %s",
                available_rooms.get(0), requestedBody.getStartTime().toString(), requestedBody.getEndTime().toString()));
    }

    public ResponseEntity<String> bookMeetingInSpecificRoom(RequestedBody requestedBody, Long roomId){
        boolean ifRoomExists = roomRepository.existsById(roomId);
        if (!ifRoomExists){
            return ResponseEntity.ok("Room "+roomId+" Does not exist");
        };
        ArrayList<Meetings> RoomMeetings = meetingsRepository.findAllOverlappingMeetingsInRoom(
                roomId,requestedBody.getStartTime(),requestedBody.getEndTime());
        boolean isRoomAvailable = RoomMeetings.isEmpty();
        if( ! isRoomAvailable){
            return ResponseEntity.ok("Room "+roomId+" not available for Meeting in given time slot.");
        };
        ArrayList<Long> invitees = requestedBody.getInvitees();
        Set<Employee> inviteesSet = new HashSet<>();

        for (Long empId : invitees) {
            Employee invitee = employeeRepository.getById(empId);
            inviteesSet.add(invitee);
        }
        book_room(roomId,requestedBody.getStartTime(),requestedBody.getEndTime(),inviteesSet);
        return ResponseEntity.ok(String.format("Meeting Booked successfully in Room %d for Timeslot %s to %s",
                roomId, requestedBody.getStartTime().toString(), requestedBody.getEndTime().toString()));
    }

    public List<MeetingHistory> get_meeting_history(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MeetingHistory> meetingHistories = meetingHistoryRepository.findAll(pageable);
        return meetingHistories.getContent();
    }

    public List<MeetingHistory> getAllMeetingsOnDate(String date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate localDate = LocalDate.parse(date);

        // Create start and end timestamps
        LocalDateTime startOfDay = localDate.atStartOfDay(); // 12 AM of the given date
        LocalDateTime startOfNextDay = startOfDay.plusDays(1); // 12 AM of the next day

        Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
        Timestamp endTimestamp = Timestamp.valueOf(startOfNextDay);

        // Fetch meetings from the repository
        Page<MeetingHistory> meetingHistories = meetingHistoryRepository.findByStartTimeBetween(startTimestamp, endTimestamp, pageable);
        return meetingHistories.getContent();
    }

    public void send_invitation(Meetings newMeetings){
        Long meeting_id = newMeetings.getId();
        Set<Employee> invitees = newMeetings.getInvitees();
        Meetings meetings = meetingsRepository.getById(meeting_id);

        for (Employee invitee : invitees) {
            Long empId = invitee.getId();
            String notification = "[* Meeting Invite *] Employee: "+ empId +" You have a meeting scheduled for meeting_id " + meeting_id + " in Room " +
                    meetings.getRoom().getId() + " at " + meetings.getStartTime() + " to " + meetings.getEndTime();

            send_notification1(empId,notification);
            System.out.println("Notification Send 1 to Invitees for Meeting: "+meeting_id);
            send_notification2(empId,notification);
            System.out.println("Notification Send 2 to Invitees for Meeting: "+meeting_id);
            send_notification3(empId,notification);
            System.out.println("Notification Send 3 to Invitees for Meeting: "+meeting_id);
            send_notification4(empId,notification);
            System.out.println("Notification Send 4 to Invitees for Meeting: "+meeting_id);
        }
    }

    // In Memory Storage of Notification in Map Data Structure
    public void send_notification1(Long empId, String notification){
        ArrayList<String> notifications;
        if (meetingNotifications.containsKey(empId)) {
            notifications = meetingNotifications.get(empId);
        } else {
            notifications = new ArrayList<>();
        }
        notifications.add(notification);
        // Check if the size exceeds notificationQueueSize
        if (notifications.size() > notificationQueueSize) {
            notifications.remove(0); // Remove the oldest notification
        }
        meetingNotifications.put(empId, notifications);
    }


    // In Memory Storage of Notification in Redis-Cache
    public void send_notification2(Long empId, String notification){
        notificationService.sendNotification(empId.toString(), notification);
    }

    // Send Notification Using WebClients.
    public void send_notification3(Long empId, String notification){
        webClientService.sendNotification(empId.toString(), notification);
    }

    // Storage of Notification in Kafka
    public void send_notification4(Long empId, String notification){
        // insert into kafka message queue.
        kafkaNotificationProducer.sendNotification(empId.toString(), notification);;
    }

    public ResponseEntity<String> send_notificationToEmployee(Long empId, String notification){
        notificationService.sendNotification(empId.toString(), notification);
        System.out.println("Notification Sent to Employee: "+empId);
        return ResponseEntity.ok("Notification Sent to Employee Successfully");
    }

    public ResponseEntity<ArrayList<String>> view_notifications1(Optional<Long> empId){
        if (empId.isPresent()) {
            Long employeeId = empId.get();
            if (meetingNotifications.containsKey(employeeId)) {
                ArrayList<String> notifications = meetingNotifications.get(employeeId);
                return ResponseEntity.ok(notifications);
            } else {
                ArrayList<String> notifications = new ArrayList<String>();
                notifications.add("No Notifications for Employee: " + employeeId);
                return ResponseEntity.ok(notifications);
            }
        } else {
            ArrayList<String> allNotifications = new ArrayList<>();
            for (Long empId2 : meetingNotifications.keySet()) {
                ArrayList<String> notifications = meetingNotifications.get(empId2);
                allNotifications.add("Notifications for Employee: " + empId2);
                allNotifications.addAll(notifications);
            }
            return ResponseEntity.ok(allNotifications);
        }
    }

    public ResponseEntity<List<Object>> view_notifications2(Optional<Long> empId){
        List<Object> Response = new ArrayList<Object>();
        if (empId.isPresent()) {
            String employeeId = empId.get().toString();
            List<Object> notifications = notificationService.getNotificationsByEmpId(employeeId);
            notifications.forEach(System.out::println);
            Response = notifications;
        } else {
            List<Object> allNotifications = notificationService.getAllNotifications();
            allNotifications.forEach(System.out::println);
            Response = allNotifications;
        }
        return ResponseEntity.ok(Response);
    }

    public void send_meeting_reminder(){

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime targetTime = currentTime.plusMinutes(10);
        Timestamp time = Timestamp.valueOf(targetTime);
        Timestamp nextMinute = Timestamp.valueOf(targetTime.plusSeconds(59)); // Adding 59 seconds to cover the minute range

        List<Meetings> upcomingMeetings = meetingsRepository.findByStartTimeBetween(time,nextMinute);


        for(Meetings meetings : upcomingMeetings){
            Long meeting_id = meetings.getId();
            Set<Employee> invitees = meetings.getInvitees();

            for (Employee invitee : invitees) {
                Long empId = invitee.getId();
                String notification = "[* Meeting Reminder *] Employee: "+ empId +" You have a meeting scheduled for meeting_id " + meeting_id + " in Room " +
                        meetings.getRoom().getId() + " in 10 mins at " + meetings.getStartTime() + " to " + meetings.getEndTime();

                send_notification1(empId,notification);
                System.out.println("Notification Send 1 to Invitees for Meeting: "+meeting_id);
                send_notification2(empId,notification);
                System.out.println("Notification Send 2 to Invitees for Meeting: "+meeting_id);
                send_notification3(empId,notification);
                System.out.println("Notification Send 3 to Invitees for Meeting: "+meeting_id);
                send_notification4(empId,notification);
                System.out.println("Notification Send 4 to Invitees for Meeting: "+meeting_id);
            }
        }
    }

    public void delete_old_meetings(){
        LocalDateTime currentTime = LocalDateTime.now();
        // Subtract 7 days
        LocalDateTime newTime = currentTime.minusDays(7);
        // Convert to Timestamp
        Timestamp oldTimeThreshold = Timestamp.valueOf(newTime);

        List<Long> oldMeetings = meetingsRepository.findByEndTimeBeforeTime(oldTimeThreshold);
        oldMeetings.forEach(meeting_id -> {
            meetingsRepository.deleteById(meeting_id);
            System.out.println("Meeting with ID: "+ meeting_id +" deleted successfully");
        });

    }

}