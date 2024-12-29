package com.sebebernaocode.ms_notification.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationEmailDTO {
    @NotBlank
    @Email(regexp = "marchesaneduardofw@gmail.com", message = "Invalid e-mail format")
    private String fromEmail;
    @NotBlank
    @Email
    private String toEmail;
    @NotBlank
    private String subject;
    @NotBlank
    private String body;
}
