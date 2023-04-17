package com.nishant.consumer.rmq;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RMQHotelConsumerService {

    Logger logger = LogManager.getLogger(RMQHotelConsumerService.class);

    @RabbitListener(queues = "${spring.rabbitmq.hotel_queue_confirm}")
    public void receivedConfirmedNotification(String hotelBookingResponse) {
        logger.info("Hotel SUCCESS NOTIFICATION, Received on QUEUE ---->\n" + hotelBookingResponse+"\n");
    }

    @RabbitListener(queues = "${spring.rabbitmq.hotel_queue_cancel}")
    public void receivedFailedNotification(String hotelBookingResponse) {
        logger.info("Hotel FAILED NOTIFICATION, Received on QUEUE ---->\n" + hotelBookingResponse+"\n");
    }
}