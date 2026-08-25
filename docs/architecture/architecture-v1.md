# SplitSync — Architecture v1

## 1. Product Vision

SplitSync is a general-purpose, offline-first group expense management application designed to help people record, split, track, and settle shared expenses reliably regardless of network availability.

Users can create a local profile, create or join groups, add and manage expenses, calculate balances, and record settlements without requiring an internet connection.

SplitSync is designed to provide a complete experience in both online and offline environments. The application automatically adapts its synchronization behavior based on the connectivity available to the device. Users are not required to manually switch between online and offline modes.

When internet connectivity is available, SplitSync synchronizes local changes with the backend server and authorized devices in the background. When internet connectivity is unavailable, the application continues to operate using locally stored data and queues changes for synchronization when connectivity is restored.

Where supported local connectivity is available, such as a common Wi-Fi network or mobile hotspot, SplitSync may also allow authorized devices to exchange relevant group data without requiring internet access.

A group is the core business concept of the application. A group can represent a trip, family, household, friends, office, event, or any other shared-expense context. A trip is treated as a specialized type of group rather than as a separate expense-management model.

The primary design principle of SplitSync is:

> Local first, sync automatically.

The local device database is the immediate source of truth for the application. Network connectivity is used to synchronize and back up local changes, but network availability must never prevent users from performing core expense-management operations.

SplitSync must provide a consistent and reliable user experience regardless of whether the device has internet connectivity, local network connectivity, or no network connectivity at all.

## 2. Goals

### 2.1 Core Product Goals

- Allow users to create a local profile without requiring an internet connection.
- Allow users to create, join, and manage shared expense groups.
- Support different group contexts such as trips, households, families, friends, offices, and events.
- Allow group members to record expenses and specify who participated in each expense.
- Support multiple expense-splitting methods including equal, exact amount, percentage, and shares.
- Automatically calculate individual balances based on expenses and splits.
- Allow users to record and track settlements between group members.
- Provide a clear overview of total group expenses, individual contributions, shares, balances, and settlements.
- Preserve the complete expense history of a group.
- Provide the same core expense-management capabilities regardless of network availability.

### 2.2 Connectivity and Reliability Goals

- The application must provide a complete and reliable experience both when internet connectivity is available and when it is unavailable.
- The application must automatically detect the available connectivity state and adapt its synchronization behavior without requiring the user to manually switch between online and offline modes.
- The user must not be required to choose between an online mode and an offline mode for normal application usage.
- The local device database must remain the immediate source of truth for the Android application regardless of network availability.
- All core user actions must first be persisted locally before synchronization is attempted.
- When internet connectivity is available, local changes must be synchronized with the backend automatically in the background.
- When internet connectivity is unavailable, local changes must be stored safely and queued for later synchronization.
- Restoring internet connectivity must automatically resume synchronization of pending local changes.
- The application must remain fully usable for its core functionality while synchronization is pending.
- Synchronization must be reliable and idempotent so that the same change cannot create duplicate data.
- Synchronization failures must not prevent users from continuing to use the application locally.
- Locally created or modified data must persist across application restarts.
- Network unavailability or temporary synchronization failures must not result in loss of locally stored data.
- The application must eventually converge toward a consistent state across authorized devices after successful synchronization.
- Internet connectivity and local device-to-device connectivity must be treated as separate capabilities.

### 2.3 Multi-User and Local Communication Goals

- Allow nearby SplitSync users to discover each other over a supported local network without requiring internet connectivity.
- Allow users to exchange the minimum profile information required for identification during local discovery.
- Allow authorized users to establish group membership and exchange relevant group data over a supported local connection without requiring internet connectivity.
- Protect group data so that it is exchanged only between authorized users and devices.
- Support local synchronization between devices when internet connectivity is unavailable but a supported local network is available.
- Ensure that local device-to-device synchronization is an additional synchronization mechanism and does not become a prerequisite for core expense-management functionality.
- Design the synchronization architecture to support groups ranging from a few members to large groups.

### 2.4 Technical Goals

- Use Java for Android application development.
- Use Room with SQLite as the Android local persistence layer.
- Use Java with Spring Boot for the backend.
- Use Hibernate/JPA for backend persistence.
- Use MySQL as the backend database.
- Expose backend functionality through versioned REST APIs.
- Use a dedicated synchronization layer between local device data and remote backend data.
- Keep the Android application functional without requiring direct access to backend services.
- Keep the local data model independent from network availability.
- Maintain clear separation between the domain model, local persistence, network communication, synchronization, and presentation layers.
- Keep the architecture modular so that future features can be added without redesigning the core expense domain.
- Design the synchronization architecture to support both remote server synchronization and future local peer-to-peer synchronization.
- Ensure that online synchronization operates in the background without blocking normal user interactions.
- Ensure that the application can recover safely from temporary network failures, application restarts, and interrupted synchronization operations.
## 3. Non-Goals

The following capabilities are intentionally outside the scope of the initial SplitSync architecture and implementation. They may be considered as future extensions, but they must not introduce unnecessary complexity into the core architecture unless explicitly added through a future architecture revision.

### 3.1 Product and Feature Non-Goals

- SplitSync will not initially function as a banking or financial-account management application.
- SplitSync will not directly process, initiate, or settle real monetary transactions such as UPI, bank transfers, credit-card payments, or wallet payments.
- SplitSync will not require integration with banking or payment-provider APIs for its core expense-management functionality.
- SplitSync will not initially provide real-time chat or messaging between group members.
- SplitSync will not initially provide social-networking or public-profile functionality.
- SplitSync will not initially provide a marketplace, rewards system, or advertising platform.
- SplitSync will not initially provide automated financial advice or investment-related functionality.
- SplitSync will not initially depend on AI or machine-learning services for core expense management.
- SplitSync will not initially provide advanced accounting, taxation, invoicing, or business-bookkeeping functionality.

### 3.2 Connectivity Non-Goals

- Internet connectivity will not be a prerequisite for core application functionality.
- The application will not require the user to manually select an online or offline mode.
- The application will not assume that internet connectivity and local network connectivity are the same capability.
- Local peer-to-peer synchronization will not be required for the application to function correctly.
- Temporary loss of internet or local connectivity will not prevent users from continuing to work with locally available data.

### 3.3 Architecture and Infrastructure Non-Goals

- The initial backend will not use a microservices architecture.
- The initial backend will be implemented as a modular monolith.
- Kubernetes, service meshes, and other container-orchestration infrastructure are outside the scope of the initial implementation.
- The initial architecture will not introduce distributed infrastructure unless a specific scalability or reliability requirement justifies it.
- The initial implementation will not require Redis, Kafka, RabbitMQ, or other distributed messaging infrastructure.
- The application will not depend on third-party cloud services for its core offline functionality.
- Cloud infrastructure and deployment automation will be introduced only when required by the backend deployment strategy.

### 3.4 Multi-Device Non-Goals

- V1 will support one active local user profile per device.
- Multiple independent local user profiles on the same device will not be supported in V1.
- Full multi-device account management for the same user will not be part of the initial offline experience.
- Cloud-based multi-device synchronization for a user may be supported later without changing the fundamental local identity model.

### 3.5 Data and Synchronization Non-Goals

- The application will not assume that the backend is always available.
- The backend will not be treated as the only source of truth for immediate local user interactions.
- Synchronization will not block normal expense-management operations.
- The application will not silently discard local changes because of synchronization conflicts or temporary network failures.
- The initial implementation will not attempt to synchronize arbitrary application data that is outside the defined domain model.
- Complex multi-user conflict resolution beyond the rules defined by the synchronization architecture will not be introduced without an explicit architecture decision.

### 3.6 Scope Control

Any capability listed as a non-goal may be considered in a future release. Adding such a capability must not implicitly change the architecture of the current version.

Major changes to the system's core behavior, data model, synchronization model, or infrastructure must be evaluated as part of a new architecture revision.

## 4. Design Principles

### 4.1 Local First

The local device database is the immediate source of truth for the Android application.

Every user action that changes application data must be persisted locally before any attempt is made to synchronize that change with a remote server or another device.

The application must remain usable even when no network connectivity is available.

### 4.2 Connectivity-Aware and Automatic Synchronization

The application must automatically detect available connectivity and select the appropriate synchronization mechanism.

Users must not be required to manually switch between online and offline modes.

When internet connectivity is available, pending local changes should be synchronized with the backend automatically.

When internet connectivity is unavailable, changes must remain safely stored locally and queued for later synchronization.

Local network connectivity and internet connectivity must be treated as separate capabilities.

### 4.3 Local Data Must Never Depend on Network Availability

Reading and modifying locally available data must not require a network request.

A temporary network failure, backend outage, timeout, or synchronization failure must not prevent users from viewing or modifying locally available group data.

### 4.4 Eventual Synchronization

Local changes may temporarily exist only on one device.

The synchronization architecture must ensure that, once authorized devices can communicate successfully, pending changes can be exchanged and eventually reflected across the relevant devices.

Successful synchronization should result in a consistent state across authorized devices.

### 4.5 Idempotent Synchronization

Synchronization operations must be idempotent.

Processing the same synchronization event more than once must not create duplicate entities, duplicate expenses, duplicate settlements, or other unintended side effects.

Every synchronizable operation must have a unique identifier that can be used for deduplication.

### 4.6 Deterministic Conflict Resolution

Multiple devices may modify related data while disconnected.

The synchronization architecture must define deterministic rules for resolving conflicts so that different devices do not permanently maintain incompatible states after synchronization.

Conflict resolution rules must be explicit, predictable, and testable.

### 4.7 Immutable Synchronization Identity

Synchronizable entities and synchronization operations must use stable unique identifiers that are independent of network availability.

Locally generated identifiers must remain valid when the data is later synchronized with the backend.

Synchronization must not require replacing local identifiers with server-generated identifiers.

### 4.8 Separation of Concerns

The application must maintain clear separation between:

- Presentation/UI
- Domain/business logic
- Local persistence
- Network communication
- Synchronization
- Backend services
- Remote persistence

The UI must not directly access the database or network layer.

The expense calculation logic must not depend on whether the application is online or offline.

### 4.9 Single Business Logic

Core business rules, especially expense splitting, balance calculation, and settlement calculation, must behave consistently regardless of connectivity.

Online and offline execution must not produce different results for the same underlying data.

### 4.10 Data Integrity

Application data must maintain referential and business-level integrity both locally and on the backend.

Invalid expenses, invalid splits, inconsistent amounts, unauthorized group changes, and invalid settlement operations must be rejected according to defined business rules.

### 4.11 Security by Design

Security must be considered at every layer rather than added after implementation.

Authentication, authorization, secure communication, local data protection, group membership validation, and synchronization authorization must be explicitly defined.

Only authorized users and devices should be able to access protected group data.

### 4.12 Privacy by Default

Only the minimum information required for a particular operation should be exposed to other users or devices.

Local device discovery must not unnecessarily broadcast private user information.

Sensitive information such as authentication credentials, internal identifiers, synchronization metadata, and protected group data must not be exposed through unrestricted local discovery.

### 4.13 Offline Persistence and Recovery

Locally stored data must survive application restarts and temporary failures.

Synchronization must be recoverable after:

- Application restart
- Device restart
- Temporary network loss
- Backend unavailability
- Interrupted synchronization
- Partial synchronization failure

The application must be able to resume synchronization without requiring the user to recreate or re-enter previously saved data.

### 4.14 API Versioning

Backend APIs must be versioned from the beginning.

The initial API namespace should use:

`/api/v1/`

Future breaking changes should be introduced through a new API version rather than silently changing the behavior of an existing version.

### 4.15 Modular Architecture

The system should be divided into well-defined functional modules with clear responsibilities.

The initial backend will use a modular monolith architecture rather than microservices.

Modules should communicate through well-defined interfaces and should avoid unnecessary coupling.

### 4.16 Testability

Every critical business rule and synchronization behavior must be independently testable.

Particular attention must be given to:

- Expense calculations
- Balance calculations
- Settlement calculations
- Offline operations
- Synchronization
- Duplicate event handling
- Conflict resolution
- Recovery after interrupted synchronization

### 4.17 Progressive Complexity

The architecture must remain as simple as possible while satisfying the application's actual requirements.

Advanced infrastructure or technologies should only be introduced when justified by a concrete functional, scalability, reliability, or security requirement.

The system should evolve incrementally rather than introducing unnecessary complexity in the initial implementation.

## 5. User Model

### 5.1 User Identity

A User represents a person using SplitSync.

A user is identified by a stable, locally generated `userId`. The `userId` is independent of internet connectivity and must remain stable throughout the lifetime of the local user profile.

The `userId` must be generated using UUID and must not depend on the backend server.

Phone numbers, email addresses, or display names must not be used as the primary technical identity of a user.

The local user identity must remain valid even if the user never creates an online account.

### 5.2 Local Profile Creation

The first launch of SplitSync must allow the user to create a local profile without requiring internet connectivity.

The minimum required information for creating a local profile is:

- Display name

The following information is optional:

- Phone number
- Email address
- Profile photo

After profile creation, the application must generate a unique `userId` locally and persist the profile in the local database.

The application must be immediately usable after local profile creation without requiring account registration or network connectivity.

### 5.3 User Profile

The initial user profile consists of:

- `userId`
- `displayName`
- `phoneNumber` (optional)
- `email` (optional)
- `profilePhoto` (optional)
- `createdAt`
- `updatedAt`

The profile must be persisted locally and must remain available when the device is offline.

### 5.4 Profile Photo

SplitSync must support profile photos while operating offline.

Profile images should be stored in the device's local application storage rather than directly as large binary objects inside the primary user database record.

The local database should maintain a reference to the locally stored profile image.

Profile images may be synchronized between authorized devices as part of a future synchronization implementation.

### 5.5 Phone Number and Email

Phone numbers and email addresses are optional identification and contact attributes.

They must not be used as the primary identity of a user because they may be changed, unavailable, or impossible to verify while offline.

Phone number and email may later be used for:

- Online account creation
- Account linking
- Account recovery
- Online invitations
- Additional user identification

Any verification of phone numbers or email addresses that requires a remote service must remain optional for local/offline profile creation.

### 5.6 One User per Device in V1

Version 1 supports one active local user profile per application installation/device.

Multiple independent local profiles on the same device are outside the scope of V1.

The architecture must nevertheless keep user identity separate from device identity so that support for multiple devices per user can be introduced in the future.

### 5.7 Local User Discovery

A user's minimal profile information may be made discoverable to nearby SplitSync devices over a supported local network.

Local discovery must expose only the minimum information required to identify the user.

The default discoverable information should be limited to:

- `userId`
- `displayName`
- Profile photo reference or appropriate local avatar representation

Phone number, email address, group data, expenses, balances, and synchronization metadata must not be broadcast through unrestricted local discovery.

Additional profile information may be exchanged only after an appropriate authorization or group interaction.

### 5.8 Online Account Linking

Creating an online account is optional for the local user.

A user may continue using SplitSync entirely with a local identity.

If the user later creates or links an online account, the existing local `userId` should remain associated with the user's local data wherever technically possible.

Account linking must not require replacing existing local expense, group, or synchronization identities.

### 5.9 User Identity Lifecycle

The local user profile follows this lifecycle:

1. Application is installed.
2. User creates a local profile.
3. A `userId` is generated locally.
4. The profile is persisted in the local database.
5. The user can immediately create or join groups and manage expenses.
6. The user may optionally connect to an online account later.
7. Existing local data remains associated with the same local user identity.

### 5.10 User Model Invariants

The following invariants must always hold:

- Every local user must have exactly one stable `userId`.
- A `userId` must be unique within the SplitSync ecosystem.
- `userId` generation must not require network connectivity.
- A user's display name, phone number, email, or profile photo may change without changing the `userId`.
- User profile changes must be persisted locally before synchronization.
- Removing or changing optional profile information must not invalidate existing expenses, groups, settlements, or synchronization records that reference the user.


## 6. Device Model

### 6.1 Device Identity

A Device represents a specific installation of SplitSync on a physical device.

Each SplitSync installation must have a stable, locally generated `deviceId`.

The `deviceId` must be generated locally using UUID and must not depend on internet connectivity or the backend server.

The `deviceId` must remain stable for the lifetime of the application installation unless the application is intentionally reset or its local data is explicitly removed.

A device must always be associated with one active local user in V1.

### 6.2 User-to-Device Relationship

The device identity must be separate from the user identity.

A User represents a person, while a Device represents a specific application installation.

The relationship must support the following model:

User
→ one or more Devices

Although V1 supports only one active local user per device, the data model must not assume that a user can have only one device.

This allows future support for the same user accessing SplitSync from multiple devices.

### 6.3 Device Information

The local device record should contain information required for identification, synchronization, and local communication.

The initial device model consists of:

- `deviceId`
- `userId`
- `deviceName`
- `deviceType`
- `appVersion`
- `protocolVersion`
- `createdAt`
- `updatedAt`
- `lastSeenAt`
- `lastSyncAt`

Internal synchronization metadata may be added as required by the synchronization architecture.

### 6.4 Device ID

The `deviceId` must:

- Be generated locally.
- Be globally unique.
- Not depend on the device's IMEI, phone number, MAC address, Android hardware identifier, or other hardware-specific identifier.
- Remain stable across normal application usage.
- Be included in synchronization metadata where required.
- Allow the synchronization system to distinguish changes originating from different installations.

Hardware identifiers must not be used as the primary device identity because of Android privacy restrictions, hardware changes, application reinstallations, and platform-specific behavior.

### 6.5 Device Registration

When SplitSync is installed for the first time:

1. A local user profile is created.
2. A unique `userId` is generated.
3. A unique `deviceId` is generated.
4. The user and device records are persisted locally.
5. The application becomes immediately usable.

No backend registration is required for the initial local setup.

When internet connectivity later becomes available, the device may be registered with the backend as part of account linking or synchronization.

### 6.6 Device Lifecycle

A device follows this general lifecycle:

```text
Application Installed
        ↓
Device ID Generated
        ↓
Local User Created
        ↓
Device Associated With User
        ↓
Application Used Offline/Online
        ↓
Optional Synchronization
        ↓
Optional Backend Registration
```

## 7. Group Model

### 7.1 Group as the Core Business Concept

A Group is the central business entity in SplitSync. It represents a collection of users who share and manage expenses together.

A group may represent:

- A trip
- A family
- A household
- A group of friends
- An office or team
- An event
- Any other shared-expense context

The core expense-management behavior must remain independent of the purpose of the group.

A Trip is treated as a type of Group rather than as a separate expense-management model.

### 7.2 Group Identity

Every group must have a stable, locally generated `groupId`.

The `groupId` must:

- Be generated locally using UUID.
- Not depend on internet connectivity.
- Not require the backend for initial creation.
- Remain unchanged when the group is synchronized with other devices or the backend.
- Be used as the stable identity of the group across local and remote synchronization.

A group created while offline is a valid group and must not be considered temporary or incomplete merely because it has not yet been synchronized with the backend.

### 7.3 Group Information

The initial group information consists of:

- `groupId`
- `name`
- `groupType`
- `description` (optional)
- `createdBy`
- `createdAt`
- `updatedAt`

Additional group-specific information may be introduced later without changing the fundamental group identity.

For example, a Trip group may later contain information such as:

- `startDate`
- `endDate`
- `location`

Such information must remain optional and must not make the core Group model dependent on the Trip use case.

### 7.4 Group Types

V1 will support descriptive group types such as:

- `GENERAL`
- `TRIP`
- `HOME`
- `FAMILY`
- `FRIENDS`
- `OFFICE`
- `EVENT`

The group type is metadata used to describe the purpose of a group.

Group type must not change the fundamental behavior of:

- Expenses
- Expense splits
- Balances
- Settlements
- Synchronization

Future group types may be added without redesigning the core expense-management model.

### 7.5 Group Creation

A user must be able to create a group completely offline.

The basic flow is:

```text
User
  ↓
Create Group
  ↓
Generate groupId locally
  ↓
Persist Group locally
  ↓
Add creator as a member
  ↓
Assign creator as OWNER
  ↓
Group immediately available
```

## 8. Expense Model

### 8.1 Expense as the Core Financial Record

An Expense represents a shared financial transaction recorded within a Group.

Every expense belongs to exactly one group.

An expense represents an amount paid for a product, service, activity, or any other group-related purpose.

An expense must contain enough information to determine:

- What was spent.
- How much was spent.
- Who paid.
- Who created the expense.
- When the expense occurred.
- Which group members are responsible for the expense.

The detailed allocation of the expense is represented through Expense Split records defined in Section 9.

### 8.2 Expense Identity

Every expense must have a stable, locally generated `expenseId`.

The `expenseId` must:

- Be generated locally using UUID.
- Not depend on internet connectivity.
- Not require the backend for creation.
- Remain unchanged during synchronization.
- Uniquely identify the expense across authorized devices.

An expense created while offline must use the same `expenseId` when it is later synchronized with other devices or the backend.

### 8.3 Expense Information

The initial expense information consists of:

- `expenseId`
- `groupId`
- `description`
- `amount`
- `currency`
- `paidBy`
- `createdBy`
- `expenseDate`
- `createdAt`
- `updatedAt`
- `category` (optional)
- `notes` (optional)

Additional synchronization, auditing, or metadata fields may be introduced later without changing the core expense identity.

The exact database representation, constraints, indexes, and data types will be defined in Section 16.

### 8.4 Expense Description

`description` identifies the purpose or nature of the expense.

Examples:

- Hotel
- Dinner
- Taxi
- Fuel
- Movie Tickets
- Groceries

The description is informational and must not affect expense splitting, balance calculation, or settlement calculation.

### 8.5 Expense Amount

The `amount` represents the total monetary value of the expense.

The amount must:

- Be greater than zero.
- Use an exact monetary representation.
- Not rely on floating-point arithmetic for financial calculations.
- Be associated with an explicit currency.

The exact monetary representation and precision rules will be defined in Section 16.

### 8.6 Currency

Every expense must have a currency.

For V1, a group should use a single primary currency for its expenses.

Multi-currency expenses and currency conversion are outside the initial expense calculation model.

The architecture should remain extensible enough to support multi-currency functionality in a future version.

### 8.7 Payer

Every expense must identify the group member who actually paid the expense.

The initial model uses:

```text
paidBy → userId
```

## 9. Expense Split Model

### 9.1 Purpose

An Expense Split represents how the total amount of an Expense is allocated among the participating group members.

An Expense and its Expense Splits are separate but related concepts:

```text
Expense
    ↓
Expense Splits
    ↓
Individual Member Share
```

The Expense defines the total amount and payer.

The Expense Split Model defines how that amount is distributed among the participating users.

### 9.2 Expense Split Identity

Every expense split must have a stable `expenseSplitId`.

The `expenseSplitId` must:

- Be generated locally using UUID.
- Not depend on internet connectivity.
- Not require the backend for creation.
- Remain unchanged during synchronization.
- Uniquely identify the split record.

Every Expense Split must also reference its parent `expenseId` and the participating `userId`.

### 9.3 Expense Split Information

The initial Expense Split model consists of:

- `expenseSplitId`
- `expenseId`
- `userId`
- `splitType`
- `shareValue`
- `allocatedAmount`
- `createdAt`
- `updatedAt`

The exact database representation, precision, constraints, and indexes will be defined in Section 16.

### 9.4 Split Types

V1 must support the following expense split methods:

- `EQUAL`
- `EXACT_AMOUNT`
- `PERCENTAGE`
- `SHARES`

The selected split type defines how the expense amount is allocated among the participating members.

The split type belongs to the Expense-level split configuration, while each Expense Split stores the member-specific allocation.

### 9.5 Equal Split

In an equal split, the total expense amount is divided equally among all selected participants.

Example:

```text
Expense Amount = ₹900

Participants:
Praveen
Rahul
Amit

Each member owes:
₹900 / 3 = ₹300
```

The number of participants determines the equal allocation.

The system must ensure that the sum of all allocated amounts exactly matches the total expense amount.

Any unavoidable monetary rounding must be handled deterministically according to the monetary precision rules defined in Section 16.

### 9.6 Exact Amount Split

In an exact amount split, the user specifies the exact amount allocated to each participant.

Example:

```text
Expense Amount = ₹1,000

Praveen = ₹500
Rahul   = ₹300
Amit    = ₹200
```

The sum of all participant allocations must exactly equal the total expense amount.

The application must reject the expense if:

```text
Sum of allocated amounts != Expense amount
```

Exact amount splits must be validated locally before the expense is persisted.

### 9.7 Percentage Split

In a percentage split, the user specifies the percentage assigned to each participant.

Example:

```text
Expense Amount = ₹1,000

Praveen = 50%
Rahul   = 30%
Amit    = 20%
```

The total percentage must equal exactly:

```text
100%
```

The allocated amount for each participant is calculated from the participant's percentage and the total expense amount.

The resulting allocated amounts must reconcile exactly with the total expense amount according to the monetary precision and rounding rules defined in Section 16.

The application must reject a percentage split if the total percentage is not valid.

### 9.8 Shares Split

In a shares split, each participant is assigned a number of shares representing their relative portion of the expense.

Example:

```text
Expense Amount = ₹1,000

Praveen = 2 shares
Rahul   = 1 share
Amit    = 1 share
```

Total shares:

```text
2 + 1 + 1 = 4
```

Therefore:

```text
Praveen = ₹500
Rahul   = ₹250
Amit    = ₹250
```

Shares must be positive values.

The system must calculate each participant's allocation proportionally based on their share relative to the total number of shares.

### 9.9 Split Validation

Before an Expense Split configuration is persisted, the application must validate:

- At least one participant exists.
- Every participant is a valid member of the group according to the applicable membership rules.
- Every participant is uniquely represented within the expense.
- The selected split type is supported.
- The split-specific values are valid.
- The calculated allocations are valid monetary values.
- The total allocated amount reconciles with the total expense amount.

Validation must happen locally before the expense is persisted.

The backend must enforce the same critical validation rules when synchronized data is received.

### 9.10 Split and Expense Amount Reconciliation

For every expense:

```text
Sum of all allocated amounts = Expense Amount
```

This must always hold after applying the defined monetary precision and deterministic rounding rules.

An expense must not be considered valid if the allocated amounts do not reconcile with the expense amount.

This invariant must be maintained:

- During local creation.
- During local editing.
- During synchronization.
- After synchronization.
- On every authorized device.

### 9.11 Split Rounding

Monetary calculations may produce fractional values that cannot be represented exactly at the supported currency precision.

The application must use a deterministic rounding strategy.

For example:

```text
Expense Amount = ₹100
Participants = 3

Raw allocation:
₹33.333...
```

The system must produce valid monetary allocations whose sum is exactly ₹100.

The rounding strategy must be deterministic so that the same expense data produces the same result on every authorized device.

The exact monetary precision and rounding strategy will be defined in Section 16.

### 9.12 Split Editing

When an expense is edited, its associated Expense Split records may also need to be modified.

For example, changing:

- Expense amount.
- Participants.
- Split type.
- Percentage values.
- Exact amounts.
- Share values.

may change the resulting allocations.

The updated split configuration must be validated locally before being persisted.

The original `expenseId` must remain unchanged.

The synchronization architecture must treat the updated split state as a modification of the existing expense rather than as a new expense.

### 9.13 Split Deletion

An Expense Split must not exist independently of its parent Expense.

If an expense is deleted, its associated split records must no longer contribute to balance calculations.

The synchronization architecture must ensure that deletion or modification of an expense also results in the correct state of its associated Expense Split records across authorized devices.

### 9.14 Split and Balance Calculation

Expense Splits determine how much each participant is responsible for within an expense.

For a participant:

```text
Amount Owed
=
Allocated Amount from Expense Split
```

The net effect of an expense for a participant is determined by combining the amount they paid with the amount they owe.

Conceptually:

```text
Net Effect of Expense
=
Amount Paid
-
Amount Owed
```

For the group:

```text
Total Expense Amount
=
Total Allocated Amount
```

The Expense Split Model does not calculate final group settlements by itself.

Final balances are derived from:

```text
Expenses
    +
Expense Splits
    +
Settlements
    ↓
Member Balances
```

The detailed settlement and balance rules are defined in Section 10.

### 9.15 Offline Split Calculation

All split calculations must work completely offline.

The application must not require:

- Internet connectivity.
- Backend access.
- Remote API calls.

to calculate an Expense Split.

The same input data and split type must produce the same allocation on every authorized device.

### 9.16 Split Synchronization

Expense Split data must be included in the synchronization model.

The synchronization mechanism must preserve:

- `expenseSplitId`
- `expenseId`
- `userId`
- Split type
- Split-specific value
- Allocated amount

Duplicate synchronization of the same split must not create duplicate split records.

If an expense is modified while different devices are offline, the synchronization and conflict-resolution mechanisms defined in Sections 12 and 13 must determine the final valid state.

### 9.17 Split Model Invariants

The following invariants must always hold:

- Every Expense Split belongs to exactly one Expense.
- Every Expense Split references exactly one `userId`.
- Every participant must be a valid group member according to the applicable membership rules.
- A participant must not appear more than once within the same expense.
- Every expense must have at least one participant.
- Every expense must have a valid split type.
- Equal splits must allocate the expense across all selected participants according to the equal-split rules.
- Exact amount splits must have allocations whose sum equals the expense amount.
- Percentage splits must have percentages whose total equals 100%.
- Shares splits must contain valid positive share values.
- Calculated allocated amounts must reconcile exactly with the expense amount.
- Split calculations must be deterministic.
- Split calculations must work without network connectivity.
- Synchronization must not create duplicate split records.
- An Expense Split must not exist independently of its parent Expense.
- Changes to an expense must result in the corresponding split state being updated correctly.

## 10. Settlement Model

### 10.1 Purpose

A Settlement represents a payment made by one group member to another group member to reduce or clear an outstanding balance resulting from shared expenses.

Expenses determine what each member paid and what each member owes.

Settlements represent payments made between members after expenses have been recorded.

The Settlement Model must remain separate from the Expense and Expense Split models.

The relationship is:

```text
Expenses
    +
Expense Splits
    ↓
Member Balances
    ↓
Settlements
    ↓
Updated Member Balances
```

### 10.2 Settlement Identity

Every settlement must have a stable, locally generated `settlementId`.

The `settlementId` must:

- Be generated locally using UUID.
- Not depend on internet connectivity.
- Not require the backend for creation.
- Remain unchanged during synchronization.
- Uniquely identify the settlement across authorized devices.

A settlement created while offline must use the same `settlementId` when it is later synchronized with other devices or the backend.

### 10.3 Settlement Information

The initial Settlement model consists of:

- `settlementId`
- `groupId`
- `paidBy`
- `paidTo`
- `amount`
- `currency`
- `createdBy`
- `settlementDate`
- `createdAt`
- `updatedAt`
- `notes` (optional)

The exact database representation, constraints, indexes, and data types will be defined in Section 16.

### 10.4 Settlement Participants

Every settlement must identify:

- The member who makes the settlement payment.
- The member who receives the settlement payment.

The initial model uses:

```text
paidBy → userId
paidTo → userId
```

Both users must belong to the relevant group according to the applicable membership rules.

A settlement must not allow:

```text
paidBy == paidTo
```

A member cannot settle a balance with themselves.

### 10.5 Settlement Amount

The settlement amount must:

- Be greater than zero.
- Use an exact monetary representation.
- Use the group's applicable currency.
- Not rely on floating-point arithmetic for financial calculations.

The exact monetary precision and representation will be defined in Section 16.

### 10.6 Settlement Creation

A user must be able to record a settlement while offline.

The basic flow is:

```text
User
  ↓
Record Settlement
  ↓
Validate Settlement
  ↓
Persist locally
  ↓
Update local balances
  ↓
Queue for synchronization
```

If internet connectivity is available, synchronization may begin automatically in the background.

If internet connectivity is unavailable, the settlement remains safely stored locally and is synchronized later.

The user must not be required to wait for synchronization before continuing to use the application.

### 10.7 Settlement Validation

Before a settlement is persisted, the application must validate the applicable business rules, including:

- The settlement belongs to an existing group.
- `paidBy` is a valid group member.
- `paidTo` is a valid group member.
- `paidBy` and `paidTo` are different users.
- The amount is greater than zero.
- The currency is valid.
- The creating user has permission to record the settlement.

The same critical validation rules must be enforced by the backend when synchronized data is received.

### 10.8 Settlement and Existing Balance

A settlement is intended to reduce an existing net balance between group members.

For example:

```text
Praveen owes Rahul ₹500
```

If Praveen records:

```text
paidBy = Praveen
paidTo = Rahul
amount = ₹500
```

the corresponding outstanding balance is reduced accordingly.

The settlement record itself must remain a historical financial record and must not depend on the current balance remaining unchanged after it is created.

### 10.9 Settlement Does Not Create an Expense

A settlement must not be treated as a new Expense.

For example:

```text
Dinner Expense:
Rahul paid ₹1,000
Praveen owes Rahul ₹500

Settlement:
Praveen pays Rahul ₹500
```

The settlement does not represent another expense of ₹500.

Instead:

```text
Expense
    ↓
Creates financial obligation

Settlement
    ↓
Reduces financial obligation
```

This distinction must be maintained for:

- Balance calculations.
- Reporting.
- Historical records.
- Synchronization.

### 10.10 Partial Settlement

A member may settle only part of an outstanding balance.

Example:

```text
Praveen owes Rahul ₹1,000
```

Praveen may record:

```text
Settlement = ₹600
```

The remaining outstanding amount becomes:

```text
₹1,000 - ₹600 = ₹400
```

Multiple settlements may therefore exist between the same pair of users.

Each settlement must have its own stable `settlementId`.

### 10.11 Multiple Settlements

The system must support multiple settlement records within the same group.

For example:

```text
Praveen → Rahul ₹500
Praveen → Rahul ₹300
Rahul   → Amit  ₹200
```

Each settlement is an independent historical record.

The application must not require multiple settlement records to be merged into a single record.

Balances are calculated from the complete set of applicable expenses, expense splits, and settlements.

### 10.12 Settlement Date and Creation Time

The model must distinguish between:

- `settlementDate` — when the actual payment occurred.
- `createdAt` — when the settlement was recorded in SplitSync.
- `updatedAt` — when the settlement record was last modified.

This distinction is important for offline usage.

For example:

