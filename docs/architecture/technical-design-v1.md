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

## 12. Sync Operation Model

### 12.1 Purpose

This section defines the detailed model of a `SyncOperation` in SplitSync V1.

A SyncOperation represents a single logical change that must be propagated from one device to another synchronization participant.

The operation model is responsible for:

- Representing local changes.
- Tracking the source Device.
- Identifying the affected Entity.
- Supporting CREATE, UPDATE, and DELETE operations.
- Supporting idempotent processing.
- Supporting version-based conflict detection.
- Supporting retry and recovery.
- Supporting backend synchronization.
- Supporting local peer-to-peer synchronization.
- Preserving operation identity across synchronization channels.

The SyncOperation is an infrastructure-level representation of a Domain change.

### 12.2 Core Principle

The fundamental synchronization rule is:

```text
Domain Change
     ↓
SyncOperation
     ↓
Transport
     ↓
Remote Validation
     ↓
Remote Domain Change
```

The transport must not redefine the meaning of the operation.

For example:

```text
REST
```

and:

```text
Peer-to-Peer
```

are only different delivery mechanisms.

The logical operation remains the same.

### 12.3 SyncOperation Structure

The conceptual V1 structure is:

```text
SyncOperation
├── operationId
├── deviceId
├── entityType
├── entityId
├── operationType
├── baseVersion
├── entityVersion
├── payload
├── createdAt
├── status
├── retryCount
├── lastAttemptAt
└── error
```

### 12.4 Operation ID

`operationId` uniquely identifies the synchronization operation.

Example:

```text
operationId = 550e8400-e29b-41d4-a716-446655440000
```

The Operation ID must:

- Be generated by the originating device.
- Be globally unique.
- Remain unchanged during synchronization.
- Be persisted locally.
- Be persisted/recognized by the backend when required.
- Be used for idempotency.

### 12.5 Operation ID vs Entity ID

These identifiers represent different concepts.

```text
Entity ID
    ↓
Identifies the business entity

Operation ID
    ↓
Identifies one change to that entity
```

Example:

```text
ExpenseId = E123

Operation 1 = O100
Operation 2 = O101
Operation 3 = O102
```

All three operations may refer to:

```text
Expense E123
```

but they represent different changes.

### 12.6 Device ID

`deviceId` identifies the device that originally created the operation.

Example:

```text
deviceId = D001
```

This is important for:

- Change attribution.
- Synchronization tracking.
- Conflict detection.
- Debugging.
- Security validation.

The Device ID must remain stable for the lifetime of the application installation.

### 12.7 Entity Type

`entityType` identifies which Domain entity is being changed.

V1 values:

```text
USER
DEVICE
GROUP
GROUP_MEMBER
EXPENSE
EXPENSE_SPLIT
SETTLEMENT
```

Financial synchronization primarily concerns:

```text
GROUP
GROUP_MEMBER
EXPENSE
EXPENSE_SPLIT
SETTLEMENT
```

### 12.8 Entity ID

`entityId` identifies the exact entity affected by the operation.

Example:

```text
entityType = EXPENSE
entityId   = E123
```

means:

```text
Expense E123
```

The Entity ID must be the same across:

```text
Local Database
Backend Database
Peer Device
```

### 12.9 Operation Type

V1 supports three operation types:

```text
CREATE
UPDATE
DELETE
```

#### CREATE

Creates a new entity.

```text
CREATE
    ↓
Entity does not previously exist
```

#### UPDATE

Changes an existing entity.

```text
UPDATE
    ↓
Entity already exists
```

#### DELETE

Marks/removes an existing entity.

```text
DELETE
    ↓
Entity is no longer active
```

### 12.10 Base Version

`baseVersion` represents the entity version from which the local change was made.

Example:

```text
Existing Expense:
Version = 5

User edits Expense

Operation:
baseVersion = 5
```

This allows the receiving side to determine whether the operation was created from the latest known state.

### 12.11 Entity Version

`entityVersion` represents the version produced by the operation.

Example:

```text
Before:
Version = 5

After UPDATE:
Version = 6
```

Operation:

```text
baseVersion   = 5
entityVersion = 6
```

### 12.12 Version Relationship

For a normal update:

```text
entityVersion > baseVersion
```

Conceptually:

```text
Version 5
   │
   │ UPDATE
   ▼
Version 6
```

The exact version allocation strategy will be defined by the Synchronization Protocol.

### 12.13 Version Is Not Operation Identity

Versions must not be used as globally unique operation identifiers.

Example:

```text
Device A:
Expense E1
baseVersion = 5
entityVersion = 6

Device B:
Expense E1
baseVersion = 5
entityVersion = 6
```

Both operations may legitimately have:

```text
entityVersion = 6
```

but they are different operations.

Therefore:

```text
Operation ID
    ↓
Globally unique

Entity Version
    ↓
Entity state/version information
```

### 12.14 Payload

The `payload` contains the information required to apply the operation.

For CREATE/UPDATE operations, V1 should generally use a complete entity representation where practical.

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

The exact JSON schema will be defined in the API Design.

### 12.15 Payload Principle

The payload must contain Domain data, not UI-specific data.

It should not contain:

```text
Screen State
ViewModel State
Android View IDs
UI Formatting
```

It should represent the actual Domain state being synchronized.

### 12.16 DELETE Payload

A DELETE operation must contain sufficient metadata to identify the deletion.

Conceptually:

```text
DELETE
├── operationId
├── deviceId
├── entityType
├── entityId
├── baseVersion
├── entityVersion
└── deletedAt
```

The actual entity row may be retained as a tombstone until it is safe to remove.

### 12.17 Operation Timestamp

Each operation contains:

```text
createdAt
```

This represents when the originating device created the operation.

Timestamps are useful for:

- Debugging.
- Auditing.
- Retry tracking.
- Operational monitoring.

However, timestamps must not be the sole conflict-resolution mechanism because device clocks may differ.

### 12.18 Operation Status

The local operation state may be:

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
Waiting to be synchronized

IN_PROGRESS
    ↓
Currently being processed

COMPLETED
    ↓
Successfully synchronized

FAILED
    ↓
Temporary/permanent processing failure

CONFLICT
    ↓
Operation conflicts with another valid change
```

### 12.19 Operation Lifecycle

Normal flow:

```text
CREATE LOCAL CHANGE
        ↓
PENDING
        ↓
IN_PROGRESS
        ↓
COMPLETED
```

Temporary failure:

```text
IN_PROGRESS
        ↓
FAILED
        ↓
PENDING
        ↓
RETRY
```

Conflict:

```text
IN_PROGRESS
        ↓
CONFLICT
        ↓
RESOLUTION
        ↓
COMPLETED
```

### 12.20 Local Operation Creation

A SyncOperation must be created as part of the same Room transaction as the Domain change.

Example:

```text
BEGIN TRANSACTION

Validate Expense
       ↓
Save Expense
       ↓
Save ExpenseSplits
       ↓
Create SyncOperation
       ↓
COMMIT
```

This guarantees that a successful local change has a corresponding synchronization operation.

### 12.21 Local Operation Failure

If the local transaction fails:

```text
ROLLBACK
```

Both:

```text
Domain Change
```

and:

```text
SyncOperation
```

must be rolled back.

There must not be a synchronization operation for a Domain change that was never successfully committed.

### 12.22 Operation Queue

The local database acts as the durable operation queue.

Conceptually:

```text
sync_operations
        │
        ├── PENDING
        ├── IN_PROGRESS
        ├── FAILED
        └── CONFLICT
```

The synchronization worker processes eligible operations from this queue.

### 12.23 Queue Persistence

Pending operations must survive:

```text
Application restart
Process termination
Device restart
Temporary connectivity loss
Backend outage
```

Therefore the queue must not exist only in:

```text
Memory
```

It must be persisted in Room.

### 12.24 Operation Processing

The synchronization engine should process operations approximately as follows:

```text
Load PENDING Operation
        ↓
Check Connectivity
        ↓
Validate Local State
        ↓
Transmit Operation
        ↓
Receive Result
        ↓
Apply Result
        ↓
Update Operation Status
```

### 12.25 Operation Locking

The same operation must not be processed concurrently by multiple local workers.

Conceptually:

```text
PENDING
   ↓
Acquire Operation
   ↓
IN_PROGRESS
```

Another worker must not simultaneously process the same Operation ID.

The exact Room locking/worker strategy will be defined during Android implementation.

### 12.26 Retry Count

Each operation may maintain:

```text
retryCount
```

Example:

```text
retryCount = 0
```

After one failed attempt:

```text
retryCount = 1
```

This supports:

- Retry policies.
- Backoff.
- Diagnostics.
- Failure thresholds.

### 12.27 Retryable Errors

Temporary failures may be retried.

Examples:

```text
Network Timeout
Connection Failure
Server Unavailable
Temporary Database Error
Rate Limit
```

The operation should remain recoverable.

### 12.28 Non-Retryable Errors

Permanent validation failures should not be retried indefinitely.

Examples:

```text
Invalid Payload
Unauthorized Operation
Invalid Group Membership
Invalid Financial Data
Unsupported Entity Type
```

Such operations should transition to an appropriate failure state and be surfaced to the synchronization/error-handling layer.

### 12.29 Idempotency

Idempotency means:

```text
Processing the same Operation ID multiple times
```

must produce the same logical result as processing it once.

Example:

```text
Operation O100
Create Expense E123
```

If received:

```text
1st time → Create E123
2nd time → Already Applied
3rd time → Already Applied
```

The result must remain:

```text
One Expense E123
```

### 12.30 Idempotency Record

The backend should retain enough information to determine whether an Operation ID has already been processed.

Conceptually:

```text
operationId
    ↓
Processed?
    │
 ┌──┴──┐
 │     │
Yes    No
 │     │
Ignore Process
```

The exact persistence strategy will be finalized during Backend Synchronization Design.

### 12.31 Duplicate Operation Scenario

Example:

```text
Device A
   │
   └── O123
        │
        ├── Network retry
        │
        ├── O123 sent again
        │
        └── Backend receives twice
```

Backend behavior:

```text
First O123
    ↓
Apply

Second O123
    ↓
Recognize O123
    ↓
Do not apply again
```

### 12.32 Duplicate Entity Scenario

If an operation references an entity that already exists:

```text
EntityId = E123
```

the receiver must determine whether the operation is:

```text
Already Applied
Newer Update
Older Update
Concurrent Update
Invalid Operation
```

It must not blindly insert another row.

### 12.33 CREATE Operation

Conceptual CREATE operation:

```text
SyncOperation
├── operationId = O100
├── entityType = EXPENSE
├── entityId = E123
├── operationType = CREATE
├── baseVersion = 0
├── entityVersion = 1
└── payload = Expense State
```

Expected result:

```text
Expense E123
Version = 1
```

### 12.34 UPDATE Operation

Conceptual UPDATE:

```text
SyncOperation
├── operationId = O101
├── entityType = EXPENSE
├── entityId = E123
├── operationType = UPDATE
├── baseVersion = 1
├── entityVersion = 2
└── payload = Updated Expense State
```

Expected result:

```text
Expense E123
Version = 2
```

provided that the operation is valid and no conflicting state exists.

### 12.35 DELETE Operation

Conceptual DELETE:

```text
SyncOperation
├── operationId = O102
├── entityType = EXPENSE
├── entityId = E123
├── operationType = DELETE
├── baseVersion = 2
├── entityVersion = 3
└── deletedAt = timestamp
```

Expected logical result:

```text
Expense E123
Version = 3
Deleted
```

### 12.36 Create Operation Dependencies

A CREATE operation may depend on another entity.

Example:

```text
Create Group G1
        ↓
Create GroupMember M1
        ↓
Create Expense E1
        ↓
Create ExpenseSplit S1
```

The synchronization engine must preserve these dependencies.

### 12.37 Operation Dependency

A SyncOperation may conceptually contain:

```text
dependsOn
```

Example:

```text
Operation O3
Entity = Expense E1

dependsOn:
O1 = Create Group G1
O2 = Create GroupMember M1
```

This field is optional from the initial storage model but may be introduced if required by the synchronization implementation.

### 12.38 Dependency Handling

If a dependency is unavailable:

```text
ExpenseSplit S1
      ↓
Expense E1 missing
```

the operation must not create invalid state.

Possible result:

```text
WAITING_FOR_DEPENDENCY
```

or equivalent internal queue handling.

### 12.39 Operation Ordering

Operations should normally be processed in creation order when they affect the same entity.

Example:

```text
O1 → Expense E1 Version 1
O2 → Expense E1 Version 2
O3 → Expense E1 Version 3
```

The synchronization system should not apply:

```text
O3
```

before:

```text
O1
O2
```

unless the protocol explicitly supports such processing.

### 12.40 Independent Operations

Operations affecting unrelated entities may be processed independently.

Example:

```text
Expense E1
Expense E2
Settlement S1
```

may be synchronized concurrently if doing so does not violate dependencies or transaction boundaries.

### 12.41 Operation Ordering Is Not Global Ordering

The system does not require one global sequence for all operations.

Example:

```text
O1 → Group G1
O2 → Group G2
```

These operations are independent.

Synchronization may process them separately.

Ordering is primarily required where there is:

```text
Entity dependency
```

or:

```text
Version dependency
```

### 12.42 Conflict Detection

A receiver compares:

```text
Incoming baseVersion
```

with:

```text
Current entity version
```

Example:

```text
Incoming:
baseVersion = 5

Current:
version = 5

Result:
No version conflict
```

But:

```text
Incoming:
baseVersion = 5

Current:
version = 6

Result:
Potential conflict
```

### 12.43 Conflict Example

Initial:

```text
Expense E1
Version = 5
Amount = ₹1000
```

Device A:

```text
baseVersion = 5
Amount = ₹1200
entityVersion = 6
```

Device B:

```text
baseVersion = 5
Amount = ₹1500
entityVersion = 6
```

If Device A's operation is already applied:

```text
Current Version = 6
```

Device B's operation arrives:

```text
baseVersion = 5
Current Version = 6
```

The operation is a concurrent update candidate.

### 12.44 Conflict Result

The receiver must not silently overwrite the current state.

Instead:

```text
Detect Conflict
      ↓
Classify
      ↓
Resolve Automatically
       OR
Create Conflict Record
```

The detailed resolution strategy is defined in Section 13.

### 12.45 Operation Rejection

An operation can be rejected even without a version conflict.

Examples:

```text
Invalid Group
Invalid User
Unauthorized Device
Invalid Split
Invalid Amount
Invalid Currency
Invalid Settlement
```

A rejected operation must not partially modify the database.

### 12.46 Atomic Remote Processing

The backend should process an incoming operation transactionally.

Conceptually:

```text
BEGIN TRANSACTION

Validate Operation
       ↓
Validate Authorization
       ↓
Validate Version
       ↓
Validate Domain Rules
       ↓
Apply Entity Change
       ↓
Record Operation Result
       ↓
COMMIT
```

Failure:

```text
ROLLBACK
```

### 12.47 Peer Operation Processing

A peer device must use the same conceptual validation pipeline:

```text
Receive Operation
       ↓
Validate Peer
       ↓
Validate Operation
       ↓
Validate Entity Relationships
       ↓
Validate Version
       ↓
Apply Local Transaction
       ↓
Record Operation Result
```

Peer communication must not bypass Domain rules.

### 12.48 Operation Result

The receiver should return a structured result.

Conceptual model:

```text
OperationResult
├── operationId
├── entityId
├── status
├── serverVersion
├── appliedVersion
├── conflictId
└── error
```

### 12.49 Operation Result Status

Recommended statuses:

```text
APPLIED
ALREADY_APPLIED
REJECTED
CONFLICT
```

Meaning:

```text
APPLIED
    ↓
Operation successfully applied

ALREADY_APPLIED
    ↓
Operation was already processed

REJECTED
    ↓
Operation is invalid or unauthorized

CONFLICT
    ↓
Operation conflicts with current state
```

### 12.50 Local Result Handling

When the device receives:

```text
APPLIED
```

it may mark the operation:

```text
COMPLETED
```

When it receives:

```text
ALREADY_APPLIED
```

it may also mark:

```text
COMPLETED
```

because the intended operation already exists remotely.

When it receives:

```text
CONFLICT
```

the operation enters:

```text
CONFLICT
```

processing.

### 12.51 Operation and Local Entity State

The SyncOperation must not become the authoritative source of Domain state.

The relationship is:

```text
Domain Entity
    ↓
Authoritative local business state

SyncOperation
    ↓
Record of how that state should be synchronized
```

### 12.52 Operation Payload and Local Entity

The payload should correspond to the entity state represented by:

```text
entityVersion
```

Example:

```text
Expense E1
Version = 7

SyncOperation
entityVersion = 7
payload = Expense state at Version 7
```

This makes the operation replayable and easier to validate.

### 12.53 Operation Immutability

Once a SyncOperation has been created, its core identity fields should not be modified.

Immutable fields include:

```text
operationId
deviceId
entityType
entityId
operationType
baseVersion
entityVersion
payload
createdAt
```

Mutable processing metadata may include:

```text
status
retryCount
lastAttemptAt
error
```

### 12.54 Why Operations Should Be Immutable

Changing the original operation during retry could make:

```text
Operation ID
```

represent different logical changes.

Instead:

```text
Original Operation
    ↓
Retry same operation
```

not:

```text
Original Operation
    ↓
Modify operation
    ↓
Send different operation with same ID
```

### 12.55 Correcting a Change

If a User makes another change after an operation was created:

```text
Operation O1
    ↓
Expense Version 5 → 6
```

and then:

```text
Operation O2
    ↓
Expense Version 6 → 7
```

O2 is a new operation.

The system should not modify O1 to represent O2.

### 12.56 Operation History

Conceptually:

```text
Expense E1

O1 → CREATE → Version 1
O2 → UPDATE → Version 2
O3 → UPDATE → Version 3
O4 → DELETE → Version 4
```

This provides a logical history of changes.

The final retention strategy may remove old operations after they are no longer required for synchronization or audit purposes.

### 12.57 Operation Retention

Local operation records may eventually be cleaned after successful synchronization and an appropriate safety period.

However, cleanup must not occur while the operation may still be required for:

- Retry.
- Peer synchronization.
- Conflict resolution.
- Recovery.

The exact retention policy will be finalized during implementation.

### 12.58 Multi-Channel Delivery

The same operation may be delivered through multiple channels.

Example:

```text
Device A
   │
   ├── Send O123 to Peer B
   │
   └── Later Send O123 to Backend
```

The system must recognize:

```text
O123
```

as the same logical operation.

This is another reason Operation ID must be globally stable.

### 12.59 Backend and Peer Duplicate Delivery

Possible scenario:

```text
Device A
      │
      ├── O123 → Device B
      │
      └── O123 → Backend
                     │
                     ▼
                Device B later
                     │
                     └── O123
```

Device B may receive the same logical operation through multiple paths.

It must process:

```text
O123
```

only once.

### 12.60 Operation Source

The original source is:

```text
deviceId
```

The transport path may be different.

For example:

```text
Origin:
Device A

Transport:
Peer → Backend → Device B
```

The operation must still retain:

```text
deviceId = Device A
```

not replace it with the intermediary device.

### 12.61 Operation Authorization

The receiver must verify that the source Device/User is authorized to perform the operation.

For Group-owned entities:

```text
Operation
    ↓
Entity
    ↓
Group
    ↓
Membership
    ↓
Authorization
```

Knowing the Entity ID is not sufficient authorization.

### 12.62 Operation Validation Order

Recommended validation sequence:

```text
1. Validate Operation Structure
        ↓
2. Validate Operation ID
        ↓
3. Validate Source Device
        ↓
4. Validate Entity Type
        ↓
5. Validate Entity ID
        ↓
6. Validate Authorization
        ↓
7. Validate Dependencies
        ↓
8. Validate Version
        ↓
9. Validate Domain Rules
        ↓
10. Apply Transaction
```

### 12.63 Invalid Operation Handling

If structural validation fails:

```text
REJECT
```

If authorization fails:

```text
REJECT
```

If version conflict exists:

```text
CONFLICT
```

If temporary infrastructure failure occurs:

```text
RETRY
```

If operation is already applied:

```text
ALREADY_APPLIED
```

### 12.64 Sync Operation Model Example

Complete Expense creation example:

```text
Operation ID:
O100

Device ID:
D001

Entity Type:
EXPENSE

Entity ID:
E123

Operation Type:
CREATE

Base Version:
0

Entity Version:
1

Payload:
{
    Expense E123
}

Created At:
2026-08-25T10:00:00Z

Status:
PENDING
```

After successful synchronization:

```text
Status:
COMPLETED
```

### 12.65 Sync Operation Model Example — Update

```text
Operation ID:
O101

Device ID:
D001

Entity Type:
EXPENSE

Entity ID:
E123

Operation Type:
UPDATE

Base Version:
1

Entity Version:
2

Payload:
{
    Updated Expense E123
}

Status:
PENDING
```

### 12.66 Sync Operation Model Example — Delete

```text
Operation ID:
O102

Device ID:
D001

Entity Type:
EXPENSE

Entity ID:
E123

Operation Type:
DELETE

Base Version:
2

Entity Version:
3

Payload:
{
    deletedAt: timestamp
}

Status:
PENDING
```

### 12.67 Sync Operation Data Model

The conceptual data model is:

```text
┌────────────────────────────────────┐
│          SYNC_OPERATION             │
├────────────────────────────────────┤
│ operation_id      PK               │
│ device_id         FK               │
│ entity_type                        │
│ entity_id                           │
│ operation_type                      │
│ base_version                        │
│ entity_version                      │
│ payload                             │
│ created_at                          │
│ status                              │
│ retry_count                         │
│ last_attempt_at                     │
│ error_code                          │
│ error_message                       │
└────────────────────────────────────┘
```

### 12.68 Relationship to Domain Entity

```text
SyncOperation
      │
      ├── entityType
      └── entityId
             │
             ▼
        Domain Entity
```

The relationship is logical/polymorphic rather than a traditional foreign key because one SyncOperation may target different entity types.

### 12.69 Relationship to Device

```text
SyncOperation
      │
      └── deviceId
             │
             ▼
          Device
```

This is a normal Device relationship.

### 12.70 Relationship to Conflict

A SyncOperation may produce a Conflict.

```text
SyncOperation
      │
      ▼
Version Conflict
      │
      ▼
Conflict
```

The exact database relationship may be:

```text
Conflict.operationId
```

or another equivalent representation.

### 12.71 Operation Model Summary

```text
SyncOperation
│
├── Identity
│   ├── operationId
│   ├── deviceId
│   ├── entityType
│   └── entityId
│
├── Change
│   ├── operationType
│   ├── baseVersion
│   ├── entityVersion
│   └── payload
│
├── Lifecycle
│   ├── status
│   ├── retryCount
│   ├── createdAt
│   └── lastAttemptAt
│
└── Failure
    ├── errorCode
    └── errorMessage
```

### 12.72 Sync Operation Model Invariants

The following rules are mandatory for V1:

- Every SyncOperation must have a globally unique Operation ID.
- Operation ID must remain unchanged across all synchronization channels.
- Every operation must identify its originating Device.
- Every operation must identify an Entity Type.
- Every operation must identify an Entity ID.
- V1 supports CREATE, UPDATE, and DELETE operations.
- UPDATE and DELETE operations must carry sufficient version information for conflict detection.
- Operation ID and Entity ID must never be treated as the same concept.
- Entity versions must not be used as globally unique operation identifiers.
- The payload must represent Domain data rather than UI state.
- Local Domain changes and their SyncOperations must be persisted atomically.
- SyncOperations must survive application and device restarts until safely processed.
- Processing the same Operation ID multiple times must be idempotent.
- Retries must reuse the same Operation ID.
- A new logical change must create a new Operation ID.
- SyncOperations should be treated as immutable after creation except for processing metadata.
- Operations must respect entity dependencies.
- Operations must not be applied in a way that violates referential integrity.
- Received operations must be validated before application.
- Authorization must be checked independently of connectivity.
- Peer connectivity must not automatically authorize an operation.
- Version conflicts must not be silently overwritten.
- Financial operations must be applied transactionally.
- Derived balances must not be treated as authoritative synchronized entities.
- The same operation may safely travel through multiple synchronization channels.
- Duplicate delivery must never create duplicate financial records.
- Temporary failures must be retryable.
- Permanent validation failures must not be retried indefinitely.
- Synchronization operations must support eventual convergence of authorized devices.

## 13. Sync State Model

### 13.1 Purpose

This section defines the `SyncState` model used by SplitSync V1 to track synchronization progress.

A `SyncState` represents the current synchronization position and status for a specific synchronization scope and channel.

The model is responsible for tracking:

- Which Device is synchronizing.
- Which synchronization scope is being synchronized.
- Which synchronization channel is being used.
- The latest successfully processed cursor.
- Synchronization status.
- Last successful synchronization time.
- Last synchronization attempt.
- Synchronization errors.
- Recovery and retry state.

A `SyncState` does not represent a Domain entity change.

Instead:

```text
SyncOperation
    ↓
Represents a change

SyncState
    ↓
Represents synchronization progress
```

### 13.2 Core Principle

The fundamental distinction is:

```text
SyncOperation
    = What changed?

SyncState
    = How far have we synchronized?
```

Example:

```text
SyncOperation O123
    ↓
Create Expense E100
```

while:

```text
SyncState
    ↓
Device D1 has processed changes through Cursor C500
```

### 13.3 SyncState Structure

The conceptual V1 structure is:

```text
SyncState
├── id
├── deviceId
├── scopeType
├── scopeId
├── channel
├── cursor
├── status
├── lastSuccessfulSyncAt
├── lastAttemptAt
├── errorCode
└── errorMessage
```

### 13.4 Sync State ID

Each SyncState has a stable identifier:

```text
id
```

The ID uniquely identifies one synchronization-state record.

The exact ID generation strategy will be finalized during implementation.

### 13.5 Device ID

Every synchronization state is associated with a Device:

```text
deviceId
```

Example:

```text
Device D001
```

This allows the application to maintain independent synchronization progress for different devices.

### 13.6 Scope Type

`scopeType` defines what the SyncState applies to.

Potential V1 scopes are:

```text
DEVICE
GROUP
```

The exact scope model may be extended later if required.

### 13.7 Device-Level Scope

A Device-level SyncState represents synchronization progress for operations or data that are not restricted to one specific Group.

Conceptually:

```text
Device D1
    ↓
DEVICE scope
    ↓
Backend synchronization
```

### 13.8 Group-Level Scope

A Group-level SyncState represents synchronization progress for a specific Group.

Example:

```text
Device D1
    ↓
Group G1
    ↓
BACKEND
    ↓
Cursor C100
```

This is useful because shared financial data is primarily organized around Groups.

### 13.9 Scope ID

When the scope is Group-based:

```text
scopeType = GROUP
scopeId   = G123
```

The Scope ID identifies the Group being synchronized.

For a Device-level scope, `scopeId` may be:

```text
NULL
```

or another defined Device-level identifier depending on the final schema.

### 13.10 Synchronization Channel

The `channel` identifies how synchronization is being performed.

V1 channels:

```text
BACKEND
PEER
```

### 13.11 Backend Channel

The Backend channel represents:

```text
Android Device
      ↓
Internet
      ↓
Spring Boot Backend
      ↓
MySQL
```

Example:

```text
channel = BACKEND
```

### 13.12 Peer Channel

The Peer channel represents local device-to-device synchronization.

Example:

```text
Device A
    ⇅
Local Wi-Fi / Hotspot
    ⇅
Device B
```

Example SyncState:

```text
channel = PEER
```

Peer synchronization state may need to be maintained separately for different peers.

### 13.13 Peer-Specific State

For peer synchronization, the same Group may synchronize with multiple devices.

Example:

```text
Group G1

Device A
   ⇅
Device B

Device A
   ⇅
Device C
```

Therefore a peer SyncState may need to identify the synchronization partner.

Conceptually:

```text
SyncState
├── deviceId
├── peerDeviceId
├── scopeType
├── scopeId
└── channel = PEER
```

The exact peer-state schema will be finalized in the Local Peer-to-Peer Synchronization implementation.

### 13.14 Cursor

The `cursor` represents the latest synchronization position known to the client.

Example:

```text
cursor = C500
```

The client can request:

```text
Give me changes after C500
```

The cursor must be treated as an opaque synchronization value.

The client must not depend on its internal format.

### 13.15 Cursor Principle

The cursor is not the same as:

```text
Entity Version
```

and not the same as:

```text
Operation ID
```

They represent different concepts:

```text
Operation ID
    ↓
Identifies one operation

Entity Version
    ↓
Identifies entity state/version

Cursor
    ↓
Identifies synchronization progress
```

### 13.16 Cursor Example

Suppose the backend has a change stream:

```text
C100
C101
C102
C103
C104
```

The device has successfully processed:

```text
C102
```

Its SyncState contains:

```text
cursor = C102
```

The next synchronization request can ask for:

```text
Changes after C102
```

and receive:

```text
C103
C104
```

### 13.17 Cursor Advancement Rule

The cursor must advance only after the corresponding changes have been successfully processed locally.

Correct:

```text
Receive Changes
      ↓
Validate
      ↓
Apply Local Transaction
      ↓
COMMIT
      ↓
Advance Cursor
```

Incorrect:

```text
Receive Changes
      ↓
Advance Cursor
      ↓
Apply Local Changes
      ↓
Failure
```

The second approach could cause data loss because the client would believe changes were processed when they were not.

### 13.18 Cursor Transaction Principle

Where practical, applying remote changes and advancing the cursor should occur within the same local transaction.

Conceptually:

```text
BEGIN TRANSACTION

Apply Remote Changes
        ↓
Update SyncState Cursor
        ↓
COMMIT
```

If the transaction fails:

```text
ROLLBACK
```

The cursor must remain unchanged.

### 13.19 Sync Status

The SyncState maintains the current synchronization status.

Recommended V1 states:

```text
IDLE
SYNCING
SYNCED
FAILED
BLOCKED
```

### 13.20 IDLE

`IDLE` means:

```text
No synchronization operation is currently running.
```

Example:

```text
Device has no pending synchronization work.
```

### 13.21 SYNCING

`SYNCING` means:

```text
Synchronization is currently in progress.
```

Example:

```text
Pending Operations
       ↓
Synchronization Worker
       ↓
SYNCING
```

### 13.22 SYNCED

`SYNCED` means:

```text
The current synchronization scope has successfully completed its latest synchronization cycle.
```

It does not necessarily mean the device will never have pending changes again.

A new local change may immediately produce:

```text
SYNCED
   ↓
Pending SyncOperation
   ↓
SYNCING
```

### 13.23 FAILED

`FAILED` means:

```text
The latest synchronization attempt failed.
```

Examples:

```text
Network timeout
Server unavailable
Peer disconnected
Temporary protocol failure
```

The synchronization engine may retry according to the retry policy.

### 13.24 BLOCKED

`BLOCKED` represents a synchronization state that cannot currently progress automatically.

Examples may include:

```text
Unresolved synchronization conflict
Invalid synchronization state
Authentication requirement
Permanent authorization issue
```

The exact conditions for entering `BLOCKED` will be defined during implementation.

### 13.25 Last Successful Synchronization

The SyncState stores:

```text
lastSuccessfulSyncAt
```

This represents the last time the synchronization scope completed successfully.

Example:

```text
lastSuccessfulSyncAt =
2026-08-25T10:30:00Z
```

This is useful for:

- UI status.
- Diagnostics.
- Monitoring.
- Retry decisions.
- User feedback.

### 13.26 Last Attempt

The SyncState also stores:

```text
lastAttemptAt
```

This represents when synchronization was most recently attempted.

Example:

```text
lastAttemptAt =
2026-08-25T10:35:00Z
```

The two timestamps are different.

```text
lastAttemptAt
    = latest attempt

lastSuccessfulSyncAt
    = latest successful synchronization
```

### 13.27 Error Code

When synchronization fails, the state may contain:

```text
errorCode
```

Examples:

```text
NETWORK_UNAVAILABLE
SERVER_UNAVAILABLE
AUTHORIZATION_FAILED
CONFLICT
INVALID_OPERATION
PROTOCOL_ERROR
```

The final error-code catalog will be defined during implementation.

### 13.28 Error Message

The state may also contain:

```text
errorMessage
```

This is intended primarily for diagnostics.

The application should not depend on exact human-readable error text for business logic.

Business logic should use:

```text
errorCode
```

### 13.29 SyncState Lifecycle

The normal lifecycle is:

```text
IDLE
  ↓
SYNCING
  ↓
SYNCED
```

Failure:

```text
SYNCING
  ↓
FAILED
  ↓
SYNCING
  ↓
SYNCED
```

Blocking condition:

```text
SYNCING
  ↓
BLOCKED
  ↓
Resolution
  ↓
SYNCING
```

### 13.30 Initial Sync State

When a synchronization scope has never been synchronized:

```text
cursor = NULL
```

or an equivalent initial cursor value.

Example:

```text
scope = Group G1
channel = BACKEND
cursor = NULL
```

The client then performs an initial synchronization.

### 13.31 Initial Synchronization

Initial synchronization may follow:

```text
Create SyncState
      ↓
cursor = NULL
      ↓
Request Initial Data
      ↓
Receive Remote State
      ↓
Apply Data
      ↓
Set Cursor
      ↓
SYNCED
```

### 13.32 Incremental Synchronization

After initial synchronization:

```text
Existing Cursor
      ↓
Request Changes After Cursor
      ↓
Receive Changes
      ↓
Apply Changes
      ↓
Advance Cursor
```

This avoids downloading the entire Group every time.

### 13.33 Full Synchronization

A full synchronization may be required when:

```text
Cursor invalid
Local data recovery
Database migration
Protocol reset
Explicit user request
Server requires resynchronization
```

The exact full-resync strategy will be defined during synchronization implementation.

### 13.34 SyncState and SyncOperation

The relationship is:

```text
SyncState
    │
    │ tracks progress of
    ▼
Synchronization Process
    │
    ▼
SyncOperations
```

More specifically:

```text
SyncOperation
    = individual change

SyncState
    = progress through changes
```

### 13.35 Push State

A SyncState may also represent progress of locally created operations being pushed to a synchronization target.

Example:

```text
Device D1
    ↓
Group G1
    ↓
BACKEND
```

The local queue contains:

```text
O1
O2
O3
```

The synchronization engine processes them.

The SyncState records the overall synchronization status.

### 13.36 Pull State

A SyncState also tracks progress while pulling remote changes.

Example:

```text
Cursor = C500
```

The device requests:

```text
Changes after C500
```

After successful processing:

```text
Cursor = C520
```

### 13.37 Push and Pull Are Related but Distinct

Conceptually:

```text
PUSH
Device
  ↓
Remote

PULL
Remote
  ↓
Device
```

The same SyncState may represent the overall channel state, while separate internal metadata may be used if the implementation requires independent push/pull tracking.

### 13.38 Backend SyncState Example

```text
Device ID:
D001

Scope Type:
GROUP

Scope ID:
G100

Channel:
BACKEND

Cursor:
C500

Status:
SYNCED

Last Successful Sync:
2026-08-25T10:30:00Z

Last Attempt:
2026-08-25T10:30:00Z
```

### 13.39 Peer SyncState Example

```text
Device ID:
D001

Peer Device ID:
D002

Scope Type:
GROUP

Scope ID:
G100

Channel:
PEER

Cursor:
P200

Status:
SYNCED

Last Successful Sync:
2026-08-25T10:35:00Z
```

### 13.40 Multiple Groups

A device can have independent synchronization states for multiple Groups.

Example:

```text
Device D1

Group G1
    BACKEND
    Cursor C100

Group G2
    BACKEND
    Cursor C250

Group G3
    BACKEND
    Cursor C500
```

One Group failing synchronization must not prevent another Group from synchronizing.

### 13.41 Multiple Peers

A device may synchronize the same Group with multiple peers.

Example:

```text
Device D1
 │
 ├── Group G1 ↔ Device D2
 │
 ├── Group G1 ↔ Device D3
 │
 └── Group G1 ↔ Device D4
```

Peer synchronization state may therefore be maintained per:

```text
Device
+
Peer Device
+
Group
```

### 13.42 Synchronization Isolation

Synchronization should be isolated by:

```text
Scope
+
Channel
```

For example:

```text
Group G1 + BACKEND
```

being unavailable should not prevent:

```text
Group G1 + PEER
```

from functioning.

Similarly:

```text
Group G1
```

failure should not block:

```text
Group G2
```

### 13.43 Connectivity and SyncState

Connectivity state is not the same as synchronization state.

Example:

```text
Internet Available
```

does not guarantee:

```text
SYNCED
```

because:

```text
Internet Available
    ↓
Backend request
    ↓
Could still fail
```

Similarly:

```text
Internet Unavailable
```

does not mean the application cannot synchronize through:

```text
PEER
```

### 13.44 Automatic Channel Selection

The synchronization engine may automatically select an available channel.

Conceptually:

```text
Check Connectivity
       │
       ├── Backend Available
       │       ↓
       │    BACKEND
       │
       └── Backend Unavailable
               ↓
          Peer Available?
               │
               ├── Yes → PEER
               └── No  → Remain Pending
```

The user should not be required to manually switch the entire application between online and offline modes.

### 13.45 Hybrid Connectivity

Both channels may be available at the same time.

Example:

```text
Internet Available
        +
Nearby Device Available
```

The synchronization engine may use:

```text
Backend
```

and:

```text
Peer
```

depending on the synchronization strategy.

Duplicate operations must remain safe because the same operation can arrive through both channels.

### 13.46 SyncState and Offline Mode

Offline mode does not require a special database mode.

Instead:

```text
No Connectivity
      ↓
SyncOperations remain PENDING
      ↓
Local Database continues operating
```

The SyncState may remain:

```text
FAILED
```

or:

```text
IDLE
```

depending on the synchronization engine's state model.

The Domain functionality remains fully available.

### 13.47 SyncState and App Restart

After application restart:

```text
Load SyncStates
      ↓
Load Pending SyncOperations
      ↓
Determine Connectivity
      ↓
Resume Synchronization
```

The application must not lose synchronization progress.

### 13.48 SyncState and Device Restart

After device restart:

```text
Room Database
      ↓
SyncState restored
      ↓
Pending Operations restored
      ↓
Synchronization resumes
```

### 13.49 SyncState and Failed Operations

A failed operation may affect the overall SyncState.

Example:

```text
Operation O1 → COMPLETED
Operation O2 → FAILED
Operation O3 → PENDING
```

Possible SyncState:

```text
FAILED
```

The exact aggregate-status calculation will be defined during implementation.

### 13.50 SyncState and Conflicts

If an unresolved conflict blocks synchronization:

```text
SyncOperation
      ↓
CONFLICT
      ↓
SyncState
      ↓
BLOCKED
```

The application must continue allowing unrelated local operations.

For example:

```text
Group G1
   ↓
Expense E1 conflict

Expense E2
   ↓
Can still be created locally
```

### 13.51 SyncState and Local Changes

A new local change may transition the state:

```text
SYNCED
   ↓
New SyncOperation
   ↓
PENDING
```

The actual SyncState may remain:

```text
IDLE
```

until the synchronization worker starts.

### 13.52 SyncState and Remote Changes

Remote changes should update the local database and cursor atomically.

Example:

```text
Receive C501-C510
      ↓
Apply Changes
      ↓
Update Cursor = C510
      ↓
COMMIT
```

### 13.53 Cursor Failure Scenario

Suppose:

```text
Current Cursor = C500
```

The client receives:

```text
C501-C510
```

but applying C506 fails.

The transaction should roll back.

Result:

```text
Cursor remains C500
```

The client can retry safely.

### 13.54 Cursor and Idempotency

A cursor must not be advanced merely because data was received.

It advances only when the corresponding data has been successfully applied.

This guarantees:

```text
Cursor
    ↔
