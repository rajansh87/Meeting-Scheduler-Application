package com.example.demo.service;

import com.example.demo.model.RequestedBody;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GoogleAiStudioService {
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models";
    private static final String MODEL = "gemini-2.0-flash:generateContent";
    // https://aistudio.google.com/apikey
    private static final String API_KEY = "API_KEY"; // Replace with your actual API key

    public String callGeminiAPI(String userInput) {
        System.out.println("Calling Gemini API with input: " + userInput);
        WebClient webClient = WebClient.create(GEMINI_URL);
        String inputPrompt = generatePromptForAi(userInput);

        // Build request body as nested Map
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", inputPrompt)
                        ))
                )
        );
        // Call Gemini API
        String geminiRawResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + MODEL)
                        .queryParam("key", API_KEY)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return geminiRawResponse;
    }

    public RequestedBody parseGeminiResponse(String geminiRawResponse) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Step 1: Get raw text from Gemini
        JsonNode root = mapper.readTree(geminiRawResponse);
        String rawText = root.at("/candidates/0/content/parts/0/text").asText();

        // Step 2: Clean text to get the pure JSON
        String cleanedJson = rawText
                .replace("```json", "")
                .replace("```", "")
                .trim();

        // Step 3: Parse JSON into intermediate object
        JsonNode extracted = mapper.readTree(cleanedJson);

        String startTimeStr = extracted.get("startTime").asText();
        String endTimeStr = extracted.get("endTime").asText();
        ArrayList<String> names = new ArrayList<>();
        for (JsonNode n : extracted.get("invitees")) {
            names.add(n.asText());
        }

        // Step 4: Convert names to empIds using the new lookup
        ArrayList<Long> empIds = names.stream()
                .map(this::nameToEmpId)  // Map using the nameToEmpId lookup method
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        // Step 5: Create RequestedBody object
        RequestedBody rb = new RequestedBody();
        rb.setStartTime(Timestamp.valueOf(LocalDateTime.parse(startTimeStr)));
        rb.setEndTime(Timestamp.valueOf(LocalDateTime.parse(endTimeStr)));
        rb.setInvitees(empIds);

        return rb;
    }

    private Long nameToEmpId(String name) {
        if (name != null && name.startsWith("Employee_")) {
            // Extract the number from "Employee_X" (X being the number)
            try {
                int empNumber = Integer.parseInt(name.substring(9));  // Employee_X -> X
                return (long) empNumber + 100;  // ID formula: (long) X + 100
            } catch (NumberFormatException e) {
                // Handle cases where parsing fails (invalid name format)
                return null;
            }
        }
        return null;
    }

    private String generatePromptForAi(String userInput){
        return "Extract the following information from this meeting instruction:\n\n" +
                "Instruction: " + userInput + "\n\n" +
                "Return only JSON in the following format:\n" +
                "{\n" +
                "  \"startTime\": \"yyyy-MM-dd'T'HH:mm:ss\",\n" +
                "  \"endTime\": \"yyyy-MM-dd'T'HH:mm:ss\",\n" +
                "  \"invitees\": [\"name1\", \"name2\"]\n" +
                "}\n\n" +
                "Notes:\n" +
                "- Do not add any explanation.\n" +
                "- If time is ambiguous, assume duration is 1 hour.\n" +
                "- Parse names mentioned in the input as invitees.";
    }
}
