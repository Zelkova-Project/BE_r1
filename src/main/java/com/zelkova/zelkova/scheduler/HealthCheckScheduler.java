package com.zelkova.zelkova.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.zelkova.zelkova.service.webhook.DiscordWebhookService;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class HealthCheckScheduler {
    
    private final RestTemplate restTemplate;
    private final DiscordWebhookService webhookService;
    
    @Value("${scheduler.enabled:true}")
    private boolean schedulerEnabled;

    public HealthCheckScheduler(RestTemplate restTemplate, DiscordWebhookService webhookService) {
        this.restTemplate = restTemplate;
        this.webhookService = webhookService;
    }

    @Scheduled(fixedRate = 900000)
    public void checkHealth() {
        String healthEndPoint = "https://namu0005.or.kr/actuator/health";

        if (schedulerEnabled) {
            try {
                String healthResponse = restTemplate.getForObject(healthEndPoint, String.class);
                log.info("healthResponse >>>>> " + healthResponse);
                webhookService.sendHealthStatus("켜졌다네");
            } catch (Exception e) {
                webhookService.sendHealthStatus("꺼져있다네");
            }           
        } else {
            System.out.println("Health check is disabled in local environment.");
        }



    }
}