Successfully Applied Remote State
```

### 13.55 SyncState Consistency

The following invariant must hold:

```text
lastSuccessfulSyncAt
```

must only be updated after a successful synchronization transaction.

Similarly:

```text
cursor
```

must only represent successfully applied remote changes.

### 13.56 SyncState Error Recovery

When a temporary failure occurs:

```text
SYNCING
   ↓
FAILED
```

The synchronization engine may later retry:

```text
FAILED
   ↓
SYNCING
```

If successful:

```text
SYNCING
   ↓
SYNCED
```

### 13.57 SyncState and Authentication

Authentication state is separate from synchronization state.

For example:

```text
Authentication
    = Is this Device/User authorized?

Synchronization
    = What changes have been synchronized?
```

Authentication failure may cause:

```text
BLOCKED
```

but credentials/tokens must not be stored as part of SyncState.

### 13.58 SyncState and Authorization

A device must only maintain synchronization state for data it is authorized to access.

For Group synchronization:

```text
Device
   ↓
User
   ↓
GroupMember
   ↓
Group
```

The synchronization engine must respect this boundary.

### 13.59 SyncState Storage

The Android local database should store SyncState in:

```text
sync_states
```

The backend may maintain corresponding server-side synchronization state where required.

Peer-specific synchronization metadata may be stored locally on Android.

### 13.60 Conceptual Local Schema

```text
sync_states
├── id
├── device_id
├── scope_type
├── scope_id
├── channel
├── peer_device_id
├── cursor
├── status
├── last_successful_sync_at
├── last_attempt_at
├── error_code
└── error_message
```

`peer_device_id` is applicable when:

```text
channel = PEER
```

and may be `NULL` for:

```text
channel = BACKEND
```

### 13.61 SyncState Uniqueness

A synchronization state must be uniquely identifiable by its synchronization context.

Conceptually:

```text
DEVICE
+
SCOPE
+
CHANNEL
+
PEER (when applicable)
```

Example Backend:

```text
(deviceId, scopeType, scopeId, channel)
```

Example Peer:

```text
(deviceId, peerDeviceId, scopeType, scopeId, channel)
```

The exact database constraint will be finalized during implementation.

### 13.62 Backend SyncState

The backend may maintain synchronization state to support:

- Device synchronization.
- Change cursors.
- Incremental synchronization.
- Recovery.
- Device-specific progress.
- Operational monitoring.

The backend does not need to mirror every local Android SyncState field exactly.

### 13.63 Server Cursor

The backend may issue an authoritative cursor/sequence representing accepted server-side changes.

Conceptually:

```text
Server Change
      ↓
Sequence
      ↓
Cursor
```

Clients use this cursor to request incremental changes.

### 13.64 Cursor Expiration

A cursor may become invalid due to:

```text
Retention policy
Server reset
Migration
Protocol version change
Corruption
```

If a cursor becomes invalid:

```text
Incremental Sync
      ↓
Cursor Invalid
      ↓
Full/Recovery Synchronization
```

The exact recovery mechanism will be finalized later.

### 13.65 Peer Cursor

Peer synchronization may use a different cursor mechanism.

For example:

```text
Peer Device D2
      ↓
Last known Operation Set
```

Peer synchronization may therefore use:

```text
Operation IDs
Versions
Peer-specific synchronization markers
```

rather than the same server cursor.

The abstraction remains:

```text
SyncState
```

while the channel-specific implementation may differ.

### 13.66 SyncState and Operation Queue

The relationship is:

```text
SyncState
      │
      ├── tracks synchronization progress
      │
      ▼
SyncOperation Queue
      │
      ├── PENDING
      ├── IN_PROGRESS
      ├── FAILED
      └── CONFLICT
```

The SyncState does not replace the operation queue.

### 13.67 SyncState and Data Integrity

SyncState metadata must never be allowed to make Domain data inconsistent.

For example:

```text
Cursor advanced
but Expense not saved
```

is invalid.

Therefore remote data application and cursor advancement must be coordinated transactionally.

### 13.68 SyncState and UI

The UI may display synchronization information derived from SyncState.

Examples:

```text
Synced
Syncing...
Offline
Sync failed
Conflict needs attention
```

However, UI strings should be mapped from SyncState values rather than stored as UI-specific states.

### 13.69 User-Facing Synchronization Status

Possible UI mapping:

```text
SYNCED
    ↓
"Synced"

SYNCING
    ↓
"Syncing..."

FAILED
    ↓
"Sync failed"

BLOCKED
    ↓
"Action required"

No Connectivity
    ↓
"Offline"
```

The exact UI wording will be defined in the UI Design.

### 13.70 SyncState Example — Fully Synced

```text
Device:
D001

Scope:
GROUP G100

Channel:
BACKEND

Cursor:
C500

Status:
SYNCED

Last Successful Sync:
2026-08-25T10:30:00Z

Last Attempt:
2026-08-25T10:30:00Z

Error:
NULL
```

### 13.71 SyncState Example — Offline

```text
Device:
D001

Scope:
GROUP G100

Channel:
BACKEND

Cursor:
C500

Status:
FAILED

Last Successful Sync:
2026-08-25T10:30:00Z

Last Attempt:
2026-08-25T10:35:00Z

Error:
NETWORK_UNAVAILABLE
```

The Group's local data remains fully usable.

### 13.72 SyncState Example — Conflict

```text
Device:
D001

Scope:
GROUP G100

Channel:
BACKEND

Cursor:
C500

Status:
BLOCKED

Last Successful Sync:
2026-08-25T10:30:00Z

Last Attempt:
2026-08-25T10:40:00Z

Error:
CONFLICT
```

The exact behavior may allow unrelated operations to continue while the conflicting operation remains unresolved.

### 13.73 SyncState Example — Peer

```text
Device:
D001

Peer:
D002

Scope:
GROUP G100

Channel:
PEER

Cursor:
P200

Status:
SYNCED

Last Successful Sync:
2026-08-25T10:45:00Z
```

### 13.74 Synchronization Recovery

Recovery should follow:

```text
Load SyncState
      ↓
Determine Last Known Cursor
      ↓
Determine Pending Operations
      ↓
Determine Connectivity
      ↓
Resume Push/Pull
```

The system should not restart synchronization from zero unless required.

### 13.75 SyncState Reset

A SyncState may need to be reset when:

```text
Full Resync
Invalid Cursor
Data Recovery
Protocol Migration
Explicit Recovery
```

Resetting SyncState must not blindly delete Domain data.

The recovery strategy must preserve financial records whenever possible.

### 13.76 SyncState Versioning

The SyncState schema itself may evolve over application versions.

Database migrations must preserve:

```text
cursor
lastSuccessfulSyncAt
pending synchronization information
```

where compatible.

### 13.77 SyncState and App Version

Different application versions may use different synchronization protocols.

The synchronization protocol may therefore expose:

```text
protocolVersion
```

where required.

The exact implementation will be finalized in the API/Protocol Design.

### 13.78 SyncState and Multi-Device User

A User may eventually use multiple devices.

Each device maintains independent synchronization progress.

Example:

```text
User U1

Device D1
    Group G1
    Cursor C100

Device D2
    Group G1
    Cursor C80
```

D1 and D2 may be at different synchronization positions.

This is expected.

### 13.79 SyncState and Eventual Convergence

Different devices may temporarily have:

```text
Different Cursor
Different Local State
Different Pending Operations
```

This does not violate the architecture.

The requirement is that authorized devices eventually converge after exchanging all required valid changes.

### 13.80 SyncState and Derived Balances

SyncState must not store:

```text
User Balance
Group Balance
Total Expense
```

as authoritative synchronization state.

These are derived from Domain entities.

```text
Expenses
+
Splits
+
Settlements
        ↓
Balance
```

### 13.81 SyncState Data Model Summary

```text
SyncState
│
├── Identity
│   ├── id
│   └── deviceId
│
├── Scope
│   ├── scopeType
│   ├── scopeId
│   └── peerDeviceId
│
├── Channel
│   └── channel
│
├── Progress
│   └── cursor
│
├── Status
│   └── status
│
└── Timing / Errors
    ├── lastSuccessfulSyncAt
    ├── lastAttemptAt
    ├── errorCode
    └── errorMessage
```

### 13.82 SyncState vs SyncOperation Summary

| Concept | SyncOperation | SyncState |
|---|---|---|
| Represents | One change | Synchronization progress |
| Identity | Operation ID | SyncState ID |
| Entity ID | Yes | No |
| Version | Yes | No, except protocol metadata if required |
| Payload | Yes | No |
| Cursor | No | Yes |
| Retry state | Yes | Overall sync state |
| Conflict | Can produce conflict | Can reflect blocked state |
| Persistence | Local + backend where required | Local + backend where required |
| Primary purpose | Change propagation | Progress tracking |

### 13.83 SyncState Invariants

The following rules are mandatory for V1:

- SyncState represents synchronization progress, not Domain state.
- SyncOperation represents an individual Domain change.
- Every SyncState must belong to a Device.
- Synchronization state must be scoped appropriately.
- Backend and Peer are distinct synchronization channels.
- Peer synchronization may require peer-specific state.
- A cursor is not an Entity ID.
- A cursor is not an Operation ID.
- A cursor is not an Entity Version.
- A cursor must advance only after corresponding remote changes are successfully applied.
- Remote data application and cursor advancement should occur in the same transaction where practical.
- A failed remote transaction must not advance the cursor.
- SyncState must survive application restart.
- SyncState must survive device restart.
- Synchronization progress must not depend on in-memory state.
- One Group's synchronization failure must not prevent local use of another Group.
- One synchronization channel being unavailable must not prevent use of another available channel.
- Internet availability does not guarantee synchronization success.
- Lack of Internet connectivity does not prevent local application functionality.
- Authentication state and synchronization state are separate concerns.
- SyncState must not store authoritative financial balances.
- Synchronization status must not be used as a replacement for Domain state.
- Full resynchronization must not blindly delete valid local financial data.
- Synchronization state must support eventual convergence across authorized devices.

## 14. Conflict Data Model

### 14.1 Purpose

This section defines the Conflict model used by SplitSync V1 to detect, represent, track, and resolve synchronization conflicts.

A conflict occurs when two or more valid changes to the same logical entity cannot be safely combined automatically.

The Conflict model must ensure that:

- Concurrent changes are detected.
- Valid local data is not silently overwritten.
- Financial data is not corrupted.
- Conflicting operations can be identified.
- Conflicts can be resolved deterministically.
- Resolved state can be synchronized to other authorized devices.
- Conflict information survives application restart.
- Conflict handling remains independent of the synchronization transport.

### 14.2 Core Principle

The fundamental rule is:

```text
Never silently overwrite a valid concurrent change.
```

When a conflict is detected:

```text
Incoming Operation
        ↓
Version Check
        ↓
Conflict Detected
        ↓
Classify Conflict
        ↓
Automatic Resolution
        OR
Manual Resolution
        ↓
Apply Final State
        ↓
Continue Synchronization
```

### 14.3 What Is a Conflict?

A conflict occurs when an incoming operation was created from a previous entity version, but the receiving device already contains another valid change based on that same or a newer state.

Example:

```text
Initial:

Expense E1
Version = 5
Amount = ₹1000
```

Device A works offline:

```text
₹1000 → ₹1200
```

Device B works offline:

```text
₹1000 → ₹1500
```

Both changes originate from:

```text
baseVersion = 5
```

Therefore:

```text
Device A → Version 6
Device B → Version 6
```

When both changes are synchronized, the system must recognize the concurrent modification.

### 14.4 Conflict Structure

The conceptual V1 structure is:

```text
Conflict
├── conflictId
├── entityType
├── entityId
├── scopeId
├── sourceDeviceId
├── localVersion
├── remoteVersion
├── localOperationId
├── remoteOperationId
├── localPayload
├── remotePayload
├── conflictType
├── status
├── resolutionType
├── resolvedPayload
├── detectedAt
├── resolvedAt
└── resolutionMetadata
```

### 14.5 Conflict ID

Every conflict must have a unique:

```text
conflictId
```

The Conflict ID is different from:

```text
Entity ID
Operation ID
Device ID
```

Example:

```text
Entity:
E123

Operation:
O500

Conflict:
C900
```

These represent different concepts.

### 14.6 Entity Type

The Conflict identifies the affected Domain entity:

```text
entityType
```

V1 values:

```text
GROUP
GROUP_MEMBER
EXPENSE
EXPENSE_SPLIT
SETTLEMENT
```

User and Device conflicts are generally infrastructure/authentication concerns and are not the primary V1 financial conflict model.

### 14.7 Entity ID

The:

```text
entityId
```

identifies the exact entity for which the conflict exists.

Example:

```text
entityType = EXPENSE
entityId   = E123
```

means:

```text
Expense E123 has a conflict.
```

### 14.8 Scope ID

For Group-owned entities, the Conflict should identify the relevant Group:

```text
scopeId = groupId
```

Example:

```text
Expense E123
      ↓
Group G100
      ↓
Conflict Scope = G100
```

This allows conflict handling and authorization to remain Group-aware.

### 14.9 Source Device ID

The Conflict records the Device that originated the incoming conflicting operation.

Example:

```text
sourceDeviceId = D002
```

This is useful for:

- Conflict attribution.
- Debugging.
- Security checks.
- Peer synchronization.
- Audit information.

### 14.10 Local Version

`localVersion` represents the version currently present on the receiving device/server when the conflict is detected.

Example:

```text
Local:
Version = 6
```

### 14.11 Remote Version

`remoteVersion` represents the version carried by the incoming operation.

Example:

```text
Incoming:
Version = 6
```

A conflict can therefore contain:

```text
localVersion  = 6
remoteVersion = 6
```

when two devices independently created the same next version from Version 5.

### 14.12 Local Operation ID

The Conflict should identify the operation that produced the current local conflicting state where available.

Example:

```text
localOperationId = O100
```

### 14.13 Remote Operation ID

The Conflict must identify the incoming operation:

```text
remoteOperationId = O200
```

This allows the system to trace exactly which operation caused the conflict.

### 14.14 Conflict Type

V1 may classify conflicts into:

```text
CONCURRENT_UPDATE
DELETE_UPDATE
UPDATE_DELETE
DEPENDENCY_CONFLICT
FINANCIAL_CONFLICT
AUTHORIZATION_CONFLICT
```

The exact classification may be extended as implementation requirements become clearer.

### 14.15 Concurrent Update Conflict

Occurs when two devices modify the same entity from the same previous version.

Example:

```text
Version 5
   │
   ├── Device A → Version 6
   │
   └── Device B → Version 6
```

This is the primary V1 conflict scenario.

### 14.16 Delete-Update Conflict

Occurs when one device deletes an entity while another device updates it.

Example:

```text
Version 5
   │
   ├── Device A → DELETE
   │
   └── Device B → UPDATE
```

The system must not silently choose one operation without applying the defined resolution rule.

### 14.17 Update-Delete Conflict

The inverse scenario:

```text
Version 5
   │
   ├── Device A → UPDATE
   │
   └── Device B → DELETE
```

The final result must be determined by the defined conflict policy.

### 14.18 Dependency Conflict

A dependency conflict occurs when an operation cannot safely be applied because a required related entity is unavailable or inconsistent.

Example:

```text
ExpenseSplit S1
        ↓
Expense E1
        ↓
Missing
```

The system may treat this as a dependency problem rather than a true concurrent data conflict.

### 14.19 Financial Conflict

Financial conflicts receive special treatment because incorrect resolution can change:

```text
Expense Amount
Expense Allocation
Settlement Amount
Group Balance
```

Examples:

```text
Expense amount changed concurrently
Expense participant changed concurrently
Expense split changed concurrently
Settlement changed concurrently
```

Financial conflicts must never be resolved using arbitrary last-write-wins behavior.

### 14.20 Authorization Conflict

An operation may become invalid because authorization changed between creation and synchronization.

Example:

```text
User was Group Member
       ↓
Membership removed
       ↓
User creates Expense offline
       ↓
Later synchronizes
```

The operation may no longer be authorized.

This is logically different from a version conflict and should be handled as an authorization failure or conflict according to the final security rules.

### 14.21 Conflict Status

V1 Conflict statuses:

```text
OPEN
RESOLVING
RESOLVED
REJECTED
IGNORED
```

### 14.22 OPEN

The conflict has been detected but has not yet been resolved.

```text
OPEN
```

The conflicting state must remain available for resolution.

### 14.23 RESOLVING

The system or user is actively resolving the conflict.

```text
RESOLVING
```

This state prevents multiple resolution processes from modifying the same conflict simultaneously.

### 14.24 RESOLVED

The conflict has been successfully resolved.

```text
RESOLVED
```

A final valid Domain state has been selected or generated.

### 14.25 REJECTED

The incoming operation was intentionally rejected.

Example:

```text
Unauthorized Expense Update
```

The final local/authoritative state remains unchanged.

### 14.26 IGNORED

An operation may be ignored when it is no longer relevant.

This status should be used carefully and must not be used as a shortcut for unresolved financial conflicts.

### 14.27 Resolution Type

Recommended V1 resolution types:

```text
LOCAL_WINS
REMOTE_WINS
MERGED
REJECT_REMOTE
REJECT_LOCAL
MANUAL
```

Not every resolution type is valid for every entity.

### 14.28 Local Wins

The existing local state remains authoritative.

Example:

```text
Local:
₹1200

Remote:
₹1500

Resolution:
LOCAL_WINS
```

The remote operation is rejected or transformed according to the synchronization protocol.

### 14.29 Remote Wins

The incoming state becomes authoritative.

Example:

```text
Local:
₹1200

Remote:
₹1500

Resolution:
REMOTE_WINS
```

This should only be used where the conflict policy explicitly allows it.

### 14.30 Merged

Two changes are combined into a new valid state.

Example:

```text
Local:
Description = "Hotel"

Remote:
Description = "Hotel Delhi"

Merged:
Description = "Hotel Delhi"
```

Automatic merging is appropriate only when the merge is deterministic and does not create financial ambiguity.

### 14.31 Reject Remote

The incoming operation is rejected:

```text
REJECT_REMOTE
```

The existing local state remains.

### 14.32 Reject Local

The local conflicting state is replaced/rejected in favor of the incoming valid state.

This must be used only after applying the appropriate conflict policy.

### 14.33 Manual Resolution

Some conflicts may require explicit user decision.

Example:

```text
Expense amount:
Local  = ₹1200
Remote = ₹1500
```

The application may present:

```text
Keep Local
Keep Remote
Choose Another Amount
```

The final resolution becomes a new valid Domain state.

### 14.34 Financial Conflict Rule

For financial entities:

```text
Do not automatically use timestamp-based last-write-wins.
```

This is important because:

```text
Device clock A
```

and:

```text
Device clock B
```

may differ.

More importantly, choosing the latest timestamp could silently change financial records.

### 14.35 Conflict Detection

The basic conflict detection process is:

```text
Incoming Operation
        ↓
Find Entity
        ↓
Compare Base Version
        ↓
Compare Current Version
        ↓
Conflict?
   ┌────┴────┐
   │         │
  No        Yes
   │         │
Apply     Create Conflict
```

### 14.36 No-Conflict Example

Current:

```text
Version = 5
```

Incoming:

```text
baseVersion = 5
entityVersion = 6
```

Result:

```text
No concurrent version conflict
```

The operation can proceed after normal validation.

### 14.37 Conflict Example

Current:

```text
Version = 6
```

Incoming:

```text
baseVersion = 5
entityVersion = 6
```

Result:

```text
Potential concurrent conflict
```

The system must determine whether the incoming operation is:

```text
Duplicate
Older
Concurrent
Invalid
```

### 14.38 Duplicate vs Conflict

A duplicate operation is not a conflict.

Example:

```text
Operation O100
```

is received again.

If O100 has already been processed:

```text
ALREADY_APPLIED
```

No Conflict record is required.

### 14.39 Older Operation

An operation may be older than the current state.

Example:

```text
Current:
Version = 8

Incoming:
baseVersion = 5
entityVersion = 6
```

The operation may be stale.

The system must determine whether it is:

```text
Already represented
Obsolete
Conflict
```

It must not blindly overwrite Version 8 with Version 6.

### 14.40 Conflict Detection and Operation ID

Operation ID must be checked before conflict processing.

Recommended order:

```text
Receive Operation
      ↓
Check Operation ID
      ↓
Already Applied?
      │
   ┌──┴──┐
  Yes    No
   │      │
Return   Continue
          ↓
   Version Validation
```

### 14.41 Conflict Creation Transaction

Conflict creation must be transactional with the operation-processing result.

Conceptually:

```text
BEGIN TRANSACTION

Detect Conflict
       ↓
Create Conflict
       ↓
Mark Operation as CONFLICT
       ↓
Persist Required Metadata
       ↓
COMMIT
```

The entity must not be partially modified.

### 14.42 Conflict Data Preservation

When a conflict is detected, the system should preserve enough information to reconstruct both competing states.

Conceptually:

```text
Local State
    +
Remote State
    +
Operation Metadata
```

This prevents information loss during resolution.

### 14.43 Local Payload

`localPayload` represents the state currently known by the receiving side.

Example:

```json
{
  "amountMinor": 120000,
  "description": "Hotel"
}
```

### 14.44 Remote Payload

`remotePayload` represents the incoming state.

Example:

```json
{
  "amountMinor": 150000,
  "description": "Hotel Delhi"
}
```

### 14.45 Payload Storage

Conflict payloads may be stored as:

```text
JSON
```

or an equivalent serialized representation.

The exact database type will follow the backend database implementation.

### 14.46 Conflict Snapshot Principle

Conflict payloads are snapshots used for resolution.

They must not automatically become new Domain entities.

Conceptually:

```text
Conflict Snapshot
      ↓
Resolution
      ↓
Final Domain State
```

### 14.47 Resolution Must Produce Valid Domain State

The result of conflict resolution must pass all normal Domain validations.

For an Expense:

```text
Amount > 0
Currency valid
Payer valid
Group valid
Splits valid
SUM(Splits) = Amount
```

Conflict resolution must not bypass these rules.

### 14.48 Manual Conflict Resolution

When manual resolution is required:

```text
Conflict
   ↓
Show User
   ↓
User Chooses Resolution
   ↓
Validate Resolution
   ↓
Create Final State
   ↓
Persist Transaction
```

### 14.49 Resolution Is a New Change

The result of a conflict should be treated as a new synchronized state/change where required.

Example:

```text
Conflict:
Local Version 6
Remote Version 6
```

Resolution produces:

```text
Final Version
```

The resolution must be propagated to other authorized devices.

The exact version/operation strategy will be finalized in the Synchronization Protocol.

### 14.50 Conflict Resolution Operation

A resolved conflict may generate a new SyncOperation:

```text
Conflict
   ↓
Resolution
   ↓
New SyncOperation
```

Example:

```text
Conflict C100
      ↓
User chooses final amount ₹1300
      ↓
New Expense State
      ↓
New SyncOperation O300
```

This ensures all devices can eventually converge on the final state.

### 14.51 Resolution Idempotency

Conflict resolution must also be idempotent.

The same resolution operation must not:

```text
Create duplicate Expense
Create duplicate Split
Create duplicate Settlement
```

### 14.52 Conflict Resolution and Peer Sync

A conflict can occur through:

```text
Backend synchronization
```

or:

```text
Peer synchronization
```

The same Domain conflict rules must apply.

Example:

```text
Device A ↔ Device B
```

must not use a completely different financial conflict model from:

```text
Device A ↔ Backend
```

### 14.53 Conflict Resolution and Backend

The backend may act as the synchronization authority when Internet synchronization is used.

However, the backend must still apply the same conflict rules and must not blindly overwrite client state.

### 14.54 Conflict Resolution and Offline Mode

A conflict does not make the application unusable.

Example:

```text
Expense E1
   ↓
Conflict
```

The user should still be able to:

```text
View Group
Create another Expense
View existing Expenses
Record other valid changes
```

Only the conflicting operation/state should require resolution where possible.

### 14.55 Conflict Isolation

A conflict should be isolated to the smallest practical synchronization scope.

Example:

```text
Expense E1
   ↓
Conflict
```

should not automatically block:

```text
Expense E2
Expense E3
Settlement S1
```

unless they directly depend on E1.

### 14.56 Conflict Dependencies

Some conflicts may affect dependent entities.

Example:

```text
Expense E1
   ↓
ExpenseSplits S1, S2
```

If the Expense amount changes:

```text
Expense Splits
```

may need to be revalidated.

The resolution process must preserve aggregate consistency.

### 14.57 Expense Conflict

For an Expense conflict, validate:

```text
Group
Payer
Amount
Currency
Description
Expense Date
Participants
Splits
```

The final state must remain internally consistent.

### 14.58 Expense Split Conflict

An ExpenseSplit conflict must be validated against its parent Expense.

Example:

```text
Expense:
₹1000

Splits:
₹600
₹400
```

A conflicting state:

```text
₹700
₹500
```

must be rejected because:

```text
₹700 + ₹500 ≠ ₹1000
```

unless the parent Expense amount is also changed consistently.

### 14.59 Settlement Conflict

A Settlement conflict must validate:

```text
Group
From User
To User
Amount
Currency
Settlement Date
```

The system must not merge two settlements into a larger amount merely because they appear to represent similar changes.

Two legitimate settlements may be separate Domain events.

### 14.60 Duplicate Settlement Protection

This is especially important for settlements.

If:

```text
Settlement S1
```

is delivered twice, the result must remain:

```text
One Settlement S1
```

not:

```text
Two Settlements
```

Operation ID and Entity ID are both important here.

### 14.61 Group Membership Conflict

Membership conflicts may occur when:

```text
Device A → Add User
Device B → Remove User
```

The final state must follow explicit membership conflict rules.

Financial operations depending on the membership must be validated against the resulting membership state.

### 14.62 Delete Conflict

Deletion requires special handling because a delete may remove information required to compare states.

Therefore V1 must preserve:

```text
Entity ID
Version
Operation ID
Deleted At
Origin Device
```

through a tombstone or equivalent mechanism.

### 14.63 Tombstone

A deleted entity may remain represented as:

```text
Entity
├── id
├── version
├── deletedAt
└── synchronization metadata
```

This allows another device to learn:

```text
Entity was deleted
```

rather than interpreting absence as:

```text
Entity never existed
```

### 14.64 Delete vs Update Example

Initial:

```text
Expense E1
Version 5
```

Device A:

```text
DELETE
baseVersion = 5
```

Device B:

```text
UPDATE
baseVersion = 5
```

After synchronization:

```text
Conflict
Type = DELETE_UPDATE
```

The system must apply the defined policy.

### 14.65 Automatic Resolution Criteria

A conflict may be automatically resolved only when:

- The resolution rule is deterministic.
- The result is semantically valid.
- No financial ambiguity is introduced.
- All affected entities remain consistent.
- All authorized devices can derive the same result.

### 14.66 Manual Resolution Criteria

Manual resolution should be considered when:

- Financial values conflict.
- Multiple valid user edits cannot be safely merged.
- Delete/update semantics are ambiguous.
- Automatic resolution could silently change financial history.
- The Domain model cannot determine a safe result.

### 14.67 Conflict Resolution Example

```text
Initial Expense:

E1
Amount = ₹1000
Version = 5
```

Offline changes:

```text
Device A:
Amount = ₹1200

Device B:
Amount = ₹1500
```

Conflict:

```text
Conflict ID:
C100

Entity:
EXPENSE E1

Local:
₹1200

Remote:
₹1500

Status:
OPEN
```

User chooses:

```text
₹1200
```

Resolution:

```text
Resolution Type:
LOCAL_WINS
```

A new valid synchronized state is then produced.

### 14.68 Conflict Resolution Example — Merge

Initial:

```text
Description = "Hotel"
```

Device A:

```text
Description = "Hotel Delhi"
```

Device B:

```text
Description = "Hotel - 2 nights"
```

If the system has a deterministic merge strategy:

```text
Merged Result
```

may be possible.

However, V1 should not introduce arbitrary string-merging rules merely to avoid manual conflicts.

### 14.69 Conflict Resolution Example — Independent Fields

If two devices modify different non-conflicting fields:

```text
Device A:
description changed

Device B:
expenseDate changed
```

the system may be able to merge:

```text
description from A
expenseDate from B
```

only if the merge semantics are explicitly defined and deterministic.

### 14.70 Field-Level Merge

Field-level merge should not be assumed for all entities.

It may be introduced for safe metadata fields such as:

```text
description
```

or:

```text
display name
```

but financial fields require stricter handling.

### 14.71 Financial Fields

The following should generally require strict conflict handling:

```text
amount
currency
payer
participants
split amounts
settlement amount
fromUser
toUser
```

A simple field-level merge must not modify these independently unless the Domain rules explicitly allow it.

### 14.72 Conflict Resolution Ordering

Recommended flow:

```text
Detect Conflict
      ↓
Classify Entity
      ↓
Classify Conflict Type
      ↓
Check Automatic Resolution Rule
      │
 ┌────┴─────┐
 │          │
Safe       Unsafe
 │          │
Auto       Manual
Resolve    Resolution
 │          │
 └────┬─────┘
      ↓
Validate Final State
      ↓
Persist Transaction
      ↓
Create/Update Sync Metadata
      ↓
Continue Synchronization
```

### 14.73 Conflict Resolution Transaction

Resolution must be atomic:

```text
BEGIN TRANSACTION

Validate Resolution
        ↓
Apply Final Entity State
        ↓
Update Related Entities
        ↓
Record Resolution
        ↓
Create New SyncOperation if required
        ↓
Mark Conflict RESOLVED
        ↓
COMMIT
```

Failure:

```text
ROLLBACK
```

### 14.74 Conflict Resolution Failure

If the selected resolution is invalid:

```text
Do not modify Domain state
```

The Conflict remains:

```text
OPEN
```

or:

```text
RESOLVING
```

according to the current processing state.

### 14.75 Conflict Resolution History

The system should retain:

```text
Conflict ID
Original Local State
Original Remote State
Resolution Type
Final State
Resolution Time
```

where required for synchronization recovery and diagnostics.

### 14.76 Conflict Cleanup

Resolved conflicts may eventually be archived or removed according to retention rules.

However, cleanup must not occur while the Conflict is required for:

```text
Synchronization
Recovery
Audit
Debugging
```

The exact retention policy will be defined later.

### 14.77 Conflict and Cursor

A conflict must not cause the synchronization cursor to advance past data that has not been safely processed.

Example:

```text
Cursor = C500

Receive:
C501 → Valid
C502 → Conflict
C503 → Valid
```

The system must follow a defined strategy.

V1 should prefer processing in an ordered transaction so that:

```text
C502
```

does not become silently skipped.

### 14.78 Conflict and Independent Operations

A conflict should not unnecessarily block unrelated operations.

Example:

```text
C502
    ↓
Expense E1 Conflict

C503
    ↓
Expense E2 Valid
```

The synchronization engine may process E2 if doing so is safe and does not violate cursor/dependency semantics.

### 14.79 Conflict Data Model

Conceptual database representation:

```text
conflicts
├── id
├── entity_type
├── entity_id
├── scope_id
├── source_device_id
├── local_version
├── remote_version
├── local_operation_id
├── remote_operation_id
├── local_payload
├── remote_payload
├── conflict_type
├── status
├── resolution_type
├── resolved_payload
├── detected_at
├── resolved_at
└── resolution_metadata
```

### 14.80 Conflict Relationship Diagram

```text
                  ┌───────────────────┐
                  │   SyncOperation   │
                  └─────────┬─────────┘
                            │
                     Conflict Detected
                            │
                            ▼
                  ┌───────────────────┐
                  │      Conflict     │
                  ├───────────────────┤
                  │ conflictId        │
                  │ entityType        │
                  │ entityId          │
                  │ localVersion      │
                  │ remoteVersion     │
                  │ localOperationId  │
                  │ remoteOperationId │
                  │ localPayload      │
                  │ remotePayload     │
                  │ conflictType      │
                  │ status             │
                  └─────────┬─────────┘
                            │
                      Resolution
                            │
                            ▼
                  ┌───────────────────┐
                  │   Final State     │
                  └─────────┬─────────┘
                            │
                            ▼
                  ┌───────────────────┐
                  │  SyncOperation    │
                  │   if required     │
                  └───────────────────┘
```

### 14.81 Conflict Data Model Summary

```text
Conflict
│
├── Identity
│   ├── conflictId
│   ├── entityType
│   └── entityId
│
├── Scope
│   └── scopeId
│
├── Source
│   ├── sourceDeviceId
│   ├── localOperationId
│   └── remoteOperationId
│
├── Versions
│   ├── localVersion
│   └── remoteVersion
│
├── States
│   ├── localPayload
│   ├── remotePayload
│   └── resolvedPayload
│
├── Classification
│   ├── conflictType
│   └── status
│
└── Resolution
    ├── resolutionType
    ├── detectedAt
    ├── resolvedAt
    └── resolutionMetadata
```

### 14.82 Conflict Data Model Invariants

The following rules are mandatory for V1:

- Every Conflict must have a unique Conflict ID.
- Every Conflict must identify the affected Entity.
- Every Conflict must identify the relevant synchronization scope where applicable.
- A Conflict must retain enough metadata to identify both competing operations.
- Local and remote versions must be preserved when a version conflict is detected.
- Local and remote states must be preserved when required for resolution.
- Duplicate operations must not create duplicate Conflict records unnecessarily.
- Duplicate delivery is not itself a conflict.
- Conflict detection must occur before silently overwriting a newer state.
- Financial conflicts must receive stricter handling than ordinary metadata conflicts.
- Timestamp-based last-write-wins must not be the default strategy for financial data.
- Conflict resolution must produce a valid Domain state.
- Conflict resolution must execute transactionally.
- Conflict resolution must not bypass Domain validation.
- Manual resolution must be supported where automatic resolution is unsafe.
- Automatic resolution must be deterministic.
- Conflict resolution must be idempotent.
- A resolved conflict may generate a new SyncOperation when required.
- Conflict handling must work through both Backend and Peer synchronization channels.
- A conflict in one entity must not unnecessarily block unrelated entities.
- Delete operations require tombstone or equivalent synchronization metadata.
- Delete/update conflicts must be explicitly handled.
- Conflict information must survive application restart.
- Conflict information must survive device restart.
- Unresolved financial conflicts must not silently change financial history.
- Final resolved state must eventually propagate to all authorized devices.
- Conflict handling must preserve eventual convergence across authorized devices.

## 15. Peer-to-Peer Communication Model

### 15.1 Purpose

This section defines the Peer-to-Peer (P2P) communication model for SplitSync V1.

The purpose of P2P communication is to allow authorized SplitSync devices to exchange profile, Group, Expense, Expense Split, Settlement, and synchronization data without requiring Internet connectivity.

The primary use case is:

```text
Internet unavailable
        ↓
Users are physically nearby
        ↓
Devices connect through local Wi-Fi / Hotspot
        ↓
SplitSync devices discover each other
        ↓
Authorized devices exchange synchronization data
        ↓
Local databases converge
```

P2P communication is an additional synchronization channel.

It must not replace the core local-first architecture.

### 15.2 Core Principle

The fundamental principle is:

```text
Local First
    +
Peer Synchronization
    +
Backend Synchronization
```

The application must remain fully usable even when:

```text
Internet = unavailable
Peer = unavailable
```

P2P connectivity improves the ability to synchronize nearby devices but is not required for core expense management.

### 15.3 P2P Communication Architecture

Conceptually:

```text
┌─────────────────────┐
│     Device A        │
│                     │
│  SplitSync App      │
│  Room Database      │
│  Sync Engine        │
└──────────┬──────────┘
           │
           │ Local Network
           │
           ▼
┌─────────────────────┐
│     Device B        │
│                     │
│  SplitSync App      │
│  Room Database      │
│  Sync Engine        │
└─────────────────────┘
```

The communication path is:

```text
Android App
    ↓
P2P Discovery
    ↓
Peer Connection
    ↓
Authentication / Authorization
    ↓
Synchronization Handshake
    ↓
Operation Exchange
    ↓
Validation
    ↓
Local Transaction
    ↓
Synchronization State Update
```

### 15.4 Supported Connectivity

V1 is designed to support nearby communication through mechanisms available on Android, such as:

```text
Local Wi-Fi
Wi-Fi Hotspot
Wi-Fi Direct
```

The exact Android transport technology will be selected during implementation after evaluating:

- Android version compatibility.
- Device support.
- Discovery reliability.
- Security.
- Battery consumption.
- Background restrictions.
- Ease of implementation.

The synchronization Domain model must remain independent of the selected transport.

### 15.5 Transport Independence

The P2P synchronization layer must not directly modify Domain entities.

The architecture is:

```text
P2P Transport
      ↓
P2P Protocol
      ↓
Sync Engine
      ↓
Domain Validation
      ↓
Room Database
```

Not:

```text
P2P Transport
      ↓
Room Database
```

This separation ensures that the transport can be changed later without redesigning the expense domain.

### 15.6 P2P Roles

During a P2P session, devices may temporarily act as:

```text
Initiator
Responder
```

The roles are session-specific.

A device being the Initiator does not make it the owner of the Group or the authority over financial data.

### 15.7 Initiator

The Initiator starts the P2P connection.

Conceptually:

```text
Device A
    ↓
Discover Device B
    ↓
Request Connection
```

The Initiator may be the device whose user explicitly selects another nearby user or whose synchronization engine starts a peer synchronization session.

### 15.8 Responder

The Responder accepts or rejects the incoming connection.

Conceptually:

```text
Device B
    ↓
Receive Connection Request
    ↓
Validate Request
    ↓
Accept / Reject
```

The Responder must not automatically trust every incoming connection.

### 15.9 Discovery

P2P discovery allows nearby SplitSync devices to become visible to one another.

Conceptually:

```text
Device A
    ↓
Start Discovery
    ↓
Nearby SplitSync Devices
    ↓
Device B
Device C
Device D
```

Discovery only identifies nearby devices.

It does not automatically authorize synchronization.

### 15.10 Discovery Information

The minimum discovery information should be limited to what is required for identification and connection establishment.

Potential information:

```text
deviceId
displayName
userId or user identification reference
protocolVersion
device capability information
```

Sensitive information must not be broadcast unnecessarily.

### 15.11 User Profile Discovery

The V1 requirement is that nearby SplitSync users should be able to identify one another.

Example:

```text
Device A
    ↓
Nearby Users

Rahul
Praveen
Amit
```

The displayed information should come from the user's local profile and should not expose unnecessary private data.

### 15.12 Phone Number During P2P Discovery

The phone number must not be broadcast as the primary discovery identifier.

Instead:

```text
Device Identity
+
User Identity
```

should be used.

If phone number matching is required for a specific user-flow, it should be exchanged only after an appropriate authenticated/authorized interaction.

### 15.13 Discovery vs Synchronization

Discovery and synchronization are separate stages.

```text
Discovery
    ↓
"Who is nearby?"

Synchronization
    ↓
"What authorized data should be exchanged?"
```

A discovered device must not automatically receive:

```text
Expenses
Groups
Settlements
```

### 15.14 Connection Establishment

Conceptually:

```text
Discover Peer
      ↓
Select Peer
      ↓
Connection Request
      ↓
Peer Accepts
      ↓
Secure Session Establishment
      ↓
