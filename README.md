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

