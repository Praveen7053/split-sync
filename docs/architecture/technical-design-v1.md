# SplitSync — Technical Design V1

## 1. System Overview

### 1.1 Purpose

This document defines the technical design of SplitSync V1 based on the architecture defined in the SplitSync Architecture V1 document.

The purpose of this document is to translate the high-level architectural decisions into an implementation-oriented technical design.

It defines:

- Application components.
- Module boundaries.
- Domain responsibilities.
- Data flow.
- Local persistence.
- Backend persistence.
- REST API interaction.
- Synchronization.
- Conflict handling.
- Local peer-to-peer communication.
- Security boundaries.
- Testing structure.

This document is intended to be used as the technical reference during implementation of SplitSync V1.

### 1.2 System Objective

SplitSync is an offline-first group expense management application.

The system allows users to:

- Create a local profile.
- Create and manage Groups.
- Join Groups.
- Add and manage Expenses.
- Define Expense Splits.
- Calculate balances.
- Record Settlements.
- Synchronize data across authorized devices.

The application must remain functional when:

- Internet connectivity is available.
- Internet connectivity is unavailable.
- Backend services are temporarily unavailable.
- Authorized nearby devices are available without internet.
- No network connectivity is available at all.

### 1.3 High-Level System Architecture

SplitSync consists of two primary application environments:

1. Android Client.
2. Backend Server.

The Android Client contains the complete local application experience and local persistence.

The Backend provides centralized persistence, authentication, authorization, and synchronization when internet connectivity is available.

The high-level architecture is:

    ┌──────────────────────────────────────────┐
    │              Android Client              │
    │                                          │
    │  Presentation                            │
    │       ↓                                  │
    │  Application / Domain                    │
    │       ↓                                  │
    │  Repository                              │
    │       ↓                                  │
    │  ┌───────────────┐  ┌─────────────────┐ │
    │  │ Room / SQLite │  │ Sync Layer      │ │
    │  └───────────────┘  └────────┬────────┘ │
    │                              │          │
    └──────────────────────────────┼──────────┘
                                   │
                    ┌──────────────┴──────────────┐
                    │                             │
               Internet                    Local Connectivity
                    │                             │
                    ▼                             ▼
          ┌─────────────────┐          ┌──────────────────┐
          │  Backend Server │          │ Authorized Peer  │
          │                 │          │ Android Device   │
          │ Spring Boot     │          └──────────────────┘
          │ REST API        │
          │                 │
          │ Hibernate/JPA   │
          └────────┬────────┘
                   │
                   ▼
             ┌───────────┐
             │   MySQL   │
             └───────────┘

### 1.4 Android Client

The Android application is the primary user-facing component of SplitSync.

The Android application is responsible for:

- User interaction.
- Local profile management.
- Device identity.
- Group management.
- Expense management.
- Expense Split calculations.
- Balance calculations.
- Settlement management.
- Local persistence.
- Offline operation.
- Synchronization.
- Peer-to-peer communication.

The Android application must not require the backend for normal core expense-management operations.

### 1.5 Android Local-First Data Flow

Normal application operations follow a local-first flow.

For example, when a user creates an Expense:

    User
      ↓
    UI
      ↓
    Use Case
      ↓
    Domain Validation
      ↓
    Repository
      ↓
    Room Transaction
      ↓
    Expense + Expense Splits
      +
    Synchronization Metadata
      ↓
    Local State Updated
      ↓
    UI Reflects New State

The operation does not wait for the backend.

If internet connectivity is available, synchronization may occur afterward.

### 1.6 Online Operation

When internet connectivity is available, the Android application may synchronize local changes with the backend.

The conceptual flow is:

    Android Client
          ↓
    Local Database
          ↓
    Synchronization Layer
          ↓
    REST API
          ↓
    Spring Boot
          ↓
    Application / Domain Logic
          ↓
    Hibernate/JPA
          ↓
    MySQL

The backend validates incoming changes before accepting them.

### 1.7 Offline Operation

When internet connectivity is unavailable, the Android application continues operating against its local Room database.

The conceptual flow is:

    User
      ↓
    Android Application
      ↓
    Domain Logic
      ↓
    Room / SQLite
      ↓
    Local State

Synchronization operations are recorded locally and remain pending until a synchronization channel becomes available.

### 1.8 Offline With Nearby Connectivity

When internet connectivity is unavailable but authorized SplitSync devices are nearby, the application may synchronize through the local peer-to-peer synchronization layer.

The conceptual flow is:

    Device A
        ↓
    Local Database
        ↓
    Peer Synchronization
        ↕
    Authorized Device B
        ↓
    Local Database

Both devices continue to use their own local databases as their local application state.

The peer-to-peer channel is used to exchange changes rather than replacing local persistence.

### 1.9 No Connectivity

When neither internet nor a nearby authorized device is available, the application continues to operate locally.

The flow is:

    User
      ↓
    Android Application
      ↓
    Room / SQLite

Changes remain locally persisted and are marked for future synchronization.

This is a fundamental requirement of SplitSync V1.

### 1.10 Connectivity Handling

The application must not require the user to manually choose between:

    Online Mode

and:

    Offline Mode

Instead, the application should operate using the local-first model continuously.

Connectivity determines which synchronization channels are available.

Conceptually:

    Application Running
          ↓
    Local Database
          ↓
    Connectivity Available?
       /             \
     Yes              No
      ↓                ↓
    Sync Available    Local Only
      ↓
    Continue Local Operation

If internet connectivity is available:

    Backend Synchronization
        +
    Local Application

If internet is unavailable but an authorized peer is available:

    Peer Synchronization
        +
    Local Application

If neither is available:

    Local Application Only

The user should not need to manually switch the application between online and offline modes.

### 1.11 Backend Server

The backend provides centralized services for SplitSync.

The backend is responsible for:

- User authentication.
- User account management where applicable.
- Device registration.
- Authorization.
- Group synchronization.
- Expense synchronization.
- Expense Split synchronization.
- Settlement synchronization.
- Conflict processing.
- Persistent server-side data.
- Synchronization metadata.
- API validation.

The backend must not become a mandatory dependency for normal local expense management.

### 1.12 Backend Architecture

The backend will initially use a modular monolithic architecture.

The high-level structure is:

    REST API
        ↓
    Application Layer
        ↓
    Domain Layer
        ↓
    Persistence Layer
        ↓
    MySQL

The backend will use:

- Java.
- Spring Boot.
- Hibernate/JPA.
- MySQL.
- Versioned REST APIs.

### 1.13 Local Database

The Android application will use:

- Room.
- SQLite.

The local database stores the data required for offline application operation.

This includes, where applicable:

- User.
- Device.
- Group.
- Group Member.
- Expense.
- Expense Split.
- Settlement.
- Synchronization metadata.
- Pending synchronization operations.
- Conflict information.
- Deletion metadata.

The exact schema will be defined in the Database Design sections.

### 1.14 Backend Database

The backend will use MySQL as the primary persistent database.

The backend database stores shared server-side state required for:

- User accounts.
- Registered devices.
- Groups.
- Group memberships.
- Expenses.
- Expense Splits.
- Settlements.
- Synchronization state.
- Conflict information where required.
- Audit information where applicable.

The exact schema will be defined separately.

### 1.15 Domain-Centric Design

The core financial domain must remain independent of the communication mechanism.

The same domain rules must apply whether an operation originates from:

- Android UI.
- Local offline operation.
- Backend synchronization.
- Peer-to-peer synchronization.

For example, an Expense must satisfy the same financial validation rules regardless of how it reaches a device.

### 1.16 Synchronization Architecture

Synchronization is treated as a separate subsystem.

The synchronization layer is responsible for:

- Tracking local changes.
- Maintaining pending operations.
- Sending changes.
- Receiving changes.
- Processing acknowledgments.
- Managing retries.
- Detecting duplicates.
- Detecting conflicts.
- Applying conflict-resolution rules.
- Updating synchronization state.

Synchronization must not be embedded directly into UI components.

### 1.17 Multiple Synchronization Channels

SplitSync V1 supports two synchronization channels:

    Synchronization Layer
          │
          ├───────────────┐
          │               │
          ▼               ▼
      Backend          Peer-to-Peer
      Sync              Sync

Both channels operate against the same logical data model.

A change created offline must remain the same logical change regardless of whether it later travels through:

    Device → Backend → Device

or:

    Device → Peer → Device

### 1.18 Local Source of Truth

For normal Android application operation, the local Room database is the primary local source of truth.

The UI should not directly depend on:

- REST responses.
- Peer-to-peer responses.
- Network availability.

Instead:

    Remote Change
        ↓
    Synchronization Layer
        ↓
    Local Database
        ↓
    UI

This keeps application behavior consistent across online and offline states.

### 1.19 Backend as Shared Synchronization State

The backend provides centralized shared state when internet connectivity is available.

The backend does not replace local state.

Instead:

    Android Local State
          ↕
    Synchronization
          ↕
    Backend Shared State

The goal is eventual convergence of authorized devices toward the same valid logical state.

### 1.20 Stable Entity Identity

Synchronizable entities use stable identifiers that can be generated without backend connectivity.

Important identifiers include:

- `userId`
- `deviceId`
- `groupId`
- `expenseId`
- `expenseSplitId`
- `settlementId`
- `operationId`

This allows entities to be created offline and synchronized later without replacing their identity.

### 1.21 Transactional Local Writes

A business operation that modifies multiple related records must be persisted transactionally.

For example:

    Create Expense
        +
    Create Expense Splits
        +
    Create Sync Operation

must be treated as one logical local transaction where required.

If the transaction fails:

    Rollback
        ↓
    No Partial Financial State

### 1.22 Financial Consistency

The system must preserve financial invariants at every stage.

For a valid Expense:

    Sum(Expense Splits)
        =
    Expense Amount

For a valid Group:

    Sum(All Member Balances)
        =
    Zero

These rules must remain valid:

- Offline.
- Online.
- After synchronization.
- After peer synchronization.
- After conflict resolution.

### 1.23 Security Boundary

The system must treat all external input as untrusted.

This includes:

- REST API requests.
- Synchronization payloads.
- Peer-to-peer data.
- Client-supplied identifiers.
- Client-supplied roles.
- Client-supplied financial calculations.

The general processing flow is:

    External Input
        ↓
    Authentication
        ↓
    Authorization
        ↓
    Validation
        ↓
    Domain Processing
        ↓
    Persistence

### 1.24 Peer-to-Peer Security Boundary

A local network is not automatically trusted.

The peer-to-peer flow must be:

    Device Discovery
        ↓
    Device Authentication
        ↓
    Secure Connection
        ↓
    Group Authorization
        ↓
    Data Exchange
        ↓
    Validation
        ↓
    Local Persistence

Nearby presence alone must never grant access to Group data.

### 1.25 Failure Handling

Failure of any external dependency must not destroy valid local state.

Examples:

    Backend Unavailable
        ↓
    Local Application Continues

    Peer Unavailable
        ↓
    Local Application Continues

    Synchronization Interrupted
        ↓
    Pending Changes Remain Recoverable

    Network Lost
        ↓
    Local Data Remains Available

The system must prefer retryable synchronization failure over local data loss.

### 1.26 Overall Data Flow

The overall system can be summarized as:

    User Action
        ↓
    Android UI
        ↓
    Application / Domain Layer
        ↓
    Local Repository
        ↓
    Room / SQLite
        ↓
    Local State Updated
        ↓
    Sync Operation Created
        ↓
    ┌─────────────────────────────┐
    │                             │
    ▼                             ▼
    Backend Sync             Peer Sync
    │                             │
    ▼                             ▼
    Spring Boot              Authorized Peer
    │                             │
    ▼                             ▼
    MySQL                  Peer Local Database
    │
    ▼
    Synchronization Result
        ↓
    Android Local Database
        ↓
    UI Updated

### 1.27 Technical Design Scope

This Technical Design V1 will define the implementation details required to build the system described above.

The following sections will progressively define:

- Component architecture.
- Android modules.
- Backend modules.
- Package structure.
- Domain models.
- Database schema.
- Synchronization model.
- API contracts.
- Security flows.
- Peer-to-peer communication.
- Testing structure.

The implementation must follow this technical design unless a documented Architecture Decision changes the relevant design.

### 1.28 Technical Design Invariants

The following principles are foundational to the Technical Design V1:

- The Android application is offline-first.
- Local core operations must not require internet connectivity.
- Room/SQLite is the primary local application state.
- The backend is a centralized synchronization and persistence system.
- Peer-to-peer communication is an additional synchronization channel.
- The user should not manually switch between online and offline modes.
- Connectivity determines available synchronization opportunities, not core application availability.
- Stable identifiers must be generated independently of backend availability.
- Financial calculations must remain deterministic and exact.
- Related financial changes must be transactionally consistent.
- Synchronization must be incremental and idempotent.
- Backend and peer-to-peer synchronization must use compatible synchronization semantics.
- External data must be authenticated, authorized, and validated before being persisted.
- Synchronization failures must not cause local data loss.
- The architecture must remain extensible without unnecessarily changing the core expense domain.


## 2. Complete Component Architecture

### 2.1 Purpose

This section defines the major technical components of SplitSync V1 and the responsibility of each component.

The purpose is to establish clear boundaries between:

- Android presentation.
- Application logic.
- Domain logic.
- Local persistence.
- Backend communication.
- Synchronization.
- Peer-to-peer communication.
- Backend services.
- Backend persistence.

Detailed package structures and class-level responsibilities will be defined in later sections.

### 2.2 Component Architecture Overview

SplitSync V1 consists of the following major components:

```text
                         SplitSync V1
                              │
              ┌───────────────┴───────────────┐
              │                               │
              ▼                               ▼
       Android Client                   Backend Server
              │                               │
      ┌───────┼────────┐              ┌───────┼────────┐
      │       │        │              │       │        │
      ▼       ▼        ▼              ▼       ▼        ▼
 Presentation Application          API    Application Persistence
             / Domain              Layer     / Domain    Layer
      │       │
      │       ├───────────────┐
      │       │               │
      ▼       ▼               ▼
   Local   Local Sync     Peer-to-Peer
   Data     Engine          Engine
      │       │               │
      ▼       ▼               ▼
    Room   Backend Sync   Nearby Device
   SQLite
```

The Android Client is responsible for local application functionality.

The Backend Server is responsible for centralized shared state, authentication, authorization, and synchronization when internet connectivity is available.

### 2.3 Android Client Components

The Android Client is divided into the following major components:

1. Presentation Layer.
2. Application Layer.
3. Domain Layer.
4. Data / Repository Layer.
5. Local Persistence Layer.
6. Backend Communication Layer.
7. Synchronization Layer.
8. Peer-to-Peer Communication Layer.
9. Security Layer.

The boundaries between these components must remain explicit.

### 2.4 Presentation Layer

The Presentation Layer is responsible for displaying application state and accepting user interaction.

Responsibilities include:

- Screens.
- UI state.
- Navigation.
- User input.
- Form validation feedback.
- Loading states.
- Error presentation.
- Synchronization status presentation.

The Presentation Layer must not directly access:

- Room DAOs.
- SQLite.
- REST APIs.
- Peer-to-peer communication.

The flow should remain:

```text
UI
 ↓
Application Layer
 ↓
Repository / Domain
```

### 2.5 Application Layer

The Application Layer coordinates application use cases.

Examples include:

- Create User.
- Create Group.
- Join Group.
- Add Group Member.
- Create Expense.
- Update Expense.
- Delete Expense.
- Calculate Group Balance.
- Create Settlement.
- Synchronize Group.
- Resolve Conflict.

The Application Layer coordinates the required Domain and Repository operations.

It should not contain:

- Android UI logic.
- SQL queries.
- HTTP implementation details.
- Low-level peer communication code.

### 2.6 Domain Layer

The Domain Layer contains the core business rules of SplitSync.

Responsibilities include:

- Expense validation.
- Expense Split validation.
- Split calculation.
- Balance calculation.
- Settlement rules.
- Group business rules.
- Membership rules.
- Financial invariants.

The Domain Layer must remain independent from:

- Android UI.
- Room.
- SQLite.
- REST.
- Spring Boot.
- Hibernate.
- MySQL.
- Peer-to-peer transport.

The same business rules must be applicable regardless of the communication channel through which data arrives.

### 2.7 Repository Layer

The Repository Layer provides a unified interface to application data.

It abstracts:

- Local database access.
- Backend APIs.
- Synchronization state.
- Peer synchronization where required.

Conceptually:

```text
Application Layer
        ↓
    Repository
        ↓
 ┌──────┼────────┐
 ▼      ▼        ▼
Local  Remote   Sync
Data   API      Engine
```

The Repository Layer decides how application data is obtained or persisted without exposing infrastructure details to the Application Layer.

### 2.8 Local Persistence Layer

The Local Persistence Layer provides persistent storage on the Android device.

Technology:

- Room.
- SQLite.

Responsibilities include:

- Persisting local user data.
- Persisting device data.
- Persisting Groups.
- Persisting Group Members.
- Persisting Expenses.
- Persisting Expense Splits.
- Persisting Settlements.
- Persisting synchronization metadata.
- Persisting pending operations.
- Persisting conflict information.
- Persisting deletion metadata.

Local persistence must work without network connectivity.

### 2.9 Backend Communication Layer

The Backend Communication Layer provides communication between the Android application and the backend REST API.

Responsibilities include:

- HTTP communication.
- Request creation.
- Response parsing.
- Authentication headers.
- API error handling.
- Network timeouts.
- Retry-compatible error reporting.
- API version handling.

This layer must not directly modify application state without going through the appropriate application/data flow.

Conceptually:

```text
Sync Engine
     ↓
Backend Client
     ↓
HTTP / REST
     ↓
Backend API
```

### 2.10 Synchronization Layer

The Synchronization Layer coordinates data synchronization between the local device and external synchronization sources.

It contains two primary synchronization channels:

```text
                Sync Engine
                     │
          ┌──────────┴──────────┐
          │                     │
          ▼                     ▼
   Backend Sync            Peer Sync
          │                     │
          ▼                     ▼
    Internet/API          Local Network
```

Responsibilities include:

- Detecting pending local changes.
- Creating synchronization operations.
- Sending local changes.
- Receiving remote changes.
- Tracking synchronization state.
- Retry handling.
- Idempotency.
- Duplicate detection.
- Conflict detection.
- Conflict resolution.
- Applying remote changes.
- Updating local synchronization metadata.

### 2.11 Backend Synchronization Component

The Backend Synchronization Component handles synchronization with the SplitSync backend.

Responsibilities include:

- Selecting pending operations.
- Sending operations to the backend.
- Receiving server changes.
- Processing acknowledgments.
- Updating synchronization cursors.
- Handling retries.
- Detecting synchronization failures.
- Triggering conflict handling.

The backend synchronization component must not directly control the UI.

The UI observes the resulting local state.

### 2.12 Peer-to-Peer Synchronization Component

The Peer-to-Peer Synchronization Component handles synchronization between authorized nearby SplitSync devices.

Responsibilities include:

- Device discovery.
- Peer connection.
- Peer authentication.
- Secure communication.
- Group authorization.
- Change exchange.
- Synchronization state exchange.
- Conflict detection.
- Synchronization completion.

Peer-to-peer communication is a synchronization mechanism and must not become the primary local persistence mechanism.

### 2.13 Connectivity Component

The Connectivity Component determines which communication channels are currently available.

It may detect:

- Internet availability.
- Local network availability.
- Nearby peer availability.

The application must continue operating regardless of the result.

Conceptually:

```text
Connectivity State
        │
        ├── Internet Available
        │       ↓
        │   Backend Sync
        │
        ├── Internet Unavailable
        │       ↓
        │   Check Peer Availability
        │
        └── No External Connectivity
                ↓
            Local Operation
```

Connectivity detection must not determine whether core business operations are allowed.

### 2.14 Background Synchronization Component

Background synchronization is responsible for performing synchronization without blocking the UI.

The implementation should use Android-supported background execution mechanisms.

WorkManager is the primary candidate for backend synchronization.

Responsibilities include:

- Scheduling synchronization.
- Applying connectivity constraints.
- Retrying failed synchronization.
- Resuming pending synchronization.
- Avoiding unnecessary repeated work.
- Coordinating with synchronization state.

Background execution must never remove or modify valid local data simply because synchronization fails.

### 2.15 Android Security Component

The Android Security Component is responsible for client-side security concerns.

Responsibilities may include:

- Secure credential storage.
- Token storage.
- Device identity protection.
- Cryptographic key management.
- Secure communication configuration.
- Peer authentication support.

Client-side security must not be treated as the sole authorization mechanism.

The backend remains authoritative for online authorization.

### 2.16 Backend Components

The Backend Server consists of the following major components:

1. API Layer.
2. Authentication Component.
3. Authorization Component.
4. Application Layer.
5. Domain Layer.
6. Synchronization Component.
7. Conflict Resolution Component.
8. Persistence Layer.
9. Database.

### 2.17 Backend API Layer

The API Layer exposes versioned REST endpoints.

Base API:

```text
/api/v1/
```

Responsibilities include:

- Request handling.
- Request validation.
- Authentication integration.
- Authorization integration.
- DTO mapping.
- Calling application services.
- Response mapping.
- Error response handling.

The API Layer must not contain core financial business logic.

### 2.18 Backend Authentication Component

The Authentication Component is responsible for establishing the identity of clients communicating with the backend.

Responsibilities may include:

- User authentication.
- Token validation.
- Session validation.
- Token refresh.
- Device authentication.

The exact authentication mechanism will be finalized in the relevant technical design section.

### 2.19 Backend Authorization Component

The Authorization Component determines whether an authenticated user or device is allowed to perform a requested operation.

Authorization must consider:

- User identity.
- Device identity.
- Group membership.
- Group role.
- Resource ownership.
- Operation type.

The backend must never trust authorization information supplied directly by the client.

For example, a client cannot become an administrator simply by sending:

```text
role = ADMIN
```

### 2.20 Backend Application Layer

The Backend Application Layer coordinates backend use cases.

Examples include:

- Register User.
- Register Device.
- Create Group.
- Add Group Member.
- Synchronize Expenses.
- Synchronize Settlements.
- Process Synchronization Operations.
- Resolve Synchronization Conflicts.

The Application Layer coordinates Domain and Persistence operations.

### 2.21 Backend Domain Layer

The Backend Domain Layer contains server-side business rules.

It must validate:

- Expense rules.
- Split rules.
- Settlement rules.
- Group rules.
- Membership rules.
- Financial invariants.
- Synchronization rules.

The backend must independently validate incoming data even when the Android client has already validated it.

### 2.22 Backend Synchronization Component

The Backend Synchronization Component is responsible for processing changes received from Android clients.

Responsibilities include:

- Receiving synchronization operations.
- Validating operations.
- Authenticating the source.
- Authorizing the operation.
- Checking operation idempotency.
- Checking entity versions.
- Detecting conflicts.
- Applying valid changes.
- Returning synchronization results.
- Providing changes required by the client.

The synchronization component must preserve transaction boundaries for related financial changes.

### 2.23 Backend Conflict Resolution Component

The Conflict Resolution Component handles concurrent changes.

Conceptually:

```text
Incoming Change
      ↓
Authentication
      ↓
Authorization
      ↓
Validation
      ↓
Duplicate Check
      ↓
Version / Conflict Check
      ↓
┌─────┴─────┐
│           │
No Conflict Conflict
│           │
▼           ▼
Apply    Resolve
             │
             ▼
        Synchronization Result
```

Conflict resolution rules are defined separately in the Conflict Resolution and Conflict Data Model sections.

### 2.24 Backend Persistence Layer

The Persistence Layer provides database access through Hibernate/JPA.

Responsibilities include:

- Entity persistence.
- Queries.
- Transactions.
- Relationships.
- Database locking where required.
- Persistence-level constraints.

The Persistence Layer must not contain application-level business decisions.

### 2.25 Backend Database

MySQL is the backend persistence system.

It stores the authoritative server-side records required for centralized synchronization.

The database must enforce appropriate:

- Primary keys.
- Foreign keys.
- Unique constraints.
- Indexes.
- Not-null constraints.
- Data types.
- Transaction boundaries.

The detailed schema will be defined later.

### 2.26 Component Interaction — Local Operation

A normal local operation follows:

```text
User
 ↓
Presentation
 ↓
Application Use Case
 ↓
Domain Validation
 ↓
Repository
 ↓
Room Transaction
 ↓
Local State
 ↓
Sync Operation
 ↓
UI observes updated local state
```

The user receives the local result without waiting for synchronization.

### 2.27 Component Interaction — Backend Synchronization

Backend synchronization follows:

```text
Local Sync Engine
 ↓
Pending Sync Operations
 ↓
Backend Client
 ↓
REST API
 ↓
Authentication
 ↓
Authorization
 ↓
Backend Sync Component
 ↓
Validation
 ↓
Conflict Detection
 ↓
Domain Processing
 ↓
MySQL Transaction
 ↓
Synchronization Response
 ↓
Local Sync State Updated
 ↓
Room
 ↓
UI
```

### 2.28 Component Interaction — Peer Synchronization

Peer synchronization follows:

```text
Device A
 ↓
Peer Discovery
 ↓
Peer Authentication
 ↓
Secure Connection
 ↓
Group Authorization
 ↓
Synchronization Handshake
 ↓
Exchange Changes
 ↓
Validate Changes
 ↓
Conflict Detection
 ↓
Apply Changes
 ↓
Room Transaction
 ↓
Synchronization State Updated
```

The same logical synchronization model must be used regardless of the transport mechanism.

### 2.29 Component Interaction — Complete Offline Operation

When there is no external connectivity:

```text
User
 ↓
Presentation
 ↓
Application Layer
 ↓
Domain Layer
 ↓
Repository
 ↓
Room / SQLite
 ↓
Local State
 ↓
Pending Sync Operation
```

No external component is required.

### 2.30 Component Interaction — Online + Peer Availability

If both internet and an authorized nearby peer are available, both synchronization channels may be available.

The system must avoid treating the two channels as independent business systems.

Instead:

```text
                 Local State
                     │
                Sync Engine
                /          \
               /            \
              ▼              ▼
        Backend Sync      Peer Sync
              │              │
              ▼              ▼
          Backend          Peer Device
```

The synchronization layer is responsible for ensuring that the same logical operation is not incorrectly applied multiple times.

### 2.31 Component Dependency Rules

The following dependency rules apply:

```text
Presentation
     ↓
Application
     ↓
Domain
     ↓
Repository Interfaces
     ↓
Data / Infrastructure
```

Infrastructure components may depend on domain abstractions where required.

The Domain Layer must not depend directly on infrastructure implementations.

### 2.32 Forbidden Dependencies

The following dependencies should be avoided:

```text
UI → Room DAO
UI → REST API
UI → MySQL
Domain → Room
Domain → Retrofit
Domain → Android Framework
Domain → Hibernate
Domain → MySQL
```

The purpose is to keep the core business logic independently testable.

### 2.33 Component Ownership

Each component should have a clearly defined responsibility.

| Component | Primary Responsibility |
|---|---|
| Presentation | UI and user interaction |
| Application | Use-case orchestration |
| Domain | Business rules |
| Repository | Data abstraction |
| Local Persistence | Android data storage |
| Backend Client | REST communication |
| Sync Engine | Synchronization orchestration |
| Backend Sync | Server-side synchronization |
| Peer Sync | Local device synchronization |
| Connectivity | Connectivity state |
| Security | Client security |
| API Layer | HTTP interface |
| Authentication | Identity verification |
| Authorization | Access control |
| Backend Domain | Server-side business rules |
| Conflict Resolution | Concurrent change handling |
| Persistence | Database access |
| MySQL | Server-side persistence |

### 2.34 Component Design Principles

All components must follow these principles:

- Single responsibility.
- Clear ownership.
- Explicit dependencies.
- Minimal coupling.
- High cohesion.
- Testability.
- Offline compatibility.
- Failure isolation.
- Security by boundary.
- Deterministic financial behavior.

### 2.35 Component Architecture Invariants

The following rules are considered mandatory for V1:

- Core expense operations must not depend on the backend.
- Local persistence must remain available without network connectivity.
- UI components must not directly access infrastructure.
- Domain logic must remain independent of Android and backend infrastructure.
- Synchronization must remain a separate subsystem.
- Backend and peer-to-peer synchronization must use compatible synchronization semantics.
- Local network presence must not imply authorization.
- Backend authorization must be authoritative for online operations.
- Financial validation must be performed independently by the backend.
- Synchronization failure must not cause local data loss.
- Related financial records must be persisted transactionally.
- The architecture must allow future replacement or extension of individual infrastructure components without redesigning the core domain.

## 3. Android Module Architecture

### 3.1 Purpose

This section defines the technical module structure of the SplitSync Android application.

The goal is to organize the Android application into clear, maintainable modules with well-defined responsibilities and dependencies.

The Android architecture must support:

- Offline-first operation.
- Local persistence.
- Expense management.
- Balance calculation.
- Backend synchronization.
- Peer-to-peer synchronization.
- Security.
- Independent testing.
- Future extensibility.

The module structure must prevent business logic from becoming tightly coupled to Android UI, Room, REST APIs, or peer-to-peer communication.

### 3.2 V1 Module Strategy

SplitSync V1 will initially use a modular package architecture inside the Android application.

The project will not introduce a large number of Gradle modules unnecessarily during the initial implementation.

The initial structure will primarily use:

- One Android application module.
- Clearly separated packages for Presentation, Application, Domain, Data, Sync, Security, and Core functionality.

The architecture must remain structured so that selected areas can be extracted into separate Gradle modules in the future if required.

### 3.3 Android Project Structure

The initial Android project structure is:

```text
android/
└── app/
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── <base-package>/
        │   │       ├── core/
        │   │       ├── presentation/
        │   │       ├── application/
        │   │       ├── domain/
        │   │       ├── data/
        │   │       ├── sync/
        │   │       └── security/
        │   │
        │   ├── res/
        │   └── AndroidManifest.xml
        │
        ├── test/
        │   └── java/
        │
        └── androidTest/
            └── java/
```

The exact base package name will be finalized according to the Android application package selected during project setup.

### 3.4 Top-Level Android Packages

The major Android packages are:

```text
<base-package>/
│
├── core/
├── presentation/
├── application/
├── domain/
├── data/
├── sync/
└── security/
```

Each package has a specific responsibility.

### 3.5 Core Package

The `core` package contains shared technical utilities and infrastructure-neutral common components.

Potential responsibilities include:

- Common result types.
- Common error models.
- Date/time utilities.
- ID generation abstractions.
- Application constants.
- Common validation utilities.
- Common extensions where appropriate.
- Shared configuration abstractions.

The `core` package must not become a dumping ground for unrelated functionality.

Only genuinely shared functionality should be placed here.

### 3.6 Presentation Package

The `presentation` package contains Android UI functionality.

Conceptually:

```text
presentation/
├── user/
├── group/
├── expense/
├── settlement/
├── balance/
├── synchronization/
└── common/
```

Responsibilities include:

- Activities.
- Fragments where used.
- ViewModels.
- UI state.
- UI events.
- Navigation.
- Adapters.
- UI-specific models.

The Presentation Layer communicates with the Application Layer.

It must not directly access:

- Room DAOs.
- Retrofit services.
- SQLite.
- Peer communication APIs.

### 3.7 Application Package

The `application` package contains application use-case orchestration.

Conceptually:

```text
application/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
├── balance/
└── synchronization/
```

Responsibilities include use cases such as:

- CreateUser.
- CreateGroup.
- JoinGroup.
- AddGroupMember.
- CreateExpense.
- UpdateExpense.
- DeleteExpense.
- CalculateBalance.
- CreateSettlement.
- Synchronize.

Use cases coordinate Domain logic and Repository interfaces.

### 3.8 Domain Package

The `domain` package contains the core SplitSync business model and rules.

Conceptually:

```text
domain/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
├── synchronization/
└── common/
```

The Domain Layer should contain:

- Domain models.
- Value objects.
- Domain services.
- Business rules.
- Financial calculations.
- Domain-level validation.
- Domain exceptions.

The Domain Layer must remain independent from Android framework classes.

### 3.9 Domain Package Independence

The Domain package must not directly depend on:

```text
Android UI
Room
SQLite
Retrofit
OkHttp
Spring Boot
Hibernate
MySQL
WorkManager
Peer-to-Peer Android APIs
```

This allows important business logic to be tested using ordinary Java unit tests.

### 3.10 Data Package

The `data` package contains implementations of data access and infrastructure required by the Android application.

Conceptually:

```text
data/
├── local/
├── remote/
├── repository/
└── mapper/
```

Responsibilities include:

- Room database.
- DAO implementations.
- REST API clients.
- DTOs.
- Repository implementations.
- Entity-to-domain mapping.
- DTO-to-domain mapping.

### 3.11 Local Data Package

The local data implementation is responsible for Room/SQLite persistence.

Conceptually:

```text
data/
└── local/
    ├── database/
    ├── entity/
    ├── dao/
    └── mapper/
```

Potential entities include:

```text
UserEntity
DeviceEntity
GroupEntity
GroupMemberEntity
ExpenseEntity
ExpenseSplitEntity
SettlementEntity
SyncOperationEntity
SyncStateEntity
ConflictEntity
```

The exact entities will be finalized in the Database Design section.

### 3.12 DAO Layer

DAO classes provide controlled access to Room.

Conceptually:

```text
dao/
├── UserDao
├── DeviceDao
├── GroupDao
├── GroupMemberDao
├── ExpenseDao
├── ExpenseSplitDao
├── SettlementDao
├── SyncOperationDao
├── SyncStateDao
└── ConflictDao
```

DAOs must contain database queries and persistence operations.

DAOs must not contain core business rules.

For example, an `ExpenseDao` may retrieve Expenses, but it should not decide whether an Expense Split is financially valid.

### 3.13 Room Database Component

The Room database configuration will provide:

- Database instance.
- Entity registration.
- DAO access.
- Database migrations.
- Transaction support.

Conceptually:

```text
RoomDatabase
     │
     ├── UserDao
     ├── DeviceDao
     ├── GroupDao
     ├── GroupMemberDao
     ├── ExpenseDao
     ├── ExpenseSplitDao
     ├── SettlementDao
     ├── SyncOperationDao
     ├── SyncStateDao
     └── ConflictDao
```

The actual DAO list may change as the database model is finalized.

### 3.14 Remote Data Package

The remote data implementation handles communication with the backend.

Conceptually:

```text
data/
└── remote/
    ├── api/
    ├── dto/
    └── mapper/
```

Responsibilities include:

- REST API interfaces.
- Request DTOs.
- Response DTOs.
- Serialization.
- HTTP error mapping.
- Authentication-related API handling.

The remote layer must not directly update UI state.

### 3.15 Repository Package

The Repository implementation connects application use cases with data sources.

Conceptually:

```text
data/
└── repository/
    ├── UserRepositoryImpl
    ├── DeviceRepositoryImpl
    ├── GroupRepositoryImpl
    ├── ExpenseRepositoryImpl
    ├── SettlementRepositoryImpl
    └── SynchronizationRepositoryImpl
```

The repository implementations implement interfaces defined at the appropriate abstraction level.

The application/domain layers must depend on abstractions rather than concrete infrastructure implementations.

### 3.16 Repository Data Flow

The normal local-first repository flow is:

```text
Use Case
   ↓
Repository Interface
   ↓
Repository Implementation
   ↓
Local Data Source
   ↓
Room
```

When synchronization is required:

```text
Repository
   ↓
Local Database
   ↓
Sync Operation
   ↓
Synchronization Engine
```

The local write must not wait for the remote operation.

### 3.17 Synchronization Package

The `sync` package contains synchronization-specific infrastructure.

Conceptually:

```text
sync/
├── engine/
├── backend/
├── peer/
├── operation/
├── conflict/
├── state/
└── worker/
```

Responsibilities include:

- Synchronization orchestration.
- Sync operations.
- Backend synchronization.
- Peer synchronization.
- Conflict processing.
- Synchronization state.
- Retry handling.
- Background synchronization.

### 3.18 Synchronization Engine

The Synchronization Engine coordinates synchronization regardless of transport.

Conceptually:

```text
                Sync Engine
                     │
          ┌──────────┴──────────┐
          │                     │
          ▼                     ▼
    Backend Channel       Peer Channel
```

The engine must operate on common synchronization concepts such as:

- Operation identity.
- Entity identity.
- Entity type.
- Operation type.
- Version.
- Synchronization state.
- Conflict state.

### 3.19 Backend Sync Package

The backend synchronization implementation handles synchronization through REST APIs.

Conceptually:

```text
sync/
└── backend/
    ├── BackendSyncManager
    ├── PushProcessor
    ├── PullProcessor
    └── SyncResponseProcessor
```

The exact class names are implementation details and may change during coding.

Responsibilities include:

- Push local operations.
- Pull remote changes.
- Process server acknowledgments.
- Update synchronization cursors.
- Handle retryable failures.

### 3.20 Peer Sync Package

The peer synchronization implementation handles local device-to-device synchronization.

Conceptually:

```text
sync/
└── peer/
    ├── PeerDiscovery
    ├── PeerConnection
    ├── PeerAuthentication
    ├── PeerSyncManager
    └── PeerMessageProcessor
```

The exact communication technology will be finalized later.

The package must isolate the selected peer-to-peer technology from the rest of the application.

### 3.21 Peer Transport Abstraction

The synchronization layer should not depend directly on a specific peer communication technology.

Conceptually:

```text
Peer Sync
    ↓
Peer Transport Interface
    ↓
┌───────────────┬─────────────────┐
│               │                 │
▼               ▼                 ▼
Implementation  Implementation    Future
     A               B          Transport
```

This allows the underlying transport to change without redesigning synchronization business logic.

### 3.22 Synchronization Operation Package

Synchronization operations represent local changes that need to be exchanged with another synchronization endpoint.

Conceptually:

```text
sync/
└── operation/
    ├── SyncOperation
    ├── OperationType
    ├── OperationStatus
    └── OperationProcessor
```

The exact synchronization model will be defined in the Synchronization Data Model section.

### 3.23 Conflict Package

Conflict-specific functionality will be isolated inside the synchronization architecture.

Conceptually:

```text
sync/
└── conflict/
    ├── ConflictDetector
    ├── ConflictResolver
    └── ConflictResult
```

Conflict resolution must use the business rules defined for each entity type.

### 3.24 Synchronization State Package

Synchronization state tracks the relationship between local data and synchronization channels.

Potential state information includes:

- Last synchronization cursor.
- Last successful synchronization.
- Pending operations.
- Failed operations.
- Retry state.
- Peer synchronization state.

The exact model will be defined in the Synchronization Data Model section.

### 3.25 Background Worker Package

Background synchronization should be isolated from business logic.

Conceptually:

```text
sync/
└── worker/
    ├── BackendSyncWorker
    └── PeerSyncWorker
```

WorkManager may be used for backend synchronization where appropriate.

Workers should invoke synchronization services rather than contain synchronization business logic themselves.

### 3.26 Security Package

The `security` package contains Android-side security functionality.

Conceptually:

```text
security/
├── authentication/
├── credentials/
├── crypto/
├── device/
└── peer/
```

Responsibilities may include:

- Token management.
- Credential storage.
- Cryptographic operations.
- Device identity protection.
- Peer authentication support.

Sensitive values must not be stored using plain-text preferences.

### 3.27 Dependency Direction

The intended Android dependency direction is:

```text
Presentation
      ↓
Application
      ↓
Domain
      ↓
Repository Interfaces
      ↓
Data Implementations
```

Synchronization infrastructure interacts with the data/domain abstractions as required.

The dependency direction must prevent infrastructure from becoming embedded inside business logic.

### 3.28 Infrastructure Isolation

Infrastructure-specific technologies should remain isolated.

For example:

```text
Room
 ↓
data/local

Retrofit / OkHttp
 ↓
data/remote

WorkManager
 ↓
sync/worker

Peer-to-Peer Technology
 ↓
sync/peer
```

Business logic should not directly depend on these technologies.

### 3.29 Android Module Boundary

Although V1 initially uses one Gradle application module, the package boundaries should make future extraction possible.

Potential future Gradle modules may include:

```text
app
core
domain
data
sync
```

This extraction is not required for the initial V1 implementation.

The initial implementation should avoid unnecessary Gradle-module complexity.

### 3.30 Recommended V1 Gradle Structure

The initial project should therefore use:

```text
android/
└── app/
```

with internal package separation.

Future extraction may evolve into:

```text
android/
├── app/
├── core/
├── domain/
├── data/
└── sync/
```

Only introduce these as separate Gradle modules when the project complexity justifies the additional build and dependency management overhead.

### 3.31 Test Package Structure

Android tests should mirror the production architecture.

Unit tests:

```text
app/
└── src/
    └── test/
        └── java/
            └── <base-package>/
                ├── domain/
                ├── application/
                ├── data/
                └── sync/
```

Instrumentation tests:

```text
app/
└── src/
    └── androidTest/
        └── java/
            └── <base-package>/
                ├── data/
                ├── presentation/
                └── sync/
```

### 3.32 Unit Testing Boundaries

The following should be unit-testable without Android framework dependencies:

- Expense calculations.
- Expense Split calculations.
- Balance calculations.
- Settlement calculations.
- Domain validation.
- Conflict rules.
- Synchronization decision logic.

These tests should execute quickly and deterministically.

### 3.33 Database Testing Boundaries

Room-specific behavior should be tested separately.

Database tests should cover:

- DAO queries.
- Transactions.
- Constraints.
- Relationships.
- Migrations.
- Synchronization metadata persistence.

The tests should use a controlled database environment.

### 3.34 Presentation Testing Boundaries

Presentation tests should verify:

- UI state.
- User actions.
- Navigation.
- Error presentation.
- Loading states.
- Synchronization status.

Presentation tests must not be responsible for verifying core financial calculations that belong to the Domain Layer.

### 3.35 Synchronization Testing Boundaries

Synchronization tests should verify:

- Operation creation.
- Operation processing.
- Idempotency.
- Retry behavior.
- Cursor handling.
- Conflict detection.
- Conflict resolution.
- Backend synchronization.
- Peer synchronization.

Transport-specific tests should be separated from synchronization business logic.

### 3.36 Android Package Naming Principles

Package names should:

- Use lowercase.
- Follow the application's base package.
- Reflect responsibility.
- Avoid generic names such as `utils` for unrelated functionality.
- Avoid placing unrelated classes into common packages.

Packages should communicate architectural responsibility.

### 3.37 Avoiding the Utils Problem

A generic package such as:

```text
utils/
```

should not become a container for unrelated helper classes.

Instead, shared functionality should be placed according to its actual responsibility.

For example:

```text
DateTime
 ↓
core/time

Money
 ↓
domain/money

IdGenerator
 ↓
core/identity
```

This keeps package ownership clear.

### 3.38 Dependency Injection

Dependency injection should be used to provide:

- Repository implementations.
- Use cases.
- Database dependencies.
- API clients.
- Synchronization services.
- Security services.

The exact dependency injection framework will be finalized before implementation.

The architecture must avoid creating infrastructure dependencies directly inside business classes.

### 3.39 Configuration

Android configuration should distinguish between:

- Development.
- Testing.
- Production.

Configuration values must not contain hardcoded secrets.

Backend base URLs and environment-specific settings should be externally configurable through appropriate Android build configuration mechanisms.

### 3.40 Logging

Logging should be centralized and controlled.

Development builds may provide detailed logs.

Production builds must avoid logging:

- Authentication tokens.
- Passwords.
- Private cryptographic material.
- Sensitive profile information.
- Financial information unnecessarily.

Synchronization logs should provide enough diagnostic information to investigate failures without exposing sensitive data.

### 3.41 Error Handling

Infrastructure errors must be translated into application-understandable results.

For example:

```text
Network Error
     ↓
Remote Data Layer
     ↓
Retryable Error
     ↓
Synchronization Layer
     ↓
Retry Later
```

A network error must not be treated as a business-data deletion or failure.

### 3.42 Offline Error Handling

When an operation can be completed locally, network failure must not prevent the operation.

For example:

```text
Create Expense
      ↓
Local Validation
      ↓
Room Transaction
      ↓
Success
      ↓
Network unavailable
      ↓
Sync Pending
```

The user should receive a successful local result.

### 3.43 Android Architecture Invariants

The following rules are mandatory for V1:

- V1 will initially use one Android Gradle application module.
- Package boundaries will provide clear architectural separation.
- The Presentation Layer must not directly access Room or REST APIs.
- The Domain Layer must remain independent of Android and infrastructure technologies.
- Core financial calculations must be executable without Android framework dependencies.
- Local persistence must use Room/SQLite.
- Repository abstractions must separate business logic from data infrastructure.
- Synchronization must remain a separate subsystem.
- Backend and peer synchronization must share common synchronization concepts.
- Peer transport technology must remain replaceable behind an abstraction.
- Background workers must orchestrate synchronization rather than contain business logic.
- Security-sensitive functionality must remain isolated from normal business logic.
- Infrastructure-specific dependencies must not leak into the Domain Layer.
- Android UI must observe local application state rather than depend directly on network responses.
- Local writes must not wait for backend synchronization.
- Synchronization failure must not cause local data loss.
- The Android architecture must remain extensible for future Gradle-module extraction.

## 4. Android Package Structure

### 4.1 Purpose

This section defines the detailed package structure of the SplitSync Android application.

The package structure is derived from the Android Module Architecture defined in Section 3.

The primary goals are:

- Keep responsibilities clearly separated.
- Keep business logic independent from infrastructure.
- Make the codebase easy to navigate.
- Prevent package-level coupling.
- Support independent testing.
- Keep synchronization isolated.
- Allow future extraction into separate Gradle modules if required.

### 4.2 Base Package

The application will use a single root package:

```text
<com.company.splitsync>
```

The actual package name will be finalized during Android project creation.

All application packages will exist below this root package.

For the remainder of this document, the placeholder:

```text
<com.company.splitsync>
```

represents the final application package.

### 4.3 Root Package Structure

The initial package structure is:

```text
<com.company.splitsync>/
│
├── core/
├── presentation/
├── application/
├── domain/
├── data/
├── sync/
└── security/
```

Each top-level package represents a major architectural responsibility.

### 4.4 Core Package Structure

The `core` package contains genuinely shared technical functionality.

Recommended structure:

```text
core/
├── common/
├── error/
├── identity/
├── money/
├── time/
└── config/
```

Responsibilities:

```text
common/
    Shared non-domain-specific components

error/
    Common technical/application error abstractions

identity/
    ID generation and identity-related abstractions

money/
    Shared monetary infrastructure where appropriate

time/
    Date/time abstractions and utilities

config/
    Application configuration abstractions
```

The `core` package must remain small.

It must not become a general-purpose container for classes that do not have a clear shared responsibility.

### 4.5 Presentation Package Structure

The `presentation` package contains all Android UI-related code.

Recommended structure:

```text
presentation/
├── common/
├── navigation/
├── user/
├── group/
├── expense/
├── balance/
├── settlement/
└── synchronization/
```

### 4.6 Presentation Common Package

The `presentation/common` package contains reusable UI components.

Potential contents include:

```text
presentation/
└── common/
    ├── components/
    ├── state/
    ├── adapter/
    └── validation/
```

Examples:

```text
components/
    Reusable UI components

state/
    Common UI state representations

adapter/
    Shared RecyclerView adapters where required

validation/
    UI-specific validation and presentation helpers
```

Business validation must remain in the Domain/Application layers.

### 4.7 Navigation Package

The `presentation/navigation` package contains application navigation.

Potential contents:

```text
presentation/
└── navigation/
    ├── AppNavigator
    ├── NavigationDestination
    └── NavigationController
```

The exact implementation will depend on the selected Android UI/navigation approach.

Navigation classes must not contain business logic.

### 4.8 User Presentation Package

The `presentation/user` package handles user-facing profile functionality.

Recommended structure:

```text
presentation/
└── user/
    ├── UserProfileActivity
    ├── UserProfileViewModel
    ├── UserProfileUiState
    └── UserProfileUiEvent
```

The exact Activity/Fragment structure may change during UI implementation.

### 4.9 Group Presentation Package

The `presentation/group` package handles Group-related screens.

Recommended structure:

```text
presentation/
└── group/
    ├── list/
    ├── create/
    ├── detail/
    ├── member/
    └── invitation/
```

Potential responsibilities:

```text
list/
    Display user's Groups

create/
    Create Group UI

detail/
    Group overview and management

member/
    Group member management

invitation/
    Group invitation/joining UI
```

### 4.10 Expense Presentation Package

The `presentation/expense` package handles Expense-related screens.

Recommended structure:

```text
presentation/
└── expense/
    ├── list/
    ├── create/
    ├── detail/
    ├── edit/
    └── split/
```

Responsibilities include:

- Expense listing.
- Expense creation.
- Expense editing.
- Expense details.
- Expense Split selection and presentation.

Expense calculation itself must remain outside the Presentation Layer.

### 4.11 Balance Presentation Package

The `presentation/balance` package handles Group balance presentation.

Recommended structure:

```text
presentation/
└── balance/
    ├── BalanceViewModel
    ├── BalanceUiState
    └── BalanceItem
```

The package displays calculated balances.

It must not independently implement the financial calculation algorithm.

### 4.12 Settlement Presentation Package

The `presentation/settlement` package handles Settlement functionality.

Recommended structure:

```text
presentation/
└── settlement/
    ├── list/
    ├── create/
    ├── detail/
    └── edit/
```

Responsibilities include:

- Settlement history.
- Settlement creation.
- Settlement details.
- Settlement editing where supported.

### 4.13 Synchronization Presentation Package

The `presentation/synchronization` package exposes synchronization status to users.

Potential contents:

```text
presentation/
└── synchronization/
    ├── SyncStatusViewModel
    ├── SyncStatusUiState
    └── SyncStatusFormatter
```

The UI may display:

- Last synchronization.
- Pending changes.
- Synchronization in progress.
- Synchronization failure.
- Conflict status.

The Presentation Layer does not perform synchronization itself.

### 4.14 Application Package Structure

The `application` package contains use-case orchestration.

Recommended structure:

```text
application/
├── user/
├── device/
├── group/
├── expense/
├── balance/
├── settlement/
└── synchronization/
```

### 4.15 User Application Package

```text
application/
└── user/
    ├── CreateUserUseCase
    ├── GetCurrentUserUseCase
    └── UpdateUserProfileUseCase
```

The exact use cases will be finalized after the User Model is implemented.

### 4.16 Device Application Package

```text
application/
└── device/
    ├── GetCurrentDeviceUseCase
    ├── RegisterDeviceUseCase
    └── GetDeviceStatusUseCase
```

Device operations remain separate from user operations because a User and Device represent different domain concepts.

### 4.17 Group Application Package

```text
application/
└── group/
    ├── CreateGroupUseCase
    ├── GetGroupUseCase
    ├── GetGroupsUseCase
    ├── UpdateGroupUseCase
    ├── AddGroupMemberUseCase
    ├── RemoveGroupMemberUseCase
    └── GetGroupMembersUseCase
```

Additional use cases may be introduced as Group functionality expands.

### 4.18 Expense Application Package

```text
application/
└── expense/
    ├── CreateExpenseUseCase
    ├── GetExpenseUseCase
    ├── GetExpensesUseCase
    ├── UpdateExpenseUseCase
    └── DeleteExpenseUseCase
```

The use cases coordinate repositories and Domain services.

### 4.19 Balance Application Package

```text
application/
└── balance/
    └── CalculateGroupBalanceUseCase
```

Balance calculation must use the authoritative local financial data.

The Use Case should delegate the actual calculation rules to the Domain Layer.

### 4.20 Settlement Application Package

```text
application/
└── settlement/
    ├── CreateSettlementUseCase
    ├── GetSettlementUseCase
    ├── GetSettlementsUseCase
    ├── UpdateSettlementUseCase
    └── DeleteSettlementUseCase
```

### 4.21 Synchronization Application Package

```text
application/
└── synchronization/
    ├── SynchronizeUseCase
    ├── GetSyncStatusUseCase
    └── RetrySynchronizationUseCase
```

These use cases coordinate synchronization but do not contain transport-specific implementation.

### 4.22 Domain Package Structure

The `domain` package contains business concepts and business rules.

Recommended structure:

```text
domain/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
├── synchronization/
└── common/
```

### 4.23 User Domain Package

```text
domain/
└── user/
    ├── User
    ├── UserId
    ├── DisplayName
    ├── PhoneNumber
    ├── EmailAddress
    └── UserValidator
```

Only concepts that are actually required by V1 should be introduced.

Optional profile fields should not be added prematurely.

### 4.24 Device Domain Package

```text
domain/
└── device/
    ├── Device
    ├── DeviceId
    ├── DeviceStatus
    └── DeviceValidator
```

The Device domain must remain independent of Android hardware identifiers.

### 4.25 Group Domain Package

```text
domain/
└── group/
    ├── Group
    ├── GroupId
    ├── GroupMember
    ├── GroupMemberId
    ├── GroupRole
    └── GroupValidator
```

The Group is the primary shared-expense boundary.

### 4.26 Expense Domain Package

```text
domain/
└── expense/
    ├── Expense
    ├── ExpenseId
    ├── ExpenseSplit
    ├── ExpenseSplitId
    ├── SplitMethod
    ├── ExpenseCalculator
    ├── ExpenseValidator
    └── BalanceCalculator
```

The exact class breakdown will be refined during Domain Model Design.

### 4.27 Settlement Domain Package

```text
domain/
└── settlement/
    ├── Settlement
    ├── SettlementId
    └── SettlementValidator
```

Settlement business rules will be finalized according to the Settlement Model.

### 4.28 Synchronization Domain Package

Synchronization concepts that are part of the business/domain model may be represented separately.

Recommended structure:

```text
domain/
└── synchronization/
    ├── SyncOperation
    ├── SyncOperationId
    ├── SyncOperationType
    ├── SyncOperationStatus
    ├── SyncVersion
    └── Conflict
```

The final distinction between Domain synchronization models and infrastructure synchronization models will be finalized during the Synchronization Data Model design.

### 4.29 Domain Common Package

```text
domain/
└── common/
    ├── EntityId
    ├── DomainException
    └── ValidationResult
```

Only genuinely domain-wide abstractions should be placed here.

### 4.30 Data Package Structure

The `data` package contains infrastructure implementations.

Recommended structure:

```text
data/
├── local/
├── remote/
├── repository/
└── mapper/
```

### 4.31 Local Data Structure

```text
data/
└── local/
    ├── database/
    ├── dao/
    ├── entity/
    └── mapper/
```

### 4.32 Database Package

```text
data/
└── local/
    └── database/
        ├── SplitSyncDatabase
        └── migration/
```

Responsibilities include:

- Room database configuration.
- Entity registration.
- Database migrations.
- Database version management.

### 4.33 DAO Package

```text
data/
└── local/
    └── dao/
        ├── UserDao
        ├── DeviceDao
        ├── GroupDao
        ├── GroupMemberDao
        ├── ExpenseDao
        ├── ExpenseSplitDao
        ├── SettlementDao
        ├── SyncOperationDao
        ├── SyncStateDao
        └── ConflictDao
```

The final DAO list will follow the finalized database schema.

### 4.34 Entity Package

