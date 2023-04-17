package com.nishant.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.flight_queue_confirm}")
    private String flightQueueConfirm;

    @Value("${spring.rabbitmq.flight_queue_cancel}")
    private String flightQueueCancel;

    @Value("${spring.rabbitmq.hotel_queue_confirm}")
    private String hotelQueueConfirm;

    @Value("${spring.rabbitmq.hotel_queue_cancel}")
    private String hotelQueueCancel;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.flight_routingKey_confirm}")
    private String flightRoutingKeyConfirm;

    @Value("${spring.rabbitmq.flight_routingKey_cancel}")
    private String flightRoutingKeyCancel;

    @Value("${spring.rabbitmq.hotel_routingKey_confirm}")
    private String hotelRoutingKeyConfirm;

    @Value("${spring.rabbitmq.hotel_routingKey_cancel}")
    private String hotelRoutingKeyCancel;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Bean
    Queue flightQueueConfirm() {
        return new Queue(flightQueueConfirm, true);
    }

    @Bean
    Queue flightQueueCancel() {
        return new Queue(flightQueueCancel, true);
    }

    @Bean
    Queue hotelQueueConfirm() {
        return new Queue(hotelQueueConfirm, true);
    }

    @Bean
    Queue hotelQueueCancel() {
        return new Queue(hotelQueueCancel, true);
    }

    @Bean
    Exchange exchange() {
        return ExchangeBuilder.topicExchange(exchange).durable(true).build();
    }

    @Bean
    Binding flightBindingConfirm() {
        return BindingBuilder
                .bind(flightQueueConfirm())
                .to(exchange())
                .with(flightRoutingKeyConfirm)
                .noargs();
    }

    @Bean
    Binding flightBindingCancel() {
        return BindingBuilder
                .bind(flightQueueCancel())
                .to(exchange())
                .with(flightRoutingKeyCancel)
                .noargs();
    }

    @Bean
    Binding hotelBindingConfirm() {
        return BindingBuilder
                .bind(hotelQueueConfirm())
                .to(exchange())
                .with(hotelRoutingKeyConfirm)
                .noargs();
    }


    @Bean
    Binding hotelBindingCancel() {
        return BindingBuilder
                .bind(hotelQueueCancel())
                .to(exchange())
                .with(hotelRoutingKeyCancel)
                .noargs();
    }

    @Bean
    public ConnectionFactory flightConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter flightJsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate flightRabbitTemplate(ConnectionFactory flightConnectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(flightConnectionFactory);
        rabbitTemplate.setMessageConverter(flightJsonMessageConverter());
        return rabbitTemplate;
    }
}