Synchronization Handshake
```

### 15.15 P2P Session

A P2P session represents one active communication session between two devices.

Conceptual model:

```text
P2PSession
├── sessionId
├── localDeviceId
├── remoteDeviceId
├── protocolVersion
├── connectionState
├── authenticated
├── startedAt
└── lastActivityAt
```

### 15.16 Session ID

Each P2P session should have a unique:

```text
sessionId
```

The Session ID identifies the communication session.

It is different from:

```text
Device ID
Operation ID
Group ID
```

### 15.17 Connection States

Recommended P2P connection states:

```text
DISCOVERING
CONNECTING
CONNECTED
AUTHENTICATING
SYNCHRONIZING
DISCONNECTING
DISCONNECTED
FAILED
```

### 15.18 Discovery State

```text
DISCOVERING
```

means the device is searching for nearby SplitSync peers.

### 15.19 Connecting State

```text
CONNECTING
```

means the connection is being established.

### 15.20 Connected State

```text
CONNECTED
```

means a communication channel exists but synchronization may not yet have started.

### 15.21 Authenticating State

```text
AUTHENTICATING
```

means the devices are establishing trust and verifying peer identity.

### 15.22 Synchronizing State

```text
SYNCHRONIZING
```

means the devices are actively exchanging synchronization information.

### 15.23 Disconnected State

```text
DISCONNECTED
```

means the P2P session has ended.

The application must continue working normally.

### 15.24 Failed State

```text
FAILED
```

means the P2P connection or protocol failed.

A failure must not delete or roll back locally committed financial data.

### 15.25 P2P Authentication

P2P communication must have an authentication mechanism.

The system must not assume:

```text
Same Wi-Fi
    =
Trusted Device
```

A device connected to the same hotspot may be:

```text
Another SplitSync User
Unknown Device
Malicious Device
```

### 15.26 Peer Identity

A peer should be identified using a stable Device identity.

Conceptually:

```text
Peer
├── deviceId
├── userId
└── identity proof
```

The exact cryptographic identity mechanism will be finalized in the Security Architecture and implementation.

### 15.27 Secure Session

After peer authentication:

```text
Authenticated Peer
        ↓
Secure Communication Session
        ↓
Synchronization
```

Synchronization payloads must not be transmitted as unauthenticated plaintext when the transport does not already provide adequate confidentiality and integrity.

### 15.28 P2P Authorization

Authentication answers:

```text
Who is this device?
```

Authorization answers:

```text
What is this device allowed to synchronize?
```

These must remain separate.

### 15.29 Group Authorization

Before synchronizing Group data:

```text
Peer Device
    ↓
User
    ↓
Group Membership
    ↓
Authorization
    ↓
Allowed Group Data
```

A peer must not receive a Group merely because it is nearby.

### 15.30 Group Membership Requirement

A device should only receive shared Group data when the corresponding User/Device has a valid authorization path to that Group.

Example:

```text
Device B
    ↓
User B
    ↓
Group G1 Member
    ↓
Allowed to synchronize G1
```

### 15.31 Group Invitation

P2P communication may support offline Group invitation flows.

Example:

```text
User A creates Group G1
        ↓
Nearby User B
        ↓
Invitation exchanged locally
        ↓
User B accepts
        ↓
Group membership created locally
        ↓
SyncOperation generated
```

The exact invitation protocol will be defined during implementation.

### 15.32 Invitation vs Membership

An invitation is not automatically membership.

Conceptually:

```text
Invitation
    ↓
User Decision
    ↓
Membership
```

The application must not add a user to a Group merely because an invitation packet was received.

### 15.33 P2P Synchronization Scope

P2P synchronization should be limited to the Groups for which both devices have appropriate authorization.

Example:

```text
Device A:
G1, G2, G3

Device B:
G2, G3, G4
```

Potential shared synchronization scopes:

```text
G2
G3
```

There is no reason for Device A to send:

```text
G1
```

to Device B.

### 15.34 Synchronization Handshake

After authentication, devices perform a synchronization handshake.

Conceptually:

```text
Device A
    ↓
Hello
    ↓
Device B
    ↓
Hello Response
    ↓
Capability Exchange
    ↓
Group Scope Exchange
    ↓
Synchronization State Exchange
    ↓
Operation Exchange
```

### 15.35 Handshake Information

The handshake may contain:

```text
protocolVersion
deviceId
userId/reference
supportedFeatures
supportedEntityTypes
supportedOperationTypes
Group synchronization scopes
peer synchronization state
```

The handshake must not expose unrelated sensitive information.

### 15.36 Protocol Version

P2P communication must include:

```text
protocolVersion
```

This allows future versions of SplitSync to determine whether two devices can communicate safely.

Example:

```text
Device A → Protocol 1
Device B → Protocol 1
```

Compatible.

If:

```text
Device A → Protocol 2
Device B → Protocol 1
```

the system must negotiate compatibility or reject the session safely.

### 15.37 Capability Exchange

Devices may exchange supported capabilities.

Example:

```text
Device A:
Expense
ExpenseSplit
Settlement

Device B:
Expense
ExpenseSplit
Settlement
GroupInvitation
```

This allows protocol evolution without assuming every device supports every future feature.

### 15.38 Group Scope Exchange

After authentication, devices determine which Groups may be synchronized.

Conceptually:

```text
Device A Groups
        ∩
Device B Authorized Groups
        ↓
Synchronization Candidates
```

The intersection must then be filtered by actual authorization and protocol rules.

### 15.39 Sync State Exchange

Devices may exchange synchronization state for the relevant Group.

Example:

```text
Device A:
G1 → last known peer state

Device B:
G1 → last known peer state
```

This helps determine which operations need to be exchanged.

### 15.40 Operation Exchange

The core P2P synchronization exchange is:

```text
Device A
    ↓
Operations available to Device B
    ↓
Device B
```

and:

```text
Device B
    ↓
Operations available to Device A
    ↓
Device A
```

Synchronization may therefore be bidirectional.

### 15.41 Bidirectional Synchronization

P2P synchronization is not:

```text
Device A → Device B
```

only.

It is:

```text
Device A ⇄ Device B
```

Both devices may have locally created changes.

### 15.42 Example

Before connection:

```text
Device A:
Expense E1
Expense E2

Device B:
Expense E3
Expense E4
```

After synchronization:

```text
Device A:
E1
E2
E3
E4

Device B:
E1
E2
E3
E4
```

assuming all operations are valid and authorized.

### 15.43 Operation Identity Preservation

When an operation moves through P2P:

```text
Operation O100
```

must remain:

```text
Operation O100
```

The receiving device must not create a new Operation ID merely because it received the operation.

### 15.44 Origin Device Preservation

If Device A creates:

```text
O100
```

and sends it to Device B:

```text
originDeviceId = Device A
```

must remain unchanged.

Device B acts as a recipient, not the originator.

### 15.45 Multi-Hop Synchronization

V1 should be designed so that operations can safely travel through more than one device.

Example:

```text
Device A
    ↓
Device B
    ↓
Device C
```

Operation:

```text
O100
```

must remain:

```text
O100
```

throughout the path.

This is important for eventual convergence in offline environments.

### 15.46 Multi-Hop Security

A device forwarding an operation must not gain additional authority merely because it received the operation.

Example:

```text
A → B → C
```

C must still validate:

```text
Origin
Authorization
Integrity
Group Membership
Operation
```

### 15.47 Operation Filtering

Before sending operations to a peer, the device must filter by:

```text
Group Authorization
Entity Scope
Peer Authorization
Operation State
Protocol Compatibility
```

It must not send the complete local synchronization queue blindly.

### 15.48 Sensitive Data Filtering

P2P synchronization must not transmit:

```text
Unrelated Groups
Unrelated Users
Private account information
Authentication credentials
Backend secrets
```

Only required synchronization data should be transmitted.

### 15.49 User Profile Exchange

When users discover one another, the application may exchange limited profile information.

Example:

```text
User ID
Display Name
Profile Identifier
```

Additional profile information should only be exchanged when necessary.

### 15.50 Profile Identification

The local profile is primarily identified through:

```text
User ID
Device ID
```

Phone number and email are attributes of the User profile rather than the primary synchronization identity.

### 15.51 P2P Expense Synchronization

Expense synchronization follows:

```text
Expense Operation
      ↓
Authorization
      ↓
Version Validation
      ↓
Conflict Detection
      ↓
Apply Expense
      ↓
Apply Expense Splits
      ↓
Commit
```

### 15.52 P2P Settlement Synchronization

Settlement synchronization follows:

```text
Settlement Operation
      ↓
Group Validation
      ↓
User Validation
      ↓
Amount Validation
      ↓
Duplicate Detection
      ↓
Apply
      ↓
Commit
```

### 15.53 P2P Balance Calculation

Balances must not be transmitted as authoritative values.

Instead:

```text
Expenses
+
ExpenseSplits
+
Settlements
        ↓
Local Balance Calculation
```

This ensures both devices derive balances from synchronized source data.

### 15.54 P2P Transaction Boundary

Received operations must be applied transactionally.

Example:

```text
BEGIN TRANSACTION

Validate Operation
      ↓
Validate Authorization
      ↓
Validate Dependencies
      ↓
Apply Domain Changes
      ↓
Update Sync Metadata
      ↓
COMMIT
```

Failure:

```text
ROLLBACK
```

### 15.55 Partial P2P Failure

Suppose:

```text
10 operations
```

are being exchanged and the connection fails after:

```text
6 operations
```

The receiving device must retain the six successfully committed operations.

The remaining operations must be retryable.

Already applied operations must remain idempotent.

### 15.56 P2P Session Resumption

A disconnected session should be resumable.

Conceptually:

```text
Session Interrupted
        ↓
Reconnect
        ↓
Exchange Current Sync State
        ↓
Continue From Last Successful Point
```

The system should not require re-transmitting all historical data unnecessarily.

### 15.57 P2P Synchronization State

Peer synchronization uses the `SyncState` model defined previously.

Conceptually:

```text
Device A
    ↓
Peer Device B
    ↓
Group G1
    ↓
Channel PEER
    ↓
Cursor / Synchronization Marker
```

### 15.58 Peer-Specific Sync State

Because synchronization progress can differ by peer:

```text
Device A ↔ Device B
```

may have:

```text
Peer State AB
```

while:

```text
Device A ↔ Device C
```

has:

```text
Peer State AC
```

These states must remain independent.

### 15.59 P2P Sync State Example

```text
Local Device:
D001

Peer Device:
D002

Group:
G100

Channel:
PEER

Status:
SYNCED

Last Successful Sync:
2026-08-25T11:00:00Z
```

### 15.60 P2P Conflict Handling

P2P synchronization uses the same Conflict model as backend synchronization.

Example:

```text
Device A
    ↓
Expense E1 Version 6

Device B
    ↓
Expense E1 Version 6
```

If the states are concurrent:

```text
Conflict
    ↓
Conflict Data Model
```

must be used.

### 15.61 No Special P2P Conflict Rules

The system must not implement:

```text
P2P = last device wins
```

or:

```text
P2P = receiving device wins
```

without an explicit Domain rule.

P2P is only a transport/synchronization channel.

### 15.62 Connection Loss

A connection can terminate because of:

```text
User moves out of range
Wi-Fi disabled
Hotspot disabled
Device sleep
Android background restriction
Peer application closed
Network interference
```

The application must handle these gracefully.

### 15.63 Connection Loss Behavior

When the connection is lost:

```text
P2P Session
    ↓
DISCONNECTED
```

The application must:

- Preserve locally committed data.
- Preserve successfully processed operations.
- Preserve synchronization state.
- Preserve pending operations.
- Allow future reconnection.
- Avoid duplicate application during retry.

### 15.64 P2P Discovery Failure

If no peers are discovered:

```text
Discovery
    ↓
No Peer
```

the application remains usable.

Local changes simply remain:

```text
PENDING
```

until another synchronization channel becomes available.

### 15.65 Internet Available During P2P

If Internet is available:

```text
Internet
+
P2P
```

the application may use both channels.

The system must remain idempotent.

Example:

```text
Operation O100
    ↓
Peer → Device B
    ↓
Backend → Device B
```

Device B must process O100 only once.

### 15.66 Internet Unavailable During P2P

The P2P channel should continue to function independently of Internet access.

Example:

```text
Internet = OFF

Device A
    ⇅
Local Wi-Fi
    ⇅
Device B
```

Synchronization can continue for authorized Groups.

### 15.67 Hotspot Scenario

A common V1 use case:

```text
Device A
    ↓
Creates Hotspot

Device B
    ↓
Connects to Hotspot

Device C
    ↓
Connects to Hotspot
```

SplitSync devices discover one another through the local network and establish P2P sessions.

The application must not assume that the hotspot has Internet access.

### 15.68 Local Network Without Internet

The network may provide:

```text
Local Connectivity = YES
Internet Connectivity = NO
```

This must still be considered a valid P2P synchronization environment.

### 15.69 Internet Detection

Internet availability and local network availability must be treated separately.

Conceptually:

```text
Internet Available?
        │
        ├── YES → Backend Channel Available
        │
        └── NO

Local Peer Available?
        │
        ├── YES → Peer Channel Available
        │
        └── NO
```

The synchronization engine can select the appropriate available channel.

### 15.70 Automatic Synchronization Strategy

The application should automatically detect suitable synchronization opportunities.

Example:

```text
Local Change
     ↓
Create SyncOperation
     ↓
Pending
     ↓
Connectivity Detected
     ↓
Select Available Channel
     ↓
Synchronize
```

The user should not have to manually choose:

```text
Online Mode
```

or:

```text
Offline Mode
```

for ordinary operation.

### 15.71 Manual Synchronization

V1 may also provide an explicit user action:

```text
Sync Now
```

This should trigger the synchronization engine but must not bypass:

```text
Authorization
Validation
Conflict Detection
Idempotency
```

### 15.72 P2P Session Termination

A session should terminate cleanly when:

```text
Synchronization Completed
User Cancels
Peer Disconnects
Application Closes
Connection Lost
```

Conceptually:

```text
SYNCHRONIZING
      ↓
DISCONNECTING
      ↓
DISCONNECTED
```

### 15.73 Battery Considerations

P2P discovery and synchronization can consume battery.

Therefore the application should avoid continuous high-frequency discovery.

Potential strategies include:

```text
On-demand discovery
User-initiated discovery
Short discovery windows
Background synchronization under controlled conditions
```

The final strategy will depend on Android platform restrictions and V1 UX requirements.

### 15.74 Background Restrictions

Android background execution restrictions must be considered.

The P2P architecture must not assume:

```text
Application can run indefinitely in background
```

The implementation may require:

```text
Foreground Service
WorkManager
User-initiated synchronization
```

depending on the selected transport and Android version.

### 15.75 P2P and WorkManager

WorkManager may be used for deferred synchronization work where appropriate.

However, WorkManager alone may not be sufficient for:

```text
Real-time peer discovery
Active local network sessions
```

The final implementation must select the correct Android mechanism for each responsibility.

### 15.76 P2P Protocol Layers

The conceptual P2P stack is:

```text
┌──────────────────────────────┐
│       Presentation / UI      │
├──────────────────────────────┤
│       Sync Engine            │
├──────────────────────────────┤
│       P2P Protocol           │
├──────────────────────────────┤
│       Security Layer         │
├──────────────────────────────┤
│       Transport Adapter      │
├──────────────────────────────┤
│ Android Local Network APIs   │
└──────────────────────────────┘
```

### 15.77 Transport Adapter

The Transport Adapter isolates Android-specific networking.

Conceptually:

```text
P2P Transport Interface
        │
        ├── Wi-Fi Implementation
        ├── Wi-Fi Direct Implementation
        └── Future Implementation
```

The exact implementation will be selected later.

### 15.78 P2P Protocol

The P2P protocol defines:

```text
Handshake
Authentication
Capability Exchange
Scope Exchange
Sync State Exchange
Operation Exchange
Acknowledgements
Errors
Session Termination
```

### 15.79 Message Types

Conceptual V1 messages:

```text
HELLO
HELLO_RESPONSE
AUTH_REQUEST
AUTH_RESPONSE
CAPABILITY_REQUEST
CAPABILITY_RESPONSE
SCOPE_REQUEST
SCOPE_RESPONSE
SYNC_STATE_REQUEST
SYNC_STATE_RESPONSE
OPERATION_BATCH
OPERATION_RESULT
SYNC_COMPLETE
ERROR
DISCONNECT
```

The exact wire protocol is not finalized in this section.

### 15.80 Message Correlation

Messages that require responses should contain a correlation identifier.

Conceptually:

```text
requestId
```

Example:

```text
SYNC_STATE_REQUEST
requestId = R100
```

Response:

```text
SYNC_STATE_RESPONSE
requestId = R100
```

This allows asynchronous message handling.

### 15.81 Operation Batch

Multiple SyncOperations may be transmitted together.

Example:

```text
OPERATION_BATCH
├── O100
├── O101
├── O102
└── O103
```

Batching reduces communication overhead.

### 15.82 Batch Atomicity

A batch does not necessarily mean all operations must be applied in one database transaction.

The implementation may process operations in smaller transactions according to:

```text
Domain Aggregate
Dependency
Conflict
Performance
```

The protocol must return individual operation results where necessary.

### 15.83 Operation Acknowledgement

The receiver should acknowledge processing.

Example:

```text
O100 → APPLIED
O101 → ALREADY_APPLIED
O102 → CONFLICT
O103 → REJECTED
```

This allows the sender to update its local SyncOperation state accurately.

### 15.84 P2P Error Handling

Errors should be classified.

Examples:

```text
PROTOCOL_ERROR
AUTHENTICATION_FAILED
AUTHORIZATION_FAILED
INVALID_OPERATION
CONFLICT
UNSUPPORTED_VERSION
CONNECTION_ERROR
TEMPORARY_FAILURE
```

### 15.85 Security Boundary

The P2P layer must protect against:

```text
Unknown devices
Unauthorized Group access
Operation tampering
Replay attacks
Duplicate operations
Impersonation
Data interception
```

Detailed cryptographic implementation belongs to the Security Architecture.

### 15.86 Replay Protection

An attacker must not be able to capture:

```text
Operation O100
```

and repeatedly send it to cause repeated processing.

Operation ID idempotency provides application-level duplicate protection.

The secure session must additionally provide appropriate replay protection.

### 15.87 Message Integrity

Received messages must be validated for integrity.

Conceptually:

```text
Message
    ↓
Integrity Verification
    ↓
Valid?
```

If invalid:

```text
Reject
```

The exact mechanism will be defined in Security Architecture.

### 15.88 Confidentiality

Sensitive synchronization data should be protected during transmission.

This includes:

```text
Expense amounts
Expense descriptions
Settlement information
Group membership
User information
```

The final encryption mechanism will be defined in Security Architecture.

### 15.89 P2P Trust Model

The trust model is:

```text
Discovery
    ≠
Authentication
    ≠
Authorization
```

All three stages must be handled independently.

### 15.90 P2P Communication Flow

The complete V1 conceptual flow is:

```text
User Opens SplitSync
        ↓
Discover Nearby Devices
        ↓
Display Nearby Users
        ↓
User Selects Peer
        ↓
Connection Request
        ↓
Peer Accepts
        ↓
Secure Authentication
        ↓
Capability Exchange
        ↓
Determine Authorized Groups
        ↓
Exchange Sync State
        ↓
Exchange Operations
        ↓
Validate Operations
        ↓
Detect Conflicts
        ↓
Apply Valid Changes
        ↓
Send Operation Results
        ↓
Update SyncState
        ↓
Synchronization Complete
        ↓
Disconnect
```

### 15.91 Example — Offline Group Synchronization

```text
User A
    ↓
Creates Expense E1 offline

User B
    ↓
Creates Expense E2 offline

Both devices have:
Internet = OFF
```

Later:

```text
User A hotspot ON
        ↓
User B connects
        ↓
Devices discover each other
        ↓
Authenticate
        ↓
Verify Group membership
        ↓
Exchange Operations
        ↓
A receives E2
B receives E1
        ↓
Both calculate balances locally
```

Final state:

```text
Device A:
E1 + E2

Device B:
E1 + E2
```

### 15.92 Example — P2P Then Backend

```text
Device A
    ↓
Creates Expense E1 offline
    ↓
P2P sync with Device B
    ↓
Device B receives E1
    ↓
Later Internet becomes available
    ↓
Device A sends E1 to Backend
    ↓
Backend accepts E1
```

Device B may later receive E1 from the Backend as well.

Because:

```text
operationId = O100
```

is unchanged:

```text
Duplicate delivery
    ↓
ALREADY_APPLIED
```

No duplicate Expense is created.

### 15.93 Example — Conflict Through P2P

```text
Expense E1
Version 5
```

Device A:

```text
Version 6
Amount ₹1200
```

Device B:

```text
Version 6
Amount ₹1500
```

P2P connection:

```text
A ⇄ B
```

Conflict:

```text
CONCURRENT_UPDATE
```

The Conflict Data Model is invoked.

Neither device should silently overwrite the other.

### 15.94 P2P Communication and Domain Independence

The Domain layer must not know whether a change arrived through:

```text
REST
Wi-Fi
Hotspot
Wi-Fi Direct
```

The Domain layer receives a validated synchronization command/state.

This maintains clean architecture.

### 15.95 P2P Module Boundary

Conceptually:

```text
feature/sync
    │
    ├── SyncEngine
    ├── SyncOperation
    ├── SyncState
    └── ConflictHandler

feature/p2p
    │
    ├── PeerDiscovery
    ├── PeerConnection
    ├── P2PProtocol
    ├── PeerAuthentication
    └── P2PSession

data/local
    │
    └── Room

domain
    │
    └── Business Rules
```

The exact Android package structure is defined in the Android Package Structure section.

### 15.96 P2P Communication Model Summary

```text
P2P Communication
│
├── Discovery
│   └── Find nearby SplitSync devices
│
├── Connection
│   └── Establish local communication
│
├── Authentication
│   └── Verify peer identity
│
├── Authorization
│   └── Verify Group access
│
├── Handshake
│   └── Negotiate protocol/capabilities
│
├── Scope Exchange
│   └── Determine synchronized Groups
│
├── Sync State Exchange
│   └── Determine synchronization progress
│
├── Operation Exchange
│   └── Exchange SyncOperations
│
├── Validation
│   └── Apply Domain rules
│
├── Conflict Handling
│   └── Detect and resolve conflicts
│
└── Session Completion
    └── Persist synchronization state
```

### 15.97 P2P Communication Invariants

The following rules are mandatory for V1:

- P2P synchronization is an additional synchronization channel, not a replacement for local-first functionality.
- Core expense functionality must work without P2P connectivity.
- P2P communication must work without Internet connectivity.
- Local Wi-Fi or Hotspot connectivity must not be assumed to provide Internet access.
- Discovery must be separate from authentication.
- Authentication must be separate from authorization.
- A nearby device must not automatically be trusted.
- P2P synchronization must only occur between authorized devices/users.
- Group data must only be synchronized for authorized Groups.
- Sensitive information must not be unnecessarily broadcast during discovery.
- Phone number must not be used as the primary P2P discovery identity.
- Device ID must remain stable for the application installation.
- User ID and Device ID must remain distinct concepts.
- Operation IDs must remain unchanged during P2P synchronization.
- Origin Device IDs must remain unchanged when operations are forwarded.
- The same operation may travel through multiple devices or synchronization channels.
- Duplicate operation delivery must never create duplicate financial records.
- P2P must use the same SyncOperation and Conflict concepts as Backend synchronization.
- P2P must not introduce a separate financial conflict-resolution model.
- Received operations must be validated before being applied.
- Received operations must be applied transactionally.
- Connection failure must not roll back successful local transactions.
- Pending operations must survive connection loss.
- P2P sessions must be resumable where practical.
- Peer synchronization progress must be tracked independently for different peers.
- One peer's synchronization failure must not prevent local application functionality.
- One Group's synchronization failure must not prevent unrelated Groups from functioning.
- Balances must be calculated from synchronized source data rather than synchronized as authoritative values.
- P2P transport implementation must remain separate from Domain logic.
- The selected Android transport technology must be replaceable without redesigning the Domain model.
- The P2P protocol must support protocol versioning.
- P2P messages must provide integrity protection.
- Sensitive synchronization data must be protected during transmission.
- Replay and duplicate delivery must be handled safely.
- P2P synchronization must support eventual convergence between authorized devices.

## 16. Authentication Flow

### 16.1 Purpose

This section defines the authentication and identity flow for SplitSync V1.

SplitSync is designed as a local-first application where a User must be able to create and use a local profile without Internet connectivity.

Authentication therefore has two distinct contexts:

```text
Local Identity
    ↓
Works without Internet

Backend Authentication
    ↓
Required only for backend-connected functionality
```

The application must not make core local expense management dependent on backend authentication.

### 16.2 Core Principle

The fundamental identity model is:

```text
One Application Installation
        ↓
One Device
        ↓
One Local User Profile
```

For V1:

```text
One Device
    =
One Local User
```

The application must not support multiple local Users on the same device in V1.

### 16.3 Identity Model

The main identity entities are:

```text
User
Device
```

They represent different concepts.

```text
User
    ↓
Represents the person/profile

Device
    ↓
Represents the application installation/device
```

The relationship in V1 is:

```text
One Device
    ↓
One User
```

### 16.4 User Identity

A User has a stable:

```text
userId
```

The User ID identifies the local profile independently of the synchronization transport.

Example:

```text
userId = U100
```

The User ID must not change merely because the device connects to:

```text
Internet
Wi-Fi
Hotspot
Peer Device
```

### 16.5 Device Identity

Every application installation has a stable:

```text
deviceId
```

Example:

```text
deviceId = D100
```

The Device ID identifies the local application/device instance.

### 16.6 User-Device Relationship

V1 uses:

```text
User U100
    │
    └── Device D100
```

The local database maintains this association.

Conceptually:

```text
Device
    ↓
User
```

### 16.7 Local Profile Creation

The first launch flow is:

```text
Install SplitSync
        ↓
Launch Application
        ↓
Generate Device ID
        ↓
Create Local User Profile
        ↓
Store User + Device
        ↓
Application Ready
```

No Internet connection is required for this process.

### 16.8 First Launch Without Internet

Example:

```text
Internet = OFF
```

User opens SplitSync.

The application should still allow:

```text
Create Profile
    ↓
Create Group
    ↓
Add Expenses
    ↓
Calculate Balances
```

The application must not block the User with:

```text
"Internet connection required"
```

for these core operations.

### 16.9 Local Profile Information

The V1 User profile may contain:

```text
userId
displayName
phoneNumber
email
createdAt
updatedAt
```

The exact required/optional fields will be finalized in the User Model.

### 16.10 Phone Number

The phone number may be collected as a User profile attribute.

It can help with:

```text
User identification
Account recovery
Future account association
```

However, the phone number is not the primary local synchronization identity.

The primary identity remains:

```text
userId
deviceId
```

### 16.11 Email

Email may be stored as an optional User profile attribute.

It may be useful for future:

```text
Account association
Recovery
Notifications
Backend identity
```

V1 local profile creation must not require an email if the application is intended to operate completely offline.

### 16.12 Local Authentication vs Backend Authentication

These are separate concepts.

```text
Local Profile
    ↓
Identifies the User on the Device

Backend Authentication
    ↓
Authenticates the User/Device with the Server
```

A User may have:

```text
Local Profile = available
Backend Authentication = not configured
```

and still use the application locally.

### 16.13 Local Profile Is Not a Server Account

Creating a local profile does not necessarily mean:

```text
Server Account Created
```

Instead:

```text
Local Profile
    ↓
Local User ID
    ↓
Optional Backend Association
```

This allows completely offline usage.

### 16.14 Optional Backend Registration

When Internet becomes available, the application may allow the User to associate the local profile with the backend.

Conceptually:

```text
Local User
    ↓
Internet Available
    ↓
Backend Registration / Association
    ↓
Backend Identity
```

The exact registration flow will be defined in the Backend API and Security Architecture.

### 16.15 Backend Authentication Requirement

Backend authentication is required for operations that require server authorization.

Examples:

```text
Backend synchronization
Server-side account operations
Server-side profile operations
```

Core local functionality remains available without it.

### 16.16 Authentication States

Recommended V1 local/backend identity states:

```text
LOCAL_ONLY
AUTHENTICATING
AUTHENTICATED
AUTHENTICATION_FAILED
AUTHENTICATION_EXPIRED
```

### 16.17 LOCAL_ONLY

```text
LOCAL_ONLY
```

means:

```text
Local User exists
Backend authentication is not currently available/configured
```

The User can continue using the application locally.

### 16.18 AUTHENTICATING

```text
AUTHENTICATING
```

means the application is currently performing a backend authentication/association flow.

Local functionality should not be blocked while this is happening.

### 16.19 AUTHENTICATED

```text
AUTHENTICATED
```

means the local profile has a valid backend authentication state.

The application can perform authorized backend operations.

### 16.20 AUTHENTICATION_FAILED

```text
AUTHENTICATION_FAILED
```

means the latest backend authentication attempt failed.

This must not delete the local profile or local financial data.

### 16.21 AUTHENTICATION_EXPIRED

```text
AUTHENTICATION_EXPIRED
```

means previously valid backend credentials/session information is no longer valid.

The application should allow local usage and request re-authentication when backend functionality is required.

### 16.22 Initial Authentication Flow

The initial local flow is:

```text
Application Launch
        ↓
Check Local Device
        ↓
Device Exists?
   ┌────┴────┐
  No        Yes
   │          │
Create       Load
Device       Device
   │          │
   └────┬─────┘
        ↓
Check Local User
        ↓
User Exists?
   ┌────┴────┐
  No        Yes
   │          │
Create       Load
User         User
   │          │
   └────┬─────┘
        ↓
Application Ready
```

### 16.23 New Installation

For a new installation:

```text
No Device
No User
```

The application creates:

```text
Device D1
User U1
```

and associates them:

```text
U1 ↔ D1
```

### 16.24 Existing Installation

If the application is reopened:

```text
Device D1 exists
User U1 exists
```

The application loads the existing local identity.

It must not create another User.

### 16.25 One User Per Device

V1 invariant:

```text
One Device
    ↓
One Local User
```

Therefore the application must prevent:

```text
Device D1
├── User U1
├── User U2
└── User U3
```

### 16.26 Local User Switching

User switching is not supported in V1.

There is no:

```text
Switch Account
```

between multiple local profiles on the same installation.

Future versions may introduce multi-account/device support.

### 16.27 Application Reinstallation

Application reinstallation may remove local application storage depending on Android behavior and user actions.

Therefore:

```text
Uninstall
    ↓
Local Identity may be lost
```

The backend account, if one exists, can potentially be used for future account recovery/association.

The exact recovery behavior is outside the V1 local-only identity guarantee.

### 16.28 Device Identity Persistence

The Device ID should be stored in persistent application storage.

It must survive:

```text
Application restart
Process restart
Device restart
```

It should not be regenerated on every application launch.

### 16.29 Device ID Regeneration

The Device ID should only change when the application installation is treated as a new Device identity.

A new Device ID must not be generated simply because:

```text
Internet changed
Wi-Fi changed
Hotspot changed
Backend unavailable
Peer changed
```

### 16.30 User ID Persistence

The User ID must also be stored locally and remain stable for the lifetime of the local profile.

Example:

```text
User U100
```

must remain:

```text
U100
```

across normal application usage.

### 16.31 Local Authentication

Because V1 uses one local User per Device, the application does not require a traditional username/password login for basic local use.

The local identity is established during:

```text
Initial Profile Creation
```

and associated with the Device.

### 16.32 Optional Local App Protection

A future or optional local protection layer may support:

```text
PIN
Biometric
Device Authentication
```

This is different from User identity.

For example:

```text
Biometric
    ↓
Unlock local application

User ID
    ↓
Identify local User
```

The exact local application-lock feature is outside the core V1 identity model unless explicitly enabled.

### 16.33 Backend Account Association

When the User chooses to use backend services:

```text
Local User U1
      ↓
Backend Association
      ↓
Server Account
```

The backend must preserve the logical association between the local User and server identity.

### 16.34 Backend Registration Flow

Conceptually:

```text
Local User Exists
        ↓
Internet Available
        ↓
User Chooses Backend Setup
        ↓
Enter/Verify Required Credentials
        ↓
Backend Registration
        ↓
Server Identity Created/Linked
        ↓
Local Identity Updated
        ↓
Synchronization Available
```

The exact credential/verification mechanism will be defined in the Security and API Design.

### 16.35 Backend Login Flow

For an existing backend-associated User:

```text
Local User
      ↓
Internet Available
      ↓
Backend Authentication
      ↓
Credentials Valid?
   ┌────┴────┐
  Yes       No
   │          │
Authenticated Failed
   │
Backend Sync Available
```

### 16.36 Authentication Does Not Block Local Use

If backend authentication fails:

```text
Backend Authentication
        ↓
FAILED
```

the application should still allow:

```text
View Groups
Add Expenses
Edit Expenses
Calculate Balances
Record Settlements
```

where those operations are otherwise valid locally.

### 16.37 Offline Authentication Scenario

Example:

```text
Internet = OFF
```

The User opens SplitSync.

Result:

```text
Local identity available
        ↓
Application opens
        ↓
Local functionality available
```

No backend login is required for local usage.

### 16.38 Online Authentication Scenario

Example:

```text
Internet = ON
```

The application may detect:

```text
Backend authentication required
```

and perform authentication/association when necessary.

The User should not be forced into a separate:

```text
Online Mode
```

or:

```text
Offline Mode
```

for normal operation.

### 16.39 Automatic Connectivity Detection

The application should detect connectivity automatically.

Conceptually:

```text
Application Operation
        ↓
Need Backend?
        │
        ├── No → Continue Locally
        │
        └── Yes
              ↓
        Internet Available?
              │
         ┌────┴────┐
        Yes        No
         │          │
      Backend     Remain
      Flow        Local
```

### 16.40 Authentication and Synchronization

Authentication is a prerequisite for backend synchronization when server authorization is required.

Conceptually:

```text
Local Changes
      ↓
SyncOperation
      ↓
Backend Available?
      ↓
Authenticated?
   ┌──┴──┐
  Yes    No
   │      │
 Sync    Wait /
         Authenticate
```

Pending local changes must remain safe while authentication is unavailable.

### 16.41 P2P Authentication

P2P authentication is separate from backend authentication.

Example:

```text
Device A
    ↓
Device B
```

The devices must establish peer trust independently.

Backend credentials must not be transmitted to peers merely to authenticate the P2P session.

### 16.42 P2P Identity Flow

Conceptually:

```text
Discover Peer
      ↓
Receive Device Identity
      ↓
Verify Peer Identity
      ↓
Authenticate Peer
      ↓
Check Group Authorization
      ↓
Allow Synchronization
```

### 16.43 P2P Does Not Require Internet

The following must work without Internet:

```text
Peer Discovery
Peer Authentication
Group Authorization
Operation Exchange
Conflict Detection
Local Synchronization
```

subject to the selected Android transport and security implementation.

### 16.44 Profile Identification During P2P

When a nearby User is discovered, the application may show:

```text
Display Name
Profile Identifier
```

The User can then select the intended peer.

The application should avoid displaying unnecessary sensitive information.

### 16.45 Same Network Is Not Authentication

This rule is mandatory:

```text
Same Wi-Fi
    ≠
Trusted SplitSync User
```

and:

```text
Same Hotspot
    ≠
Authorized Group Member
```

Authentication and authorization must still occur.

### 16.46 Group Invitation Authentication

When a Group invitation is exchanged:

```text
Invitation
    ↓
Verify Source
    ↓
Verify Integrity
    ↓
Show User
    ↓
User Accepts
    ↓
Membership Created
```

The receiver must not silently create membership from an untrusted invitation.

### 16.47 Authentication Token Storage

Backend authentication credentials/tokens must not be stored as plain text.

The Android implementation should use appropriate secure platform storage.

The exact mechanism will be defined in the Security Architecture.

### 16.48 Token Lifecycle

Conceptually:

```text
Authenticate
    ↓
Receive Credential/Token
    ↓
Secure Storage
    ↓
Use for Authorized Requests
    ↓
Refresh / Expire
    ↓
Re-authenticate if required
```

### 16.49 Token Expiration

If backend credentials expire:

```text
AUTHENTICATED
      ↓
Credential Expired
      ↓
AUTHENTICATION_EXPIRED
```

The local application remains usable.

Backend synchronization pauses until authentication is restored.

### 16.50 Authentication Failure Handling

Authentication failures must be classified.

Examples:

```text
INVALID_CREDENTIALS
ACCOUNT_NOT_FOUND
ACCOUNT_DISABLED
TOKEN_EXPIRED
SERVER_UNAVAILABLE
NETWORK_UNAVAILABLE
```

The exact error catalog will be defined in the API and Security sections.

### 16.51 Temporary vs Permanent Authentication Failure

Temporary:

```text
NETWORK_UNAVAILABLE
SERVER_UNAVAILABLE
```

may be retried.

Potentially permanent:

```text
INVALID_CREDENTIALS
ACCOUNT_DISABLED
```

should not be retried indefinitely without User action.

### 16.52 Authentication Retry

Retry behavior should use controlled backoff.

Conceptually:

```text
Authentication Failed
       ↓
Determine Error
       ↓
Retryable?
   ┌───┴───┐
  Yes      No
   │        │
Backoff   User Action
   │
Retry
```

### 16.53 Local Data During Authentication Failure

Authentication failure must never cause deletion of:

```text
Users
Devices
Groups
Expenses
ExpenseSplits
Settlements
SyncOperations
```

Local data remains authoritative for local usage.

### 16.54 Backend Account Linking

When a local User becomes associated with a backend account:

```text
Local User U1
      ↓
Backend User B1
```

the relationship must be persisted safely.

The application must avoid creating duplicate server accounts for the same logical association.

### 16.55 Account Linking Idempotency

If account linking is attempted multiple times:

```text
Link U1 → B1
```

the second attempt must not create:

```text
B2
```

unless explicitly required by the backend identity rules.

### 16.56 Identity Mapping

The system may maintain a mapping conceptually similar to:

```text
Local User ID
       ↓
Backend User ID
```

and:

```text
Local Device ID
       ↓
Backend Device ID / Registration
```

The exact mapping schema will be finalized in the Backend Database and API Design.

### 16.57 Authentication and Device Registration

Backend authentication may require Device registration.

Conceptually:

```text
Local Device
      ↓
Backend Registration
      ↓
Authenticated Device
```

This helps the backend associate synchronization activity with a known device.

### 16.58 Device Revocation

A future backend/security flow may revoke a Device.

Example:

```text
Device D1
    ↓
Revoked
```

Then:

```text
Backend Sync
    ↓
Rejected
```

but:

```text
Local Application
    ↓
Continues Working
```

The exact revocation behavior will be defined in the Security Architecture.

### 16.59 P2P Device Revocation

If a Device is no longer trusted:

```text
Peer Device
    ↓
No longer authorized
```

the application must prevent future synchronization with that peer.

Previously synchronized valid local data must not be deleted merely because the peer is no longer trusted.

### 16.60 Authentication and User Deletion

Deleting a local profile is a destructive operation and is outside the automatic authentication flow.

The application must not delete the local User simply because:

```text
Backend Authentication Failed
```

### 16.61 Authentication and Group Membership

Authentication identifies the User/Device.

Group authorization determines:

```text
Which Groups the User can access.
```

Therefore:

```text
Authentication
    ≠
Group Membership
```

### 16.62 Authentication and Expense Authorization

Similarly:

```text
Authenticated User
```

does not automatically mean:

```text
Authorized to modify every Expense.
```

The Group membership and Domain authorization rules must still be evaluated.

### 16.63 Complete Local-First Authentication Flow

```text
Application Installed
        ↓
Generate Device ID
        ↓
Create Local User
        ↓
Associate User + Device
        ↓
Local Profile Ready
        ↓
Create / Join Groups
        ↓
Use Application Offline
```

No Internet is required.

### 16.64 Complete Online Flow

```text
Local User Exists
        ↓
Internet Available
        ↓
Backend Setup Required?
        │
   ┌────┴────┐
  Yes        No
   │          │
Register     Continue
/ Login      Session
   │          │
   └────┬─────┘
        ↓
Authenticated
        ↓
Backend Synchronization
```

### 16.65 Complete Hybrid Flow

```text
Local User
    ↓
Local Expense Created
    ↓
SyncOperation Created
    ↓
Internet Available?
    │
 ┌──┴──┐
