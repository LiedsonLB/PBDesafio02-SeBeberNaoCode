package com.sebebernaocode.ms_notification.repositories;

import com.sebebernaocode.ms_notification.entities.NotificationEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEmailRepository extends JpaRepository<NotificationEmail, Long> {
}
