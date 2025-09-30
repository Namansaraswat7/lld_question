package org.lld.notificationsystem;

import java.time.LocalDateTime;
import java.util.Map;

public class Notification {
    private final String id;
    private final NotificationType type;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final Map<String, String> personalizationData;
    private final User sender;


    public Notification(String id, NotificationType type, String title, String content, Map<String, String> personalizationData, User sender) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.personalizationData = personalizationData != null? personalizationData: Map.of();
        this.sender = sender;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Map<String, String> getPersonalizationData() {
        return personalizationData;
    }

    public User getSender() {
        return sender;
    }

    public String getPersonalizedContent(User recipient) {
        String personalizedContent = content;
        personalizedContent = personalizedContent.replace("{userName}",recipient.getName());
        personalizedContent = personalizedContent.replace("{userEmail}", recipient.getEmail());

        for(Map.Entry<String,String> entry : personalizationData.entrySet()) {
            personalizedContent = personalizedContent.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return this.type.formatContent(personalizedContent,recipient);

    }

}
