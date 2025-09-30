package org.lld.notificationsystem.channels;

import org.lld.notificationsystem.Notification;
import org.lld.notificationsystem.NotificationChannel;
import org.lld.notificationsystem.User;

public class EmailChannel implements NotificationChannel {

    private final String provider;
    private final String fromAddress;

    public EmailChannel(String provider, String fromAddress) {
        this.provider = provider;
        this.fromAddress = fromAddress;
    }

    @Override
    public String getChannelName() {
        return "EMAIL";
    }

    @Override
    public boolean sendNotification(User user, Notification notification) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            System.out.println("No email found for user " + user.getName());
            return false;
        }
        String body = notification.getPersonalizedContent(user);
        System.out.println("[Email via " + provider + "] to " + user.getEmail() + " from " + fromAddress + ": " + body);
        return true;
    }

    @Override
    public boolean isChannelAvailableForUser(User user) {
        return user.getEmail() != null && !user.getEmail().isEmpty();
    }
}