```text
Monday:
Praveen paid Rahul ₹500.

Tuesday:
Praveen records the settlement while offline.
```

The settlement must retain Monday as the `settlementDate` while `createdAt` represents Tuesday.

### 10.13 Settlement Editing

A user with appropriate permissions may be able to edit a settlement.

Editing a settlement must preserve its existing `settlementId`.

An edit represents a change to the existing settlement and must not create a second settlement.

Editable fields may include:

- Amount
- Settlement date
- Notes

Changes to `paidBy` or `paidTo` may be restricted because they change the parties involved in the settlement.

The exact permission and conflict rules will be defined in Sections 13 and 18.

Any settlement edit must:

1. Be validated locally.
2. Be persisted locally.
3. Update local balances.
4. Enter the synchronization workflow.

### 10.14 Settlement Deletion

Settlement deletion must be treated as a synchronizable operation.

A physical database deletion must not prevent other authorized devices from learning that the settlement was deleted.

The synchronization architecture may therefore require a logical deletion or tombstone mechanism.

If a settlement is deleted, the corresponding amount must no longer reduce the relevant balances.

The exact deletion, retention, and synchronization strategy will be defined in Sections 12, 13, and 16.

### 10.15 Balance Calculation

A member's net balance is derived from expenses, expense splits, and settlements.

Conceptually:

```text
Net Balance
=
Amount Paid
-
Amount Owed
+
Settlement Amounts Received
-
Settlement Amounts Paid
```

The resulting balance represents the member's current net financial position within the group.

For example:

```text
Positive Balance
→ Member should receive money.

Negative Balance
→ Member owes money.

Zero Balance
→ Member is settled.
```

The exact balance calculation algorithm will be defined as part of the settlement implementation.

### 10.16 Settlement Direction

Settlement direction must be explicitly represented.

For:

```text
paidBy = Praveen
paidTo = Rahul
amount = ₹500
```

the settlement means:

```text
Praveen → Rahul ₹500
```

The direction must not be inferred from names, balances, or display order.

This explicit direction is important for:

- Balance calculation.
- Synchronization.
- Historical records.
- Conflict resolution.
- Auditability.

### 10.17 Settlement Suggestions

The application may calculate suggested settlements based on current member balances.

For example:

```text
Praveen owes Rahul ₹500
Amit owes Rahul ₹300
```

The application may suggest:

```text
Praveen → Rahul ₹500
Amit   → Rahul ₹300
```

A suggested settlement is not a financial record until the user explicitly confirms or records it.

The exact settlement simplification algorithm will be defined during implementation.

### 10.18 Offline Settlement Operation

Settlement creation, editing, deletion, and balance recalculation must work without internet connectivity.

The application must not require:

- Backend access.
- Remote API calls.
- Internet connectivity.

to calculate locally available balances or record a settlement.

When connectivity becomes available, settlement changes must automatically enter the synchronization workflow.

### 10.19 Settlement Synchronization

Every locally created or modified settlement must be represented in the synchronization workflow.

The stable `settlementId` must be used to identify the settlement during synchronization.

Synchronization must be idempotent.

If the same settlement or synchronization operation is received more than once, it must not result in duplicate settlement records.

The exact synchronization operation/event model will be defined in Section 12.

### 10.20 Settlement and Conflict Resolution

Multiple devices may record or modify settlements while offline.

The synchronization architecture must ensure that settlement changes are not silently lost.

If conflicting modifications occur, the deterministic conflict-resolution rules defined in Section 13 must be applied.

Conflict resolution must preserve financial consistency.

### 10.21 Historical Integrity

Settlements are historical financial records.

A settlement must retain its identity even if:

- The user's profile information changes.
- The group member changes their display name.
- The settlement is synchronized later.
- Other expenses are added or modified.

Historical settlement records must continue referencing stable `userId` and `groupId` values.

### 10.22 Settlement Model Invariants

The following invariants must always hold:

- Every settlement belongs to exactly one group.
- Every settlement has exactly one stable `settlementId`.
- `settlementId` generation does not require network connectivity.
- Every settlement has a valid `paidBy` user.
- Every settlement has a valid `paidTo` user.
- `paidBy` and `paidTo` must be different users.
- Every settlement has a positive monetary amount.
- Every settlement has a valid currency.
- Every settlement has a creator.
- A settlement is not an Expense.
- A settlement may partially or completely reduce an outstanding balance.
- Multiple settlements may exist between the same users.
- Editing a settlement must preserve its `settlementId`.
- Settlement deletion must remain synchronizable.
- Settlement calculations must work without network connectivity.
- Synchronization must not create duplicate settlements.
- Settlement direction must remain explicit.
- Historical settlement records must reference stable `userId` and `groupId` values.

## 11. Offline-First Architecture

### 11.1 Purpose

SplitSync must follow an offline-first architecture in which the local device is the primary source of application state for day-to-day usage.

The application must remain usable for its core expense-management functionality regardless of internet connectivity.

Internet connectivity must improve synchronization, backup, and cross-device data availability, but must not be a mandatory dependency for core local operations.

The primary principle is:

> Local first, sync later.

### 11.2 Offline-First Principle

All core user actions must be performed against the local data store first.

The general flow is:

    User Action
        ↓
    Local Validation
        ↓
    Local Database
        ↓
    Update Local Application State
        ↓
    Queue Synchronization
        ↓
    Synchronize When Connectivity Is Available

The user must not be required to wait for a remote API response before a locally supported operation is completed.

### 11.3 Local Database as the Primary Application Store

The Android application must maintain a complete local representation of the data required for core application functionality.

The local database will use:

- Room
- SQLite

The local database must store the data required to:

- Manage the local user profile.
- Manage locally available groups.
- Manage group memberships.
- Create and view expenses.
- Store expense splits.
- Record settlements.
- Calculate balances.
- Track synchronization state.
- Recover from interrupted synchronization.

The exact database schema will be defined in Section 16.

### 11.4 Online and Offline Operation

SplitSync must support both online and offline operation.

When internet connectivity is available:

    Local Operation
        +
    Background Synchronization
        +
    Backend Connectivity

When internet connectivity is unavailable:

    Local Operation
        +
    Pending Synchronization

The application must automatically adapt its synchronization behavior based on connectivity.

The user should not be required to manually switch between an "Online Mode" and an "Offline Mode" for normal application usage.

Connectivity should determine synchronization behavior rather than determine whether core application functionality is available.

### 11.5 Core Operations That Must Work Offline

The following core operations must work without internet connectivity:

- Create local user profile.
- View local user profile.
- Create a group.
- View locally available groups.
- View locally available group members.
- Add supported group members through available offline mechanisms.
- Add expenses.
- Edit expenses.
- Delete expenses where permitted.
- Configure expense splits.
- Calculate expense allocations.
- View group balances.
- Record settlements.
- Edit settlements where permitted.
- Delete settlements where permitted.
- View locally available expense history.
- View locally available settlement history.

The exact availability of group membership operations depends on the local peer-to-peer mechanism defined in Section 19.

### 11.6 Local-First Write Strategy

Every supported write operation must follow a local-first strategy.

For example, when a user creates an expense:

    User Creates Expense
            ↓
    Validate Locally
            ↓
    Generate expenseId
            ↓
    Save Expense Locally
            ↓
    Save Expense Splits Locally
            ↓
    Update Local Balance
            ↓
    Create Sync Record
            ↓
    UI Shows Updated Data

The application must not block the user interface while waiting for synchronization.

### 11.7 Local Read Strategy

Read operations must use locally stored data as the primary source for the application UI.

For example:

    UI Request
        ↓
    Local Database
        ↓
    Return Current Local State

When internet connectivity is available, remote synchronization updates the local database.

The UI then observes the updated local state.

The architecture should avoid making direct remote API calls the primary source for normal application screens.

### 11.8 Synchronization Queue

Every locally created, modified, or deleted synchronizable entity must generate synchronization information.

The local synchronization layer must maintain sufficient information to identify pending changes.

Conceptually:

    Local Change
        ↓
    Sync Queue
        ↓
    Pending
        ↓
    Synchronization Attempt
        ↓
    Success
        ↓
    Marked as Synchronized

If synchronization fails:

    Synchronization Attempt
        ↓
    Failure
        ↓
    Keep Local Data
        ↓
    Keep Sync Pending
        ↓
    Retry Later

The exact synchronization data model will be defined in Section 12.

### 11.9 Connectivity Detection

The application must monitor network connectivity using Android-supported connectivity mechanisms.

Connectivity information should be used to determine when synchronization attempts can be made.

The application must distinguish between:

- Internet connectivity.
- Local network connectivity.
- Complete offline state.

For example:

    Internet Available
        ↓
    Backend Synchronization Available

    Internet Unavailable
        +
    Local Network Available
        ↓
    Local Peer-to-Peer Operations May Be Available

    Internet Unavailable
        +
    Local Network Unavailable
        ↓
    Local-Only Operation

The exact local peer-to-peer behavior will be defined in Section 19.

### 11.10 Automatic Synchronization

Synchronization should begin automatically when suitable connectivity becomes available.

The user should not normally be required to manually press a "Sync" button.

The synchronization process may run:

- When connectivity becomes available.
- When the application starts.
- When the application resumes.
- Periodically in the background where permitted by Android.
- After a locally created change.
- After a previously failed synchronization becomes retryable.

The exact scheduling and background execution strategy will be defined in the Android and Synchronization Architecture.

### 11.11 Retry Strategy

Temporary synchronization failures must not cause data loss.

The synchronization layer must support retrying failed operations.

Failures may occur because of:

- No internet connectivity.
- Backend unavailable.
- Request timeout.
- Temporary server error.
- Application termination.
- Device restart.
- Network transition.

The synchronization system should use controlled retries rather than continuously retrying failed operations.

The exact retry and backoff strategy will be defined in Section 12.

### 11.12 Idempotent Synchronization

Synchronization operations must be idempotent.

If the same local change is transmitted multiple times because of retries or network failures, the backend and receiving devices must not create duplicate records.

Stable identifiers such as:

- `userId`
- `deviceId`
- `groupId`
- `expenseId`
- `expenseSplitId`
- `settlementId`

must be used to identify entities consistently across synchronization.

The exact idempotency mechanism will be defined in Section 12.

### 11.13 Local Data Persistence

Locally created data must be persisted before the application reports the operation as successfully completed.

For example:

    User Action
        ↓
    Validate
        ↓
    Persist in Local Database
        ↓
    Operation Completed
        ↓
    Synchronization Pending

The application must not consider an operation successful merely because a synchronization request was created.

The local database is the first persistence boundary.

### 11.14 Application Restart

The application must remain fully capable of continuing local operation after an application restart.

Pending local changes must remain available after:

- Application termination.
- Application restart.
- Device restart.

The synchronization process must be able to resume using the locally persisted synchronization state.

Users must not be required to recreate previously saved data after an application restart.

### 11.15 Temporary Connectivity Loss

Connectivity may be lost at any point during synchronization.

For example:

    Local Change
        ↓
    Synchronization Started
        ↓
    Network Lost
        ↓
    Synchronization Interrupted
        ↓
    Local Data Preserved
        ↓
    Change Remains Pending
        ↓
    Retry When Connectivity Returns

A temporary connectivity failure must not roll back a successfully persisted local operation.

The synchronization layer must be capable of safely retrying the operation.

### 11.16 Backend Unavailability

The application must continue to operate locally when the backend is unavailable.

Backend failures may include:

- Server unavailable.
- HTTP 5xx errors.
- Timeout.
- Maintenance.
- Network routing failure.

In such cases:

- Local data must remain available.
- New local changes must continue to be accepted where supported.
- Synchronization changes must remain pending.
- The application must retry synchronization later.
- The user must not lose locally stored data.

### 11.17 Local Balance Calculation

All balance calculations required by the application must be possible from locally available data.

The calculation must use:

- Expenses.
- Expense Splits.
- Settlements.

Conceptually:

    Expenses
        +
    Expense Splits
        +
    Settlements
        ↓
    Local Balance Calculation
        ↓
    Current Group Balances

The application must not require a backend request to calculate balances for locally available data.

### 11.18 Local Consistency

Local data must remain internally consistent.

For example:

- Every Expense must belong to a valid local Group.
- Every Expense Split must belong to a valid Expense.
- Every Expense Split must reference a valid group member.
- Every Settlement must belong to a valid Group.
- Every Settlement participant must be a valid group member according to the applicable membership rules.
- Expense Split allocations must reconcile with their Expense amount.
- Financial calculations must use the same locally persisted data.

Local validation must prevent invalid application state wherever possible.

### 11.19 Online Synchronization Without Blocking the UI

When internet connectivity is available, synchronization should normally operate in the background.

The user should be able to:

- Continue viewing groups.
- Add expenses.
- Edit supported data.
- Record settlements.
- View balances.

while synchronization is running.

The UI must not depend on the completion of synchronization for normal local operations.

### 11.20 Local Changes While Synchronization Is Running

The application must allow new local changes while previously created changes are being synchronized.

For example:

    Expense A Created
        ↓
    Sync Started

    While Sync Is Running:

    Expense B Created
        ↓
    Expense B Saved Locally
        ↓
    Expense B Added to Sync Queue

The synchronization system must correctly track each change independently.

A synchronization operation for one entity must not cause unrelated local changes to be lost.

### 11.21 Offline Changes Across Multiple Devices

Different authorized devices may create changes while offline.

For example:

    Device A
        ↓
    Expense A Created Offline

    Device B
        ↓
    Expense B Created Offline

When connectivity or local peer-to-peer communication becomes available, both changes must be capable of being synchronized.

The synchronization architecture must preserve valid changes from both devices.

If two devices modify the same logical entity, the conflict-resolution rules defined in Section 13 must be applied.

### 11.22 Local Peer-to-Peer Operation

When internet connectivity is unavailable but supported local connectivity exists, SplitSync may communicate directly with nearby authorized devices.

Potential capabilities include:

- Discover nearby SplitSync users.
- Exchange group membership information.
- Exchange authorized group data.
- Synchronize locally created expenses.
- Synchronize expense splits.
- Synchronize settlements.
- Exchange synchronization metadata.

Local peer-to-peer communication must not bypass authorization or expose protected group data to unauthorized devices.

The exact protocol and security mechanism will be defined in Section 19.

### 11.23 Internet and Local Network Independence

Internet connectivity and local network connectivity must be treated as separate capabilities.

For example:

    Internet Available
    Local Network Available

The application may synchronize with:

- Backend services.
- Nearby authorized devices.

Another possible state is:

    Internet Unavailable
    Local Network Available

In this state:

- Backend synchronization is unavailable.
- Local application functionality remains available.
- Local peer-to-peer synchronization may be available.

Another possible state is:

    Internet Unavailable
    Local Network Unavailable

In this state:

- Backend synchronization is unavailable.
- Peer-to-peer synchronization is unavailable.
- Local application functionality must continue to work.

### 11.24 Data Availability During Offline Operation

The application must clearly distinguish between:

- Data that exists locally.
- Data that exists remotely but has not yet been synchronized to the device.

The application must not assume that remote data is available locally when the device is offline.

Offline screens must operate using the locally available dataset.

Synchronization may later expand or update the local dataset when connectivity becomes available.

### 11.25 Synchronization Status

The application should maintain synchronization status for locally managed data.

Possible conceptual states include:

- `SYNCED`
- `PENDING`
- `SYNCING`
- `FAILED`
- `CONFLICT`

These states describe synchronization status and are separate from physical connectivity status.

For example:

    Internet Available
        +
    Sync Pending

is a valid state.

Similarly:

    Internet Unavailable
        +
    Sync Pending

is also a valid state.

The exact synchronization state model will be defined in Section 12.

### 11.26 Offline-First Failure Principle

No synchronization failure should cause loss of successfully persisted local data.

The following principle must always hold:

> Synchronization failure must degrade synchronization, not core local functionality.

If synchronization fails:

- Local data remains available.
- Local calculations continue to work.
- New supported local operations continue to work.
- Pending changes remain queued.
- Synchronization is retried later.

### 11.27 Offline-First Model Invariants

The following invariants must always hold:

- Core expense-management functionality must work without internet connectivity.
- Local data must be persisted before an operation is considered locally successful.
- Local application state must not depend on immediate backend availability.
- Local balance calculations must work without network connectivity.
- Local changes must survive application restart.
- Local changes must survive device restart.
- Network failure must not cause loss of persisted local data.
- Synchronization must be asynchronous from normal UI operations wherever practical.
- Synchronization must be retryable.
- Synchronization must be idempotent.
- Duplicate synchronization must not create duplicate business records.
- Connectivity state must not determine whether core local functionality is available.
- Internet connectivity and local network connectivity must be treated separately.
- Local peer-to-peer communication must require appropriate authorization.
- Offline-created data must retain its stable local identity after synchronization.
- Conflicting changes must be handled according to the conflict-resolution rules defined in Section 13.
- The local database must remain the primary source for normal application reads.

## 12. Synchronization Architecture

### 12.1 Purpose

The Synchronization Architecture defines how locally stored SplitSync data is exchanged between authorized devices and the backend while preserving the offline-first behavior of the application.

Synchronization must allow SplitSync to operate independently while offline and synchronize changes automatically when connectivity becomes available.

The primary principle is:

> Local changes are persisted first and synchronized later.

### 12.2 Synchronization Responsibilities

The synchronization layer is responsible for:

- Detecting pending local changes.
- Tracking synchronization state.
- Sending local changes to the backend.
- Receiving remote changes.
- Applying remote changes to the local database.
- Synchronizing data between authorized local peer devices where supported.
- Preventing duplicate data.
- Retrying failed synchronization operations.
- Maintaining synchronization metadata.
- Detecting synchronization conflicts.
- Passing conflicts to the conflict-resolution mechanism defined in Section 13.

The synchronization layer must not contain core business calculations that belong to the domain layer.

### 12.3 Synchronization Scope

The synchronization architecture must support synchronization of the following entities:

- User
- Device
- Group
- Group Membership
- Expense
- Expense Split
- Settlement

Additional entities may be synchronized in future versions without redesigning the fundamental synchronization architecture.

### 12.4 Synchronization Participants

SplitSync may synchronize data between:

```text
Android Device
      ↕
Backend Server
      ↕
Android Device
```

When internet connectivity is unavailable but supported local connectivity exists:

```text
Android Device
      ↕
Authorized Nearby Device
```

The backend and local peer-to-peer synchronization mechanisms may use different communication protocols, but both must preserve the same business entity identities and synchronization semantics.

### 12.5 Stable Entity Identity

Every synchronizable business entity must have a stable identifier.

The primary identifiers are:

- `userId`
- `deviceId`
- `groupId`
- `expenseId`
- `expenseSplitId`
- `settlementId`

These identifiers must be generated locally where defined by the corresponding domain models.

Synchronization must never replace a locally generated business identifier with a backend-generated identifier.

For example:

```text
Offline Device

expenseId = E123
        ↓
Synchronize
        ↓
Backend
        ↓
expenseId remains E123
```

This is required to prevent duplicate entities and preserve references between related records.

### 12.6 Synchronization Direction

Synchronization must support bidirectional data exchange.

The two primary directions are:

```text
Local Device
     ↓
Backend
```

and:

```text
Backend
     ↓
Local Device
```

Where local peer-to-peer synchronization is supported:

```text
Device A
     ↕
Device B
```

The synchronization mechanism must be capable of handling changes originating from multiple devices.

### 12.7 Local Change Creation

When a user creates, modifies, or deletes a synchronizable entity, the change must first be persisted locally.

The general flow is:

```text
User Action
    ↓
Local Validation
    ↓
Local Database Transaction
    ↓
Business Entity Updated
    ↓
Synchronization Metadata Created
    ↓
Change Becomes Pending
```

The local operation must be considered successful once the required local transaction has been committed.

Synchronization must occur independently after the local operation.

### 12.8 Synchronization Metadata

The local database must maintain synchronization metadata sufficient to determine the state of local changes.

Synchronization metadata may include:

- Entity identifier.
- Entity type.
- Operation type.
- Local change identifier.
- Creation timestamp.
- Last modification timestamp.
- Synchronization status.
- Retry count.
- Last synchronization attempt.
- Last synchronization error.
- Remote synchronization information where required.

The exact database structure will be defined in Section 16.

### 12.9 Synchronization Operations

The synchronization layer must support the following conceptual operations:

- `CREATE`
- `UPDATE`
- `DELETE`

For example:

```text
Expense Created
    ↓
CREATE operation
```

```text
Expense Modified
    ↓
UPDATE operation
```

```text
Expense Deleted
    ↓
DELETE operation
```

The exact API representation of these operations will be defined in Section 17.

### 12.10 Synchronization Queue

Pending synchronization operations must be maintained locally.

Conceptually:

```text
Local Change
    ↓
Sync Queue
    ↓
PENDING
    ↓
Synchronization Attempt
    ↓
SUCCESS
```

If synchronization fails:

```text
Synchronization Attempt
    ↓
FAILURE
    ↓
Remain PENDING
    ↓
Retry Later
```

The queue must survive:

- Application restart.
- Device restart.
- Temporary network loss.
- Backend unavailability.

### 12.11 Synchronization State

The synchronization layer may use the following conceptual states:

- `PENDING`
- `SYNCING`
- `SYNCED`
- `FAILED`
- `CONFLICT`

`PENDING` means a local change has not yet been successfully synchronized.

`SYNCING` means synchronization is currently being attempted.

`SYNCED` means the change has been successfully synchronized.

`FAILED` means synchronization failed and requires a future retry.

`CONFLICT` means the local change cannot be automatically reconciled and requires the conflict-resolution rules defined in Section 13.

### 12.12 Automatic Synchronization

Synchronization should occur automatically when suitable connectivity becomes available.

Potential synchronization triggers include:

- Internet connectivity becoming available.
- Application startup.
- Application resume.
- New local change.
- Periodic background execution where permitted by Android.
- Retry after a temporary failure.

The user should not normally be required to manually initiate synchronization.

A manual synchronization action may be provided as an additional diagnostic or user-control feature but must not be required for normal operation.

### 12.13 Connectivity-Aware Synchronization

The synchronization engine must distinguish between available synchronization channels.

When internet connectivity is available:

```text
Local Database
      ↓
Backend Synchronization
      ↓
Remote Changes
      ↓
Local Database
```

When internet connectivity is unavailable but local peer connectivity exists:

```text
Local Database
      ↕
Authorized Nearby Device
```

When neither is available:

```text
Local Database
      ↓
Pending Changes
```

The absence of connectivity must not prevent local application usage.

### 12.14 Synchronization Transaction Boundaries

Synchronization must preserve local database consistency.

Applying a synchronized change should use an appropriate local database transaction where multiple related records must be updated together.

For example, an expense and its associated split records must not leave the local database in a partially applied state.

Conceptually:

```text
Receive Remote Change
        ↓
Validate
        ↓
Begin Local Transaction
        ↓
Apply Related Changes
        ↓
Update Synchronization Metadata
        ↓
Commit
```

If the transaction fails:

```text
Rollback
    ↓
Keep Previous Valid Local State
    ↓
Retry Later
```

### 12.15 Idempotency

Synchronization must be idempotent.

The same synchronization operation may be delivered more than once because of:

- Network retries.
- Request timeouts.
- Application restarts.
- Duplicate delivery.
- Peer-to-peer retransmission.

Repeated processing of the same operation must not create duplicate business entities.

For example:

```text
CREATE Expense E123
```

received twice must result in exactly one logical Expense with:

```text
expenseId = E123
```

The exact idempotency mechanism will be defined in the Backend and Database Architecture.

### 12.16 Duplicate Detection

Duplicate detection must use stable entity identifiers and synchronization metadata.

The synchronization system must prevent duplicate:

- Groups.
- Group memberships.
- Expenses.
- Expense splits.
- Settlements.

The same business entity received from multiple synchronization paths must still resolve to the same logical entity.

For example:

```text
Device A → Backend → Device B
```

and:

```text
Device A → Device B
```

must not result in two copies of the same business entity on Device B.

### 12.17 Ordering of Changes

Synchronization must preserve required ordering between dependent changes.

For example:

```text
Create Group
    ↓
Create Group Membership
    ↓
Create Expense
    ↓
Create Expense Split
```

An Expense must not be applied before the local system can resolve its required Group and membership references.

The synchronization engine must either:

- Synchronize dependent changes in the required order.
- Temporarily defer a dependent change until its prerequisites are available.

The exact ordering strategy will be defined during implementation.

### 12.18 Parent-Child Synchronization

Some entities depend on other entities.

The primary relationships include:

```text
Group
  ↓
Group Membership
```

```text
Group
  ↓
Expense
  ↓
Expense Split
```

```text
Group
  ↓
Settlement
```

Synchronization must preserve these relationships.

A child entity must not result in invalid local references.

### 12.19 Remote Change Application

When a remote change is received, the local device must:

1. Identify the entity.
2. Validate the received data.
3. Determine whether the change is new, already processed, or conflicting.
4. Apply the change if valid.
5. Update synchronization metadata.
6. Recalculate affected local application state.
7. Make the updated state available to the UI.

Remote changes must be applied through the local persistence layer rather than directly to UI state.

### 12.20 Local and Remote Changes

The synchronization architecture must support the possibility that local and remote changes occur independently.

For example:

```text
Device A
    ↓
Expense E123 Modified Offline
```

At the same time:

```text
Device B
    ↓
Expense E123 Modified Offline
```

When synchronization occurs, both changes must be evaluated.

If the changes are compatible, they may be merged according to the synchronization rules.

If they conflict, the conflict-resolution rules defined in Section 13 must be applied.

### 12.21 Conflict Detection

The synchronization layer must detect situations where two different changes modify the same logical entity or otherwise create incompatible states.

Potential conflicts include:

- Same expense modified on multiple devices.
- Same settlement modified on multiple devices.
- Group ownership changed differently on multiple devices.
- Group membership changed differently on multiple devices.
- Related entities changed in incompatible ways.

Conflict detection must be deterministic.

The exact conflict-resolution strategy is defined in Section 13.

### 12.22 Conflict Preservation

The synchronization system must not silently discard a valid local change merely because a remote change exists.

When automatic resolution is not possible, the system must preserve enough information to:

- Identify the conflict.
- Determine the affected entity.
- Determine the local version.
- Determine the remote version.
- Apply the conflict-resolution strategy.
- Provide an appropriate user-facing result where required.

### 12.23 Retry Strategy

Temporary synchronization failures must be retried.

The synchronization layer should use controlled retry and backoff behavior.

For example:

```text
Attempt 1
    ↓
Failure
    ↓
Wait
    ↓
Attempt 2
    ↓
Failure
    ↓
Longer Wait
    ↓
Attempt 3
```

The synchronization system must avoid continuously retrying in a tight loop.

Permanent validation or authorization failures must not be treated as temporary network failures.

### 12.24 Synchronization Failure Categories

Synchronization failures should be classified into appropriate categories.

Examples include:

- Connectivity failure.
- Timeout.
- Temporary backend failure.
- Authentication failure.
- Authorization failure.
- Validation failure.
- Conflict.
- Unknown or unexpected failure.

Temporary failures should be retryable.

Permanent failures should be recorded and handled according to the appropriate application rules.

### 12.25 Backend Synchronization

When internet connectivity is available, the Android application may synchronize with the Spring Boot backend through versioned REST APIs.

The general flow is:

```text
Android Local Database
        ↓
Synchronization Layer
        ↓
REST API
        ↓
Spring Boot Backend
        ↓
Hibernate/JPA
        ↓
MySQL
```

The backend must validate synchronized data before persisting it.

The exact REST API contracts will be defined in Section 17.

### 12.26 Local Peer-to-Peer Synchronization

When supported local connectivity is available, authorized SplitSync devices may synchronize directly.

The general flow is:

```text
Device A
    ↓
Local Discovery
    ↓
Authorization
    ↓
Secure Synchronization Channel
    ↓
Device B
```

Local peer-to-peer synchronization must use the same stable entity identities and business rules as backend synchronization.

The local peer-to-peer mechanism must not create a separate business model.

The exact protocol and transport will be defined in Section 19.

### 12.27 Synchronization Security

Only authorized users and devices may synchronize protected group data.

Synchronization must validate:

- Device identity.
- User identity.
- Group membership.
- Authorization.
- Integrity of synchronized data.

The exact security mechanisms will be defined in Section 18.

### 12.28 Synchronization and Data Ownership

The backend must not be treated as the only source of truth for newly created local data.

A locally created entity is valid locally before backend synchronization.

After synchronization, the backend and authorized devices must converge toward a consistent representation of the same logical entity.

The architecture therefore follows:

```text
Local Creation
      ↓
Local Valid State
      ↓
Synchronization
      ↓
Shared Consistent State
```

### 12.29 Eventual Consistency

SplitSync uses eventual consistency for synchronized data.

When devices are temporarily disconnected:

```text
Device A
    ↓
Local State A

Device B
    ↓
Local State B
```

The two devices may temporarily contain different valid states.

After successful synchronization:

```text
Device A
    ↓
Shared State

Device B
    ↓
Shared State
```

The system should eventually converge toward a consistent state when:

- Connectivity is available.
- Synchronization succeeds.
- Conflicts are resolved.

### 12.30 Synchronization and Local Calculations

Local financial calculations must not wait for synchronization.

A device must calculate balances using the data currently available locally.

After synchronization introduces new or modified data:

```text
Remote Change
    ↓
Local Database Updated
    ↓
Local Balance Recalculated
    ↓
UI Updated
```

The resulting balance may change because the local dataset has changed.

This is expected behavior in an offline-first eventually consistent system.

### 12.31 Synchronization After Application Restart

Pending synchronization state must survive application restarts.

After restart:

```text
Application Starts
    ↓
Load Local Database
    ↓
Load Pending Synchronization State
    ↓
Check Connectivity
    ↓
Resume Synchronization When Possible
```

Previously persisted local changes must not be lost.

### 12.32 Synchronization After Device Restart

The synchronization architecture must also survive device restarts.

After the Android device restarts, the application must be able to recover:

- Local business data.
- Pending synchronization operations.
- Synchronization metadata.
- Retry information where required.

Synchronization may resume when the application is started or when the Android background execution rules permit it.

### 12.33 Synchronization and Deletion

Deletion must be synchronized as a logical operation where required.

A deleted entity may need to remain represented by synchronization metadata or a tombstone so that other devices can learn about the deletion.

For example:

```text
Device A
    ↓
Expense E123 Deleted
    ↓
Deletion Synchronization
    ↓
Device B
    ↓
Expense E123 Removed From Active Data
```

The exact tombstone and retention strategy will be defined in Section 16.

### 12.34 Synchronization and Historical Data

Synchronization must preserve historical financial records according to the business rules defined in Sections 8, 9, and 10.

Changes to:

- User profile information.
- Display names.
- Group membership status.
- Expense metadata.
- Settlement metadata.

must not unnecessarily break historical references.

Stable identifiers must be used for historical relationships.

### 12.35 Synchronization Observability

The application should maintain enough synchronization information to diagnose synchronization problems.

Where appropriate, the system should be able to identify:

- Last successful synchronization.
- Number of pending changes.
- Last synchronization failure.
- Number of retry attempts.
- Conflicting entities.
- Current synchronization state.

Sensitive financial or authentication information must not be unnecessarily exposed through diagnostic information.

### 12.36 Synchronization Model Invariants

The following invariants must always hold:

- Local data is persisted before synchronization is attempted.
- Synchronization must not be required for core offline functionality.
- Stable entity identifiers must be preserved across synchronization.
- Synchronization must be idempotent.
- Duplicate synchronization must not create duplicate business entities.
- Related entities must maintain valid references.
- Synchronization must survive application restart.
- Synchronization must survive device restart.
- Temporary synchronization failure must not cause local data loss.
- Failed synchronization operations must remain recoverable.
- Conflicting changes must be handled according to Section 13.
- Protected data must only be synchronized between authorized parties.
- Backend synchronization and local peer-to-peer synchronization must preserve the same business entity identities.
- Local balance calculations must not depend on successful synchronization.
- Deletions must be represented in a way that allows other synchronized devices to learn about them.
- Synchronization must eventually converge toward a consistent state when connectivity and synchronization are available.


## 13. Conflict Resolution

### 13.1 Purpose

SplitSync is an offline-first application in which multiple authorized devices may create or modify data independently while disconnected.

As a result, two or more devices may modify the same logical entity before synchronization occurs.

The Conflict Resolution Architecture defines how such situations are detected, evaluated, and resolved while preserving data integrity and financial consistency.

The primary principle is:

> Never silently lose a valid user change.

### 13.2 Why Conflicts Can Occur

Conflicts may occur when:

- Multiple devices modify the same Expense while offline.
- Multiple devices modify the same Settlement while offline.
- Group membership changes independently on different devices.
- Group ownership changes independently on different devices.
- The same entity is deleted on one device while modified on another.
- Related entities are changed independently.
- Synchronization occurs through different paths, such as backend and local peer-to-peer synchronization.

For example:

    Device A
        ↓
    Expense E123 = ₹1,000
        ↓
    Offline Edit
        ↓
    Expense E123 = ₹1,200

At the same time:

    Device B
        ↓
    Expense E123 = ₹1,000
        ↓
    Offline Edit
        ↓
    Expense E123 = ₹1,500

When synchronization occurs, the system must detect that both devices modified the same logical entity.

### 13.3 Conflict Definition

A conflict exists when two or more independently created changes cannot be safely applied together while preserving the defined business rules.

Not every difference between devices is a conflict.

For example:

    Device A:
    Expense description changed

    Device B:
    Expense notes changed

These changes may be mergeable if the affected fields are independent.

However:

    Device A:
    Expense amount = ₹1,200

    Device B:
    Expense amount = ₹1,500

represents a conflict because both devices changed the same financial field differently.

### 13.4 Stable Entity Identity

Conflict detection must use stable entity identifiers.

The primary identifiers include:

- `groupId`
- `expenseId`
- `expenseSplitId`
- `settlementId`
- `userId`
- `deviceId`

A conflict must be evaluated against the same logical entity rather than based on display names or other mutable fields.

### 13.5 Change Metadata

The synchronization system must maintain sufficient metadata to determine whether changes are:

- New.
- Already synchronized.
- Sequential updates.
- Concurrent updates.
- Conflicting updates.

The exact metadata may include:

