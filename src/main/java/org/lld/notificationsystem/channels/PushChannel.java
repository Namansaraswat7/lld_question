package org.lld.notificationsystem.channels;

import org.lld.notificationsystem.Notification;
import org.lld.notificationsystem.NotificationChannel;
import org.lld.notificationsystem.User;

public class PushChannel implements NotificationChannel {
    private final String provider;

    public PushChannel(String provider) {
        this.provider = provider;
    }

    @Override
    public String getChannelName() {
        return "PUSH";
    }

    @Override
    public boolean sendNotification(User user, Notification notification) {
        // Simplified: assume a device token exists if phone number exists
        String body = notification.getPersonalizedContent(user);
        System.out.println("[Push via " + provider + "] to user " + user.getName() + ": " + body);
        return true;
    }

    @Override
    public boolean isChannelAvailableForUser(User user) {
        return true; // demo
    }
}


