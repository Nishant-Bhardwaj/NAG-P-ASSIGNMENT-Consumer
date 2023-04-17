package com.nishant.consumer.rmq;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RMQFlightConsumerService {

    Logger logger = LogManager.getLogger(RMQFlightConsumerService.class);

    @RabbitListener(queues = "${spring.rabbitmq.flight_queue_confirm}")
    public void receivedConfirmedNotification(String flightBookingResponse) {
        logger.info("Flight SUCCESS NOTIFICATION, Received on QUEUE ---->\n" + flightBookingResponse+"\n");
    }

    @RabbitListener(queues = "${spring.rabbitmq.flight_queue_cancel}")
    public void receivedFailedNotification(String flightBookingResponse) {
        logger.info("Flight FAILED NOTIFICATION, Received on QUEUE ---->\n" + flightBookingResponse+"\n");
    }
}