- Entity identifier.
- Device identifier.
- Change identifier.
- Local modification timestamp.
- Logical version.
- Parent version where applicable.
- Synchronization status.

The exact versioning mechanism will be finalized during implementation.

### 13.6 Local Changes Must Be Preserved

A local change that has already been persisted must not be silently discarded simply because another device contains a different version.

For example:

    Device A
        ↓
    Local Change A

    Device B
        ↓
    Local Change B

    Synchronization
        ↓
    Conflict Detected

The conflict-resolution mechanism must determine the appropriate result without silently losing a valid local change.

### 13.7 Conflict Categories

The initial conflict categories are:

- Field-level conflict.
- Entity-level conflict.
- Delete-versus-update conflict.
- Membership conflict.
- Ownership conflict.
- Financial consistency conflict.
- Synchronization ordering conflict.

Each category may require different resolution rules.

### 13.8 Field-Level Conflict

A field-level conflict occurs when two devices modify the same field differently.

Example:

    Device A:
    description = "Dinner"

    Device B:
    description = "Dinner with friends"

If both changes are based on the same previous version, the system must determine whether the field is automatically mergeable or requires conflict resolution.

For non-critical descriptive fields, an automatic deterministic strategy may be used.

For financially significant fields, automatic overwriting should not be performed without an explicitly defined rule.

### 13.9 Entity-Level Conflict

An entity-level conflict occurs when multiple changes affect the same logical entity in ways that cannot safely be merged.

Examples include:

- Expense amount changed differently.
- Expense payer changed differently.
- Settlement amount changed differently.
- Settlement participants changed differently.

The system must resolve such conflicts according to deterministic rules.

### 13.10 Financial Data Protection

Financially significant data requires stricter conflict handling.

The following fields are considered financially significant:

- Expense amount.
- Expense payer.
- Expense currency.
- Expense Split allocations.
- Expense Split participants.
- Settlement amount.
- Settlement payer.
- Settlement receiver.
- Settlement currency.

The system must not silently select an arbitrary value for a financial conflict unless that behavior has been explicitly defined and is deterministic.

### 13.11 Expense Conflict

Expenses may be modified independently on multiple devices.

For example:

    Original:
    Expense = ₹1,000

    Device A:
    Expense = ₹1,200

    Device B:
    Expense = ₹1,500

The synchronization layer must detect the concurrent modification.

The final resolution must preserve financial consistency between:

- Expense amount.
- Expense Split records.
- Group balances.
- Related synchronization metadata.

The exact automatic resolution strategy will be finalized before implementation.

### 13.12 Expense Split Conflict

Expense Split records are directly related to an Expense.

A conflict involving:

- Participants.
- Split type.
- Exact amounts.
- Percentages.
- Shares.
- Allocated amounts.

must be evaluated together with the parent Expense.

The system must not allow a conflict-resolution result that causes:

    Sum of allocated amounts != Expense amount

The Expense and its Expense Splits must therefore be treated as a logically related financial state during conflict resolution.

### 13.13 Settlement Conflict

Settlements are historical financial records.

If the same Settlement is modified differently on multiple devices, the conflict must be detected using its stable `settlementId`.

For example:

    Original Settlement:
    Praveen → Rahul ₹500

    Device A:
    ₹400

    Device B:
    ₹600

The system must not silently treat both values as separate settlements.

The final state must be determined according to the defined conflict-resolution strategy.

### 13.14 Delete Versus Update Conflict

A conflict may occur when one device deletes an entity while another device modifies the same entity.

For example:

    Device A:
    Delete Expense E123

    Device B:
    Modify Expense E123

The synchronization system must detect this situation.

The system must not simply apply whichever operation arrives first without evaluating the conflict.

The final result must follow deterministic conflict-resolution rules.

### 13.15 Membership Conflict

Group membership may change while devices are offline.

For example:

    Device A:
    Member Rahul removed

    Device B:
    Member Rahul remains active

The synchronization system must evaluate the membership changes according to:

- Authorization.
- Operation ordering.
- Membership state.
- Historical financial references.

Removing a member must not invalidate historical expenses, splits, or settlements involving that member.

### 13.16 Ownership Conflict

Ownership is a critical group-level operation.

A conflict may occur if two devices independently attempt to change the group owner while offline.

For example:

    Device A:
    Owner → Rahul

    Device B:
    Owner → Amit

The system must not allow the final synchronized group to contain multiple active owners.

Ownership conflicts must be resolved deterministically according to authorization and operation-order rules.

### 13.17 Concurrent Creation

The creation of different entities with different stable UUIDs is generally not a conflict.

For example:

    Device A:
    Expense E100 created

    Device B:
    Expense E200 created

Both expenses must be preserved.

Synchronization must merge independent valid creations rather than treating them as conflicting changes.

### 13.18 Duplicate Creation Detection

If two devices contain the same stable entity identifier, the synchronization system must determine whether they represent:

- The same logical entity.
- A duplicate or invalid entity.
- Different versions of the same entity.

Stable identifiers must therefore be globally unique within their entity scope.

A duplicate synchronization operation for the same entity must not create a second business record.

### 13.19 Conflict Detection Strategy

The initial architecture should use explicit change/version metadata rather than relying only on wall-clock timestamps.

Timestamps may differ between devices because device clocks are not guaranteed to be synchronized.

Therefore, timestamps may be used as supporting metadata, but they must not be the sole authority for determining whether two changes are concurrent.

The final versioning strategy will be defined in the Synchronization Architecture implementation.

### 13.20 Automatic Conflict Resolution

Conflicts should be automatically resolved only when the result is deterministic and does not violate business rules.

Suitable candidates may include:

- Independent non-financial field changes.
- Duplicate delivery of the same operation.
- Changes that are already known to have been applied.
- Non-conflicting metadata changes.

Financial conflicts should require stricter handling.

The system must never resolve a financial conflict by simply choosing an arbitrary device version.

### 13.21 Manual Conflict Resolution

Where a conflict cannot be safely resolved automatically, the architecture must support a controlled conflict state.

Conceptually:

    Local Version
        +
    Remote Version
        ↓
    Conflict Detected
        ↓
    CONFLICT State
        ↓
    Resolution
        ↓
    Valid Final State

The exact user interface for manual conflict resolution will be defined during Android application design.

The underlying data model must preserve enough information to support such a resolution.

### 13.22 Conflict Preservation

When a conflict is detected, the system must preserve the information required to resolve it.

Where necessary, this may include:

- Local version.
- Remote version.
- Entity identifier.
- Change identifiers.
- Originating device.
- Version information.
- Conflict type.
- Detection time.
- Resolution state.

Conflict information must not expose protected data to unauthorized users or devices.

### 13.23 Conflict Resolution and Financial Consistency

After conflict resolution, all financial invariants must be revalidated.

For an Expense:

    Expense Amount
        =
    Sum of Expense Split Allocations

For a Settlement:

    Settlement Amount > 0

For group balances:

    Balances must be derivable from the resulting valid set of
    Expenses, Expense Splits, and Settlements.

A conflict must never result in an invalid financial state.

### 13.24 Conflict Resolution and Local Database

Conflict resolution must be applied through the local persistence layer.

The general flow is:

    Conflict Detected
        ↓
    Load Relevant Local State
        ↓
    Evaluate Conflict
        ↓
    Resolve Conflict
        ↓
    Validate Result
        ↓
    Persist Result in Transaction
        ↓
    Update Synchronization State
        ↓
    Recalculate Affected Data

If validation fails, the invalid result must not be committed.

### 13.25 Conflict Resolution and Synchronization

After a conflict has been resolved, the resulting state must itself become part of the synchronization workflow where required.

For example:

    Conflict
        ↓
    Resolution
        ↓
    Valid Local State
        ↓
    Synchronization
        ↓
    Other Authorized Devices

This ensures that all authorized devices can eventually converge toward the same resolved state.

### 13.26 Conflict Resolution and Peer-to-Peer Synchronization

The same conflict-resolution rules must apply regardless of synchronization source.

A conflict must be handled consistently whether the change is received through:

- Backend synchronization.
- Local peer-to-peer synchronization.
- A later synchronization from another authorized device.

The application must not maintain separate business conflict rules for online and offline synchronization.

### 13.27 Conflict Resolution and Deletion

Deletion conflicts must preserve historical integrity.

If an entity is deleted on one device and modified on another, the system must determine the final state according to the defined deletion and conflict rules.

Deletion must not silently remove information that is still required for:

- Historical records.
- Financial calculations.
- Synchronization.
- Auditability.

Logical deletion or tombstone information may be required to communicate deletion across devices.

### 13.28 Conflict Resolution and Group Membership

Membership conflicts must not corrupt historical financial data.

A member who leaves or is removed from a group may still be referenced by:

- Existing Expenses.
- Expense Splits.
- Settlements.

Conflict resolution must preserve those historical references.

Membership status changes and historical financial records must therefore be treated as separate concerns.

### 13.29 Conflict Resolution and Ownership

The final synchronized state of a group must contain at most one active owner.

If conflicting ownership operations are received, the conflict-resolution mechanism must determine a single valid owner according to the defined authorization and operation-order rules.

The result must be synchronized to all authorized devices.

### 13.30 Conflict Resolution and User Experience

The application should hide synchronization complexity from normal users whenever possible.

Users should not normally need to understand:

- Synchronization versions.
- Change identifiers.
- Device identifiers.
- Internal conflict metadata.

However, if a conflict requires user intervention, the application must provide enough information for the user to make an informed decision without exposing unnecessary technical details.

### 13.31 Conflict Resolution Auditability

Where a conflict changes a financial or security-sensitive state, the system should maintain sufficient metadata to determine:

- What conflict occurred.
- Which entities were involved.
- Which versions were considered.
- How the conflict was resolved.
- When the resolution occurred.
- Which device or user performed the resolution where applicable.

The exact audit model will be defined in the Security and Database Architecture sections.

### 13.32 Conflict Resolution Model Invariants

The following invariants must always hold:

- A valid locally persisted change must not be silently discarded.
- Conflicts must be detected deterministically.
- Stable entity identifiers must be used for conflict detection.
- Wall-clock timestamps must not be the sole conflict-detection mechanism.
- Independent entity creations must be preserved.
- Duplicate synchronization must not create duplicate entities.
- Financial conflicts must receive stricter handling than non-financial metadata conflicts.
- Expense conflicts must preserve Expense and Expense Split consistency.
- Settlement conflicts must preserve financial consistency.
- Membership conflicts must not invalidate historical financial records.
- Ownership conflicts must not result in multiple active owners.
- Delete-versus-update conflicts must be explicitly evaluated.
- Automatically resolved conflicts must produce deterministic results.
- Unresolved conflicts must be represented explicitly.
- Conflict resolution must be validated before being committed.
- Conflict resolution must be persisted through the local database.
- Resolved changes must be capable of being synchronized to other authorized devices.
- The same conflict-resolution rules must apply to backend and peer-to-peer synchronization.
- Conflict resolution must not result in an invalid financial state.
- Protected conflict information must only be available to authorized users and devices.

## 14. Android Architecture

### 14.1 Purpose

The Android application must be designed as an offline-first, modular, and maintainable client application.

The architecture must keep the following concerns clearly separated:

- Presentation.
- Domain/business logic.
- Local persistence.
- Network communication.
- Synchronization.
- Local peer-to-peer communication.
- Security.
- Device and connectivity management.

The Android application must not depend directly on the backend for normal core application functionality.

### 14.2 Architectural Principles

The Android application must follow these principles:

- Local-first operation.
- Separation of concerns.
- Single source of truth for local application state.
- Repository-based data access.
- Domain-driven business logic.
- Reactive UI updates.
- Transactional local persistence.
- Background synchronization.
- Testable business logic.
- Minimal coupling between UI and infrastructure.
- Stable entity identity across local and remote systems.

### 14.3 Recommended Android Architecture

The initial Android architecture will follow a layered architecture inspired by Clean Architecture.

The primary layers are:

```text
Presentation Layer
        ↓
Domain Layer
        ↓
Data Layer
        ↓
Local / Remote / Sync Infrastructure
```

The architecture may be represented as:

```text
┌─────────────────────────────┐
│       Presentation          │
│  UI + ViewModel + UI State  │
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│           Domain            │
│ Use Cases + Business Rules  │
│  Models + Calculations      │
└──────────────┬──────────────┘
               ↓
┌─────────────────────────────┐
│            Data             │
│ Repositories + Mappers      │
└───────┬───────────┬─────────┘
        ↓           ↓
┌────────────┐ ┌──────────────┐
│   Local    │ │    Remote    │
│ Room/SQLite│ │ REST API     │
└────────────┘ └──────────────┘
        ↑           ↑
        └─────┬─────┘
              ↓
       Synchronization
```

### 14.4 Presentation Layer

The Presentation Layer is responsible for displaying application state and receiving user interaction.

It should contain:

- Activities.
- Fragments where required.
- ViewModels.
- UI state models.
- UI event handling.
- Navigation.
- User-facing validation messages.

The Presentation Layer must not directly access:

- Room DAOs.
- Retrofit services.
- SQLite.
- Synchronization queues.
- Backend APIs.

Instead, it must communicate with the Domain Layer through appropriate use cases or application-facing interfaces.

### 14.5 ViewModel

Android ViewModels will manage screen-level state and coordinate user actions with the Domain Layer.

A ViewModel should:

- Receive UI events.
- Invoke appropriate use cases.
- Observe application state.
- Expose UI state.
- Survive configuration changes.
- Avoid directly containing database or network implementation details.

For example:

```text
Expense Screen
      ↓
ExpenseViewModel
      ↓
CreateExpenseUseCase
      ↓
ExpenseRepository
```

The ViewModel must not contain the actual expense-splitting algorithm.

### 14.6 UI State

The UI should be driven by explicit application state.

A screen may represent states such as:

- `Loading`
- `Content`
- `Empty`
- `Error`
- `Offline`
- `Syncing`

For example:

```text
Expense List
    ↓
Local Database
    ↓
Repository
    ↓
ViewModel
    ↓
UI State
    ↓
Android UI
```

The UI should react to changes in local application state rather than waiting for network responses.

### 14.7 Domain Layer

The Domain Layer contains the core business rules of SplitSync.

It must remain independent of:

- Android UI.
- Room.
- SQLite.
- Retrofit.
- HTTP.
- Spring Boot.
- MySQL.

The Domain Layer should contain:

- Domain models where appropriate.
- Use cases.
- Expense calculations.
- Split calculations.
- Balance calculations.
- Settlement calculations.
- Business validation.
- Group-related business rules.
- Synchronization-independent business rules.

### 14.8 Use Cases

Business operations should be represented through explicit use cases.

Examples include:

- `CreateUserProfileUseCase`
- `CreateGroupUseCase`
- `JoinGroupUseCase`
- `AddGroupMemberUseCase`
- `CreateExpenseUseCase`
- `UpdateExpenseUseCase`
- `DeleteExpenseUseCase`
- `CalculateExpenseSplitUseCase`
- `CalculateGroupBalanceUseCase`
- `CreateSettlementUseCase`
- `UpdateSettlementUseCase`
- `DeleteSettlementUseCase`

The exact use-case list may evolve during implementation.

A use case should represent a meaningful business operation rather than simply wrapping a single database method.

### 14.9 Expense Calculation Domain Logic

Expense splitting and balance calculations must be implemented in the Domain Layer.

For example:

```text
Expense
    ↓
Split Configuration
    ↓
Split Calculation
    ↓
Expense Splits
    ↓
Balance Calculation
```

These calculations must be executable without:

- Internet.
- Android networking.
- Backend access.
- Room database access.

This allows the same business logic to be unit tested independently.

### 14.10 Data Layer

The Data Layer is responsible for providing data to the Domain Layer.

It should contain:

- Repository implementations.
- Local data sources.
- Remote data sources.
- Data mappers.
- Synchronization data access.
- Persistence coordination.

The Domain Layer should depend on repository abstractions rather than concrete database or network implementations.

### 14.11 Repository Pattern

Repositories provide a stable interface between the Domain Layer and the Data Layer.

For example:

```text
Domain
   ↓
ExpenseRepository
   ↓
ExpenseRepositoryImpl
   ├── Local Expense Data Source
   ├── Remote Expense Data Source
   └── Synchronization
```

The repository must determine how data is obtained or persisted.

The UI must not need to know whether data came from:

- Local Room database.
- Backend API.
- Local peer-to-peer synchronization.

### 14.12 Local Data Source

The Local Data Source will use:

- Room.
- SQLite.

Room will provide:

- Entity mapping.
- DAO interfaces.
- Transactions.
- Queries.
- Reactive database observation.
- Local persistence.

The local database is the primary source for normal application reads.

### 14.13 Room Database

The Android application will use a single application-level Room database.

The database will contain entities representing the locally required application state.

Initial entities are expected to include:

- UserEntity.
- DeviceEntity.
- GroupEntity.
- GroupMemberEntity.
- ExpenseEntity.
- ExpenseSplitEntity.
- SettlementEntity.
- SyncOperationEntity.
- ConflictEntity where required.

The final schema will be defined in Section 16.

### 14.14 DAO Layer

Each major persistence area should have a dedicated DAO or appropriately scoped DAO.

Examples include:

- `UserDao`
- `DeviceDao`
- `GroupDao`
- `GroupMemberDao`
- `ExpenseDao`
- `ExpenseSplitDao`
- `SettlementDao`
- `SyncOperationDao`
- `ConflictDao`

DAOs must focus on persistence operations and queries.

Business rules should not be implemented directly inside DAOs.

### 14.15 Local Database Transactions

Operations that modify multiple related entities must use appropriate database transactions.

For example, creating an Expense may require:

```text
Begin Transaction
    ↓
Insert Expense
    ↓
Insert Expense Splits
    ↓
Create Synchronization Metadata
    ↓
Commit
```

If any required operation fails:

```text
Rollback
```

This prevents partially persisted financial state.

### 14.16 Remote Data Source

The Remote Data Source is responsible for communication with the backend REST APIs.

The Android application will use Java-compatible networking components such as:

- Retrofit.
- OkHttp.
- JSON serialization/deserialization.

The exact library configuration will be finalized during implementation.

The Remote Data Source must not contain core business calculations.

### 14.17 REST API Client

The REST API client should expose backend endpoints through typed interfaces.

Conceptually:

```text
ExpenseRepository
       ↓
RemoteExpenseDataSource
       ↓
ExpenseApi
       ↓
HTTP
       ↓
Spring Boot REST API
```

Authentication, authorization, request handling, and response mapping must remain separate from the domain business logic.

### 14.18 Local-First Repository Behavior

Repositories must follow the offline-first behavior defined in Section 11.

For a write operation:

```text
User Action
    ↓
Use Case
    ↓
Repository
    ↓
Local Database
    ↓
Synchronization Queue
```

The repository must not require a successful network call before confirming the local operation.

For a read operation:

```text
UI
    ↓
ViewModel
    ↓
Use Case
    ↓
Repository
    ↓
Local Database
    ↓
UI State
```

Remote synchronization updates the local database separately.

### 14.19 Synchronization Layer

Synchronization must be implemented as a separate infrastructure component.

Its responsibilities include:

- Reading pending synchronization operations.
- Detecting available synchronization channels.
- Sending local changes.
- Receiving remote changes.
- Applying remote changes.
- Handling retries.
- Detecting conflicts.
- Updating synchronization state.

The synchronization layer must not directly manipulate UI state.

The UI should observe the resulting local database state.

### 14.20 Background Synchronization

Android background execution mechanisms should be used for synchronization where appropriate.

The implementation should consider:

- Android WorkManager.
- Connectivity constraints.
- Battery constraints.
- Retry policies.
- Application lifecycle.

The synchronization worker should process pending synchronization operations without blocking the main UI thread.

### 14.21 Connectivity Management

The application should have a dedicated connectivity component responsible for determining available connectivity.

It should distinguish between:

- Internet connectivity.
- Local network availability.
- Complete offline state.

The connectivity component should expose application-level connectivity information to the synchronization layer.

It must not be responsible for determining whether core business operations are allowed.

Core local operations remain available regardless of connectivity.

### 14.22 Local Peer-to-Peer Layer

Local peer-to-peer communication must be isolated from the core business and presentation layers.

Conceptually:

```text
Synchronization Layer
        ↓
Peer-to-Peer Data Source
        ↓
Local Communication Transport
        ↓
Nearby Authorized Device
```

The exact Android communication technology will be selected during implementation.

Potential technologies may include:

- Wi-Fi Direct.
- Nearby Connections.
- Local network communication.
- Other Android-supported peer-to-peer mechanisms.

The selected technology must support the offline communication requirements defined in Section 19.

### 14.23 Security Layer

Security-related functionality must be separated from business logic.

The Android security architecture should handle:

- Local device identity.
- Authentication credentials.
- Secure token storage.
- Cryptographic keys where required.
- Secure communication.
- Peer authorization.
- Sensitive local data protection.

Sensitive credentials and cryptographic material must not be stored as plain text.

The exact security architecture will be defined in Section 18.

### 14.24 Device Identity

Each installation must have a stable application-level `deviceId`.

The device identifier must:

- Be generated locally.
- Be unique for the application installation.
- Not depend on internet connectivity.
- Be associated with the local user.
- Be used by synchronization mechanisms.

The device identity must be separate from Android hardware identifiers.

The exact device lifecycle is defined in Section 6.

### 14.25 User Identity

V1 supports one active local user per application installation.

The local user profile must be available without internet connectivity.

The Android application must associate the local user with the application-level device identity.

Conceptually:

```text
Application Installation
        ↓
Device ID
        ↓
Local User
        ↓
Groups
        ↓
Expenses
        ↓
Settlements
```

### 14.26 Navigation

Navigation should remain independent of the data source.

Screens should navigate based on application state and user actions rather than directly on network responses.

The initial navigation structure may include:

```text
Profile
    ↓
Groups
    ↓
Group Details
    ├── Members
    ├── Expenses
    ├── Balances
    └── Settlements
```

The exact UI/navigation structure will be finalized during application implementation.

### 14.27 Error Handling

Errors must be handled at the appropriate architectural layer.

Examples:

```text
Network Error
    ↓
Remote Data Source
    ↓
Repository / Sync Layer
    ↓
Retry or Sync Status
```

```text
Business Validation Error
    ↓
Domain Layer
    ↓
Use Case
    ↓
ViewModel
    ↓
UI Message
```

```text
Database Error
    ↓
Local Data Source
    ↓
Repository
    ↓
Application Error Handling
```

Infrastructure-specific exceptions should not leak unnecessarily into the Presentation Layer.

### 14.28 Offline Error Handling

Offline state must not be treated as a general application error.

For example:

```text
Internet Unavailable
```

must not prevent:

```text
Create Expense
Calculate Split
View Balance
Record Settlement
```

Instead, the application should continue locally and mark synchronization as pending where required.

### 14.29 Threading

The Android application must keep database, network, and synchronization operations away from the main UI thread.

The architecture should use appropriate asynchronous mechanisms for:

- Room operations.
- REST API calls.
- Synchronization.
- Peer-to-peer communication.
- Potentially expensive calculations.

UI state updates must be delivered safely to the Presentation Layer.

### 14.30 Dependency Injection

The application should use dependency injection to manage major components.

Dependency injection should be used for components such as:

- Database.
- DAOs.
- Repositories.
- Use cases.
- REST API clients.
- Synchronization services.
- Connectivity services.
- Security services.

The exact dependency injection framework will be selected during implementation.

### 14.31 Package Structure

The Android project should follow a feature-oriented or hybrid feature/layer package structure that keeps related functionality organized.

A possible structure is:

    com.splitsync
    │
    ├── presentation
    │   ├── profile
    │   ├── group
    │   ├── expense
    │   ├── settlement
    │   └── common
    │
    ├── domain
    │   ├── model
    │   ├── usecase
    │   ├── repository
    │   └── calculation
    │
    ├── data
    │   ├── local
    │   │   ├── database
    │   │   ├── dao
    │   │   └── entity
    │   │
    │   ├── remote
    │   │   ├── api
    │   │   ├── dto
    │   │   └── datasource
    │   │
    │   ├── repository
    │   └── mapper
    │
    ├── sync
    │   ├── queue
    │   ├── worker
    │   ├── conflict
    │   └── peer
    │
    ├── security
    │
    └── core
        ├── connectivity
        ├── error
        ├── logging
        └── utility

The final package structure may be refined during implementation without changing the architectural principles.

### 14.32 Data Flow

The preferred application data flow is:

    User Interaction
          ↓
    Presentation
          ↓
    ViewModel
          ↓
    Use Case
          ↓
    Repository
          ↓
    Local Database
          ↓
    Local State Updated
          ↓
    ViewModel Observes State
          ↓
    UI Updated

Synchronization operates alongside this flow:

    Local Change
          ↓
    Sync Queue
          ↓
    Sync Worker
          ↓
    Remote / Peer Device
          ↓
    Remote Change
          ↓
    Local Database
          ↓
    UI Automatically Reflects Updated State

### 14.33 Single Source of Truth

For normal Android application reads, the local Room database should act as the single source of truth.

The UI should not maintain an independent copy of the complete business state.

The backend is a synchronization participant rather than the direct source for every screen.

This approach ensures that the application remains functional when the network is unavailable.

### 14.34 Domain and Persistence Model Separation

Room entities should not automatically become the application's domain models.

Where appropriate, the Data Layer should map between:

```text
Room Entity
    ↓
Domain Model
```

and:

```text
Domain Model
    ↓
Room Entity
```

This separation prevents database-specific implementation details from leaking into business logic.

### 14.35 DTO and Domain Model Separation

Remote API DTOs should also remain separate from Domain Models where appropriate.

The architecture should use:

```text
REST Response
    ↓
Remote DTO
    ↓
Mapper
    ↓
Domain Model
```

and:

```text
Domain Model
    ↓
Mapper
    ↓
Request DTO
    ↓
REST API
```

This allows backend API changes to be isolated from the core domain model.

### 14.36 Architecture and Testability

Each architectural layer must be testable independently.

The architecture should support:

- Domain unit tests.
- Split calculation tests.
- Balance calculation tests.
- Repository tests.
- Room database tests.
- Synchronization tests.
- Conflict-resolution tests.
- REST API integration tests.
- ViewModel tests.
- UI tests.
- Peer-to-peer synchronization tests.

Business calculations should be testable without an Android device or network connection.

### 14.37 Android Architecture Model Invariants

The following invariants must always hold:

- Core application functionality must not depend on network availability.
- The local database is the primary source for normal application reads.
- UI components must not directly access Room DAOs or REST APIs.
- Business rules must remain independent of Android infrastructure.
- Expense, split, balance, and settlement calculations must work offline.
- Local writes must be persisted before synchronization.
- Synchronization must operate independently from the UI.
- Background synchronization must not block normal application usage.
- Network failures must not cause local data loss.
- Local peer-to-peer communication must remain isolated from core business logic.
- Domain models must not depend directly on Room or Retrofit.
- Remote DTOs must not become the application's core domain model by default.
- Related financial changes must be persisted transactionally where required.
- Synchronization and conflict resolution must update the local database rather than directly manipulating UI state.
- The architecture must support both backend synchronization and local peer-to-peer synchronization.
- The Android application must support one active local user per installation in V1.
- The architecture must remain extensible for future features without redesigning the core expense domain.

## 15. Backend Architecture

### 15.1 Purpose

The SplitSync backend provides centralized synchronization, remote data persistence, authentication, authorization, and cross-device data exchange.

The backend must support the offline-first Android architecture defined in Section 11.

The backend must not become a mandatory dependency for core local expense operations.

The primary backend responsibility is:

> Synchronize and persist shared application data while allowing devices to operate independently when offline.

### 15.2 Backend Technology Stack

V1 backend will use:

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- REST APIs

The backend should be designed as a modular monolithic application for V1.

The architecture must remain extensible so that individual components can later be separated into independent services if required.

### 15.3 Backend Architecture Style

The backend should follow a layered architecture with clear separation between:

- API Layer.
- Application Layer.
- Domain Layer.
- Persistence Layer.
- Synchronization Layer.
- Security Layer.
- Infrastructure Layer.

The conceptual architecture is:

    Android Application
            ↓
        REST API
            ↓
    ┌───────────────────────┐
    │       API Layer       │
    └───────────┬───────────┘
                ↓
    ┌───────────────────────┐
    │   Application Layer   │
    │      Use Cases        │
    └───────────┬───────────┘
                ↓
    ┌───────────────────────┐
    │     Domain Layer      │
    │ Business Rules        │
    └───────────┬───────────┘
                ↓
    ┌───────────────────────┐
    │   Persistence Layer   │
    │ JPA / Hibernate       │
    └───────────┬───────────┘
                ↓
    ┌───────────────────────┐
    │        MySQL          │
    └───────────────────────┘

Synchronization and security operate across the appropriate application layers.

### 15.4 Modular Monolith for V1

V1 should use a modular monolithic backend rather than microservices.

The backend should contain logically separated modules such as:

- User.
- Device.
- Group.
- Group Membership.
- Expense.
- Expense Split.
- Settlement.
- Synchronization.
- Authentication.
- Authorization.

These modules should have clear boundaries even though they run inside the same Spring Boot application.

This approach reduces operational complexity while keeping the codebase ready for future scaling.

### 15.5 API Layer

The API Layer is responsible for exposing versioned REST endpoints to Android clients.

Responsibilities include:

- Request handling.
- Request validation.
- Authentication extraction.
- Authorization checks.
- DTO conversion.
- Calling application services.
- Response generation.
- HTTP error handling.

Controllers must not contain core business logic.

The general flow is:

    HTTP Request
        ↓
    Controller
        ↓
    Application Service
        ↓
    Domain Logic
        ↓
    Repository
        ↓
    MySQL

### 15.6 REST API Versioning

The backend REST APIs must be versioned from the beginning.

The initial API version should use:

    /api/v1/

For example:

    /api/v1/groups
    /api/v1/expenses
    /api/v1/settlements

API versioning must allow future versions to evolve without unexpectedly breaking existing Android clients.

### 15.7 Application Layer

The Application Layer coordinates business operations exposed through the API.

It should contain application services or use-case services such as:

- UserService.
- DeviceService.
- GroupService.
- GroupMembershipService.
- ExpenseService.
- ExpenseSplitService.
- SettlementService.
- SynchronizationService.

The Application Layer is responsible for coordinating:

- Validation.
- Domain operations.
- Transactions.
- Repository operations.
- Synchronization state.

It should not contain HTTP-specific logic.

### 15.8 Domain Layer

The Domain Layer contains the core business rules of SplitSync.

It should define rules related to:

- Groups.
- Membership.
- Expenses.
- Expense Splits.
- Settlements.
- Balance calculations.
- Financial validation.
- Business invariants.

Domain logic must not depend directly on:

- Spring MVC.
- HTTP.
- MySQL.
- REST controllers.

Where practical, the core calculation logic should remain independently testable.

### 15.9 Backend Domain Models

The backend domain model must preserve the stable identifiers used by the Android application.

Important identifiers include:

- `userId`
- `deviceId`
- `groupId`
- `expenseId`
- `expenseSplitId`
- `settlementId`

The backend must not replace these identifiers with unrelated identifiers during synchronization.

Database-specific internal identifiers may exist where useful, but they must not replace the stable business identifiers used for synchronization.

### 15.10 Persistence Layer

The Persistence Layer is responsible for storing backend data in MySQL.

The backend will use:

- Spring Data JPA.
- Hibernate.
- MySQL.

Responsibilities include:

- Entity persistence.
- Queries.
- Transactions.
- Relationship management.
- Database constraints.
- Indexing.
- Persistence-level synchronization support.

The persistence layer must not contain API-specific logic.

### 15.11 Hibernate and JPA

Hibernate will be used as the JPA implementation.

JPA entities should represent persistence concerns rather than becoming the sole representation of the complete business domain.

Where appropriate, the architecture may separate:

- JPA Entities.
- Domain Models.
- Request DTOs.
- Response DTOs.

This prevents database and API concerns from leaking throughout the application.

### 15.12 Repository Layer

Repositories should provide controlled access to persisted data.

Examples include:

- `UserRepository`
- `DeviceRepository`
- `GroupRepository`
- `GroupMemberRepository`
- `ExpenseRepository`
- `ExpenseSplitRepository`
- `SettlementRepository`
- `SyncRepository`

Repositories should focus on persistence and retrieval.

Business workflows should be coordinated by the Application Layer.

### 15.13 Database Transactions

Operations involving multiple related records must use appropriate database transactions.

For example, creating an Expense may involve:

    Begin Transaction
        ↓
    Validate Group
        ↓
    Validate Payer
        ↓
    Create Expense
        ↓
    Create Expense Splits
        ↓
    Create Synchronization Metadata
        ↓
    Commit

If any required operation fails:

    Rollback
        ↓
    No Partial Financial State

Transactions are especially important for:

- Expense creation.
- Expense modification.
- Expense deletion.
- Settlement creation.
- Group membership changes.
- Ownership changes.
- Synchronization application.

### 15.14 Backend Validation

The backend must validate all data received from Android devices.

Client-side validation must not be considered sufficient for security or data integrity.

The backend must validate:

- Entity identifiers.
- Group membership.
- User authorization.
- Expense amounts.
- Expense participants.
- Expense Split allocations.
- Settlement participants.
- Settlement amounts.
- Currency rules.
- Synchronization metadata.
- Business invariants.

The backend must reject invalid data rather than relying on the client to behave correctly.

### 15.15 Backend as Synchronization Authority

The backend acts as the centralized synchronization point when internet connectivity is available.

The backend is responsible for:

- Receiving device changes.
- Validating changes.
- Persisting valid changes.
- Detecting duplicate operations.
- Returning remote changes.
- Maintaining synchronization metadata.
- Supporting eventual consistency across authorized devices.

The backend does not invalidate the local-first nature of the Android application.

### 15.16 Synchronization API

The backend must expose synchronization capabilities through REST APIs.

The synchronization API should support:

- Uploading local changes.
- Downloading remote changes.
- Acknowledging successfully processed changes.
- Detecting conflicts.
- Returning synchronization metadata.
- Supporting incremental synchronization.

The exact API request and response structures will be defined in Section 17.

### 15.17 Incremental Synchronization

The backend should support incremental synchronization rather than requiring the Android client to download the entire dataset after every connection.