```text
data/
└── local/
    └── entity/
        ├── UserEntity
        ├── DeviceEntity
        ├── GroupEntity
        ├── GroupMemberEntity
        ├── ExpenseEntity
        ├── ExpenseSplitEntity
        ├── SettlementEntity
        ├── SyncOperationEntity
        ├── SyncStateEntity
        └── ConflictEntity
```

Entities represent persistence concerns and must not automatically become Domain objects.

### 4.35 Local Mapper Package

```text
data/
└── local/
    └── mapper/
        ├── UserEntityMapper
        ├── DeviceEntityMapper
        ├── GroupEntityMapper
        ├── ExpenseEntityMapper
        └── SettlementEntityMapper
```

Mappers convert between persistence models and Domain models.

### 4.36 Remote Package Structure

```text
data/
└── remote/
    ├── api/
    ├── dto/
    ├── interceptor/
    └── mapper/
```

### 4.37 Remote API Package

```text
data/
└── remote/
    └── api/
        ├── UserApi
        ├── DeviceApi
        ├── GroupApi
        ├── ExpenseApi
        ├── SettlementApi
        └── SyncApi
```

The exact API interface breakdown will follow the API Design section.

### 4.38 Remote DTO Package

```text
data/
└── remote/
    └── dto/
        ├── user/
        ├── device/
        ├── group/
        ├── expense/
        ├── settlement/
        └── synchronization/
```

DTOs represent API contracts.

They must not be treated as Domain entities.

### 4.39 Remote Interceptor Package

```text
data/
└── remote/
    └── interceptor/
        ├── AuthenticationInterceptor
        └── NetworkInterceptor
```

These components handle HTTP-level concerns.

They must not contain financial business logic.

### 4.40 Remote Mapper Package

```text
data/
└── remote/
    └── mapper/
        ├── UserDtoMapper
        ├── DeviceDtoMapper
        ├── GroupDtoMapper
        ├── ExpenseDtoMapper
        ├── SettlementDtoMapper
        └── SyncDtoMapper
```

### 4.41 Repository Package Structure

```text
data/
└── repository/
    ├── UserRepositoryImpl
    ├── DeviceRepositoryImpl
    ├── GroupRepositoryImpl
    ├── ExpenseRepositoryImpl
    ├── SettlementRepositoryImpl
    └── SynchronizationRepositoryImpl
```

Repository implementations coordinate appropriate data sources.

### 4.42 Sync Package Structure

The synchronization package is separated from normal data access because synchronization has specialized responsibilities.

Recommended structure:

```text
sync/
├── engine/
├── operation/
├── backend/
├── peer/
├── conflict/
├── state/
├── worker/
└── transport/
```

### 4.43 Sync Engine Package

```text
sync/
└── engine/
    ├── SyncEngine
    ├── SyncCoordinator
    └── SyncResultProcessor
```

The Sync Engine coordinates synchronization channels.

### 4.44 Sync Operation Package

```text
sync/
└── operation/
    ├── SyncOperationManager
    ├── SyncOperationProcessor
    └── SyncOperationFactory
```

These classes manage the lifecycle of synchronization operations.

### 4.45 Backend Sync Package

```text
sync/
└── backend/
    ├── BackendSyncManager
    ├── BackendPushProcessor
    ├── BackendPullProcessor
    └── BackendSyncResponseProcessor
```

Backend synchronization communicates through the REST API.

### 4.46 Peer Sync Package

```text
sync/
└── peer/
    ├── PeerDiscoveryManager
    ├── PeerConnectionManager
    ├── PeerAuthenticationManager
    ├── PeerSyncManager
    └── PeerMessageProcessor
```

These components isolate peer synchronization from the rest of the application.

### 4.47 Peer Transport Package

```text
sync/
└── transport/
    ├── PeerTransport
    ├── PeerConnection
    └── PeerMessage
```

The transport abstraction allows the underlying Android communication technology to be changed later.

### 4.48 Conflict Package

```text
sync/
└── conflict/
    ├── ConflictDetector
    ├── ConflictResolver
    └── ConflictResolutionResult
```

Conflict handling must remain deterministic.

### 4.49 Sync State Package

```text
sync/
└── state/
    ├── SyncStateManager
    └── SyncCursorManager
```

These components manage synchronization state without owning the underlying database implementation.

### 4.50 Worker Package

```text
sync/
└── worker/
    ├── BackendSyncWorker
    └── SyncRetryWorker
```

Workers trigger synchronization operations.

They should delegate actual work to the Sync Engine.

### 4.51 Security Package Structure

Recommended structure:

```text
security/
├── authentication/
├── credentials/
├── crypto/
├── device/
└── peer/
```

### 4.52 Authentication Package

```text
security/
└── authentication/
    ├── AuthenticationManager
    ├── SessionManager
    └── TokenManager
```

The exact classes depend on the final authentication design.

### 4.53 Credentials Package

```text
security/
└── credentials/
    └── SecureCredentialStore
```

Credentials must be stored using secure Android mechanisms.

### 4.54 Crypto Package

```text
security/
└── crypto/
    ├── CryptoManager
    ├── KeyManager
    └── SecureRandomProvider
```

The exact cryptographic implementation will be finalized during Security Architecture implementation.

### 4.55 Device Security Package

```text
security/
└── device/
    └── DeviceIdentityManager
```

This component manages application-level device identity.

It must not depend on hardware identifiers such as IMEI.

### 4.56 Peer Security Package

```text
security/
└── peer/
    ├── PeerIdentityVerifier
    ├── PeerSessionManager
    └── PeerTrustManager
```

These components support secure peer-to-peer synchronization.

### 4.57 Complete Package Structure

The resulting V1 package structure is:

```text
<com.company.splitsync>/
│
├── core/
│   ├── common/
│   ├── error/
│   ├── identity/
│   ├── money/
│   ├── time/
│   └── config/
│
├── presentation/
│   ├── common/
│   ├── navigation/
│   ├── user/
│   ├── group/
│   │   ├── list/
│   │   ├── create/
│   │   ├── detail/
│   │   ├── member/
│   │   └── invitation/
│   ├── expense/
│   │   ├── list/
│   │   ├── create/
│   │   ├── detail/
│   │   ├── edit/
│   │   └── split/
│   ├── balance/
│   ├── settlement/
│   │   ├── list/
│   │   ├── create/
│   │   ├── detail/
│   │   └── edit/
│   └── synchronization/
│
├── application/
│   ├── user/
│   ├── device/
│   ├── group/
│   ├── expense/
│   ├── balance/
│   ├── settlement/
│   └── synchronization/
│
├── domain/
│   ├── user/
│   ├── device/
│   ├── group/
│   ├── expense/
│   ├── settlement/
│   ├── synchronization/
│   └── common/
│
├── data/
│   ├── local/
│   │   ├── database/
│   │   ├── dao/
│   │   ├── entity/
│   │   └── mapper/
│   │
│   ├── remote/
│   │   ├── api/
│   │   ├── dto/
│   │   ├── interceptor/
│   │   └── mapper/
│   │
│   └── repository/
│
├── sync/
│   ├── engine/
│   ├── operation/
│   ├── backend/
│   ├── peer/
│   ├── conflict/
│   ├── state/
│   ├── worker/
│   └── transport/
│
└── security/
    ├── authentication/
    ├── credentials/
    ├── crypto/
    ├── device/
    └── peer/
```

### 4.58 Package Responsibility Summary

| Package | Responsibility |
|---|---|
| `core` | Shared technical abstractions |
| `presentation` | Android UI |
| `application` | Use-case orchestration |
| `domain` | Business rules and domain models |
| `data.local` | Room/SQLite |
| `data.remote` | REST API communication |
| `data.repository` | Repository implementations |
| `sync` | Synchronization infrastructure |
| `security` | Authentication and security infrastructure |

### 4.59 Package Dependency Rules

The following dependency direction must be maintained:

```text
presentation
      ↓
application
      ↓
domain
      ↓
abstractions
      ↓
data / infrastructure
```

Synchronization infrastructure may depend on appropriate Domain and Data abstractions.

The following direct dependencies are prohibited:

```text
presentation → DAO
presentation → Retrofit API
presentation → SQLite
domain → Room
domain → Retrofit
domain → OkHttp
domain → WorkManager
domain → Android Framework
```

### 4.60 Package Naming Rules

All packages must:

- Use lowercase names.
- Represent a meaningful responsibility.
- Avoid unnecessary abbreviations.
- Avoid generic dumping-ground packages.
- Keep infrastructure-specific code isolated.
- Keep Domain code infrastructure-independent.

Class names should use standard Java naming conventions.

### 4.61 Package Structure Invariants

The following rules are mandatory for V1:

- The Android application will initially use one Gradle application module.
- Package boundaries must reflect architectural boundaries.
- Presentation code must remain separate from persistence and networking.
- Application use cases must remain separate from infrastructure.
- Domain code must remain independent of Android and infrastructure.
- Room entities must remain separate from Domain models.
- API DTOs must remain separate from Domain models.
- Repository implementations must remain in the Data layer.
- Synchronization infrastructure must remain isolated from normal CRUD infrastructure.
- Peer transport implementation must remain replaceable.
- Security-sensitive code must remain isolated.
- Generic utility packages must not become dumping grounds.
- Package structure must support future Gradle-module extraction.

## 5. Backend Module Architecture

### 5.1 Purpose

This section defines the technical module architecture of the SplitSync V1 backend.

The backend will use:

- Java.
- Spring Boot.
- Hibernate/JPA.
- MySQL.
- REST APIs.

The backend will initially follow a modular monolith architecture.

The objective is to maintain clear separation between:

- API handling.
- Application use cases.
- Domain business rules.
- Persistence.
- Synchronization.
- Conflict resolution.
- Authentication.
- Authorization.
- Security.

The backend must remain simple enough for V1 while maintaining boundaries that allow future scaling or service extraction if required.

### 5.2 Backend Module Strategy

SplitSync V1 will initially use a single Spring Boot application.

The initial Gradle/Maven module structure should remain simple:

```text
backend/
└── splitsync-server/
```

Inside the application, clear package boundaries will represent the backend architecture.

Separate Spring Boot services or microservices are not required for V1.

### 5.3 Backend Architecture Overview

The backend structure is:

```text
                         SplitSync Backend
                                │
                         REST API Layer
                                │
                    ┌───────────┴───────────┐
                    │                       │
                    ▼                       ▼
             Authentication          Authorization
                    │                       │
                    └───────────┬───────────┘
                                │
                                ▼
                       Application Layer
                                │
                                ▼
                          Domain Layer
                                │
              ┌─────────────────┼─────────────────┐
              │                 │                 │
              ▼                 ▼                 ▼
       Synchronization    Conflict Resolution   Other
              │                 │
              └─────────────────┼─────────────────┘
                                │
                                ▼
                       Persistence Layer
                                │
                                ▼
                         Hibernate / JPA
                                │
                                ▼
                              MySQL
```

### 5.4 Backend Package Structure

The initial backend package structure is:

```text
<com.company.splitsync>/
│
├── api/
├── application/
├── domain/
├── persistence/
├── synchronization/
├── security/
├── configuration/
└── common/
```

The exact Java base package will be finalized during backend project initialization.

### 5.5 API Module

The `api` package exposes the backend REST API.

Responsibilities include:

- HTTP request handling.
- HTTP response handling.
- Request DTO validation.
- Authentication integration.
- Authorization integration.
- Calling Application services.
- Mapping Application results to API responses.
- Standardized error responses.

The API layer must not contain core business rules.

### 5.6 API Package Structure

Recommended structure:

```text
api/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
├── synchronization/
├── authentication/
├── error/
└── common/
```

Each API area should contain only HTTP-facing components.

### 5.7 User API Package

```text
api/
└── user/
    ├── UserController
    ├── UserRequest
    └── UserResponse
```

Responsibilities include:

- User registration where applicable.
- User profile retrieval.
- User profile updates.
- User-related API operations.

The exact endpoints will be defined in the API Design section.

### 5.8 Device API Package

```text
api/
└── device/
    ├── DeviceController
    ├── DeviceRegistrationRequest
    └── DeviceResponse
```

Responsibilities include:

- Device registration.
- Device information.
- Device synchronization state where applicable.

### 5.9 Group API Package

```text
api/
└── group/
    ├── GroupController
    ├── GroupRequest
    ├── GroupResponse
    └── GroupMemberResponse
```

Responsibilities include:

- Group operations.
- Group membership operations.
- Group information.

### 5.10 Expense API Package

```text
api/
└── expense/
    ├── ExpenseController
    ├── ExpenseRequest
    ├── ExpenseResponse
    └── ExpenseSplitResponse
```

Responsibilities include:

- Expense operations.
- Expense Split data exchange.
- Expense retrieval.

The API layer does not calculate balances independently.

### 5.11 Settlement API Package

```text
api/
└── settlement/
    ├── SettlementController
    ├── SettlementRequest
    └── SettlementResponse
```

Responsibilities include:

- Settlement creation.
- Settlement retrieval.
- Settlement updates.
- Settlement deletion where supported.

### 5.12 Synchronization API Package

Synchronization is a dedicated API area.

Recommended structure:

```text
api/
└── synchronization/
    ├── SyncController
    ├── SyncPushRequest
    ├── SyncPushResponse
    ├── SyncPullRequest
    └── SyncPullResponse
```

The exact request/response models will be finalized in the API Design and Synchronization Design sections.

### 5.13 Authentication API Package

```text
api/
└── authentication/
    ├── AuthenticationController
    ├── LoginRequest
    ├── LoginResponse
    └── RefreshTokenRequest
```

The exact authentication mechanism will be finalized separately.

### 5.14 Application Module

The `application` package contains backend use-case orchestration.

Responsibilities include:

- Coordinating Domain logic.
- Coordinating persistence.
- Managing transaction boundaries.
- Executing application use cases.
- Calling synchronization services.
- Returning application-level results.

The Application Layer must not contain HTTP-specific logic.

### 5.15 Application Package Structure

```text
application/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
├── synchronization/
└── authentication/
```

### 5.16 User Application Package

```text
application/
└── user/
    ├── CreateUserService
    ├── GetUserService
    └── UpdateUserService
```

These services coordinate user-related use cases.

### 5.17 Device Application Package

```text
application/
└── device/
    ├── RegisterDeviceService
    ├── GetDeviceService
    └── RevokeDeviceService
```

Device lifecycle operations remain separate from User operations.

### 5.18 Group Application Package

```text
application/
└── group/
    ├── CreateGroupService
    ├── GetGroupService
    ├── UpdateGroupService
    ├── AddGroupMemberService
    ├── RemoveGroupMemberService
    └── GetGroupMembersService
```

These services coordinate Group-related operations.

### 5.19 Expense Application Package

```text
application/
└── expense/
    ├── CreateExpenseService
    ├── GetExpenseService
    ├── UpdateExpenseService
    └── DeleteExpenseService
```

Expense services coordinate Domain validation and persistence.

### 5.20 Settlement Application Package

```text
application/
└── settlement/
    ├── CreateSettlementService
    ├── GetSettlementService
    ├── UpdateSettlementService
    └── DeleteSettlementService
```

### 5.21 Synchronization Application Package

```text
application/
└── synchronization/
    ├── PushChangesService
    ├── PullChangesService
    ├── ProcessSyncOperationService
    └── GetSyncStateService
```

These services coordinate synchronization use cases.

Synchronization logic must remain separate from ordinary CRUD services.

### 5.22 Authentication Application Package

```text
application/
└── authentication/
    ├── AuthenticateUserService
    ├── RefreshAuthenticationService
    └── LogoutService
```

The exact services depend on the final authentication design.

### 5.23 Domain Module

The `domain` package contains backend business concepts and rules.

The Domain Layer must remain independent from:

- Spring MVC.
- Spring Security.
- Hibernate.
- JPA.
- MySQL.
- HTTP.
- REST DTOs.

### 5.24 Domain Package Structure

```text
domain/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
├── synchronization/
└── common/
```

### 5.25 User Domain Package

```text
domain/
└── user/
    ├── User
    ├── UserId
    ├── DisplayName
    ├── PhoneNumber
    ├── EmailAddress
    └── UserValidator
```

The final User model will follow the decisions defined in the Architecture and Domain Model sections.

### 5.26 Device Domain Package

```text
domain/
└── device/
    ├── Device
    ├── DeviceId
    ├── DeviceStatus
    └── DeviceValidator
```

A Device represents an application installation/device identity and is distinct from a User.

### 5.27 Group Domain Package

```text
domain/
└── group/
    ├── Group
    ├── GroupId
    ├── GroupMember
    ├── GroupMemberId
    ├── GroupRole
    └── GroupValidator
```

Group membership and role rules belong to the Domain Layer.

### 5.28 Expense Domain Package

```text
domain/
└── expense/
    ├── Expense
    ├── ExpenseId
    ├── ExpenseSplit
    ├── ExpenseSplitId
    ├── SplitMethod
    ├── ExpenseValidator
    └── ExpenseCalculator
```

Financial calculations must remain deterministic.

### 5.29 Settlement Domain Package

```text
domain/
└── settlement/
    ├── Settlement
    ├── SettlementId
    └── SettlementValidator
```

Settlement rules belong to the Domain Layer.

### 5.30 Synchronization Domain Package

```text
domain/
└── synchronization/
    ├── SyncOperation
    ├── SyncOperationId
    ├── SyncOperationType
    ├── SyncOperationStatus
    ├── SyncVersion
    └── Conflict
```

The final synchronization model will be defined separately.

### 5.31 Persistence Module

The `persistence` package contains infrastructure-specific database implementation.

Responsibilities include:

- JPA entities.
- Repositories.
- Database queries.
- Persistence mapping.
- Transactions.
- Database configuration.

The Persistence Layer must not contain API logic.

### 5.32 Persistence Package Structure

```text
persistence/
├── entity/
├── repository/
├── mapper/
└── specification/
```

### 5.33 Persistence Entity Package

Recommended entities include:

```text
persistence/
└── entity/
    ├── UserEntity
    ├── DeviceEntity
    ├── GroupEntity
    ├── GroupMemberEntity
    ├── ExpenseEntity
    ├── ExpenseSplitEntity
    ├── SettlementEntity
    ├── SyncOperationEntity
    ├── SyncStateEntity
    └── ConflictEntity
```

The final entity list will follow the Database Design.

### 5.34 Persistence Repository Package

Recommended repository structure:

```text
persistence/
└── repository/
    ├── UserJpaRepository
    ├── DeviceJpaRepository
    ├── GroupJpaRepository
    ├── GroupMemberJpaRepository
    ├── ExpenseJpaRepository
    ├── ExpenseSplitJpaRepository
    ├── SettlementJpaRepository
    ├── SyncOperationJpaRepository
    ├── SyncStateJpaRepository
    └── ConflictJpaRepository
```

Repositories should focus on persistence operations.

Business decisions must remain in the Domain/Application layers.

### 5.35 Persistence Mapper Package

```text
persistence/
└── mapper/
    ├── UserPersistenceMapper
    ├── DevicePersistenceMapper
    ├── GroupPersistenceMapper
    ├── ExpensePersistenceMapper
    ├── SettlementPersistenceMapper
    └── SyncPersistenceMapper
```

Persistence mappers convert between persistence entities and Domain models where required.

### 5.36 Synchronization Module

The `synchronization` package contains server-side synchronization infrastructure.

Responsibilities include:

- Processing synchronization requests.
- Operation validation.
- Idempotency.
- Version checking.
- Change detection.
- Conflict detection.
- Conflict resolution.
- Change retrieval.
- Synchronization cursor management.

### 5.37 Synchronization Package Structure

```text
synchronization/
├── engine/
├── operation/
├── conflict/
├── cursor/
└── validation/
```

### 5.38 Synchronization Engine

```text
synchronization/
└── engine/
    ├── SynchronizationEngine
    ├── PushProcessor
    ├── PullProcessor
    └── SynchronizationResultProcessor
```

The engine coordinates synchronization without containing transport-specific HTTP handling.

### 5.39 Synchronization Operation Package

```text
synchronization/
└── operation/
    ├── SyncOperationProcessor
    ├── SyncOperationValidator
    └── SyncOperationIdempotencyService
```

Responsibilities include:

- Processing incoming operations.
- Validating operations.
- Detecting duplicate operations.
- Applying operations transactionally.

### 5.40 Conflict Package

```text
synchronization/
└── conflict/
    ├── ConflictDetector
    ├── ConflictResolver
    └── ConflictResolutionResult
```

Conflict resolution must remain deterministic and entity-aware.

### 5.41 Cursor Package

```text
synchronization/
└── cursor/
    ├── SyncCursorManager
    └── ChangeCursor
```

The cursor system tracks which server-side changes have been delivered to a client.

The exact cursor strategy will be finalized in the Synchronization Design.

### 5.42 Synchronization Validation Package

```text
synchronization/
└── validation/
    ├── SyncAuthorizationValidator
    ├── SyncVersionValidator
    └── SyncPayloadValidator
```

All incoming synchronization data must be treated as untrusted input.

### 5.43 Security Module

The `security` package contains backend security infrastructure.

Responsibilities include:

- Authentication.
- Authorization.
- Token validation.
- Password/credential handling where applicable.
- Security filters.
- Device authentication.
- Security context management.

### 5.44 Security Package Structure

```text
security/
├── authentication/
├── authorization/
├── token/
├── device/
└── configuration/
```

### 5.45 Authentication Package

```text
security/
└── authentication/
    ├── AuthenticationService
    ├── AuthenticationProvider
    └── AuthenticationContext
```

The exact implementation depends on the final authentication mechanism.

### 5.46 Authorization Package

```text
security/
└── authorization/
    ├── AuthorizationService
    ├── GroupPermissionEvaluator
    └── ResourceAccessEvaluator
```

Authorization must be based on server-side information.

### 5.47 Token Package

```text
security/
└── token/
    ├── TokenService
    ├── TokenValidator
    └── RefreshTokenService
```

The exact token model will be finalized later.

### 5.48 Device Security Package

```text
security/
└── device/
    ├── DeviceAuthenticationService
    └── DeviceTrustService
```

This component handles backend-side device identity and trust where applicable.

### 5.49 Configuration Module

The `configuration` package contains Spring Boot configuration.

Recommended structure:

```text
configuration/
├── DatabaseConfiguration
├── SecurityConfiguration
├── WebConfiguration
├── JacksonConfiguration
└── ApplicationConfiguration
```

Configuration classes must configure infrastructure rather than contain business logic.

### 5.50 Common Module

The `common` package contains genuinely shared backend infrastructure.

Recommended structure:

```text
common/
├── error/
├── validation/
├── time/
└── id/
```

Examples include:

```text
error/
    Common exception abstractions

validation/
    Shared technical validation abstractions

time/
    Clock/time abstractions

id/
    Identifier generation abstractions
```

The `common` package must remain small and focused.

### 5.51 Exception Handling

API errors should be handled centrally.

Recommended structure:

```text
api/
└── error/
    ├── GlobalExceptionHandler
    ├── ApiErrorResponse
    └── ErrorCode
```

The API layer converts internal exceptions into stable API responses.

Internal implementation details must not be exposed to clients.

### 5.52 Transaction Management

Application services should define transaction boundaries for operations that require atomic persistence.

For example:

```text
Create Expense
      ↓
Validate Expense
      ↓
Persist Expense
      ↓
Persist Expense Splits
      ↓
Persist Synchronization Metadata
      ↓
Commit
```

If any required operation fails:

```text
Rollback
```

The exact transaction boundaries will be finalized with the Database Design.

### 5.53 Backend Dependency Direction

The intended backend dependency direction is:

```text
API
 ↓
Application
 ↓
Domain
 ↓
Persistence Abstractions
 ↓
Persistence Implementation
 ↓
Hibernate/JPA
 ↓
MySQL
```

Security and synchronization infrastructure integrate at clearly defined boundaries.

### 5.54 Forbidden Backend Dependencies

The following dependencies must be avoided:

```text
Domain → Spring MVC
Domain → Spring Security
Domain → JPA
Domain → Hibernate
Domain → MySQL
Domain → HTTP
Domain → REST DTO
Persistence → API Controller
API Controller → MySQL
API Controller → JPA Repository directly
```

Controllers should invoke Application services rather than directly manipulating persistence.

### 5.55 REST DTO Isolation

REST request and response objects must remain separate from Domain models.

The intended flow is:

```text
HTTP Request
     ↓
Request DTO
     ↓
API Controller
     ↓
Application Service
     ↓
Domain Model
     ↓
Persistence Model
```

And for responses:

```text
Persistence
     ↓
Domain
     ↓
Application Result
     ↓
Response DTO
     ↓
HTTP Response
```

This prevents API contracts from leaking into the Domain Layer.

### 5.56 Backend Module Responsibilities

| Module | Responsibility |
|---|---|
| `api` | REST API and HTTP handling |
| `application` | Use-case orchestration |
| `domain` | Business rules and domain models |
| `persistence` | Hibernate/JPA and database access |
| `synchronization` | Server-side synchronization |
| `security` | Authentication and authorization |
| `configuration` | Spring Boot configuration |
| `common` | Shared backend infrastructure |

### 5.57 Backend V1 Architecture Style

The backend will use a modular monolith architecture.

This means:

- One deployable Spring Boot application.
- One primary MySQL database.
- Clearly separated internal modules.
- No requirement for microservices.
- No network communication between internal modules.
- Shared Domain concepts within the application.
- Clear boundaries that allow future extraction if necessary.

### 5.58 Why Modular Monolith for V1

A modular monolith is preferred for V1 because:

- The application domain is still evolving.
- The initial user and Group scale is expected to be manageable.
- Deployment is simpler.
- Debugging is easier.
- Transactions are easier to manage.
- Development overhead is lower.
- Synchronization logic can remain centralized.
- Future service extraction remains possible if boundaries are maintained.

Microservices should not be introduced solely for architectural complexity.

### 5.59 Future Backend Extraction

If future scale requires service separation, potential boundaries could include:

```text
User Service
Group Service
Expense Service
Settlement Service
Synchronization Service
Authentication Service
```

However, these are future possibilities and are not part of the V1 deployment architecture.

### 5.60 Testing Structure

Backend tests should mirror the module boundaries.

Recommended structure:

```text
src/
├── test/
│   └── java/
│       └── <base-package>/
│           ├── domain/
│           ├── application/
│           ├── synchronization/
│           ├── security/
│           └── persistence/
│
└── integrationTest/
    └── java/
        └── <base-package>/
            ├── api/
            ├── synchronization/
            └── persistence/
```

The exact test-source configuration may vary depending on the build tool.

### 5.61 Domain Testing

Domain tests should cover:

- Expense validation.
- Expense Split validation.
- Balance calculation.
- Settlement validation.
- Group rules.
- Membership rules.
- Financial invariants.
- Conflict rules.

These tests should not require Spring Boot or MySQL.

### 5.62 Application Testing

Application tests should verify:

- Use-case orchestration.
- Transaction boundaries.
- Repository interactions.
- Authorization decisions.
- Synchronization use cases.

Infrastructure dependencies should be mocked or replaced with controlled test implementations where appropriate.

### 5.63 Persistence Testing

Persistence tests should verify:

- JPA mappings.
- Relationships.
- Constraints.
- Queries.
- Transactions.
- Database migrations.
- Synchronization persistence.

Tests should use a database environment sufficiently close to production MySQL behavior.

### 5.64 API Testing

API tests should verify:

- HTTP status codes.
- Request validation.
- Authentication.
- Authorization.
- Response structure.
- Error responses.
- API versioning.
- Synchronization endpoints.

### 5.65 Synchronization Testing

Synchronization tests should verify:

- Idempotency.
- Duplicate operation handling.
- Version checks.
- Change retrieval.
- Conflict detection.
- Conflict resolution.
- Transactional application of changes.
- Retry-safe processing.

### 5.66 Backend Configuration by Environment

The backend must support separate environments.

At minimum:

```text
development
test
production
```

