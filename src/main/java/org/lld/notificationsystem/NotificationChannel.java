package org.lld.notificationsystem;

public interface NotificationChannel {
    String getChannelName();

    boolean sendNotification(User user, Notification notification);

    boolean isChannelAvailableForUser(User user);
}
