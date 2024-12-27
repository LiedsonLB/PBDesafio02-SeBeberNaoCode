package com.sebebernaocode.ms_notification.services;

import com.sebebernaocode.ms_notification.exception.EmailException;
import com.sebebernaocode.ms_notification.entities.NotificationEmail;
import com.sebebernaocode.ms_notification.repositories.NotificationEmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationEmailService {
    @Autowired
    NotificationEmailRepository notificationEmailRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    public NotificationEmail send(NotificationEmail notificationEmail) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(notificationEmail.getFromEmail());
            message.setTo(notificationEmail.getToEmail());
            message.setSubject(notificationEmail.getSubject());
            message.setText(notificationEmail.getBody());
            javaMailSender.send(message);
        }
        catch (EmailException e){
            e.printStackTrace();
        }

        return notificationEmailRepository.save(notificationEmail);
    }

    public Page<NotificationEmail> findAll(Pageable pageable) {
        return notificationEmailRepository.findAll(pageable);
    }
    public NotificationEmail findById(Long id) {
        return notificationEmailRepository.findById(id).orElse(null);
    }
}