Environment-specific configuration must include:

- Database connection.
- API configuration.
- Security configuration.
- Logging configuration.
- External service configuration where applicable.

Secrets must never be committed to Git.

### 5.67 Database Migration

Production database schema changes must use a dedicated migration mechanism.

The final migration technology will be selected later.

Potential technology:

```text
Flyway
```

or:

```text
Liquibase
```

Hibernate schema generation must not be relied upon as the sole production migration strategy.

### 5.68 Logging

Backend logging must be centralized and environment-aware.

Development may use detailed logging.

Production logs must avoid:

- Passwords.
- Authentication tokens.
- Private keys.
- Sensitive profile information.
- Unnecessary financial information.

Synchronization logs should include sufficient metadata to diagnose failures without exposing sensitive data.

### 5.69 Monitoring Readiness

The backend architecture should allow future monitoring of:

- API latency.
- Error rates.
- Synchronization failures.
- Database performance.
- Authentication failures.
- Active synchronization operations.

Monitoring implementation may be introduced incrementally.

### 5.70 Backend Architecture Invariants

The following rules are mandatory for V1:

- The backend will initially be a modular monolith.
- The backend will use Java and Spring Boot.
- Hibernate/JPA will be used for persistence.
- MySQL will be the primary backend database.
- REST APIs will be versioned.
- Controllers must not contain business logic.
- Application services must coordinate use cases.
- Domain logic must remain independent of Spring and JPA.
- Persistence entities must remain separate from API DTOs.
- Persistence implementation must remain isolated from API handling.
- Synchronization must be a dedicated backend subsystem.
- Conflict resolution must be deterministic.
- Incoming synchronization operations must be authenticated and authorized.
- Synchronization operations must be idempotent.
- Financial operations must preserve transactional consistency.
- Database schema changes must use controlled migrations.
- Secrets must not be stored in source control.
- V1 will not require microservices.
- Internal module boundaries must be maintained so future service extraction remains possible.

## 6. Backend Package Structure

### 6.1 Purpose

This section defines the detailed Java package structure of the SplitSync V1 backend.

The package structure follows the Backend Module Architecture defined in Section 5.

The primary goals are:

- Clear separation of responsibilities.
- Strong boundaries between API, Application, Domain, and Persistence.
- Independent and testable business logic.
- Isolation of synchronization infrastructure.
- Isolation of security infrastructure.
- Clear package ownership.
- Maintainability as the application grows.
- Future support for extracting modules into separate services if required.

### 6.2 Backend Base Package

The backend will use a single root package:

```text
<com.company.splitsync>
```

The actual package name will be finalized during Spring Boot project creation.

All backend packages will exist below this root package.

### 6.3 Root Package Structure

The initial backend package structure is:

```text
<com.company.splitsync>/
│
├── api/
├── application/
├── domain/
├── persistence/
├── synchronization/
├── security/
├── configuration/
└── common/
```

Each top-level package represents a major architectural responsibility.

### 6.4 API Package Structure

The `api` package contains all HTTP and REST-facing components.

```text
api/
├── authentication/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
├── synchronization/
├── error/
└── common/
```

The API package must contain HTTP-specific concerns only.

### 6.5 Authentication API Package

```text
api/
└── authentication/
    ├── AuthenticationController
    ├── LoginRequest
    ├── LoginResponse
    ├── RefreshTokenRequest
    └── RefreshTokenResponse
```

Responsibilities:

- Login.
- Authentication-related requests.
- Token refresh.
- Authentication responses.

The exact classes will depend on the final authentication mechanism.

### 6.6 User API Package

```text
api/
└── user/
    ├── UserController
    ├── UserResponse
    ├── UpdateUserRequest
    └── UserMapper
```

Responsibilities:

- User profile operations.
- User retrieval.
- User profile updates.

The User API must not directly access JPA repositories.

### 6.7 Device API Package

```text
api/
└── device/
    ├── DeviceController
    ├── DeviceRegistrationRequest
    ├── DeviceResponse
    └── DeviceMapper
```

Responsibilities:

- Device registration.
- Device information.
- Device lifecycle operations where applicable.

### 6.8 Group API Package

```text
api/
└── group/
    ├── GroupController
    ├── CreateGroupRequest
    ├── UpdateGroupRequest
    ├── GroupResponse
    ├── GroupMemberResponse
    └── GroupMapper
```

Responsibilities:

- Group creation.
- Group retrieval.
- Group updates.
- Group membership operations.

### 6.9 Expense API Package

```text
api/
└── expense/
    ├── ExpenseController
    ├── CreateExpenseRequest
    ├── UpdateExpenseRequest
    ├── ExpenseResponse
    ├── ExpenseSplitResponse
    └── ExpenseMapper
```

Responsibilities:

- Expense creation.
- Expense retrieval.
- Expense updates.
- Expense deletion where supported.

The API layer must not independently calculate financial balances.

### 6.10 Settlement API Package

```text
api/
└── settlement/
    ├── SettlementController
    ├── CreateSettlementRequest
    ├── UpdateSettlementRequest
    ├── SettlementResponse
    └── SettlementMapper
```

Responsibilities:

- Settlement creation.
- Settlement retrieval.
- Settlement updates.
- Settlement deletion where supported.

### 6.11 Synchronization API Package

```text
api/
└── synchronization/
    ├── SyncController
    ├── SyncPushRequest
    ├── SyncPushResponse
    ├── SyncPullRequest
    ├── SyncPullResponse
    └── SyncMapper
```

Responsibilities:

- Push local synchronization operations.
- Pull server-side changes.
- Return synchronization results.
- Return conflicts.
- Return synchronization metadata.

The exact API contract will be finalized in the API Design section.

### 6.12 API Error Package

```text
api/
└── error/
    ├── GlobalExceptionHandler
    ├── ApiErrorResponse
    ├── ApiErrorCode
    └── ValidationErrorResponse
```

Responsibilities:

- Convert application exceptions into API responses.
- Provide consistent error structures.
- Handle validation errors.
- Prevent internal implementation details from leaking to clients.

### 6.13 API Common Package

```text
api/
└── common/
    ├── ApiResponse
    └── PaginationResponse
```

Only genuinely API-wide abstractions should be placed here.

Generic utility classes should not be added without a clear responsibility.

### 6.14 Application Package Structure

The `application` package contains use-case orchestration.

```text
application/
├── authentication/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
└── synchronization/
```

Application classes coordinate Domain logic and Persistence abstractions.

They must not depend on HTTP-specific classes.

### 6.15 Authentication Application Package

```text
application/
└── authentication/
    ├── AuthenticateUserService
    ├── RefreshAuthenticationService
    └── LogoutService
```

Responsibilities:

- Authentication use cases.
- Session/token lifecycle.
- Authentication-related application logic.

### 6.16 User Application Package

```text
application/
└── user/
    ├── CreateUserService
    ├── GetUserService
    └── UpdateUserService
```

Responsibilities:

- User creation where applicable.
- User retrieval.
- User profile updates.

### 6.17 Device Application Package

```text
application/
└── device/
    ├── RegisterDeviceService
    ├── GetDeviceService
    └── RevokeDeviceService
```

Responsibilities:

- Device registration.
- Device retrieval.
- Device revocation.

### 6.18 Group Application Package

```text
application/
└── group/
    ├── CreateGroupService
    ├── GetGroupService
    ├── GetGroupsService
    ├── UpdateGroupService
    ├── AddGroupMemberService
    ├── RemoveGroupMemberService
    └── GetGroupMembersService
```

Responsibilities:

- Group lifecycle.
- Group membership.
- Group retrieval.

### 6.19 Expense Application Package

```text
application/
└── expense/
    ├── CreateExpenseService
    ├── GetExpenseService
    ├── GetExpensesService
    ├── UpdateExpenseService
    └── DeleteExpenseService
```

Responsibilities:

- Expense lifecycle.
- Expense retrieval.
- Expense modification.

### 6.20 Settlement Application Package

```text
application/
└── settlement/
    ├── CreateSettlementService
    ├── GetSettlementService
    ├── GetSettlementsService
    ├── UpdateSettlementService
    └── DeleteSettlementService
```

Responsibilities:

- Settlement lifecycle.
- Settlement retrieval.
- Settlement modification.

### 6.21 Synchronization Application Package

```text
application/
└── synchronization/
    ├── PushChangesService
    ├── PullChangesService
    ├── ProcessSyncOperationService
    └── GetSyncStateService
```

Responsibilities:

- Synchronization use cases.
- Coordinating synchronization processing.
- Returning synchronization results.

Transport-specific synchronization logic must remain outside the Application Layer.

### 6.22 Domain Package Structure

The `domain` package contains the backend business model and rules.

```text
domain/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
├── synchronization/
└── common/
```

The Domain Layer must not depend on:

```text
Spring MVC
Spring Security
Spring Data JPA
Hibernate
MySQL
REST
HTTP
API DTOs
```

### 6.23 User Domain Package

```text
domain/
└── user/
    ├── User
    ├── UserId
    ├── DisplayName
    ├── PhoneNumber
    ├── EmailAddress
    └── UserValidator
```

Only finalized V1 concepts should be introduced.

### 6.24 Device Domain Package

```text
domain/
└── device/
    ├── Device
    ├── DeviceId
    ├── DeviceStatus
    └── DeviceValidator
```

A Device represents an application installation/device identity and is separate from the User identity.

### 6.25 Group Domain Package

```text
domain/
└── group/
    ├── Group
    ├── GroupId
    ├── GroupMember
    ├── GroupMemberId
    ├── GroupRole
    └── GroupValidator
```

Group membership and authorization-related business rules belong here where they are domain rules.

### 6.26 Expense Domain Package

```text
domain/
└── expense/
    ├── Expense
    ├── ExpenseId
    ├── ExpenseSplit
    ├── ExpenseSplitId
    ├── SplitMethod
    ├── ExpenseValidator
    └── ExpenseCalculator
```

Financial rules must remain deterministic and independent of persistence.

### 6.27 Settlement Domain Package

```text
domain/
└── settlement/
    ├── Settlement
    ├── SettlementId
    └── SettlementValidator
```

Settlement rules belong to the Domain Layer.

### 6.28 Synchronization Domain Package

```text
domain/
└── synchronization/
    ├── SyncOperation
    ├── SyncOperationId
    ├── SyncOperationType
    ├── SyncOperationStatus
    ├── SyncVersion
    └── Conflict
```

The exact synchronization model will be finalized in the Synchronization Data Model section.

### 6.29 Domain Common Package

```text
domain/
└── common/
    ├── EntityId
    ├── DomainException
    └── ValidationResult
```

Only genuinely domain-wide abstractions should be placed here.

### 6.30 Persistence Package Structure

The `persistence` package contains Hibernate/JPA and database-specific implementations.

```text
persistence/
├── entity/
├── repository/
├── mapper/
└── specification/
```

### 6.31 Persistence Entity Package

Recommended structure:

```text
persistence/
└── entity/
    ├── UserEntity
    ├── DeviceEntity
    ├── GroupEntity
    ├── GroupMemberEntity
    ├── ExpenseEntity
    ├── ExpenseSplitEntity
    ├── SettlementEntity
    ├── SyncOperationEntity
    ├── SyncStateEntity
    └── ConflictEntity
```

The final entity structure will be defined in the Database Design section.

### 6.32 Persistence Repository Package

```text
persistence/
└── repository/
    ├── UserJpaRepository
    ├── DeviceJpaRepository
    ├── GroupJpaRepository
    ├── GroupMemberJpaRepository
    ├── ExpenseJpaRepository
    ├── ExpenseSplitJpaRepository
    ├── SettlementJpaRepository
    ├── SyncOperationJpaRepository
    ├── SyncStateJpaRepository
    └── ConflictJpaRepository
```

These repositories are responsible for persistence access.

They must not contain business rules.

### 6.33 Persistence Mapper Package

```text
persistence/
└── mapper/
    ├── UserPersistenceMapper
    ├── DevicePersistenceMapper
    ├── GroupPersistenceMapper
    ├── ExpensePersistenceMapper
    ├── SettlementPersistenceMapper
    └── SyncPersistenceMapper
```

Persistence mappers translate between persistence models and Domain models where required.

### 6.34 Persistence Specification Package

The `specification` package contains reusable persistence query specifications where required.

```text
persistence/
└── specification/
    ├── GroupSpecification
    ├── ExpenseSpecification
    └── SyncOperationSpecification
```

Specifications must remain persistence-focused.

They must not contain business workflows.

### 6.35 Synchronization Package Structure

The `synchronization` package contains server-side synchronization infrastructure.

```text
synchronization/
├── engine/
├── operation/
├── conflict/
├── cursor/
└── validation/
```

### 6.36 Synchronization Engine Package

```text
synchronization/
└── engine/
    ├── SynchronizationEngine
    ├── PushProcessor
    ├── PullProcessor
    └── SynchronizationResultProcessor
```

Responsibilities:

- Coordinate synchronization processing.
- Process push requests.
- Process pull requests.
- Build synchronization results.

### 6.37 Synchronization Operation Package

```text
synchronization/
└── operation/
    ├── SyncOperationProcessor
    ├── SyncOperationValidator
    └── SyncOperationIdempotencyService
```

Responsibilities:

- Validate operations.
- Process operations.
- Detect duplicate operations.
- Apply operations transactionally.

### 6.38 Synchronization Conflict Package

```text
synchronization/
└── conflict/
    ├── ConflictDetector
    ├── ConflictResolver
    └── ConflictResolutionResult
```

Conflict resolution must be deterministic and entity-aware.

### 6.39 Synchronization Cursor Package

```text
synchronization/
└── cursor/
    ├── SyncCursorManager
    └── ChangeCursor
```

Responsibilities:

- Track synchronization progress.
- Identify changes that need to be delivered.
- Maintain synchronization cursors.

The exact cursor mechanism will be defined later.

### 6.40 Synchronization Validation Package

```text
synchronization/
└── validation/
    ├── SyncPayloadValidator
    ├── SyncAuthorizationValidator
    └── SyncVersionValidator
```

All synchronization input must be treated as untrusted input.

Validation must occur before persistence.

### 6.41 Security Package Structure

The `security` package contains backend security infrastructure.

```text
security/
├── authentication/
├── authorization/
├── token/
├── device/
└── configuration/
```

### 6.42 Authentication Security Package

```text
security/
└── authentication/
    ├── AuthenticationService
    ├── AuthenticationProvider
    └── AuthenticationContext
```

Responsibilities:

- Establish authenticated identity.
- Validate authentication credentials.
- Build authentication context.

The exact implementation will depend on the final authentication mechanism.

### 6.43 Authorization Security Package

```text
security/
└── authorization/
    ├── AuthorizationService
    ├── GroupPermissionEvaluator
    └── ResourceAccessEvaluator
```

Responsibilities:

- Evaluate access permissions.
- Validate Group membership.
- Validate Group roles.
- Validate resource ownership.

Authorization decisions must be based on trusted server-side information.

### 6.44 Token Security Package

```text
security/
└── token/
    ├── TokenService
    ├── TokenValidator
    └── RefreshTokenService
```

The exact implementation depends on the selected authentication/token strategy.

### 6.45 Device Security Package

```text
security/
└── device/
    ├── DeviceAuthenticationService
    └── DeviceTrustService
```

Responsibilities:

- Validate registered devices.
- Establish device trust.
- Handle device revocation where applicable.

### 6.46 Security Configuration Package

```text
security/
└── configuration/
    ├── SecurityFilterConfiguration
    └── SecurityProperties
```

Security configuration should remain separate from Domain logic.

### 6.47 Configuration Package Structure

The `configuration` package contains Spring Boot infrastructure configuration.

```text
configuration/
├── DatabaseConfiguration
├── SecurityConfiguration
├── WebConfiguration
├── JacksonConfiguration
├── TransactionConfiguration
└── ApplicationConfiguration
```

Configuration classes should configure infrastructure and framework behavior.

They must not implement business workflows.

### 6.48 Common Package Structure

The `common` package contains genuinely shared backend infrastructure.

```text
common/
├── error/
├── validation/
├── time/
└── id/
```

### 6.49 Common Error Package

```text
common/
└── error/
    ├── ApplicationException
    ├── DomainException
    └── ErrorCode
```

Exceptions should have clear ownership.

Domain exceptions should originate from the Domain Layer.

Infrastructure exceptions should remain infrastructure-specific until translated into an appropriate application/API result.

### 6.50 Common Validation Package

```text
common/
└── validation/
    └── ValidationResult
```

Only generic validation abstractions should be placed here.

Business-specific validation belongs to the Domain Layer.

### 6.51 Common Time Package

```text
common/
└── time/
    └── ClockProvider
```

A controllable clock abstraction may be used to make time-dependent business logic testable.

### 6.52 Common ID Package

```text
common/
└── id/
    └── IdGenerator
```

The exact identifier-generation strategy will be finalized in the Domain and Database Design sections.

### 6.53 Complete Backend Package Structure

The resulting V1 backend package structure is:

```text
<com.company.splitsync>/
│
├── api/
│   ├── authentication/
│   ├── user/
│   ├── device/
│   ├── group/
│   ├── expense/
│   ├── settlement/
│   ├── synchronization/
│   ├── error/
│   └── common/
│
├── application/
│   ├── authentication/
│   ├── user/
│   ├── device/
│   ├── group/
│   ├── expense/
│   ├── settlement/
│   └── synchronization/
│
├── domain/
│   ├── user/
│   ├── device/
│   ├── group/
│   ├── expense/
│   ├── settlement/
│   ├── synchronization/
│   └── common/
│
├── persistence/
│   ├── entity/
│   ├── repository/
│   ├── mapper/
│   └── specification/
│
├── synchronization/
│   ├── engine/
│   ├── operation/
│   ├── conflict/
│   ├── cursor/
│   └── validation/
│
├── security/
│   ├── authentication/
│   ├── authorization/
│   ├── token/
│   ├── device/
│   └── configuration/
│
├── configuration/
│
└── common/
    ├── error/
    ├── validation/
    ├── time/
    └── id/
```

### 6.54 Package Responsibility Summary

| Package | Responsibility |
|---|---|
| `api` | REST/HTTP layer |
| `application` | Use-case orchestration |
| `domain` | Business rules and domain models |
| `persistence` | Hibernate/JPA and MySQL access |
| `synchronization` | Server-side synchronization |
| `security` | Authentication and authorization |
| `configuration` | Spring Boot configuration |
| `common` | Shared backend infrastructure |

### 6.55 API Dependency Rules

The API layer should follow:

```text
Controller
    ↓
Application Service
    ↓
Domain
    ↓
Persistence Abstraction
    ↓
Persistence Implementation
```

Controllers must not directly manipulate persistence entities.

### 6.56 Domain Dependency Rules

The Domain Layer must remain independent of infrastructure.

The following dependencies are prohibited:

```text
Domain → Spring MVC
Domain → Spring Security
Domain → JPA
Domain → Hibernate
Domain → MySQL
Domain → REST DTO
Domain → HTTP
```

### 6.57 Persistence Dependency Rules

Persistence may depend on:

```text
Domain
Spring Data JPA
Hibernate
MySQL
```

Persistence must not depend on:

```text
API Controllers
REST Request DTOs
REST Response DTOs
```

### 6.58 Synchronization Dependency Rules

The Synchronization Layer may depend on:

```text
Domain
Application abstractions
Persistence abstractions
```

It must not embed:

- REST controller logic.
- Authentication UI logic.
- Android-specific code.

### 6.59 Security Dependency Rules

Security infrastructure may integrate with:

- Spring Security.
- Authentication mechanisms.
- Device identity.
- Application authorization services.

Security must not contain financial business rules.

### 6.60 DTO and Entity Isolation

The following models must remain separate:

```text
API DTO
   ↕
Domain Model
   ↕
Persistence Entity
```

They should not be treated as interchangeable objects.

This separation allows:

- API evolution.
- Database evolution.
- Domain evolution.
- Independent validation.
- Safer synchronization contracts.

### 6.61 Package Naming Rules

Backend packages must:

- Use lowercase names.
- Represent a clear responsibility.
- Avoid unnecessary abbreviations.
- Avoid generic dumping-ground packages.
- Keep infrastructure-specific code isolated.
- Keep Domain code framework-independent.

Class names must follow standard Java naming conventions.

### 6.62 Package Structure and Future Service Extraction

The package structure intentionally creates boundaries that could support future extraction.

For example:

```text
domain/group
application/group
api/group
persistence/...Group...
```

could eventually become part of a Group Service if scale requires it.

However, V1 must remain a modular monolith.

### 6.63 Backend Package Structure Invariants

The following rules are mandatory for V1:

- The backend will initially be a single Spring Boot application.
- Package boundaries must reflect architectural boundaries.
- API packages must contain HTTP-specific code only.
- Application packages must contain use-case orchestration.
- Domain packages must remain independent of Spring and persistence frameworks.
- Persistence packages must contain database-specific implementations.
- REST DTOs must remain separate from Domain models.
- JPA entities must remain separate from API DTOs.
- Synchronization must remain isolated as a dedicated subsystem.
- Security must remain isolated from financial business logic.
- Controllers must not directly access JPA repositories.
- Business rules must not be implemented inside controllers.
- Synchronization operations must be validated before persistence.
- Synchronization processing must be idempotent.
- Database operations requiring atomicity must use appropriate transaction boundaries.
- Generic packages must not become dumping grounds.
- The package structure must remain suitable for future module/service extraction.

## 7. Domain Model

### 7.1 Purpose

This section defines the core business domain of SplitSync V1.

The Domain Model represents the concepts and rules that remain valid regardless of:

- Android UI.
- Room/SQLite.
- REST APIs.
- Spring Boot.
- Hibernate/JPA.
- MySQL.
- Internet connectivity.
- Local peer-to-peer communication.

The Domain Model is therefore the central business model shared conceptually between the Android client and backend.

The Android client must be capable of executing the core Domain logic while offline.

The backend must independently validate the same business rules when processing synchronized data.

### 7.2 Domain Model Overview

The primary domain concepts are:

```text
User
  │
  └── Device
       
User
  │
  └── Group Membership
           │
           ▼
         Group
           │
     ┌─────┼───────────────┐
     │     │               │
     ▼     ▼               ▼
  Expense  Settlement   Group Members
     │
     ▼
Expense Split
     │
     ▼
  Balance
```

Synchronization is a supporting domain/infrastructure concept:

```text
Local Domain State
        │
        ▼
 Sync Operation
        │
        ├───────────────┐
        ▼               ▼
 Backend Sync       Peer Sync
```

### 7.3 Core Domain Entities

The primary V1 entities are:

```text
User
Device
Group
GroupMember
Expense
ExpenseSplit
Settlement
```

Synchronization-related entities are:

```text
SyncOperation
SyncState
Conflict
```

The exact persistence representation of these concepts is defined separately in the Database Design.

### 7.4 User

A `User` represents a person using SplitSync.

A User is identified independently from the physical Android device.

Core responsibilities:

- Represent the application user.
- Store profile information required for identification.
- Participate in Groups.
- Create or record Expenses.
- Participate in Expense Splits.
- Participate in Settlements.

Conceptually:

```text
User
 ├── UserId
 ├── DisplayName
 ├── PhoneNumber
 ├── EmailAddress
 └── ProfileMetadata
```

Only finalized V1 profile attributes should be stored.

### 7.5 User Identity

The primary identity of a User is represented by a generated application-level identifier.

```text
UserId
```

The identifier must not depend on:

- Phone number.
- Email address.
- Android hardware identifiers.
- Device MAC address.
- IMEI.

Phone number and email address, where collected, are profile/contact attributes rather than primary database identifiers.

### 7.6 User Profile

The V1 User Profile may contain:

- Display name.
- Phone number.
- Email address where supported.
- Profile image/reference where supported in future.

The profile must remain usable offline.

Creating a local profile must not require internet connectivity.

### 7.7 Device

A `Device` represents one installed SplitSync application instance.

The Device is different from the User.

Conceptually:

```text
User
 │
 └── Device
```

V1 supports:

```text
One Device
    ↓
One Local User
```

A device-level identity is required for:

- Local synchronization.
- Backend synchronization.
- Peer identification.
- Change attribution.
- Device trust.

### 7.8 Device Identity

The application must generate its own application-level Device ID.

The Device ID must not rely on restricted or unreliable hardware identifiers.

Examples of identifiers that must not be used as the primary application Device ID:

```text
IMEI
MAC Address
Hardware Serial Number
```

The Device ID must persist across normal application usage.

### 7.9 Device Lifecycle

The logical Device lifecycle is:

```text
Application Installed
        ↓
Device ID Generated
        ↓
Local User Created
        ↓
Device Associated With User
        ↓
Application Used
        ↓
Optional Backend Registration
        ↓
Optional Peer Synchronization
```

The application must remain functional even if backend registration has not yet occurred.

### 7.10 Group

A `Group` is the primary shared-expense boundary in SplitSync.

A Group may represent:

- Trip.
- Family.
- Household.
- Friends.
- Office.
- Event.
- Any other shared-expense context.

A Trip is therefore not a separate core domain model in V1.

Instead:

```text
Trip
  ↓
Group
```

### 7.11 Group Identity

Every Group has a unique application-level identifier:

```text
GroupId
```

The identifier must remain stable across:

- Local persistence.
- Backend synchronization.
- Peer-to-peer synchronization.

### 7.12 Group Attributes

A Group may contain:

```text
Group
 ├── GroupId
 ├── Name
 ├── Description
 ├── GroupType
 ├── CreatedBy
 ├── CreatedAt
 ├── UpdatedAt
 └── Status
```

The exact attribute set will be finalized during Database Design.

### 7.13 Group Type

V1 may support a flexible Group type.

Conceptually:

```text
GroupType
├── GENERAL
├── TRIP
├── FAMILY
├── HOUSEHOLD
├── FRIENDS
├── OFFICE
└── EVENT
```

The Group Type primarily helps organization and presentation.

It must not fundamentally change the expense calculation model.

### 7.14 Group Membership

A User participates in a Group through a `GroupMember`.

Conceptually:

```text
User
   │
   │ membership
   ▼
GroupMember
   │
   ▼
Group
```

A GroupMember represents the relationship between:

- User.
- Group.
- Role.
- Membership status.

### 7.15 GroupMember

Conceptually:

```text
GroupMember
 ├── GroupMemberId
 ├── GroupId
 ├── UserId
 ├── Role
 ├── Status
 ├── JoinedAt
 └── UpdatedAt
```

A User may belong to multiple Groups.

A Group may contain multiple Users.

Therefore:

```text
User 1 ─────── * GroupMember * ─────── 1 Group
```

### 7.16 Group Roles

V1 should support a minimal role model.

Recommended roles:

```text
OWNER
MEMBER
```

The Owner is responsible for Group-level administrative operations.

Members participate in the Group and its expenses.

Additional roles should not be introduced unless required by an actual V1 use case.

### 7.17 Group Membership Status

Membership may have states such as:

```text
ACTIVE
PENDING
REMOVED
```

The final state model will depend on the invitation and synchronization design.

A removed member must not automatically lose historical participation in previously recorded Expenses.

### 7.18 Expense

An `Expense` represents money spent for a Group.

An Expense belongs to exactly one Group.

Conceptually:

```text
Group
  │
  └── Expense
```

An Expense contains:

- Amount.
- Currency.
- Description.
- Date/time.
- Payer.
- Expense Splits.
- Metadata.

### 7.19 Expense Identity

Every Expense has a unique:

```text
ExpenseId
```

The Expense ID must remain stable across:

- Local storage.
- Backend synchronization.
- Peer synchronization.

### 7.20 Expense Attributes

Conceptually:

```text
Expense
 ├── ExpenseId
 ├── GroupId
 ├── PaidBy
 ├── Amount
 ├── Currency
 ├── Description
 ├── ExpenseDate
 ├── CreatedAt
 ├── UpdatedAt
 └── Status
```

