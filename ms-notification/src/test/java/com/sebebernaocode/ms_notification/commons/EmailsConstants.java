package com.sebebernaocode.ms_notification.commons;

import com.sebebernaocode.ms_notification.entities.NotificationEmail;

public class EmailsConstants {
    public static final NotificationEmail EMAIL = new NotificationEmail(1 , "testename", "teste@gmail.com", "to@email.com", "toname", "cc", "subject", "body", "contentType");
    public static final NotificationEmail INVALID_EMAIL = new NotificationEmail(1 , "", "", "", "", "", "", "", "");

}
