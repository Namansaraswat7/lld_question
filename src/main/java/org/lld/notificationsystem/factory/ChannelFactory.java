package org.lld.notificationsystem.factory;

import org.lld.notificationsystem.ChannelType;
import org.lld.notificationsystem.NotificationChannel;
import org.lld.notificationsystem.channels.EmailChannel;
import org.lld.notificationsystem.channels.InAppChannel;
import org.lld.notificationsystem.channels.PushChannel;
import org.lld.notificationsystem.channels.SmsChannel;

public class ChannelFactory {
    public static NotificationChannel create(ChannelType type) {
        switch (type) {
            case EMAIL:
                return new EmailChannel("SMTP", "noreply@example.com");
            case SMS:
                return new SmsChannel("Twilio", "api-key");
            case PUSH:
                return new PushChannel("FCM");
            case IN_APP:
                return new InAppChannel();
            default:
                throw new IllegalArgumentException("Unsupported channel type: " + type);
        }
    }
}