The exact persistence representation will be defined separately.

### 7.21 Money Representation

Financial amounts must not be represented using binary floating-point types such as:

```text
float
double
```

The Domain Model must use an exact monetary representation.

The implementation may use:

```text
BigDecimal
```

or an integer minor-unit representation such as:

```text
long amountMinor
```

The final V1 representation must be selected consistently across Android, backend, synchronization, and database layers.

### 7.22 Currency

Every Expense must have an explicit currency.

Example:

```text
INR
USD
EUR
```

V1 should not assume that every future Group will necessarily use INR.

Currency must therefore be part of the Expense financial model.

Multi-currency expense conversion is not required unless explicitly included in the V1 feature scope.

### 7.23 Expense Payer

Every Expense has exactly one payer in V1.

Conceptually:

```text
Expense
   │
   └── PaidBy → User
```

The payer must be an active or historically valid member of the Group according to the applicable business rules.

### 7.24 Expense Participants

An Expense contains one or more Expense Splits.

An Expense Split determines how the Expense amount is attributed to participating Group members.

Conceptually:

```text
Expense
   │
   ├── ExpenseSplit → User A
   ├── ExpenseSplit → User B
   └── ExpenseSplit → User C
```

The payer and participants are separate concepts.

A payer may or may not participate in the split.

### 7.25 Expense Split

An `ExpenseSplit` represents one member's share of an Expense.

Conceptually:

```text
Expense
   │
   └── ExpenseSplit
          ├── User
          ├── SplitMethod
          └── Share
```

An Expense must have one or more Expense Splits.

The sum of all resulting split amounts must equal the Expense amount.

### 7.26 Split Methods

V1 should support the following split methods:

```text
EQUAL
EXACT
PERCENTAGE
SHARES
```

#### EQUAL

The Expense amount is divided equally among selected participants.

#### EXACT

Each participant receives an explicitly specified amount.

#### PERCENTAGE

Each participant receives a percentage of the Expense.

#### SHARES

Each participant receives a number of shares, and the Expense is divided proportionally according to those shares.

### 7.27 Split Invariants

The following rules must always hold:

```text
Expense amount > 0
```

and:

```text
sum(all split amounts) = expense amount
```

For percentage splits:

```text
sum(all percentages) = 100%
```

For shares:

```text
total shares > 0
```

Invalid split configurations must be rejected before persistence.

### 7.28 Balance

A `Balance` represents the net financial position of a User within a Group.

A simplified calculation is:

```text
Balance = Amount Paid - Amount Owed
```

Therefore:

```text
Positive Balance
    ↓
User should receive money

Negative Balance
    ↓
User owes money

Zero Balance
    ↓
User is settled
```

The exact balance calculation must also consider recorded Settlements.

### 7.29 Group Balance

A Group Balance is derived from:

```text
Expenses
+
Expense Splits
+
Settlements
```

It should not be stored as an independently editable value.

Conceptually:

```text
Group
 │
 ├── Expenses
 │     └── Expense Splits
 │
 └── Settlements
          │
          ▼
     Balance Calculation
```

This prevents manually edited balance values from becoming inconsistent with the underlying financial records.

### 7.30 Settlement

A `Settlement` represents a transfer of money between two Group members to reduce or clear an outstanding balance.

Conceptually:

```text
Settlement
 ├── FromUser
 ├── ToUser
 ├── Amount
 ├── Currency
 ├── GroupId
 ├── SettlementDate
 └── Status
```

### 7.31 Settlement Rules

A Settlement must:

- Belong to exactly one Group.
- Have exactly one sender.
- Have exactly one receiver.
- Have a positive amount.
- Use a valid Group currency where required by the financial model.
- Reference valid Group members.
- Not have the same sender and receiver.

### 7.32 Settlement Effect

If:

```text
User A owes User B = ₹500
```

and:

```text
User A settles ₹300 with User B
```

then the remaining balance is:

```text
₹200
```

The Settlement does not modify historical Expenses.

Instead, it is recorded as an independent financial event.

### 7.33 Financial Event Principle

Expenses and Settlements are separate financial events.

```text
Expense
    ↓
Creates financial obligation

Settlement
    ↓
Reduces financial obligation
```

Historical Expenses must not be modified merely because a Settlement is recorded.

### 7.34 Group Financial State

The financial state of a Group is derived from its financial events.

```text
Group
 │
 ├── Expense 1
 ├── Expense 2
 ├── Expense 3
 │
 ├── Settlement 1
 └── Settlement 2
          │
          ▼
     Balance Engine
          │
          ▼
    Member Balances
```

This allows the application to recalculate balances from source data.

### 7.35 Synchronization Operation

A `SyncOperation` represents a local change that must be propagated through one or more synchronization channels.

Conceptually:

```text
SyncOperation
 ├── OperationId
 ├── DeviceId
 ├── EntityType
 ├── EntityId
 ├── OperationType
 ├── Version
 ├── Timestamp
 └── Payload
```

The exact representation will be finalized in the Synchronization Data Model.

### 7.36 Operation Types

V1 synchronization should conceptually support:

```text
CREATE
UPDATE
DELETE
```

Additional operation types may be introduced if required by the synchronization protocol.

### 7.37 Entity Types

Synchronization may operate on:

```text
USER
DEVICE
GROUP
GROUP_MEMBER
EXPENSE
EXPENSE_SPLIT
SETTLEMENT
```

The exact list will follow the final synchronization model.

### 7.38 Sync State

`SyncState` represents the synchronization progress of local data with an external synchronization source.

Conceptually:

```text
SyncState
 ├── LastSyncCursor
 ├── LastSuccessfulSync
 ├── PendingOperations
 └── SyncStatus
```

Sync state is primarily an infrastructure concern and should not alter the underlying financial Domain state.

### 7.39 Conflict

A `Conflict` represents a situation where two valid changes cannot be automatically applied without a resolution decision.

Examples include:

```text
Device A updates Expense X
Device B updates Expense X
```

before either device has received the other change.

Conflict handling rules are defined separately in the Conflict Resolution design.

### 7.40 Entity Identity

All synchronizable entities must have stable identifiers.

Recommended identifiers:

```text
UserId
DeviceId
GroupId
GroupMemberId
ExpenseId
ExpenseSplitId
SettlementId
OperationId
```

Identifiers must remain stable across synchronization channels.

### 7.41 Entity Ownership

Ownership must be explicitly defined.

Examples:

```text
Expense
    belongs to Group

ExpenseSplit
    belongs to Expense

Settlement
    belongs to Group

GroupMember
    belongs to Group and references User
```

This ownership is important for:

- Authorization.
- Synchronization.
- Conflict resolution.
- Data consistency.

### 7.42 Aggregate Boundaries

The main Domain aggregate boundaries are:

```text
Group Aggregate
    ├── Group
    └── Group Membership Rules

Expense Aggregate
    ├── Expense
    └── Expense Splits

Settlement Aggregate
    └── Settlement
```

The exact aggregate implementation may evolve during coding, but transaction boundaries must respect the defined business invariants.

### 7.43 Group Aggregate

The Group is responsible for Group-level invariants such as:

- Group identity.
- Membership rules.
- Group ownership.
- Group status.

Expense and Settlement records remain separate financial aggregates while referencing the Group.

### 7.44 Expense Aggregate

An Expense and its Expense Splits form a logical financial unit.

```text
Expense
   │
   ├── Split 1
   ├── Split 2
   └── Split 3
```

The Expense cannot be considered valid if its required Splits are invalid.

Therefore creation/update of an Expense and its Splits must be handled atomically.

### 7.45 Settlement Aggregate

A Settlement is an independent financial event.

It must be validated independently and persisted atomically.

A Settlement must not directly modify historical Expense records.

### 7.46 Domain Services

Some operations do not naturally belong to a single Entity.

Potential Domain Services include:

```text
ExpenseSplitCalculator
BalanceCalculator
SettlementCalculator
ConflictResolver
```

These services should contain pure business logic wherever possible.

### 7.47 Expense Split Calculator

The Expense Split Calculator is responsible for:

- Equal splitting.
- Exact amount validation.
- Percentage calculation.
- Share-based calculation.
- Rounding handling.
- Final split validation.

The calculator must guarantee:

```text
sum(split amounts) = expense amount
```

### 7.48 Balance Calculator

The Balance Calculator derives balances from financial events.

Conceptually:

```text
For each member:

Net Balance =
    Total Paid
    - Total Share
    + Settlements Received
    - Settlements Paid
```

The exact sign convention must remain consistent across Android and backend.

### 7.49 Settlement Calculator

The Settlement Calculator may be used to determine suggested transfers required to reduce outstanding balances.

For example:

```text
A owes B ₹500
C owes B ₹300
B owes D ₹200
```

The system may calculate an optimized settlement plan.

However, suggested settlements and recorded settlements are different concepts.

### 7.50 Recorded vs Suggested Settlement

A suggested settlement is a calculated recommendation.

A recorded Settlement is an actual financial event entered into the system.

```text
Balance Calculation
       ↓
Suggested Settlement
       ↓
User confirms
       ↓
Recorded Settlement
```

The application must not treat a suggestion as an actual payment until explicitly recorded.

### 7.51 Domain Validation

Domain validation must occur before accepting a state change.

Examples:

```text
Invalid Expense Amount
        ↓
Reject

Invalid Split Total
        ↓
Reject

Invalid Group Member
        ↓
Reject

Settlement With Same Sender/Receiver
        ↓
Reject
```

Validation must be performed locally.

The backend must independently validate synchronized operations.

### 7.52 Domain Invariants

The following invariants are mandatory:

```text
UserId must be unique.

DeviceId must be unique.

GroupId must be unique.

ExpenseId must be unique.

SettlementId must be unique.

Expense amount must be positive.

Expense must belong to a valid Group.

Expense payer must be a valid Group participant according to Group rules.

Expense must contain valid Splits.

Sum of split amounts must equal Expense amount.

Settlement amount must be positive.

Settlement sender and receiver must be different.

Settlement participants must belong to the Group according to applicable rules.
```

### 7.53 Historical Data Principle

Historical financial records must remain auditable.

The system should not silently rewrite historical financial events to make balances appear correct.

Instead:

```text
Original Expense
       ↓
Correction / Update
       ↓
New synchronized state
```

or, where required by the final audit model:

```text
Original Event
       ↓
Correction Event
```

The final edit/delete audit strategy will be defined in the Synchronization and Database Design sections.

### 7.54 Deletion Principle

Deletion of financial records must be treated carefully.

A deleted Expense must not cause synchronization inconsistencies or silently disappear from another device.

The synchronization model must therefore preserve sufficient deletion metadata, such as:

```text
EntityId
DeletedAt
Version
DeletionOperation
```

The exact implementation will be defined later.

### 7.55 Offline Domain Requirement

All core Domain operations must be executable without network access.

The following must work offline:

```text
Create User
Create Group
Add Member
Create Expense
Calculate Split
Calculate Balance
Create Settlement
View History
```

No Domain operation should require an HTTP request.

### 7.56 Online Domain Requirement

When internet connectivity is available, the same Domain rules remain applicable.

The application does not switch to a different financial calculation model simply because it is online.

The communication path changes:

```text
Offline:

UI
 ↓
Domain
 ↓
Local Data


Online:

UI
 ↓
Domain
 ↓
Local Data
 ↓
Synchronization
 ↓
Backend
```

The Domain Model remains the same.

### 7.57 Peer Synchronization Requirement

Peer-to-peer synchronization must exchange Domain data through the synchronization protocol.

Peer communication must not bypass Domain validation.

Conceptually:

```text
Peer Change
     ↓
Deserialize
     ↓
Validate
     ↓
Domain Rules
     ↓
Apply Local Transaction
```

A nearby device must not be trusted merely because it is connected to the same Wi-Fi/hotspot.

### 7.58 Domain Model Independence

The Domain Model must not know whether a change originated from:

```text
Local User Input
Backend
Peer Device
Imported Data
```

All changes must ultimately pass through the appropriate validation and state transition rules.

### 7.59 Domain Model Summary

The core relationship is:

```text
User
 │
 ├── Device
 │
 └── Group Membership
          │
          ▼
        Group
          │
          ├── Expense
          │     └── Expense Split
          │
          └── Settlement
                    │
                    ▼
              Balance Calculation
```

Synchronization operates around these entities:

```text
Domain State
     │
     ▼
Sync Operation
     │
     ├── Backend
     │
     └── Peer
```

### 7.60 Domain Package Summary

```text
domain/
├── user/
├── device/
├── group/
├── expense/
├── settlement/
├── synchronization/
└── common/
```

### 7.61 Domain Model Invariants

The following rules are mandatory for V1:

- User identity must be independent of Device identity.
- V1 supports one local User per Device.
- A Group is the primary shared-expense boundary.
- Trip is a Group type rather than a separate financial model.
- Users participate in Groups through GroupMember.
- Expenses belong to Groups.
- Expenses have exactly one payer in V1.
- Expenses contain one or more Expense Splits.
- Expense Split amounts must reconcile exactly with the Expense amount.
- Supported split methods are Equal, Exact, Percentage, and Shares.
- Settlements are independent financial events.
- Settlements must not rewrite historical Expenses.
- Balances are derived from Expenses, Splits, and Settlements.
- Core Domain operations must work offline.
- Domain logic must remain independent of Android, Spring Boot, Hibernate, MySQL, and network transport.
- Android and backend must independently validate financial rules.
- Synchronization must preserve stable entity identities.
- Peer connectivity must never be treated as authorization by itself.
- Financial calculations must be deterministic across Android and backend.

## 8. Entity Relationships

### 8.1 Purpose

This section defines the relationships between the core SplitSync V1 domain entities.

The relationship model must support:

- Offline-first operation.
- Multiple Groups per User.
- Multiple Users per Group.
- Multiple Expenses per Group.
- Multiple Expense Splits per Expense.
- Multiple Settlements per Group.
- Stable relationships across synchronization.
- Consistent financial calculations.
- Secure Group-level authorization.
- Local peer-to-peer synchronization.

### 8.2 Core Entity Relationship Overview

The primary relationships are:

```text
User
 │
 ├───────────────┐
 │               │
 │               ▼
 │            Device
 │
 │
 └───────< GroupMember >─────── Group
                                  │
                         ┌────────┼─────────┐
                         │        │         │
                         ▼        ▼         ▼
                      Expense  Settlement  Members
                         │
                         ▼
                   ExpenseSplit
                         │
                         ▼
                        User
```

The relationship model can be represented as:

```text
User 1 ──────── * Device

User 1 ──────── * GroupMember
Group 1 ─────── * GroupMember

Group 1 ─────── * Expense
Expense 1 ───── * ExpenseSplit

User 1 ──────── * ExpenseSplit

Group 1 ─────── * Settlement

User 1 ──────── * Settlement (as sender)
User 1 ──────── * Settlement (as receiver)
```

### 8.3 User to Device Relationship

A User and Device represent different concepts.

Relationship:

```text
User 1 ──────── * Device
```

A User may have multiple Devices in the overall system.

However, V1 has a local-device restriction:

```text
One Device
    ↓
One Local User
```

This means a single installed application instance represents one local User in V1.

The backend architecture should still allow a User to be associated with multiple Devices in the future.

### 8.4 User to GroupMember Relationship

A User participates in Groups through GroupMember.

Relationship:

```text
User 1 ──────── * GroupMember
```

This allows a User to participate in multiple Groups.

Example:

```text
User A
 │
 ├── GroupMember → Trip Goa
 ├── GroupMember → Family
 └── GroupMember → Office
```

The User record itself does not directly store a list of Groups as its primary relationship representation.

Group membership is represented through the GroupMember relationship.

### 8.5 Group to GroupMember Relationship

A Group contains multiple GroupMember records.

Relationship:

```text
Group 1 ─────── * GroupMember
```

Example:

```text
Trip Goa
 │
 ├── Praveen
 ├── Rahul
 ├── Amit
 └── Neha
```

Each membership record represents one User's participation in the Group.

### 8.6 User to Group Relationship

User and Group have a many-to-many relationship.

Conceptually:

```text
User * ─────── * Group
```

This is implemented through:

```text
GroupMember
```

Therefore:

```text
User
  │
  ▼
GroupMember
  │
  ▼
Group
```

This relationship must not be implemented as a direct User-to-Group foreign key.

### 8.7 GroupMember Relationship

GroupMember references exactly one User and exactly one Group.

Conceptually:

```text
GroupMember
 ├── UserId
 └── GroupId
```

Therefore:

```text
User 1 ─────── 1 GroupMember
Group 1 ────── 1 GroupMember
```

from the perspective of an individual membership record.

A User can have many GroupMember records, and a Group can have many GroupMember records.

### 8.8 Group to Expense Relationship

Every Expense belongs to exactly one Group.

Relationship:

```text
Group 1 ─────── * Expense
```

Example:

```text
Trip Goa
 │
 ├── Hotel ₹5000
 ├── Food ₹1200
 ├── Taxi ₹800
 └── Tickets ₹2000
```

An Expense cannot belong to multiple Groups.

If the same real-world payment needs to be represented in multiple Groups, separate Group-specific Expenses must be created.

### 8.9 Expense to ExpenseSplit Relationship

An Expense contains one or more ExpenseSplits.

Relationship:

```text
Expense 1 ─────── * ExpenseSplit
```

Example:

```text
Expense: Dinner ₹1200
 │
 ├── Split → Praveen ₹400
 ├── Split → Rahul   ₹400
 └── Split → Amit    ₹400
```

The Expense and its Splits form a logical financial unit.

### 8.10 User to ExpenseSplit Relationship

Each ExpenseSplit belongs to one User.

Relationship:

```text
User 1 ─────── * ExpenseSplit
```

A User may participate in many Expenses through ExpenseSplits.

Example:

```text
Praveen
 │
 ├── Dinner → ₹400
 ├── Hotel  → ₹1500
 ├── Taxi   → ₹300
 └── Lunch  → ₹250
```

The User does not directly own the ExpenseSplit.

The Expense owns the ExpenseSplit, while the ExpenseSplit references the participating User.

### 8.11 Expense to Payer Relationship

Every Expense has exactly one payer in V1.

Relationship:

```text
User 1 ─────── * Expense
```

where the relationship represents:

```text
User → PaidBy → Expense
```

Example:

```text
Praveen
   │
   ├── paid for → Hotel
   ├── paid for → Dinner
   └── paid for → Taxi
```

The payer must be a valid participant of the Group according to the applicable Group rules.

### 8.12 Payer vs Expense Participant

The payer and Expense participants are independent relationships.

For example:

```text
Expense = ₹1000
PaidBy = Praveen

Participants:
Praveen
Rahul
Amit
```

Praveen can be both:

```text
Payer
+
Participant
```

But the model also supports:

```text
Payer = Praveen

Participants:
Rahul
Amit
```

Therefore:

```text
Expense
 ├── PaidBy → User
 └── ExpenseSplits
        ├── User
        ├── User
        └── User
```

### 8.13 Group to Settlement Relationship

Every Settlement belongs to exactly one Group.

Relationship:

```text
Group 1 ─────── * Settlement
```

Example:

```text
Trip Goa
 │
 ├── Praveen → Rahul ₹500
 ├── Amit → Praveen ₹300
 └── Rahul → Amit ₹200
```

A Settlement must never belong to multiple Groups.

### 8.14 User to Settlement Relationships

A Settlement has two User relationships:

```text
FromUser
ToUser
```

Therefore:

```text
User 1 ─────── * Settlement
User 1 ─────── * Settlement
```

The two relationships have different semantic meanings:

```text
FromUser
    ↓
Person making the settlement

ToUser
    ↓
Person receiving the settlement
```

### 8.15 Settlement Participants

A Settlement must have:

```text
FromUser != ToUser
```

Both users must satisfy the applicable Group membership rules.

Example:

```text
Group: Trip Goa

FromUser = Rahul
ToUser   = Praveen
Amount   = ₹500
```

This represents:

```text
Rahul → Praveen ₹500
```

### 8.16 Group to Financial Records

The Group is the parent boundary for financial records.

```text
Group
 │
 ├── Expense
 │     └── ExpenseSplit
 │
 └── Settlement
```

This relationship is central to balance calculation.

### 8.17 Group to ExpenseSplit Relationship

There is no need for ExpenseSplit to independently reference Group as its primary domain relationship because:

```text
ExpenseSplit
    ↓
Expense
    ↓
Group
```

Therefore:

```text
ExpenseSplit → Expense → Group
```

The final database model may include denormalized Group references for performance or synchronization purposes only if justified by the Database Design.

The source-of-truth relationship remains:

```text
ExpenseSplit → Expense → Group
```

### 8.18 Group to User Financial Relationship

A User's financial position inside a Group is derived through:

```text
Group
 │
 ├── Expenses
 │     └── ExpenseSplits
 │
 └── Settlements
       │
       ├── FromUser
       └── ToUser
```

The Group does not need to store a manually editable balance for each User.

### 8.19 Balance Relationship

Balance is a derived concept.

It is calculated from:

```text
Expenses
+
ExpenseSplits
+
Payer relationships
+
Settlements
```

Conceptually:

```text
Group
 │
 ├── Expenses
 │     ├── PaidBy
 │     └── Splits
 │
 └── Settlements
       ├── FromUser
       └── ToUser
             │
             ▼
       Balance Calculator
             │
             ▼
        User Balances
```

### 8.20 User Balance Within Group

A User can have a different balance in different Groups.

Example:

```text
Praveen

Trip Goa
    Balance = +₹500

Office Lunch
    Balance = -₹200

Family
    Balance = ₹0
```

Therefore a Balance must always be interpreted within a Group context.

```text
Balance = User + Group + Financial Events
```

### 8.21 GroupMember and Financial Relationships

A GroupMember is the authorization and participation boundary for Group financial activity.

Conceptually:

```text
GroupMember
    │
    ├── can participate in Expense
    ├── can be Expense payer
    └── can participate in Settlement
```

A removed member may still appear in historical Expense and Settlement records because historical financial records must remain understandable.

### 8.22 Historical Member Relationship

Removing a GroupMember must not automatically delete:

- Historical Expenses.
- Historical ExpenseSplits.
- Historical Settlements.

For example:

```text
Rahul
  ↓
Group Member
  ↓
Removed from Group
  ↓
Historical Expenses remain
```

This preserves financial history.

### 8.23 User Deletion and Historical Data

Deleting or deactivating a User must not break historical financial relationships.

Historical records may continue to reference the User identity.

The exact User deletion/deactivation strategy will be finalized in the Security and Database Design sections.

### 8.24 Device and Synchronization Relationship

Synchronization operations are associated with the Device that created or submitted them.

Conceptually:

```text
Device
  │
  └── * SyncOperation
```

Relationship:

```text
Device 1 ─────── * SyncOperation
```

This allows the system to identify the source device of a change.

### 8.25 Entity to SyncOperation Relationship

A SyncOperation references the entity being synchronized.

Conceptually:

```text
SyncOperation
 ├── EntityType
 ├── EntityId
 ├── OperationType
 └── DeviceId
```

Therefore the relationship is polymorphic at the synchronization level.

Example:

```text
SyncOperation
    EntityType = EXPENSE
    EntityId   = E123
```

The operation applies to:

```text
Expense E123
```

The exact synchronization schema will be finalized separately.

### 8.26 Group-Level Synchronization Boundary

Synchronization authorization is ultimately constrained by Group membership for Group-owned entities.

For example:

```text
Expense
   ↓
Group
   ↓
GroupMember
   ↓
User / Device Authorization
```

A device must not be allowed to synchronize an Expense simply because it knows the Expense ID.

### 8.27 Entity Relationship Diagram

The conceptual ER model is:

```text
┌───────────────┐
│     USER      │
├───────────────┤
│ UserId        │
│ DisplayName   │
│ PhoneNumber   │
│ EmailAddress  │
└───────┬───────┘
        │
        │ 1
        │
        │ *
┌───────▼───────────┐
│    GROUP_MEMBER   │
├───────────────────┤
│ GroupMemberId     │
│ UserId            │
│ GroupId           │
│ Role              │
│ Status            │
└───────┬───────────┘
        │
        │ *
        │
        │ 1
┌───────▼───────────┐
│       GROUP       │
├───────────────────┤
│ GroupId           │
│ Name              │
│ GroupType         │
│ CreatedBy         │
│ Status            │
└───────┬───────────┘
        │
        ├──────────────────────┐
        │ 1                    │ 1
        │ *                    │ *
┌───────▼───────────┐   ┌──────▼────────────┐
│      EXPENSE      │   │    SETTLEMENT     │
├───────────────────┤   ├───────────────────┤
│ ExpenseId         │   │ SettlementId      │
│ GroupId           │   │ GroupId           │
│ PaidBy            │   │ FromUser          │
│ Amount            │   │ ToUser            │
│ Currency          │   │ Amount            │
│ Description       │   │ Currency          │
│ ExpenseDate       │   │ SettlementDate    │
└───────┬───────────┘   └───────────────────┘
        │
        │ 1
        │
        │ *
┌───────▼───────────┐
│   EXPENSE_SPLIT   │
├───────────────────┤
│ ExpenseSplitId    │
│ ExpenseId         │
│ UserId            │
│ SplitMethod       │
│ Amount            │
│ Percentage        │
│ Shares            │
└───────────────────┘


┌───────────────┐
│    DEVICE     │
├───────────────┤
│ DeviceId      │
│ UserId        │
│ Status        │
└───────┬───────┘
        │
        │ 1
        │
        │ *
┌───────▼──────────────┐
│    SYNC_OPERATION    │
├──────────────────────┤
│ OperationId          │
│ DeviceId             │
│ EntityType           │
│ EntityId             │
│ OperationType        │
│ Version              │
└──────────────────────┘
```

### 8.29 Relationship Cardinality Summary

| Relationship | Cardinality |
|---|---|
| User → Device | 1:N |
| User → GroupMember | 1:N |
| Group → GroupMember | 1:N |
| User ↔ Group | N:M through GroupMember |
| Group → Expense | 1:N |
| Expense → ExpenseSplit | 1:N |
| User → ExpenseSplit | 1:N |
| User → Expense as Payer | 1:N |
| Group → Settlement | 1:N |
| User → Settlement as FromUser | 1:N |
| User → Settlement as ToUser | 1:N |
| Device → SyncOperation | 1:N |

### 8.30 Relationship Ownership Summary

The ownership hierarchy is:

```text
User
 └── Device

Group
 └── GroupMember
      └── User reference

Group
 ├── Expense
 │    └── ExpenseSplit
 │         └── User reference
 │
 └── Settlement
      ├── FromUser reference
      └── ToUser reference
```

### 8.31 Foreign Key Principles

Where relational persistence is used, foreign keys should enforce valid relationships.

Examples:

```text
Device.user_id
    → User.id

GroupMember.user_id
    → User.id

GroupMember.group_id
    → Group.id

Expense.group_id
    → Group.id

Expense.paid_by
    → User.id

ExpenseSplit.expense_id
    → Expense.id

ExpenseSplit.user_id
    → User.id

Settlement.group_id
    → Group.id

Settlement.from_user_id
    → User.id

Settlement.to_user_id
    → User.id

SyncOperation.device_id
    → Device.id
```

The exact database column names will be finalized in the Database Design.

### 8.32 Referential Integrity

The system must maintain referential integrity.

Examples:

```text
Expense cannot reference a non-existent Group.

ExpenseSplit cannot reference a non-existent Expense.

ExpenseSplit cannot reference a non-existent User.

Settlement cannot reference a non-existent Group.

Settlement cannot reference a non-existent User.

GroupMember cannot reference a non-existent User.

GroupMember cannot reference a non-existent Group.
```

### 8.33 Financial Referential Integrity

