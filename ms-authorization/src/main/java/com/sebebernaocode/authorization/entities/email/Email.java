package com.sebebernaocode.authorization.entities.email;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Email {
    private String from;
    @NotBlank
    private String to;
    @NotBlank
    private String subject;
    @NotBlank
    private String body;

    @Override
    public String toString() {
        return "{\n" +
                "\"fromEmail\": \"" + from + "\",\n" +
                "\"toEmail\": \""+ to + "\",\n" +
                "\"subject\": \"" + subject + "\",\n" +
                "\"body\": \""+ body + "\"\n" +
                "}";
    }
}

