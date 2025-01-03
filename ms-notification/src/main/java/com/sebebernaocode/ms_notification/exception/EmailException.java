package com.sebebernaocode.ms_notification.exception;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;

public class EmailException extends AmqpRejectAndDontRequeueException {
    public EmailException(String message) {
        super(message);
    }
}
