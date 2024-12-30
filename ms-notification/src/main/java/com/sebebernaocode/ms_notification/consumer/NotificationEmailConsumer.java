package com.sebebernaocode.ms_notification.consumer;

import com.sebebernaocode.ms_notification.dtos.NotificationEmailDTO;
import com.sebebernaocode.ms_notification.services.NotificationEmailService;
import com.sebebernaocode.ms_notification.entities.NotificationEmail;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NotificationEmailConsumer {
    @Autowired
    NotificationEmailService service;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void listen (@Payload NotificationEmail email) {
        service.save(email);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(email, NotificationEmailDTO.class);
        service.send(email);
    }
}