Yes    No
 │      │
Backend  Peer Available?
 │       │
 │    ┌──┴──┐
 │   Yes    No
 │    │      │
 │   P2P    Pending
 │    │
 └────┴──────┐
             ↓
      Synchronization
```

### 16.66 Authentication Flow Summary

```text
Authentication
│
├── Local Identity
│   ├── Device ID
│   ├── User ID
│   └── One User per Device
│
├── Local Profile
│   ├── Display Name
│   ├── Phone
│   └── Optional Email
│
├── Backend Authentication
│   ├── Registration
│   ├── Login
│   ├── Token Management
│   └── Session Expiration
│
├── P2P Authentication
│   ├── Peer Identity
│   ├── Secure Session
│   └── Group Authorization
│
└── Offline Support
    ├── No Internet Required
    ├── Local Profile Available
    ├── Local Expenses Available
    └── Pending Sync Preserved
```

### 16.67 Authentication Invariants

The following rules are mandatory for V1:

- A User must be able to create a local profile without Internet connectivity.
- Core local expense functionality must not require backend authentication.
- One application installation/device supports one local User in V1.
- One Device must not have multiple active local Users.
- User ID and Device ID are separate identifiers.
- Device ID must remain stable across normal application usage.
- User ID must remain stable for the lifetime of the local profile.
- Internet connectivity must not be required to identify the local User.
- Phone number is a profile attribute, not the primary synchronization identity.
- Email is optional for local profile creation.
- Local profile creation does not automatically imply backend account creation.
- Backend authentication is separate from local identity.
- P2P authentication is separate from backend authentication.
- Discovery does not imply authentication.
- Authentication does not imply Group authorization.
- Being connected to the same Wi-Fi or Hotspot does not imply trust.
- Backend authentication failure must not delete local financial data.
- Backend authentication failure must not prevent local expense management.
- Expired backend credentials must not prevent offline usage.
- Pending SyncOperations must remain persisted while authentication is unavailable.
- Backend credentials/tokens must be stored securely.
- Authentication retries must distinguish temporary failures from permanent failures.
- Backend authentication must be required only for operations that require backend authorization.
- P2P synchronization must use peer authentication and Group authorization.
- P2P synchronization must not require Internet connectivity.
- Local identity must remain available when both Internet and P2P connectivity are unavailable.
- Authentication state must not be treated as Domain financial state.
- Group membership must be validated separately from authentication.
- Expense authorization must be validated separately from authentication.
- Identity association and account linking must be idempotent.
- Authentication state changes must not corrupt or delete local Domain data.

## 17. Authorization Flow

### 17.1 Purpose

This section defines the authorization model and authorization flow for SplitSync V1.

Authentication answers:

```text
Who is this User/Device?
```

Authorization answers:

```text
What is this User/Device allowed to do?
```

SplitSync must keep these responsibilities separate.

The authorization system controls access to:

- Groups.
- Group membership.
- Expenses.
- Expense Splits.
- Settlements.
- Synchronization operations.
- Peer-to-peer synchronization.
- Backend resources.

### 17.2 Core Principle

The fundamental authorization rule is:

```text
Authenticated
    ≠
Authorized
```

A User may be correctly identified but still not have permission to perform a particular operation.

Example:

```text
User U1
    ↓
Authenticated
    ↓
Not a member of Group G2
    ↓
Cannot modify Group G2
```

### 17.3 Authorization Layers

SplitSync V1 uses multiple authorization layers:

```text
Device Authorization
        ↓
User Authorization
        ↓
Group Authorization
        ↓
Entity Authorization
        ↓
Operation Authorization
```

The exact checks depend on the operation being performed.

### 17.4 Local Authorization

Local authorization must work without Internet connectivity.

Example:

```text
Internet = OFF

User U1
    ↓
Member of Group G1
    ↓
Create Expense
    ↓
Allowed
```

The application must not require a backend request merely to validate ordinary local Group permissions.

### 17.5 Backend Authorization

When using the backend:

```text
Request
    ↓
Authenticate User/Device
    ↓
Validate Authorization
    ↓
Apply Operation
```

The backend must independently validate authorization.

The Android client must never be treated as the sole authority for backend access control.

### 17.6 P2P Authorization

P2P authorization follows:

```text
Peer Identity
      ↓
Authenticated Peer
      ↓
User Identity
      ↓
Group Membership
      ↓
Allowed Synchronization Scope
```

A nearby peer must not automatically receive Group data.

### 17.7 Authorization Subjects

The primary authorization subjects are:

```text
User
Device
```

A User represents the person performing the action.

A Device represents the application installation from which the action originates.

### 17.8 User Authorization

User authorization determines whether the User can perform an action.

Example:

```text
User U1
    ↓
Member of Group G1
    ↓
Can perform permitted G1 operations
```

### 17.9 Device Authorization

The Device must also be recognized as an authorized source where required.

Example:

```text
User U1
    +
Device D1
    ↓
Authorized synchronization source
```

A valid User identity must not automatically authorize an unknown or revoked Device.

### 17.10 Group as Primary Authorization Boundary

The Group is the primary authorization boundary for shared expense data.

Conceptually:

```text
User
  ↓
Group Membership
  ↓
Group
  ↓
Expenses / Splits / Settlements
```

A User's access to Group-owned financial data is determined through Group membership and applicable permissions.

### 17.11 Group Membership

A User may be associated with a Group through:

```text
GroupMember
```

Conceptually:

```text
User U1
    ↓
GroupMember
    ↓
Group G1
```

Membership is the basis for ordinary Group access.

### 17.12 Membership Status

A Group membership should have a status such as:

```text
PENDING
ACTIVE
REMOVED
DECLINED
```

The exact membership lifecycle is defined by the Group model.

For authorization, the important distinction is:

```text
ACTIVE
```

versus:

```text
Not Active
```

### 17.13 Active Membership

An Active member may perform operations allowed by their Group role/permissions.

Example:

```text
User U1
    ↓
ACTIVE member of G1
    ↓
Create Expense
```

### 17.14 Removed Membership

A removed member must not automatically retain authorization to perform new Group operations.

Example:

```text
User U1
    ↓
Removed from G1
    ↓
New Expense
    ↓
Reject
```

Previously synchronized valid data may remain locally according to the application's data-retention rules.

### 17.15 Pending Membership

A pending invitation does not automatically grant full Group access.

Example:

```text
Invitation
    ↓
PENDING
    ↓
Not yet authorized for normal Group operations
```

Membership becomes active only after the required acceptance/authorization flow.

### 17.16 Group Roles

V1 may define Group roles such as:

```text
OWNER
ADMIN
MEMBER
```

The exact role set must remain small and clear.

### 17.17 OWNER

The Owner has the highest Group-level authority.

Typical responsibilities may include:

```text
Manage Group
Manage Membership
Change Group Settings
Delete/Archive Group
```

The exact operations will be defined in the Group authorization matrix.

### 17.18 ADMIN

An Admin may perform selected management operations delegated by the Owner.

Typical examples:

```text
Manage Members
Manage Group Information
```

The exact permissions must be explicitly defined rather than assumed.

### 17.19 MEMBER

A Member is an ordinary Group participant.

Typical permissions may include:

```text
View Group
Create Expense
View Expenses
Create Settlement
View Settlements
```

The exact permission matrix will be finalized before implementation.

### 17.20 Role Is Not Authentication

The role does not identify the User.

```text
User ID
    ↓
Identifies User

Role
    ↓
Defines permissions within a Group
```

The same User may have different roles in different Groups.

Example:

```text
User U1

G1 → OWNER
G2 → MEMBER
G3 → ADMIN
```

### 17.21 Group-Specific Authorization

Authorization must be evaluated in the context of the relevant Group.

Example:

```text
User U1
    ↓
G1 → OWNER
G2 → MEMBER
```

The User's OWNER permissions in G1 must not automatically apply to G2.

### 17.22 Resource Ownership

Some operations may depend on ownership.

Example:

```text
Group G1
    ↓
Owner U1
```

Only the Owner may perform certain administrative operations.

Ownership must be explicitly validated.

### 17.23 Expense Authorization

An Expense belongs to a Group.

Therefore:

```text
Expense
    ↓
Group
    ↓
User Membership
    ↓
Authorization
```

A User cannot modify an Expense in a Group where the User is not authorized.

### 17.24 Expense Creation

A typical authorization flow is:

```text
Create Expense
      ↓
Identify User
      ↓
Identify Group
      ↓
Check Active Membership
      ↓
Check Expense Permission
      ↓
Validate Expense
      ↓
Create Expense
```

### 17.25 Expense Update

For updating an Expense:

```text
Update Expense
      ↓
Identify User
      ↓
Identify Group
      ↓
Check Membership
      ↓
Check Update Permission
      ↓
Validate Entity Version
      ↓
Update
```

Version validation and authorization are separate checks.

### 17.26 Expense Deletion

Expense deletion requires stronger validation because it can affect financial history.

Conceptually:

```text
Delete Expense
      ↓
Identify User
      ↓
Check Group Membership
      ↓
Check Delete Permission
      ↓
Check Domain Rules
      ↓
Create Delete Operation
```

The exact deletion policy will be finalized with the Expense lifecycle.

### 17.27 Expense Split Authorization

Expense Splits belong to an Expense.

Therefore:

```text
ExpenseSplit
    ↓
Expense
    ↓
Group
    ↓
Authorization
```

A User should not modify an ExpenseSplit independently if doing so would violate the parent Expense's Domain rules.

### 17.28 Settlement Authorization

A Settlement belongs to a Group.

Typical flow:

```text
Create Settlement
      ↓
Identify User
      ↓
Identify Group
      ↓
Check Active Membership
      ↓
Validate Settlement Permission
      ↓
Validate Participants
      ↓
Validate Amount
      ↓
Create Settlement
```

### 17.29 Settlement Participant Validation

A settlement involving:

```text
From User
To User
```

must validate that the relevant Users are authorized participants in the Group according to the Domain rules.

The system must not allow arbitrary Users to be referenced in a Group settlement.

### 17.30 Group Membership Authorization

Membership changes are more sensitive than ordinary expense creation.

Conceptually:

```text
Membership Change
      ↓
Identify Actor
      ↓
Check Group Role
      ↓
Check Target User
      ↓
Apply Membership Rule
```

Only authorized roles should be able to add/remove members.

### 17.31 Invitation Authorization

An invitation should be generated only by an authorized Group actor.

Example:

```text
User U1
    ↓
OWNER / authorized role
    ↓
Invite User U2
```

A normal Member should not automatically gain the ability to modify Group membership unless explicitly permitted.

### 17.32 Accept Invitation

Accepting an invitation is a User action.

Conceptually:

```text
Invitation
      ↓
Verify Invitation
      ↓
Verify Recipient
      ↓
User Accepts
      ↓
Create/Activate Membership
```

The receiving User must not be able to accept an invitation intended for another User.

### 17.33 Peer Authorization

P2P authorization must validate:

```text
Peer Device
      ↓
Source User
      ↓
Group Membership
      ↓
Requested Operation
```

Example:

```text
Peer Device D2
    ↓
User U2
    ↓
Member of G1
    ↓
Allowed to synchronize G1
```

### 17.34 Peer Discovery Is Not Authorization

This rule is mandatory:

```text
Discovered Peer
    ≠
Authorized Peer
```

The application may display a discovered User, but synchronization still requires authentication and authorization.

### 17.35 Peer Group Scope

When two peers connect:

```text
Device A Groups
        ∩
Device B Authorized Groups
        ↓
Allowed Synchronization Scope
```

Only this scope should be considered for P2P synchronization.

### 17.36 Synchronization Authorization

A SyncOperation must be authorized before being applied.

Conceptually:

```text
SyncOperation
      ↓
Identify Origin Device
      ↓
Identify User
      ↓
Identify Group
      ↓
Check Membership / Permission
      ↓
Validate Operation
      ↓
Apply
```

### 17.37 Origin Authorization

The receiving side must validate the origin of the operation.

Example:

```text
Operation O100
Origin Device = D1
Origin User   = U1
Group         = G1
```

The receiver must determine whether:

```text
U1 / D1
```

was authorized to perform the operation against:

```text
G1
```

when the operation was created.

### 17.38 Forwarded Operation

An operation may travel:

```text
Device A
    ↓
Device B
    ↓
Device C
```

Device B must not become the origin.

The authorization information must continue to identify:

```text
Original User
Original Device
Original Operation
```

### 17.39 P2P Forwarding

A peer may forward an operation only when the protocol permits it.

The forwarding device must not modify:

```text
operationId
originDeviceId
originUserId
```

The receiving device must still validate the operation.

### 17.40 Backend Authorization

The backend must independently verify:

```text
Authenticated User
Authenticated Device
Group Membership
Role
Requested Operation
Entity State
```

The Android application cannot be trusted as the only authorization layer.

### 17.41 Client-Side Authorization

Android should perform authorization checks before executing local operations.

Benefits include:

- Immediate User feedback.
- Consistent local behavior.
- Reduced invalid local data.
- Better offline support.

However, client-side authorization does not replace backend authorization.

### 17.42 Backend-Side Authorization

The backend must independently repeat security-sensitive authorization checks.

Example:

```text
Android says:
"User is allowed"

Backend:
Verify independently
```

This prevents a modified or compromised client from bypassing authorization.

### 17.43 Defense in Depth

The authorization architecture follows:

```text
UI Validation
      ↓
Domain Authorization
      ↓
Local Persistence Validation
      ↓
Backend Authorization
```

Not every operation requires every layer, but security-sensitive operations must be validated at the appropriate boundaries.

### 17.44 Domain Authorization

Authorization rules that are part of the business domain should live in the Domain/Application layer rather than inside Android UI code.

For example:

```text
Can User U1 modify Expense E1?
```

should not be decided by:

```text
ExpenseEditActivity
```

alone.

### 17.45 UI Role Checks

The UI may hide or disable actions based on permissions.

Example:

```text
MEMBER
    ↓
Hide "Remove Member"
```

But UI visibility is not security.

The underlying operation must still validate authorization.

### 17.46 Unauthorized Local Operation

If a User attempts an unauthorized local operation:

```text
Operation
      ↓
Authorization Check
      ↓
Denied
```

No Domain change should be persisted.

There should be no:

```text
Expense
+
SyncOperation
```

created for a denied operation.

### 17.47 Unauthorized Remote Operation

If an unauthorized SyncOperation is received:

```text
Receive Operation
      ↓
Authorization Check
      ↓
Denied
```

The operation must not modify the local Domain state.

The result should be represented as an appropriate rejection.

### 17.48 Authorization Failure vs Conflict

These are different:

```text
Authorization Failure
    ↓
User/Device is not allowed

Conflict
    ↓
Multiple valid changes cannot be safely combined
```

An unauthorized operation must not be treated as an ordinary data conflict.

### 17.49 Authorization Failure vs Authentication Failure

These are also different:

```text
Authentication Failure
    ↓
Cannot establish identity

Authorization Failure
    ↓
Identity established
but operation is not permitted
```

### 17.50 Authorization Failure vs Validation Failure

Similarly:

```text
Authorization
    ↓
Is the actor allowed?

Validation
    ↓
Is the operation data valid?
```

Example:

```text
User is authorized
but split amounts are invalid
```

This is a Domain validation failure, not an authorization failure.

### 17.51 Authorization Check Order

Recommended order:

```text
1. Establish Identity
        ↓
2. Verify Device
        ↓
3. Verify Group Scope
        ↓
4. Verify Membership
        ↓
5. Verify Role / Permission
        ↓
6. Validate Domain Data
        ↓
7. Apply Operation
```

For backend synchronization:

```text
1. Authenticate Request
        ↓
2. Validate Device
        ↓
3. Validate User
        ↓
4. Validate Group Authorization
        ↓
5. Validate Operation
        ↓
6. Validate Version
        ↓
7. Apply Transaction
```

### 17.52 Authorization and Version

Authorization must be checked independently from version.

Example:

```text
User authorized
+
Version conflict
```

Result:

```text
CONFLICT
```

Another example:

```text
User unauthorized
+
Version matches
```

Result:

```text
REJECTED
```

### 17.53 Authorization and Offline Changes

A User may create a valid local operation while offline.

Example:

```text
User U1
    ↓
Active Member of G1
    ↓
Internet OFF
    ↓
Create Expense
    ↓
Authorized locally
    ↓
Persist Expense
    ↓
Create SyncOperation
```

When the operation later reaches the backend, authorization is checked again.

### 17.54 Membership Changed While Offline

Important scenario:

```text
Device A:
User U1 is member of G1
```

Offline:

```text
U1 creates Expense E1
```

Meanwhile:

```text
Device B / Backend
    ↓
U1 removed from G1
```

Later:

```text
E1 synchronization
    ↓
Backend checks current authorization
```

The backend may reject the operation if the authorization rules require current membership.

The exact historical-membership policy must be finalized in the Domain and Security design.

### 17.55 Authorization Snapshot

For synchronization, the system may need to preserve enough metadata to determine whether an operation was created by an authorized actor.

Conceptually:

```text
Operation
├── originUserId
├── originDeviceId
└── groupId
```

The exact authorization metadata will be finalized during implementation.

### 17.56 Revoked Device

If a Device is revoked:

```text
Device D1
    ↓
REVOKED
```

future backend synchronization operations from D1 should be rejected.

Previously synchronized local data must not automatically be deleted.

### 17.57 Revoked Peer

Similarly:

```text
Peer D1
    ↓
No longer trusted
```

the local device must stop accepting new synchronization sessions from that peer.

### 17.58 Group Removal and Synchronization

When a User is removed from a Group:

```text
Group Membership
    ↓
REMOVED
```

new synchronization access should be blocked according to the authorization policy.

Existing local data handling must follow the Group data-retention policy.

### 17.59 Group Ownership Transfer

If ownership can be transferred:

```text
Owner U1
    ↓
Transfer Ownership
    ↓
Owner U2
```

the authorization state must be synchronized consistently.

Both local and backend authorization checks must respect the new ownership.

### 17.60 Group Deletion

Group deletion is a privileged operation.

Conceptually:

```text
Delete Group
      ↓
Verify Owner / Authorized Role
      ↓
Verify Group State
      ↓
Create Group Deletion Change
      ↓
Synchronize
```

The exact deletion/tombstone strategy will be defined in the Group lifecycle.

### 17.61 Permission Matrix

The final permission matrix should explicitly define operations.

A preliminary V1 model:

| Operation | OWNER | ADMIN | MEMBER |
|---|---:|---:|---:|
| View Group | Yes | Yes | Yes |
| View Expenses | Yes | Yes | Yes |
| Create Expense | Yes | Yes | Yes |
| Update Own Expense | Yes | Yes | Yes |
| Update Others' Expense | Yes | Configurable | Configurable |
| Delete Own Expense | Yes | Yes | Yes |
| Delete Others' Expense | Yes | Configurable | No |
| Create Settlement | Yes | Yes | Yes |
| View Settlements | Yes | Yes | Yes |
| Manage Members | Yes | Yes | No |
| Change Group Settings | Yes | Configurable | No |
| Delete/Archive Group | Yes | No | No |

The exact V1 permission matrix must be finalized before implementation.

### 17.62 Principle of Least Privilege

Users should receive only the permissions required for their role.

For example:

```text
MEMBER
```

should not automatically receive:

```text
Group Membership Administration
```

### 17.63 Default Deny

Authorization should follow:

```text
Permission Explicitly Granted
        ↓
Allowed

Permission Not Granted
        ↓
Denied
```

The system should not assume permission merely because an operation was not explicitly blocked.

### 17.64 Authorization and Synchronization Scope

A synchronization request must be scoped.

Example:

```text
Request:
Synchronize G1
```

must not implicitly return:

```text
G2
G3
G4
```

unless explicitly authorized and requested.

### 17.65 Data Minimization

The synchronization layer should transmit only data required for the authorized scope.

Example:

```text
Peer authorized for G1
```

should receive:

```text
G1 data
```

not:

```text
All local Groups
```

### 17.66 Authorization and Profile Data

User profile data also requires access control.

Not every Group member necessarily needs access to every profile attribute.

For example:

```text
Display Name
```

may be shared for Group identification.

Sensitive attributes such as:

```text
Phone Number
Email
```

should only be exposed when required by the relevant feature and authorization rules.

### 17.67 Phone Number Authorization

A phone number must not become globally visible simply because a User belongs to a Group.

Access to phone numbers should be explicitly controlled by the relevant profile/privacy policy.

### 17.68 Email Authorization

Email follows the same principle.

The application should not broadcast email addresses to nearby peers unless explicitly required and authorized.

### 17.69 P2P Authorization Example

```text
Device A
    ↓
Discovers Device B
    ↓
B belongs to G1
    ↓
A and B authenticate
    ↓
B requests G1 synchronization
    ↓
A verifies B's Group membership
    ↓
Authorized
    ↓
Exchange G1 operations
```

### 17.70 P2P Unauthorized Example

```text
Device A
    ↓
Discovers Device B
    ↓
B is not a member of G1
    ↓
B requests G1 synchronization
    ↓
Authorization Check
    ↓
DENIED
```

No Group financial data should be transmitted.

### 17.71 Backend Unauthorized Example

```text
Device A
    ↓
Authenticated User U1
    ↓
Request:
Update Expense E100
    ↓
Expense belongs to G2
    ↓
U1 is not authorized for G2
    ↓
REJECT
```

The backend must not trust the client-provided claim that U1 owns or belongs to G2.

### 17.72 Authorization Result

Authorization checks should produce a structured result.

Conceptually:

```text
AuthorizationResult
├── allowed
├── permission
├── reasonCode
└── metadata
```

Example:

```text
allowed = false
reasonCode = NOT_GROUP_MEMBER
```

### 17.73 Authorization Reason Codes

Potential V1 reason codes:

```text
NOT_AUTHENTICATED
DEVICE_NOT_REGISTERED
DEVICE_REVOKED
NOT_GROUP_MEMBER
MEMBERSHIP_INACTIVE
INSUFFICIENT_ROLE
RESOURCE_NOT_FOUND
GROUP_ACCESS_DENIED
OPERATION_NOT_ALLOWED
```

The final error-code catalog will be defined during API/Security implementation.

### 17.74 Authorization Audit

Security-sensitive authorization events may be logged for diagnostics/auditing.

Examples:

```text
Unauthorized synchronization attempt
Device revocation
Membership change
Role change
Ownership transfer
```

Logs must not unnecessarily contain sensitive financial or personal information.

### 17.75 Local Authorization Logs

The Android application should avoid retaining excessive sensitive authorization logs.

Local logs should primarily support:

```text
Debugging
Error Diagnosis
Security Diagnostics
```

### 17.76 Backend Authorization Logs

The backend may maintain security-relevant audit information for:

```text
Authentication
Authorization failures
Device registration
Device revocation
Membership changes
Administrative actions
```

The exact retention policy will be defined separately.

### 17.77 Authorization and Transactions

Authorization must be validated before committing the Domain change.

Conceptually:

```text
BEGIN TRANSACTION

Authorize
    ↓
Validate
    ↓
Apply Domain Change
    ↓
Create SyncOperation
    ↓
COMMIT
```

If authorization fails:

```text
ROLLBACK
```

### 17.78 Unauthorized Operation and Sync Queue

An unauthorized local operation must never enter:

```text
sync_operations
```

because no valid Domain change was created.

### 17.79 Unauthorized Remote Operation

A rejected remote operation may be recorded as:

```text
REJECTED
```

for synchronization diagnostics, but it must not modify Domain state.

### 17.80 Authorization and Conflict Data

Authorization failure must not automatically create a normal Conflict.

Example:

```text
User removed from Group
      ↓
Offline Expense arrives
      ↓
Authorization fails
```

This is:

```text
AUTHORIZATION_FAILURE
```

not:

```text
CONCURRENT_UPDATE
```

unless both conditions independently exist.

### 17.81 Authorization and Conflict Can Coexist

An operation may have:

```text
Version Conflict
+
Authorization Problem
```

The system must apply a defined validation order.

Security-sensitive authorization checks should not be bypassed simply because a version conflict exists.

### 17.82 Authorization and Derived Data

Derived balances do not require independent authorization records.

Instead:

```text
User authorized to view Group
        ↓
Can calculate/view authorized Group balance
```

The balance is derived from authorized Domain data.

### 17.83 Authorization and Offline Balance Calculation

Offline:

```text
User
    ↓
Authorized Group Member
    ↓
Read Group Expenses
    ↓
Calculate Balance
```

No Internet request is required.

### 17.84 Authorization Flow — Local Expense

```text
User
  ↓
Authenticated Locally
  ↓
Select Group
  ↓
Check Active Membership
  ↓
Check Permission
  ↓
Validate Expense
  ↓
Save Expense
  ↓
Create SyncOperation
```

### 17.85 Authorization Flow — Backend Expense

```text
Request
  ↓
Authenticate User/Device
  ↓
Identify Group
  ↓
Check Membership
  ↓
Check Permission
  ↓
Validate Expense
  ↓
Validate Version
  ↓
Apply Transaction
```

### 17.86 Authorization Flow — P2P Expense

```text
Receive Operation
  ↓
Authenticate Peer
  ↓
Identify Origin User/Device
  ↓
Identify Group
  ↓
Check Group Authorization
  ↓
Validate Operation
  ↓
Validate Version
  ↓
Apply Transaction
```

### 17.87 Complete Authorization Flow

```text
Request / Local Action
        ↓
Identify User
        ↓
Identify Device
        ↓
Authentication Valid?
        │
        ├── No → Reject / Local-only handling
        │
        └── Yes
              ↓
        Identify Resource
              ↓
        Identify Group
              ↓
        Check Membership
              ↓
        Check Role / Permission
              ↓
        Check Device Authorization
              ↓
        Validate Domain Rules
              ↓
        Validate Version
              ↓
        Apply Transaction
              ↓
        Create SyncOperation if required
```

### 17.88 Authorization Model Summary

```text
Authorization
│
├── Identity
│   ├── User
│   └── Device
│
├── Group Access
│   ├── Membership
│   ├── Role
│   └── Permissions
│
├── Entity Access
│   ├── Group
│   ├── Expense
│   ├── ExpenseSplit
│   └── Settlement
│
├── Synchronization Access
│   ├── Backend
│   └── Peer
│
└── Security Rules
    ├── Default Deny
    ├── Least Privilege
    ├── Independent Backend Validation
    └── Data Minimization
```

### 17.89 Authorization Invariants

The following rules are mandatory for V1:

- Authentication and authorization must remain separate concerns.
- A successfully authenticated User is not automatically authorized for every operation.
- Local authorization must work without Internet connectivity.
- Backend authorization must be independently validated by the backend.
- P2P authorization must be independently validated by the receiving device.
- Being connected to the same Wi-Fi or Hotspot does not grant authorization.
- Group membership is the primary authorization boundary for shared expense data.
- Only active Group members may perform ordinary Group operations.
- Pending invitations do not automatically grant full Group access.
- Removed members must not retain authorization for new Group operations.
- Group roles must be scoped to the specific Group.
- A User's role in one Group must not automatically apply to another Group.
- Ownership and administrative permissions must be explicitly defined.
- The permission model must follow least privilege.
- The default authorization behavior must be deny unless permission is explicitly granted.
- UI visibility must not be treated as a security boundary.
- Domain/Application authorization must exist independently of UI checks.
- Unauthorized local operations must not modify Domain state.
- Unauthorized local operations must not create SyncOperations.
- Unauthorized remote operations must not modify Domain state.
- Authorization failures must be distinguished from authentication failures.
- Authorization failures must be distinguished from Domain validation failures.
- Authorization failures must be distinguished from synchronization conflicts.
- SyncOperations must carry enough origin information to validate authorization.
- Forwarded SyncOperations must preserve their original User and Device identity.
- P2P synchronization must be limited to authorized Groups.
- Synchronization requests must be scoped to authorized data.
- Sensitive profile information must not be unnecessarily exposed during synchronization or discovery.
- Phone numbers and email addresses must not be treated as unrestricted Group data.
- Device revocation must prevent future unauthorized synchronization.
- Authentication failure must not delete local Domain data.
- Membership changes must be synchronized consistently.
- Authorization checks must occur before committing sensitive Domain changes.
- Authorization and Domain changes should be applied transactionally where required.
- Backend authorization must never rely solely on claims supplied by the Android client.
- Conflict resolution must not bypass authorization checks.
- Derived balances inherit authorization from the underlying Group/Domain data.
- Authorization rules must support both Backend and Peer synchronization.
- Authorization design must preserve eventual convergence only among authorized devices and Users.

## 18. Online Flow

### 18.1 Purpose

This section defines the online execution flow for SplitSync V1.

SplitSync is designed to support both:

```text
Online Operation
```

and:

```text
Offline Operation
```

without maintaining separate application modes.

When Internet connectivity is available, the application should automatically use the backend for:

- Synchronization.
- Server-side backup.
- Cross-device synchronization.
- Backend authentication.
- Server-side Group synchronization.
- Recovery of synchronized data.

When Internet connectivity is unavailable, the application must continue using the local database and local-first architecture.

### 18.2 Core Principle

The application must not require the User to manually select:

```text
ONLINE MODE
```

or:

```text
OFFLINE MODE
```

Instead:

```text
Application
     ↓
Detect Available Connectivity
     ↓
Select Appropriate Synchronization Channel
```

The User should experience one continuous application.

### 18.3 Online Does Not Mean Local Data Is Replaced

Even when Internet is available:

```text
Room Database
```

remains the primary local working data source.

The architecture is:

```text
User
 ↓
Android Application
 ↓
Local Domain / Room
 ↓
Sync Engine
 ↓
Backend
```

not:

```text
User
 ↓
Backend
 ↓
Application
```

### 18.4 Online Architecture

Conceptually:

```text
┌─────────────────────────────┐
│       Android Device        │
│                             │
│  UI                          │
│   ↓                          │
│  Domain                      │
│   ↓                          │
│  Room Database               │
│   ↓                          │
│  Sync Engine                 │
└──────────────┬──────────────┘
               │
               │ Internet
               ▼
┌─────────────────────────────┐
│       Spring Boot Backend   │
│                             │
│ Authentication              │
│ Authorization               │
│ Sync API                    │
│ Domain Validation           │
│ Persistence                 │
└──────────────┬──────────────┘
               │
               ▼
           MySQL
```

### 18.5 Online Flow Overview

The general online flow is:

```text
Application Launch
        ↓
Load Local User
        ↓
Load Local Data
        ↓
Detect Connectivity
        ↓
Internet Available
        ↓
Check Backend Authentication
        ↓
Authenticate if Required
        ↓
Start Synchronization
        ↓
Push Local Changes
        ↓
Pull Remote Changes
        ↓
Apply Changes Locally
        ↓
Update SyncState
        ↓
Application Continues Normally
```

### 18.6 Application Launch With Internet

When the application starts while Internet is available:

```text
Application Launch
        ↓
Load Device
        ↓
Load Local User
        ↓
Load Local Groups
        ↓
Load Local Expenses
        ↓
Display Local State
        ↓
Check Connectivity
        ↓
Start Backend Synchronization
```

The application should not wait for synchronization before displaying locally available data unless a specific operation explicitly requires fresh server data.

### 18.7 Local Data First

The User should see locally stored data immediately.

Example:

```text
Application Launch
      ↓
Room Database
      ↓
Display Groups
      ↓
Display Expenses
      ↓
Background Synchronization
```

This keeps the application responsive even when:

```text
Backend is slow
```

or:

```text
Network is unstable
```

### 18.8 Connectivity Detection

The application should determine whether backend connectivity is currently available.

Conceptually:

```text
Connectivity Check
        ↓
Internet Available?
    ┌───┴────┐
   Yes       No
    │         │
Backend     Local
Available   Operation
```

Connectivity detection must not be treated as proof that the backend request will succeed.

### 18.9 Internet Available but Backend Unavailable

Possible scenario:

```text
Internet = ON
Backend = DOWN
```

The application must behave similarly to an offline synchronization condition:

```text
Local functionality
       ↓
Available

SyncOperations
       ↓
Remain PENDING / Retryable
```

The application must not lose local data.

### 18.10 Backend Authentication

Before backend synchronization:

```text
Check Authentication
        ↓
Authenticated?
   ┌────┴────┐
  Yes       No
   │          │
 Sync       Authenticate
```

If authentication is unavailable:

```text
Local functionality continues
```

### 18.11 Authentication Expiration

If the backend session has expired:

```text
Authenticated
      ↓
Token Expired
      ↓
Authentication Required
```

The application should:

```text
Preserve local data
Preserve pending SyncOperations
Request/perform re-authentication
Resume synchronization after success
```

### 18.12 Local Expense Creation While Online

Even online, a newly created Expense follows the local-first path:

```text
User Creates Expense
        ↓
Validate Locally
        ↓
Save Expense in Room
        ↓
Save ExpenseSplits
        ↓
Create SyncOperation
        ↓
COMMIT
        ↓
Synchronization Worker
        ↓
Backend
```

The application should not require the backend request to succeed before the Expense appears locally.

### 18.13 Online Write Flow

Complete write flow:

```text
User Action
    ↓
Domain Validation
    ↓
Authorization
    ↓
Room Transaction
    ├── Save Domain Entity
    ├── Save Related Entities
    └── Create SyncOperation
    ↓
COMMIT
    ↓
UI Updates
    ↓
Sync Worker
    ↓
Backend API
```

### 18.14 Backend Push

The Sync Engine sends pending operations to the backend.

Example:

```text
sync_operations

O100 → PENDING
O101 → PENDING
O102 → PENDING
```

The synchronization worker sends them:

```text
O100
O101
O102
```

and processes the corresponding results.

### 18.15 Backend Operation Validation

The backend validates each operation:

```text
Authentication
        ↓
Authorization
        ↓
Operation Structure
        ↓
Domain Rules
        ↓
Version
        ↓
Idempotency
        ↓
Apply Transaction
```

### 18.16 Backend Operation Result

The backend may return:

```text
APPLIED
ALREADY_APPLIED
CONFLICT
REJECTED
```

The Android application processes each result accordingly.

### 18.17 Successful Push

Example:

```text
O100
    ↓
Backend
    ↓
APPLIED
```

Local state:

```text
O100
    ↓
COMPLETED
```

### 18.18 Duplicate Push

Example:

```text
O100
    ↓
Backend
    ↓
Already processed
    ↓
ALREADY_APPLIED
```

The local operation can safely become:

```text
COMPLETED
```

because the intended change already exists remotely.

### 18.19 Conflict During Push

Example:

```text
O100
    ↓
Backend
    ↓
Version Conflict
```

Result:

```text
CONFLICT
```

The Android application creates/updates the corresponding Conflict state.

The local User's data must not be silently overwritten.

### 18.20 Rejected Operation

Example:

```text
O100
    ↓
Backend
    ↓
Authorization Failed
```

Result:

```text
REJECTED
```

The local application must process the rejection according to the authorization/conflict policy.

It must not repeatedly retry a permanent rejection indefinitely.

### 18.21 Pull Flow

After pushing local changes, the application pulls remote changes.

Conceptually:

```text
Local SyncState
      ↓
Current Cursor
      ↓
Backend
      ↓
Changes After Cursor
      ↓
Android
      ↓
Validate
      ↓
Apply Transaction
      ↓
Advance Cursor
```

### 18.22 Incremental Pull

Example:

```text
Local Cursor:
C500
```

Backend:

```text
C501
C502
C503
C504
```

Request:

```text
Changes after C500
```

Response:

```text
C501-C504
```

After successful application:

```text
Local Cursor:
C504
```

### 18.23 Pull Transaction

Remote changes and SyncState advancement should be applied transactionally:

```text
BEGIN TRANSACTION

Apply Remote Changes
        ↓
Update SyncState Cursor
        ↓
COMMIT
```

Failure:

```text
ROLLBACK
```

The cursor must remain at the last successfully applied position.

### 18.24 Online Synchronization Order

The default V1 synchronization cycle may follow:

```text
1. Check Authentication
        ↓
2. Push Local Operations
        ↓
3. Pull Remote Changes
        ↓
4. Apply Remote Changes
        ↓
5. Update SyncState
        ↓
6. Repeat if More Changes Exist
        ↓
7. Mark Synchronized
```

The exact push/pull optimization can be adjusted during implementation.

### 18.25 Why Push First

Pushing local changes first can reduce the period in which:

```text
Local change
```

exists only on the device.

However, the final synchronization protocol may optimize the order depending on:

```text
Conflict Detection
Cursor Semantics
Server Protocol
Performance
```

### 18.26 Online Synchronization Is Incremental

The application should not download the complete Group database every time.

Instead:

```text
Initial Sync
    ↓
Full Required State

Future Sync
    ↓
Incremental Changes
```

This reduces:

```text
Bandwidth
Processing
Battery Consumption
Server Load
```

### 18.27 Initial Online Synchronization

For a newly backend-associated User:

```text
Local User
      ↓
Backend Authentication
      ↓
Initial Sync
      ↓
Download Authorized Groups
      ↓
Download Group Members
      ↓
Download Expenses
      ↓
Download Expense Splits
      ↓
Download Settlements
      ↓
Create/Update Local State
      ↓
Set SyncState
```

### 18.28 Existing Local Data During Initial Sync

If local data already exists before backend association:

```text
Local Data
      +
Backend Data
```

must not be blindly merged or overwritten.

The system must perform:

```text
Identity Mapping
+
Duplicate Detection
+
Operation Synchronization
+
Conflict Detection
```

The exact local-to-backend account-linking migration strategy will be defined before implementation.

### 18.29 Online Group Creation

When Internet is available:

```text
User Creates Group
      ↓
Save Group Locally
      ↓
Create SyncOperation
      ↓
Backend Synchronization
      ↓
Backend Accepts
      ↓
Operation Completed
```

The Group should appear immediately in the local UI.

### 18.30 Online Group Join

A User joining a Group may follow:

```text
Invitation / Join Request
      ↓
Authentication
      ↓
Authorization
      ↓
Membership Created
      ↓
Local Group State Updated
      ↓
SyncState Created
      ↓
Group Data Synchronization
```

The exact invitation/join mechanism depends on the Group flow.

### 18.31 Online Profile Synchronization

When backend connectivity exists, the local profile may synchronize permitted profile information.

Conceptually:

```text
Local User Profile
      ↓
SyncOperation
      ↓
Backend
      ↓
Validated User Profile
```

Sensitive attributes must follow the Security and Authorization rules.

### 18.32 Online Expense Editing

Example:

```text
Expense E1
Version 5
```

User edits:

```text
Version 6
```

Local:

```text
Save E1 Version 6
Create SyncOperation
```

Backend:

```text
Validate baseVersion = 5
Current Version = 5
        ↓
Apply Version 6
```

### 18.33 Online Concurrent Edit

Example:

```text
Device A:
Version 5 → Version 6

Device B:
Version 5 → Version 6
```

Both synchronize with the backend.

The backend detects:

```text
Concurrent Update
```

and creates a Conflict according to the Conflict Data Model.

### 18.34 Online Settlement

Settlement follows the same local-first pattern:

```text
Create Settlement
      ↓
Validate
      ↓
Save Locally
      ↓
Create SyncOperation
      ↓
Push Backend
      ↓
Backend Validation
      ↓
Apply / Reject / Conflict
```

### 18.35 Online Balance Calculation

Balances remain locally derived:

```text
Expenses
+
ExpenseSplits
+
Settlements
        ↓
