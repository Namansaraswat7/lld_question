## Notification System (LLD)

This module implements a modular, extensible notification system that supports multiple channels, user preferences, and notification types. It follows SOLID principles, using interfaces, enums, and small factories for clean extensibility.

### Core Entities

#### User (`org.lld.notificationsystem.User`)
- id: String — globally unique identifier for the user
- name: String — display name
- email: String — email address (used by Email channel)
- phoneNumber: String — phone number (used by SMS channel)
- globalOptOut: boolean — if true, the user will not receive any notifications
- optedOutTypes: Set<NotificationTypeEnum> — per-type opt-out list
- preferences: Map<NotificationTypeEnum, Set<ChannelType>> — which channels the user prefers for each notification type

Key methods:
- subscribeToNotification(type, channel): add a preferred channel for a type (removes type from opt-outs)
- getPreferredChannels(type): returns the preferred `ChannelType`s for a type
- isSubscribedTo(type): checks if the user is subscribed for a type considering opt-outs
- optOutFromNotificationType(type): opt out from a specific type (removes its channel prefs)
- setGlobalOptOut(optOut): toggle global opt-out

Relationships:
- Preferences and opt-outs drive routing in the `NotificationBroker`.

#### Notification (`org.lld.notificationsystem.Notification`)
- id: String — unique notification id
- type: NotificationType — behavior object describing the logical type
- title: String — headline/title of the notification
- content: String — base content with placeholders (e.g., `{userName}`, `{userEmail}`)
- createdAt: LocalDateTime — creation timestamp
- personalizationData: Map<String, String> — additional template variables
- sender: User — optional, who initiated the notification

Key methods:
- getPersonalizedContent(recipient): fills placeholders and lets `NotificationType` post-process content

Relationships:
- Refers to `NotificationType` for formatting rules.
- Delivered to `User`s via channels by the broker.

#### NotificationType (`org.lld.notificationsystem.NotificationType`)
Interface for behavior per logical type.

Key methods:
- getNotificationType(): NotificationTypeEnum — enum key for the type
- formatContent(baseContent, user): hook to customize content per type
- hasSpecializedDeliveryRequirement(): boolean — extension hook for routing rules

Concrete types:
- `MessageNotificationType` — NEW_MESSAGE
- `FriendRequestNotificationType` — FRIEND_REQUEST

Factory:
- `org.lld.notificationsystem.factory.NotificationTypeFactory` — returns a `NotificationType` for a `NotificationTypeEnum`.

#### NotificationTypeEnum (`org.lld.notificationsystem.NotificationTypeEnum`)
Enum keys for notification types: NEW_MESSAGE, FRIEND_REQUEST, SYSTEM_ALERT, PROMOTIONAL, SECURITY_ALERT, CUSTOM_EVENT.

Used as stable keys in user preferences and broker subscriptions.

#### ChannelType (`org.lld.notificationsystem.ChannelType`)
Enum keys for delivery channels: EMAIL, SMS, PUSH, IN_APP.

Used as stable keys in user preferences and broker channel registry.

#### NotificationChannel (`org.lld.notificationsystem.NotificationChannel`)
Interface abstracting channel delivery.

Key methods:
- getChannelName(): String — human-readable channel name
- sendNotification(user, notification): boolean — attempt delivery; return true if delivered
- isChannelAvailableForUser(user): boolean — channel- and user-specific availability

Concrete channels (`org.lld.notificationsystem.channels`):
- `EmailChannel` — console-simulated email send
- `SmsChannel` — console-simulated SMS send
- `PushChannel` — console-simulated push send
- `InAppChannel` — console-simulated in-app send

Factory:
- `org.lld.notificationsystem.factory.ChannelFactory` — builds a `NotificationChannel` for a `ChannelType`.

#### Publisher (`org.lld.notificationsystem.Publisher`)
Interface for producers that create and publish notifications.

Key methods:
- getName(), getPublisherId() — identity
- setBroker(broker) — inject the broker to route notifications
- publishNotification(type, title, content, personalizedData) — create `Notification` and pass to broker
- start(), stop() — lifecycle

Concrete publisher:
- `org.lld.notificationsystem.publishers.NotificationPublisher` — basic implementation

#### NotificationBroker (`org.lld.notificationsystem.NotificationBroker`)
Central coordinator that manages users, subscriptions, channels, and asynchronous dispatch.

Fields:
- users: Map<String, User> — registry of known users
- subscriptions: Map<NotificationTypeEnum, Set<User>> — which users should receive each type
- channelRegistry: Map<ChannelType, NotificationChannel> — available channels by type
- registeredPublishers: Map<String, Publisher> — publisher registry
- notificationHistory: Map<String, Notification> — stored notifications by id
- executorService: ExecutorService — async dispatch pool

Key methods:
- registerUser(user), unRegisterUser(userId)
- registerPublisher(publisher), unRegisterPublisher(publisherId)
- addNotificationChannel(type, channel): register a channel implementation under a `ChannelType`
- subscribeUserToType(userId, type), unsubscribeUserFromType(userId, type): manage subscriptions at the broker level
- sendNotification(notification): routes a notification to subscribed users and their preferred channels
- shutdown(): gracefully stop executors

