package org.lld.notificationsystem.channels;

import org.lld.notificationsystem.Notification;
import org.lld.notificationsystem.NotificationChannel;
import org.lld.notificationsystem.User;

public class InAppChannel implements NotificationChannel {
    @Override
    public String getChannelName() {
        return "IN_APP";
    }

    @Override
    public boolean sendNotification(User user, Notification notification) {
        String body = notification.getPersonalizedContent(user);
        System.out.println("[In-App] for user " + user.getName() + ": " + body);
        return true;
    }

    @Override
    public boolean isChannelAvailableForUser(User user) {
        return true; // demo: always available
    }
}


