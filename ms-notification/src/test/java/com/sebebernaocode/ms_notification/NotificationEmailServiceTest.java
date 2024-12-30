package com.sebebernaocode.ms_notification;

import com.sebebernaocode.ms_notification.entities.NotificationEmail;
import com.sebebernaocode.ms_notification.exception.EmailException;
import com.sebebernaocode.ms_notification.repositories.NotificationEmailRepository;
import com.sebebernaocode.ms_notification.services.NotificationEmailService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.sebebernaocode.ms_notification.commons.EmailsConstants.EMAIL;
import static com.sebebernaocode.ms_notification.commons.EmailsConstants.INVALID_EMAIL;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationEmailServiceTest {

    @InjectMocks
    private NotificationEmailService notificationEmailService;

    @Mock
    private NotificationEmailRepository notificationEmailRepository;

    @Test
    public void testSaveEmail_ValidEmail() {
    when(notificationEmailRepository.save(EMAIL)).thenReturn(EMAIL);
        NotificationEmail sut = notificationEmailService.save(EMAIL);

        Assertions.assertThat(sut).isNotNull().isEqualTo(EMAIL);
    }
    @Test
    public void testSaveEmail_InvalidEmail() {
        when(notificationEmailRepository.save(INVALID_EMAIL)).thenThrow(EmailException.class);
        Assertions.assertThatThrownBy(() -> notificationEmailService.save(INVALID_EMAIL)).isInstanceOf(EmailException.class);
    }

    @Test public void testSendEmail_InvalidEmail() {
        Assertions.assertThatThrownBy(() -> notificationEmailService.send(INVALID_EMAIL)).isInstanceOf(EmailException.class);
    }
}
