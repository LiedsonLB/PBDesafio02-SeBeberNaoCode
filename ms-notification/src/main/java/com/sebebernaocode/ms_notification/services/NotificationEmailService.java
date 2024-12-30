package com.sebebernaocode.ms_notification.services;

import com.sebebernaocode.ms_notification.entities.Status;
import com.sebebernaocode.ms_notification.exception.EmailException;
import com.sebebernaocode.ms_notification.entities.NotificationEmail;
import com.sebebernaocode.ms_notification.repositories.NotificationEmailRepository;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationEmailService {
    @Autowired
    NotificationEmailRepository notificationEmailRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Transactional
    public void send(NotificationEmail notificationEmail) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(notificationEmail.getFromEmail());
            message.setTo(notificationEmail.getToEmail());
            message.setSubject(notificationEmail.getSubject());
            message.setText(notificationEmail.getBody());
            javaMailSender.send(message);
        }
        catch (Exception e) {
            notificationEmail.setStatus(Status.ERROR);
            throw new EmailException(e.getMessage());
        }
    }
    public NotificationEmail save(NotificationEmail notificationEmail) {
        try{
            notificationEmail.setSentDate(LocalDateTime.now());
            notificationEmail.setStatus(Status.SENT);
            return notificationEmailRepository.save(notificationEmail);
        }
        catch (Exception e) {
            throw new EmailException(e.getMessage());
        }
    }

    public Page<NotificationEmail> findAll(Pageable pageable) {
        return notificationEmailRepository.findAll(pageable);
    }
    public NotificationEmail findById(Long id) {
        return notificationEmailRepository.findById(id).orElse(null);
    }
}
