package org.lld.notificationsystem.factory;

import org.lld.notificationsystem.NotificationType;
import org.lld.notificationsystem.NotificationTypeEnum;
import org.lld.notificationsystem.types.FriendRequestNotificationType;
import org.lld.notificationsystem.types.MessageNotificationType;

public class NotificationTypeFactory {
    public static NotificationType get(NotificationTypeEnum type) {
        switch (type) {
            case NEW_MESSAGE:
                return new MessageNotificationType();
            case FRIEND_REQUEST:
                return new FriendRequestNotificationType();
            default:
                throw new IllegalArgumentException("Unsupported notification type: " + type);
        }
    }
}


