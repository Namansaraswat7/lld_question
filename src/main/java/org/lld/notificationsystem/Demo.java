package org.lld.notificationsystem;

import org.lld.notificationsystem.factory.ChannelFactory;
import org.lld.notificationsystem.factory.NotificationTypeFactory;
import org.lld.notificationsystem.publishers.NotificationPublisher;

import java.util.*;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        NotificationBroker broker = new NotificationBroker();

        // Register channels via factory
        for (ChannelType channelType : ChannelType.values()) {
            try {
                broker.addNotificationChannel(channelType, ChannelFactory.create(channelType));
            } catch (IllegalArgumentException ignored) { }
        }

        User alice = new User(
                UUID.randomUUID().toString(),
                "Alice",
                "alice@example.com",
                "+1111111111",
                new HashSet<>(),
                new HashMap<>()
        );

        User bob = new User(
                UUID.randomUUID().toString(),
                "Bob",
                "bob@example.com",
                "+1222222222",
                new HashSet<>(),
                new HashMap<>()
        );

        broker.registerUser(alice);
        broker.registerUser(bob);

        NotificationType messageType = NotificationTypeFactory.get(NotificationTypeEnum.NEW_MESSAGE);
        NotificationType friendRequestType = NotificationTypeFactory.get(NotificationTypeEnum.FRIEND_REQUEST);

        // user-level preferences (enums)
        alice.subscribeToNotification(NotificationTypeEnum.NEW_MESSAGE, ChannelType.EMAIL);
        alice.subscribeToNotification(NotificationTypeEnum.NEW_MESSAGE, ChannelType.SMS);
        bob.subscribeToNotification(NotificationTypeEnum.FRIEND_REQUEST, ChannelType.IN_APP);
        bob.subscribeToNotification(NotificationTypeEnum.FRIEND_REQUEST, ChannelType.PUSH);

        // broker-level subscriptions
        broker.subscribeUserToType(alice.getId(), NotificationTypeEnum.NEW_MESSAGE);
        broker.subscribeUserToType(bob.getId(), NotificationTypeEnum.FRIEND_REQUEST);

        NotificationPublisher publisher = new NotificationPublisher("pub-1", "CorePublisher");
        broker.registerPublisher(publisher);
        publisher.start();

        Map<String, String> data = new HashMap<>();
        data.put("userName", "Alice");
        data.put("userEmail", "alice@example.com");

        publisher.publishNotification(
                messageType,
                "New DM",
                "Hello {userName} ({userEmail}), you have a new direct message!",
                data
        );

        publisher.publishNotification(
                friendRequestType,
                "New Friend Request",
                "{userName}, you received a new friend request.",
                Collections.emptyMap()
        );

        Thread.sleep(1000);
        publisher.stop();
        broker.shutdown();
    }
}


