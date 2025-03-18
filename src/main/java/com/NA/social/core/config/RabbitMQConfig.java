package com.NA.social.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {
    public static final String MESSAGE_QUEUE_NAME = "message-queue";
    public static final String MESSAGE_EXCHANGE_NAME = "message-exchange";
    public static final String MESSAGE_ROUTING_KEY = "message.routingKey";

    @Bean
    public Queue queue() {
        return new Queue(MESSAGE_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(MESSAGE_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MESSAGE_ROUTING_KEY);
    }
}
