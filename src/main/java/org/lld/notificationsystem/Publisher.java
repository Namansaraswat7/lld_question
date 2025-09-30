package org.lld.notificationsystem;

import java.util.Map;

public interface Publisher {
    String getName();

    String getPublisherId();

    void setBroker(NotificationBroker broker);

    void publishNotification(NotificationType type, String title, String content,
                             Map<String, String> personalizedData);

    void start();

    void stop();
}
