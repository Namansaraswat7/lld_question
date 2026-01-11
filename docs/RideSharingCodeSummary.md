# Ride Sharing Application - Code Folder Summary

## 📁 Folder Structure

```
src/main/java/org/lld/ridesharingapplication/
│
├── domain/                          # Domain Entities (Core Business Objects)
│   ├── User.java                    # User entity
│   ├── Vehicle.java                 # Vehicle entity
│   ├── Ride.java                    # Ride entity
│   ├── RideStatus.java              # Enum: Ride status values
│   └── VehicleType.java             # Enum: Vehicle type values
│
├── repository/                      # Data Access Layer
│   ├── UserRepository.java          # Interface for user data access
│   ├── UserRepositoryImpl.java     # Implementation: Thread-safe user storage
│   ├── VehicleRepository.java       # Interface for vehicle data access
│   ├── VehicleRepositoryImpl.java   # Implementation: Thread-safe vehicle storage
│   ├── RideRepository.java          # Interface for ride data access
│   └── RideRepositoryImpl.java     # Implementation: Thread-safe ride storage
│
├── service/                         # Business Logic Layer
│   ├── UserService.java             # User business operations
│   ├── VehicleService.java           # Vehicle business operations
│   ├── RideService.java             # Ride business operations
│   ├── RideStrategy.java            # Interface: Ride selection strategy
│   ├── EarliestEndingRideStrategy.java      # Strategy: Select earliest ending ride
│   ├── LowestDurationRideStrategy.java       # Strategy: Select shortest duration ride
│   ├── MostVacantRideStrategy.java          # Strategy: Select most vacant ride
│   └── PreferredVehicleRideStrategy.java   # Strategy: Select preferred vehicle ride
│
├── RideSharingFacade.java           # Facade: Single entry point for clients
└── RideSharingDemo.java             # Demo: Example usage
```

---

## 📦 Domain Entities & Fields

### 1. User (`domain/User.java`)

**Purpose:** Represents a user who can offer rides (driver) or consume rides (passenger)

**Fields:**
```java
- name: String           // Unique identifier, user's name
- gender: String         // User's gender (M/F)
- age: int               // User's age in years
```

**Key Methods:**
- `getName(): String`
- `getGender(): String`
- `getAge(): int`
- `toString(): String`

**Relationships:**
- Owns multiple Vehicles (1:N)
- Offers multiple Rides as driver (1:N)
- Takes multiple Rides as passenger (M:N)

---

### 2. Vehicle (`domain/Vehicle.java`)

**Purpose:** Represents a vehicle owned by a user that can be used for rides

**Fields:**
```java
- vehicleOwnerName: String    // Name of the user who owns this vehicle
- vehicleName: String         // Model/name of vehicle (e.g., "Swift", "Activa")
- vehicleType: VehicleType    // Type: CAR or BIKE (optional, nullable)
- regNo: String               // Registration number (unique identifier)
```

**Key Methods:**
- `getVehicleOwnerName(): String`
- `getVehicleName(): String`
- `getVehicleType(): VehicleType`
- `getRegNo(): String`
- `setVehicleType(vehicleType: VehicleType): void`
- `toString(): String`

**Relationships:**
- Belongs to one User (N:1 via vehicleOwnerName)
- Used in multiple Rides (1:N)

---

### 3. Ride (`domain/Ride.java`)

**Purpose:** Represents a ride offered by a driver that can be shared with passengers

**Fields:**
```java
- ride_id: String                    // Unique identifier (UUID)
- origin: String                     // Starting location
- destination: String                 // Ending location
- vehicleForRide: Vehicle             // Vehicle being used
- totalSeatsForRide: Integer          // Available seats (decreases as passengers join)
- rideStatus: RideStatus              // Current status (START/IN_PROGRESS/COMPLETE/NOT_STARTED)
- rideUser: User                      // Driver offering the ride
- rideFare: Integer                   // Fare amount (optional, nullable)
- startTime: LocalDateTime            // Start date and time
- durationHours: Integer              // Duration in hours
```

**Computed Fields:**
```java
- endTime: LocalDateTime              // Calculated: startTime + durationHours
```