Relationships:
- Uses `User` preferences to select channels.
- Uses `ChannelType`→`NotificationChannel` registry to perform delivery.
- Stores notifications in `notificationHistory` (can be extended for delivery logs).

### How it fits together
1. A `Publisher` publishes a `Notification` (with a `NotificationType`).
2. `NotificationBroker` locates subscribed `User`s by `NotificationTypeEnum`.
3. For each user, the broker looks up preferred `ChannelType`s, resolves the channel instance, validates availability, and dispatches asynchronously.
4. Channels personalize and send the content, returning success/failure (can be extended with retry logic).

### Running the Demo
- Entry point: `org.lld.notificationsystem.Demo`
- What it does:
  - Registers all channels via `ChannelFactory`
  - Creates two users and sets enum-based preferences
  - Subscribes users to two different notification types
  - Publishes notifications through a `NotificationPublisher`
  - Outputs simulated sends to the console

### UML Diagram
See PlantUML class diagram:
- `docs/notification_system.puml`

You can render it with PlantUML or any compatible renderer.

# lld_question

# Customer Support System

A low-level design implementation of a customer support system with comprehensive features for issue management and agent assignment.

## Features

- **Issue Management**: Create, update, resolve, and track customer issues
- **Agent Management**: Add agents with specialized issue type handling
- **Smart Assignment**: Automatic issue assignment to agents based on configurable strategies
- **Search & Filtering**: Find issues by customer email, issue type, or other criteria
- **Work History Tracking**: Complete audit trail of agent work history

## Architecture & Design Patterns Used

### 1. **Repository Pattern**
- `IssueRepository` and `AgentRepository` interfaces with in-memory implementations
- Provides data access abstraction and easy testing

### 2. **Strategy Pattern**
- `IssueAssignmentStrategy` interface with multiple implementations
- `FreeAgentAssignmentStrategy`: Assigns to free agents, then least busy
- `RoundRobinAssignmentStrategy`: Round-robin distribution
- Easily extensible for new assignment algorithms

### 3. **Facade Pattern**
- `CustomerSupportSystem` provides a unified, simplified interface
- Hides complexity of multiple services and repositories

### 4. **Service Layer Pattern**
- `IssueService` and `AgentService` handle business logic
- Clean separation of concerns

## SOLID Principles Implementation

### **Single Responsibility Principle (SRP)**
- Each class has one clear responsibility:
  - `Issue`: Represents issue data
  - `Agent`: Represents agent data and capabilities
  - `IssueService`: Issue business operations
  - `AgentService`: Agent business operations
  - Repository classes: Data access only

### **Open/Closed Principle (OCP)**
- System is open for extension through:
  - New assignment strategies (implement `IssueAssignmentStrategy`)
  - New repository implementations
  - New issue types and statuses
- Closed for modification of existing code

### **Liskov Substitution Principle (LSP)**
- All strategy implementations are interchangeable
- Repository implementations can be swapped seamlessly

### **Interface Segregation Principle (ISP)**
- Focused interfaces: `IssueService`, `AgentService`, `IssueRepository`, `AgentRepository`
- Clients depend only on methods they use

### **Dependency Inversion Principle (DIP)**
- High-level modules depend on abstractions (interfaces)
- Concrete implementations can be injected
- Easy to mock for testing

## API Functions

```java
// Issue Management
createIssue(transactionId, issueType, subject, description, email)
getIssues(filters)
updateIssue(issueId, status, resolution)  
resolveIssue(issueId, resolution)

// Agent Management
addAgent(email, name, issueTypes)
assignIssue(issueId)
viewAgentsWorkHistory()
```

## Key Classes

### Core Entities
- `Issue`: Customer issue with transaction mapping
- `Agent`: Support agent with specializations
- `IssueStatus`: Issue lifecycle states

### Services
- `CustomerSupportSystem`: Main facade
- `IssueService`: Issue operations
- `AgentService`: Agent operations

### Repositories
- In-memory data storage with thread-safe operations
- Support for complex filtering and queries

### Strategies
- Pluggable assignment algorithms
- Currently supports free-agent-first and round-robin

## Thread Safety

The system uses `ConcurrentHashMap` and `AtomicInteger` for thread-safe operations in the repository layer.

## Extensibility

The system is designed for easy extension:
- Add new issue types
- Implement new assignment strategies
- Create different repository backends (database, file, etc.)
- Add new filtering criteria
- Implement notification systems

## Running the Demo

```bash
javac -cp src/main/java src/main/java/org/practice/lld/customersupportsystem/*.java src/main/java/org/practice/lld/customersupportsystem/**/*.java
java -cp src/main/java org.practice.lld.customersupportsystem.CustomerSupportSystemDemo
```

The demo runs all the example scenarios provided in the requirements and demonstrates the complete functionality.

UML diagram 

<img width="2113" height="1847" alt="CustomerSupportSystemLLD" src="https://github.com/user-attachments/assets/ee863f41-0071-4953-9d5b-09ca909820a7" />

