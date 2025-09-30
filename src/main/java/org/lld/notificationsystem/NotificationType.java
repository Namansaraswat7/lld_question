package org.lld.notificationsystem;

public interface NotificationType {
    NotificationTypeEnum getNotificationType();

    default String formatContent(String baseContent, User user) {
        return baseContent; // default implementation
    }

    default boolean hasSpecializedDeliveryRequirement() {
        return false; // default: no special requirement;
    }
}