In addition to database-level foreign keys, business-level validation is required.

For example:

```text
Expense
   ↓
Group = G1

Expense Payer
   ↓
Must satisfy applicable membership rules for G1
```

Similarly:

```text
ExpenseSplit User
   ↓
Must satisfy applicable participation rules for the Expense's Group
```

And:

```text
Settlement FromUser
Settlement ToUser
   ↓
Must satisfy applicable membership rules for the Settlement's Group
```

### 8.34 Synchronization Referential Integrity

Synchronization must preserve relationships.

An operation must not create:

```text
ExpenseSplit
    ↓
Non-existent Expense
```

or:

```text
Expense
    ↓
Non-existent Group
```

When related changes arrive in a different order, the synchronization system must handle dependency ordering or temporarily retain pending operations until their dependencies are available.

### 8.35 Relationship Ordering During Synchronization

A logical dependency order is:

```text
User
 ↓
Group
 ↓
GroupMember
 ↓
Expense
 ↓
ExpenseSplit
```

Settlement depends on:

```text
Group
+
FromUser
+
ToUser
```

Therefore synchronization must be capable of handling:

```text
Parent before Child
```

or:

```text
Dependency-aware pending operations
```

### 8.36 Offline Relationship Creation

Relationships can be created offline.

Example:

```text
Offline
  ↓
Create Group
  ↓
Create GroupMember
  ↓
Create Expense
  ↓
Create ExpenseSplit
```

All related entities are persisted locally.

When synchronization becomes available, the changes are propagated while preserving their stable identifiers.

### 8.37 Peer Relationship Synchronization

When two devices synchronize locally:

```text
Device A
    │
    └── Group G1
          │
          ├── Expense E1
          └── ExpenseSplit S1

          ⇅

Device B
    │
    └── Group G1
          │
          ├── Expense E1
          └── ExpenseSplit S1
```

Both devices must recognize the same logical entities using stable IDs.

The system must not create duplicate entities merely because the data arrived from another device.

### 8.38 Duplicate Prevention

Stable identifiers are used to prevent duplicate logical entities.

For example:

```text
ExpenseId = E123
```

If Device B receives Expense `E123` that already exists locally:

```text
Do not create E124.
```

Instead:

```text
Process E123
    ↓
Check Version / Operation
    ↓
Apply / Ignore / Resolve
```

### 8.39 Relationship and Authorization Boundary

Entity relationships must also support authorization.

For a Group-owned Expense:

```text
Expense
 ↓
Group
 ↓
GroupMember
 ↓
User
 ↓
Device
```

The server can therefore determine whether the requesting Device/User is authorized to access the Expense.

### 8.40 Relationship and Deletion

Deletion must preserve relationship consistency.

For example, deleting an Expense must not leave orphaned ExpenseSplits.

The operation should be atomic:

```text
Delete Expense
    ↓
Delete / Mark ExpenseSplits
    ↓
Update Synchronization Metadata
    ↓
Commit
```

The exact soft-delete/tombstone strategy will be finalized in the Database and Synchronization Design.

### 8.41 Relationship Invariants

The following rules are mandatory for V1:

- A User may participate in multiple Groups.
- A Group may contain multiple Users.
- User-to-Group membership must be represented through GroupMember.
- A Group may contain multiple Expenses.
- An Expense belongs to exactly one Group.
- An Expense contains one or more ExpenseSplits.
- An ExpenseSplit belongs to exactly one Expense.
- An ExpenseSplit references exactly one User.
- An Expense has exactly one payer in V1.
- A Group may contain multiple Settlements.
- A Settlement has exactly one FromUser and one ToUser.
- FromUser and ToUser must be different.
- Financial participants must satisfy applicable Group membership rules.
- Historical financial records must remain referentially understandable after membership changes.
- Balance must be derived from related financial records rather than manually edited.
- Synchronization must preserve stable entity relationships.
- Synchronization must handle dependent entities safely.
- Duplicate synchronization must not create duplicate logical entities.
- Foreign-key and business-level referential integrity must both be enforced.
- Peer connectivity must not bypass relationship or authorization validation.

## 9. Local Database Schema

### 9.1 Purpose

This section defines the local Android database schema for SplitSync V1.

The local database is the primary persistence layer of the Android application and must support the complete core application functionality without network connectivity.

Technology:

- Room.
- SQLite.

The local database stores:

- User profile.
- Device information.
- Groups.
- Group memberships.
- Expenses.
- Expense Splits.
- Settlements.
- Synchronization operations.
- Synchronization state.
- Conflict information.
- Deletion/tombstone information where required.

The local database must be designed independently from the backend database while maintaining compatible domain identifiers and synchronization semantics.

### 9.2 Local Database Principles

The local database follows these principles:

- Local-first.
- Persistent.
- Transactional.
- Offline-capable.
- Synchronization-aware.
- Referentially consistent.
- Idempotent synchronization support.
- Stable entity identifiers.
- No network dependency.
- No UI-specific persistence model.

The local database must remain fully usable when:

```text
Internet = unavailable
Local Network = unavailable
Peer Devices = unavailable
```

### 9.3 Database Name

The initial Room database name will be:

```text
splitsync.db
```

The final name may be changed before implementation if required.

### 9.4 Local Database Overview

The conceptual local schema is:

```text
┌───────────────┐
│     users     │
└───────┬───────┘
        │
        ├───────────────┐
        │               │
        ▼               ▼
┌───────────────┐ ┌───────────────┐
│    devices    │ │ group_members │
└───────┬───────┘ └───────┬───────┘
        │                 │
        │                 ▼
        │          ┌───────────────┐
        │          │    groups     │
        │          └───────┬───────┘
        │                  │
        │          ┌───────┴────────┐
        │          │                │
        │          ▼                ▼
        │   ┌───────────────┐ ┌───────────────┐
        │   │    expenses   │ │  settlements  │
        │   └───────┬───────┘ └───────────────┘
        │           │
        │           ▼
        │   ┌──────────────────┐
        │   │ expense_splits   │
        │   └──────────────────┘
        │
        ▼
┌────────────────────┐
│  sync_operations   │
└──────────┬─────────┘
           │
           ├─────────────────┐
           ▼                 ▼
┌────────────────┐   ┌────────────────┐
│  sync_states   │   │   conflicts    │
└────────────────┘   └────────────────┘
```

### 9.5 Table List

The initial V1 local database contains the following tables:

```text
users
devices
groups
group_members
expenses
expense_splits
settlements
sync_operations
sync_states
conflicts
```

Additional supporting tables may be introduced only when required by the finalized synchronization implementation.

### 9.6 Users Table

The `users` table stores the local User profile.

Conceptual schema:

```text
users
├── id
├── display_name
├── phone_number
├── email
├── created_at
├── updated_at
├── version
└── deleted_at
```

### 9.7 Users Table Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Stable User ID |
| `display_name` | TEXT | User display name |
| `phone_number` | TEXT | Optional phone number |
| `email` | TEXT | Optional email address |
| `created_at` | INTEGER | Creation timestamp |
| `updated_at` | INTEGER | Last update timestamp |
| `version` | INTEGER | Synchronization/entity version |
| `deleted_at` | INTEGER | Soft-delete timestamp, nullable |

The exact Room types will be finalized during implementation.

### 9.8 User Primary Key

The primary key is:

```text
users.id
```

The ID must be generated by the application and remain stable across synchronization.

The database must not use phone number or email as the primary key.

### 9.9 Device Table

The `devices` table represents an installed SplitSync application instance.

Conceptual schema:

```text
devices
├── id
├── user_id
├── device_name
├── created_at
├── updated_at
├── last_seen_at
├── status
└── version
```

### 9.10 Device Table Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Stable Device ID |
| `user_id` | TEXT | Associated User |
| `device_name` | TEXT | Optional device display name |
| `created_at` | INTEGER | Creation timestamp |
| `updated_at` | INTEGER | Last update timestamp |
| `last_seen_at` | INTEGER | Last known activity |
| `status` | TEXT | Device status |
| `version` | INTEGER | Synchronization/entity version |

### 9.11 Device Relationship

```text
devices.user_id
       ↓
users.id
```

The local V1 application enforces:

```text
One Device
    ↓
One Local User
```

The backend may support broader User-to-Device relationships in the future.

### 9.12 Groups Table

The `groups` table stores Group information.

Conceptual schema:

```text
groups
├── id
├── name
├── description
├── group_type
├── created_by
├── created_at
├── updated_at
├── status
├── version
└── deleted_at
```

### 9.13 Groups Table Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Stable Group ID |
| `name` | TEXT | Group name |
| `description` | TEXT | Optional description |
| `group_type` | TEXT | Group type |
| `created_by` | TEXT | User who created the Group |
| `created_at` | INTEGER | Creation timestamp |
| `updated_at` | INTEGER | Last update timestamp |
| `status` | TEXT | Group status |
| `version` | INTEGER | Synchronization/entity version |
| `deleted_at` | INTEGER | Soft-delete timestamp, nullable |

### 9.14 Group Creator Relationship

```text
groups.created_by
        ↓
users.id
```

The creator must be a valid User.

The creator may also be represented as the Group Owner through the GroupMember record.

### 9.15 Group Members Table

The `group_members` table represents User membership in a Group.

Conceptual schema:

```text
group_members
├── id
├── group_id
├── user_id
├── role
├── status
├── joined_at
├── updated_at
├── version
└── deleted_at
```

### 9.16 Group Members Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Stable GroupMember ID |
| `group_id` | TEXT | Group reference |
| `user_id` | TEXT | User reference |
| `role` | TEXT | Group role |
| `status` | TEXT | Membership status |
| `joined_at` | INTEGER | Membership creation timestamp |
| `updated_at` | INTEGER | Last update timestamp |
| `version` | INTEGER | Synchronization/entity version |
| `deleted_at` | INTEGER | Removal/deletion timestamp |

### 9.17 Group Membership Constraint

A User should not have duplicate active memberships in the same Group.

Conceptually:

```text
(group_id, user_id)
        ↓
Unique active membership
```

The exact database constraint strategy will be finalized during Room implementation.

### 9.18 Group Member Roles

V1 roles:

```text
OWNER
MEMBER
```

The role is stored as a controlled value.

The application must not allow arbitrary role strings.

### 9.19 Group Member Status

Recommended V1 statuses:

```text
ACTIVE
PENDING
REMOVED
```

The exact state transitions will be defined in the Group and Synchronization implementation.

### 9.20 Expenses Table

The `expenses` table stores Group expenses.

Conceptual schema:

```text
expenses
├── id
├── group_id
├── paid_by
├── amount_minor
├── currency
├── description
├── expense_date
├── created_at
├── updated_at
├── version
└── deleted_at
```

### 9.21 Expense Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Stable Expense ID |
| `group_id` | TEXT | Group reference |
| `paid_by` | TEXT | User who paid |
| `amount_minor` | INTEGER | Expense amount in minor currency units |
| `currency` | TEXT | Currency code |
| `description` | TEXT | Expense description |
| `expense_date` | INTEGER | Expense date/time |
| `created_at` | INTEGER | Creation timestamp |
| `updated_at` | INTEGER | Last update timestamp |
| `version` | INTEGER | Synchronization/entity version |
| `deleted_at` | INTEGER | Soft-delete timestamp, nullable |

### 9.22 Monetary Representation

The local database should store monetary values using integer minor units.

Example:

```text
₹125.50
```

may be stored as:

```text
12550
```

where:

```text
amount_minor = 12550
```

This avoids floating-point precision problems.

The exact minor-unit interpretation is determined by the currency.

### 9.23 Expense Currency

Every Expense must store its currency explicitly.

Example:

```text
INR
USD
EUR
```

Currency must not be inferred from the device locale.

### 9.24 Expense Payer Relationship

```text
expenses.paid_by
        ↓
users.id
```

The payer must satisfy applicable Group membership rules.

### 9.25 Expense Group Relationship

```text
expenses.group_id
        ↓
groups.id
```

Every Expense belongs to exactly one Group.

### 9.26 Expense Splits Table

The `expense_splits` table stores the participant allocation of an Expense.

Conceptual schema:

```text
expense_splits
├── id
├── expense_id
├── user_id
├── split_method
├── amount_minor
├── percentage
├── shares
├── created_at
├── updated_at
├── version
└── deleted_at
```

### 9.27 Expense Split Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Stable ExpenseSplit ID |
| `expense_id` | TEXT | Expense reference |
| `user_id` | TEXT | Participant User |
| `split_method` | TEXT | Split method |
| `amount_minor` | INTEGER | Calculated/final share amount |
| `percentage` | INTEGER/DECIMAL | Percentage when applicable |
| `shares` | INTEGER/DECIMAL | Share value when applicable |
| `created_at` | INTEGER | Creation timestamp |
| `updated_at` | INTEGER | Last update timestamp |
| `version` | INTEGER | Synchronization/entity version |
| `deleted_at` | INTEGER | Soft-delete timestamp, nullable |

The exact precision for `percentage` and `shares` will be finalized during implementation.

### 9.28 Expense Split Relationship

```text
expense_splits.expense_id
        ↓
expenses.id
```

Every ExpenseSplit belongs to exactly one Expense.

### 9.29 Expense Split User Relationship

```text
expense_splits.user_id
        ↓
users.id
```

Every ExpenseSplit references exactly one User.

### 9.30 Expense Split Methods

The `split_method` field may contain:

```text
EQUAL
EXACT
PERCENTAGE
SHARES
```

The selected method determines how the split is validated/calculated.

### 9.31 Expense Split Financial Invariant

For every active Expense:

```text
SUM(expense_splits.amount_minor)
=
expenses.amount_minor
```

This invariant must hold after every successful Expense transaction.

### 9.32 Percentage Split Invariant

For percentage-based splitting:

```text
SUM(percentage)
=
100
```

The exact precision strategy will be finalized during Domain implementation.

### 9.33 Shares Split Invariant

For share-based splitting:

```text
SUM(shares) > 0
```

The resulting monetary amounts must reconcile exactly with the Expense amount.

### 9.34 Settlements Table

The `settlements` table stores recorded settlements between Group members.

Conceptual schema:

```text
settlements
├── id
├── group_id
├── from_user_id
├── to_user_id
├── amount_minor
├── currency
├── settlement_date
├── description
├── created_at
├── updated_at
├── version
└── deleted_at
```

### 9.35 Settlement Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Stable Settlement ID |
| `group_id` | TEXT | Group reference |
| `from_user_id` | TEXT | User making payment |
| `to_user_id` | TEXT | User receiving payment |
| `amount_minor` | INTEGER | Settlement amount |
| `currency` | TEXT | Currency |
| `settlement_date` | INTEGER | Settlement date/time |
| `description` | TEXT | Optional description |
| `created_at` | INTEGER | Creation timestamp |
| `updated_at` | INTEGER | Last update timestamp |
| `version` | INTEGER | Synchronization/entity version |
| `deleted_at` | INTEGER | Soft-delete timestamp, nullable |

### 9.36 Settlement Relationships

```text
settlements.group_id
        ↓
groups.id

settlements.from_user_id
        ↓
users.id

settlements.to_user_id
        ↓
users.id
```

### 9.37 Settlement Constraints

The application must enforce:

```text
amount_minor > 0

from_user_id != to_user_id
```

Both users must satisfy the applicable Group membership rules.

### 9.38 Synchronization Operations Table

The `sync_operations` table stores local changes waiting to be synchronized.

Conceptual schema:

```text
sync_operations
├── id
├── device_id
├── entity_type
├── entity_id
├── operation_type
├── payload
├── entity_version
├── created_at
├── status
├── retry_count
├── last_attempt_at
├── next_retry_at
└── error_code
```

### 9.39 Sync Operation Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Unique synchronization operation ID |
| `device_id` | TEXT | Device that created the operation |
| `entity_type` | TEXT | Entity being synchronized |
| `entity_id` | TEXT | Entity identifier |
| `operation_type` | TEXT | CREATE/UPDATE/DELETE |
| `payload` | TEXT | Serialized change payload |
| `entity_version` | INTEGER | Entity version associated with operation |
| `created_at` | INTEGER | Operation creation time |
| `status` | TEXT | Pending/sync state |
| `retry_count` | INTEGER | Number of attempts |
| `last_attempt_at` | INTEGER | Last attempt timestamp |
| `next_retry_at` | INTEGER | Next eligible retry timestamp |
| `error_code` | TEXT | Last synchronization error |

The payload representation will be finalized during Synchronization Design.

### 9.40 Sync Operation Status

Recommended states:

```text
PENDING
IN_PROGRESS
COMPLETED
FAILED
CONFLICT
```

The exact state machine will be defined in the Synchronization Design.

### 9.41 Sync Operation Identity

The `sync_operations.id` value must be globally unique.

The operation ID is used for idempotency.

If the same operation reaches the backend or another peer more than once:

```text
Same OperationId
        ↓
Recognize duplicate
        ↓
Do not apply twice
```

### 9.42 Sync State Table

The `sync_states` table stores synchronization progress.

Conceptual schema:

```text
sync_states
├── id
├── scope_type
├── scope_id
├── channel
├── cursor
├── last_successful_sync_at
├── last_attempt_at
├── status
└── error_code
```

### 9.43 Sync State Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Sync state ID |
| `scope_type` | TEXT | Synchronization scope |
| `scope_id` | TEXT | Scope identifier |
| `channel` | TEXT | BACKEND/PEER |
| `cursor` | TEXT | Last processed synchronization cursor |
| `last_successful_sync_at` | INTEGER | Last successful synchronization |
| `last_attempt_at` | INTEGER | Last attempted synchronization |
| `status` | TEXT | Current sync state |
| `error_code` | TEXT | Last synchronization error |

### 9.44 Synchronization Scope

Synchronization state may be maintained at different scopes.

Potential scopes:

```text
DEVICE
GROUP
BACKEND
PEER
```

The final strategy will be defined in the Synchronization Architecture implementation.

### 9.45 Conflicts Table

The `conflicts` table stores synchronization conflicts that cannot be automatically resolved.

Conceptual schema:

```text
conflicts
├── id
├── entity_type
├── entity_id
├── local_version
├── remote_version
├── local_payload
├── remote_payload
├── detected_at
├── status
└── resolution
```

### 9.46 Conflict Fields

| Column | Type | Description |
|---|---|---|
| `id` | TEXT | Conflict ID |
| `entity_type` | TEXT | Conflicting entity type |
| `entity_id` | TEXT | Conflicting entity ID |
| `local_version` | INTEGER | Local version |
| `remote_version` | INTEGER | Remote version |
| `local_payload` | TEXT | Local conflicting state |
| `remote_payload` | TEXT | Remote conflicting state |
| `detected_at` | INTEGER | Conflict detection time |
| `status` | TEXT | Conflict status |
| `resolution` | TEXT | Resolution information |

### 9.47 Conflict Status

Recommended states:

```text
OPEN
RESOLVED
IGNORED
```

The exact state model will be finalized in Conflict Resolution Design.

### 9.48 Foreign Key Relationships

The logical local foreign key relationships are:

```text
devices.user_id
        → users.id

groups.created_by
        → users.id

group_members.group_id
        → groups.id

group_members.user_id
        → users.id

expenses.group_id
        → groups.id

expenses.paid_by
        → users.id

expense_splits.expense_id
        → expenses.id

expense_splits.user_id
        → users.id

settlements.group_id
        → groups.id

settlements.from_user_id
        → users.id

settlements.to_user_id
        → users.id

sync_operations.device_id
        → devices.id
```

### 9.49 Cascading Rules

Financial data must not be blindly deleted through database cascade rules.

In particular:

```text
Delete Group
    X
    ↓
Automatically delete all Expenses
```

must not be assumed.

Financial and synchronization data require explicit application-level handling.

The final delete strategy will use controlled operations and synchronization metadata.

### 9.50 Expense Transaction

Creating an Expense should be performed as one Room transaction.

Conceptually:

```text
BEGIN TRANSACTION

Create / Update Expense
        ↓
Create Expense Splits
        ↓
Validate Split Total
        ↓
Create Sync Operation
        ↓
COMMIT
```

If validation or persistence fails:

```text
ROLLBACK
```

No partial Expense should remain.

### 9.51 Settlement Transaction

Creating a Settlement should be atomic:

```text
BEGIN TRANSACTION

Create Settlement
        ↓
Validate Participants
        ↓
Create Sync Operation
        ↓
COMMIT
```

A Settlement must not partially exist.

### 9.52 Group Creation Transaction

Creating a Group locally may require:

```text
BEGIN TRANSACTION

Create Group
        ↓
Create Owner GroupMember
        ↓
Create Sync Operation(s)
        ↓
COMMIT
```

The exact synchronization operation strategy will be finalized later.

### 9.53 Local User Creation Transaction

Initial application setup may use:

```text
BEGIN TRANSACTION

Create User
        ↓
Create Device
        ↓
Associate Device with User
        ↓
COMMIT
```

This must work without network connectivity.

### 9.54 Timestamps

All synchronized entities should contain sufficient timestamps for local state management.

Recommended fields:

```text
created_at
updated_at
```

Timestamps should use a consistent representation.

The application should avoid using device-local formatted date strings as synchronization values.

### 9.55 Version Fields

Synchronizable entities should contain an entity version:

```text
version
```

The version is used to detect concurrent modifications.

Example:

```text
Expense E1
Version = 4
```

After a valid update:

```text
Version = 5
```

The exact version-generation strategy will be defined in Synchronization Design.

### 9.56 Soft Delete and Tombstones

Entities that can be deleted or removed through synchronization should preserve deletion information long enough for synchronization.

Example:

```text
deleted_at
```

This prevents a deleted entity from simply disappearing locally and becoming impossible to communicate to other devices.

The exact tombstone retention strategy will be finalized later.

### 9.57 Local Database and Offline Operation

The following operations must require only the local database:

```text
Create User
Create Group
View Groups
View Members
Create Expense
Edit Expense
Delete Expense
View Expenses
Calculate Balance
Create Settlement
View Settlements
```

Network access must not be part of these operations.

### 9.58 Local Database and Synchronization

The local database acts as the durable source for local application state.

Conceptually:

```text
User Action
    ↓
Domain Validation
    ↓
Room Transaction
    ↓
Local State Updated
    ↓
Sync Operation Created
```

The application does not perform:

```text
User Action
    ↓
HTTP Request
    ↓
Wait
    ↓
Save Local Data
```

This would violate the offline-first architecture.

### 9.59 Local Database as Source of Immediate UI State

The UI should observe local database state.

Conceptually:

```text
Room
 ↓
Repository
 ↓
Application Layer
 ↓
ViewModel
 ↓
UI
```

Synchronization updates the local database, which automatically updates the UI.

This avoids separate online and offline UI implementations.

### 9.60 Local Database and Backend Database

The local and backend databases are not required to have identical physical schemas.

They must, however, share compatible:

- Domain identifiers.
- Entity relationships.
- Financial rules.
- Synchronization semantics.
- Versioning concepts.

Therefore:

```text
Android Room Schema
        ≠
Backend MySQL Schema
```

but:

```text
Android Domain Model
        ≈
Backend Domain Model
```

at the business-concept level.

### 9.61 Local Database Indexing

Indexes should be created for frequently accessed relationships and synchronization queries.

Potential indexes include:

```text
group_members.group_id
group_members.user_id

expenses.group_id
expenses.paid_by
expenses.updated_at

expense_splits.expense_id
expense_splits.user_id

settlements.group_id
settlements.from_user_id
settlements.to_user_id

sync_operations.status
sync_operations.entity_id
sync_operations.created_at

sync_states.scope_id
sync_states.channel

conflicts.entity_id
conflicts.status
```

The final indexes will be determined from actual query patterns.

### 9.62 Synchronization Query Requirements

The local database must efficiently support:

```text
Get pending sync operations
Get failed sync operations
Get operations by entity
Get synchronization state
Get unresolved conflicts
Get entities changed after a given point
```

These queries are important for reliable offline synchronization.

### 9.63 Database Migration Strategy

Room database schema changes must use explicit migrations.

Example:

```text
Version 1
   ↓
Version 2
   ↓
Version 3
   ↓
...
```

Destructive migration must not be used casually because it may cause loss of locally stored financial data.

### 9.64 Migration Safety

Before changing the local schema:

- Existing financial data must be preserved.
- Synchronization metadata must be considered.
- Pending operations must remain valid.
- Migration must be tested.
- Rollout must be backward-aware where necessary.

### 9.65 Database Transaction Principle

Any operation that modifies multiple related records and must preserve a business invariant must be transactional.

Examples:

```text
Expense + ExpenseSplits
Group + GroupMember
User + Device
Settlement + SyncOperation
```

### 9.66 Database Security

Sensitive local data must be protected appropriately.

The database should not contain:

- Plain-text authentication secrets.
- Plain-text passwords.
- Private cryptographic keys without secure protection.

Where sensitive information is required, it should be stored using appropriate Android security mechanisms.

### 9.67 Database Reset Principle

A normal application restart must never reset the database.

Local data must survive:

```text
App restart
Process termination
Device restart
Temporary network loss
```

Application uninstall/reinstall behavior is a separate lifecycle concern.

### 9.68 Local Database ER Overview

```text
users
  │
  ├───────────────┐
  │               │
  ▼               ▼
devices       group_members
                  │
                  ▼
                groups
                  │
             ┌────┴─────┐
             │          │
             ▼          ▼
          expenses   settlements
             │
             ▼
       expense_splits

devices
   │
   ▼
sync_operations

sync_states
conflicts
```

### 9.69 Local Database Schema Summary

| Table | Primary Purpose |
|---|---|
| `users` | Local user profiles |
| `devices` | Device identity |
| `groups` | Group information |
| `group_members` | User-Group membership |
| `expenses` | Group expenses |
| `expense_splits` | Expense participant allocations |
| `settlements` | Recorded payments between members |
| `sync_operations` | Pending/completed synchronization changes |
| `sync_states` | Synchronization progress |
| `conflicts` | Unresolved synchronization conflicts |

### 9.70 Local Database Design Invariants

The following rules are mandatory for V1:

- Room/SQLite is the primary local persistence mechanism.
- The local database must work without network connectivity.
- Stable application-generated IDs must be used for synchronizable entities.
- Phone number and email must not be used as primary keys.
- Monetary amounts must use exact representations.
- The local schema should use integer minor currency units for stored monetary values.
- Every Expense belongs to exactly one Group.
- Every Expense has exactly one payer in V1.
- Every Expense contains valid ExpenseSplits.
- Expense Split amounts must reconcile exactly with the Expense amount.
- Every Settlement belongs to exactly one Group.
- Settlement sender and receiver must be different.
- Group membership must prevent duplicate active memberships.
- Financial records must not be blindly deleted through database cascades.
- Multi-record financial operations must be transactional.
- Local writes must not wait for network synchronization.
- Local changes must create durable synchronization operations where synchronization is required.
- Synchronization operations must have stable unique operation IDs.
- Synchronization processing must support idempotency.
- Deletions must preserve sufficient metadata for synchronization.
- Database migrations must preserve existing financial and synchronization data.
- UI state should be derived from local persisted state.
- The local database schema may differ physically from the backend schema but must remain compatible at the Domain and synchronization levels.

## 10. Backend Database Schema

### 10.1 Purpose

This section defines the MySQL database schema for the SplitSync V1 backend.

The backend database is responsible for:

- Persisting registered Users.
- Persisting Devices.
- Persisting Groups.
- Persisting Group Memberships.
- Persisting Expenses.
- Persisting Expense Splits.
- Persisting Settlements.
- Persisting synchronization operations.
- Persisting synchronization state.
- Persisting conflicts where required.
- Maintaining referential integrity.
- Supporting synchronization and conflict resolution.
- Supporting reliable and transactional financial operations.

The backend database is not required to have the exact same physical schema as the Android Room database.

The two databases must remain compatible at the Domain and Synchronization levels.

