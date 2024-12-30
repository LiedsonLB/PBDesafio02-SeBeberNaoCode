package com.sebebernaocode.ms_notification.commons;

import com.sebebernaocode.ms_notification.entities.NotificationEmail;

public class EmailsConstants {
    public static final NotificationEmail EMAIL = new NotificationEmail(1 , "testename", "marchesaneduardofw@gmail.com", "to@email.com", "toname", "cc", "subject", "body", "contentType", null, null);
    public static final NotificationEmail INVALID_EMAIL = new NotificationEmail(1 , "", "", "", "", "", "", "", "", null, null);

}
