package org.lld.notificationsystem.types;

import org.lld.notificationsystem.NotificationType;
import org.lld.notificationsystem.NotificationTypeEnum;
import org.lld.notificationsystem.User;

import static org.lld.notificationsystem.NotificationTypeEnum.FRIEND_REQUEST;

public class FriendRequestNotificationType implements NotificationType {
    @Override
    public NotificationTypeEnum getNotificationType() {
        return FRIEND_REQUEST;
    }

    @Override
    public String formatContent(String baseContent, User user) {
        return "**" + baseContent + " [Accept] [Decline]";
    }
}