### 10.2 Database Technology

V1 backend database:

```text
MySQL
```

Persistence technology:

```text
Hibernate / JPA
```

Database migrations:

```text
Flyway or Liquibase
```

The final migration framework will be selected during backend implementation.

### 10.3 Database Design Principles

The backend database follows these principles:

- Relational integrity.
- Strong foreign-key relationships.
- Transactional financial operations.
- Stable entity identifiers.
- Synchronization-aware persistence.
- Idempotent synchronization support.
- Explicit versioning.
- Controlled deletion.
- Audit-friendly financial records.
- Indexed synchronization queries.
- No floating-point monetary storage.
- No business logic inside database triggers unless explicitly justified.

### 10.4 Backend Database Overview

The conceptual schema is:

```text
┌───────────────┐
│     users     │
└───────┬───────┘
        │
        ├─────────────────┐
        │                 │
        ▼                 ▼
┌───────────────┐ ┌───────────────┐
│    devices    │ │ group_members │
└───────┬───────┘ └───────┬───────┘
        │                 │
        │                 ▼
        │          ┌───────────────┐
        │          │    groups     │
        │          └───────┬───────┘
        │                  │
        │          ┌───────┴────────┐
        │          │                │
        │          ▼                ▼
        │   ┌───────────────┐ ┌───────────────┐
        │   │    expenses   │ │  settlements  │
        │   └───────┬───────┘ └───────────────┘
        │           │
        │           ▼
        │   ┌──────────────────┐
        │   │ expense_splits   │
        │   └──────────────────┘
        │
        ▼
┌────────────────────┐
│  sync_operations   │
└──────────┬─────────┘
           │
           ├─────────────────┐
           ▼                 ▼
┌────────────────┐   ┌────────────────┐
│  sync_states   │   │   conflicts    │
└────────────────┘   └────────────────┘
```

### 10.5 Backend Table List

The initial V1 database contains:

```text
users
devices
groups
group_members
expenses
expense_splits
settlements
sync_operations
sync_states
conflicts
```

Additional tables may be introduced if required by the final authentication or synchronization implementation.

### 10.6 Identifier Strategy

All primary entities must use stable application-generated identifiers.

Recommended identifier type:

```text
UUID
```

The exact Java/database representation will be finalized during implementation.

The identifier must remain stable across:

```text
Android
    ↓
Peer Synchronization
    ↓
Backend
```

An entity must not receive a new identity merely because it was synchronized from another device.

### 10.7 Users Table

The `users` table stores backend User profiles.

Conceptual schema:

```text
users
├── id
├── display_name
├── phone_number
├── email
├── status
├── created_at
├── updated_at
├── version
└── deleted_at
```

### 10.8 Users Table Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Stable User ID |
| `display_name` | VARCHAR | User display name |
| `phone_number` | VARCHAR | Optional phone number |
| `email` | VARCHAR | Optional email |
| `status` | VARCHAR | User status |
| `created_at` | DATETIME | Creation timestamp |
| `updated_at` | DATETIME | Last update timestamp |
| `version` | BIGINT | Entity version |
| `deleted_at` | DATETIME | Soft-delete timestamp |

### 10.9 User Constraints

The following constraints should be considered:

```text
PRIMARY KEY (id)
```

Where phone number or email is used for account identification, uniqueness rules must be defined according to the final authentication design.

Phone number and email must not be treated as the immutable primary identity of the User.

### 10.10 Device Table

The `devices` table stores registered SplitSync application instances.

Conceptual schema:

```text
devices
├── id
├── user_id
├── device_name
├── platform
├── status
├── created_at
├── updated_at
├── last_seen_at
└── version
```

### 10.11 Device Table Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Stable Device ID |
| `user_id` | UUID | Associated User |
| `device_name` | VARCHAR | Device display name |
| `platform` | VARCHAR | Client platform |
| `status` | VARCHAR | Device status |
| `created_at` | DATETIME | Creation timestamp |
| `updated_at` | DATETIME | Last update timestamp |
| `last_seen_at` | DATETIME | Last known activity |
| `version` | BIGINT | Entity version |

### 10.12 Device Foreign Key

```text
devices.user_id
        ↓
users.id
```

Constraint:

```text
FOREIGN KEY (user_id)
REFERENCES users(id)
```

A Device must belong to a valid User.

### 10.13 Device Status

Recommended statuses:

```text
ACTIVE
REVOKED
INACTIVE
```

The final state model will be defined in the Security and Synchronization sections.

### 10.14 Groups Table

The `groups` table stores Group information.

Conceptual schema:

```text
groups
├── id
├── name
├── description
├── group_type
├── created_by
├── status
├── created_at
├── updated_at
├── version
└── deleted_at
```

### 10.15 Groups Table Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Stable Group ID |
| `name` | VARCHAR | Group name |
| `description` | TEXT | Optional description |
| `group_type` | VARCHAR | Group type |
| `created_by` | UUID | User who created the Group |
| `status` | VARCHAR | Group status |
| `created_at` | DATETIME | Creation timestamp |
| `updated_at` | DATETIME | Last update timestamp |
| `version` | BIGINT | Entity version |
| `deleted_at` | DATETIME | Soft-delete timestamp |

### 10.16 Group Creator Foreign Key

```text
groups.created_by
        ↓
users.id
```

Constraint:

```text
FOREIGN KEY (created_by)
REFERENCES users(id)
```

### 10.17 Group Member Table

The `group_members` table represents User membership in a Group.

Conceptual schema:

```text
group_members
├── id
├── group_id
├── user_id
├── role
├── status
├── joined_at
├── updated_at
├── version
└── deleted_at
```

### 10.18 Group Member Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Stable GroupMember ID |
| `group_id` | UUID | Group reference |
| `user_id` | UUID | User reference |
| `role` | VARCHAR | Group role |
| `status` | VARCHAR | Membership status |
| `joined_at` | DATETIME | Membership timestamp |
| `updated_at` | DATETIME | Last update timestamp |
| `version` | BIGINT | Entity version |
| `deleted_at` | DATETIME | Removal timestamp |

### 10.19 Group Member Foreign Keys

```text
group_members.group_id
        ↓
groups.id

group_members.user_id
        ↓
users.id
```

### 10.20 Group Membership Uniqueness

A User should not have duplicate active membership in the same Group.

Conceptually:

```text
(group_id, user_id)
        ↓
Unique membership
```

The exact implementation of uniqueness together with soft deletion will be finalized during database implementation.

### 10.21 Group Roles

V1 roles:

```text
OWNER
MEMBER
```

The database should use controlled values rather than arbitrary role strings.

### 10.22 Group Membership Status

Recommended statuses:

```text
ACTIVE
PENDING
REMOVED
```

The exact transitions will be controlled by application logic.

### 10.23 Expenses Table

The `expenses` table stores Group expenses.

Conceptual schema:

```text
expenses
├── id
├── group_id
├── paid_by
├── amount_minor
├── currency
├── description
├── expense_date
├── created_at
├── updated_at
├── version
└── deleted_at
```

### 10.24 Expense Table Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Stable Expense ID |
| `group_id` | UUID | Group reference |
| `paid_by` | UUID | Payer User ID |
| `amount_minor` | BIGINT | Amount in minor currency units |
| `currency` | CHAR/VARCHAR | Currency code |
| `description` | VARCHAR/TEXT | Expense description |
| `expense_date` | DATETIME | Expense date/time |
| `created_at` | DATETIME | Creation timestamp |
| `updated_at` | DATETIME | Last update timestamp |
| `version` | BIGINT | Entity version |
| `deleted_at` | DATETIME | Soft-delete timestamp |

### 10.25 Expense Group Foreign Key

```text
expenses.group_id
        ↓
groups.id
```

### 10.26 Expense Payer Foreign Key

```text
expenses.paid_by
        ↓
users.id
```

The payer must satisfy the applicable Group membership rules.

A database foreign key alone cannot enforce all membership business rules.

Those rules must therefore also be validated by the Application/Domain Layer.

### 10.27 Monetary Representation

Monetary values must not use:

```text
FLOAT
DOUBLE
```

The V1 schema uses:

```text
BIGINT amount_minor
```

Example:

```text
₹125.50
```

may be stored as:

```text
12550
```

The currency determines the interpretation of the minor unit.

### 10.28 Expense Currency

Every Expense must explicitly store:

```text
currency
```

Example:

```text
INR
USD
EUR
```

The currency must not be inferred from:

- User locale.
- Device locale.
- Group name.
- Phone country code.

### 10.29 Expense Splits Table

The `expense_splits` table stores the participant allocation for an Expense.

Conceptual schema:

```text
expense_splits
├── id
├── expense_id
├── user_id
├── split_method
├── amount_minor
├── percentage
├── shares
├── created_at
├── updated_at
├── version
└── deleted_at
```

### 10.30 Expense Split Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Stable ExpenseSplit ID |
| `expense_id` | UUID | Expense reference |
| `user_id` | UUID | Participant User |
| `split_method` | VARCHAR | Split method |
| `amount_minor` | BIGINT | Final share amount |
| `percentage` | DECIMAL | Percentage where applicable |
| `shares` | DECIMAL | Shares where applicable |
| `created_at` | DATETIME | Creation timestamp |
| `updated_at` | DATETIME | Last update timestamp |
| `version` | BIGINT | Entity version |
| `deleted_at` | DATETIME | Soft-delete timestamp |

### 10.31 Expense Split Foreign Keys

```text
expense_splits.expense_id
        ↓
expenses.id

expense_splits.user_id
        ↓
users.id
```

### 10.32 Expense Split Methods

V1 supports:

```text
EQUAL
EXACT
PERCENTAGE
SHARES
```

The database stores the resulting monetary allocation in:

```text
amount_minor
```

The additional fields are retained where required to represent the original split configuration.

### 10.33 Expense Split Invariant

For every active Expense:

```text
SUM(expense_splits.amount_minor)
=
expenses.amount_minor
```

This invariant must be enforced by the Application/Domain Layer.

Database-level constraints may be added where practical, but cross-row financial validation should not rely exclusively on database constraints.

### 10.34 Percentage Invariant

For percentage-based splits:

```text
SUM(percentage)
=
100
```

The exact precision/scale will be finalized with the implementation.

### 10.35 Shares Invariant

For share-based splits:

```text
SUM(shares) > 0
```

The calculated split amounts must reconcile exactly with the Expense amount.

### 10.36 Settlements Table

The `settlements` table stores recorded payments between Group members.

Conceptual schema:

```text
settlements
├── id
├── group_id
├── from_user_id
├── to_user_id
├── amount_minor
├── currency
├── settlement_date
├── description
├── created_at
├── updated_at
├── version
└── deleted_at
```

### 10.37 Settlement Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Stable Settlement ID |
| `group_id` | UUID | Group reference |
| `from_user_id` | UUID | Sender User |
| `to_user_id` | UUID | Receiver User |
| `amount_minor` | BIGINT | Settlement amount |
| `currency` | CHAR/VARCHAR | Currency code |
| `settlement_date` | DATETIME | Settlement date/time |
| `description` | VARCHAR/TEXT | Optional description |
| `created_at` | DATETIME | Creation timestamp |
| `updated_at` | DATETIME | Last update timestamp |
| `version` | BIGINT | Entity version |
| `deleted_at` | DATETIME | Soft-delete timestamp |

### 10.38 Settlement Foreign Keys

```text
settlements.group_id
        ↓
groups.id

settlements.from_user_id
        ↓
users.id

settlements.to_user_id
        ↓
users.id
```

### 10.39 Settlement Constraints

The following must be enforced at the Domain/Application level:

```text
amount_minor > 0

from_user_id != to_user_id
```

Both Users must satisfy the applicable Group membership rules.

### 10.40 Synchronization Operations Table

The `sync_operations` table stores synchronization operations processed by the backend.

Conceptual schema:

```text
sync_operations
├── id
├── device_id
├── entity_type
├── entity_id
├── operation_type
├── payload
├── entity_version
├── created_at
├── processed_at
├── status
├── error_code
└── error_message
```

### 10.41 Sync Operation Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Unique operation ID |
| `device_id` | UUID | Source Device |
| `entity_type` | VARCHAR | Entity type |
| `entity_id` | UUID | Entity identifier |
| `operation_type` | VARCHAR | CREATE/UPDATE/DELETE |
| `payload` | JSON/TEXT | Operation payload |
| `entity_version` | BIGINT | Entity version |
| `created_at` | DATETIME | Operation creation timestamp |
| `processed_at` | DATETIME | Processing timestamp |
| `status` | VARCHAR | Operation status |
| `error_code` | VARCHAR | Error code |
| `error_message` | TEXT | Diagnostic information |

The exact payload strategy will be finalized in the Synchronization Design.

### 10.42 Sync Operation Idempotency

The `sync_operations.id` must be unique.

The backend must be able to perform:

```text
Receive Operation
       ↓
Check Operation ID
       ↓
Already Processed?
       │
   ┌───┴───┐
   │       │
  Yes      No
   │       │
 Ignore    Process
```

This prevents duplicate application of the same synchronization operation.

### 10.43 Sync Operation Status

Recommended statuses:

```text
RECEIVED
PROCESSING
COMPLETED
FAILED
CONFLICT
```

The exact state machine will be defined later.

### 10.44 Sync States Table

The `sync_states` table stores server-side synchronization progress.

Conceptual schema:

```text
sync_states
├── id
├── device_id
├── scope_type
├── scope_id
├── channel
├── cursor
├── last_successful_sync_at
├── last_attempt_at
├── status
└── error_code
```

### 10.45 Sync State Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Sync state ID |
| `device_id` | UUID | Device |
| `scope_type` | VARCHAR | Scope type |
| `scope_id` | UUID/TEXT | Scope identifier |
| `channel` | VARCHAR | Synchronization channel |
| `cursor` | VARCHAR/TEXT | Last processed cursor |
| `last_successful_sync_at` | DATETIME | Last successful synchronization |
| `last_attempt_at` | DATETIME | Last synchronization attempt |
| `status` | VARCHAR | Sync status |
| `error_code` | VARCHAR | Last error |

### 10.46 Synchronization Channels

V1 may use:

```text
BACKEND
PEER
```

The backend database primarily manages backend synchronization.

Peer synchronization may maintain its state locally on Android devices.

### 10.47 Conflicts Table

The `conflicts` table stores synchronization conflicts that require tracking or resolution.

Conceptual schema:

```text
conflicts
├── id
├── entity_type
├── entity_id
├── device_id
├── local_version
├── remote_version
├── local_payload
├── remote_payload
├── detected_at
├── status
├── resolution
└── resolved_at
```

### 10.48 Conflict Fields

| Column | Type | Description |
|---|---|---|
| `id` | UUID | Conflict ID |
| `entity_type` | VARCHAR | Conflicting entity |
| `entity_id` | UUID | Entity ID |
| `device_id` | UUID | Device involved |
| `local_version` | BIGINT | Local version |
| `remote_version` | BIGINT | Remote version |
| `local_payload` | JSON/TEXT | Local state |
| `remote_payload` | JSON/TEXT | Remote state |
| `detected_at` | DATETIME | Detection timestamp |
| `status` | VARCHAR | Conflict status |
| `resolution` | VARCHAR/TEXT | Resolution |
| `resolved_at` | DATETIME | Resolution timestamp |

### 10.49 Conflict Status

Recommended statuses:

```text
OPEN
RESOLVED
IGNORED
```

The final conflict model will be defined in Conflict Resolution Design.

### 10.50 Foreign Key Relationship Overview

The primary backend relationships are:

```text
devices.user_id
        → users.id

groups.created_by
        → users.id

group_members.group_id
        → groups.id

group_members.user_id
        → users.id

expenses.group_id
        → groups.id

expenses.paid_by
        → users.id

expense_splits.expense_id
        → expenses.id

expense_splits.user_id
        → users.id

settlements.group_id
        → groups.id

settlements.from_user_id
        → users.id

settlements.to_user_id
        → users.id

sync_operations.device_id
        → devices.id

sync_states.device_id
        → devices.id

conflicts.device_id
        → devices.id
```

### 10.51 Referential Integrity

The database must enforce core referential integrity through foreign keys.

For example:

```text
Expense
    ↓
must reference an existing Group

ExpenseSplit
    ↓
must reference an existing Expense

Settlement
    ↓
must reference an existing Group

GroupMember
    ↓
must reference an existing User and Group
```

### 10.52 Business Integrity vs Database Integrity

Database foreign keys cannot enforce every business rule.

For example:

```text
Expense.paid_by
    → users.id
```

does not guarantee:

```text
User is an active member of Expense.group_id
```

Therefore validation must happen in the Domain/Application Layer.

The database provides structural integrity.

The Application/Domain Layer provides business integrity.

### 10.53 Financial Transaction Boundaries

Creating an Expense must be transactional.

Conceptually:

```text
BEGIN TRANSACTION

Create Expense
      ↓
Create Expense Splits
      ↓
Validate Financial Invariants
      ↓
Create Synchronization Metadata
      ↓
COMMIT
```

Failure:

```text
ROLLBACK
```

No partial Expense must remain.

### 10.54 Settlement Transaction

Settlement creation must be atomic:

```text
BEGIN TRANSACTION

Validate Settlement
      ↓
Create Settlement
      ↓
Create Synchronization Metadata
      ↓
COMMIT
```

### 10.55 Group Creation Transaction

Group creation may require:

```text
BEGIN TRANSACTION

Create Group
      ↓
Create Owner GroupMember
      ↓
Create Synchronization Metadata
      ↓
COMMIT
```

### 10.56 User and Device Transaction

Initial backend registration may involve:

```text
BEGIN TRANSACTION

Create User
      ↓
Create Device
      ↓
Associate Device
      ↓
COMMIT
```

The exact registration workflow depends on the final authentication design.

### 10.57 Deletion Strategy

Financial records must not be physically deleted blindly.

The preferred V1 strategy is to use controlled deletion with:

```text
deleted_at
version
synchronization metadata
```

This allows deletions to propagate to other devices.

### 10.58 Tombstone Requirement

If an entity is physically removed immediately:

```text
Device A
    ↓
Delete Expense
    ↓
Row disappears
```

another device may never know that the Expense was deleted.

Therefore synchronized deletions require a tombstone or equivalent deletion metadata.

Example:

```text
Expense E123
deleted_at = timestamp
version = 8
```

The exact retention strategy will be defined in the Synchronization Design.

### 10.59 Cascading Delete Policy

Blind cascading deletion should not be used for financial records.

For example:

```text
DELETE Group
    ↓
DELETE Expenses
    ↓
DELETE ExpenseSplits
```

must not happen automatically without an explicit application-level decision.

The final deletion workflow must preserve synchronization correctness and historical integrity.

### 10.60 Database Indexing

Indexes must be created according to actual access patterns.

Important indexes include:

```text
devices.user_id

group_members.group_id
group_members.user_id

expenses.group_id
expenses.paid_by
expenses.updated_at

expense_splits.expense_id
expense_splits.user_id

settlements.group_id
settlements.from_user_id
settlements.to_user_id

sync_operations.device_id
sync_operations.status
sync_operations.entity_id
sync_operations.created_at

sync_states.device_id
sync_states.scope_id
sync_states.channel

conflicts.entity_id
conflicts.status
```

### 10.61 Group Query Optimization

Common queries include:

```text
Get Groups for User
Get Members of Group
Get Expenses for Group
Get Settlements for Group
Get Group Changes
```

Therefore the database must efficiently query through:

```text
group_members.user_id
group_members.group_id
expenses.group_id
settlements.group_id
```

### 10.62 Synchronization Query Optimization

Synchronization must efficiently support:

```text
Get Pending Operations
Get Operations for Device
Get Changes After Cursor
Get Entity by ID
Get Conflicts
Get Changes for Group
```

Indexes must be created accordingly.

### 10.63 Versioning

Synchronizable entities contain:

```text
version BIGINT
```

Example:

```text
Expense E123
version = 1
```

After a valid update:

```text
version = 2
```

The exact version-generation strategy will be defined in the Synchronization Design.

### 10.64 Version and Concurrency

Versions are used to detect concurrent modifications.

Example:

```text
Device A:
Expense E1 → Version 5

Device B:
Expense E1 → Version 5
```

Both modify the Expense offline.

Later:

```text
Device A → Version 6
Device B → Version 6
```

The backend must detect that both changes originated from the same previous version and apply the defined conflict-resolution strategy.

### 10.65 Backend Database as Source of Server State

The backend database represents the server-side synchronized state.

However, the server must not blindly overwrite a client's local state.

Synchronization processing must consider:

```text
Operation ID
Entity ID
Entity Version
Device ID
Authorization
Conflict Rules
```

### 10.66 Database Transactions and Synchronization

Synchronization operations that modify multiple related records must be transactional.

Example:

```text
Receive Expense Operation
        ↓
Validate Operation
        ↓
Validate Group Membership
        ↓
Validate Expense
        ↓
Persist Expense
        ↓
Persist ExpenseSplits
        ↓
Persist Sync Metadata
        ↓
COMMIT
```

If any required step fails:

```text
ROLLBACK
```

### 10.67 Database Migration Strategy

All production schema changes must use controlled migrations.

Example:

```text
V1
 ↓
V2
 ↓
V3
 ↓
V4
```

Hibernate's automatic schema generation must not be used as the sole production schema migration mechanism.

### 10.68 Migration Requirements

Every migration must:

- Be versioned.
- Be repeatable/testable.
- Preserve existing financial data.
- Preserve synchronization metadata.
- Be tested against a representative database.
- Avoid unnecessary destructive changes.

### 10.69 Database Environment Separation

The backend should support separate database configurations for:

```text
Development
Test
Production
```

Each environment should use its own:

```text
Database
Credentials
Connection Pool
Migration State
```

Production credentials must never be committed to Git.

### 10.70 Database Security

Database credentials must be provided through secure configuration.

The source code must not contain:

```text
MySQL username
MySQL password
Production connection credentials
Encryption keys
JWT secrets
```

Sensitive configuration should be supplied through:

```text
Environment Variables
Secret Management
Deployment Configuration
```

### 10.71 Database Connection Management

The backend should use a connection pool.

The exact connection-pool configuration will be determined during backend implementation.

The application must:

- Reuse database connections.
- Configure maximum pool size appropriately.
- Configure connection timeout.
- Monitor connection failures.
- Avoid connection leaks.

### 10.72 Hibernate/JPA Mapping Principle

JPA entities should map to database tables explicitly and consistently.

Conceptually:

```text
UserEntity
    ↕
users

GroupEntity
    ↕
groups

ExpenseEntity
    ↕
expenses
```

JPA entities must remain persistence models.

They must not be exposed directly through REST APIs.

### 10.73 Entity and Domain Separation

The backend must maintain:

```text
Domain Model
        ↕
Persistence Entity
        ↕
MySQL Table
```

A JPA Entity must not automatically become the API Response DTO.

### 10.74 Lazy Loading

Relationships should use appropriate fetch strategies.

Large collections such as:

```text
Group → Expenses
Group → Members
Expense → ExpenseSplits
```

must not be eagerly loaded everywhere.

The exact fetch strategy will be finalized based on actual use cases and query requirements.

### 10.75 N+1 Query Prevention

The backend must avoid unnecessary N+1 queries.

Examples:

```text
Get Group
   ↓
Get Members
   ↓
Get Expenses
   ↓
Get Splits
```

should be implemented with intentional query strategies rather than accidental lazy-loading chains.

### 10.76 Pagination

Large collections should support pagination where appropriate.

Potentially paginated resources:

```text
Expenses
Settlements
Synchronization Changes
Synchronization Operations
```

The Group member list may initially remain unpaginated for small V1 Groups.

### 10.77 Database Consistency Model

The backend database provides transactional consistency for operations executed on the server.

However, the overall SplitSync system is eventually consistent across offline devices.

Therefore:

```text
Single Backend Transaction
        ↓
Strong Transactional Consistency

Multiple Offline Devices
        ↓
Eventual Synchronization Consistency
```

This distinction is fundamental to the architecture.

### 10.78 Local vs Backend Database

The physical schemas may differ:

```text
Android Room
      ≠
MySQL
```

but both represent compatible Domain concepts:

```text
User
Device
Group
GroupMember
Expense
ExpenseSplit
Settlement
Synchronization
```

The backend database may contain additional infrastructure metadata required for:

- Authentication.
- Authorization.
- Synchronization.
- Server-side auditing.
- Operational monitoring.

### 10.79 Backend Database ER Diagram

```text
┌──────────────────┐
│      users       │
├──────────────────┤
│ id PK            │
│ display_name     │
│ phone_number     │
│ email            │
│ status           │
│ created_at       │
│ updated_at       │
│ version          │
│ deleted_at       │
└───────┬──────────┘
        │
        ├─────────────────────┐
        │                     │
        │                     │
        ▼                     ▼
┌──────────────────┐   ┌──────────────────────┐
│     devices      │   │    group_members     │
├──────────────────┤   ├──────────────────────┤
│ id PK            │   │ id PK                │
│ user_id FK       │   │ group_id FK          │
│ device_name      │   │ user_id FK           │
│ platform         │   │ role                 │
│ status           │   │ status               │
│ created_at       │   │ joined_at            │
│ updated_at       │   │ updated_at           │
│ last_seen_at     │   │ version              │
│ version          │   │ deleted_at           │
└──────────────────┘   └──────────┬───────────┘
                                  │
                                  │
                                  ▼
                         ┌──────────────────┐
                         │      groups      │
                         ├──────────────────┤
                         │ id PK            │
                         │ name              │
                         │ description       │
                         │ group_type        │
                         │ created_by FK     │
                         │ status            │
                         │ created_at        │
                         │ updated_at        │
                         │ version           │
                         │ deleted_at        │
                         └────────┬─────────┬─┘
                                  │         │
                                  │         │
                         ┌────────▼───┐ ┌───▼─────────────┐
                         │  expenses  │ │   settlements   │
                         ├────────────┤ ├─────────────────┤
                         │ id PK      │ │ id PK           │
                         │ group_id   │ │ group_id        │
                         │ paid_by    │ │ from_user_id   │
                         │ amount     │ │ to_user_id     │
                         │ currency   │ │ amount          │
                         │ description│ │ currency        │
                         │ expense_date││ settlement_date│
                         │ version    │ │ version         │
                         │ deleted_at │ │ deleted_at      │
                         └─────┬──────┘ └─────────────────┘
                               │
                               │
                               ▼
                      ┌────────────────────┐
                      │  expense_splits   │
                      ├────────────────────┤
                      │ id PK              │
                      │ expense_id FK      │
                      │ user_id FK         │
                      │ split_method       │
                      │ amount_minor       │
                      │ percentage         │
                      │ shares             │
                      │ version            │
                      │ deleted_at         │
                      └────────────────────┘


┌──────────────────┐
│     devices      │
└────────┬─────────┘
         │
         ▼
┌────────────────────────┐
│    sync_operations     │
├────────────────────────┤
│ id PK                  │
│ device_id FK           │
│ entity_type            │
│ entity_id              │
│ operation_type         │
│ payload                │
│ entity_version         │
│ status                 │
│ created_at             │
│ processed_at           │
└────────────────────────┘

┌────────────────────────┐
│      sync_states       │
├────────────────────────┤
│ id PK                  │
│ device_id FK           │
│ scope_type             │
│ scope_id               │
│ channel                │
│ cursor                 │
│ status                 │
└────────────────────────┘

┌────────────────────────┐
│       conflicts        │
├────────────────────────┤
│ id PK                  │
│ entity_type            │
│ entity_id              │
│ device_id FK           │
│ local_version          │
│ remote_version         │
│ local_payload          │
│ remote_payload         │
│ status                 │
│ resolution             │
└────────────────────────┘
```

### 10.80 Database Table Summary