Balance Calculation
```

The application does not need to request a balance from the backend for every screen update.

### 18.36 Backend Balance Validation

The backend may calculate balances independently for:

```text
Validation
Reporting
API Responses
```

but the synchronized source of truth remains the Domain entities.

### 18.37 Online UI State

The UI may show synchronization status:

```text
Synced
Syncing...
Sync pending
Sync failed
Conflict requires attention
```

The exact UI wording will be defined in the UI Design.

### 18.38 Online Sync Indicator

A lightweight status indicator may represent:

```text
SYNCED
SYNCING
PENDING
FAILED
CONFLICT
```

The indicator must not block normal local operations.

### 18.39 Online Request Failure

A backend request can fail despite Internet availability.

Example:

```text
Internet = ON
Request = FAILED
```

The application should:

```text
Preserve local data
Keep SyncOperation retryable
Update SyncState
Retry according to policy
```

### 18.40 Network Timeout

Example:

```text
Request
   ↓
Timeout
```

The operation remains safe because:

```text
Operation ID
```

provides idempotency.

The client can retry:

```text
Same Operation ID
```

without creating duplicate data.

### 18.41 Server Unavailable

If:

```text
Backend = unavailable
```

the application behaves locally:

```text
Local changes
    ↓
Persist
    ↓
SyncOperation = PENDING
```

Synchronization resumes when the backend becomes available.

### 18.42 Backend Maintenance

During backend maintenance:

```text
Backend unavailable
```

the application remains usable locally.

The User should not lose:

```text
Expenses
Groups
Settlements
Balances
```

### 18.43 Online to Offline Transition

Important scenario:

```text
Online
   ↓
Internet Lost
```

The application should automatically transition operationally to:

```text
Local-first operation
```

No restart should be required.

### 18.44 Online to Offline Flow

```text
Application Online
      ↓
Internet Lost
      ↓
Current Local Data Remains Available
      ↓
New Changes Saved Locally
      ↓
SyncOperations Become/Remain PENDING
      ↓
P2P Available?
   ┌──┴──┐
  Yes    No
   │      │
 P2P    Wait
```

### 18.45 Offline to Online Transition

When Internet becomes available:

```text
Offline
   ↓
Connectivity Detected
   ↓
Backend Available
   ↓
Authenticate if Required
   ↓
Process Pending SyncOperations
   ↓
Pull Remote Changes
   ↓
Update SyncState
   ↓
Continue Normal Online Synchronization
```

### 18.46 Automatic Recovery

The User should not have to manually re-enter expenses after connectivity is restored.

Example:

```text
Offline:
Expense E1 created

Internet returns

E1
 ↓
Automatic Sync
 ↓
Backend
 ↓
Completed
```

### 18.47 P2P and Online Simultaneously

Possible state:

```text
Internet = ON
P2P Peer = Available
```

The synchronization engine may have:

```text
Backend Channel
+
Peer Channel
```

available simultaneously.

Both channels must use:

```text
Operation ID
Idempotency
Conflict Detection
Authorization
```

### 18.48 Online Sync Through Backend After P2P

Example:

```text
Device A
    ↓
Creates E1
    ↓
P2P → Device B
    ↓
Device B receives E1
    ↓
Internet becomes available
    ↓
Device A → Backend
    ↓
Backend receives E1
```

If Device B later receives E1 from the backend:

```text
Operation ID already processed
    ↓
ALREADY_APPLIED
```

No duplicate Expense is created.

### 18.49 Online Multi-Device Flow

Example:

```text
Device A
    ↓
Creates Expense E1
    ↓
Backend
    ↓
Device B
    ↓
Receives E1
```

Device B updates:

```text
Room Database
```

and:

```text
SyncState
```

after successful application.

### 18.50 Backend as Synchronization Hub

The backend may act as the central synchronization hub when Internet is available:

```text
Device A
      ↓
      Backend
      ↑
Device B
```

The backend stores authorized synchronized state and exposes incremental changes to connected devices.

### 18.51 Backend Is Not Required for Local Operation

Even though the backend acts as the central synchronization hub online:

```text
Backend unavailable
```

does not mean:

```text
Application unavailable
```

The local database remains operational.

### 18.52 Online Data Consistency

The goal is:

```text
Local Changes
      +
Remote Changes
      ↓
Validated Synchronization
      ↓
Eventual Convergence
```

Temporary differences between devices are expected during synchronization.

### 18.53 Online Conflict Handling

Conflict handling follows the previously defined model:

```text
Incoming Change
      ↓
Version Check
      ↓
Conflict?
   ┌──┴──┐
  No    Yes
   │      │
Apply   Conflict
         ↓
    Resolution
```

The Online Flow does not introduce a separate conflict model.

### 18.54 Online Authorization

Every backend operation must pass:

```text
Authentication
      ↓
Authorization
      ↓
Domain Validation
      ↓
Version Validation
```

The Android application must not assume that a locally valid operation will necessarily be accepted by the backend.

### 18.55 Online Security

All backend communication must use secure transport and the authentication/security mechanisms defined in the Security Architecture.

The application must protect:

```text
Authentication credentials
Access tokens
User information
Group data
Financial information
```

### 18.56 Online API Failure Handling

API responses should be classified into:

```text
Success
Retryable Failure
Authentication Failure
Authorization Failure
Validation Failure
Conflict
Permanent Failure
```

The Sync Engine maps these responses to:

```text
SyncOperation Status
SyncState Status
Conflict State
```

### 18.57 Online Retry

Retryable operations should use controlled retry/backoff.

Conceptually:

```text
Request Failed
      ↓
Determine Error
      ↓
Retryable?
   ┌──┴──┐
  Yes    No
   │      │
Backoff  Process Result
   │
Retry
```

The same Operation ID must be reused.

### 18.58 Online Batch Synchronization

The Sync Engine may send multiple operations in a batch:

```text
O100
O101
O102
O103
```

Batching may improve:

```text
Network efficiency
Battery usage
Server throughput
```

The backend must still provide sufficient per-operation results.

### 18.59 Online Large Group

For larger Groups, synchronization should use:

```text
Incremental changes
Pagination
Batched operations
Cursor-based pulling
```

rather than downloading the entire Group on every synchronization cycle.

### 18.60 Online Initial Group Sync

For a newly joined Group:

```text
Join Group
      ↓
Authorized
      ↓
Request Initial Group State
      ↓
Download Required Data
      ↓
Persist Locally
      ↓
Create SyncState
      ↓
Calculate Local Balances
```

### 18.61 Online Data Pagination

Large remote change sets may be returned in pages/batches.

Example:

```text
C500
   ↓
Page 1 → C501-C550
   ↓
Page 2 → C551-C600
   ↓
Page 3 → C601-C650
```

The cursor must only advance after the corresponding data has been successfully committed.

### 18.62 Online Sync Completion

A synchronization cycle is considered complete when:

```text
All eligible local operations processed
        +
All required remote changes processed
        +
No unresolved blocking synchronization state
```

The SyncState may then become:

```text
SYNCED
```

### 18.63 Online Sync State Example

```text
Device:
D001

Scope:
GROUP G100

Channel:
BACKEND

Status:
SYNCED

Cursor:
C1000

Last Successful Sync:
2026-08-25T12:00:00Z
```

### 18.64 Online Failed State Example

```text
Device:
D001

Scope:
GROUP G100

Channel:
BACKEND

Status:
FAILED

Cursor:
C1000

Last Successful Sync:
2026-08-25T12:00:00Z

Error:
SERVER_UNAVAILABLE
```

Local Group data remains available.

### 18.65 Online Conflict State Example

```text
Device:
D001

Scope:
GROUP G100

Channel:
BACKEND

Status:
BLOCKED

Cursor:
C1000

Error:
CONFLICT
```

Only the affected conflict should require resolution where possible.

### 18.66 Online Flow With Peer Fallback

A possible hybrid strategy is:

```text
Local Change
      ↓
Backend Available?
   ┌──┴──┐
  Yes    No
   │      │
Backend  Peer Available?
           │
        ┌──┴──┐
       Yes    No
        │      │
       Peer   Pending
```

The synchronization engine can retry the backend later even after successful P2P synchronization.

### 18.67 No Manual Mode Switching

The application must not require the User to choose:

```text
Offline Mode
```

when the Internet disappears.

Similarly, the User should not have to manually choose:

```text
Online Mode
```

when connectivity returns.

Connectivity should influence synchronization behavior automatically.

### 18.68 Online Flow and Local-First Guarantee

The most important rule is:

```text
Online synchronization must never break local-first operation.
```

Therefore:

```text
Backend Failure
    ≠
Local Failure
```

and:

```text
Internet Failure
    ≠
Application Failure
```

### 18.69 Complete Online Flow

```text
Application Launch
        ↓
Load Local Identity
        ↓
Load Local Domain Data
        ↓
Display Local State
        ↓
Detect Internet
        ↓
Backend Available?
        │
   ┌────┴────┐
  Yes        No
   │          │
Authenticate  Continue
   │          │
   ↓          ↓
Push Local   Local
Changes      Operation
   ↓
Pull Remote
Changes
   ↓
Apply Changes
   ↓
Detect Conflicts
   ↓
Update SyncState
   ↓
Synchronization Complete
```

### 18.70 Online Flow Summary

```text
Online Flow
│
├── Local Startup
│   ├── Load Device
│   ├── Load User
│   └── Load Local Data
│
├── Connectivity
│   ├── Detect Internet
│   └── Detect Backend Availability
│
├── Authentication
│   ├── Validate Session
│   └── Authenticate if Required
│
├── Push
│   ├── Load Pending Operations
│   ├── Send to Backend
│   └── Process Results
│
├── Pull
│   ├── Use Sync Cursor
│   ├── Receive Remote Changes
│   └── Apply Locally
│
├── Conflict Handling
│   ├── Detect
│   ├── Record
│   └── Resolve
│
└── Completion
    ├── Update SyncState
    └── Continue Local Operation
```

### 18.71 Online Flow Invariants

The following rules are mandatory for V1:

- The application must automatically detect and use Internet connectivity when available.
- The User must not be required to manually switch between Online and Offline modes.
- Local Room data remains the primary working data source even when online.
- The application must display locally available data without unnecessarily waiting for backend synchronization.
- Core expense functionality must remain available if the backend is unavailable.
- Internet availability does not guarantee backend availability.
- Backend authentication is required only for operations that require server authorization.
- Authentication failure must not delete or invalidate local financial data.
- Local changes must be persisted before backend synchronization is attempted.
- Every successful local Domain change must have its corresponding SyncOperation persisted atomically.
- Pending SyncOperations must survive network failures.
- Backend synchronization must be idempotent.
- Retries must reuse the original Operation ID.
- Duplicate operations must never create duplicate financial records.
- Backend authorization must be independently validated.
- Remote changes must be validated before being applied locally.
- Remote changes and cursor advancement must be applied transactionally where practical.
- A failed remote transaction must not advance the synchronization cursor.
- Online synchronization must use incremental synchronization after initial synchronization.
- Large synchronization sets must support batching and pagination.
- Balances must remain derived from synchronized Domain data.
- Online synchronization must use the same Conflict Data Model as offline/peer synchronization.
- Financial conflicts must not be silently resolved using arbitrary last-write-wins.
- A synchronization failure must not make the local application unusable.
- Transition from Online to Offline must happen without requiring application restart.
- Transition from Offline to Online must automatically resume pending synchronization.
- P2P and Backend synchronization may coexist.
- The same Operation ID must remain valid across P2P and Backend synchronization.
- Successful P2P synchronization must not prevent later Backend synchronization.
- Backend synchronization must not create duplicate records for data already received through P2P.
- Synchronization state must be persisted and recoverable after application/device restart.
- Online synchronization must preserve eventual convergence across authorized devices.
- Online functionality must improve synchronization and backup capabilities without compromising the local-first architecture.

## 19. Offline Flow

### 19.1 Purpose

This section defines the offline execution flow for SplitSync V1.

SplitSync must remain fully usable for its core expense-management functionality when:

```text
Internet = unavailable
Backend = unavailable
```

The application continues to operate using:

```text
Local User
+
Local Groups
+
Local Expenses
+
Local Expense Splits
+
Local Settlements
+
Local SyncOperations
+
Local SyncState
```

The fundamental principle is:

```text
No Internet
    ≠
No Application
```

### 19.2 Core Principle

The offline flow is based on:

```text
Create Locally
      ↓
Persist Locally
      ↓
Use Locally
      ↓
Queue Synchronization
      ↓
Sync Later
```

The User should not have to manually enable an Offline Mode.

### 19.3 Offline Architecture

Conceptually:

```text
┌─────────────────────────────┐
│       Android Device        │
│                             │
│           UI                │
│            ↓                │
│        Domain Layer         │
│            ↓                │
│       Room Database         │
│            ↓                │
│       Sync Engine           │
│            ↓                │
│     Pending SyncOperations  │
│                             │
└─────────────────────────────┘

Internet = OFF
Backend  = Unavailable
```

The application continues to function entirely inside the device.

### 19.4 Offline Startup

When the application starts without Internet:

```text
Application Launch
        ↓
Load Device
        ↓
Load Local User
        ↓
Load Local Groups
        ↓
Load Local Expenses
        ↓
Load Local Settlements
        ↓
Load SyncState
        ↓
Application Ready
```

No backend request is required for startup.

### 19.5 Offline Identity

The local identity remains available:

```text
User ID
+
Device ID
```

The application must not attempt to create a new User merely because the Internet is unavailable.

### 19.6 Offline User Creation

A new User can be created completely offline:

```text
Application Installed
        ↓
Generate Device ID
        ↓
Create Local User
        ↓
Store User Locally
        ↓
Application Ready
```

No server account is required.

### 19.7 Offline Group Creation

A User can create a Group offline:

```text
User
  ↓
Create Group
  ↓
Validate Locally
  ↓
Generate Group ID
  ↓
Save Group
  ↓
Create Group Membership
  ↓
Create SyncOperation
  ↓
COMMIT
```

The Group becomes immediately available locally.

### 19.8 Offline Group Identity

A Group created offline must have a stable:

```text
groupId
```

The Group ID must remain unchanged when the Group later synchronizes with:

```text
Backend
```

or:

```text
Peer Device
```

### 19.9 Offline Group Membership

The local creator can establish the initial local membership:

```text
User U1
    ↓
Group G1
    ↓
OWNER
```

This state is persisted locally.

Later, membership changes can be synchronized through:

```text
P2P
```

or:

```text
Backend
```

### 19.10 Offline Profile Identification

Nearby Users may be identified through P2P connectivity when available.

Example:

```text
Internet = OFF
Local Wi-Fi = ON
Hotspot = ON
```

Users may discover nearby SplitSync profiles through the P2P mechanism.

### 19.11 Offline Without Any Connectivity

The application must also work when:

```text
Internet = OFF
P2P = unavailable
```

The User can still:

```text
View Groups
View Expenses
Create Expenses
Edit Expenses
Calculate Balances
Create Settlements
View Settlements
```

subject to normal local authorization and Domain rules.

### 19.12 Offline Expense Creation

The standard offline Expense flow is:

```text
User Creates Expense
        ↓
Validate Expense Locally
        ↓
Validate Group Membership
        ↓
Validate Permission
        ↓
Generate Expense ID
        ↓
Generate Operation ID
        ↓
Save Expense
        ↓
Save Expense Splits
        ↓
Create SyncOperation
        ↓
COMMIT
```

### 19.13 Offline Transaction Boundary

Expense creation must be atomic.

Conceptually:

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

If anything fails:

```text
ROLLBACK
```

This prevents situations where:

```text
Expense exists
```

but:

```text
SyncOperation does not exist
```

for a change that needs synchronization.

### 19.14 Offline Expense Editing

An existing Expense can be edited offline:

```text
Expense E1
    ↓
User Edits
    ↓
Validate Authorization
    ↓
Validate Domain Rules
    ↓
Update Local Expense
    ↓
Generate SyncOperation
    ↓
COMMIT
```

The edited state is immediately visible locally.

### 19.15 Offline Expense Deletion

If deletion is supported by the Expense lifecycle:

```text
Delete Expense
      ↓
Authorization
      ↓
Domain Validation
      ↓
Create Delete Operation / Tombstone
      ↓
Persist Locally
      ↓
Queue Synchronization
```

The exact deletion/tombstone strategy follows the Expense lifecycle and synchronization model.

### 19.16 Offline Expense Split

Expense Splits are created and maintained locally.

Example:

```text
Expense E1
      ↓
Participants
      ↓
Expense Splits
      ↓
Validate Split
      ↓
Save Locally
```

The split must satisfy the applicable splitting rules before the transaction is committed.

### 19.17 Offline Settlement

Settlements can also be created offline:

```text
Create Settlement
      ↓
Validate Group
      ↓
Validate Participants
      ↓
Validate Amount
      ↓
Save Settlement
      ↓
Create SyncOperation
      ↓
COMMIT
```

### 19.18 Offline Balance Calculation

Balances are calculated entirely from local data:

```text
Expenses
+
Expense Splits
+
Settlements
        ↓
Balance Calculation
```

No Internet request is required.

### 19.19 Offline Balance Example

Suppose:

```text
A paid ₹1000
B paid ₹500
```

and the split data indicates:

```text
A owes ₹750
B owes ₹750
```

The local application can calculate:

```text
A → receives ₹250
B → pays ₹250
```

without contacting the backend.

### 19.20 Offline Data Source

For normal application screens:

```text
Room Database
```

is the source of locally available state.

The UI should not depend on a network API to display:

```text
Groups
Expenses
Balances
Settlements
```

### 19.21 Offline SyncOperation

Every local change that must eventually synchronize should create a:

```text
SyncOperation
```

Example:

```text
Expense Created
      ↓
SyncOperation O100
      ↓
Status = PENDING
```

### 19.22 Offline SyncOperation Status

Typical state:

```text
PENDING
```

while there is no available synchronization channel.

Other states may include:

```text
IN_PROGRESS
COMPLETED
FAILED
CONFLICT
REJECTED
```

### 19.23 Offline Pending Queue

Example:

```text
SyncOperations

O100 → PENDING
O101 → PENDING
O102 → PENDING
O103 → PENDING
```

These operations remain persisted locally until successfully synchronized or otherwise resolved.

### 19.24 Offline Queue Persistence

The pending synchronization queue must survive:

```text
Application Restart
Process Death
Device Restart
Temporary Connectivity Loss
```

The queue must not exist only in memory.

### 19.25 Offline Application Restart

Example:

```text
User creates Expense E1
      ↓
SyncOperation O100 = PENDING
      ↓
Application Closed
      ↓
Application Reopened
```

After restart:

```text
Expense E1
```

and:

```text
O100 = PENDING
```

must still exist.

### 19.26 Offline Device Restart

The same guarantee applies after device restart:

```text
Device Restart
      ↓
Open SplitSync
      ↓
Load Room Database
      ↓
Load Pending Operations
      ↓
Continue Offline
```

### 19.27 Offline Queue Ordering

Operations may have dependencies.

Example:

```text
Create Group G1
        ↓
Create Expense E1 in G1
```

The Expense operation cannot be safely synchronized before the Group exists remotely unless the backend protocol explicitly supports dependency resolution.

Therefore the synchronization engine must preserve or determine required operation dependencies.

### 19.28 Offline Operation Dependencies

Example:

```text
O100
Create Group G1

O101
Create Expense E1 → G1
```

Dependency:

```text
O101 depends on O100
```

The Sync Engine must process operations in a safe order.

### 19.29 Offline Membership Dependencies

Similarly:

```text
Create Group
      ↓
Create Membership
      ↓
Create Expense
```

may require dependency ordering during synchronization.

### 19.30 Offline Local Authorization

Authorization is performed locally before a Domain change is committed.

Example:

```text
User U1
    ↓
Active Member of G1
    ↓
Create Expense
    ↓
Permission Check
    ↓
Allowed
```

### 19.31 Offline Unauthorized Operation

If the User is not authorized:

```text
Create Expense
      ↓
Authorization Check
      ↓
DENIED
```

The application must not create:

```text
Expense
```

or:

```text
SyncOperation
```

for the denied operation.

### 19.32 Offline Conflict Detection

Some conflicts can be detected locally.

Example:

```text
Two local changes
```

may affect the same entity.

The application should apply the Conflict Data Model where required.

However, some conflicts can only become known when synchronization occurs.

### 19.33 Offline Concurrent Changes

Example:

```text
Device A
    ↓
Expense E1 edited offline

Device B
    ↓
Same Expense E1 edited offline
```

Neither device may know about the other's change until:

```text
P2P synchronization
```

or:

```text
Backend synchronization
```

occurs.

The system must therefore preserve the operation/version information required for later conflict detection.

### 19.34 Offline Version Tracking

Every mutable Domain entity should maintain the version information required by the synchronization architecture.

Example:

```text
Expense E1
Version = 5
```

Offline edit:

```text
Version = 6
```

The synchronization system can later compare this against remote state.

### 19.35 Offline Operation Metadata

A locally generated SyncOperation should preserve metadata such as:

```text
operationId
entityId
entityType
operationType
groupId
originUserId
originDeviceId
baseVersion
createdAt
```

The exact model is defined in the Synchronization Data Model and Sync Operation Model.

### 19.36 Offline Time

The application should record local timestamps for:

```text
Entity creation
Entity update
Operation creation
```

These timestamps may assist with diagnostics and synchronization but must not automatically be treated as the sole conflict-resolution mechanism.

### 19.37 Offline and Last-Write-Wins

The offline architecture must not assume:

```text
Latest Local Timestamp
    =
Correct Change
```

Concurrent financial changes must follow the defined Conflict Resolution model.

### 19.38 Offline P2P Availability

When Internet is unavailable, the Sync Engine should check whether an authorized peer is available.

Conceptually:

```text
Internet = OFF
      ↓
Peer Available?
   ┌──┴──┐
  Yes    No
   │      │
 P2P    Pending
```

### 19.39 Offline P2P Synchronization

If a peer is available:

```text
Discover Peer
      ↓
Authenticate
      ↓
Authorize Group
      ↓
Exchange Sync State
      ↓
Exchange Operations
      ↓
Apply Valid Changes
      ↓
Update SyncState
```

### 19.40 Offline Without P2P

If no peer is available:

```text
Local Changes
      ↓
Persist
      ↓
SyncOperation = PENDING
```

No error should be shown merely because synchronization cannot currently occur.

### 19.41 Offline Sync Status

The UI may display:

```text
Saved locally
Sync pending
Waiting for connection
```

rather than treating pending synchronization as a failed expense operation.

### 19.42 Offline User Experience

The User should be able to perform core operations normally:

```text
Create Expense
      ↓
Save
      ↓
Continue Using App
```

The application should not unnecessarily show:

```text
Network Error
```

for every local operation.

### 19.43 Offline Error Handling

Errors should distinguish between:

```text
Local Operation Failure
```

and:

```text
Synchronization Unavailability
```

For example:

```text
Expense validation failed
```

is a local operation failure.

While:

```text
Internet unavailable
```

is not a failure to save the Expense locally.

### 19.44 Offline Storage Failure

If Room cannot persist a local operation:

```text
Save Expense
      ↓
Database Failure
```

the application must report the actual local persistence failure.

This is fundamentally different from network unavailability.

### 19.45 Offline Data Integrity

Local data must remain internally consistent.

For an Expense:

```text
Expense
+
ExpenseSplits
+
SyncOperation
```

must satisfy the applicable Domain and database constraints.

### 19.46 Offline Referential Integrity

The local database must enforce required relationships.

Example:

```text
ExpenseSplit
    ↓
Expense
    ↓
Group
```

An ExpenseSplit must not reference a nonexistent Expense.

### 19.47 Offline Group Data

A Group can contain:

```text
Group
Group Members
Expenses
Expense Splits
Settlements
SyncOperations
SyncState
```

All of this data can be used locally without Internet.

### 19.48 Offline Profile Data

The local User profile remains available:

```text
Display Name
Phone
Email
User ID
```

subject to the profile model and privacy rules.

### 19.49 Offline Phone Number

The phone number may be stored locally as a profile attribute.

It does not require Internet connectivity to exist.

However:

```text
Phone Number
```

must not be treated as the primary local identity.

### 19.50 Offline Email

Email is optional.

The User must not be blocked from creating a local profile merely because no email is available.

### 19.51 Offline Group Member Identification

When Users are connected through P2P:

```text
User ID
+
Display Name
```

can be used to identify Group participants.

Additional profile attributes should only be exchanged when required.

### 19.52 Offline Expense History

The complete locally available Group expense history should remain accessible.

The User should be able to:

```text
View
Search
Filter
Calculate
```

using local data without Internet.

### 19.53 Offline Search

Search and filtering of locally stored data must not require network access.

Example:

```text
Search Expenses
      ↓
Room Query
      ↓
Results
```

### 19.54 Offline Sorting

Sorting of local Groups, Expenses, Settlements, and other locally stored records must also work without Internet.

### 19.55 Offline Derived Data

Derived values should be calculated from local source data.

Examples:

```text
Total Group Expense
Individual Contribution
Individual Share
Net Balance
Settlement Effect
```

### 19.56 Offline Balance Recalculation

After a local Expense change:

```text
Expense Updated
      ↓
Recalculate Affected Balance
      ↓
Update UI
```

No backend request is required.

### 19.57 Offline Settlement Effect

After a local Settlement:

```text
Settlement Created
      ↓
Persist
      ↓
Recalculate Balance
      ↓
Display Updated Balance
```

### 19.58 Offline Group Statistics

Group statistics can be calculated locally:

```text
Total Expenses
Total Settlements
Member Contributions
Member Shares
Outstanding Balances
```

### 19.59 Offline Sync With Multiple Peers

A User may synchronize with multiple nearby devices over time:

```text
Device A
   ↕
Device B

Device A
   ↕
Device C

Device A
   ↕
Device D
```

Each peer synchronization state must be tracked appropriately.

### 19.60 Offline Multi-Hop Synchronization

An operation may move through:

```text
Device A
    ↓
Device B
    ↓
Device C
```

The original:

```text
operationId
originUserId
originDeviceId
```

must remain unchanged.

### 19.61 Offline Duplicate Delivery

The same operation may arrive through different paths:

```text
P2P
+
Backend
```

or:

```text
Peer A
+
Peer B
```

The receiving device must use idempotency to prevent duplicate application.

### 19.62 Offline Operation Idempotency

Example:

```text
O100
```

arrives twice.

First:

```text
O100 → APPLIED
```

Second:

```text
O100 → ALREADY_APPLIED
```

No duplicate Expense should be created.

### 19.63 Offline Synchronization Failure

If P2P synchronization fails:

```text
P2P Failure
      ↓
Preserve Local Data
      ↓
Keep SyncOperation Pending
      ↓
Retry Later
```

A failed synchronization must not roll back successfully committed local changes.

### 19.64 Offline P2P Connection Loss

Example:

```text
10 operations
```

are being synchronized.

Connection fails after:

```text
6 operations
```

The six successfully committed operations remain.

The remaining operations stay retryable.

### 19.65 Offline to Online Transition

When Internet becomes available:

```text
Offline
      ↓
Connectivity Detected
      ↓
Backend Available
      ↓
Authenticate if Required
      ↓
Process Pending SyncOperations
      ↓
Pull Remote Changes
      ↓
Apply / Resolve Conflicts
      ↓
Update SyncState
```

### 19.66 Offline to P2P Transition

When a peer becomes available:

```text
Offline
      ↓
Peer Discovered
      ↓
Authenticate
      ↓
Authorize
      ↓
Synchronize
```

The local application remains usable throughout the process.

### 19.67 Offline to Online and P2P

Both channels may become available:

```text
Internet = ON
P2P = ON
```

The Sync Engine may use:

```text
Backend
+
P2P
```

while maintaining the same:

```text
Operation ID
Conflict Model
Authorization Model
Sync State
```

### 19.68 Offline Data and Backend Association

A User may have significant local data before backend registration:

```text
Local User
    ↓
Groups
    ↓
Expenses
    ↓
Settlements
```

Later:

```text
Internet Available
    ↓
Backend Association
```

The local data must be preserved.

The account-linking flow must determine how existing local data is associated with the backend.

### 19.69 Offline Security

Offline functionality must still enforce:

```text
Local Authentication / App Protection
Local Authorization
Data Integrity
Secure Local Storage
```

where applicable.

Offline must not mean:

```text
No Security
```

### 19.70 Offline P2P Security

P2P synchronization must still require:

```text
Peer Authentication
Group Authorization
Message Integrity
Secure Communication
```

Internet connectivity is not a prerequisite for these protections.

### 19.71 Offline Sensitive Data

The application should minimize unnecessary exposure of:

```text
Phone Numbers
Email Addresses
Financial Information
```

especially during local peer discovery.

### 19.72 Offline Application Restart During Pending Sync

Example:

```text
Expense E1 created
      ↓
O100 = PENDING
      ↓
Device Restart
      ↓
Application Launch
```

After restart:

```text
E1 exists
O100 exists
O100 = PENDING
```

Synchronization can resume when a suitable channel becomes available.

### 19.73 Offline Crash During Write

If the application crashes during a local transaction:

```text
BEGIN TRANSACTION
      ↓
Crash
```

Room/SQLite transaction semantics must ensure the transaction is either:

```text
Committed
```

or:

```text
Rolled Back
```

There must not be a partially committed Domain state.

### 19.74 Offline Crash During Synchronization

If the application crashes while synchronizing:

```text
Sync in progress
      ↓
Crash
```

already committed operations remain committed.

Uncommitted operations are retried safely.

### 19.75 Offline Sync Recovery

After restart:

```text
Load SyncOperations
      ↓
Find PENDING / IN_PROGRESS
      ↓
Normalize Recoverable State
      ↓
Retry When Channel Available
```

An `IN_PROGRESS` state that cannot be proven complete should be safely recoverable through idempotent retry.

### 19.76 Offline Sync State Recovery

The SyncState must also survive application restart.

Example:

```text
Group G1
Peer D2
Last Successful Operation = O500
```

After restart:

```text
Same SyncState
```

must be available.

### 19.77 Offline Conflict Persistence

If a conflict is detected through P2P:

```text
Conflict
    ↓
Persist Conflict Data
```

The conflict must survive:

```text
Application Restart
Device Restart
Connectivity Loss
```

### 19.78 Offline Conflict Resolution

A User may resolve supported conflicts locally when the Conflict Data Model permits.

The resolution itself becomes a Domain/Synchronization operation where required.

### 19.79 Offline and Eventual Consistency

The system accepts that devices may temporarily have different states:

```text
Device A ≠ Device B
```

while offline.

The target is:

```text
After Successful Synchronization
        ↓
Device A = Device B
```

for the authorized synchronized scope, subject to resolved conflicts and valid authorization.

### 19.80 Offline Consistency Model

The application follows:

```text
Strong Local Consistency
+
Eventual Cross-Device Consistency
```

Meaning:

```text
Local Database
    ↓
Immediately Consistent

Other Devices
    ↓
Eventually Consistent
```

### 19.81 Offline Availability

Core application operations should remain available when:

```text
Internet = OFF
```

and:

```text
P2P = OFF
```

This is one of the primary V1 requirements.

### 19.82 Offline Performance

Local operations should not wait for network timeouts.

Example:

```text
Create Expense
      ↓
Local Validation
      ↓
Room Transaction
      ↓
UI Update
```

The User should receive immediate local feedback.

### 19.83 Offline Network Independence

The local Domain and Room layers must not directly depend on:

```text
HTTP
REST
Wi-Fi
Hotspot
Internet
```

The Sync Engine handles communication separately.

### 19.84 Offline Architecture Boundary

Conceptually:

```text
UI
 ↓
Domain
 ↓
Local Repository
 ↓
Room
```

and independently:

```text
Sync Engine
 ├── Backend Channel
 └── P2P Channel
```

The local Domain must remain operational even if both synchronization channels are unavailable.

### 19.85 Offline Flow — Create Expense

```text
User
  ↓
Create Expense
  ↓
Local Authorization
  ↓
Domain Validation
  ↓
Room Transaction
  ├── Expense
  ├── Expense Splits
  └── SyncOperation
  ↓
COMMIT
  ↓
Update Balance
  ↓
Display Expense
  ↓
Wait for Synchronization
```

### 19.86 Offline Flow — Create Settlement

```text
User
  ↓
Create Settlement
  ↓
Local Authorization
  ↓
Validate Participants
  ↓
Validate Amount
  ↓
Room Transaction
  ├── Settlement
  └── SyncOperation
  ↓
COMMIT
  ↓
Recalculate Balance
  ↓
Display Updated State
```

### 19.87 Offline Flow — P2P Sync

```text
Local Pending Operations
        ↓
Peer Discovered
        ↓
Peer Authentication
        ↓
Group Authorization
        ↓
Sync State Exchange
        ↓
Operation Exchange
        ↓
Validation
        ↓
Conflict Detection
        ↓
Apply Valid Operations
        ↓
Update SyncState
        ↓
Mark Operations Completed
```

### 19.88 Offline Flow — No Peer

```text
Local Change
      ↓
Room Transaction
      ↓
SyncOperation = PENDING
      ↓
No Peer
      ↓
No Internet
      ↓
Continue Local Usage
```

### 19.89 Offline Flow — Internet Returns

```text
Pending SyncOperations
        ↓
Internet Detected
        ↓
Backend Authentication
        ↓
Push Pending Operations
        ↓
Process Results
        ↓
Pull Remote Changes
        ↓
Apply Changes
        ↓
Resolve Conflicts
        ↓
Update SyncState
        ↓
Continue Normal Operation
```

### 19.90 Offline Flow Summary

```text
Offline Flow
│
├── Local Identity
│   ├── User
│   └── Device
│
├── Local Data
│   ├── Groups
│   ├── Members
│   ├── Expenses
│   ├── Expense Splits
│   └── Settlements
│
├── Local Processing
│   ├── Validation
│   ├── Authorization
│   ├── Balance Calculation
│   └── Domain Rules
│
├── Local Persistence
│   ├── Room
│   ├── Transactions
│   └── Durable Storage
│
├── Synchronization Queue
│   ├── SyncOperations
│   ├── SyncState
│   └── Conflicts
│
├── Connectivity Options
│   ├── No Connectivity
│   ├── P2P
│   └── Backend
│
└── Recovery
    ├── Application Restart
    ├── Device Restart
    ├── Connection Loss
    ├── Internet Return
    └── Peer Availability
```

### 19.91 Offline Flow Invariants

The following rules are mandatory for V1:

- Core expense-management functionality must work without Internet connectivity.
- Core expense-management functionality must also work when no P2P peer is available.
- The User must not manually switch to Offline Mode.
- Local identity must remain available without Internet.
- One local User must remain associated with the Device.
- Local Groups must remain accessible without Internet.
- Local Expenses must remain accessible without Internet.
- Local Expense Splits must remain accessible without Internet.
- Local Settlements must remain accessible without Internet.
- Local balances must be calculated without Internet.
- Local search and filtering must work without Internet.
- Local authorization must work without Internet.
- Local Domain validation must work without Internet.
- Local changes must be persisted in Room before synchronization is attempted.
- Domain changes and their corresponding SyncOperations must be created atomically.
- Pending SyncOperations must be durable.
- Pending SyncOperations must survive application restart.
- Pending SyncOperations must survive device restart.
- Pending SyncOperations must survive temporary connectivity loss.
- Local data must not be deleted because synchronization is unavailable.
- Network failure must not be treated as local persistence failure.
- P2P synchronization must remain optional.
- Backend synchronization must remain optional while offline.
- P2P synchronization must not require Internet connectivity.
- Backend synchronization must resume automatically when Internet becomes available.
- P2P synchronization may resume when an authorized peer becomes available.
- Operation IDs must remain unchanged during later synchronization.
- Origin User and Device identities must remain unchanged during synchronization.
- Duplicate operations must never create duplicate financial records.
- Operation dependencies must be respected during synchronization.
- Local transactions must preserve referential and Domain integrity.
- Application crashes must not leave partially committed Domain transactions.
- Synchronization crashes must be recoverable through persisted state and idempotent retry.
- Offline edits must preserve version information required for later conflict detection.
- Offline concurrent changes must not be silently resolved using arbitrary last-write-wins.
- Conflicts must use the defined Conflict Data Model.
- Conflict state must survive application and device restart.
- Offline authorization failures must not create Domain changes or SyncOperations.
- Offline functionality must not expose sensitive profile information unnecessarily.
- Phone number and email must not be treated as unrestricted peer-discovery data.
- Local financial data must remain available even when backend authentication fails.
- Online and offline execution must use the same Domain model.
- Online and offline execution must use the same local persistence model.
- Synchronization channels must remain separate from the core local Domain logic.
- The system must provide strong local consistency.
- The system must provide eventual cross-device consistency after successful synchronization.
- Transition from Offline to Online must not require application restart.
- Transition from Offline to P2P synchronization must not require application restart.
- The application must continue normal local operation while synchronization is running.
- Synchronization availability must improve connectivity and backup capabilities without becoming a prerequisite for local expense management.

## 20. Peer-to-Peer Offline Flow

### 20.1 Purpose

This section defines the complete Peer-to-Peer (P2P) offline flow for SplitSync V1.

The purpose of P2P synchronization is to allow authorized nearby SplitSync users to exchange required application data without Internet connectivity.

The primary scenario is:

```text
Internet = OFF
        ↓
Users are physically nearby
        ↓
Devices connect through Wi-Fi / Hotspot / supported local transport
        ↓
SplitSync discovers nearby peers
        ↓
Peers authenticate each other
        ↓
Group authorization is verified
        ↓
Synchronization takes place
        ↓
Local databases converge
```

P2P is an optional synchronization channel.

The application must remain fully functional even when:

```text
Internet = OFF
P2P = unavailable
```

### 20.2 Core Principle

The P2P architecture follows:

```text
Local First
    +
Peer Synchronization
    +
Backend Synchronization
```

P2P does not replace the local-first architecture.

It provides a way for nearby devices to synchronize while the backend is unavailable.

### 20.3 Primary P2P Scenario

Example:

```text
User A
    ↓
Creates Group G1

User B
    ↓
Needs to join G1

Internet = OFF
```

Users can connect locally:

```text
Device A
    ⇅
Local Network / Hotspot
    ⇅
Device B
```

and exchange the required Group information.

### 20.4 P2P Offline Flow Overview

```text
Local Changes
      ↓
Pending SyncOperations
      ↓
Peer Discovery
      ↓
Peer Selection
      ↓
Connection Establishment
      ↓
Peer Authentication
      ↓
Group Authorization
      ↓
Synchronization Handshake
      ↓
Sync State Exchange
      ↓
Operation Exchange
      ↓
Operation Validation
      ↓
Conflict Detection
      ↓
Apply Valid Changes
      ↓
Update SyncState
      ↓
Mark Operations Completed
      ↓
Disconnect
```

### 20.5 P2P Connectivity

P2P communication may operate through supported Android local connectivity mechanisms such as:

```text
Wi-Fi
Wi-Fi Hotspot
Wi-Fi Direct
```

The exact transport implementation will be selected during Android implementation.

The Domain and Sync Engine must not depend directly on a specific transport.

### 20.6 Internet Is Not Required

The P2P flow must work when:

```text
Local Network = Available
Internet = Unavailable
```

For example:

```text
Device A
    ↓
Creates Hotspot

Device B
    ↓
Connects to Hotspot

Internet
    ↓
OFF
```

The devices can still establish a local synchronization session.

### 20.7 Local Network Without Internet

The application must distinguish:

```text
Local Connectivity
```

from:

```text
Internet Connectivity
```

Therefore:

```text
Local Network = YES
Internet = NO
```

is a valid synchronization environment.

### 20.8 P2P Without Internet and Without Backend

During this flow:

```text
Backend = unavailable
Internet = unavailable
```

the synchronization authority is temporarily:

```text
Peer-to-Peer
```

while each device remains locally authoritative for its own committed local data.

### 20.9 Device A Offline State

Example:

```text
Device A

Group G1
Expense E1
Expense E2

SyncOperations:
O100 = PENDING
O101 = PENDING
```

### 20.10 Device B Offline State

Example:

```text
Device B

Group G1
Expense E3

SyncOperations:
O200 = PENDING
```

Both devices have valid local changes.

### 20.11 P2P Discovery

The first step is:

```text
Start Discovery
      ↓