Conceptually:

    Android Device
        ↓
    "Give me changes after synchronization point X"
        ↓
    Backend
        ↓
    Changed Entities
        ↓
    Android Local Database

The exact synchronization cursor/version mechanism will be defined in Section 17.

### 15.18 Synchronization Cursor

Each device should maintain synchronization progress information.

A synchronization cursor may represent:

- Last processed server version.
- Last synchronization sequence.
- Last synchronization token.

The exact mechanism will be selected during implementation.

The cursor must allow a device to resume synchronization without reprocessing the entire dataset unnecessarily.

### 15.19 Idempotent Backend Operations

Backend synchronization operations must be idempotent.

If the same operation is received multiple times because of retries, the backend must not create duplicate business records.

For example:

    Device
        ↓
    CREATE Expense E123
        ↓
    Request Timeout
        ↓
    Device Retries
        ↓
    CREATE Expense E123

The backend must still contain only one logical Expense with:

    expenseId = E123

Idempotency must be implemented using stable identifiers and appropriate synchronization metadata.

### 15.20 Backend Concurrency

The backend must support concurrent requests from multiple devices.

For example:

    Device A
        ↓
    Update Expense E123

    Device B
        ↓
    Update Expense E123

The backend must detect whether the changes are:

- Sequential.
- Duplicate.
- Compatible.
- Concurrent.
- Conflicting.

The final conflict behavior is defined in Section 13.

### 15.21 Optimistic Concurrency

The backend should use optimistic concurrency mechanisms for synchronizable entities where appropriate.

The purpose is to detect when a device is modifying an entity based on an outdated version.

Conceptually:

    Entity Version = 5

    Device A updates:
        Version 5 → Version 6

    Device B tries to update:
        Version 5 → ?

The backend must detect that Device B is operating on an outdated version and evaluate the change according to the synchronization and conflict-resolution rules.

The exact versioning mechanism will be finalized during implementation.

### 15.22 Security Architecture

The backend must authenticate and authorize API requests.

Security responsibilities include:

- User authentication.
- Device authentication.
- Access token handling.
- Group-level authorization.
- Resource ownership validation.
- Secure synchronization.
- Protection of sensitive user information.

Authentication and authorization must be separate concepts.

Authentication answers:

    "Who is making this request?"

Authorization answers:

    "Is this user/device allowed to perform this operation?"

The detailed security architecture is defined in Section 18.

### 15.23 Group-Level Authorization

Backend authorization must be evaluated at the group level for protected group resources.

For example, a user must not be allowed to retrieve or modify an Expense merely because the user knows its `expenseId`.

The backend must verify that the authenticated user has appropriate access to the corresponding Group.

The authorization chain is conceptually:

    Request
        ↓
    Authenticated User
        ↓
    Group Membership
        ↓
    Required Permission
        ↓
    Resource Access

### 15.24 Historical Data Protection

Historical financial records must remain consistent even when group membership changes.

For example:

    User Rahul
        ↓
    Participated in Expense E123
        ↓
    Rahul Leaves Group

Expense E123 must continue to reference Rahul's stable `userId`.

Leaving a group must not automatically delete historical financial records.

### 15.25 Error Handling

The backend must return consistent API error responses.

Errors should be categorized appropriately, including:

- Validation errors.
- Authentication errors.
- Authorization errors.
- Resource-not-found errors.
- Conflict errors.
- Rate-limit errors where applicable.
- Temporary server errors.
- Unexpected server errors.

The API should use appropriate HTTP status codes and a consistent error response structure.

The exact response format will be defined in Section 17.

### 15.26 Backend Logging

The backend should provide structured logging for operational and synchronization diagnostics.

Logs may include:

- Request correlation ID.
- Device ID where appropriate.
- User ID where appropriate.
- Synchronization operation ID.
- Entity type.
- Entity ID.
- Operation result.
- Error category.

Sensitive information such as passwords, authentication secrets, or unnecessary financial details must not be logged.

### 15.27 Backend Observability

The backend should expose sufficient operational information to monitor:

- API availability.
- Request failures.
- Synchronization failures.
- Database failures.
- Processing latency.
- Conflict frequency.
- Retry patterns.

The exact observability stack is a deployment concern and may evolve independently of the core application architecture.

### 15.28 Database Constraints

Important business invariants should be enforced at multiple levels where practical.

The system should use:

- Application validation.
- Domain validation.
- Database constraints.

Examples include:

- Stable business identifiers must be unique.
- Required relationships must exist.
- Monetary values must satisfy applicable constraints.
- Duplicate group membership must be prevented.
- Foreign-key relationships must remain valid where applicable.

Database constraints must complement rather than replace domain validation.

### 15.29 Backend and Offline Operation

The backend does not need to be available for a device to:

- Create expenses.
- Calculate splits.
- Calculate local balances.
- Record settlements.
- Manage locally supported group operations.

These operations are performed locally on Android.

The backend becomes relevant when:

- Synchronization is required.
- Cross-device data must be exchanged through the server.
- Remote backup/persistence is required.
- Online authentication or account operations are required.

### 15.30 Backend and Peer-to-Peer Synchronization

The backend and local peer-to-peer synchronization mechanisms must share the same business entity model.

For example:

    Backend Synchronization
            ↓
        expenseId = E123

and:

    Peer-to-Peer Synchronization
            ↓
        expenseId = E123

Both must represent the same logical Expense.

The backend must not create a second business identity simply because the change arrived through a different synchronization path.

### 15.31 Backend Scalability

The V1 backend should be designed as a stateless application layer wherever practical.

Application instances should not depend on in-memory state for business correctness.

Shared state should be stored in appropriate persistent or distributed infrastructure.

This allows the backend to scale horizontally in the future.

The initial deployment may use a single Spring Boot instance, but the architecture should not prevent multiple instances later.

### 15.32 Database Scalability

MySQL will be the primary backend database for V1.

The database design should consider:

- Proper indexing.
- Foreign keys.
- Query efficiency.
- Transaction boundaries.
- Synchronization queries.
- Group-based data access.
- Large expense histories.

Indexes should be designed around actual query patterns rather than added indiscriminately.

The final database schema and indexing strategy will be defined in Section 16.

### 15.33 Caching

Caching is not required for the initial V1 architecture.

The system should prioritize:

- Correctness.
- Offline synchronization.
- Transactional integrity.
- Simple data access.

Caching may be introduced later for suitable read-heavy workloads without changing the core domain model.

### 15.34 Background Processing

Backend operations that do not need to block an API response may eventually use asynchronous or background processing.

Potential candidates include:

- Large synchronization operations.
- Cleanup of synchronization metadata.
- Tombstone cleanup.
- Analytics generation.
- Notifications.

V1 should avoid unnecessary asynchronous complexity unless required by the actual synchronization design.

### 15.35 Backend Testing

The backend must support multiple levels of testing.

The testing strategy should include:

- Domain unit tests.
- Service/use-case tests.
- Repository tests.
- REST controller tests.
- Database integration tests.
- Synchronization tests.
- Conflict-resolution tests.
- Security and authorization tests.
- End-to-end API tests.

Financial calculations and business rules must be covered with deterministic test cases.

### 15.36 Backend Architecture Model Invariants

The following invariants must always hold:

- The backend must support the offline-first Android architecture.
- Core local expense operations must not depend on backend availability.
- Backend APIs must be versioned.
- Controllers must not contain core business logic.
- Business rules must remain separated from HTTP and persistence concerns.
- Spring Boot will provide the backend application framework.
- Java will be used for backend development.
- Hibernate/JPA will be used for persistence.
- MySQL will be used as the backend database.
- Synchronizable business identifiers must remain stable across devices and backend synchronization.
- Backend synchronization must be idempotent.
- Duplicate synchronization must not create duplicate business records.
- Backend validation must not rely solely on client-side validation.
- Group-level authorization must protect shared group data.
- Financial operations must use appropriate database transactions.
- Concurrent updates must be detected and handled according to Section 13.
- Backend failures must not cause loss of locally persisted Android data.
- The backend must support incremental synchronization.
- The architecture must support future horizontal scaling.
- Backend and peer-to-peer synchronization must use the same business entity identities.
- Security-sensitive information must be protected and must not be unnecessarily exposed through logs or API responses.

## 16. Database Architecture

### 16.1 Purpose

The Database Architecture defines how SplitSync stores, organizes, validates, and retrieves persistent data on both the Android device and the backend.

SplitSync uses two database environments:

- Android local database using Room and SQLite.
- Backend database using MySQL with Hibernate/JPA.

The local and backend databases serve different operational purposes but must preserve the same core business identities and relationships.

The primary principle is:

> Local data must remain complete enough to support core application functionality without network connectivity.

### 16.2 Database Architecture Overview

The overall database architecture is:

    Android Device
        ↓
    Room
        ↓
    SQLite
        ↓
    Local Application State

and:

    Android Device
        ↓
    REST API
        ↓
    Spring Boot
        ↓
    Hibernate / JPA
        ↓
    MySQL
        ↓
    Shared Remote Application State

Synchronization connects the two database environments.

### 16.3 Local Database

The Android application will use Room as the persistence framework over SQLite.

The local database is responsible for storing:

- Local user profile.
- Device information.
- Groups.
- Group memberships.
- Expenses.
- Expense splits.
- Settlements.
- Synchronization metadata.
- Conflict information where required.

The local database must be usable without internet connectivity.

### 16.4 Backend Database

The backend will use MySQL as the primary persistent database.

Hibernate/JPA will provide the object-relational persistence layer.

The backend database will store:

- Users.
- Devices.
- Groups.
- Group memberships.
- Expenses.
- Expense splits.
- Settlements.
- Synchronization metadata where required.
- Security-related persistent data where required.
- Audit information where required.

The final backend schema will be implemented using appropriate JPA entities and MySQL constraints.

### 16.5 Stable Business Identifiers

All synchronizable business entities must have stable identifiers.

The primary identifiers are:

- `userId`
- `deviceId`
- `groupId`
- `expenseId`
- `expenseSplitId`
- `settlementId`

These identifiers must remain unchanged across:

- Local creation.
- Backend synchronization.
- Peer-to-peer synchronization.
- Application restarts.
- Device restarts.

The backend must not replace these identifiers with different synchronization identities.

### 16.6 Identifier Generation

Identifiers for synchronizable entities should be generated using UUID-based identifiers.

For example:

    groupId
        ↓
    UUID generated locally

    expenseId
        ↓
    UUID generated locally

    settlementId
        ↓
    UUID generated locally

This allows entities to be created offline without requiring a database-generated identifier from the backend.

### 16.7 Local Database Entities

The initial Android Room database is expected to contain the following entities:

- `UserEntity`
- `DeviceEntity`
- `GroupEntity`
- `GroupMemberEntity`
- `ExpenseEntity`
- `ExpenseSplitEntity`
- `SettlementEntity`
- `SyncOperationEntity`
- `ConflictEntity`

Additional entities may be introduced when required by synchronization, security, or future features.

### 16.8 Backend Database Entities

The initial backend persistence model is expected to contain corresponding persistence entities:

- `User`
- `Device`
- `Group`
- `GroupMember`
- `Expense`
- `ExpenseSplit`
- `Settlement`

Additional persistence entities may be introduced for:

- Synchronization metadata.
- Authentication.
- Authorization.
- Auditing.
- Conflict handling.

### 16.9 User Table

The User model stores the application's user identity and profile information.

Conceptually, the User record includes:

- `userId`
- `displayName`
- `phoneNumber` where applicable
- `email` where applicable
- `createdAt`
- `updatedAt`

The exact required profile fields are defined in Section 5.

User identity must not depend on display name, phone number, or email address alone.

The stable `userId` is the primary business identity.

### 16.10 Device Table

The Device model stores application-level device information.

Conceptually, the Device record includes:

- `deviceId`
- `userId`
- `createdAt`
- `updatedAt`
- Device status information where required.

A user may have device-related records according to the V1 device lifecycle rules.

The exact device model is defined in Section 6.

### 16.11 Group Table

The Group table stores the core group information.

Conceptually, it includes:

- `groupId`
- `name`
- `groupType`
- `description`
- `createdBy`
- `createdAt`
- `updatedAt`

A group must have a stable `groupId`.

The group creator must be represented through a stable user reference.

### 16.12 Group Membership Table

Group membership must be represented separately from the Group table.

Conceptually:

    Group
       ↓
    GroupMember
       ↓
    User

A Group Membership record may contain:

- `groupMemberId`
- `groupId`
- `userId`
- `role`
- `status`
- `joinedAt`
- `updatedAt`

The exact membership identifier strategy will be finalized during implementation.

A user must not have duplicate active membership in the same group.

### 16.13 Expense Table

The Expense table stores the core expense record.

Conceptually, it includes:

- `expenseId`
- `groupId`
- `description`
- `amount`
- `currency`
- `paidBy`
- `createdBy`
- `expenseDate`
- `category`
- `notes`
- `createdAt`
- `updatedAt`
- Deletion metadata where required.

The expense must reference its parent Group using `groupId`.

The payer and creator must reference stable `userId` values.

### 16.14 Expense Split Table

Expense Split records must be stored separately from the Expense table.

Conceptually:

    Expense
       ↓
    ExpenseSplit
       ↓
    User

An Expense Split record includes:

- `expenseSplitId`
- `expenseId`
- `userId`
- `splitType`
- `shareValue`
- `allocatedAmount`
- `createdAt`
- `updatedAt`

The exact representation of `shareValue` may depend on the selected split type.

The Expense Split table must enforce the relationship between an expense and its participants.

### 16.15 Settlement Table

The Settlement table stores payments between group members.

Conceptually, it includes:

- `settlementId`
- `groupId`
- `paidBy`
- `paidTo`
- `amount`
- `currency`
- `createdBy`
- `settlementDate`
- `notes`
- `createdAt`
- `updatedAt`
- Deletion metadata where required.

A settlement must reference stable user and group identities.

### 16.16 Synchronization Operation Table

The local database must maintain synchronization information for changes that need to be exchanged with other systems.

The `SyncOperationEntity` may contain:

- `syncOperationId`
- `entityType`
- `entityId`
- `operationType`
- `deviceId`
- `createdAt`
- `updatedAt`
- `status`
- `retryCount`
- `lastAttemptAt`
- `lastError`
- Version information where required.

The exact structure will be finalized during implementation of Section 12.

### 16.17 Conflict Table

The local database may contain a dedicated Conflict entity for unresolved synchronization conflicts.

A Conflict record may contain:

- `conflictId`
- `entityType`
- `entityId`
- Local version information.
- Remote version information.
- Conflict type.
- Conflict status.
- Created timestamp.
- Resolution timestamp where applicable.
- Resolution metadata where required.

Conflict data must be protected because it may contain sensitive financial information.

### 16.18 Relationships

The primary database relationships are:

    User
      ↓
    GroupMember
      ↓
    Group

    Group
      ↓
    Expense
      ↓
    ExpenseSplit
      ↓
    User

    Group
      ↓
    Settlement
      ↓
    User

The logical relationship model is:

    User
      │
      ├───────────────┐
      ↓               ↓
    GroupMember    Expense
                      ↓
                ExpenseSplit

    Group
      ↓
    Settlement

### 16.19 Foreign Key Relationships

Where appropriate, the backend database should use foreign-key constraints to preserve referential integrity.

Examples include:

- Group Member → Group.
- Group Member → User.
- Expense → Group.
- Expense → User.
- Expense Split → Expense.
- Expense Split → User.
- Settlement → Group.
- Settlement → User.

The Android Room database should also define appropriate relationships and foreign-key constraints where they are compatible with offline synchronization requirements.

### 16.20 Historical References

Historical financial records must reference stable identifiers.

For example:

    Expense
        ↓
    paidBy = userId

The system must not store only the user's current display name as the financial relationship.

If the user changes their display name, historical records must continue to resolve to the same `userId`.

The same principle applies to:

- Expense Splits.
- Settlements.
- Group memberships.
- Group ownership.

### 16.21 Monetary Data Types

Financial amounts must not use binary floating-point types such as `float` or `double`.

The application should use exact monetary representations.

For Android, an appropriate representation may be:

- Integer minor units where supported by the currency model.

For example:

    ₹100.50
        ↓
    10050 minor units

For backend MySQL, an appropriate representation may use:

    DECIMAL

The exact precision and scale will be finalized during implementation.

The Android and backend representations must produce consistent financial calculations.

### 16.22 Currency Storage

Every financial record must have an explicit currency according to the business rules defined in Sections 8, 9, and 10.

For V1, a Group should use a single primary currency for its expenses and settlements.

Currency values should use a standardized representation such as an ISO currency code where applicable.

Multi-currency support is outside the initial V1 calculation model.

### 16.23 Date and Time Storage

The database must distinguish between business dates and system timestamps.

Examples:

- `expenseDate` — date of the actual expense.
- `settlementDate` — date of the actual settlement.
- `createdAt` — creation timestamp.
- `updatedAt` — last modification timestamp.

System timestamps should use a consistent representation across Android and backend systems.

The exact timezone strategy will be finalized during implementation.

### 16.24 Soft Deletion and Tombstones

Synchronizable entities may require logical deletion rather than immediate physical deletion.

For example:

    Expense E123
        ↓
    Deleted on Device A
        ↓
    Tombstone / Deletion Metadata
        ↓
    Synchronization
        ↓
    Device B learns about deletion

This prevents a deleted record from unexpectedly reappearing on another device during synchronization.

The exact tombstone structure and retention period will be defined during synchronization implementation.

### 16.25 Database Transactions

Financially related database changes must be applied transactionally.

For example, creating an expense should ensure that:

    Expense
        +
    Expense Splits
        +
    Synchronization Metadata

are persisted consistently.

A partially persisted financial operation must not be treated as successful.

### 16.26 Expense Split Integrity

The database design must support validation of the relationship:

    Sum of Expense Split Allocations
        =
    Expense Amount

The exact enforcement mechanism may involve:

- Application-level validation.
- Domain-level validation.
- Transactional persistence.
- Backend validation.
- Database constraints where practical.

The database alone should not be relied upon for complex financial validation.

### 16.27 Group Membership Integrity

The database must prevent duplicate active membership for the same user within the same group.

Conceptually:

    groupId + userId
        ↓
    Unique Active Membership

Historical membership states must remain available where required for synchronization and auditability.

### 16.28 Indexing Strategy

Indexes must be designed around actual application and synchronization query patterns.

Important indexing candidates include:

- `userId`
- `deviceId`
- `groupId`
- `expenseId`
- `settlementId`
- `expenseDate`
- `createdAt`
- Synchronization status.
- Synchronization version or cursor fields.

Composite indexes may be used where queries commonly filter by multiple fields.

Indexes must be introduced based on measured or expected query patterns rather than indiscriminately.

### 16.29 Group-Based Query Optimization

A large portion of application data is accessed within the context of a Group.

Common queries include:

    Get Group Expenses
    Get Group Members
    Get Group Settlements
    Calculate Group Balances

The database should therefore optimize group-based access patterns.

For example, `groupId` should be indexed on entities that are frequently queried by group.

### 16.30 Synchronization Query Optimization

Synchronization may require queries such as:

    Get Pending Changes

    Get Changes After Version X

    Get Changes For Device Y

The database should provide appropriate indexes for synchronization metadata.

Synchronization queries must avoid scanning the entire database unnecessarily.

### 16.31 Local Database Migrations

The Room database schema must use explicit database migrations.

When the local schema changes:

    Existing Database
        ↓
    Migration
        ↓
    New Database Schema

The migration process must preserve user data.

Destructive migration must not be used casually for production data.

### 16.32 Backend Database Migrations

The MySQL schema must also use version-controlled database migrations.

A migration framework such as Flyway or Liquibase may be used.

The selected migration system must ensure that:

- Schema changes are versioned.
- Migrations are repeatable in controlled environments where appropriate.
- Production schema changes are traceable.
- Data migrations can be handled explicitly.

The final migration tool will be selected during implementation.

### 16.33 Local and Backend Schema Independence

The Android Room schema and backend MySQL schema do not need to be physically identical.

They must, however, preserve compatible business concepts and stable identifiers.

For example:

    Android
    ExpenseEntity

and:

    Backend
    Expense

may have different technical fields while both represent the same logical Expense.

The synchronization layer is responsible for mapping between these representations.

### 16.34 Entity and Domain Model Separation

Database entities should remain persistence-specific.

The architecture should avoid allowing database-specific concerns to spread into the Domain Layer.

The preferred flow is:

    Database Entity
        ↓
    Mapper
        ↓
    Domain Model

and:

    Domain Model
        ↓
    Mapper
        ↓
    Database Entity

This keeps database schema changes isolated from business logic.

### 16.35 Data Integrity

Database integrity must be protected at multiple levels:

    UI Validation
        ↓
    Domain Validation
        ↓
    Repository Validation
        ↓
    Database Constraints
        ↓
    Transactional Persistence

No single layer should be considered sufficient for all integrity requirements.

### 16.36 Database Security

Sensitive information stored in the database must be protected according to the Security Architecture.

The Android database should be protected against unauthorized application-level access where practical.

Authentication credentials, tokens, and cryptographic secrets must not be stored as ordinary unprotected business data.

The exact Android local-storage security mechanism will be defined in Section 18.

### 16.37 Database Backup and Recovery

The backend database must support an appropriate backup and recovery strategy.

The exact production backup policy is outside the core V1 application architecture but should include:

- Regular backups.
- Backup verification.
- Recovery procedures.
- Appropriate retention.

The Android local database is not considered the sole permanent backup mechanism.

Backend synchronization provides the mechanism for restoring shared application data across authorized devices where supported.

### 16.38 Database Performance

Database performance must be considered for:

- Large groups.
- Large expense histories.
- Frequent synchronization.
- Multiple devices.
- Repeated balance calculations.

The application should avoid loading unnecessary records into memory.

Queries should retrieve only the data required for the current operation.

Pagination should be introduced where datasets may become large.

### 16.39 Balance Calculation Data

The database should store the source financial records required to calculate balances.

The primary source data is:

    Expenses
        +
    Expense Splits
        +
    Settlements

The application should not depend on storing a permanently calculated balance as the only source of truth.

Balances should be derivable from authoritative financial records.

Future performance optimizations may introduce cached or materialized balance data, but such data must remain derived from the authoritative records.

### 16.40 Database and Synchronization Consistency

When a local business operation is created, the business data and the corresponding synchronization metadata must be persisted consistently.

For example:

    Begin Transaction
        ↓
    Save Expense
        ↓
    Save Expense Splits
        ↓
    Save Sync Operation
        ↓
    Commit

If synchronization metadata is missing after a successful local operation, the change could become invisible to synchronization.

Therefore, the local database transaction must protect the relationship between business changes and synchronization state.

### 16.41 Database and Conflict Resolution

Conflict-resolution operations must use database transactions when multiple related records are changed.

For example, resolving an Expense conflict may require updating:

    Expense
        +
    Expense Splits
        +
    Conflict State
        +
    Synchronization State

These changes should be committed atomically where required.

### 16.42 Database Cleanup

The system may eventually need to clean up:

- Old synchronization metadata.
- Resolved conflicts.
- Expired tombstones.
- Temporary records.

Cleanup must not remove information that is still required by an unsynchronized device or for historical integrity.

The exact retention and cleanup strategy will be defined during synchronization implementation.

### 16.43 Database Testing

The database architecture must be tested at multiple levels.

Testing should include:

- Room DAO tests.
- Room migration tests.
- Backend repository tests.
- MySQL integration tests.
- Constraint tests.
- Transaction tests.
- Synchronization persistence tests.
- Conflict persistence tests.
- Large dataset performance tests.

Financial integrity must be explicitly tested.

### 16.44 Database Architecture Model Invariants

The following invariants must always hold:

- Android uses Room with SQLite for local persistence.
- Backend uses MySQL with Hibernate/JPA.
- Stable business identifiers remain unchanged across synchronization.
- Core local application data must remain available without internet connectivity.
- Financial amounts must use exact monetary representations.
- Floating-point types must not be used for financial calculations.
- Expenses must belong to valid Groups.
- Expense Splits must belong to valid Expenses.
- Settlements must belong to valid Groups.
- Financial records must reference stable user identities.
- Duplicate active group membership must be prevented.
- Related financial records must be persisted transactionally where required.
- Synchronization metadata must remain consistent with local business changes.
- Deletions must be representable for synchronization.
- Database migrations must preserve existing user data.
- Local and backend schemas may differ technically but must preserve compatible business concepts.
- Balance calculations must be derivable from authoritative financial records.
- Database constraints must complement domain and application validation.
- Synchronization and conflict-resolution changes must preserve database integrity.
- Database cleanup must not remove data required for synchronization or historical financial integrity.

## 17. API Architecture

### 17.1 Purpose

The API Architecture defines how SplitSync Android clients communicate with the Spring Boot backend.

The API layer must support:

- User and device operations.
- Group management.
- Group membership.
- Expense management.
- Expense Split management.
- Settlement management.
- Synchronization.
- Authentication and authorization.
- Conflict reporting where required.

The API must support the offline-first architecture defined in Section 11.

The Android application must not require an API response to complete core local operations.

The primary principle is:

> REST APIs synchronize shared state; they do not control core local application availability.

### 17.2 API Technology

V1 backend APIs will use:

- Java.
- Spring Boot.
- Spring Web.
- REST.
- JSON.
- HTTP/HTTPS.

The API will communicate with the Android application over HTTPS in production environments.

### 17.3 API Versioning

All public application APIs must be versioned.

V1 will use:

    /api/v1/

Examples:

    /api/v1/users
    /api/v1/groups
    /api/v1/expenses
    /api/v1/settlements
    /api/v1/sync

API versioning must allow future versions to evolve without unexpectedly breaking existing Android clients.

### 17.4 API Design Principles

The API must follow these principles:

- Resource-oriented REST design.
- Explicit request and response contracts.
- Stable business identifiers.
- Stateless HTTP requests where practical.
- Idempotent synchronization operations.
- Consistent error responses.
- Explicit authorization.
- Validation at the backend.
- Incremental synchronization.
- Backward compatibility within an API version.
- No dependence on client-side validation for security.

### 17.5 API Resource Model

The primary API resources are:

- Users.
- Devices.
- Groups.
- Group Members.
- Expenses.
- Expense Splits.
- Settlements.
- Synchronization.

Conceptually:

    User
      ↓
    Group Membership
      ↓
    Group
      ├── Expenses
      │      └── Expense Splits
      │
      └── Settlements

### 17.6 Resource Identifiers

API resources must use stable business identifiers.

Examples:

    /api/v1/groups/{groupId}

    /api/v1/expenses/{expenseId}

    /api/v1/settlements/{settlementId}

The identifiers used by the API must correspond to the stable identifiers used by the Android local database.

The backend must not replace an offline-created business identifier with a different identifier after synchronization.

### 17.7 HTTP Methods

The API should use standard HTTP methods according to resource behavior.

The initial mapping is:

- `GET` — Retrieve resources.
- `POST` — Create resources or submit operations where appropriate.
- `PUT` — Replace/update a resource where appropriate.
- `PATCH` — Partially update a resource where appropriate.
- `DELETE` — Request deletion where supported.

Synchronization endpoints may use operation-specific request structures rather than mapping every synchronization operation directly to standard CRUD endpoints.

### 17.8 User APIs

User APIs are responsible for backend user-related operations.

Potential endpoints include:

    POST /api/v1/users

    GET /api/v1/users/{userId}

    PATCH /api/v1/users/{userId}

The exact user API contract will be finalized during implementation.

The API must preserve the stable `userId`.

Profile information such as display name, phone number, or email must not be treated as the permanent technical identity of the user.

### 17.9 Device APIs

Device APIs manage application-level device registration and synchronization identity.

Potential endpoints include:

    POST /api/v1/devices

    GET /api/v1/devices/{deviceId}

    PATCH /api/v1/devices/{deviceId}

The `deviceId` must remain stable for the applicable device lifecycle.

The API must not depend on Android hardware identifiers as the primary application-level device identity.

### 17.10 Group APIs

Group APIs manage group-level resources.

Potential endpoints include:

    POST /api/v1/groups

    GET /api/v1/groups/{groupId}

    PATCH /api/v1/groups/{groupId}

    DELETE /api/v1/groups/{groupId}

A group created offline may later be synchronized through the API using its existing `groupId`.

The backend must recognize the synchronized group as the same logical group rather than creating a duplicate group.

### 17.11 Group Membership APIs

Group membership operations must be explicitly represented.

Potential endpoints include:

    GET /api/v1/groups/{groupId}/members

    POST /api/v1/groups/{groupId}/members

    PATCH /api/v1/groups/{groupId}/members/{userId}

    DELETE /api/v1/groups/{groupId}/members/{userId}

Membership operations must validate:

- User identity.
- Group identity.
- Membership status.
- Authorization.
- Role permissions.
- Duplicate membership rules.

The exact invitation and joining API will be finalized after the peer-to-peer and security architecture is defined.

### 17.12 Expense APIs

Expense APIs manage expense resources.

Potential endpoints include:

    GET /api/v1/groups/{groupId}/expenses

    POST /api/v1/groups/{groupId}/expenses

    GET /api/v1/expenses/{expenseId}

    PATCH /api/v1/expenses/{expenseId}

    DELETE /api/v1/expenses/{expenseId}

Expense requests must include sufficient information to validate:

- Group.
- Creator.
- Payer.
- Amount.
- Currency.
- Participants.
- Expense Splits.

The backend must validate the complete financial state before committing the operation.

### 17.13 Expense Split API Representation

Expense Splits are logically associated with their parent Expense.

For normal expense operations, the API may represent Expense Splits as part of the Expense request and response rather than requiring independent client operations.

For example:

    POST /api/v1/groups/{groupId}/expenses

may contain:

    Expense
        +
    Expense Split Configuration
        +
    Calculated Allocations

The API must ensure that the Expense and its Expense Splits are persisted consistently.

Independent Expense Split endpoints may be introduced if required by the synchronization implementation.

### 17.14 Settlement APIs

Settlement APIs manage settlement resources.

Potential endpoints include:

    GET /api/v1/groups/{groupId}/settlements

    POST /api/v1/groups/{groupId}/settlements

    GET /api/v1/settlements/{settlementId}

    PATCH /api/v1/settlements/{settlementId}

    DELETE /api/v1/settlements/{settlementId}

The backend must validate:

- Group membership.
- `paidBy`.
- `paidTo`.
- Settlement amount.
- Currency.
- Authorization.

A settlement must not be treated as an Expense.

### 17.15 Balance API

The application may expose a balance endpoint for online clients where useful.

For example:

    GET /api/v1/groups/{groupId}/balances

However, the Android application must not depend on this endpoint for normal balance calculation.

The local Android database must be capable of calculating balances from:

    Expenses
        +
    Expense Splits
        +
    Settlements

The server-provided balance may be used for validation, comparison, optimization, or future features.

### 17.16 Synchronization API

Synchronization requires dedicated API operations.

A conceptual synchronization API may include:

    POST /api/v1/sync/push

    GET /api/v1/sync/pull

or an equivalent combined synchronization endpoint.

The synchronization API must support:

- Uploading local changes.
- Downloading remote changes.
- Acknowledging processed changes.
- Incremental synchronization.
- Duplicate detection.
- Conflict detection.
- Synchronization cursors or versions.

The exact request and response structure will be finalized during implementation.

### 17.17 Push Synchronization

The Push operation sends locally persisted changes from an Android device to the backend.

Conceptually:

    Android Local Database
            ↓
    Pending Sync Operations
            ↓
    POST /api/v1/sync/push
            ↓
    Backend Validation
            ↓
    Apply Changes
            ↓
    Return Result

The backend must process each operation safely and return enough information for the Android synchronization layer to update local synchronization state.

### 17.18 Pull Synchronization

The Pull operation retrieves changes that the Android device has not yet processed.

Conceptually:

    Android Device
        ↓
    Synchronization Cursor
        ↓
    GET /api/v1/sync/pull
        ↓
    Backend
        ↓
    Changes Since Cursor
        ↓
    Android Local Database

The backend must support incremental synchronization.

The Android client should not need to download the complete group dataset after every synchronization.

### 17.19 Combined Synchronization

The final implementation may use a combined synchronization endpoint if it simplifies synchronization.

For example:

    POST /api/v1/sync

The request may contain:

- Device identity.
- Current synchronization cursor.
- Pending local operations.

The response may contain:

- Accepted operations.
- Rejected operations.
- Conflicts.
- Remote changes.
- New synchronization cursor.

The exact design will be selected during synchronization implementation.

### 17.20 Synchronization Request Structure

A synchronization request should contain enough information to uniquely identify each change.

Conceptually:

    Synchronization Request
        ├── deviceId
        ├── synchronizationCursor
        └── operations
              ├── operationId
              ├── entityType
              ├── entityId
              ├── operationType
              ├── entityVersion
              └── payload

The exact JSON schema will be defined during implementation.

### 17.21 Synchronization Response Structure

A synchronization response should provide enough information for the Android client to update its local state.

Conceptually:

    Synchronization Response
        ├── acceptedOperations
        ├── rejectedOperations
        ├── conflicts
        ├── remoteChanges
        └── nextCursor

The response must allow the Android synchronization layer to distinguish between:

- Successfully synchronized changes.
- Retryable failures.
- Permanent failures.
- Conflicts.
- New remote changes.

### 17.22 Idempotency

Synchronization requests must be idempotent.

The backend must support safe retrying of the same synchronization operation.

Each synchronization operation must have a stable operation identifier.

For example:

    operationId = OP123

If:

    OP123

is received multiple times, the backend must not create duplicate business records.

The backend should return the previous processing result where appropriate.

### 17.23 API Concurrency Control

Synchronizable resource updates should support optimistic concurrency.

A request may include version information such as:

    entityVersion = 5

If the server currently contains:

    entityVersion = 6

the backend must detect that the client is operating on an outdated version.

The backend must then:

- Reject the stale update.
- Evaluate whether it can be merged.
- Or return a conflict.

The exact versioning mechanism will be finalized during implementation.

### 17.24 API Error Response

All API errors should follow a consistent response structure.

A conceptual error response may contain:

    {
        "code": "RESOURCE_CONFLICT",
        "message": "The resource has been modified.",
        "details": {},
        "requestId": "..."
    }

The exact JSON structure will be finalized during implementation.

Error responses must not expose:

- Database internals.
- Stack traces.
- Sensitive authentication information.
- Internal infrastructure details.

### 17.25 HTTP Status Codes

The API should use appropriate HTTP status codes.

Examples include:

- `200 OK` — Successful retrieval or operation.
- `201 Created` — Resource successfully created.
- `202 Accepted` — Operation accepted for asynchronous processing where applicable.
- `204 No Content` — Successful operation without response body.
- `400 Bad Request` — Invalid request.
- `401 Unauthorized` — Authentication required or invalid.
- `403 Forbidden` — Authenticated but not authorized.
- `404 Not Found` — Resource not found.
- `409 Conflict` — Synchronization or resource conflict.
- `422 Unprocessable Entity` — Valid request structure but invalid business data where appropriate.
- `429 Too Many Requests` — Rate limiting where applicable.
- `500 Internal Server Error` — Unexpected server error.
- `503 Service Unavailable` — Temporary backend unavailability.

The exact status-code mapping will be standardized during implementation.

### 17.26 Request Validation

API requests must be validated before reaching business logic.

Validation should include:

- Required fields.
- Field formats.
- String lengths.
- Numeric ranges.
- UUID formats.
- Currency format.
- Date and timestamp formats.

After request-level validation, the Application and Domain Layers must perform business validation.

### 17.27 Business Validation

Request validation alone is not sufficient.

Business validation must verify:

- User authorization.
- Group membership.
- Expense participants.
- Expense Split reconciliation.
- Settlement participants.
- Ownership rules.
- Membership rules.
- Synchronization versions.
- Financial invariants.

The backend must reject invalid business states even if the request is syntactically valid.

### 17.28 Authorization

Every protected API operation must verify authorization.

For example:

    GET /api/v1/groups/{groupId}/expenses

must verify that the authenticated user or device is authorized to access the specified Group.

Knowing a `groupId` or `expenseId` must not be sufficient to access protected data.

The exact authentication and authorization mechanism is defined in Section 18.

### 17.29 API Security

Production APIs must use HTTPS.

The API must protect:

- Authentication credentials.
- Access tokens.
- User profile information.
- Group data.
- Expense data.
- Settlement data.
- Synchronization payloads.

The backend must validate authenticated requests before accessing protected resources.

### 17.30 Pagination

Collection endpoints should support pagination where datasets may become large.

Potential examples include:

    GET /api/v1/groups/{groupId}/expenses?page=0&size=50

    GET /api/v1/groups/{groupId}/settlements?page=0&size=50

The synchronization API should use cursor/version-based incremental synchronization rather than normal UI pagination for change propagation.

### 17.31 Filtering and Sorting

Collection APIs may support controlled filtering and sorting where required.

For example:

    GET /api/v1/groups/{groupId}/expenses?from=...&to=...

The available filters must be explicitly defined by the API contract.

Clients must not be allowed to construct arbitrary database queries through API parameters.

### 17.32 API and Offline-First Behavior

The Android client must not interpret API unavailability as a reason to disable core local functionality.

For example:

    POST /api/v1/expenses
        ↓
    Backend Unavailable

must not result in:

    Expense Creation Failed

if the expense has already been successfully persisted locally.

Instead:

    Local Expense Created
        ↓
    Sync Pending
        ↓
    Backend Unavailable
        ↓
    Retry Later

### 17.33 API and Peer-to-Peer Synchronization

Peer-to-peer synchronization does not need to use the backend REST API directly.

However, the data exchanged through peer-to-peer synchronization must follow the same business models and synchronization semantics.

For example:

    Backend Sync
        ↓
    Expense E123

and:

    Peer Sync
        ↓
    Expense E123

must represent the same logical Expense.

The peer-to-peer transport is defined separately in Section 19.

### 17.34 API DTOs

API requests and responses should use dedicated DTOs.

The preferred flow is:

    Android Request
        ↓
    Request DTO
        ↓
    Application Service
        ↓
    Domain Model

and:

    Domain Model
        ↓
    Response DTO
        ↓
    JSON Response
        ↓
    Android

DTOs should prevent internal database entities from becoming public API contracts.

### 17.35 API Mapping

The backend should use explicit mapping between:

- Request DTOs.
- Domain models.
- JPA entities.
- Response DTOs.

Conceptually:

    Request DTO
        ↓
    Domain Model
        ↓
    JPA Entity

and:

    JPA Entity
        ↓
    Domain Model
        ↓
    Response DTO

This keeps API and database changes isolated from core business logic.

### 17.36 API Transaction Boundaries

Operations involving multiple related database records must execute within appropriate backend transactions.

For example:

    POST /api/v1/groups/{groupId}/expenses
        ↓
    Validate Request
        ↓
    Validate Group
        ↓
    Validate Payer
        ↓
    Validate Splits
        ↓
    Save Expense
        ↓
    Save Expense Splits
        ↓
    Commit

If validation or persistence fails, the complete operation must fail without leaving partial financial data.

### 17.37 API Observability

Each API request should have a correlation or request identifier.

This identifier can be used to trace:

    Android
        ↓
    REST API
        ↓
    Application Service
        ↓
    Database
        ↓
    Response

Synchronization requests should additionally expose appropriate synchronization operation identifiers for diagnostics.

Sensitive information must not be included unnecessarily in logs.

### 17.38 API Rate Limiting

Rate limiting may be introduced to protect backend resources from excessive requests.

Rate limiting should primarily protect:

- Authentication endpoints.
- Synchronization endpoints.
- Resource-intensive operations.

Rate limiting must not interfere with the normal offline-first behavior of the Android client.

A rate-limited synchronization operation must remain pending locally and be retried according to the synchronization strategy.

### 17.39 API Compatibility

Within `/api/v1`, backward compatibility should be maintained wherever practical.

Breaking changes should require:

    /api/v2/

The backend should support older Android clients for an appropriate period where required.

The exact client-version compatibility policy will be defined before production deployment.

### 17.40 API Testing

The API architecture must support:

- Controller tests.
- Request validation tests.
- Authentication tests.
- Authorization tests.
- Service tests.
- Repository integration tests.
- Synchronization API tests.
- Idempotency tests.
- Concurrency tests.
- Conflict-response tests.
- Error-response tests.
- End-to-end API tests.

Financial APIs must include tests that verify that invalid financial states cannot be persisted.

### 17.41 API Architecture Model Invariants

The following invariants must always hold:

- APIs must be versioned.
- V1 APIs must use `/api/v1/`.
- REST APIs must not be required for core offline application functionality.
- Stable business identifiers must remain unchanged during synchronization.
- Synchronization operations must be idempotent.
- Duplicate synchronization must not create duplicate business records.
- Backend validation must be performed independently of Android validation.
- Protected resources must require appropriate authorization.
- Financially related operations must be transactionally consistent.
- Expense and Expense Split data must remain financially consistent.
- Settlement data must remain financially consistent.
- API DTOs must remain separate from persistence entities.
- Controllers must not contain core business logic.
- Synchronization must support incremental data exchange.
- Synchronization must support conflict detection.
- API failures must not cause loss of locally persisted Android data.
- Production API communication must use HTTPS.
- Error responses must use a consistent structure.
- API responses must not expose sensitive internal implementation details.
- Peer-to-peer synchronization and backend synchronization must preserve the same business entity identities.
- API design must remain extensible for future versions without unnecessarily breaking existing clients.

## 18. Security Architecture

### 18.1 Purpose

The SplitSync Security Architecture defines how users, devices, groups, synchronization operations, local data, backend APIs, and peer-to-peer communication are protected.

Security must be designed around the offline-first architecture.

The application must remain functional offline without weakening the security of synchronized data.

The primary principle is:

> Offline availability must not bypass authorization, identity, or data-integrity requirements.

### 18.2 Security Principles

SplitSync must follow these security principles:

- Authentication must identify the user or device making an operation.
- Authorization must determine whether the authenticated identity is allowed to perform the operation.
- Sensitive data must be protected during transmission.
- Sensitive credentials and cryptographic material must not be stored as plain text.
- Stable user and device identities must be preserved.
- Synchronization must verify the origin and integrity of changes.
- Backend validation must not trust Android clients blindly.
- Peer-to-peer synchronization must require authorization.
- Local offline functionality must not automatically imply permission to access another user's data.
- Financial data must not be modified without appropriate authorization.
- Security failures must not result in silent data corruption.

### 18.3 Security Boundaries

SplitSync has several security boundaries:

    Android Application
          ↓
    Local Database
          ↓
    Synchronization Layer
          ↓
    Internet / Local Network
          ↓
    Backend API
          ↓
    Application Services
          ↓
    Database

There is also a peer-to-peer boundary:

    Device A
        ↓
    Local Communication
        ↓
    Device B

Every boundary must validate the information crossing it.

### 18.4 Identity Model

SplitSync has three important identities:

- User identity.
- Device identity.
- Group membership identity.

The primary stable identities are:

- `userId`
- `deviceId`
- `groupId`

A user profile identifies the application user.

A device identifies an application installation/device participating in synchronization.

A group identifies the shared expense context.

These identities must not depend solely on mutable fields such as:

- Display name.
- Phone number.
- Email address.

### 18.5 User Authentication

When internet connectivity is available, the backend must authenticate users before allowing protected remote operations.

Authentication may use an appropriate token-based mechanism.

The exact authentication mechanism will be finalized during implementation.

Potential mechanisms include:

- Access tokens.
- Refresh tokens.
- Device-bound authentication.
- Secure credential-based authentication.

The selected mechanism must support the offline-first model.

### 18.6 Offline User Identity

V1 must allow a user to create and use a local profile without internet connectivity.

The local profile must therefore have a locally generated stable `userId`.

Conceptually:

    Application Installed
        ↓
    Device ID Generated
        ↓
    Local User Created
        ↓
    Local User ID Generated
        ↓
    Offline Application Usage

The absence of internet connectivity must not prevent local profile creation.

### 18.7 Local Authentication State

The Android application must maintain enough local authentication state to determine the currently active local user.

V1 supports one active local user per application installation.

The application must not require repeated internet authentication for every local operation.

Local authentication state must be protected from unauthorized access.

### 18.8 Device Identity

Each application installation must have a stable application-level `deviceId`.

The device identifier must:

- Be generated locally.
- Be unique within the application identity system.
- Not depend on internet connectivity.
- Be associated with the local user.
- Participate in synchronization.
- Be protected against unauthorized modification where practical.

The application must not rely on hardware identifiers such as IMEI as the primary application-level device identity.

### 18.9 Device Registration

When internet connectivity is available, a device may register its application-level identity with the backend.

Conceptually:

    Local Device
        ↓
    deviceId
        +
    userId
        ↓
    Backend Registration
        ↓
    Authorized Device

Device registration must verify that the device is associated with an authenticated user.

### 18.10 Device Trust

A device participating in synchronization must be recognized as belonging to an authorized user.

The backend must not trust a client merely because it provides a valid `deviceId`.

Device identity must be associated with authenticated user identity and appropriate authorization state.

### 18.11 Authentication Versus Authorization

Authentication and authorization must remain separate.

Authentication answers:

    "Who are you?"

Authorization answers:

    "What are you allowed to do?"

For example:

    Authenticated User
        ↓
    User = Rahul

does not automatically mean:

    Rahul
        ↓
    Allowed to access every Group

Group-level authorization must still be checked.

### 18.12 Group-Level Authorization

Group data must be protected according to group membership and role.

For example:

    User Rahul
        ↓
    Member of Group G123
        ↓
    Authorized to access Group G123

But:

    User Rahul
        ↓
    Not a member of Group G999
        ↓
    Access Denied

Knowing a `groupId`, `expenseId`, or `settlementId` must not be sufficient to access protected data.

### 18.13 Group Roles

Groups may define roles such as:

- Owner.
- Member.

Additional roles may be introduced in future versions.

Roles must determine which operations a user can perform.

For example, ownership may be required for operations such as:

- Changing group ownership.
- Removing members.
- Deleting a group.

The exact V1 permission matrix will be defined during implementation.

### 18.14 Expense Authorization

Users must only be allowed to modify Expenses for Groups they are authorized to access.

The backend must validate:

    Authenticated User
        ↓
    Group Membership
        ↓
    Expense.groupId
        ↓
    Authorization
        ↓
    Allow / Deny

A client must not be trusted simply because it sends a valid `expenseId`.

### 18.15 Settlement Authorization

Settlement operations must also be authorized at the Group level.

The backend must verify that the authenticated user is allowed to operate on the corresponding Group.

The system must also validate:

- `paidBy`.
- `paidTo`.
- Group membership.
- Settlement amount.
- Currency.
- Operation permissions.

### 18.16 Synchronization Authorization

Synchronization requires additional authorization checks.

The backend must verify:

- Authenticated user.
- Registered device.
- Device ownership/association.
- Group authorization.
- Entity authorization.
- Synchronization operation validity.

The client must not be able to synchronize arbitrary data merely by knowing another entity's identifier.

### 18.17 Synchronization Identity

Every synchronization operation must contain sufficient information to identify its origin.

Conceptually:

    Synchronization Operation
        ├── operationId
        ├── deviceId
        ├── userId where required
        ├── entityType
        ├── entityId
        └── operationType

The backend must validate that the operation is authorized for the identified entity.

### 18.18 Synchronization Integrity

Synchronization data must be protected against unauthorized modification.

The system must ensure that:

- Entity identifiers are valid.
- Operation types are valid.
- Payloads are structurally valid.
- The sender is authorized.
- The operation has not been tampered with.
- The operation does not violate business rules.

HTTPS/TLS provides transport protection for backend communication.

Additional integrity mechanisms may be required for local peer-to-peer synchronization.

### 18.19 HTTPS and TLS

All production communication between Android clients and the backend must use HTTPS.

The backend must use valid TLS certificates.

Plain HTTP must not be used for sensitive production API communication.

Transport security protects:

- Authentication credentials.
- Tokens.
- User information.
- Group information.
- Expenses.
- Settlements.
- Synchronization payloads.

### 18.20 Local Network Security

A local network must not automatically be considered trusted.

For example:

    Same Wi-Fi
        ≠
    Automatically Authorized

Two devices being connected to the same:

- Wi-Fi network.
- Mobile hotspot.
- Local network.

must not automatically grant access to SplitSync data.

Peer-to-peer communication must perform explicit application-level authorization.

### 18.21 Peer-to-Peer Discovery Security

Nearby device discovery may expose limited information required for discovery.

Discovery must not expose complete group or financial information before authorization.

Conceptually:

    Device Discovery
        ↓
    Identify SplitSync Device
        ↓
    Authentication / Pairing
        ↓
    Authorization
        ↓
    Data Exchange

The exact discovery and pairing protocol will be defined in Section 19.

### 18.22 Peer-to-Peer Authentication

Before exchanging protected group data directly between devices, the devices must establish trust.

The authentication mechanism may use:

- Device identity.
- User identity.
- Pairing information.
- Cryptographic keys.
- Secure challenge-response mechanisms.

The final mechanism will be selected during implementation.

### 18.23 Peer-to-Peer Authorization

Even after two devices authenticate each other, they must only exchange data that the requesting user/device is authorized to access.

For example:

    Device A
        ↓
    Requests Group G123
        ↓
    Verify Membership
        ↓
    Authorized
        ↓
    Exchange G123 Data

A device must not be allowed to request arbitrary groups simply because it is a recognized SplitSync device.

### 18.24 Local Database Security

The Android local database contains potentially sensitive information such as:

- User profile.
- Group members.
- Expenses.
- Expense amounts.
- Settlements.
- Synchronization metadata.

The application should protect sensitive local data against unauthorized application-level access.

Where appropriate, database encryption may be introduced.

The final local database encryption strategy will be defined during implementation.

### 18.25 Secure Credential Storage

Authentication credentials, access tokens, refresh tokens, and cryptographic secrets must not be stored as plain text in ordinary application preferences or database fields.

Android secure storage mechanisms should be used where appropriate.

Potentially sensitive secrets should be protected using Android-supported secure key storage mechanisms.

### 18.26 Cryptographic Keys

If cryptographic keys are required for:

- Device authentication.
- Peer-to-peer communication.
- Message integrity.
- Data encryption.

they must be generated and stored securely.

Private keys must not be transmitted unnecessarily between devices.

The exact key-management architecture will be defined before peer-to-peer synchronization implementation.

### 18.27 API Input Validation

The backend must validate all API input.

The backend must assume that the Android client may be:

- Outdated.
- Buggy.
- Compromised.
- Modified.
- Sending invalid data.

Therefore:

    Client Validation
        +
    Backend Validation

must both exist.

Backend validation must be authoritative for protected server-side operations.

### 18.28 Financial Integrity

Security architecture must protect financial integrity in addition to confidentiality.

The backend must prevent unauthorized modification of:

- Expense amounts.
- Expense payers.
- Expense Splits.
- Settlement amounts.
- Settlement participants.

The backend must validate financial invariants before committing changes.

### 18.29 Replay Protection

Synchronization operations may be retransmitted due to retries or network failures.

The backend must distinguish:

    New Operation

from:

    Previously Processed Operation

Stable operation identifiers and synchronization metadata must be used to prevent replayed operations from creating duplicate or unintended changes.

### 18.30 Idempotency and Security

Idempotency is also a security requirement.

If a malicious or malfunctioning client repeatedly sends the same synchronization operation:

    Operation OP123
        ↓
    OP123
        ↓
    OP123
        ↓
    OP123

the backend must not create repeated financial records.

The same logical operation must produce the same resulting business state.

### 18.31 Authorization During Offline Operation

Offline operation requires careful distinction between local ownership and remote authorization.

A user may continue operating on locally available group data while offline because the device already has authorized local group state.

However, offline availability must not automatically grant permission to access data that was never authorized or synchronized to the device.

For example:

    Group G123
        ↓
    Already Authorized and Stored Locally
        ↓
    Offline Access Allowed

but:

    Group G999
        ↓
    Never Authorized / Not Available Locally
        ↓
    Offline Access Not Available

### 18.32 Offline Membership Changes

Membership changes performed offline must be subject to the same authorization rules as online operations.

The application must not assume that every local user is automatically authorized to:

- Add arbitrary users.
- Remove members.
- Change ownership.

Offline authorization must be based on the locally known group role and authorization state.

When synchronization occurs, the backend must validate the operation again.

### 18.33 Offline Ownership Changes

Ownership changes are security-sensitive.

An offline device may create an ownership change only if the local user has the required role.

When synchronized with the backend or another authorized device, the operation must be validated again.

Conflicting ownership changes must follow Section 13.

### 18.34 Data Minimization

Devices should store only the group and user data required for their authorized application functionality.

The application should avoid downloading or storing unnecessary sensitive information.

For example:

    Group Member
        ↓
    Required Profile Information

should be synchronized rather than the user's complete private profile.

The exact profile information exchanged during local discovery and synchronization will be defined in Section 19.

### 18.35 Privacy of Phone Number and Email

Phone numbers and email addresses are identity/contact information and should not automatically be exposed to every group member or nearby device.

The system must distinguish between:

- Identity information required for identification.
- Contact information.
- Public group profile information.
- Private user information.

The exact V1 profile visibility rules will be defined during implementation.

### 18.36 Sensitive Logging

The application and backend must avoid logging sensitive information unnecessarily.

Logs should not contain:

- Passwords.
- Authentication tokens.
- Private keys.
- Full authentication credentials.
- Unnecessary personal information.

Financial details should also be minimized in logs.

Synchronization logs should prefer stable identifiers and operation metadata over complete financial payloads.

### 18.37 Error Message Security

Security-sensitive errors should not expose unnecessary internal information.

For example, the API should avoid returning:

    SQL exception
    Stack trace
    Database credentials
    Internal service details

to the Android client.

Client-facing errors should provide useful but controlled information.

### 18.38 Account and Device Revocation

The architecture should support revoking access for a device or user.

Potential scenarios include:

- Device lost.
- Device replaced.
- User account disabled.
- Device no longer trusted.
- Security credentials compromised.

After revocation:

    Revoked Device
        ↓
    Synchronization Request
        ↓
    Backend
        ↓
    Authorization Failed

The exact revocation workflow will be defined during implementation.

### 18.39 Device Loss

If a device is lost, locally stored financial information may remain on the device.

Therefore, the security architecture should consider:

- Android device security.
- Application data protection.
- Local database protection.
- Token revocation.
- Device revocation.
- Remote synchronization access control.

Remote revocation cannot erase all information from a completely offline device, so local device security remains important.

### 18.40 Authentication Token Handling

If token-based authentication is used, the Android application must:

- Store tokens securely.
- Avoid exposing tokens through logs.
- Refresh tokens safely.
- Handle expiration.
- Clear invalid credentials appropriately.
- Re-authenticate when required.

The exact token lifecycle will be finalized during implementation.

### 18.41 Session Security

Backend sessions or tokens must have appropriate expiration and renewal behavior.

Long-lived credentials should be avoided where unnecessary.

The system should support invalidating credentials when required.

The exact session and token policy will be defined in the authentication implementation.

### 18.42 Backend Database Security

The MySQL database must not be directly exposed to Android clients.

The intended access path is:

    Android
        ↓
    HTTPS
        ↓
    Spring Boot API
        ↓
    Hibernate/JPA
        ↓
    MySQL

Database credentials must remain on the backend infrastructure.

The Android application must never contain MySQL credentials.

### 18.43 Database Credential Security

Backend database credentials must be supplied through secure server-side configuration mechanisms.

Credentials must not be:

- Hard-coded in source code.
- Committed to Git.
- Included in Android applications.
- Exposed in API responses.

Production secrets should be managed through an appropriate secret-management mechanism.

### 18.44 Security Headers and API Protection

The backend should use appropriate HTTP security controls.

Depending on the final deployment, these may include:

- Secure HTTP headers.
- CORS restrictions where applicable.
- Request-size limits.
- Rate limiting.
- TLS configuration.
- Secure cookie settings if cookies are used.

The exact configuration will be finalized during deployment.

### 18.45 Authorization at Multiple Layers

Security-sensitive operations should be protected at more than one level where appropriate.

For example:

    Controller
        ↓
    Authentication
        ↓
    Application Service
        ↓
    Authorization
        ↓
    Domain Validation
        ↓
    Repository
        ↓
    Database

Authorization must not rely solely on the Android UI hiding an action.

### 18.46 No Trust in Client-Supplied Roles

The backend must not blindly trust role information supplied by the Android client.

For example, the client must not be able to send:

    role = OWNER

and thereby become the owner.

The backend must determine the user's actual role from trusted server-side state and authorized synchronization information.

### 18.47 No Trust in Client-Supplied Financial Results

The backend should validate financial calculations received from clients.

For example, if an Android client sends:

    Expense = ₹1,000

    Split A = ₹400
    Split B = ₹700

the backend must detect:

    ₹400 + ₹700 != ₹1,000

and reject the invalid state.

The client is not considered authoritative for financial integrity.

### 18.48 Security and Conflict Resolution

Security checks must occur before conflict resolution is applied.

The correct conceptual flow is:

    Incoming Change
        ↓
    Authentication
        ↓
    Authorization
        ↓
    Validation
        ↓
    Conflict Detection
        ↓
    Conflict Resolution
        ↓
    Persistence

An unauthorized change must never become a valid conflict-resolution candidate.

### 18.49 Security and Synchronization

Synchronization must not bypass normal authorization.

The synchronization endpoint must enforce:

- User authentication.
- Device authorization.
- Group authorization.
- Entity authorization.
- Payload validation.

A synchronization request is still an API operation and must be treated as a security-sensitive operation.

### 18.50 Security and Peer-to-Peer Synchronization

Peer-to-peer synchronization must implement application-level security even when the devices communicate over a trusted local network.

The minimum conceptual flow is:

    Device Discovery
        ↓
    Device Authentication
        ↓
    Secure Channel
        ↓
    Group Authorization
        ↓
    Synchronization
        ↓
    Integrity Validation

The exact transport and cryptographic protocol will be defined in Section 19.

### 18.51 Security Testing

Security testing must cover:

- Authentication.
- Authorization.
- Group access control.
- Device authorization.
- Synchronization authorization.
- Replay protection.
- Duplicate operation handling.
- Invalid financial payloads.
- Invalid group membership.
- Unauthorized expense access.
- Unauthorized settlement access.
- Peer-to-peer authorization.
- Token handling.
- Local data protection.
- API input validation.

Security testing must include attempts to bypass authorization through manipulated API requests.

### 18.52 Security Model Invariants

The following invariants must always hold:

- Authentication and authorization must remain separate.
- Stable `userId` and `deviceId` identities must be preserved.
- Core offline functionality must not require internet connectivity.
- Offline availability must not bypass authorization.
- Backend APIs must authenticate protected requests.
- Backend APIs must authorize access to protected resources.
- Group membership must be validated before protected group data is accessed.
- Knowing an entity ID must not grant access to that entity.
- Synchronization operations must be authenticated and authorized.
- Synchronization operations must be protected against unintended replay.
- Duplicate synchronization must not create duplicate financial records.
- Production backend communication must use HTTPS.
- MySQL credentials must never be included in the Android application.
- Sensitive credentials and cryptographic secrets must not be stored as plain text.
- Local peer-to-peer communication must not automatically trust devices on the same network.
- Peer-to-peer data exchange must require application-level authorization.
- Backend validation must not trust client-supplied roles or financial calculations.
- Financial integrity must be validated before persistence.
- Security checks must occur before conflict resolution.
- Revoked devices must not continue backend synchronization.
- Sensitive information must not be unnecessarily exposed through logs or error responses.
- Security architecture must protect confidentiality, integrity, authentication, authorization, and availability of SplitSync data.

## 19. Local Peer-to-Peer Synchronization

### 19.1 Purpose

Local Peer-to-Peer Synchronization allows authorized SplitSync devices to exchange group-related data directly when internet connectivity is unavailable.

The purpose of peer-to-peer synchronization is to allow a group to continue sharing expense information even when:

- Internet connectivity is unavailable.
- Backend synchronization is unavailable.
- Group members are physically nearby.
- Devices can establish a supported local communication connection.

The primary principle is:

> No internet should not mean no synchronization when authorized devices are nearby.

### 19.2 Role in the Overall Architecture

Peer-to-peer synchronization is an additional synchronization channel.

It does not replace:

- Local Room database.
- Backend synchronization.
- REST APIs.
- Core domain logic.

The overall synchronization model is:

    Local Device
        ↓
    Synchronization Layer
        ├── Backend Synchronization
        └── Peer-to-Peer Synchronization

The same business entities and synchronization rules must be used regardless of the synchronization channel.

### 19.3 Supported Connectivity Scenarios

SplitSync must support the following conceptual scenarios.

Online with internet:

    Device A
        ↓
    Internet
        ↓
    Backend
        ↓
    Device B

Offline but nearby:

    Device A
        ↕
    Local Network / Nearby Connection
        ↕
    Device B

Completely offline and without nearby devices:

    Device A
        ↓
    Local Database
        ↓
    Pending Changes

The application must remain fully usable in all three scenarios for supported local functionality.

### 19.4 Peer-to-Peer Is Optional for Core Operation

Core expense management must not depend on peer-to-peer synchronization.

If peer-to-peer communication is unavailable:

- Users can continue using the application locally.
- Expenses can still be created.
- Splits can still be calculated.
- Balances can still be calculated.
- Settlements can still be recorded.
- Changes remain pending for future synchronization.

Peer-to-peer communication improves synchronization availability but is not the primary source of application state.

### 19.5 Peer-to-Peer Communication Flow

The general flow is:

    Device A
        ↓
    Nearby Device Discovery
        ↓
    Device Authentication
        ↓
    Authorization
        ↓
    Secure Connection
        ↓
    Synchronization Handshake
        ↓
    Exchange Synchronization Metadata
        ↓
    Exchange Changes
        ↓
    Validate Changes
        ↓
    Apply Changes Locally
        ↓
    Update Synchronization State

The same process applies in the opposite direction.

### 19.6 Device Discovery

Devices must be able to discover nearby SplitSync devices through a supported Android communication mechanism.

Potential technologies include:

- Wi-Fi Direct.
- Nearby Connections.
- Local network discovery.
- Wi-Fi hotspot communication.
- Another Android-supported local communication mechanism.

The final technology must be selected based on:

- Android compatibility.
- Reliability.
- Offline capability.
- Battery usage.
- Security.
- Ease of implementation.
- Support for multiple nearby devices.

The exact transport selection will be finalized during implementation.

### 19.7 Discovery Information

Device discovery should expose only the minimum information required to identify a nearby SplitSync installation.

Potential discovery information may include:

- SplitSync device identifier.
- Temporary discovery identifier.
- Display name where appropriate.
- Protocol version.
- Application version.
- Capability information.

Discovery must not expose:

- Complete expense history.
- Settlement information.
- Private profile information.
- Group financial data.

Protected data must only be exchanged after authorization.

### 19.8 Nearby User Visibility

When two SplitSync users are nearby, the application may display limited profile information for identification.

For example:

    Nearby SplitSync Users

    Rahul
    Amit
    Priya

The exact information displayed must follow the profile visibility rules defined in Section 18.

Phone numbers and email addresses must not automatically be exposed merely because devices are nearby.

### 19.9 Device Authentication

Device discovery alone does not establish trust.

After discovery, devices must authenticate each other before exchanging protected data.

Conceptually:

    Device A
        ↓
    Discovers Device B
        ↓
    Authentication / Pairing
        ↓
    Device B Verified
        ↓
    Secure Communication

The exact authentication mechanism will be selected during implementation.

### 19.10 Secure Peer-to-Peer Channel

After authentication, devices should establish a protected communication channel.

The channel must provide appropriate:

- Confidentiality.
- Integrity.
- Authentication.

Sensitive group data must not be transmitted through an unauthenticated plain-text local connection.

The exact cryptographic protocol will be defined during implementation.

### 19.11 User Authorization

Device authentication alone is not sufficient.

The system must also determine which groups the connected users are authorized to synchronize.

For example:

    Device A
        ↓
    User Rahul
        ↓
    Member of Group G123

and:

    Device B
        ↓
    User Amit
        ↓
    Member of Group G123

The devices may exchange authorized Group G123 data.

If Device B is not authorized for Group G123:

    Group G123
        ↓
    Synchronization Not Allowed

### 19.12 Group-Based Synchronization

Peer-to-peer synchronization must be scoped to authorized groups.

A device should exchange only the data required for groups that the participating users are authorized to access.

For example:

    Device A
        ├── Group G123
        ├── Group G456
        └── Group G789

    Device B
        └── Group G123

The synchronization session should exchange Group G123 data rather than automatically exposing G456 and G789.

### 19.13 Synchronization Handshake

Before exchanging business data, devices should perform a synchronization handshake.

The handshake may exchange:

- Device identity.
- User identity where required.
- Protocol version.
- Application version.
- Supported synchronization capabilities.
- Group identifiers that may be synchronized.
- Synchronization cursor/version information.

Conceptually:

    Device A
        ↓
    Handshake
        ↕
    Device B
        ↓
    Determine Compatible Protocol
        ↓
    Determine Authorized Groups
        ↓
    Begin Synchronization

### 19.14 Synchronization Capability Negotiation

Devices may run different application versions.

The peer-to-peer protocol should therefore support capability negotiation.

For example:

    Device A
    Protocol Version 1

    Device B
    Protocol Version 1

    ↓

    Compatible
    ↓
    Synchronization Allowed

If the protocol versions are incompatible:

    Protocol Incompatible
        ↓
    Synchronization Not Started

The application should avoid exchanging data using an unsupported protocol.

### 19.15 Exchange of Synchronization Metadata

Before transferring complete business records, devices should exchange synchronization metadata where practical.

This may include:

- Entity identifiers.
- Entity versions.
- Change identifiers.
- Synchronization cursors.
- Last-known synchronization information.

This allows devices to determine what data actually needs to be exchanged.

### 19.16 Incremental Peer Synchronization

Peer-to-peer synchronization should be incremental.

The system should avoid transferring the entire group dataset every time two devices connect.

Conceptually:

    Device A
        ↓
    Local Sync State

    Device B
        ↓
    Local Sync State

        ↓

    Determine Missing Changes
        ↓
    Exchange Only Required Changes

This reduces:

- Network traffic.
- Battery consumption.
- Synchronization time.
- Duplicate processing.

### 19.17 Push Changes to Peer

A device may send its pending local changes to another authorized device.

For example:

    Device A
        ↓
    Pending Expense E123
        ↓
    Peer Device B
        ↓
    Validate
        ↓
    Apply Locally
        ↓
    Mark Received

The originating device must not assume that the receiving device successfully applied the change until an appropriate acknowledgment is received.

### 19.18 Pull Changes from Peer

A device may request changes that exist on another authorized device but are missing locally.

For example:

    Device A
        ↓
    "Give me changes I do not have"
        ↓
    Device B
        ↓
    Missing Changes
        ↓
    Device A
        ↓
    Local Database

This allows a device to obtain changes even when the backend is unavailable.

### 19.19 Bidirectional Peer Synchronization

Peer-to-peer synchronization must support bidirectional exchange.

For example:

    Device A
        ↓
    Expense E100

    Device B
        ↓
    Expense E200

When they connect:

    Device A
        ↕
    Device B

Both changes should be exchanged.

The final result should contain:

    Expense E100
    Expense E200

provided both changes are valid and authorized.

### 19.20 Offline Expense Synchronization

A user must be able to create an Expense while completely offline.

For example:

    Device A
        ↓
    Expense E123 Created
        ↓
    No Internet
        ↓
    Sync Pending

Later:

    Device A
        ↕
    Device B
        ↓
    Peer Synchronization
        ↓
    Expense E123
        ↓
    Device B Local Database Updated

The expense must retain the same `expenseId`.

### 19.21 Expense Split Synchronization

Expense Splits must be synchronized together with their parent Expense where required.

The receiving device must not apply an Expense in a way that leaves invalid or incomplete Expense Split data.

The preferred conceptual flow is:

    Expense
        +
    Expense Splits
        ↓
    Validate Complete Financial State
        ↓
    Local Transaction
        ↓
    Commit

The final implementation may transfer these records as a single synchronization unit or as dependency-ordered operations.

### 19.22 Settlement Synchronization

Settlements must also be synchronized between authorized devices.

For example:

    Device A
        ↓
    Settlement S123
        ↓
    Peer Synchronization
        ↓
    Device B
        ↓
    Local Settlement Updated