**Key Methods:**
- `getRide_id(): String`
- `getOrigin(): String`
- `getDestination(): String`
- `getVehicleForRide(): Vehicle`
- `getTotalSeatsForRide(): Integer`
- `setTotalSeatsForRide(seats: Integer): void`  // Updates available seats
- `getRideStatus(): RideStatus`
- `getRideUser(): User`
- `getStartTime(): LocalDateTime`
- `getDurationHours(): Integer`
- `getEndTime(): LocalDateTime`        // Computed: startTime + durationHours
- `toString(): String`

**Relationships:**
- Belongs to one User (driver) via rideUser (N:1)
- Uses one Vehicle via vehicleForRide (N:1)
- Has one RideStatus (N:1)

---

### 4. RideStatus (`domain/RideStatus.java`) - Enum

**Purpose:** Represents the current status of a ride

**Values:**
```java
START          // Ride offered, ready to accept passengers
IN_PROGRESS    // Ride has started, currently ongoing
COMPLETE       // Ride has been completed
NOT_STARTED    // Ride created but not started (initial state)
```

**Usage:**
- New rides created with status `START`
- Only rides with status `START` are available for selection

---

### 5. VehicleType (`domain/VehicleType.java`) - Enum

**Purpose:** Represents the type/category of a vehicle

**Values:**
```java
CAR    // Four-wheeled motor vehicle
BIKE   // Two-wheeled motor vehicle
```

**Usage:**
- Optional field for vehicle categorization
- Used in preference-based ride selection

---

## 🔄 Application Flow

### Flow 1: User Registration

**File:** `RideSharingFacade.java` → `UserService.java` → `UserRepository.java`

**Sequence:**
```
1. Client calls: RideSharingFacade.create_user(name, gender, age)
   ↓
2. Facade delegates to: UserService.createUser(name, gender, age)
   ↓
3. UserService validates:
   - name is not null/empty
   - gender is not null/empty
   - age > 0
   - user doesn't already exist
   ↓
4. UserService creates: new User(name, gender, age)
   ↓
5. UserService calls: UserRepository.save(user)
   ↓
6. UserRepositoryImpl stores user in ConcurrentHashMap<String, User>
   ↓
7. Returns saved User object
```

**Code Path:**
```
RideSharingFacade.create_user()
  └─> UserService.createUser()
      └─> UserRepository.save()
          └─> UserRepositoryImpl.save() [stores in ConcurrentHashMap]
```

---

### Flow 2: Vehicle Addition

**File:** `RideSharingFacade.java` → `VehicleService.java` → `VehicleRepository.java` + `UserRepository.java`

**Sequence:**
```
1. Client calls: RideSharingFacade.create_vehicle(ownerName, vehicleName, regNo)
   ↓
2. Facade delegates to: VehicleService.createVehicle(ownerName, vehicleName, regNo)
   ↓
3. VehicleService validates:
   - ownerName is not null/empty
   - vehicleName is not null/empty
   - regNo is not null/empty
   ↓
4. VehicleService checks: UserRepository.exists(ownerName)
   - If user doesn't exist → throws IllegalArgumentException
   ↓
5. VehicleService checks: VehicleRepository.findByOwnerAndVehicleName()
   - If vehicle already exists → throws IllegalArgumentException
   ↓
6. VehicleService creates: new Vehicle(regNo, vehicleName, ownerName)
   ↓
7. VehicleService calls: VehicleRepository.save(vehicle)
   ↓
8. VehicleRepositoryImpl stores vehicle in ConcurrentHashMap<String, List<Vehicle>>
   ↓
9. Returns saved Vehicle object
```

**Code Path:**
```
RideSharingFacade.create_vehicle()
  └─> VehicleService.createVehicle()
      ├─> UserRepository.exists() [validates owner]
      └─> VehicleRepository.save()
          └─> VehicleRepositoryImpl.save() [stores in ConcurrentHashMap]
```

---

### Flow 3: Offering a Ride

**File:** `RideSharingFacade.java` → `RideService.java` → `RideRepository.java` + `UserRepository.java` + `VehicleRepository.java`