| Table | Purpose |
|---|---|
| `users` | Backend User profiles |
| `devices` | Registered application devices |
| `groups` | Group information |
| `group_members` | User-Group relationships |
| `expenses` | Group expenses |
| `expense_splits` | Expense allocations |
| `settlements` | Recorded settlements |
| `sync_operations` | Synchronization operations |
| `sync_states` | Synchronization progress |
| `conflicts` | Synchronization conflicts |

### 10.81 Backend Database Invariants

The following rules are mandatory for V1:

- MySQL is the primary backend database.
- Hibernate/JPA is used for persistence.
- Stable application-generated identifiers are used for synchronizable entities.
- UUID is the preferred V1 identifier strategy.
- Phone number and email are not primary entity identifiers.
- Monetary values must not use FLOAT or DOUBLE.
- Monetary values should be stored as integer minor units.
- Every Expense belongs to exactly one Group.
- Every Expense has exactly one payer in V1.
- Every Expense has valid ExpenseSplits.
- Expense Split amounts must equal the Expense amount.
- Every Settlement belongs to exactly one Group.
- Settlement sender and receiver must be different.
- Group membership must not contain duplicate active memberships.
- Foreign keys must enforce structural referential integrity.
- Domain/Application validation must enforce business-level referential integrity.
- Financial operations must be transactional.
- Synchronization operations must be idempotent.
- Synchronization entities must maintain stable versions.
- Deletions must preserve sufficient metadata for synchronization.
- Financial records must not be blindly removed through cascading deletes.
- Database schema changes must use controlled migrations.
- Production database credentials must never be committed to source control.
- JPA entities must remain separate from REST DTOs.
- Large relationships must not be eagerly loaded without justification.
- N+1 query problems must be explicitly avoided.
- Synchronization queries must be indexed appropriately.
- The backend database provides transactional consistency for server-side operations.
- Cross-device consistency remains eventually consistent because SplitSync is offline-first.

## 11. Synchronization Data Model

### 11.1 Purpose

This section defines the data model used by SplitSync V1 to synchronize changes between:

- Android local databases.
- Backend server.
- Nearby SplitSync devices through local peer-to-peer communication.

The synchronization model must support the core requirement:

> Local changes are immediately persisted locally and synchronized later when a suitable communication channel becomes available.

The synchronization system must work independently of whether the available channel is:

```text
Internet
    ↓
Backend Synchronization

or

Local Wi-Fi / Hotspot
    ↓
Peer-to-Peer Synchronization
```

The underlying Domain entities remain the same.

Only the synchronization transport changes.

### 11.2 Synchronization Principles

The synchronization data model follows these principles:

- Local-first.
- Eventually consistent.
- Stable entity identity.
- Operation-based synchronization.
- Idempotent operations.
- Version-aware changes.
- Conflict detection.
- Deterministic conflict resolution.
- Retry-safe processing.
- No data loss because of temporary connectivity loss.
- Backend synchronization and peer synchronization use compatible operations.
- Financial data must never be duplicated because the same operation is received multiple times.

### 11.3 Synchronization Architecture Overview

The conceptual flow is:

```text
User Action
     ↓
Domain Validation
     ↓
Local Database Transaction
     ↓
Local Entity Updated
     ↓
Sync Operation Created
     ↓
Pending Queue
     │
     ├───────────────┐
     │               │
     ▼               ▼
 Backend Channel   Peer Channel
     │               │
     ▼               ▼
 Server           Nearby Device
     │               │
     └───────┬───────┘
             ▼
       Remote Changes
             ↓
       Local Validation
             ↓
       Conflict Detection
             ↓
       Apply Local Change
```

### 11.4 Synchronization Unit

The basic synchronization unit is a:

```text
SyncOperation
```

A SyncOperation represents one logical change made to a synchronizable entity.

Examples:

```text
Create Group
Update Group
Add Group Member
Create Expense
Update Expense
Delete Expense
Create Settlement
```

### 11.5 Synchronizable Entities

V1 synchronization supports:

```text
USER
DEVICE
GROUP
GROUP_MEMBER
EXPENSE
EXPENSE_SPLIT
SETTLEMENT
```

The exact synchronization scope of User and Device records may be restricted according to the final authentication/security model.

Financial synchronization primarily concerns:

```text
GROUP
GROUP_MEMBER
EXPENSE
EXPENSE_SPLIT
SETTLEMENT
```

### 11.6 SyncOperation

Conceptual model:

```text
SyncOperation
├── operationId
├── deviceId
├── entityType
├── entityId
├── operationType
├── entityVersion
├── baseVersion
├── payload
├── createdAt
├── status
└── metadata
```

### 11.7 Operation ID

Every SyncOperation must have a globally unique:

```text
operationId
```

The Operation ID is generated by the device that creates the change.

Example:

```text
operationId = 8f6c...
```

The same Operation ID must be preserved when the operation is transmitted through:

```text
Device → Backend
```

or:

```text
Device A → Device B
```

### 11.8 Operation ID and Idempotency

The Operation ID is the primary mechanism for preventing duplicate operation processing.

Example:

```text
Device A
    │
    └── Operation O123
            │
            ▼
        Backend
            │
            ├── First receive → Process
            │
            └── Second receive → Ignore duplicate
```

The same operation must never create duplicate financial records.

### 11.9 Device ID

Every SyncOperation contains:

```text
deviceId
```

The Device ID identifies the device that originally created the operation.

This allows the system to determine:

- Operation source.
- Change attribution.
- Device-specific synchronization state.
- Conflict information.
- Debugging information.

### 11.10 Entity Type

Every SyncOperation identifies the affected entity through:

```text
entityType
```

Supported V1 values:

```text
USER
DEVICE
GROUP
GROUP_MEMBER
EXPENSE
EXPENSE_SPLIT
SETTLEMENT
```

### 11.11 Entity ID

Every SyncOperation contains:

```text
entityId
```

This identifies the logical entity being modified.

Example:

```text
entityType = EXPENSE
entityId   = E123
```

means:

```text
Expense E123
```

The Entity ID remains unchanged across all synchronization channels.

### 11.12 Operation Type

V1 supports:

```text
CREATE
UPDATE
DELETE
```

Conceptually:

```text
CREATE
    ↓
New entity

UPDATE
    ↓
Existing entity changed

DELETE
    ↓
Entity removed/deactivated
```

### 11.13 Entity Version

Each synchronizable entity has a logical:

```text
version
```

Example:

```text
Expense E123
Version = 5
```

After an update:

```text
Expense E123
Version = 6
```

The version is used to identify the state from which a change was generated.

### 11.14 Base Version

Every UPDATE operation should contain the version from which the change was made:

```text
baseVersion
```

Example:

```text
Current local Expense:
Version = 5

User edits Expense

SyncOperation:
baseVersion   = 5
entityVersion = 6
```

This allows the receiving system to detect concurrent modifications.

### 11.15 Version Example

Suppose:

```text
Server Expense E1
Version = 5
```

Device A receives Version 5.

Device B also receives Version 5.

Both devices work offline.

Device A creates:

```text
baseVersion   = 5
entityVersion = 6
```

Device B creates:

```text
baseVersion   = 5
entityVersion = 6
```

When both changes reach the same synchronization authority, the system can detect that both originated from Version 5.

This is a conflict candidate.

### 11.16 Version Ownership

The exact version allocation mechanism must be controlled by the synchronization protocol.

The system must not assume that:

```text
Device A Version 6
```

and:

```text
Device B Version 6
```

represent the same state.

Version numbers identify an entity state lineage, not globally unique operations.

The Operation ID remains globally unique.

### 11.17 Payload

The SyncOperation contains the data required to apply the change.

Conceptually:

```text
payload
```

may contain a serialized representation of the entity state or operation-specific data.

Example:

```json
{
  "id": "E123",
  "groupId": "G100",
  "paidBy": "U10",
  "amountMinor": 125000,
  "currency": "INR",
  "description": "Hotel",
  "expenseDate": "2026-08-25T10:00:00Z"
}
```

The exact serialization format and schema will be finalized in the API Design.

### 11.18 Full-State vs Partial Update

For V1, synchronization should prefer transmitting a complete entity state for CREATE and UPDATE operations where practical.

Conceptually:

```text
UPDATE Expense E123

Send:

Current Expense State
+
Version Metadata
```

rather than:

```text
Change description:
"amount += 500"
```

This makes operations easier to validate and replay.

The exact PATCH/partial-update strategy will be finalized before implementation.

### 11.19 Delete Payload

DELETE operations must retain sufficient metadata for synchronization.

Conceptually:

```text
Delete Operation
├── operationId
├── entityType
├── entityId
├── baseVersion
├── entityVersion
└── deletedAt
```

The system must not depend solely on the physical absence of a database row to communicate deletion.

### 11.20 Sync Operation Status

Local synchronization operations may use:

```text
PENDING
IN_PROGRESS
COMPLETED
FAILED
CONFLICT
```

Meaning:

```text
PENDING
    ↓
Waiting for synchronization

IN_PROGRESS
    ↓
Currently being transmitted/processed

COMPLETED
    ↓
Successfully synchronized

FAILED
    ↓
Synchronization failed and may be retried

CONFLICT
    ↓
Requires conflict handling
```

### 11.21 Operation Lifecycle

The normal lifecycle is:

```text
Local Change
    ↓
PENDING
    ↓
IN_PROGRESS
    ↓
COMPLETED
```

Failure:

```text
IN_PROGRESS
    ↓
FAILED
    ↓
PENDING
    ↓
Retry
```

Conflict:

```text
IN_PROGRESS
    ↓
CONFLICT
    ↓
Conflict Resolution
    ↓
Resolved
```

### 11.22 Local Write and Sync Operation Atomicity

A local Domain change and its SyncOperation must be created in the same local database transaction.

Example:

```text
BEGIN TRANSACTION

Create Expense
        ↓
Create Expense Splits
        ↓
Create SyncOperation
        ↓
COMMIT
```

This guarantees:

```text
Local state exists
        ↔
Synchronization operation exists
```

If the transaction fails:

```text
ROLLBACK
```

No orphan synchronization operation should remain.

### 11.23 Why the Sync Queue Is Local

The local SyncOperation queue allows the application to survive:

```text
No Internet
No Wi-Fi
No Mobile Data
App Restart
Process Death
Device Restart
Temporary Backend Failure
```

Example:

```text
User creates expense
        ↓
Local DB saved
        ↓
SyncOperation = PENDING
        ↓
Internet unavailable
        ↓
App continues working
```

When connectivity returns:

```text
Connectivity Available
        ↓
Process Pending Operations
```

### 11.24 Synchronization Channels

V1 supports two conceptual synchronization channels:

```text
BACKEND
PEER
```

### 11.25 Backend Synchronization Channel

The Backend channel is:

```text
Android
   ↓
Internet
   ↓
Spring Boot Backend
   ↓
MySQL
```

It is the primary synchronization mechanism when internet connectivity is available.

### 11.26 Peer Synchronization Channel

The Peer channel is:

```text
Device A
   ⇅
Local Wi-Fi / Hotspot
   ⇅
Device B
```

The peer channel does not require internet access.

Peer synchronization must use the same Domain and synchronization concepts.

### 11.27 Transport Independence

The SyncOperation model must not depend on the transport.

The same logical operation can travel through:

```text
REST
```

or:

```text
Local Network
```

Example:

```text
SyncOperation O123
       │
       ├── REST → Backend
       │
       └── Local Protocol → Peer
```

The operation remains:

```text
O123
```

### 11.28 Sync Scope

Synchronization is primarily Group-oriented for shared financial data.

Conceptually:

```text
Group G1
 │
 ├── Members
 ├── Expenses
 ├── Expense Splits
 └── Settlements
```

A device should synchronize only the Groups and entities for which it is authorized.

### 11.29 Group Synchronization Identity

A Group is identified by:

```text
groupId
```

All Group-owned financial entities can be associated with their Group through their relationships.

Example:

```text
Expense E1
    ↓
Group G1
```

This allows synchronization authorization to be evaluated at the Group boundary.

### 11.30 Synchronization Scope Example

Suppose a User belongs to:

```text
Group A
Group B
Group C
```

The device may have synchronization state such as:

```text
Group A → synchronized
Group B → pending changes
Group C → offline only
```

The synchronization system must not require all Groups to synchronize together.

### 11.31 Sync Cursor

A synchronization cursor represents the client's progress through a stream of remote changes.

Conceptually:

```text
cursor = C12345
```

The client can request:

```text
Give me changes after C12345
```

The exact cursor implementation will be finalized in the API/Synchronization Protocol section.

### 11.32 Pull Synchronization

Pull synchronization retrieves remote changes.

Conceptually:

```text
Client
  ↓
Send Cursor
  ↓
Backend
  ↓
Find Changes After Cursor
  ↓
Return Changes
  ↓
Client Validates
  ↓
Client Applies Changes
  ↓
Update Cursor
```

### 11.33 Push Synchronization

Push synchronization sends local changes.

Conceptually:

```text
Client
  ↓
Select Pending Operations
  ↓
Send Operations
  ↓
Backend
  ↓
Validate
  ↓
Apply / Reject / Conflict
  ↓
Return Result
```

### 11.34 Push Result

Each operation should receive a processing result.

Conceptually:

```text
OperationResult
├── operationId
├── status
├── entityId
├── serverVersion
├── conflict
└── error
```

Possible statuses:

```text
APPLIED
ALREADY_APPLIED
REJECTED
CONFLICT
```

### 11.35 Already Applied

If the backend receives an Operation ID that has already been processed:

```text
operationId = O123
```

the backend should return:

```text
ALREADY_APPLIED
```

rather than creating another entity or applying the financial change again.

### 11.36 Rejected Operation

An operation may be rejected because of:

```text
Invalid payload
Unauthorized user/device
Invalid Group membership
Invalid financial data
Invalid entity relationship
Unsupported operation
```

The client must not blindly retry permanent validation failures.

### 11.37 Retryable Failure

Temporary failures include:

```text
Network timeout
Backend unavailable
Database temporarily unavailable
Rate limiting
Temporary service failure
```

These should be retried using a controlled retry strategy.

### 11.38 Retry Strategy

The local synchronization queue should support:

```text
retry_count
last_attempt_at
next_retry_at
```

A future implementation may use exponential backoff:

```text
1st retry → short delay
2nd retry → longer delay
3rd retry → longer delay
...
```

The exact retry policy will be finalized during implementation.

### 11.39 Operation Ordering

Some operations depend on other operations.

Example:

```text
Create Group
       ↓
Add Group Member
       ↓
Create Expense
       ↓
Create Expense Split
```

The synchronization engine must respect dependencies.

Operations must not be applied in an order that violates referential integrity.

### 11.40 Dependency Graph

Conceptually:

```text
User
 ↓
Group
 ↓
GroupMember
 ↓
Expense
 ↓
ExpenseSplit
```

Settlement depends on:

```text
Group
+
FromUser
+
ToUser
```

### 11.41 Out-of-Order Operations

If an operation arrives before its dependency:

```text
ExpenseSplit S1
       ↓
Expense E1 not available
```

the receiving system should not create invalid data.

Possible behavior:

```text
Pending Dependency
       ↓
Wait for Expense E1
       ↓
Apply S1
```

The exact pending-dependency mechanism will be finalized during synchronization implementation.

### 11.42 Duplicate Entity Prevention

Stable Entity IDs prevent duplicate entities.

Example:

```text
Expense E123
```

received twice:

```text
First receive
    ↓
Create E123

Second receive
    ↓
Find E123
    ↓
Process version/operation
```

The system must never blindly create:

```text
E124
```

because the same logical Expense arrived from another device.

### 11.43 Duplicate Operation Prevention

Entity ID alone is not sufficient for idempotency.

The system must also track:

```text
operationId
```

because multiple legitimate operations may affect the same entity.

Example:

```text
Expense E123
    ↓
Operation O1 → Version 1
    ↓
Operation O2 → Version 2
```

Both operations are valid but have different Operation IDs.

### 11.44 Operation History

The backend may retain processed SyncOperations to support:

- Idempotency.
- Debugging.
- Conflict analysis.
- Synchronization recovery.
- Auditability.

Retention duration is a deployment/storage decision.

### 11.45 Sync State Model

A device's synchronization state can be represented as:

```text
SyncState
├── deviceId
├── scopeType
├── scopeId
├── channel
├── cursor
├── status
├── lastSuccessfulSyncAt
└── lastAttemptAt
```

Example:

```text
Device D1
Group G1
Channel BACKEND
Cursor C100
Status SYNCED
```

### 11.46 Sync State vs Sync Operation

These are different concepts.

```text
SyncOperation
    ↓
Represents a specific change

SyncState
    ↓
Represents synchronization progress
```

Example:

```text
Operation O123
    = Create Expense E1

SyncState
    = Client has processed changes through Cursor C500
```

### 11.47 Conflict Data Model

A conflict represents concurrent changes that cannot safely be merged automatically.

Conceptual model:

```text
Conflict
├── conflictId
├── entityType
├── entityId
├── localVersion
├── remoteVersion
├── localPayload
├── remotePayload
├── detectedAt
├── status
└── resolution
```

### 11.48 Conflict Example

Initial state:

```text
Expense E1
Version = 5
Amount = ₹1000
```

Device A changes:

```text
Amount = ₹1200
baseVersion = 5
```

Device B changes:

```text
Amount = ₹1500
baseVersion = 5
```

Both changes are based on the same previous state.

The synchronization system must detect this as a concurrent update.

### 11.49 Conflict Classification

Conflicts may be classified as:

```text
AUTO_RESOLVABLE
MANUAL_REVIEW
INVALID_CHANGE
```

The exact entity-specific rules will be defined in Conflict Resolution.

### 11.50 Deterministic Resolution

Where automatic resolution is possible, every device must reach the same result.

For example:

```text
Same Inputs
     ↓
Same Resolution Rule
     ↓
Same Final State
```

The system must not depend on:

```text
Device-specific timing
Random selection
UI order
```

### 11.51 Financial Conflict Principle

Financial records require stricter conflict handling than ordinary metadata.

A conflict must never result in:

```text
Duplicate Expense
```

or:

```text
Incorrect Expense Amount
```

or:

```text
Incorrect Settlement
```

without explicit validation.

### 11.52 Expense and ExpenseSplit Synchronization

Expense and its ExpenseSplits are logically related.

A valid synchronized Expense must eventually have a consistent set of Splits.

Conceptually:

```text
Expense Operation
        ↓
Expense State
        ↓
ExpenseSplit Operations
        ↓
Validate:
SUM(Splits) = Expense Amount
```

The exact atomic synchronization strategy will be finalized before implementation.

### 11.53 Group Membership Synchronization

Group membership changes must be synchronized before dependent financial operations when required.

Example:

```text
Add Rahul to Group G1
        ↓
Expense E1 includes Rahul
```

If the membership operation has not yet reached a peer/backend, the Expense operation may require dependency handling.

### 11.54 Settlement Synchronization

A Settlement is synchronized as an independent financial event.

Example:

```text
Settlement S1
FromUser = Rahul
ToUser   = Praveen
Amount   = ₹500
```

Receiving devices apply the Settlement only after validating:

```text
Group
FromUser
ToUser
Amount
Currency
```

### 11.55 Balance Synchronization

Balances should not be synchronized as primary financial data.

Instead:

```text
Expenses
+
ExpenseSplits
+
Settlements
        ↓
Balance Calculation
```

Each device can calculate the resulting balance locally.

This avoids synchronization conflicts on derived balance values.

### 11.56 Derived Data Principle

The following should generally be derived rather than synchronized as authoritative state:

```text
Member Balance
Group Total
Total Paid
Total Owed
Settlement Suggestions
```

Source records are synchronized.

Derived values are recalculated locally.

### 11.57 Sync Payload Validation

Every received SyncOperation must be validated before being applied.

Validation includes:

```text
Operation ID
Device ID
Entity Type
Entity ID
Operation Type
Version
Payload
Authorization
Relationships
Financial Invariants
```

### 11.58 Untrusted Synchronization Input

All remote synchronization data must be considered untrusted.

This applies to:

```text
Backend responses
Peer device messages
Imported synchronization payloads
```

A local Wi-Fi connection must not automatically imply trust.

### 11.59 Peer Synchronization Security Boundary

Peer synchronization must verify:

```text
Peer Identity
Group Authorization
Operation Integrity
Operation ID
Entity Relationships
```

before applying received changes.

The exact cryptographic handshake is defined in the Security and Peer Synchronization sections.

### 11.60 Synchronization Metadata

The synchronization model may maintain metadata such as:

```text
createdByDeviceId
lastModifiedByDeviceId
version
baseVersion
operationId
updatedAt
```

This metadata supports:

- Conflict detection.
- Debugging.
- Idempotency.
- Attribution.
- Recovery.

### 11.61 Clock Considerations

Device clocks cannot be assumed to be perfectly synchronized.

Therefore timestamps must not be the sole mechanism for conflict detection.

The synchronization model primarily relies on:

```text
Operation ID
Entity Version
Base Version
Server/Protocol Ordering
```

rather than simply:

```text
updatedAt
```

### 11.62 Server Ordering

The backend may assign an authoritative ordering to accepted changes.

Conceptually:

```text
Accepted Operation
        ↓
Server Sequence
        ↓
Synchronization Cursor
```

This allows clients to pull changes in a deterministic order.

The exact server sequence/cursor implementation will be finalized in the API and synchronization protocol.

### 11.63 Peer Ordering

Peer synchronization cannot assume a single permanent central authority.

Therefore peers must exchange:

```text
Operation ID
Entity ID
Version
Base Version
Origin Device
```

and use the defined conflict/dependency rules.

The exact peer convergence mechanism will be defined in the Local Peer-to-Peer Synchronization section.

### 11.64 Synchronization Consistency Model

SplitSync V1 uses:

```text
Local Strong Consistency
+
Cross-Device Eventual Consistency
```

Meaning:

```text
Local Transaction
    ↓
Immediately consistent local state
```

while:

```text
Device A
    ↓
offline

Device B
    ↓
offline

Later synchronize
    ↓
Converge
```

### 11.65 Convergence Requirement

If two authorized devices eventually receive the same valid set of synchronization operations, they must converge toward the same logical Domain state.

Conceptually:

```text
Device A
   │
   ├── Operations O1, O2, O3
   │
   ▼
Synchronization
   │
   ▼
Common State

Device B
   │
   ├── Operations O1, O2, O3
   │
   ▼
Synchronization
   │
   ▼
Common State
```

### 11.66 Sync Failure Principle

A failed synchronization attempt must not roll back successful local application data.

Example:

```text
Expense Created Locally
        ↓
Local Transaction COMMIT
        ↓
Sync Attempt
        ↓
Network Failure
```

Result:

```text
Expense remains locally available
SyncOperation remains PENDING/FAILED
```

### 11.67 Application Restart Recovery

After application restart:

```text
Read Pending SyncOperations
        ↓
Retry eligible operations
        ↓
Continue synchronization
```

The application must not lose pending operations because of process termination.

### 11.68 Device Restart Recovery

After device restart:

```text
Room Database
        ↓
Load Sync State
        ↓
Load Pending Operations
        ↓
Continue Synchronization
```

The synchronization process must be resumable.

### 11.69 Sync Queue Ordering

The local queue should generally process operations in creation order while allowing dependency-aware ordering.

Example:

```text
O1 Create Group
O2 Add Member
O3 Create Expense
O4 Create Settlement
```

Expected logical processing:

```text
O1
 ↓
O2
 ↓
O3
 ↓
O4
```

Independent operations may be processed concurrently where safe.

### 11.70 Atomicity vs Operation Granularity

A SyncOperation represents a synchronization unit.

However, some Domain changes involve multiple records.

For example:

```text
Expense
+
ExpenseSplits
```

The synchronization protocol must ensure that receiving an Expense does not leave the local database in an invalid financial state.

The final implementation may use:

```text
Aggregate-level synchronization
```

or:

```text
Dependency-linked operations
```

The final strategy must be selected before implementation.

### 11.71 Recommended V1 Aggregate Synchronization

For financial operations, V1 should prefer synchronizing logically atomic aggregates where practical.

For example:

```text
Expense Aggregate
    ├── Expense
    └── ExpenseSplits
```

rather than allowing an Expense to become permanently visible without its required Splits.

This reduces intermediate invalid states.

### 11.72 Sync Operation Schema Summary

Conceptually:

```text
SyncOperation
────────────────────────────
operation_id
device_id
entity_type
entity_id
operation_type
base_version
entity_version
payload
created_at
status
metadata
```

### 11.73 Sync State Schema Summary

```text
SyncState
────────────────────────────
id
device_id
scope_type
scope_id
channel
cursor
status
last_successful_sync_at
last_attempt_at
error_code
```

### 11.74 Conflict Schema Summary

```text
Conflict
────────────────────────────
conflict_id
entity_type
entity_id
local_version
remote_version
local_payload
remote_payload
detected_at
status
resolution
resolved_at
```

### 11.75 Synchronization Flow

The complete V1 synchronization flow is:

```text
                 LOCAL DEVICE
                     │
                     ▼
               User Action
                     │
                     ▼
              Domain Validation
                     │
                     ▼
              Room Transaction
                     │
          ┌──────────┴──────────┐
          │                     │
          ▼                     ▼
     Entity State         SyncOperation
          │                     │
          └──────────┬──────────┘
                     ▼
                Local UI
                     │
                     │
             Connectivity
                     │
          ┌──────────┴──────────┐
          │                     │
          ▼                     ▼
       BACKEND                PEER
          │                     │
          ▼                     ▼
     Validate               Validate
          │                     │
          ▼                     ▼
    Apply / Conflict      Apply / Conflict
          │                     │
          └──────────┬──────────┘
                     ▼
              Remote Changes
                     │
                     ▼
             Local Validation
                     │
                     ▼
            Local Transaction
                     │
                     ▼
              Updated Room
                     │
                     ▼
                    UI
```

### 11.76 Synchronization Data Model Invariants

The following rules are mandatory for V1:

- Every synchronization operation must have a globally unique Operation ID.
- Operation IDs must remain unchanged across synchronization channels.
- Every operation must identify its source Device.
- Every operation must identify its Entity Type.
- Every operation must identify its Entity ID.
- Supported operation types are CREATE, UPDATE, and DELETE.
- Synchronizable entities must have stable identifiers.
- UPDATE operations should include a Base Version.
- Synchronizable entities must maintain version information.
- Operation ID must be used for idempotency.
- Duplicate operations must never create duplicate financial records.
- Local entity changes and their SyncOperations must be persisted atomically.
- Synchronization must not be required for immediate local data availability.
- Failed synchronization must not remove successful local changes.
- Pending operations must survive application and device restarts.
- Synchronization must validate all received data before applying it.
- Peer connectivity must not be treated as automatic authorization.
- Synchronization must respect Group-level authorization.
- Synchronization must preserve referential integrity.
- Synchronization must handle dependency ordering.
- Financial records must not be duplicated through retries or multi-channel delivery.
- Balances and other derived financial values should be recalculated rather than synchronized as authoritative records.
- Device timestamps must not be the sole conflict-resolution mechanism.
- Conflict resolution must be deterministic.
- Valid synchronized state must eventually converge across authorized devices.
- Backend synchronization and peer synchronization must use compatible Domain and operation semantics.
- The synchronization model must support eventual consistency without sacrificing local transactional consistency.

12. Sync Operation Model

13. Sync State Model

14. Conflict Data Model

15. Peer-to-Peer Communication Model

16. Authentication Flow

17. Authorization Flow

18. Online Flow

19. Offline Flow

20. Peer-to-Peer Offline Flow

21. Backend Synchronization Flow

22. Error Handling

23. Transaction Boundaries

24. API Design Principles

25. Versioning Strategy

26. Package Naming

27. Configuration Strategy

28. Environment Strategy

29. Logging Strategy

30. Testing Structure