The settlement must retain the same stable `settlementId`.

### 19.23 Group Membership Synchronization

Group membership changes may be exchanged through peer-to-peer synchronization where supported.

For example:

    Device A
        ↓
    Add Rahul to Group G123
        ↓
    Peer Synchronization
        ↓
    Device B

The receiving device must validate the membership operation according to:

- Group authorization.
- User identity.
- Membership state.
- Operation version.
- Conflict rules.

### 19.24 Offline Group Creation

A user may create a group while completely offline.

For example:

    Device A
        ↓
    Create Group
        ↓
    groupId generated locally
        ↓
    Group Stored Locally

Later, the user may synchronize the group with authorized devices through:

- Peer-to-peer synchronization.
- Backend synchronization.

The same `groupId` must be retained.

### 19.25 Offline Group Joining

V1 may support offline group joining through a local peer-to-peer invitation or pairing mechanism.

Conceptually:

    User A
        ↓
    Creates / Owns Group G123

    User B
        ↓
    Nearby Discovery

    User A
        ↓
    Sends Group Invitation

    User B
        ↓
    Accepts Invitation

    ↓

    Group Membership Created Locally

The exact invitation mechanism will be defined during implementation.

### 19.26 Group Invitation Security

A group invitation must not be equivalent to unrestricted access.

The receiving device must verify:

- Invitation authenticity.
- Group identity.
- Inviter authorization.
- Invitation validity.
- Intended recipient where applicable.

The invitation should not expose complete group financial data before acceptance.

### 19.27 Peer-to-Peer Synchronization Authorization

The peer synchronization layer must verify authorization before transferring each protected group dataset.

Conceptually:

    Peer Connection
        ↓
    Authenticated Device
        ↓
    Authenticated User
        ↓
    Group Membership
        ↓
    Synchronization Permission
        ↓
    Data Exchange

The system must not rely solely on the fact that a device was previously paired.

### 19.28 Peer-to-Peer Data Validation

All data received through peer-to-peer synchronization must be validated before being persisted.

Validation must include:

- Entity identifier.
- Entity type.
- Operation type.
- Group relationship.
- User references.
- Financial values.
- Expense Split consistency.
- Settlement validity.
- Version information.
- Authorization.
- Payload integrity.

The receiving device must treat peer-provided data as untrusted input until validated.

### 19.29 Peer-to-Peer Transaction Boundaries

Related changes must be applied transactionally.

For example:

    Receive Expense
        ↓
    Validate Expense
        ↓
    Validate Expense Splits
        ↓
    Begin Local Transaction
        ↓
    Apply Expense
        ↓
    Apply Expense Splits
        ↓
    Update Sync State
        ↓
    Commit

If any required operation fails:

    Rollback
        ↓
    Keep Previous Valid State
        ↓
    Retry / Report Failure

### 19.30 Duplicate Peer Synchronization

The same peer synchronization operation may be received multiple times.

For example:

    Device A
        ↓
    Expense E123
        ↓
    Device B

and later:

    Device A
        ↓
    Expense E123
        ↓
    Device B

The receiving device must recognize that the entity already exists.

Duplicate delivery must not create duplicate business records.

### 19.31 Peer-to-Peer Idempotency

Peer-to-peer synchronization must follow the same idempotency principles as backend synchronization.

Stable identifiers and synchronization operation identifiers must be used to prevent duplicate processing.

The same operation must produce the same logical result when processed more than once.

### 19.32 Conflict Detection Between Peers

Peer-to-peer synchronization must use the same conflict-resolution architecture defined in Section 13.

For example:

    Device A
        ↓
    Expense E123 = ₹1,200

    Device B
        ↓
    Expense E123 = ₹1,500

When they synchronize:

    E123
        ↓
    Concurrent Changes
        ↓
    Conflict Detection
        ↓
    Section 13 Conflict Resolution

The peer-to-peer layer must not implement a separate conflict-resolution model.

### 19.33 Peer-to-Peer Conflict Resolution

If a conflict is detected during peer synchronization:

- The conflicting entity must not be silently overwritten.
- The conflict must be represented locally.
- The conflict-resolution rules from Section 13 must be applied.
- Financial consistency must be preserved.
- The final resolved state must become synchronizable.

### 19.34 Peer-to-Peer Deletion Synchronization

Deletion operations must also be exchanged.

For example:

    Device A
        ↓
    Delete Expense E123
        ↓
    Tombstone / Deletion Metadata
        ↓
    Peer Synchronization
        ↓
    Device B
        ↓
    Expense E123 Deleted

Deletion metadata must remain available long enough to prevent the deleted entity from being reintroduced by another synchronization path.

### 19.35 Peer-to-Peer Synchronization and Backend Synchronization

Peer-to-peer synchronization and backend synchronization may occur at different times.

For example:

    Device A
        ↓
    Expense E123 Created Offline
        ↓
    Peer Sync
        ↓
    Device B

Later:

    Device B
        ↓
    Internet Available
        ↓
    Backend Sync
        ↓
    Server

The backend must recognize `E123` as the same logical Expense.

Similarly:

    Device A
        ↓
    Backend Sync
        ↓
    Server

Later:

    Device B
        ↓
    Peer Sync
        ↓
    Device A

must not create duplicate records.

### 19.36 Multi-Device Synchronization

A group may contain multiple devices.

For example:

    Group G123

    ├── Device A
    ├── Device B
    ├── Device C
    └── Device D

Changes may be created independently on each device.

The synchronization architecture must allow these changes to eventually propagate through:

- Peer-to-peer synchronization.
- Backend synchronization.
- A combination of both.

### 19.37 Synchronization Through Multiple Paths

The same change may reach a device through multiple synchronization paths.

For example:

    Device A
       ↓
    Backend
       ↓
    Device C

and:

    Device A
       ↓
    Device B
       ↓
    Device C

Device C must recognize that the same logical entity or operation has already been processed.

Stable identifiers and synchronization metadata must prevent duplicate application.

### 19.38 Peer-to-Peer Offline Group Scenario

A typical offline group scenario is:

    Five Friends
        ↓
    No Internet

    Friend A creates expense
        ↓
    Friend B creates expense
        ↓
    Friend C creates expense
        ↓
    Friend D creates expense
        ↓
    Friend E creates expense

Later:

    Friends connect to the same local network
        ↓
    Devices Discover Each Other
        ↓
    Authorized Connections Established
        ↓
    Synchronization Metadata Exchanged
        ↓
    Missing Changes Exchanged
        ↓
    Conflicts Resolved
        ↓
    Local Databases Converge

All members should eventually have the same valid group state.

### 19.39 Multi-Hop Synchronization

V1 should not require multi-hop peer-to-peer forwarding as a mandatory feature.

For example:

    Device A
        ↓
    Device B
        ↓
    Device C

Device B should not automatically act as a synchronization relay for Device C unless explicitly supported by a future protocol.

V1 should focus on direct authorized peer-to-peer synchronization and backend synchronization.

Future versions may introduce more advanced synchronization topologies.

### 19.40 Multiple Nearby Devices

When multiple SplitSync devices are nearby, the application may discover several peers.

The synchronization layer must avoid unnecessary duplicate synchronization.

For example:

    Device A
        ├── Device B
        ├── Device C
        └── Device D

The system should maintain synchronization metadata so that each peer exchange transfers only required changes.

### 19.41 Synchronization Session

A peer-to-peer synchronization session should have a defined lifecycle.

Conceptually:

    Discovery
        ↓
    Authentication
        ↓
    Authorization
        ↓
    Handshake
        ↓
    Metadata Exchange
        ↓
    Change Exchange
        ↓
    Validation
        ↓
    Local Commit
        ↓
    Acknowledgment
        ↓
    Session Complete

If any stage fails, the local data must remain intact and pending synchronization must remain recoverable.

### 19.42 Session Timeout

Peer-to-peer sessions must have controlled timeouts.

A session must not remain active indefinitely because:

- Device moved out of range.
- Wi-Fi changed.
- User disabled connectivity.
- Device went to sleep.
- Application was backgrounded.
- Peer disconnected.

A timeout must terminate the session safely.

Partially transferred changes must be recoverable through a future synchronization attempt.

### 19.43 Interrupted Peer Synchronization

If a peer-to-peer synchronization session is interrupted:

    Synchronization Started
        ↓
    Data Partially Transferred
        ↓
    Connection Lost
        ↓
    Local Transaction Boundary
        ↓
    Commit Only Complete Valid Data
        ↓
    Remaining Data Stays Pending

The system must not leave partially applied financial records.

### 19.44 Peer-to-Peer Battery Considerations

Peer-to-peer synchronization should minimize battery consumption.

The implementation should:

- Avoid continuous discovery.
- Avoid unnecessary background scanning.
- Prefer synchronization when the application is active or when appropriate Android mechanisms permit it.
- Transfer only required changes.
- Close connections after synchronization completes.

Battery optimization must not compromise data integrity.

### 19.45 Peer-to-Peer Data Size

Synchronization should transfer changes incrementally rather than repeatedly transferring the entire group dataset.

Large groups may contain:

- Many expenses.
- Many expense splits.
- Many settlements.
- Large historical data.

The synchronization protocol should therefore support:

- Batching.
- Incremental transfer.
- Change cursors.
- Acknowledgments.
- Resume capability where practical.

### 19.46 Peer-to-Peer Protocol Compatibility

The peer-to-peer protocol must include a protocol version.

For example:

    protocolVersion = 1

Devices with incompatible protocol versions must not exchange unsupported data formats.

Future protocol versions may introduce backward-compatible capabilities.

### 19.47 Peer-to-Peer Application Version

Application version information may also be exchanged during the handshake.

For example:

    Device A
        ↓
    App Version 1.0

    Device B
        ↓
    App Version 1.1

The synchronization layer may use capability negotiation to determine which features can safely be exchanged.

Application version differences must not automatically prevent synchronization if the underlying protocol remains compatible.

### 19.48 Peer-to-Peer Security Requirements

The peer-to-peer layer must satisfy the following security requirements:

- Nearby devices must not automatically be trusted.
- Device identity must be verified.
- User/group authorization must be verified.
- Communication must be protected.
- Synchronization payloads must be validated.
- Duplicate operations must be safely handled.
- Unauthorized group data must not be exposed.
- Sensitive profile information must not be unnecessarily transmitted.
- Cryptographic keys must be protected.

The detailed security mechanisms are defined in Section 18 and will be finalized during implementation.

### 19.49 Peer-to-Peer User Experience

The application should minimize the technical complexity presented to users.

The user should ideally see something similar to:

    Nearby SplitSync Users

        Rahul
        Amit
        Priya

    [Connect]

After authorization:

    Connected to Rahul

    Synchronizing Group: Goa Trip

    5 changes received
    2 changes sent

The user should not need to understand synchronization cursors, entity versions, or operation identifiers.

### 19.50 Peer-to-Peer Synchronization Status

The application may expose synchronization status such as:

- `DISCOVERING`
- `CONNECTING`
- `AUTHENTICATING`
- `SYNCING`
- `COMPLETED`
- `FAILED`
- `CONFLICT`

These statuses are informational and must not block core local application functionality.

### 19.51 Peer-to-Peer Failure Handling

Failure of peer-to-peer synchronization must not cause data loss.

Possible failures include:

- Peer unavailable.
- Connection rejected.
- Authentication failure.
- Authorization failure.
- Protocol incompatibility.
- Network interruption.
- Validation failure.
- Conflict.
- Timeout.

The local device must retain its valid local state and pending synchronization information.

### 19.52 Peer-to-Peer and Offline-First Principle

Peer-to-peer synchronization must strengthen the offline-first architecture rather than replace it.

The complete model is:

    No Internet
        ↓
    Local Application Works

    No Internet
        +
    Nearby Authorized Device
        ↓
    Peer Synchronization Available

    Internet Available
        ↓
    Backend Synchronization Available

    Internet + Nearby Devices
        ↓
    Backend and Peer Synchronization May Both Be Available

### 19.53 Peer-to-Peer Model Invariants

The following invariants must always hold:

- Peer-to-peer synchronization is optional for core local functionality.
- Internet connectivity must not be required for local expense management.
- Devices on the same network must not automatically be trusted.
- Device authentication must occur before protected data exchange.
- Group authorization must occur before group data exchange.
- Only authorized group data may be synchronized.
- Peer-to-peer synchronization must use stable business identifiers.
- Peer-to-peer synchronization must be idempotent.
- Duplicate peer synchronization must not create duplicate business records.
- Peer synchronization must use the same conflict-resolution rules as backend synchronization.
- Financial data must be validated before local persistence.
- Related financial records must be applied transactionally where required.
- Interrupted synchronization must not corrupt local financial data.
- Deletions must be synchronized using appropriate deletion metadata.
- Peer synchronization must support bidirectional change exchange.
- Peer synchronization must support incremental synchronization.
- Peer synchronization must preserve compatibility between supported protocol versions.
- Sensitive profile information must not be unnecessarily exposed during discovery.
- Local network presence alone must not grant access to group data.
- Backend synchronization and peer-to-peer synchronization must converge on the same logical business state.
- Peer-to-peer synchronization must never become a mandatory dependency for normal application operation.

## 20. Scalability

### 20.1 Purpose

SplitSync V1 must be designed to support growth in:

- Number of users.
- Number of devices.
- Number of groups.
- Number of group members.
- Number of expenses.
- Number of settlements.
- Number of synchronization operations.
- Number of backend requests.

The scalability architecture must not compromise the core offline-first principles defined in Section 11.

The primary principle is:

> Scale the shared synchronization infrastructure without making the local application dependent on it.

### 20.2 V1 Scalability Strategy

V1 will use a modular monolithic Spring Boot backend.

The initial architecture should prioritize:

- Simplicity.
- Correctness.
- Maintainability.
- Database integrity.
- Efficient synchronization.
- Clear module boundaries.

Microservices are not required for V1.

The backend should, however, be designed so that individual modules can be separated in the future if actual scale requires it.

### 20.3 Scalability Dimensions

SplitSync must consider multiple types of scalability.

These include:

- User scalability.
- Device scalability.
- Group scalability.
- Expense scalability.
- Synchronization scalability.
- API scalability.
- Database scalability.
- Storage scalability.
- Background processing scalability.

Each dimension may require a different optimization strategy.

### 20.4 User Scalability

The backend should support increasing numbers of registered users without requiring changes to the core business model.

Users are identified by stable `userId` values.

The database must use appropriate indexes for common user-related queries.

User-related operations should not require scanning unrelated users.

### 20.5 Device Scalability

A growing number of devices may synchronize with the backend.

The backend must therefore treat device synchronization as an independently scalable workload.

Synchronization operations must be associated with:

- `deviceId`
- `userId`
- Entity identifiers
- Synchronization metadata

The backend should not maintain synchronization state only in application memory.

Persistent synchronization state allows backend instances to scale horizontally.

### 20.6 Group Scalability

Groups are the primary shared-expense boundary.

The system should support groups ranging from:

- Small groups of a few members.
- Medium groups with dozens of members.
- Larger groups with significantly more financial records.

Group-based queries should be optimized using indexed `groupId` fields.

The architecture should avoid loading an entire application's dataset when only one Group is required.

### 20.7 Large Group Considerations

For large groups, operations such as:

- Loading members.
- Loading expenses.
- Loading settlements.
- Calculating balances.
- Synchronizing changes.

must avoid unnecessary full-dataset operations.

The system should use:

- Pagination.
- Incremental queries.
- Indexed access.
- Incremental synchronization.

Large groups must not require every device to download unrelated data.

### 20.8 Expense Scalability

Expense history may grow significantly over time.

The database should support efficient queries for:

- Recent expenses.
- Expenses by Group.
- Expenses by date range.
- Expenses by participant.
- Expenses by payer.

The Android application should not load an unlimited expense history into memory at once.

UI-level pagination or incremental loading should be used where appropriate.

### 20.9 Settlement Scalability

Settlement history should follow similar scalability principles.

The system should support:

- Group-based settlement queries.
- Date-based filtering.
- Incremental loading.
- Incremental synchronization.

Settlement records should remain independently identifiable using stable `settlementId` values.

### 20.10 Synchronization Scalability

Synchronization is expected to become one of the most important backend workloads.

The synchronization architecture must avoid requiring:

    Device
        ↓
    Download Entire Database
        ↓
    Compare Everything
        ↓
    Synchronize

Instead, it should use:

    Device
        ↓
    Synchronization Cursor / Version
        ↓
    Request Only Required Changes
        ↓
    Incremental Synchronization

This reduces:

- Network traffic.
- CPU usage.
- Database load.
- Battery consumption.
- Synchronization time.

### 20.11 Incremental Synchronization

Synchronization must be incremental.

A device should request only changes that occurred after its last successfully processed synchronization point.

Conceptually:

    Device A
        ↓
    Last Cursor = 1050

    Backend
        ↓
    Changes > 1050

    Response
        ↓
    1051 ... 1080

The exact cursor/version mechanism will be defined during implementation.

### 20.12 Synchronization Batching

Synchronization should support batching.

For example:

    Pending Changes
        ↓
    Batch 1
        ↓
    Batch 2
        ↓
    Batch 3

Batching helps prevent:

- Extremely large HTTP requests.
- Excessive database transactions.
- Memory pressure.
- Long-running synchronization operations.

The batch size should be configurable and adjusted based on performance testing.

### 20.13 Backend Statelessness

The Spring Boot application layer should remain stateless wherever practical.

Business-critical synchronization state must not exist only in:

- Java heap memory.
- Static variables.
- Local application memory.

Persistent state should be stored in MySQL or appropriate shared infrastructure.

This allows multiple backend instances to process requests.

### 20.14 Horizontal Scaling

The backend should support future horizontal scaling.

Conceptually:

    Android Devices
          ↓
       Load Balancer
          ↓
    ┌───────────────┐
    │ Spring Boot 1 │
    ├───────────────┤
    │ Spring Boot 2 │
    ├───────────────┤
    │ Spring Boot 3 │
    └───────────────┘
          ↓
        MySQL

Multiple backend instances must be able to access the same persistent application state.

The application must not rely on a particular backend instance for correctness.

### 20.15 Database Scalability

MySQL is the primary database for V1.

Database scalability must initially focus on:

- Correct indexes.
- Efficient queries.
- Appropriate transaction boundaries.
- Connection pooling.
- Proper schema design.
- Incremental synchronization queries.
- Avoiding unnecessary full-table scans.

Database optimization should be driven by measured workload rather than premature complexity.

### 20.16 Database Indexing

Indexes should support common query patterns.

Important candidates include:

- `userId`
- `deviceId`
- `groupId`
- `expenseId`
- `settlementId`
- `createdAt`
- `updatedAt`
- Synchronization version/cursor fields.
- Synchronization status.

Composite indexes may be introduced where query patterns require them.

Indexes must be reviewed as the application workload grows.

### 20.17 Synchronization Indexing

Synchronization queries are particularly important for backend scalability.

The database should efficiently support operations such as:

    Get changes after cursor X

    Get pending synchronization operations

    Find operation by operationId

    Find entity by entityId

The synchronization architecture should avoid repeatedly scanning unrelated historical data.

### 20.18 Connection Pooling

The backend should use an appropriate database connection pool.

The pool size must be configured based on:

- Available database capacity.
- Backend instance count.
- Expected concurrency.
- Query duration.

Increasing connection counts without sufficient database capacity must be avoided.

### 20.19 API Scalability

REST APIs should remain stateless and horizontally scalable where practical.

API endpoints should avoid storing request-specific state in server memory.

Long-running operations should not unnecessarily block HTTP requests.

Where asynchronous processing is required in future versions, background processing may be introduced.

### 20.20 Synchronization Load Isolation

Synchronization traffic may be significantly higher than normal CRUD traffic.

The architecture should allow synchronization workload to be optimized independently.

Potential future strategies include:

- Dedicated synchronization processing.
- Background workers.
- Queue-based processing.
- Separate synchronization infrastructure.

These are not required for V1 unless actual workload requires them.

### 20.21 Backend Caching

Caching is not required as a primary V1 scalability mechanism.

The initial focus should remain on:

- Efficient database queries.
- Correct indexes.
- Incremental synchronization.
- Stateless backend design.

Caching may be introduced later for suitable read-heavy operations.

Examples may include:

- Frequently accessed group metadata.
- User profile information.
- Static configuration.

Financial synchronization data should not be cached in a way that risks stale or inconsistent state.

### 20.22 Android Scalability

The Android application must also scale with increasing local data.

The local database should avoid loading the complete dataset into memory.

For example:

    100 Expenses
        ↓
    Load Required Records

rather than:

    100 Expenses
        ↓
    Load Entire Database Into Memory

Room queries should use efficient filtering and pagination where appropriate.

### 20.23 Android Memory Management

The application must avoid retaining large collections of:

- Expenses.
- Expense Splits.
- Settlements.
- Group members.

in memory unnecessarily.

UI state should contain only the data required for the current screen.

Large datasets should be retrieved incrementally from Room.

### 20.24 Balance Calculation Scalability

Balance calculation may become expensive for very large groups with long histories.

The initial V1 approach should calculate balances from authoritative local financial data.

The architecture should remain open to future optimizations such as:

- Incremental balance calculation.
- Cached derived balances.
- Materialized balance summaries.

However, derived balance data must never become the only authoritative financial state.

### 20.25 Synchronization Storage Growth

Synchronization metadata may grow significantly over time.

The architecture should support cleanup of synchronization records that are no longer required.

However, cleanup must not remove information required by:

- Unsynchronized devices.
- Conflict resolution.
- Deletion propagation.
- Historical integrity.

Retention rules must therefore be designed carefully.

### 20.26 Tombstone Scalability

Deleted entities may require tombstones for synchronization.

If tombstones are retained indefinitely, storage requirements may grow.

The system should eventually support controlled tombstone cleanup after it is safe to remove them.

The cleanup mechanism must ensure that an offline device cannot later reintroduce a previously deleted entity because the deletion information was removed too early.

### 20.27 Conflict Scalability

Most synchronization operations should not result in conflicts.

The architecture should therefore optimize for the normal case:

    Valid Change
        ↓
    Validate
        ↓
    Apply
        ↓
    Synchronize

Conflicts should be handled as exceptional synchronization states rather than making every synchronization operation unnecessarily expensive.

### 20.28 Large Synchronization Conflicts

When many changes are synchronized simultaneously, conflict detection must remain deterministic.

The system should:

- Identify conflicts by stable entity identity.
- Process changes in controlled batches.
- Avoid loading unrelated entities.
- Preserve unresolved conflict information.
- Prevent partial financial updates.

A single conflict must not invalidate unrelated valid changes.

### 20.29 Storage Scalability

Backend storage requirements will grow with:

- Number of users.
- Number of groups.
- Number of expenses.
- Number of settlements.
- Synchronization metadata.
- Audit information.

The database schema should avoid unnecessary duplication of large data fields.

Large historical datasets may eventually require archival strategies.

Archival is a future scalability feature and is not required for the initial V1 implementation.

### 20.30 API Pagination

Collection APIs must support pagination where datasets can become large.

For example:

    GET /api/v1/groups/{groupId}/expenses?page=0&size=50

Pagination prevents the backend from returning extremely large responses.

Synchronization should continue to use cursor/version-based incremental processing rather than ordinary UI pagination.

### 20.31 Rate Limiting

Rate limiting may be used to protect the backend from excessive traffic.

It should be particularly considered for:

- Authentication.
- Synchronization.
- Resource-intensive APIs.

Rate limiting must not cause data loss.

If synchronization is temporarily rejected because of rate limits:

    Sync Request
        ↓
    Rate Limited
        ↓
    Local Changes Remain Pending
        ↓
    Retry Later

### 20.32 Background Processing

Background processing may become necessary as workload increases.

Potential workloads include:

- Large synchronization batches.
- Tombstone cleanup.
- Synchronization metadata cleanup.
- Audit processing.
- Notifications.
- Analytics.

V1 should introduce background processing only where there is a clear need.

### 20.33 Deployment Scalability

The initial deployment may use:

    Android Clients
        ↓
    Spring Boot Backend
        ↓
    MySQL

As usage grows, the deployment may evolve to:

    Android Clients
        ↓
    Load Balancer
        ↓
    Multiple Spring Boot Instances
        ↓
    Shared MySQL Infrastructure

Additional infrastructure may be introduced later without changing the core Android domain model.

### 20.34 Monitoring for Scalability

The backend should monitor metrics such as:

- API request count.
- API latency.
- Error rate.
- Synchronization request count.
- Synchronization duration.
- Synchronization failure rate.
- Conflict rate.
- Database query latency.
- Database connection usage.
- Storage growth.
- JVM memory usage.
- CPU usage.

Monitoring should be used to identify actual scalability bottlenecks.

### 20.35 Performance Testing

Before production scaling, the system should perform performance testing for:

- Large groups.
- Large expense histories.
- Large settlement histories.
- Multiple concurrent devices.
- Large synchronization batches.
- Concurrent modifications.
- High API request volume.

Testing should identify practical limits rather than relying only on theoretical capacity.

### 20.36 Load Testing

Load tests should simulate realistic SplitSync behavior.

For example:

    Many Users
        ↓
    Many Groups
        ↓
    Multiple Devices Per Group
        ↓
    Offline Changes
        ↓
    Connectivity Restored
        ↓
    Large Synchronization Burst

The backend should remain stable while processing synchronization bursts.

### 20.37 Scalability Without Architectural Overengineering

V1 should avoid introducing infrastructure that is not yet justified.

The initial system should not require:

- Microservices.
- Distributed event buses.
- Multiple database clusters.
- Complex caching infrastructure.
- Dedicated synchronization servers.

unless actual requirements justify them.

The architecture should instead establish clean boundaries that allow these technologies to be introduced later.

### 20.38 Future Scaling Path

A possible future evolution is:

    V1

    Android
        ↓
    Spring Boot Monolith
        ↓
    MySQL

    ↓

    Growth

    Android
        ↓
    Load Balancer
        ↓
    Multiple Spring Boot Instances
        ↓
    MySQL
        +
    Cache / Background Workers

    ↓

    Large Scale

    Android
        ↓
    API Gateway / Load Balancer
        ↓
    Modular Backend Services
        ↓
    Dedicated Infrastructure
        ↓
    Scalable Database / Storage

The exact evolution must be driven by actual workload and operational requirements.

### 20.39 Scalability Model Invariants

The following invariants must always hold:

- V1 must use a modular monolithic backend.
- Scalability must not compromise offline-first functionality.
- Core local expense operations must remain independent of backend scale.
- Backend application instances should remain stateless where practical.
- Persistent synchronization state must not depend only on server memory.
- Stable business identifiers must remain unchanged as the system scales.
- Synchronization must remain incremental.
- Synchronization should support batching.
- Database queries must use appropriate indexes.
- Large collections must support pagination or incremental loading.
- Android must not load the complete local dataset into memory unnecessarily.
- Financial records must remain authoritative and must not be replaced by cached derived values.
- Synchronization metadata cleanup must not break offline devices.
- Tombstone cleanup must not allow deleted data to reappear.
- Conflicts must remain deterministic as synchronization volume increases.
- Backend failures or scaling events must not cause loss of locally persisted Android data.
- Horizontal backend scaling must not change business behavior.
- Performance optimizations must not compromise financial consistency.
- Caching must not become the authoritative source of financial state.
- Additional infrastructure should be introduced only when justified by measured requirements.
- The architecture must provide a clear path from V1 modular monolith to future horizontal scaling.

## 21. Testing Strategy

### 21.1 Purpose

The SplitSync testing strategy must ensure that the application remains correct, reliable, secure, and usable across:

- Online operation.
- Offline operation.
- Backend synchronization.
- Local peer-to-peer synchronization.
- Multiple devices.
- Concurrent changes.
- Conflict scenarios.
- Large groups.
- Large expense histories.
- Application restarts.
- Network interruptions.

The primary testing principle is:

> Every core business operation must be correct locally before synchronization is considered.

### 21.2 Testing Principles

Testing must follow these principles:

- Test business logic independently from infrastructure.
- Test offline functionality as a first-class scenario.
- Test online functionality independently.
- Test synchronization separately from normal CRUD operations.
- Test peer-to-peer synchronization separately from backend synchronization.
- Test conflict resolution explicitly.
- Test financial calculations with exact expected results.
- Test security and authorization independently.
- Test database migrations.
- Test failure and recovery scenarios.
- Test multiple devices and concurrent changes.
- Prefer deterministic automated tests wherever possible.

### 21.3 Testing Pyramid

The overall testing strategy should follow a testing pyramid:

    ┌──────────────────────────────┐
    │        UI / E2E Tests        │
    └──────────────────────────────┘
              ▲
              │
    ┌──────────────────────────────┐
    │ Integration / Sync Tests     │
    └──────────────────────────────┘
              ▲
              │
    ┌──────────────────────────────┐
    │ Repository / Database Tests  │
    └──────────────────────────────┘
              ▲
              │
    ┌──────────────────────────────┐
    │     Domain Unit Tests        │
    └──────────────────────────────┘

The majority of business logic tests should exist at the unit-test level.

### 21.4 Testing Layers

Testing should cover:

- Domain Layer.
- Application Layer.
- Data Layer.
- Local Database.
- REST API.
- Synchronization.
- Peer-to-peer communication.
- Security.
- Presentation.
- End-to-end application behavior.

### 21.5 Domain Unit Testing

The Domain Layer must have extensive unit-test coverage.

Domain tests must not require:

- Android device.
- Internet connection.
- MySQL.
- REST API.
- Room database.

The following areas must be tested:

- Expense validation.
- Expense Split calculations.
- Equal splitting.
- Exact amount splitting.
- Percentage splitting.
- Shares-based splitting.
- Balance calculations.
- Settlement calculations.
- Group business rules.
- Membership rules.

### 21.6 Expense Split Testing

Expense Split calculations are financially critical and require extensive testing.

For example:

    Expense = ₹1,000

    User A = ₹500
    User B = ₹500

Expected:

    Total Split = ₹1,000

The tests must verify that:

    Sum(Splits) = Expense Amount

for all supported split methods.

### 21.7 Equal Split Tests

Tests must cover equal splitting among:

- 2 users.
- 3 users.
- 4 users.
- Larger numbers of users.

Examples should include amounts that divide evenly and amounts that require rounding.

For example:

    ₹100
    3 Users

The system must produce a deterministic and financially correct result according to the rounding rules defined by the application.

### 21.8 Exact Amount Split Tests

Tests must verify exact allocation.

Example:

    Expense = ₹1,000

    User A = ₹250
    User B = ₹300
    User C = ₹450

Expected:

    Total = ₹1,000

Invalid allocations must be rejected.

### 21.9 Percentage Split Tests

Tests must verify:

    Sum(Percentages) = 100%

where applicable.

Examples should include:

- 50% / 50%.
- 25% / 25% / 50%.
- Decimal percentages.
- Invalid totals.
- Negative percentages.
- Percentages greater than allowed limits.

### 21.10 Shares Split Tests

Tests must verify proportional allocation based on shares.

For example:

    User A = 1 share
    User B = 2 shares
    User C = 1 share

Total:

    4 shares

Expected:

    A = 25%
    B = 50%
    C = 25%

The tests must verify deterministic rounding.

### 21.11 Monetary Precision Testing

Financial calculations must never rely on floating-point precision.

Tests must cover:

- Whole amounts.
- Decimal amounts.
- Smallest supported currency units.
- Rounding.
- Repeated calculations.
- Large amounts.
- Split rounding.

The same input must always produce the same result.

### 21.12 Balance Calculation Testing

Balance calculations must be tested independently.

The tests should verify:

- Total paid by each user.
- Total share of each user.
- Net balance.
- Settlement adjustments.
- Final amount owed.
- Final amount receivable.

Example:

    User A pays ₹1,000
    User B owes ₹500
    User C owes ₹500

Expected:

    A = +₹1,000
    B = -₹500
    C = -₹500

After settlement:

    B pays A ₹500

Expected:

    A = +₹500
    B = ₹0
    C = -₹500

### 21.13 Settlement Testing

Settlement tests must verify:

- Valid settlement creation.
- Invalid settlement amount.
- Invalid participants.
- Same-user settlement rules if prohibited.
- Group membership.
- Balance impact.
- Settlement history.
- Settlement deletion/update behavior.

The exact business rules will follow Section 10.

### 21.14 Group Testing

Group tests must cover:

- Group creation.
- Group update.
- Group deletion where supported.
- Group ownership.
- Member addition.
- Member removal.
- Membership status.
- Duplicate membership.
- Offline group creation.
- Group synchronization.

### 21.15 User and Device Testing

Tests must verify:

- Local user creation.
- Stable `userId`.
- Device ID generation.
- Stable `deviceId`.
- User-device association.
- One active local user per installation in V1.
- Application restart behavior.
- Device registration when online.

### 21.16 Room Database Testing

Room tests must verify:

- Entity persistence.
- DAO queries.
- Relationships.
- Foreign keys.
- Transactions.
- Constraint behavior.
- Synchronization metadata persistence.
- Conflict persistence.
- Deletion metadata.
- Database recovery behavior.

### 21.17 Room Migration Testing

Every Room database schema migration must have automated migration tests.

For example:

    Database Version 1
        ↓
    Migration
        ↓
    Database Version 2

The tests must verify:

- Existing user data is preserved.
- Existing groups are preserved.
- Existing expenses are preserved.
- Existing settlements are preserved.
- Synchronization metadata remains valid.
- New fields receive appropriate values.
- No unintended records are lost.

### 21.18 Repository Testing

Repositories must be tested for:

- Local reads.
- Local writes.
- Local updates.
- Local deletion.
- Mapping between domain models and entities.
- Offline behavior.
- Synchronization queue creation.
- Error handling.

Repository tests should verify that local persistence remains successful even when the network is unavailable.

### 21.19 Offline-First Testing

Offline operation must be treated as a primary test category.

Tests must verify that the application can perform core operations with:

    Internet = OFF

including:

- Create profile.
- Create group.
- View group.
- Add expense.
- Edit expense.
- Calculate split.
- View balances.
- Record settlement.
- View settlement history.

### 21.20 Network Loss Testing

Tests must simulate network loss during normal application usage.

Examples:

    Online
      ↓
    Create Expense
      ↓
    Network Lost
      ↓
    Continue Using App

