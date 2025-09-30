package org.lld.notificationsystem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {

    private final String id;

    private final String name;

    private final String email;

    private final String phoneNumber;
    private final Set<NotificationTypeEnum> optedOutTypes;
    private boolean globalOptOut;
    private final Map<NotificationTypeEnum, Set<ChannelType>> preferences;

    public User(String id, String name, String email, String phoneNumber, Set<NotificationTypeEnum> optedOutTypes, Map<NotificationTypeEnum, Set<ChannelType>> preference) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.optedOutTypes = optedOutTypes;
        this.preferences = preference;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Set<NotificationTypeEnum> getOptedOutTypes() {
        return optedOutTypes;
    }

    public boolean isGlobalOptOut() {
        return globalOptOut;
    }



    public Map<NotificationTypeEnum, Set<ChannelType>> getPreferences() {
        return preferences;
    }

    public void setGlobalOptOut(boolean optOut) {
        this.globalOptOut = optOut;
        if(optOut) {
            System.out.println(name + " opted out from all notification");
        }
        else {
            System.out.println(name + " opted back into notification");
        }
    }

    public void subscribeToNotification(NotificationTypeEnum notificationType, ChannelType channel) {
        if(globalOptOut) {
            System.out.println( name + " is globally opted out");
            return;
        }
        preferences.computeIfAbsent(notificationType, k-> new HashSet<>()).add(channel);
        optedOutTypes.remove(notificationType); // remove from opted out if previously opted-out
        System.out.println(name + " subscribe to " + notificationType.name() +
                " via " + channel.name());
    }

    public Set<ChannelType> getPreferredChannels(NotificationTypeEnum type) {
        if (globalOptOut || optedOutTypes.contains(type)) {
            return Collections.emptySet();
        }
        return preferences.getOrDefault(type, Collections.emptySet());
    }
    public boolean isSubscribedTo(NotificationTypeEnum type) {
        return !globalOptOut && !optedOutTypes.contains(type) && preferences.containsKey(type);
    }

    public void optOutFromNotificationType(NotificationTypeEnum type) {
        optedOutTypes.add(type);
        preferences.remove(type); // Remove all channel preferences for this type
        System.out.println(name + " opted out from all " + type + " notifications");
    }



}
