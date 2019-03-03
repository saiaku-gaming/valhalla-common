package com.valhallagame.common.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

public class RabbitSender {
    private final static Logger logger = LoggerFactory.getLogger(RabbitSender.class);

    private RabbitTemplate rabbitTemplate;

    public RabbitSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(RabbitMQRouting.Exchange exchange, String routingKey, NotificationMessage message) {
        if(rabbitTemplate == null) {
            logger.error("Rabbit template was not set in the Rabbit Sender!");
            return;
        }

        if(MDC.getMDCAdapter() != null) {
            String requestId = MDC.get("request_id") != null ? MDC.get("request_id") : UUID.randomUUID().toString();
            message.addData("requestId", requestId);
        }

        rabbitTemplate.convertAndSend(exchange.name(), routingKey, message);
    }
}
