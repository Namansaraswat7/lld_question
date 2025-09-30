package org.lld.notificationsystem.channels;

import org.lld.notificationsystem.Notification;
import org.lld.notificationsystem.NotificationChannel;
import org.lld.notificationsystem.User;

public class SmsChannel implements NotificationChannel {

    private final String provider;
    private final String apiKey;

    public SmsChannel(String provider, String apiKey) {
        this.provider = provider;
        this.apiKey = apiKey;
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }

    @Override
    public boolean sendNotification(User user, Notification notification) {
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            System.out.println("No phone number was found for user " + user.getName());
            return false;
        }

        try {
            Thread.sleep(250); // simulate network latency
            String personalizedContent = notification.getPersonalizedContent(user);
            System.out.println("[SMS via " + provider + "] to " + user.getPhoneNumber() + ": " + personalizedContent);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("SMS sending interrupted for user " + user.getName());
            return false;
        }
    }

    @Override
    public boolean isChannelAvailableForUser(User user) {
        return user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty();
    }
}
