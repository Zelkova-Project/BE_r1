package com.zelkova.zelkova.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.zelkova.zelkova.service.RedisSubscriber;

import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

 @Bean
 RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
   MessageListenerAdapter listenerAdapter) {
  RedisMessageListenerContainer container = new RedisMessageListenerContainer();
  container.setConnectionFactory(connectionFactory);
  container.addMessageListener(listenerAdapter, new PatternTopic("chat"));
  return container;
 }

 @Bean
 MessageListenerAdapter listenerAdapter(RedisSubscriber subscriber) {
  return new MessageListenerAdapter(subscriber, "receiveMessage");
 }

 @Bean
 RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
  RedisTemplate<String, Object> template = new RedisTemplate<>();
  template.setConnectionFactory(connectionFactory);
  return template;
 }
}