Find Nearby SplitSync Devices
```

Example:

```text
Nearby SplitSync Users

Praveen
Rahul
Amit
```

Discovery only identifies nearby devices.

It does not authorize access.

### 20.12 Discovery Information

Only minimum required information should be exposed during discovery.

Potential information:

```text
deviceId / discovery identifier
displayName
protocolVersion
capabilities
```

Sensitive information should not be unnecessarily broadcast.

### 20.13 Phone Number During Discovery

Phone number should not be broadcast as the primary discovery identifier.

The discovery identity should be based on:

```text
Device Identity
+
User Identity / Display Identity
```

Phone number may be exchanged later only if required by an authorized feature.

### 20.14 Peer Selection

After discovery:

```text
Nearby Users
      ↓
User Selects Peer
      ↓
Connection Request
```

The User should be able to identify the intended person before synchronization begins.

### 20.15 Connection Request

Example:

```text
Device A
    ↓
Request Connection
    ↓
Device B
```

Device B may:

```text
Accept
```

or:

```text
Reject
```

the connection.

### 20.16 No Automatic Trust

The application must never assume:

```text
Same Wi-Fi
    =
Trusted User
```

or:

```text
Same Hotspot
    =
Authorized Group Member
```

Authentication and authorization remain mandatory.

### 20.17 P2P Authentication

After connection:

```text
Connection Established
        ↓
Peer Authentication
```

The devices establish confidence in:

```text
Who is the peer?
```

### 20.18 P2P Identity

The peer identity is based on:

```text
User ID
+
Device ID
```

The exact cryptographic identity mechanism is defined by the Security Architecture.

### 20.19 P2P Authorization

After authentication:

```text
Who is this?
        ↓
What is this peer allowed to access?
```

The receiving device must verify:

```text
User
Device
Group Membership
Requested Scope
```

### 20.20 Group Scope Determination

Suppose:

```text
Device A:
G1
G2
G3

Device B:
G2
G3
G4
```

The candidate shared scope is:

```text
G2
G3
```

Only authorized Groups should actually be synchronized.

### 20.21 P2P Synchronization Handshake

After authentication:

```text
HELLO
      ↓
HELLO_RESPONSE
      ↓
Capability Exchange
      ↓
Group Scope Exchange
      ↓
Sync State Exchange
      ↓
Operation Exchange
```

### 20.22 Protocol Version

The handshake must include:

```text
protocolVersion
```

This allows devices to determine compatibility.

Example:

```text
Device A → Protocol 1
Device B → Protocol 1
```

Compatible.

### 20.23 Capability Exchange

Devices may exchange supported capabilities.

Example:

```text
Device A:
Expense
ExpenseSplit
Settlement

Device B:
Expense
ExpenseSplit
Settlement
GroupInvitation
```

Only mutually supported functionality should be used during the session.

### 20.24 Group Authorization Check

Before exchanging Group data:

```text
Peer User
    ↓
Authenticated
    ↓
Group Membership
    ↓
Active?
    ↓
Permission Valid?
```

If authorization fails:

```text
Synchronization for that Group = DENIED
```

### 20.25 Group Invitation Through P2P

A Group invitation may be exchanged offline.

Example:

```text
User A
    ↓
Creates Group G1
    ↓
Invite User B
    ↓
P2P Connection
    ↓
Invitation Sent
```

User B must explicitly accept the invitation.

### 20.26 Invitation Is Not Membership

Receiving an invitation does not automatically create active membership.

The flow is:

```text
Invitation Received
      ↓
Validate Invitation
      ↓
Show User
      ↓
User Accepts
      ↓
Create Membership
```

### 20.27 Offline Membership Creation

After acceptance:

```text
User B
    ↓
Membership Created Locally
    ↓
Role Assigned
    ↓
Group State Updated
    ↓
SyncOperation Created
```

The membership change can later synchronize with other devices/backend.

### 20.28 Group Data Exchange

Once authorization is established:

```text
Group Metadata
+
Authorized Membership Data
+
Required Expense Data
+
Expense Splits
+
Settlements
+
Relevant SyncOperations
```

may be exchanged according to the synchronization protocol.

### 20.29 Operation Exchange

P2P synchronization is based on:

```text
SyncOperation
```

rather than directly copying arbitrary database rows.

Conceptually:

```text
Device A
    ↓
Operations required by Device B
    ↓
Device B
```

and:

```text
Device B
    ↓
Operations required by Device A
    ↓
Device A
```

### 20.30 Bidirectional P2P Synchronization

P2P synchronization must support:

```text
Device A ⇄ Device B
```

Both devices may have locally created changes.

### 20.31 Example — Bidirectional Sync

Before synchronization:

```text
Device A:
E1
E2

Device B:
E3
E4
```

After successful synchronization:

```text
Device A:
E1
E2
E3
E4

Device B:
E1
E2
E3
E4
```

assuming all operations are authorized and valid.

### 20.32 Sync State Exchange

Before sending large amounts of data:

```text
Device A
    ↓
Current SyncState
```

and:

```text
Device B
    ↓
Current SyncState
```

are exchanged for the relevant scope.

This allows the devices to determine what still needs to be synchronized.

### 20.33 Incremental P2P Synchronization

The system should avoid exchanging the entire Group history on every connection.

Instead:

```text
Previous Sync State
        ↓
Determine Missing Operations
        ↓
Exchange Only Required Changes
```

### 20.34 P2P Operation Filtering

Before sending operations, the Sync Engine must filter by:

```text
Group
Authorization
Operation State
Peer State
Protocol Compatibility
```

The device must not send its entire local database.

### 20.35 Operation Identity

When an operation is transmitted:

```text
Operation ID
```

must remain unchanged.

Example:

```text
O100
```

sent from A to B remains:

```text
O100
```

on B.

### 20.36 Origin Identity

The original:

```text
originUserId
originDeviceId
```

must remain unchanged.

Example:

```text
Created on Device A
```

must remain:

```text
originDeviceId = A
```

even after reaching Device B.

### 20.37 Multi-Hop Synchronization

Operations may travel through multiple devices:

```text
Device A
    ↓
Device B
    ↓
Device C
```

The original operation identity must remain unchanged.

This allows eventual convergence even when all devices cannot connect directly.

### 20.38 P2P Operation Validation

Every received operation must be validated before application:

```text
Receive Operation
      ↓
Verify Integrity
      ↓
Verify Origin
      ↓
Verify Authorization
      ↓
Validate Domain Rules
      ↓
Validate Dependencies
      ↓
Validate Version
      ↓
Apply
```

### 20.39 P2P Expense Synchronization

For an Expense:

```text
Receive Expense Operation
      ↓
Validate Group
      ↓
Validate Origin User
      ↓
Validate Membership
      ↓
Validate Expense
      ↓
Validate Expense Splits
      ↓
Check Duplicate
      ↓
Check Version / Conflict
      ↓
Apply Transaction
```

### 20.40 P2P Settlement Synchronization

For a Settlement:

```text
Receive Settlement Operation
      ↓
Validate Group
      ↓
Validate Participants
      ↓
Validate Authorization
      ↓
Validate Amount
      ↓
Check Duplicate
      ↓
Check Conflict
      ↓
Apply Transaction
```

### 20.41 P2P Group Synchronization

For a Group:

```text
Receive Group Operation
      ↓
Validate Group Identity
      ↓
Validate Origin
      ↓
Validate Authorization
      ↓
Check Existing Group
      ↓
Apply / Merge / Conflict
```

### 20.42 P2P Membership Synchronization

Membership operations require additional authorization:

```text
Receive Membership Operation
      ↓
Verify Origin
      ↓
Verify Origin Role
      ↓
Verify Target User
      ↓
Validate Membership Rule
      ↓
Apply
```

### 20.43 P2P Profile Synchronization

Profile information should be synchronized only when required.

Example:

```text
Display Name
```

may be shared for Group identification.

Sensitive information such as:

```text
Phone Number
Email
```

must follow explicit privacy and authorization rules.

### 20.44 P2P Balance Synchronization

Balances should not be treated as authoritative synchronization records.

Instead:

```text
Expenses
+
ExpenseSplits
+
Settlements
        ↓
Local Balance Calculation
```

Each device calculates its own balances from synchronized source data.

### 20.45 P2P Transaction Boundary

Received changes should be committed transactionally.

Example:

```text
BEGIN TRANSACTION

Validate
    ↓
Apply Domain Change
    ↓
Update SyncOperation State
    ↓
Update SyncState
    ↓
COMMIT
```

If the transaction fails:

```text
ROLLBACK
```

### 20.46 Partial Batch Failure

Suppose:

```text
10 operations
```

are received.

If:

```text
6 operations
```

are successfully committed and:

```text
4 operations
```

fail:

```text
6 = successfully applied
4 = retryable / rejected / conflict
```

The successful operations must not be rolled back merely because the remaining operations failed.

### 20.47 P2P Acknowledgement

The receiving device should return results such as:

```text
O100 → APPLIED
O101 → ALREADY_APPLIED
O102 → CONFLICT
O103 → REJECTED
```

The sending device updates its local synchronization state accordingly.

### 20.48 Duplicate Operation

If:

```text
O100
```

is received twice:

First:

```text
O100 → APPLIED
```

Second:

```text
O100 → ALREADY_APPLIED
```

The second delivery must not create another Domain entity.

### 20.49 P2P Conflict

Example:

```text
Expense E1
Version 5
```

Device A:

```text
Version 6
Amount ₹1200
```

Device B:

```text
Version 6
Amount ₹1500
```

When they synchronize:

```text
Concurrent Update
      ↓
Conflict
```

The Conflict Data Model is used.

### 20.50 No Arbitrary Last-Write-Wins

P2P must not automatically use:

```text
Last Device Wins
```

or:

```text
Last Message Wins
```

for financial data.

Conflict handling must follow the defined Conflict Resolution rules.

### 20.51 P2P Connection Loss

A P2P connection can fail because of:

```text
User moves away
Wi-Fi disabled
Hotspot disabled
Device sleeps
Application closed
Android background restriction
Network interference
```

The application must handle this safely.

### 20.52 Connection Loss Behavior

When the connection is lost:

```text
P2P Session
      ↓
DISCONNECTED
```

The application must:

- Preserve successfully committed changes.
- Preserve pending operations.
- Preserve SyncState.
- Preserve conflict information.
- Allow future synchronization.
- Avoid duplicate application during retry.

### 20.53 P2P Session Recovery

After reconnection:

```text
Reconnect
      ↓
Authenticate
      ↓
Exchange SyncState
      ↓
Determine Missing Operations
      ↓
Continue Synchronization
```

The application should not unnecessarily restart synchronization from the beginning.

### 20.54 Interrupted Synchronization

Example:

```text
O100
O101
O102
O103
O104
```

are being exchanged.

Connection fails after:

```text
O100
O101
O102
```

are committed.

After reconnect:

```text
O103
O104
```

should be retried.

Already committed operations should be safely recognized as already applied.

### 20.55 P2P Sync State

Peer synchronization state must be persisted.

Conceptually:

```text
Local Device:
D1

Peer:
D2

Group:
G1

Channel:
PEER

Status:
SYNCED
```

### 20.56 Peer-Specific State

Different peers may have different synchronization progress.

Example:

```text
D1 ↔ D2
G1 → O500

D1 ↔ D3
G1 → O450
```

These states must not be treated as one global cursor.

### 20.57 P2P Sync Completion

A P2P synchronization cycle is complete when:

```text
All eligible local operations exchanged
        +
All required peer operations processed
        +
No blocking synchronization state remains
```

The session can then move toward:

```text
DISCONNECTED
```

### 20.58 Offline P2P and Local Availability

Synchronization must not block normal local usage.

While:

```text
P2P Synchronization = running
```

the User should still be able to create valid local expenses.

These new changes become additional:

```text
SyncOperations
```

and can be synchronized during the current or next cycle.

### 20.59 New Local Change During P2P

Example:

```text
P2P Synchronization Running
        ↓
User Creates Expense E10
        ↓
Local Transaction
        ↓
SyncOperation O300 = PENDING
```

The application may:

```text
Synchronize O300
```

during the current session or later.

It must not block the User from creating the Expense.

### 20.60 P2P and Local Authorization

Every local operation remains subject to:

```text
Local Authentication
+
Group Membership
+
Role / Permission
+
Domain Validation
```

P2P availability does not change local authorization rules.

### 20.61 P2P and Remote Authorization

Every received operation remains subject to:

```text
Peer Authentication
+
Origin Validation
+
Group Authorization
+
Domain Validation
```

A trusted peer connection does not automatically authorize every operation.

### 20.62 P2P Security Boundary

The P2P flow must protect against:

```text
Unknown Devices
Unauthorized Users
Unauthorized Groups
Operation Tampering
Replay
Impersonation
Duplicate Operations
Data Interception
```

The cryptographic implementation belongs to the Security Architecture.

### 20.63 P2P Secure Communication

Once peers authenticate:

```text
Authenticated Peer
      ↓
Secure Session
      ↓
Synchronization
```

Sensitive synchronization data should not be transmitted without appropriate confidentiality and integrity protection.

### 20.64 P2P Replay Protection

An attacker must not be able to repeatedly replay:

```text
O100
```

to create duplicate financial records.

Application-level:

```text
Operation ID
```

provides idempotency.

The secure communication layer must additionally provide appropriate replay protection.

### 20.65 P2P Message Integrity

Received messages must be validated for integrity.

Conceptually:

```text
Message
    ↓
Integrity Verification
    ↓
Valid?
   ┌──┴──┐
  Yes    No
   │      │
Process  Reject
```

### 20.66 P2P Data Minimization

Only the required authorized data should be exchanged.

For example:

```text
Peer authorized for G1
```

should not automatically receive:

```text
G2
G3
G4
```

### 20.67 P2P Profile Privacy

Discovery should expose only the minimum information required for identifying a nearby User.

Potentially visible:

```text
Display Name
```

Potentially restricted:

```text
Phone
Email
Financial Information
```

### 20.68 P2P Hotspot Scenario

A common V1 flow:

```text
Device A
    ↓
Hotspot ON

Device B
    ↓
Connects to Hotspot

Device C
    ↓
Connects to Hotspot
```

SplitSync devices discover one another.

Then:

```text
Authentication
      ↓
Authorization
      ↓
Synchronization
```

### 20.69 Hotspot Without Internet

The hotspot may have:

```text
Internet = OFF
```

and still provide:

```text
Local Network = ON
```

P2P synchronization must continue to work in this scenario.

### 20.70 Multiple Peers

A device may synchronize with several peers sequentially:

```text
Device A
   ↕
Device B

Device A
   ↕
Device C

Device A
   ↕
Device D
```

The Sync Engine must preserve operation identity and synchronization state across all sessions.

### 20.71 Multi-Hop Example

```text
Device A
    ↓
Creates E1

Device B
    ↓
Synchronizes with A
    ↓
Receives E1

Device C
    ↓
Synchronizes with B
    ↓
Receives E1
```

The operation remains:

```text
operationId = O100
originDeviceId = A
```

### 20.72 P2P Then Backend

Example:

```text
Device A
    ↓
Creates E1
    ↓
P2P → Device B
    ↓
Device B receives E1
```

Later:

```text
Internet Available
    ↓
Device A → Backend
    ↓
Backend receives E1
```

If Device B later receives E1 from the Backend:

```text
O100 already exists
    ↓
ALREADY_APPLIED
```

No duplicate Expense is created.

### 20.73 Backend Then P2P

The reverse is also possible:

```text
Device A
    ↓
Backend
    ↓
Device B

Later:

Device B
    ↓
P2P
    ↓
Device C
```

The same operation identity is preserved.

### 20.74 P2P as a Synchronization Channel

The architecture treats P2P as:

```text
Sync Channel
```

alongside:

```text
Backend Channel
```

Conceptually:

```text
                Sync Engine
                    │
          ┌─────────┴─────────┐
          │                   │
     Backend Channel      P2P Channel
          │                   │
       Internet          Local Network
```

### 20.75 Transport Independence

The P2P flow must not directly depend on:

```text
Wi-Fi implementation
Hotspot implementation
Wi-Fi Direct implementation
```

Instead:

```text
P2P Transport
      ↓
P2P Protocol
      ↓
Sync Engine
```

### 20.76 P2P and Sync Engine

The Sync Engine remains responsible for:

```text
Operation Selection
Operation Ordering
Validation
Conflict Detection
Idempotency
SyncState
```

The P2P layer is responsible for:

```text
Discovery
Connection
Transport
Message Exchange
Session Management
```

### 20.77 P2P Session Lifecycle

```text
DISCOVERING
      ↓
CONNECTING
      ↓
CONNECTED
      ↓
AUTHENTICATING
      ↓
SYNCHRONIZING
      ↓
DISCONNECTING
      ↓
DISCONNECTED
```

Failure:

```text
Any State
    ↓
FAILED
```

### 20.78 P2P Session Persistence

The active communication session itself does not need to survive an application restart.

However, the important synchronization state must survive:

```text
SyncOperation
SyncState
Conflict
Domain Data
```

### 20.79 P2P Failure Recovery

Example:

```text
P2P Failed
      ↓
Local Data Safe
      ↓
SyncOperations Remain Pending
      ↓
Retry Later
```

The application remains fully usable.

### 20.80 P2P Offline Flow — Complete Example

```text
User A creates Expense E1
        ↓
Room Database
        ↓
SyncOperation O100 = PENDING
        ↓
Internet = OFF
        ↓
User B becomes nearby
        ↓
Device Discovery
        ↓
User A selects User B
        ↓
Connection Request
        ↓
Peer Authentication
        ↓
Group Authorization
        ↓
Sync Handshake
        ↓
Sync State Exchange
        ↓
A sends O100
        ↓
B validates O100
        ↓
B applies E1
        ↓
B returns APPLIED
        ↓
A updates O100
        ↓
B sends its pending operations
        ↓
A validates and applies them
        ↓
Both devices update SyncState
        ↓
Synchronization Complete
        ↓
Disconnect
```

### 20.81 P2P Offline Flow — Group Invitation

```text
User A
    ↓
Creates Group G1
    ↓
Creates Invite for User B
    ↓
P2P Connection
    ↓
Send Invitation
    ↓
User B Validates Invitation
    ↓
User B Accepts
    ↓
Create Local Membership
    ↓
Create SyncOperation
    ↓
Exchange Required Group Data
```

### 20.82 P2P Offline Flow — Multiple Expenses

Before synchronization:

```text
Device A:
E1
E2
E3

Device B:
E4
E5
```

After synchronization:

```text
Device A:
E1
E2
E3
E4
E5

Device B:
E1
E2
E3
E4
E5
```

assuming all operations are valid and authorized.

### 20.83 P2P Offline Flow — Connection Failure

```text
10 Operations
      ↓
6 Applied
      ↓
Connection Lost
      ↓
Local Transactions Preserved
      ↓
4 Operations Remain Pending
      ↓
Reconnect Later
      ↓
Continue From Current Sync State
```

### 20.84 P2P Offline Flow — No Peer

```text
User Creates Expense
      ↓
Local Transaction
      ↓
SyncOperation = PENDING
      ↓
No Peer Found
      ↓
No Internet
      ↓
Continue Using Application
```

### 20.85 P2P Offline Flow — Internet Returns

```text
P2P Synchronization May Have Completed
        ↓
Internet Becomes Available
        ↓
Backend Authentication
        ↓
Backend Synchronization
        ↓
Already Applied Operations Detected
        ↓
No Duplicate Data
        ↓
Remaining Changes Synchronized
```

### 20.86 P2P Offline Consistency Model

P2P synchronization follows:

```text
Strong Local Consistency
+
Eventual Peer Consistency
```

Immediately after a local change:

```text
Local Device
    ↓
Consistent
```

Before synchronization:

```text
Peer Device
    ↓
May be Different
```

After successful synchronization:

```text
Authorized Shared Scope
    ↓
Converges
```

### 20.87 P2P Offline Availability

P2P must not reduce local availability.

Even when:

```text
P2P = unavailable
```

the User can continue:

```text
Create Expense
Edit Expense
Create Settlement
View Balance
```

### 20.88 P2P Offline Performance

P2P synchronization should not block normal local operations.

Local operation:

```text
Create Expense
      ↓
Room Transaction
      ↓
UI Update
```

should remain independent of:

```text
P2P Network
```

### 20.89 P2P Offline Data Integrity

The receiving device must never apply partially validated financial data.

The required sequence is:

```text
Receive
    ↓
Verify
    ↓
Validate
    ↓
Authorize
    ↓
Apply Transaction
```

### 20.90 P2P Offline Synchronization Boundary

The architectural boundary is:

```text
┌──────────────────────────────┐
│        Local Domain          │
│                              │
│ Groups                       │
│ Expenses                     │
│ Expense Splits               │
│ Settlements                  │
└──────────────┬───────────────┘
               │
               ▼
        ┌──────────────┐
        │ Sync Engine  │
        └──────┬───────┘
               │
               ▼
        ┌──────────────┐
        │ P2P Channel  │
        └──────┬───────┘
               │
               ▼
          Remote Peer
```

The P2P layer must not directly manipulate UI state.

### 20.91 P2P Offline Flow Summary

```text
P2P Offline Flow
│
├── Connectivity
│   ├── Local Wi-Fi
│   ├── Hotspot
│   └── Supported Local Transport
│
├── Discovery
│   └── Find Nearby SplitSync Devices
│
├── Connection
│   └── Establish Peer Session
│
├── Authentication
│   └── Verify Peer Identity
│
├── Authorization
│   └── Verify Group Access
│
├── Handshake
│   ├── Protocol Version
│   ├── Capabilities
│   └── Group Scope
│
├── Synchronization
│   ├── Sync State
│   ├── Pending Operations
│   ├── Operation Validation
│   └── Bidirectional Exchange
│
├── Conflict Handling
│   └── Conflict Data Model
│
├── Persistence
│   ├── Room
│   ├── SyncOperation
│   └── SyncState
│
└── Recovery
    ├── Connection Loss
    ├── Retry
    ├── Duplicate Delivery
    └── Internet Return
```

### 20.92 P2P Offline Flow Invariants

The following rules are mandatory for V1:

- P2P synchronization must work without Internet connectivity.
- P2P synchronization is an additional synchronization channel.
- Core application functionality must remain available even when no P2P peer is available.
- The User must not manually switch to a special P2P Offline Mode.
- Local data remains available throughout the P2P synchronization process.
- Local changes must always be persisted before synchronization.
- P2P synchronization must operate on SyncOperations rather than arbitrary database copying.
- P2P discovery must be separate from authentication.
- P2P authentication must be separate from authorization.
- Being connected to the same Wi-Fi or Hotspot does not imply trust.
- A discovered peer must not automatically receive Group data.
- Only authenticated and authorized peers may synchronize Group data.
- Group synchronization must be limited to authorized Groups.
- Sensitive profile information must not be unnecessarily broadcast.
- Phone number must not be used as the primary discovery identity.
- User ID and Device ID must remain distinct.
- Operation IDs must remain unchanged during P2P synchronization.
- Origin User ID and Origin Device ID must remain unchanged.
- Operations must remain valid when forwarded through multiple peers.
- Received operations must be validated before being applied.
- Received operations must be authorized before being applied.
- Received operations must be applied transactionally.
- Duplicate operations must never create duplicate financial records.
- P2P synchronization must be bidirectional.
- Peer-specific SyncState must be maintained independently.
- P2P synchronization must support incremental synchronization.
- P2P synchronization must not require full database exchange on every connection.
- Operation dependencies must be respected.
- Connection failure must not roll back successful local transactions.
- Pending operations must survive connection loss.
- P2P synchronization must be resumable where practical.
- P2P synchronization must use the same Conflict Data Model as Backend synchronization.
- P2P must not introduce arbitrary last-write-wins behavior for financial data.
- Balances must be calculated from synchronized source data rather than treated as authoritative transferred values.
- P2P synchronization must not block normal local application usage.
- New local changes created during P2P synchronization must be persisted normally.
- P2P synchronization must remain independent of the selected Android transport implementation.
- P2P transport must not directly modify Domain entities.
- Internet connectivity must not be required for P2P authentication or synchronization.
- P2P synchronization may coexist with Backend synchronization when Internet becomes available.
- The same Operation ID must remain idempotent across P2P and Backend channels.
- Data received through P2P must not be duplicated when later received through the Backend.
- P2P synchronization must support eventual convergence among authorized devices.
- Local consistency must be maintained even when peer synchronization is unavailable.
- Security protections must remain active during offline P2P communication.
- P2P synchronization must fail safely without deleting or corrupting local financial data.

## 21. Backend Synchronization Flow

### 21.1 Purpose

This section defines the complete Backend Synchronization Flow for SplitSync V1.

The Backend Synchronization Flow is responsible for synchronizing locally persisted changes from Android devices with the SplitSync backend and for receiving authorized changes created by other devices.

The backend synchronization architecture must support:

```text
Online Operation
+
Offline Operation
+
Automatic Recovery
+
Multiple Devices
+
Idempotent Operations
+
Conflict Detection
+
Authorization
+
Eventual Consistency
```

The backend acts as the central synchronization point when Internet connectivity is available.

### 21.2 Core Principle

The Backend Synchronization Flow follows:

```text
Local First
    ↓
Persist Locally
    ↓
Queue SyncOperation
    ↓
Backend Synchronization
    ↓
Validate
    ↓
Apply
    ↓
Return Result
    ↓
Pull Remote Changes
    ↓
Apply Locally
```

The backend must never become a mandatory dependency for local expense management.

### 21.3 Backend Synchronization Architecture

```text
┌──────────────────────────────┐
│        Android Device        │
│                              │
│ UI                           │
│  ↓                           │
│ Domain                       │
│  ↓                           │
│ Room                         │
│  ↓                           │
│ Sync Engine                  │
│  ↓                           │
│ Backend Sync Channel         │
└──────────────┬───────────────┘
               │
             HTTPS
               │
               ▼
┌──────────────────────────────┐
│       Spring Boot Backend    │
│                              │
│ Authentication               │
│ Authorization                │
│ Sync API                     │
│ Domain Validation            │
│ Conflict Detection           │
│ Idempotency                  │
│ Transaction Management       │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│            MySQL             │
└──────────────────────────────┘
```

### 21.4 Backend as Synchronization Hub

When Internet connectivity is available:

```text
Device A
      ↓
      Backend
      ↑
Device B
```

The backend provides a common synchronization point for authorized devices.

This allows:

```text
Device A
    ↓
Backend
    ↓
Device B
```

without requiring Device A and Device B to be directly connected.

### 21.5 Backend Is Not the Local Working Database

The Android application must continue using:

```text
Room
```

as its local persistence layer.

The backend is responsible for:

```text
Remote Persistence
+
Synchronization
+
Cross-Device Data Exchange
+
Server-Side Validation
+
Authorization
```

### 21.6 Synchronization Trigger

Backend synchronization may be triggered by:

```text
Application Launch
Connectivity Restored
Pending SyncOperation
Periodic Background Sync
User-Initiated Sync
Successful Authentication
Relevant Data Change
```

The exact Android scheduling mechanism is defined in the Android Architecture.

### 21.7 Connectivity Detection

The application first determines whether backend communication may be possible:

```text
Connectivity Available?
        │
   ┌────┴────┐
  Yes        No
   │          │
Sync       Wait
```

Connectivity availability does not guarantee backend availability.

### 21.8 Backend Availability

The synchronization engine must distinguish:

```text
Internet Available
```

from:

```text
Backend Available
```

Example:

```text
Internet = ON
Backend = DOWN
```

The local application continues normally and pending operations remain retryable.

### 21.9 Authentication Check

Before synchronization:

```text
Check Authentication
        ↓
Authenticated?
   ┌────┴────┐
  Yes        No
   │          │
 Sync      Authenticate
```

Authentication failure must not remove local data.

### 21.10 Authentication Recovery

If authentication has expired:

```text
Token Expired
      ↓
Refresh / Re-authenticate
      ↓
Authentication Successful
      ↓
Resume Synchronization
```

If authentication cannot be restored:

```text
Local Operation
      ↓
Continue
```

while backend synchronization remains pending.

### 21.11 Synchronization Scope

Synchronization must be scoped to the authorized User and Groups.

The application must not request or receive unrelated data.

Conceptually:

```text
Authenticated User
      ↓
Authorized Groups
      ↓
Synchronization Scope
```

### 21.12 Synchronization Cycle

A standard synchronization cycle is:

```text
Start Sync
    ↓
Authenticate
    ↓
Push Local Operations
    ↓
Process Push Results
    ↓
Pull Remote Changes
    ↓
Apply Remote Changes
    ↓
Update SyncState
    ↓
Check More Changes
    ↓
Complete
```

### 21.13 Push Before Pull

The default V1 flow is:

```text
Push
  ↓
Pull
```

This is intended to reduce the period during which local changes exist only on the device.

The exact protocol may optimize this sequence later without changing the local-first principle.

### 21.14 Loading Pending Operations

The Sync Engine queries:

```text
sync_operations
```

for operations such as:

```text
PENDING
FAILED_RETRYABLE
```

that are eligible for synchronization.

### 21.15 Operation Ordering

Operations must be processed in a safe order.

For example:

```text
Create Group G1
      ↓
Create Membership M1
      ↓
Create Expense E1
```

must respect the required dependency order.

### 21.16 Operation Dependencies

Each operation may have dependencies.

Example:

```text
O100 → Create Group G1

O101 → Create Expense E1 in G1
```

Therefore:

```text
O101 depends on O100
```

The Sync Engine must not blindly send dependent operations in an invalid order.

### 21.17 Preparing a Sync Request

For each operation:

```text
SyncOperation
      ↓
Serialize Required Data
      ↓
Attach Authentication
      ↓
Attach Device Identity
      ↓
Send Backend Request
```

The request must contain the information required by the Backend Synchronization API.

### 21.18 Operation Identity

Every synchronization operation must have a stable:

```text
operationId
```

The same Operation ID must be reused during retries.

Example:

```text
O100
```

Retry:

```text
O100
```

not:

```text
O101
```

### 21.19 Idempotency

The backend must process operations idempotently.

Example:

```text
Device sends O100
      ↓
Backend applies O100
      ↓
Response lost
      ↓
Device retries O100
      ↓
Backend recognizes O100
      ↓
ALREADY_APPLIED
```

No duplicate financial entity should be created.

### 21.20 Backend Operation Lookup

The backend should be able to determine whether:

```text
operationId
```

has already been processed.

Conceptually:

```text
operationId
      ↓
Already Processed?
   ┌──┴──┐
  Yes    No
   │      │
Return   Validate
Result
```

### 21.21 Backend Authentication

The backend validates:

```text
User Identity
+
Device Identity
+
Authentication Credentials
```

before processing the synchronization request.

### 21.22 Backend Device Validation

The backend must verify that the Device is allowed to synchronize.

Example:

```text
Device D1
    ↓
Registered?
    │
 ┌──┴──┐
Yes    No
 │      │
Sync   Reject
```

A revoked Device must not continue synchronization.

### 21.23 Backend Authorization

After authentication:

```text
User
 ↓
Device
 ↓
Group
 ↓
Membership
 ↓
Role / Permission
 ↓
Operation
```

must be validated.

### 21.24 Backend Group Authorization

For an operation against:

```text
Group G1
```

the backend must independently determine whether the originating User/Device is authorized for:

```text
G1
```

The Android client cannot be trusted to provide this authorization decision.

### 21.25 Backend Operation Validation

The backend validates:

```text
Operation Structure
+
Entity Data
+
Domain Rules
+
Authorization
+
Dependencies
+
Version
+
Idempotency
```

before applying the operation.

### 21.26 Backend Transaction

A successful operation should be applied transactionally:

```text
BEGIN TRANSACTION

Validate
    ↓
Apply Domain Change
    ↓
Record Operation
    ↓
Create Change Record
    ↓
COMMIT
```

Failure:

```text
ROLLBACK
```

### 21.27 Change Record

The backend needs a mechanism to determine which changes should later be pulled by other devices.

Conceptually:

```text
Domain Change
      ↓
Change Record
      ↓
Global / Group Sequence
      ↓
Available for Pull
```

The exact database implementation is defined in the Database Architecture.

### 21.28 Server-Side Change Ordering

The backend should assign an ordering mechanism to synchronized changes.

Example:

```text
Change 1001
Change 1002
Change 1003
```

Devices can use this ordering for incremental synchronization.

The exact cursor strategy is defined by the Synchronization Data Model.

### 21.29 Push Result

The backend may return:

```text
APPLIED
ALREADY_APPLIED
CONFLICT
REJECTED
```

Additional retryable error states may be defined by the API.

### 21.30 APPLIED

Example:

```text
O100
    ↓
Backend
    ↓
APPLIED
```

Android updates:

```text
O100 → COMPLETED
```

### 21.31 ALREADY_APPLIED

Example:

```text
O100
    ↓
Backend
    ↓
Already exists
```

Result:

```text
ALREADY_APPLIED
```

The Android device can safely mark the operation as completed.

### 21.32 CONFLICT

Example:

```text
O100
    ↓
Backend
    ↓
Version Conflict
```

The backend returns:

```text
CONFLICT
```

The Android device stores the conflict according to the Conflict Data Model.

### 21.33 REJECTED

Example:

```text
O100
    ↓
Authorization Failure
```

or:

```text
Invalid Domain Operation
```

Result:

```text
REJECTED
```

The Sync Engine must distinguish permanent rejection from retryable failures.

### 21.34 Retryable Failure

Examples:

```text
Network Timeout
Temporary Server Error
Service Unavailable
```

These may remain:

```text
PENDING / RETRYABLE
```

and be retried later.

### 21.35 Permanent Failure

Examples:

```text
Invalid Operation
Unauthorized Operation
Invalid Group
Invalid Domain State
```

These should not be retried indefinitely.

### 21.36 Push Result Processing

Conceptually:

```text
Backend Response
      ↓
Classify Result
      │
      ├── APPLIED
      │      ↓
      │   COMPLETED
      │
      ├── ALREADY_APPLIED
      │      ↓
      │   COMPLETED
      │
      ├── CONFLICT
      │      ↓
      │   CONFLICT
      │
      ├── REJECTED
      │      ↓
      │   REJECTED
      │
      └── RETRYABLE
             ↓
          Retry Later
```

### 21.37 Pull Synchronization

After pushing local operations:

```text
Pull Remote Changes
```

The application sends its current synchronization cursor.

Example:

```text
lastCursor = C500
```

### 21.38 Pull Request

Conceptually:

```text
User / Device
      ↓
Group Scope
      ↓
Current Cursor
      ↓
Backend
```

The backend returns authorized changes after that cursor.

### 21.39 Incremental Pull

Example:

```text
Local Cursor:
C500
```

Backend:

```text
C501
C502
C503
C504
```

Response:

```text
C501-C504
```

After successful local application:

```text
Cursor = C504
```

### 21.40 Cursor Semantics

The cursor represents:

```text
Last Successfully Applied Remote Change
```

It must not advance merely because a response was received.

### 21.41 Cursor Update Transaction

Remote changes and cursor advancement should be handled transactionally:

```text
BEGIN TRANSACTION

Apply Remote Changes
      ↓
Update Cursor
      ↓
COMMIT
```

If application fails:

```text
ROLLBACK
```

and the cursor remains unchanged.

### 21.42 Remote Change Validation

Every pulled change must be validated before local application.

Conceptually:

```text
Receive Change
      ↓
Verify Integrity
      ↓
Verify Origin
      ↓
Check Authorization
      ↓
Validate Domain Data
      ↓
Check Duplicate
      ↓
Check Version
      ↓
Apply
```

### 21.43 Remote Authorization

A remote change is not automatically trusted merely because it came from the backend.

The Android application should still validate the change according to the synchronization protocol and local Domain constraints.

### 21.44 Applying Remote Changes

Example:

```text
Remote Expense E10
      ↓
Not Present Locally
      ↓
Validate
      ↓
Insert Expense
      ↓
Insert Expense Splits
      ↓
Record Operation
      ↓
Update SyncState
```

### 21.45 Existing Remote Entity

If the entity already exists:

```text
Remote Expense E10
      ↓
Local Expense E10 exists
      ↓
Compare Version / Operation
```

Possible outcomes:

```text
Already Applied
Update Required
Conflict
Invalid
```

### 21.46 Duplicate Remote Change

If:

```text
operationId = O100
```

has already been applied locally:

```text
O100
    ↓
Already Applied
    ↓
Do Not Apply Again
```

### 21.47 Remote Conflict

Example:

```text
Local:
Expense E1 Version 6

Remote:
Expense E1 Version 6
Different Change
```

The application detects:

```text
Concurrent Update
```

and creates the appropriate Conflict state.

### 21.48 Conflict Does Not Mean Data Loss

When a conflict occurs:

```text
Local State
+
Remote State
```

must be preserved according to the Conflict Data Model.

The system must not silently discard one side merely because synchronization failed.

### 21.49 Conflict Resolution

Conflict resolution may produce:

```text
Resolution Operation
```

where required.

Example:

```text
Conflict
   ↓
User Resolves
   ↓
Resolved State
   ↓
New SyncOperation
   ↓
Backend
```

### 21.50 Pull Pagination

A large number of changes may require multiple pages.

Example:

```text
Cursor C500

Page 1:
C501-C550

Page 2:
C551-C600

Page 3:
C601-C650
```

The Sync Engine processes each page safely.

### 21.51 Pull Page Transaction

Each page may be applied transactionally:

```text
BEGIN TRANSACTION

Apply Page
      ↓
Update Cursor
      ↓
COMMIT
```

This allows recovery if the next page fails.

### 21.52 Large Group Synchronization

Large Groups should use:

```text
Incremental Pull
+
Pagination
+
Batching
```

rather than repeatedly transferring complete Group state.

### 21.53 Initial Backend Synchronization

A new device may require initial synchronization:

```text
Authenticate
      ↓
Determine Authorized Groups
      ↓
Download Initial Group State
      ↓
Persist Locally
      ↓
Initialize SyncState
```

### 21.54 Initial Sync With Existing Local Data

If the device already contains local data:

```text
Local State
+
Backend State
```

must not be blindly overwritten.

The synchronization process must determine:

```text
Identity
+
Ownership
+
Operation History
+
Duplicate Operations
+
Conflicts
```

before combining the states.

The exact account-linking/migration strategy is defined separately.

### 21.55 Backend Group Creation

Online Group creation follows:

```text
Create Group Locally
      ↓
Create SyncOperation
      ↓
Push Backend
      ↓
Backend Authorization
      ↓
Backend Validation
      ↓
Create Group
      ↓
Create Change Record
      ↓
Return APPLIED
```

### 21.56 Backend Expense Creation

```text
Create Expense Locally
      ↓
Create Expense Splits
      ↓
Create SyncOperation
      ↓
Push Backend
      ↓
Authenticate
      ↓
Authorize
      ↓
Validate
      ↓
Persist
      ↓
Create Change Record
      ↓
Return APPLIED
```

### 21.57 Backend Expense Update

Example:

```text
Local Expense:
E1 Version 5

User edits:
Version 6

SyncOperation:
baseVersion = 5
```

Backend:

```text
Current Version = 5
      ↓
Version Matches
      ↓
Apply Update
      ↓
Version = 6
```

### 21.58 Backend Expense Conflict

Example:

```text
Device A:
E1 Version 5 → Version 6

Device B:
E1 Version 5 → Version 6
```

If Device A reaches the backend first:

```text
A → Version 6
```

Then Device B:

```text
B → baseVersion 5
```

Backend detects:

```text
Conflict
```

according to the Conflict Resolution model.

### 21.59 Backend Settlement Creation

```text
Create Settlement Locally
      ↓
SyncOperation
      ↓
Backend
      ↓
Authenticate
      ↓
Authorize
      ↓
Validate Participants
      ↓