**Sequence:**
```
1. Client calls: RideSharingFacade.offer_ride(name, vehicleName, seats_available, origin, destination, start, durationHours)
   ↓
2. Facade delegates to: RideService.offerRide(name, vehicleName, seatsAvailable, origin, destination, start, durationHours)
   ↓
3. RideService validates all parameters:
   - name, vehicleName, origin, destination are not null/empty
   - seatsAvailable > 0
   - start is not null
   - durationHours > 0
   ↓
4. RideService fetches: UserRepository.findByName(name)
   - If user doesn't exist → throws IllegalArgumentException
   ↓
5. RideService fetches: VehicleRepository.findByOwnerAndVehicleName(name, vehicleName)
   - If vehicle doesn't exist or doesn't belong to user → throws IllegalArgumentException
   ↓
6. RideService creates: new Ride(UUID.randomUUID(), origin, destination, vehicle, seatsAvailable, RideStatus.START, driver, durationHours, start)
   ↓
7. RideService calls: RideRepository.save(ride)
   ↓
8. RideRepositoryImpl stores ride:
   - In ConcurrentHashMap<String, Ride> (by ride_id)
   - In ConcurrentHashMap<String, List<Ride>> (by driver name)
   ↓
9. Returns saved Ride object
```

**Code Path:**
```
RideSharingFacade.offer_ride()
  └─> RideService.offerRide()
      ├─> UserRepository.findByName() [validates driver]
      ├─> VehicleRepository.findByOwnerAndVehicleName() [validates vehicle]
      └─> RideRepository.save()
          └─> RideRepositoryImpl.save() [stores in ConcurrentHashMap]
```

**State Changes:**
- New Ride created with `rideStatus = START`
- `totalSeatsForRide = seatsAvailable`

---

### Flow 4: Selecting a Ride

**File:** `RideSharingFacade.java` → `RideService.java` → `RideRepository.java` + `RideStrategy.java`

**Sequence:**
```
1. Client calls: RideSharingFacade.select_ride(name, origin, destination, seats_required, rideStrategy)
   ↓
2. Facade delegates to: RideService.selectRide(passengerName, origin, destination, seatsRequired, rideStrategy)
   ↓
3. RideService validates all parameters:
   - passengerName, origin, destination are not null/empty
   - seatsRequired > 0
   - rideStrategy is not null
   ↓
4. RideService checks: UserRepository.exists(passengerName)
   - If user doesn't exist → throws IllegalArgumentException
   ↓
5. RideService queries: RideRepository.findByOriginAndDestination(origin, destination)
   ↓
6. RideService filters eligible rides:
   - ride.getTotalSeatsForRide() >= seatsRequired
   - ride.getRideStatus() == RideStatus.START
   ↓
7. If no eligible rides → returns null
   ↓
8. RideService applies strategy: rideStrategy.selectRide(eligibleRides)
   ↓
9. Strategy implementations:
   - EarliestEndingRideStrategy: Selects ride with earliest endTime
   - LowestDurationRideStrategy: Selects ride with minimum durationHours
   - MostVacantRideStrategy: Selects ride with maximum totalSeatsForRide
   - PreferredVehicleRideStrategy: Selects ride with matching vehicle name
   ↓
10. RideService updates selected ride:
    - selectedRide.setTotalSeatsForRide(currentSeats - seatsRequired)
    ↓
11. RideService records selection:
    - ridesTakenByPassenger.computeIfAbsent(passengerName, ...).add(selectedRide)
    ↓
12. Returns selected Ride object
```

**Code Path:**
```
RideSharingFacade.select_ride()
  └─> RideService.selectRide()
      ├─> UserRepository.exists() [validates passenger]
      ├─> RideRepository.findByOriginAndDestination() [finds eligible rides]
      ├─> Filters by seats and status
      ├─> RideStrategy.selectRide() [applies selection strategy]
      │   ├─> EarliestEndingRideStrategy.selectRide()
      │   ├─> LowestDurationRideStrategy.selectRide()
      │   ├─> MostVacantRideStrategy.selectRide()
      │   └─> PreferredVehicleRideStrategy.selectRide()
      ├─> Updates ride.totalSeatsForRide
      └─> Records in ridesTakenByPassenger map
```

