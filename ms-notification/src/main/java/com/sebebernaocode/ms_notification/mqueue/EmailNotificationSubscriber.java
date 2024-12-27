package com.sebebernaocode.ms_notification.mqueue;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSubscriber {

    @RabbitListener(queues = "${mq.queues.send-email}")
    public void sendEmailNotification(String payload) {
        System.out.println(payload);
    }
}