Expected:

- Expense remains available locally.
- Balance remains correct.
- Synchronization becomes pending.
- No local data is lost.

### 21.21 Network Loss During Synchronization

Tests must simulate network loss during synchronization.

Example:

    Synchronization Started
        ↓
    10 Changes
        ↓
    5 Changes Processed
        ↓
    Network Lost

Expected:

- Successfully committed changes remain synchronized.
- Unprocessed changes remain pending.
- No duplicate records are created.
- Future synchronization can resume safely.

### 21.22 Application Restart Testing

The application must be tested after:

- Normal restart.
- Force close.
- Device restart.
- Background/foreground transitions.
- Network changes.

Locally persisted data must remain available.

Pending synchronization operations must remain recoverable.

### 21.23 Backend API Testing

Backend APIs must be tested for:

- Valid requests.
- Invalid requests.
- Authentication.
- Authorization.
- Resource access.
- Validation.
- Error responses.
- Pagination.
- Synchronization.
- Idempotency.
- Concurrency.

### 21.24 API Contract Testing

Android and backend communication must use stable API contracts.

Tests should verify:

- Request structure.
- Response structure.
- Required fields.
- Field types.
- Error format.
- API version compatibility.

Breaking API changes must be detected before deployment.

### 21.25 Backend Integration Testing

Backend integration tests should verify complete flows such as:

    REST Request
        ↓
    Controller
        ↓
    Application Service
        ↓
    Domain Logic
        ↓
    Repository
        ↓
    MySQL
        ↓
    Response

These tests should use a controlled test database.

### 21.26 Synchronization Testing

Synchronization requires dedicated testing.

Tests must cover:

- Push changes.
- Pull changes.
- Bidirectional synchronization.
- Incremental synchronization.
- Synchronization cursors.
- Duplicate operations.
- Retry behavior.
- Partial failures.
- Conflict detection.
- Conflict resolution.
- Deletions.
- Tombstones.

### 21.27 Idempotency Testing

The same synchronization operation must be submitted multiple times.

Example:

    OP123
    OP123
    OP123

Expected:

    One Logical Operation
        ↓
    One Business Result

No duplicate:

- Expense.
- Settlement.
- Group.
- Membership.

must be created.

### 21.28 Synchronization Cursor Testing

Tests must verify that synchronization cursors advance only after the corresponding changes have been safely processed.

Example:

    Cursor = 100

    Receive Changes 101–110

If processing fails at 106:

    Cursor must not incorrectly become 110.

The system must preserve enough state to retry safely.

### 21.29 Conflict Testing

Conflict scenarios must be explicitly tested.

Example:

    Device A
        ↓
    Expense E123 = ₹1,000

    Device B
        ↓
    Expense E123 = ₹1,500

Both changes occur independently.

Synchronization must:

- Detect the conflict.
- Avoid silent data loss.
- Apply Section 13 conflict rules.
- Preserve financial consistency.

### 21.30 Conflict Types

Tests should cover conflicts involving:

- Expense updates.
- Expense deletion versus update.
- Expense Split changes.
- Settlement updates.
- Settlement deletion versus update.
- Group membership.
- Group metadata.
- Ownership changes.

### 21.31 Peer-to-Peer Testing

Peer-to-peer synchronization must be tested separately from backend synchronization.

Tests must cover:

- Device discovery.
- Device authentication.
- Group authorization.
- Handshake.
- Capability negotiation.
- Data exchange.
- Bidirectional synchronization.
- Interrupted synchronization.
- Duplicate delivery.
- Conflict handling.
- Secure communication.

### 21.32 Nearby Device Scenario Testing

A realistic scenario should be tested:

    Device A
        ↓
    No Internet

    Device B
        ↓
    No Internet

    Both devices connected locally
        ↓
    Discover Each Other
        ↓
    Authenticate
        ↓
    Synchronize

Expected:

- Authorized data is exchanged.
- Unauthorized data is not exchanged.
- Local data remains intact.
- Changes converge correctly.

### 21.33 Multi-Device Testing

The application must be tested with multiple devices participating in the same Group.

Example:

    Group G123

    Device A
    Device B
    Device C
    Device D

Each device creates different changes while offline.

Later:

    A ↔ B
    B ↔ C
    C ↔ D
    Internet → Backend

The final state should converge to the same valid group state.

### 21.34 Mixed Synchronization Testing

The application must be tested when both synchronization channels are used.

Example:

    Device A
        ↓
    Peer Sync
        ↓
    Device B

and later:

    Device B
        ↓
    Backend Sync
        ↓
    Server

The same business changes must not be duplicated.

### 21.35 Security Testing

Security testing must cover:

- Authentication.
- Authorization.
- Group access control.
- Device authorization.
- Invalid tokens.
- Expired tokens.
- Revoked devices.
- Unauthorized synchronization.
- Unauthorized group access.
- Manipulated entity IDs.
- Manipulated user IDs.
- Manipulated roles.
- Invalid financial payloads.
- Replay attacks.
- Duplicate synchronization.

### 21.36 Local Security Testing

Local security tests should verify:

- Sensitive credentials are not stored in plain text.
- Authentication tokens are protected.
- Cryptographic material is protected.
- Unauthorized local access is restricted where applicable.
- Database protection works as designed.

### 21.37 API Security Testing

API security tests must attempt to access protected resources using:

- Invalid credentials.
- Expired credentials.
- Valid credentials for the wrong user.
- Valid credentials for a non-member.
- Manipulated `groupId`.
- Manipulated `expenseId`.
- Manipulated `userId`.

Expected result:

    Access Denied

where the request is unauthorized.

### 21.38 Financial Integrity Testing

Financial integrity must be tested independently from security.

Examples include:

    Expense = ₹1,000

    Splits:
        A = ₹500
        B = ₹500

Valid.

But:

    Expense = ₹1,000

    Splits:
        A = ₹600
        B = ₹500

Invalid.

The backend and Android application must reject invalid financial states according to the business rules.

### 21.39 Property-Based Testing

Where appropriate, property-based tests may be used for financial calculations.

Examples of properties include:

    Sum(Splits) = Expense Amount

    Sum(Percentages) = 100%

    Total Group Balance = 0

For a valid group:

    Sum(All Member Net Balances) = 0

These properties should hold regardless of the number of members or expenses.

### 21.40 Balance Conservation Testing

One of the most important financial invariants is:

    Sum(All Member Balances) = 0

For every valid group state.

Tests must verify this after:

- Expense creation.
- Expense modification.
- Expense deletion.
- Settlement creation.
- Settlement modification.
- Settlement deletion.
- Synchronization.
- Conflict resolution.

### 21.41 Transaction Testing

Database transactions must be tested for atomicity.

Example:

    Create Expense
        +
    Create Splits
        +
    Create Sync Operation

If any operation fails:

    Rollback All

Expected:

    No Partial Expense State

### 21.42 Failure Injection Testing

The system should deliberately simulate failures such as:

- Database failure.
- Network timeout.
- Connection loss.
- Invalid API response.
- Synchronization interruption.
- Peer disconnection.
- Backend unavailable.
- Authentication expiration.

The application must recover without losing valid local data.

### 21.43 Performance Testing

Performance tests should measure:

- Expense creation time.
- Balance calculation time.
- Group loading time.
- Database query performance.
- Synchronization duration.
- API response time.
- Peer synchronization duration.

Tests should use realistic dataset sizes.

### 21.44 Large Dataset Testing

The system should be tested with progressively larger datasets.

Examples may include:

    Small Group
        5 members
        100 expenses

    Medium Group
        25 members
        1,000 expenses

    Large Group
        100+ members
        10,000+ expenses

The exact production limits will be determined through performance testing.

### 21.45 Synchronization Load Testing

The backend should be tested with multiple devices synchronizing simultaneously.

Example:

    1,000 Devices
        ↓
    Connectivity Restored
        ↓
    Synchronization Burst
        ↓
    Backend

The exact load-test scale will be determined according to the expected deployment size.

The test must verify:

- API stability.
- Database stability.
- Synchronization correctness.
- No duplicate records.
- Acceptable latency.

### 21.46 Concurrency Testing

Concurrent operations must be tested.

Examples:

    Device A
        ↓
    Update Expense E123

    Device B
        ↓
    Update Expense E123

Tests must verify that:

- Concurrent updates are detected.
- Conflict rules are applied.
- No silent overwrite occurs where prohibited.
- Financial consistency remains intact.

### 21.47 Migration Testing

Both Android and backend database migrations must be tested.

Testing must include:

- Upgrade from previous schema.
- Multiple consecutive migrations.
- Existing production-like data.
- Migration failure.
- Recovery.
- Data integrity after migration.

### 21.48 Backward Compatibility Testing

The system should test synchronization between compatible application versions.

For example:

    Device A
        ↓
    SplitSync V1.x

    Device B
        ↓
    SplitSync V1.y

If the synchronization protocol is compatible, synchronization should work correctly.

Incompatible protocol versions must fail safely without corrupting data.

### 21.49 UI Testing

UI tests should verify major user flows.

Examples:

    Create Profile
        ↓
    Create Group
        ↓
    Add Expense
        ↓
    View Balance
        ↓
    Record Settlement

Additional flows:

    Offline
        ↓
    Create Expense
        ↓
    Restore Connectivity
        ↓
    Synchronize

### 21.50 End-to-End Testing

End-to-end tests should verify complete real-world scenarios.

Example:

    Install App
        ↓
    Create User
        ↓
    Create Group
        ↓
    Add Members
        ↓
    Disconnect Internet
        ↓
    Create Expenses
        ↓
    Calculate Balances
        ↓
    Connect Devices Locally
        ↓
    Peer Synchronization
        ↓
    Restore Internet
        ↓
    Backend Synchronization
        ↓
    Verify Final State

The final state must be consistent across authorized devices.

### 21.51 Regression Testing

Every new feature must include regression tests for existing functionality.

Changes to:

- Expense calculations.
- Database schema.
- Synchronization.
- Conflict resolution.
- API contracts.

must trigger relevant automated test suites.

### 21.52 Continuous Integration Testing

The project should use CI to automatically execute tests after changes are pushed.

A typical CI pipeline should include:

    Git Push
        ↓
    Build
        ↓
    Static Checks
        ↓
    Unit Tests
        ↓
    Integration Tests
        ↓
    API Tests
        ↓
    Android Tests
        ↓
    Result

The exact CI platform and workflow will be defined during project implementation.

### 21.53 Test Data

Test data should include:

- Different user counts.
- Different group sizes.
- Different expense amounts.
- Different split types.
- Different currencies where supported.
- Multiple settlements.
- Offline-created records.
- Concurrent changes.
- Deleted records.
- Conflicts.

Financial test data must include rounding edge cases.

### 21.54 Deterministic Tests

Tests must be deterministic.

Avoid relying on:

- Real internet services.
- Real user accounts.
- Random uncontrolled timing.
- Unstable peer discovery.
- Production databases.

External dependencies should be mocked, stubbed, or run in controlled test environments where appropriate.

### 21.55 Test Environment Separation

Development, testing, and production environments must remain separated.

Tests must never unintentionally modify production data.

Backend integration tests should use:

- Dedicated test database.
- Test credentials.
- Controlled configuration.

### 21.56 Test Coverage

Code coverage should be used as a supporting metric rather than the sole definition of quality.

High coverage is particularly important for:

- Expense calculations.
- Split calculations.
- Balance calculations.
- Settlement calculations.
- Synchronization.
- Conflict resolution.
- Authorization.

A high percentage of coverage does not replace meaningful scenario testing.

### 21.57 Definition of Done

A feature should not be considered complete until:

- Domain tests pass.
- Relevant repository tests pass.
- Database tests pass.
- API tests pass where applicable.
- Offline behavior is tested where applicable.
- Synchronization behavior is tested where applicable.
- Security behavior is tested where applicable.
- UI tests pass where applicable.
- Regression tests pass.

### 21.58 Testing Model Invariants

The following invariants must always hold:

- Core expense functionality must be testable without internet connectivity.
- Financial calculations must be deterministic.
- Financial calculations must use exact monetary representations.
- Valid Expense Splits must reconcile with the Expense amount.
- Valid group balances must sum to zero.
- Local writes must survive application restart.
- Network failure must not cause local data loss.
- Synchronization retries must not create duplicate business records.
- Synchronization must eventually converge for valid non-conflicting changes.
- Conflicts must not be silently overwritten where conflict resolution is required.
- Unauthorized users must not access protected group data.
- Unauthorized devices must not synchronize protected data.
- Peer-to-peer synchronization must be tested independently from backend synchronization.
- Backend synchronization must be tested independently from peer-to-peer synchronization.
- Database migrations must preserve existing data.
- Transaction failures must not leave partial financial state.
- Large datasets must remain usable within defined performance limits.
- Compatible application versions must synchronize correctly.
- Incompatible synchronization protocols must fail safely.
- Every production-critical business rule must have automated regression coverage.

## 22. Future Extensions

### 22.1 Purpose

The V1 architecture is intentionally designed to provide a stable foundation for future SplitSync features without requiring a redesign of the core expense domain.

Future features should extend the existing:

- User model.
- Device model.
- Group model.
- Expense model.
- Expense Split model.
- Settlement model.
- Offline-first architecture.
- Synchronization architecture.
- Security architecture.

Future extensions must preserve the core principle:

> New features must not unnecessarily break offline-first functionality.

### 22.2 Multiple Users Per Device

V1 supports one active local user per application installation.

A future version may support multiple users on the same device.

Potential capabilities include:

- Multiple local profiles.
- User switching.
- Local profile locking.
- Separate local data access.
- Multiple device associations per user.

The existing User and Device models should remain extensible enough to support this evolution.

### 22.3 Account-Based User Management

Future versions may provide more complete account management.

Potential features include:

- Online account registration.
- Login.
- Logout.
- Password or passwordless authentication.
- Account recovery.
- Account verification.
- Account deletion.

Offline local profiles should remain compatible with the account model where technically feasible.

### 22.4 Phone Number and Email Verification

Future versions may support verified identity/contact information.

Potential features include:

- Phone number verification.
- Email verification.
- OTP-based verification.
- Account recovery through verified contact information.

Phone number and email must remain contact/verification attributes rather than replacing the stable `userId`.

### 22.5 Profile Enhancements

Future profile capabilities may include:

- Profile photo.
- Custom avatar.
- Preferred display name.
- Optional bio.
- User preferences.
- Currency preference.
- Timezone preference.

Profile synchronization should continue to expose only information appropriate for the user's privacy settings.

### 22.6 Advanced Group Types

The Group model may be extended with additional group contexts.

Examples include:

- Trip.
- Family.
- Household.
- Friends.
- Office.
- Event.
- Project.
- Sports team.
- Shared subscription group.

These should continue to use the general-purpose Group model rather than introducing unrelated expense-management models.

### 22.7 Group Templates

Future versions may provide predefined group templates.

Examples:

    Trip
        ↓
    Travel expenses
    Accommodation
    Food
    Transport

    Household
        ↓
    Rent
    Utilities
    Groceries

Templates should configure group behavior without changing the underlying expense model.

### 22.8 Recurring Expenses

Future versions may support recurring expenses.

Examples include:

- Monthly rent.
- Subscription.
- Internet bill.
- Utility bill.
- Regular household expense.

A recurring expense should generate normal Expense records rather than becoming a fundamentally different financial entity unless required by future business rules.

### 22.9 Expense Categories

The Expense model may be extended with richer categorization.

Potential categories include:

- Food.
- Travel.
- Accommodation.
- Transport.
- Shopping.
- Entertainment.
- Utilities.
- Medical.
- Other.

Future versions may also support:

- Custom categories.
- Category icons.
- Category colors.
- Category-based reports.

### 22.10 Attachments

Future versions may allow users to attach supporting information to expenses.

Examples include:

- Receipt photos.
- Bills.
- Invoices.
- Documents.

Attachments would require additional considerations for:

- Local storage.
- Backend storage.
- Peer-to-peer transfer.
- Synchronization.
- Encryption.
- Storage limits.

Attachment synchronization should remain independent from the core financial entity synchronization where practical.

### 22.11 Receipt Scanning

A future version may support receipt scanning using the device camera.

Potential capabilities include:

- Receipt image capture.
- OCR.
- Merchant extraction.
- Amount extraction.
- Date extraction.
- Category suggestions.

The extracted information must remain user-editable and must not automatically be considered financially authoritative without validation.

### 22.12 Advanced Split Methods

Future versions may support additional splitting strategies.

Potential examples include:

- Item-level splitting.
- Percentage with adjustments.
- Weighted shares.
- Custom formulas.
- Tax-aware splitting.
- Discount-aware splitting.
- Tip splitting.
- Mixed split methods.

New split methods must preserve the core invariant:

    Sum(Expense Splits)
        =
    Expense Amount

according to the defined financial rules.

### 22.13 Item-Level Expense Splitting

A future version may allow:

    Receipt
        ↓
    Item A → User 1
    Item B → User 2
    Item C → User 1 + User 3

This would provide more detailed expense allocation.

The existing Expense and Expense Split architecture should remain the foundation for this feature.

### 22.14 Multi-Currency Support

V1 is designed around a Group using a primary currency.

Future versions may support multiple currencies within the same Group.

Potential requirements include:

- Original expense currency.
- Group base currency.
- Exchange rates.
- Conversion date.
- Manual exchange rates.
- Automatic exchange rates.
- Currency-aware settlements.

Multi-currency support must preserve historical exchange-rate information so that old financial records do not unexpectedly change when exchange rates change.

### 22.15 Advanced Settlement Optimization

Future versions may optimize settlement recommendations.

For example:

    Before Optimization

    A → B ₹500
    A → C ₹300
    D → A ₹600

may be simplified into a smaller number of transactions.

The optimization must preserve the final net balances.

Settlement optimization should remain a derived calculation and must not modify the authoritative Expense or Settlement history without explicit user action.

### 22.16 Settlement Suggestions

The application may automatically suggest:

    "Rahul should pay Amit ₹750"

based on current group balances.

Suggestions should be derived from:

- Expenses.
- Expense Splits.
- Existing settlements.

Users should explicitly confirm any actual settlement transaction.

### 22.17 Group Activity History

Future versions may provide a detailed activity timeline.

Examples:

    Rahul added an expense
    Amit joined the group
    Priya recorded a settlement
    Rahul edited an expense

Activity history may be stored as an audit-oriented model rather than modifying the primary financial entities.

### 22.18 Audit Trail

Future versions may provide stronger financial auditability.

Potential information includes:

- Who created a record.
- Who modified it.
- When it was modified.
- Previous value.
- New value.
- Synchronization source.
- Device that performed the operation.

Audit data may be particularly useful for:

- Office groups.
- Shared households.
- Large groups.
- Dispute resolution.

### 22.19 Notifications

Future versions may support notifications for:

- New group invitations.
- New expenses.
- Expense modifications.
- Settlement requests.
- Settlement confirmations.
- Synchronization completion.
- Conflict notifications.

Notifications must not become a dependency for core expense functionality.

### 22.20 Offline Notifications

Where supported by Android, local notifications may be generated without internet connectivity.

For example:

    Settlement Due
        ↓
    Local Reminder
        ↓
    Android Notification

The application should not require backend availability for locally generated reminders.

### 22.21 Real-Time Online Synchronization

Future versions may provide faster online synchronization.

Potential technologies include:

- WebSockets.
- Server-Sent Events.
- Push notifications.
- Other real-time communication mechanisms.

The local database should remain the primary application state.

Real-time communication should update synchronization state rather than bypassing the local persistence architecture.

### 22.22 Advanced Peer-to-Peer Networking

Future versions may extend local synchronization beyond direct peer-to-peer connections.

Potential capabilities include:

- Multi-hop synchronization.
- Temporary synchronization relays.
- Group synchronization hubs.
- Improved discovery.
- Automatic peer synchronization.
- More advanced local-network protocols.

These features must preserve the same authorization and synchronization semantics defined in V1.

### 22.23 Temporary Local Group Hub

A future version may allow one authorized device to act as a temporary local synchronization hub.

For example:

    Device A
       ↑
    Device B ←→ Device C
       ↑
    Device D

This could help groups synchronize without every device directly connecting to every other device.

Such a feature must not automatically grant the hub device access to data it is not authorized to access.

### 22.24 QR Code Group Joining

Future versions may support QR-code-based group joining.

For example:

    Group Owner
        ↓
    Generate QR Code
        ↓
    Friend Scans QR
        ↓
    Verify Invitation
        ↓
    Join Group

The QR code should contain only the information required to initiate the joining process.

Sensitive group financial data must not be embedded directly in the QR code.

### 22.25 Nearby User Discovery Enhancements

Future versions may improve local discovery with:

- Profile avatars.
- Friendly device names.
- Temporary pairing codes.
- QR-based verification.
- Contact matching where explicitly authorized.

Discovery must continue to minimize unnecessary exposure of personal information.

### 22.26 Group Access Links

A future version may support secure invitation links.

Potential flow:

    Generate Invitation
        ↓
    Share Link
        ↓
    User Opens Link
        ↓
    Authenticate / Verify
        ↓
    Accept Group Invitation

Invitation links must be:

- Time-limited where appropriate.
- Revocable.
- Scoped to the intended Group.
- Protected against unauthorized reuse.

### 22.27 Advanced Permissions

Future versions may introduce more granular group permissions.

Examples:

- Owner.
- Admin.
- Member.
- Viewer.
- Expense Manager.
- Settlement Manager.

Permissions should be represented independently from UI visibility.

The backend must remain authoritative for online authorization.

### 22.28 Group-Level Privacy

Future versions may allow users to control what profile information is visible to other members.

Possible controls include:

- Display name visibility.
- Phone visibility.
- Email visibility.
- Profile photo visibility.

Privacy settings must not prevent the system from maintaining the stable technical identity required for synchronization.

### 22.29 Data Export

Future versions may allow users to export group financial data.

Possible formats include:

- CSV.
- JSON.
- PDF.
- Excel.

Exports should be generated from authoritative financial records.

### 22.30 Data Import

Future versions may support importing expenses from:

- CSV.
- Excel.
- JSON.
- Other expense applications.

Imported data must pass the same financial validation rules as manually created expenses.

### 22.31 Backup and Restore

Future versions may provide explicit backup and restore capabilities.

Potential options include:

- Backend cloud backup.
- Encrypted backup files.
- Device-to-device backup.
- User-controlled export/import.

Backup and restore must preserve stable business identifiers to prevent duplicate records after restoration.

### 22.32 Encrypted Backup

Sensitive financial data may be stored in encrypted backup files.

Potential flow:

    Local Data
        ↓
    Encrypt
        ↓
    Backup
        ↓
    Restore
        ↓
    Decrypt
        ↓
    Validate
        ↓
    Local Database

The encryption key must be protected separately from the backup data where appropriate.

### 22.33 Analytics and Reports

Future versions may provide:

- Total group spending.
- Spending by member.
- Spending by category.
- Spending over time.
- Monthly reports.
- Trip summaries.
- Settlement summaries.

Analytics must be derived from authoritative financial data.

### 22.34 Charts and Visualization

Future UI versions may include:

- Expense charts.
- Category charts.
- Member contribution charts.
- Spending timelines.
- Balance visualizations.

These features should remain presentation-layer functionality and must not alter the underlying financial model.

### 22.35 Search

Future versions may provide global and group-level search.

Users may search by:

- Expense description.
- Member.
- Category.
- Date.
- Amount.
- Notes.

Search should use appropriate local database indexes and should not require internet connectivity for locally stored data.

### 22.36 Advanced Filtering

Future versions may support filters such as:

    Date Range
    Member
    Category
    Amount
    Payer
    Participant
    Settlement Status

Filtering should work against local data first.

### 22.37 Group Archiving

Future versions may support archiving completed groups.

For example:

    Goa Trip 2026
        ↓
    Trip Completed
        ↓
    Archive Group

Archived groups should remain available for historical viewing without appearing in active group lists.

### 22.38 Group Duplication

Future versions may allow users to duplicate a Group configuration.

For example:

    Goa Trip 2026
        ↓
    Duplicate
        ↓
    Goa Trip 2027

Only appropriate configuration data should be copied.

Historical expenses and settlements should not be copied unless explicitly requested.

### 22.39 Recurring Group Events

Future versions may support recurring group contexts such as:

- Monthly household expenses.
- Annual trips.
- Recurring events.

These should extend the existing Group model rather than creating unrelated business concepts.

### 22.40 Multi-Platform Clients

The backend architecture may eventually support additional clients such as:

- Web application.
- iOS application.
- Desktop application.

All clients should use the same backend business identities and synchronization model.

The Android offline-first architecture should remain compatible with this expansion.

### 22.41 Web Application

A future web client could provide:

- Group management.
- Expense management.
- Reports.
- Administrative functionality.
- Data export.

The backend REST API should remain the shared application interface.

### 22.42 iOS Application

A future iOS client could implement the same domain model and synchronization architecture.

The iOS implementation may use platform-specific:

- Local database.
- Background synchronization.
- Nearby communication.
- Secure storage.

The business identifiers and synchronization protocol should remain platform-independent.

### 22.43 Backend Service Decomposition

If the system grows significantly, the modular monolith may eventually be decomposed into services.

Potential future services include:

- User Service.
- Group Service.
- Expense Service.
- Settlement Service.
- Synchronization Service.
- Notification Service.
- Identity Service.

Service decomposition should only occur when operational or scalability requirements justify it.

### 22.44 Event-Driven Architecture

Future versions may introduce event-driven processing.

Potential events include:

    ExpenseCreated
    ExpenseUpdated
    ExpenseDeleted
    SettlementCreated
    GroupMemberAdded
    GroupMemberRemoved

Events may be used for:

- Notifications.
- Analytics.
- Audit processing.
- Search indexing.
- Background tasks.

The authoritative financial state should remain clearly defined.

### 22.45 Advanced Synchronization Protocol

Future versions may improve the synchronization protocol with:

- More efficient change sets.
- Compression.
- Streaming.
- Better conflict metadata.
- Partial synchronization.
- Prioritized synchronization.
- Resumable large transfers.

Any protocol enhancement must preserve backward compatibility where possible.

### 22.46 End-to-End Encryption

Future versions may introduce stronger end-to-end encryption for selected group data.

Potentially:

    Device A
        ↓
    Encrypt
        ↓
    Backend / Peer
        ↓
    Encrypted Data
        ↓
    Device B
        ↓
    Decrypt

This would require careful key-management architecture, especially for:

- New group members.
- Removed members.
- Device loss.
- Key rotation.
- Conflict resolution.
- Backup and restore.

### 22.47 Device Trust Management

Future versions may provide a user-facing trusted-device management screen.

For example:

    My Devices

    Pixel Phone
    Windows Device
    iPhone

Users may be able to:

- View devices.
- Rename devices.
- Revoke devices.
- Review last synchronization.
- Review device activity.

### 22.48 Data Retention Controls

Future versions may allow users to configure retention policies for:

- Archived groups.
- Synchronization history.
- Audit history.
- Deleted records.

Retention policies must never compromise synchronization correctness or historical financial integrity.

### 22.49 Advanced Conflict Resolution

Future versions may provide user-assisted conflict resolution.

For example:

    Conflict Detected

    Local:
        ₹1,000

    Remote:
        ₹1,200

    [Keep Local]
    [Keep Remote]
    [Review]

The system should preserve the conflicting states until the user or defined automatic resolution strategy resolves them.

### 22.50 AI-Assisted Features

Future versions may optionally introduce AI-assisted features such as:

- Expense categorization.
- Receipt understanding.
- Expense description suggestions.
- Spending summaries.
- Natural-language expense search.
- Group spending insights.

AI features must remain optional and must not become required for core expense management.

Sensitive financial data should be processed according to explicit privacy and security requirements.

### 22.51 Smart Settlement Suggestions

A future version may optimize settlement recommendations based on:

- Current balances.
- Number of transactions.
- Existing settlement history.
- User preferences.

The recommendation must remain a derived suggestion.

Actual financial settlement must require explicit user action.

### 22.52 Internationalization

Future versions may support:

- Multiple languages.
- Regional number formats.
- Regional date formats.
- Currency formatting.
- Timezone-aware presentation.

The underlying financial model should remain locale-independent.

### 22.53 Accessibility

Future versions should improve accessibility through:

- Screen-reader support.
- Larger text.
- High-contrast UI.
- Accessible controls.
- Keyboard navigation where applicable.
- Clear error messages.

Accessibility improvements must not change core business behavior.

### 22.54 Feature Flags

Future versions may use feature flags to introduce new functionality gradually.

Feature flags may control:

- Experimental synchronization features.
- New split methods.
- New UI functionality.
- Beta features.

Feature flags must not compromise security or financial integrity.

### 22.55 Future Extension Principles

All future extensions must follow these principles:

- Preserve stable business identifiers.
- Preserve offline-first functionality.
- Preserve financial integrity.
- Preserve synchronization correctness.
- Preserve backward compatibility where practical.
- Avoid unnecessary coupling.
- Keep derived data separate from authoritative data.
- Maintain clear security boundaries.
- Keep the core domain model reusable.
- Avoid introducing infrastructure complexity without a justified requirement.

### 22.56 Future Extensions Model Invariants

The following invariants must always hold:

- Future features must not make internet connectivity mandatory for core expense management.
- Existing Groups, Expenses, Expense Splits, and Settlements must remain compatible with future features.
- Stable `userId`, `deviceId`, `groupId`, `expenseId`, and `settlementId` identities must be preserved.
- Future synchronization mechanisms must use the existing synchronization principles.
- Future peer-to-peer mechanisms must preserve existing authorization requirements.
- Future financial features must preserve exact monetary calculations.
- Derived analytics must not replace authoritative financial records.
- Future account systems must remain compatible with locally created users where supported.
- Future multi-user device support must not invalidate the V1 one-user-per-installation model.
- Future multi-currency support must preserve historical financial correctness.
- Future encryption mechanisms must provide a safe migration path for existing data.
- Future backend decomposition must preserve API and domain contracts.
- Future clients must use compatible business identities and synchronization semantics.
- Future AI-assisted features must remain optional.
- Future scalability improvements must not compromise financial consistency.
- Future privacy features must not expose sensitive user information unnecessarily.
- Future features must remain independently testable.

## 23. Architecture Decisions

### 23.1 Purpose

This section records the major architectural decisions made for SplitSync V1.

The purpose of documenting these decisions is to:

- Preserve the reasoning behind the architecture.
- Prevent important design decisions from being unintentionally changed during implementation.
- Provide a reference for future development.
- Make future architecture changes explicit.
- Keep the Android, backend, database, and synchronization designs aligned.

Each decision should be revisited only when a concrete requirement, technical limitation, security concern, or scalability requirement justifies changing it.

### 23.2 Decision: General-Purpose Group Model

**Decision:**

SplitSync will use a general-purpose `Group` as the core shared-expense concept.

A Trip will not be implemented as a separate expense-management model in V1.

A Group may represent:

- Trip.
- Family.
- Household.
- Friends.
- Office.
- Event.
- Other shared-expense contexts.

**Reason:**

A general-purpose Group model provides better reuse and prevents unnecessary duplication in the expense domain.

Conceptually:

    Group
      ├── Trip
      ├── Household
      ├── Family
      ├── Friends
      └── Event

These are contexts of the same underlying Group concept rather than separate financial models.

### 23.3 Decision: Offline-First Architecture

**Decision:**

SplitSync will be designed as an offline-first application.

Core application functionality must work without internet connectivity.

The local Android database is the primary source for normal application reads.

**Reason:**

The application is specifically intended for situations such as:

- Trips with poor connectivity.
- Groups without internet.
- Remote locations.
- Airplane mode.
- Temporary network failures.
- Local-only group interaction.

Internet connectivity should improve synchronization rather than determine whether the application can be used.

### 23.4 Decision: Local Database as Primary Application State

**Decision:**

Room/SQLite on Android will act as the primary local source of truth for normal application operation.

The UI will read application state from the local database through repositories.

**Reason:**

This ensures that:

- The application remains usable offline.
- Application restarts do not lose data.
- UI behavior does not depend on network responses.
- Synchronization can operate independently.
- Local changes are immediately available.

The backend is a synchronization and shared persistence system rather than the direct source for every Android screen.

### 23.5 Decision: Java for Android

**Decision:**

The Android application will be implemented using Java.

**Reason:**

Java is the selected implementation language for the project and provides compatibility with the intended Android architecture and development workflow.

The architecture should remain compatible with standard Android development practices.

### 23.6 Decision: Room + SQLite for Android Persistence

**Decision:**

Android local persistence will use:

- Room.
- SQLite.

**Reason:**

Room provides:

- Structured local persistence.
- DAO abstraction.
- Transactions.
- Query support.
- Database migrations.
- Reactive observation.
- Compile-time query validation.

SQLite provides the underlying local database.

### 23.7 Decision: Spring Boot for Backend

**Decision:**

The backend will use Java with Spring Boot.

**Reason:**

Spring Boot provides a mature ecosystem for:

- REST APIs.
- Dependency injection.
- Security.
- Transactions.
- Persistence.
- Validation.
- Testing.
- Production deployment.

The backend will initially remain a modular monolith.

### 23.8 Decision: Hibernate/JPA for Persistence

**Decision:**

The backend will use Hibernate as the JPA implementation.

**Reason:**

Hibernate/JPA provides:

- Object-relational mapping.
- Transaction management integration.
- Repository integration.
- Relationship mapping.
- Database abstraction.

Persistence concerns will remain separated from the Domain and API layers.

### 23.9 Decision: MySQL for Backend Database

**Decision:**

MySQL will be used as the primary backend relational database for V1.

**Reason:**

The application requires strong relational consistency for:

- Users.
- Devices.
- Groups.
- Group Memberships.
- Expenses.
- Expense Splits.
- Settlements.

MySQL provides the relational constraints and transaction capabilities required for these financial relationships.

### 23.10 Decision: REST API

**Decision:**

Android-to-backend communication will use versioned REST APIs.

V1 APIs will use:

    /api/v1/

**Reason:**

REST provides:

- Clear resource boundaries.
- Platform independence.
- Easy debugging.
- Straightforward Android integration.
- Compatibility with future clients.

The API will not be required for normal offline expense operations.

### 23.11 Decision: Stable UUID-Based Business Identifiers

