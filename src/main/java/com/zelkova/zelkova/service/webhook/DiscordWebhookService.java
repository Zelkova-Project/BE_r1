package com.zelkova.zelkova.service.webhook;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DiscordWebhookService {
    
    private RestTemplate restTemplate;

    private final String webhookUrl = "https://discord.com/api/webhooks/1253013261637390346/Hf6esHTDZQISwUfTTCWavIQSZ7WKnyZge2_j9B5dBpxUc3RtKr_9yqnG2aVb3QSDOYry";

    public DiscordWebhookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendHealthStatus(String status) {
        String message = String.format("반갑네 나는 캡틴 훅이라네 서버는 현재 : %s", status);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        String payload = String.format("{\"content\":\"%s\"}", message);

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        restTemplate.exchange(webhookUrl, HttpMethod.POST,entity, String.class);

    }
}

