package com.sebebernaocode.ms_notification.controllers;

import com.sebebernaocode.ms_notification.dtos.NotificationEmailDTO;
import com.sebebernaocode.ms_notification.entities.NotificationEmail;
import com.sebebernaocode.ms_notification.services.NotificationEmailService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class NotificationEmailController {

    @Autowired
    private NotificationEmailService notificationEmailService;

    @PostMapping("/sending-email")
    public ResponseEntity sendingEmail(@RequestBody @Valid NotificationEmailDTO notificationEmailDTO) {
        ModelMapper modelMapper = new ModelMapper();
        NotificationEmail notificationEmail = modelMapper.map(notificationEmailDTO, NotificationEmail.class);
        notificationEmailService.send(notificationEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping(name = "/emails")
    public ResponseEntity getAllEmails(@PageableDefault(page = 0, size = 10, sort = "emailId", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(notificationEmailService.findAll(pageable), HttpStatus.OK);
    }

//    @GetMapping(name = "/emails/{emailId}")
//    public ResponseEntity getEmailById(@PathVariable Long emailId) {
//        return new ResponseEntity<>(notificationEmailService.findById(emailId), HttpStatus.OK);
//    }
}