**Decision:**

Synchronizable business entities will use stable UUID-based identifiers generated independently of the backend.

Primary identifiers include:

- `userId`
- `deviceId`
- `groupId`
- `expenseId`
- `expenseSplitId`
- `settlementId`

**Reason:**

Entities must be creatable while offline.

If identifiers depended on backend-generated database IDs:

    Offline Device
        ↓
    Create Expense
        ↓
    Backend ID Required
        ↓
    Internet Required

This would violate the offline-first architecture.

Locally generated stable identifiers allow:

    Offline Device
        ↓
    Generate UUID
        ↓
    Save Entity
        ↓
    Synchronize Later

### 23.12 Decision: One Active Local User Per Installation in V1

**Decision:**

V1 will support one active local user per application installation.

**Reason:**

This simplifies:

- Local identity.
- Device association.
- Authentication state.
- Synchronization.
- Security.
- Peer-to-peer identity.

The architecture will remain extensible for multiple local users in a future version.

### 23.13 Decision: Application-Level Device Identity

**Decision:**

SplitSync will use its own application-level `deviceId`.

The application will not use Android hardware identifiers such as IMEI as the primary identity.

**Reason:**

Application-level identity provides:

- Better privacy.
- Platform independence.
- Stable synchronization identity.
- Independence from hardware-specific identifiers.
- Easier device lifecycle management.

### 23.14 Decision: Repository Pattern

**Decision:**

The Android application will use repositories between the Domain Layer and data sources.

Conceptually:

    Use Case
        ↓
    Repository
        ↓
    Local / Remote / Synchronization

**Reason:**

Repositories isolate the business layer from:

- Room.
- Retrofit.
- REST APIs.
- Peer-to-peer communication.

This allows the data implementation to evolve without changing business logic.

### 23.15 Decision: Domain Layer Independent of Infrastructure

**Decision:**

Core business logic must remain independent of:

- Android UI.
- Room.
- SQLite.
- REST.
- Retrofit.
- Spring Boot.
- MySQL.

**Reason:**

Expense calculations, balance calculations, and settlement logic must work:

- Offline.
- In unit tests.
- Without a network.
- Without a backend.
- Without a database implementation.

### 23.16 Decision: Expense Calculations Performed Locally

**Decision:**

Expense Split and balance calculations will be performed locally on Android.

The backend may validate or reproduce these calculations, but the Android application must not depend on the backend to calculate balances.

**Reason:**

A user must be able to determine:

- Who paid.
- Who owes.
- Who is owed.
- Current group balances.

even when completely offline.

### 23.17 Decision: Exact Monetary Representation

**Decision:**

Financial amounts must not use binary floating-point types such as `float` or `double`.

The system will use exact monetary representations.

Android may use integer minor units where appropriate.

Backend MySQL may use `DECIMAL`.

**Reason:**

Financial calculations must be deterministic and must not suffer from floating-point precision errors.

### 23.18 Decision: Balance as Derived Data

**Decision:**

Balances will be derived from authoritative financial records.

The authoritative data consists primarily of:

    Expenses
        +
    Expense Splits
        +
    Settlements

**Reason:**

Storing balances as the only authoritative state creates synchronization and consistency problems.

A derived balance can be recalculated whenever required.

Future performance optimizations may cache derived balances, but cached balances must not replace authoritative financial records.

### 23.19 Decision: Transactional Financial Operations

**Decision:**

Related financial records must be persisted transactionally.

For example:

    Expense
        +
    Expense Splits
        +
    Synchronization Metadata

must be persisted consistently.

**Reason:**

A partially persisted financial operation could produce invalid balances or synchronization corruption.

### 23.20 Decision: Synchronization as a Separate Layer

**Decision:**

Synchronization will be implemented as a separate architectural layer rather than being embedded directly inside UI or business logic.

**Reason:**

Synchronization has its own responsibilities:

- Change tracking.
- Retry.
- Incremental synchronization.
- Idempotency.
- Conflict detection.
- Conflict resolution.
- Peer synchronization.
- Backend synchronization.

Keeping it separate prevents synchronization complexity from spreading throughout the application.

### 23.21 Decision: Multiple Synchronization Channels

**Decision:**

SplitSync will support two synchronization channels:

- Backend synchronization through the internet.
- Local peer-to-peer synchronization.

**Reason:**

The two channels solve different connectivity problems.

Online:

    Device
        ↓
    Internet
        ↓
    Backend

Offline but nearby:

    Device
        ↕
    Nearby Authorized Device

Both channels must use the same business entities and synchronization semantics.

### 23.22 Decision: Peer-to-Peer Is Not the Primary Source of Truth

**Decision:**

Peer-to-peer communication is a synchronization mechanism, not the primary source of application state.

**Reason:**

The application must continue working even when:

- No internet exists.
- No nearby device exists.
- Peer discovery fails.
- Peer synchronization fails.

The local Room database remains the primary local application state.

### 23.23 Decision: Same Synchronization Model for Backend and Peer-to-Peer

**Decision:**

Backend synchronization and peer-to-peer synchronization must use compatible:

- Entity identities.
- Operation identities.
- Versioning.
- Conflict rules.
- Deletion rules.
- Validation rules.

**Reason:**

A change must remain the same logical change regardless of how it travels.

For example:

    Expense E123

must remain:

    Expense E123

whether it reaches a device through:

    Device → Backend → Device

or:

    Device → Peer → Device

### 23.24 Decision: Incremental Synchronization

**Decision:**

Synchronization will use incremental changes rather than repeatedly transferring complete group datasets.

**Reason:**

Incremental synchronization reduces:

- Network usage.
- Battery consumption.
- Database load.
- Processing time.
- Synchronization latency.

This becomes increasingly important as group histories grow.

### 23.25 Decision: Idempotent Synchronization

**Decision:**

Synchronization operations must be idempotent.

**Reason:**

Network failures may cause clients to retry operations.

For example:

    OP123
        ↓
    Timeout
        ↓
    Retry OP123

The backend or receiving device must not create duplicate business records.

### 23.26 Decision: Conflict Resolution Is Explicit

**Decision:**

Concurrent modifications must be detected and handled through explicit conflict-resolution rules.

The system must not silently overwrite one device's changes merely because another change arrived later.

**Reason:**

Multiple devices may operate independently while offline.

Example:

    Device A
        ↓
    Expense E123 = ₹1,000

    Device B
        ↓
    Expense E123 = ₹1,500

These changes must be recognized as concurrent changes and handled according to Section 13.

### 23.27 Decision: Deletion Through Synchronizable Metadata

**Decision:**

Synchronizable entities may use logical deletion/tombstone metadata rather than immediately removing all information required for synchronization.

**Reason:**

Without deletion metadata:

    Device A
        ↓
    Delete Expense E123

    Device B
        ↓
    Still Has Expense E123

The deleted expense could reappear during synchronization.

Deletion information must therefore survive long enough to propagate safely.

### 23.28 Decision: Backend as Central Synchronization Point When Online

**Decision:**

When internet connectivity is available, the backend acts as the centralized synchronization point for authorized devices.

**Reason:**

The backend provides:

- Central persistence.
- Cross-device synchronization.
- Authentication.
- Authorization.
- Recovery support.
- Shared remote state.

However, backend availability must not determine whether the Android application can perform core local operations.

### 23.29 Decision: Modular Monolith for Backend V1

**Decision:**

The backend will be implemented as a modular monolith rather than microservices.

**Reason:**

V1 benefits more from:

- Simpler deployment.
- Easier debugging.
- Lower infrastructure complexity.
- Easier transactions.
- Faster development.
- Clear module boundaries.

The architecture will preserve module boundaries so that future service decomposition remains possible.

### 23.30 Decision: DTO Separation

**Decision:**

API DTOs, Domain Models, and Database Entities should remain separate where appropriate.

Conceptually:

    Request DTO
        ↓
    Domain Model
        ↓
    JPA Entity

and:

    JPA Entity
        ↓
    Domain Model
        ↓
    Response DTO

**Reason:**

This prevents:

- Database changes from unnecessarily breaking APIs.
- API changes from unnecessarily affecting domain logic.
- Persistence-specific concerns from leaking into the Domain Layer.

### 23.31 Decision: Backend Validation Is Authoritative

**Decision:**

The backend must independently validate data received from clients.

**Reason:**

Android clients cannot be considered fully trusted.

A client may be:

- Outdated.
- Buggy.
- Modified.
- Compromised.

Therefore:

    Client Validation
        +
    Backend Validation

are both required.

### 23.32 Decision: Local Network Is Not Automatically Trusted

**Decision:**

Being connected to the same Wi-Fi or hotspot does not automatically authorize synchronization.

**Reason:**

A local network may contain unknown or malicious devices.

The security model must therefore distinguish:

    Same Network

from:

    Authorized SplitSync Device

Peer-to-peer synchronization requires application-level authentication and authorization.

### 23.33 Decision: One Group Uses One Primary Currency in V1

**Decision:**

V1 will use a single primary currency per Group.

**Reason:**

This keeps:

- Expense calculations.
- Split calculations.
- Balance calculations.
- Settlement calculations.

simple and deterministic for V1.

Multi-currency support is reserved for a future extension.

### 23.34 Decision: Group as Financial Boundary

**Decision:**

Expenses, Expense Splits, and Settlements are associated with a Group.

**Reason:**

The Group provides the primary authorization and financial context.

Conceptually:

    Group
        ├── Members
        ├── Expenses
        ├── Expense Splits
        └── Settlements

This also simplifies synchronization and access control.

### 23.35 Decision: Historical Financial Records Are Preserved

**Decision:**

Leaving a Group must not automatically delete historical Expenses or Settlements involving the user.

**Reason:**

Financial history must remain consistent.

A user's current membership state and historical participation are separate concepts.

### 23.36 Decision: Local Changes Are Persisted Before Synchronization

**Decision:**

A local write must be successfully persisted before it is considered available for synchronization.

The conceptual flow is:

    User Action
        ↓
    Local Transaction
        ↓
    Local Data Persisted
        ↓
    Sync Operation Created
        ↓
    Synchronization Later

**Reason:**

Network failure must never cause loss of a successfully created local expense.

### 23.37 Decision: Synchronization Must Not Block the UI

**Decision:**

Synchronization must run independently of the main UI flow.

**Reason:**

A user should be able to continue using the application while:

- Backend synchronization is running.
- Peer synchronization is running.
- Network retries are occurring.

The UI should observe local database state rather than waiting for synchronization to complete.

### 23.38 Decision: Background Synchronization

**Decision:**

Android background synchronization should use appropriate Android background execution mechanisms, with WorkManager as the primary candidate.

**Reason:**

Synchronization may need to continue when the application is not actively visible.

The implementation must respect:

- Android lifecycle.
- Battery restrictions.
- Connectivity constraints.
- Retry behavior.

### 23.39 Decision: Security Before Conflict Resolution

**Decision:**

Authentication and authorization must occur before a received change can participate in conflict resolution.

The conceptual flow is:

    Incoming Change
        ↓
    Authentication
        ↓
    Authorization
        ↓
    Validation
        ↓
    Conflict Detection
        ↓
    Conflict Resolution
        ↓
    Persistence

**Reason:**

An unauthorized change must never become a valid candidate for conflict resolution.

### 23.40 Decision: No Microservices in V1

**Decision:**

Microservices are explicitly outside the initial backend architecture.

**Reason:**

They would introduce unnecessary complexity in:

- Deployment.
- Networking.
- Service discovery.
- Distributed transactions.
- Monitoring.
- Debugging.

The modular monolith provides sufficient architectural separation for V1.

### 23.41 Decision: Scalability Through Modular Design

**Decision:**

V1 will prepare for future horizontal scaling through:

- Stateless backend application design.
- Persistent synchronization state.
- Proper database indexing.
- Incremental synchronization.
- Pagination.
- Clear module boundaries.

**Reason:**

This provides a practical scaling path without prematurely introducing distributed infrastructure.

### 23.42 Decision: Testing Offline as a First-Class Requirement

**Decision:**

Offline operation is a primary testing requirement rather than an edge case.

**Reason:**

Offline functionality is one of SplitSync's core product requirements.

Testing must therefore explicitly cover:

- No internet.
- Network loss.
- Peer-only connectivity.
- Backend-only connectivity.
- Mixed synchronization.
- Complete offline operation.

### 23.43 Decision: Financial Integrity Is More Important Than Convenience

**Decision:**

When a conflict or invalid financial state cannot be safely resolved automatically, the system must prefer preserving data integrity over silently choosing a potentially incorrect value.

**Reason:**

Incorrect financial data can affect:

- Balances.
- Settlements.
- Group trust.
- Historical records.

The system should never silently corrupt financial state for the sake of synchronization convenience.

### 23.44 Decision: Architecture Documentation Is Versioned

**Decision:**

The architecture document will serve as the primary architectural reference for V1.

Significant architectural changes should be documented rather than made silently during implementation.

For each major change, the project should record:

- Previous decision.
- New decision.
- Reason for change.
- Impact.
- Migration requirements.

### 23.45 Architecture Decision Status

The current decisions should be considered:

    Status: Accepted for V1

The architecture remains subject to refinement during detailed implementation when concrete technical constraints are discovered.

Such refinements must not violate the established core invariants without an explicit architecture decision.

### 23.46 Architecture Decision Summary

The major V1 architectural decisions are summarized below:

| Area | V1 Decision |
|---|---|
| Product Model | General-purpose Group |
| Trip Model | Specialized Group context |
| Android Language | Java |
| Local Database | Room + SQLite |
| Backend Language | Java |
| Backend Framework | Spring Boot |
| ORM | Hibernate/JPA |
| Backend Database | MySQL |
| API Style | Versioned REST |
| API Version | `/api/v1/` |
| Local Architecture | Offline-first |
| Local Source of Truth | Room database |
| Backend Architecture | Modular Monolith |
| Business IDs | Locally generated stable UUIDs |
| Local Users | One active user per installation |
| Device Identity | Application-level `deviceId` |
| Expense Calculation | Local/domain-based |
| Monetary Representation | Exact, non-floating-point |
| Synchronization | Incremental |
| Sync Channels | Backend + Peer-to-Peer |
| Sync Idempotency | Required |
| Conflict Handling | Explicit |
| Deletion Sync | Tombstone/deletion metadata |
| Peer Trust | Explicit authentication + authorization |
| Group Security Boundary | Group membership |
| V1 Currency Model | One primary currency per Group |
| Balance Storage | Derived from authoritative records |
| Backend Scaling | Horizontally scalable in future |
| V1 Deployment Model | Modular monolith |
| Testing | Offline + Online + Sync + P2P |
| Microservices | Future, not V1 |

### 23.47 Architecture Decision Invariants

The following decisions are considered foundational for SplitSync V1:

- SplitSync is offline-first.
- Core expense functionality must work without internet connectivity.
- Room/SQLite is the primary local application state.
- The backend is a synchronization and shared persistence system.
- A Group is the core shared-expense concept.
- Trip is a Group context rather than a separate financial model.
- Stable identifiers must be generated independently of backend availability.
- Financial calculations must be deterministic and exact.
- Backend and peer-to-peer synchronization must use compatible business identities.
- Synchronization must be idempotent.
- Conflicts must be explicitly detected and resolved.
- Unauthorized data must never be accepted merely because it arrived through a synchronization channel.
- Financially related changes must remain transactionally consistent.
- Backend V1 will remain a modular monolith.
- The architecture must remain extensible without unnecessarily redesigning the core expense domain.

## 24. Open Questions

### 24.1 Purpose

This section contains architectural and product decisions that are intentionally not finalized in V1.

These questions must be resolved before implementing the corresponding functionality.

Open questions should not block the overall architecture unless they affect a foundational decision.

### 24.2 User Authentication

What authentication mechanism should be used when internet connectivity is available?

Possible options:

- Phone number + OTP.
- Email + password.
- Email + OTP.
- Passwordless authentication.
- Combination of phone number and email.

The final mechanism must remain compatible with the offline local-profile model.

### 24.3 Local Profile to Backend Account Linking

How should an offline-created local profile become associated with a backend account later?

Possible flow:

    Local User
        ↓
    Internet Available
        ↓
    Account Creation / Login
        ↓
    Link Local User
        ↓
    Backend User

The exact identity-linking mechanism must prevent duplicate users and accidental account merging.

### 24.4 Phone Number and Email Requirement

Should V1 require:

- Phone number.
- Email.
- Both.
- Neither during offline profile creation.

The current architecture allows local profile creation without internet, but the final identity/verification rules are still open.

### 24.5 User Profile Visibility

What profile information should be visible to:

- Group members.
- Nearby SplitSync devices.
- Users receiving invitations.
- Backend services.

Possible fields include:

- Display name.
- Profile photo.
- Phone number.
- Email.

Privacy rules must be finalized before implementing profile discovery and peer-to-peer synchronization.

### 24.6 Group Joining Mechanism

How should a user join a Group while offline?

Possible options:

- Nearby device invitation.
- QR code.
- Short pairing code.
- Local network invitation.
- Combination of the above.

The selected mechanism must work without internet connectivity.

### 24.7 Group Invitation Approval

Should joining a Group require:

- Owner approval.
- Existing member approval.
- Invitation-only access.
- Direct local acceptance.

The V1 permission model must define who is allowed to invite and approve members.

### 24.8 Offline Membership Changes

Which membership operations should be allowed while completely offline?

Possible operations:

- Add member.
- Remove member.
- Accept invitation.
- Reject invitation.
- Change role.
- Transfer ownership.

The authorization rules for each operation must be finalized before implementing offline membership synchronization.

### 24.9 Group Ownership

How should ownership changes work when multiple devices are offline?

For example:

    Device A
        ↓
    Makes Rahul Owner

    Device B
        ↓
    Makes Amit Owner

The final conflict-resolution behavior for ownership changes must be explicitly defined.

### 24.10 Expense Editing Rules

Who should be allowed to edit an Expense?

Possible options:

- Expense creator only.
- Expense payer.
- Any Group member.
- Group owner/admin.
- Configurable Group permission.

The V1 permission model must define the exact rule.

### 24.11 Expense Deletion Rules

Who should be allowed to delete an Expense?

Possible options:

- Expense creator.
- Expense payer.
- Group owner.
- Any member.

The system must also define whether deletion is:

- Permanent.
- Logical deletion.
- Recoverable.

### 24.12 Settlement Editing Rules

Who should be allowed to modify or delete a Settlement?

Possible options:

- Settlement creator.
- Payer.
- Receiver.
- Group owner/admin.
- Any Group member.

This must be finalized before implementing settlement authorization.

### 24.13 Settlement Model

Should a Settlement represent:

- A real payment that occurred.
- A confirmation that an amount was paid.
- A manual balance adjustment.

The V1 meaning of Settlement must be explicitly fixed so that synchronization and balance calculations remain consistent.

### 24.14 Settlement Direction

Should Settlement always represent:

    paidBy → paidTo

or should the system support a more general adjustment model?

The preferred V1 model should remain simple and deterministic.

### 24.15 Currency Representation

V1 currently assumes one primary currency per Group.

Open questions include:

- Should the currency be selected when the Group is created?
- Can the Group currency be changed later?
- What happens when the currency is changed after Expenses exist?
- Should currency changes be prohibited once financial records exist?

### 24.16 Decimal Precision

What is the exact monetary precision supported by V1?

For example:

    ₹1.00
    ₹1.50

The system must define:

- Minimum monetary unit.
- Decimal scale.
- Rounding rules.
- Division rules.
- Percentage precision.

### 24.17 Rounding Rules

How should rounding be handled when an Expense cannot be divided evenly?

Example:

    Expense = ₹100
    Members = 3

Possible allocation:

    ₹33.33
    ₹33.33
    ₹33.34

The exact rule for assigning the rounding remainder must be deterministic.

### 24.18 Expense Split Rounding

When using:

- Equal split.
- Percentage split.
- Shares split.

how should rounding differences be distributed?

The final rule must ensure:

    Sum(Splits) = Expense Amount

### 24.19 Expense Date and Time

Should an Expense contain:

- Date only.
- Date + time.
- User-selected date/time.
- Device creation timestamp.
- Server timestamp when synchronized.

The system should distinguish between:

- Business event time.
- Local creation time.
- Synchronization time.

### 24.20 Time Zone Handling

How should timestamps be handled when Group members are in different time zones?

This is particularly relevant for trips.

Possible approach:

- Store timestamps in UTC.
- Convert to local timezone for display.
- Preserve original event timezone where required.

### 24.21 Expense Notes

Should Expenses support free-form notes in V1?

If supported:

- Maximum length.
- Formatting.
- Searchability.
- Synchronization behavior.

must be defined.

### 24.22 Expense Categories

Should Expense categories be included in V1 or deferred?

If included:

- Fixed categories.
- Custom categories.
- Group-specific categories.

must be decided.

### 24.23 Expense Attachments

Should receipt images or files be supported in V1?

If yes, the architecture must define:

- Local storage.
- Backend storage.
- Peer-to-peer transfer.
- Encryption.
- Maximum size.
- Synchronization behavior.

If not, attachments should remain a future extension.

### 24.24 Peer-to-Peer Technology

Which Android technology should be used for local communication?

Candidates include:

- Nearby Connections.
- Wi-Fi Direct.
- Local network sockets.
- Other Android-supported mechanisms.

The final selection must consider:

- Android version compatibility.
- Reliability.
- Discovery.
- Security.
- Battery consumption.
- Multiple-device support.
- Background restrictions.

### 24.25 Peer-to-Peer Pairing

How should two devices establish trust?

Possible options:

- QR code.
- Numeric verification code.
- Nearby confirmation.
- Cryptographic challenge-response.
- Combination of methods.

The selected mechanism must prevent unauthorized devices from joining synchronization sessions.

### 24.26 Peer-to-Peer User Experience

Should synchronization be:

- Fully automatic.
- User initiated.
- Automatic after user approval.
- Configurable.

The preferred V1 experience should minimize manual technical steps while keeping authorization explicit.

### 24.27 Peer-to-Peer Discovery Frequency

How often should the application search for nearby SplitSync devices?

Potential strategies:

- Only when the user opens a Group.
- Only when synchronization is requested.
- While the application is active.
- Background discovery.

The decision must balance usability, battery consumption, and Android background restrictions.

### 24.28 Backend Synchronization Frequency

When internet connectivity becomes available, should synchronization happen:

- Immediately.
- Periodically.
- On application launch.
- When the user opens a Group.
- Through WorkManager.
- Through a combination of these.

The final strategy must balance synchronization speed and battery usage.

### 24.29 Sync Priority

Should some synchronization operations have higher priority?

For example:

    Group Membership
        ↓
    Higher Priority

    Expense
        ↓
    Normal Priority

    Historical Data
        ↓
    Lower Priority

The dependency model must be defined before introducing priority-based synchronization.

### 24.30 Sync Ordering

Should synchronization operations be processed according to:

- Creation time.
- Operation sequence.
- Entity dependency.
- Group.
- Priority.

The final ordering must preserve business consistency.

### 24.31 Conflict Resolution Strategy

Section 13 defines the conflict-resolution architecture, but the exact strategy for each entity type must be finalized.

For example:

- Expense update vs update.
- Expense update vs delete.
- Settlement update vs update.
- Membership update vs update.
- Ownership change vs ownership change.

Each conflict type must have deterministic behavior.

### 24.32 Conflict User Interface

Should users be informed about synchronization conflicts?

If yes, the UI must define:

- Where conflicts appear.
- What information is shown.
- Whether users can manually resolve them.
- Whether conflicts block synchronization.
- How resolved conflicts are recorded.

### 24.33 Deletion Retention

How long should tombstones be retained?

The system must ensure that a device that was offline for a long time cannot accidentally resurrect deleted data.

The final retention strategy should consider:

- Device synchronization frequency.
- Group activity.
- Storage requirements.

### 24.34 Offline Group Lifetime

Should a completely offline Group remain valid indefinitely?

Or should there be any restrictions when:

- The Group has never synchronized with the backend.
- The Group has been inactive for a long time.
- All known members are offline.

The preferred V1 behavior should preserve local data unless the user explicitly removes it.

### 24.35 Offline User Lifetime

Should a locally created user remain valid indefinitely until the user deletes the profile?

The final lifecycle rules must define:

- Profile deletion.
- Device reset.
- Application uninstall.
- Reinstallation.
- Account linking.

### 24.36 Application Reinstallation

What should happen when the application is uninstalled and reinstalled?

By default, Android application data may be removed.

The architecture must define whether the user can recover the previous local identity through:

- Backend account.
- Backup.
- Export/import.
- Device restore.

### 24.37 Device Replacement

How should a user move their SplitSync identity to a new device?

Potential approaches:

- Backend account login.
- Encrypted backup.
- Device-to-device transfer.
- QR-based migration.

This becomes especially important when the original device contains offline-only data.

### 24.38 Lost Device

What should happen when a device containing unsynchronized offline data is lost?

The architecture must define:

- Device revocation.
- Account security.
- Recovery options.
- Backup strategy.
- Whether unsynchronized data can be recovered.

A completely offline device cannot synchronize data that it cannot connect to the network with.

### 24.39 Backend Account Recovery

How should a user recover their backend identity if they lose their device?

Possible mechanisms:

- Phone OTP.
- Email verification.
- Recovery codes.
- Another trusted device.

The final mechanism must be defined with security requirements.

### 24.40 Backend Hosting

Where will the V1 backend be deployed?

Potential options include:

- Cloud VM.
- Container-based hosting.
- Managed application platform.
- Self-hosted server.

The deployment environment must support:

- HTTPS.
- MySQL.
- Secure secrets.
- Backups.
- Monitoring.

### 24.41 MySQL Deployment

Will MySQL be:

- Self-hosted.
- Managed.
- Containerized for development.
- Managed separately in production.

Development and production deployment strategies may differ.

### 24.42 Backup Strategy

How frequently should backend MySQL backups occur?

The system should eventually define:

- Backup frequency.
- Retention.
- Encryption.
- Restore testing.
- Disaster recovery.

### 24.43 Disaster Recovery

What Recovery Point Objective (RPO) and Recovery Time Objective (RTO) are required?

These values will determine the appropriate:

- Backup frequency.
- Replication strategy.
- Recovery procedures.

This is not required for initial local development but should be defined before production deployment.

### 24.44 API Authentication Mechanism

Which token/session mechanism should V1 use?

Candidates include:

- JWT access token + refresh token.
- Server-managed sessions.
- OAuth2/OIDC-based authentication.

The decision must consider:

- Android storage.
- Token expiration.
- Refresh.
- Revocation.
- Offline behavior.

### 24.45 API Rate Limiting

What rate limits should be applied?

Potentially different limits may be required for:

- Authentication.
- Normal APIs.
- Synchronization.
- Device registration.

Limits must not prevent legitimate offline devices from synchronizing after reconnecting.

### 24.46 API Versioning Policy

V1 currently uses:

    /api/v1/

Open questions include:

- When should V2 be introduced?
- How long should V1 remain supported?
- How should old Android clients synchronize with newer backends?

The synchronization protocol may require independent versioning from the REST API.

### 24.47 Synchronization Protocol Versioning

What should the peer-to-peer and backend synchronization protocol version format be?

The system should be able to determine whether two devices or clients can safely synchronize.

### 24.48 Data Retention

How long should the backend retain:

- Groups.
- Expenses.
- Settlements.
- Synchronization metadata.
- Audit records.
- Deleted records.

The retention policy must balance:

- User expectations.
- Storage cost.
- Synchronization correctness.
- Privacy.

### 24.49 Audit Requirements

Is a complete audit trail required in V1?

If yes, the architecture must define which operations are audited and how long audit records are retained.

If not, audit functionality may remain a future extension.

### 24.50 Privacy Requirements

What privacy controls are required for V1?

The final requirements should define:

- Profile visibility.
- Phone visibility.
- Email visibility.
- Group visibility.
- Expense visibility.
- Device visibility.

### 24.51 Encryption at Rest

Should the Android Room database be encrypted in V1?

If yes, the exact implementation and key-management strategy must be selected.

Backend database encryption requirements must also be defined according to the deployment environment.

### 24.52 End-to-End Encryption

Is true end-to-end encryption required for group financial data?

If introduced, the architecture must define:

- Group encryption keys.
- Key distribution.
- Key rotation.
- New member access.
- Removed member access.
- Device replacement.
- Backup and restore.

This is a significant architectural decision and should not be implemented partially.

### 24.53 Notification Strategy

Should V1 support push notifications?

If yes, the notification architecture must account for:

- Android push services.
- Offline operation.
- Group invitations.
- Settlement notifications.
- Synchronization notifications.

Notifications must remain optional for core functionality.

### 24.54 Analytics

Should V1 collect application analytics?

If yes, the architecture must define:

- What data is collected.
- Whether financial information is excluded.
- User consent.
- Data retention.
- Privacy requirements.

Financial data should not be unnecessarily included in analytics.

### 24.55 Crash Reporting

Should V1 use a crash-reporting service?

If yes, crash reports must be reviewed to ensure they do not expose:

- Expense amounts.
- Phone numbers.
- Email addresses.
- Authentication tokens.
- Private keys.
- Other sensitive information.

### 24.56 Logging Strategy

What level of logging should be enabled in:

- Development.
- Testing.
- Production.

Production logs should avoid sensitive financial and authentication data.

### 24.57 Supported Android Versions

What is the minimum Android version supported by V1?

This decision directly affects:

- Room.
- WorkManager.
- Nearby communication.
- Encryption APIs.
- Background execution.
- UI capabilities.

### 24.58 UI Technology

Should V1 use:

- XML layouts + Views.
- Jetpack Compose.
- Hybrid approach.

The selected approach should be consistent across the project unless a specific screen requires otherwise.

### 24.59 Dependency Injection

Which dependency-injection approach should be used?

Possible options include:

- Manual dependency injection.
- Dagger.
- Hilt.

The final choice should consider the Java-based Android architecture.

### 24.60 Networking Library

Which Android HTTP client should be used?

Possible options include:

- Retrofit + OkHttp.
- Another HTTP client.

The final choice should support:

- REST.
- Authentication.
- Retry.
- Timeouts.
- Error handling.
- Serialization.

### 24.61 JSON Serialization

Which serialization library should be used?

Possible options include:

- Gson.
- Jackson.
- Moshi.
- Kotlin Serialization if applicable.

The selected library must be compatible with the Java Android implementation.

### 24.62 Backend API Documentation

Should the REST API use OpenAPI/Swagger documentation in V1?

Using OpenAPI could provide:

- API documentation.
- Contract visibility.
- Easier testing.
- Client development support.

### 24.63 Database Migration Tool

Hibernate/JPA will manage persistence, but the production database migration strategy remains open.

Potential options include:

- Flyway.
- Liquibase.

A dedicated migration strategy should be selected before production database deployment.

### 24.64 Backend Testing Database

What database should be used for backend integration tests?

The preferred approach should provide behavior close to production MySQL.

Possible options include:

- Dedicated MySQL test instance.
- Containerized MySQL.
- Testcontainers.

### 24.65 CI/CD Platform

Which CI/CD platform should be used?

Potential options include:

- GitHub Actions.
- Other CI platforms.

The pipeline should eventually build and test both:

- Android application.
- Spring Boot backend.

### 24.66 Release Strategy

How should V1 releases be managed?

Possible approaches include:

- Internal testing.
- Closed beta.
- Public beta.
- Production release.

The release strategy should include database/API compatibility requirements.

### 24.67 Feature Scope for V1

The final V1 feature boundary must be confirmed before implementation begins.

The minimum expected V1 should include:

- Local profile.
- Device identity.
- Group creation.
- Group membership.
- Expense creation.
- Expense Splits.
- Balance calculation.
- Settlements.
- Local offline operation.
- Backend synchronization.
- Local peer-to-peer synchronization.
- Conflict handling.
- Security basics.

Features not required for this core V1 should remain future extensions unless explicitly promoted into scope.

### 24.68 Open Question Resolution Process

Each open question should be resolved before the corresponding implementation begins.

The decision should be recorded in Section 23.

The process should be:

    Open Question
        ↓
    Evaluate Requirements
        ↓
    Evaluate Technical Constraints
        ↓
    Select Approach
        ↓
    Record Architecture Decision
        ↓
    Implement
        ↓
    Test

### 24.69 Open Questions Status

Current status:

    Architecture V1
        ↓
    Core Structure Defined
        ↓
    Detailed Implementation Decisions Pending

Open questions do not invalidate the architecture defined in Sections 1–23.

They represent decisions that must be finalized before implementing the affected components.

### 24.70 Priority Classification

Open questions should be classified as:

**High Priority**

Questions that can affect the core architecture.

Examples:

- Authentication model.
- Peer-to-peer technology.
- Synchronization protocol.
- Conflict rules.
- Monetary precision.
- Local-to-backend identity linking.

**Medium Priority**

Questions that affect implementation but not the overall architecture.

Examples:

- UI technology.
- Networking library.
- Dependency injection.
- Database migration tooling.
- Logging strategy.

**Low Priority**

Questions that can be deferred without affecting V1 architecture.

Examples:

- Advanced reports.
- AI features.
- Additional profile customization.
- Advanced analytics.

### 24.71 Foundational Open Questions

The following questions should be resolved before production implementation of the corresponding systems:

1. What exact authentication mechanism will V1 use?
2. How will an offline local user be linked to a backend account?
3. What exact phone/email requirements will V1 have?
4. What exact peer-to-peer technology will Android use?
5. What exact peer-to-peer pairing/authentication mechanism will be used?
6. What exact synchronization protocol and cursor/version model will be used?
7. What are the exact conflict-resolution rules for each entity type?
8. What exact monetary precision and rounding rules will be used?
9. What exact Group membership and permission rules will V1 use?
10. What exact Settlement semantics will V1 use?
11. What Android minimum version will V1 support?
12. What local database encryption strategy will be used?
13. What backend authentication/token mechanism will be used?
14. What database migration strategy will be used?
15. What exact V1 feature scope will be frozen before coding begins?

### 24.72 Open Questions Invariant

Open questions must not be resolved implicitly during implementation.

Any decision that materially changes:

- Data model.
- Synchronization model.
- Security model.
- Financial model.
- API contract.
- Offline behavior.
- Peer-to-peer architecture.

must be explicitly reviewed and recorded as an Architecture Decision before implementation.