**State Changes:**
- Selected Ride: `totalSeatsForRide` decreases by `seatsRequired`
- Passenger's ride history updated in `ridesTakenByPassenger` map

---

### Flow 5: Getting Rides Offered by User

**File:** `RideSharingFacade.java` → `RideService.java` → `RideRepository.java`

**Sequence:**
```
1. Client calls: RideSharingFacade.getRidesOfferedByUser(name)
   ↓
2. Facade delegates to: RideService.getRidesOfferedByUser(name)
   ↓
3. RideService validates: name is not null/empty
   ↓
4. RideService queries: RideRepository.findByDriverName(name)
   ↓
5. RideRepositoryImpl looks up in ConcurrentHashMap<String, List<Ride>>
   ↓
6. Returns List<Ride> (empty list if none found)
```

**Code Path:**
```
RideSharingFacade.getRidesOfferedByUser()
  └─> RideService.getRidesOfferedByUser()
      └─> RideRepository.findByDriverName()
          └─> RideRepositoryImpl.findByDriverName() [looks up in ConcurrentHashMap]
```

---

### Flow 6: Getting Rides Taken by User

**File:** `RideSharingFacade.java` → `RideService.java` → Internal tracking map

**Sequence:**
```
1. Client calls: RideSharingFacade.getRidesTakenByUser(name)
   ↓
2. Facade delegates to: RideService.getRidesTakenByUser(name)
   ↓
3. RideService validates: name is not null/empty
   ↓
4. RideService queries: ridesTakenByPassenger.getOrDefault(name, emptyList())
   ↓
5. Returns List<Ride> from internal ConcurrentHashMap tracking
```

**Code Path:**
```
RideSharingFacade.getRidesTakenByUser()
  └─> RideService.getRidesTakenByUser()
      └─> ridesTakenByPassenger.getOrDefault() [internal ConcurrentHashMap]
```

---

## 🔀 Complete Use Case Flow Example

### Scenario: User Registration → Vehicle Addition → Offer Ride → Select Ride

**Step-by-Step Code Execution:**

```java
// 1. Initialize Facade
RideSharingFacade facade = new RideSharingFacade();
// Flow: Creates repositories and services internally

// 2. Register User "John"
facade.create_user("John", "M", 26);
// Flow: Facade → UserService → UserRepository → Stores User in ConcurrentHashMap

// 3. Add Vehicle for John
facade.create_vehicle("John", "Swift", "KA-09-32321");
// Flow: Facade → VehicleService → Validates User → VehicleRepository → Stores Vehicle

// 4. Register User "Smith"
facade.create_user("Smith", "M", 30);
// Flow: Facade → UserService → UserRepository → Stores User

// 5. John Offers Ride
LocalDateTime start = LocalDateTime.of(2024, 3, 21, 10, 0);
facade.offer_ride("John", "Swift", 3, "Bangalore", "Mysore", start, 3);
// Flow: Facade → RideService → Validates User & Vehicle → Creates Ride → RideRepository → Stores Ride

// 6. Smith Selects Ride
Ride ride = facade.select_ride("Smith", "Bangalore", "Mysore", 2, 
                                new EarliestEndingRideStrategy());
// Flow: Facade → RideService → Validates User → Finds Eligible Rides → 
//       Applies Strategy → Updates Seats → Records Selection → Returns Ride

// 7. Query History
List<Ride> johnsRides = facade.getRidesOfferedByUser("John");
// Flow: Facade → RideService → RideRepository → Returns List

List<Ride> smithsRides = facade.getRidesTakenByUser("Smith");
// Flow: Facade → RideService → Internal Map → Returns List
```

---

## 📊 Data Storage Structure

### UserRepositoryImpl
```java
ConcurrentHashMap<String, User> users
// Key: user.name
// Value: User object
```

### VehicleRepositoryImpl
```java
ConcurrentHashMap<String, List<Vehicle>> vehiclesByOwner
// Key: vehicle.vehicleOwnerName
// Value: List of Vehicle objects owned by that user
```

### RideRepositoryImpl
```java
ConcurrentHashMap<String, Ride> ridesById
// Key: ride.ride_id
// Value: Ride object

ConcurrentHashMap<String, List<Ride>> ridesByDriver
// Key: ride.rideUser.name
// Value: List of Ride objects offered by that driver
```

