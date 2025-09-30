package org.lld.notificationsystem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationBroker {

    private final Map<String, User> users;

    private final Map<NotificationTypeEnum, Set<User>> subscriptions;

    private final Map<ChannelType, NotificationChannel> channelRegistry;

    private final ExecutorService executorService;

    private final Map<String, Publisher> registeredPublishers;

    private final Map<String, Notification> notificationHistory;

    public NotificationBroker() {
        this.users = new ConcurrentHashMap<>();
        this.subscriptions = new ConcurrentHashMap<>();
        this.channelRegistry = new ConcurrentHashMap<>();
        this.registeredPublishers = new ConcurrentHashMap<>();
        this.executorService = Executors.newCachedThreadPool();
        this.notificationHistory = new ConcurrentHashMap<>();
    }

    public void registerUser(User user) {
        this.users.put(user.getId(), user);
        System.out.println("User -> " + user.getName() + " registered with Notification broker");
    }

    public void unRegisterUser(String userId) {

            User user = users.remove(userId);
            if (user!= null) {
                subscriptions.values().forEach(userSet -> userSet.remove(user));
                System.out.println("User -> " + user.getName() + " unregerstered from Notification broker");
            }
    }

    public void registerPublisher(Publisher publisher) {
        this.registeredPublishers.put(publisher.getPublisherId(),publisher);
        publisher.setBroker(this);
        System.out.println("Publisher -> " + publisher.getName() + " registered with Notification broker");
    }

    public void unRegisterPublisher(String publisherId) {
        this.registeredPublishers.remove(publisherId);
    }

    public void addNotificationChannel(ChannelType channelType, NotificationChannel notificationChannel) {
        channelRegistry.put(channelType, notificationChannel);
        System.out.println("Channel -> " + channelType.name()+ " added to broker");
    }

    public void subscribeUserToType(String userId, NotificationTypeEnum type) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found for subscription: " + userId);
            return;
        }
        subscriptions.computeIfAbsent(type, t -> Collections.newSetFromMap(new ConcurrentHashMap<>())).add(user);
        System.out.println("User -> " + user.getName() + " subscribed in broker to " + type);
    }

    public void unsubscribeUserFromType(String userId, NotificationTypeEnum type) {
        User user = users.get(userId);
        if (user == null) return;
        Set<User> set = subscriptions.get(type);
        if (set != null) {
            set.remove(user);
        }
        System.out.println("User -> " + user.getName() + " unsubscribed in broker from " + type);
    }


    public void sendNotification(Notification notification) {

        System.out.println("Sending notification -> " + notification.getTitle());

        Set<User> subscribedUsers = subscriptions.getOrDefault(notification.getType().getNotificationType(), Collections.emptySet());

        if(subscribedUsers.isEmpty()) {
            System.out.println("No users subscribed to " + notification.getType().toString());
            return;
        }

        for(User user: subscribedUsers) {
            if(user.isGlobalOptOut()) continue;

            Set<ChannelType> preferredChannels = user.getPreferredChannels(notification.getType().getNotificationType());
            if (preferredChannels.isEmpty()) {
                System.out.println("No preferred channels for user " + user.getName());
                continue;
            }

            for (ChannelType channelType : preferredChannels) {
                NotificationChannel channel = channelRegistry.get(channelType);
                if (channel == null) {
                    System.out.println("Channel " + channelType.name() + " not registered with broker");
                    continue;
                }
                if (!channel.isChannelAvailableForUser(user)) {
                    System.out.println("Channel " + channel.getChannelName() + " unavailable for user " + user.getName());
                    continue;
                }
                executorService.submit(() -> channel.sendNotification(user, notification));
            }
        }

        notificationHistory.put(notification.getId(),notification);

    }

    public void shutdown() {
        executorService.shutdown();
    }
}
