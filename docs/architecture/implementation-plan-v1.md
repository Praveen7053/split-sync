# Implementation Plan V1

## 1. Overview

### 1.1 Purpose

This document defines the implementation plan for SplitSync V1.

The purpose of this document is to convert the Technical Design into an executable development plan covering:

```text
Android Application
+
Backend Application
+
Local Database
+
Backend Database
+
Synchronization
+
Conflict Resolution
+
Peer-to-Peer Communication
+
Authentication
+
Authorization
+
Testing
+
Deployment
```

The implementation plan defines:

```text
What to Build
+
In Which Order
+
Which Components Depend on Each Other
+
What Each Phase Must Deliver
+
How Implementation Progress Is Validated
```

### 1.2 Implementation Principle

SplitSync V1 follows:

```text
Foundation First
        ↓
Domain First
        ↓
Persistence
        ↓
Application Logic
        ↓
API
        ↓
Synchronization
        ↓
P2P
        ↓
UI
        ↓
Testing
        ↓
Deployment
```

The implementation must avoid building higher-level functionality before its required lower-level components are stable.

### 1.3 Primary Implementation Targets

The V1 implementation consists of two primary applications:

```text
Android Application
        +
Backend Application
```

The Android application is responsible for:

```text
User Interface
Local Persistence
Offline Operations
Local Domain Operations
Synchronization
P2P Communication
Authentication State
```

The Backend is responsible for:

```text
API
Authentication
Authorization
Persistent Server Data
Synchronization
Conflict Processing
Server-Side Validation
```

### 1.4 Implementation Technology

The V1 implementation is based on:

```text
Android
    ↓
Java

Backend
    ↓
Java
    ↓
Spring Boot

Database
    ↓
Relational Database

Android Local Database
    ↓
Room / SQLite-based Persistence
```

The exact library versions and infrastructure dependencies will be finalized during project setup.

### 1.5 Implementation Scope

The V1 implementation covers:

```text
User
Group
Membership
Expense
Expense Split
Settlement
Balance
SyncOperation
SyncState
Conflict
Device
Authentication
Authorization
Backend Synchronization
Peer-to-Peer Synchronization
```

### 1.6 Implementation Order

The recommended implementation order is:

```text
Phase 0
Project Foundation
        ↓
Phase 1
Domain Model
        ↓
Phase 2
Local Database
        ↓
Phase 3
Backend Database
        ↓
Phase 4
Application Services
        ↓
Phase 5
Backend API
        ↓
Phase 6
Android Repository Layer
        ↓
Phase 7
Offline-First Operations
        ↓
Phase 8
Synchronization
        ↓
Phase 9
Conflict Resolution
        ↓
Phase 10
Authentication & Authorization
        ↓
Phase 11
Android UI
        ↓
Phase 12
Peer-to-Peer Synchronization
        ↓
Phase 13
Integration & E2E Testing
        ↓
Phase 14
Deployment
```

### 1.7 Dependency Principle

Implementation dependencies should generally follow:

```text
Domain
   ↓
Persistence
   ↓
Application
   ↓
API / Synchronization
   ↓
Presentation / UI
```

The Domain layer must remain independent from:

```text
Android UI
HTTP
Database Framework
Spring Framework
P2P Transport
```

### 1.8 Implementation Milestones

Major milestones are:

```text
M1 - Project Builds
M2 - Domain Complete
M3 - Local Persistence Complete
M4 - Backend Persistence Complete
M5 - Core Expense Flow Complete
M6 - Backend API Complete
M7 - Offline-First Flow Complete
M8 - Backend Synchronization Complete
M9 - Conflict Resolution Complete
M10 - Authentication Complete
M11 - Android UI Complete
M12 - P2P Synchronization Complete
M13 - Full Integration Complete
M14 - Production Deployment Ready
```

### 1.9 Definition of Done

A feature is considered implemented only when:

```text
Code Exists
+
Required Tests Exist
+
Persistence Works
+
Error Handling Exists
+
Offline Behavior Is Defined
+
Synchronization Behavior Is Defined
+
Security Rules Are Applied
+
Documentation Is Updated
```

### 1.10 Implementation Rules

The following rules apply throughout implementation:

- Domain logic must remain independent from infrastructure.
- Financial calculations must use precise monetary representation.
- Local persistence must be treated as the primary source for offline Android operations.
- User operations must not depend on immediate Backend availability.
- Every operation requiring synchronization must produce the appropriate SyncOperation.
- Synchronization must be idempotent.
- Conflict handling must be deterministic.
- P2P and Backend synchronization must use the same logical synchronization model.
- API contracts must remain versioned.
- Database changes must use migrations.
- Authentication must be implemented before exposing protected Production APIs.
- Authorization must be enforced server-side.
- Sensitive information must not be logged.
- Production configuration must never be hard-coded.
- Tests must be implemented alongside functionality rather than postponed until the end.
- Critical financial and synchronization invariants must have automated tests.
- Each implementation phase must leave the project in a buildable state.

## 2. Implementation Phases

SplitSync V1 will be implemented through a sequence of controlled phases.

Each phase should establish a stable foundation for the next phase.

### 2.1 Phase Overview

```text
Phase 0
Project Foundation
        ↓
Phase 1
Domain Model
        ↓
Phase 2
Local Database
        ↓
Phase 3
Backend Database
        ↓
Phase 4
Application Services
        ↓
Phase 5
Backend API
        ↓
Phase 6
Android Repository Layer
        ↓
Phase 7
Offline-First Operations
        ↓
Phase 8
Synchronization
        ↓
Phase 9
Conflict Resolution
        ↓
Phase 10
Authentication & Authorization
        ↓
Phase 11
Android UI
        ↓
Phase 12
Peer-to-Peer Synchronization
        ↓
Phase 13
Integration & E2E Testing
        ↓
Phase 14
Deployment
```

---

### 2.2 Phase 0 — Project Foundation

#### Objective

Create the initial project structure and ensure that both Android and Backend applications can build successfully.

#### Tasks

```text
Create Repository
        ↓
Create Android Project
        ↓
Create Backend Project
        ↓
Configure Java
        ↓
Configure Build System
        ↓
Configure Modules
        ↓
Configure Code Style
        ↓
Configure Static Analysis
        ↓
Configure Basic CI
```

#### Android Tasks

Implement:

```text
Android Application Module
Gradle Configuration
Build Types
Product Flavors
Application Configuration
Base Package Structure
```

#### Backend Tasks

Implement:

```text
Spring Boot Application
Build Configuration
Base Package Structure
Application Configuration
Health Endpoint
```

#### CI Tasks

Configure:

```text
Build
Unit Test Execution
Static Analysis
Artifact Generation
```

#### Expected Result

```text
Android Project → Builds Successfully

Backend Project → Builds Successfully

CI → Executes Successfully
```

---

### 2.3 Phase 1 — Domain Model

#### Objective

Implement the core business model without infrastructure dependencies.

#### Domain Components

```text
User
Group
Membership
Expense
ExpenseSplit
Settlement
Balance
Device
SyncOperation
SyncState
Conflict
```

#### Tasks

Implement:

```text
Domain Entities
Value Objects
Enums
Domain Exceptions
Validation Rules
Business Rules
```

#### Financial Logic

Implement and test:

```text
Expense Total
Expense Split
Balance Calculation
Settlement
Rounding
Currency Rules
```

#### Synchronization Domain

Implement:

```text
SyncOperation
SyncOperation Status
SyncState
Conflict
Version
Cursor
```

#### Expected Result

```text
Domain Layer
      ↓
Compiles
      ↓
Unit Tests Pass
```

No Android, HTTP, or Database dependency should be required by the core Domain logic.

---

### 2.4 Phase 2 — Local Database

#### Objective

Implement Android local persistence.

#### Tasks

Create:

```text
Room Database
Entities
DAOs
Converters
Indexes
Relations
Migrations
```

#### Tables

Initial tables should represent the required local model:

```text
users
groups
memberships
expenses
expense_splits
settlements
devices
sync_operations
sync_state
conflicts
```

The final schema must follow the Local Database Schema defined in the Technical Design.

#### Repository Integration

Implement:

```text
DAO
 ↓
Local Repository
 ↓
Application Layer
```

#### Migration

Create the initial migration strategy.

#### Tests

Test:

```text
Insert
Read
Update
Delete / Tombstone
Queries
Relations
Transactions
Migration
```

#### Expected Result

```text
Android
   ↓
Local Database
   ↓
Domain Data Persisted
```

---

### 2.5 Phase 3 — Backend Database

#### Objective

Implement persistent Backend storage.

#### Tasks

Create:

```text
Database
Tables
Entities
Repositories
Indexes
Constraints
Migrations
```

#### Core Tables

```text
users
groups
memberships
expenses
expense_splits
settlements
devices
sync_operations
conflicts
```

Additional tables may be introduced where required by:

```text
Authentication
Authorization
Synchronization
Audit
```

#### Migration Framework

Implement versioned database migrations.

#### Tests

Validate:

```text
Schema
Constraints
Indexes
Relationships
Transactions
Migration
```

#### Expected Result

```text
Backend
   ↓
Repository
   ↓
Database
```

must be operational.

---

### 2.6 Phase 4 — Application Services

#### Objective

Implement application/use-case logic between the Domain and infrastructure layers.

#### Core Use Cases

Implement:

```text
Create Group
Update Group
Add Member
Remove Member

Create Expense
Update Expense
Delete / Tombstone Expense

Create Expense Split

Create Settlement
Update Settlement

Calculate Balance
```

#### Synchronization Use Cases

Implement:

```text
Create SyncOperation
Process SyncOperation
Read SyncState
Update SyncState
Create Conflict
Resolve Conflict
```

#### Service Responsibilities

Application services should:

```text
Validate Input
Load Domain Data
Execute Domain Rules
Persist Changes
Create SyncOperation
Return Result
```

#### Transaction Handling

Critical operations should be transactional.

Example:

```text
Create Expense
      +
Create Expense Splits
      +
Create SyncOperation
```

must be persisted atomically where required.

#### Expected Result

Core business use cases can execute independently of the UI.

---

### 2.7 Phase 5 — Backend API

#### Objective

Expose the required application functionality through versioned APIs.

#### API Structure

Implement:

```text
/api/v1/...
```

#### Core API Areas

```text
Authentication
Users
Groups
Memberships
Expenses
Expense Splits
Settlements
Balances
Synchronization
Conflicts
Devices
```

#### Controller Layer

Controllers should handle:

```text
Request
 ↓
Validation
 ↓
Application Service
 ↓
Response
```

#### Error Handling

Implement consistent:

```text
Error Code
HTTP Status
Message
Request ID
```

#### API Tests

Test:

```text
Valid Requests
Invalid Requests
Authentication
Authorization
Validation
Not Found
Conflict
Server Errors
```

#### Expected Result

```text
Android / Client
       ↓
HTTP API
       ↓
Application Service
       ↓
Domain
       ↓
Repository
       ↓
Database
```

---

### 2.8 Phase 6 — Android Repository Layer

#### Objective

Connect Android application logic with:

```text
Local Database
+
Backend API
```

#### Repository Structure

Conceptually:

```text
UI
 ↓
ViewModel / Presentation
 ↓
Repository
 ├── Local Data Source
 └── Remote Data Source
```

#### Local Data Source

Responsible for:

```text
Room Queries
Local Persistence
Local Transactions
```

#### Remote Data Source

Responsible for:

```text
HTTP Requests
Serialization
Authentication Headers
API Error Mapping
```

#### Repository Strategy

The Repository determines whether data should come from:

```text
Local Database
```

or:

```text
Backend
```

according to the offline-first rules.

#### Expected Result

The Android application can access application data through a unified Repository abstraction.

---

### 2.9 Phase 7 — Offline-First Operations

#### Objective

Implement the local-first behavior of SplitSync.

#### Core Rule

User operations should not require immediate Backend availability.

```text
User Action
      ↓
Domain Validation
      ↓
Local Transaction
      ↓
Local Database
      ↓
SyncOperation
      ↓
UI Updated
```

#### Offline Expense Flow

```text
Create Expense
      ↓
Validate
      ↓
Persist Expense
      ↓
Persist Splits
      ↓
Create SyncOperation
      ↓
Commit
```

#### Offline Settlement Flow

```text
Create Settlement
      ↓
Validate
      ↓
Persist Settlement
      ↓
Create SyncOperation
      ↓
Commit
```

#### Offline Update Flow

```text
Update Entity
      ↓
Local Persistence
      ↓
Create SyncOperation
      ↓
Commit
```

#### Connectivity Handling

Implement:

```text
Online
Offline
Online → Offline
Offline → Online
```

#### Background Synchronization Trigger

When connectivity becomes available:

```text
Pending SyncOperations
      ↓
Synchronization Triggered
```

#### Expected Result

The application remains usable without Backend connectivity.

---

### 2.10 Phase 8 — Synchronization

#### Objective

Implement Backend synchronization.

#### Sync Components

```text
Sync Engine
SyncOperation Queue
Push
Pull
Cursor
Retry
Idempotency
Batching
```

#### Push Flow

```text
Local SyncOperation
      ↓
Sync Engine
      ↓
Backend Push API
      ↓
Operation Validation
      ↓
Idempotency Check
      ↓
Apply
      ↓
Result
```

#### Pull Flow

```text
Sync Cursor
      ↓
Backend Pull API
      ↓
Changes
      ↓
Local Apply
      ↓
Cursor Update
```

#### Synchronization Order

A basic synchronization cycle may follow:

```text
Start Sync
      ↓
Push Pending Operations
      ↓
Process Results
      ↓
Pull Changes
      ↓
Apply Changes
      ↓
Update Cursor
      ↓
Complete Sync
```

The exact ordering may be adjusted according to the synchronization protocol.

#### Retry

Implement:

```text
Retryable Failure
      ↓
Backoff
      ↓
Retry
```

#### Idempotency

Every SyncOperation must be safely re-processable.

#### Expected Result

```text
Android
   ↕
Backend
```

can synchronize changes reliably.

---

### 2.11 Phase 9 — Conflict Resolution

#### Objective

Implement deterministic conflict detection and resolution.

#### Conflict Detection

Use the defined:

```text
Entity Version
+
Client Base Version
+
Server Version
```

model.

#### Conflict Flow

```text
Client Operation
      ↓
Backend
      ↓
Version Check
      ↓
No Conflict
   OR
Conflict
```

#### No Conflict

```text
Operation
      ↓
Apply
      ↓
New Version
      ↓
Success
```

#### Conflict

```text
Operation
      ↓
Version Mismatch
      ↓
Conflict Created
      ↓
Resolution
```

#### Resolution

Implement the V1-defined conflict strategy.

The strategy must be:

```text
Deterministic
Repeatable
Idempotent
```

#### Tests

Test:

```text
No Conflict
Concurrent Update
Stale Update
Conflict Creation
Conflict Resolution
Repeated Resolution
```

#### Expected Result

Concurrent changes produce a predictable final state.

---

### 2.12 Phase 10 — Authentication & Authorization

#### Objective

Implement secure access to Backend resources.

#### Authentication

Implement:

```text
Login / Authentication
Token Generation
Token Validation
Token Expiration
Device Association
Device Revocation
```

according to the Security Architecture.

#### Authorization

Implement:

```text
User Authorization
Group Authorization
Resource Authorization
Role / Permission Checks
```

#### API Security

Protected endpoints must validate:

```text
Authentication
+
Authorization
```

before executing the application operation.

#### Security Tests

Test:

```text
Valid Authentication
Invalid Authentication
Expired Token
Revoked Device
Unauthorized Group Access
Unauthorized Resource Access
```

#### Expected Result

Only authorized Users and Devices can access protected resources.

---

### 2.13 Phase 11 — Android UI

#### Objective

Implement the User-facing Android application.

#### UI Areas

Initial UI should cover:

```text
Home
Groups
Group Details
Expenses
Expense Details
Create Expense
Expense Splits
Settlements
Balance
Synchronization Status
Settings
```

#### UI Architecture

Follow:

```text
UI
 ↓
ViewModel / Presentation
 ↓
Application Layer
 ↓
Repository
 ↓
Local / Remote
```

#### UI State

UI should represent:

```text
Loading
Success
Empty
Offline
Syncing
Sync Failed
Conflict
Error
```

#### Offline UI

The User should be able to identify that the application is:

```text
Offline
```

without losing access to locally available data.

#### Sync UI

Where appropriate, display:

```text
Syncing
Synced
Pending
Failed
Conflict
```

#### Expected Result

Core User flows are available through Android UI.

---

### 2.14 Phase 12 — Peer-to-Peer Synchronization

#### Objective

Implement local peer-to-peer synchronization.

#### Components

Implement:

```text
Peer Discovery
Peer Connection
Peer Authentication
Protocol Negotiation
Session
Message Transport
Sync Operation Transfer
Integrity Verification
```

#### P2P Flow

```text
Device A
      ↓
Discovery
      ↓
Device B
      ↓
Environment Check
      ↓
Authentication
      ↓
Protocol Negotiation
      ↓
Session
      ↓
Sync Operations
      ↓
Apply
      ↓
Session Complete
```

#### P2P Safety

Verify:

```text
Environment
Device Identity
Protocol Version
Authentication
Message Integrity
```

#### P2P Idempotency

P2P-delivered operations must use the same logical idempotency rules as Backend-delivered operations.

#### P2P Tests

Test:

```text
Discovery
Connection
Authentication
Version Mismatch
Environment Mismatch
Message Integrity
Duplicate Operation
Disconnect
Reconnect
Synchronization
```

#### Expected Result

Supported devices can exchange synchronization operations directly.

---

### 2.15 Phase 13 — Integration & End-to-End Testing

#### Objective

Validate the complete system.

#### Integration Areas

Test:

```text
Android
+
Local Database
+
Backend API
+
Backend Database
```

#### Synchronization Testing

Test:

```text
Offline
      ↓
Local Operation
      ↓
SyncOperation
      ↓
Backend
      ↓
Another Device
```

#### P2P Testing

Test:

```text
Device A
      ↓
Offline Operation
      ↓
P2P
      ↓
Device B
      ↓
Local Apply
```

#### Conflict Testing

Test:

```text
Device A
      ↓
Update

Device B
      ↓
Update

      ↓

Synchronization
      ↓
Conflict
      ↓
Resolution
```

#### Recovery Testing

Test:

```text
Application Crash
Backend Failure
Network Failure
P2P Disconnect
Database Failure
```

#### Expected Result

All critical end-to-end scenarios pass.

---

### 2.16 Phase 14 — Deployment

#### Objective

Prepare SplitSync V1 for controlled deployment.

#### Backend Deployment

Implement:

```text
Build Artifact
Container / JAR
Environment Configuration
Database Migration
Health Checks
Deployment
Monitoring
```

#### Android Deployment

Implement:

```text
Release Build
Signing
Versioning
Distribution
Rollout
Crash Monitoring
```

#### Environment Deployment

Validate:

```text
Development
Test
Staging
Production
```

#### Production Validation

Before release:

```text
Tests Passed
Migration Validated
Configuration Validated
Security Validated
Staging Passed
Rollback Plan Ready
```

#### Expected Result

SplitSync V1 can be deployed safely to Production.

---

### 2.17 Phase Dependency Graph

The implementation dependencies can be represented as:

```text
Project Foundation
        ↓
Domain Model
        ↓
   ┌────┴────┐
   ↓         ↓
Local DB   Backend DB
   ↓         ↓
   └────┬────┘
        ↓
Application Services
        ↓
Backend API
        ↓
Android Repository
        ↓
Offline-First Operations
        ↓
Synchronization
        ↓
Conflict Resolution
        ↓
Authentication / Authorization
        ↓
Android UI
        ↓
P2P Synchronization
        ↓
Integration Testing
        ↓
Deployment
```

Authentication and Authorization may be introduced earlier during API development and must be in place before exposing protected functionality.

### 2.18 Parallel Implementation

Some work can proceed in parallel after the Domain contract is stable.

```text
                Domain Model
                     │
          ┌──────────┴──────────┐
          ↓                     ↓
    Android Local DB       Backend DB
          │                     │
          ↓                     ↓
    Android Repository    Backend Services
          │                     │
          └──────────┬──────────┘
                     ↓
                Synchronization
```

### 2.19 Recommended Parallel Work

The following streams may be developed in parallel:

```text
Stream A
Android Local Persistence

Stream B
Backend Persistence

Stream C
Domain Unit Tests

Stream D
API Contract

Stream E
Authentication / Authorization
```

They must converge through stable interfaces.

### 2.20 Interface-First Principle

When parallel implementation is required, define contracts before implementation.

Examples:

```text
Repository Interface
API Contract
SyncOperation Contract
P2P Message Contract
Authentication Contract
```

### 2.21 Phase Completion Criteria

A phase is complete when:

```text
Implementation Complete
+
Required Tests Passing
+
Integration Boundary Verified
+
Documentation Updated
```

### 2.22 Phase Failure Rule

If a phase introduces an unresolved critical defect:

```text
Current Phase
      ↓
Fix
      ↓
Retest
      ↓
Continue
```

The next dependent phase should not proceed with known critical defects.

### 2.23 Milestone M1 — Project Builds

M1 is complete when:

```text
Android Builds
Backend Builds
CI Builds
Basic Tests Execute
```

### 2.24 Milestone M2 — Domain Complete

M2 is complete when:

```text
Core Entities Implemented
Business Rules Implemented
Financial Rules Tested
Domain Tests Pass
```

### 2.25 Milestone M3 — Local Persistence Complete

M3 is complete when:

```text
Room Database Works
Repositories Work
Transactions Work
Migrations Work
Database Tests Pass
```

### 2.26 Milestone M4 — Backend Persistence Complete

M4 is complete when:

```text
Backend Database Works
Repositories Work
Migrations Work
Constraints Work
Integration Tests Pass
```

### 2.27 Milestone M5 — Core Expense Flow Complete

M5 is complete when:

```text
Create Group
      ↓
Add Members
      ↓
Create Expense
      ↓
Create Splits
      ↓
Calculate Balance
```

works locally.

### 2.28 Milestone M6 — Backend API Complete

M6 is complete when:

```text
Core API Endpoints
Authentication
Authorization
Validation
Error Handling
```

are operational and tested.

### 2.29 Milestone M7 — Offline-First Complete

M7 is complete when:

```text
Offline Group Operation
Offline Expense Operation
Offline Settlement Operation
Offline Update
Pending SyncOperation
```

work correctly.

### 2.30 Milestone M8 — Synchronization Complete

M8 is complete when:

```text
Push
Pull
Retry
Idempotency
Cursor
Batching
```

work correctly.

### 2.31 Milestone M9 — Conflict Resolution Complete

M9 is complete when:

```text
Conflict Detection
Conflict Storage
Conflict Resolution
Resolution Synchronization
```

work correctly.

### 2.32 Milestone M10 — Authentication Complete

M10 is complete when:

```text
Authentication
Token Validation
Device Association
Authorization
Revocation
```

work correctly.

### 2.33 Milestone M11 — Android UI Complete

M11 is complete when:

```text
Core Screens
Core User Flows
Offline State
Sync State
Error State
```

are functional.

### 2.34 Milestone M12 — P2P Complete

M12 is complete when:

```text
Discovery
Authentication
Protocol Negotiation
Transfer
Integrity
Idempotency
Recovery
```

work correctly.

### 2.35 Milestone M13 — Integration Complete

M13 is complete when:

```text
Android
+
Backend
+
Database
+
Synchronization
+
P2P
```

pass the required end-to-end scenarios.

### 2.36 Milestone M14 — Production Ready

M14 is complete when:

```text
Production Build
+
Security Validation
+
Migration Validation
+
Staging Validation
+
Deployment Validation
+
Monitoring
+
Rollback Plan
```

are ready.

### 2.37 Implementation Priority

Priority should be:

```text
P0 - Mandatory
P1 - Required
P2 - Important
P3 - Future
```

### 2.38 P0 Components

P0 components include:

```text
Domain Model
Local Database
Backend Database
Expense
Expense Split
Settlement
Offline Persistence
SyncOperation
Synchronization
Authentication
Authorization
Core API
Core Android UI
```

### 2.39 P1 Components

P1 components include:

```text
Conflict Resolution
P2P Synchronization
Advanced Error Handling
Advanced Monitoring
Performance Optimization
```

### 2.40 P2 Components

P2 components may include:

```text
Advanced Analytics
Advanced Reporting
Advanced Sync Optimization
Additional UI Improvements
```

### 2.41 P3 Components

P3 components are future extensions outside the V1 critical path.

Examples:

```text
Advanced Integrations
Additional Platforms
Advanced Automation
```

### 2.42 Implementation Risk Areas

The highest-risk implementation areas are:

```text
Financial Calculations
Local Transactions
Synchronization
Conflict Resolution
P2P Communication
Database Migration
Offline Recovery
Concurrency
Authentication
Authorization
```

These areas require additional testing and review.

### 2.43 Risk Mitigation

For high-risk components:

```text
Design
 ↓
Prototype
 ↓
Unit Tests
 ↓
Integration Tests
 ↓
Failure Tests
 ↓
End-to-End Tests
```

### 2.44 Prototype Before Full Implementation

Where technical uncertainty exists, implement a small prototype first.

Potential prototype areas:

```text
P2P Transport
Synchronization Protocol
Database Migration
Background Synchronization
```

### 2.45 Implementation Review Points

Formal review should occur after:

```text
Domain Model
Database Schema
API Contract
Synchronization
Conflict Resolution
P2P Protocol
Security
```

### 2.46 Code Review

Every major feature should undergo code review before being merged into the main development branch.

### 2.47 Implementation Documentation

Implementation decisions that differ from the Technical Design must be documented.

Example:

```text
Technical Design
      ↓
Implementation Constraint
      ↓
Decision
      ↓
Documentation Update
```

### 2.48 Technical Design Changes

If implementation reveals that the Technical Design is incorrect:

```text
Identify Issue
      ↓
Update Technical Design
      ↓
Update Implementation Plan
      ↓
Implement
```

The implementation should not silently diverge from the architecture.

### 2.49 Database Change Process

Any Database schema change should follow:

```text
Design
      ↓
Migration
      ↓
Test
      ↓
Staging
      ↓
Production
```

### 2.50 API Change Process

Any API contract change should follow:

```text
Design
      ↓
Compatibility Review
      ↓
Implementation
      ↓
Tests
      ↓
Documentation
```

### 2.51 Synchronization Change Process

Any Sync protocol change should follow:

```text
Protocol Design
      ↓
Compatibility Review
      ↓
Implementation
      ↓
Idempotency Tests
      ↓
Conflict Tests
      ↓
Multi-Device Tests
```

### 2.52 P2P Change Process

Any P2P protocol change should follow:

```text
Protocol Change
      ↓
Compatibility Review
      ↓
Security Review
      ↓
Implementation
      ↓
Multi-Device Testing
```

### 2.53 Implementation Tracking

Progress should be tracked at:

```text
Phase
Milestone
Feature
Task
Test
```

### 2.54 Feature Tracking Example

```text
Feature: Expense Creation

Domain
[Complete]

Local Database
[Complete]

Backend Database
[Complete]

Backend API
[Complete]

Android Repository
[Complete]

Offline Support
[Complete]

Synchronization
[Complete]

UI
[In Progress]

E2E Test
[Pending]
```

### 2.55 Implementation Completion

The implementation plan is complete only when every required V1 component has:

```text
Implementation
+
Tests
+
Integration
+
Documentation
```

### 2.56 Implementation Plan Summary

```text
Foundation
    ↓
Domain
    ↓
Persistence
    ↓
Application Services
    ↓
API
    ↓
Android Repository
    ↓
Offline-First
    ↓
Synchronization
    ↓
Conflict Resolution
    ↓
Security
    ↓
UI
    ↓
P2P
    ↓
Integration
    ↓
Deployment
```

### 2.57 V1 Implementation Goal

The final V1 implementation must provide:

```text
Reliable Local-First Expense Management
+
Offline Operation
+
Backend Synchronization
+
Conflict Resolution
+
Peer-to-Peer Synchronization
+
Secure Authentication
+
Authorization
+
Data Integrity
+
Production Deployment
```

while maintaining the architectural boundaries defined in:

```text
technical-design-v1.md
```

## 3. Project Setup and Initial Structure

### 3.1 Objective

This section defines the initial implementation setup for SplitSync V1.

The goal is to establish a clean and stable project foundation before implementing business functionality.

The initial structure must support:

```text
Android Application
+
Backend Application
+
Shared Domain Concepts
+
Local Database
+
Backend Database
+
Synchronization
+
Testing
+
CI/CD
```

### 3.2 Repository Structure

The repository should be organized into clearly separated application areas.

Recommended high-level structure:

```text
splitsync/
│
├── android/
│
├── backend/
│
├── docs/
│
├── scripts/
│
└── README.md
```

The exact repository structure may be adjusted during project initialization, but Android and Backend implementation must remain clearly separated.

### 3.3 Android Project

The Android application should be created as a standard Gradle-based Java Android project.

Initial structure:

```text
android/
├── app/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── gradle/
```

Additional modules should only be introduced when there is a clear architectural requirement.

### 3.4 Backend Project

The Backend should be created as a Java Spring Boot application.

Initial structure:

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│       └── java/
│
├── build.gradle
└── settings.gradle
```

The exact build configuration may use Maven instead if selected during project initialization.

### 3.5 Java Version

A single supported Java version should be selected for the V1 implementation.

The selected version must be used consistently across:

```text
Android
Backend
CI
Local Development
Build Environment
```

### 3.6 Build System

The build system must provide:

```text
Dependency Management
Compilation
Testing
Packaging
Build Variants
Static Analysis
```

### 3.7 Dependency Management

Dependencies must be explicitly declared.

Avoid unnecessary dependencies.

Every dependency should have a clear purpose.

### 3.8 Dependency Categories

Dependencies should generally fall into:

```text
Core Framework
Persistence
Networking
Serialization
Security
Testing
Build Tools
```

### 3.9 Dependency Version Management

Dependency versions should be centrally managed where practical.

This reduces version drift between modules.

### 3.10 Android Application ID

The Production Android application should use the final agreed application ID.

Development and staging variants may use separate IDs when multiple environments need to coexist on the same device.

### 3.11 Backend Application Name

The Backend application should have a stable application identifier.

Example:

```text
splitsync-backend
```

The exact naming may be finalized during project initialization.

### 3.12 Base Package

Android and Backend packages should follow the Package Naming rules defined in the Technical Design.

Example:

```text
com.splitsync
```

The actual organization should remain consistent across the project.

### 3.13 Android Base Package

A conceptual Android package structure is:

```text
com.splitsync.android
```

with feature/layer packages below it.

### 3.14 Backend Base Package

A conceptual Backend package structure is:

```text
com.splitsync.backend
```

with application, domain, infrastructure, API, and configuration packages below it.

### 3.15 Initial Android Package Structure

The initial structure should prepare for:

```text
com.splitsync.android
│
├── domain
├── application
├── data
├── sync
├── p2p
├── security
├── presentation
└── configuration
```

The final package organization must follow the detailed Android Package Structure from the Technical Design.

### 3.16 Initial Backend Package Structure

The Backend should prepare for:

```text
com.splitsync.backend
│
├── domain
├── application
├── infrastructure
├── api
├── security
├── synchronization
└── configuration
```

### 3.17 Domain Package

The Domain package should contain only business concepts.

Examples:

```text
User
Group
Membership
Expense
ExpenseSplit
Settlement
Balance
SyncOperation
SyncState
Conflict
```

### 3.18 Domain Independence

The Domain package must not directly depend on:

```text
Android Framework
Spring MVC
HTTP
Room
JPA
Database Driver
P2P Transport
```

### 3.19 Application Package

The Application package contains use-case orchestration.

Examples:

```text
CreateGroupService
CreateExpenseService
CreateSettlementService
SynchronizationService
ConflictResolutionService
```

Names may differ based on the final implementation style.

### 3.20 Infrastructure Package

Infrastructure contains implementation details such as:

```text
Database
Network
External Services
Persistence
P2P Transport
```

### 3.21 API Package

The Backend API package contains:

```text
Controllers
Request DTOs
Response DTOs
API Exception Handling
API Mappers
```

### 3.22 Security Package

Security-related implementation belongs in the appropriate security package.

Examples:

```text
Authentication
Token Handling
Authorization
Device Security
```

### 3.23 Synchronization Package

Synchronization-specific implementation should be isolated from general API logic.

Examples:

```text
Sync Engine
Sync Processor
Sync Queue
Cursor Handling
Conflict Detection
```

### 3.24 P2P Package

P2P-specific implementation should be isolated from the core Domain.

Examples:

```text
Peer Discovery
Peer Session
Handshake
Protocol
Transport
Message Serialization
```

### 3.25 Configuration Package

Configuration-related code should remain separate from business logic.

Examples:

```text
Database Configuration
Security Configuration
Network Configuration
Application Configuration
```

### 3.26 Android Build Variants

The initial Android project should support environment-specific configuration.

Recommended conceptual variants:

```text
dev
test
staging
prod
```

These may be combined with:

```text
debug
release
```

as appropriate.

### 3.27 Development Variant

Development should use:

```text
Development API
Development Database
Debug Logging
Development Configuration
```

### 3.28 Test Variant

Test should use:

```text
Test API
Test Database
Test Configuration
Controlled Test Data
```

### 3.29 Staging Variant

Staging should use:

```text
Staging API
Staging Database
Release-like Configuration
```

### 3.30 Production Variant

Production should use:

```text
Production API
Production Configuration
Production Signing
Production Security Settings
```

### 3.31 Backend Profiles

The Backend should support environment-specific configuration.

Conceptually:

```text
dev
test
staging
prod
```

### 3.32 Configuration Separation

Environment-specific values must not be embedded directly in business code.

Examples:

```text
API URLs
Database URLs
Credentials
Logging Levels
Feature Flags
```

must be externally configurable.

### 3.33 Local Development Configuration

Local development should provide an easy way to run:

```text
Android
+
Backend
+
Database
```

without requiring Production infrastructure.

### 3.34 Local Backend

Developers should be able to start the Backend locally.

Conceptually:

```text
Developer Machine
      ↓
Backend
      ↓
Local Database
```

### 3.35 Local Database

A dedicated development database should be used.

Production databases must never be used for normal local development.

### 3.36 Local Android Configuration

Android development builds should point to the configured development Backend.

The API endpoint must be configurable.

### 3.37 Initial Health Check

The Backend should provide a simple health endpoint during Phase 0.

Example:

```text
GET /health
```

The exact endpoint may be finalized with the API design.

### 3.38 Build Verification

After project initialization:

```text
Android Build
      ↓
Success

Backend Build
      ↓
Success
```

must be verified before proceeding.

### 3.39 Initial Unit Test

Each application should contain at least one basic test to verify that the test infrastructure is operational.

### 3.40 Android Test Verification

Verify:

```text
Unit Test Execution
Instrumentation Test Configuration
```

where applicable.

### 3.41 Backend Test Verification

Verify:

```text
Unit Test Execution
Spring Context Test Configuration
Database Test Configuration
```

where applicable.

### 3.42 Static Analysis

Initial project setup should configure appropriate static checks.

Examples:

```text
Compilation Warnings
Code Style
Unused Code
Potential Errors
```

### 3.43 Formatting

A consistent code formatting strategy should be selected.

Formatting should be applied consistently across the project.

### 3.44 Naming

Implementation naming must follow:

```text
Class Naming
Method Naming
Variable Naming
Package Naming
Database Naming
API Naming
```

defined by the project conventions.

### 3.45 Java Code Style

Java code should follow standard Java naming conventions.

Examples:

```text
ClassName
methodName
variableName
CONSTANT_NAME
```

### 3.46 Repository Naming

Repository implementations should have consistent naming.

Example:

```text
ExpenseRepository
ExpenseRepositoryImpl
```

where an interface/implementation split is required.

### 3.47 Service Naming

Application services should clearly represent the operation or use case.

Example:

```text
CreateExpenseService
CreateSettlementService
```

### 3.48 Controller Naming

Backend controllers should clearly represent their resource or API boundary.

Example:

```text
ExpenseController
GroupController
SyncController
```

### 3.49 DTO Naming

API models should be explicitly separated from Domain models.

Example:

```text
CreateExpenseRequest
ExpenseResponse
SyncRequest
SyncResponse
```

### 3.50 Entity Naming

Persistence entities should be clearly distinguished from Domain models when separate representations are required.

Example:

```text
ExpenseEntity
Expense
```

### 3.51 Mapper Strategy

Where different representations exist:

```text
API DTO
   ↓
Domain Model
   ↓
Persistence Entity
```

mapping should be explicit.

### 3.52 No Direct Domain Exposure

The Backend API should not expose internal persistence entities directly.

### 3.53 Android Data Mapping

Android should similarly avoid unnecessarily coupling UI models directly to database entities.

Conceptually:

```text
Database Entity
      ↓
Domain / Application Model
      ↓
UI Model
```

where separation is required.

### 3.54 Initial Logging

Initial logging should be configured before implementing complex features.

Development logs may be more verbose.

Production logs must remain controlled.

### 3.55 Logging Categories

Logging should distinguish:

```text
Application
Database
Network
Synchronization
Security
P2P
```

### 3.56 Sensitive Logging

The following must not be logged:

```text
Passwords
Authentication Secrets
Private Keys
Tokens
Sensitive Personal Data
```

### 3.57 Request Correlation

Backend requests should support correlation identifiers where appropriate.

Example:

```text
Request
  ↓
requestId
  ↓
Application Logs
```

### 3.58 Error Handling Foundation

A common error-handling mechanism should be established early.

Backend errors should follow a consistent response structure.

### 3.59 Exception Categories

At minimum, distinguish:

```text
Validation Error
Authentication Error
Authorization Error
Not Found
Conflict
Transient Failure
Internal Error
```

### 3.60 Domain Exception Foundation

Domain-specific exceptions should be defined independently of HTTP.

Example:

```text
InvalidExpenseException
InvalidSettlementException
InvalidMembershipException
```

The exact names are implementation-specific.

### 3.61 API Error Mapping

Domain/Application exceptions should be mapped to API responses at the API boundary.

```text
Domain Exception
      ↓
Application Boundary
      ↓
API Error
```

### 3.62 Database Error Mapping

Database-specific exceptions must not leak directly to API consumers.

### 3.63 Build Profiles

Build configuration should distinguish:

```text
Development
Testing
Staging
Production
```

without duplicating business logic.

### 3.64 Feature Flags

Feature flags may be introduced for functionality that needs controlled activation.

Feature flags must not replace proper architectural boundaries.

### 3.65 Initial CI Pipeline

The initial CI pipeline should execute:

```text
Checkout
   ↓
Build
   ↓
Unit Tests
   ↓
Static Checks
   ↓
Artifact
```

### 3.66 Android CI

Android CI should verify:

```text
Gradle Build
Unit Tests
Lint / Static Checks
```

### 3.67 Backend CI

Backend CI should verify:

```text
Build
Unit Tests
Integration Tests
Static Checks
```

### 3.68 Branch Strategy

The repository should use a controlled branch strategy.

The exact branching model may be selected according to team size and development workflow.

### 3.69 Pull Request Validation

Pull Requests should automatically run the required CI checks.

### 3.70 Main Branch Protection

The main development/release branch should require passing mandatory checks before merge.

### 3.71 Initial README

The repository should contain a README covering:

```text
Project Purpose
Repository Structure
Prerequisites
Android Setup
Backend Setup
Database Setup
Build Instructions
Test Instructions
```

### 3.72 Developer Setup Documentation

A developer should be able to follow the documentation to reach:

```text
Clone
 ↓
Configure
 ↓
Build
 ↓
Run Backend
 ↓
Run Android
 ↓
Execute Tests
```

### 3.73 Local Development Workflow

The expected local workflow is:

```text
Start Database
      ↓
Start Backend
      ↓
Start Android
      ↓
Run Application
      ↓
Execute Tests
```

### 3.74 Initial Database Initialization

The Backend development environment should initialize the database through the configured migration mechanism rather than manually modifying schema objects.

### 3.75 Seed Data

Development/test environments may use controlled seed data.

Production must not automatically receive development seed data.

### 3.76 Test Data

Test data should be deterministic and isolated.

### 3.77 Time Configuration

The application should provide a controlled mechanism for testing time-dependent behavior where necessary.

### 3.78 ID Generation

ID generation should be centralized according to the Domain and persistence strategy.

### 3.79 UUID / Identifier Strategy

The final identifier strategy should be selected before implementing synchronization.

Identifiers must support:

```text
Offline Creation
Uniqueness
Synchronization
Idempotency
```

### 3.80 Synchronization Foundation Requirement

Before implementing synchronization, the following must already be stable:

```text
Entity IDs
Entity Versions
Timestamps
SyncOperation IDs
Device IDs
```

### 3.81 Device ID Foundation

The Android application must establish the Device ID mechanism before implementing synchronization.

Conceptually:

```text
Application Install
      ↓
Device ID
      ↓
Local Persistence
      ↓
Sync Identity
```

### 3.82 Device Identity Persistence

The Device ID must remain stable according to the lifecycle rules defined in the Technical Design.

### 3.83 Local User Foundation

The local User representation should be established before implementing:

```text
Group Membership
Expense Ownership
Authentication Association
```

### 3.84 Initial Security Foundation

Before protected APIs are implemented:

```text
Authentication Model
Device Identity
Token Model
Authorization Model
```

must be defined.

### 3.85 Initial API Contract

Before implementing the full API, define:

```text
Endpoint
HTTP Method
Request
Response
Error
Authentication
Authorization
```

for each endpoint.

### 3.86 API Contract Stability

Once implementation begins against an API contract, changes should be reviewed for backward compatibility.

### 3.87 Initial Synchronization Contract

Before implementing Sync Engine code, define:

```text
SyncOperation
Operation ID
Entity ID
Entity Type
Operation Type
Version
Timestamp
Device ID
Payload
```

according to the synchronization model.

### 3.88 Initial P2P Contract

Before implementing P2P transport, define:

```text
Peer Identity
Protocol Version
Handshake
Message Type
Message ID
Integrity
Session
```

### 3.89 Project Setup Completion Criteria

Project Setup is complete when:

```text
Repository Created
Android Project Builds
Backend Project Builds
Java Version Configured
Build System Configured
Package Structure Created
Environment Configuration Created
Local Backend Runs
Local Database Runs
Android Connects to Development Backend
Basic Tests Pass
CI Pipeline Passes
README Created
```

### 3.90 Phase 0 Exit Criteria

Phase 0 can be considered complete when:

```text
Developer
    ↓
Clones Repository
    ↓
Follows Setup Documentation
    ↓
Builds Android
    ↓
Builds Backend
    ↓
Starts Backend
    ↓
Starts Android
    ↓
Runs Basic Test
```

successfully.

### 3.91 Initial Project Structure Summary

```text
splitsync/
│
├── android/
│   ├── app/
│   ├── gradle/
│   └── settings.gradle
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   └── build.gradle
│
├── docs/
│   ├── technical-design-v1.md
│   └── implementation-plan-v1.md
│
├── scripts/
│
└── README.md
```

### 3.92 Phase 0 Dependency Flow

```text
Repository
      ↓
Build Configuration
      ↓
Android + Backend
      ↓
Environment Configuration
      ↓
Local Database
      ↓
Basic Tests
      ↓
CI
```

### 3.93 Phase 0 Deliverables

The following artifacts should exist after Phase 0:

```text
Android Project
Backend Project
Build Configuration
Environment Configuration
Local Development Database Configuration
Basic Test Suite
CI Configuration
README
Initial Documentation
```

### 3.94 Phase 0 Final State

At the end of Phase 0:

```text
No Business Feature Required Yet
```

but the project must provide a reliable foundation for implementing:

```text
Domain
Persistence
Application Services
API
Synchronization
UI
P2P
```

### 3.95 Next Phase

After Phase 0 is complete, implementation proceeds to:

```text
Phase 1
Domain Model
```

The Domain Model becomes the first major implementation layer because all subsequent components depend on its entities, rules, identifiers, versions, and invariants.

## 4. Development Environment Setup

### 4.1 Purpose

This section defines the development environment required to build, run, test, and debug SplitSync V1.

### 4.2 Development Environment Components

The development environment consists of:

```text
Operating System
        ↓
Java Development Kit
        ↓
Android Development Tools
        ↓
Backend Development Tools
        ↓
Build Tools
        ↓
Local Database
        ↓
Version Control
        ↓
Testing Tools
```

### 4.3 Required Development Tools

The development environment should provide:

```text
Git
JDK
Android Studio
Android SDK
Gradle
Backend Build Tool
Database
Database Client
API Testing Tool
```

### 4.4 Java Development Kit

A single supported JDK version must be selected for V1.

The same supported Java version should be used consistently across:

```text
Local Development
Android Build
Backend Build
CI/CD
```

### 4.5 Android Studio

Android development should use Android Studio as the primary IDE.

It should provide:

```text
Android SDK Management
Gradle Integration
Java Development
Android Emulator
Debugging
Testing
Logcat
```

### 4.6 Android SDK

The required Android SDK components must be defined centrally.

The project should specify:

```text
compileSdk
minSdk
targetSdk
Build Tools
```

The exact versions are implementation configuration and should be locked during project setup.

### 4.7 Android Emulator

Development should support Android Emulator configurations for:

```text
Supported Android Version
Network Testing
Offline Testing
Background Processing
P2P Testing
```

### 4.8 Physical Android Device

At least one physical Android device should be available for testing:

```text
Network Connectivity
Background Execution
Battery Behavior
P2P Communication
Real Device Storage
```

### 4.9 Backend Development Environment

The Backend development environment should support:

```text
Java
Spring Boot
Gradle / Maven
Local Database
HTTP Server
Debugging
Testing
```

### 4.10 Local Backend Execution

Developers must be able to start the Backend locally.

Conceptually:

```text
Developer Machine
      ↓
Spring Boot Backend
      ↓
Local Database
```

### 4.11 Local Database

Development should use a dedicated local database.

The database must not be:

```text
Production Database
```

### 4.12 Database Management

Developers should have access to an appropriate database client for:

```text
Schema Inspection
Query Testing
Migration Verification
Debugging
```

### 4.13 API Development Tools

API development should support testing:

```text
GET
POST
PUT / PATCH
DELETE
```

and synchronization-specific endpoints.

### 4.14 API Testing

Developers should be able to test:

```text
Authentication
Authorization
Groups
Expenses
Settlements
Synchronization
Conflicts
```

without depending on the Android application.

### 4.15 Version Control

Git must be used for source control.

The repository should contain:

```text
Android Source
Backend Source
Tests
Build Configuration
Documentation
CI/CD Configuration
```

### 4.16 Repository Initialization

The repository should be initialized before feature implementation.

Initial flow:

```text
Create Repository
      ↓
Initial Project Structure
      ↓
Initial Commit
      ↓
CI Configuration
```

### 4.17 Git Ignore Configuration

The repository must exclude:

```text
Build Artifacts
IDE Metadata
Local Secrets
Generated Files
Local Database Files
Temporary Files
```

### 4.18 Local Secrets

Local development secrets must not be committed to Git.

Examples:

```text
Database Password
API Secret
Private Key
Authentication Secret
```

### 4.19 Environment Variables

Environment-specific configuration may use:

```text
Environment Variables
Configuration Files
Secret Management
```

according to the environment.

### 4.20 Development Environment Configuration

Development configuration should contain:

```text
Backend URL
Database URL
Database Credentials
Logging Configuration
Feature Flags
```

where applicable.

### 4.21 Android Development Configuration

Android development builds should point to the configured Development Backend.

The endpoint must be configurable rather than hard-coded throughout the codebase.

### 4.22 Backend Development Configuration

The Backend should provide a Development profile containing:

```text
Development Database
Development Logging
Development Security Configuration
Development External Services
```

### 4.23 Test Environment

The Test environment must remain separate from Development where practical.

Conceptually:

```text
Development
      ≠
Test
```

### 4.24 Staging Environment

Staging should closely resemble Production.

It should use:

```text
Staging Backend
Staging Database
Staging Configuration
```

### 4.25 Production Environment

Production configuration must remain completely isolated.

Production should use:

```text
Production Backend
Production Database
Production Secrets
Production TLS
Production Monitoring
```

### 4.26 Local Network Configuration

The development environment must support:

```text
Internet Available
Internet Unavailable
Local Network Available
Local Network Unavailable
```

for testing Offline-First and P2P behavior.

### 4.27 Offline Testing

Developers must be able to simulate:

```text
Online
Offline
Online → Offline
Offline → Online
```

### 4.28 Network Failure Testing

The development environment should support testing:

```text
Timeout
Connection Failure
Server Unavailable
Slow Network
Intermittent Network
```

### 4.29 P2P Development Environment

P2P development requires at least two supported endpoints when implementing actual device-to-device communication.

Conceptually:

```text
Android Device A
       ↕
Android Device B
```

### 4.30 P2P Development Isolation

Development P2P devices must not accidentally communicate with Production P2P environments.

### 4.31 Device Identity Testing

Development must support testing:

```text
New Device
Existing Device
Revoked Device
Unknown Device
```

### 4.32 Database Migration Development

Developers must be able to:

```text
Create Schema
Apply Migration
Verify Migration
Rollback / Recover
```

according to the selected migration strategy.

### 4.33 Android Database Migration

Android developers must be able to test:

```text
Old Local Database
      ↓
Application Upgrade
      ↓
Migration
      ↓
New Local Database
```

### 4.34 Backend Database Migration

Backend developers must be able to test:

```text
Schema V1
      ↓
Migration
      ↓
Schema V2
```

and subsequent supported versions.

### 4.35 Test Data

Development and Test environments may use controlled test data.

Examples:

```text
Test User
Test Group
Test Expense
Test Settlement
Test Conflict
```

### 4.36 Seed Data

Seed data must be clearly separated from Production data.

Production must not receive Development/Test seed data automatically.

### 4.37 Deterministic Test Data

Automated tests should use deterministic data where practical.

Avoid unnecessary dependence on:

```text
Random Values
Current Time
External Services
External Network
```

### 4.38 Time Control

Time-dependent Domain behavior should support controlled time during tests where required.

Examples:

```text
Created At
Updated At
Version Timestamp
Sync Timestamp
```

### 4.39 Identifier Generation

The development environment must use the same identifier strategy intended for Production unless a specific test requires otherwise.

### 4.40 Local Development Ports

Development services should use clearly defined ports.

Example:

```text
Android
    ↓
Development Backend
    ↓
Database
```

The exact port numbers should be maintained in configuration rather than scattered through source code.

### 4.41 Backend Debugging

The Backend should support local debugging through the selected IDE.

Developers should be able to inspect:

```text
Request
Application Service
Domain Logic
Repository
Database
```

### 4.42 Android Debugging

Android development should support:

```text
Breakpoints
Logcat
Network Inspection
Database Inspection
Background Worker Debugging
```

### 4.43 Database Debugging

Database debugging should support inspection of:

```text
Groups
Members
Expenses
Expense Splits
Settlements
SyncOperations
SyncState
Conflicts
```

### 4.44 Synchronization Debugging

Development tooling should make it possible to inspect:

```text
SyncOperation ID
Entity ID
Operation Type
Status
Retry Count
Error
Cursor
Conflict
```

### 4.45 Logging Levels

Development should support configurable logging levels:

```text
ERROR
WARN
INFO
DEBUG
```

Production should use an appropriately restricted level.

### 4.46 Sensitive Data in Development Logs

Even Development logs must avoid unnecessary exposure of:

```text
Passwords
Tokens
Private Keys
Authentication Secrets
```

### 4.47 Request Identification

Backend development logs should support request identification where practical.

Example:

```text
Request ID
User ID
Device ID
```

Sensitive identifiers should be handled according to the Security and Logging strategy.

### 4.48 Application Startup

The local development startup sequence should be:

```text
Database
      ↓
Backend
      ↓
Android
```

### 4.49 Backend Startup Validation

At startup, the Backend should validate:

```text
Configuration
Database Connectivity
Required Dependencies
```

### 4.50 Android Startup Validation

Android should safely initialize:

```text
Local Database
Device ID
Local Configuration
Authentication State
Synchronization State
```

### 4.51 Development Health Check

The Backend health endpoint should be used to verify local availability.

Conceptually:

```text
GET /health
      ↓
Backend Available
```

### 4.52 Development Reset

Development environments should support resetting local state when required.

Example:

```text
Reset Database
Reset Test Data
Reset Local Application Data
```

Production data must never be affected by a Development reset operation.

### 4.53 Android Application Reset

Developers may clear application data to simulate:

```text
Fresh Installation
New Device
New Local User
```

### 4.54 Offline Queue Reset

Development tools may provide controlled reset of:

```text
Pending SyncOperations
SyncState
Conflicts
```

for testing.

### 4.55 Development Documentation

The repository must document:

```text
Prerequisites
Installation
Configuration
Database Setup
Backend Startup
Android Startup
Testing
Troubleshooting
```

### 4.56 Developer Onboarding

A new developer should be able to reach:

```text
Clone
 ↓
Install Dependencies
 ↓
Configure Environment
 ↓
Start Database
 ↓
Start Backend
 ↓
Run Android
 ↓
Execute Tests
```

using documented instructions.

### 4.57 Environment Troubleshooting

Documentation should cover common failures such as:

```text
Java Version Mismatch
Android SDK Missing
Gradle Failure
Database Unavailable
Backend Port Conflict
Android Cannot Reach Backend
Migration Failure
```

### 4.58 CI Environment

CI must provide a reproducible environment for:

```text
Android Build
Backend Build
Unit Tests
Integration Tests
Static Analysis
```

### 4.59 CI Java Version

CI must use the same supported Java version defined for the project.

### 4.60 CI Dependency Resolution

CI should resolve dependencies in a reproducible manner.

### 4.61 CI Database

Integration tests requiring a database should use an isolated Test database.

### 4.62 CI Environment Isolation

CI must not use:

```text
Production Database
Production Credentials
Production Secrets
```

### 4.63 Local vs CI Differences

Differences between Local and CI environments should be minimized.

Where differences are unavoidable, they must be documented.

### 4.64 Development Environment Security

Development environments must follow the Security Architecture as far as practical.

Especially:

```text
No Production Secrets
No Production Database
No Unprotected Sensitive Data
```

### 4.65 Development Environment Completion Criteria

The Development Environment Setup is complete when:

```text
JDK Configured
Android Studio Configured
Android SDK Configured
Backend Tooling Configured
Database Available
Git Configured
Android Builds
Backend Builds
Backend Runs
Android Runs
Tests Execute
Offline Testing Possible
P2P Testing Environment Available
```

---

## 5. Build Configuration

### 5.1 Purpose

This section defines the build configuration required to consistently compile, test, package, and release SplitSync V1.

### 5.2 Build System

The project must use a reproducible build system for:

```text
Android
Backend
Tests
Packaging
CI/CD
```

### 5.3 Android Build System

The Android application should use:

```text
Gradle
+
Android Gradle Plugin
```

with versions explicitly defined and controlled.

### 5.4 Backend Build System

The Backend should use a Java build system such as:

```text
Gradle
```

or:

```text
Maven
```

The selected tool must be standardized across Local Development and CI.

### 5.5 Build Version Control

Build tool versions must be pinned or otherwise controlled.

The build must not depend on an unspecified local tool version.

### 5.6 Java Compatibility

The project must define:

```text
Source Compatibility
Target Compatibility
Runtime Compatibility
```

for the selected Java version.

### 5.7 Android Compile Configuration

Android configuration must explicitly define:

```text
compileSdk
minSdk
targetSdk
```

The exact values will be locked during implementation setup.

### 5.8 Android Build Types

At minimum, Android should support:

```text
debug
release
```

### 5.9 Android Product Flavors

Where environment separation is required, use:

```text
dev
test
staging
prod
```

or an equivalent configuration strategy.

### 5.10 Build Variant

The resulting Android build variants may conceptually be:

```text
devDebug
devRelease
testDebug
stagingRelease
prodRelease
```

Only required combinations should be generated.

### 5.11 Development Build

Development builds should support:

```text
Debugging
Verbose Development Logging
Development API
Development Configuration
```

### 5.12 Test Build

Test builds should support:

```text
Test API
Test Configuration
Automated Testing
```

### 5.13 Staging Build

Staging builds should be as close to Production as practical while remaining isolated.

### 5.14 Production Build

Production builds must:

```text
Use Production API
Use Production Configuration
Disable Debug Features
Use Release Optimization
Use Production Signing
```

### 5.15 Android Signing

Android signing must distinguish:

```text
Debug Signing
Production Signing
```

### 5.16 Production Signing Security

Production signing credentials must:

```text
Not Be Committed
Not Be Hard-Coded
Not Be Printed in Logs
```

### 5.17 Android Versioning

Android releases must define:

```text
versionName
versionCode
```

according to Android release requirements.

### 5.18 Backend Build Profiles

Backend builds should support environment-specific profiles where required.

Conceptually:

```text
dev
test
staging
prod
```

### 5.19 Backend Artifact

The Backend build should produce a deployable artifact such as:

```text
JAR
```

or:

```text
Container Image
```

### 5.20 Backend Artifact Metadata

The artifact should contain traceable version information.

Example:

```text
Application Version
Build Version
Source Commit
```

### 5.21 Dependency Management

All dependencies must be explicitly declared.

Dependencies should be reviewed before being introduced.

### 5.22 Dependency Locking

Where supported and practical, dependency versions should be locked to improve reproducibility.

### 5.23 Dependency Updates

Dependency upgrades should be treated as controlled changes.

They should include:

```text
Build
Tests
Compatibility Review
Security Review
```

### 5.24 Build Reproducibility

The same source revision and build configuration should produce an equivalent artifact.

### 5.25 Clean Build

The project must support a clean build:

```text
Clean
 ↓
Build
```

without depending on stale local artifacts.

### 5.26 Incremental Build

Normal development should support incremental builds for faster feedback.

### 5.27 Build Outputs

Generated outputs should remain outside source-controlled source directories where possible.

Examples:

```text
build/
generated/
temporary files
```

### 5.28 Generated Code

Generated sources must be clearly separated from manually maintained source code.

### 5.29 Build Configuration Separation

Build configuration must remain separate from:

```text
Domain Logic
Business Rules
Application Services
```

### 5.30 Environment Configuration

Environment values should be supplied through:

```text
Build Configuration
Runtime Configuration
Environment Variables
Secret Management
```

as appropriate.

### 5.31 API URL Configuration

Android API URLs must be environment-specific.

Example:

```text
Development → Development API
Staging     → Staging API
Production  → Production API
```

### 5.32 Database Configuration

Backend database configuration must be environment-specific.

### 5.33 Secret Configuration

Secrets must be injected securely during build or deployment.

### 5.34 Build-Time vs Runtime Configuration

Use build-time configuration for values that genuinely differ between application artifacts.

Use runtime configuration for values that can safely change without rebuilding the application.

### 5.35 Android Build-Time Configuration

Possible build-time values include:

```text
Application ID
Environment Identifier
API Endpoint
Feature Configuration
```

### 5.36 Backend Runtime Configuration

Backend runtime configuration may include:

```text
Database URL
Database Credentials
Authentication Configuration
Logging Level
External Service Configuration
```

### 5.37 Production Configuration Validation

Production builds must validate that:

```text
Production API
Production Configuration
Production Signing
```

are correctly selected.

### 5.38 Debug Configuration Protection

Debug-only configuration must not be accidentally included in Production.

### 5.39 Release Optimization

Production Android builds should enable appropriate release optimizations.

Backend builds should also use the appropriate Production runtime configuration.

### 5.40 Build Testing

Every build must be validated by the appropriate automated tests.

### 5.41 Android Build Validation

At minimum:

```text
Compile
Unit Tests
Static Checks
```

### 5.42 Backend Build Validation

At minimum:

```text
Compile
Unit Tests
Integration Tests
Static Checks
```

### 5.43 Build Failure

If a mandatory build or test step fails:

```text
Build
   ↓
Failure
   ↓
Deployment Blocked
```

### 5.44 Build Warnings

Important compiler/build warnings should be reviewed rather than ignored.

### 5.45 Static Analysis

The build pipeline should include appropriate static analysis.

Potential checks include:

```text
Code Quality
Nullability
Unused Code
Security Issues
Style Violations
```

### 5.46 Formatting Verification

Where automated formatting is used, CI should verify formatting consistency.

### 5.47 Test Source Sets

Android should separate:

```text
Unit Tests
Instrumentation Tests
```

Backend should separate:

```text
Unit Tests
Integration Tests
```

where appropriate.

### 5.48 Test Dependencies

Test-only dependencies must not unnecessarily become Production runtime dependencies.

### 5.49 Production Dependency Review

Before release, verify that Production contains only required dependencies.

### 5.50 Build Artifact Validation

Before deployment, validate:

```text
Artifact Exists
Artifact Version Correct
Artifact Configuration Correct
Artifact Tests Passed
Artifact Traceable to Commit
```

### 5.51 Artifact Immutability

After an artifact is validated:

```text
Do Not Modify Artifact
```

The same validated artifact should proceed through the deployment pipeline.

### 5.52 Android Release Artifact

Production Android distribution should use the appropriate release artifact, preferably:

```text
AAB
```

for supported app distribution.

### 5.53 Backend Release Artifact

Production Backend should use the validated artifact produced by CI.

### 5.54 Container Build

If the Backend is containerized:

```text
Source
 ↓
Build
 ↓
Test
 ↓
Container Image
 ↓
Scan
 ↓
Deploy
```

### 5.55 Container Image Version

Container images should use immutable version identifiers.

Example:

```text
splitsync-backend:1.0.0
```

or a commit-based identifier.

### 5.56 Container Security

Container images should:

```text
Use Minimal Base Image
Avoid Unnecessary Packages
Run With Appropriate User Permissions
Be Scanned for Vulnerabilities
```

where applicable.

### 5.57 Backend Runtime Configuration

Container images should not embed Production secrets.

Secrets should be supplied at runtime.

### 5.58 Build Cache

Build caching may be used to improve performance but must not compromise reproducibility.

### 5.59 CI Build Environment

CI should use a controlled environment containing:

```text
Required JDK
Required Android SDK
Required Build Tools
Required Dependencies
```

### 5.60 CI Build Sequence

```text
Checkout
      ↓
Setup JDK
      ↓
Setup Android SDK
      ↓
Resolve Dependencies
      ↓
Build
      ↓
Test
      ↓
Static Analysis
      ↓
Artifact
```

### 5.61 Build Parallelization

Independent Android and Backend builds may run in parallel.

```text
              Source
                ↓
        ┌───────┴───────┐
        ↓               ↓
     Android          Backend
        ↓               ↓
      Tests           Tests
        └───────┬───────┘
                ↓
             Results
```

### 5.62 Build Cache Isolation

Cached artifacts must not cause one environment to accidentally use another environment's configuration.

### 5.63 Build Secrets

Build secrets must be available only to jobs that require them.

### 5.64 Pull Request Build

Every Pull Request should execute the relevant:

```text
Android Build
Backend Build
Unit Tests
Static Checks
```

### 5.65 Main Branch Build

The main branch should execute the complete mandatory build and test suite.

### 5.66 Release Build

A Release Build should execute:

```text
Full Required Tests
Security Checks
Version Validation
Artifact Generation
Artifact Validation
```

### 5.67 Build Versioning

The build system should make it possible to identify:

```text
Application Version
Build Number
Commit
Environment
```

### 5.68 Build Metadata

The Backend should expose a safe application version through an appropriate mechanism.

Sensitive build information must not be exposed.

### 5.69 Android Build Metadata

Android should maintain:

```text
Version Name
Version Code
```

and optionally build metadata for diagnostics.

### 5.70 Database Migration Build Validation

If database migrations are part of the Backend build pipeline, CI should validate:

```text
Migration Syntax
Migration Application
Schema Version
```

### 5.71 Migration Compatibility

Build validation should verify that the application can start against the expected database schema.

### 5.72 Build and Synchronization Compatibility

Builds affecting:

```text
SyncOperation
SyncState
Conflict
API Contract
```

must trigger relevant synchronization tests.

### 5.73 Build and P2P Compatibility

Builds affecting P2P protocol behavior must trigger appropriate P2P compatibility tests.

### 5.74 Build and Security

Security-sensitive changes should trigger additional security validation.

### 5.75 Build Failure Diagnostics

CI failures should provide:

```text
Build Logs
Test Reports
Failure Details
Artifact Information
```

### 5.76 Build Cleanup

CI should clean temporary resources after execution.

### 5.77 Local Build Commands

The project should document standard commands for:

```text
Clean
Build
Test
Package
Run
```

The exact commands depend on the selected build tools.

### 5.78 Developer Build Consistency

Developers should use the same build commands as CI wherever practical.

### 5.79 Build Documentation

The README should document:

```text
Required Java Version
Android SDK Requirements
Build Commands
Test Commands
Release Build Process
```

### 5.80 Build Configuration Completion Criteria

Build Configuration is complete when:

```text
Android Build Works
Backend Build Works
Debug Build Works
Release Build Works
Tests Run
Static Checks Run
Environment Configuration Works
CI Build Works
Artifacts Are Versioned
Artifacts Are Traceable
Production Secrets Are Protected
```

### 5.81 Build Configuration Invariants

The following rules are mandatory for V1:

- Build configuration must be version controlled.
- Environment-specific configuration must be separated.
- Production secrets must not be committed.
- Production signing credentials must remain protected.
- Debug configuration must not leak into Production.
- Dependency versions must be controlled.
- CI and Local Development must use compatible tool versions.
- Clean builds must succeed.
- Mandatory tests must run before release.
- Build failures must block deployment.
- Release artifacts must be traceable to source commits.
- Validated artifacts must not be modified before deployment.
- Production artifacts must use Production configuration.
- Android Production builds must use Production signing.
- Backend Production builds must use Production runtime configuration.
- Database migrations must be validated before Production deployment.
- Synchronization changes must trigger synchronization testing.
- P2P changes must trigger P2P compatibility testing.
- Security-sensitive changes must receive appropriate security validation.
- Build outputs and generated files must not pollute source control.
- Build configuration must not contain business logic.

## 6. Domain Implementation

### 6.1 Purpose

This section defines how the SplitSync V1 Domain Model will be implemented in Java.

The Domain layer represents the core business rules and must remain independent from:

```text
Android Framework
Spring Framework
HTTP
Database
Room
JPA
Network
P2P Transport
UI
```

### 6.2 Domain Implementation Principle

The implementation should follow:

```text
Domain Model
      ↓
Business Rules
      ↓
Domain Validation
      ↓
Domain Operations
```

Infrastructure must depend on the Domain rather than the Domain depending on infrastructure.

### 6.3 Domain Components

The initial Domain implementation includes:

```text
User
Group
Membership
Expense
ExpenseSplit
Settlement
Balance
Device
SyncOperation
SyncState
Conflict
```

### 6.4 Domain Package

The Domain implementation should reside under the configured Domain package.

Conceptually:

```text
domain/
├── model/
├── valueobject/
├── service/
├── exception/
└── rule/
```

The exact package structure should remain consistent with the Package Naming strategy.

### 6.5 Domain Entities

Entities represent objects with a stable identity.

Core entities include:

```text
User
Group
Expense
Settlement
Device
SyncOperation
Conflict
```

### 6.6 Entity Identity

Every entity requiring synchronization must have a stable identifier.

Conceptually:

```text
Entity
  ↓
Entity ID
  ↓
Local Persistence
  ↓
Synchronization
```

### 6.7 Offline-Created IDs

IDs for synchronizable entities must support local creation.

Therefore the ID strategy must allow:

```text
Create Offline
      ↓
Generate ID Locally
      ↓
Persist Locally
      ↓
Synchronize Later
```

### 6.8 Entity Equality

Entity equality should be based on the defined identity rules rather than object reference.

The implementation must avoid accidental equality based solely on mutable fields.

### 6.9 Entity Mutability

Mutable Domain entities should expose controlled operations rather than allowing unrestricted state modification.

Example:

```text
Expense
   ↓
updateAmount(...)
updateDescription(...)
updateSplits(...)
```

rather than unrestricted field mutation.

### 6.10 Domain Constructors

Constructors or factory methods must prevent creation of invalid Domain objects where practical.

Example:

```text
Invalid Expense
      ↓
Creation Rejected
```

### 6.11 Factory Methods

Factories may be used when entity creation requires:

```text
Validation
Default Values
Generated IDs
Initial Version
Initial Timestamp
```

### 6.12 Domain Invariants

The Domain must enforce critical invariants.

Examples:

```text
Expense Amount > 0
Settlement Amount > 0
Expense Split Amount > 0
Expense Split Total = Expense Total
User Belongs To Group
```

### 6.13 Expense Invariant

For every valid Expense:

```text
Expense Total
      =
Sum(Expense Splits)
```

subject to the defined monetary precision and rounding rules.

### 6.14 Settlement Invariant

A valid Settlement must satisfy:

```text
Amount > 0
Payer Exists
Receiver Exists
Payer ≠ Receiver
```

according to the defined business rules.

### 6.15 Membership Invariant

Operations requiring Group membership must verify:

```text
User
  ↓
Group Membership
  ↓
Authorized Operation
```

### 6.16 Domain Validation

Validation should happen as close to the Domain rule as practical.

Validation includes:

```text
Required Values
Value Ranges
Relationships
Financial Rules
Membership Rules
State Rules
```

### 6.17 Domain Exceptions

Domain violations should produce Domain-specific exceptions.

Examples:

```text
InvalidExpense
InvalidExpenseSplit
InvalidSettlement
InvalidMembership
InvalidGroup
InvalidSyncOperation
```

The exact exception naming is implementation-specific.

### 6.18 Domain Services

Domain Services should be introduced only when a business operation does not naturally belong to a single Entity or Value Object.

Potential Domain Services include:

```text
BalanceCalculator
ExpenseSplitCalculator
ConflictResolver
```

### 6.19 Balance Calculation

Balance calculation should remain deterministic.

Conceptually:

```text
Expenses
+
Settlements
+
Membership
      ↓
Balance Calculation
      ↓
User Balances
```

### 6.20 Expense Split Calculation

Split calculation should be implemented independently from UI and persistence.

It must handle:

```text
Equal Split
Custom Split
Rounding
Total Validation
```

according to the V1 requirements.

### 6.21 Monetary Representation

Financial values must use a precise representation.

Avoid using binary floating-point types such as:

```text
float
double
```

for authoritative monetary calculations.

### 6.22 Currency

Currency must be represented explicitly where required.

Conceptually:

```text
Money
├── Amount
└── Currency
```

### 6.23 Timestamp

Time-related Domain fields should use a consistent representation.

Examples:

```text
createdAt
updatedAt
```

### 6.24 Version

Synchronizable entities should maintain the required version information.

Conceptually:

```text
Entity
├── ID
├── Version
└── UpdatedAt
```

### 6.25 Version Increment

A successful local or server-side mutation must update the entity version according to the synchronization rules.

### 6.26 Domain State

Entities with lifecycle state should expose valid state transitions only.

Example:

```text
State A
   ↓
Allowed Operation
   ↓
State B
```

Invalid transitions must be rejected.

### 6.27 SyncOperation Domain Model

A SyncOperation represents a logical change that must be synchronized.

It should contain the information required to:

```text
Identify Operation
Identify Entity
Identify Device
Identify Operation Type
Track Version
Track Payload
Track State
```

### 6.28 SyncOperation Identity

Every SyncOperation must have a unique operation ID.

This ID is critical for:

```text
Idempotency
Retry
Duplicate Detection
Conflict Tracking
```

### 6.29 SyncOperation State

The Domain should represent the defined SyncOperation lifecycle.

Example:

```text
PENDING
   ↓
IN_FLIGHT
   ↓
APPLIED
```

with failure states as defined in the Technical Design.

### 6.30 SyncState

SyncState represents synchronization progress for a Device or synchronization context.

It should support:

```text
Cursor
Last Sync
Status
Pending State
```

according to the synchronization model.

### 6.31 Conflict

Conflict represents an incompatible concurrent change.

A Conflict should contain enough information to support:

```text
Detection
Inspection
Resolution
Synchronization
```

### 6.32 Conflict Identity

Each Conflict must have a stable identifier.

### 6.33 Conflict Lifecycle

Conceptually:

```text
Detected
   ↓
Stored
   ↓
Resolved
   ↓
Resolution Synchronized
```

### 6.34 Device

Device represents an installation/device participating in synchronization.

It should contain:

```text
Device ID
User Association
Device State
Registration State
```

as required by the Domain model.

### 6.35 User

User represents the application-level user identity.

The Domain User must not depend directly on:

```text
Android Account APIs
HTTP Authentication
Database Entity
```

### 6.36 Group

Group represents the shared expense context.

A Group should contain:

```text
Group ID
Name
Owner / Creator
Version
Timestamps
```

according to the defined Domain Model.

### 6.37 Membership

Membership represents the relationship between:

```text
User
+
Group
```

Membership rules must be enforced by the Domain/Application layer.

### 6.38 Expense

Expense represents a financial transaction within a Group.

It should contain the required:

```text
Expense ID
Group ID
Creator
Amount
Currency
Description
Timestamp
Version
```

### 6.39 ExpenseSplit

ExpenseSplit represents an individual User's share of an Expense.

It should contain:

```text
Expense ID
User ID
Amount
```

and any additional fields defined by the Domain Model.

### 6.40 Settlement

Settlement represents a transfer that reduces outstanding balances.

It should contain:

```text
Settlement ID
Group ID
Payer
Receiver
Amount
Currency
Timestamp
Version
```

### 6.41 Domain Creation Flow

Entity creation should follow:

```text
Input
  ↓
Validation
  ↓
Factory / Constructor
  ↓
Valid Domain Entity
```

### 6.42 Domain Update Flow

Entity updates should follow:

```text
Existing Entity
      ↓
Validate Mutation
      ↓
Apply Mutation
      ↓
Update Version / Timestamp
      ↓
Updated Entity
```

### 6.43 Domain Delete / Tombstone

If deletion is represented through tombstones:

```text
Active Entity
      ↓
Delete Request
      ↓
Tombstone State
      ↓
Synchronization
```

The physical deletion strategy must follow the synchronization design.

### 6.44 Domain Serialization Independence

Domain objects should not be designed around JSON serialization requirements.

Serialization should occur at the boundary:

```text
Domain
   ↓
Mapper
   ↓
DTO
   ↓
JSON
```

### 6.45 Domain Persistence Independence

Domain objects should not require Room or JPA annotations unless the implementation explicitly chooses a shared model and accepts the coupling.

The preferred V1 approach is to keep persistence representation separate where practical.

### 6.46 Domain Testing

Every critical Domain rule must have Unit Tests.

Examples:

```text
Expense Validation
Split Validation
Settlement Validation
Balance Calculation
Membership Validation
Version Handling
Conflict Resolution
```

### 6.47 Domain Test Independence

Domain Unit Tests must run without:

```text
Android Device
Backend
Database
Network
P2P Connection
```

### 6.48 Domain Determinism

The same valid inputs must produce the same Domain result.

This is especially important for:

```text
Balance Calculation
Split Calculation
Conflict Resolution
```

### 6.49 Domain Layer Dependency Direction

The dependency direction should remain:

```text
Infrastructure
      ↓
Application
      ↓
Domain
```

and not:

```text
Domain
      ↓
Infrastructure
```

### 6.50 Domain Implementation Summary

```text
Domain
│
├── Entities
│   ├── User
│   ├── Group
│   ├── Membership
│   ├── Expense
│   ├── ExpenseSplit
│   ├── Settlement
│   ├── Device
│   ├── SyncOperation
│   ├── SyncState
│   └── Conflict
│
├── Value Objects
│   ├── Money
│   ├── Currency
│   ├── IDs
│   └── Timestamps
│
├── Domain Services
│   ├── Balance Calculation
│   ├── Split Calculation
│   └── Conflict Resolution
│
├── Validation
│
└── Domain Exceptions
```

### 6.51 Domain Completion Criteria

Domain implementation is complete when:

```text
Core Entities Implemented
Value Objects Implemented
Identifiers Defined
Versioning Defined
Validation Implemented
Business Rules Implemented
Financial Rules Implemented
Domain Exceptions Implemented
Domain Services Implemented
Domain Unit Tests Passing
```

---

## 7. Value Objects and Common Types

### 7.1 Purpose

This section defines the common Value Objects and primitive abstractions used throughout the SplitSync Domain.

The purpose is to prevent inconsistent handling of:

```text
Money
Currency
IDs
Timestamps
Versions
Cursors
```

### 7.2 Value Object Principle

A Value Object represents a value defined by its contents rather than an independent identity.

Conceptually:

```text
Value Object
      ↓
Value
      +
Validation
      +
Equality
```

### 7.3 Value Object Immutability

Value Objects should preferably be immutable.

Example:

```text
Money
   ↓
New Money
   ↓
New Value
```

rather than mutating the existing instance.

### 7.4 Value Object Equality

Value Objects should use value-based equality.

Example:

```text
Money(100, INR)
=
Money(100, INR)
```

### 7.5 Money

Money represents a monetary amount together with its currency.

Conceptually:

```text
Money
├── Amount
└── Currency
```

### 7.6 Money Validation

Money must validate:

```text
Amount
Currency
Precision
```

according to the supported financial rules.

### 7.7 Positive Money

Operations requiring a positive amount must reject:

```text
0
Negative Amount
```

where the Domain rule requires:

```text
Amount > 0
```

### 7.8 Zero Money

Zero may be valid for specific calculations but should not automatically be valid for:

```text
Expense
Settlement
ExpenseSplit
```

when those operations require positive amounts.

### 7.9 Negative Money

Negative monetary values must not be introduced implicitly.

If a Domain operation requires a negative balance, that should be represented by the Balance model rather than by incorrectly storing a negative Expense amount.

### 7.10 Currency

Currency should be represented using a dedicated type or validated enum/value.

Example:

```text
Currency
├── Code
```

### 7.11 Currency Code

Currency codes should follow the selected standard and be normalized consistently.

Example:

```text
INR
USD
EUR
```

### 7.12 Currency Validation

Unsupported currencies must be rejected according to the V1 supported-currency policy.

### 7.13 Currency Equality

Currency comparison should be case-normalized according to the implementation rules.

### 7.14 Monetary Precision

The implementation must define the supported decimal precision.

All calculations must follow the same precision rules.

### 7.15 Rounding

Rounding must be deterministic.

The same inputs must always produce the same rounded result.

### 7.16 Split Rounding

When an Expense cannot be divided evenly:

```text
Expense Total
      ↓
Split Calculation
      ↓
Rounding
      ↓
Final Split Amounts
```

must preserve:

```text
Sum(Splits) = Expense Total
```

### 7.17 User ID

User ID should be represented consistently throughout the Domain.

Conceptually:

```text
UserId
```

rather than passing arbitrary Strings throughout business logic.

### 7.18 Group ID

Group ID should use a dedicated representation:

```text
GroupId
```

### 7.19 Expense ID

Expense ID should use:

```text
ExpenseId
```

and must support offline creation.

### 7.20 Expense Split ID

If ExpenseSplit requires an independent identity, it should use a dedicated identifier.

If its identity is derived from:

```text
Expense
+
User
```

that rule must remain consistent across persistence and synchronization.

### 7.21 Settlement ID

Settlement should have a dedicated:

```text
SettlementId
```

### 7.22 Device ID

Device ID must uniquely identify an installation/device participating in synchronization.

```text
DeviceId
```

### 7.23 SyncOperation ID

Every SyncOperation must have:

```text
SyncOperationId
```

with uniqueness suitable for offline generation.

### 7.24 Conflict ID

Conflicts should have:

```text
ConflictId
```

for stable tracking.

### 7.25 ID Generation

IDs should be generated according to the selected V1 identifier strategy.

The strategy must support:

```text
Offline Generation
Global Uniqueness
Idempotency
Synchronization
```

### 7.26 ID Immutability

Once assigned, an entity ID must not change.

### 7.27 Timestamp

Timestamp should be represented using a consistent Domain type.

Conceptually:

```text
Timestamp
```

### 7.28 Timestamp Precision

Timestamp precision should be explicitly defined.

All platforms participating in synchronization must use compatible precision.

### 7.29 Timestamp Timezone

Persisted timestamps should use a consistent timezone strategy.

The Domain should not depend on the device's display timezone.

### 7.30 Created Timestamp

Entities requiring creation tracking should contain:

```text
createdAt
```

### 7.31 Updated Timestamp

Mutable entities should contain:

```text
updatedAt
```

where required.

### 7.32 Clock Abstraction

Time-dependent Domain operations may use a Clock abstraction.

Conceptually:

```text
Clock
  ↓
Current Timestamp
```

This allows deterministic tests.

### 7.33 Version

Version represents the revision of a synchronizable entity.

Conceptually:

```text
Version
├── Numeric Value
```

or another explicitly defined version representation.

### 7.34 Version Immutability

A Version value should be immutable.

### 7.35 Version Comparison

Version values must support deterministic comparison.

Example:

```text
Version 4
   <
Version 5
```

### 7.36 Version Increment

Version increments must follow the synchronization rules.

No arbitrary version modification should be allowed.

### 7.37 Cursor

Cursor represents synchronization progress.

Conceptually:

```text
Cursor
├── Position / Token
```

### 7.38 Cursor Validation

Invalid cursors must be rejected rather than silently interpreted as valid positions.

### 7.39 Cursor Immutability

Cursor values should be immutable.

### 7.40 Operation Type

SyncOperation should use a controlled operation type.

Example:

```text
CREATE
UPDATE
DELETE
```

The final set must follow the synchronization model.

### 7.41 SyncOperation Status

SyncOperation status should use a controlled type.

Example:

```text
PENDING
IN_FLIGHT
APPLIED
RETRYABLE_FAILURE
PERMANENT_FAILURE
```

### 7.42 Conflict Status

Conflict status should be explicitly represented.

Example:

```text
OPEN
RESOLVED
```

Additional states may be introduced if required.

### 7.43 Entity Type

Synchronization should use a controlled representation of the entity being synchronized.

Example:

```text
GROUP
EXPENSE
EXPENSE_SPLIT
SETTLEMENT
MEMBERSHIP
```

### 7.44 Device State

Device state should use a controlled representation.

Example:

```text
ACTIVE
REVOKED
```

### 7.45 Membership State

If membership has lifecycle state, it should use a controlled representation.

Example:

```text
ACTIVE
REMOVED
```

### 7.46 Common Identifier Rules

All Domain IDs must:

```text
Be Immutable
Be Validated
Support Equality
Support Serialization
Support Persistence
Support Synchronization
```

### 7.47 Common Type Serialization

Common types may provide explicit serialization mappings at infrastructure boundaries.

The Domain type itself should not be coupled to a specific transport format.

### 7.48 Common Type Persistence

Persistence adapters are responsible for converting common Domain types into database-compatible values.

Example:

```text
Money
   ↓
Persistence Mapper
   ↓
Amount + Currency Columns
```

### 7.49 Common Type API Mapping

API adapters are responsible for converting common types into API representations.

Example:

```text
Money
   ↓
API DTO
   ↓
JSON
```

### 7.50 Common Type Validation

Value Objects should reject invalid values at creation time where practical.

Example:

```text
Invalid Currency
      ↓
Currency Creation Rejected
```

### 7.51 Common Type Exceptions

Invalid Value Objects should use appropriate Domain exceptions.

### 7.52 Common Type Testing

Each Value Object should have dedicated Unit Tests.

Example:

```text
Money Tests
Currency Tests
ID Tests
Timestamp Tests
Version Tests
Cursor Tests
```

### 7.53 Money Tests

Money tests should cover:

```text
Valid Amount
Invalid Amount
Currency
Precision
Equality
Addition
Subtraction
Comparison
Rounding
```

where those operations are supported.

### 7.54 Currency Tests

Currency tests should cover:

```text
Valid Code
Invalid Code
Normalization
Equality
Supported Currency
Unsupported Currency
```

### 7.55 ID Tests

ID tests should cover:

```text
Valid ID
Invalid ID
Equality
Serialization
Generation
```

### 7.56 Timestamp Tests

Timestamp tests should cover:

```text
Creation
Comparison
Serialization
Precision
Timezone Handling
```

### 7.57 Version Tests

Version tests should cover:

```text
Comparison
Increment
Equality
Invalid Version
```

### 7.58 Cursor Tests

Cursor tests should cover:

```text
Valid Cursor
Invalid Cursor
Equality
Serialization
Comparison
```

where comparison is supported.

### 7.59 Common Type Dependency Rule

Value Objects must not depend on:

```text
Android UI
Room
JPA
Spring MVC
HTTP
P2P Transport
```

### 7.60 Common Type Usage

The same Domain type should be reused wherever the same business concept exists.

Avoid representing the same concept inconsistently.

Example:

```text
User ID
```

should not be represented as unrelated:

```text
String
Long
UUID
```

across different Domain operations without an explicit conversion boundary.

### 7.61 Primitive Obsession Prevention

Where a primitive represents a meaningful business concept, consider a dedicated Value Object.

Examples:

```text
UserId
GroupId
ExpenseId
Money
Currency
Version
Cursor
```

### 7.62 Common Types Summary

```text
Common Types
│
├── Money
├── Currency
├── UserId
├── GroupId
├── ExpenseId
├── ExpenseSplitId
├── SettlementId
├── DeviceId
├── SyncOperationId
├── ConflictId
├── Timestamp
├── Version
└── Cursor
```

### 7.63 Completion Criteria

Value Objects and Common Types are complete when:

```text
Types Defined
Validation Implemented
Equality Implemented
Immutability Enforced
Serialization Boundaries Defined
Persistence Mapping Defined
Unit Tests Passing
```

### 7.64 Implementation Invariants

The following rules are mandatory:

- Domain Value Objects must remain infrastructure-independent.
- Money must use precise monetary representation.
- Floating-point types must not be used for authoritative financial calculations.
- Currency must be explicitly represented.
- Monetary precision must be consistent.
- Rounding must be deterministic.
- Expense split calculations must preserve the Expense total.
- Synchronizable entities must have stable IDs.
- IDs must support offline creation.
- Entity IDs must remain immutable.
- SyncOperation IDs must support idempotency.
- Timestamps must use a consistent representation.
- Version values must support deterministic comparison.
- Cursors must be validated.
- Value Objects should be immutable.
- Value Objects must use value-based equality.
- Invalid Domain values must be rejected.
- Common Domain types must not depend on Android, Spring, Room, JPA, HTTP, or P2P infrastructure.
- API and Database representations must be mapped at the appropriate boundaries.
- Critical Value Objects must have dedicated Unit Tests.

## 8. Domain Validation

### 8.1 Purpose

This section defines how Domain validation will be implemented in SplitSync V1.

Domain validation is responsible for ensuring that invalid business states cannot be created or modified through the application.

Validation must be applied consistently regardless of whether an operation originates from:

```text
Android UI
Offline Operation
Backend API
Synchronization
Peer-to-Peer Synchronization
```

### 8.2 Validation Principle

The validation flow should follow:

```text
Input
  ↓
Basic Validation
  ↓
Domain Validation
  ↓
Business Rule Validation
  ↓
Valid Domain Operation
```

Invalid input must be rejected before it can create an invalid Domain state.

### 8.3 Validation Layers

Validation should be divided into:

```text
API / Input Validation
        ↓
Application Validation
        ↓
Domain Validation
        ↓
Persistence Constraints
```

Each layer has a different responsibility.

### 8.4 API Validation

API validation handles malformed external input.

Examples:

```text
Missing Required Field
Invalid JSON
Invalid Format
Invalid Parameter
Invalid Request Structure
```

API validation must not replace Domain validation.

### 8.5 Application Validation

Application validation verifies contextual requirements before invoking Domain operations.

Examples:

```text
User Exists
Group Exists
Membership Exists
Entity Exists
```

### 8.6 Domain Validation

Domain validation enforces business invariants.

Examples:

```text
Expense Amount > 0
Settlement Amount > 0
Expense Split Total = Expense Total
Payer ≠ Receiver
```

### 8.7 Persistence Validation

Database constraints provide the final protection against invalid persistent states.

Examples:

```text
NOT NULL
UNIQUE
FOREIGN KEY
CHECK
```

where supported and appropriate.

### 8.8 Validation Must Not Depend on UI

Business validation must not be implemented only inside Android UI code.

Incorrect:

```text
UI
 ↓
Validation
 ↓
Database
```

Preferred:

```text
UI
 ↓
Application
 ↓
Domain Validation
 ↓
Persistence
```

### 8.9 Validation Must Not Depend on API

The Backend Domain must validate operations even if the request bypasses the expected API client.

### 8.10 Entity Validation

Every Domain entity must enforce its required invariants during:

```text
Creation
Update
State Transition
Synchronization
```

### 8.11 User Validation

User creation/update validation should verify required User information according to the User model.

Examples:

```text
Required Identity
Valid Identifier
Valid State
```

### 8.12 Group Validation

Group validation should verify:

```text
Valid Group ID
Valid Name
Valid Owner / Creator
Valid State
```

### 8.13 Group Name Validation

Group names should reject invalid values according to the defined rules.

Examples:

```text
Null
Blank
Unsupported Length
```

### 8.14 Membership Validation

Membership validation must verify:

```text
User Exists
Group Exists
Membership State Is Valid
```

### 8.15 Duplicate Membership

The same User must not have multiple active Membership records for the same Group.

Conceptually:

```text
User + Group
      ↓
One Active Membership
```

### 8.16 Membership Removal

Removing a Member must follow the defined Group rules.

The implementation must prevent invalid states such as:

```text
Remove Required Group Owner
```

if the Domain rules prohibit it.

### 8.17 Expense Validation

An Expense must satisfy:

```text
Valid Expense ID
Valid Group
Valid Creator
Valid Amount
Valid Currency
Valid Timestamp
Valid State
```

### 8.18 Expense Amount Validation

An Expense amount must satisfy:

```text
Amount > 0
```

where required by the V1 financial rules.

### 8.19 Expense Currency Validation

Expense currency must be:

```text
Present
Supported
Valid
```

according to the supported currency policy.

### 8.20 Expense Group Validation

An Expense must belong to a valid Group.

```text
Expense
   ↓
Group
   ↓
Valid Group
```

### 8.21 Expense Creator Validation

The User creating an Expense must satisfy the Group membership rules.

### 8.22 Expense Split Validation

Each ExpenseSplit must contain:

```text
Valid Expense
Valid User
Valid Amount
```

### 8.23 Expense Split Amount

An ExpenseSplit amount must satisfy the defined monetary rules.

Where positive splits are required:

```text
Split Amount > 0
```

### 8.24 Expense Split Total

The sum of all ExpenseSplits must equal the Expense amount.

```text
Σ ExpenseSplits
      =
Expense Amount
```

This is a mandatory financial invariant.

### 8.25 Expense Split User

A User receiving an ExpenseSplit must satisfy the Group membership rules.

### 8.26 Duplicate Expense Split

An Expense must not contain duplicate active splits for the same User unless explicitly supported by the Domain model.

### 8.27 Split Currency

ExpenseSplit currency must be consistent with the Expense currency unless multi-currency behavior is explicitly supported.

### 8.28 Settlement Validation

A Settlement must contain:

```text
Valid Settlement ID
Valid Group
Valid Payer
Valid Receiver
Valid Amount
Valid Currency
Valid Timestamp
```

### 8.29 Settlement Amount

Settlement amount must satisfy:

```text
Amount > 0
```

where required by the V1 financial rules.

### 8.30 Settlement Payer

The Payer must be a valid User associated with the Group according to the Membership rules.

### 8.31 Settlement Receiver

The Receiver must be a valid User associated with the Group according to the Membership rules.

### 8.32 Settlement Payer and Receiver

The following must be rejected:

```text
Payer == Receiver
```

when the V1 rules require a transfer between two different Users.

### 8.33 Settlement Currency

Settlement currency must follow the supported currency rules and must be compatible with the relevant Group/Expense balance model.

### 8.34 Balance Validation

Balance calculations must preserve financial consistency.

Conceptually:

```text
Total Outstanding
=
Calculated Expenses
-
Applicable Settlements
```

according to the defined balance algorithm.

### 8.35 Balance Determinism

The same Domain state must always produce the same balance result.

### 8.36 Money Validation

Money values must validate:

```text
Amount
Currency
Precision
```

before entering financial calculations.

### 8.37 Rounding Validation

Rounding must be deterministic.

After rounding:

```text
Sum(Splits)
=
Expense Total
```

must remain true.

### 8.38 Timestamp Validation

Timestamps must:

```text
Use Supported Format
Use Supported Precision
Use Valid Time Representation
```

### 8.39 Version Validation

Synchronizable entities must contain a valid version.

Invalid values such as unsupported negative versions must be rejected.

### 8.40 Version Transition Validation

Version changes must follow the synchronization rules.

A client must not arbitrarily set:

```text
Version = Any Value
```

without going through the defined mutation/synchronization mechanism.

### 8.41 SyncOperation Validation

A SyncOperation must contain the required information:

```text
Operation ID
Entity ID
Entity Type
Operation Type
Device ID
Version
Timestamp
Payload
```

according to the synchronization model.

### 8.42 SyncOperation ID Validation

SyncOperation ID must:

```text
Exist
Be Valid
Be Unique
```

according to the identifier strategy.

### 8.43 SyncOperation Entity Validation

Every SyncOperation must reference a valid:

```text
Entity Type
Entity ID
```

combination.

### 8.44 SyncOperation Type Validation

Only supported operation types may be accepted.

Example:

```text
CREATE
UPDATE
DELETE
```

The exact V1 set must follow the synchronization design.

### 8.45 SyncOperation State Validation

Only valid state transitions may occur.

Example:

```text
PENDING
   ↓
IN_FLIGHT
   ↓
APPLIED
```

Invalid transitions must be rejected.

### 8.46 SyncOperation Idempotency Validation

Repeated processing of the same SyncOperation must not create duplicate business effects.

```text
Operation O1
   ↓
Apply

Operation O1
   ↓
Apply Again

Result
   ↓
Same Logical State
```

### 8.47 SyncState Validation

SyncState must contain a valid synchronization context.

Examples:

```text
Device
Cursor
Status
```

### 8.48 Cursor Validation

A Cursor must be valid for the synchronization context.

Malformed or incompatible cursors must be rejected.

### 8.49 Conflict Validation

A Conflict must reference:

```text
Valid Entity
Valid Entity Version
Valid Operation
```

according to the conflict model.

### 8.50 Conflict State Validation

Conflict state transitions must follow the defined lifecycle.

Example:

```text
OPEN
 ↓
RESOLVED
```

Invalid transitions must be rejected.

### 8.51 Device Validation

A Device must have:

```text
Valid Device ID
Valid User Association
Valid Device State
```

where applicable.

### 8.52 Revoked Device Validation

A revoked Device must not be allowed to perform operations that require an active Device identity.

### 8.53 Authorization Validation

Authorization checks must verify:

```text
Authenticated User
      ↓
Resource Ownership / Membership
      ↓
Allowed Operation
```

### 8.54 Group Authorization

A User must not modify Group data unless authorized.

### 8.55 Expense Authorization

A User must not modify an Expense unless authorized according to the Group and Expense rules.

### 8.56 Settlement Authorization

A User must not create or modify a Settlement unless authorized according to the Group rules.

### 8.57 Synchronization Authorization

Synchronization requests must verify:

```text
Authenticated Device
+
Associated User
+
Authorized Resource
```

### 8.58 P2P Validation

P2P operations must validate:

```text
Peer Identity
Protocol Version
Authentication
Message Type
Message Integrity
```

before applying received operations.

### 8.59 Validation During Synchronization

Received synchronized data must not bypass Domain validation.

```text
Remote Operation
      ↓
Deserialize
      ↓
Validate
      ↓
Domain Operation
      ↓
Persist
```

### 8.60 Validation During P2P

P2P data must follow the same principle:

```text
P2P Message
      ↓
Validate
      ↓
Domain Operation
      ↓
Persist
```

### 8.61 Validation Error Types

Validation failures should be categorized.

Examples:

```text
INVALID_INPUT
INVALID_ENTITY
INVALID_STATE
INVALID_MEMBERSHIP
INVALID_AMOUNT
INVALID_CURRENCY
INVALID_SPLIT
INVALID_SETTLEMENT
INVALID_VERSION
INVALID_SYNC_OPERATION
UNAUTHORIZED_OPERATION
```

The exact error codes should follow the API Error Model.

### 8.62 Validation Exception Mapping

Domain validation exceptions must be mapped at the application/API boundary.

```text
Domain Exception
      ↓
Application Boundary
      ↓
API Error Response
```

### 8.63 Validation Message

Validation errors should provide enough information to identify the problem without exposing sensitive internal information.

### 8.64 Validation Ordering

Validation should generally follow:

```text
Structure
   ↓
Required Values
   ↓
Value Objects
   ↓
Entity State
   ↓
Relationships
   ↓
Business Rules
   ↓
Persistence
```

### 8.65 Validation and Transactions

Validation must happen before committing an operation.

For multi-entity operations:

```text
Validate
      ↓
Apply
      ↓
Commit
```

### 8.66 Atomic Validation

Operations that modify multiple related records must validate the complete operation before partial persistence.

Example:

```text
Expense
+
Expense Splits
+
SyncOperation
```

must not leave an incomplete state.

### 8.67 Validation and Rollback

If Domain validation or persistence fails:

```text
Operation
      ↓
Failure
      ↓
Rollback
```

must prevent partial financial state.

### 8.68 Validation and Offline Operations

Offline operations must use the same Domain rules as online operations.

```text
Offline
   ↓
Domain Validation
```

must not be weaker than:

```text
Online
   ↓
Domain Validation
```

### 8.69 Validation and Backend Operations

Backend operations must validate synchronized data even when the Android client already validated it.

### 8.70 Defense in Depth

Validation must exist at multiple boundaries because:

```text
Client Can Be Modified
Network Can Fail
Requests Can Be Replayed
Devices Can Be Compromised
```

### 8.71 Validation and Database Constraints

Database constraints should reinforce, but not replace, Domain validation.

### 8.72 Validation Testing

Every critical validation rule must have automated tests.

Tests should include:

```text
Valid Input
Invalid Input
Boundary Values
Concurrent State
Stale State
Duplicate Input
Malformed Input
```

### 8.73 Boundary Testing

For numeric values test:

```text
Minimum Valid
Below Minimum
Maximum Valid
Above Maximum
Zero
Negative
```

where applicable.

### 8.74 Expense Validation Tests

Test:

```text
Valid Expense
Zero Amount
Negative Amount
Invalid Currency
Invalid Group
Unauthorized Creator
Invalid Splits
Split Total Mismatch
Duplicate Split
```

### 8.75 Settlement Validation Tests

Test:

```text
Valid Settlement
Zero Amount
Negative Amount
Invalid Payer
Invalid Receiver
Payer == Receiver
Unauthorized User
Invalid Currency
```

### 8.76 Membership Validation Tests

Test:

```text
Valid Membership
Duplicate Membership
Unknown User
Unknown Group
Invalid Removal
Unauthorized Membership Change
```

### 8.77 Synchronization Validation Tests

Test:

```text
Valid Operation
Invalid Operation ID
Invalid Entity
Invalid Entity Type
Invalid Version
Invalid State Transition
Duplicate Operation
Unauthorized Device
```

### 8.78 Conflict Validation Tests

Test:

```text
Valid Conflict
Invalid Entity
Invalid Version
Invalid Resolution
Duplicate Resolution
Invalid State Transition
```

### 8.79 P2P Validation Tests

Test:

```text
Valid Peer
Unknown Peer
Invalid Protocol
Invalid Message
Invalid Signature / Integrity
Unauthorized Device
Duplicate Message
```

### 8.80 Validation Completion Criteria

Domain Validation is complete when:

```text
Entity Validation Implemented
Value Validation Implemented
Relationship Validation Implemented
Financial Validation Implemented
State Validation Implemented
Synchronization Validation Implemented
Authorization Validation Implemented
P2P Validation Implemented
Database Constraints Defined
Validation Tests Passing
```

### 8.81 Validation Invariants

The following rules are mandatory:

- Domain validation must be independent of UI.
- Domain validation must be independent of HTTP.
- Domain validation must run for locally created operations.
- Domain validation must run for synchronized operations.
- Domain validation must run for P2P operations.
- Invalid Domain states must not be persisted.
- Expense amounts must follow the defined positive amount rule.
- Expense splits must preserve the Expense total.
- Settlement amounts must follow the defined positive amount rule.
- Settlement payer and receiver must follow the defined relationship rules.
- Users participating in Group operations must satisfy Membership rules.
- Synchronizable entities must have valid identifiers.
- SyncOperations must have valid identifiers and types.
- SyncOperation state transitions must be controlled.
- Duplicate SyncOperations must not create duplicate business effects.
- Conflicts must reference valid entity/version information.
- Revoked Devices must not perform unauthorized operations.
- Database constraints must reinforce critical Domain invariants.
- Multi-entity financial operations must be atomic.
- Validation failures must not result in partial financial state.
- Validation errors must not expose sensitive internal information.
- All critical validation rules must have automated tests.


## 9. Local Database Implementation

### 9.1 Purpose

This section defines the implementation of the SplitSync V1 Android local database.

The local database is a core part of the Offline-First architecture.

It provides:

```text
Persistent Local State
+
Offline Operation
+
Local Queries
+
Pending Synchronization
+
Conflict Storage
```

### 9.2 Local Database Principle

The Android application should treat the local database as the primary operational data store for the device.

Conceptually:

```text
Android Application
        ↓
Local Database
        ↓
User Reads / Writes
        ↓
Synchronization
```

### 9.3 Database Technology

The Android local database should use:

```text
Room
```

on top of SQLite.

The exact Room version must be selected during Build Configuration.

### 9.4 Database Layer

The local persistence architecture should follow:

```text
Application
      ↓
Repository
      ↓
DAO
      ↓
Room
      ↓
SQLite
```

### 9.5 Local Database Package

The local database implementation should be isolated under the Android data/infrastructure package.

Conceptually:

```text
data/
└── local/
    ├── database/
    ├── entity/
    ├── dao/
    ├── mapper/
    └── migration/
```

### 9.6 Room Database

A single configured Room database should manage the application-local data required by V1.

Conceptually:

```text
SplitSyncDatabase
```

### 9.7 Local Tables

The initial local schema should support:

```text
users
groups
memberships
expenses
expense_splits
settlements
devices
sync_operations
sync_state
conflicts
```

The exact columns must follow the Local Database Schema defined in the Technical Design.

### 9.8 Local User Entity

The local User entity stores the information required by the Android application.

It should support:

```text
User ID
User Information
Local State
Synchronization Metadata
```

as required by the Domain Model.

### 9.9 Local Group Entity

The Group entity should store:

```text
Group ID
Name
Creator / Owner
Version
Created At
Updated At
State
```

according to the schema.

### 9.10 Local Membership Entity

Membership should represent:

```text
User
+
Group
```

and contain the required membership state and synchronization metadata.

### 9.11 Local Expense Entity

Expense should store:

```text
Expense ID
Group ID
Creator
Amount
Currency
Description
Created At
Updated At
Version
State
```

according to the final schema.

### 9.12 Local ExpenseSplit Entity

ExpenseSplit should store:

```text
Expense ID
User ID
Amount
```

plus any required identity/version/state information.

### 9.13 Local Settlement Entity

Settlement should store:

```text
Settlement ID
Group ID
Payer
Receiver
Amount
Currency
Created At
Updated At
Version
State
```

according to the schema.

### 9.14 Local Device Entity

Device data should support:

```text
Device ID
User Association
Device State
Registration State
```

as required.

### 9.15 Local SyncOperation Entity

SyncOperation should store the pending synchronization information.

Conceptually:

```text
Operation ID
Entity Type
Entity ID
Operation Type
Device ID
Version
Timestamp
Payload
Status
Retry Information
```

### 9.16 Local SyncState Entity

SyncState should store synchronization progress.

Conceptually:

```text
Device / Context
Cursor
Last Sync
Status
```

### 9.17 Local Conflict Entity

Conflict should store:

```text
Conflict ID
Entity ID
Entity Type
Local Version
Remote Version
Operation Information
Resolution State
```

according to the Conflict Data Model.

### 9.18 Primary Keys

Every table must have a clearly defined primary key.

Primary keys must support:

```text
Uniqueness
Offline Creation
Efficient Lookup
Synchronization
```

where required.

### 9.19 Foreign Keys

Foreign keys should be used where appropriate to protect relationships.

Examples:

```text
Expense → Group
ExpenseSplit → Expense
ExpenseSplit → User
Settlement → Group
Membership → User
Membership → Group
```

### 9.20 Referential Integrity

The local database must prevent invalid references where the persistence model permits enforcement.

### 9.21 Indexes

Indexes should be created for frequently queried fields.

Potential indexes include:

```text
Group ID
User ID
Expense ID
Settlement ID
SyncOperation Status
SyncOperation Entity ID
Conflict Status
Cursor
```

The final index strategy must be based on actual query patterns.

### 9.22 Unique Constraints

Unique constraints should enforce required uniqueness.

Examples:

```text
User ID
Group ID
SyncOperation ID
Device ID
```

and composite uniqueness where required.

### 9.23 Membership Uniqueness

The database should prevent duplicate active Membership records for the same:

```text
User + Group
```

according to the schema strategy.

### 9.24 SyncOperation Uniqueness

SyncOperation IDs must be unique.

This is essential for idempotency.

### 9.25 Conflict Uniqueness

Conflict uniqueness should prevent accidental duplicate Conflict records where the Domain requires uniqueness.

### 9.26 Nullability

Columns should be nullable only when the Domain allows absence of the value.

### 9.27 Database Representation of Money

Money should be stored using a precise representation.

Avoid SQLite floating-point storage for authoritative monetary values.

A suitable representation may use:

```text
Integer Minor Units
```

or another explicitly defined precise representation.

### 9.28 Money and Currency Storage

Depending on the selected model:

```text
Amount
+
Currency
```

may be stored as separate columns.

### 9.29 Decimal Precision

The local representation must preserve the Domain's required monetary precision without loss.

### 9.30 Timestamp Storage

Timestamps should use a consistent storage representation.

The representation must preserve the precision required for synchronization.

### 9.31 Version Storage

Entity versions must be stored in a representation that supports deterministic comparison.

### 9.32 Device ID Storage

Device IDs must be stored consistently across all synchronization-related tables.

### 9.33 SyncOperation Payload

If SyncOperation payloads are stored locally, the representation must be:

```text
Deterministic
Serializable
Versionable
Queryable where required
```

### 9.34 Payload Strategy

The exact payload representation may be:

```text
Serialized JSON
```

or another defined format.

The choice must remain consistent with the Synchronization Data Model.

### 9.35 Payload Validation

Stored SyncOperation payloads must represent valid Domain operations.

### 9.36 DAO Layer

Each major aggregate or persistence concern should have an appropriate DAO.

Examples:

```text
UserDao
GroupDao
MembershipDao
ExpenseDao
ExpenseSplitDao
SettlementDao
SyncOperationDao
SyncStateDao
ConflictDao
DeviceDao
```

### 9.37 DAO Responsibility

DAOs should handle:

```text
Queries
Inserts
Updates
Deletes / Tombstones
Existence Checks
Transactions
```

as appropriate.

### 9.38 DAO and Domain Separation

DAOs should return persistence representations or mapped models according to the chosen architecture.

They should not contain complex business rules.

### 9.39 Repository Responsibility

Repositories coordinate:

```text
DAO
+
Domain
+
Remote Data Source
```

where required.

### 9.40 Local Repository

The Local Repository should provide Domain/Application-friendly operations.

Example:

```text
ExpenseRepository
      ↓
ExpenseDao
```

### 9.41 Database Transactions

Multi-table operations must use Room transactions when atomicity is required.

Example:

```text
Create Expense
      +
Create Expense Splits
      +
Create SyncOperation
```

must commit atomically.

### 9.42 Expense Transaction

Expense creation should follow:

```text
Validate Expense
      ↓
Insert Expense
      ↓
Insert Splits
      ↓
Create SyncOperation
      ↓
Commit
```

### 9.43 Settlement Transaction

Settlement creation should follow:

```text
Validate Settlement
      ↓
Insert Settlement
      ↓
Create SyncOperation
      ↓
Commit
```

### 9.44 Group Transaction

Group creation and required initial state should be persisted atomically.

### 9.45 Membership Transaction

Membership changes and related synchronization operations should be atomic where required.

### 9.46 SyncOperation Transaction

The Domain mutation and corresponding SyncOperation must not become inconsistent.

Preferred:

```text
Business Mutation
      +
SyncOperation
      ↓
One Local Transaction
```

### 9.47 Crash Safety

If the application terminates during a transaction:

```text
Either
Complete Transaction

OR

No Partial Transaction
```

must remain visible.

### 9.48 Local Database as Source of Truth

For supported offline operations:

```text
UI
 ↓
Local Database
```

must not depend on immediate Backend availability.

### 9.49 Remote Data Merge

Remote data received through synchronization must be applied through controlled database transactions.

### 9.50 Remote Apply

The synchronization layer should follow:

```text
Remote Change
      ↓
Validate
      ↓
Map
      ↓
Apply Transaction
      ↓
Update SyncState
```

### 9.51 Remote Change Atomicity

A batch of logically related changes should be applied atomically where required by the synchronization protocol.

### 9.52 Cursor Update

Cursor advancement must occur only after the corresponding remote changes have been successfully applied.

Incorrect:

```text
Update Cursor
      ↓
Apply Changes
```

Preferred:

```text
Apply Changes
      ↓
Successful Commit
      ↓
Update Cursor
```

### 9.53 Sync Failure

If remote changes cannot be applied:

```text
Database Changes
      ↓
Rollback
```

and the cursor must not advance beyond the successfully applied data.

### 9.54 Local Tombstones

If synchronization requires deleted entities to remain visible to the sync engine, deletion should use tombstones.

Conceptually:

```text
Active
  ↓
Deleted / Tombstone
  ↓
Synchronize
  ↓
Cleanup
```

### 9.55 Physical Deletion

Physical deletion should occur only when it is safe according to:

```text
Synchronization
Conflict Resolution
Retention
```

rules.

### 9.56 Soft Delete State

Where required, entities may contain:

```text
deleted
```

or an equivalent state.

### 9.57 Local Query Strategy

Queries should be designed around actual application use cases.

Avoid loading unnecessary data.

### 9.58 Group Query

Group details may require:

```text
Group
+
Memberships
+
Recent Expenses
+
Balance
+
Sync State
```

depending on the UI requirement.

### 9.59 Expense Query

Expense queries should support:

```text
By Group
By Expense ID
By Creator
By Date
```

where required.

### 9.60 Settlement Query

Settlement queries should support:

```text
By Group
By Settlement ID
By User
```

where required.

### 9.61 Pending Sync Query

The SyncOperation DAO must support efficient retrieval of pending operations.

Example:

```text
WHERE status = PENDING
ORDER BY creation order
```

according to the synchronization policy.

### 9.62 Retry Query

Retryable operations must be queryable based on:

```text
Status
Retry Time
Priority
```

where required.

### 9.63 Conflict Query

The Conflict DAO must support:

```text
Open Conflicts
Conflict By ID
Conflict By Entity
Resolved Conflicts
```

where required.

### 9.64 SyncState Query

SyncState must be retrievable efficiently for the current Device/context.

### 9.65 Reactive Local Data

The Android UI may observe local database changes through reactive mechanisms.

Conceptually:

```text
Database Change
      ↓
Repository
      ↓
ViewModel
      ↓
UI
```

### 9.66 Offline UI Updates

After a local operation:

```text
Database Updated
      ↓
UI Observes Change
      ↓
UI Updated
```

without waiting for synchronization.

### 9.67 Database Threading

Database operations must not block the Android main thread.

### 9.68 Background Database Operations

Use appropriate asynchronous mechanisms for:

```text
Queries
Inserts
Updates
Transactions
Synchronization
Migrations
```

### 9.69 Database Connection Lifecycle

Room should manage database connections according to Android lifecycle requirements.

### 9.70 Database Singleton

The Room database should generally be created as a controlled application-level instance.

Avoid creating unnecessary database instances.

### 9.71 Database Initialization

Database initialization should occur during application startup or first required access according to performance requirements.

### 9.72 Database Migration Strategy

Every schema change must have a versioned migration.

Conceptually:

```text
Schema V1
   ↓
Migration V1 → V2
   ↓
Schema V2
```

### 9.73 Migration Testing

Every migration must be tested against representative previous schemas.

### 9.74 Migration Failure

Migration failure must not silently discard financial data.

### 9.75 Destructive Migration

Destructive migrations must not be used for Production unless data loss is explicitly acceptable and approved.

For SplitSync financial data, destructive migration should generally be avoided.

### 9.76 Database Version

The Room database version must be incremented when the schema changes.

### 9.77 Schema Export

Room schema history should be preserved where supported.

This provides:

```text
Migration Review
Migration Testing
Schema History
```

### 9.78 Local Database Backup

The Android database must follow the application's data backup strategy.

Sensitive or synchronization-specific data must be considered when enabling platform backups.

### 9.79 Database Encryption

If local encryption is required by the Security Architecture, the selected encryption strategy must be integrated without changing Domain behavior.

### 9.80 Local Database Security

Local persistence must consider:

```text
Device Access
Application Data Extraction
Backup
Debug Builds
Rooted / Compromised Devices
```

according to the V1 Security requirements.

### 9.81 Database Logging

Database logs must not expose:

```text
Sensitive User Data
Authentication Secrets
Private Keys
```

### 9.82 Local Database Testing

Tests must cover:

```text
Insert
Read
Update
Delete / Tombstone
Relations
Constraints
Transactions
Migrations
Indexes
Queries
```

### 9.83 Transaction Tests

Transaction tests should verify that:

```text
All Operations Succeed
```

or:

```text
All Operations Roll Back
```

when failure occurs.

### 9.84 Sync Transaction Tests

Specifically verify:

```text
Expense
+
Expense Splits
+
SyncOperation
```

remain consistent.

### 9.85 Cursor Transaction Tests

Verify that:

```text
Changes Applied
+
Cursor Updated
```

occur atomically when required.

### 9.86 Conflict Transaction Tests

Verify that Conflict creation and related state changes remain consistent.

### 9.87 Concurrent Access

The database implementation must safely handle concurrent operations from:

```text
UI
Background Sync
P2P Sync
Remote Sync
```

### 9.88 Concurrent Mutation

Concurrent local operations must not result in:

```text
Lost Update
Partial Expense
Duplicate Settlement
Corrupt SyncOperation
```

### 9.89 Synchronization Concurrency

The Sync Engine must coordinate database access to prevent conflicting writes.

### 9.90 P2P Concurrency

P2P synchronization must use the same persistence safety rules as Backend synchronization.

### 9.91 Database Cleanup

Cleanup jobs may remove obsolete data only when the synchronization and retention rules permit it.

### 9.92 SyncOperation Cleanup

Applied SyncOperations may eventually be compacted or removed according to the retention policy.

They must not be removed while still required for:

```text
Retry
Conflict
Recovery
Audit
```

### 9.93 Conflict Cleanup

Resolved conflicts may be retained or removed according to the defined retention strategy.

### 9.94 Database Performance

Queries must be measured for:

```text
Execution Time
Memory Usage
Returned Rows
Index Usage
```

where necessary.

### 9.95 Large Groups

The local database must support groups with many:

```text
Members
Expenses
Expense Splits
Settlements
```

without loading the entire dataset unnecessarily.

### 9.96 Pagination

Pagination should be introduced for large datasets where required.

### 9.97 Expense List Pagination

Expense history should support pagination or incremental loading if required by expected data volume.

### 9.98 Database Schema Consistency

The local schema must remain aligned with:

```text
Domain Model
Technical Design
Synchronization Model
```

### 9.99 Database Mapping

Mappings should follow:

```text
Domain
   ↓
Local Entity
```

and:

```text
Local Entity
   ↓
Domain
```

### 9.100 Mapping Validation

Mapping code must preserve:

```text
ID
Amount
Currency
Timestamp
Version
State
```

without unintended transformation.

### 9.101 Local Database Completion Criteria

Local Database Implementation is complete when:

```text
Room Configured
Entities Implemented
DAOs Implemented
Repositories Connected
Indexes Defined
Constraints Defined
Transactions Implemented
Migrations Implemented
Mapping Implemented
Offline Persistence Works
Sync Persistence Works
Conflict Persistence Works
Database Tests Pass
```

### 9.102 Local Database Invariants

The following rules are mandatory:

- The local database must support offline operation.
- The local database must preserve financial data across application restarts.
- Persistent financial data must not depend on Backend availability.
- Room must be used as the V1 local persistence mechanism.
- Database operations must not block the Android main thread.
- Critical multi-table operations must be transactional.
- Expense and ExpenseSplit persistence must remain consistent.
- Settlement persistence must remain consistent.
- Domain mutations and corresponding SyncOperations must remain consistent.
- SyncOperation IDs must be unique.
- Required foreign-key relationships must be protected.
- Required unique constraints must be enforced.
- Monetary values must use precise storage.
- Floating-point storage must not be used for authoritative monetary values.
- Entity versions must be persisted consistently.
- Timestamps must use a consistent representation.
- Cursor updates must occur only after successful change application.
- Failed synchronization transactions must not advance the cursor incorrectly.
- Deleted entities must use tombstones when required by synchronization.
- Physical deletion must not occur while synchronization still requires the data.
- Database migrations must be versioned.
- Production migrations must not silently discard financial data.
- Migration failures must not silently reset the database.
- Local database changes must be tested.
- Synchronization writes must be safe under concurrent access.
- P2P synchronization must use the same database integrity rules as Backend synchronization.
- Database credentials and sensitive local data must be handled according to the Security Architecture.
- Database logs must not expose sensitive information.
- Local schema changes must remain aligned with the Domain and Technical Design.

## 10. Backend Database Implementation

### 10.1 Purpose

This section defines the implementation of the SplitSync V1 Backend database.

The Backend database provides persistent server-side storage for:

```text
Users
Groups
Memberships
Expenses
Expense Splits
Settlements
Devices
Synchronization Data
Conflicts
Authentication Data
Authorization Data
```

### 10.2 Database Principle

The Backend database is the persistent source of server-side state.

Conceptually:

```text
Backend Application
        ↓
Repository
        ↓
Database
```

The database must preserve data across:

```text
Backend Restart
Backend Deployment
Backend Scaling
Application Instance Replacement
```

### 10.3 Database Technology

The Backend should use a relational database for V1.

The exact database engine and version must be finalized during infrastructure setup.

### 10.4 Database Access

The Backend should access the database through a controlled persistence layer.

Conceptually:

```text
API
 ↓
Application Service
 ↓
Domain
 ↓
Repository
 ↓
Persistence
 ↓
Database
```

### 10.5 Database Package

Backend database implementation should be isolated under the infrastructure/persistence package.

Conceptually:

```text
infrastructure/
└── persistence/
    ├── entity/
    ├── repository/
    ├── mapper/
    ├── migration/
    └── configuration/
```

### 10.6 Database Tables

The initial Backend schema should support:

```text
users
groups
memberships
expenses
expense_splits
settlements
devices
sync_operations
conflicts
```

Additional tables may be introduced for:

```text
Authentication
Authorization
Sessions / Tokens
Audit
```

when required by the Security Architecture.

### 10.7 User Table

The User table should store the persistent User information required by the Backend.

It should contain the fields defined by the Backend Database Schema.

### 10.8 Group Table

The Group table should store:

```text
Group ID
Name
Creator / Owner
Version
Created At
Updated At
State
```

according to the final schema.

### 10.9 Membership Table

Membership represents:

```text
User
+
Group
```

and stores the membership state required by the authorization and Domain models.

### 10.10 Expense Table

Expense should store:

```text
Expense ID
Group ID
Creator
Amount
Currency
Description
Created At
Updated At
Version
State
```

according to the defined schema.

### 10.11 Expense Split Table

ExpenseSplit should store:

```text
Expense ID
User ID
Amount
```

and any additional fields required by the synchronization model.

### 10.12 Settlement Table

Settlement should store:

```text
Settlement ID
Group ID
Payer
Receiver
Amount
Currency
Created At
Updated At
Version
State
```

according to the final schema.

### 10.13 Device Table

Device data should support:

```text
Device ID
User ID
Device State
Registration State
Created At
Updated At
```

where required.

### 10.14 SyncOperation Table

SyncOperation must preserve the server-side synchronization information.

Conceptually:

```text
Operation ID
Device ID
User ID
Entity Type
Entity ID
Operation Type
Version
Timestamp
Payload
Status
```

Additional metadata may be included according to the Synchronization Data Model.

### 10.15 Conflict Table

Conflict should preserve:

```text
Conflict ID
Entity Type
Entity ID
Local / Client Version
Server Version
Operation Information
Conflict State
Resolution Information
```

according to the Conflict Data Model.

### 10.16 Authentication Data

Authentication-specific persistent data must be stored separately from normal business entities where appropriate.

Sensitive authentication material must be stored according to the Security Architecture.

### 10.17 Authorization Data

Authorization-related data should support:

```text
User
Group
Membership
Role / Permission
```

as defined by the Authorization Model.

### 10.18 Primary Keys

Every table must have a stable primary key.

Synchronizable entities must use identifiers compatible with:

```text
Offline Creation
Synchronization
Idempotency
```

### 10.19 Foreign Keys

Foreign keys should protect required relationships.

Examples:

```text
Expense → Group
ExpenseSplit → Expense
ExpenseSplit → User
Settlement → Group
Membership → User
Membership → Group
Device → User
```

### 10.20 Referential Integrity

The database must prevent invalid relationships where database-level enforcement is appropriate.

### 10.21 Unique Constraints

Unique constraints should enforce Domain-required uniqueness.

Examples:

```text
User ID
Group ID
Expense ID
Settlement ID
Device ID
SyncOperation ID
Conflict ID
```

### 10.22 Membership Uniqueness

The database should prevent duplicate active membership relationships for:

```text
User + Group
```

according to the selected schema strategy.

### 10.23 Device Uniqueness

Device IDs must be unique.

### 10.24 SyncOperation Uniqueness

SyncOperation IDs must be globally unique within the synchronization system.

This is required for idempotent processing.

### 10.25 Conflict Uniqueness

Conflict records must not be duplicated accidentally for the same logical conflict.

The exact uniqueness constraint must follow the Conflict Data Model.

### 10.26 Nullability

Columns must be nullable only when the Domain allows missing values.

### 10.27 Monetary Storage

Authoritative monetary values must use precise database representations.

Avoid:

```text
FLOAT
DOUBLE
```

for financial amounts.

### 10.28 Monetary Representation

A suitable representation may use:

```text
Integer Minor Units
```

or:

```text
Exact Decimal
```

depending on the selected financial model.

The representation must remain consistent with the Domain.

### 10.29 Currency Storage

Currency should be stored explicitly.

Conceptually:

```text
amount
currency
```

### 10.30 Timestamp Storage

Timestamps must use a consistent representation across:

```text
Android
Backend
Database
Synchronization
```

### 10.31 Version Storage

Entity versions must use a representation that supports deterministic comparison.

### 10.32 Database Indexes

Indexes should be created based on actual access patterns.

Potential indexes include:

```text
User ID
Group ID
Expense ID
Settlement ID
Device ID
SyncOperation Status
SyncOperation Entity ID
Conflict Status
Version
Timestamp
```

### 10.33 Synchronization Indexes

Synchronization queries are expected to be frequent.

Indexes should support:

```text
Pending Operations
Operations By Device
Operations By Entity
Changes After Cursor
Conflicts
```

### 10.34 Group Query Indexes

Group-related queries should support efficient access to:

```text
Members
Expenses
Settlements
```

### 10.35 Expense Query Indexes

Expense queries should support:

```text
Group ID
Creator
Timestamp
Version
```

where required.

### 10.36 Database Constraints

Database constraints should reinforce critical invariants.

Examples:

```text
NOT NULL
UNIQUE
FOREIGN KEY
CHECK
```

where supported and appropriate.

### 10.37 Database Entities

If JPA/Hibernate is used, persistence entities should represent database rows independently from the core Domain model where practical.

Example:

```text
ExpenseEntity
```

should not automatically become the Domain:

```text
Expense
```

unless the implementation explicitly accepts that coupling.

### 10.38 Entity Mapping

Persistence mapping should follow:

```text
Database Entity
      ↓
Mapper
      ↓
Domain Entity
```

and:

```text
Domain Entity
      ↓
Mapper
      ↓
Database Entity
```

### 10.39 Repository Interface

Application code should depend on Repository abstractions rather than directly accessing JPA/SQL implementation details.

### 10.40 Transaction Management

Database transactions must be used for operations that modify multiple related records.

### 10.41 Expense Transaction

Creating an Expense should ensure consistency between:

```text
Expense
+
Expense Splits
+
Required Sync State
```

according to the Backend synchronization strategy.

### 10.42 Settlement Transaction

Settlement creation/update should remain atomic where multiple records are affected.

### 10.43 Membership Transaction

Membership changes and related authorization/synchronization state must remain consistent.

### 10.44 Synchronization Transaction

Applying a SyncOperation should be transactional when the operation affects multiple records.

Example:

```text
Receive Operation
      ↓
Validate
      ↓
Apply Entity Change
      ↓
Record Operation Result
      ↓
Update Version
      ↓
Commit
```

### 10.45 Idempotent Transaction

The database transaction must ensure duplicate SyncOperations do not create duplicate business effects.

Conceptually:

```text
Operation O1
      ↓
Check Operation ID
      ↓
Already Applied?
      ↓
Return Existing Result
```

### 10.46 Optimistic Concurrency

Synchronizable entities should use optimistic concurrency where appropriate.

Conceptually:

```text
Current Version = 5
Client Base Version = 5
        ↓
Apply
        ↓
Version = 6
```

### 10.47 Version Conflict

If:

```text
Current Version = 6
Client Base Version = 5
```

the Backend must apply the defined conflict detection strategy rather than blindly overwriting the current state.

### 10.48 Lost Update Prevention

The database and Application layer must prevent:

```text
Client A Update
+
Client B Update
```

from silently overwriting each other when the synchronization rules identify a conflict.

### 10.49 Database Isolation

Transaction isolation must be selected according to the consistency requirements of:

```text
Financial Operations
Synchronization
Conflict Resolution
```

### 10.50 Financial Integrity

Database operations must preserve:

```text
Expense Total
=
Sum(Expense Splits)
```

for valid persisted Expense states.

### 10.51 Settlement Integrity

Settlement records must satisfy the Domain validation rules before persistence.

### 10.52 Membership Integrity

A Membership must reference valid:

```text
User
+
Group
```

### 10.53 Database Validation

Database validation should occur in addition to Domain validation.

```text
Domain Validation
      ↓
Persistence
      ↓
Database Constraints
```

### 10.54 Migration Framework

Database schema changes must use versioned migrations.

The selected migration mechanism should support:

```text
Versioning
Repeatability
Testing
Production Deployment
```

### 10.55 Migration Naming

Migrations should use a consistent naming convention.

Example:

```text
V1__initial_schema
V2__add_conflict_table
V3__add_sync_index
```

The exact naming depends on the selected migration framework.

### 10.56 Migration Testing

Every migration must be tested against the previous schema version.

### 10.57 Migration Order

Migrations must execute in deterministic order.

### 10.58 Migration Failure

If a migration fails:

```text
Migration
      ↓
Failure
      ↓
Deployment Halted
```

No partially migrated Production environment should be accepted.

### 10.59 Destructive Migration

Destructive migrations must be avoided unless explicitly approved.

For financial data:

```text
Data Loss
```

must not be an implicit migration consequence.

### 10.60 Expand-and-Contract

Breaking schema changes should preferably follow:

```text
Expand
      ↓
Deploy Compatible Code
      ↓
Migrate Data
      ↓
Switch Application
      ↓
Contract
```

### 10.61 Database Backup

Production database backups must be configured according to the Deployment Architecture.

### 10.62 Backup Before Migration

Critical Production migrations should have an appropriate recovery point before execution.

### 10.63 Recovery

Recovery procedures must be tested periodically.

### 10.64 Database Security

The database must:

```text
Not Be Publicly Exposed
Use Controlled Credentials
Use Least Privilege
Use Encrypted Connections Where Required
```

### 10.65 Database Credentials

Database credentials must be supplied through secure configuration.

They must not be committed to source control.

### 10.66 Database User Permissions

The Backend database user should have only the permissions required by the application.

### 10.67 Administrative Database Access

Administrative access should use separate controlled credentials from the normal application database account.

### 10.68 Sensitive Data

Sensitive data stored in the database must follow the Security Architecture.

### 10.69 Authentication Secrets

Authentication secrets must not be stored as plain text unless explicitly required by the selected security mechanism.

### 10.70 Database Logging

SQL/database logs must not expose sensitive values.

### 10.71 Database Monitoring

Production monitoring should include:

```text
Database Availability
Connection Count
Query Latency
Storage Usage
Error Rate
Locking / Contention
```

where applicable.

### 10.72 Connection Pool

The Backend should use a controlled database connection pool.

The pool must be configured according to:

```text
Backend Instances
Database Capacity
Expected Load
```

### 10.73 Connection Scaling

When Backend instances scale horizontally:

```text
Total Connections
=
Instances × Connections Per Instance
```

must remain within database capacity.

### 10.74 Query Performance

Queries must be reviewed for:

```text
Index Usage
Execution Time
Returned Data Size
N+1 Problems
```

### 10.75 N+1 Prevention

Persistence code must avoid unnecessary N+1 query patterns.

### 10.76 Pagination

Large collections must use pagination where appropriate.

Examples:

```text
Group Expenses
Group Members
Sync Operations
Conflicts
```

### 10.77 Batch Operations

Synchronization should support controlled batch processing where appropriate.

### 10.78 Batch Size

Batch sizes must be limited to avoid:

```text
Large Transactions
Excessive Memory Usage
Database Timeouts
Large HTTP Requests
```

### 10.79 Database Transactions and Batching

Large synchronization batches may be split into smaller transactions when the synchronization protocol allows it.

### 10.80 Database Concurrency

The database implementation must safely support concurrent requests from multiple Backend instances.

### 10.81 Concurrent Synchronization

Multiple synchronization requests may attempt to update the same entity.

The implementation must rely on:

```text
Transactions
Version Checks
Constraints
Idempotency
```

to preserve correctness.

### 10.82 Concurrent Expense Operations

Concurrent Expense operations must not result in:

```text
Duplicate Expense
Invalid Splits
Lost Update
Corrupt Group State
```

### 10.83 Concurrent Settlement Operations

Concurrent Settlement operations must preserve settlement and balance integrity.

### 10.84 Concurrent Membership Operations

Concurrent membership changes must preserve authorization consistency.

### 10.85 Database Cleanup

Cleanup operations must be safe and controlled.

Examples:

```text
Old SyncOperations
Resolved Conflicts
Expired Sessions
Temporary Records
```

### 10.86 SyncOperation Retention

Applied SyncOperations must not be deleted before they are no longer required for:

```text
Idempotency
Recovery
Conflict Processing
Audit
```

### 10.87 Conflict Retention

Conflict records should be retained according to the defined operational/audit requirements.

### 10.88 Data Integrity Checks

Periodic integrity checks may verify:

```text
Expense / Split Consistency
Membership Integrity
SyncOperation Integrity
Conflict Integrity
```

### 10.89 Database Testing

Backend database tests must cover:

```text
Schema
Constraints
Repositories
Transactions
Migrations
Indexes
Concurrency
```

### 10.90 Repository Integration Tests

Repository tests should use an isolated Test database rather than Production.

### 10.91 Migration Tests

Migration tests should verify:

```text
Old Schema
      ↓
Migration
      ↓
New Schema
      ↓
Data Preserved
```

### 10.92 Transaction Tests

Transaction tests should verify:

```text
Success
```

and:

```text
Failure
 ↓
Rollback
```

### 10.93 Idempotency Tests

Synchronization persistence tests must verify:

```text
Operation O1
      ↓
Applied

Operation O1
      ↓
Applied Again

Final State
      ↓
Same Logical Result
```

### 10.94 Concurrency Tests

Concurrency tests should verify that simultaneous updates preserve the defined version/conflict behavior.

### 10.95 Database Completion Criteria

Backend Database Implementation is complete when:

```text
Database Created
Schema Implemented
Entities Implemented
Mappings Implemented
Repositories Connected
Constraints Defined
Indexes Defined
Transactions Implemented
Migrations Implemented
Security Configured
Backup Strategy Defined
Database Tests Passing
Concurrency Tests Passing
```

### 10.96 Backend Database Invariants

The following rules are mandatory:

- Production database must not be publicly exposed.
- Database credentials must be securely managed.
- Database users must follow least privilege.
- Financial amounts must use precise storage.
- Floating-point types must not be used for authoritative monetary values.
- Required relationships must use appropriate foreign keys.
- Required uniqueness must be enforced.
- Synchronizable entities must have stable identifiers.
- SyncOperation IDs must be unique.
- Duplicate SyncOperations must not create duplicate business effects.
- Critical financial operations must be transactional.
- Synchronization operations must use version/concurrency protection.
- Lost updates must not be silently accepted where conflict detection is required.
- Expense and ExpenseSplit state must remain financially consistent.
- Settlement state must remain valid.
- Membership state must remain valid.
- Database validation must reinforce Domain validation.
- Schema changes must use versioned migrations.
- Production migrations must have an appropriate recovery strategy.
- Destructive migrations must not silently destroy financial data.
- Backend instances must be able to scale without corrupting shared state.
- Connection pool configuration must account for horizontal scaling.
- Large datasets must use appropriate indexes and pagination.
- Database cleanup must not remove data still required for synchronization or recovery.
- Database tests must cover critical constraints and transactions.
- Sensitive data must not be unnecessarily exposed through database logs.
- Database implementation must remain aligned with the Domain Model and Technical Design.


## 11. Repository Implementation

### 11.1 Purpose

This section defines the implementation of the Repository layer in SplitSync V1.

Repositories provide an abstraction between:

```text
Application / Domain
```

and:

```text
Persistence / Network
```

The Repository layer prevents higher-level application code from depending directly on:

```text
Room
JPA
SQL
HTTP
Retrofit
P2P Transport
```

### 11.2 Repository Principle

The Repository should provide application-oriented access to data.

Conceptually:

```text
Application
      ↓
Repository
 ┌────┴────┐
 ↓         ↓
Local     Remote
Data      Data
Source    Source
```

### 11.3 Repository Responsibilities

Repositories may coordinate:

```text
Local Data
Remote Data
Domain Mapping
Caching
Persistence
Synchronization
```

according to the specific Repository.

### 11.4 Repository Must Not Contain UI Logic

Repositories must not depend on:

```text
Activity
Fragment
View
Compose UI
View Binding
```

### 11.5 Repository Must Not Contain HTTP Logic

HTTP-specific implementation should remain in the Remote Data Source.

Preferred:

```text
Repository
      ↓
RemoteDataSource
      ↓
API
```

### 11.6 Repository Must Not Contain SQL Logic

SQL/Room/JPA implementation should remain below the Repository boundary.

### 11.7 Repository Types

Core repositories should include:

```text
UserRepository
GroupRepository
MembershipRepository
ExpenseRepository
SettlementRepository
DeviceRepository
SyncRepository
ConflictRepository
```

Additional repositories may be introduced where required.

### 11.8 UserRepository

UserRepository should provide operations required to:

```text
Create / Store User
Read User
Update User
Find User
```

according to the application requirements.

### 11.9 GroupRepository

GroupRepository should provide:

```text
Create Group
Read Group
Update Group
List Groups
```

and related operations.

### 11.10 MembershipRepository

MembershipRepository should provide:

```text
Add Member
Remove Member
Find Membership
List Members
Check Membership
```

### 11.11 ExpenseRepository

ExpenseRepository should provide:

```text
Create Expense
Read Expense
Update Expense
Delete / Tombstone Expense
List Expenses
```

### 11.12 ExpenseSplitRepository

If ExpenseSplit is managed independently, a dedicated repository may be used.

Otherwise it may remain part of the Expense aggregate persistence.

### 11.13 SettlementRepository

SettlementRepository should provide:

```text
Create Settlement
Read Settlement
Update Settlement
List Settlements
```

### 11.14 DeviceRepository

DeviceRepository should provide:

```text
Get Device
Create Device
Update Device
Register Device
Revoke Device
```

according to the Device lifecycle.

### 11.15 SyncRepository

SyncRepository should manage:

```text
Pending SyncOperations
SyncOperation State
SyncState
Cursors
Retry Information
```

### 11.16 ConflictRepository

ConflictRepository should manage:

```text
Create Conflict
Read Conflict
List Open Conflicts
Update Conflict
Resolve Conflict
```

### 11.17 Local Repository

Android repositories must interact with the local data source.

Example:

```text
ExpenseRepository
      ↓
ExpenseDao
      ↓
Room
```

### 11.18 Remote Repository Access

Where remote data is required:

```text
ExpenseRepository
      ↓
ExpenseRemoteDataSource
      ↓
Backend API
```

### 11.19 Local-First Repository

For Offline-First operations, repositories should prioritize local persistence.

Example:

```text
Create Expense
      ↓
Domain Validation
      ↓
Local Repository
      ↓
Local Database
      ↓
SyncOperation
```

### 11.20 Read Strategy

Read operations may use:

```text
Local Database
```

as the primary source for the Android application.

Remote refresh may occur through synchronization.

### 11.21 Write Strategy

Local writes should normally follow:

```text
Domain Operation
      ↓
Local Transaction
      ↓
SyncOperation
```

### 11.22 Remote Write Strategy

Remote writes should be handled by:

```text
Synchronization Engine
```

rather than allowing arbitrary UI code to directly modify the Backend.

### 11.23 Repository and Synchronization

The Repository must integrate with synchronization without leaking transport details to the UI.

Conceptually:

```text
Repository
      ↓
Local State
      +
Pending SyncOperation
```

### 11.24 Repository and SyncOperation

When a local mutation requires synchronization:

```text
Repository
      ↓
Local Mutation
      +
SyncOperation
```

must remain consistent.

### 11.25 Atomic Local Write

The following should be atomic where required:

```text
Entity Mutation
+
SyncOperation Creation
```

### 11.26 Repository Transaction Boundary

Repository methods should use the appropriate transaction boundary for multi-record operations.

### 11.27 Aggregate Operations

Operations involving an aggregate should preferably be coordinated through a single application-level operation.

Example:

```text
Create Expense
      ↓
Expense
+
Expense Splits
+
SyncOperation
```

### 11.28 Repository Return Types

Repositories should return:

```text
Domain Models
Application Models
```

rather than exposing:

```text
Room Entity
JPA Entity
HTTP Response
```

to higher layers.

### 11.29 Persistence Mapping

The mapping flow should be:

```text
Database Entity
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
Database Entity
```

### 11.30 API Mapping

Remote API models should follow:

```text
API DTO
      ↓
Remote Mapper
      ↓
Domain Model
```

### 11.31 Repository Abstraction

The application layer should depend on interfaces where abstraction is beneficial.

Example:

```text
ExpenseRepository
```

with infrastructure implementations such as:

```text
LocalExpenseRepository
RemoteExpenseRepository
```

or a combined implementation.

### 11.32 Repository Implementation Strategy

The exact split between:

```text
Local Repository
Remote Repository
Combined Repository
```

may vary by feature.

The architectural rule is that higher layers must not depend directly on infrastructure.

### 11.33 Repository Error Mapping

Infrastructure-specific failures should be converted into application/domain-understandable failures.

Example:

```text
DatabaseException
      ↓
Repository
      ↓
PersistenceFailure
```

### 11.34 Network Error Mapping

Remote errors should be mapped into categories such as:

```text
NetworkUnavailable
Timeout
Unauthorized
Forbidden
NotFound
Conflict
ServerError
```

### 11.35 Repository and Offline State

Network failure during an offline-first local operation must not cause the local operation to fail if the Domain operation itself is valid.

Example:

```text
Create Expense
      ↓
Local Save
      ↓
Success

Backend Unavailable
      ↓
Sync Later
```

### 11.36 Repository and Connectivity

The Repository should not make business decisions based solely on connectivity status.

The application should remain local-first even if the device incorrectly reports connectivity.

### 11.37 Repository and Cache

The local database is the authoritative local cache/store.

An additional in-memory cache may be used for performance but must not become the sole source of required state.

### 11.38 Repository Cache Invalidation

If an in-memory cache is used:

```text
Database Change
      ↓
Cache Update / Invalidation
```

must remain consistent.

### 11.39 Reactive Repository

Repositories may expose reactive data streams for UI observation.

Conceptually:

```text
Room
 ↓
Repository
 ↓
Flow / Observable
 ↓
ViewModel
 ↓
UI
```

### 11.40 Repository Threading

Repositories must perform blocking operations away from the Android main thread.

### 11.41 Backend Repository

Backend repositories should provide persistence access to Application Services.

Conceptually:

```text
Application Service
      ↓
Repository
      ↓
JPA / SQL
      ↓
Database
```

### 11.42 Backend Repository Transactions

Backend Repository operations must participate in Application transaction boundaries.

### 11.43 Repository and Domain Rules

Repositories should not duplicate Domain business rules.

Incorrect:

```text
Repository
      ↓
Business Calculation
```

Preferred:

```text
Application
      ↓
Domain
      ↓
Repository
```

### 11.44 Repository Query Rules

Repositories may contain data-access-specific query logic.

Examples:

```text
Find Expenses By Group
Find Pending SyncOperations
Find Open Conflicts
```

### 11.45 Repository Query Optimization

Queries should return only the data required by the operation.

### 11.46 Pagination

Repositories should support pagination where data sets can grow large.

### 11.47 Repository and Authorization

Repositories should not be treated as the primary security boundary.

Authorization must be enforced by the Application/API layer.

### 11.48 Repository and Transactions

Repositories should not independently commit partial operations when the Application Service requires one atomic operation.

### 11.49 Transaction Propagation

Backend transaction propagation must be configured so that related Repository calls participate in the same transaction where required.

### 11.50 Repository and Idempotency

Synchronization repositories must support idempotency checks.

Example:

```text
Find SyncOperation By ID
```

before applying a duplicate operation.

### 11.51 Repository and Versioning

Repositories must support version-aware operations.

Example:

```text
Update Entity
WHERE ID = ?
AND Version = ?
```

where optimistic concurrency is used.

### 11.52 Repository and Conflict Detection

Repository operations must expose enough information to Application Services to detect version conflicts.

### 11.53 Repository and SyncState

SyncRepository must update SyncState only after successful synchronization progress.

### 11.54 Repository and Cursor

Cursor persistence must be atomic with the corresponding successfully applied changes where required.

### 11.55 Repository and P2P

P2P synchronization should ultimately use the same local persistence/repository abstractions as Backend synchronization.

Conceptually:

```text
Backend Sync
      ↓
Sync Application Layer
      ↓
Repository
```

and:

```text
P2P Sync
      ↓
Sync Application Layer
      ↓
Repository
```

### 11.56 Shared Synchronization Logic

Backend and P2P transport should not implement separate business synchronization rules.

Both should feed the same logical synchronization layer.

### 11.57 Repository Testing

Repositories require tests at multiple levels.

```text
Unit Tests
Integration Tests
Database Tests
Synchronization Tests
```

### 11.58 Local Repository Tests

Test:

```text
Create
Read
Update
Delete / Tombstone
Query
Transaction
Mapping
```

### 11.59 Backend Repository Tests

Test:

```text
Create
Read
Update
Query
Transaction
Constraints
Version Check
```

### 11.60 Sync Repository Tests

Test:

```text
Pending Operations
Status Update
Retry
Idempotency
Cursor
Conflict
```

### 11.61 Repository Mapping Tests

Verify that:

```text
Domain → Persistence
```

and:

```text
Persistence → Domain
```

preserve all required values.

### 11.62 Repository Failure Tests

Test:

```text
Database Failure
Network Failure
Timeout
Constraint Failure
Concurrency Failure
```

### 11.63 Repository Concurrency Tests

Verify that concurrent operations do not cause:

```text
Lost Update
Duplicate Operation
Corrupt State
```

### 11.64 Repository Completion Criteria

Repository Implementation is complete when:

```text
Repository Interfaces Defined
Local Repositories Implemented
Backend Repositories Implemented
Mappings Implemented
Transactions Integrated
Error Mapping Implemented
Sync Repository Implemented
Conflict Repository Implemented
Version Handling Implemented
Repository Tests Passing
```

### 11.65 Repository Invariants

The following rules are mandatory:

- Higher-level application code must not depend directly on database implementation details.
- UI code must not access Room/JPA directly.
- Repository implementations must not contain UI logic.
- Repository implementations must not expose persistence entities to higher layers.
- Domain business rules must remain in the Domain/Application layers.
- Local-first writes must persist locally before synchronization.
- Local entity mutation and required SyncOperation creation must remain consistent.
- Backend repository operations must participate in required transaction boundaries.
- Synchronization repositories must support idempotency.
- Synchronization repositories must support version-aware operations.
- Cursor updates must not advance before required changes are successfully applied.
- P2P synchronization must reuse the logical synchronization layer.
- Network failures must not unnecessarily fail valid offline operations.
- Database failures must be mapped into appropriate application-level errors.
- Repository queries must be optimized for expected data volume.
- Large collections must support pagination where required.
- Repository mappings must preserve financial and synchronization metadata.
- Repository concurrency behavior must be tested.
- Repository implementations must remain aligned with the Domain and Technical Design.

## 12. Application Service Implementation

### 12.1 Purpose

This section defines the implementation of Application Services in SplitSync V1.

Application Services coordinate complete use cases between:

```text
API / UI
   ↓
Application Service
   ↓
Domain
   ↓
Repository
```

They are responsible for orchestration, transaction coordination, authorization context, and interaction between Domain and persistence boundaries.

### 12.2 Application Service Principle

Application Services should coordinate business use cases without becoming the location for core Domain rules.

Preferred:

```text
Application Service
      ↓
Domain Operation
      ↓
Repository
```

Not:

```text
Application Service
      ↓
Large Business Logic
```

### 12.3 Application Service Responsibilities

Application Services may handle:

```text
Use Case Orchestration
Authorization Context
Transaction Coordination
Repository Coordination
Domain Service Invocation
Error Translation
Synchronization Triggering
```

### 12.4 Application Service Independence

Application Services should not depend directly on:

```text
Android UI
HTTP Controllers
Room DAO
JPA Entity
P2P Transport
```

### 12.5 Application Service Structure

Conceptually:

```text
application/
├── service/
├── command/
├── query/
├── result/
└── exception/
```

The exact package structure should follow the Backend and Android package architecture.

### 12.6 Command Services

Commands represent operations that modify state.

Examples:

```text
CreateGroup
AddMember
CreateExpense
UpdateExpense
CreateSettlement
ResolveConflict
```

### 12.7 Query Services

Queries retrieve state without modifying it.

Examples:

```text
GetGroup
GetExpense
GetBalance
ListExpenses
ListSettlements
GetSyncState
GetConflicts
```

### 12.8 Command / Query Separation

Where useful, command and query responsibilities should remain clearly separated.

```text
Command
   ↓
Mutation

Query
   ↓
Read
```

### 12.9 Create Group Service

The Create Group use case should follow:

```text
Request
   ↓
Validate Input
   ↓
Create Group
   ↓
Persist Group
   ↓
Create Initial State
   ↓
Return Result
```

### 12.10 Add Member Service

The Add Member use case should:

```text
Validate User
      ↓
Validate Group
      ↓
Validate Membership
      ↓
Create Membership
      ↓
Persist
      ↓
Create SyncOperation
```

where synchronization is required.

### 12.11 Remove Member Service

The Remove Member use case should:

```text
Validate Authorization
      ↓
Validate Membership
      ↓
Validate Group Rules
      ↓
Update Membership State
      ↓
Create SyncOperation
```

### 12.12 Create Expense Service

The Create Expense use case should follow:

```text
Request
   ↓
Validate User
   ↓
Validate Group Membership
   ↓
Create Money
   ↓
Create Expense
   ↓
Validate Splits
   ↓
Create Expense Splits
   ↓
Persist Atomically
   ↓
Create SyncOperation
   ↓
Return Expense
```

### 12.13 Update Expense Service

The Update Expense use case should:

```text
Load Existing Expense
      ↓
Validate Authorization
      ↓
Validate Expected Version
      ↓
Apply Domain Mutation
      ↓
Validate Splits
      ↓
Persist Atomically
      ↓
Create SyncOperation
      ↓
Return Updated Expense
```

### 12.14 Delete Expense Service

If Expense deletion is supported:

```text
Load Expense
      ↓
Validate Authorization
      ↓
Apply Delete / Tombstone
      ↓
Create SyncOperation
      ↓
Commit
```

Physical deletion must follow the synchronization strategy.

### 12.15 Create Settlement Service

The Create Settlement use case should:

```text
Validate Group
      ↓
Validate Payer
      ↓
Validate Receiver
      ↓
Validate Membership
      ↓
Validate Amount
      ↓
Create Settlement
      ↓
Persist
      ↓
Create SyncOperation
```

### 12.16 Balance Query Service

Balance calculation should follow:

```text
Load Group State
      ↓
Load Expenses
      ↓
Load Settlements
      ↓
Apply Domain Balance Calculation
      ↓
Return Balances
```

### 12.17 Sync Service

The synchronization Application Service should coordinate:

```text
Outgoing Operations
Incoming Operations
Version Checks
Conflict Detection
Conflict Resolution
SyncState
```

### 12.18 Conflict Resolution Service

Conflict resolution should:

```text
Load Conflict
      ↓
Validate Resolution
      ↓
Apply Domain Resolution
      ↓
Persist Result
      ↓
Update Conflict State
      ↓
Create Required SyncOperation
```

### 12.19 Device Service

Device-related operations may include:

```text
Register Device
Get Device
Update Device
Revoke Device
```

### 12.20 Authentication Service

Authentication orchestration should remain separate from business Domain operations.

It may coordinate:

```text
Credentials / Identity
      ↓
Authentication Provider
      ↓
Authenticated User
      ↓
Session / Token
```

### 12.21 Authorization Service

Authorization should verify:

```text
User
+
Resource
+
Action
```

before allowing protected operations.

### 12.22 Authorization Context

Application Services should receive the authenticated context required to determine:

```text
User ID
Device ID
Roles / Permissions
```

where applicable.

### 12.23 Authorization Before Mutation

Protected mutations should generally follow:

```text
Authenticate
      ↓
Authorize
      ↓
Validate Domain Operation
      ↓
Persist
```

### 12.24 Domain Validation Responsibility

Application Services should invoke Domain validation rather than duplicating Domain rules.

### 12.25 Repository Coordination

An Application Service may coordinate multiple repositories.

Example:

```text
ExpenseRepository
      +
ExpenseSplitRepository
      +
SyncRepository
```

### 12.26 Transaction Boundary

The Application Service should define the logical transaction boundary for a use case.

Example:

```text
Create Expense
      ↓
Transaction
 ┌────┼─────────────┐
 ↓    ↓             ↓
Expense Splits  SyncOperation
 └────┼─────────────┘
      ↓
    Commit
```

### 12.27 Transaction Failure

If any required operation fails:

```text
Rollback
```

must prevent partial state.

### 12.28 Application Service Return

Application Services should return:

```text
Domain Result
Application Result
```

rather than infrastructure-specific objects.

### 12.29 Result Models

Result models may represent:

```text
Created Entity
Updated Entity
Balance
Sync Result
Conflict Result
```

### 12.30 Error Handling

Application Services should convert lower-level failures into meaningful application-level failures.

### 12.31 Application Exception Categories

Examples:

```text
ValidationFailure
AuthorizationFailure
NotFound
Conflict
PersistenceFailure
SynchronizationFailure
```

### 12.32 Idempotent Commands

Operations that may be retried must support idempotency where required.

This is especially important for:

```text
Synchronization
P2P
Network Retry
```

### 12.33 Command Idempotency

Where a command carries an operation ID:

```text
Operation ID
      ↓
Check Existing Operation
      ↓
Already Applied?
      ↓
Return Existing Result
```

### 12.34 Query Idempotency

Queries should not modify Domain state unless explicitly designed as a read-through/cache operation.

### 12.35 Offline Application Service

Android local use cases should not require Backend availability for supported offline operations.

Example:

```text
Create Expense
      ↓
Domain
      ↓
Local Repository
      ↓
SyncOperation
```

### 12.36 Online Application Service

Online operations may additionally trigger:

```text
Remote Synchronization
```

but local persistence should remain consistent with the Offline-First architecture.

### 12.37 P2P Application Service

P2P synchronization should enter the same Application synchronization flow as Backend synchronization.

```text
P2P Message
      ↓
Sync Application Service
      ↓
Domain
      ↓
Repository
```

### 12.38 Application Service Testing

Every Application Service should have tests covering:

```text
Success
Validation Failure
Authorization Failure
Not Found
Conflict
Persistence Failure
Retry / Idempotency
```

### 12.39 Mocking

Application Service Unit Tests may mock Repository interfaces and Domain Services where appropriate.

### 12.40 Integration Testing

Important Application Services should also have Integration Tests using real persistence.

### 12.41 Expense Service Test

Create Expense tests should verify:

```text
Expense Created
Splits Created
Totals Valid
SyncOperation Created
Transaction Atomicity
```

### 12.42 Settlement Service Test

Settlement tests should verify:

```text
Settlement Created
Participants Valid
Amount Valid
SyncOperation Created
```

### 12.43 Membership Service Test

Membership tests should verify:

```text
Authorization
Membership State
Duplicate Prevention
Synchronization
```

### 12.44 Sync Service Test

Synchronization tests should verify:

```text
Incoming Operation
Outgoing Operation
Idempotency
Version Handling
Conflict Detection
SyncState
```

### 12.45 Application Service Dependency Direction

The dependency direction should remain:

```text
API / UI
   ↓
Application
   ↓
Domain
   ↓
Repository Abstraction
   ↓
Infrastructure
```

### 12.46 Application Service Completion Criteria

Application Service Implementation is complete when:

```text
Core Use Cases Implemented
Command Services Implemented
Query Services Implemented
Authorization Integrated
Domain Validation Integrated
Repository Integration Complete
Transaction Boundaries Defined
Error Handling Implemented
Idempotency Implemented
Synchronization Integration Implemented
Unit Tests Passing
Integration Tests Passing
```

### 12.47 Application Service Invariants

The following rules are mandatory:

- Application Services must orchestrate use cases.
- Core business rules must remain in the Domain.
- Application Services must not depend on UI.
- Application Services must not directly depend on database implementation details.
- Protected operations must perform authorization.
- Domain validation must be executed for all mutations.
- Multi-record operations must use appropriate transaction boundaries.
- Partial financial state must not be committed.
- Local mutations and required SyncOperations must remain consistent.
- Retryable operations must support idempotency where required.
- Backend and P2P synchronization must use the same logical synchronization rules.
- Infrastructure-specific exceptions must not leak unnecessarily into higher layers.
- Application Services must be independently testable.
- Critical use cases must have both Unit and Integration Tests.


## 13. Expense Management Implementation

### 13.1 Purpose

This section defines the implementation of Expense management in SplitSync V1.

Expense Management covers:

```text
Expense Creation
Expense Reading
Expense Updating
Expense Deletion / Tombstone
Expense Splits
Expense Validation
Expense Persistence
Expense Synchronization
```

### 13.2 Expense Aggregate

The Expense aggregate should conceptually contain:

```text
Expense
   └── ExpenseSplits
```

The aggregate must preserve the financial invariant:

```text
Expense Amount
=
Sum(Expense Splits)
```

### 13.3 Expense Creation Flow

The complete creation flow is:

```text
User Input
    ↓
Create Expense Command
    ↓
Authorization
    ↓
Domain Validation
    ↓
Expense Creation
    ↓
Split Validation
    ↓
Local / Backend Persistence
    ↓
SyncOperation
    ↓
Commit
```

### 13.4 Expense Creation Input

The Create Expense operation should provide:

```text
Group ID
Creator User ID
Amount
Currency
Description
Expense Splits
```

plus required metadata.

### 13.5 Expense ID Generation

Expense ID must be generated using the project identifier strategy.

It must support:

```text
Offline Creation
Uniqueness
Synchronization
```

### 13.6 Expense Creator

The creator must be a valid User participating in the Group according to the Membership rules.

### 13.7 Expense Group

The Expense must belong to an existing valid Group.

### 13.8 Expense Membership

Users involved in the Expense must satisfy the Group Membership rules.

### 13.9 Expense Amount

Expense amount must be represented using the Domain Money type.

It must satisfy:

```text
Amount > 0
```

where required by V1.

### 13.10 Expense Currency

Currency must be explicitly represented and validated.

### 13.11 Expense Description

Description should follow the defined validation rules for:

```text
Optional / Required
Length
Whitespace
Encoding
```

### 13.12 Expense Timestamp

The Expense must receive a valid creation timestamp.

The timestamp strategy must remain compatible with synchronization.

### 13.13 Expense Version

A newly created Expense receives its initial version according to the synchronization model.

### 13.14 Expense State

Expense lifecycle state should follow the defined Domain model.

If deletion is supported through tombstones:

```text
ACTIVE
   ↓
DELETED
```

may be represented.

### 13.15 Expense Splits

Every Expense must contain the required ExpenseSplit records.

Each split represents:

```text
User
+
Share
```

### 13.16 Equal Split

For equal splitting:

```text
Expense Total
      ↓
Number Of Participants
      ↓
Equal Allocation
      ↓
Rounding
```

### 13.17 Custom Split

For custom splitting:

```text
User 1 → Amount
User 2 → Amount
User 3 → Amount
```

must satisfy:

```text
Sum(Splits)
=
Expense Total
```

### 13.18 Split Validation

Before persistence:

```text
All Users Valid
All Amounts Valid
No Invalid Duplicate
Total Matches Expense
```

must be verified.

### 13.19 Split Rounding

Rounding must be deterministic.

If the amount cannot be divided evenly, the defined rounding strategy must distribute the remainder consistently.

### 13.20 Split Currency

All splits must use the Expense's currency unless the V1 Domain explicitly supports another model.

### 13.21 Expense Persistence

Expense and its splits must be persisted atomically.

```text
Transaction
├── Expense
└── Expense Splits
```

### 13.22 Expense SyncOperation

When an Expense is created locally, the corresponding synchronization operation must be created in the same transaction.

```text
Expense
+
ExpenseSplits
+
SyncOperation
```

### 13.23 Offline Expense Creation

Offline creation must work without Backend availability.

```text
Offline
  ↓
Create Expense
  ↓
Validate
  ↓
Persist Locally
  ↓
Create SyncOperation
  ↓
UI Updated
```

### 13.24 Online Expense Creation

Online availability must not fundamentally change the Domain behavior.

The application should still preserve local consistency before remote synchronization.

### 13.25 Expense Read

Expense retrieval should support:

```text
By Expense ID
By Group
By Date
```

where required.

### 13.26 Expense List

Group Expense lists should be retrieved efficiently.

Large lists should support pagination/incremental loading.

### 13.27 Expense Update

Expense updates must:

```text
Load Current Expense
      ↓
Authorize
      ↓
Validate Version
      ↓
Apply Mutation
      ↓
Validate Splits
      ↓
Persist
      ↓
Create SyncOperation
```

### 13.28 Expense Version Check

When optimistic concurrency is used:

```text
Expected Version
      ↓
Current Version
      ↓
Match?
```

If versions do not match:

```text
Conflict
```

must be detected according to the synchronization model.

### 13.29 Expense Update Atomicity

Updating Expense data and its ExpenseSplits must remain atomic.

### 13.30 Expense Update Example

Conceptually:

```text
Expense V3
   ↓
Update
   ↓
Expense V4
+
Updated Splits
+
SyncOperation
```

### 13.31 Expense Delete

If Expense deletion is supported, it should use the defined deletion strategy.

For synchronization-safe deletion:

```text
Expense
   ↓
Tombstone
   ↓
SyncOperation
```

### 13.32 Expense Physical Deletion

Physical deletion must not occur while synchronization requires the record.

### 13.33 Expense Authorization

Expense operations must verify that the requesting User is authorized.

Authorization may depend on:

```text
Group Membership
Expense Ownership
Group Role
```

according to the Authorization Model.

### 13.34 Expense Creation Authorization

Before creation:

```text
Authenticated User
      ↓
Group Membership
      ↓
Permission
```

must be verified.

### 13.35 Expense Update Authorization

Before update:

```text
Authenticated User
      ↓
Expense Group
      ↓
Membership / Permission
      ↓
Allowed?
```

### 13.36 Expense Delete Authorization

Deletion must require the appropriate permission according to the V1 authorization rules.

### 13.37 Expense Synchronization

Expense changes must be represented as SyncOperations.

Example:

```text
CREATE
UPDATE
DELETE
```

depending on the supported operation model.

### 13.38 Incoming Expense Synchronization

Incoming Expense operations should follow:

```text
Receive Operation
      ↓
Authenticate
      ↓
Authorize
      ↓
Validate Operation
      ↓
Validate Domain State
      ↓
Check Version
      ↓
Apply / Detect Conflict
      ↓
Persist
```

### 13.39 Duplicate Expense Operation

If the same operation is received again:

```text
Operation ID
      ↓
Already Applied?
      ↓
No Duplicate Business Effect
```

### 13.40 Expense Conflict

If concurrent Expense modifications occur:

```text
Local Version
      +
Remote Version
      ↓
Conflict Detection
```

must follow the Conflict Resolution architecture.

### 13.41 Expense Conflict Persistence

Detected conflicts must be persisted for later inspection/resolution when required.

### 13.42 Expense Balance Impact

Creating or updating an Expense must affect Group balances according to the Balance Calculation model.

### 13.43 Settlement Interaction

Expense changes must not directly mutate Settlement records unless explicitly required by the Domain model.

Balances should be derived from:

```text
Expenses
+
Settlements
```

rather than duplicated inconsistently.

### 13.44 Balance Recalculation

When Expense state changes:

```text
Expense Change
      ↓
Balance Query
      ↓
Recalculate / Recompute
```

according to the selected implementation.

### 13.45 Expense Repository

ExpenseRepository should provide:

```text
Create
Find
List
Update
Delete / Tombstone
```

operations.

### 13.46 Expense DAO

The local Expense DAO should provide persistence-specific queries.

### 13.47 Backend Expense Repository

The Backend Expense Repository should provide:

```text
Find Expense
Persist Expense
Update Expense
Find By Group
```

and synchronization-related queries where required.

### 13.48 Expense Mapping

Mapping must preserve:

```text
ID
Group ID
Creator
Amount
Currency
Description
Timestamp
Version
State
Splits
```

### 13.49 Expense DTO

API DTOs should be separate from Domain and persistence models.

Example:

```text
CreateExpenseRequest
UpdateExpenseRequest
ExpenseResponse
```

### 13.50 Create Expense API Flow

Conceptually:

```text
HTTP Request
      ↓
Controller
      ↓
Request DTO
      ↓
Application Service
      ↓
Domain
      ↓
Repository
      ↓
Response DTO
```

### 13.51 Expense API Validation

Malformed requests should be rejected at the API boundary.

Business validation must still execute in the Domain.

### 13.52 Expense Error Handling

Expense operations should expose meaningful errors such as:

```text
Invalid Expense
Group Not Found
User Not Member
Unauthorized
Version Conflict
Invalid Split
```

### 13.53 Expense Error Consistency

The same logical error should map consistently across:

```text
Android
Backend API
Synchronization
P2P
```

where applicable.

### 13.54 Expense Offline Queue

Offline Expense mutations should create pending SyncOperations.

Conceptually:

```text
Expense Mutation
      ↓
SyncOperation = PENDING
```

### 13.55 Expense Sync Retry

Failed Expense synchronization should remain retryable when the error is transient.

### 13.56 Permanent Expense Failure

If an Expense SyncOperation cannot be applied permanently:

```text
SyncOperation
      ↓
Permanent Failure
      ↓
Conflict / Error State
```

according to the synchronization strategy.

### 13.57 Expense Recovery

The system must preserve enough information to recover from:

```text
Network Failure
Application Restart
Backend Failure
P2P Interruption
```

### 13.58 Expense Testing

Expense Management must include:

```text
Domain Tests
Application Service Tests
Repository Tests
Database Tests
API Tests
Synchronization Tests
```

### 13.59 Expense Creation Tests

Test:

```text
Valid Expense
Invalid Amount
Invalid Currency
Invalid Group
Unauthorized User
Invalid Split
Split Total Mismatch
Duplicate Split
```

### 13.60 Expense Update Tests

Test:

```text
Valid Update
Unauthorized Update
Stale Version
Invalid Amount
Invalid Split
Concurrent Update
```

### 13.61 Expense Delete Tests

Test:

```text
Valid Delete
Unauthorized Delete
Repeated Delete
Synchronization
Tombstone Handling
```

### 13.62 Expense Offline Tests

Test:

```text
Offline Create
Offline Update
Application Restart
Pending Sync
Online Recovery
```

### 13.63 Expense Synchronization Tests

Test:

```text
Create Sync
Update Sync
Delete Sync
Duplicate Operation
Version Conflict
Retry
Recovery
```

### 13.64 Expense P2P Tests

Test:

```text
Expense Transfer
Duplicate Message
Interrupted Transfer
Version Conflict
Reconnection
```

### 13.65 Expense Transaction Tests

Verify:

```text
Expense
+
Splits
+
SyncOperation
```

are committed together.

Failure in any required part must roll back the transaction.

### 13.66 Expense Performance

Expense operations should remain efficient for expected V1 group sizes.

Large Expense histories should not require loading the entire Group into memory.

### 13.67 Expense Pagination

Expense history should support pagination/incremental loading where required.

### 13.68 Expense Query Optimization

Indexes should support:

```text
Group ID
Expense ID
Creator
Timestamp
Version
```

according to actual query patterns.

### 13.69 Expense Auditability

The implementation should preserve enough metadata to support synchronization and troubleshooting.

Examples:

```text
Created At
Updated At
Version
Device ID
SyncOperation ID
```

where required by the model.

### 13.70 Expense Logging

Logs should contain operational information necessary for debugging without exposing sensitive financial or personal information unnecessarily.

### 13.71 Expense Security

Expense data must be protected according to:

```text
Authentication
Authorization
Database Security
Transport Security
Local Storage Security
```

### 13.72 Expense Implementation Completion Criteria

Expense Management Implementation is complete when:

```text
Expense Domain Model Implemented
Expense Creation Implemented
Expense Update Implemented
Expense Delete / Tombstone Implemented
Expense Splits Implemented
Validation Implemented
Authorization Implemented
Local Persistence Implemented
Backend Persistence Implemented
API Implemented
Offline Creation Implemented
SyncOperation Integration Implemented
Conflict Handling Integrated
P2P Synchronization Integrated
Tests Passing
```

### 13.73 Expense Management Invariants

The following rules are mandatory:

- Every Expense must belong to a valid Group.
- The Expense creator must satisfy Group membership rules.
- Expense amounts must use precise monetary representation.
- Expense amounts must satisfy the defined positive amount rule.
- Currency must be explicitly represented and validated.
- Every Expense must have valid ExpenseSplits according to the Domain model.
- The sum of ExpenseSplits must equal the Expense amount.
- ExpenseSplit users must satisfy Group membership rules.
- Invalid duplicate ExpenseSplits must be rejected.
- Expense creation must be atomic.
- Expense and ExpenseSplits must remain consistent.
- Required Expense mutations must create corresponding SyncOperations.
- Expense mutation and SyncOperation creation must remain atomic.
- Offline Expense creation must not require Backend availability.
- Expense synchronization must be idempotent.
- Stale Expense updates must not silently overwrite newer state.
- Expense conflicts must follow the Conflict Resolution architecture.
- Expense deletion must respect synchronization requirements.
- Physical deletion must not remove data required by synchronization.
- Expense changes must affect balance calculations consistently.
- Settlement records must not be mutated implicitly unless explicitly required by the Domain model.
- Expense API DTOs must remain separate from persistence entities.
- Expense operations must enforce authorization.
- Expense operations must be covered by automated tests.
- Expense behavior must remain consistent across Online, Offline, Backend Sync, and P2P Sync.

## 14. Expense Split Implementation

### 14.1 Purpose

This section defines the implementation of Expense Splits in SplitSync V1.

Expense Splits determine how an Expense amount is distributed among participating Users.

The implementation must guarantee:

```text
Expense Amount
      =
Sum of All Expense Splits
```

### 14.2 Expense Split Model

An ExpenseSplit represents:

```text
Expense
+
User
+
Share Amount
```

Conceptually:

```text
Expense
├── Expense Amount
├── Currency
└── Expense Splits
    ├── User A → Amount
    ├── User B → Amount
    └── User C → Amount
```

### 14.3 Expense Split Aggregate Relationship

ExpenseSplits belong to the Expense aggregate.

They should not be modified independently in a way that can violate the Expense total.

### 14.4 Expense Split Identity

The identity strategy must be consistent with the Domain Model.

An ExpenseSplit may use:

```text
ExpenseSplit ID
```

or a defined composite identity such as:

```text
Expense ID
+
User ID
```

The selected strategy must remain consistent across:

```text
Domain
Database
API
Synchronization
```

### 14.5 Split Creation

Split creation should follow:

```text
Expense
      ↓
Participants
      ↓
Split Calculation
      ↓
Split Validation
      ↓
ExpenseSplit Objects
```

### 14.6 Supported Split Types

V1 should support the split types defined by the Domain requirements.

At minimum, the implementation should be prepared for:

```text
Equal Split
Custom Amount Split
```

Additional split strategies must not be introduced without updating the Domain and Technical Design.

### 14.7 Equal Split

Equal splitting divides the Expense amount among the selected Users.

Conceptually:

```text
Expense Amount
      ÷
Number of Participants
      ↓
Base Share
```

### 14.8 Equal Split Rounding

When the Expense cannot be divided exactly:

```text
Expense Amount
      ↓
Base Share
      +
Remainder
      ↓
Deterministic Distribution
```

must produce a final result where:

```text
Sum(Splits) = Expense Amount
```

### 14.9 Custom Split

Custom splitting allows each User to receive a specific share.

Example:

```text
User A → ₹400
User B → ₹350
User C → ₹250
```

For an Expense of:

```text
₹1000
```

the validation result must be:

```text
400 + 350 + 250 = 1000
```

### 14.10 Custom Split Validation

Custom splits must reject:

```text
Sum(Splits) ≠ Expense Amount
```

### 14.11 Split Amount Validation

Each split amount must follow the Domain monetary rules.

Where positive shares are required:

```text
Split Amount > 0
```

### 14.12 Zero Split

Zero-value ExpenseSplits should be rejected unless explicitly supported by the Domain rules.

### 14.13 Negative Split

Negative ExpenseSplit amounts must not be accepted.

### 14.14 Duplicate User Split

The same User must not appear more than once within the same ExpenseSplit collection unless explicitly supported.

Incorrect:

```text
User A → ₹300
User A → ₹200
```

Preferred:

```text
User A → ₹500
```

### 14.15 Participant Validation

Every User included in an ExpenseSplit must satisfy the Group membership rules.

```text
Expense
   ↓
Group
   ↓
User Membership
   ↓
Valid Participant
```

### 14.16 Removed Member

A User who is no longer eligible to participate must not be added to a new Expense unless the Domain explicitly permits historical membership behavior.

Existing historical ExpenseSplits must remain valid for reporting and synchronization.

### 14.17 Currency Validation

All ExpenseSplits must use the Expense currency unless multi-currency support is explicitly introduced.

### 14.18 Money Precision

Split calculations must use the same precise monetary representation as the Expense.

Avoid:

```text
float
double
```

for authoritative calculations.

### 14.19 Split Calculation Service

A dedicated Domain/Application service may be used for split calculation.

Conceptually:

```text
ExpenseSplitCalculator
```

### 14.20 Split Calculator Input

The calculator should receive:

```text
Expense Amount
Currency
Participants
Split Type
Custom Values
```

where applicable.

### 14.21 Split Calculator Output

The calculator should return:

```text
Validated ExpenseSplits
```

or a validation failure.

### 14.22 Split Calculator Determinism

The same inputs must always produce the same split result.

This is especially important for:

```text
Offline Devices
Backend
P2P Devices
```

### 14.23 Split Remainder Strategy

The remainder created during equal division must be distributed using a deterministic rule.

The rule must be documented and shared across all implementations.

### 14.24 Cross-Platform Calculation

Android and Backend must not produce different results for the same split input.

```text
Same Input
    ↓
Android Result
    =
Backend Result
```

### 14.25 Split Validation Location

Split validation must exist in the Domain.

UI validation may provide early feedback but must not be the authoritative validation layer.

### 14.26 Split Persistence

ExpenseSplits must be persisted atomically with their Expense.

```text
Transaction
├── Expense
└── ExpenseSplits
```

### 14.27 Split Update

Updating ExpenseSplits should follow:

```text
Load Expense
      ↓
Validate Authorization
      ↓
Validate Version
      ↓
Calculate / Validate Splits
      ↓
Replace / Update Splits
      ↓
Persist Atomically
      ↓
Create SyncOperation
```

### 14.28 Split Replacement

If the V1 update strategy replaces the complete split collection:

```text
Existing Splits
      ↓
Validate New Splits
      ↓
Replace
```

must occur inside one transaction.

### 14.29 Partial Split Update

Partial updates should only be supported if explicitly defined by the Domain/API model.

Avoid partial modifications that can temporarily violate:

```text
Sum(Splits) = Expense Amount
```

### 14.30 Split Deletion

An individual split should generally not be independently deleted if that would leave the Expense invalid.

Instead:

```text
Expense Update
      ↓
Recalculate Splits
      ↓
Persist Valid Collection
```

### 14.31 Expense Split Transaction

The complete Expense mutation must remain atomic:

```text
Expense
+
Splits
+
SyncOperation
```

### 14.32 Split Synchronization

ExpenseSplit changes should normally be synchronized as part of the Expense aggregate operation.

### 14.33 Split Sync Payload

The synchronization payload should contain enough information to reconstruct the required ExpenseSplit state.

### 14.34 Incoming Split Synchronization

Incoming synchronization should follow:

```text
Receive Operation
      ↓
Validate Operation
      ↓
Validate Expense
      ↓
Validate Splits
      ↓
Check Version
      ↓
Apply / Conflict
      ↓
Persist
```

### 14.35 Duplicate Split Operation

Duplicate synchronization operations must not create duplicate ExpenseSplits.

### 14.36 Split Conflict

If two devices modify the same Expense's splits concurrently:

```text
Local Expense Version
        +
Remote Expense Version
        ↓
Conflict Detection
```

must follow the Conflict Resolution architecture.

### 14.37 Split Conflict Resolution

Conflict resolution must produce a complete valid Expense state.

A resolution must never produce:

```text
Sum(Splits) ≠ Expense Amount
```

### 14.38 Split and Balance

Changing ExpenseSplits changes the distribution of the Expense among Users.

Therefore:

```text
Expense Split Change
      ↓
Balance Calculation
```

must reflect the updated shares.

### 14.39 Split and Settlement

ExpenseSplit changes must not automatically modify Settlement records unless explicitly defined by the Domain.

Settlements represent separate financial events.

### 14.40 Split Repository

ExpenseSplits may be persisted through:

```text
ExpenseRepository
```

as part of the Expense aggregate, or through a dedicated:

```text
ExpenseSplitRepository
```

if the implementation requires it.

The aggregate boundary must remain consistent.

### 14.41 Split DAO

The local DAO should provide efficient operations for:

```text
Find Splits By Expense
Insert Splits
Delete / Replace Splits
```

where required.

### 14.42 Split Mapping

Mapping must preserve:

```text
Expense ID
User ID
Amount
Currency
Identity
Version / State
```

where applicable.

### 14.43 Split API Model

API models should distinguish:

```text
CreateExpenseSplitRequest
ExpenseSplitResponse
```

from persistence entities.

### 14.44 Split API Request

A Create/Update Expense request may contain:

```text
Split Type
Participants
Custom Amounts
```

according to the API contract.

### 14.45 Split API Response

The response should expose the validated resulting splits rather than relying on the client to reconstruct them.

### 14.46 Split Error Handling

Possible errors include:

```text
INVALID_SPLIT
INVALID_SPLIT_AMOUNT
SPLIT_TOTAL_MISMATCH
DUPLICATE_SPLIT_USER
INVALID_PARTICIPANT
UNSUPPORTED_SPLIT_TYPE
```

### 14.47 Offline Split Calculation

Split calculation must work fully offline.

```text
Offline
  ↓
Calculate Splits
  ↓
Validate
  ↓
Persist
```

### 14.48 Offline Split Consistency

An Expense created offline must contain valid splits before being stored locally.

### 14.49 P2P Split Consistency

P2P synchronization must validate incoming ExpenseSplits using the same Domain rules.

### 14.50 Backend Split Consistency

Backend synchronization must validate incoming ExpenseSplits even if they were already validated by Android.

### 14.51 Split Testing

Expense Split implementation must include:

```text
Domain Tests
Application Tests
Repository Tests
Database Tests
API Tests
Synchronization Tests
```

### 14.52 Equal Split Tests

Test:

```text
Even Division
Uneven Division
One Participant
Multiple Participants
Large Amount
Smallest Supported Amount
```

### 14.53 Custom Split Tests

Test:

```text
Exact Total
Total Too High
Total Too Low
Zero Amount
Negative Amount
Duplicate User
Invalid Participant
```

### 14.54 Rounding Tests

Test cases where:

```text
Amount % Participants ≠ 0
```

and verify:

```text
Sum(Splits) = Expense Amount
```

### 14.55 Cross-Implementation Tests

The same test vectors should be used to verify:

```text
Android
Backend
```

produce identical split results.

### 14.56 Synchronization Tests

Test:

```text
Create Expense + Splits
Update Expense + Splits
Duplicate Operation
Concurrent Split Update
Conflict
Retry
```

### 14.57 Transaction Tests

Verify that a failure during split persistence causes the entire Expense transaction to roll back.

### 14.58 Split Performance

Split calculation should remain efficient for expected V1 participant counts.

### 14.59 Split Implementation Completion Criteria

Expense Split Implementation is complete when:

```text
Split Model Implemented
Equal Split Implemented
Custom Split Implemented
Rounding Implemented
Validation Implemented
Persistence Implemented
Transaction Handling Implemented
Synchronization Integrated
Conflict Handling Integrated
API Mapping Implemented
Offline Calculation Works
P2P Validation Works
Tests Passing
```

### 14.60 Expense Split Invariants

The following rules are mandatory:

- Every ExpenseSplit must belong to a valid Expense.
- Every ExpenseSplit User must satisfy Group membership rules.
- Split amounts must use precise monetary representation.
- Negative split amounts must not be accepted.
- Zero split amounts must follow the defined Domain rule.
- Duplicate active User splits must not be accepted.
- All splits must use the Expense currency unless explicitly supported otherwise.
- The sum of all ExpenseSplits must equal the Expense amount.
- Rounding must be deterministic.
- Equal split results must be consistent across Android and Backend.
- Split validation must exist in the Domain.
- UI validation must not be the authoritative validation layer.
- Expense and ExpenseSplits must be persisted atomically.
- Partial split updates must not leave an invalid Expense.
- Split changes must synchronize with the corresponding Expense state.
- Duplicate synchronization operations must not duplicate splits.
- Conflict resolution must produce a financially valid Expense.
- Split changes must correctly affect balance calculations.
- Split changes must not implicitly modify Settlement records.
- Offline split calculation must work without Backend availability.
- P2P and Backend synchronization must apply the same split validation rules.
- Critical split rules must have automated tests.


## 15. Settlement Implementation

### 15.1 Purpose

This section defines the implementation of Settlements in SplitSync V1.

A Settlement represents a financial transfer between two Users that reduces outstanding balances within a Group.

Conceptually:

```text
Payer
  ↓
Settlement
  ↓
Receiver
```

### 15.2 Settlement Model

A Settlement should contain:

```text
Settlement ID
Group ID
Payer
Receiver
Amount
Currency
Created At
Updated At
Version
State
```

according to the final Domain Model.

### 15.3 Settlement Aggregate

A Settlement is an independent financial event associated with a Group.

It should not be treated as an Expense.

```text
Expense
    ≠
Settlement
```

### 15.4 Settlement Identity

Every Settlement must have a stable:

```text
SettlementId
```

### 15.5 Offline Settlement ID

Settlement IDs must support:

```text
Offline Creation
Global Uniqueness
Synchronization
Idempotency
```

### 15.6 Settlement Creation Flow

The creation flow is:

```text
User Input
      ↓
Create Settlement Command
      ↓
Authentication
      ↓
Authorization
      ↓
Membership Validation
      ↓
Domain Validation
      ↓
Persist Settlement
      ↓
Create SyncOperation
      ↓
Commit
```

### 15.7 Settlement Input

A Create Settlement operation should contain:

```text
Group ID
Payer User ID
Receiver User ID
Amount
Currency
```

plus required metadata.

### 15.8 Settlement Group

The Settlement must belong to a valid Group.

### 15.9 Settlement Payer

The Payer must be a valid User associated with the Group according to the Membership rules.

### 15.10 Settlement Receiver

The Receiver must be a valid User associated with the Group according to the Membership rules.

### 15.11 Payer and Receiver

The following must be rejected when V1 requires two distinct Users:

```text
Payer == Receiver
```

### 15.12 Settlement Amount

Settlement amount must use the Domain Money type.

Where positive settlements are required:

```text
Amount > 0
```

### 15.13 Settlement Currency

Currency must be explicitly represented and validated.

### 15.14 Settlement Timestamp

The Settlement must receive a valid timestamp.

The timestamp must remain compatible with synchronization.

### 15.15 Settlement Version

A newly created Settlement receives its initial version according to the synchronization model.

### 15.16 Settlement State

Settlement state must follow the Domain lifecycle.

If deletion is supported through tombstones:

```text
ACTIVE
   ↓
DELETED
```

may be represented.

### 15.17 Settlement Authorization

The authenticated User must have permission to create a Settlement within the Group.

### 15.18 Settlement Creation Authorization

The flow should be:

```text
Authenticated User
      ↓
Group Membership
      ↓
Required Permission
      ↓
Create Settlement
```

### 15.19 Settlement Update Authorization

If Settlement updates are supported, the same authorization model must apply.

### 15.20 Settlement Delete Authorization

Settlement deletion must require the appropriate permission according to the Authorization Model.

### 15.21 Settlement Persistence

Settlement persistence must be atomic with its required synchronization metadata.

```text
Transaction
├── Settlement
└── SyncOperation
```

### 15.22 Settlement Repository

SettlementRepository should provide:

```text
Create
Find
List
Update
Delete / Tombstone
```

as required.

### 15.23 Settlement DAO

The local DAO should support:

```text
Find Settlement
Find By Group
Find By User
Insert
Update
Delete / Tombstone
```

where required.

### 15.24 Backend Settlement Repository

The Backend Repository should support:

```text
Find Settlement
Find By Group
Persist Settlement
Update Settlement
```

and synchronization-related queries.

### 15.25 Settlement Mapping

Mapping must preserve:

```text
Settlement ID
Group ID
Payer
Receiver
Amount
Currency
Timestamp
Version
State
```

### 15.26 Settlement API Model

API models should remain separate from Domain and persistence entities.

Example:

```text
CreateSettlementRequest
UpdateSettlementRequest
SettlementResponse
```

### 15.27 Settlement Creation API

Conceptually:

```text
HTTP Request
      ↓
Controller
      ↓
Request DTO
      ↓
Application Service
      ↓
Domain Validation
      ↓
Repository
      ↓
Response DTO
```

### 15.28 Settlement Validation

The Domain must validate:

```text
Group
Payer
Receiver
Amount
Currency
State
```

### 15.29 Settlement Financial Validation

The Settlement must satisfy the defined financial rules.

At minimum:

```text
Amount > 0
```

when required by V1.

### 15.30 Settlement Balance Validation

Settlement creation should be validated against the defined balance rules.

If V1 requires preventing settlements greater than an outstanding balance, that rule must be enforced consistently across:

```text
Android
Backend
Synchronization
```

If V1 permits over-settlement, the resulting balance behavior must be explicitly defined.

### 15.31 Settlement and Balance

Settlements affect balances.

Conceptually:

```text
Expenses
      ↓
Initial Balances
      ↓
Settlements
      ↓
Final Balances
```

### 15.32 Settlement Must Not Modify Expense

Creating a Settlement should not modify the original Expense records.

```text
Expense
      +
Settlement
      ↓
Balance Calculation
```

### 15.33 Settlement History

Settlements should remain independently queryable as financial events.

### 15.34 Settlement List

Settlement queries should support:

```text
By Group
By Settlement ID
By Payer
By Receiver
By Date
```

where required.

### 15.35 Settlement Pagination

Large Settlement histories should support pagination/incremental loading where required.

### 15.36 Settlement Update

If V1 supports Settlement updates:

```text
Load Settlement
      ↓
Authorize
      ↓
Validate Version
      ↓
Apply Mutation
      ↓
Validate
      ↓
Persist
      ↓
Create SyncOperation
```

### 15.37 Settlement Version Check

Optimistic concurrency should prevent stale updates from silently overwriting newer state.

### 15.38 Settlement Conflict

Concurrent Settlement updates should follow the Conflict Resolution architecture.

### 15.39 Settlement Conflict Detection

Conceptually:

```text
Current Version
      +
Incoming Base Version
      ↓
Version Match?
      ↓
No → Conflict
```

### 15.40 Settlement Conflict Resolution

Conflict resolution must result in a valid Settlement state.

### 15.41 Settlement Delete

If deletion is supported:

```text
Settlement
      ↓
Authorization
      ↓
Tombstone
      ↓
SyncOperation
```

### 15.42 Settlement Physical Deletion

Physical deletion must not occur while synchronization requires the Settlement record.

### 15.43 Settlement Synchronization

Settlement mutations must generate SyncOperations.

Example:

```text
CREATE
UPDATE
DELETE
```

according to the synchronization model.

### 15.44 Offline Settlement

Settlement creation must work without Backend availability.

```text
Offline
  ↓
Create Settlement
  ↓
Validate
  ↓
Persist Locally
  ↓
Create SyncOperation
```

### 15.45 Offline Settlement Consistency

A Settlement must be fully valid before being stored locally.

### 15.46 Incoming Settlement Synchronization

Incoming Settlement operations should follow:

```text
Receive Operation
      ↓
Authenticate
      ↓
Authorize
      ↓
Validate
      ↓
Check Version
      ↓
Apply / Conflict
      ↓
Persist
```

### 15.47 Duplicate Settlement Operation

Repeated delivery of the same SyncOperation must not create duplicate Settlements.

### 15.48 Settlement Idempotency

The system must use:

```text
SyncOperation ID
```

or another defined idempotency mechanism to prevent duplicate financial effects.

### 15.49 Settlement and P2P

P2P Settlement synchronization must use the same Domain validation and synchronization rules as Backend synchronization.

### 15.50 P2P Settlement Flow

```text
P2P Message
      ↓
Authenticate Peer
      ↓
Validate Operation
      ↓
Validate Settlement
      ↓
Check Version
      ↓
Apply / Conflict
      ↓
Persist
```

### 15.51 Settlement Error Handling

Possible errors include:

```text
INVALID_SETTLEMENT
INVALID_AMOUNT
INVALID_CURRENCY
INVALID_PAYER
INVALID_RECEIVER
PAYER_EQUALS_RECEIVER
INVALID_MEMBERSHIP
UNAUTHORIZED
VERSION_CONFLICT
```

### 15.52 Settlement Repository Errors

Persistence failures should be translated into application-level errors rather than exposing database implementation details.

### 15.53 Settlement Transaction

The Settlement and required SyncOperation must be committed atomically.

```text
Settlement
+
SyncOperation
      ↓
Commit
```

### 15.54 Settlement Recovery

The system must preserve enough information to recover from:

```text
Application Restart
Network Failure
Backend Failure
P2P Interruption
```

### 15.55 Settlement Retry

Transient synchronization failures should leave the Settlement operation retryable.

### 15.56 Permanent Settlement Failure

Permanent synchronization failures must enter the appropriate:

```text
Conflict
Failure
Manual Resolution
```

state according to the synchronization architecture.

### 15.57 Settlement and Balance Recalculation

After a Settlement mutation:

```text
Settlement Change
      ↓
Balance Calculation
      ↓
Updated Balance
```

must reflect the new state.

### 15.58 Balance Consistency

The same Group state must produce the same balances on:

```text
Android
Backend
```

### 15.59 Settlement Ordering

Settlement ordering should use the defined timestamp/version/event ordering strategy.

The implementation must not rely solely on device clock order when synchronization requires stronger ordering guarantees.

### 15.60 Settlement Audit Metadata

Where required, preserve:

```text
Settlement ID
Created At
Updated At
Version
Device ID
SyncOperation ID
```

### 15.61 Settlement Logging

Settlement logs should contain only the operational information required for troubleshooting.

Sensitive financial/user information should not be unnecessarily logged.

### 15.62 Settlement Security

Settlement operations must be protected through:

```text
Authentication
Authorization
Transport Security
Database Security
Local Storage Security
```

according to the Security Architecture.

### 15.63 Settlement Testing

Settlement implementation must include:

```text
Domain Tests
Application Service Tests
Repository Tests
Database Tests
API Tests
Synchronization Tests
P2P Tests
```

### 15.64 Settlement Creation Tests

Test:

```text
Valid Settlement
Invalid Group
Invalid Payer
Invalid Receiver
Payer == Receiver
Invalid Amount
Invalid Currency
Unauthorized User
```

### 15.65 Settlement Update Tests

If updates are supported, test:

```text
Valid Update
Unauthorized Update
Invalid Amount
Stale Version
Concurrent Update
```

### 15.66 Settlement Delete Tests

Test:

```text
Valid Delete
Unauthorized Delete
Repeated Delete
Tombstone
Synchronization
```

### 15.67 Settlement Balance Tests

Test:

```text
Expense Only
Expense + Settlement
Multiple Settlements
Multiple Users
Zero Final Balance
Positive Balance
Negative Balance
```

according to the balance rules.

### 15.68 Settlement Offline Tests

Test:

```text
Offline Creation
Application Restart
Pending Sync
Online Recovery
```

### 15.69 Settlement Synchronization Tests

Test:

```text
Create Sync
Update Sync
Delete Sync
Duplicate Operation
Version Conflict
Retry
Recovery
```

### 15.70 Settlement P2P Tests

Test:

```text
P2P Settlement
Duplicate Message
Interrupted Transfer
Reconnection
Version Conflict
```

### 15.71 Settlement Transaction Tests

Verify:

```text
Settlement
+
SyncOperation
```

are committed together.

### 15.72 Settlement Performance

Settlement queries should remain efficient for expected V1 Group sizes.

### 15.73 Settlement Query Optimization

Indexes should support:

```text
Group ID
Payer
Receiver
Timestamp
Version
```

where required.

### 15.74 Settlement Implementation Completion Criteria

Settlement Implementation is complete when:

```text
Settlement Domain Model Implemented
Creation Implemented
Update Implemented
Delete / Tombstone Implemented
Validation Implemented
Authorization Implemented
Persistence Implemented
API Implemented
Offline Operation Implemented
SyncOperation Integration Implemented
Conflict Handling Integrated
Balance Integration Implemented
P2P Synchronization Integrated
Tests Passing
```

### 15.75 Settlement Invariants

The following rules are mandatory:

- Every Settlement must belong to a valid Group.
- Payer must be a valid User according to Group membership rules.
- Receiver must be a valid User according to Group membership rules.
- Payer and Receiver must satisfy the defined distinct-user rule.
- Settlement amounts must use precise monetary representation.
- Settlement amounts must satisfy the defined positive amount rule.
- Currency must be explicitly represented and validated.
- Settlement creation must be authorized.
- Settlement updates must be version-aware.
- Stale Settlement updates must not silently overwrite newer state.
- Settlement and SyncOperation creation must remain atomic.
- Offline Settlement creation must work without Backend availability.
- Duplicate Settlement synchronization must not create duplicate financial effects.
- Settlement changes must follow the synchronization architecture.
- Settlement conflicts must follow the Conflict Resolution architecture.
- Settlement creation must not implicitly modify Expense records.
- Settlement changes must be reflected correctly in balance calculations.
- Android and Backend must calculate the same balance for the same Domain state.
- P2P Settlement synchronization must use the same Domain validation as Backend synchronization.
- Physical Settlement deletion must respect synchronization requirements.
- Settlement operations must preserve recovery information required by synchronization.
- Settlement logs must not unnecessarily expose sensitive information.
- Critical Settlement rules must have automated tests.

## 16. Balance Calculation Implementation

### 16.1 Purpose

This section defines the implementation of Balance Calculation in SplitSync V1.

Balance Calculation determines the net financial position of each User within a Group based on:

```text
Expenses
+
Expense Splits
+
Settlements
```

The calculation must be deterministic and must produce the same result for the same Group state.

### 16.2 Balance Principle

The balance calculation should follow:

```text
Group Financial State
        ↓
Expense Contributions
        ↓
Expense Shares
        ↓
Settlement Transfers
        ↓
Net Balance
```

### 16.3 Balance Model

A User's balance represents the net amount owed or receivable within a Group.

Conceptually:

```text
Positive Balance
    ↓
User Should Receive

Negative Balance
    ↓
User Should Pay
```

The exact sign convention must remain consistent throughout the application.

### 16.4 Balance Input

Balance calculation requires:

```text
Group
Members
Expenses
Expense Splits
Settlements
```

and any additional state defined by the Domain Model.

### 16.5 Expense Contribution

For each Expense:

```text
Expense Amount
```

represents the amount paid by the Expense creator/payer according to the Domain model.

### 16.6 Expense Share

For each ExpenseSplit:

```text
User Share
```

represents the User's responsibility for that Expense.

### 16.7 Expense Balance Effect

For an Expense:

```text
Payer
      ↓
Credit / Contribution

Participants
      ↓
Debit / Share
```

The exact arithmetic must follow the defined balance algorithm.

### 16.8 Settlement Effect

A Settlement represents a transfer:

```text
Payer
      ↓
Money
      ↓
Receiver
```

Therefore:

```text
Payer Balance
      ↑
Receiver Balance
      ↓
```

according to the selected sign convention.

### 16.9 Net Balance

The final balance is derived from all relevant financial events.

Conceptually:

```text
Net Balance
=
Expense Effects
+
Settlement Effects
```

### 16.10 Balance Calculation Service

The Domain/Application layer should provide a deterministic balance calculation service.

Conceptually:

```text
BalanceCalculator
```

### 16.11 Balance Calculator Input

The calculator should receive the relevant Group financial state:

```text
Members
Expenses
ExpenseSplits
Settlements
```

### 16.12 Balance Calculator Output

The calculator should return:

```text
User → Balance
```

Conceptually:

```text
User A → +₹500
User B → -₹300
User C → -₹200
```

where:

```text
500 - 300 - 200 = 0
```

### 16.13 Balance Conservation

For a closed Group financial state:

```text
Sum(All User Balances)
=
0
```

subject to the defined rounding rules.

This is a mandatory invariant.

### 16.14 Expense Split Consistency

Before calculating balances:

```text
Expense Amount
=
Sum(ExpenseSplits)
```

must hold for valid Expense data.

### 16.15 Invalid Expense State

If an Expense has invalid splits:

```text
Sum(Splits) ≠ Expense Amount
```

the balance calculation must not silently produce an incorrect result.

The operation should fail or exclude the invalid state according to the defined error-handling strategy.

### 16.16 Settlement Consistency

Settlements must be included exactly once in the balance calculation.

Duplicate Settlement records must not produce duplicate financial effects.

### 16.17 Deleted Expenses

Deleted/tombstoned Expenses must follow the defined Domain synchronization rules.

The balance calculator must use the appropriate active/historical state.

### 16.18 Deleted Settlements

Deleted/tombstoned Settlements must be handled consistently with the Settlement lifecycle.

### 16.19 User Membership

Balance results should only include Users relevant to the Group according to the Membership model.

Historical financial records must remain valid even when Membership state changes.

### 16.20 New Member

A newly added Member should initially have:

```text
Balance = 0
```

unless existing financial records explicitly affect that User.

### 16.21 Removed Member

Removing a Member must not automatically erase their historical financial state.

Existing Expenses and Settlements must continue to follow the Domain rules.

### 16.22 Expense Creation Impact

When a new Expense is created:

```text
Expense
+
ExpenseSplits
      ↓
Balance Recalculation
```

must reflect the new financial state.

### 16.23 Expense Update Impact

When an Expense is updated:

```text
Old Expense State
      ↓
New Expense State
      ↓
Balance Recalculation
```

must replace the old financial effect rather than applying the new effect on top of it.

### 16.24 Expense Delete Impact

If an Expense is deleted according to the Domain rules:

```text
Expense Removed From Active State
      ↓
Balance Recalculation
```

must remove its active financial effect.

### 16.25 Settlement Creation Impact

When a Settlement is created:

```text
Settlement
      ↓
Balance Recalculation
```

must reflect the transfer.

### 16.26 Settlement Update Impact

When a Settlement is updated:

```text
Old Settlement Effect
      ↓
New Settlement Effect
      ↓
Balance Recalculation
```

must not double-count the transfer.

### 16.27 Settlement Delete Impact

When a Settlement is deleted:

```text
Settlement Removed From Active State
      ↓
Balance Recalculation
```

must remove its financial effect.

### 16.28 Balance Calculation Ordering

The calculation should use a deterministic ordering.

Conceptually:

```text
Load Group
      ↓
Load Expenses
      ↓
Load Splits
      ↓
Load Settlements
      ↓
Calculate
      ↓
Return Balances
```

The calculation must not depend on database row ordering.

### 16.29 Balance Calculation Determinism

The following must always produce the same result:

```text
Same Group State
      +
Same Financial Events
      ↓
Same Balances
```

### 16.30 Currency

Balance calculation must use a consistent currency model.

V1 should not silently combine incompatible currencies.

### 16.31 Multi-Currency

If multiple currencies are not supported by V1:

```text
Different Currency
      ↓
Reject / Unsupported
```

rather than performing an implicit conversion.

### 16.32 Currency Conversion

Currency conversion must not be introduced into balance calculation unless explicitly defined by the Domain and Technical Design.

### 16.33 Monetary Precision

Balance calculations must use precise monetary representation.

Avoid:

```text
float
double
```

for authoritative calculations.

### 16.34 Rounding

Rounding must be deterministic and must preserve financial consistency.

### 16.35 Balance Rounding

After calculation:

```text
Sum(All Balances)
```

must remain zero within the defined monetary precision.

### 16.36 Balance Result Ordering

If balances are returned as a list, the ordering must be deterministic.

For example:

```text
User ID
```

or another explicitly defined ordering.

### 16.37 Balance Query

The application should expose a balance query through the appropriate Application Service.

Conceptually:

```text
GetGroupBalances
```

### 16.38 Balance Query Flow

```text
Request
   ↓
Authenticate
   ↓
Authorize Group Access
   ↓
Load Financial State
   ↓
Calculate Balances
   ↓
Return Result
```

### 16.39 Balance Repository Access

Balance calculation should use repositories to load required financial state.

It should not directly access:

```text
Room
JPA
SQL
```

### 16.40 Balance Persistence

Calculated balances may be derived dynamically rather than persisted.

The preferred V1 approach should avoid storing duplicated balance state unless there is a clear performance requirement.

### 16.41 Derived Balance

Conceptually:

```text
Expenses
+
Settlements
      ↓
BalanceCalculator
      ↓
Current Balance
```

This avoids inconsistent duplicated balance data.

### 16.42 Balance Cache

If balance results are cached:

```text
Financial State Change
      ↓
Invalidate / Refresh Balance
```

must occur reliably.

### 16.43 Offline Balance

Balance calculation must work fully offline using local data.

```text
Offline
   ↓
Local Expenses
+
Local Settlements
   ↓
BalanceCalculator
   ↓
Balance
```

### 16.44 Online Balance

Online state may refresh local financial data through synchronization, after which the same local calculation should produce the current balance.

### 16.45 Backend Balance

Backend balance calculation should use the same Domain rules as Android wherever the same financial state is available.

### 16.46 Cross-Platform Consistency

For the same Group state:

```text
Android Balance
      =
Backend Balance
```

subject to the defined precision rules.

### 16.47 P2P Balance

P2P synchronization must update the local financial state first.

The balance should then be derived from the updated local state.

```text
P2P Change
   ↓
Local Persistence
   ↓
Balance Calculation
```

### 16.48 Balance and Synchronization

Balance must not be synchronized as an independent financial fact unless explicitly required.

The preferred model is:

```text
Financial Events
      ↓
Synchronize Events
      ↓
Recalculate Balance
```

### 16.49 Balance Conflict

Balance itself should generally not be treated as an independently synchronized entity.

Conflicts should occur at the underlying financial event level:

```text
Expense
Settlement
Membership
```

### 16.50 Balance After Conflict Resolution

After resolving an Expense or Settlement conflict:

```text
Resolved State
      ↓
Recalculate Balance
```

must produce the new deterministic balance.

### 16.51 Balance and Transactions

A balance query should operate on a consistent financial snapshot.

It must not combine:

```text
Old Expense State
+
New Settlement State
```

from different transaction snapshots.

### 16.52 Balance Transaction Consistency

When a financial mutation completes:

```text
Mutation Transaction
      ↓
Commit
      ↓
Balance Query
```

should observe the complete committed state.

### 16.53 Balance Performance

Balance calculation must be efficient enough for expected V1 Group sizes.

### 16.54 Large Group Optimization

For large Groups, queries should avoid loading unnecessary data.

Potential optimizations include:

```text
Indexed Queries
Pagination Where Applicable
Batch Loading
Efficient Projection
```

### 16.55 Balance Query Optimization

Indexes should support:

```text
Group ID
Expense Group ID
Settlement Group ID
Expense Timestamp
Settlement Timestamp
```

where required.

### 16.56 Balance Calculation Complexity

The implementation should avoid unnecessary repeated scans.

Conceptually:

```text
Expenses → Single Processing Pass
Splits   → Single Processing Pass
Settlements → Single Processing Pass
```

where practical.

### 16.57 Balance Testing

Balance calculation must have dedicated Unit Tests.

### 16.58 Basic Balance Test

Example:

```text
User A Pays ₹100
User A Share = ₹50
User B Share = ₹50

Result:

User A → +₹50
User B → -₹50
```

### 16.59 Multiple Expense Test

Test:

```text
Multiple Expenses
Multiple Participants
Different Payers
```

and verify total balance conservation.

### 16.60 Settlement Test

Test:

```text
Expense Balance
      ↓
Settlement
      ↓
Reduced Outstanding Balance
```

### 16.61 Multiple Settlement Test

Test multiple settlements between different Users and verify that all transfers are included exactly once.

### 16.62 Zero Balance Test

Test scenarios where all outstanding amounts are settled:

```text
User A → 0
User B → 0
User C → 0
```

### 16.63 Negative Balance Test

Verify that Users who owe money receive the defined negative balance representation.

### 16.64 Positive Balance Test

Verify that Users who should receive money receive the defined positive balance representation.

### 16.65 Balance Conservation Test

Every valid Group financial state must satisfy:

```text
Sum(All Balances) = 0
```

### 16.66 Rounding Test

Test monetary values that require rounding and verify conservation within the defined precision.

### 16.67 Currency Test

Test:

```text
Supported Currency
Unsupported Currency
Mixed Currency
```

according to V1 rules.

### 16.68 Offline Balance Test

Verify that balances can be calculated when:

```text
Backend Unavailable
```

### 16.69 Synchronization Balance Test

Verify:

```text
Remote Expense
      ↓
Local Apply
      ↓
Balance Updated
```

### 16.70 Conflict Balance Test

Verify that resolving a financial conflict updates balances correctly.

### 16.71 Balance Regression Tests

Known financial scenarios should be preserved as regression test cases.

### 16.72 Balance Completion Criteria

Balance Calculation Implementation is complete when:

```text
Balance Model Implemented
Balance Calculator Implemented
Expense Effects Implemented
Settlement Effects Implemented
Currency Rules Implemented
Rounding Rules Implemented
Offline Calculation Works
Backend Calculation Works
P2P Integration Works
Conflict Integration Works
Performance Validated
Tests Passing
```

### 16.73 Balance Invariants

The following rules are mandatory:

- Balance must be derived from valid financial state.
- Balance calculation must be deterministic.
- The same Group state must produce the same balances.
- Expense Splits must equal their Expense total.
- Settlements must be included exactly once.
- Duplicate financial events must not be double-counted.
- Balance calculation must use precise monetary representation.
- Floating-point arithmetic must not be used for authoritative financial calculations.
- Currency rules must be enforced.
- Unsupported currency combinations must not be silently converted.
- Sum of all User balances must equal zero for a valid closed financial state.
- Expense updates must replace their previous financial effect.
- Settlement updates must replace their previous financial effect.
- Deleted financial events must follow the defined Domain lifecycle.
- Balance must not be treated as an independently synchronized financial event unless explicitly required.
- Balance must be recalculated from synchronized financial state.
- Offline balance calculation must work without Backend availability.
- Android and Backend must produce consistent results for the same state.
- P2P synchronization must update underlying financial state before balance calculation.
- Balance queries must observe a consistent financial snapshot.
- Critical balance scenarios must have automated regression tests.


## 17. Group and Membership Implementation

### 17.1 Purpose

This section defines the implementation of Groups and Memberships in SplitSync V1.

Groups provide the shared context in which:

```text
Members
Expenses
Expense Splits
Settlements
Balances
Synchronization
```

are managed.

Membership defines the relationship between:

```text
User
+
Group
```

### 17.2 Group Model

A Group should contain:

```text
Group ID
Name
Creator / Owner
Version
Created At
Updated At
State
```

according to the Domain Model.

### 17.3 Membership Model

Membership represents:

```text
User
      +
Group
      +
Membership State
      +
Role / Permission
```

where role information is required.

### 17.4 Group Identity

Every Group must have a stable:

```text
GroupId
```

### 17.5 Offline Group Creation

Group IDs must support offline creation.

```text
Offline
   ↓
Generate Group ID
   ↓
Create Group
   ↓
Synchronize Later
```

### 17.6 Group Creation Flow

```text
Create Group Request
      ↓
Validate Input
      ↓
Create Group
      ↓
Create Owner Membership
      ↓
Persist Atomically
      ↓
Create SyncOperation
      ↓
Commit
```

### 17.7 Group Name Validation

Group names must follow the defined Domain rules.

At minimum:

```text
Not Null
Not Blank
Valid Length
```

### 17.8 Group Creator

The creator must be a valid authenticated User.

The creator becomes the initial Group owner according to the Group rules.

### 17.9 Initial Membership

Group creation should create the required initial Membership atomically with the Group.

```text
Group
+
Owner Membership
```

### 17.10 Group Creation Transaction

Group creation should use one transaction:

```text
Create Group
      +
Create Initial Membership
      +
Create SyncOperation
```

### 17.11 Group State

Group lifecycle state should follow the Domain model.

If required:

```text
ACTIVE
DELETED
```

or another explicitly defined state set may be used.

### 17.12 Group Version

Synchronizable Group state must maintain the required version.

### 17.13 Group Timestamp

Group creation and updates must use consistent timestamps.

### 17.14 Group Update

Group updates should follow:

```text
Load Group
      ↓
Authorize
      ↓
Validate Version
      ↓
Validate Mutation
      ↓
Update Group
      ↓
Create SyncOperation
```

### 17.15 Group Delete

If Group deletion is supported:

```text
Authorize
      ↓
Validate Group State
      ↓
Tombstone Group
      ↓
Create SyncOperation
```

### 17.16 Group Physical Deletion

Physical deletion must respect:

```text
Expenses
Settlements
Memberships
Synchronization
Conflicts
```

and should not occur while historical/synchronization data requires the Group.

### 17.17 Group Authorization

Group operations must verify:

```text
Authenticated User
      ↓
Membership
      ↓
Role / Permission
      ↓
Allowed Operation
```

### 17.18 Group Owner

The Group owner should have the permissions defined by the Authorization Model.

### 17.19 Group Member

A normal Member should receive only the permissions explicitly granted by the Group authorization rules.

### 17.20 Membership Creation

Adding a Member should follow:

```text
Validate Group
      ↓
Validate User
      ↓
Authorize Request
      ↓
Check Existing Membership
      ↓
Create Membership
      ↓
Create SyncOperation
```

### 17.21 Membership Uniqueness

A User must not have duplicate active Memberships within the same Group.

```text
User + Group
      ↓
One Active Membership
```

### 17.22 Membership State

Membership should have a controlled state.

Example:

```text
ACTIVE
REMOVED
```

The exact states must follow the Domain Model.

### 17.23 Membership Activation

A valid Membership can transition into:

```text
ACTIVE
```

only through an authorized operation.

### 17.24 Membership Removal

Removing a Member should follow:

```text
Authorize
      ↓
Validate Group Rules
      ↓
Validate Membership
      ↓
Change Membership State
      ↓
Create SyncOperation
```

### 17.25 Owner Removal

The system must enforce the Group owner rules.

If the owner cannot be removed while the Group exists:

```text
Remove Owner
      ↓
Rejected
```

### 17.26 Last Member Removal

The system must enforce the defined behavior when removing the final active Member.

### 17.27 Membership and Historical Data

Removing a Member must not invalidate historical:

```text
Expenses
ExpenseSplits
Settlements
Balances
```

that already reference the User.

### 17.28 Membership and New Expenses

A removed/inactive Member must not be added to new ExpenseSplits unless explicitly permitted by the Domain rules.

### 17.29 Membership and Settlements

Settlement participants must satisfy the defined Membership rules.

Historical Settlements must remain valid after Membership state changes.

### 17.30 Membership and Balance

Membership state must not automatically erase historical financial balances.

Balance calculation must use the defined financial event model.

### 17.31 Membership Repository

MembershipRepository should provide:

```text
Create Membership
Find Membership
List Members
Check Membership
Update Membership
Remove Membership
```

### 17.32 Group Repository

GroupRepository should provide:

```text
Create Group
Find Group
List Groups
Update Group
Delete / Tombstone Group
```

### 17.33 Group DAO

The local Group DAO should provide efficient queries for:

```text
Group ID
User Membership
Group List
```

### 17.34 Membership DAO

The local Membership DAO should support:

```text
Find Membership
Find Members By Group
Find Groups By User
Insert
Update
```

### 17.35 Backend Group Repository

Backend Group Repository should provide persistence operations for Group state.

### 17.36 Backend Membership Repository

Backend Membership Repository should provide persistence operations for Membership state.

### 17.37 Group Mapping

Group mapping must preserve:

```text
Group ID
Name
Creator / Owner
Version
Timestamps
State
```

### 17.38 Membership Mapping

Membership mapping must preserve:

```text
User ID
Group ID
Role
State
Version
Timestamps
```

where applicable.

### 17.39 Group API

Group APIs should use dedicated DTOs.

Examples:

```text
CreateGroupRequest
UpdateGroupRequest
GroupResponse
```

### 17.40 Membership API

Membership APIs may include:

```text
AddMemberRequest
UpdateMembershipRequest
MembershipResponse
```

according to the API Design.

### 17.41 Group Creation API Flow

```text
HTTP Request
      ↓
Controller
      ↓
Request Validation
      ↓
Application Service
      ↓
Authorization
      ↓
Domain
      ↓
Repository
      ↓
Response
```

### 17.42 Membership API Flow

```text
Add Member Request
      ↓
Authenticate
      ↓
Authorize
      ↓
Validate User
      ↓
Validate Group
      ↓
Create Membership
      ↓
Persist
      ↓
Sync
```

### 17.43 Group Synchronization

Group mutations must produce SyncOperations.

Examples:

```text
CREATE
UPDATE
DELETE
```

### 17.44 Membership Synchronization

Membership mutations must also be represented in synchronization.

Examples:

```text
ADD
UPDATE
REMOVE
```

according to the synchronization model.

### 17.45 Group Incoming Synchronization

Incoming Group operations should follow:

```text
Receive
   ↓
Authenticate
   ↓
Authorize
   ↓
Validate
   ↓
Check Version
   ↓
Apply / Conflict
   ↓
Persist
```

### 17.46 Membership Incoming Synchronization

Incoming Membership operations should follow the same validation and version rules.

### 17.47 Duplicate Group Operation

Duplicate Group SyncOperations must not create duplicate Groups.

### 17.48 Duplicate Membership Operation

Repeated Membership operations must be idempotent.

Example:

```text
ADD Member
      ↓
Already Active?
      ↓
No Duplicate Membership
```

### 17.49 Group Conflict

Concurrent Group updates must follow the Conflict Resolution architecture.

### 17.50 Membership Conflict

Concurrent Membership changes must use version/conflict handling.

Example:

```text
Device A → Add Member
Device B → Remove Member
       ↓
Conflict Detection
```

### 17.51 Membership Authorization Conflict

A Membership operation received from synchronization must be checked against the authorization rules and trusted synchronization context.

### 17.52 Group and Expense Relationship

Expenses must reference an existing valid Group.

A Group cannot be treated as deleted while active financial records require it unless the Domain explicitly defines such behavior.

### 17.53 Group and Settlement Relationship

Settlements must reference an existing valid Group.

### 17.54 Group and Sync Relationship

Group state must remain synchronized before dependent entities where the synchronization protocol requires ordering.

### 17.55 Group Creation Ordering

When synchronizing a newly created Group:

```text
Group Creation
      ↓
Initial Membership
      ↓
Dependent Expenses / Settlements
```

must follow the synchronization dependency rules.

### 17.56 Membership Dependency

Dependent operations should not be applied against a Group/User state that does not yet exist.

If operations arrive out of order, the synchronization layer must handle them using:

```text
Dependency Resolution
Retry
Deferred Apply
```

as defined by the synchronization architecture.

### 17.57 Group Offline Creation

Group creation must work without Backend availability.

```text
Offline
   ↓
Create Group
   ↓
Create Owner Membership
   ↓
Persist
   ↓
Queue Sync
```

### 17.58 Offline Membership

Membership changes that are permitted offline must be persisted locally and synchronized later.

### 17.59 Offline Authorization

Offline authorization must use the locally available authorization state according to the security model.

Security-sensitive operations must not bypass required Backend authorization.

### 17.60 P2P Group Synchronization

P2P synchronization must use the same Group and Membership Domain rules.

```text
P2P Message
      ↓
Validate
      ↓
Domain
      ↓
Repository
```

### 17.61 P2P Membership Synchronization

P2P Membership changes must validate:

```text
Peer
Operation
User
Group
Membership State
Version
```

### 17.62 Group and Balance

Group state provides the context for Balance Calculation.

```text
Group
   ↓
Expenses
+
Settlements
   ↓
Balances
```

### 17.63 Membership and Balance

Membership changes must not directly modify historical financial events.

Balance calculation must continue to use the valid financial state.

### 17.64 Group Query

Group details may return:

```text
Group
Members
Recent Expenses
Recent Settlements
Balance Summary
```

depending on the Application API.

### 17.65 Group List

A User's Group list should return only Groups accessible to that User.

### 17.66 Membership Query

Membership queries should support:

```text
Members By Group
Groups By User
Membership By User + Group
```

where required.

### 17.67 Pagination

Large Group and Membership lists should support pagination where necessary.

### 17.68 Group Testing

Group implementation must include:

```text
Domain Tests
Application Tests
Repository Tests
Database Tests
API Tests
Synchronization Tests
```

### 17.69 Group Creation Tests

Test:

```text
Valid Group
Invalid Name
Unauthorized User
Owner Membership
Transaction Atomicity
Offline Creation
```

### 17.70 Group Update Tests

Test:

```text
Valid Update
Unauthorized Update
Invalid Name
Stale Version
Concurrent Update
```

### 17.71 Group Delete Tests

Test:

```text
Valid Delete
Unauthorized Delete
Financial Data Dependency
Tombstone
Synchronization
```

### 17.72 Membership Creation Tests

Test:

```text
Valid Member
Unknown User
Unknown Group
Duplicate Membership
Unauthorized Request
```

### 17.73 Membership Removal Tests

Test:

```text
Valid Removal
Unknown Membership
Unauthorized Removal
Owner Removal
Historical Data Preservation
```

### 17.74 Membership Synchronization Tests

Test:

```text
Add Member
Remove Member
Duplicate Operation
Out-of-Order Operation
Version Conflict
Retry
```

### 17.75 Group Conflict Tests

Test:

```text
Concurrent Group Update
Stale Update
Conflict Creation
Conflict Resolution
```

### 17.76 Membership Conflict Tests

Test:

```text
Concurrent Membership Change
Add vs Remove
Duplicate Operation
Version Conflict
Conflict Resolution
```

### 17.77 Group Transaction Tests

Verify:

```text
Group
+
Initial Membership
+
SyncOperation
```

are committed atomically.

### 17.78 Membership Transaction Tests

Verify Membership changes and required SyncOperations remain consistent.

### 17.79 Group Performance

Group queries must remain efficient for expected V1 Group sizes.

### 17.80 Group Query Optimization

Indexes should support:

```text
Group ID
User ID
Membership
Group State
Updated At
```

where required.

### 17.81 Group Security

Group and Membership operations must be protected through:

```text
Authentication
Authorization
Transport Security
Persistence Security
```

### 17.82 Group Logging

Logs should not unnecessarily expose:

```text
Personal Information
Financial Information
Authentication Data
```

### 17.83 Group Implementation Completion Criteria

Group and Membership Implementation is complete when:

```text
Group Model Implemented
Membership Model Implemented
Group Creation Implemented
Group Update Implemented
Group Delete / Tombstone Implemented
Membership Add Implemented
Membership Remove Implemented
Authorization Implemented
Persistence Implemented
API Implemented
Offline Operation Implemented
Synchronization Integrated
Conflict Handling Integrated
P2P Integration Implemented
Balance Integration Verified
Tests Passing
```

### 17.84 Group and Membership Invariants

The following rules are mandatory:

- Every Group must have a stable Group ID.
- Group IDs must support offline creation.
- Group names must satisfy Domain validation rules.
- Group creation must create the required initial Membership atomically.
- Every Membership must reference a valid User and Group.
- Duplicate active Memberships must not be created.
- Membership state must use controlled transitions.
- Membership changes must be authorized.
- Group operations must be authorized.
- Group owner rules must be enforced.
- Removing a Member must not erase historical financial records.
- Removed Members must not be added to new financial operations when prohibited by the Domain rules.
- Historical Expenses and Settlements must remain valid after Membership changes.
- Group and Membership mutations must synchronize through SyncOperations.
- Duplicate Group and Membership operations must be idempotent.
- Group and Membership version conflicts must follow the Conflict Resolution architecture.
- Out-of-order synchronization must not create invalid Group dependencies.
- Offline Group and permitted Membership operations must work without Backend availability.
- P2P Group and Membership synchronization must use the same Domain validation rules.
- Group creation, initial Membership, and required SyncOperation state must remain consistent.
- Group and Membership persistence must preserve synchronization metadata.
- Group state must remain compatible with dependent Expenses and Settlements.
- Group and Membership implementations must be covered by automated tests.

## 18. Device Identity Implementation

### 18.1 Purpose

This section defines the implementation of Device Identity in SplitSync V1.

Device Identity is used to distinguish individual application installations participating in:

```text
Backend Synchronization
Peer-to-Peer Synchronization
SyncOperation Creation
Conflict Detection
Device Authorization
```

### 18.2 Device Identity Principle

Each application installation must have a stable logical Device ID.

Conceptually:

```text
Application Installation
        ↓
Device Identity
        ↓
Device ID
        ↓
Synchronization
```

### 18.3 Device ID

The Device ID must be:

```text
Unique
Stable
Locally Available
Suitable for Synchronization
```

### 18.4 Device ID Generation

Device IDs should be generated locally when the application installation is initialized.

Conceptually:

```text
First Application Start
        ↓
Generate Device ID
        ↓
Persist Device ID
        ↓
Reuse Device ID
```

### 18.5 Device ID Persistence

The Device ID must survive:

```text
Application Restart
Process Restart
Temporary Offline State
```

It must not be regenerated during normal application execution.

### 18.6 Device ID Regeneration

A new Device ID may be generated when the application is considered a new installation according to the lifecycle strategy.

### 18.7 Device ID Immutability

Once assigned, the Device ID must not be modified arbitrarily.

### 18.8 Device Identity Storage

The Device ID should be stored in secure local application storage appropriate to its security requirements.

### 18.9 Device Identity and Android

The Device Identity implementation must not depend unnecessarily on hardware identifiers that may be:

```text
Restricted
Unavailable
Resettable
Privacy-sensitive
```

The application-level Device ID should be the authoritative synchronization identity.

### 18.10 Device Identity and User

A Device is associated with a User.

Conceptually:

```text
User
 ├── Device A
 ├── Device B
 └── Device C
```

The exact maximum number of Devices per User must follow the V1 requirements.

### 18.11 Device Registration

Before participating in Backend synchronization, a Device may need to be registered.

```text
Device ID
      ↓
Authentication
      ↓
Device Registration
      ↓
Active Device
```

### 18.12 Device Registration Data

The Backend may store:

```text
Device ID
User ID
Device State
Created At
Updated At
Last Seen
```

according to the Device model.

### 18.13 Device State

Device lifecycle state should use a controlled representation.

Example:

```text
ACTIVE
REVOKED
```

### 18.14 Active Device

An ACTIVE Device may perform operations allowed by the Authentication and Authorization model.

### 18.15 Revoked Device

A REVOKED Device must not perform protected synchronization operations.

```text
Revoked Device
      ↓
Synchronization Request
      ↓
Rejected
```

### 18.16 Device Authentication

Device Identity and User Authentication are separate concepts.

```text
User Identity
      +
Device Identity
      ↓
Authenticated Session
```

### 18.17 Device Authorization

Synchronization authorization may require:

```text
Authenticated User
+
Registered Device
+
Device State
```

### 18.18 Device and SyncOperation

Every locally generated SyncOperation should contain the Device ID where required.

Conceptually:

```text
SyncOperation
├── Operation ID
├── Device ID
├── Entity ID
└── Operation Type
```

### 18.19 Device and Conflict

Device identity may be used to determine the source of concurrent changes.

Example:

```text
Device A → Version 5
Device B → Version 5

Both Update
   ↓
Conflict
```

### 18.20 Device and Offline Operations

Offline-created operations must contain the local Device ID.

```text
Offline Mutation
      ↓
Device ID
      ↓
SyncOperation
```

### 18.21 Device and P2P

P2P synchronization requires both peers to identify themselves.

Conceptually:

```text
Peer A
  ↕
Peer B
```

Each peer must have a stable Device ID.

### 18.22 P2P Device Identification

A P2P peer should establish:

```text
Peer Device ID
Protocol Version
Authentication State
```

before exchanging synchronization data.

### 18.23 Device Identity Verification

A received Device ID must not automatically be trusted.

It must be associated with an authenticated/trusted identity according to the Security Architecture.

### 18.24 Device Spoofing Prevention

The system must not rely solely on a transmitted Device ID for authentication.

Incorrect:

```text
Received Device ID
      ↓
Trust Device
```

Preferred:

```text
Authenticated Peer
      +
Device ID
      ↓
Trusted Device
```

### 18.25 Device Registration API

The Backend may expose a Device Registration operation.

Conceptually:

```text
Register Device
      ↓
Authenticate User
      ↓
Validate Device ID
      ↓
Create / Update Device
      ↓
Return Device State
```

### 18.26 Device Revocation API

Device revocation should follow:

```text
Authenticate
      ↓
Authorize
      ↓
Identify Device
      ↓
Set REVOKED
      ↓
Persist
```

### 18.27 Device Listing

Users may be able to view their registered Devices if supported by V1.

The response should expose only appropriate metadata.

### 18.28 Device Last Seen

The Backend may maintain:

```text
lastSeen
```

to support operational visibility.

This value must not be treated as a security proof by itself.

### 18.29 Device Metadata

Optional metadata may include:

```text
Platform
Application Version
Created At
Last Seen
```

Only required metadata should be stored.

### 18.30 Device Identity Security

Device identity data must be protected according to:

```text
Authentication
Authorization
Transport Security
Local Storage Security
Backend Database Security
```

### 18.31 Device ID Logging

Device IDs may be logged for operational debugging where appropriate, but logs must not expose:

```text
Authentication Secrets
Tokens
Private Keys
```

### 18.32 Device Identity and Tokens

Authentication tokens should not be treated as the Device ID.

Conceptually:

```text
Device ID
      ≠
Access Token
```

### 18.33 Token Storage

Authentication credentials/tokens must follow the Security Architecture and secure Android storage strategy.

### 18.34 Device Identity Rotation

Device ID rotation should not occur during normal synchronization.

If rotation is required due to security or installation lifecycle, it must follow an explicit re-registration process.

### 18.35 Device Replacement

If a User installs the application on a new device:

```text
New Installation
      ↓
New Device ID
      ↓
Device Registration
      ↓
Synchronization
```

### 18.36 Device Recovery

A new Device should obtain the User's synchronized state through the defined synchronization process rather than copying another Device's identity.

### 18.37 Device Identity and Local Database

The local database should associate synchronization metadata with the current Device identity where required.

### 18.38 Device Identity and SyncState

SyncState may be scoped by:

```text
Device
+
Synchronization Context
```

depending on the synchronization design.

### 18.39 Device Identity and Cursor

Synchronization cursors should not be incorrectly shared between independent Device installations.

### 18.40 Device Identity and Idempotency

Device ID must not be the sole idempotency key.

Idempotency should use the unique:

```text
SyncOperation ID
```

### 18.41 Device Identity and Versioning

Device ID identifies the source of an operation.

Version identifies the state/revision.

These concepts must remain separate.

```text
Device ID
   ↓
Who Produced Change

Version
   ↓
Which Revision
```

### 18.42 Device Identity Testing

Device Identity must include tests for:

```text
Generation
Persistence
Restart
Registration
Revocation
Authentication
Synchronization
P2P
```

### 18.43 Device Generation Test

Verify:

```text
First Start
      ↓
Device ID Generated

Second Start
      ↓
Same Device ID
```

### 18.44 Device Restart Test

Verify that application restart does not generate a new Device ID.

### 18.45 Device Registration Test

Test:

```text
Valid Registration
Duplicate Registration
Invalid Device
Unauthorized Registration
```

### 18.46 Device Revocation Test

Test:

```text
Active Device
      ↓
Revoke
      ↓
Synchronization Rejected
```

### 18.47 Device Synchronization Test

Verify that:

```text
SyncOperation
      ↓
Correct Device ID
```

is persisted and transmitted.

### 18.48 Device P2P Test

Test:

```text
Peer Discovery
Device Identification
Authentication
Message Exchange
Unknown Peer
Revoked Peer
```

### 18.49 Device Security Test

Verify that a transmitted Device ID without valid authentication cannot impersonate a trusted Device.

### 18.50 Device Implementation Completion Criteria

Device Identity Implementation is complete when:

```text
Device ID Generation Implemented
Device ID Persistence Implemented
Device Registration Implemented
Device State Implemented
Device Revocation Implemented
Authentication Integration Implemented
Authorization Integration Implemented
SyncOperation Integration Implemented
P2P Integration Implemented
Secure Storage Defined
Testing Implemented
```

### 18.51 Device Identity Invariants

The following rules are mandatory:

- Every application installation must have a stable logical Device ID.
- Device ID must support offline operation.
- Device ID must remain stable across normal application restarts.
- Device ID must not be regenerated during normal execution.
- Device ID must not be treated as an authentication secret.
- Device ID alone must never be sufficient for authentication.
- Registered Devices must have a controlled lifecycle.
- Revoked Devices must not perform protected synchronization operations.
- Device IDs must be associated with authenticated/trusted identities.
- SyncOperations must use unique Operation IDs for idempotency.
- Device ID must identify the source of an operation, not its version.
- Independent Device installations must not incorrectly share synchronization cursors.
- New installations must receive new Device identities.
- P2P peers must authenticate before trusting Device identity.
- Device identity implementation must remain independent from UI.
- Device identity must follow the Security Architecture.
- Device identity behavior must be covered by automated tests.


## 19. Authentication Implementation

### 19.1 Purpose

This section defines the implementation of Authentication in SplitSync V1.

Authentication establishes the identity of the User and, where required, the Device participating in the application.

Conceptually:

```text
Credentials / Identity
      ↓
Authentication
      ↓
Authenticated User
      +
Authenticated Device
```

### 19.2 Authentication Principle

Authentication must answer:

```text
Who is the User?
```

and where required:

```text
Which Device is acting for that User?
```

Authentication must be completed before protected operations are executed.

### 19.3 Authentication Components

The Authentication implementation may contain:

```text
Authentication API
Authentication Service
Credential Verification
Token Management
Device Association
Session Management
Authentication State
```

### 19.4 Authentication Boundary

Authentication should be implemented at the Application/Security boundary.

The Domain should not depend directly on:

```text
JWT
HTTP Session
Android Credential APIs
OAuth SDK
Spring Security
```

### 19.5 Authentication Flow

The general flow is:

```text
User
 ↓
Login / Authentication Request
 ↓
Authentication Endpoint
 ↓
Credential Verification
 ↓
Identity Established
 ↓
Token / Session Created
 ↓
Authenticated Application
```

### 19.6 Device-Aware Authentication

Where Device Identity is part of the security model:

```text
User Authentication
      +
Device Identity
      ↓
Authenticated Device Session
```

### 19.7 Credential Model

The exact credential mechanism must follow the Security Architecture.

Possible mechanisms include:

```text
Password
OTP
OAuth / External Identity
Passkey
```

Only the mechanism selected for V1 should be implemented.

### 19.8 Credential Validation

Authentication requests must validate:

```text
Required Fields
Format
Credential Availability
```

before verification.

### 19.9 Credential Verification

Credentials must be verified through the configured Authentication Provider.

The application must not implement insecure custom cryptographic verification.

### 19.10 Password Authentication

If password authentication is selected, passwords must be stored only as secure password hashes.

Never store:

```text
Plain Text Password
Reversible Password Encryption
```

### 19.11 Password Hashing

Password hashing must use an established password hashing algorithm suitable for password storage.

The exact algorithm and parameters must follow the Security Architecture.

### 19.12 Password Comparison

Password verification must use the secure verification mechanism provided by the selected password hashing implementation.

### 19.13 Password Logging

Passwords must never appear in:

```text
Application Logs
Database Logs
API Logs
Exception Messages
```

### 19.14 Authentication Tokens

If token-based authentication is used, successful authentication should produce an appropriate token/session representation.

Conceptually:

```text
Authentication
      ↓
Access Token
```

### 19.15 Access Token

The Access Token represents authenticated access for a limited period according to the selected security model.

### 19.16 Refresh Token

If refresh tokens are used:

```text
Access Token
      ↓
Expires
      ↓
Refresh Token
      ↓
New Access Token
```

The refresh mechanism must follow the Security Architecture.

### 19.17 Token Expiration

Tokens must have explicit expiration rules.

Expired access tokens must not authorize protected requests.

### 19.18 Token Revocation

The system should support revocation where required by the selected authentication model.

Examples:

```text
User Logout
Device Revocation
Credential Compromise
Security Event
```

### 19.19 Token Storage on Android

Tokens must be stored using secure Android storage mechanisms.

They must not be stored in:

```text
Plain Text Files
SharedPreferences Without Appropriate Protection
Logs
Database Columns Without Required Protection
```

### 19.20 Token Storage on Backend

Backend secrets and signing keys must be stored through secure configuration/secret management.

They must not be committed to source control.

### 19.21 Token Transport

Authentication tokens must be transmitted only through protected transport.

```text
HTTPS
```

must be used for Backend communication.

### 19.22 Authentication Header

For token-based HTTP authentication, the standard authorization mechanism should be used according to the API contract.

### 19.23 Authentication Middleware

Backend protected endpoints should use centralized authentication middleware/filtering.

Conceptually:

```text
HTTP Request
      ↓
Authentication Middleware
      ↓
Authenticated Principal
      ↓
Controller
```

### 19.24 Authentication Principal

The authenticated principal should provide the Application layer with:

```text
User ID
Device ID
Authentication State
```

and required security claims.

### 19.25 Authentication Context

The Application Service should receive an Authentication Context rather than directly parsing HTTP headers or tokens.

### 19.26 Authentication Context Example

Conceptually:

```text
AuthenticationContext
├── User ID
├── Device ID
└── Authentication Metadata
```

### 19.27 Authentication Failure

Authentication failures should produce a generic authentication failure response.

Avoid revealing whether:

```text
User Exists
Password Exists
Credential Exists
```

unless explicitly required by the security design.

### 19.28 Invalid Credentials

Invalid credentials must not produce detailed information that helps credential enumeration.

### 19.29 Account Enumeration Prevention

Where applicable, authentication errors should use consistent responses for:

```text
Unknown User
Invalid Password
Invalid Credential
```

### 19.30 Brute Force Protection

Authentication should include appropriate protection against repeated failed attempts.

Potential mechanisms include:

```text
Rate Limiting
Temporary Lockout
Progressive Delay
Monitoring
```

The selected strategy must follow the Security Architecture.

### 19.31 Rate Limiting

Authentication endpoints should have stricter rate limits than ordinary application endpoints where appropriate.

### 19.32 Login Retry

Repeated authentication failures should not cause unlimited backend work.

### 19.33 Session Management

If server-side sessions are used, sessions must have:

```text
Expiration
Revocation
Secure Storage
User Association
Device Association
```

as required.

### 19.34 Stateless Authentication

If stateless token authentication is used, server-side session storage may not be required for every access token.

The exact model must follow the selected Security Architecture.

### 19.35 Logout

Logout should invalidate the appropriate authentication state.

Depending on the token model:

```text
Client Token Removed
```

and/or:

```text
Server Session / Refresh Token Revoked
```

### 19.36 Logout and Device

Logging out should not necessarily delete the Device Identity.

Conceptually:

```text
Logout
   ↓
Authentication State Removed
   ↓
Device Identity Preserved
```

unless the security model explicitly requires re-registration.

### 19.37 Device Revocation

Device revocation is stronger than logout.

```text
Logout
   ↓
Session Ended

Device Revocation
   ↓
Device No Longer Trusted
```

### 19.38 Authentication and Synchronization

Synchronization requests must require valid authentication according to the synchronization security model.

### 19.39 Authentication and SyncOperation

A SyncOperation received from a remote client must be associated with an authenticated:

```text
User
+
Device
```

where required.

### 19.40 Authentication and P2P

P2P authentication must establish trust between peers before synchronization data is exchanged.

Conceptually:

```text
Peer Discovery
      ↓
Peer Authentication
      ↓
Peer Trust Established
      ↓
Sync Data Exchange
```

### 19.41 P2P Authentication

P2P authentication must not rely solely on:

```text
Device ID
Device Name
IP Address
Bluetooth Address
```

These are identifiers, not sufficient authentication mechanisms by themselves.

### 19.42 Peer Trust

A peer must be verified using the security mechanism defined for P2P synchronization.

### 19.43 Authentication and Offline Mode

The application must support the defined offline authentication/session behavior.

Where previously authenticated state is allowed offline:

```text
Valid Local Authentication State
      ↓
Offline Access
```

must still respect the security policy.

### 19.44 Offline Authentication Limitations

Offline authentication must not grant permissions that cannot be verified locally if the Security Architecture requires Backend verification.

### 19.45 Authentication State

The Android application should maintain an explicit authentication state.

Example:

```text
AUTHENTICATED
UNAUTHENTICATED
SESSION_EXPIRED
DEVICE_REVOKED
```

### 19.46 Authentication State Persistence

Only the minimum required authentication state should persist locally.

Sensitive credentials must not be stored unnecessarily.

### 19.47 Session Expiration

When authentication expires:

```text
Session Expired
      ↓
Protected Operation
      ↓
Authentication Required
```

### 19.48 Automatic Token Refresh

If refresh tokens are supported:

```text
Access Token Expiring
      ↓
Refresh
      ↓
New Access Token
```

Failure must transition the application into the appropriate authentication state.

### 19.49 Concurrent Token Refresh

Multiple simultaneous requests must not create uncontrolled concurrent refresh operations.

A controlled refresh mechanism should coordinate:

```text
Request A
Request B
Request C
      ↓
One Refresh
      ↓
Shared New Token
```

where appropriate.

### 19.50 Authentication API

The Backend Authentication API should expose only the endpoints required by V1.

Potential operations include:

```text
Login
Refresh
Logout
Register
```

according to the selected Authentication model.

### 19.51 Registration

If User registration is part of V1:

```text
Registration Request
      ↓
Validate
      ↓
Create User
      ↓
Create Authentication Credential
      ↓
Create Device Association
      ↓
Return Authentication Result
```

### 19.52 Registration Transaction

User creation and required authentication state should be persisted atomically.

### 19.53 Authentication Repository

Authentication-specific persistence should use a dedicated repository abstraction.

Conceptually:

```text
AuthenticationRepository
```

### 19.54 Authentication Service

AuthenticationService should coordinate:

```text
Credential Verification
User Identity
Device Identity
Token / Session Creation
```

### 19.55 Authentication Provider

The implementation may delegate credential verification to an external or framework-provided Authentication Provider.

### 19.56 Authentication Domain Separation

Authentication infrastructure should not introduce authentication-specific fields directly into unrelated Domain entities unless required.

### 19.57 User Identity Mapping

The authenticated User ID must map consistently to the Domain User.

```text
Authenticated Principal
      ↓
UserId
      ↓
Domain User
```

### 19.58 Authentication Error Categories

Errors may include:

```text
INVALID_CREDENTIALS
AUTHENTICATION_REQUIRED
SESSION_EXPIRED
TOKEN_EXPIRED
TOKEN_INVALID
DEVICE_REVOKED
AUTHENTICATION_RATE_LIMITED
```

The final error codes must follow the API Error Design.

### 19.59 Authorization Boundary

Authentication only establishes identity.

It must not be treated as authorization.

```text
Authentication
      ↓
Who Are You?

Authorization
      ↓
What Can You Do?
```

### 19.60 Authentication and Authorization Flow

Protected operation:

```text
Request
  ↓
Authenticate
  ↓
Build Authentication Context
  ↓
Authorize
  ↓
Validate Domain Operation
  ↓
Execute
```

### 19.61 Authentication Logging

Authentication events may be logged for security monitoring.

Logs should avoid:

```text
Passwords
Tokens
Secrets
Private Keys
```

### 19.62 Security Monitoring

Authentication monitoring should identify suspicious activity such as:

```text
Repeated Failed Login
Unexpected Device
Repeated Token Failure
Revoked Device Attempts
```

according to the security requirements.

### 19.63 Authentication Testing

Authentication must have dedicated tests.

```text
Unit Tests
Integration Tests
API Tests
Security Tests
```

### 19.64 Credential Tests

Test:

```text
Valid Credential
Invalid Credential
Missing Credential
Malformed Credential
Expired Credential
```

### 19.65 Token Tests

Test:

```text
Valid Token
Expired Token
Invalid Token
Malformed Token
Revoked Token
```

### 19.66 Device Authentication Tests

Test:

```text
Registered Device
Unknown Device
Revoked Device
Valid User + Device
Invalid User + Device
```

### 19.67 Authentication API Tests

Test:

```text
Login
Refresh
Logout
Registration
Rate Limiting
```

where supported.

### 19.68 Offline Authentication Tests

Test:

```text
Previously Authenticated
Offline Application Start
Expired Session
Revoked Device
Restricted Operation
```

according to the offline security policy.

### 19.69 P2P Authentication Tests

Test:

```text
Trusted Peer
Unknown Peer
Invalid Authentication
Revoked Peer
Replay Attempt
```

where supported by the P2P protocol.

### 19.70 Authentication Security Tests

Test:

```text
Credential Enumeration
Brute Force
Token Replay
Token Expiration
Device Revocation
Unauthorized Access
```

### 19.71 Authentication Implementation Completion Criteria

Authentication Implementation is complete when:

```text
Authentication Flow Implemented
Credential Verification Implemented
Authentication Context Implemented
Token / Session Management Implemented
Device Integration Implemented
Backend Middleware Implemented
Android Secure Storage Implemented
Logout Implemented
Expiration Handling Implemented
Rate Limiting Implemented
P2P Authentication Integrated
Offline Authentication Policy Implemented
Security Tests Passing
```

### 19.72 Authentication Invariants

The following rules are mandatory:

- Authentication must establish User identity.
- Device identity and User identity must remain separate concepts.
- Authentication must occur before protected operations.
- Authentication must not be treated as authorization.
- Authorization must be performed separately for protected resources.
- Authentication tokens must not be treated as Device IDs.
- Device IDs alone must never authenticate a User.
- Passwords must never be stored in plain text.
- Passwords must never appear in logs.
- Authentication tokens must be securely stored on Android.
- Authentication secrets must be securely managed on the Backend.
- Authentication tokens must use protected transport.
- Expired authentication credentials must not authorize protected requests.
- Revoked Devices must not perform protected synchronization operations.
- Logout must clear/invalidate the appropriate authentication state.
- Device revocation must be stronger than normal logout.
- Authentication failures must not unnecessarily reveal sensitive account information.
- Authentication endpoints must have appropriate abuse protection.
- Offline authentication must follow the defined Security Architecture.
- P2P authentication must establish peer trust before synchronization.
- P2P authentication must not rely solely on Device ID or network identifiers.
- Authentication context must be independent of HTTP implementation details.
- Authentication failures must be mapped consistently.
- Authentication implementation must be covered by automated security tests.

## 20. Authorization Implementation

### 20.1 Purpose

This section defines the implementation of Authorization in SplitSync V1.

Authorization determines whether an authenticated User or Device is allowed to perform a specific operation on a specific resource.

Conceptually:

```text
Authenticated User
        +
Resource
        +
Action
        ↓
Authorization
        ↓
Allowed / Denied
```

### 20.2 Authorization Principle

Authentication answers:

```text
Who are you?
```

Authorization answers:

```text
What are you allowed to do?
```

These responsibilities must remain separate.

### 20.3 Authorization Components

Authorization may include:

```text
Authorization Service
Permission Model
Role Model
Membership Checks
Resource Ownership Checks
Authentication Context
```

### 20.4 Authorization Context

Authorization decisions should use an explicit context.

Conceptually:

```text
AuthorizationContext
├── User ID
├── Device ID
├── Roles
└── Authentication State
```

### 20.5 Authorization Input

An authorization decision should consider:

```text
Authenticated User
Resource
Action
Group
Membership
Role
Resource Ownership
```

where applicable.

### 20.6 Authorization Action

Actions should be represented explicitly.

Examples:

```text
CREATE
READ
UPDATE
DELETE
ADD_MEMBER
REMOVE_MEMBER
SYNC
RESOLVE_CONFLICT
```

### 20.7 Authorization Resource

Resources may include:

```text
GROUP
MEMBERSHIP
EXPENSE
SETTLEMENT
SYNC_OPERATION
CONFLICT
DEVICE
```

### 20.8 Group Authorization

Group operations must verify the requesting User's Membership and required role/permission.

Conceptually:

```text
User
 ↓
Group Membership
 ↓
Role / Permission
 ↓
Group Operation
```

### 20.9 Group Owner Authorization

The Group Owner should receive the permissions explicitly defined by the Authorization Model.

### 20.10 Group Member Authorization

A normal Group Member should receive only the permissions granted to that Membership role.

### 20.11 Expense Authorization

Expense operations must verify that the User has permission to access the associated Group and Expense.

```text
Expense
  ↓
Group
  ↓
Membership
  ↓
Permission
```

### 20.12 Expense Creation Authorization

Before creating an Expense:

```text
Authenticated User
      ↓
Group Membership
      ↓
Create Expense Permission
      ↓
Allowed
```

### 20.13 Expense Update Authorization

Before updating an Expense:

```text
Authenticated User
      ↓
Expense Group
      ↓
Membership / Role
      ↓
Update Permission
```

### 20.14 Expense Delete Authorization

Deleting an Expense must require the appropriate permission.

### 20.15 Settlement Authorization

Settlement operations must verify:

```text
User
+
Group Membership
+
Settlement Permission
```

### 20.16 Settlement Update Authorization

If Settlement updates are supported, the requesting User must have the appropriate permission.

### 20.17 Settlement Delete Authorization

Settlement deletion must be authorized according to the Group permission model.

### 20.18 Membership Authorization

Membership changes are privileged operations.

```text
Add Member
      ↓
Authorization
      ↓
Membership Permission
```

### 20.19 Add Member Authorization

Only Users with the required Group permission may add Members.

### 20.20 Remove Member Authorization

Only Users with the required Group permission may remove Members.

### 20.21 Owner Protection

Authorization must enforce Group owner rules.

For example:

```text
Remove Owner
      ↓
Authorization / Domain Rule
      ↓
Rejected
```

when the Domain prohibits owner removal.

### 20.22 Device Authorization

Device operations should verify:

```text
Authenticated User
+
Device Ownership
+
Device State
```

where applicable.

### 20.23 Device Revocation Authorization

A User should only be allowed to revoke Devices according to the defined Device security rules.

### 20.24 Sync Authorization

Synchronization must require a valid authenticated/trusted context.

Conceptually:

```text
Sync Request
      ↓
Authenticate
      ↓
Validate Device
      ↓
Authorize Sync
```

### 20.25 Sync Resource Authorization

A synchronization request must not allow a User to modify resources outside their authorized Groups.

### 20.26 Incoming Operation Authorization

Incoming SyncOperations should be checked against:

```text
Authenticated User
+
Authenticated Device
+
Operation Source
+
Target Resource
```

### 20.27 P2P Authorization

P2P synchronization must establish peer trust before allowing synchronization operations.

```text
Peer Authentication
      ↓
Peer Authorization
      ↓
Synchronization
```

### 20.28 P2P Resource Authorization

A trusted peer must still only synchronize resources it is authorized to access.

### 20.29 Conflict Authorization

Conflict resolution is a protected operation.

```text
Conflict
      ↓
Authenticated User
      ↓
Required Permission
      ↓
Resolve
```

### 20.30 Conflict Ownership

Conflict resolution permissions should follow the Group authorization rules.

### 20.31 Authorization Service

A dedicated AuthorizationService should centralize authorization decisions where appropriate.

Conceptually:

```text
AuthorizationService
      ↓
Permission Evaluation
      ↓
Allow / Deny
```

### 20.32 Authorization Policy

Authorization rules should be represented explicitly rather than scattered throughout Controllers and Repositories.

### 20.33 Controller Authorization

Controllers may enforce coarse authorization boundaries.

Business/resource authorization should be handled through the Application/Security layer.

### 20.34 Application Service Authorization

Application Services should receive the authenticated context and perform the authorization required for their use cases.

### 20.35 Repository Authorization

Repositories must not be treated as the primary authorization boundary.

They may enforce technical data access constraints but should not replace Application authorization.

### 20.36 Domain Authorization

The Domain should contain intrinsic business rules that affect whether an operation is valid.

Authorization-specific infrastructure should remain outside the Domain.

### 20.37 Authorization and Domain Validation

The preferred order is:

```text
Authenticate
      ↓
Authorize
      ↓
Domain Validation
      ↓
Execute
```

### 20.38 Not Found vs Forbidden

The API should follow a consistent policy for cases where revealing resource existence may expose information.

The exact behavior must follow the Security Architecture.

### 20.39 Authorization Failure

Authorization failures should return a consistent application/API error.

Example:

```text
FORBIDDEN
```

### 20.40 Authentication vs Authorization Failure

The implementation must distinguish:

```text
Unauthenticated
      ↓
Authentication Required

Authenticated but Not Allowed
      ↓
Forbidden
```

### 20.41 Permission Model

Permissions should be explicit.

Examples:

```text
GROUP_READ
GROUP_UPDATE
MEMBER_ADD
MEMBER_REMOVE
EXPENSE_CREATE
EXPENSE_READ
EXPENSE_UPDATE
EXPENSE_DELETE
SETTLEMENT_CREATE
SETTLEMENT_UPDATE
SETTLEMENT_DELETE
CONFLICT_RESOLVE
DEVICE_MANAGE
SYNC
```

Only permissions required by V1 should be implemented.

### 20.42 Role Model

If roles are used, roles should map to explicit permissions.

Conceptually:

```text
Role
 ↓
Permissions
```

### 20.43 Role Example

Conceptually:

```text
OWNER
 ↓
Group Management
Member Management
Expense Management
Settlement Management

MEMBER
 ↓
Allowed Financial Operations
```

The exact permissions must follow the V1 Authorization Model.

### 20.44 Role Storage

If roles are persisted, Membership should contain the required role representation.

### 20.45 Permission Evaluation

Authorization should evaluate:

```text
User
+
Membership
+
Role
+
Action
+
Resource
```

### 20.46 Authorization Caching

Authorization data may be cached for performance, but cache invalidation must occur when:

```text
Membership Changes
Role Changes
Device Revocation
User Access Changes
```

### 20.47 Offline Authorization

Offline authorization must use the locally available authorization state according to the security policy.

### 20.48 Offline Permission Changes

If authorization state changes remotely while a Device is offline:

```text
Remote Permission Change
      ↓
Device Offline
      ↓
Old Local State
```

the application must follow the defined security policy.

Sensitive operations may require Backend confirmation.

### 20.49 Backend Authorization

Backend authorization is authoritative for protected server-side resources.

```text
Client Authorization
      +
Backend Authorization
```

Client-side authorization must never be treated as sufficient security.

### 20.50 Client Authorization

Android authorization checks are primarily for:

```text
UI Behavior
Early Validation
User Experience
```

They must not replace Backend authorization.

### 20.51 Backend Trust Boundary

The Backend must assume that:

```text
Client Can Be Modified
Client Can Be Malicious
Requests Can Be Forged
```

Therefore every protected API operation must perform required authorization independently.

### 20.52 API Authorization

Protected API endpoints must enforce authorization before executing mutations.

### 20.53 API Authorization Flow

```text
HTTP Request
      ↓
Authentication
      ↓
Authorization
      ↓
Application Service
      ↓
Domain
      ↓
Repository
```

### 20.54 Resource Ownership

Where ownership is part of the Domain rules, authorization must verify ownership.

Example:

```text
Expense
      ↓
Creator / Owner
      ↓
Allowed Action
```

### 20.55 Group Scope

Financial resources must be evaluated within their Group scope.

```text
Expense
      ↓
Group
      ↓
User Membership
```

### 20.56 Cross-Group Access Prevention

A User authorized for:

```text
Group A
```

must not automatically receive access to:

```text
Group B
```

### 20.57 IDOR Prevention

The Backend must not trust resource IDs supplied by the client.

Incorrect:

```text
GET /expenses/{id}
      ↓
Return Expense
```

Preferred:

```text
Expense ID
      +
Authenticated User
      +
Group Authorization
      ↓
Access Decision
```

### 20.58 Membership Enumeration

Membership endpoints must not expose unauthorized Group membership data.

### 20.59 Device Enumeration

Device management must expose only Devices the authenticated User is authorized to manage.

### 20.60 Authorization and Synchronization

Synchronization must not bypass normal resource authorization.

### 20.61 Authorization and Idempotency

An already-applied SyncOperation must still belong to an authorized source/context.

Idempotency must not become a mechanism for bypassing authorization.

### 20.62 Authorization and Transactions

Authorization should be evaluated before entering expensive or sensitive mutation transactions where possible.

### 20.63 Authorization Logging

Security-relevant authorization failures may be logged.

Logs must avoid unnecessary sensitive data.

### 20.64 Authorization Monitoring

The system should monitor suspicious patterns such as:

```text
Repeated Forbidden Requests
Cross-Group Access Attempts
Revoked Device Access
Invalid Resource Access
```

### 20.65 Authorization Testing

Authorization must have dedicated tests.

```text
Unit Tests
Integration Tests
API Tests
Security Tests
```

### 20.66 Group Authorization Tests

Test:

```text
Owner Allowed
Member Allowed
Unauthorized User
Cross-Group User
```

### 20.67 Expense Authorization Tests

Test:

```text
Create Allowed
Create Denied
Read Allowed
Read Denied
Update Allowed
Update Denied
Delete Allowed
Delete Denied
```

### 20.68 Settlement Authorization Tests

Test:

```text
Create Allowed
Create Denied
Update Allowed
Update Denied
Delete Allowed
Delete Denied
```

### 20.69 Membership Authorization Tests

Test:

```text
Add Allowed
Add Denied
Remove Allowed
Remove Denied
Owner Protection
```

### 20.70 Device Authorization Tests

Test:

```text
Own Device
Other User Device
Revoked Device
Device Management Permission
```

### 20.71 Synchronization Authorization Tests

Test:

```text
Authorized Sync
Unauthorized Sync
Cross-Group Operation
Unknown Device
Revoked Device
```

### 20.72 P2P Authorization Tests

Test:

```text
Trusted Peer
Unknown Peer
Unauthorized Group
Revoked Peer
```

### 20.73 IDOR Tests

Test that changing resource IDs in API requests cannot bypass authorization.

### 20.74 Authorization Regression Tests

All discovered security authorization issues should become permanent regression tests.

### 20.75 Authorization Implementation Completion Criteria

Authorization Implementation is complete when:

```text
Authorization Context Implemented
Permission Model Implemented
Role Model Implemented
Group Authorization Implemented
Expense Authorization Implemented
Settlement Authorization Implemented
Membership Authorization Implemented
Device Authorization Implemented
Sync Authorization Implemented
P2P Authorization Implemented
Backend Authorization Enforced
Offline Authorization Policy Implemented
Security Logging Implemented
Authorization Tests Passing
```

### 20.76 Authorization Invariants

The following rules are mandatory:

- Authentication and Authorization must remain separate.
- Every protected operation must have an authorization decision.
- Backend authorization must be authoritative for server-side resources.
- Client-side authorization must not be treated as a security boundary.
- Group access must be scoped by Membership and required permissions.
- Cross-Group access must be prevented.
- Resource IDs supplied by clients must never bypass authorization.
- Membership changes must require appropriate permission.
- Owner rules must be enforced.
- Expense operations must be authorized.
- Settlement operations must be authorized.
- Device management must be authorized.
- Synchronization operations must be authorized.
- P2P peers must be authenticated and authorized before synchronization.
- Revoked Devices must not perform protected operations.
- Authorization state changes must invalidate stale cached permissions where applicable.
- Authorization failures must be handled consistently.
- Authorization must not be implemented solely inside repositories.
- Domain business validity and authorization must remain separate concerns.
- Authorization must be tested independently and through API integration tests.
- Security-sensitive authorization failures must be observable without exposing unnecessary sensitive information.


## 21. Backend API Implementation

### 21.1 Purpose

This section defines the implementation of the Backend API for SplitSync V1.

The Backend API provides the communication boundary between Android clients and the Backend Application.

Conceptually:

```text
Android
   ↓
HTTPS
   ↓
Backend API
   ↓
Application Services
   ↓
Domain
   ↓
Repositories
```

### 21.2 API Principle

The API should expose application use cases rather than database operations.

Preferred:

```text
Create Expense
Get Group
Sync Changes
Resolve Conflict
```

Not:

```text
Insert Expense Row
Update Database Record
Execute SQL
```

### 21.3 API Transport

V1 Backend API should use:

```text
HTTP
+
HTTPS
+
JSON
```

unless the final Technical Design specifies another transport.

### 21.4 API Base Path

The API should use a versioned base path.

Example:

```text
/api/v1
```

The exact base path must follow the API Design.

### 21.5 API Version

V1 endpoints should be explicitly versioned.

Conceptually:

```text
/api/v1/...
```

### 21.6 API Controllers

Controllers should be responsible for:

```text
Request Reception
DTO Parsing
Basic Input Validation
Authentication Context Access
Application Service Invocation
Response Mapping
```

### 21.7 Controller Responsibilities

Controllers must not contain:

```text
Complex Business Rules
Financial Calculations
Synchronization Algorithms
Database Queries
```

### 21.8 Request DTO

API requests should use dedicated Request DTOs.

Examples:

```text
CreateGroupRequest
UpdateGroupRequest
CreateExpenseRequest
UpdateExpenseRequest
CreateSettlementRequest
AddMemberRequest
SyncRequest
ResolveConflictRequest
```

### 21.9 Response DTO

Responses should use dedicated Response DTOs.

Examples:

```text
GroupResponse
ExpenseResponse
SettlementResponse
MembershipResponse
SyncResponse
ConflictResponse
```

### 21.10 DTO Separation

API DTOs must remain separate from:

```text
Domain Entities
Database Entities
Room Entities
```

### 21.11 API Mapping

The flow should be:

```text
Request DTO
      ↓
Mapper
      ↓
Application Command
```

and:

```text
Application Result
      ↓
Mapper
      ↓
Response DTO
```

### 21.12 Authentication Middleware

Protected API requests should pass through centralized Authentication middleware.

```text
Request
  ↓
Authentication
  ↓
Authenticated Context
  ↓
Controller
```

### 21.13 Authorization

After authentication:

```text
Authentication
      ↓
Authorization
      ↓
Application Service
```

must be enforced for protected operations.

### 21.14 Public Endpoints

Only explicitly defined public endpoints may bypass authentication.

Examples may include:

```text
Health
Authentication
Registration
```

according to V1.

### 21.15 Group API

The Group API should support the required operations.

Conceptually:

```text
POST   /groups
GET    /groups/{groupId}
GET    /groups
PUT    /groups/{groupId}
DELETE /groups/{groupId}
```

Only endpoints required by V1 should be implemented.

### 21.16 Membership API

Membership operations may include:

```text
POST   /groups/{groupId}/members
GET    /groups/{groupId}/members
DELETE /groups/{groupId}/members/{userId}
```

The exact endpoint contract must follow the API Design.

### 21.17 Expense API

Expense operations may include:

```text
POST   /groups/{groupId}/expenses
GET    /groups/{groupId}/expenses
GET    /expenses/{expenseId}
PUT    /expenses/{expenseId}
DELETE /expenses/{expenseId}
```

### 21.18 Settlement API

Settlement operations may include:

```text
POST   /groups/{groupId}/settlements
GET    /groups/{groupId}/settlements
GET    /settlements/{settlementId}
PUT    /settlements/{settlementId}
DELETE /settlements/{settlementId}
```

### 21.19 Balance API

Balance retrieval may expose:

```text
GET /groups/{groupId}/balances
```

The response should contain derived balance information.

### 21.20 Sync API

Synchronization should expose an endpoint designed around the SyncOperation model.

Conceptually:

```text
POST /sync
```

or another versioned synchronization endpoint defined by the API Design.

### 21.21 Sync Request

A synchronization request may contain:

```text
Device ID
Cursor
Operations
Client Version
Protocol Version
```

according to the Synchronization Data Model.

### 21.22 Sync Response

A synchronization response may contain:

```text
Applied Operations
Rejected Operations
Incoming Operations
New Cursor
Conflicts
Sync State
```

according to the synchronization protocol.

### 21.23 Sync Authentication

The Sync API must verify:

```text
Authenticated User
+
Registered Device
+
Device State
```

before processing operations.

### 21.24 Sync Authorization

The Backend must verify that the operations belong to resources the authenticated User/Device is authorized to synchronize.

### 21.25 Sync Idempotency

The Sync API must support idempotent operation processing.

```text
Operation ID
      ↓
Already Applied?
      ↓
Return Existing Result
```

### 21.26 Conflict API

If conflicts require explicit client resolution, the API may expose:

```text
GET  /conflicts
GET  /conflicts/{conflictId}
POST /conflicts/{conflictId}/resolve
```

according to the final API contract.

### 21.27 Device API

Device management may expose:

```text
POST   /devices
GET    /devices
DELETE /devices/{deviceId}
```

where required.

### 21.28 Authentication API

Authentication endpoints depend on the selected Authentication model.

Potential endpoints include:

```text
POST /auth/login
POST /auth/refresh
POST /auth/logout
POST /auth/register
```

### 21.29 HTTP Methods

HTTP methods should represent the intended operation.

Conceptually:

```text
GET
    Read

POST
    Create / Command

PUT
    Replace / Update

PATCH
    Partial Update, if supported

DELETE
    Delete / Tombstone Command
```

### 21.30 HTTP Status Codes

The API should use standard HTTP status semantics.

Examples:

```text
200 OK
201 Created
204 No Content
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
422 Unprocessable Entity
429 Too Many Requests
500 Internal Server Error
```

Only applicable statuses should be used.

### 21.31 Validation Error

Invalid requests should return a structured validation error.

### 21.32 Authentication Error

Unauthenticated requests should return the appropriate authentication error.

### 21.33 Authorization Error

Authenticated but unauthorized requests should return the appropriate forbidden response.

### 21.34 Not Found Error

Requests for resources that do not exist should use the defined Not Found policy.

### 21.35 Conflict Error

Version conflicts should use the defined conflict response.

Conceptually:

```text
409 Conflict
```

where appropriate.

### 21.36 Error Response

All API errors should follow a consistent structure.

Conceptually:

```text
{
    code,
    message,
    details,
    requestId
}
```

The exact response fields must follow the API Design.

### 21.37 Error Code

Error codes should be stable machine-readable identifiers.

Examples:

```text
INVALID_REQUEST
AUTHENTICATION_REQUIRED
FORBIDDEN
RESOURCE_NOT_FOUND
VERSION_CONFLICT
INVALID_SPLIT
INVALID_SETTLEMENT
SYNC_OPERATION_REJECTED
```

### 21.38 Error Message

Messages should be safe for clients and must not expose:

```text
Stack Traces
SQL
Secrets
Internal Infrastructure
```

### 21.39 Request ID

Each API request should have a traceable Request ID.

Conceptually:

```text
Request
      ↓
Request ID
      ↓
Logs
      ↓
Response
```

### 21.40 Correlation ID

Synchronization operations may additionally use:

```text
Correlation ID
```

to trace a complete synchronization request.

### 21.41 API Logging

API logs should capture operational metadata such as:

```text
Request ID
Endpoint
HTTP Method
Status
Latency
Authenticated User ID
Device ID
```

where appropriate.

Sensitive credentials must never be logged.

### 21.42 API Logging Restrictions

Never log:

```text
Password
Access Token
Refresh Token
Private Key
Authentication Secret
```

### 21.43 API Timeout

Backend endpoints should have appropriate timeout limits.

Long-running operations should not block HTTP requests unnecessarily.

### 21.44 API Pagination

Collection endpoints should support pagination where required.

Examples:

```text
Groups
Members
Expenses
Settlements
Conflicts
```

### 21.45 Pagination Request

Pagination may use:

```text
page
size
cursor
```

depending on the API design.

### 21.46 Pagination Response

The response should provide enough information for the client to request the next page.

### 21.47 API Filtering

Filtering should be limited to supported query parameters.

Example:

```text
Group ID
Date Range
State
```

### 21.48 API Sorting

Sorting must be deterministic.

The API must not rely on unspecified database ordering.

### 21.49 API Request Size

Requests must have controlled size limits.

This is particularly important for:

```text
Sync Requests
Expense Payloads
P2P-related API Data
```

### 21.50 Sync Batch Size

Synchronization requests should limit the number of operations per request.

### 21.51 API Rate Limiting

Rate limiting should protect:

```text
Authentication
Synchronization
Expensive Queries
Public Endpoints
```

according to the security and deployment requirements.

### 21.52 API Idempotency

Mutation endpoints that may be retried should support idempotency where required.

Synchronization must always support operation-level idempotency.

### 21.53 API Transaction Boundary

Controllers must not define low-level transaction logic.

Transactions should be coordinated by the Application Service layer.

```text
Controller
      ↓
Application Service
      ↓
Transaction
```

### 21.54 API and Domain

API validation must not replace Domain validation.

```text
API Validation
      ↓
Application
      ↓
Domain Validation
```

### 21.55 API and Repository

Controllers must never directly access repositories.

Preferred:

```text
Controller
      ↓
Application Service
      ↓
Repository
```

### 21.56 API and Synchronization

The Sync API should use the same synchronization Domain/Application logic as other synchronization mechanisms.

### 21.57 API and P2P

P2P communication should not bypass Domain synchronization logic simply because it does not use HTTP.

Both should converge on:

```text
Synchronization Application Service
```

### 21.58 API Versioning

Breaking changes must require a new API version.

Example:

```text
/api/v1
/api/v2
```

### 21.59 Backward Compatibility

Non-breaking V1 changes should preserve existing client behavior.

### 21.60 Field Addition

Adding optional response fields should generally remain backward compatible.

### 21.61 Field Removal

Removing fields used by existing clients should require an appropriate versioning strategy.

### 21.62 Field Renaming

Field renaming should be treated as a compatibility-sensitive change.

### 21.63 API Contract

The API contract should be documented and kept synchronized with the implementation.

### 21.64 API Schema

An API schema such as OpenAPI may be used to document:

```text
Endpoints
Requests
Responses
Authentication
Errors
```

### 21.65 API Client

Android should access the Backend through a dedicated Remote Data Source/API client.

Conceptually:

```text
Repository
      ↓
RemoteDataSource
      ↓
ApiClient
      ↓
Backend
```

### 21.66 API Client Models

Android API models should remain separate from:

```text
Room Entities
Domain Models
```

### 21.67 API Response Mapping

Remote response:

```text
API DTO
      ↓
Mapper
      ↓
Domain Model
```

### 21.68 API Request Mapping

Domain/Application input:

```text
Domain / Application Model
      ↓
Mapper
      ↓
API Request DTO
```

### 21.69 Network Failure

Network failures should be mapped into application-level errors.

Examples:

```text
NETWORK_UNAVAILABLE
TIMEOUT
SERVER_UNAVAILABLE
```

### 21.70 API Retry

Retry behavior should be controlled.

Safe retry candidates may include:

```text
Transient Network Failure
Timeout
Temporary Server Failure
```

### 21.71 Unsafe Retry

Mutations must not be blindly retried unless idempotency is guaranteed.

### 21.72 Authentication Retry

Authentication failures should not be retried indefinitely.

### 21.73 API Security

All protected API communication must use:

```text
HTTPS
Authentication
Authorization
Input Validation
Rate Limiting
```

according to the Security Architecture.

### 21.74 API Input Security

The Backend must validate:

```text
Input Size
Data Type
Length
Allowed Values
Identifiers
Nested Objects
```

### 21.75 API Output Security

Responses must expose only fields required by the client.

### 21.76 Sensitive Field Protection

API responses must not expose:

```text
Password Hash
Authentication Secrets
Private Keys
Internal Security Metadata
```

### 21.77 API Database Isolation

API controllers must not expose database structure directly.

### 21.78 API Concurrency

Concurrent requests must be handled through:

```text
Transactions
Version Checks
Idempotency
Conflict Detection
```

### 21.79 API Health Endpoint

A health endpoint may be provided for deployment monitoring.

It should not expose sensitive internal information.

### 21.80 API Metrics

The Backend should collect metrics such as:

```text
Request Count
Latency
Error Rate
Status Codes
Sync Requests
Authentication Failures
```

### 21.81 API Testing

Backend API implementation must include:

```text
Controller Tests
Integration Tests
Authentication Tests
Authorization Tests
Contract Tests
Error Tests
```

### 21.82 Group API Tests

Test:

```text
Create
Read
Update
Delete
Authorization
Validation
```

### 21.83 Expense API Tests

Test:

```text
Create
Read
Update
Delete
Invalid Split
Authorization
Version Conflict
```

### 21.84 Settlement API Tests

Test:

```text
Create
Read
Update
Delete
Authorization
Validation
Version Conflict
```

### 21.85 Membership API Tests

Test:

```text
Add
List
Remove
Authorization
Duplicate Membership
```

### 21.86 Sync API Tests

Test:

```text
Push
Pull
Duplicate Operation
Conflict
Cursor
Retry
Batch
```

### 21.87 Authentication API Tests

Test:

```text
Login
Refresh
Logout
Invalid Credentials
Expired Token
Revoked Device
```

### 21.88 API Security Tests

Test:

```text
Unauthorized Request
Forbidden Request
Cross-Group Access
IDOR
Token Replay
Rate Limiting
Invalid Input
```

### 21.89 API Contract Tests

Verify that implementation matches the documented API contract.

### 21.90 API Integration Tests

Test the complete flow:

```text
HTTP Request
      ↓
Authentication
      ↓
Authorization
      ↓
Controller
      ↓
Application Service
      ↓
Domain
      ↓
Repository
      ↓
Database
      ↓
HTTP Response
```

### 21.91 API Implementation Completion Criteria

Backend API Implementation is complete when:

```text
API Versioning Implemented
Controllers Implemented
Request DTOs Implemented
Response DTOs Implemented
Authentication Integrated
Authorization Integrated
Group API Implemented
Membership API Implemented
Expense API Implemented
Settlement API Implemented
Balance API Implemented
Sync API Implemented
Conflict API Implemented
Device API Implemented
Error Handling Implemented
Pagination Implemented
Rate Limiting Implemented
API Security Implemented
API Documentation Implemented
Integration Tests Passing
Contract Tests Passing
```

### 21.92 Backend API Invariants

The following rules are mandatory:

- API endpoints must represent application use cases rather than database operations.
- Controllers must remain thin.
- Controllers must not contain core business logic.
- Controllers must not directly access repositories.
- API DTOs must remain separate from Domain and persistence entities.
- Protected endpoints must authenticate requests.
- Protected endpoints must authorize requests.
- Client-side authorization must never replace Backend authorization.
- Resource IDs must not bypass authorization.
- Cross-Group access must be prevented.
- Domain validation must remain authoritative for business rules.
- API errors must use a consistent structure.
- Authentication secrets must never be logged.
- API tokens must never be logged.
- Protected communication must use HTTPS.
- Mutation retries must be idempotent where required.
- SyncOperations must always be processed idempotently.
- Version conflicts must be represented consistently.
- Pagination must be used for potentially large collections.
- API request sizes must be controlled.
- Rate limiting must protect sensitive/expensive endpoints.
- API versioning must protect backward compatibility.
- Breaking API changes must require an appropriate versioning strategy.
- API responses must not expose sensitive internal data.
- API transactions must be coordinated by Application Services.
- API and P2P synchronization must converge on the same logical synchronization rules.
- API behavior must be covered by integration and security tests.

## 22. Android Data Layer Implementation

### 22.1 Purpose

This section defines the implementation of the Android Data Layer in SplitSync V1.

The Android Data Layer is responsible for:

```text
Local Persistence
Remote API Communication
Synchronization Persistence
Repository Implementation
Data Mapping
Offline-First Data Access
```

The Data Layer must isolate infrastructure details from the Domain and Application layers.

### 22.2 Data Layer Principle

The dependency direction should remain:

```text
Presentation
      ↓
Application / Domain
      ↓
Repository Interface
      ↓
Data Layer Implementation
      ↓
Local / Remote Data Sources
```

### 22.3 Data Layer Components

The Android Data Layer should contain:

```text
Repository Implementations
Local Data Sources
Remote Data Sources
Room DAOs
Room Entities
API Clients
DTOs
Mappers
Sync Data Sources
```

### 22.4 Repository Implementation

Repository implementations coordinate:

```text
Local Data Source
+
Remote Data Source
+
Synchronization State
```

according to the Offline-First strategy.

Conceptually:

```text
ExpenseRepositoryImpl
        ↓
 ┌──────┴──────┐
 ↓             ↓
Local        Remote
```

### 22.5 Repository Interface

Repository interfaces must remain independent of Android infrastructure.

Example:

```text
ExpenseRepository
```

is defined at the appropriate Domain/Application boundary, while:

```text
ExpenseRepositoryImpl
```

belongs to the Data Layer.

### 22.6 Local Data Source

The Local Data Source provides access to the local database.

Examples:

```text
ExpenseLocalDataSource
GroupLocalDataSource
SettlementLocalDataSource
MembershipLocalDataSource
SyncLocalDataSource
```

### 22.7 Remote Data Source

The Remote Data Source communicates with Backend APIs.

Examples:

```text
ExpenseRemoteDataSource
GroupRemoteDataSource
SettlementRemoteDataSource
SyncRemoteDataSource
```

### 22.8 Local Data Source Responsibility

Local Data Sources should handle:

```text
DAO Access
Local Queries
Local Inserts
Local Updates
Local Deletes
```

They should not contain core business rules.

### 22.9 Remote Data Source Responsibility

Remote Data Sources should handle:

```text
API Requests
API Responses
HTTP Errors
Network Errors
DTO Conversion
```

### 22.10 Room Database

Room should be used as the Android local persistence implementation according to the Database Architecture.

Conceptually:

```text
RoomDatabase
    ├── DAOs
    └── Entities
```

### 22.11 Room Entity

Room Entities represent persistence state.

They must remain separate from Domain Models.

```text
Room Entity
    ≠
Domain Entity
```

### 22.12 Expense Entity

The Expense Room Entity should persist the required Expense state.

Conceptually:

```text
ExpenseEntity
├── Expense ID
├── Group ID
├── Creator ID
├── Amount
├── Currency
├── Description
├── Created At
├── Updated At
├── Version
└── State
```

### 22.13 Expense Split Entity

ExpenseSplits should be persisted according to the Local Database Schema.

Conceptually:

```text
ExpenseSplitEntity
├── Split ID
├── Expense ID
├── User ID
└── Amount
```

### 22.14 Group Entity

The Group Entity should contain the persistence representation of Group state.

### 22.15 Membership Entity

The Membership Entity should represent:

```text
User ID
Group ID
Role
State
Version
```

where required.

### 22.16 Settlement Entity

The Settlement Entity should represent the persistence state of Settlement.

### 22.17 SyncOperation Entity

The SyncOperation Entity must persist pending synchronization operations.

Conceptually:

```text
SyncOperationEntity
├── Operation ID
├── Device ID
├── Entity Type
├── Entity ID
├── Operation Type
├── Payload
├── Version
├── State
├── Retry Count
└── Created At
```

The exact fields must follow the Sync Data Model.

### 22.18 SyncState Entity

SyncState must persist the local synchronization position/state.

Conceptually:

```text
SyncStateEntity
├── Context
├── Cursor
├── Last Sync
└── State
```

according to the Synchronization Data Model.

### 22.19 Conflict Entity

Conflict state must be persisted when required.

Conceptually:

```text
ConflictEntity
├── Conflict ID
├── Entity Type
├── Entity ID
├── Local Version
├── Remote Version
├── State
└── Metadata
```

### 22.20 Device Identity Storage

Device Identity must be stored independently from normal business entities.

Secure application storage should be used where required.

### 22.21 Local Database Transactions

Financial mutations must use Room transactions where multiple records must remain consistent.

Example:

```text
Create Expense
├── Expense
├── Expense Splits
└── SyncOperation
```

### 22.22 Repository Transaction Boundary

Repository implementations should expose operations that preserve the required Application transaction boundary.

### 22.23 Local Mutation Flow

Offline-first mutation:

```text
Application Service
      ↓
Repository
      ↓
Room Transaction
 ┌────┼─────────────┐
 ↓    ↓             ↓
Entity Splits   SyncOperation
 └────┼─────────────┘
      ↓
   Commit
```

### 22.24 Local Read Flow

```text
Presentation
      ↓
Application / Domain
      ↓
Repository
      ↓
Local Data Source
      ↓
Room DAO
      ↓
Room Database
```

### 22.25 Remote Read Flow

When remote data is required:

```text
Repository
      ↓
Remote Data Source
      ↓
API Client
      ↓
Backend
```

### 22.26 Remote-to-Local Flow

Remote data should generally be persisted locally before being exposed as the current local state.

```text
Backend
   ↓
Remote DTO
   ↓
Mapper
   ↓
Domain / Persistence Model
   ↓
Room
   ↓
Local State
```

### 22.27 Offline-First Principle

For supported operations:

```text
Local State
      ↓
Source for UI
```

must not depend on continuous Backend availability.

### 22.28 Local Database as Offline Source

The local database should contain enough information to support:

```text
Group Viewing
Expense Viewing
Expense Creation
Expense Update
Settlement Viewing
Settlement Creation
Balance Calculation
Synchronization
```

according to the V1 offline requirements.

### 22.29 Repository Read Strategy

Repositories should prefer local state for normal application reads.

Conceptually:

```text
UI Request
      ↓
Repository
      ↓
Local Database
      ↓
Current State
```

### 22.30 Synchronization Refresh

When Backend data changes:

```text
Remote Changes
      ↓
Sync
      ↓
Local Database Updated
      ↓
Local Observers Notified
```

### 22.31 Local Data Observability

Where reactive UI updates are required, the Data Layer should expose observable local state through the selected Android architecture.

Possible mechanisms include:

```text
Flow
StateFlow
LiveData
```

The project should use the mechanism defined by the Android architecture.

### 22.32 Local Source of Truth

For normal UI state:

```text
Room
  ↓
Repository
  ↓
Presentation
```

should be the preferred flow.

### 22.33 Network Availability

Network availability must not be used as the sole indicator of data validity.

The repository should handle:

```text
Available
Unavailable
Timeout
Server Failure
```

explicitly.

### 22.34 Remote Failure

If a remote request fails:

```text
Network Failure
      ↓
Keep Valid Local State
      ↓
Return Appropriate Result
```

where the operation supports offline behavior.

### 22.35 Local Mutation During Network Failure

A supported mutation should still be committed locally when offline.

```text
Mutation
      ↓
Local Transaction
      ↓
SyncOperation = PENDING
```

### 22.36 Sync Queue

The Sync Data Source should expose pending operations.

Conceptually:

```text
PENDING
   ↓
SYNCING
   ↓
APPLIED
```

or:

```text
PENDING
   ↓
FAILED
```

according to the Sync State Model.

### 22.37 Sync Queue Processing

The synchronization worker should:

```text
Load Pending Operations
      ↓
Batch Operations
      ↓
Send to Backend
      ↓
Process Response
      ↓
Update Sync State
```

### 22.38 Sync Retry

Transient failures should result in retryable state.

### 22.39 Sync Retry Backoff

Retries should use controlled backoff rather than continuously retrying.

### 22.40 Sync Operation Atomicity

Creating a local business mutation and its SyncOperation must occur in one transaction.

### 22.41 Duplicate Sync Operations

The local Data Layer must prevent accidental creation of duplicate operations for the same logical mutation.

### 22.42 Remote DTO Handling

Remote DTOs must not be persisted directly unless the persistence model is intentionally identical and the architecture explicitly permits it.

### 22.43 DTO Mapping

The mapping flow should be:

```text
Remote DTO
      ↓
Mapper
      ↓
Persistence Entity
```

or:

```text
Remote DTO
      ↓
Mapper
      ↓
Domain Model
```

depending on the operation.

### 22.44 Entity Mapping

Room Entities should map through dedicated mappers.

Conceptually:

```text
ExpenseEntity
      ↓
ExpenseMapper
      ↓
Expense Domain Model
```

### 22.45 Domain-to-Entity Mapping

```text
Domain Model
      ↓
Mapper
      ↓
Room Entity
```

### 22.46 Mapping Rules

Mappers must preserve:

```text
IDs
Money
Currency
Timestamps
Versions
States
Relationships
```

### 22.47 Money Mapping

Money must retain exact precision when mapped between:

```text
Domain
Room
API
```

### 22.48 Timestamp Mapping

Timestamp conversion must be deterministic and timezone-safe.

### 22.49 Version Mapping

Entity versions must map without loss.

Version information is required for:

```text
Concurrency
Synchronization
Conflict Detection
```

### 22.50 State Mapping

Domain state and persistence state must have explicit mapping rules.

Unknown remote states must not silently map to an incorrect local state.

### 22.51 Database Migration

Room schema changes must use explicit migrations.

Production data must not be lost during normal schema upgrades.

### 22.52 Migration Testing

Every migration must be tested against:

```text
Previous Schema
      ↓
Migration
      ↓
New Schema
```

### 22.53 Repository Error Mapping

Infrastructure errors should be translated into application-level errors.

Example:

```text
SQLException
      ↓
PersistenceFailure
```

### 22.54 Network Error Mapping

Network-specific failures should be mapped to:

```text
NetworkUnavailable
Timeout
ServerFailure
AuthenticationFailure
AuthorizationFailure
Conflict
```

where applicable.

### 22.55 Authentication Error Handling

If a remote request returns an authentication failure:

```text
API
 ↓
Authentication Failure
 ↓
Session Handling
```

must follow the Authentication implementation.

### 22.56 Authorization Error Handling

A Backend authorization failure must not be converted into a successful local mutation.

### 22.57 Conflict Response Handling

When synchronization returns a conflict:

```text
Remote Conflict
      ↓
Conflict Data
      ↓
Local Conflict Persistence
      ↓
Conflict Resolution Flow
```

### 22.58 Data Consistency

The Data Layer must not expose partially persisted financial state.

### 22.59 Financial Transaction

The following must remain atomic where applicable:

```text
Expense
+
ExpenseSplits
+
SyncOperation
```

and:

```text
Settlement
+
SyncOperation
```

### 22.60 Group Transaction

Group creation must preserve:

```text
Group
+
Initial Membership
+
SyncOperation
```

### 22.61 Membership Transaction

Membership mutations and required synchronization state must remain consistent.

### 22.62 Local Delete Strategy

Deletes must follow the synchronization strategy.

Where tombstones are required:

```text
ACTIVE
   ↓
DELETED
```

must be represented rather than physically removing the record.

### 22.63 Database Cleanup

Physical cleanup of synchronized tombstones must only occur when the synchronization protocol confirms that the records are no longer required.

### 22.64 Data Layer Security

The Data Layer must protect:

```text
Authentication State
Device Identity
Local Financial Data
Synchronization Metadata
```

### 22.65 Local Data Security

Sensitive local data must follow the Security Architecture.

### 22.66 API Security

Remote communication must use:

```text
HTTPS
Authenticated Requests
Certificate / Transport Security
```

according to the Security Architecture.

### 22.67 Data Layer Logging

Data Layer logs should contain operational information only.

Avoid logging:

```text
Passwords
Tokens
Full Financial Payloads
Sensitive Personal Data
```

### 22.68 Data Layer Testing

The Android Data Layer must include:

```text
Repository Tests
DAO Tests
Database Tests
Mapper Tests
Remote Data Source Tests
Synchronization Tests
Migration Tests
```

### 22.69 DAO Tests

DAO tests must verify:

```text
Insert
Read
Update
Delete
Queries
Indexes
Relationships
```

### 22.70 Repository Tests

Repository tests must verify:

```text
Local Read
Local Mutation
Remote Read
Remote Mutation
Offline Behavior
Error Mapping
Synchronization
```

### 22.71 Mapper Tests

Mapper tests must verify:

```text
Domain → Entity
Entity → Domain
DTO → Entity
DTO → Domain
```

### 22.72 Offline Repository Tests

Test:

```text
No Network
Local Read
Local Create
Local Update
Local Delete
Pending Sync
```

### 22.73 Remote Repository Tests

Test:

```text
Successful Request
Timeout
Network Failure
Authentication Failure
Authorization Failure
Server Error
Conflict
```

### 22.74 Synchronization Repository Tests

Test:

```text
Pending Operations
Batching
Successful Sync
Retry
Duplicate Operation
Conflict
Cursor Update
```

### 22.75 Data Migration Tests

Every schema migration must have automated tests.

### 22.76 Data Layer Performance

The Data Layer should minimize:

```text
Unnecessary Database Reads
Repeated Mapping
Large In-Memory Collections
Unnecessary Network Requests
```

### 22.77 Database Indexing

Indexes must follow the Local Database Schema and actual query patterns.

Important fields may include:

```text
Group ID
Expense ID
Settlement ID
User ID
Version
Updated At
Sync State
```

### 22.78 Database Pagination

Large queries should use pagination or incremental loading.

### 22.79 Data Layer Completion Criteria

Android Data Layer Implementation is complete when:

```text
Repository Implementations Completed
Local Data Sources Completed
Remote Data Sources Completed
Room Entities Completed
DAOs Completed
API Client Completed
DTOs Completed
Mappers Completed
Offline-First Flow Completed
Sync Queue Integrated
Transactions Implemented
Error Mapping Implemented
Migration Strategy Implemented
Security Integrated
Tests Passing
```

### 22.80 Android Data Layer Invariants

The following rules are mandatory:

- Repository interfaces must remain independent of Android infrastructure.
- Repository implementations belong to the Data Layer.
- Room Entities must remain separate from Domain Models.
- API DTOs must remain separate from Domain Models.
- Local Data Sources must not contain core business rules.
- Remote Data Sources must not contain core business rules.
- Normal application reads should prefer the local database.
- Supported offline mutations must not require Backend availability.
- Local business mutations and required SyncOperations must be atomic.
- Financial state must never be partially persisted.
- Expense and ExpenseSplits must remain consistent.
- Settlement and SyncOperation state must remain consistent.
- Group, initial Membership, and required SyncOperation state must remain consistent.
- Synchronization must update local state through the same persistence boundaries.
- Duplicate synchronization operations must not create duplicate financial effects.
- Version information must be preserved across mappings.
- Monetary precision must be preserved across Domain, Database, and API layers.
- Timestamp conversion must be deterministic.
- Database schema changes must use explicit migrations.
- Tombstones must not be physically removed before synchronization permits cleanup.
- Infrastructure errors must be mapped to appropriate application-level errors.
- Sensitive authentication and financial data must not be unnecessarily logged.
- Local data security must follow the Security Architecture.
- Repository, DAO, Mapper, Migration, and Synchronization behavior must be covered by automated tests.


## 23. Android Presentation Layer Implementation

### 23.1 Purpose

This section defines the implementation of the Android Presentation Layer in SplitSync V1.

The Presentation Layer is responsible for:

```text
UI Rendering
User Interaction
UI State
Navigation
Loading State
Error State
User Feedback
```

It must consume application/domain data without directly accessing:

```text
Room
DAO
Retrofit / HTTP Client
Backend API
```

### 23.2 Presentation Layer Principle

The dependency direction should remain:

```text
UI
 ↓
ViewModel / Presentation Logic
 ↓
Application / Domain
 ↓
Repository
```

### 23.3 Presentation Components

The Presentation Layer should contain:

```text
Screens
UI Components
ViewModels
UI State
UI Events
Navigation
Presentation Mappers
```

according to the Android Architecture.

### 23.4 Screen Architecture

Each major feature should have a dedicated screen/state boundary.

Examples:

```text
Group Screen
Expense List Screen
Expense Detail Screen
Create Expense Screen
Settlement Screen
Balance Screen
Sync / Conflict Screen
Settings Screen
```

### 23.5 ViewModel

ViewModels should coordinate UI state and user actions.

They should not directly access:

```text
Room DAO
Retrofit
Android Database
```

### 23.6 ViewModel Responsibility

A ViewModel may handle:

```text
UI State
User Events
Use Case Invocation
Loading State
Error State
Navigation Events
```

### 23.7 ViewModel Dependency

Preferred:

```text
ViewModel
    ↓
Application Use Case
    ↓
Repository
```

### 23.8 UI State

Each screen should expose an explicit UI state.

Conceptually:

```text
UiState
├── Loading
├── Content
└── Error
```

The exact representation may use sealed classes or another project-standard approach.

### 23.9 Loading State

The UI must represent loading explicitly.

```text
Loading
   ↓
Content
```

### 23.10 Empty State

Screens displaying collections should distinguish:

```text
Loading
      ≠
Empty
```

### 23.11 Error State

Recoverable errors should be represented explicitly.

Examples:

```text
Network Error
Validation Error
Authorization Error
Conflict
Unknown Error
```

### 23.12 UI Event

User interactions should be represented as explicit events.

Examples:

```text
CreateExpense
UpdateExpense
DeleteExpense
CreateSettlement
AddMember
Refresh
Retry
```

### 23.13 Unidirectional Data Flow

The preferred Presentation flow is:

```text
User Action
    ↓
UI Event
    ↓
ViewModel
    ↓
Use Case
    ↓
Repository
    ↓
State Update
    ↓
UI
```

### 23.14 State Observation

UI should observe state from the ViewModel.

Conceptually:

```text
ViewModel
    ↓
StateFlow / Flow
    ↓
UI
```

according to the Android architecture.

### 23.15 Local Data Observation

Where local data is reactive:

```text
Room
 ↓
Repository
 ↓
Use Case
 ↓
ViewModel
 ↓
UI
```

### 23.16 Offline-First UI

The UI should display available local data even when the Backend is unavailable.

```text
Backend Unavailable
      ↓
Local Data
      ↓
UI Continues
```

### 23.17 Offline Mutation UI

When a mutation succeeds locally but is not synchronized yet, the UI should represent the appropriate pending state.

Example:

```text
Expense Created
      ↓
Local State Updated
      ↓
Sync Pending
```

### 23.18 Sync Status

Where required, UI may expose:

```text
Synced
Syncing
Pending
Failed
Conflict
```

### 23.19 Expense Screen

Expense screens should display:

```text
Expense Amount
Description
Payer
Participants
Splits
Date
Sync State
```

as required by the UI design.

### 23.20 Expense Creation UI

The Create Expense screen should collect:

```text
Amount
Description
Payer
Participants
Split Type
Split Values
```

where required.

### 23.21 Expense Validation UI

UI should provide immediate validation feedback for obvious invalid input.

Examples:

```text
Empty Amount
Invalid Amount
Invalid Split
Missing Participant
```

### 23.22 Domain Validation UI

UI validation must not replace Domain validation.

```text
UI Validation
      ↓
Early Feedback

Domain Validation
      ↓
Authoritative Rule
```

### 23.23 Expense Split UI

The UI should support the split types defined by V1.

Conceptually:

```text
Equal
Custom
```

### 23.24 Split Total Feedback

For custom splits, the UI should clearly indicate:

```text
Expense Total
Allocated Total
Remaining Amount
```

where useful.

### 23.25 Balance Screen

The Balance screen should display the derived balance state.

Conceptually:

```text
User A → Receives
User B → Pays
User C → Settled
```

### 23.26 Balance Refresh

Balance should update automatically when underlying:

```text
Expenses
ExpenseSplits
Settlements
```

change.

### 23.27 Settlement UI

The Settlement screen should allow supported Users to:

```text
Select Payer
Select Receiver
Enter Amount
Confirm Settlement
```

### 23.28 Settlement Validation UI

The UI should validate:

```text
Payer
Receiver
Amount
```

before invoking the use case.

### 23.29 Group Screen

The Group screen may display:

```text
Group Name
Members
Balances
Expenses
Settlements
Sync State
```

according to the UI requirements.

### 23.30 Membership UI

Membership management UI should be shown only to Users with the required permission.

```text
Authorized
      ↓
Show Management Controls
```

### 23.31 Unauthorized UI

The UI must not assume that hiding a control is sufficient authorization.

Backend/Application authorization remains authoritative.

### 23.32 Conflict UI

If synchronization creates a Conflict:

```text
Conflict Detected
      ↓
Conflict State
      ↓
User Resolution
```

where manual resolution is supported.

### 23.33 Conflict Details

Conflict UI should provide enough information for the User to understand the conflicting state without exposing unnecessary internal synchronization data.

### 23.34 Conflict Resolution Action

Resolution actions should invoke an Application Use Case.

```text
UI
 ↓
ViewModel
 ↓
ResolveConflictUseCase
 ↓
Application
```

### 23.35 Authentication UI

Authentication screens should handle:

```text
Login
Registration
Session Expiration
Logout
```

according to the Authentication model.

### 23.36 Authentication State

The application root navigation should react to:

```text
Authenticated
Unauthenticated
Session Expired
Device Revoked
```

as required.

### 23.37 Navigation

Navigation should be controlled through the Presentation architecture.

The UI should not directly construct Repository or Database dependencies.

### 23.38 Navigation State

Navigation should respond to application state where appropriate.

Example:

```text
Unauthenticated
      ↓
Login

Authenticated
      ↓
Main Application
```

### 23.39 Deep Links

If supported, deep links must still pass through:

```text
Authentication
Authorization
Resource Validation
```

### 23.40 Configuration

Presentation behavior should use the application's configuration/environment strategy.

Hard-coded Backend URLs must not be scattered across UI classes.

### 23.41 Error Presentation

Application errors should be mapped to user-friendly UI messages.

Conceptually:

```text
Application Error
      ↓
Presentation Mapper
      ↓
User Message
```

### 23.42 Technical Error Separation

The UI should not expose technical details such as:

```text
SQL Exception
HTTP Stack Trace
Server Stack Trace
```

### 23.43 Retry UI

Recoverable failures should provide a retry action where appropriate.

```text
Error
 ↓
Retry
 ↓
Use Case
```

### 23.44 Network Error UI

Network failures should not automatically imply that local data is unavailable.

```text
Network Failure
      ↓
Show Local Data
+
Show Sync / Retry State
```

where applicable.

### 23.45 Empty Group UI

An empty Group should have a clear empty state rather than appearing broken.

### 23.46 Empty Expense UI

No Expenses should be represented explicitly.

### 23.47 Empty Settlement UI

No Settlements should be represented explicitly.

### 23.48 Loading Behavior

Loading indicators should not unnecessarily block already available local data.

Preferred:

```text
Existing Local Data
      +
Background Refresh
```

where supported.

### 23.49 UI Responsiveness

Long-running operations must not execute on the main thread.

Examples:

```text
Database Operations
Network Operations
Large Calculations
Synchronization
```

### 23.50 Coroutine Usage

If Kotlin Coroutines are used by the Android architecture, asynchronous work should use structured concurrency.

### 23.51 Lifecycle Awareness

Presentation state collection must respect Android lifecycle boundaries.

### 23.52 Configuration Changes

ViewModels should preserve appropriate UI state across configuration changes.

### 23.53 Process Death

Important application state that must survive process death should be reconstructed from:

```text
Local Database
Persistent State
Authentication State
```

rather than relying solely on in-memory ViewModel state.

### 23.54 UI State Restoration

Transient UI state may use Android's state restoration mechanisms where required.

### 23.55 Presentation Mapping

Domain models should not expose UI-specific formatting.

For example:

```text
Money
      ↓
Presentation Mapper
      ↓
Formatted Currency String
```

### 23.56 Date Formatting

Dates should be formatted only at the Presentation boundary.

The Domain must preserve the underlying timestamp.

### 23.57 Money Formatting

Money formatting must use the correct:

```text
Currency
Locale
Precision
```

without changing the underlying financial value.

### 23.58 Balance Formatting

Positive/negative balances should follow the application's defined sign convention and user-facing wording.

### 23.59 Accessibility

Presentation components should support appropriate:

```text
Content Descriptions
Readable Text
Touch Targets
Semantic Labels
```

according to Android accessibility requirements.

### 23.60 UI Security

Sensitive information should not unnecessarily appear in:

```text
Screenshots
Logs
Clipboard
Accessibility Output
```

where security policy requires protection.

### 23.61 Authentication UI Security

Credential input fields must use appropriate secure input behavior.

### 23.62 Presentation Logging

UI logs should not contain:

```text
Passwords
Tokens
Sensitive Financial Payloads
```

### 23.63 Presentation Testing

Presentation implementation must include:

```text
ViewModel Tests
UI State Tests
Navigation Tests
UI Tests
```

### 23.64 ViewModel Tests

Test:

```text
Initial State
Loading
Success
Error
Retry
User Actions
```

### 23.65 Expense ViewModel Tests

Test:

```text
Load Expenses
Create Expense
Update Expense
Delete Expense
Validation Failure
Offline Mutation
```

### 23.66 Settlement ViewModel Tests

Test:

```text
Load Settlements
Create Settlement
Update Settlement
Delete Settlement
Validation Failure
Offline Mutation
```

### 23.67 Balance ViewModel Tests

Test:

```text
Load Balance
Expense Change
Settlement Change
Refresh
Error
```

### 23.68 Group ViewModel Tests

Test:

```text
Load Group
Load Members
Update Group
Membership Changes
Authorization State
```

### 23.69 Conflict ViewModel Tests

Test:

```text
Load Conflict
Display Conflict
Resolve Conflict
Resolution Failure
Retry
```

### 23.70 Authentication ViewModel Tests

Test:

```text
Login
Invalid Credentials
Session Expiration
Logout
Authentication State
```

### 23.71 UI Tests

Important user flows should have UI tests.

Examples:

```text
Create Group
Create Expense
Split Expense
View Balance
Create Settlement
Add Member
Resolve Conflict
```

### 23.72 Offline UI Tests

Verify that the UI remains functional when:

```text
Network Unavailable
```

for supported offline features.

### 23.73 Synchronization UI Tests

Test:

```text
Pending Sync
Syncing
Sync Success
Sync Failure
Conflict
```

### 23.74 Presentation Performance

UI rendering should avoid unnecessary recomposition/re-rendering.

### 23.75 Large List Performance

Expense and Settlement lists should support efficient rendering and incremental loading.

### 23.76 Presentation Completion Criteria

Android Presentation Layer Implementation is complete when:

```text
Screens Implemented
ViewModels Implemented
UI State Implemented
UI Events Implemented
Navigation Implemented
Authentication UI Implemented
Group UI Implemented
Expense UI Implemented
Settlement UI Implemented
Balance UI Implemented
Membership UI Implemented
Conflict UI Implemented
Offline UI Behavior Implemented
Sync State UI Implemented
Error Presentation Implemented
Accessibility Implemented
Unit Tests Passing
UI Tests Passing
```

### 23.77 Android Presentation Invariants

The following rules are mandatory:

- Presentation must not directly access Room DAOs.
- Presentation must not directly access the Backend API.
- Presentation must not contain core business rules.
- ViewModels must coordinate UI state and user actions.
- UI state must be explicit.
- Loading, Empty, Content, and Error states must be distinguishable.
- UI must follow the defined unidirectional data flow.
- Normal UI reads should originate from local application state.
- Supported offline operations must remain usable without Backend availability.
- UI validation must not replace Domain validation.
- Client-side authorization must not replace Backend authorization.
- Financial values must not be modified by presentation formatting.
- Money formatting must preserve the underlying Domain value.
- Date formatting must occur at the Presentation boundary.
- Network failures must not unnecessarily hide valid local data.
- Long-running operations must not block the Android main thread.
- ViewModels must respect Android lifecycle behavior.
- Important state must be recoverable after process recreation where required.
- Authentication state must control protected navigation.
- Conflict resolution must invoke Application Use Cases.
- Sensitive authentication and financial information must not be unnecessarily logged or exposed.
- Presentation behavior must be covered by ViewModel and UI tests.

## 24. Offline-First Implementation

### 24.1 Purpose

This section defines the implementation of the Offline-First architecture in SplitSync V1.

The application must remain usable for supported operations when the Backend is unavailable.

The fundamental principle is:

```text
Local Database
      ↓
Primary Application State
      ↓
Backend Synchronization
```

### 24.2 Offline-First Principle

The application must not depend on continuous network connectivity for supported core operations.

```text
Network Available
      ↓
Local State + Synchronization

Network Unavailable
      ↓
Local State
```

### 24.3 Local Source of Truth

For Android application behavior, the local database is the primary source of truth for currently available application state.

```text
Room
  ↓
Repository
  ↓
Application
  ↓
Presentation
```

### 24.4 Supported Offline Operations

V1 offline behavior should support the operations defined by the Offline Architecture.

Core operations include:

```text
View Groups
View Members
View Expenses
Create Expense
Update Expense
View Settlements
Create Settlement
Calculate Balances
Queue Synchronization
```

Additional operations must follow their individual feature requirements.

### 24.5 Offline Read Flow

```text
UI
 ↓
ViewModel
 ↓
Use Case
 ↓
Repository
 ↓
Local Data Source
 ↓
Room
 ↓
Current Local State
```

### 24.6 Offline Mutation Flow

```text
User Action
      ↓
Application Service
      ↓
Domain Validation
      ↓
Repository
      ↓
Local Transaction
 ┌────┼─────────────┐
 ↓    ↓             ↓
Entity Related   SyncOperation
Data             = PENDING
 └────┼─────────────┘
      ↓
    Commit
      ↓
UI Updated
```

### 24.7 Offline Mutation Requirement

A supported mutation must not wait for the Backend before updating valid local application state.

### 24.8 Local Validation

Offline mutations must execute the same Domain validation rules as online mutations.

```text
Offline
   ↓
Domain Validation

Online
   ↓
Domain Validation
```

### 24.9 Offline Expense Creation

Expense creation should follow:

```text
Create Expense
      ↓
Validate Group
      ↓
Validate Membership
      ↓
Validate Amount
      ↓
Validate Splits
      ↓
Persist Expense
      ↓
Persist Splits
      ↓
Create SyncOperation
      ↓
Commit
```

### 24.10 Offline Settlement Creation

Settlement creation should follow:

```text
Create Settlement
      ↓
Validate
      ↓
Persist Settlement
      ↓
Create SyncOperation
      ↓
Commit
```

### 24.11 Offline Group Creation

Group creation should follow:

```text
Create Group
      ↓
Create Owner Membership
      ↓
Create SyncOperation
      ↓
Commit
```

### 24.12 Offline Membership Changes

Membership changes that are permitted offline must:

```text
Validate
      ↓
Persist Membership State
      ↓
Create SyncOperation
      ↓
Commit
```

### 24.13 Local Transaction Boundary

Offline mutations must use a transaction whenever multiple records must remain consistent.

Example:

```text
Expense
+
ExpenseSplits
+
SyncOperation
```

must commit together.

### 24.14 Offline SyncOperation

Every supported offline mutation that requires Backend synchronization must create a SyncOperation.

Conceptually:

```text
Local Mutation
      +
SyncOperation
```

### 24.15 SyncOperation State

A newly queued operation should enter:

```text
PENDING
```

according to the Sync State Model.

### 24.16 Offline Queue

The local database acts as the persistent synchronization queue.

```text
Local Mutation
      ↓
SyncOperation
      ↓
PENDING
      ↓
Sync Worker
```

### 24.17 Queue Persistence

Pending operations must survive:

```text
Application Restart
Process Death
Temporary Network Failure
Device Restart
```

where supported by the platform lifecycle.

### 24.18 Application Restart

After restart:

```text
Room
 ↓
Pending SyncOperations
 ↓
Synchronization Worker
```

must continue synchronization.

### 24.19 Network Availability

Network availability should be treated as an input to synchronization rather than a prerequisite for local application functionality.

### 24.20 Network Recovery

When connectivity becomes available:

```text
Network Available
      ↓
Sync Worker
      ↓
Pending Operations
      ↓
Backend
```

### 24.21 Synchronization Trigger

Synchronization may be triggered by:

```text
Network Recovery
Application Start
Periodic Work
User Refresh
Background Work
```

according to the Sync Architecture and Android lifecycle constraints.

### 24.22 Background Synchronization

Background synchronization should use the Android background execution mechanism selected by the project architecture.

### 24.23 Sync Worker

Conceptually:

```text
SyncWorker
      ↓
Load Pending Operations
      ↓
Prepare Batch
      ↓
Send Request
      ↓
Process Response
      ↓
Update Local State
```

### 24.24 Batch Synchronization

Pending operations should be processed in controlled batches.

```text
Pending Operations
      ↓
Batch
      ↓
Backend
```

### 24.25 Batch Size

Batch size must be limited to prevent:

```text
Large Requests
Memory Pressure
Long Transactions
Network Timeouts
```

### 24.26 Operation Ordering

Operations must respect dependency ordering where required.

Example:

```text
Create Group
      ↓
Create Membership
      ↓
Create Expense
```

### 24.27 Dependency Handling

If an operation depends on state that has not yet synchronized:

```text
Dependency Missing
      ↓
Defer Operation
      ↓
Retry Later
```

### 24.28 Out-of-Order Operations

The synchronization layer must not apply operations in an order that creates invalid Domain state.

### 24.29 Offline Updates

If a User updates an entity multiple times while offline:

```text
Update V1
   ↓
Update V2
   ↓
Update V3
```

the synchronization layer should follow the SyncOperation compaction/coalescing strategy defined by V1.

### 24.30 Operation Coalescing

If operation coalescing is supported:

```text
CREATE
+
UPDATE
+
UPDATE
      ↓
Final Synchronizable State
```

may be represented by a reduced operation set.

The behavior must remain deterministic.

### 24.31 No Operation Loss

Operation coalescing must never remove a required financial state transition incorrectly.

### 24.32 Offline Delete

Offline deletion must follow the synchronization-safe deletion strategy.

```text
Delete
   ↓
Tombstone
   ↓
SyncOperation
```

where tombstones are required.

### 24.33 Offline Conflict

Conflicts may occur after an offline mutation reaches the Backend.

```text
Offline Change
      ↓
Local State
      ↓
Sync
      ↓
Remote Conflict
```

### 24.34 Conflict Handling

A conflict must not silently overwrite newer remote state.

```text
Conflict
      ↓
Persist Conflict State
      ↓
Resolution
```

### 24.35 Offline Conflict UI

If manual resolution is required:

```text
Conflict State
      ↓
Presentation
      ↓
User Resolution
      ↓
Application Service
```

### 24.36 Local Balance Calculation

Balance must be calculated from local financial state.

```text
Local Expenses
+
Local Settlements
      ↓
BalanceCalculator
      ↓
Current Balance
```

### 24.37 Offline Balance

Balance calculation must not require Backend availability.

### 24.38 Balance After Local Mutation

After a successful local mutation:

```text
Local Mutation
      ↓
Room Commit
      ↓
Balance Recalculation
      ↓
UI Update
```

### 24.39 Offline UI State

The UI should distinguish between:

```text
Local State
+
Synchronization State
```

For example:

```text
Expense Visible
Sync Pending
```

### 24.40 Offline Sync Indicator

Where required, the UI may show:

```text
Synced
Pending
Syncing
Failed
Conflict
```

### 24.41 Offline Error Handling

A network error should not be treated as a local persistence failure.

```text
Network Failure
      ↓
Local State Remains
      ↓
Sync Pending
```

where the mutation was successfully committed locally.

### 24.42 Local Persistence Failure

If local persistence fails:

```text
Database Failure
      ↓
Mutation Not Committed
      ↓
Error
```

The UI must not display the mutation as successfully persisted.

### 24.43 Transaction Failure

If any required part of an offline mutation fails:

```text
Rollback
```

must prevent partial state.

### 24.44 Offline Authentication

Offline authentication behavior must follow the Authentication and Security architecture.

Previously authenticated Users may continue supported offline operations only when permitted by the security policy.

### 24.45 Offline Authorization

Offline authorization must use the locally available authorization state according to the defined security model.

### 24.46 Remote Authorization Changes

If authorization changes while the Device is offline, the Device may temporarily hold stale authorization state.

Sensitive operations must follow the security policy and may require Backend confirmation.

### 24.47 Device Identity

All offline synchronization operations must use the current Device Identity.

```text
Device ID
      ↓
SyncOperation
```

### 24.48 Offline Operation Identity

Every locally generated SyncOperation must receive a unique Operation ID.

### 24.49 Idempotency

Offline operations must remain idempotent when transmitted multiple times.

```text
Same Operation ID
      ↓
No Duplicate Financial Effect
```

### 24.50 Retry Strategy

Transient synchronization failures should use controlled retries.

```text
FAILED
  ↓
Retry
  ↓
PENDING / SYNCING
```

according to the Sync State Model.

### 24.51 Retry Backoff

Retries should use exponential or otherwise controlled backoff with an appropriate upper limit.

### 24.52 Permanent Failure

Operations that cannot be applied after validation or authorization failure must not retry indefinitely.

```text
Permanent Failure
      ↓
Failed / Conflict State
```

### 24.53 Authentication Failure During Sync

If synchronization fails because authentication has expired:

```text
Sync
 ↓
Authentication Failure
 ↓
Refresh / Re-authenticate
 ↓
Retry
```

according to the Authentication implementation.

### 24.54 Authorization Failure During Sync

If synchronization is rejected because the Device/User is no longer authorized:

```text
Authorization Failure
      ↓
Do Not Blindly Retry
      ↓
Update Local Security / Sync State
```

### 24.55 Server Conflict During Sync

If the Backend detects a version conflict:

```text
Conflict Response
      ↓
Persist Conflict
      ↓
Stop Invalid Overwrite
```

### 24.56 Offline Data Recovery

The local database must remain the persistent state across application restarts.

### 24.57 Crash Recovery

If the application crashes during a mutation transaction:

```text
Transaction
      ↓
Crash
      ↓
Room Recovery
      ↓
Atomic State
```

The application must not contain partial financial state.

### 24.58 Sync Worker Crash Recovery

If the Sync Worker terminates unexpectedly:

```text
SYNCING Operation
      ↓
Worker Termination
      ↓
Recoverable Sync State
      ↓
Retry
```

The exact recovery mechanism must follow the Sync State Model.

### 24.59 Offline Data Consistency

The following must remain consistent:

```text
Domain Entity
+
Related Entities
+
SyncOperation
```

### 24.60 Offline Expense Consistency

```text
Expense
+
ExpenseSplits
+
SyncOperation
```

must remain consistent.

### 24.61 Offline Settlement Consistency

```text
Settlement
+
SyncOperation
```

must remain consistent.

### 24.62 Offline Group Consistency

```text
Group
+
Initial Membership
+
SyncOperation
```

must remain consistent.

### 24.63 Local Database as Durable Queue

The synchronization queue must not depend solely on:

```text
In-Memory Queue
```

because application process termination must not lose pending operations.

### 24.64 Memory Usage

Offline synchronization must process operations in bounded batches and must not load an unbounded queue into memory.

### 24.65 Large Offline Queue

If a Device accumulates a large number of pending operations:

```text
Pending Queue
      ↓
Bounded Batch Processing
      ↓
Progressive Synchronization
```

must be used.

### 24.66 Offline Data Retention

Local data retention must follow the Database and Security Architecture.

### 24.67 Tombstone Retention

Tombstones must remain until the synchronization protocol determines that they can be safely removed.

### 24.68 Offline Logging

Offline logs should provide enough information to diagnose:

```text
Sync Failure
Database Failure
Retry
Conflict
```

without exposing sensitive data.

### 24.69 Offline Metrics

Where telemetry is supported, track:

```text
Pending Operations
Sync Success
Sync Failure
Retry Count
Conflict Count
```

without collecting unnecessary personal/financial data.

### 24.70 Offline Testing

Offline-First implementation must include:

```text
Unit Tests
Repository Tests
Database Tests
Integration Tests
UI Tests
Synchronization Tests
```

### 24.71 Offline Read Tests

Test:

```text
Network Available
Network Unavailable
Local Data Present
Local Data Empty
```

### 24.72 Offline Mutation Tests

Test:

```text
Create Expense Offline
Update Expense Offline
Create Settlement Offline
Create Group Offline
```

according to supported operations.

### 24.73 Offline Restart Tests

Test:

```text
Create Offline
Application Restart
Pending Sync Still Present
Synchronization Continues
```

### 24.74 Offline Crash Tests

Test application termination during:

```text
Local Transaction
Sync Processing
```

and verify consistent recovery.

### 24.75 Offline Retry Tests

Test:

```text
Network Failure
Retry
Network Recovery
Successful Sync
```

### 24.76 Offline Conflict Tests

Test:

```text
Offline Mutation
Remote Mutation
Sync
Conflict
Resolution
Balance Recalculation
```

### 24.77 Offline Authorization Tests

Test behavior when:

```text
User Authorized Offline
Permission Changed Remotely
Device Reconnects
```

### 24.78 Offline Completion Criteria

Offline-First Implementation is complete when:

```text
Local Source of Truth Implemented
Offline Reads Implemented
Offline Mutations Implemented
Local Transactions Implemented
Sync Queue Implemented
Persistent Pending Operations Implemented
Retry Implemented
Backoff Implemented
Conflict Handling Integrated
Offline Balance Implemented
Offline UI State Implemented
Crash Recovery Verified
Restart Recovery Verified
Security Policy Integrated
Tests Passing
```

### 24.79 Offline-First Invariants

The following rules are mandatory:

- Supported core operations must work without continuous Backend availability.
- Local database must be the primary Android application state.
- Offline mutations must use Domain validation.
- Local mutations and required SyncOperations must be atomic.
- Pending SyncOperations must survive application restart.
- Synchronization state must not depend solely on in-memory memory.
- Every synchronizable offline mutation must have a unique Operation ID.
- Duplicate delivery must not create duplicate financial effects.
- Network failure must not erase successfully committed local state.
- Local persistence failure must not report a successful mutation.
- Financial data must never be partially persisted.
- Expense and ExpenseSplits must remain consistent.
- Settlement and SyncOperation state must remain consistent.
- Group, Membership, and SyncOperation state must remain consistent.
- Offline balance calculation must work from local financial state.
- Offline UI must distinguish local state from synchronization state where required.
- Authentication and Authorization rules must remain applicable offline.
- Sensitive authorization changes may require Backend confirmation.
- Tombstones must not be removed before synchronization permits cleanup.
- Retryable synchronization failures must not cause uncontrolled retry loops.
- Permanent failures must not be retried indefinitely.
- Offline conflicts must not silently overwrite newer remote state.
- Crash and process termination must not leave partial financial state.
- Offline synchronization must use the current Device Identity.
- Offline behavior must be covered by automated tests.


## 25. Sync Operation Implementation

### 25.1 Purpose

This section defines the implementation of SyncOperations in SplitSync V1.

A SyncOperation represents a synchronizable change produced by a Device.

Conceptually:

```text
Local Mutation
      ↓
SyncOperation
      ↓
Pending Queue
      ↓
Backend / P2P
```

### 25.2 SyncOperation Principle

The synchronization system must synchronize logical operations/state changes rather than arbitrary database rows.

### 25.3 SyncOperation Identity

Every SyncOperation must have a globally unique:

```text
Operation ID
```

### 25.4 Operation ID Generation

Operation IDs must be generated locally and must support:

```text
Offline Creation
Global Uniqueness
Idempotency
P2P Synchronization
Backend Synchronization
```

### 25.5 SyncOperation Structure

Conceptually:

```text
SyncOperation
├── Operation ID
├── Device ID
├── User ID
├── Entity Type
├── Entity ID
├── Operation Type
├── Base Version
├── Resulting Version
├── Payload
├── Timestamp
└── State
```

The exact fields must follow the Sync Operation Model.

### 25.6 Entity Type

Entity Type identifies the affected Domain resource.

Examples:

```text
GROUP
MEMBERSHIP
EXPENSE
EXPENSE_SPLIT
SETTLEMENT
```

### 25.7 Operation Type

Operation Type identifies the mutation.

Examples:

```text
CREATE
UPDATE
DELETE
```

Membership-specific operations may use the operation model defined by the synchronization design.

### 25.8 Entity ID

Every operation must identify the affected entity.

```text
Entity Type
+
Entity ID
```

must uniquely identify the target resource within the synchronization context.

### 25.9 Device ID

The SyncOperation must identify the Device that generated it.

```text
Device ID
```

is the source identity and must remain separate from:

```text
Operation ID
Version
```

### 25.10 User ID

Where required, the operation should identify the User responsible for the mutation.

### 25.11 Base Version

For version-aware synchronization, the operation should contain the version from which the mutation was produced.

Conceptually:

```text
Base Version = 5
```

### 25.12 Resulting Version

The resulting local version may be included according to the Sync Data Model.

### 25.13 Operation Timestamp

Operations should contain a timestamp according to the synchronization model.

The timestamp must not be treated as the sole conflict-resolution mechanism.

### 25.14 Operation Payload

The payload must contain sufficient information for the receiving side to validate and apply the operation.

### 25.15 Payload Principle

The payload must not expose unnecessary infrastructure-specific data.

### 25.16 Expense Operation Payload

An Expense operation may contain:

```text
Expense ID
Group ID
Creator
Amount
Currency
Description
Timestamp
Version
Splits
State
```

according to the operation type.

### 25.17 Settlement Operation Payload

A Settlement operation may contain:

```text
Settlement ID
Group ID
Payer
Receiver
Amount
Currency
Timestamp
Version
State
```

### 25.18 Group Operation Payload

A Group operation may contain:

```text
Group ID
Name
Owner
Version
State
```

### 25.19 Membership Operation Payload

A Membership operation may contain:

```text
Group ID
User ID
Role
State
Version
```

### 25.20 Operation Creation

A SyncOperation must be created as part of the same transaction as the local mutation.

```text
Mutation
+
SyncOperation
      ↓
Commit
```

### 25.21 Operation State

The operation lifecycle should follow the Sync State Model.

Conceptually:

```text
PENDING
   ↓
SYNCING
   ↓
APPLIED
```

or:

```text
PENDING
   ↓
FAILED
```

with conflict handling where applicable.

### 25.22 PENDING

PENDING means:

```text
Operation Created Locally
+
Not Yet Successfully Synchronized
```

### 25.23 SYNCING

SYNCING means:

```text
Operation Currently Being Processed
```

### 25.24 APPLIED

APPLIED means the synchronization target has acknowledged the operation according to the synchronization protocol.

### 25.25 FAILED

FAILED represents an operation that could not currently be applied.

The failure must be classified as:

```text
Retryable
```

or:

```text
Permanent
```

where required.

### 25.26 CONFLICT

If the operation cannot be safely applied because of concurrent state:

```text
Operation
      ↓
Conflict
```

must be represented according to the Conflict Data Model.

### 25.27 Retryable Failure

Examples:

```text
Network Failure
Timeout
Temporary Server Failure
```

should remain retryable.

### 25.28 Permanent Failure

Examples may include:

```text
Invalid Domain State
Unauthorized Operation
Invalid Resource
Unsupported Operation
```

These must not be retried indefinitely.

### 25.29 SyncOperation Queue

Pending operations must be persisted in the local database.

```text
Room
 ↓
SyncOperation
 ↓
PENDING
```

### 25.30 Queue Retrieval

The synchronization worker should retrieve operations using deterministic ordering.

### 25.31 Operation Ordering

Ordering may consider:

```text
Creation Time
Dependency
Entity Relationship
Sequence
```

according to the synchronization protocol.

### 25.32 Dependency Ordering

Dependent operations must respect required dependencies.

Example:

```text
CREATE GROUP
      ↓
CREATE MEMBERSHIP
      ↓
CREATE EXPENSE
```

### 25.33 Operation Batching

Operations should be sent in bounded batches.

### 25.34 Batch Processing

```text
Load Pending
      ↓
Validate Batch
      ↓
Send
      ↓
Process Individual Results
      ↓
Update States
```

### 25.35 Partial Batch Failure

If some operations succeed and others fail:

```text
Operation A → APPLIED
Operation B → RETRY
Operation C → CONFLICT
```

the system must preserve each operation's independent state.

### 25.36 Batch Transaction

Updating local synchronization state after a response must use appropriate transaction boundaries.

### 25.37 Idempotency

The Backend must recognize duplicate Operation IDs.

```text
Operation ID
      ↓
Already Applied?
      ↓
Return Existing Result
```

### 25.38 P2P Idempotency

P2P receivers must also detect duplicate Operation IDs.

### 25.39 Duplicate Delivery

Duplicate delivery must not create:

```text
Duplicate Expense
Duplicate Settlement
Duplicate Membership
```

or another duplicate business effect.

### 25.40 Operation Acknowledgement

The synchronization target should acknowledge successfully processed operations.

### 25.41 Acknowledgement Handling

After acknowledgement:

```text
PENDING
   ↓
APPLIED
```

and required synchronization metadata should be updated.

### 25.42 Cursor Update

Incoming synchronization data and cursor updates must remain consistent.

```text
Apply Remote Changes
      +
Update Cursor
      ↓
Commit
```

### 25.43 Cursor Safety

A cursor must not advance beyond changes that have not been successfully persisted.

### 25.44 Incoming Operation

Incoming operations should follow:

```text
Receive
   ↓
Authenticate
   ↓
Authorize
   ↓
Validate
   ↓
Check Dependency
   ↓
Check Version
   ↓
Apply / Conflict
   ↓
Persist
```

### 25.45 Incoming Operation Persistence

Incoming changes must be persisted atomically with the required synchronization state.

### 25.46 Version Validation

For version-aware operations:

```text
Incoming Base Version
      +
Current Local Version
      ↓
Compatible?
```

### 25.47 Version Match

If versions are compatible:

```text
Apply Operation
```

### 25.48 Version Mismatch

If versions are incompatible:

```text
Version Mismatch
      ↓
Conflict Detection
```

### 25.49 Conflict Creation

A Conflict record should preserve enough information to support resolution.

Conceptually:

```text
Conflict
├── Conflict ID
├── Entity ID
├── Local State
├── Remote State
├── Local Version
└── Remote Version
```

### 25.50 Conflict Operation State

An operation involved in a conflict must not be marked APPLIED until the conflict is resolved according to the synchronization protocol.

### 25.51 Conflict Resolution

Resolution should follow:

```text
Conflict
      ↓
Resolution Decision
      ↓
Valid Domain State
      ↓
Persist
      ↓
Synchronization State Updated
```

### 25.52 Operation Retry

Retryable operations should return to a retryable state.

### 25.53 Retry Count

The system may track:

```text
Retry Count
```

for operational control.

### 25.54 Retry Limit

Retry limits should prevent infinite processing loops.

### 25.55 Backoff

Retries should use controlled backoff.

### 25.56 Dead-Letter / Permanent Failure

If the architecture uses a dead-letter or permanent failure state, operations that exceed retry policy should move there.

### 25.57 Operation Compaction

Where supported, multiple operations for the same entity may be compacted.

Example:

```text
CREATE
UPDATE
UPDATE
      ↓
Final State Operation
```

### 25.58 Compaction Safety

Compaction must preserve:

```text
Final Entity State
Dependencies
Conflict Detection
Audit Requirements
```

### 25.59 Financial Operation Compaction

Financial operations must not be compacted in a way that changes the financial outcome.

### 25.60 Operation Serialization

Operation payload serialization must be:

```text
Deterministic
Versioned
Compatible
Validated
```

### 25.61 Serialization Format

The synchronization payload format should follow the defined API/Data Model.

JSON may be used where the Backend API uses JSON.

### 25.62 Payload Version

If payload structure evolves, the operation should carry or be associated with the appropriate protocol/schema version.

### 25.63 Unknown Payload Version

Unsupported payload versions must be rejected safely.

### 25.64 Unknown Operation Type

Unknown operation types must not be silently applied.

```text
Unknown Operation
      ↓
Reject
```

### 25.65 Unknown Entity Type

Unknown Entity Types must be rejected safely.

### 25.66 Operation Validation

Before applying an operation, validate:

```text
Operation ID
Device ID
Entity Type
Entity ID
Operation Type
Payload
Version
Authorization
```

### 25.67 Domain Validation

After synchronization-level validation:

```text
Sync Validation
      ↓
Domain Validation
      ↓
Apply
```

### 25.68 Operation Authorization

The receiving side must verify that the operation source is authorized to mutate the target resource.

### 25.69 Operation Authentication

Remote operations must be associated with an authenticated/trusted source.

### 25.70 Operation Replay Protection

Operation IDs and synchronization metadata must prevent malicious or accidental replay.

### 25.71 Operation Source Verification

A received:

```text
Device ID
```

must correspond to the authenticated/trusted source.

### 25.72 Operation and Device Revocation

Operations from revoked Devices must not be accepted for protected synchronization.

### 25.73 Operation Logging

Synchronization logs may contain:

```text
Operation ID
Entity Type
Entity ID
Device ID
Operation Type
Result
```

but must not unnecessarily log full financial payloads.

### 25.74 Operation Metrics

Useful synchronization metrics include:

```text
Pending Count
Applied Count
Failed Count
Conflict Count
Retry Count
Batch Size
Sync Latency
```

### 25.75 Operation Cleanup

Successfully synchronized operations may be cleaned up according to the retention strategy.

Cleanup must not occur before the protocol guarantees that the operation is no longer required.

### 25.76 Operation Audit

If auditability is required, operation metadata must remain available according to the retention policy.

### 25.77 Operation and Local Database

The local SyncOperation table must be indexed for:

```text
Operation ID
State
Device ID
Entity Type
Entity ID
Created At
```

where required.

### 25.78 Operation Query

The Data Layer should provide queries for:

```text
Pending Operations
Retryable Operations
Operations By Entity
Operation By ID
Conflicted Operations
```

### 25.79 Operation Repository

A SyncOperationRepository should abstract persistence and retrieval of synchronization operations.

### 25.80 Sync Service

The SyncService should coordinate:

```text
Operation Retrieval
Batching
Remote Transmission
Response Processing
Retry
Conflict Handling
State Updates
```

### 25.81 Sync Worker

The Android SyncWorker should invoke SyncService rather than implementing the complete synchronization algorithm itself.

### 25.82 Backend Sync Service

The Backend should use a dedicated synchronization service to:

```text
Authenticate
Authorize
Validate
Deduplicate
Apply
Detect Conflict
Return Changes
```

### 25.83 P2P Sync Service

P2P synchronization should use the same logical SyncOperation processing rules.

### 25.84 Sync Protocol Consistency

Backend and P2P synchronization must apply the same:

```text
Operation Validation
Domain Validation
Version Rules
Conflict Rules
Idempotency Rules
```

### 25.85 Operation and Balance

After an Expense or Settlement operation is applied:

```text
Persist Financial State
      ↓
Balance Calculation
```

must reflect the resulting state.

### 25.86 Operation and Membership

Membership operations must affect authorization and future financial operations according to the Group and Membership rules.

### 25.87 Operation and Group Dependency

Group operations must be available before dependent operations are applied where required.

### 25.88 Operation Transaction

The following must be atomic where applicable:

```text
Incoming Operation
+
Domain State Change
+
Sync State
+
Cursor
```

### 25.89 Local Operation Transaction

For local mutations:

```text
Domain State
+
SyncOperation
```

must be atomic.

### 25.90 Remote Operation Transaction

For incoming changes:

```text
Incoming State
+
Applied Operation State
+
Cursor
```

must remain consistent.

### 25.91 Operation Testing

SyncOperation implementation must include:

```text
Unit Tests
Repository Tests
Serialization Tests
Synchronization Tests
Integration Tests
Security Tests
```

### 25.92 Operation Creation Tests

Test:

```text
Unique ID
Correct Entity
Correct Operation Type
Correct Version
Correct Device ID
Correct Payload
```

### 25.93 Operation Queue Tests

Test:

```text
Pending
Ordering
Batching
Retry
Failure
Cleanup
```

### 25.94 Idempotency Tests

Test:

```text
Same Operation Twice
      ↓
One Business Effect
```

### 25.95 Version Tests

Test:

```text
Matching Version
Stale Version
Future Version
Concurrent Update
```

### 25.96 Conflict Tests

Test:

```text
Conflict Detection
Conflict Persistence
Conflict Resolution
Operation State
Balance Recalculation
```

### 25.97 Retry Tests

Test:

```text
Transient Failure
Retry
Backoff
Success
Permanent Failure
```

### 25.98 Dependency Tests

Test:

```text
Group Before Expense
Membership Before Expense
Out-of-Order Operations
Deferred Operation
```

### 25.99 P2P Tests

Test:

```text
Operation Transfer
Duplicate Operation
Unknown Operation
Version Conflict
Peer Authentication
Peer Authorization
```

### 25.100 Operation Security Tests

Test:

```text
Invalid Device
Revoked Device
Unauthorized Entity
Operation Replay
Invalid Payload
Unknown Operation Type
```

### 25.101 Operation Performance Tests

Test:

```text
Large Queue
Large Batch
Multiple Entities
Repeated Synchronization
```

### 25.102 SyncOperation Completion Criteria

Sync Operation Implementation is complete when:

```text
Operation Model Implemented
Operation ID Generation Implemented
Local Queue Implemented
Operation Persistence Implemented
Operation Serialization Implemented
Batch Processing Implemented
Idempotency Implemented
Version Validation Implemented
Conflict Integration Implemented
Retry Implemented
Backoff Implemented
Dependency Handling Implemented
Backend Integration Implemented
P2P Integration Implemented
Security Validation Implemented
Cleanup Strategy Implemented
Tests Passing
```

### 25.103 SyncOperation Invariants

The following rules are mandatory:

- Every SyncOperation must have a unique Operation ID.
- Operation IDs must support offline creation.
- Operation ID, Device ID, and Version must remain separate concepts.
- Every synchronizable mutation must produce the required SyncOperation.
- Local mutation and SyncOperation creation must be atomic.
- SyncOperations must survive application restart until successfully processed or permanently resolved.
- Pending operations must be processed in a controlled manner.
- Operation ordering must respect required dependencies.
- Duplicate Operation IDs must not produce duplicate business effects.
- Incoming operations must be authenticated.
- Incoming operations must be authorized.
- Incoming operations must pass synchronization validation.
- Incoming operations must pass Domain validation.
- Version mismatches must not silently overwrite newer state.
- Conflicting operations must follow the Conflict Resolution architecture.
- Retryable failures must not become permanent failures prematurely.
- Permanent failures must not be retried indefinitely.
- Retry processing must use controlled backoff.
- Unknown operation types must be rejected safely.
- Unknown entity types must be rejected safely.
- Unsupported payload versions must be rejected safely.
- Operation payloads must be validated before application.
- Operation replay must be prevented.
- Revoked Devices must not submit protected synchronization operations.
- Backend and P2P synchronization must use the same logical operation rules.
- Cursor advancement must occur only after required changes are safely persisted.
- Financial operations must not be compacted in a way that changes their financial outcome.
- Incoming financial state and synchronization metadata must remain transactionally consistent.
- Successfully applied operations may only be cleaned up when synchronization no longer requires them.
- SyncOperation implementation must be covered by automated tests.

## 26. Sync Engine Implementation

### 26.1 Purpose

This section defines the implementation of the Sync Engine in SplitSync V1.

The Sync Engine coordinates synchronization between the local Android state and remote synchronization targets.

Conceptually:

```text
Local Database
      ↓
Sync Engine
      ↓
┌─────┴─────┐
↓           ↓
Backend     Peer
```

### 26.2 Sync Engine Responsibility

The Sync Engine is responsible for:

```text
Pending Operation Processing
Push Coordination
Pull Coordination
Operation Ordering
Batching
Retry Handling
Conflict Handling
Sync State Management
Cursor Management
```

### 26.3 Sync Engine Boundary

The Sync Engine belongs to the Application/Data synchronization infrastructure.

It must not contain UI logic.

```text
Presentation
      ↓
Application
      ↓
Sync Engine
      ↓
Data Sources
```

### 26.4 Sync Engine Components

The implementation should contain logical components such as:

```text
SyncEngine
SyncCoordinator
PushProcessor
PullProcessor
SyncOperationRepository
SyncStateRepository
ConflictProcessor
SyncTransport
```

### 26.5 SyncCoordinator

The SyncCoordinator coordinates the complete synchronization cycle.

Conceptually:

```text
SyncCoordinator
      ↓
Push
      ↓
Pull
      ↓
Apply
      ↓
Update Sync State
```

### 26.6 SyncEngine Interface

The Sync Engine should expose a high-level synchronization operation.

Conceptually:

```text
sync()
```

The caller should not need to know the internal push/pull sequence.

### 26.7 Synchronization Cycle

The complete cycle should follow the defined synchronization protocol.

Conceptually:

```text
Start
  ↓
Load Sync State
  ↓
Push Local Changes
  ↓
Pull Remote Changes
  ↓
Apply Remote Changes
  ↓
Update Cursor
  ↓
Complete
```

The exact ordering must follow the final synchronization protocol.

### 26.8 Sync State Loading

At the beginning of synchronization:

```text
SyncState
      ↓
Load Cursor
      ↓
Load Pending Operations
```

### 26.9 Sync State Validation

The Sync Engine must validate that the local synchronization state is internally consistent before processing.

### 26.10 Pending Operation Loading

Pending operations should be loaded using deterministic ordering.

```text
SyncOperationRepository
      ↓
Pending Operations
      ↓
Ordered Batch
```

### 26.11 Batch Preparation

The Sync Engine should prepare bounded batches.

Batching must consider:

```text
Maximum Operation Count
Payload Size
Dependencies
Network Constraints
```

### 26.12 Operation Dependency

Before sending a batch, required dependencies must be satisfied.

Example:

```text
CREATE GROUP
      ↓
CREATE MEMBERSHIP
      ↓
CREATE EXPENSE
```

### 26.13 Sync Transport

The Sync Engine should communicate through an abstraction.

Conceptually:

```text
SyncTransport
├── BackendSyncTransport
└── PeerSyncTransport
```

### 26.14 Transport Independence

The Sync Engine must not depend directly on:

```text
HTTP Client
Bluetooth API
Wi-Fi API
Socket API
```

The transport implementation must remain replaceable.

### 26.15 Backend Synchronization

Backend synchronization uses:

```text
SyncEngine
      ↓
BackendSyncTransport
      ↓
Backend API
```

### 26.16 P2P Synchronization

P2P synchronization uses:

```text
SyncEngine
      ↓
PeerSyncTransport
      ↓
Trusted Peer
```

### 26.17 Common Synchronization Logic

Backend and P2P synchronization must share:

```text
Operation Validation
Idempotency
Version Checking
Conflict Detection
Domain Validation
Sync State Rules
```

### 26.18 Push Phase

The Push phase sends locally generated operations to the synchronization target.

```text
Local Pending Operations
      ↓
Push Processor
      ↓
Transport
      ↓
Remote Target
```

### 26.19 Pull Phase

The Pull phase retrieves remote changes not yet present locally.

```text
Sync Cursor
      ↓
Pull Processor
      ↓
Transport
      ↓
Remote Changes
```

### 26.20 Apply Phase

Remote changes must be applied through the defined synchronization/application logic.

```text
Remote Operation
      ↓
Validate
      ↓
Authorize
      ↓
Version Check
      ↓
Domain Apply
      ↓
Persist
```

### 26.21 Cursor Management

The Sync Engine must maintain a synchronization cursor.

```text
Current Cursor
      ↓
Pull Changes
      ↓
Apply Successfully
      ↓
Advance Cursor
```

### 26.22 Cursor Safety

The cursor must never advance past changes that have not been successfully persisted.

### 26.23 Cursor Transaction

Where applicable:

```text
Apply Remote Changes
+
Update Cursor
      ↓
Single Transaction
```

must be used.

### 26.24 Sync State Update

The Sync Engine must update:

```text
Last Sync
Cursor
Sync Status
Failure State
```

according to the Sync State Model.

### 26.25 Sync Status

The engine may expose:

```text
IDLE
SYNCING
SUCCESS
FAILED
CONFLICT
```

according to the defined Sync State Model.

### 26.26 Concurrent Sync Prevention

Multiple synchronization executions for the same Device/context must not modify synchronization state concurrently without coordination.

### 26.27 Sync Lock

The implementation should use a synchronization lock or equivalent coordination mechanism.

```text
Sync Request A
      ↓
SYNCING

Sync Request B
      ↓
Wait / Skip
```

### 26.28 Application Start

Application startup may trigger synchronization when permitted.

```text
Application Start
      ↓
Load Local State
      ↓
Schedule Sync
```

### 26.29 Manual Sync

A User-triggered refresh may request synchronization.

The UI should not directly execute the synchronization algorithm.

### 26.30 Background Sync

Background synchronization should be scheduled through the Android background execution mechanism selected by the architecture.

### 26.31 Network Constraint

Synchronization should execute only when required network conditions are satisfied.

### 26.32 Offline State

If the network is unavailable:

```text
Sync Request
      ↓
Network Unavailable
      ↓
Keep Pending Operations
      ↓
Retry Later
```

### 26.33 Authentication Handling

If authentication expires:

```text
Sync
 ↓
Authentication Failure
 ↓
Refresh / Re-authenticate
 ↓
Retry
```

according to the Authentication implementation.

### 26.34 Authorization Handling

If synchronization is no longer authorized:

```text
Authorization Failure
      ↓
Stop Invalid Operation
      ↓
Update Sync State
```

### 26.35 Device Revocation

If the Device is revoked:

```text
Device Revoked
      ↓
Synchronization Blocked
```

### 26.36 Retryable Failure

Transient errors should be retried.

Examples:

```text
Network Timeout
Temporary Server Failure
Connection Failure
```

### 26.37 Permanent Failure

Permanent failures must not enter an endless retry loop.

### 26.38 Retry Backoff

The Sync Engine should use controlled retry backoff.

### 26.39 Retry State

Retry metadata may include:

```text
Retry Count
Last Attempt
Next Retry Time
Failure Type
```

### 26.40 Sync Failure Isolation

A failure affecting one operation should not incorrectly mark unrelated successfully processed operations as failed.

### 26.41 Partial Success

The Sync Engine must support partial results.

Example:

```text
Operation A → APPLIED
Operation B → RETRY
Operation C → CONFLICT
```

Each state must be persisted correctly.

### 26.42 Conflict Handling

Conflicts must be delegated to the Conflict Processor.

```text
Version Conflict
      ↓
ConflictProcessor
      ↓
Conflict State
```

### 26.43 Conflict Persistence

Conflict information must be persisted before the Sync Engine reports the conflict state.

### 26.44 Conflict Isolation

One conflict must not corrupt unrelated synchronized entities.

### 26.45 Operation Idempotency

The Sync Engine must rely on Operation IDs for idempotency.

```text
Operation ID
      ↓
Duplicate Detection
      ↓
No Duplicate Effect
```

### 26.46 Remote Operation Deduplication

Incoming operations must be checked against already processed operations where required.

### 26.47 Operation Ordering

The Sync Engine must preserve ordering where the Domain requires it.

### 26.48 Operation Coalescing

If operation coalescing is enabled, the Sync Engine must apply the rules defined in the SyncOperation model.

### 26.49 Financial Safety

The Sync Engine must never merge or reorder operations in a way that changes financial results.

### 26.50 Local Transaction

When applying a remote financial operation:

```text
Financial State
+
Required Related State
+
Sync Metadata
```

must be persisted atomically.

### 26.51 Sync Engine and Balance

After applying financial changes:

```text
Expenses
+
Settlements
      ↓
Balance Calculation
```

must reflect the resulting local state.

### 26.52 Sync Engine and Membership

Membership changes must update the local authorization-relevant state.

### 26.53 Sync Engine and Group

Group changes must be available before dependent operations are applied.

### 26.54 Sync Engine and Tombstones

Deleted entities must remain represented through synchronization-safe tombstones where required.

### 26.55 Tombstone Processing

The Sync Engine must synchronize deletion state before physical cleanup is allowed.

### 26.56 Sync Engine Logging

Logs should include operational identifiers such as:

```text
Sync ID
Operation ID
Device ID
Batch ID
Result
```

without logging sensitive payloads unnecessarily.

### 26.57 Sync ID

Each synchronization execution may have a unique Sync ID for tracing.

Conceptually:

```text
Sync ID
   ↓
Push
   ↓
Pull
   ↓
Apply
```

### 26.58 Batch ID

Each batch may have a unique identifier for diagnostics.

### 26.59 Sync Metrics

The engine may record:

```text
Sync Duration
Push Count
Pull Count
Applied Count
Failed Count
Conflict Count
Retry Count
```

### 26.60 Sync Performance

The Sync Engine should:

```text
Use Bounded Batches
Avoid Unnecessary Reads
Avoid Duplicate Network Requests
Avoid Unbounded Memory Usage
```

### 26.61 Large Queue Handling

A large pending queue must be processed incrementally.

```text
Large Queue
      ↓
Batch 1
      ↓
Batch 2
      ↓
Batch 3
```

### 26.62 Sync Cancellation

Synchronization should support safe cancellation where required by Android lifecycle/background execution.

Cancellation must not leave partially persisted synchronization state.

### 26.63 Sync Crash Recovery

If the Sync Engine stops unexpectedly:

```text
Process Termination
      ↓
Persisted Sync State
      ↓
Next Sync
      ↓
Resume Safely
```

### 26.64 Sync Restart

Synchronization must be safely restartable after:

```text
Application Restart
Device Restart
Worker Restart
Network Recovery
```

### 26.65 Sync State Recovery

The engine must derive recoverable state from persisted data rather than relying solely on in-memory state.

### 26.66 Sync Engine Testing

The Sync Engine must have dedicated tests for:

```text
Push
Pull
Apply
Retry
Conflict
Cursor
Idempotency
Ordering
Crash Recovery
Concurrency
```

### 26.67 Sync Engine Unit Tests

Test:

```text
Batch Creation
Ordering
Retry Decision
Conflict Decision
State Transition
```

### 26.68 Sync Engine Integration Tests

Test:

```text
Local Database
Sync Engine
Transport
Backend
```

as a complete flow.

### 26.69 Sync Engine Offline Tests

Test:

```text
No Network
Pending Queue
Network Recovery
Successful Synchronization
```

### 26.70 Sync Engine Concurrency Tests

Test:

```text
Two Sync Requests
      ↓
Only One Active Synchronization
```

### 26.71 Sync Engine Crash Tests

Test termination during:

```text
Push
Pull
Apply
Cursor Update
```

and verify safe recovery.

### 26.72 Sync Engine Security Tests

Test:

```text
Expired Authentication
Revoked Device
Unauthorized Operation
Invalid Peer
Replay
```

### 26.73 Sync Engine Completion Criteria

Sync Engine Implementation is complete when:

```text
SyncCoordinator Implemented
Push Processing Implemented
Pull Processing Implemented
Apply Processing Implemented
Sync State Management Implemented
Cursor Management Implemented
Concurrency Control Implemented
Retry Implemented
Backoff Implemented
Conflict Integration Implemented
Backend Transport Integrated
P2P Transport Integrated
Crash Recovery Implemented
Security Integrated
Metrics / Logging Implemented
Tests Passing
```

### 26.74 Sync Engine Invariants

The following rules are mandatory:

- The Sync Engine must coordinate synchronization without containing UI logic.
- Backend and P2P synchronization must share the same logical synchronization rules.
- Pending operations must be loaded from durable local storage.
- Synchronization must process operations in deterministic order where required.
- Operation dependencies must be respected.
- Synchronization batches must be bounded.
- Multiple synchronization executions must not corrupt shared SyncState.
- Cursor advancement must occur only after required changes are safely persisted.
- Remote financial changes and required synchronization metadata must be transactionally consistent.
- Duplicate Operation IDs must not create duplicate financial effects.
- Version conflicts must not silently overwrite newer state.
- Conflicts must be persisted before being surfaced as unresolved.
- Retryable failures must use controlled retry/backoff.
- Permanent failures must not be retried indefinitely.
- Device revocation must prevent protected synchronization.
- Authentication failures must follow the Authentication flow.
- Authorization failures must not be blindly retried.
- Tombstones must be retained until synchronization permits cleanup.
- Synchronization must recover safely after application or worker termination.
- Synchronization must not depend solely on in-memory state.
- Financial operation ordering/compaction must not alter financial outcomes.
- Sync Engine behavior must be covered by automated tests.


## 27. Push Synchronization Implementation

### 27.1 Purpose

This section defines the implementation of Push Synchronization in SplitSync V1.

Push Synchronization transfers locally generated SyncOperations from an Android Device to a synchronization target.

```text
Local Device
      ↓
Pending SyncOperations
      ↓
Push Synchronization
      ↓
Backend / Peer
```

### 27.2 Push Principle

Push must transmit only operations that are eligible for synchronization.

```text
PENDING
      ↓
Push
      ↓
Remote Processing
```

### 27.3 Push Entry Point

The Sync Engine should invoke Push Synchronization.

```text
SyncEngine
      ↓
PushProcessor
```

### 27.4 PushProcessor

The PushProcessor is responsible for:

```text
Loading Operations
Ordering
Batching
Serialization
Transmission
Response Processing
State Updates
```

### 27.5 Push Source

The PushProcessor retrieves operations from:

```text
SyncOperationRepository
```

### 27.6 Pushable States

Only operations in an eligible state should be pushed.

Typically:

```text
PENDING
```

and retryable operations according to the Sync State Model.

### 27.7 Syncing State

Before transmission, operations may transition:

```text
PENDING
   ↓
SYNCING
```

to prevent duplicate concurrent processing.

### 27.8 Claiming Operations

The operation claim/update must be performed safely so that multiple workers cannot process the same operations concurrently.

### 27.9 Push Batch

The PushProcessor should create a bounded batch.

```text
Pending Operations
      ↓
Select
      ↓
Order
      ↓
Batch
```

### 27.10 Push Batch Ordering

Operations must be ordered according to:

```text
Dependency
Creation Order
Sequence
```

as required.

### 27.11 Push Dependency Validation

Before transmission, the PushProcessor should verify required dependencies.

Example:

```text
Group CREATE
      ↓
Membership CREATE
      ↓
Expense CREATE
```

### 27.12 Push Serialization

Operations must be converted into the defined synchronization wire representation.

```text
SyncOperation
      ↓
Push DTO
      ↓
JSON / Protocol Payload
```

### 27.13 Push Request

A Push request should contain:

```text
Device Identity
Authentication Context
Protocol Version
Operations
```

according to the API contract.

### 27.14 Push Authentication

Push requests must be authenticated.

### 27.15 Push Authorization

The receiving side must authorize every operation according to the resource and Device/User context.

### 27.16 Push Device Validation

The receiving side must verify:

```text
Authenticated User
+
Registered Device
+
Device State
```

### 27.17 Push Operation Validation

Each operation must be validated before application.

```text
Operation ID
Entity Type
Entity ID
Operation Type
Payload
Version
```

### 27.18 Push Operation ID

The receiving side must use Operation ID for idempotency.

### 27.19 Duplicate Push

If an operation has already been applied:

```text
Push Same Operation
      ↓
Already Applied
      ↓
Return Existing Result
```

No duplicate Domain mutation may occur.

### 27.20 Push Response

The receiving target should return a result for each submitted operation.

Conceptually:

```text
Operation A → APPLIED
Operation B → CONFLICT
Operation C → RETRY
```

### 27.21 Push Result

Possible results include:

```text
APPLIED
ALREADY_APPLIED
RETRY
CONFLICT
REJECTED
```

The exact result enumeration must follow the Sync API contract.

### 27.22 Applied Operation

When the target confirms successful application:

```text
SYNCING
   ↓
APPLIED
```

### 27.23 Already Applied Operation

If the target reports the operation was already applied:

```text
SYNCING
   ↓
APPLIED
```

because the synchronization effect already exists.

### 27.24 Retry Result

For transient failures:

```text
SYNCING
   ↓
PENDING
```

or the equivalent retryable state.

### 27.25 Conflict Result

For a conflict:

```text
SYNCING
   ↓
CONFLICT
```

and Conflict data must be persisted.

### 27.26 Rejected Operation

For permanent rejection:

```text
SYNCING
   ↓
FAILED
```

with an appropriate failure reason.

### 27.27 Push Response Transaction

Local SyncOperation state updates must be transactionally consistent.

### 27.28 Partial Push Success

Each operation must be updated based on its own result.

```text
A → APPLIED
B → RETRY
C → CONFLICT
```

must remain represented independently.

### 27.29 Push Retry

Retryable operations should return to a retryable queue state.

### 27.30 Push Backoff

The Sync Engine must apply controlled retry backoff.

### 27.31 Authentication Failure

If Push returns an authentication failure:

```text
Authentication Failure
      ↓
Refresh / Re-authenticate
      ↓
Retry Push
```

according to the Authentication implementation.

### 27.32 Authorization Failure

If Push returns authorization failure:

```text
Authorization Failure
      ↓
Do Not Blindly Retry
```

The operation should transition to the appropriate failure/security state.

### 27.33 Device Revocation

If the Device is revoked:

```text
Device Revoked
      ↓
Push Blocked
```

### 27.34 Network Failure

If transmission fails because of network unavailability:

```text
SYNCING
   ↓
PENDING
   ↓
Retry Later
```

according to the worker's recovery strategy.

### 27.35 Timeout

A timeout should be treated as potentially ambiguous.

The client must not assume that the operation was never received.

```text
Push
 ↓
Timeout
 ↓
Retry Same Operation ID
```

### 27.36 Timeout Idempotency

Operation ID ensures that retrying after an ambiguous timeout does not duplicate the business effect.

### 27.37 Server Failure

Temporary server failures should return operations to a retryable state.

### 27.38 Permanent Validation Failure

Invalid operations must not be retried indefinitely.

### 27.39 Push Ordering After Failure

If an operation is a dependency for later operations:

```text
Dependency Failed
      ↓
Dependent Operations
      ↓
Defer
```

until the dependency is resolved.

### 27.40 Push Dependency Queue

The PushProcessor must not send dependent operations in a way that causes invalid remote state.

### 27.41 Push Operation Coalescing

If V1 enables operation coalescing, it should occur before batching.

```text
Pending Operations
      ↓
Coalesce
      ↓
Validate Dependencies
      ↓
Batch
```

### 27.42 Push Coalescing Safety

Coalescing must preserve the final valid Domain state and financial outcome.

### 27.43 Push Payload Size

The PushProcessor must enforce request payload limits.

### 27.44 Push Batch Size

Batch size should be configurable within safe operational limits.

### 27.45 Push Progress

For large queues:

```text
Batch 1
 ↓
Batch 2
 ↓
Batch 3
```

should be processed progressively.

### 27.46 Push Queue Cleanup

Successfully applied operations may be removed or archived according to the retention strategy.

### 27.47 Push Cleanup Safety

Cleanup must not occur before the target has acknowledged successful application.

### 27.48 Push Auditability

If operation audit history is required, cleanup must preserve the necessary audit metadata.

### 27.49 Push and Local State

Local financial state should remain available regardless of whether Push has completed.

```text
Local Mutation
      ↓
Local State Updated
      ↓
Push Pending
```

### 27.50 Push and UI

The UI may observe:

```text
PENDING
SYNCING
APPLIED
FAILED
CONFLICT
```

through the local SyncState/operation state.

### 27.51 Push and Balance

Push completion itself must not independently modify balances.

Balance changes must result from applying valid Domain state.

### 27.52 Push and Pull

Push and Pull must coordinate through the Sync Engine.

```text
Push
 ↓
Pull
 ↓
Apply
```

or the protocol-defined sequence.

### 27.53 Push and Cursor

Push should not incorrectly advance the pull cursor.

Push acknowledgements and pull cursors are separate synchronization concepts.

### 27.54 Push and Conflict

A Push conflict must create/update the local Conflict state.

### 27.55 Push Conflict Resolution

Conflict resolution should produce a new valid synchronization state/operation according to the Conflict Resolution architecture.

### 27.56 Push Security

Push must enforce:

```text
Authentication
Authorization
Device Verification
Operation Validation
Replay Protection
Transport Security
```

### 27.57 Push Transport Security

Backend Push must use HTTPS.

P2P Push must use the authenticated secure peer transport defined by the P2P architecture.

### 27.58 Push Logging

Push logs may contain:

```text
Sync ID
Batch ID
Operation ID
Operation Result
Latency
```

but must not log sensitive payloads unnecessarily.

### 27.59 Push Metrics

Useful metrics include:

```text
Push Requests
Operations Sent
Operations Applied
Duplicates
Conflicts
Failures
Retries
Latency
```

### 27.60 Push Concurrency

Only one Push process should claim the same operations for a synchronization context at a time.

### 27.61 Push Worker Safety

If the worker stops after operations are marked SYNCING:

```text
Worker Stops
      ↓
Recovery
      ↓
Recoverable Operations
      ↓
Retry
```

### 27.62 Push Crash Recovery

The implementation must prevent permanently stuck SYNCING operations.

### 27.63 Push State Recovery

On startup or next synchronization, stale SYNCING operations must be detected and recovered according to the Sync State Model.

### 27.64 Push Testing

Push Synchronization must include:

```text
Unit Tests
Integration Tests
API Tests
Security Tests
Failure Tests
Concurrency Tests
```

### 27.65 Push Success Test

Test:

```text
PENDING
 ↓
Push
 ↓
APPLIED
```

### 27.66 Duplicate Push Test

Test:

```text
Push Operation
Push Same Operation Again
      ↓
One Business Effect
```

### 27.67 Push Timeout Test

Test:

```text
Push
 ↓
Timeout
 ↓
Retry
 ↓
Already Applied
 ↓
APPLIED
```

### 27.68 Push Conflict Test

Test:

```text
Push
 ↓
Version Conflict
 ↓
Conflict Persisted
 ↓
Operation Not Marked APPLIED
```

### 27.69 Push Partial Success Test

Test:

```text
A → APPLIED
B → RETRY
C → CONFLICT
```

and verify independent state persistence.

### 27.70 Push Dependency Test

Test:

```text
Group
 ↓
Membership
 ↓
Expense
```

and verify that dependent operations are not incorrectly sent before required dependencies.

### 27.71 Push Retry Test

Test:

```text
Transient Failure
 ↓
Backoff
 ↓
Retry
 ↓
Success
```

### 27.72 Push Authentication Test

Test:

```text
Expired Token
 ↓
Refresh
 ↓
Push
```

### 27.73 Push Authorization Test

Test:

```text
Unauthorized Operation
 ↓
Rejected
 ↓
No Financial Mutation
```

### 27.74 Push Revocation Test

Test:

```text
Active Device
 ↓
Revoked
 ↓
Push
 ↓
Rejected
```

### 27.75 Push Crash Recovery Test

Test termination during:

```text
Operation Claim
Transmission
Response Processing
State Update
```

and verify recoverability.

### 27.76 Push Performance Test

Test:

```text
Small Queue
Large Queue
Large Batch
Repeated Retry
```

### 27.77 Push Completion Criteria

Push Synchronization Implementation is complete when:

```text
Pending Operation Selection Implemented
Operation Claiming Implemented
Dependency Ordering Implemented
Batching Implemented
Serialization Implemented
Authentication Integrated
Authorization Integrated
Idempotency Implemented
Push Response Processing Implemented
Partial Success Handling Implemented
Retry Implemented
Backoff Implemented
Conflict Handling Implemented
Timeout Handling Implemented
Crash Recovery Implemented
Cleanup Strategy Implemented
Security Integrated
Metrics / Logging Implemented
Tests Passing
```

### 27.78 Push Synchronization Invariants

The following rules are mandatory:

- Only eligible SyncOperations may be pushed.
- Operations must be safely claimed before processing.
- Concurrent workers must not process the same operation incorrectly.
- Operation dependencies must be respected.
- Push batches must remain bounded.
- Every pushed operation must retain its unique Operation ID.
- Duplicate Push requests must not create duplicate business effects.
- Timeout retries must use the same Operation ID.
- Authentication must be validated before protected Push processing.
- Authorization must be validated for every protected operation.
- Revoked Devices must not successfully Push protected operations.
- Partial batch results must be processed independently.
- Retryable failures must return to a retryable state.
- Permanent failures must not retry indefinitely.
- Push conflicts must not be marked as successfully applied.
- Conflict information must be persisted before the conflict is surfaced.
- Push acknowledgements must not incorrectly advance the pull cursor.
- Successfully applied operations may only be cleaned up after safe acknowledgement.
- Financial state must not be modified merely because an operation was transmitted.
- Financial state changes must occur only through valid Domain application.
- Stale SYNCING operations must be recoverable after worker/application termination.
- Backend and P2P Push must use the same logical operation validation and idempotency rules.
- Push synchronization must be covered by automated tests.

## 28. Pull Synchronization Implementation

### 28.1 Purpose

This section defines the implementation of Pull Synchronization in SplitSync V1.

Pull Synchronization retrieves remote changes that are not yet present in the local Android state.

```text
Remote Changes
      ↓
Pull Synchronization
      ↓
Local Sync Engine
      ↓
Local Database
```

### 28.2 Pull Principle

Pull Synchronization must retrieve changes incrementally using the synchronization cursor.

```text
Local Cursor
      ↓
Pull Request
      ↓
Remote Changes
      ↓
Apply Locally
      ↓
Advance Cursor
```

### 28.3 Pull Entry Point

The Sync Engine should invoke Pull Synchronization.

```text
SyncEngine
      ↓
PullProcessor
```

### 28.4 PullProcessor

The PullProcessor is responsible for:

```text
Loading Cursor
Requesting Remote Changes
Receiving Operations
Validating Operations
Applying Operations
Updating Cursor
Handling Conflicts
```

### 28.5 Pull Cursor

The local SyncState contains the cursor representing the latest successfully processed remote position.

Conceptually:

```text
SyncState
└── Cursor
```

### 28.6 Initial Pull

For a new Device or synchronization context:

```text
No Cursor
      ↓
Initial Synchronization
      ↓
Retrieve Required Remote State
```

The exact bootstrap behavior must follow the synchronization protocol.

### 28.7 Incremental Pull

For an existing synchronization context:

```text
Cursor = N
      ↓
Request Changes After N
      ↓
Receive N+1 ... M
```

### 28.8 Pull Request

A Pull request should contain the required synchronization context.

Conceptually:

```text
User / Device Identity
Protocol Version
Cursor
Batch Size
```

### 28.9 Pull Authentication

Pull requests must be authenticated.

### 28.10 Pull Authorization

The Backend or trusted peer must verify that the requesting User/Device is authorized to retrieve the requested synchronization data.

### 28.11 Pull Device Validation

The receiving side must verify:

```text
Authenticated User
+
Registered Device
+
Device State
```

where applicable.

### 28.12 Pull Response

A Pull response may contain:

```text
Operations
Next Cursor
Has More
Conflicts
Protocol Metadata
```

according to the synchronization API.

### 28.13 Pull Batch

Remote changes must be retrieved in bounded batches.

```text
Remote Changes
      ↓
Batch
      ↓
Local Processing
```

### 28.14 Batch Size

Pull batch size must be limited to avoid:

```text
Large Responses
Memory Pressure
Long Transactions
Database Locking
```

### 28.15 Pull Ordering

Remote operations must be returned and processed in deterministic synchronization order.

### 28.16 Operation Validation

Every incoming operation must be validated before it is applied.

```text
Incoming Operation
      ↓
Structural Validation
      ↓
Authentication
      ↓
Authorization
      ↓
Domain Validation
```

### 28.17 Operation Identity

The Pull Processor must use Operation ID to detect duplicate operations.

### 28.18 Duplicate Remote Operation

If an operation has already been applied:

```text
Incoming Operation
      ↓
Operation ID Already Known
      ↓
Skip Duplicate Effect
```

### 28.19 Duplicate Safety

Duplicate remote delivery must never create duplicate:

```text
Expenses
Settlements
Memberships
Groups
```

or other financial effects.

### 28.20 Remote Version

The Pull Processor must preserve the version associated with the incoming state.

### 28.21 Version Validation

Before applying an incoming operation:

```text
Remote Base Version
      +
Local Current Version
      ↓
Version Check
```

### 28.22 Compatible Version

If the incoming operation is compatible:

```text
Version Valid
      ↓
Apply Operation
```

### 28.23 Version Conflict

If the incoming operation conflicts with local state:

```text
Version Mismatch
      ↓
Conflict Processor
```

### 28.24 Conflict Persistence

Conflict information must be persisted before the Pull operation is considered successfully handled.

### 28.25 Conflict Isolation

A conflict for one entity must not corrupt unrelated entities.

### 28.26 Remote Operation Application

The application flow should be:

```text
Remote Operation
      ↓
Validate
      ↓
Authorize
      ↓
Check Duplicate
      ↓
Check Version
      ↓
Domain Validation
      ↓
Apply
      ↓
Persist
```

### 28.27 Local Transaction

Applying a remote operation must use a transaction where multiple records are affected.

Example:

```text
Expense
+
ExpenseSplits
+
Synchronization Metadata
```

must remain consistent.

### 28.28 Remote Expense Application

An incoming Expense operation may require:

```text
Expense
+
ExpenseSplits
+
Group Validation
```

to be persisted consistently.

### 28.29 Remote Settlement Application

An incoming Settlement operation must preserve:

```text
Settlement
+
Synchronization State
```

consistency.

### 28.30 Remote Group Application

An incoming Group operation must preserve the required Group state.

### 28.31 Remote Membership Application

Membership changes must update local authorization-relevant state.

### 28.32 Balance Update

After applying financial changes:

```text
Expenses
+
Settlements
      ↓
Local Balance
```

must reflect the resulting state.

### 28.33 Pull and Local UI

Once remote changes are committed:

```text
Room
 ↓
Repository
 ↓
ViewModel
 ↓
UI
```

should expose the updated state.

### 28.34 Pull and Offline State

Pull only executes when the required transport is available.

If the Device is offline:

```text
No Network
      ↓
Keep Current Local State
      ↓
Retry Later
```

### 28.35 Pull Retry

Transient Pull failures should be retried.

Examples:

```text
Network Failure
Timeout
Temporary Server Failure
```

### 28.36 Pull Backoff

Retries must use controlled backoff.

### 28.37 Pull Authentication Failure

If authentication has expired:

```text
Pull
 ↓
Authentication Failure
 ↓
Refresh / Re-authenticate
 ↓
Retry
```

according to the Authentication implementation.

### 28.38 Pull Authorization Failure

If the Device/User is no longer authorized:

```text
Authorization Failure
      ↓
Stop Pull
      ↓
Update Security / Sync State
```

### 28.39 Device Revocation

A revoked Device must not continue receiving protected synchronization data.

### 28.40 Pull Timeout

A timeout must not cause the local cursor to advance.

```text
Timeout
      ↓
Cursor Unchanged
```

### 28.41 Pull Partial Response

If only part of a batch is successfully persisted:

```text
Applied Changes
      +
Cursor
```

must remain consistent.

The cursor must advance only to the last safely persisted remote position.

### 28.42 Cursor Advancement

The cursor must advance only after all changes represented by that cursor have been successfully handled.

```text
Receive
 ↓
Apply
 ↓
Commit
 ↓
Advance Cursor
```

### 28.43 Cursor Transaction

Where supported by the local database:

```text
Apply Remote Changes
+
Update Cursor
      ↓
Atomic Transaction
```

must be used.

### 28.44 Cursor Failure

If cursor persistence fails:

```text
Remote Changes
      ↓
Transaction Rollback
      ↓
Cursor Unchanged
```

### 28.45 Duplicate After Cursor Failure

If changes are received again after cursor failure:

```text
Same Operation ID
      ↓
Duplicate Detection
      ↓
No Duplicate Effect
```

This makes retry safe.

### 28.46 Multiple Pull Batches

If more changes remain:

```text
Batch 1
 ↓
Next Cursor
 ↓
Batch 2
 ↓
Next Cursor
```

must continue until the synchronization protocol indicates completion.

### 28.47 Has More

If the response contains:

```text
hasMore = true
```

the Pull Processor must continue from the returned cursor.

### 28.48 Empty Pull

If no new changes exist:

```text
Cursor
      ↓
No Changes
      ↓
Sync Complete
```

### 28.49 Pull and Push Coordination

Pull must coordinate with Push through the Sync Engine.

```text
SyncEngine
 ├── Push
 └── Pull
```

The ordering must follow the synchronization protocol.

### 28.50 Pull and Local Pending Operations

Incoming remote changes must be evaluated against local pending operations.

```text
Local Pending Change
      +
Remote Change
      ↓
Version / Conflict Check
```

### 28.51 Pull and Conflict Detection

If local pending state conflicts with remote state:

```text
Remote Operation
      ↓
Conflict Detection
      ↓
Conflict State
```

must be used instead of silent overwrite.

### 28.52 Pull and Tombstones

Remote deletion operations must be represented through synchronization-safe tombstones where required.

### 28.53 Tombstone Application

```text
Remote DELETE
      ↓
Local Tombstone
      ↓
Cursor Update
```

Physical deletion should only occur when safe cleanup is permitted.

### 28.54 Pull and Authorization State

Remote Membership changes may change what the current User is allowed to access.

The local authorization-relevant state must be updated accordingly.

### 28.55 Pull and Group Membership

If a User is removed from a Group remotely:

```text
Remote Membership Change
      ↓
Local Membership Update
      ↓
Authorization State Update
```

### 28.56 Pull and Device State

If remote security state indicates Device revocation:

```text
Device Revoked
      ↓
Local Authentication / Sync State Updated
```

according to the Security Architecture.

### 28.57 Pull Security

Pull must enforce:

```text
Authentication
Authorization
Device Verification
Operation Validation
Replay Protection
Transport Security
```

### 28.58 Pull Transport Security

Backend Pull must use HTTPS.

P2P Pull must use the authenticated secure peer transport.

### 28.59 Pull Logging

Logs may contain:

```text
Sync ID
Batch ID
Cursor
Operation ID
Result
Latency
```

but should not expose sensitive payloads.

### 28.60 Pull Metrics

Useful metrics include:

```text
Pull Requests
Operations Received
Operations Applied
Duplicates
Conflicts
Failures
Retries
Latency
Batch Size
```

### 28.61 Pull Performance

Pull should:

```text
Use Bounded Batches
Avoid Unnecessary Queries
Avoid Duplicate Processing
Use Incremental Cursors
```

### 28.62 Large Remote Dataset

Initial synchronization must process large datasets incrementally.

```text
Remote Dataset
      ↓
Batch 1
      ↓
Batch 2
      ↓
Batch N
```

### 28.63 Pull Memory Safety

The Pull Processor must not load the entire remote dataset into memory.

### 28.64 Pull Crash Recovery

If the application terminates during Pull:

```text
Process Stops
      ↓
Transaction Recovery
      ↓
Cursor Remains Safe
      ↓
Next Pull
```

### 28.65 Pull Restart

Pull must be safely restartable after:

```text
Application Restart
Worker Restart
Device Restart
Network Recovery
```

### 28.66 Pull State Recovery

Recovery must rely on persisted:

```text
SyncState
Operation State
Conflict State
```

rather than transient in-memory state.

### 28.67 Pull Testing

Pull Synchronization must include:

```text
Unit Tests
Repository Tests
Integration Tests
API Tests
Security Tests
Failure Tests
```

### 28.68 Pull Basic Test

Test:

```text
Cursor N
 ↓
Remote Changes
 ↓
Apply
 ↓
Cursor M
```

### 28.69 Pull Empty Test

Test:

```text
Cursor Current
 ↓
No Changes
 ↓
Sync Complete
```

### 28.70 Pull Duplicate Test

Test:

```text
Operation Already Applied
      ↓
Remote Operation Received Again
      ↓
No Duplicate Effect
```

### 28.71 Pull Conflict Test

Test:

```text
Local Version
+
Remote Version
      ↓
Conflict
      ↓
Conflict Persisted
```

### 28.72 Pull Cursor Test

Test that the cursor advances only after successful persistence.

### 28.73 Pull Failure Test

Test:

```text
Network Failure
 ↓
Cursor Unchanged
 ↓
Retry
```

### 28.74 Pull Partial Batch Test

Test partial processing and verify that the cursor never skips unprocessed changes.

### 28.75 Pull Authentication Test

Test:

```text
Expired Authentication
 ↓
Refresh
 ↓
Pull
```

### 28.76 Pull Authorization Test

Test that unauthorized synchronization data cannot be retrieved or applied.

### 28.77 Pull Revocation Test

Test:

```text
Active Device
 ↓
Revoked
 ↓
Pull
 ↓
Rejected
```

### 28.78 Pull Crash Recovery Test

Test application termination during:

```text
Operation Apply
Cursor Update
```

and verify safe recovery.

### 28.79 Pull Performance Test

Test:

```text
Small Dataset
Large Dataset
Large Batch
Multiple Batches
```

### 28.80 Pull Completion Criteria

Pull Synchronization Implementation is complete when:

```text
Cursor Loading Implemented
Pull Request Implemented
Authentication Integrated
Authorization Integrated
Batching Implemented
Operation Validation Implemented
Duplicate Detection Implemented
Version Checking Implemented
Conflict Handling Implemented
Remote State Application Implemented
Cursor Advancement Implemented
Cursor Transaction Implemented
Retry Implemented
Backoff Implemented
Tombstone Handling Implemented
Crash Recovery Implemented
Security Integrated
Metrics / Logging Implemented
Tests Passing
```

### 28.81 Pull Synchronization Invariants

The following rules are mandatory:

- Pull must use the persisted synchronization cursor.
- Pull requests must be authenticated.
- Pull requests must be authorized.
- Revoked Devices must not retrieve protected synchronization data.
- Remote operations must be validated before application.
- Remote operations must pass Domain validation.
- Duplicate Operation IDs must not create duplicate business effects.
- Version mismatches must not silently overwrite newer local state.
- Conflicts must be persisted when they cannot be safely resolved automatically.
- Remote financial changes must be applied transactionally with required related state.
- Balance state must reflect successfully applied financial changes.
- Cursor advancement must occur only after successful persistence.
- Cursor updates must not skip unprocessed remote changes.
- Cursor persistence failure must leave the previous cursor intact.
- Retry after cursor failure must remain safe through idempotency.
- Pull batches must be bounded.
- Large remote datasets must be processed incrementally.
- Network failure must not advance the cursor.
- Authentication failure must follow the Authentication flow.
- Authorization failure must not be blindly retried.
- Remote deletion must follow the tombstone strategy.
- Membership changes must update local authorization state.
- Pull must recover safely after application or worker termination.
- Pull must not depend solely on in-memory synchronization state.
- Pull behavior must be covered by automated tests.


## 29. Sync State Implementation

### 29.1 Purpose

This section defines the implementation of Sync State in SplitSync V1.

Sync State represents the current synchronization status of the Android Device and its synchronization context.

Conceptually:

```text
Local Data
+
Pending Operations
+
Remote Cursor
      ↓
Sync State
```

### 29.2 Sync State Principle

Synchronization state must be:

```text
Persistent
Recoverable
Deterministic
Consistent
```

### 29.3 Sync State Components

Sync State may contain:

```text
Sync Status
Cursor
Last Successful Sync
Last Attempt
Failure Information
Retry Information
```

according to the Sync State Model.

### 29.4 Sync Context

Sync State must be associated with the appropriate synchronization context.

Depending on the architecture, this may include:

```text
Device
User
Backend
Peer
Group
Synchronization Scope
```

### 29.5 Sync State Entity

Conceptually:

```text
SyncStateEntity
├── Context ID
├── Device ID
├── Cursor
├── Status
├── Last Successful Sync
├── Last Attempt
├── Retry Count
└── Error State
```

The exact fields must follow the defined Sync State Model.

### 29.6 Sync Status

The implementation should represent explicit states.

Example:

```text
IDLE
SYNCING
SUCCESS
FAILED
CONFLICT
```

### 29.7 IDLE

IDLE means:

```text
No Synchronization Currently Running
```

### 29.8 SYNCING

SYNCING means:

```text
Synchronization Is Currently Running
```

### 29.9 SUCCESS

SUCCESS means:

```text
The Latest Synchronization Completed Successfully
```

### 29.10 FAILED

FAILED means:

```text
Synchronization Did Not Complete Successfully
```

The failure should be classified where possible.

### 29.11 CONFLICT

CONFLICT indicates unresolved synchronization conflicts requiring the defined handling flow.

### 29.12 Status Transition

Conceptually:

```text
IDLE
 ↓
SYNCING
 ↓
SUCCESS
```

or:

```text
SYNCING
 ↓
FAILED
```

or:

```text
SYNCING
 ↓
CONFLICT
```

### 29.13 State Transition Authority

Sync State transitions must be controlled by the Sync Engine rather than arbitrary UI code.

### 29.14 State Persistence

Sync State must be persisted in the local database.

### 29.15 State Recovery

After application restart:

```text
Room
 ↓
SyncState
 ↓
Recover Synchronization
```

### 29.16 Last Successful Sync

The system may persist the timestamp of the latest successful synchronization.

### 29.17 Last Attempt

The system may persist the timestamp of the latest synchronization attempt.

### 29.18 Cursor

The Cursor identifies the latest remote synchronization position successfully processed.

### 29.19 Cursor Ownership

A cursor must belong to the synchronization context for which it was generated.

### 29.20 Cursor Independence

Independent synchronization contexts must not incorrectly share cursors.

### 29.21 Cursor Monotonicity

A cursor should move forward according to the synchronization protocol.

```text
Cursor 10
   ↓
Cursor 11
   ↓
Cursor 12
```

It must not move backward during normal synchronization.

### 29.22 Cursor Rollback

If a synchronization transaction fails:

```text
Current Cursor
      ↓
Transaction Failure
      ↓
Previous Cursor Retained
```

### 29.23 Cursor Persistence

Cursor updates must be persisted atomically with the corresponding remote changes where required.

### 29.24 Sync Failure State

Failure state should capture enough information to determine:

```text
Retryable
Permanent
Authentication
Authorization
Conflict
```

### 29.25 Error Classification

Synchronization errors should be categorized.

Example:

```text
NETWORK
TIMEOUT
AUTHENTICATION
AUTHORIZATION
VALIDATION
CONFLICT
SERVER
UNKNOWN
```

### 29.26 Retry Count

Retry count may be tracked for retry policy.

### 29.27 Retry Reset

After successful synchronization:

```text
Retry Count → Reset
```

according to the implementation.

### 29.28 Retry Scheduling

The Sync State may contain the next retry time if the architecture requires persisted scheduling metadata.

### 29.29 Stale SYNCING State

If the application terminates while state is:

```text
SYNCING
```

the next synchronization must detect and recover the stale state.

### 29.30 Sync Recovery

Recovery may follow:

```text
SYNCING
      ↓
Worker Terminated
      ↓
Recover
      ↓
IDLE / Retryable
```

according to the Sync State Model.

### 29.31 Sync Lock State

Synchronization locking should not depend solely on persisted status.

Runtime synchronization coordination may use an in-process lock in addition to persisted state.

### 29.32 Concurrent Synchronization

Two synchronization executions for the same context must not concurrently modify:

```text
Cursor
SyncState
Pending Operation State
```

without coordination.

### 29.33 State Transaction

State changes that affect multiple synchronization records should be transactionally consistent.

### 29.34 Push State Integration

Push results must update the relevant operation state without incorrectly changing the Pull cursor.

### 29.35 Pull State Integration

Pull completion must update:

```text
Cursor
Last Successful Sync
Status
```

only after successful application.

### 29.36 Conflict State Integration

When unresolved conflicts exist:

```text
SyncState = CONFLICT
```

where required by the Sync State Model.

### 29.37 Conflict Resolution

After all required conflicts are resolved:

```text
CONFLICT
   ↓
Sync
   ↓
SUCCESS
```

### 29.38 Partial Conflict

If only some operations conflict:

```text
Successful Operations
+
Unresolved Conflicts
```

must remain independently represented.

### 29.39 Operation State vs Sync State

These concepts must remain separate.

```text
SyncOperation State
      ↓
State of One Operation

SyncState
      ↓
State of Synchronization Context
```

### 29.40 Sync State and UI

The UI may observe SyncState through the Repository/Application layer.

```text
SyncState
 ↓
Repository
 ↓
ViewModel
 ↓
UI
```

### 29.41 UI Sync Status

The UI may display:

```text
Synced
Syncing
Sync Pending
Sync Failed
Conflict
```

based on the Presentation mapping.

### 29.42 Sync State and Offline Mode

Offline mode should not be represented simply as:

```text
SYNC FAILED
```

when no synchronization attempt was possible.

The application should distinguish:

```text
Network Unavailable
```

from:

```text
Synchronization Failed
```

where required.

### 29.43 Network State

Network availability and SyncState are separate concepts.

```text
Network State
      ≠
Sync State
```

### 29.44 Sync Pending

If local operations exist that have not yet synchronized:

```text
Pending Operations > 0
```

the application may expose a pending synchronization indicator.

### 29.45 Sync Completion

Synchronization should be considered complete only when:

```text
Required Push Complete
+
Required Pull Complete
+
Local State Consistent
```

according to the protocol.

### 29.46 Last Successful Sync

Last Successful Sync must only be updated after the synchronization cycle reaches its successful completion condition.

### 29.47 Failed Sync

A failed synchronization must not update Last Successful Sync.

### 29.48 Conflict Sync

If unresolved conflicts prevent synchronization completion:

```text
Last Successful Sync
```

must not incorrectly indicate that all synchronization work is complete.

### 29.49 Sync State and Authentication

If synchronization cannot execute because authentication has expired:

```text
Authentication Required
```

must be represented without corrupting the synchronization cursor.

### 29.50 Sync State and Authorization

If authorization fails:

```text
Authorization Failure
```

must be represented distinctly from network failure.

### 29.51 Sync State and Device Revocation

A revoked Device must enter an appropriate blocked synchronization state.

### 29.52 Sync State and Device Identity

Sync State must be associated with the correct Device Identity.

### 29.53 Sync State and P2P

P2P synchronization may require peer-specific synchronization state.

Conceptually:

```text
Device A
   ↕
Peer B
```

may have a synchronization context associated with that peer.

### 29.54 Peer Cursor

Peer-specific synchronization cursors must not be incorrectly shared with Backend cursors if the protocols use different positions.

### 29.55 Backend Cursor

Backend synchronization should maintain the cursor defined by the Backend synchronization protocol.

### 29.56 Cursor Namespace

Cursor values must be interpreted only within their defined synchronization context.

A cursor from one Backend/peer context must not automatically be used for another context.

### 29.57 Sync State Repository

A dedicated repository should manage SyncState persistence.

Conceptually:

```text
SyncStateRepository
```

### 29.58 Sync State Queries

The repository should support:

```text
Get Sync State
Get Cursor
Get Status
Get Pending State
Update Status
Update Cursor
Record Failure
Record Success
```

where required.

### 29.59 Sync State Transaction

Cursor and state updates must be transactionally coordinated with remote data application where required.

### 29.60 Sync State Cleanup

Old synchronization metadata may be cleaned up according to retention rules.

Active cursors must never be removed while required.

### 29.61 Sync State Migration

Changes to SyncState schema must use explicit database migrations.

### 29.62 Sync State Logging

Logs may include:

```text
Sync ID
Context
Status
Cursor
Failure Category
```

Sensitive information must not be logged.

### 29.63 Sync State Metrics

Useful metrics include:

```text
Sync Success Rate
Sync Failure Rate
Pending Queue Size
Conflict Count
Retry Count
Time Since Last Successful Sync
```

### 29.64 Sync State Performance

SyncState operations should be lightweight and indexed appropriately.

### 29.65 Sync State Testing

Sync State implementation must include:

```text
State Transition Tests
Cursor Tests
Failure Tests
Recovery Tests
Concurrency Tests
Persistence Tests
Migration Tests
```

### 29.66 State Transition Tests

Test:

```text
IDLE → SYNCING
SYNCING → SUCCESS
SYNCING → FAILED
SYNCING → CONFLICT
```

### 29.67 Cursor Tests

Test:

```text
Cursor Creation
Cursor Advancement
Cursor Persistence
Cursor Failure
Cursor Recovery
Cursor Isolation
```

### 29.68 Recovery Tests

Test:

```text
SYNCING
 ↓
Application Termination
 ↓
Restart
 ↓
Recovery
```

### 29.69 Authentication State Tests

Test:

```text
Sync
 ↓
Authentication Expired
 ↓
Authentication Required
```

### 29.70 Authorization State Tests

Test:

```text
Sync
 ↓
Authorization Failure
 ↓
Correct Sync State
```

### 29.71 Conflict State Tests

Test:

```text
Conflict
 ↓
CONFLICT
 ↓
Resolve
 ↓
SUCCESS
```

### 29.72 Concurrent Sync Tests

Test:

```text
Sync Request A
Sync Request B
      ↓
Safe Coordination
```

### 29.73 Persistence Tests

Test that SyncState survives:

```text
Application Restart
Process Death
Device Restart
```

### 29.74 Migration Tests

Test every SyncState schema migration.

### 29.75 Sync State Completion Criteria

Sync State Implementation is complete when:

```text
SyncState Entity Implemented
SyncState Repository Implemented
Status Model Implemented
Cursor Implemented
Cursor Persistence Implemented
Failure Classification Implemented
Retry Metadata Implemented
Recovery Implemented
Concurrency Control Integrated
Push Integration Implemented
Pull Integration Implemented
Conflict Integration Implemented
Backend Context Supported
P2P Context Supported
UI Integration Implemented
Migration Implemented
Tests Passing
```

### 29.76 Sync State Invariants

The following rules are mandatory:

- SyncState must be persistent.
- SyncState must be recoverable after application/process termination.
- SyncState must be scoped to the correct synchronization context.
- Backend and P2P cursors must not be incorrectly shared when they represent different synchronization contexts.
- Cursor values must be interpreted only within their defined namespace/context.
- Cursor advancement must occur only after corresponding remote changes are safely persisted.
- Cursor must not advance after failed transactions.
- Cursor must not move backward during normal synchronization.
- SyncState and SyncOperation state must remain separate concepts.
- Push acknowledgements must not incorrectly advance the Pull cursor.
- Last Successful Sync must only update after successful synchronization completion.
- Failed synchronization must not update Last Successful Sync.
- Unresolved conflicts must be represented explicitly.
- Authentication failure must remain distinguishable from network failure.
- Authorization failure must remain distinguishable from network failure.
- Device revocation must prevent protected synchronization.
- Concurrent synchronization must not corrupt SyncState.
- Stale SYNCING state must be recoverable.
- SyncState updates must be transactionally consistent with required synchronization changes.
- Network availability and SyncState must remain separate concepts.
- SyncState must be covered by automated persistence, transition, recovery, and concurrency tests.

## 30. Retry and Recovery Implementation

## 31. Conflict Detection Implementation

## 32. Conflict Resolution Implementation

## 33. Peer-to-Peer Implementation

## 34. P2P Discovery Implementation

## 35. P2P Connection and Handshake

## 36. P2P Synchronization Implementation

## 37. Transaction Implementation

## 38. Error Handling Implementation

## 39. Logging Implementation

## 40. Configuration Implementation

## 41. API Versioning Implementation

## 42. Database Migration Implementation

## 43. Android Background Processing

## 44. Testing Implementation

## 45. Unit Testing

## 46. Integration Testing

## 47. Synchronization Testing

## 48. Conflict Testing

## 49. P2P Testing

## 50. End-to-End Testing

## 51. Security Testing

## 52. Performance Testing

## 53. CI/CD Implementation

## 54. Development Deployment

## 55. Test Deployment

## 56. Staging Deployment

## 57. Production Deployment

## 58. Monitoring and Observability

## 59. Backup and Recovery

## 60. Release Management

## 61. Implementation Validation

## 62. Implementation Checklist

## 63. V1 Completion Criteria

## 64. Future Implementation Extensions