Validate Amount
      ↓
Persist
      ↓
Create Change Record
      ↓
APPLIED
```

### 21.60 Backend Membership Change

Membership operations require additional authorization.

Example:

```text
OWNER
    ↓
Remove Member
    ↓
Local Operation
    ↓
SyncOperation
    ↓
Backend
    ↓
Validate Owner Permission
    ↓
Apply Membership Change
```

### 21.61 Backend Invitation

An invitation may be synchronized as a Domain operation:

```text
Create Invitation
      ↓
SyncOperation
      ↓
Backend
      ↓
Validate Inviter Authorization
      ↓
Persist Invitation
      ↓
Available to Recipient
```

### 21.62 Backend Device Registration

A Device may need backend registration before synchronization.

Conceptually:

```text
Local Device
      ↓
Authentication
      ↓
Device Registration
      ↓
Backend Associates Device
      ↓
Device Authorized
      ↓
Synchronization
```

The exact registration flow is defined in Authentication and Security Architecture.

### 21.63 Backend Device Revocation

If a Device is revoked:

```text
Device D1
      ↓
REVOKED
```

future synchronization requests must be rejected.

Local data should not automatically be deleted.

### 21.64 Backend Sync Queue Recovery

After application restart:

```text
Load SyncOperations
      ↓
Find Pending / Retryable
      ↓
Resume Synchronization
```

The application does not need to recreate operations.

### 21.65 Crash During Push

Example:

```text
Send O100
      ↓
Backend applies O100
      ↓
Android crashes before response
```

After restart:

```text
O100 still exists
```

Android retries:

```text
O100
```

Backend responds:

```text
ALREADY_APPLIED
```

Therefore no duplicate record is created.

### 21.66 Crash During Pull

Example:

```text
Receive C501-C550
      ↓
Application crashes
```

If the transaction was not committed:

```text
Changes rolled back
Cursor remains previous value
```

The next synchronization safely requests the same range again.

### 21.67 Backend Timeout

Example:

```text
Send O100
      ↓
Timeout
```

The result is unknown.

The Sync Engine must not create a new Operation ID.

Instead:

```text
Retry O100
```

using idempotency.

### 21.68 Backend Temporary Failure

Example:

```text
HTTP 503
```

The operation remains:

```text
RETRYABLE
```

and is retried using controlled backoff.

### 21.69 Backend Permanent Failure

Example:

```text
Authorization denied
```

The operation becomes:

```text
REJECTED
```

and should not be continuously retried.

### 21.70 Retry Policy

Retryable failures should use:

```text
Exponential Backoff
+
Maximum Retry Delay
+
Connectivity Awareness
```

The exact numeric retry policy will be defined during implementation.

### 21.71 Background Synchronization

Backend synchronization should preferably run in the background when Android allows it.

Potential triggers:

```text
Connectivity Restored
Periodic Work
Application Start
User Action
Pending Operations
```

The Android implementation should use the appropriate background execution mechanism.

### 21.72 Foreground Synchronization

Some operations may be initiated while the User is actively using the application:

```text
User Creates Expense
      ↓
Local Save
      ↓
Sync Worker
```

The User should not need to wait for backend completion to continue using the application.

### 21.73 Background Synchronization Failure

If background synchronization fails:

```text
Local Data
      ↓
Remains Available
```

The failure is represented through:

```text
SyncState
+
SyncOperation State
```

### 21.74 Manual Synchronization

The application may provide:

```text
Sync Now
```

as an optional User action.

However:

```text
Sync Now
```

must not be required for ordinary synchronization.

### 21.75 Automatic Synchronization

Preferred flow:

```text
Connectivity Available
      ↓
Pending Changes?
   ┌──┴──┐
  Yes    No
   │      │
 Sync   Done
```

### 21.76 Backend Synchronization and Local Changes

A User can continue making changes while synchronization is active.

Example:

```text
O100
O101
```

are being synchronized.

User creates:

```text
O102
```

The new operation is persisted locally and becomes eligible for a later/current synchronization cycle.

### 21.77 Concurrent Local and Remote Changes

Possible state:

```text
Local:
E1 Version 5

Remote:
E2 Version 4
```

The Sync Engine can:

```text
Push E1
Pull E2
```

without blocking unrelated entities.

### 21.78 Entity-Level Conflict Scope

A conflict should normally affect the smallest applicable scope.

Example:

```text
Expense E1
```

has a conflict.

This should not automatically block:

```text
Expense E2
Expense E3
Settlement S1
```

unless the Domain dependency requires it.

### 21.79 Group-Level Blocking

Some operations may legitimately require Group-level serialization or conflict handling.

For example:

```text
Group Ownership Transfer
```

may affect multiple related entities.

The exact rules are defined by the Domain and Conflict Resolution models.

### 21.80 Backend Change Propagation

When Device A changes an Expense:

```text
Device A
    ↓
Backend
    ↓
Change Record
```

Device B later pulls:

```text
Changes after cursor
```

and receives the Expense change.

### 21.81 Backend as Source of Remote Changes

For online synchronization:

```text
Backend
```

is the source from which a device receives changes created by:

```text
Other Authorized Devices
```

and backend-authoritative operations.

### 21.82 Backend Does Not Replace Local State

The Android application must not periodically discard Room data and redownload the complete backend database.

Instead:

```text
Local State
+
Incremental Remote Changes
```

are combined through the synchronization protocol.

### 21.83 Synchronization Scope by Group

A device may synchronize multiple Groups:

```text
G1
G2
G3
```

Each Group must be synchronized according to its authorized scope.

### 21.84 Group Cursor

The synchronization system may maintain a Group-specific cursor or another equivalent scope-aware mechanism.

Example:

```text
G1 → C500
G2 → C820
G3 → C120
```

The exact cursor design is defined in the Synchronization Data Model.

### 21.85 Device-Level Sync State

The application may also maintain device-level synchronization metadata:

```text
Device ID
Last Successful Sync
Last Failure
Backend State
```

This is separate from Group-level change tracking where required.

### 21.86 Backend Synchronization State

Possible states:

```text
IDLE
SYNCING
SYNCED
FAILED
BLOCKED
AUTH_REQUIRED
```

The exact enum is defined in the Sync State Model.

### 21.87 Synchronization Metrics

The application may record non-sensitive synchronization metrics such as:

```text
Last Successful Sync
Pending Operation Count
Last Sync Error
Last Sync Duration
```

These metrics support diagnostics and User visibility.

### 21.88 Backend Sync Error Classification

Errors should be classified into:

```text
NETWORK_ERROR
AUTHENTICATION_ERROR
AUTHORIZATION_ERROR
VALIDATION_ERROR
CONFLICT
SERVER_ERROR
RATE_LIMITED
UNKNOWN_ERROR
```

The final API error catalog will be defined separately.

### 21.89 Network Error

Example:

```text
Timeout
No Internet
Connection Reset
```

Result:

```text
Retryable
```

unless the system determines otherwise.

### 21.90 Authentication Error

Example:

```text
401
```

Flow:

```text
Authentication Error
      ↓
Refresh / Re-authenticate
      ↓
Retry Synchronization
```

If re-authentication fails:

```text
Sync Paused
Local Operation Continues
```

### 21.91 Authorization Error

Example:

```text
403
```

Flow:

```text
Authorization Error
      ↓
Do Not Blindly Retry
      ↓
Mark Operation Rejected / Blocked
      ↓
Apply Security Policy
```

### 21.92 Validation Error

Example:

```text
Invalid Expense Split
```

Result:

```text
Permanent Rejection
```

The invalid operation must not be retried indefinitely.

### 21.93 Conflict Error

Example:

```text
409 / Domain Conflict
```

Result:

```text
Create Conflict
```

and stop retrying the same operation until the conflict is resolved according to the Conflict Resolution model.

### 21.94 Rate Limiting

If the backend rate-limits a device:

```text
Rate Limited
      ↓
Respect Retry-After / Backoff
      ↓
Retry Later
```

The local application remains available.

### 21.95 Backend Security Boundary

The backend must independently enforce:

```text
Authentication
Authorization
Validation
Idempotency
Transaction Integrity
```

The Android application is not trusted as a security authority.

### 21.96 Backend Transaction and Operation Record

For a successful synchronization operation:

```text
BEGIN TRANSACTION

Apply Domain Change
      ↓
Persist Operation ID
      ↓
Create Change Record
      ↓
COMMIT
```

This ensures that the operation and resulting change are not separated by a partial transaction.

### 21.97 Operation Record and Idempotency

The backend must persist enough information to determine:

```text
Was operation O100 already processed?
```

The stored operation record may include:

```text
operationId
originDeviceId
originUserId
entityId
operationType
result
processedAt
```

The exact schema is defined in the Database Architecture.

### 21.98 Backend Change Record

A change record should provide enough information for:

```text
Incremental Pull
Ordering
Scope Filtering
```

Conceptually:

```text
Change
├── changeId / sequence
├── groupId
├── entityType
├── entityId
├── operationId
└── createdAt
```

The exact model is defined separately.

### 21.99 Pull Authorization

When a device requests:

```text
Changes for G1
```

the backend must verify that the authenticated User/Device is authorized to access G1.

The client-provided:

```text
groupId
```

must not itself grant access.

### 21.100 Backend Data Minimization

The backend should return only:

```text
Authorized
+
Required
+
Not Already Synchronized
```

data.

### 21.101 Backend Sync and P2P

The Backend and P2P channels must use the same synchronization concepts:

```text
SyncOperation
Operation ID
Origin Identity
Version
Conflict Model
SyncState
```

This allows:

```text
P2P → Backend
```

and:

```text
Backend → P2P
```

without creating duplicate Domain records.

### 21.102 P2P Then Backend Example

```text
Device A
    ↓
Creates O100
    ↓
P2P → Device B
    ↓
B applies O100
    ↓
Internet returns
    ↓
A → Backend
    ↓
Backend applies O100
    ↓
B → Backend
    ↓
Backend / B recognize O100
    ↓
No duplicate
```

### 21.103 Backend Then P2P Example

```text
Device A
    ↓
Creates O100
    ↓
Backend
    ↓
Device B
    ↓
B receives O100

Later:
B
 ↓
P2P
 ↓
Device C
```

Device C receives the same operation identity:

```text
O100
```

### 21.104 Backend Synchronization and Eventual Consistency

The system aims for:

```text
Local Strong Consistency
+
Remote Eventual Consistency
```

Temporary differences are expected while devices are:

```text
Offline
Synchronizing
Disconnected
```

### 21.105 Backend Synchronization Completion

A Group synchronization cycle is complete when:

```text
Eligible Local Operations
        ↓
Processed
        +
Required Remote Changes
        ↓
Applied
        +
Sync Cursor Updated
        +
No Blocking Error
```

Then:

```text
SyncState = SYNCED
```

### 21.106 Complete Backend Push Flow

```text
Pending SyncOperations
        ↓
Check Connectivity
        ↓
Authenticate
        ↓
Load Eligible Operations
        ↓
Order Operations
        ↓
Send Batch / Operation
        ↓
Backend Authentication
        ↓
Backend Authorization
        ↓
Backend Validation
        ↓
Idempotency Check
        ↓
Version / Conflict Check
        ↓
Apply Transaction
        ↓
Create Change Record
        ↓
Return Result
        ↓
Update Local SyncOperation
```

### 21.107 Complete Backend Pull Flow

```text
Current SyncState
        ↓
Authenticate
        ↓
Request Changes After Cursor
        ↓
Backend Authorization
        ↓
Return Authorized Changes
        ↓
Receive Batch
        ↓
Validate Changes
        ↓
Check Duplicate
        ↓
Check Conflict
        ↓
Apply Transaction
        ↓
Update Cursor
        ↓
COMMIT
        ↓
Request Next Batch if Required
```

### 21.108 Complete Backend Synchronization Flow

```text
Application / Connectivity Trigger
                ↓
        Check Backend Availability
                ↓
        Authenticate User/Device
                ↓
          Determine Scope
                ↓
        Load Pending Operations
                ↓
          Order Operations
                ↓
             PUSH
                ↓
        Process Push Results
                ↓
       Handle Conflicts/Rejects
                ↓
              PULL
                ↓
      Receive Remote Changes
                ↓
         Validate Changes
                ↓
      Apply Changes Transactionally
                ↓
          Update SyncState
                ↓
      More Changes Available?
             ┌──┴──┐
            Yes    No
             │      │
            Pull   Complete
             │
             └───────┐
                     ↓
                  SYNCED
```

### 21.109 Backend Synchronization Summary

```text
Backend Synchronization
│
├── Connectivity
│   ├── Internet Detection
│   └── Backend Availability
│
├── Authentication
│   ├── User
│   └── Device
│
├── Scope
│   └── Authorized Groups
│
├── Push
│   ├── Pending Operations
│   ├── Ordering
│   ├── Idempotency
│   ├── Authorization
│   ├── Validation
│   └── Conflict Detection
│
├── Pull
│   ├── Cursor
│   ├── Incremental Changes
│   ├── Pagination
│   ├── Validation
│   └── Transactional Apply
│
├── Recovery
│   ├── Retry
│   ├── Timeout
│   ├── Authentication Recovery
│   └── Application Restart
│
└── Completion
    ├── SyncState
    ├── Operation State
    └── Eventual Convergence
```

### 21.110 Backend Synchronization Invariants

The following rules are mandatory for V1:

- Backend synchronization must remain optional for core local expense functionality.
- Local Room data remains the primary working data source on Android.
- Backend synchronization must not require the User to manually switch modes.
- Synchronization should start automatically when suitable connectivity is available.
- Internet availability does not guarantee backend availability.
- Backend authentication must be validated before synchronization.
- Backend authorization must be independently validated.
- Device authorization must be independently validated.
- Group authorization must be independently validated.
- Client-provided authorization claims must never be trusted without server-side verification.
- Synchronization must be scoped to authorized Groups.
- The backend must not return unauthorized Group data.
- Local changes must be persisted before being sent to the backend.
- Domain changes and their SyncOperations must be created atomically.
- Every synchronization operation must have a stable Operation ID.
- Retries must reuse the same Operation ID.
- Backend operation processing must be idempotent.
- Duplicate operations must never create duplicate financial records.
- Operation dependencies must be respected.
- Backend Domain changes must be applied transactionally.
- Operation records and resulting change records should be committed consistently with the Domain change.
- Backend changes must be available for incremental synchronization by authorized devices.
- Pull synchronization must use a cursor or equivalent incremental synchronization mechanism.
- The synchronization cursor must represent the last successfully applied remote change.
- The cursor must not advance before the corresponding changes are successfully committed locally.
- Remote changes must be validated before being applied to Room.
- Remote changes must not bypass local Domain integrity rules.
- Remote changes must not bypass local synchronization validation.
- Conflicts must use the defined Conflict Data Model.
- Financial conflicts must not be resolved using arbitrary last-write-wins.
- Permanent failures must not be retried indefinitely.
- Retryable failures must use controlled retry/backoff.
- Authentication failures must trigger appropriate authentication recovery.
- Authorization failures must not be blindly retried.
- Validation failures must not be blindly retried.
- Timeouts must be safely retryable through idempotency.
- Application crashes must not cause duplicate backend operations.
- Application crashes during pull must not cause the synchronization cursor to advance incorrectly.
- Pending SyncOperations must survive application restart.
- SyncState must survive application restart.
- Backend synchronization must continue automatically after connectivity is restored.
- Backend synchronization must not block normal local application usage.
- New local changes may be created while synchronization is running.
- New local changes must receive their own SyncOperations.
- Synchronization should be incremental rather than repeatedly downloading complete Group state.
- Large synchronization sets must support batching and pagination.
- Group synchronization state must be tracked with sufficient scope to prevent incorrect cursor sharing.
- P2P and Backend synchronization must use the same Operation ID and Domain synchronization concepts.
- Data synchronized through P2P must not be duplicated when later synchronized through the Backend.
- Data synchronized through the Backend must not be duplicated when later synchronized through P2P.
- Backend synchronization must preserve eventual convergence across authorized devices.
- Backend failure must not make the local application unusable.
- Local financial data must never be deleted merely because backend synchronization fails.
- Synchronization state must accurately represent pending, completed, failed, rejected, and conflicted operations.
- The backend must remain the authoritative remote synchronization point for online cross-device synchronization.
- The local application must remain the authoritative working environment for the User's current device.

## 22. Error Handling

### 22.1 Purpose

This section defines the error-handling strategy for SplitSync V1.

SplitSync must distinguish between:

- Local application errors.
- Domain validation errors.
- Authentication errors.
- Authorization errors.
- Network errors.
- Backend errors.
- Synchronization errors.
- Conflict errors.
- P2P communication errors.
- Persistence errors.
- Security-related errors.

The most important principle is:

```text
An error in synchronization must not cause loss of valid local data.
```

### 22.2 Core Principle

SplitSync follows:

```text
Fail Safely
+
Preserve Local Data
+
Classify Errors
+
Retry Only When Appropriate
+
Never Silently Lose Financial Data
```

### 22.3 Error Handling Architecture

```text
User / System Action
        ↓
Operation
        ↓
Validation
        ↓
Execution
        ↓
Error?
   ┌────┴────┐
  No        Yes
   │          │
Success    Classify Error
              ↓
        Handle by Category
              ↓
        Recover / Retry /
        Reject / Report
```

### 22.4 Error Categories

The primary V1 error categories are:

```text
LOCAL_ERROR
VALIDATION_ERROR
AUTHENTICATION_ERROR
AUTHORIZATION_ERROR
NETWORK_ERROR
BACKEND_ERROR
SYNC_ERROR
CONFLICT_ERROR
P2P_ERROR
DATABASE_ERROR
SECURITY_ERROR
UNKNOWN_ERROR
```

### 22.5 Error Classification

Every significant error should be classified before determining the recovery strategy.

Example:

```text
Network Timeout
    ↓
NETWORK_ERROR
    ↓
Retryable
```

Another example:

```text
Invalid Expense Split
    ↓
VALIDATION_ERROR
    ↓
Permanent
```

### 22.6 Local Errors

A local error occurs while performing an operation entirely on the device.

Examples:

```text
Invalid local state
Database failure
Unexpected application state
Invalid local reference
```

Local errors must be handled without assuming backend availability.

### 22.7 Validation Errors

Validation errors occur when input or Domain data violates application rules.

Examples:

```text
Expense amount <= 0
Invalid split amount
Invalid percentage total
Invalid settlement amount
Missing required Group
Invalid participant
```

These errors are normally:

```text
NON-RETRYABLE
```

until the underlying data is corrected.

### 22.8 Authentication Errors

Authentication errors occur when the application cannot establish a valid identity.

Examples:

```text
Invalid credentials
Expired authentication
Invalid token
Revoked session
Invalid device authentication
```

The application should attempt the appropriate authentication recovery where possible.

### 22.9 Authorization Errors

Authorization errors occur when the identity is known but the operation is not permitted.

Examples:

```text
User is not a Group member
Insufficient Group role
Device revoked
Operation not permitted
```

These errors must not be blindly retried.

### 22.10 Network Errors

Network errors occur when communication cannot be established or maintained.

Examples:

```text
No Internet
Connection timeout
Connection reset
DNS failure
Network unavailable
```

For backend synchronization:

```text
Network Error
    ↓
Keep Local Data
    ↓
Keep SyncOperation
    ↓
Retry Later
```

### 22.11 Backend Errors

Backend errors are failures returned by the backend service.

Examples:

```text
HTTP 500
HTTP 502
HTTP 503
Service unavailable
Unexpected server response
```

The application must determine whether the error is:

```text
Retryable
```

or:

```text
Permanent
```

### 22.12 Synchronization Errors

Synchronization errors occur while processing:

```text
SyncOperation
SyncState
Remote Changes
Cursor
Conflict
```

Examples:

```text
Operation rejected
Invalid remote change
Cursor error
Dependency failure
Synchronization protocol error
```

### 22.13 Conflict Errors

A conflict occurs when multiple valid changes cannot be automatically combined.

Example:

```text
Device A
    ↓
Expense E1 → Version 6

Device B
    ↓
Expense E1 → Version 6
```

The system must create:

```text
CONFLICT
```

rather than silently overwrite one change.

### 22.14 P2P Errors

P2P errors include:

```text
Peer discovery failure
Connection failure
Authentication failure
Authorization failure
Protocol mismatch
Message integrity failure
Connection interruption
Peer unavailable
```

P2P errors must not affect already committed local Domain data.

### 22.15 Database Errors

Database errors occur when local persistence fails.

Examples:

```text
SQLite error
Room transaction failure
Constraint violation
Database corruption
Storage unavailable
```

A database failure is fundamentally different from:

```text
Network unavailable
```

### 22.16 Security Errors

Security errors include:

```text
Invalid authentication
Invalid signature
Tampered message
Untrusted peer
Invalid security token
Revoked device
```

Security-sensitive errors must fail closed.

### 22.17 Unknown Errors

Unexpected errors must be classified as:

```text
UNKNOWN_ERROR
```

until sufficient information is available.

Unknown errors must not result in destructive recovery behavior.

### 22.18 Error Severity

Errors may be classified by severity:

```text
INFO
WARNING
ERROR
CRITICAL
```

Examples:

```text
Sync temporarily unavailable
    → WARNING

Database transaction failure
    → ERROR

Detected security compromise
    → CRITICAL
```

### 22.19 Retryability

Every operational error should be classified as:

```text
RETRYABLE
NON_RETRYABLE
USER_ACTION_REQUIRED
```

Example:

```text
Network Timeout
    → RETRYABLE

Invalid Expense
    → NON_RETRYABLE

Authentication Expired
    → USER_ACTION_REQUIRED / RECOVERABLE
```

### 22.20 Retryable Errors

Typical retryable errors include:

```text
Network timeout
Temporary backend failure
Service unavailable
Temporary connection failure
Rate limiting
Temporary P2P disconnection
```

### 22.21 Non-Retryable Errors

Typical non-retryable errors include:

```text
Invalid Domain data
Invalid Expense Split
Unauthorized operation
Invalid Group membership
Malformed operation
Unsupported operation
Permanent protocol mismatch
```

### 22.22 User Action Required

Some errors require the User to take action.

Examples:

```text
Authentication expired
Conflict requires resolution
Permission changed
Invalid user input
```

The application should clearly communicate the required action.

### 22.23 Error Handling Must Preserve Local Data

The following must never happen merely because synchronization fails:

```text
Delete Expense
Delete Group
Delete Settlement
Delete SyncOperation
Clear Local Database
```

### 22.24 Local Operation Failure

For a local operation:

```text
User Action
    ↓
Validation
    ↓
Database Transaction
    ↓
Failure
```

The transaction must be rolled back if it cannot be safely committed.

### 22.25 Local Transaction Failure

Example:

```text
Create Expense
      ↓
Create Expense Splits
      ↓
Database Failure
```

Result:

```text
ROLLBACK
```

The application must not leave:

```text
Partial Expense
```

in the local database.

### 22.26 Atomic Expense Creation

Expense creation must be atomic:

```text
BEGIN TRANSACTION

Expense
Expense Splits
SyncOperation

COMMIT
```

If any required part fails:

```text
ROLLBACK
```

### 22.27 Atomic Settlement Creation

Similarly:

```text
BEGIN TRANSACTION

Settlement
SyncOperation

COMMIT
```

If persistence fails:

```text
ROLLBACK
```

### 22.28 Error During SyncOperation Creation

If the Domain entity is successfully saved but the corresponding SyncOperation cannot be created, the entire transaction must fail.

Therefore:

```text
Expense Saved
+
SyncOperation Failed
```

must not become a committed state for operations requiring synchronization.

### 22.29 Synchronization Error

For:

```text
SyncOperation O100
```

the Sync Engine must preserve the operation until its final state is known.

Possible states:

```text
PENDING
IN_PROGRESS
COMPLETED
FAILED
CONFLICT
REJECTED
```

### 22.30 Retry State

A retryable failure may result in:

```text
FAILED
```

with metadata such as:

```text
retryCount
lastAttemptAt
nextRetryAt
lastErrorCode
```

The exact fields are defined by the Sync State / Sync Operation model.

### 22.31 Retry Backoff

Retryable synchronization failures should use controlled backoff:

```text
Attempt 1
    ↓
Wait
Attempt 2
    ↓
Longer Wait
Attempt 3
    ↓
Longer Wait
```

This prevents:

```text
Battery Drain
Network Flooding
Server Overload
```

### 22.32 Retry With Same Operation ID

A retry must use:

```text
Same operationId
```

Example:

```text
O100
```

must remain:

```text
O100
```

for every retry.

### 22.33 Timeout Handling

Example:

```text
Send O100
    ↓
Timeout
```

The result may be unknown.

The application must not assume:

```text
Operation definitely failed
```

Instead:

```text
Retry O100
```

using idempotency.

### 22.34 Backend 5xx Errors

Typical:

```text
500
502
503
504
```

may be treated as retryable depending on the specific response.

The local operation remains safe:

```text
SyncOperation = RETRYABLE
```

### 22.35 Backend 4xx Errors

Typical client-side errors may include:

```text
400
401
403
404
409
422
429
```

They must not all be handled identically.

Example:

```text
401 → Authentication recovery
403 → Authorization failure
409 → Conflict
422 → Validation failure
429 → Retry with backoff
```

### 22.36 Authentication Error Flow

```text
Backend Request
      ↓
Authentication Error
      ↓
Refresh / Re-authenticate
      ↓
Success?
   ┌──┴──┐
  Yes    No
   │      │
Retry   Pause Sync
         ↓
    Local Operation
       Remains
```

### 22.37 Authorization Error Flow

```text
Backend Request
      ↓
Authorization Error
      ↓
Do Not Blindly Retry
      ↓
Mark Operation Rejected / Blocked
      ↓
Preserve Local State
      ↓
Apply Defined Resolution Policy
```

### 22.38 Validation Error Flow

```text
Backend Request
      ↓
Validation Error
      ↓
Permanent Failure
      ↓
Mark Operation REJECTED
      ↓
Do Not Retry Automatically
```

### 22.39 Conflict Error Flow

```text
Backend Request
      ↓
Conflict
      ↓
Create Conflict Record
      ↓
Preserve Relevant States
      ↓
Mark Operation CONFLICT
      ↓
User / Resolution Strategy
```

### 22.40 Rate Limit Error

```text
Backend
    ↓
429 / Rate Limited
    ↓
Read Retry Information
    ↓
Wait
    ↓
Retry
```

The application must not continuously retry without delay.

### 22.41 Backend Unavailable

Example:

```text
Internet = ON
Backend = OFF
```

The application should behave as:

```text
Local Operation = Available
Synchronization = Pending
```

### 22.42 Internet Unavailable

Example:

```text
Internet = OFF
```

The application should not treat normal local operations as failures.

Instead:

```text
Expense Creation
      ↓
Local Save
      ↓
SyncOperation = PENDING
```

### 22.43 Offline Error Handling

The User should not receive a blocking:

```text
Network Error
```

merely because the application cannot synchronize.

A more appropriate state is:

```text
Saved locally
Sync pending
```

### 22.44 P2P Connection Error

Example:

```text
P2P Session
    ↓
Connection Lost
```

Result:

```text
Local Data = Preserved
Successful Operations = Preserved
Remaining Operations = Retryable
```

### 22.45 P2P Authentication Failure

```text
Peer Connection
      ↓
Authentication Failure
      ↓
Reject P2P Session
      ↓
Do Not Exchange Group Data
      ↓
Local Application Continues
```

### 22.46 P2P Authorization Failure

```text
Authenticated Peer
      ↓
Group Authorization Failure
      ↓
Deny Group Synchronization
      ↓
Do Not Send Group Data
```

Other authorized Groups may still be synchronized if the protocol permits.

### 22.47 P2P Protocol Mismatch

Example:

```text
Device A → Protocol 1
Device B → Protocol 99
```

If incompatible:

```text
Reject Session / Negotiate Compatible Version
```

The application must not attempt to interpret unsupported messages.

### 22.48 P2P Message Integrity Failure

If a received message fails integrity verification:

```text
Message
    ↓
Integrity Check
    ↓
FAIL
    ↓
Reject Message
    ↓
Do Not Apply Data
```

Repeated security failures may terminate the peer session.

### 22.49 P2P Duplicate Data

If the same operation is received repeatedly:

```text
operationId
    ↓
Already Applied?
   ┌──┴──┐
  Yes    No
   │      │
Ignore  Process
```

The duplicate must not create another financial record.

### 22.50 Remote Data Validation Failure

If a remote change cannot satisfy local Domain rules:

```text
Remote Change
      ↓
Validation
      ↓
FAIL
```

The application must not blindly insert the data.

The failure should be recorded for synchronization diagnostics and appropriate recovery.

### 22.51 Cursor Failure

If a pull cursor cannot be safely advanced:

```text
Do Not Advance Cursor
```

The application should retry from the last known valid cursor.

### 22.52 Cursor Corruption

If local synchronization state becomes invalid:

```text
Invalid Cursor
      ↓
Stop Affected Synchronization Scope
      ↓
Recover / Reinitialize SyncState
      ↓
Resynchronize Required Data
```

Recovery must not delete valid local Domain data.

### 22.53 SyncOperation Corruption

If an operation is malformed locally:

```text
Invalid SyncOperation
      ↓
Do Not Send
      ↓
Mark Invalid / Error
      ↓
Preserve Domain Data
      ↓
Record Diagnostic Information
```

The application must avoid infinite retry loops.

### 22.54 Dependency Failure

Example:

```text
Create Expense E1
```

depends on:

```text
Create Group G1
```

If Group synchronization fails:

```text
E1
    ↓
WAITING_FOR_DEPENDENCY
```

The Expense operation should not be sent until the dependency is resolved.

### 22.55 Dependency Cycle

If the synchronization system detects:

```text
O100 → O101
O101 → O100
```

it must treat this as a synchronization error rather than continuously retrying.

### 22.56 Conflict Does Not Mean Failure of Local Save

Important rule:

```text
Local Expense Save = Successful
Backend Conflict = Later Synchronization Result
```

The application must not roll back a previously successful local transaction merely because a later synchronization conflict occurs.

### 22.57 Conflict Does Not Mean Data Deletion

When a conflict occurs:

```text
Local State
+
Remote State
```

must be preserved according to the Conflict Data Model.

### 22.58 Error and SyncOperation Relationship

Conceptually:

```text
SyncOperation
├── status
├── retryCount
├── lastErrorCode
├── lastErrorMessage / safe diagnostic
├── lastAttemptAt
└── nextRetryAt
```

Sensitive information should not be stored unnecessarily in error messages.

### 22.59 Error and SyncState Relationship

SyncState may contain:

```text
status
lastSuccessfulSync
lastFailure
lastErrorCode
cursor
```

The exact fields are defined by the Sync State Model.

### 22.60 Error Persistence

Important synchronization errors should be persisted when they are required for:

```text
Retry
Recovery
Diagnostics
User Notification
```

Transient UI errors do not necessarily need permanent persistence.

### 22.61 Error Logging

Logging should support:

```text
Debugging
Diagnostics
Failure Analysis
```

Logs must avoid unnecessary sensitive information.

### 22.62 Sensitive Data in Logs

The application must avoid logging:

```text
Passwords
Authentication Tokens
Private Keys
Full Phone Numbers
Full Email Addresses
Financial Details
Sensitive Personal Information
```

unless explicitly required and securely handled.

### 22.63 Backend Error Logging

The backend may log:

```text
Request ID
Operation ID
User / Device reference
Error Code
Timestamp
Service Component
```

but should avoid unnecessary sensitive payload data.

### 22.64 Correlation ID

Backend requests may use a:

```text
requestId / correlationId
```

to trace a synchronization request across services and logs.

Example:

```text
requestId = R1001
operationId = O500
```

This helps diagnose failures without exposing sensitive payloads.

### 22.65 User-Facing Error Messages

User-facing messages should be understandable.

Avoid exposing technical details such as:

```text
NullPointerException
SQL constraint violation
HTTP 500
Stack trace
```

Instead:

```text
"Your expense was saved locally and will sync when the connection is available."
```

### 22.66 Local Validation Message

Example:

```text
Split amounts must equal the expense total.
```

This is preferable to:

```text
VALIDATION_EXCEPTION_102
```

for normal User-facing UI.

### 22.67 Sync Failure Message

Example:

```text
"Saved locally. Sync will retry when the connection is available."
```

This communicates that:

```text
Local Data = Safe
```

### 22.68 Authentication Message

Example:

```text
"Your session has expired. Please sign in again to sync your data."
```

Local data remains available where permitted.

### 22.69 Authorization Message

Example:

```text
"You no longer have permission to modify this group."
```

The application should not expose unnecessary security details.

### 22.70 Conflict Message

Example:

```text
"This expense was changed on another device. Review the changes to resolve the conflict."
```

### 22.71 P2P Failure Message

Example:

```text
"Could not synchronize with this device. Your local changes are safe."
```

### 22.72 Database Failure Message

Example:

```text
"Unable to save this change locally. Please try again."
```

A database failure should not be described as a network error.

### 22.73 Unknown Error Message

Example:

```text
"Something went wrong. Your existing data has not been removed."
```

The application should record diagnostic information where appropriate.

### 22.74 Error Recovery Hierarchy

Recovery should follow:

```text
1. Prevent Data Loss
        ↓
2. Roll Back Unsafe Transaction
        ↓
3. Preserve Valid Local State
        ↓
4. Classify Error
        ↓
5. Retry if Safe
        ↓
6. Request User Action if Required
        ↓
7. Record Diagnostic Information
```

### 22.75 Error Handling During Application Startup

If synchronization fails during startup:

```text
Application Startup
      ↓
Load Local Database
      ↓
Display Local State
      ↓
Attempt Synchronization
      ↓
Failure
```

The application should continue using local data.

### 22.76 Error Handling During Background Sync

If background synchronization fails:

```text
Background Sync
      ↓
Failure
      ↓
Persist Sync State
      ↓
Retry Later
```

The failure should not interrupt ordinary User interaction.

### 22.77 Error Handling During User-Initiated Sync

If the User selects:

```text
Sync Now
```

and synchronization fails:

```text
Show Appropriate Status
+
Keep Local Data
+
Keep Retryable Operations
```

### 22.78 Error Handling During P2P Sync

If P2P fails:

```text
End / Pause P2P Session
      ↓
Preserve Local State
      ↓
Keep Pending Operations
      ↓
Retry Later
```

### 22.79 Error Handling During Online-to-Offline Transition

```text
Online
   ↓
Network Lost
   ↓
Request Fails
   ↓
Classify as Network Error
   ↓
Keep Operation Locally
   ↓
Continue Offline
```

### 22.80 Error Handling During Offline-to-Online Transition

```text
Offline
   ↓
Internet Returns
   ↓
Sync Attempt
   ↓
Backend Error
```

The application remains:

```text
Locally Operational
```

and synchronization retries later.

### 22.81 Error Handling After Application Restart

After restart:

```text
Load SyncOperations
      ↓
Find Incomplete Operations
      ↓
Recover Safe States
      ↓
Retry Eligible Operations
```

The application must not assume that an `IN_PROGRESS` operation failed merely because the previous process ended.

Idempotency must make retry safe.

### 22.82 Error Handling After Device Restart

Same principle:

```text
Device Restart
      ↓
Load Room
      ↓
Load SyncState
      ↓
Load SyncOperations
      ↓
Resume
```

### 22.83 Error Handling and Data Integrity

The system must guarantee:

```text
No Partial Domain Transaction
```

For example:

```text
Expense exists
```

without:

```text
Required ExpenseSplits
```

must not be committed when the operation requires both.

### 22.84 Error Handling and Referential Integrity

Database constraints and Domain validation must prevent invalid references such as:

```text
ExpenseSplit → nonexistent Expense
Settlement → nonexistent Group
Expense → nonexistent Group
```

### 22.85 Error Handling and Idempotency

Idempotency is mandatory for operations that may be retried.

The same:

```text
operationId
```

must produce a consistent backend result without duplicate Domain changes.

### 22.86 Error Handling and Authorization

Authorization errors must not be converted into:

```text
Retry Forever
```

Example:

```text
403
    ↓
Authorization Failure
    ↓
Do Not Retry Indefinitely
```

### 22.87 Error Handling and Security

Security-sensitive failures should use:

```text
Fail Closed
```

Example:

```text
Cannot verify peer identity
    ↓
Do Not Trust Peer
```

### 22.88 Error Handling and Privacy

Error messages must not reveal sensitive information.

For example, an unauthorized request should not expose:

```text
Whether a private Group exists
```

unless the relevant authorization policy permits such disclosure.

### 22.89 Error Handling and P2P Discovery

Discovery failure:

```text
No Peer Found
```

is not an application failure.

The User can continue:

```text
Offline
```

and synchronize later.

### 22.90 Error Handling and Backend Discovery

Backend unavailable:

```text
No Backend
```

is not a local application failure.

The application remains:

```text
Local-first
```

### 22.91 Error Handling and Financial Data

Because SplitSync manages financial records:

```text
Expense
Settlement
Balance
```

must receive stronger data-integrity guarantees than ordinary UI state.

Errors must never silently alter:

```text
Amount
Participant
Split
Settlement
Balance Source Data
```

### 22.92 Error Handling and Derived Balances

If balance calculation fails:

```text
Do Not Persist Incorrect Balance
```

Balances should be recalculated from valid source data.

Where possible:

```text
Expenses
+
Splits
+
Settlements
```

remain the source of truth.

### 22.93 Error Handling and Synchronization State

Synchronization state is operational metadata.

If SyncState becomes invalid:

```text
Recover / Reinitialize SyncState
```

without deleting valid Domain data.

### 22.94 Error Handling and Conflict State

Conflict data is itself important synchronization state.

If a conflict exists:

```text
Do Not Silently Delete Conflict
```

unless it has been explicitly resolved or superseded according to the Conflict Model.

### 22.95 Error Handling and Operation State

An operation should move through controlled states:

```text
PENDING
   ↓
IN_PROGRESS
   ↓
COMPLETED
```

or:

```text
IN_PROGRESS
   ↓
FAILED
```

or:

```text
IN_PROGRESS
   ↓
CONFLICT
```

or:

```text
IN_PROGRESS
   ↓
REJECTED
```

### 22.96 Error State Recovery

Recoverable:

```text
FAILED
    ↓
Retry
    ↓
IN_PROGRESS
```

Permanent:

```text
REJECTED
    ↓
No Automatic Retry
```

Conflict:

```text
CONFLICT
    ↓
Resolution Required
```

### 22.97 Error Handling and Operation Dependencies

If a parent operation fails:

```text
Create Group G1
```

dependent operations:

```text
Create Expense E1
```

may need to remain:

```text
WAITING
```

until the dependency is resolved.

### 22.98 Error Handling and Batch Operations

If a batch contains:

```text
O100
O101
O102
O103
```

each operation should receive an independently meaningful result where possible.

One failure should not unnecessarily cause unrelated valid operations to be lost.

### 22.99 Error Handling and Partial Synchronization

Example:

```text
10 operations
```

Results:

```text
6 → APPLIED
2 → RETRYABLE
1 → CONFLICT
1 → REJECTED
```

The local Sync Engine must preserve each result separately.

### 22.100 Error Handling and Eventual Recovery

A temporary error should not permanently break synchronization.

Example:

```text
Backend unavailable
      ↓
Sync paused
      ↓
Backend available
      ↓
Sync resumes
```

### 22.101 Error Handling and Monitoring

The system should provide enough diagnostic information to determine:

```text
What failed?
Where did it fail?
Which operation failed?
Can it be retried?
Does the User need to act?
```

### 22.102 Error Handling and Observability

Backend observability should support:

```text
Request ID
Operation ID
Device ID reference
Error Code
Timestamp
Service Component
```

without unnecessarily logging sensitive payloads.

### 22.103 Error Handling and Testing

Every error category must have corresponding tests.

At minimum:

```text
Validation Failure
Authentication Failure
Authorization Failure
Network Failure
Backend Failure
Timeout
Duplicate Operation
Conflict
Database Failure
P2P Failure
Application Restart
Device Restart
```

### 22.104 Error Handling Test Principle

For every retryable error:

```text
Error
    ↓
