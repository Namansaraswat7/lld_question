package org.lld.notificationsystem.types;

import org.lld.notificationsystem.NotificationType;
import org.lld.notificationsystem.NotificationTypeEnum;
import org.lld.notificationsystem.User;

import static org.lld.notificationsystem.NotificationTypeEnum.NEW_MESSAGE;

public class MessageNotificationType implements NotificationType {
    @Override
    public NotificationTypeEnum getNotificationType() {
        return NEW_MESSAGE;
    }

    @Override
    public String formatContent(String baseContent, User user) {
        return NotificationType.super.formatContent(baseContent, user);
    }
}
