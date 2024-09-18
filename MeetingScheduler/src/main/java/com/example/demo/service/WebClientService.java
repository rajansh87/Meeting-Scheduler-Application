package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;



@Service
public class WebClientService {

    public void sendNotification(String empId, String notification) {
        WebClient webClient = WebClient.create("http://localhost:9090"); // calling self api of sending notificaition.

        String requestBody = "{ \"empId\": \""+Long.parseLong(empId)+"\",\"notification\": \""+notification+"\" }";
        System.out.println("Request Body: " + requestBody);
        String response = webClient.post()
                .uri("/api/meeting_scheduler/send_notification/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Response: " + response);
    }

    public void getRequest(){
        WebClient webClient = WebClient.create("https://localhost:8181");

        String response = webClient.get()
                .uri("/endpoint")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Response: " + response);
    }

    public void postRequest(){

        WebClient webClient = WebClient.create("http://localhost:8181");

        String requestBody = "{ \"key\": \"value\" }";

        String response = webClient.post()
                .uri("/api/request")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Response: " + response);
    }

    public void postRequestWithErrorHandling(){
        WebClient webClient = WebClient.create("https://api.example.com");

        String requestBody = "{ \"key\": \"value\" }";

        String response = webClient.post()
                .uri("/post-endpoint")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return Mono.error(new RuntimeException("Client Error"));
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    return Mono.error(new RuntimeException("Server Error"));
                })
                .bodyToMono(String.class)
                .block();
    }

    public void asynchronousCall(){
        WebClient webClient = WebClient.create("https://api.example.com");
        Mono<String> responseMono = webClient.get()
                .uri("/endpoint")
                .retrieve()
                .bodyToMono(String.class);

        responseMono.subscribe(response -> {
            System.out.println("Response: " + response);
        });

    }
}