Retry
    ↓
No Duplicate Data
```

For every permanent error:

```text
Error
    ↓
No Infinite Retry
```

For every local transaction failure:

```text
Error
    ↓
No Partial Domain State
```

### 22.105 Error Handling Flow — Local Operation

```text
User Action
      ↓
Authorization
      ↓
Validation
      ↓
Room Transaction
      ↓
Success?
   ┌──┴──┐
  Yes    No
   │      │
Commit   Rollback
   │      │
Success  Classify Error
```

### 22.106 Error Handling Flow — Backend Sync

```text
Pending Operation
      ↓
Connectivity
      ↓
Authentication
      ↓
Send Operation
      ↓
Backend Result
      ↓
Classify
      │
      ├── APPLIED
      │      ↓
      │   COMPLETED
      │
      ├── ALREADY_APPLIED
      │      ↓
      │   COMPLETED
      │
      ├── RETRYABLE
      │      ↓
      │   Backoff / Retry
      │
      ├── CONFLICT
      │      ↓
      │   Conflict State
      │
      └── REJECTED
             ↓
          Permanent State
```

### 22.107 Error Handling Flow — P2P

```text
Peer Discovery
      ↓
Connection
      ↓
Authentication
      ↓
Authorization
      ↓
Synchronization
      ↓
Error?
   ┌──┴──┐
  No    Yes
   │      │
Success  Classify
          │
          ├── Retryable
          ├── Authorization
          ├── Security
          ├── Conflict
          └── Protocol
```

### 22.108 Error Handling Flow — Complete

```text
Operation
    ↓
Validate
    ↓
Authorize
    ↓
Persist / Synchronize
    ↓
Error?
    │
 ┌──┴───────────────────────────┐
 │                              │
No                              Yes
 │                               │
Success                    Classify Error
                                │
             ┌──────────────────┼──────────────────┐
             │                  │                  │
        Retryable          Permanent          User Action
             │                  │                  │
          Retry             Reject             Notify
             │                  │                  │
             └──────────────────┴──────────────────┘
                                ↓
                         Preserve Local Data
```

### 22.109 Error Handling Summary

```text
Error Handling
│
├── Local
│   ├── Validation
│   ├── Database
│   └── Domain
│
├── Authentication
│   └── Session / Identity
│
├── Authorization
│   └── Permission / Membership
│
├── Network
│   ├── Timeout
│   ├── Offline
│   └── Connection Failure
│
├── Backend
│   ├── 4xx
│   ├── 5xx
│   └── Rate Limit
│
├── Synchronization
│   ├── Operation Failure
│   ├── Cursor Failure
│   └── Dependency Failure
│
├── Conflict
│   └── Concurrent Changes
│
├── P2P
│   ├── Discovery
│   ├── Connection
│   ├── Authentication
│   └── Protocol
│
├── Security
│   ├── Integrity
│   ├── Trust
│   └── Revocation
│
└── Recovery
    ├── Retry
    ├── Backoff
    ├── User Action
    └── Preserve Local Data
```

### 22.110 Error Handling Invariants

The following rules are mandatory for V1:

- Error handling must never intentionally discard valid local financial data.
- Local application errors must be distinguished from synchronization errors.
- Validation errors must be distinguished from authorization errors.
- Authentication errors must be distinguished from authorization errors.
- Network errors must be distinguished from backend errors.
- Synchronization conflicts must be distinguished from ordinary failures.
- Security failures must fail closed.
- Local Domain transactions must be atomic.
- Expense creation must not leave partially persisted Expense data.
- Expense Splits must remain consistent with their Expense.
- Settlement creation must be atomic.
- Domain changes requiring synchronization must be committed atomically with their SyncOperations.
- A failed local transaction must not create a SyncOperation for an uncommitted change.
- Network failure must not prevent valid local operations from being saved.
- Backend failure must not make the local application unusable.
- P2P failure must not roll back successfully committed local changes.
- Retryable synchronization errors must remain retryable.
- Permanent synchronization errors must not be retried indefinitely.
- Retries must reuse the same Operation ID.
- Idempotency must prevent duplicate financial records.
- Timeouts must be safely retryable because the final backend result may be unknown.
- Authentication failures must trigger appropriate authentication recovery.
- Authorization failures must not be blindly retried.
- Validation failures must not be blindly retried.
- Conflict errors must create or update Conflict state.
- Conflicts must not silently discard either relevant state.
- Remote changes must be validated before local application.
- Remote changes must be applied transactionally.
- Synchronization cursors must not advance before successful local application.
- SyncOperation state must accurately represent the operation's current outcome.
- SyncState must accurately represent synchronization progress and failure.
- Pending operations must survive application restart.
- Pending operations must survive device restart.
- Recoverable synchronization state must survive process termination.
- Retry logic must use controlled backoff.
- Rate-limited requests must respect server-provided retry information where available.
- Dependency failures must not cause infinite retry loops.
- Batch synchronization must preserve per-operation results where possible.
- One failed operation must not unnecessarily discard unrelated successful operations.
- User-facing error messages must be understandable and must not expose unnecessary technical details.
- Logs must not contain sensitive credentials or unnecessary personal/financial information.
- Backend error responses must not expose internal implementation details to the User.
- P2P authentication failures must prevent unauthorized data exchange.
- P2P authorization failures must prevent unauthorized Group synchronization.
- P2P integrity failures must prevent application of tampered data.
- Database failures must not be represented as network failures.
- Synchronization unavailability must not be represented as failure to save a local Expense.
- Balance calculations must never silently persist incorrect derived financial values.
- Error recovery must prefer preserving valid local state over destructive synchronization.
- Online-to-offline transitions must preserve pending synchronization state.
- Offline-to-online transitions must automatically resume eligible synchronization.
- Error handling must support eventual recovery whenever the underlying error is temporary.
- All major error categories must be covered by automated tests.
- Error handling must preserve the local-first architecture of SplitSync.

## 23. Transaction Boundaries

### 23.1 Purpose

This section defines transaction boundaries for SplitSync V1.

Transaction boundaries determine:

- Which operations must be atomic.
- Which data changes must succeed or fail together.
- Which operations may be processed independently.
- How local Room transactions maintain consistency.
- How backend transactions maintain consistency.
- How synchronization state is updated safely.
- How failures and retries affect persisted data.

The primary principle is:

```text
A transaction must never leave the system in an invalid intermediate state.
```

### 23.2 Core Principle

SplitSync uses transactions at two primary persistence boundaries:

```text
Android
   ↓
Room / SQLite Transaction
```

and:

```text
Backend
   ↓
MySQL Transaction
```

The synchronization layer coordinates these two independent transaction systems.

A local transaction and a backend transaction are not treated as one distributed transaction.

### 23.3 No Distributed Transaction

SplitSync V1 must not depend on:

```text
Android Room Transaction
        +
Backend MySQL Transaction
```

being part of one atomic distributed transaction.

Instead:

```text
Local Transaction
      ↓
Committed Locally
      ↓
SyncOperation
      ↓
Backend Transaction
      ↓
Committed Remotely
```

This is fundamental to the offline-first architecture.

### 23.4 Local Transaction Boundary

A local transaction is responsible for maintaining consistency inside the Android database.

Example:

```text
BEGIN TRANSACTION

Expense
Expense Splits
SyncOperation

COMMIT
```

If any required operation fails:

```text
ROLLBACK
```

### 23.5 Local Expense Creation

Creating an Expense must be atomic.

```text
BEGIN TRANSACTION

Create Expense
      ↓
Create Expense Splits
      ↓
Create SyncOperation

COMMIT
```

The application must not commit only part of this operation.

### 23.6 Local Expense Update

An Expense update must include all locally required changes:

```text
BEGIN TRANSACTION

Update Expense
      ↓
Update / Replace Expense Splits
      ↓
Create SyncOperation

COMMIT
```

If the Expense update and its required Splits cannot be persisted consistently:

```text
ROLLBACK
```

### 23.7 Local Expense Deletion

If Expense deletion is supported:

```text
BEGIN TRANSACTION

Create Deletion / Tombstone
      ↓
Update Local Expense State
      ↓
Create SyncOperation

COMMIT
```

The deletion must not be committed locally without the synchronization information required to propagate it.

### 23.8 Local Settlement Creation

Settlement creation must be atomic:

```text
BEGIN TRANSACTION

Create Settlement
      ↓
Create SyncOperation

COMMIT
```

If the SyncOperation cannot be created:

```text
ROLLBACK
```

### 23.9 Local Group Creation

Group creation may include:

```text
Group
+
Initial Membership
+
SyncOperation
```

Therefore:

```text
BEGIN TRANSACTION

Create Group
      ↓
Create Owner Membership
      ↓
Create SyncOperation

COMMIT
```

### 23.10 Local Membership Creation

Membership changes should be transactional when multiple local records must change together.

Example:

```text
BEGIN TRANSACTION

Create Membership
      ↓
Create SyncOperation

COMMIT
```

### 23.11 Local Invitation Acceptance

If accepting an invitation creates a Membership and synchronization operation:

```text
BEGIN TRANSACTION

Accept Invitation
      ↓
Create Membership
      ↓
Update Invitation State
      ↓
Create SyncOperation

COMMIT
```

### 23.12 Local Profile Creation

Local User creation should be atomic with the required local identity information.

Example:

```text
BEGIN TRANSACTION

Create User
      ↓
Associate Device
      ↓
COMMIT
```

The exact Device/User relationship follows the Device Lifecycle and Authentication models.

### 23.13 Local Device Association

When a Device becomes associated with a local User:

```text
BEGIN TRANSACTION

Create / Update User
      ↓
Associate Device

COMMIT
```

### 23.14 SyncOperation Atomicity

Any local Domain change that requires synchronization must create its corresponding SyncOperation in the same transaction.

Mandatory relationship:

```text
Domain Change
      +
SyncOperation
```

must be atomic.

### 23.15 Why SyncOperation Must Be Atomic

Unsafe state:

```text
Expense Saved
      ↓
Application Crash
      ↓
SyncOperation Not Created
```

The Expense would exist locally but the system would have no durable record that it needs synchronization.

Therefore V1 requires:

```text
Expense
+
SyncOperation
```

to be committed together.

### 23.16 Local SyncOperation State Update

Updating the state of a SyncOperation should be transactional with related synchronization metadata when both must remain consistent.

Example:

```text
BEGIN TRANSACTION

SyncOperation = COMPLETED
      ↓
Update Related SyncState

COMMIT
```

### 23.17 Local Pull Transaction

Remote changes received from the backend should be applied transactionally.

Example:

```text
BEGIN TRANSACTION

Apply Remote Expense
      ↓
Apply Remote Expense Splits
      ↓
Record Applied Operation
      ↓
Update Sync Cursor

COMMIT
```

If any required part fails:

```text
ROLLBACK
```

### 23.18 Cursor Atomicity

The synchronization cursor must not advance before the corresponding remote data is successfully persisted.

Unsafe:

```text
Cursor = C500
      ↓
Apply Change C500
      ↓
Failure
```

This could cause the change to be skipped later.

Safe:

```text
BEGIN TRANSACTION

Apply C500
      ↓
Update Cursor = C500

COMMIT
```

### 23.19 Local Pull Batch Transaction

For a batch:

```text
C501
C502
C503
```

the application may process the batch transactionally:

```text
BEGIN TRANSACTION

Apply C501
Apply C502
Apply C503
Update Cursor

COMMIT
```

The exact batch size may be implementation-dependent.

### 23.20 Large Pull Batches

Large batches should not require an excessively large single transaction.

Instead:

```text
Page 1
    ↓
Transaction
    ↓
Commit

Page 2
    ↓
Transaction
    ↓
Commit
```

This reduces:

```text
Memory Usage
Lock Duration
Transaction Size
Crash Recovery Cost
```

### 23.21 P2P Receive Transaction

P2P operations must also be applied transactionally.

Example:

```text
BEGIN TRANSACTION

Validate Operation
      ↓
Apply Domain Change
      ↓
Record Applied Operation
      ↓
Update SyncState

COMMIT
```

### 23.22 P2P Expense Transaction

For a received Expense:

```text
BEGIN TRANSACTION

Create / Update Expense
      ↓
Create / Update Expense Splits
      ↓
Record Operation
      ↓
Update SyncState

COMMIT
```

### 23.23 P2P Settlement Transaction

For a received Settlement:

```text
BEGIN TRANSACTION

Create Settlement
      ↓
Record Operation
      ↓
Update SyncState

COMMIT
```

### 23.24 P2P Membership Transaction

For a received Membership operation:

```text
BEGIN TRANSACTION

Validate Membership Change
      ↓
Apply Membership
      ↓
Record Operation
      ↓
Update SyncState

COMMIT
```

### 23.25 Backend Transaction Boundary

The backend must use database transactions for Domain changes that must remain atomic.

Example:

```text
BEGIN TRANSACTION

Validate Request
      ↓
Apply Domain Change
      ↓
Record Operation
      ↓
Create Change Record

COMMIT
```

### 23.26 Backend Expense Creation

```text
BEGIN TRANSACTION

Create Expense
      ↓
Create Expense Splits
      ↓
Record Operation
      ↓
Create Change Record

COMMIT
```

If any step fails:

```text
ROLLBACK
```

### 23.27 Backend Expense Update

```text
BEGIN TRANSACTION

Validate Version
      ↓
Update Expense
      ↓
Update Expense Splits if Required
      ↓
Record Operation
      ↓
Create Change Record

COMMIT
```

### 23.28 Backend Settlement Creation

```text
BEGIN TRANSACTION

Validate Settlement
      ↓
Create Settlement
      ↓
Record Operation
      ↓
Create Change Record

COMMIT
```

### 23.29 Backend Group Creation

```text
BEGIN TRANSACTION

Create Group
      ↓
Create Initial Membership
      ↓
Record Operation
      ↓
Create Change Record

COMMIT
```

### 23.30 Backend Membership Change

Membership changes should be atomic with their corresponding operation/change records.

```text
BEGIN TRANSACTION

Validate Authorization
      ↓
Update Membership
      ↓
Record Operation
      ↓
Create Change Record

COMMIT
```

### 23.31 Backend Invitation

If an invitation and its operation/change record are persisted together:

```text
BEGIN TRANSACTION

Create Invitation
      ↓
Record Operation
      ↓
Create Change Record

COMMIT
```

### 23.32 Operation Record Atomicity

The backend must not create:

```text
Operation Record
```

without the corresponding Domain result when the operation is reported as successfully applied.

Similarly, the Domain change must not be committed without enough operation information to maintain idempotency.

Therefore:

```text
Domain Change
+
Operation Record
+
Change Record
```

should be committed within the same backend transaction where applicable.

### 23.33 Backend Idempotency Transaction

For:

```text
operationId = O100
```

the backend transaction must safely handle:

```text
First Request
```

and:

```text
Retry Request
```

Example:

```text
BEGIN TRANSACTION

Check O100
      ↓
Already Exists?
      ↓
Return Existing Result

COMMIT
```

If it does not exist:

```text
BEGIN TRANSACTION

Validate
      ↓
Apply Domain Change
      ↓
Persist O100
      ↓
Create Change Record

COMMIT
```

### 23.34 Backend Conflict Transaction

Conflict detection must occur inside the transaction that evaluates the relevant version/state.

Example:

```text
BEGIN TRANSACTION

Load Entity
      ↓
Compare Version
      ↓
Conflict?
   ┌──┴──┐
  No    Yes
   │      │
Apply   Record Conflict
   │      │
Operation Result
      ↓
COMMIT
```

### 23.35 Version Check Atomicity

The following must not be separated:

```text
Read Current Version
      ↓
Compare Base Version
      ↓
Apply Update
```

Otherwise concurrent requests may both incorrectly pass the version check.

Therefore these operations must be protected by the appropriate transaction/isolation/locking strategy.

### 23.36 Backend Concurrent Update

Example:

```text
Device A → E1 Version 5 → Version 6
Device B → E1 Version 5 → Version 6
```

The backend must ensure that concurrent processing cannot incorrectly apply both as if they were based on the same valid state.

### 23.37 Transaction Isolation

The backend transaction strategy must provide sufficient isolation for:

```text
Version Validation
Idempotency
Concurrent Updates
Membership Authorization
```

The exact isolation level may be selected during implementation based on MySQL requirements and performance.

### 23.38 Local Database Transaction Isolation

Room/SQLite transaction semantics provide the required atomicity for local operations.

The application must use transactions where multiple related records must remain consistent.

### 23.39 Transaction Scope

Transactions should be:

```text
Small
Focused
Atomic
Short-Lived
```

A transaction should contain only the operations that must succeed or fail together.

### 23.40 Do Not Hold Transactions During Network Calls

The Android application must not keep a Room transaction open while waiting for:

```text
HTTP
Internet
Backend
P2P
```

Unsafe:

```text
BEGIN ROOM TRANSACTION
      ↓
HTTP Request
      ↓
Wait
      ↓
COMMIT
```

Safe:

```text
Local Transaction
      ↓
COMMIT
      ↓
Network Request
      ↓
Backend
```

### 23.41 Do Not Hold Backend Transactions During Network Calls

The backend must not hold a MySQL transaction open while waiting for another external network service unless explicitly required by the architecture.

Prefer:

```text
Receive Request
      ↓
Validate
      ↓
Database Transaction
      ↓
Commit
      ↓
Return Response
```

### 23.42 Network Independence

Transactions must remain independent from network availability.

For example:

```text
Internet OFF
```

does not prevent:

```text
Room Transaction
```

from committing a valid local Expense.

### 23.43 Local Transaction and Backend Synchronization

The correct sequence is:

```text
User Action
      ↓
Room Transaction
      ↓
COMMIT
      ↓
SyncOperation = PENDING
      ↓
Backend Synchronization
```

Not:

```text
User Action
      ↓
Backend Request
      ↓
Room Commit
```

### 23.44 Backend Response Transaction

The backend response itself does not become part of the original local Domain transaction.

Instead:

```text
Local Transaction
      ↓
Commit
      ↓
Network Synchronization
      ↓
Synchronization Result
      ↓
Separate Local Transaction
```

### 23.45 Sync Result Update

When the backend returns:

```text
APPLIED
```

Android may perform:

```text
BEGIN TRANSACTION

SyncOperation = COMPLETED
Update SyncState

COMMIT
```

### 23.46 Sync Conflict Update

When the backend returns:

```text
CONFLICT
```

Android may perform:

```text
BEGIN TRANSACTION

SyncOperation = CONFLICT
Create / Update Conflict Record

COMMIT
```

### 23.47 Sync Rejection Update

When the backend returns:

```text
REJECTED
```

Android may perform:

```text
BEGIN TRANSACTION

SyncOperation = REJECTED
Persist Error Metadata

COMMIT
```

### 23.48 Retryable Failure Update

For a temporary failure:

```text
BEGIN TRANSACTION

SyncOperation = FAILED
Update Retry Metadata

COMMIT
```

The Domain data remains unchanged.

### 23.49 Local Domain vs Sync Metadata

The transaction boundary should distinguish:

```text
Domain Data
```

from:

```text
Synchronization Metadata
```

However, when a Domain change requires a SyncOperation to represent that change, both must be committed together during creation.

### 23.50 Derived Balance Data

Balances are derived from:

```text
Expenses
+
Expense Splits
+
Settlements
```

Therefore, V1 should preferably calculate balances from source data rather than treating a separately stored balance value as the authoritative source.

If derived balance caches are introduced, their update must follow an explicitly defined consistency strategy.

### 23.51 Expense and Balance Transaction

If the application stores a derived balance cache, the following must be considered one consistency boundary:

```text
Expense Change
+
Expense Split Change
+
Required Balance Cache Update
+
SyncOperation
```

If balances are calculated dynamically, no separate balance persistence transaction is required.

### 23.52 Settlement and Balance

Similarly:

```text
Settlement
```

affects the calculated balance.

The source-of-truth transaction is:

```text
Settlement
+
SyncOperation
```

while the balance is recalculated from persisted source data.

### 23.53 Group Deletion

If Group deletion is supported, all required related state changes must follow a defined transaction boundary.

Potentially:

```text
Group State
+
Membership State
+
Deletion/Tombstone
+
SyncOperation
```

must be handled consistently.

The exact deletion strategy depends on the final Group lifecycle.

### 23.54 User Deletion

If User deletion is supported, it must not be implemented as an uncontrolled cascade that destroys historical financial records.

The final lifecycle must define whether:

```text
User
```

is:

```text
Deactivated
Anonymized
Soft Deleted
```

while preserving required financial history.

### 23.55 Device Revocation

Device revocation may be a backend security transaction:

```text
BEGIN TRANSACTION

Mark Device Revoked
      ↓
Update Security State

COMMIT
```

Subsequent synchronization attempts are rejected.

### 23.56 Transaction Boundary for Authentication

Authentication transactions should remain separate from Domain transactions.

Example:

```text
Authentication
      ↓
Session Established
```

does not need to be part of:

```text
Expense Transaction
```

### 23.57 Transaction Boundary for Authorization

Authorization checks that affect whether a Domain operation may proceed must occur within the appropriate consistency boundary.

On the backend:

```text
Load Relevant Authorization State
      ↓
Validate Permission
      ↓
Apply Domain Change
```

must be protected against concurrent membership changes where required.

### 23.58 Membership Authorization Race

Example:

```text
Device A:
Member of G1

Device B:
Removes A from G1
```

If A simultaneously attempts:

```text
Create Expense in G1
```

the backend must use an appropriate transaction/locking strategy so that authorization is evaluated consistently with the current membership state.

### 23.59 P2P Authorization Race

P2P authorization is more complex because peers may be temporarily disconnected from the backend.

The P2P protocol must rely on the locally available authorized membership state and security mechanism defined for offline synchronization.

Any resulting membership conflict must follow the Conflict Resolution model.

### 23.60 P2P Transaction Boundary

The P2P communication itself is not a database transaction.

Instead:

```text
P2P Message
      ↓
Validate
      ↓
Local Transaction
      ↓
Commit
      ↓
Acknowledgement
```

### 23.61 Acknowledgement After Commit

A P2P device should not acknowledge:

```text
APPLIED
```

before the corresponding local transaction has successfully committed.

Correct:

```text
Receive
   ↓
Validate
   ↓
Commit
   ↓
ACK = APPLIED
```

### 23.62 Backend Response After Commit

Similarly, the backend should return:

```text
APPLIED
```

only after the relevant database transaction has committed successfully.

Correct:

```text
BEGIN
 ↓
Apply
 ↓
Commit
 ↓
HTTP Response = APPLIED
```

### 23.63 Crash Before Acknowledgement

If the receiving device commits:

```text
O100
```

but crashes before sending the acknowledgement:

```text
Sender
    ↓
No ACK
```

the sender retries:

```text
O100
```

The receiver recognizes:

```text
O100 = ALREADY_APPLIED
```

This depends on persistent operation identity.

### 23.64 Crash Before Backend Response

If the backend commits:

```text
O100
```

but the response is lost:

```text
Android = uncertain
```

Android retries:

```text
O100
```

The backend returns:

```text
ALREADY_APPLIED
```

### 23.65 Transaction and Idempotency

Transactions guarantee:

```text
Atomicity
```

Idempotency guarantees safe:

```text
Retry
```

Both are required.

```text
Transaction
    +
Idempotency
    ↓
Reliable Synchronization
```

### 23.66 Transaction and Conflict Resolution

Transactions ensure that conflict detection and state updates occur consistently.

They do not determine which conflicting state is correct.

That decision belongs to:

```text
Conflict Resolution
```

### 23.67 Transaction and Eventual Consistency

Transaction boundaries provide:

```text
Strong Local Consistency
```

while synchronization provides:

```text
Eventual Cross-Device Consistency
```

These are complementary.

### 23.68 Transaction and Offline Mode

Offline operations use the same local transaction boundaries as online operations.

There must not be two separate Domain implementations:

```text
Offline Expense Logic
Online Expense Logic
```

Instead:

```text
Same Domain Logic
+
Same Room Transaction
```

### 23.69 Transaction and Online Mode

When online:

```text
Local Transaction
      ↓
SyncOperation
      ↓
Backend Transaction
```

When offline:

```text
Local Transaction
      ↓
SyncOperation = PENDING
```

The difference is only when synchronization occurs.

### 23.70 Transaction and P2P Mode

When P2P is used:

```text
Local Transaction
      ↓
SyncOperation
      ↓
P2P Transfer
      ↓
Remote Local Transaction
```

### 23.71 Transaction and Backend Mode

When Backend synchronization is used:

```text
Local Transaction
      ↓
SyncOperation
      ↓
Backend Transaction
      ↓
Remote Device Pull
```

### 23.72 Transaction Boundary for Complete Expense Lifecycle

```text
CREATE

BEGIN
 ├── Expense
 ├── Expense Splits
 └── SyncOperation
COMMIT
```

```text
UPDATE

BEGIN
 ├── Expense
 ├── Expense Splits
 └── SyncOperation
COMMIT
```

```text
DELETE

BEGIN
 ├── Tombstone / Delete State
 └── SyncOperation
COMMIT
```

### 23.73 Transaction Boundary for Complete Settlement Lifecycle

```text
CREATE

BEGIN
 ├── Settlement
 └── SyncOperation
COMMIT
```

```text
UPDATE

BEGIN
 ├── Settlement
 └── SyncOperation
COMMIT
```

```text
DELETE

BEGIN
 ├── Tombstone / Delete State
 └── SyncOperation
COMMIT
```

where the corresponding lifecycle operations are supported.

### 23.74 Transaction Boundary for Complete Group Lifecycle

```text
CREATE

BEGIN
 ├── Group
 ├── Owner Membership
 └── SyncOperation
COMMIT
```

Other Group lifecycle operations should follow the same principle.

### 23.75 Transaction Boundary for Synchronization

```text
PUSH

Local:
BEGIN
 └── Update SyncOperation
COMMIT

Network
 ↓

Backend:
BEGIN
 ├── Apply Domain Change
 ├── Operation Record
 └── Change Record
COMMIT
```

### 23.76 Pull Transaction

```text
Backend
   ↓
Remote Changes
   ↓
Android

BEGIN
 ├── Apply Changes
 ├── Record Applied Operations
 └── Update Cursor
COMMIT
```

### 23.77 P2P Transaction

```text
Peer A
   ↓
Operation
   ↓
Peer B

BEGIN
 ├── Validate
 ├── Apply Domain Change
 ├── Record Operation
 └── Update SyncState
COMMIT

ACK
```

### 23.78 Transaction Failure

If any required operation inside a transaction fails:

```text
ROLLBACK
```

No partial Domain state should remain.

### 23.79 Transaction Rollback Does Not Mean Global Rollback

A local rollback does not roll back:

```text
Backend
```

or:

```text
Peer Device
```

and vice versa.

The architecture deliberately avoids distributed transactions.

### 23.80 Compensating Operations

When a previously committed operation must be corrected after synchronization, the system should prefer a new valid Domain operation where appropriate rather than attempting to perform a distributed rollback.

Example:

```text
Original Expense
      ↓
Committed
      ↓
Later Correction
      ↓
New Update Operation
```

This follows the synchronization model.

### 23.81 Transaction and Auditability

Important Domain changes should preserve the operation identity needed to understand:

```text
What changed?
Who originated it?
Which Device originated it?
When was it created?
Was it synchronized?
```

The exact audit model is defined elsewhere.

### 23.82 Transaction and Operation Ordering

Transactions do not automatically determine cross-device ordering.

Cross-device ordering is handled through:

```text
Operation IDs
Versions
Change Sequences
SyncState
```

### 23.83 Transaction and Versioning

When an entity is updated:

```text
Current Version
      ↓
Validate Base Version
      ↓
Apply Update
      ↓
Increment Version
```

These steps must occur consistently within the relevant backend transaction.

### 23.84 Transaction and Concurrent Operations

Concurrent operations must be handled using:

```text
Database Transactions
+
Appropriate Isolation
+
Version Checks
+
Conflict Resolution
```

rather than relying solely on application-level timing.

### 23.85 Transaction and Database Constraints

Database constraints should provide an additional integrity layer.

Examples:

```text
Foreign Keys
Unique Operation IDs
Required Fields
Valid References
```

Application validation should still exist before database persistence.

### 23.86 Transaction and Unique Operation ID

The backend should enforce uniqueness for:

```text
operationId
```

where required.

This provides an additional protection against duplicate operations.

### 23.87 Transaction and Unique Entity Identity

Entity identifiers such as:

```text
groupId
expenseId
settlementId
```

must remain stable across synchronization.

Transactions must not generate new identities merely because an operation is retried.

### 23.88 Transaction and Local IDs

Offline-created entities must retain their locally generated IDs when synchronized to the backend.

Example:

```text
Offline:
expenseId = E100
```

After synchronization:

```text
Backend:
expenseId = E100
```

A new ID must not be generated solely because the record crossed a synchronization boundary.

### 23.89 Transaction and Referential Integrity

When multiple related records are created:

```text
Group
    ↓
Expense
    ↓
Expense Split
```

their creation order and transaction boundaries must maintain valid references.

### 23.90 Parent-Child Transaction

For a new Expense:

```text
Expense
   ├── ExpenseSplit 1
   ├── ExpenseSplit 2
   └── ExpenseSplit 3
```

the required parent and child records should be created atomically.

### 23.91 Independent Operations

Not every operation must share one transaction.

Example:

```text
Expense E1
```

and:

```text
Expense E2
```

may be committed independently.

This prevents an unrelated failure in E2 from rolling back E1.

### 23.92 Batch Transaction Strategy

The application may group operations into batches for synchronization, but Domain operations must not become incorrectly coupled.

Example:

```text
O100 → Expense E1
O101 → Expense E2
O102 → Expense E3
```

If E2 fails validation, E1 and E3 should not necessarily be treated as invalid.

### 23.93 Batch vs Domain Transaction

Important distinction:

```text
Synchronization Batch
        ≠
One Domain Transaction
```

A batch is a transport mechanism.

Transaction boundaries are determined by Domain consistency requirements.

### 23.94 Backend Batch Processing

The backend may receive multiple operations in one request.

However, it must define whether:

```text
Entire Batch
```

or:

```text
Each Operation
```

is the transaction boundary.

For V1, independent Domain operations should preferably be processed with independent logical results unless a dependency requires atomic processing.

### 23.95 Dependency Batch

If operations are tightly dependent:

```text
Create Group
+
Create Owner Membership
```

they may be processed within the same backend transaction.

### 23.96 Independent Batch

For unrelated expenses:

```text
Expense E1
Expense E2
Expense E3
```

failure of E2 should not unnecessarily roll back E1 and E3.

### 23.97 Transaction and Retry

A failed transaction:

```text
ROLLBACK
```

may be safely retried if the operation is retryable.

The same:

```text
operationId
```

must be reused.

### 23.98 Transaction and Permanent Rejection

If validation fails:

```text
ROLLBACK
```

and:

```text
Operation = REJECTED
```

The invalid Domain state must not be committed.

### 23.99 Transaction and Conflict

If a conflict is detected:

```text
BEGIN
 ↓
Detect Conflict
 ↓
Persist Conflict State / Result
 ↓
COMMIT
```

The conflicting update must not be incorrectly applied as a normal successful update.

### 23.100 Transaction and Security Failure

If authorization or integrity verification fails:

```text
ROLLBACK / Do Not Begin Domain Mutation
```

No unauthorized Domain change should be committed.

### 23.101 Transaction and Error Recovery

The general recovery model is:

```text
Transaction Start
      ↓
Validation
      ↓
Mutation
      ↓
Success?
   ┌──┴──┐
  Yes    No
   │      │
Commit   Rollback
   │      │
Continue Classify Error
```

### 23.102 Transaction and Application Crash

If the application crashes during a local transaction:

```text
Transaction
      ↓
Crash
```

Room/SQLite transaction semantics must ensure that the database does not remain in a partially committed transaction state.

### 23.103 Transaction and Backend Crash

If the backend crashes during a MySQL transaction:

```text
Transaction
      ↓
Crash
```

the database transaction must either:

```text
Commit
```

or:

```text
Rollback
```

according to database transaction semantics.

The client must safely retry uncertain operations using idempotency.

### 23.104 Transaction and P2P Crash

If a peer crashes during synchronization:

```text
Remote Transaction
      ↓
Crash
```

the remote device must recover its local database safely.

The sender can retry the operation using:

```text
operationId
```

### 23.105 Transaction and Acknowledgement

Acknowledgements must represent committed state.

```text
COMMIT
   ↓
ACK
```

not:

```text
ACK
   ↓
COMMIT
```

### 23.106 Transaction and SyncState

SyncState must never indicate:

```text
SYNCED
```

when the corresponding Domain changes have not been successfully committed.

### 23.107 Transaction and Cursor

The cursor must never indicate:

```text
C500
```

if C500 has not been successfully applied locally.

### 23.108 Transaction and Operation State

Similarly:

```text
SyncOperation = COMPLETED
```

must only be persisted when the corresponding synchronization result is safely accepted.

### 23.109 Transaction and Local Source of Truth

The local source of truth remains:

```text
Room Database
```

after a successful local transaction.

Derived UI state should be regenerated from the committed database state.

### 23.110 Transaction and UI

The UI must not display a successful state before the underlying local transaction has committed.

Correct:

```text
Room Commit
    ↓
Observe Updated Data
    ↓
UI Updates
```

### 23.111 Transaction and Repository Layer

Repository methods should define clear transaction boundaries.

For example:

```text
ExpenseRepository.createExpense()
```

may internally perform:

```text
Expense
+
Splits
+
SyncOperation
```

within one transaction.

### 23.112 Transaction and Domain Layer

The Domain layer defines what must be logically atomic.

The persistence layer implements the required transaction.

Example:

```text
Create Expense
```

is a Domain operation.

The Room transaction provides its persistence atomicity.

### 23.113 Transaction and Network Layer

The Network layer must not control local Domain transactions.

Instead:

```text
Domain
 ↓
Local Repository
 ↓
Room Transaction
```

and:

```text
Sync Engine
 ↓
Network
```

remain separate.

### 23.114 Transaction and Sync Engine

The Sync Engine coordinates:

```text
Local Transaction
+
Network Communication
+
Remote Result
+
Sync State
```

but does not turn them into one distributed transaction.

### 23.115 Transaction and Backend Service Layer

The Backend Service layer should define Domain operations, while the Repository/Data layer manages database transaction boundaries.

Conceptually:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
MySQL Transaction
```

### 23.116 Transaction and REST API

A REST request may represent:

```text
One Operation
```

or:

```text
A Synchronization Batch
```

but the transaction boundary must be explicitly defined rather than assuming:

```text
One HTTP Request = One Database Transaction
```

### 23.117 Transaction and HTTP Failure

HTTP response failure does not necessarily mean the backend transaction failed.

Example:

```text
Backend Commit
      ↓
Network Failure
      ↓
Client Timeout
```

The client must treat the result as uncertain and retry safely.

### 23.118 Transaction and Eventual Consistency

Transactions provide consistency within each persistence boundary:

```text
Device A Local DB
Backend DB
Device B Local DB
```

Synchronization connects these boundaries over time.

### 23.119 Transaction and Offline-First Principle

The transaction architecture directly supports:

```text
Local First
```

because local persistence does not wait for remote persistence.

### 23.120 Transaction Boundary Summary

```text
Transaction Boundaries
│
├── Android / Room
│   ├── Group
│   ├── Membership
│   ├── Expense
│   ├── Expense Split
│   ├── Settlement
│   ├── SyncOperation
│   └── SyncState
│
├── Backend / MySQL
│   ├── Group
│   ├── Membership
│   ├── Expense
│   ├── Expense Split
│   ├── Settlement
│   ├── Operation Record
│   └── Change Record
│
├── Synchronization
│   ├── Push Result
│   ├── Pull Changes
│   ├── Cursor
│   └── Conflict
│
└── P2P
    ├── Receive
    ├── Validate
    ├── Apply
    ├── SyncState
    └── Acknowledge
```

### 23.121 Complete Transaction Flow

```text
USER ACTION
    ↓
DOMAIN VALIDATION
    ↓
LOCAL ROOM TRANSACTION
    ├── Domain Entity
    ├── Related Entities
    └── SyncOperation
    ↓
COMMIT
    ↓
LOCAL STATE AVAILABLE
    ↓
SYNCHRONIZATION
    ↓
BACKEND / P2P
    ↓
REMOTE VALIDATION
    ↓
REMOTE TRANSACTION
    ├── Domain Entity
    ├── Operation Record
    └── Change Record
    ↓
COMMIT
    ↓
SYNC RESULT
    ↓
LOCAL SYNC TRANSACTION
    ├── Operation State
    └── SyncState
    ↓
COMMIT
```

### 23.122 Transaction Boundary Invariants

The following rules are mandatory for V1:

- Every transaction must preserve Domain consistency.
- Local Domain changes must be persisted transactionally where multiple records must remain consistent.
- Backend Domain changes must be persisted transactionally where multiple records must remain consistent.
- An Expense and its required Expense Splits must be created atomically.
- An Expense and its required SyncOperation must be created atomically.
- A Settlement and its required SyncOperation must be created atomically.
- A Group and its initial required Membership and SyncOperation must be created atomically.
- Membership changes and their required SyncOperations must be created atomically.
- Remote changes must be applied transactionally.
- P2P received changes must be applied transactionally.
- SyncOperation state updates must be persisted safely.
- SyncState updates must be persisted safely.
- Remote changes and their corresponding cursor advancement must be committed atomically.
- The synchronization cursor must never advance before the corresponding data is safely persisted.
- A synchronization acknowledgement must only indicate successfully committed state.
- The backend must only return a successful operation result after the relevant transaction has committed.
- A local transaction must not remain open while waiting for a network request.
- A backend database transaction must not unnecessarily remain open while waiting for external network communication.
- Local persistence must not depend on Internet connectivity.
- Online and offline Domain operations must use the same local transaction rules.
- Room transactions and MySQL transactions are separate persistence boundaries.
- SplitSync V1 must not depend on distributed transactions between Android and Backend.
- Local commit must occur before remote synchronization.
- Remote synchronization failure must not roll back an already committed local Domain transaction.
- Remote synchronization failure must not delete valid local data.
- Retry must reuse the same Operation ID.
- Transactions and idempotency must work together to prevent duplicate financial records.
- Backend version checks and updates must be protected against concurrent modification.
- Authorization checks must be consistent with the transaction that applies the protected Domain change.
- Database constraints must provide an additional integrity layer.
- Parent-child records must maintain referential integrity.
- Independent Domain operations should not be unnecessarily coupled into one transaction.
- Synchronization batches must not automatically imply one large Domain transaction.
- Batch transport and transaction boundaries must remain conceptually separate.
- A failed operation must not unnecessarily roll back unrelated successful operations.
- A transaction failure must result in rollback of the affected atomic unit.
- A retryable failure must preserve enough state for safe retry.
- A permanent failure must not create an invalid committed Domain state.
- A conflict must not be treated as a successful normal update.
- Conflict state must be persisted according to the Conflict Data Model.
- A crash during a transaction must not leave a partially committed atomic unit.
- A crash after backend commit but before client acknowledgement must be recoverable through idempotency.
- A crash after P2P commit but before acknowledgement must be recoverable through idempotency.
- UI success state must be based on committed local data.
- Derived balances must be based on committed source data.
- The transaction architecture must preserve the local-first and offline-first principles of SplitSync.

24. API Design Principles

25. Versioning Strategy

26. Package Naming

27. Configuration Strategy

28. Environment Strategy

29. Logging Strategy

30. Testing Structure