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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.sebebernaocode.ms_notification.commons.EmailsConstants.EMAIL;
import static com.sebebernaocode.ms_notification.commons.EmailsConstants.INVALID_EMAIL;
import static org.mockito.Mockito.*;

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

    @Test
    public void testSendEmail_InvalidEmail() {
        Assertions.assertThatThrownBy(() -> notificationEmailService.send(INVALID_EMAIL)).isInstanceOf(EmailException.class);
    }

    @Test
    public void testFindAll_validEmails() {
        Pageable pageable = mock(Pageable.class);
        NotificationEmail email1 = EMAIL;
        NotificationEmail email2 = new NotificationEmail(2 , "anyname", "anyemailatall@gmail.com", "to@email.com", "toname", "cc", "subject", "body", "contentType", null, null);
        List<NotificationEmail> emailList = Arrays.asList(email1, email2);

        Page<NotificationEmail> page = new PageImpl<>(emailList, pageable, emailList.size());

        when(notificationEmailRepository.findAll(pageable)).thenReturn(page);

        Page<NotificationEmail> result = notificationEmailService.findAll(pageable);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getContent()).isEqualTo(emailList);

        verify(notificationEmailRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testFindById_WithExistingId() {
        when(notificationEmailRepository.findById(1L)).thenReturn(Optional.of(EMAIL));

        NotificationEmail result = notificationEmailService.findById(1L);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getEmailId()).isEqualTo(EMAIL.getEmailId());
        Assertions.assertThat(result.getFromEmail()).isEqualTo(EMAIL.getFromEmail());

        verify(notificationEmailRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_WithUnexistingId_NotFound() {
        when(notificationEmailRepository.findById(2L)).thenReturn(Optional.empty());

        NotificationEmail result = notificationEmailService.findById(2L);

        Assertions.assertThat(result).isNull();

        verify(notificationEmailRepository, times(1)).findById(2L);
    }

}