### RideService (Internal Tracking)
```java
ConcurrentHashMap<String, List<Ride>> ridesTakenByPassenger
// Key: passenger name
// Value: List of Ride objects taken by that passenger
```

---

## 🎯 Key Design Patterns

### 1. Facade Pattern
- **File:** `RideSharingFacade.java`
- **Purpose:** Single entry point, hides complexity
- **Hides:** Service layer, repository layer, dependency injection

### 2. Repository Pattern
- **Files:** `*Repository.java` (interfaces), `*RepositoryImpl.java` (implementations)
- **Purpose:** Data access abstraction
- **Storage:** Thread-safe ConcurrentHashMap

### 3. Service Layer Pattern
- **Files:** `UserService.java`, `VehicleService.java`, `RideService.java`
- **Purpose:** Business logic separation
- **Contains:** Validation, business rules, orchestration

### 4. Strategy Pattern
- **Files:** `RideStrategy.java` (interface), `*RideStrategy.java` (implementations)
- **Purpose:** Pluggable ride selection algorithms
- **Strategies:** EarliestEnding, LowestDuration, MostVacant, PreferredVehicle

### 5. Dependency Injection
- **Pattern:** Constructor injection
- **Usage:** Services receive repositories via constructors
- **Benefit:** Loose coupling, testability

---

## 🔐 Thread Safety

### Thread-Safe Collections:
- `ConcurrentHashMap` used in all repositories
- `ConcurrentHashMap` used for passenger ride tracking
- All operations are thread-safe by default

### Concurrent Operations Supported:
- Multiple users can register simultaneously
- Multiple drivers can offer rides concurrently
- Multiple passengers can select rides concurrently
- No race conditions in seat allocation

---

## 📝 Key Files Summary

| File | Purpose | Key Responsibilities |
|------|---------|---------------------|
| `User.java` | Domain Entity | Represents user with name, gender, age |
| `Vehicle.java` | Domain Entity | Represents vehicle with owner, name, regNo |
| `Ride.java` | Domain Entity | Represents ride with origin, destination, seats, timing |
| `UserService.java` | Business Logic | User creation, validation, retrieval |
| `VehicleService.java` | Business Logic | Vehicle creation, validation, retrieval |
| `RideService.java` | Business Logic | Ride offering, selection, history tracking |
| `RideSharingFacade.java` | Entry Point | Simplified API for all operations |
| `*Repository.java` | Data Access | Interface for data operations |
| `*RepositoryImpl.java` | Data Storage | Thread-safe ConcurrentHashMap storage |
| `*RideStrategy.java` | Selection Logic | Pluggable ride selection algorithms |

---

## 🚀 Entry Points

### Main Entry Point:
- **File:** `RideSharingFacade.java`
- **Constructor:** Initializes all repositories and services
- **Methods:** All public API methods

### Demo Entry Point:
- **File:** `RideSharingDemo.java`
- **Method:** `main(String[] args)`
- **Purpose:** Example usage demonstration

---

## 📋 Method Call Chain Summary

### User Registration:
```
RideSharingFacade.create_user()
  → UserService.createUser()
    → UserRepository.save()
      → UserRepositoryImpl.save()
```

### Vehicle Addition:
```
RideSharingFacade.create_vehicle()
  → VehicleService.createVehicle()
    → UserRepository.exists()
    → VehicleRepository.save()
      → VehicleRepositoryImpl.save()
```

### Offer Ride:
```
RideSharingFacade.offer_ride()
  → RideService.offerRide()
    → UserRepository.findByName()
    → VehicleRepository.findByOwnerAndVehicleName()
    → RideRepository.save()
      → RideRepositoryImpl.save()
```

### Select Ride:
```
RideSharingFacade.select_ride()
  → RideService.selectRide()
    → UserRepository.exists()
    → RideRepository.findByOriginAndDestination()
    → RideStrategy.selectRide()
      → [Strategy Implementation].selectRide()
    → Update ride.totalSeatsForRide
    → Record in ridesTakenByPassenger
```

---

This summary provides a complete overview of the code structure, entity fields, and application flows in the Ride Sharing Application.

