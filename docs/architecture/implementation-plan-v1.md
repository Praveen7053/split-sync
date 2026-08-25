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

### 30.1 Purpose

This section defines the implementation of retry and recovery mechanisms in SplitSync V1.

The retry and recovery system must ensure that temporary failures do not result in:

```text
Lost Operations
Duplicate Financial Effects
Corrupted Sync State
Stuck Synchronization
```

### 30.2 Retry Principle

Only failures that are potentially recoverable should be retried.

```text
Failure
   ↓
Classify
   ↓
┌───────────────┬────────────────┐
↓               ↓
Retryable      Permanent
↓               ↓
Retry          Stop
```

### 30.3 Retryable Failures

Typical retryable failures include:

```text
Network Unavailable
Connection Failure
Timeout
Temporary Server Error
Temporary Peer Failure
```

### 30.4 Permanent Failures

Typical permanent failures include:

```text
Invalid Payload
Invalid Domain State
Unauthorized Operation
Unsupported Operation
Invalid Resource
Revoked Device
```

### 30.5 Retry State

Retry metadata should be persisted where required.

Conceptually:

```text
Retry Count
Last Attempt
Next Retry
Failure Type
Failure Reason
```

### 30.6 Retry Count

Each retryable operation may maintain a retry count.

```text
Retry 0
 ↓
Retry 1
 ↓
Retry 2
 ↓
Retry 3
```

### 30.7 Retry Limit

The system must define a maximum retry policy to prevent infinite retries.

### 30.8 Exponential Backoff

Retry delays should increase between attempts.

Conceptually:

```text
Attempt 1 → Short Delay
Attempt 2 → Longer Delay
Attempt 3 → Longer Delay
...
```

### 30.9 Backoff Jitter

Where appropriate, jitter should be added to prevent multiple Devices from retrying simultaneously.

### 30.10 Retry Scheduling

Retry scheduling should be delegated to the Android background execution mechanism.

The application must not use an uncontrolled loop such as:

```text
while (failure) {
    retry();
}
```

### 30.11 Network Recovery

When the network becomes available:

```text
Network Recovery
      ↓
Eligible Sync Work
      ↓
Retry
```

### 30.12 Authentication Recovery

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

### 30.13 Authentication Failure Limit

Repeated authentication failures must not cause endless synchronization retries.

### 30.14 Authorization Failure

Authorization failures must not be blindly retried.

```text
Authorization Failure
      ↓
Stop Operation
      ↓
Update State
```

### 30.15 Device Revocation

If the Device is revoked:

```text
Device Revoked
      ↓
Stop Protected Synchronization
```

### 30.16 Conflict Recovery

Conflicts are not ordinary retry failures.

```text
Conflict
   ↓
Conflict Handling
   ↓
Resolution
```

### 30.17 Retry After Conflict

An operation involved in a conflict should only be retried after the conflict resolution process produces a valid synchronization state.

### 30.18 Timeout Recovery

Timeouts are potentially ambiguous.

The receiving side may have already processed the operation.

Therefore:

```text
Timeout
   ↓
Retry Same Operation ID
```

must be used rather than generating a new operation.

### 30.19 Idempotent Recovery

All retryable synchronization operations must remain idempotent.

```text
Same Operation ID
      ↓
Repeated Delivery
      ↓
Single Business Effect
```

### 30.20 Push Recovery

Push recovery should follow:

```text
PENDING
   ↓
SYNCING
   ↓
Failure
   ↓
PENDING
   ↓
Retry
```

for retryable failures.

### 30.21 Pull Recovery

Pull recovery should preserve the last safely committed cursor.

```text
Pull
 ↓
Failure
 ↓
Cursor Unchanged
 ↓
Retry
```

### 30.22 Cursor Recovery

The cursor must only advance after corresponding remote changes are safely persisted.

### 30.23 Partial Batch Recovery

If a batch contains:

```text
A → APPLIED
B → RETRY
C → CONFLICT
```

each result must be persisted independently.

### 30.24 Sync Worker Recovery

If the Android Sync Worker terminates unexpectedly:

```text
Worker Termination
      ↓
Persisted Sync State
      ↓
Detect Incomplete Work
      ↓
Recover
```

### 30.25 Stale SYNCING Operations

Operations left in:

```text
SYNCING
```

after unexpected termination must be detected as stale according to a defined timeout/recovery policy.

### 30.26 Stale State Recovery

Stale operations should transition to a retryable state where safe.

```text
SYNCING
   ↓
Stale
   ↓
PENDING
```

### 30.27 Crash During Local Transaction

If a crash occurs during a local mutation:

```text
Transaction
 ↓
Crash
 ↓
Database Recovery
```

must preserve atomicity.

### 30.28 Crash During Sync

If a crash occurs while synchronization is processing:

```text
Crash
 ↓
Persisted State
 ↓
Retry
```

must not create duplicate financial effects.

### 30.29 Application Restart

After application restart:

```text
Load SyncState
Load Pending Operations
Recover Stale Operations
Schedule Sync
```

### 30.30 Device Restart

The same recovery process must work after a complete Device restart.

### 30.31 Recovery Ordering

Recovery should occur before normal synchronization resumes.

```text
Recover
   ↓
Validate State
   ↓
Normal Sync
```

### 30.32 Recovery Validation

Before retrying recovered operations, validate:

```text
Operation State
Operation ID
Entity State
Version
Sync Context
```

### 30.33 Recovery and Dependencies

Recovered operations must continue to respect operation dependencies.

### 30.34 Recovery and Conflicts

An operation already identified as conflicting must not be automatically moved back to PENDING without valid conflict handling.

### 30.35 Recovery and Permanent Failure

Permanent failures must remain permanently failed until an explicit valid resolution/retry mechanism exists.

### 30.36 Retry State Persistence

Retry state must survive application termination where required.

### 30.37 Retry Cleanup

After successful synchronization:

```text
Retry Count → Reset / Cleanup
```

according to the retention strategy.

### 30.38 Retry Logging

Retry logs may contain:

```text
Operation ID
Retry Count
Failure Type
Next Retry
```

but must not expose sensitive payloads.

### 30.39 Retry Metrics

Useful metrics include:

```text
Retry Count
Retry Success Rate
Permanent Failure Count
Average Retry Attempts
Recovery Count
Stale Operation Count
```

### 30.40 Retry Testing

Test:

```text
Network Failure
Timeout
Server Failure
Authentication Failure
Authorization Failure
Crash
Restart
Stale Operation
Repeated Retry
```

### 30.41 Retry Completion Criteria

Retry and Recovery Implementation is complete when:

```text
Failure Classification Implemented
Retry Policy Implemented
Retry Limit Implemented
Backoff Implemented
Jitter Implemented Where Required
Authentication Recovery Implemented
Timeout Recovery Implemented
Crash Recovery Implemented
Restart Recovery Implemented
Stale Operation Recovery Implemented
Cursor Recovery Implemented
Conflict Integration Implemented
Metrics / Logging Implemented
Tests Passing
```

### 30.42 Retry and Recovery Invariants

The following rules are mandatory:

- Only retryable failures may be automatically retried.
- Permanent failures must not retry indefinitely.
- Authentication failures must follow the Authentication recovery flow.
- Authorization failures must not be blindly retried.
- Revoked Devices must not continue protected synchronization.
- Conflicts must be handled through the Conflict architecture.
- Timeout retries must reuse the same Operation ID.
- Retry must never create duplicate financial effects.
- Retry state must survive application termination where required.
- Retry delays must be controlled.
- Synchronization must not use uncontrolled retry loops.
- Pull failures must not advance the cursor.
- Stale SYNCING operations must be recoverable.
- Recovery must respect operation dependencies.
- Recovery must preserve transaction atomicity.
- Recovered operations must pass the normal synchronization validation process.
- Retry and recovery behavior must be covered by automated tests.


## 31. Conflict Detection Implementation

### 31.1 Purpose

This section defines the implementation of Conflict Detection in SplitSync V1.

Conflict Detection determines whether a local or remote synchronization operation can be safely applied to the current state.

```text
Incoming / Local Operation
          ↓
Current Entity State
          ↓
Version / Rule Check
          ↓
┌─────────┴─────────┐
↓                   ↓
Compatible        Conflict
↓                   ↓
Apply             Resolve
```

### 31.2 Conflict Principle

The system must never silently overwrite a newer or incompatible state.

### 31.3 Conflict Detection Boundary

Conflict Detection belongs to the synchronization/domain integration layer.

It must not be implemented in the UI.

### 31.4 Conflict Inputs

Conflict detection may use:

```text
Entity ID
Operation ID
Device ID
Base Version
Current Version
Incoming Version
Operation Type
Entity State
```

### 31.5 Version-Based Detection

The primary detection mechanism should use the versioning strategy defined by the synchronization model.

Conceptually:

```text
Expected Version
      +
Current Version
      ↓
Compatible?
```

### 31.6 Compatible Operation

If the incoming operation is compatible:

```text
No Conflict
   ↓
Apply
```

### 31.7 Version Mismatch

If the operation was generated against an incompatible version:

```text
Version Mismatch
      ↓
Conflict Candidate
```

### 31.8 Concurrent Modification

A conflict may occur when:

```text
Device A
   ↓
Update V5

Device B
   ↓
Update V5
```

and both changes cannot be safely merged.

### 31.9 Conflict Detection Result

The detector should return an explicit result.

Conceptually:

```text
NO_CONFLICT
CONFLICT
INVALID
```

### 31.10 Conflict Detector

A dedicated component should encapsulate conflict detection logic.

Conceptually:

```text
ConflictDetector
```

### 31.11 Conflict Detection Flow

```text
Operation
   ↓
Load Current State
   ↓
Check Entity Existence
   ↓
Check Version
   ↓
Check Operation Compatibility
   ↓
Return Result
```

### 31.12 Entity Existence

The detector must determine whether the target entity exists when required.

Examples:

```text
CREATE on existing Entity
UPDATE on missing Entity
DELETE on missing Entity
```

must follow explicit rules.

### 31.13 Create Conflict

A CREATE operation may conflict when the same logical entity already exists with incompatible state.

### 31.14 Update Conflict

An UPDATE operation may conflict when its Base Version does not match the applicable current version.

### 31.15 Delete Conflict

A DELETE operation may conflict when the target has changed after the operation was generated.

### 31.16 Tombstone Conflict

A mutation against an entity already represented by a tombstone must follow the deletion/conflict rules.

### 31.17 Membership Conflict

Membership changes must consider:

```text
Current Membership State
+
Incoming Membership Operation
+
Version
```

### 31.18 Group Conflict

Group updates must use the Group versioning rules.

### 31.19 Expense Conflict

Expense conflicts must preserve:

```text
Amount
Splits
Payer
Participants
Version
```

and must never silently alter financial values.

### 31.20 Settlement Conflict

Settlement conflicts must preserve:

```text
Payer
Receiver
Amount
Version
```

### 31.21 Financial Conflict Safety

Conflict detection must never resolve a financial conflict by simply choosing an arbitrary value.

### 31.22 Conflict and Balance

A conflicting financial operation must not incorrectly modify the balance until its resulting Domain state is valid.

### 31.23 Conflict Detection Transaction

Conflict detection and the related state check must occur against a consistent local snapshot.

### 31.24 Atomic Detection and Apply

Where required:

```text
Load Current State
+
Detect Conflict
+
Apply
```

must occur within one transaction or equivalent concurrency boundary.

### 31.25 Race Prevention

Two synchronization processes must not perform conflict detection against stale state and then both apply incompatible mutations.

### 31.26 Concurrency Control

Conflict detection must use the concurrency mechanism defined by the persistence architecture.

### 31.27 Operation ID Check

Duplicate Operation IDs should be checked before generating a new conflict.

### 31.28 Already Applied Operation

If the operation has already been successfully applied:

```text
Already Applied
      ↓
No New Conflict
```

### 31.29 Remote Conflict

A remote operation may conflict with local unsynchronized state.

```text
Local Pending Change
      +
Remote Change
      ↓
Conflict Detection
```

### 31.30 Local Conflict

A locally generated operation may be rejected by the Backend/Peer because the remote state has changed.

```text
Local Operation
      ↓
Remote Version Check
      ↓
Conflict
```

### 31.31 Conflict Metadata

A detected conflict should capture:

```text
Conflict ID
Entity Type
Entity ID
Operation ID
Local Version
Remote Version
Local State
Remote State
Detection Time
Status
```

according to the Conflict Data Model.

### 31.32 Conflict Persistence

Conflict information must be persisted durably.

### 31.33 Conflict State

Conceptually:

```text
OPEN
RESOLVING
RESOLVED
REJECTED
```

according to the Conflict Data Model.

### 31.34 Conflict Status Authority

Conflict status transitions must be controlled by the synchronization/application layer.

### 31.35 Conflict Detection Failure

If conflict detection cannot reliably determine the state:

```text
Do Not Apply Blindly
      ↓
Safe Failure
```

### 31.36 Invalid Operation

An operation that fails Domain validation is not automatically a conflict.

It should be classified as:

```text
INVALID
```

unless the synchronization protocol explicitly defines it as a conflict.

### 31.37 Conflict vs Validation

These concepts must remain separate:

```text
Validation Failure
      ≠
Synchronization Conflict
```

### 31.38 Conflict vs Authorization

Authorization failure is not a conflict.

```text
Unauthorized
      ≠
Conflict
```

### 31.39 Conflict vs Network Failure

Network failure is not a conflict.

```text
Network Failure
      ≠
Conflict
```

### 31.40 Conflict Detection and P2P

P2P synchronization must use the same logical conflict detection rules.

### 31.41 Conflict Detection and Backend

Backend synchronization must use the same version/conflict rules defined by the protocol.

### 31.42 Conflict Detection Result

The result must be deterministic for the same:

```text
Current State
+
Operation
+
Synchronization Rules
```

### 31.43 Conflict Logging

Logs may contain:

```text
Conflict ID
Operation ID
Entity Type
Entity ID
Local Version
Remote Version
```

but should not expose unnecessary financial payloads.

### 31.44 Conflict Metrics

Useful metrics include:

```text
Conflict Count
Conflict Rate
Conflict Type
Resolution Time
Unresolved Conflicts
```

### 31.45 Conflict Testing

Test:

```text
Matching Version
Stale Version
Concurrent Update
Duplicate Operation
Create Conflict
Update Conflict
Delete Conflict
Membership Conflict
Financial Conflict
```

### 31.46 Conflict Completion Criteria

Conflict Detection Implementation is complete when:

```text
ConflictDetector Implemented
Version Checks Implemented
Entity Existence Checks Implemented
Create Conflict Rules Implemented
Update Conflict Rules Implemented
Delete Conflict Rules Implemented
Duplicate Detection Integrated
Conflict Persistence Implemented
Concurrency Control Implemented
Backend Integration Implemented
P2P Integration Implemented
Logging / Metrics Implemented
Tests Passing
```

### 31.47 Conflict Detection Invariants

The following rules are mandatory:

- Conflicts must be detected deterministically.
- Version mismatches must not be silently ignored.
- Newer or incompatible state must not be silently overwritten.
- Duplicate operations must not create duplicate conflicts unnecessarily.
- Validation failures must remain distinct from conflicts.
- Authorization failures must remain distinct from conflicts.
- Network failures must remain distinct from conflicts.
- Financial conflicts must preserve financial correctness.
- Conflict detection must operate against a consistent state.
- Concurrent synchronization must not allow stale conflict checks to produce invalid state.
- Conflict information must be persisted durably.
- Conflict detection must be shared logically between Backend and P2P synchronization.
- Conflict detection must be covered by automated tests.


## 32. Conflict Resolution Implementation

### 32.1 Purpose

This section defines the implementation of Conflict Resolution in SplitSync V1.

Conflict Resolution determines how a detected synchronization conflict is converted into a valid final Domain state.

```text
Conflict
   ↓
Resolution Strategy
   ↓
Valid Domain State
   ↓
Synchronization
```

### 32.2 Resolution Principle

Conflict resolution must never produce invalid Domain or financial state.

### 32.3 Resolution Strategies

The supported strategy must follow the Conflict Data Model.

Possible categories include:

```text
Automatic Resolution
Manual Resolution
Reject
Retry After Reconciliation
```

Only strategies explicitly supported by V1 should be implemented.

### 32.4 Conflict Resolver

A dedicated component should perform resolution.

Conceptually:

```text
ConflictResolver
```

### 32.5 Resolution Inputs

The resolver may require:

```text
Conflict
Local State
Remote State
Operation
Version Information
Resolution Decision
```

### 32.6 Automatic Resolution

Automatic resolution may be used only where the conflict rules define an unambiguous result.

### 32.7 Manual Resolution

Manual resolution should be used where the system cannot safely determine the correct outcome automatically.

```text
Conflict
 ↓
User Decision
 ↓
Resolve Conflict
```

### 32.8 Financial Conflict Resolution

Financial conflicts require stricter handling.

The system must not automatically choose:

```text
Higher Amount
Lower Amount
Latest Timestamp
```

unless explicitly defined by the Domain rules.

### 32.9 Expense Conflict Resolution

An Expense conflict must preserve consistency between:

```text
Expense
+
ExpenseSplits
```

### 32.10 Settlement Conflict Resolution

A Settlement conflict must preserve:

```text
Payer
Receiver
Amount
```

and the resulting balance.

### 32.11 Membership Conflict Resolution

Membership conflicts must preserve valid:

```text
Group Membership
Role
Authorization State
```

### 32.12 Group Conflict Resolution

Group conflicts must preserve the Group's valid Domain state.

### 32.13 Resolution Validation

Every resolution must pass Domain validation.

```text
Resolution
   ↓
Domain Validation
   ↓
Valid?
```

### 32.14 Invalid Resolution

If the proposed resolution is invalid:

```text
Resolution
   ↓
Validation Failure
   ↓
Conflict Remains Open
```

### 32.15 Resolution Transaction

Resolution must be transactional.

```text
Resolved Entity State
+
Conflict State
+
Required SyncOperation
      ↓
Commit
```

### 32.16 Conflict State Transition

Conceptually:

```text
OPEN
 ↓
RESOLVING
 ↓
RESOLVED
```

or:

```text
OPEN
 ↓
REJECTED
```

where supported.

### 32.17 Resolution Operation

A manual resolution should produce a new valid synchronization operation where required.

```text
Conflict Resolution
      ↓
New Domain Mutation
      ↓
New SyncOperation
```

### 32.18 Original Operation

The original conflicting operation must retain its history/state according to the synchronization model.

### 32.19 No Silent Overwrite

Resolution must explicitly account for both conflicting states.

### 32.20 Resolution Metadata

The system may retain:

```text
Resolution ID
Conflict ID
Resolver
Resolution Type
Resolution Time
Resulting Version
```

according to the data model.

### 32.21 Conflict Resolution UI

If manual resolution is supported:

```text
Conflict
 ↓
Conflict Screen
 ↓
User Decision
 ↓
ViewModel
 ↓
ResolveConflictUseCase
 ↓
ConflictResolver
```

### 32.22 UI Responsibility

The UI must not directly modify conflict persistence.

### 32.23 Resolution Options

Only valid resolution options should be exposed to the User.

### 32.24 Resolution Authorization

Only an authorized User may resolve a conflict.

### 32.25 Resolution Authentication

Conflict resolution must operate under a valid authenticated context where required.

### 32.26 Resolution Revalidation

Before committing a manual resolution:

```text
Reload Current State
      ↓
Revalidate Conflict
      ↓
Apply Resolution
```

This prevents resolving against stale state.

### 32.27 Concurrent Resolution

Two Devices must not independently resolve the same conflict into incompatible final state without the synchronization protocol detecting it.

### 32.28 Resolution Version

A successful resolution must produce the appropriate resulting version.

### 32.29 Resolution Sync

After local resolution:

```text
Resolved State
      ↓
SyncOperation
      ↓
Push
```

where required.

### 32.30 Resolution and Pull

The resolved state may subsequently be received by other Devices through Pull synchronization.

### 32.31 Resolution Idempotency

Repeated processing of the same resolution operation must not create duplicate financial effects.

### 32.32 Resolution Failure

If resolution cannot be applied:

```text
Conflict
      ↓
Resolution Failure
      ↓
Conflict Remains Open
```

### 32.33 Resolution Retry

A resolution operation may be retried if the failure is transient.

### 32.34 Resolution Conflict

A conflict resolution itself may conflict with a newer remote state.

```text
Resolution
      ↓
Remote State Changed
      ↓
New Conflict
```

The normal conflict detection process must then apply.

### 32.35 Resolution Audit

Where required, resolution actions should be auditable.

### 32.36 Resolution Logging

Logs may contain:

```text
Conflict ID
Resolution ID
Entity ID
Resolution Type
Result
```

Sensitive payloads should not be logged unnecessarily.

### 32.37 Resolution Metrics

Useful metrics include:

```text
Open Conflicts
Resolved Conflicts
Rejected Conflicts
Resolution Time
Automatic Resolution Rate
```

### 32.38 Resolution Cleanup

Resolved conflicts may be archived or cleaned according to the retention policy.

### 32.39 Resolution Testing

Test:

```text
Automatic Resolution
Manual Resolution
Invalid Resolution
Concurrent Resolution
Resolution Retry
Resolution Conflict
Resolution Idempotency
```

### 32.40 Financial Resolution Tests

Test that resolving an Expense or Settlement conflict preserves:

```text
Amount
Currency
Splits
Payer
Receiver
Balance
```

### 32.41 Membership Resolution Tests

Test:

```text
Role
Membership State
Authorization State
```

after resolution.

### 32.42 Resolution Completion Criteria

Conflict Resolution Implementation is complete when:

```text
ConflictResolver Implemented
Resolution Strategies Implemented
Domain Validation Integrated
Transactional Resolution Implemented
Manual Resolution Integrated Where Required
Authorization Integrated
Resolution SyncOperation Implemented
Revalidation Implemented
Idempotency Implemented
Retry Implemented
Conflict Re-detection Implemented
Logging / Metrics Implemented
Tests Passing
```

### 32.43 Conflict Resolution Invariants

The following rules are mandatory:

- Conflict resolution must produce valid Domain state.
- Financial conflicts must never be resolved through arbitrary value selection.
- Expense and ExpenseSplits must remain consistent after resolution.
- Settlement state must remain financially correct after resolution.
- Membership resolution must preserve valid authorization state.
- Every resolution must pass Domain validation.
- Resolution must be transactional.
- The original conflict state must be preserved according to the conflict model.
- Manual resolution must be authorized.
- Manual resolution must revalidate current state before commit.
- Resolution must not silently overwrite newer state.
- Resolution operations must be idempotent.
- Resolution failures must leave the conflict recoverable.
- A resolution that encounters a newer conflicting state must enter normal conflict detection again.
- Resolved state must synchronize through the normal synchronization protocol.
- Conflict resolution must be covered by automated tests.


## 33. Peer-to-Peer Implementation

### 33.1 Purpose

This section defines the implementation of local Peer-to-Peer synchronization in SplitSync V1.

P2P synchronization allows trusted Devices to exchange synchronization operations without requiring the Backend for the transfer itself.

```text
Device A
   ↕
Trusted P2P Channel
   ↕
Device B
```

### 33.2 P2P Principle

P2P synchronization must use the same logical synchronization model as Backend synchronization.

```text
SyncOperation
      ↓
P2P Transport
      ↓
Remote Device
```

### 33.3 P2P Responsibilities

The P2P implementation is responsible for:

```text
Peer Discovery
Peer Authentication
Peer Trust Verification
Connection Establishment
Capability Negotiation
Push
Pull
Operation Validation
Conflict Detection
Synchronization State
Connection Recovery
```

### 33.4 P2P Architecture

Conceptually:

```text
P2P Sync Engine
      ↓
Peer Manager
      ↓
P2P Transport
      ↓
Secure Peer Connection
      ↓
Remote Sync Engine
```

### 33.5 P2P Components

Logical components include:

```text
PeerManager
PeerDiscovery
PeerAuthenticator
PeerConnection
PeerTransport
PeerSyncService
PeerStateRepository
```

### 33.6 Peer Identity

Each Device must have a stable Device Identity.

```text
Device Identity
      ↓
Peer Identity
```

### 33.7 Peer Trust

A discovered Device must not automatically become a trusted synchronization peer.

```text
Discovered
      ↓
Verify
      ↓
Trusted
```

### 33.8 Peer Authentication

P2P connections must authenticate both endpoints.

```text
Device A ↔ Device B
      ↓
Mutual Authentication
```

### 33.9 Peer Authorization

Authentication alone is not sufficient.

The peer must be authorized to exchange synchronization data for the relevant context.

### 33.10 Group Context

P2P synchronization must be scoped to the appropriate Group/context.

```text
Peer Connection
      +
Group Context
      ↓
Synchronization
```

### 33.11 P2P Transport

The transport implementation must be abstracted from the synchronization logic.

```text
PeerSyncTransport
```

may use the platform/network mechanism selected by the architecture.

### 33.12 Transport Independence

The Sync Engine must not depend directly on the underlying discovery or transport technology.

### 33.13 P2P Connection Flow

```text
Discover Peer
      ↓
Select Peer
      ↓
Authenticate
      ↓
Authorize
      ↓
Establish Secure Channel
      ↓
Negotiate Protocol
      ↓
Synchronize
      ↓
Close / Maintain Connection
```

### 33.14 P2P Protocol Version

Peers must negotiate a compatible synchronization protocol version.

### 33.15 Unsupported Version

If protocol versions are incompatible:

```text
Version Mismatch
      ↓
Reject Synchronization
```

without corrupting local state.

### 33.16 P2P Capabilities

Peers may exchange supported capabilities.

Examples:

```text
Supported Entity Types
Protocol Version
Maximum Batch Size
Compression Support
Conflict Protocol
```

### 33.17 Capability Validation

Synchronization must only use mutually supported capabilities.

### 33.18 P2P Push

P2P Push follows the same logical operation model:

```text
Pending Operations
      ↓
P2P Push
      ↓
Peer
```

### 33.19 P2P Pull

P2P Pull retrieves peer changes:

```text
Peer Changes
      ↓
P2P Pull
      ↓
Local Database
```

### 33.20 P2P Sync State

Peer synchronization state must be persisted where required.

```text
Peer
+
Sync Context
+
Cursor / State
```

### 33.21 Peer-Specific Cursor

If the P2P protocol uses cursors, they must be scoped to the peer synchronization context.

### 33.22 P2P Operation Idempotency

P2P synchronization must use Operation IDs for duplicate detection.

### 33.23 P2P Duplicate Operation

If the same operation is received twice:

```text
Operation ID
      ↓
Already Processed
      ↓
No Duplicate Effect
```

### 33.24 P2P Conflict Detection

P2P synchronization must use the same conflict detection rules.

```text
Remote Operation
      ↓
Version Check
      ↓
Conflict Detection
```

### 33.25 P2P Conflict Resolution

Conflicts must follow the common Conflict Resolution implementation.

### 33.26 P2P Financial Safety

P2P synchronization must not introduce financial state that could not be introduced through Backend synchronization.

### 33.27 P2P Domain Validation

All received operations must pass Domain validation before application.

### 33.28 P2P Authorization

The receiving Device must verify that the operation source is permitted to mutate the relevant Group/resource.

### 33.29 P2P Replay Protection

Previously processed operations must not be replayed to produce additional business effects.

### 33.30 P2P Secure Channel

The communication channel must provide appropriate:

```text
Confidentiality
Integrity
Peer Authentication
```

according to the Security Architecture.

### 33.31 P2P Key Management

Cryptographic keys and trust material must follow the Security Architecture.

### 33.32 P2P Data Exposure

Only synchronization data required for the authorized context should be exchanged.

### 33.33 P2P Group Isolation

A peer must not receive data belonging to Groups for which synchronization is not authorized.

### 33.34 P2P Connection Loss

If the peer connection is lost:

```text
Connection Lost
      ↓
Preserve Local State
      ↓
Keep Pending Operations
      ↓
Retry Later
```

### 33.35 P2P Retry

P2P connection failures should use controlled retry.

### 33.36 P2P Backoff

Repeated connection attempts must use backoff.

### 33.37 P2P Partial Transfer

If a connection fails during transfer:

```text
Partial Transfer
      ↓
Do Not Assume Complete
      ↓
Resume / Retry
```

using Operation IDs and protocol state.

### 33.38 P2P Transfer Atomicity

A partially received operation must not be applied until the complete operation payload has been validated.

### 33.39 P2P Batch Processing

P2P synchronization should use bounded batches.

### 33.40 P2P Large Queue

Large queues must be transferred incrementally.

### 33.41 P2P Discovery Integration

Peer discovery must provide candidate peers to the PeerManager.

```text
Discovery
      ↓
PeerManager
      ↓
Verification
```

### 33.42 P2P Discovery Security

Discovery results must be treated as untrusted until peer identity is verified.

### 33.43 Peer Trust Store

Trusted peer information may be persisted securely according to the Security Architecture.

### 33.44 Peer Revocation

A previously trusted peer must be removable/revocable.

```text
Trusted Peer
      ↓
Revoked
      ↓
Synchronization Blocked
```

### 33.45 P2P Session

A P2P session should have a bounded lifecycle.

Conceptually:

```text
CONNECTING
CONNECTED
SYNCING
IDLE
DISCONNECTED
FAILED
```

### 33.46 Session State

Session state must not be confused with persistent SyncState.

### 33.47 P2P Session Recovery

A disconnected session must be recreated rather than leaving synchronization permanently stuck.

### 33.48 P2P Peer Selection

If multiple peers are available, the PeerManager may select a suitable peer based on:

```text
Trust
Availability
Group Membership
Protocol Compatibility
Signal / Connection Quality
```

according to V1 requirements.

### 33.49 Multiple Peer Synchronization

When multiple peers are used:

```text
Peer A
Peer B
Peer C
```

the same Operation ID and conflict rules must apply across all peers.

### 33.50 P2P Concurrency

Concurrent synchronization with multiple peers must not corrupt local:

```text
Entity State
SyncState
Conflict State
Operation State
```

### 33.51 P2P Synchronization Lock

Appropriate synchronization coordination must prevent incompatible concurrent modifications.

### 33.52 P2P and Backend

P2P synchronization does not replace Backend synchronization.

```text
P2P
 +
Backend
```

must converge toward the same valid Domain state.

### 33.53 Eventual Convergence

If all valid operations are eventually exchanged and conflicts are resolved:

```text
Device A
   ↕
Device B
   ↕
Backend
```

should converge according to the synchronization protocol.

### 33.54 P2P Offline Use

P2P synchronization is specifically intended to support data exchange when Backend connectivity is unavailable.

### 33.55 P2P Reconnection to Backend

After Backend connectivity returns:

```text
P2P Changes
      ↓
Local Sync Queue
      ↓
Backend Push
      ↓
Backend Pull
```

must reconcile the Device with the Backend.

### 33.56 P2P Logging

P2P logs may contain:

```text
Peer ID
Session ID
Operation ID
Connection Result
Sync Result
```

Sensitive payloads must not be logged unnecessarily.

### 33.57 P2P Metrics

Useful metrics include:

```text
Peers Discovered
Successful Connections
Failed Connections
P2P Syncs
Operations Exchanged
Conflicts
Retries
Transfer Duration
```

### 33.58 P2P Testing

P2P implementation must include:

```text
Discovery Tests
Authentication Tests
Authorization Tests
Transport Tests
Synchronization Tests
Conflict Tests
Failure Tests
Security Tests
```

### 33.59 P2P Connection Tests

Test:

```text
Discovery
Connection
Authentication
Protocol Negotiation
Disconnect
Reconnect
```

### 33.60 P2P Synchronization Tests

Test:

```text
Push
Pull
Duplicate Operation
Version Conflict
Retry
Partial Transfer
```

### 33.61 P2P Security Tests

Test:

```text
Unknown Peer
Untrusted Peer
Revoked Peer
Invalid Identity
Replay
Unauthorized Group
```

### 33.62 P2P Offline Tests

Test:

```text
Backend Unavailable
Peer Available
P2P Sync
Backend Returns
Backend Reconciliation
```

### 33.63 P2P Completion Criteria

Peer-to-Peer Implementation is complete when:

```text
Peer Identity Implemented
Peer Trust Implemented
Peer Authentication Implemented
Peer Authorization Implemented
Secure Transport Implemented
Protocol Negotiation Implemented
P2P Push Implemented
P2P Pull Implemented
P2P Sync State Implemented
Idempotency Implemented
Conflict Integration Implemented
Connection Recovery Implemented
Peer Revocation Implemented
Backend Reconciliation Implemented
Security Integrated
Tests Passing
```

### 33.64 P2P Invariants

The following rules are mandatory:

- Discovery does not imply trust.
- Every P2P synchronization session must authenticate the peer.
- Every P2P synchronization session must be authorized.
- P2P synchronization must be scoped to the authorized Group/context.
- Untrusted peers must not receive protected synchronization data.
- P2P must use the common SyncOperation model.
- P2P must use Operation IDs for idempotency.
- Duplicate operations must not create duplicate business effects.
- P2P must use the common conflict detection rules.
- P2P must use the common conflict resolution rules.
- P2P received operations must pass Domain validation.
- Partial operations must never be applied.
- Connection loss must not lose locally persisted operations.
- P2P retry must use controlled backoff.
- Peer revocation must block further protected synchronization.
- P2P synchronization must not corrupt local SyncState.
- P2P synchronization must not change financial outcomes compared with the common Domain rules.
- P2P changes must eventually reconcile with Backend state when Backend connectivity returns.
- P2P implementation must be covered by automated tests.


## 34. P2P Discovery Implementation

### 34.1 Purpose

This section defines the implementation of Peer-to-Peer Device Discovery in SplitSync V1.

P2P Discovery identifies nearby candidate Devices that may participate in synchronization.

```text
Nearby Devices
      ↓
Discovery
      ↓
Candidate Peers
      ↓
Identity Verification
      ↓
Trusted Peer
```

### 34.2 Discovery Principle

Discovery only identifies potential peers.

```text
Discovered
      ≠
Trusted
```

### 34.3 Discovery Responsibility

The Discovery layer is responsible for:

```text
Scanning
Advertising
Candidate Detection
Peer Metadata Retrieval
Discovery Filtering
Discovery Lifecycle
```

### 34.4 Discovery Component

A dedicated component should encapsulate platform discovery.

Conceptually:

```text
PeerDiscovery
```

### 34.5 Discovery Abstraction

The P2P architecture must isolate Android/platform discovery APIs from the synchronization engine.

```text
PeerDiscovery
      ↓
Platform Discovery API
```

### 34.6 Discovery Technologies

The actual discovery mechanism should follow the selected Android/P2P architecture.

Possible technologies may include:

```text
Bluetooth
Wi-Fi Direct
Local Network Discovery
Nearby Connections
```

Only the technology selected for V1 should be implemented.

### 34.7 Discovery Start

Discovery may be initiated by:

```text
User Action
P2P Sync Request
Offline Sync Workflow
Application Flow
```

according to V1 requirements.

### 34.8 Discovery Stop

Discovery must stop when:

```text
Peer Found
Timeout Reached
User Cancels
Application Lifecycle Requires Stop
```

### 34.9 Discovery Timeout

Discovery should use a bounded timeout.

It must not scan indefinitely.

### 34.10 Discovery Result

A discovered peer may provide:

```text
Peer ID
Device Name
Protocol Version
Capabilities
Availability
```

Only non-sensitive metadata should be exposed before authentication.

### 34.11 Peer ID

The Peer ID must correspond to the Device Identity used by the P2P synchronization architecture.

### 34.12 Discovery Metadata Trust

Discovery metadata must be considered untrusted until peer authentication succeeds.

### 34.13 Discovery Filtering

Candidate peers should be filtered based on:

```text
Supported Protocol
Known Group Context
Device State
Availability
```

where information is safely available.

### 34.14 Unknown Peer

Unknown peers may be shown as candidates but must not automatically receive synchronization data.

### 34.15 Trusted Peer

A peer becomes trusted only after:

```text
Identity Verification
+
Authentication
+
Authorization
```

### 34.16 Peer Verification

After discovery:

```text
Candidate Peer
      ↓
Connect
      ↓
Authenticate
      ↓
Verify Identity
```

### 34.17 Peer Authorization

After authentication:

```text
Peer Identity
      ↓
Authorization Check
      ↓
Allowed Context
```

### 34.18 Discovery and Group Context

Discovery should not expose Group data merely because a peer is nearby.

Group synchronization authorization must occur after peer verification.

### 34.19 Discovery Advertisement

If the Device advertises itself:

```text
Device
      ↓
Advertisement
```

the advertisement must expose only the minimum required metadata.

### 34.20 Advertisement Security

The advertisement must not expose:

```text
Financial Data
Expense Details
Settlement Details
Authentication Tokens
Sensitive User Data
```

### 34.21 Advertisement Identity

The advertisement may contain a discoverable identifier that can be mapped to the Device Identity after secure verification.

### 34.22 Discovery Authentication

Discovery itself may not provide full authentication.

Authentication must occur through the secure P2P connection.

### 34.23 Discovery Spoofing

A malicious Device may advertise a fake Peer ID.

Therefore:

```text
Discovered Peer ID
      ↓
Cryptographic / Protocol Verification
```

must be performed before trust.

### 34.24 Discovery Replay

Discovery metadata must not be sufficient to authorize synchronization.

### 34.25 Discovery Duplicate Results

The Discovery layer should de-duplicate repeated discovery events.

```text
Same Peer
   ↓
One Candidate
```

### 34.26 Peer Lifecycle

A discovered peer may transition through:

```text
DISCOVERED
   ↓
VERIFYING
   ↓
TRUSTED
```

or:

```text
DISCOVERED
   ↓
REJECTED
```

### 34.27 Discovery Failure

Discovery failure should be represented separately from:

```text
Authentication Failure
Authorization Failure
Synchronization Failure
```

### 34.28 Discovery Retry

Discovery may be retried after:

```text
Timeout
Temporary Platform Failure
No Peer Found
```

using bounded retries.

### 34.29 Discovery Backoff

Repeated discovery attempts should use controlled scheduling.

### 34.30 Discovery Cancellation

Discovery must support safe cancellation.

Cancellation must release platform resources.

### 34.31 Lifecycle Handling

Discovery must respect Android lifecycle state.

```text
Screen / Feature Active
      ↓
Discovery

Feature Inactive
      ↓
Stop / Release Discovery
```

### 34.32 Battery Considerations

Discovery must minimize:

```text
Continuous Scanning
High-Power Operations
Unnecessary Advertising
```

### 34.33 Background Discovery

Background discovery should only be used if explicitly supported and permitted by the Android architecture and V1 requirements.

### 34.34 Permission Handling

Discovery must handle required Android permissions according to the selected platform technology.

Permission denial must not crash the application.

### 34.35 Permission Failure

If discovery permissions are unavailable:

```text
Discovery
      ↓
Permission Required / Unavailable
      ↓
User Feedback
```

### 34.36 Discovery UI

The UI may display:

```text
Searching
Peers Found
No Peers Found
Permission Required
Discovery Failed
```

### 34.37 Discovery UI Responsibility

The UI should not directly interact with low-level discovery APIs.

```text
UI
 ↓
ViewModel
 ↓
PeerDiscovery
```

### 34.38 Peer Selection

If multiple peers are discovered:

```text
Peer A
Peer B
Peer C
```

the User or PeerManager may select the appropriate peer according to the P2P workflow.

### 34.39 Automatic Peer Selection

If automatic selection is supported, it must use deterministic rules.

Possible criteria:

```text
Trusted Peer
Correct Group
Protocol Compatibility
Availability
Connection Quality
```

### 34.40 Discovery and Trust Store

Previously trusted peers may be recognized from the secure trust store.

However:

```text
Known Peer
      ≠
Currently Authorized Peer
```

Current authorization must still be verified.

### 34.41 Peer Revocation

If a peer has been revoked:

```text
Discovered
      ↓
Revoked Peer
      ↓
Reject
```

### 34.42 Discovery and Device Identity

The local Device Identity must never be confused with a temporary discovery/session identifier.

### 34.43 Session Identifier

A P2P connection may have:

```text
Peer ID
+
Session ID
```

where:

```text
Peer ID = Device Identity
Session ID = One Connection
```

### 34.44 Discovery Transport Independence

The discovery implementation must remain replaceable without changing:

```text
Sync Engine
SyncOperation
Conflict Detection
Conflict Resolution
```

### 34.45 Discovery Logging

Logs may contain:

```text
Peer ID
Discovery Start
Discovery End
Candidate Count
Connection Result
```

Sensitive information must not be logged.

### 34.46 Discovery Metrics

Useful metrics include:

```text
Discovery Attempts
Peers Found
Successful Connections
Discovery Time
Permission Failures
No-Peer Results
```

### 34.47 Discovery Testing

Discovery implementation must include:

```text
Discovery Start
Discovery Stop
Peer Found
Duplicate Peer
No Peer
Timeout
Permission Failure
Revoked Peer
Lifecycle Stop
```

### 34.48 Discovery Security Tests

Test:

```text
Fake Peer
Spoofed Identity
Unknown Peer
Revoked Peer
Unauthorized Peer
```

### 34.49 Discovery Lifecycle Tests

Test:

```text
Start Discovery
Pause / Stop
Resume
Application Restart
```

### 34.50 Discovery Performance Tests

Test:

```text
Many Nearby Devices
Repeated Discovery
Large Candidate Set
```

### 34.51 Discovery Completion Criteria

P2P Discovery Implementation is complete when:

```text
Discovery Abstraction Implemented
Platform Integration Implemented
Scanning Implemented
Advertising Implemented Where Required
Candidate Filtering Implemented
Peer Deduplication Implemented
Timeout Implemented
Cancellation Implemented
Permission Handling Implemented
Identity Verification Integrated
Trust Verification Integrated
Revocation Handling Implemented
Lifecycle Handling Implemented
Security Integrated
Metrics / Logging Implemented
Tests Passing
```

### 34.52 P2P Discovery Invariants

The following rules are mandatory:

- Discovery only identifies candidate peers.
- Discovery does not establish trust.
- Discovered peers must be authenticated before synchronization.
- Discovered peers must be authorized before receiving protected data.
- Discovery metadata must be treated as untrusted.
- Discovery advertisements must not expose sensitive financial data.
- Authentication tokens must not be exposed through discovery.
- Fake or spoofed peer identities must not become trusted automatically.
- Unknown peers must not receive protected synchronization data.
- Revoked peers must be rejected.
- Duplicate discovery events must not create duplicate peer sessions.
- Discovery must use bounded scanning.
- Discovery must stop when its lifecycle ends.
- Discovery must release platform resources.
- Required Android permissions must be handled safely.
- Permission denial must not crash the application.
- Discovery must minimize unnecessary battery consumption.
- Discovery implementation must remain independent from synchronization logic.
- Discovery must be covered by automated tests.

## 35. P2P Connection and Handshake

### 35.1 Purpose

This section defines the implementation of P2P connection establishment and handshake in SplitSync V1.

The handshake establishes:

```text
Peer Identity
Protocol Compatibility
Trust
Authentication
Authorization Context
Secure Session
```

before synchronization begins.

### 35.2 Connection Principle

Discovery identifies a candidate peer.

The handshake establishes whether that peer can participate in synchronization.

```text
Discovered Peer
      ↓
Connect
      ↓
Handshake
      ↓
Authenticate
      ↓
Authorize
      ↓
Secure Session
      ↓
P2P Synchronization
```

### 35.3 Connection Components

The implementation should contain logical components such as:

```text
PeerConnection
PeerAuthenticator
HandshakeService
PeerSession
PeerTrustManager
```

### 35.4 Connection Lifecycle

A P2P connection may transition through:

```text
DISCONNECTED
   ↓
CONNECTING
   ↓
HANDSHAKING
   ↓
CONNECTED
   ↓
SYNCING
   ↓
DISCONNECTED
```

Failure may transition to:

```text
CONNECTING
   ↓
FAILED
```

### 35.5 Connection Initiation

The connection initiator must have a discovered or otherwise authorized peer candidate.

```text
Peer Candidate
      ↓
Connection Request
```

### 35.6 Connection Acceptance

The receiving Device must accept the connection only when the underlying transport and platform security requirements are satisfied.

### 35.7 Handshake Start

After transport connection:

```text
Transport Connected
      ↓
HANDSHAKING
```

No synchronization operation should be exchanged before successful handshake completion.

### 35.8 Handshake Information

The handshake should exchange the minimum information required to establish a trusted synchronization session.

Conceptually:

```text
Device Identity
Protocol Version
Capabilities
Authentication Material
Synchronization Context
```

### 35.9 Protocol Version

Both peers must establish a mutually supported synchronization protocol version.

```text
Peer A Version
+
Peer B Version
      ↓
Compatible?
```

### 35.10 Protocol Mismatch

If no compatible protocol version exists:

```text
Version Mismatch
      ↓
Reject Connection
```

The connection must not proceed to synchronization.

### 35.11 Capability Negotiation

Peers may negotiate:

```text
Supported Entity Types
Batch Size
Compression
Conflict Protocol
Synchronization Features
```

Only capabilities supported by both peers may be used.

### 35.12 Peer Identity

Each peer must present its Device Identity during the handshake.

### 35.13 Identity Verification

The receiving peer must verify that the presented identity corresponds to the authenticated peer.

```text
Presented Identity
      ↓
Verification
      ↓
Valid?
```

### 35.14 Peer Authentication

P2P authentication must establish that:

```text
Peer A is Peer A
Peer B is Peer B
```

according to the Security Architecture.

### 35.15 Mutual Authentication

Where required by the security model:

```text
Device A authenticates Device B
Device B authenticates Device A
```

must complete before synchronization.

### 35.16 Trust Verification

A successfully authenticated Device must still be checked against the applicable trust/authorization policy.

```text
Authenticated
      ↓
Trusted / Authorized?
```

### 35.17 Revoked Peer

A revoked peer must be rejected.

```text
Peer Revoked
      ↓
Handshake Rejected
```

### 35.18 Group Context

The handshake must establish or verify the synchronization context.

```text
Peer Identity
+
Group Context
      ↓
Synchronization Allowed?
```

### 35.19 Group Authorization

A peer must not synchronize data for a Group unless the peer is authorized for that Group.

### 35.20 Secure Session

After successful authentication:

```text
Authenticated Peer
      ↓
Secure Session
```

must be established according to the Security Architecture.

### 35.21 Session Identity

Each active connection may have a Session ID.

```text
Peer ID
+
Session ID
```

must remain separate concepts.

### 35.22 Session Lifetime

A session exists only for the active P2P connection.

Closing the connection must invalidate the session.

### 35.23 Handshake Completion

Handshake succeeds only when:

```text
Identity Verified
Authentication Successful
Authorization Successful
Protocol Compatible
Capabilities Negotiated
Secure Channel Established
```

### 35.24 Synchronization Gate

Synchronization must only begin after successful handshake completion.

```text
Handshake SUCCESS
      ↓
P2P Sync
```

### 35.25 Handshake Failure

Any required handshake step failure must prevent synchronization.

```text
Handshake Failure
      ↓
Close / Reject Connection
```

### 35.26 Authentication Failure

Authentication failure must not expose synchronization data.

### 35.27 Authorization Failure

Authorization failure must not expose Group data or SyncOperations.

### 35.28 Invalid Identity

An invalid identity must cause the connection to be rejected.

### 35.29 Unsupported Protocol

Unsupported protocol versions must cause safe connection termination.

### 35.30 Invalid Capability

Invalid or unsupported capability information must not be trusted.

### 35.31 Handshake Timeout

Handshake must have a bounded timeout.

```text
HANDSHAKING
      ↓
Timeout
      ↓
FAILED
```

### 35.32 Connection Timeout

Connection establishment must also use a bounded timeout.

### 35.33 Connection Retry

Temporary connection failures may be retried using the Retry and Recovery implementation.

### 35.34 Connection Backoff

Repeated connection failures must use controlled backoff.

### 35.35 Connection Loss

If the connection is lost after handshake:

```text
CONNECTED
      ↓
DISCONNECTED
```

Local synchronization state must remain intact.

### 35.36 Reconnection

A disconnected peer must establish a new secure session before synchronization resumes.

```text
Disconnected
      ↓
Reconnect
      ↓
New Handshake
      ↓
Synchronize
```

### 35.37 Session Reuse

A previously established session must not be reused after its security/session lifetime has expired.

### 35.38 Replay Protection

Handshake messages must be protected against replay according to the Security Architecture.

### 35.39 Challenge / Response

If the authentication protocol uses challenge/response:

```text
Challenge
      ↓
Response
      ↓
Verification
```

must complete before the session is trusted.

### 35.40 Cryptographic Verification

Where cryptographic identity verification is used, cryptographic verification must be completed before protected synchronization data is exchanged.

### 35.41 Handshake Data Minimization

Only information necessary for:

```text
Authentication
Authorization
Protocol Negotiation
Synchronization
```

should be exchanged.

### 35.42 Financial Data Protection

Handshake messages must never contain unnecessary:

```text
Expense Details
Settlement Details
Balance Details
```

### 35.43 Handshake Logging

Handshake logs may contain:

```text
Session ID
Peer ID
Protocol Version
Handshake Result
Failure Category
```

Sensitive authentication material must never be logged.

### 35.44 Handshake Metrics

Useful metrics include:

```text
Handshake Attempts
Successful Handshakes
Failed Handshakes
Authentication Failures
Authorization Failures
Version Mismatches
Handshake Duration
```

### 35.45 Connection Testing

Test:

```text
Successful Connection
Authentication Failure
Authorization Failure
Invalid Identity
Protocol Mismatch
Timeout
Revoked Peer
Connection Loss
Reconnect
```

### 35.46 Security Testing

Test:

```text
Fake Peer
Identity Spoofing
Replay
Invalid Credentials
Invalid Session
Revoked Device
```

### 35.47 Connection Completion Criteria

P2P Connection and Handshake Implementation is complete when:

```text
Connection Lifecycle Implemented
Handshake Protocol Implemented
Peer Identity Verification Implemented
Authentication Implemented
Authorization Implemented
Protocol Negotiation Implemented
Capability Negotiation Implemented
Secure Session Implemented
Timeout Implemented
Connection Recovery Implemented
Replay Protection Implemented
Revocation Handling Implemented
Logging / Metrics Implemented
Tests Passing
```

### 35.48 Connection and Handshake Invariants

The following rules are mandatory:

- Discovery does not establish a trusted connection.
- Synchronization must not begin before handshake completion.
- Peer identity must be verified.
- Peer authentication must succeed before protected data exchange.
- Peer authorization must succeed before synchronization.
- Revoked peers must be rejected.
- Protocol incompatibility must prevent synchronization.
- Only mutually supported capabilities may be used.
- Handshake must establish a secure session.
- Handshake must use bounded timeouts.
- Failed handshakes must not expose synchronization data.
- Session identity must remain separate from Device Identity.
- Reconnection must establish a new valid session.
- Handshake must provide replay protection.
- Authentication material must never be logged.
- P2P connection and handshake behavior must be covered by automated tests.


## 36. P2P Synchronization Implementation

### 36.1 Purpose

This section defines the complete P2P synchronization execution after a successful connection and handshake.

```text
Secure P2P Session
      ↓
P2P Sync Engine
      ↓
Push / Pull
      ↓
Local State Convergence
```

### 36.2 P2P Sync Principle

P2P synchronization must reuse the common synchronization concepts already implemented for Backend synchronization.

```text
SyncOperation
SyncState
Conflict Detection
Conflict Resolution
Idempotency
Versioning
```

### 36.3 P2P Sync Entry Point

After handshake:

```text
PeerSession
      ↓
PeerSyncService
      ↓
SyncEngine
```

### 36.4 P2P Sync Context

The synchronization context must identify:

```text
Local Device
Remote Peer
Group / Scope
Protocol Version
Sync State
```

### 36.5 P2P Sync State

P2P synchronization state must be maintained separately where the protocol requires peer-specific state.

### 36.6 P2P Synchronization Cycle

Conceptually:

```text
Handshake
   ↓
Load P2P Sync State
   ↓
Exchange Sync Metadata
   ↓
Push Local Operations
   ↓
Pull Peer Operations
   ↓
Apply Changes
   ↓
Update P2P Sync State
   ↓
Complete
```

### 36.7 Sync Metadata Exchange

Peers may exchange:

```text
Cursor
Last Known Position
Pending Operation Metadata
Protocol Information
```

according to the P2P protocol.

### 36.8 Push Phase

```text
Local Pending Operations
      ↓
P2P Transport
      ↓
Peer
```

### 36.9 Pull Phase

```text
Peer Changes
      ↓
P2P Transport
      ↓
Local Pull Processor
```

### 36.10 P2P Operation Validation

Every received operation must pass:

```text
Structural Validation
Peer Authentication
Authorization
Duplicate Check
Version Check
Domain Validation
```

### 36.11 P2P Operation Application

Valid operations must be applied using the same Domain/application rules used by Backend synchronization.

### 36.12 P2P Transaction Boundary

Remote P2P changes must be persisted atomically where multiple records are affected.

### 36.13 P2P Cursor Update

The P2P cursor must only advance after corresponding operations are safely persisted.

### 36.14 P2P Duplicate Handling

Duplicate Operation IDs must not produce duplicate financial effects.

### 36.15 P2P Conflict Detection

Conflicting operations must be sent through the common Conflict Detection implementation.

### 36.16 P2P Conflict Resolution

Conflict resolution must use the common Conflict Resolution implementation.

### 36.17 P2P Retry

Temporary P2P failures must use the common Retry and Recovery implementation.

### 36.18 P2P Connection Loss During Sync

If connection loss occurs:

```text
Synchronization Interrupted
      ↓
Persist Completed Work
      ↓
Preserve Remaining Operations
      ↓
Reconnect Later
```

### 36.19 P2P Partial Synchronization

A partially completed synchronization must leave each successfully applied operation in its correct state.

### 36.20 P2P Recovery

After reconnection:

```text
New Handshake
      ↓
Load Persisted State
      ↓
Continue Synchronization
```

### 36.21 P2P Queue

Pending operations must remain in the local persistent queue until safely synchronized.

### 36.22 P2P Batch Size

P2P batches must be bounded.

### 36.23 P2P Large Queue

Large queues must be synchronized incrementally.

### 36.24 P2P Ordering

Operation dependencies must be respected.

### 36.25 P2P Idempotency

Repeated synchronization attempts must remain safe.

### 36.26 P2P Financial State

P2P synchronization must preserve:

```text
Expense Amount
Expense Splits
Settlement Amount
Payer
Receiver
Balance
```

### 36.27 P2P Membership State

Membership changes must update local authorization state.

### 36.28 P2P Tombstones

Deletion state must follow the common tombstone strategy.

### 36.29 P2P Backend Reconciliation

After Backend connectivity returns:

```text
P2P Changes
      ↓
Local Sync Queue
      ↓
Backend Synchronization
      ↓
Conflict Detection
      ↓
Converged State
```

### 36.30 P2P and Backend Authority

P2P synchronization must not bypass the Backend's synchronization rules when changes are later submitted to the Backend.

### 36.31 P2P Convergence

After valid operations are exchanged and conflicts resolved:

```text
Device A
   ↕
Device B
   ↕
Backend
```

must converge according to the defined synchronization model.

### 36.32 P2P Security

All synchronization must occur through the authenticated secure P2P session.

### 36.33 P2P Authorization Scope

Only data authorized for the current peer/session/context may be exchanged.

### 36.34 P2P Session Expiration

When the secure session expires:

```text
Stop Synchronization
      ↓
Re-handshake
```

### 36.35 P2P Sync Cancellation

Synchronization must support safe cancellation.

### 36.36 P2P Sync Completion

P2P synchronization is complete when:

```text
Required Push Complete
+
Required Pull Complete
+
Local State Consistent
+
Sync State Persisted
```

### 36.37 P2P Sync Testing

Test:

```text
Successful Sync
No Changes
Push Only
Pull Only
Push + Pull
Duplicate Operations
Conflict
Connection Loss
Reconnect
Large Queue
Backend Reconciliation
```

### 36.38 P2P Sync Completion Criteria

P2P Synchronization Implementation is complete when:

```text
P2P Sync Coordinator Implemented
Push Integrated
Pull Integrated
P2P Sync State Implemented
Operation Validation Integrated
Idempotency Integrated
Conflict Detection Integrated
Conflict Resolution Integrated
Retry / Recovery Integrated
Cursor Handling Implemented
Batching Implemented
Connection Recovery Implemented
Backend Reconciliation Implemented
Security Integrated
Tests Passing
```

### 36.39 P2P Synchronization Invariants

The following rules are mandatory:

- P2P synchronization must only execute over an authenticated and authorized session.
- P2P must use the common SyncOperation model.
- P2P must use the common validation rules.
- P2P must use the common conflict rules.
- P2P must use the common retry rules.
- Duplicate operations must not create duplicate business effects.
- P2P cursor advancement must occur only after successful persistence.
- Connection loss must not lose completed local work.
- Pending operations must remain durable.
- Partial synchronization must be safely recoverable.
- Operation dependencies must be respected.
- Financial state must remain correct.
- P2P synchronization must reconcile with Backend synchronization.
- P2P synchronization must be covered by automated tests.


## 37. Transaction Implementation

### 37.1 Purpose

This section defines transaction implementation across the Android and Backend layers.

Transactions must preserve consistency of Domain and synchronization state.

### 37.2 Transaction Principle

A transaction must ensure:

```text
All Required Changes
      ↓
Commit Together
```

or:

```text
Any Required Failure
      ↓
Rollback
```

### 37.3 Transaction Boundary

Transaction boundaries must follow Domain consistency requirements rather than arbitrary method boundaries.

### 37.4 Local Transaction

Android transactions use the local database transaction mechanism.

### 37.5 Backend Transaction

Backend transactions use the selected persistence transaction mechanism.

### 37.6 Expense Transaction

Expense creation must atomically persist:

```text
Expense
+
ExpenseSplits
+
SyncOperation
```

where synchronization is required.

### 37.7 Settlement Transaction

Settlement creation must atomically persist:

```text
Settlement
+
SyncOperation
```

where synchronization is required.

### 37.8 Group Transaction

Group creation may require:

```text
Group
+
Initial Membership
+
SyncOperation
```

to commit together.

### 37.9 Membership Transaction

Membership mutations must preserve required Group authorization state.

### 37.10 Remote Operation Transaction

Incoming synchronization operations must atomically persist:

```text
Domain State
+
Synchronization Metadata
```

where required.

### 37.11 Cursor Transaction

Pull processing should atomically coordinate:

```text
Remote Changes
+
Cursor
```

where supported by the database/protocol.

### 37.12 Conflict Transaction

Conflict creation may require:

```text
Conflict
+
Operation State
+
SyncState
```

to remain consistent.

### 37.13 Resolution Transaction

Conflict resolution may require:

```text
Resolved Domain State
+
Conflict State
+
New SyncOperation
```

to commit together.

### 37.14 Transaction Rollback

Any failure that invalidates the transaction must cause rollback.

### 37.15 Partial Commit Prevention

The application must prevent states such as:

```text
Expense Saved
ExpenseSplits Missing
```

or:

```text
Expense Saved
SyncOperation Missing
```

when both are required.

### 37.16 Transaction Isolation

Transaction isolation must follow the capabilities of the selected database and the consistency requirements of the Domain.

### 37.17 Concurrent Transactions

Concurrent mutations must not result in lost updates or invalid financial state.

### 37.18 Optimistic Concurrency

Where versioning is used:

```text
Expected Version
      ↓
Current Version
      ↓
Match?
```

must be checked before commit.

### 37.19 Transaction Failure

A failed transaction must return an appropriate application error without falsely reporting success.

### 37.20 Transaction Retry

Only safe transient transaction failures may be retried.

### 37.21 Transaction Logging

Transaction logs should identify:

```text
Transaction Type
Entity Type
Entity ID
Result
```

without exposing unnecessary sensitive data.

### 37.22 Transaction Testing

Test:

```text
Successful Commit
Rollback
Constraint Failure
Concurrent Update
Crash
Retry
Nested/Composed Operations
```

### 37.23 Transaction Completion Criteria

Transaction Implementation is complete when:

```text
Local Transactions Implemented
Backend Transactions Implemented
Expense Transactions Implemented
Settlement Transactions Implemented
Group Transactions Implemented
Membership Transactions Implemented
Sync Transactions Implemented
Cursor Transactions Implemented
Conflict Transactions Implemented
Resolution Transactions Implemented
Concurrency Handling Implemented
Rollback Verified
Tests Passing
```

### 37.24 Transaction Invariants

The following rules are mandatory:

- Required related Domain state must commit atomically.
- Required SyncOperations must commit atomically with local mutations.
- Partial financial state must never be committed.
- Cursor updates must not commit independently from required remote state application.
- Conflict state must remain consistent with operation state.
- Resolution state and required SyncOperation state must remain consistent.
- Concurrent transactions must not produce invalid financial state.
- Transaction retries must be safe.
- Failed transactions must not report success.
- Transaction behavior must be covered by automated tests.


## 38. Error Handling Implementation

### 38.1 Purpose

This section defines the implementation of application, synchronization, API, database, and P2P error handling.

### 38.2 Error Handling Principle

Errors must be:

```text
Detected
Classified
Handled
Logged Appropriately
Exposed Safely
Recoverable Where Possible
```

### 38.3 Error Categories

The implementation should distinguish:

```text
Validation Error
Authentication Error
Authorization Error
Network Error
Timeout Error
Database Error
Synchronization Error
Conflict Error
P2P Error
Configuration Error
Unexpected Error
```

### 38.4 Domain Error

Domain validation failures should return structured Domain errors.

Examples:

```text
Invalid Amount
Invalid Split
Invalid Membership
Invalid Settlement
```

### 38.5 Application Error

Application services should translate lower-level errors into application-level outcomes where required.

### 38.6 Repository Error

Repositories should not expose infrastructure-specific exceptions directly to Presentation unless explicitly required.

### 38.7 API Error

Backend APIs should return structured error responses.

Conceptually:

```text
Error Code
Message
Details
Request ID
```

### 38.8 Error Codes

Errors should use stable machine-readable codes.

Example:

```text
INVALID_REQUEST
UNAUTHORIZED
FORBIDDEN
NOT_FOUND
CONFLICT
VALIDATION_FAILED
TEMPORARY_FAILURE
```

### 38.9 User Message

User-facing messages must be safe and understandable.

Internal exception details must not be exposed directly.

### 38.10 Error Mapping

```text
Infrastructure Error
      ↓
Repository Error
      ↓
Application Error
      ↓
Presentation State
```

### 38.11 Network Error

Network failures should result in:

```text
Preserve Local State
Queue / Retain Operation
Retry Later
```

where applicable.

### 38.12 Timeout Error

Timeouts must be treated as potentially ambiguous for synchronization operations.

### 38.13 Authentication Error

Authentication errors should trigger the Authentication recovery flow where possible.

### 38.14 Authorization Error

Authorization errors must not be treated as retryable network failures.

### 38.15 Conflict Error

Conflict errors must be routed to Conflict Detection/Resolution.

### 38.16 Database Error

Database failures must not result in partial financial state.

### 38.17 P2P Error

P2P errors should distinguish:

```text
Discovery
Connection
Handshake
Authentication
Authorization
Transport
Synchronization
```

### 38.18 Configuration Error

Missing or invalid required configuration must fail safely.

### 38.19 Unexpected Error

Unexpected errors must be captured by the application's global error handling mechanism without exposing internal details to the User.

### 38.20 Error Recovery

Recovery behavior must be defined per error category.

```text
Retryable → Retry
Conflict → Resolve
Authentication → Re-authenticate
Authorization → Stop
Validation → Correct Input
Permanent → Fail Safely
```

### 38.21 Error Propagation

Errors should propagate through controlled abstractions.

### 38.22 Exception Boundary

Exceptions should be caught at appropriate infrastructure/application boundaries.

The implementation must avoid broad exception swallowing.

### 38.23 Error Logging

Errors should log enough information for diagnosis without exposing:

```text
Passwords
Tokens
Cryptographic Keys
Sensitive Financial Payloads
Personal Data
```

### 38.24 Error Correlation

Where applicable, errors should include:

```text
Request ID
Sync ID
Operation ID
Session ID
```

for tracing.

### 38.25 Error Metrics

Track:

```text
Error Count
Error Type
Retry Count
Conflict Count
Authentication Failures
Authorization Failures
```

### 38.26 Error UI State

Presentation should expose controlled states such as:

```text
Loading
Success
Validation Error
Offline
Sync Pending
Sync Failed
Authentication Required
Permission Denied
Conflict
Unexpected Error
```

### 38.27 Error Recovery UI

Where User action is required:

```text
Error
 ↓
Action
 ↓
Retry / Re-authenticate / Resolve
```

### 38.28 Error Testing

Test:

```text
Validation Failure
Network Failure
Timeout
Authentication Failure
Authorization Failure
Database Failure
Conflict
P2P Failure
Unexpected Exception
```

### 38.29 Error Completion Criteria

Error Handling Implementation is complete when:

```text
Error Categories Implemented
Error Mapping Implemented
API Error Model Implemented
Repository Error Mapping Implemented
Retry Integration Implemented
Conflict Integration Implemented
Authentication Recovery Integrated
Authorization Handling Integrated
Safe UI Error States Implemented
Logging Implemented
Metrics Implemented
Tests Passing
```

### 38.30 Error Handling Invariants

The following rules are mandatory:

- Errors must be classified before recovery decisions are made.
- Validation errors must not be treated as retryable network errors.
- Authorization failures must not be blindly retried.
- Authentication failures must follow authentication recovery.
- Conflicts must use the Conflict architecture.
- Network failures must not discard committed local state.
- Database failures must not leave partial financial state.
- Timeout recovery must remain idempotent.
- User-facing errors must not expose sensitive internal details.
- Logs must not contain authentication secrets or cryptographic keys.
- Unexpected exceptions must not crash the application without controlled handling.
- Error handling must be covered by automated tests.


## 39. Logging Implementation

### 39.1 Purpose

This section defines the implementation of application and synchronization logging in SplitSync V1.

Logging exists primarily for:

```text
Debugging
Operational Diagnosis
Synchronization Troubleshooting
Failure Analysis
```

### 39.2 Logging Principle

Logs must provide useful diagnostic information while minimizing sensitive data exposure.

### 39.3 Log Levels

The implementation may use:

```text
DEBUG
INFO
WARN
ERROR
```

### 39.4 DEBUG

DEBUG should be used for development-level diagnostics.

It should not be enabled indiscriminately in production.

### 39.5 INFO

INFO should represent important application/system events.

Examples:

```text
Application Started
Sync Started
Sync Completed
Peer Connected
```

### 39.6 WARN

WARN should represent recoverable or unusual conditions.

Examples:

```text
Retry Scheduled
Stale Sync Operation
Unexpected Peer Disconnect
```

### 39.7 ERROR

ERROR should represent failures requiring investigation.

Examples:

```text
Database Failure
Synchronization Failure
Unexpected Exception
```

### 39.8 Structured Logging

Logs should use structured fields where possible.

Conceptually:

```text
timestamp
level
component
event
requestId
syncId
operationId
errorCode
```

### 39.9 Correlation IDs

The implementation should use correlation identifiers for tracing.

Possible identifiers:

```text
Request ID
Sync ID
Operation ID
Session ID
Batch ID
```

### 39.10 Operation Logging

Synchronization logs may include:

```text
Operation ID
Entity Type
Operation Type
Result
```

### 39.11 Sync Logging

Sync logs may include:

```text
Sync ID
Direction
Batch Size
Success
Failure
Duration
```

### 39.12 P2P Logging

P2P logs may include:

```text
Peer ID
Session ID
Connection State
Handshake Result
Sync Result
```

### 39.13 Security Logging

Security-relevant events may include:

```text
Authentication Failure
Authorization Failure
Peer Rejection
Device Revocation
```

### 39.14 Sensitive Data

The logging implementation must never intentionally log:

```text
Passwords
Authentication Tokens
Private Keys
Encryption Keys
Raw Credentials
```

### 39.15 Financial Data

Logs should not contain complete financial payloads unless explicitly required for a controlled diagnostic purpose.

Prefer:

```text
Expense ID
Operation ID
```

over:

```text
Full Expense Payload
```

### 39.16 Personal Data

Personal data should be minimized in logs.

### 39.17 Exception Logging

Exceptions should include:

```text
Error Type
Error Code
Stack Trace
Correlation ID
```

where appropriate.

### 39.18 Stack Traces

Stack traces may be captured for internal diagnostics but must not be exposed directly to Users.

### 39.19 Production Logging

Production logging should use appropriate log levels and avoid verbose sensitive diagnostics.

### 39.20 Development Logging

Development environments may enable additional diagnostic logging.

### 39.21 Environment Configuration

Logging levels should be configurable per environment.

### 39.22 Log Retention

Log retention must follow the application's operational and security requirements.

### 39.23 Log Rotation

Long-running systems should use log rotation or bounded storage.

### 39.24 Android Logging

Android logging must use the logging abstraction selected by the project.

Application code should not scatter direct platform logging calls throughout the Domain layer.

### 39.25 Backend Logging

Backend logging should use the server-side logging framework selected by the project.

### 39.26 Logging Abstraction

Application components should depend on a logging abstraction where practical.

Conceptually:

```text
Logger
```

### 39.27 Domain Logging

The Domain layer should remain minimally dependent on infrastructure logging.

### 39.28 Repository Logging

Repositories may log infrastructure failures with appropriate context.

### 39.29 Sync Logging

Synchronization components should log:

```text
Start
Batch
Result
Retry
Conflict
Completion
```

at appropriate levels.

### 39.30 Logging and Performance

Logging must not significantly degrade synchronization or application performance.

### 39.31 Logging Failure

Failure of logging infrastructure must not cause the financial transaction or synchronization operation itself to fail unless explicitly required.

### 39.32 Logging Metrics

Logging systems may expose:

```text
Log Volume
Error Volume
Critical Events
```

for operational monitoring.

### 39.33 Logging Testing

Test:

```text
Correct Log Level
Required Context
Sensitive Data Redaction
Exception Logging
Environment Configuration
```

### 39.34 Logging Completion Criteria

Logging Implementation is complete when:

```text
Logging Abstraction Implemented
Log Levels Implemented
Structured Logging Implemented
Correlation IDs Implemented
Sensitive Data Protection Implemented
Android Logging Integrated
Backend Logging Integrated
Sync Logging Integrated
P2P Logging Integrated
Environment Configuration Implemented
Retention / Rotation Defined
Tests Passing
```

### 39.35 Logging Invariants

The following rules are mandatory:

- Logs must support operational diagnosis.
- Logs must not contain passwords.
- Logs must not contain authentication tokens.
- Logs must not contain private or encryption keys.
- Logs must minimize personal data.
- Logs must minimize financial payload exposure.
- Correlation IDs should be used for synchronization tracing.
- Logging failures must not corrupt Domain state.
- Production logging must use appropriate verbosity.
- Sensitive security events must be logged without exposing secrets.
- Logging behavior must be covered by automated tests.


## 40. Configuration Implementation

### 40.1 Purpose

This section defines the implementation of application configuration in SplitSync V1.

Configuration controls environment-specific and operational values without embedding them directly into business logic.

### 40.2 Configuration Principle

Configuration must be:

```text
Centralized
Typed
Validated
Environment-Aware
Secure
```

### 40.3 Configuration Categories

Configuration may include:

```text
Backend URL
API Settings
Sync Settings
Retry Settings
P2P Settings
Logging Settings
Feature Flags
Database Settings
```

### 40.4 Configuration Separation

Business rules must not depend directly on environment configuration.

```text
Configuration
      ↓
Infrastructure
      ↓
Application
```

### 40.5 Android Configuration

Android configuration should support:

```text
Development
Testing
Production
```

through the selected build configuration strategy.

### 40.6 Backend Configuration

Backend configuration should support separate environment-specific values.

### 40.7 Environment Variables

Backend secrets and environment-specific values should be supplied through secure environment configuration.

### 40.8 Secret Management

Secrets must not be committed to source control.

Examples:

```text
API Secrets
Private Keys
Database Credentials
Signing Secrets
```

### 40.9 Android Secret Handling

Sensitive server-side secrets must never be embedded in the Android application as if they were confidential.

### 40.10 Configuration Object

Application configuration should be represented by typed configuration objects where practical.

Conceptually:

```text
AppConfig
SyncConfig
P2PConfig
LoggingConfig
```

### 40.11 Configuration Validation

Configuration must be validated during application/service startup.

### 40.12 Invalid Configuration

Invalid required configuration must fail fast and clearly.

```text
Invalid Configuration
      ↓
Startup Failure
```

rather than allowing unpredictable runtime behavior.

### 40.13 Default Values

Safe defaults may be provided for non-sensitive operational values.

### 40.14 Required Values

Required values must not silently fall back to unsafe defaults.

### 40.15 Sync Configuration

Synchronization configuration may include:

```text
Batch Size
Retry Limit
Retry Delay
Backoff Limit
Sync Interval
```

### 40.16 P2P Configuration

P2P configuration may include:

```text
Discovery Timeout
Connection Timeout
Handshake Timeout
Maximum Batch Size
Session Timeout
```

### 40.17 Logging Configuration

Logging configuration may include:

```text
Log Level
Environment
Retention
Diagnostic Mode
```

### 40.18 API Configuration

API configuration may include:

```text
Base URL
Connection Timeout
Read Timeout
Write Timeout
```

### 40.19 Database Configuration

Database configuration may include:

```text
Database Name
Migration Strategy
Debug Configuration
```

Sensitive database credentials must be securely managed on the Backend.

### 40.20 Feature Flags

Feature flags may be used for controlled activation of optional features.

Examples:

```text
P2P Enabled
Experimental Sync Feature
Diagnostic Logging
```

### 40.21 Feature Flag Principle

Feature flags must not be used to bypass Domain validation or Security controls.

### 40.22 Configuration Immutability

Configuration should be treated as immutable during normal application execution where practical.

### 40.23 Configuration Reload

Dynamic configuration reload should only be supported where explicitly required.

### 40.24 Configuration and Security

Security-sensitive configuration must be protected from unauthorized modification.

### 40.25 Configuration and Build Types

Android build types/flavors may provide environment-specific configuration.

Conceptually:

```text
debug
release
```

or:

```text
dev
staging
production
```

according to the Build Configuration strategy.

### 40.26 Configuration and Testing

Tests must be able to supply deterministic configuration.

### 40.27 Test Configuration

Test environments should use isolated:

```text
Database
Backend URL
Credentials
Sync Settings
```

### 40.28 Configuration Validation Tests

Test:

```text
Valid Configuration
Missing Required Value
Invalid URL
Invalid Timeout
Invalid Batch Size
Invalid Retry Limit
```

### 40.29 Configuration Security Tests

Test that:

```text
Secrets Are Not Logged
Secrets Are Not Committed
Invalid Secrets Fail Safely
```

### 40.30 Configuration Logging

Configuration logs must never print secret values.

Safe example:

```text
Backend URL configured
```

Unsafe example:

```text
API Secret = ...
```

### 40.31 Configuration Error Handling

Configuration failures should be reported clearly to developers/operators without exposing secret values.

### 40.32 Configuration Documentation

Every configuration property must document:

```text
Name
Purpose
Type
Required / Optional
Default
Environment
Security Classification
```

where applicable.

### 40.33 Configuration Ownership

Configuration should have clear ownership by component.

Example:

```text
SyncConfig → Sync Layer
P2PConfig → P2P Layer
LoggingConfig → Logging Layer
```

### 40.34 Configuration Dependency Injection

Configuration should be injected into components rather than read directly from global state wherever practical.

### 40.35 Configuration and Domain

Domain logic should receive business-relevant configuration through explicit abstractions rather than reading environment variables directly.

### 40.36 Configuration Migration

Changes to configuration structure must be documented and validated across environments.

### 40.37 Configuration Completion Criteria

Configuration Implementation is complete when:

```text
Configuration Model Implemented
Typed Configuration Implemented
Environment Configuration Implemented
Validation Implemented
Secret Handling Implemented
Android Configuration Integrated
Backend Configuration Integrated
Sync Configuration Integrated
P2P Configuration Integrated
Logging Configuration Integrated
Feature Flags Integrated Where Required
Testing Configuration Implemented
Documentation Completed
Tests Passing
```

### 40.38 Configuration Invariants

The following rules are mandatory:

- Configuration must remain separate from Domain business logic.
- Required configuration must be validated at startup.
- Invalid required configuration must fail safely.
- Secrets must never be committed to source control.
- Secrets must not be logged.
- Server-side secrets must not be embedded in Android.
- Configuration should be typed and validated.
- Environment-specific configuration must remain separate.
- Test configuration must remain isolated from production configuration.
- Feature flags must not bypass Domain or Security rules.
- Configuration should be injected rather than accessed through uncontrolled global state.
- Configuration changes must be documented.
- Configuration implementation must be covered by automated tests.

## 41. API Versioning Implementation

### 41.1 Purpose

This section defines the implementation of API versioning in SplitSync V1.

API versioning must allow the Backend and Android application to evolve without unexpectedly breaking existing clients.

### 41.2 Versioning Principle

API contracts must have an explicit version.

Conceptually:

```text
Android Client
      ↓
API Version
      ↓
Backend API
```

### 41.3 API Version

The initial API version is:

```text
v1
```

### 41.4 Version Scope

Versioning applies to:

```text
REST Endpoints
Request Models
Response Models
Synchronization Payloads
Error Models
```

where applicable.

### 41.5 URL Versioning

The API may expose the version through the URL.

Conceptually:

```text
/api/v1/...
```

The exact endpoint structure must follow the API Architecture.

### 41.6 Request Version

Every request must be interpreted according to the API version used by the client.

### 41.7 Response Version

Responses must conform to the contract of the requested API version.

### 41.8 DTO Versioning

API DTOs should remain separate from Domain entities.

```text
API DTO
   ↓
Mapper
   ↓
Domain Model
```

### 41.9 Domain Independence

API versioning must not require version-specific logic to be embedded throughout the Domain layer.

### 41.10 Backward Compatibility

Non-breaking API changes may be introduced within the same major API version.

Examples:

```text
Optional Response Field
Additional Optional Metadata
```

### 41.11 Breaking Changes

Breaking changes require a new API version.

Examples:

```text
Changed Required Field
Changed Meaning of Existing Field
Removed Field
Changed Response Structure
Changed Authentication Contract
```

### 41.12 Version Migration

When a new API version is introduced:

```text
v1
 ↓
v2
```

the existing v1 contract should remain available for the supported compatibility period.

### 41.13 Client Version

The Android application must explicitly target the supported API version.

### 41.14 Backend Version Support

The Backend must explicitly define which API versions it supports.

### 41.15 Unsupported Version

If a client requests an unsupported version:

```text
Unsupported Version
      ↓
Structured API Error
```

### 41.16 Version Negotiation

Where protocol negotiation is required, the supported version should be determined before exchanging version-specific payloads.

### 41.17 Sync Protocol Version

Synchronization protocol versioning must remain distinct from general REST API versioning where required.

```text
API Version
      ≠
Sync Protocol Version
```

### 41.18 P2P Protocol Version

P2P peers must negotiate a compatible synchronization protocol version during the handshake.

### 41.19 Payload Version

Synchronization payloads must be interpreted according to their defined protocol/schema version.

### 41.20 Unknown Version

Unknown or unsupported payload versions must be rejected safely.

### 41.21 API Deprecation

Deprecated API versions must have a defined deprecation period.

### 41.22 Deprecation Communication

Deprecated versions should provide appropriate documentation and migration guidance.

### 41.23 Version Documentation

Each API version must document:

```text
Endpoints
Request Models
Response Models
Error Models
Authentication
Authorization
Synchronization Contract
```

### 41.24 API Version Testing

Tests must verify:

```text
Supported Version
Unsupported Version
Backward Compatibility
Breaking Change
DTO Compatibility
Error Compatibility
```

### 41.25 API Version Completion Criteria

API Versioning Implementation is complete when:

```text
v1 API Contract Implemented
Version Routing Implemented
DTO Versioning Implemented
Unsupported Version Handling Implemented
Sync Protocol Versioning Integrated
P2P Version Negotiation Integrated
Deprecation Strategy Defined
Compatibility Tests Implemented
Documentation Completed
Tests Passing
```

### 41.26 API Versioning Invariants

The following rules are mandatory:

- API contracts must have explicit versions.
- V1 must remain stable once released.
- Breaking changes must require a new API version.
- API DTOs must remain separate from Domain entities.
- Unsupported API versions must fail safely.
- Sync protocol versioning must remain explicit.
- P2P peers must negotiate compatible protocol versions.
- Unknown synchronization payload versions must not be silently processed.
- API version behavior must be covered by automated tests.


## 42. Database Migration Implementation

### 42.1 Purpose

This section defines the implementation of database schema migrations for Android and Backend databases.

### 42.2 Migration Principle

Database schema changes must be explicit, versioned, deterministic, and testable.

```text
Schema V1
   ↓
Migration
   ↓
Schema V2
```

### 42.3 Android Database Version

The Android local database must maintain an explicit schema version.

### 42.4 Backend Database Version

The Backend database must use the migration mechanism selected by the Backend architecture.

### 42.5 Migration Ownership

Database migrations belong to the Data/Infrastructure layer.

### 42.6 Migration Types

Migrations may include:

```text
Create Table
Add Column
Remove Column
Rename Column
Add Index
Remove Index
Change Constraint
Data Transformation
```

### 42.7 Additive Migration

Prefer additive schema changes where possible.

```text
Existing Schema
      +
New Optional Field
```

### 42.8 Destructive Migration

Destructive changes require explicit migration logic and verification.

### 42.9 Data Preservation

Migrations must preserve existing financial data unless a documented data transformation explicitly requires otherwise.

### 42.10 Expense Data

Migrations must preserve:

```text
Expense
ExpenseSplits
```

consistency.

### 42.11 Settlement Data

Migrations must preserve Settlement records and their financial meaning.

### 42.12 Group Data

Migrations must preserve:

```text
Groups
Memberships
```

consistency.

### 42.13 SyncOperation Migration

SyncOperation schema changes must preserve pending operations.

```text
Pending Operation
      ↓
Migration
      ↓
Still Synchronizable
```

### 42.14 SyncState Migration

SyncState migration must preserve:

```text
Cursor
Status
Retry Metadata
Synchronization Context
```

where applicable.

### 42.15 Conflict Migration

Conflict records must remain resolvable after migration.

### 42.16 Migration Transaction

Schema/data migrations must use transactional mechanisms where supported.

### 42.17 Migration Failure

If a migration fails:

```text
Migration Failure
      ↓
Do Not Start With Invalid Schema
```

### 42.18 Migration Ordering

Migrations must execute in deterministic version order.

```text
V1 → V2 → V3
```

### 42.19 Migration Idempotency

Migration tooling must ensure a migration is not incorrectly applied multiple times.

### 42.20 Android Room Migration

Android Room migrations should explicitly define:

```text
From Version
To Version
Schema Changes
Data Changes
```

### 42.21 Backend Migration

Backend migrations should be stored and executed through the selected migration framework.

### 42.22 Schema Validation

The application must validate the resulting schema where supported.

### 42.23 Migration Testing

Every migration must have automated tests.

### 42.24 Migration Test Flow

```text
Old Schema
      ↓
Run Migration
      ↓
New Schema
      ↓
Verify Data
      ↓
Verify Constraints
```

### 42.25 Existing Data Testing

Tests must use representative existing data, especially:

```text
Expenses
Splits
Settlements
Groups
Memberships
SyncOperations
SyncState
Conflicts
```

### 42.26 Migration Failure Testing

Test:

```text
Invalid Schema
Migration Failure
Interrupted Migration
Restart
Recovery
```

### 42.27 Production Migration

Production migrations must be reviewed before deployment.

### 42.28 Backup

Where Backend operational architecture supports it, database backups must be available before risky migrations.

### 42.29 Rollback Strategy

Every migration must define whether rollback is:

```text
Supported
Not Supported
Handled Through Forward Migration
```

### 42.30 Migration and Application Version

Android application versions must be compatible with the local database schema they expect.

### 42.31 Migration and API Version

Database schema migration must not silently change the public API contract.

### 42.32 Migration Completion Criteria

Database Migration Implementation is complete when:

```text
Android Schema Versioning Implemented
Backend Migration Framework Implemented
Migration Scripts Implemented
Data Preservation Verified
SyncOperation Migration Verified
SyncState Migration Verified
Conflict Migration Verified
Migration Tests Implemented
Failure Handling Implemented
Production Migration Procedure Defined
Documentation Completed
Tests Passing
```

### 42.33 Database Migration Invariants

The following rules are mandatory:

- Every schema change must have an explicit migration.
- Migrations must execute in deterministic order.
- Existing financial data must be preserved.
- Expense and ExpenseSplits consistency must be preserved.
- Group and Membership consistency must be preserved.
- Pending SyncOperations must remain synchronizable after migration.
- SyncState cursor/state must not be lost.
- Conflict records must remain valid after migration.
- Migration failure must not leave the application operating against an invalid schema.
- Every migration must have automated tests.
- Destructive migrations require explicit review.


## 43. Android Background Processing

### 43.1 Purpose

This section defines the implementation of Android background processing required by SplitSync V1.

Background processing is primarily required for:

```text
Synchronization
Retry
Recovery
Periodic Work
```

### 43.2 Background Processing Principle

Background work must respect Android lifecycle and execution restrictions.

### 43.3 Background Worker

The synchronization implementation should use the Android background mechanism selected by the architecture.

Conceptually:

```text
Android Scheduler
      ↓
SyncWorker
      ↓
SyncEngine
```

### 43.4 Worker Responsibility

The Worker should coordinate execution but must not contain the complete synchronization algorithm.

```text
SyncWorker
      ↓
SyncEngine
```

### 43.5 Worker Input

The Worker may receive:

```text
Sync Context
Trigger Reason
Retry Metadata
```

where required.

### 43.6 Network Constraint

Synchronization work should require appropriate network availability when Backend/P2P transport requires it.

### 43.7 Offline Work

Local offline mutations must not depend on background execution.

```text
User Mutation
      ↓
Local Transaction
      ↓
Pending SyncOperation
```

### 43.8 Background Sync

Background synchronization processes pending operations.

```text
Pending Operations
      ↓
SyncWorker
      ↓
SyncEngine
```

### 43.9 Periodic Synchronization

Periodic synchronization may be scheduled according to Android platform constraints and the project's Sync Strategy.

### 43.10 Network Recovery

Network recovery may trigger synchronization scheduling.

### 43.11 Application Start

Application startup may schedule synchronization when appropriate.

### 43.12 User-Initiated Sync

A User may trigger a synchronization request.

The UI should request work rather than directly executing long-running synchronization.

### 43.13 Work Constraints

Background work may use constraints such as:

```text
Network Available
Battery Conditions
Device State
```

only where justified by the feature requirements.

### 43.14 Worker Idempotency

A Worker may be executed more than once.

Therefore:

```text
Worker Execution
      ↓
SyncEngine
      ↓
Idempotent Synchronization
```

must remain safe.

### 43.15 Duplicate Worker Execution

Multiple scheduled executions must not corrupt:

```text
SyncState
SyncOperation
Cursor
```

### 43.16 Synchronization Lock

The Sync Engine must coordinate concurrent synchronization attempts.

### 43.17 Worker Retry

Worker-level retry must integrate with the Sync Retry and Recovery strategy.

### 43.18 Worker Failure

Transient failures should request retry according to Android background execution rules.

Permanent failures should not cause endless Worker retries.

### 43.19 Worker Backoff

Worker retry backoff should use the platform's supported scheduling mechanism together with the application's retry policy.

### 43.20 Authentication

If authentication is required:

```text
Worker
 ↓
SyncEngine
 ↓
Authentication
```

must follow the Authentication implementation.

### 43.21 Authorization

Background synchronization must apply the same authorization rules as foreground synchronization.

### 43.22 Device Revocation

A revoked Device must not continue protected background synchronization.

### 43.23 Worker Cancellation

Worker cancellation must not corrupt local transactions.

### 43.24 Process Death

If Android terminates the process:

```text
Process Death
      ↓
Persisted State
      ↓
Future Worker
      ↓
Recovery
```

must remain safe.

### 43.25 Worker Crash Recovery

Stale SyncOperations and SyncState must be recovered according to the Retry and Recovery implementation.

### 43.26 Battery Efficiency

Background processing must avoid unnecessary:

```text
Network Requests
CPU Usage
Wakeups
Peer Discovery
```

### 43.27 P2P Background Processing

P2P background behavior must follow Android platform restrictions and the selected P2P architecture.

Continuous background discovery should not be assumed unless explicitly supported.

### 43.28 Background P2P

If background P2P synchronization is supported, it must use the approved platform mechanism and security model.

### 43.29 Foreground Requirements

If a synchronization task requires foreground execution under Android platform rules, the appropriate foreground execution mechanism must be used.

### 43.30 Worker Observability

Background work should provide operational status through:

```text
SyncState
Logs
Metrics
```

### 43.31 Worker Testing

Test:

```text
Worker Start
Worker Retry
Worker Cancellation
Process Death
Network Recovery
Duplicate Execution
Authentication Failure
Authorization Failure
```

### 43.32 Background Processing Completion Criteria

Android Background Processing is complete when:

```text
SyncWorker Implemented
Background Scheduling Implemented
Network Constraints Implemented
Retry Integrated
Backoff Integrated
Concurrency Control Integrated
Process Death Recovery Implemented
Authentication Integrated
Authorization Integrated
P2P Constraints Integrated
Battery Considerations Addressed
Worker Tests Passing
```

### 43.33 Background Processing Invariants

The following rules are mandatory:

- Background workers must delegate synchronization logic to SyncEngine.
- Background execution must respect Android lifecycle restrictions.
- Worker execution must be idempotent.
- Duplicate worker execution must not corrupt synchronization state.
- Pending operations must remain durable.
- Process death must not lose synchronization state.
- Worker retry must use controlled retry/backoff.
- Permanent failures must not cause endless retries.
- Background synchronization must use the same Authentication and Authorization rules.
- Revoked Devices must not perform protected background synchronization.
- Background processing must minimize unnecessary battery/network usage.
- Background processing must be covered by automated tests.


## 44. Testing Implementation

### 44.1 Purpose

This section defines the overall implementation of the testing strategy for SplitSync V1.

Testing must verify:

```text
Domain Correctness
Data Consistency
API Correctness
Offline Behavior
Synchronization
Security
P2P
Recovery
```

### 44.2 Testing Principle

Tests must exist at multiple levels.

```text
Unit
 ↓
Integration
 ↓
System
 ↓
UI
```

### 44.3 Test Pyramid

The project should prefer:

```text
Many Unit Tests
      ↓
Integration Tests
      ↓
Fewer End-to-End Tests
```

### 44.4 Test Modules

The project should separate tests logically.

Conceptually:

```text
Domain Tests
Data Tests
Application Tests
Backend Tests
Sync Tests
P2P Tests
UI Tests
```

### 44.5 Test Environment

Testing must use isolated environments.

```text
Test Database
Test Configuration
Test Backend
Test Credentials
```

### 44.6 Deterministic Tests

Tests should avoid unnecessary dependencies on:

```text
Real Network
Real Time
Random Values
External Services
```

### 44.7 Test Data

Test data should use controlled fixtures/builders.

### 44.8 Test Fixtures

Reusable fixtures should represent:

```text
User
Device
Group
Membership
Expense
ExpenseSplit
Settlement
SyncOperation
SyncState
Conflict
```

### 44.9 Test Builders

Test builders may simplify creation of valid Domain objects.

### 44.10 Test Isolation

Each test should start from a predictable state.

### 44.11 Test Cleanup

Test databases and test resources must be cleaned/reset appropriately.

### 44.12 Test Naming

Test names should clearly describe:

```text
Given
When
Then
```

or an equivalent project convention.

### 44.13 Assertion Principle

Tests should verify business outcomes rather than implementation details wherever possible.

### 44.14 Domain Testing

Domain tests must verify:

```text
Validation
Business Rules
Balance Calculation
Split Calculation
Settlement Rules
```

### 44.15 Repository Testing

Repository tests must verify:

```text
Read
Create
Update
Delete
Transactions
Mapping
```

### 44.16 Database Testing

Database tests must verify:

```text
Schema
Constraints
Indexes
Queries
Migrations
Transactions
```

### 44.17 Application Service Testing

Application tests must verify:

```text
Use Cases
Authorization
Domain Coordination
Transaction Boundaries
```

### 44.18 API Testing

API tests must verify:

```text
Request
Authentication
Authorization
Validation
Response
Errors
Versioning
```

### 44.19 Synchronization Testing

Synchronization tests must verify:

```text
Push
Pull
Sync State
Retry
Conflict
Idempotency
Cursor
```

### 44.20 Offline Testing

Offline tests must verify:

```text
Offline Read
Offline Mutation
Pending Operation
Restart
Network Recovery
```

### 44.21 P2P Testing

P2P tests must verify:

```text
Discovery
Handshake
Authentication
Authorization
Push
Pull
Conflict
Connection Loss
Recovery
```

### 44.22 Security Testing

Security tests must verify:

```text
Authentication
Authorization
Device Identity
Replay Protection
Peer Trust
Secret Handling
```

### 44.23 Error Testing

Every major error category must have tests.

```text
Validation
Network
Timeout
Authentication
Authorization
Database
Conflict
P2P
```

### 44.24 Transaction Testing

Transaction tests must verify:

```text
Commit
Rollback
Atomicity
Concurrency
```

### 44.25 Migration Testing

Every database migration must be tested from the previous supported schema version.

### 44.26 Background Testing

Background execution must be tested for:

```text
Scheduling
Retry
Cancellation
Process Death
Network Recovery
```

### 44.27 Test Doubles

Where appropriate, use:

```text
Fake
Stub
Mock
Test Database
Test Transport
```

without over-mocking Domain behavior.

### 44.28 Fake Sync Transport

Synchronization tests should be able to use an in-memory/fake transport.

```text
SyncEngine
      ↓
FakeTransport
```

### 44.29 Fake P2P Transport

P2P synchronization tests should be able to simulate peers without requiring physical Devices for every test.

### 44.30 Fake Network

Network failure scenarios should be deterministic.

### 44.31 Time Control

Retry/backoff tests should use controllable test time rather than waiting for real delays.

### 44.32 Randomness Control

Identifiers and random security test values should be controllable where deterministic testing requires it.

### 44.33 Integration Environment

Integration tests may use:

```text
Real Database
Real Backend
Test Transport
```

within an isolated environment.

### 44.34 End-to-End Testing

Critical workflows should be tested end-to-end.

Examples:

```text
Create Expense
Offline Sync
Backend Sync
P2P Sync
Conflict Resolution
Settlement
```

### 44.35 Test Execution

The project should define separate test commands/tasks for:

```text
Unit
Integration
Android Instrumentation
Backend
P2P
End-to-End
```

### 44.36 Continuous Integration

CI should execute the required automated test suites before merging/releasing.

### 44.37 Test Failure Policy

A required test failure must block the relevant build/release stage.

### 44.38 Test Coverage

Coverage should be used as a quality indicator rather than the sole measure of correctness.

### 44.39 Critical Coverage

Higher confidence is required for:

```text
Financial Calculations
Transactions
Synchronization
Conflict Handling
Authentication
Authorization
Security
```

### 44.40 Regression Testing

Every defect fixed in the implementation should receive an appropriate regression test.

### 44.41 Test Documentation

Complex synchronization and conflict tests should document the scenario and expected outcome.

### 44.42 Test Completion Criteria

Testing Implementation is complete when:

```text
Test Structure Implemented
Test Fixtures Implemented
Unit Tests Implemented
Integration Tests Implemented
Database Tests Implemented
API Tests Implemented
Offline Tests Implemented
Sync Tests Implemented
P2P Tests Implemented
Security Tests Implemented
Migration Tests Implemented
Background Tests Implemented
CI Integration Implemented
Regression Strategy Implemented
```

### 44.43 Testing Invariants

The following rules are mandatory:

- Tests must cover critical Domain rules.
- Financial calculations must have deterministic automated tests.
- Transaction boundaries must be tested.
- Offline mutations must be tested.
- SyncOperation idempotency must be tested.
- Push and Pull must be tested independently and together.
- Cursor safety must be tested.
- Retry and recovery must be tested.
- Conflict detection and resolution must be tested.
- Authentication and Authorization must be tested.
- P2P handshake and synchronization must be tested.
- Database migrations must be tested.
- Background process recovery must be tested.
- Regression tests must be added for fixed defects.
- Required CI tests must pass before release.


## 45. Unit Testing

### 45.1 Purpose

This section defines the implementation of Unit Testing for SplitSync V1.

Unit tests verify isolated behavior of:

```text
Domain
Application Services
Repositories
Synchronization Components
Utilities
Validators
Calculators
```

### 45.2 Unit Testing Principle

Unit tests should be:

```text
Fast
Deterministic
Isolated
Repeatable
```

### 45.3 Domain Unit Tests

Domain unit tests must not require:

```text
Android Framework
Database
Network
Backend
P2P Transport
```

unless a specific unit under test requires an abstraction.

### 45.4 Value Object Tests

Test:

```text
Valid Construction
Invalid Construction
Equality
Normalization
```

for applicable Value Objects.

### 45.5 Expense Tests

Test:

```text
Valid Expense
Invalid Amount
Invalid Currency
Invalid Group
Invalid Creator
```

### 45.6 Expense Split Tests

Test:

```text
Valid Split
Invalid Split
Total Split Validation
Participant Validation
Rounding Rules
```

### 45.7 Settlement Tests

Test:

```text
Valid Settlement
Invalid Amount
Invalid Payer
Invalid Receiver
Invalid Group
```

### 45.8 Balance Calculator Tests

Test:

```text
No Expenses
Single Expense
Multiple Expenses
Settlements
Partial Settlement
Fully Settled
Multiple Participants
```

### 45.9 Balance Invariants

Verify:

```text
Total Debits
=
Total Credits
```

according to the defined balance model.

### 45.10 Group Tests

Test:

```text
Group Creation
Group Validation
Group State
```

### 45.11 Membership Tests

Test:

```text
Add Member
Remove Member
Role Validation
Membership State
Authorization Rules
```

### 45.12 Domain Validation Tests

Every Domain invariant should have at least one positive and negative test where applicable.

### 45.13 Application Service Tests

Application services should be tested with controlled dependencies.

```text
Application Service
      ↓
Fake Repository
Fake Clock
Fake Sync Repository
```

### 45.14 Expense Use Case Tests

Test:

```text
Create Expense
Update Expense
Delete Expense
Validation Failure
Authorization Failure
```

### 45.15 Settlement Use Case Tests

Test:

```text
Create Settlement
Validation Failure
Authorization Failure
```

### 45.16 Group Use Case Tests

Test:

```text
Create Group
Update Group
Membership Change
Authorization
```

### 45.17 Repository Unit Tests

Where repositories contain non-trivial mapping or logic, test:

```text
Entity Mapping
DTO Mapping
Error Mapping
Query Construction
```

### 45.18 SyncOperation Tests

Test:

```text
Operation Creation
Operation ID
Payload
State
Version
Entity Reference
```

### 45.19 SyncState Tests

Test:

```text
State Transition
Cursor
Retry Count
Failure State
Recovery
```

### 45.20 Conflict Detector Tests

Test:

```text
No Conflict
Version Conflict
Create Conflict
Update Conflict
Delete Conflict
Duplicate Operation
```

### 45.21 Conflict Resolver Tests

Test:

```text
Valid Resolution
Invalid Resolution
Resolution Validation
Resolution State
Idempotency
```

### 45.22 Retry Tests

Test:

```text
Retryable Failure
Permanent Failure
Retry Limit
Backoff Calculation
Timeout Recovery
```

### 45.23 Push Processor Tests

Test:

```text
Pending Selection
Ordering
Batching
Serialization
Response Handling
Partial Success
```

### 45.24 Pull Processor Tests

Test:

```text
Cursor
Batch Processing
Duplicate Detection
Remote Apply
Cursor Advancement
```

### 45.25 Sync Engine Tests

Test:

```text
Push
Pull
Push + Pull
Retry
Conflict
State Update
Recovery
```

### 45.26 P2P Tests

Unit tests should cover:

```text
Peer Selection
Protocol Negotiation
Capability Negotiation
Handshake Decisions
Peer State
```

### 45.27 Discovery Tests

Test:

```text
Peer Found
Duplicate Peer
No Peer
Timeout
Filtering
Revoked Peer
```

### 45.28 Authentication Tests

Test:

```text
Valid Credentials
Invalid Credentials
Expired Authentication
Refresh
Authentication Failure
```

### 45.29 Authorization Tests

Test:

```text
Authorized User
Unauthorized User
Group Access
Membership Change
Device Revocation
```

### 45.30 Error Mapping Tests

Test mapping between:

```text
Infrastructure Error
Application Error
Presentation Error
```

### 45.31 Configuration Tests

Test:

```text
Valid Configuration
Missing Configuration
Invalid Configuration
Default Configuration
Environment Configuration
```

### 45.32 Logging Tests

Test:

```text
Log Level
Correlation ID
Sensitive Data Redaction
Exception Context
```

### 45.33 Unit Test Isolation

Unit tests must not depend on:

```text
Real Backend
Real Database
Real P2P Device
Real Network
```

### 45.34 Test Doubles

Use appropriate:

```text
Fake
Stub
Mock
Spy
```

based on the behavior being tested.

### 45.35 Mocking Principle

Do not mock Domain objects unnecessarily.

Prefer real Domain objects for Domain unit tests.

### 45.36 Test Data Builders

Use reusable builders for complex test objects.

### 45.37 Assertion Quality

Assertions must verify meaningful outcomes.

Avoid tests that only verify:

```text
Method Was Called
```

without verifying the resulting business behavior.

### 45.38 Exception Tests

Tests should verify:

```text
Exception Type
Error Code
Domain State
Persistence State
```

where applicable.

### 45.39 Parameterized Tests

Parameterized tests should be used for repeated rule variations.

Example:

```text
Amount
Split Count
Participant Count
Rounding
```

### 45.40 Property-Based Testing

Property-based testing may be used for calculations and invariants where useful.

### 45.41 Unit Test Naming

Unit tests should use a consistent naming convention.

Example:

```text
createExpense_whenAmountIsZero_shouldReject
```

### 45.42 Unit Test Organization

Tests should mirror the production package structure where practical.

```text
src/test/...
```

### 45.43 Fast Execution

Unit tests should remain fast enough to run frequently during development.

### 45.44 Unit Test Coverage

Critical units should have strong behavioral coverage.

Especially:

```text
BalanceCalculator
SplitCalculator
Validators
ConflictDetector
ConflictResolver
SyncEngine
RetryPolicy
```

### 45.45 Regression Unit Tests

Every discovered logic defect should result in a regression unit test where applicable.

### 45.46 Unit Testing Completion Criteria

Unit Testing is complete when:

```text
Domain Unit Tests Implemented
Value Object Tests Implemented
Validation Tests Implemented
Calculation Tests Implemented
Application Service Tests Implemented
SyncOperation Tests Implemented
SyncState Tests Implemented
Conflict Tests Implemented
Retry Tests Implemented
Push Tests Implemented
Pull Tests Implemented
P2P Tests Implemented
Authentication Tests Implemented
Authorization Tests Implemented
Configuration Tests Implemented
Logging Tests Implemented
Regression Tests Implemented
CI Execution Configured
```

### 45.47 Unit Testing Invariants

The following rules are mandatory:

- Unit tests must be deterministic.
- Domain unit tests must remain independent of infrastructure.
- Financial calculations must have comprehensive unit coverage.
- Domain validation must be tested for valid and invalid cases.
- SyncOperation creation must be tested.
- SyncState transitions must be tested.
- Conflict detection must be tested.
- Conflict resolution must be tested.
- Retry policies must be tested.
- Push and Pull processors must be tested.
- Authentication and Authorization logic must be tested.
- P2P handshake decisions must be tested.
- Sensitive-data logging behavior must be tested.
- Regression tests must be added for fixed logic defects.
- Unit tests must remain fast and suitable for frequent execution.

## 46. Integration Testing

### 46.1 Purpose

This section defines the implementation of Integration Testing for SplitSync V1.

Integration tests verify that multiple application components work correctly together.

```text
Component A
     +
Component B
     +
Infrastructure
     ↓
Integration Test
```

### 46.2 Integration Testing Principle

Integration tests should verify actual interaction between components while keeping external dependencies controlled where appropriate.

### 46.3 Integration Test Scope

Integration testing should cover:

```text
Domain ↔ Application
Application ↔ Repository
Repository ↔ Database
Application ↔ API
Sync Engine ↔ Repository
Sync Engine ↔ Transport
P2P ↔ Sync Engine
```

### 46.4 Android Integration Tests

Android integration tests should verify:

```text
Room
Repository
Application Services
ViewModel
Sync Components
```

where applicable.

### 46.5 Database Integration Tests

Database integration tests must verify:

```text
Entity Persistence
Queries
Relationships
Constraints
Transactions
Migrations
```

### 46.6 Room Integration Tests

Room tests should use a test database rather than the production database.

### 46.7 Repository Integration Tests

Repository tests must verify:

```text
Domain Model
      ↓
Repository
      ↓
Room
      ↓
Database
```

### 46.8 Transaction Integration Tests

Test complete transactions such as:

```text
Create Expense
      ↓
Expense
+
ExpenseSplits
+
SyncOperation
```

### 46.9 Backend Integration Tests

Backend integration tests should verify:

```text
Controller
 ↓
Application Service
 ↓
Repository
 ↓
Database
```

### 46.10 API Integration Tests

API integration tests must verify:

```text
Request
 ↓
Authentication
 ↓
Authorization
 ↓
Validation
 ↓
Application Service
 ↓
Response
```

### 46.11 Sync Integration Tests

Test:

```text
Local Database
      ↕
Sync Engine
      ↕
Sync Transport
```

### 46.12 Push Integration Tests

Test:

```text
Pending SyncOperation
      ↓
Push Processor
      ↓
Transport
      ↓
Remote Response
      ↓
Local State Update
```

### 46.13 Pull Integration Tests

Test:

```text
Remote Changes
      ↓
Pull Processor
      ↓
Conflict Detection
      ↓
Repository
      ↓
Local Database
```

### 46.14 SyncState Integration

Verify that:

```text
SyncOperation
+
SyncState
+
Database
```

remain consistent.

### 46.15 Conflict Integration

Verify:

```text
Remote Operation
      ↓
Conflict Detector
      ↓
Conflict Repository
      ↓
Conflict State
```

### 46.16 Authentication Integration

Test:

```text
Authentication
      ↓
Token / Session
      ↓
Authenticated API Request
```

### 46.17 Authorization Integration

Test:

```text
User
+
Membership
+
Resource
      ↓
Authorization Decision
```

### 46.18 P2P Integration

Test:

```text
Peer Connection
      ↓
Handshake
      ↓
P2P Sync
      ↓
Local Database
```

### 46.19 Error Integration

Test failures across component boundaries:

```text
Database Failure
Network Failure
Authentication Failure
Authorization Failure
Transport Failure
```

### 46.20 Integration Test Isolation

Each integration test should use isolated state where practical.

### 46.21 Test Database

Test databases must not contain production data.

### 46.22 Test Backend

Backend integration tests should use an isolated test environment.

### 46.23 Test Transport

Synchronization integration tests may use:

```text
Fake Transport
Test Transport
In-Memory Transport
```

where appropriate.

### 46.24 Integration Test Data

Test data should represent realistic Domain scenarios.

### 46.25 Integration Test Cleanup

Test resources must be cleaned after each test or test suite according to the isolation strategy.

### 46.26 Integration Test Completion Criteria

Integration Testing is complete when:

```text
Database Integration Tests Implemented
Repository Integration Tests Implemented
Application Integration Tests Implemented
API Integration Tests Implemented
Sync Integration Tests Implemented
Conflict Integration Tests Implemented
Authentication Integration Tests Implemented
Authorization Integration Tests Implemented
P2P Integration Tests Implemented
Failure Integration Tests Implemented
CI Execution Configured
Tests Passing
```

### 46.27 Integration Testing Invariants

The following rules are mandatory:

- Integration tests must verify real component boundaries.
- Test environments must remain isolated from production.
- Database integration tests must verify transactions and constraints.
- Repository integration tests must verify persistence behavior.
- Synchronization integration tests must verify Push and Pull behavior.
- SyncState and SyncOperation consistency must be tested.
- Conflict persistence must be tested.
- Authentication and Authorization boundaries must be tested.
- P2P component integration must be tested.
- Integration tests must cover important failure paths.


## 47. Synchronization Testing

### 47.1 Purpose

This section defines the implementation of Synchronization Testing for SplitSync V1.

Synchronization testing verifies that local and remote state remains correct across:

```text
Online
Offline
Backend Sync
P2P Sync
Retry
Conflict
Recovery
```

### 47.2 Synchronization Testing Principle

Synchronization tests must validate both:

```text
Correctness
+
Convergence
```

### 47.3 Sync Test Components

Tests should cover:

```text
SyncOperation
SyncState
Push
Pull
Sync Engine
Cursor
Retry
Conflict
```

### 47.4 Local Mutation Test

Test:

```text
Create Expense
      ↓
Local Database
      ↓
SyncOperation = PENDING
```

### 47.5 Push Test

Test:

```text
PENDING
 ↓
Push
 ↓
Remote Accepted
 ↓
SYNCED
```

### 47.6 Pull Test

Test:

```text
Remote Change
 ↓
Pull
 ↓
Local Database
```

### 47.7 Push and Pull Test

Test:

```text
Local Changes
+
Remote Changes
      ↓
Sync
      ↓
Consistent Local State
```

### 47.8 Cursor Test

Verify:

```text
Cursor N
 ↓
Process Changes
 ↓
Cursor M
```

and ensure the cursor advances only after successful persistence.

### 47.9 Cursor Failure Test

Test:

```text
Apply Changes
 ↓
Persistence Failure
 ↓
Cursor Unchanged
```

### 47.10 Duplicate Operation Test

Send the same Operation ID multiple times.

Expected:

```text
One Business Effect
```

### 47.11 Retry Test

Test:

```text
Network Failure
 ↓
Retry
 ↓
Success
```

### 47.12 Retry Limit Test

Verify that permanent failures do not retry indefinitely.

### 47.13 Timeout Test

Test:

```text
Request
 ↓
Timeout
 ↓
Retry Same Operation ID
```

### 47.14 Crash Recovery Test

Test:

```text
Sync Running
 ↓
Process Terminated
 ↓
Restart
 ↓
Recover
 ↓
Continue Sync
```

### 47.15 Offline Mutation Test

Test:

```text
Offline
 ↓
Create Expense
 ↓
Local Commit
 ↓
Pending SyncOperation
```

### 47.16 Network Recovery Test

Test:

```text
Offline
 ↓
Pending Operations
 ↓
Network Available
 ↓
Synchronization
```

### 47.17 Multiple Device Test

Test:

```text
Device A
   ↕
Backend
   ↕
Device B
```

and verify state propagation.

### 47.18 Concurrent Mutation Test

Test:

```text
Device A → Update
Device B → Update
       ↓
Synchronization
       ↓
Conflict / Convergence
```

### 47.19 Sync Conflict Test

Verify that incompatible changes enter Conflict handling rather than silent overwrite.

### 47.20 Sync Ordering Test

Verify that dependent operations are processed in valid order.

### 47.21 Batch Test

Test:

```text
Small Batch
Large Batch
Multiple Batches
Empty Batch
```

### 47.22 Partial Batch Test

Verify safe recovery when only part of a batch can be processed.

### 47.23 Tombstone Test

Test:

```text
Delete
 ↓
Tombstone
 ↓
Synchronization
 ↓
Remote Delete
```

### 47.24 Membership Sync Test

Test:

```text
Membership Change
 ↓
Sync
 ↓
Local Authorization State
```

### 47.25 Expense Sync Test

Test:

```text
Expense
+
ExpenseSplits
 ↓
Sync
 ↓
Consistent Remote State
```

### 47.26 Settlement Sync Test

Test:

```text
Settlement
 ↓
Sync
 ↓
Correct Balance
```

### 47.27 Balance Convergence Test

After valid synchronization:

```text
Device A Balance
=
Device B Balance
```

for the same converged Domain state.

### 47.28 Backend and P2P Convergence Test

Test:

```text
Device A
   ↕ P2P
Device B
   ↕ Backend
Backend
```

and verify eventual convergence according to the synchronization model.

### 47.29 Sync Authentication Test

Verify that unauthenticated synchronization is rejected.

### 47.30 Sync Authorization Test

Verify that unauthorized synchronization data cannot be retrieved or applied.

### 47.31 Device Revocation Test

Test:

```text
Active Device
 ↓
Revoked
 ↓
Sync Attempt
 ↓
Rejected
```

### 47.32 Sync Performance Test

Test:

```text
Small Dataset
Medium Dataset
Large Dataset
Large Pending Queue
```

### 47.33 Sync Recovery Test

Verify recovery after:

```text
Network Loss
Process Death
Worker Cancellation
Database Failure
Peer Disconnect
```

### 47.34 Sync Property Tests

Where useful, verify properties such as:

```text
Duplicate Delivery → Same Final Effect
Retry → Same Final Effect
Successful Convergence → Same Domain State
```

### 47.35 Synchronization Completion Criteria

Synchronization Testing is complete when:

```text
Offline Tests Passing
Push Tests Passing
Pull Tests Passing
Cursor Tests Passing
Retry Tests Passing
Recovery Tests Passing
Conflict Tests Passing
Multi-Device Tests Passing
Financial Convergence Tests Passing
Authentication Tests Passing
Authorization Tests Passing
P2P Reconciliation Tests Passing
Performance Tests Passing
```

### 47.36 Synchronization Testing Invariants

The following rules are mandatory:

- Synchronization must be tested in both online and offline conditions.
- Duplicate operations must be tested.
- Cursor safety must be tested.
- Retry behavior must be tested.
- Crash recovery must be tested.
- Conflict behavior must be tested.
- Financial state convergence must be tested.
- Membership synchronization must be tested.
- Backend and P2P synchronization must be tested together where applicable.
- Authentication and Authorization must be tested.
- Permanent failures must not produce infinite retries.
- Synchronization tests must verify actual resulting state rather than only method calls.


## 48. Conflict Testing

### 48.1 Purpose

This section defines the implementation of Conflict Testing for SplitSync V1.

Conflict testing verifies that concurrent or incompatible changes are detected, persisted, and resolved correctly.

### 48.2 Conflict Testing Principle

Conflict tests must prove:

```text
No Silent Overwrite
+
Deterministic Detection
+
Safe Resolution
```

### 48.3 Conflict Test Categories

Test:

```text
Create Conflict
Update Conflict
Delete Conflict
Version Conflict
Membership Conflict
Expense Conflict
Settlement Conflict
```

### 48.4 Version Conflict Test

Test:

```text
Local Version = 5
Remote Base Version = 4
      ↓
Conflict
```

### 48.5 Concurrent Update Test

Test:

```text
Device A → V5
Device B → V5
      ↓
Both Modify
      ↓
Conflict Detection
```

### 48.6 Create Conflict Test

Test two operations attempting to create the same logical entity.

### 48.7 Update Conflict Test

Test updates based on stale versions.

### 48.8 Delete Conflict Test

Test:

```text
Device A → Update
Device B → Delete
      ↓
Synchronization
```

and verify the defined conflict behavior.

### 48.9 Tombstone Conflict Test

Test mutation against an entity already deleted/tombstoned.

### 48.10 Expense Conflict Test

Test conflicting Expense updates.

Verify:

```text
Amount
Payer
Participants
ExpenseSplits
```

remain correct.

### 48.11 Settlement Conflict Test

Test conflicting Settlement operations.

Verify:

```text
Payer
Receiver
Amount
Balance
```

remain correct.

### 48.12 Membership Conflict Test

Test simultaneous membership changes.

Verify:

```text
Membership
Role
Authorization
```

remain consistent.

### 48.13 Group Conflict Test

Test concurrent Group modifications.

### 48.14 Duplicate Operation Test

A duplicate operation must not be treated as a new conflict unnecessarily.

### 48.15 Invalid Operation Test

Verify that Domain-invalid operations are classified as validation failures rather than incorrectly classified as conflicts.

### 48.16 Authorization Test

Verify that unauthorized operations are rejected as authorization failures rather than conflicts.

### 48.17 Conflict Persistence Test

Verify that detected conflicts are persisted durably.

### 48.18 Conflict State Test

Verify:

```text
OPEN
 ↓
RESOLVING
 ↓
RESOLVED
```

according to the supported lifecycle.

### 48.19 Conflict Resolution Test

Test a valid resolution and verify the resulting Domain state.

### 48.20 Invalid Resolution Test

Test:

```text
Conflict
 ↓
Invalid Resolution
 ↓
Conflict Remains Open
```

### 48.21 Financial Resolution Test

Verify that resolving a financial conflict preserves:

```text
Expense Amount
ExpenseSplits
Settlement Amount
Balance
```

### 48.22 Manual Resolution Test

Where manual resolution is supported:

```text
Conflict
 ↓
User Decision
 ↓
Resolution
 ↓
New SyncOperation
```

### 48.23 Resolution Authorization Test

Verify that only authorized Users can resolve conflicts.

### 48.24 Stale Resolution Test

Test:

```text
Conflict Open
 ↓
Remote State Changes
 ↓
User Attempts Resolution
 ↓
Revalidation
 ↓
New Conflict / Rejection
```

### 48.25 Resolution Idempotency Test

Apply the same resolution operation repeatedly.

Expected:

```text
One Final Business Effect
```

### 48.26 Conflict Recovery Test

Test application termination while a conflict is:

```text
OPEN
RESOLVING
```

### 48.27 Conflict Convergence Test

After successful resolution:

```text
Device A
   ↕
Device B
   ↕
Backend
```

must converge to the same valid state according to the synchronization protocol.

### 48.28 Conflict Logging Test

Verify that diagnostic logs contain sufficient conflict context without exposing sensitive payloads.

### 48.29 Conflict Security Test

Test:

```text
Unauthorized Resolution
Tampered Conflict Data
Invalid Operation
Revoked Device
```

### 48.30 Conflict Completion Criteria

Conflict Testing is complete when:

```text
Version Conflict Tests Passing
Create Conflict Tests Passing
Update Conflict Tests Passing
Delete Conflict Tests Passing
Expense Conflict Tests Passing
Settlement Conflict Tests Passing
Membership Conflict Tests Passing
Conflict Persistence Tests Passing
Resolution Tests Passing
Invalid Resolution Tests Passing
Authorization Tests Passing
Recovery Tests Passing
Idempotency Tests Passing
Convergence Tests Passing
Security Tests Passing
```

### 48.31 Conflict Testing Invariants

The following rules are mandatory:

- Conflicting updates must be detected.
- Newer state must not be silently overwritten.
- Duplicate operations must not create duplicate conflicts.
- Validation failures must remain distinct from conflicts.
- Authorization failures must remain distinct from conflicts.
- Financial conflicts must preserve financial correctness.
- Conflicts must be persisted.
- Resolution must be validated.
- Resolution must be authorized.
- Resolution must be idempotent.
- Stale resolutions must be revalidated.
- Conflict testing must verify final Domain state.
- Resolved conflicts must converge across synchronization participants.


## 49. P2P Testing

### 49.1 Purpose

This section defines the implementation of P2P Testing for SplitSync V1.

P2P tests verify:

```text
Discovery
Connection
Handshake
Authentication
Authorization
Synchronization
Recovery
Security
```

### 49.2 P2P Testing Principle

P2P testing must validate both:

```text
Transport Correctness
+
Synchronization Correctness
```

### 49.3 P2P Test Levels

Tests should exist at:

```text
Unit
Integration
Device
End-to-End
```

where applicable.

### 49.4 Discovery Test

Test:

```text
Device A Advertising
      ↓
Device B Discovery
      ↓
Peer Candidate
```

### 49.5 Discovery Timeout Test

Verify that discovery stops after the configured timeout.

### 49.6 Duplicate Discovery Test

Verify that repeated discovery events for the same peer do not create duplicate sessions.

### 49.7 Unknown Peer Test

Verify that an unknown peer cannot synchronize protected data without successful trust/authentication.

### 49.8 Peer Authentication Test

Test:

```text
Valid Peer
 ↓
Authentication Success
```

and:

```text
Invalid Peer
 ↓
Authentication Failure
```

### 49.9 Mutual Authentication Test

Verify both sides authenticate each other where required.

### 49.10 Authorization Test

Verify:

```text
Authenticated Peer
      ↓
Authorized Group
      ↓
Synchronization Allowed
```

and unauthorized contexts are rejected.

### 49.11 Revoked Peer Test

Test:

```text
Trusted Peer
 ↓
Revoked
 ↓
Reconnect
 ↓
Rejected
```

### 49.12 Handshake Test

Verify:

```text
Connection
 ↓
Handshake
 ↓
Protocol Negotiation
 ↓
Authentication
 ↓
Authorization
 ↓
Secure Session
```

### 49.13 Protocol Mismatch Test

Test:

```text
Peer A = V1
Peer B = Unsupported Version
      ↓
Handshake Rejected
```

### 49.14 Capability Negotiation Test

Verify that only mutually supported capabilities are enabled.

### 49.15 Secure Session Test

Verify that synchronization cannot begin before the secure session is established.

### 49.16 P2P Push Test

Test:

```text
Device A
 ↓
Pending Operation
 ↓
P2P Push
 ↓
Device B
```

### 49.17 P2P Pull Test

Test:

```text
Device B
 ↓
Remote Operation
 ↓
P2P Pull
 ↓
Device A
```

### 49.18 Duplicate Operation Test

Send the same Operation ID multiple times.

Expected:

```text
One Business Effect
```

### 49.19 P2P Conflict Test

Test concurrent changes:

```text
Device A → Change
Device B → Change
      ↓
P2P Sync
      ↓
Conflict
```

### 49.20 P2P Conflict Resolution Test

Verify that the common Conflict Resolution mechanism works through P2P.

### 49.21 Connection Loss Test

Test:

```text
Connected
 ↓
Syncing
 ↓
Connection Lost
```

and verify local state remains safe.

### 49.22 Reconnection Test

Test:

```text
Connection Lost
 ↓
Reconnect
 ↓
Handshake
 ↓
Resume Sync
```

### 49.23 Partial Transfer Test

Terminate the connection during an operation/batch transfer.

Verify:

```text
Partial Data
      ↓
Not Applied
```

### 49.24 P2P Retry Test

Test temporary connection failures and controlled retry.

### 49.25 P2P Backoff Test

Verify increasing retry delays.

### 49.26 P2P Cursor Test

If P2P cursors are used:

```text
Cursor N
 ↓
Pull
 ↓
Persist
 ↓
Cursor M
```

must be verified.

### 49.27 P2P SyncState Test

Verify peer-specific SyncState persistence and recovery.

### 49.28 P2P Financial Test

Synchronize:

```text
Expense
ExpenseSplits
Settlement
```

and verify financial correctness.

### 49.29 P2P Membership Test

Synchronize membership changes and verify authorization state.

### 49.30 P2P Backend Reconciliation Test

Test:

```text
Device A
   ↕ P2P
Device B
   ↓
Backend
```

and verify that P2P-created changes reconcile correctly with Backend synchronization.

### 49.31 P2P Offline Test

Test P2P synchronization while Backend connectivity is unavailable.

### 49.32 P2P Security Test

Test:

```text
Spoofed Peer
Replay
Revoked Peer
Unauthorized Group
Invalid Identity
Invalid Session
```

### 49.33 P2P Battery/Resource Test

Where practical, verify that discovery and synchronization do not perform unnecessary continuous work.

### 49.34 P2P Device Test

Physical-device tests should verify the selected Android P2P transport and platform behavior.

### 49.35 P2P Simulation Test

A simulated/fake transport should be used for most automated synchronization tests.

### 49.36 P2P Completion Criteria

P2P Testing is complete when:

```text
Discovery Tests Passing
Connection Tests Passing
Handshake Tests Passing
Authentication Tests Passing
Authorization Tests Passing
Protocol Negotiation Tests Passing
Secure Session Tests Passing
Push Tests Passing
Pull Tests Passing
Conflict Tests Passing
Retry Tests Passing
Recovery Tests Passing
Security Tests Passing
Offline Tests Passing
Backend Reconciliation Tests Passing
Physical Device Tests Passing Where Required
```

### 49.37 P2P Testing Invariants

The following rules are mandatory:

- Discovery must be tested separately from trust.
- Unknown peers must not synchronize protected data.
- Authentication must be tested.
- Authorization must be tested.
- Revoked peers must be rejected.
- Protocol mismatch must prevent synchronization.
- Secure session establishment must be tested.
- Duplicate operations must remain idempotent.
- Partial transfers must not create partial Domain state.
- Connection loss must preserve local synchronization state.
- Reconnection must perform the required handshake.
- P2P conflicts must use the common conflict model.
- P2P financial synchronization must preserve balances.
- P2P changes must reconcile with Backend synchronization.
- P2P security behavior must be tested on appropriate physical devices.


## 50. End-to-End Testing

### 50.1 Purpose

This section defines the implementation of End-to-End Testing for SplitSync V1.

End-to-End tests validate complete user/business workflows across the major system boundaries.

```text
Android
   ↓
Application
   ↓
Database
   ↓
Backend / P2P
   ↓
Remote State
```

### 50.2 E2E Testing Principle

E2E tests must validate actual business outcomes rather than individual method behavior.

### 50.3 E2E Environment

E2E testing should use an isolated environment containing:

```text
Android Application
Test Backend
Test Database
Test User Accounts
Test Devices
```

### 50.4 E2E User Scenario

A complete workflow may be:

```text
Login
 ↓
Create Group
 ↓
Add Members
 ↓
Create Expense
 ↓
Split Expense
 ↓
Synchronize
 ↓
View Balances
 ↓
Create Settlement
 ↓
Synchronize
 ↓
Verify Final Balance
```

### 50.5 Group Creation E2E

Test:

```text
User Login
 ↓
Create Group
 ↓
Group Persisted
 ↓
Group Visible
```

### 50.6 Membership E2E

Test:

```text
Create Group
 ↓
Add Member
 ↓
Member Sync
 ↓
Remote Member State
```

### 50.7 Expense E2E

Test:

```text
Create Expense
 ↓
Create Splits
 ↓
Persist Locally
 ↓
Synchronize
 ↓
Remote Expense
```

### 50.8 Settlement E2E

Test:

```text
Create Settlement
 ↓
Synchronize
 ↓
Remote Settlement
 ↓
Updated Balance
```

### 50.9 Offline Expense E2E

Test:

```text
Network Unavailable
 ↓
Create Expense
 ↓
Local Success
 ↓
Pending Sync
 ↓
Network Restored
 ↓
Synchronization
 ↓
Remote Expense
```

### 50.10 Offline Settlement E2E

Test:

```text
Offline
 ↓
Create Settlement
 ↓
Pending Sync
 ↓
Reconnect
 ↓
Sync
 ↓
Remote Settlement
```

### 50.11 Multi-Device E2E

Test:

```text
Device A
 ↓
Create Expense
 ↓
Backend Sync
 ↓
Device B Pull
 ↓
Expense Visible
```

### 50.12 P2P E2E

Test:

```text
Device A
 ↓
Create Expense
 ↓
Backend Unavailable
 ↓
P2P Discovery
 ↓
Handshake
 ↓
P2P Sync
 ↓
Device B
```

### 50.13 P2P Offline E2E

Test:

```text
Backend Offline
+
Device A Offline
+
Device B Available
      ↓
P2P Synchronization
      ↓
Converged Local State
```

### 50.14 Backend Reconciliation E2E

Test:

```text
P2P Sync
 ↓
Backend Returns
 ↓
Backend Push
 ↓
Backend Pull
 ↓
Reconciliation
```

### 50.15 Conflict E2E

Test:

```text
Device A → Update Expense
Device B → Update Same Expense
        ↓
Synchronization
        ↓
Conflict
        ↓
Resolution
        ↓
Converged State
```

### 50.16 Financial Conflict E2E

Verify that a conflict involving:

```text
Expense Amount
Expense Splits
Settlement
```

does not corrupt the final balance.

### 50.17 Authorization E2E

Test:

```text
Unauthorized User
 ↓
Protected Group
 ↓
Operation Rejected
```

### 50.18 Device Revocation E2E

Test:

```text
Device Active
 ↓
Device Revoked
 ↓
Sync Attempt
 ↓
Rejected
```

### 50.19 Authentication Expiration E2E

Test:

```text
Authenticated User
 ↓
Session Expires
 ↓
Sync
 ↓
Re-authentication
 ↓
Sync Continues
```

### 50.20 Application Restart E2E

Test:

```text
Offline Mutation
 ↓
Application Terminated
 ↓
Application Restart
 ↓
Pending Operation Recovered
 ↓
Synchronization
```

### 50.21 Device Restart E2E

Test:

```text
Pending Sync
 ↓
Device Restart
 ↓
Recovery
 ↓
Synchronization
```

### 50.22 Background Sync E2E

Test:

```text
Pending Operation
 ↓
Background Worker
 ↓
Synchronization
 ↓
Updated SyncState
```

### 50.23 Retry E2E

Test:

```text
Sync Attempt
 ↓
Network Failure
 ↓
Retry
 ↓
Success
```

### 50.24 Timeout E2E

Test:

```text
Sync Request
 ↓
Timeout
 ↓
Retry
 ↓
No Duplicate Effect
```

### 50.25 Migration E2E

Test:

```text
Existing Application Data
 ↓
Application Upgrade
 ↓
Database Migration
 ↓
Existing Data Available
 ↓
Synchronization Continues
```

### 50.26 E2E Balance Verification

After each major financial workflow, verify:

```text
Expense State
+
Settlement State
+
Balance State
```

are consistent.

### 50.27 E2E Data Verification

E2E tests should verify both:

```text
UI State
+
Persisted State
```

where appropriate.

### 50.28 E2E Synchronization Verification

Verify:

```text
Local State
=
Expected Remote State
```

after synchronization completes.

### 50.29 E2E Convergence Verification

For multi-device scenarios:

```text
Device A Final State
=
Device B Final State
```

for the same synchronized Domain scope.

### 50.30 E2E Error Verification

Verify that failures result in appropriate User-visible behavior without exposing internal implementation details.

### 50.31 E2E Test Data Isolation

Each E2E scenario should use isolated test Users/Groups/data.

### 50.32 E2E Cleanup

After each scenario:

```text
Test Data
 ↓
Cleanup
```

must prevent cross-test contamination.

### 50.33 E2E Test Stability

E2E tests should avoid unnecessary timing assumptions.

Use explicit synchronization conditions rather than arbitrary sleep durations wherever possible.

### 50.34 E2E Test Execution

Critical E2E scenarios should execute in CI where practical.

Physical P2P scenarios may execute in dedicated device test environments.

### 50.35 E2E Regression Suite

The following workflows should form the core regression suite:

```text
Create Group
Add Member
Create Expense
Create Settlement
Offline Expense
Offline Settlement
Backend Sync
P2P Sync
Conflict Resolution
Authentication Recovery
Device Revocation
Application Restart
Database Migration
```

### 50.36 E2E Completion Criteria

End-to-End Testing is complete when:

```text
Core User Workflows Passing
Offline Workflows Passing
Backend Sync Passing
P2P Sync Passing
Conflict Workflow Passing
Financial Verification Passing
Authentication Workflow Passing
Authorization Workflow Passing
Recovery Workflow Passing
Migration Workflow Passing
Background Sync Passing
Regression Suite Passing
```

### 50.37 End-to-End Testing Invariants

The following rules are mandatory:

- E2E tests must validate complete business workflows.
- Critical financial workflows must verify final balances.
- Offline mutations must be verified after reconnection.
- Backend synchronization must be verified across multiple Devices.
- P2P synchronization must be verified where supported.
- Conflict workflows must verify both resolution and final convergence.
- Authentication and Authorization workflows must be tested end-to-end.
- Device revocation must be tested end-to-end.
- Application/process restart must not lose pending synchronization work.
- Database migrations must preserve existing application data.
- Background synchronization must be tested through the actual scheduling path where practical.
- E2E test data must remain isolated.
- Critical E2E scenarios must be part of the regression suite.

## 51. Security Testing

### 51.1 Purpose

This section defines the implementation of Security Testing for SplitSync V1.

Security testing must verify:

```text
Authentication
Authorization
Device Identity
Data Protection
Transport Security
P2P Security
Secret Management
Replay Protection
```

### 51.2 Security Testing Principle

Security tests must verify that unauthorized actions fail safely and authorized actions remain functional.

### 51.3 Authentication Testing

Test:

```text
Valid Credentials
Invalid Credentials
Expired Credentials
Missing Credentials
Refresh
Logout
```

### 51.4 Authorization Testing

Test:

```text
Authorized User
Unauthorized User
Unauthorized Group
Unauthorized Membership
Unauthorized Operation
```

### 51.5 Resource Authorization

Verify that a User cannot access resources outside the authorized Group/context.

### 51.6 Device Identity Testing

Test:

```text
Valid Device
Unknown Device
Invalid Device Identity
Revoked Device
```

### 51.7 Device Revocation Testing

Verify:

```text
Active Device
      ↓
Revoked
      ↓
API Request
      ↓
Rejected
```

and:

```text
Active Device
      ↓
Revoked
      ↓
P2P Connection
      ↓
Rejected
```

### 51.8 API Security Testing

Test:

```text
Missing Authentication
Invalid Authentication
Insufficient Authorization
Malformed Request
Invalid Resource
Unsupported Version
```

### 51.9 API Input Validation

Test malicious and invalid inputs against:

```text
Amounts
IDs
Strings
Dates
Enum Values
Pagination
Synchronization Payloads
```

### 51.10 Injection Testing

Verify protection against applicable injection classes, including:

```text
SQL Injection
Command Injection
JSON Manipulation
Query Manipulation
```

### 51.11 Database Security Testing

Verify:

```text
Parameterized Queries
Access Controls
Schema Constraints
Credential Protection
```

### 51.12 Transport Security Testing

Verify Backend communication uses secure transport.

```text
Android
   ↓ HTTPS
Backend
```

### 51.13 P2P Transport Security

Verify P2P communication provides the security guarantees defined by the Security Architecture.

### 51.14 P2P Identity Testing

Test:

```text
Valid Peer
Fake Peer
Spoofed Peer
Unknown Peer
Revoked Peer
```

### 51.15 P2P Handshake Security

Verify that synchronization cannot begin before:

```text
Identity Verification
Authentication
Authorization
Secure Session
```

### 51.16 Replay Testing

Test replay of:

```text
Authentication Messages
SyncOperations
P2P Messages
Previously Processed Requests
```

Expected:

```text
Replay Rejected
```

or safely treated as an already-processed operation.

### 51.17 SyncOperation Security

Verify that a User/Device cannot forge an operation that it is not authorized to create.

### 51.18 Sync Payload Tampering

Modify synchronization payloads during testing and verify integrity validation rejects tampered data.

### 51.19 Conflict Security

Verify that a malicious or unauthorized operation cannot use conflict handling to bypass authorization or Domain validation.

### 51.20 Financial Security Testing

Verify unauthorized Users cannot modify:

```text
Expense Amount
ExpenseSplits
Settlement Amount
Balance-Relevant Data
```

### 51.21 Membership Security Testing

Verify unauthorized Users cannot:

```text
Add Members
Remove Members
Change Roles
Access Restricted Groups
```

### 51.22 Secret Management Testing

Verify:

```text
Secrets Not In Source Control
Secrets Not In Logs
Secrets Not In APK
Secrets Not In API Responses
```

where applicable.

### 51.23 Logging Security Testing

Search test logs for:

```text
Passwords
Tokens
Private Keys
Encryption Keys
Sensitive Financial Payloads
```

and verify they are not exposed.

### 51.24 Error Security Testing

Verify errors do not expose:

```text
Stack Traces
Database Details
Internal Paths
Credentials
Security Configuration
```

to Users.

### 51.25 Session Security Testing

Test:

```text
Session Expiration
Invalid Session
Logout
Session Reuse
Expired P2P Session
```

### 51.26 Authentication Brute-Force Protection

Where applicable, test that repeated authentication failures are handled according to the Security Architecture.

### 51.27 Rate Limiting Testing

Where API rate limiting is implemented, test:

```text
Normal Request Rate
Excessive Request Rate
Recovery After Limit
```

### 51.28 Authorization Regression Testing

Every new protected endpoint or operation must have an authorization test.

### 51.29 Dependency Security Testing

Project dependencies should be checked for known vulnerabilities through the selected dependency/security tooling.

### 51.30 Android Security Testing

Test:

```text
Secure Storage
Application Permissions
Exported Components
Backup Configuration
Network Security Configuration
```

where applicable to the implementation.

### 51.31 Backend Security Testing

Test:

```text
Authentication
Authorization
Input Validation
Database Access
Secret Handling
API Exposure
```

### 51.32 Security Regression Testing

Every discovered security defect must result in a regression test where practical.

### 51.33 Security Test Environment

Security tests must use isolated test credentials and test data.

Production credentials must never be used for security testing.

### 51.34 Security Testing Completion Criteria

Security Testing is complete when:

```text
Authentication Tests Passing
Authorization Tests Passing
Device Identity Tests Passing
Device Revocation Tests Passing
API Security Tests Passing
Input Validation Tests Passing
Transport Security Tests Passing
P2P Security Tests Passing
Replay Tests Passing
Sync Security Tests Passing
Secret Handling Tests Passing
Logging Security Tests Passing
Error Security Tests Passing
Dependency Security Checks Passing
Security Regression Tests Passing
```

### 51.35 Security Testing Invariants

The following rules are mandatory:

- Authentication must be tested for valid and invalid credentials.
- Authorization must be tested independently from authentication.
- Resource-level authorization must be tested.
- Revoked Devices must be rejected.
- Unauthorized financial mutations must be rejected.
- P2P peer identity must be verified.
- P2P synchronization must require authentication and authorization.
- Replay attacks must be tested.
- Tampered synchronization payloads must be rejected.
- Secrets must not appear in logs or source control.
- User-facing errors must not expose internal security details.
- Security defects must receive regression tests.
- Production credentials must never be used in security tests.


## 52. Performance Testing

### 52.1 Purpose

This section defines the implementation of Performance Testing for SplitSync V1.

Performance testing must verify that the application remains responsive and synchronization remains efficient under expected workloads.

### 52.2 Performance Testing Principle

Performance testing must measure:

```text
Latency
Throughput
Memory
CPU
Database Performance
Network Usage
Battery Impact
Synchronization Time
```

### 52.3 Performance Test Categories

Test:

```text
Android
Backend
Database
API
Synchronization
P2P
```

### 52.4 Android UI Performance

Measure important UI operations such as:

```text
Group List
Expense List
Expense Details
Balance Screen
Settlement Screen
```

### 52.5 Database Performance

Measure:

```text
Insert
Update
Query
Join
Aggregation
Transaction
```

### 52.6 Expense Query Performance

Test queries over:

```text
Small Dataset
Medium Dataset
Large Dataset
```

### 52.7 Balance Calculation Performance

Measure balance calculation with increasing:

```text
Groups
Members
Expenses
Settlements
```

### 52.8 Repository Performance

Measure repository operations under realistic data volumes.

### 52.9 API Latency

Measure:

```text
Request
 ↓
Backend
 ↓
Response
```

under normal and loaded conditions.

### 52.10 API Throughput

Measure supported request throughput for important endpoints.

### 52.11 Synchronization Performance

Measure:

```text
Push Duration
Pull Duration
Full Sync Duration
Incremental Sync Duration
```

### 52.12 Batch Performance

Test different synchronization batch sizes.

```text
Small Batch
Medium Batch
Large Batch
```

### 52.13 Large Sync Queue

Measure synchronization of a large number of pending operations.

### 52.14 Pull Performance

Measure processing of large remote change sets.

### 52.15 Cursor Performance

Verify incremental synchronization does not repeatedly process already synchronized data.

### 52.16 Conflict Performance

Measure conflict detection under concurrent operation workloads.

### 52.17 P2P Performance

Measure:

```text
Discovery Time
Handshake Time
Connection Time
Transfer Rate
Sync Duration
```

### 52.18 P2P Large Transfer

Test P2P synchronization with large operation queues.

### 52.19 Memory Testing

Measure memory usage during:

```text
Large Expense List
Large Sync Batch
P2P Transfer
Initial Synchronization
```

### 52.20 Memory Safety

Verify synchronization does not load an entire large dataset into memory unnecessarily.

### 52.21 CPU Testing

Measure CPU usage during:

```text
Balance Calculation
Database Operations
Serialization
Synchronization
P2P Transfer
```

### 52.22 Battery Testing

Where practical, measure battery impact of:

```text
Background Sync
Retry
P2P Discovery
P2P Synchronization
```

### 52.23 Network Usage

Measure network data consumption for:

```text
Push
Pull
Full Sync
Incremental Sync
P2P
```

### 52.24 Offline Performance

Verify local operations remain responsive without network connectivity.

### 52.25 Startup Performance

Measure:

```text
Application Start
Database Initialization
Sync State Loading
```

### 52.26 Background Worker Performance

Measure:

```text
Worker Start
Sync Execution
Worker Completion
```

### 52.27 Backend Load Testing

The Backend should be tested under expected concurrent User/device workloads.

### 52.28 Database Load Testing

Measure database behavior under concurrent:

```text
Reads
Writes
Transactions
Synchronization Operations
```

### 52.29 Stress Testing

Increase workload beyond expected normal usage to identify failure thresholds.

### 52.30 Endurance Testing

Run synchronization and application workflows over extended periods to detect:

```text
Memory Leaks
Queue Growth
Resource Leaks
Performance Degradation
```

### 52.31 Performance Baselines

The project should define baseline measurements for critical operations.

Examples:

```text
Expense Creation
Balance Calculation
Incremental Sync
Initial Sync
P2P Sync
```

### 52.32 Performance Regression

Performance-sensitive tests should be repeated after major architectural or database changes.

### 52.33 Performance Monitoring

Production performance should be observable through appropriate metrics without collecting unnecessary sensitive data.

### 52.34 Performance Test Environment

Performance testing should use a controlled environment with documented:

```text
Device
Android Version
Backend Configuration
Database Size
Network Conditions
Dataset Size
```

### 52.35 Performance Completion Criteria

Performance Testing is complete when:

```text
Android Performance Tests Passing
Database Performance Tests Passing
API Performance Tests Passing
Sync Performance Tests Passing
P2P Performance Tests Passing
Memory Tests Passing
CPU Tests Passing
Network Usage Measured
Battery Impact Evaluated
Load Tests Passing
Stress Tests Completed
Performance Baselines Documented
```

### 52.36 Performance Testing Invariants

The following rules are mandatory:

- Performance must be measured using representative data.
- Large synchronization datasets must be tested.
- Synchronization must use bounded memory.
- Incremental Pull must not repeatedly process unnecessary historical data.
- Database queries must be tested at realistic scale.
- Critical financial calculations must remain responsive.
- Background synchronization must avoid unnecessary resource consumption.
- P2P synchronization must be tested with realistic operation queues.
- Performance regressions must be identified after significant architectural changes.


## 53. CI/CD Implementation

### 53.1 Purpose

This section defines the implementation of Continuous Integration and Continuous Delivery for SplitSync V1.

CI/CD automates:

```text
Build
Test
Validate
Package
Deploy
```

### 53.2 CI/CD Principle

Every change should pass automated validation before it becomes releasable.

```text
Commit
 ↓
Build
 ↓
Static Checks
 ↓
Unit Tests
 ↓
Integration Tests
 ↓
Package
 ↓
Deploy
```

### 53.3 CI Pipeline

The CI pipeline should contain stages such as:

```text
Checkout
Dependency Resolution
Compile
Static Analysis
Unit Tests
Integration Tests
Build Artifacts
Security Checks
```

### 53.4 Android CI

Android CI should execute:

```text
Compile
Lint
Unit Tests
Instrumentation Tests
Build APK/AAB
```

according to the release workflow.

### 53.5 Backend CI

Backend CI should execute:

```text
Compile
Static Analysis
Unit Tests
Integration Tests
API Tests
Build Artifact
```

### 53.6 Database Migration CI

CI must verify database migrations.

```text
Previous Schema
      ↓
Migration
      ↓
Current Schema
      ↓
Migration Tests
```

### 53.7 Synchronization CI

Synchronization tests should execute automatically.

### 53.8 P2P CI

P2P unit/integration tests should execute in CI.

Physical-device P2P tests may execute in a dedicated device pipeline.

### 53.9 Security CI

CI should execute automated security checks such as:

```text
Dependency Vulnerability Checks
Secret Detection
Static Security Analysis
```

where supported.

### 53.10 Code Quality

CI should enforce the selected:

```text
Formatting
Lint
Static Analysis
Quality Rules
```

### 53.11 Test Failure

Required test failures must fail the pipeline.

### 53.12 Build Failure

Compilation or packaging failures must fail the pipeline.

### 53.13 Artifact Generation

Successful builds should produce versioned artifacts.

Examples:

```text
Android APK
Android AAB
Backend Artifact
```

### 53.14 Artifact Naming

Artifacts should include enough information to identify:

```text
Application
Version
Build
Environment
```

### 53.15 Build Metadata

Build artifacts should expose a build/version identifier for diagnostics.

### 53.16 Branch Validation

Pull requests should run the required validation pipeline before merge.

### 53.17 Main Branch

The main branch should remain buildable and testable.

### 53.18 Release Branch

If release branches are used, they should follow the project's branching strategy.

### 53.19 Versioning

CI/CD must integrate:

```text
Application Version
API Version
Database Version
Build Number
```

where applicable.

### 53.20 Environment Separation

CI/CD environments must remain separate:

```text
Development
Test
Production
```

### 53.21 Secrets in CI

Secrets must be supplied through secure CI secret management.

They must not be committed to the repository.

### 53.22 Deployment Approval

Production deployment should require the approval mechanism defined by the project.

### 53.23 Deployment Rollback

Deployment procedures must define rollback or recovery procedures.

### 53.24 Database Deployment

Backend deployments involving schema changes must execute the approved migration procedure.

### 53.25 Deployment Ordering

Where required:

```text
Database Migration
      ↓
Backend Deployment
      ↓
Client Compatibility
```

must follow the compatibility strategy.

### 53.26 API Compatibility

Deployments must not introduce an API change that breaks currently supported clients.

### 53.27 Release Validation

Before release:

```text
Build
Tests
Security Checks
Migration Checks
Performance Checks
```

must meet the release criteria.

### 53.28 CI Logs

CI logs must not expose:

```text
Secrets
Credentials
Private Keys
Tokens
```

### 53.29 CI Artifact Security

Build artifacts and deployment credentials must be protected.

### 53.30 CI Monitoring

Pipeline failures should be observable and traceable.

### 53.31 CI/CD Completion Criteria

CI/CD Implementation is complete when:

```text
Android CI Implemented
Backend CI Implemented
Unit Tests Integrated
Integration Tests Integrated
Migration Tests Integrated
Sync Tests Integrated
P2P Tests Integrated
Security Checks Integrated
Static Analysis Integrated
Artifact Generation Implemented
Environment Separation Implemented
Secret Management Implemented
Deployment Pipeline Implemented
Rollback Procedure Defined
```

### 53.32 CI/CD Invariants

The following rules are mandatory:

- Required tests must run automatically.
- Required test failures must block the relevant pipeline stage.
- Build failures must block deployment.
- Security checks must run before release.
- Secrets must not be committed to source control.
- CI logs must not expose secrets.
- Development, Test, and Production environments must remain separated.
- Database migrations must be validated before deployment.
- API compatibility must be preserved for supported clients.
- Production deployment must follow an explicit approval/release process.


## 54. Development Deployment

### 54.1 Purpose

This section defines the deployment process for the Development environment.

### 54.2 Development Environment

Development deployment is intended for:

```text
Active Development
Feature Testing
Developer Integration
Debugging
```

### 54.3 Development Components

The environment may contain:

```text
Android Development Build
Development Backend
Development Database
Development Configuration
```

### 54.4 Development Backend

The Backend must use Development-specific configuration.

### 54.5 Development Database

Development database data must remain isolated from Test and Production data.

### 54.6 Android Development Build

The Android application should use the Development configuration.

### 54.7 Development API

The Android development build must target the Development API endpoint.

### 54.8 Development Secrets

Development secrets must be managed separately from production secrets.

### 54.9 Development Deployment Flow

```text
Developer Change
      ↓
CI Validation
      ↓
Build
      ↓
Deploy Development Backend
      ↓
Run Migrations
      ↓
Deploy / Install Android Build
      ↓
Smoke Test
```

### 54.10 Development Database Migration

Development deployments may automatically execute approved migrations.

### 54.11 Development Data

Development data may be reset/recreated when required.

### 54.12 Development Logging

Development may enable more verbose diagnostic logging than Production.

### 54.13 Development P2P

P2P development testing should use designated Development Devices/peer identities.

### 54.14 Development Testing

After deployment, execute smoke tests:

```text
Application Start
Login
Group Creation
Expense Creation
Sync
Settlement
```

### 54.15 Development Failure

If deployment fails:

```text
Deployment Failure
      ↓
Fix / Rollback
      ↓
Re-run Validation
```

### 54.16 Development Rollback

Development rollback may be simpler than Production but must not leave the database in an incompatible state.

### 54.17 Development Completion Criteria

Development Deployment is complete when:

```text
Development Backend Deployed
Development Database Available
Migrations Applied
Android Development Build Available
Configuration Applied
Smoke Tests Passing
P2P Development Environment Available
Logging Available
```

### 54.18 Development Deployment Invariants

The following rules are mandatory:

- Development must remain isolated from Production.
- Development must not use Production credentials.
- Development must not use Production financial data.
- Development API configuration must point to Development infrastructure.
- Development database migrations must be validated.
- Development builds must not accidentally target Production services.
- Development P2P identities must be separated from Production identities.


## 55. Test Deployment

### 55.1 Purpose

This section defines the deployment process for the Test environment.

The Test environment is used for:

```text
Integration Testing
System Testing
E2E Testing
Security Testing
Performance Testing
Release Validation
```

### 55.2 Test Environment

The Test environment should contain:

```text
Test Android Build
Test Backend
Test Database
Test Configuration
Test Accounts
```

### 55.3 Test Data Isolation

Test data must remain isolated from Development and Production.

### 55.4 Test Backend

The Backend must use Test-specific configuration.

### 55.5 Test Database

The Test database must contain only controlled test data.

### 55.6 Test Android Build

The Android Test build must target the Test API endpoint.

### 55.7 Test Secrets

Test credentials and secrets must be separate from Development and Production credentials.

### 55.8 Test Deployment Flow

```text
Validated Build
      ↓
Deploy Test Backend
      ↓
Run Database Migrations
      ↓
Deploy Test Android Build
      ↓
Run Integration Tests
      ↓
Run E2E Tests
      ↓
Release Validation
```

### 55.9 Test Migration

All database migrations must execute through the same migration mechanism intended for release environments.

### 55.10 Test Configuration

Test configuration should use deterministic:

```text
Backend URL
Database
Authentication
Sync Settings
P2P Settings
```

### 55.11 Test Accounts

Dedicated test accounts should be used for:

```text
User
Admin
Group Member
Unauthorized User
Test Device
Test Peer
```

where required.

### 55.12 Test P2P Environment

The Test environment should provide controlled P2P peers/devices where P2P E2E testing is required.

### 55.13 Test Security

Test deployment must support:

```text
Authentication Tests
Authorization Tests
Device Revocation Tests
P2P Security Tests
```

without exposing production credentials.

### 55.14 Test Performance

Performance tests should execute against a controlled Test environment with documented resources and dataset size.

### 55.15 Test Logging

Test logging may be more verbose than Production but must still follow sensitive-data protection rules.

### 55.16 Test Smoke Tests

After deployment:

```text
Application Start
Authentication
Group Creation
Expense Creation
Settlement
Backend Sync
P2P Sync
```

should be validated where applicable.

### 55.17 Test Regression Suite

The required regression suite should execute after deployment.

### 55.18 Test Failure

If Test deployment or validation fails:

```text
Deployment / Test Failure
      ↓
Block Release
      ↓
Fix
      ↓
Redeploy
```

### 55.19 Test Rollback

Test deployment must support rollback or redeployment to a known-good state.

### 55.20 Test Release Candidate

A build that passes the required Test environment validation may be promoted as a release candidate.

```text
Test Passed
      ↓
Release Candidate
```

### 55.21 Test Environment Promotion

Promotion must use the validated artifact rather than rebuilding an unverified artifact for the next environment.

### 55.22 Test Completion Criteria

Test Deployment is complete when:

```text
Test Backend Deployed
Test Database Available
Migrations Applied
Test Android Build Deployed
Test Configuration Applied
Integration Tests Passing
E2E Tests Passing
Security Tests Passing
Required Performance Tests Passing
P2P Tests Passing
Regression Suite Passing
Release Candidate Validated
```

### 55.23 Test Deployment Invariants

The following rules are mandatory:

- Test infrastructure must remain isolated from Production.
- Test credentials must never be Production credentials.
- Test data must not contain uncontrolled Production financial data.
- Test Android builds must target Test infrastructure.
- Database migrations must be executed and validated.
- The same validated artifact must be promoted where possible.
- Required integration, E2E, security, and synchronization tests must pass before release promotion.
- P2P Test Devices/identities must remain isolated from Production peers.
- Test failures must block release promotion.
- Test deployment must support recovery to a known-good state.

## 56. Staging Deployment

### 56.1 Purpose

This section defines the deployment process for the Staging environment.

Staging is the final environment for validating the release candidate against production-like infrastructure and configuration before Production deployment.

### 56.2 Staging Environment

The Staging environment should contain:

```text
Staging Android Build
Staging Backend
Staging Database
Staging Configuration
Staging Authentication
Staging P2P Test Infrastructure
```

### 56.3 Staging Principle

Staging should be as close to Production as practical while remaining completely isolated from Production.

```text
Test
 ↓
Release Candidate
 ↓
Staging
 ↓
Production
```

### 56.4 Artifact Promotion

The validated Test artifact should be promoted to Staging without rebuilding it where practical.

```text
Test Artifact
      ↓
Staging
```

### 56.5 Staging Backend

The Backend must use Staging-specific configuration.

### 56.6 Staging Database

The Staging database must be isolated from Production.

### 56.7 Staging Data

Staging data should use controlled non-production data.

Production financial data must not be copied into Staging without an explicit approved data-protection process.

### 56.8 Staging Secrets

Staging credentials and secrets must remain separate from Production secrets.

### 56.9 Staging Configuration

Staging should use Production-like configuration for:

```text
API
Database
Authentication
Synchronization
Logging
Security
```

while using Staging-specific endpoints and credentials.

### 56.10 Staging Deployment Flow

```text
Validated Release Candidate
      ↓
Deploy Backend
      ↓
Run Database Migration
      ↓
Deploy Android Build
      ↓
Run Smoke Tests
      ↓
Run Regression Tests
      ↓
Run Security Validation
      ↓
Run Synchronization Tests
      ↓
Release Approval
```

### 56.11 Database Migration

Staging must execute the same migration mechanism intended for Production.

### 56.12 Migration Verification

After migration:

```text
Schema
+
Existing Staging Data
+
Constraints
+
Indexes
```

must be verified.

### 56.13 API Compatibility

Staging must verify compatibility between:

```text
Android Release Candidate
+
Backend Release
+
Database Schema
```

### 56.14 Synchronization Validation

Staging must validate:

```text
Push
Pull
Offline
Retry
Recovery
Conflict
P2P
```

where applicable.

### 56.15 P2P Staging

P2P testing should use dedicated Staging peer identities and Devices.

Production peer identities must not be used.

### 56.16 Security Validation

Staging should execute the required:

```text
Authentication Tests
Authorization Tests
Device Revocation Tests
P2P Security Tests
Secret Checks
```

### 56.17 Performance Validation

Critical performance tests should execute against the Staging environment.

### 56.18 Monitoring Validation

Monitoring and logging must be verified before Production deployment.

### 56.19 Staging Smoke Tests

At minimum:

```text
Application Start
Authentication
Group Creation
Expense Creation
Expense Sync
Settlement
Balance Calculation
P2P Sync
```

where applicable.

### 56.20 Staging Regression

The required regression suite must pass before Production promotion.

### 56.21 Staging Failure

If any release-blocking validation fails:

```text
Staging Failure
      ↓
Block Production Promotion
      ↓
Fix
      ↓
Rebuild / Revalidate
      ↓
Redeploy
```

### 56.22 Staging Rollback

Staging must support rollback or redeployment to a known-good release.

### 56.23 Production Readiness

The release is Production-ready only when:

```text
Staging Deployment Successful
+
Migrations Verified
+
Regression Passing
+
Security Passing
+
Synchronization Passing
+
Monitoring Verified
```

### 56.24 Staging Completion Criteria

Staging Deployment is complete when:

```text
Release Candidate Deployed
Staging Backend Available
Staging Database Available
Migrations Verified
Android Build Verified
API Compatibility Verified
Synchronization Verified
P2P Verified
Security Validation Passed
Performance Validation Passed
Monitoring Verified
Regression Suite Passed
Production Approval Obtained
```

### 56.25 Staging Deployment Invariants

The following rules are mandatory:

- Staging must remain isolated from Production.
- Staging credentials must never be Production credentials.
- Staging P2P identities must remain separate from Production identities.
- Staging migrations must use the Production migration mechanism.
- The validated release artifact should be promoted without unnecessary rebuilding.
- Production promotion must be blocked by release-blocking Staging failures.
- Staging must validate synchronization behavior before Production deployment.
- Staging must validate monitoring and observability before Production deployment.


## 57. Production Deployment

### 57.1 Purpose

This section defines the deployment process for the Production environment.

Production deployment introduces the validated release to real Users and production infrastructure.

### 57.2 Production Principle

Production deployment must be controlled, repeatable, auditable, and reversible where technically possible.

```text
Approved Release
      ↓
Production Deployment
      ↓
Validation
      ↓
Monitoring
```

### 57.3 Production Components

Production may contain:

```text
Production Android Application
Production Backend
Production Database
Production Configuration
Production Authentication
Production P2P Infrastructure
Monitoring Infrastructure
Backup Infrastructure
```

### 57.4 Production Approval

Production deployment requires the release approval defined by the Release Management process.

### 57.5 Production Artifact

Production must use the validated release artifact.

### 57.6 Production Configuration

Production configuration must be supplied through secure configuration management.

### 57.7 Production Secrets

Production secrets must be managed through secure secret-management mechanisms.

They must never be committed to source control.

### 57.8 Production Database

The Production database must be backed up according to the Backup and Recovery strategy before risky schema changes.

### 57.9 Production Migration

Database migrations must be reviewed and validated before Production execution.

### 57.10 Migration Ordering

Production deployment must follow the API/database compatibility strategy.

Where required:

```text
Backward-Compatible Migration
      ↓
Backend Deployment
      ↓
Client Compatibility
```

### 57.11 Production Deployment Flow

```text
Release Approval
      ↓
Backup / Pre-Deployment Checks
      ↓
Database Migration
      ↓
Backend Deployment
      ↓
Backend Health Check
      ↓
Android Release
      ↓
Smoke Tests
      ↓
Monitoring
```

### 57.12 Pre-Deployment Checks

Verify:

```text
Release Artifact
Database Backup
Migration Status
Configuration
Secrets
Dependencies
Monitoring
Rollback Plan
```

### 57.13 Backend Health Check

After deployment:

```text
Backend
 ↓
Health Check
 ↓
Ready
```

must be verified before exposing the release broadly.

### 57.14 Database Health Check

Verify:

```text
Connectivity
Schema Version
Migration Status
Critical Queries
```

### 57.15 API Health Check

Verify critical API operations.

### 57.16 Synchronization Health Check

Verify:

```text
Push
Pull
SyncState
```

for controlled test data where possible.

### 57.17 Android Release

The Production Android release must use:

```text
Production API
Production Configuration
Production Signing
```

according to the Build Configuration strategy.

### 57.18 Production P2P

Production P2P must use Production Device Identity and trust mechanisms.

### 57.19 Production Logging

Production logging must use controlled verbosity and sensitive-data protection.

### 57.20 Production Monitoring

Immediately after deployment, monitor:

```text
API Errors
Authentication Failures
Synchronization Failures
Database Errors
Crash Rate
Latency
P2P Failures
```

### 57.21 Deployment Validation

Post-deployment smoke tests should verify:

```text
Login
Group Access
Expense Creation
Settlement
Synchronization
Balance
```

### 57.22 Gradual Rollout

Where supported, Production Android releases should use a controlled rollout strategy.

### 57.23 Rollout Monitoring

During rollout:

```text
Release
 ↓
Monitor
 ↓
Evaluate
 ↓
Continue / Pause / Rollback
```

### 57.24 Deployment Failure

If critical Production failures occur:

```text
Detect
 ↓
Assess
 ↓
Stop / Rollout Pause
 ↓
Rollback or Hotfix
```

### 57.25 Rollback

Rollback strategy must distinguish between:

```text
Application Rollback
Backend Rollback
Database Rollback
```

Database rollback should not be assumed safe for every migration.

### 57.26 Forward Recovery

If a database migration cannot safely roll back:

```text
Migration
 ↓
Forward-Compatible Fix
```

must be used.

### 57.27 Production Incident

Production incidents must follow the operational incident process.

### 57.28 Production Security

Production deployment must verify:

```text
TLS
Secrets
Authentication
Authorization
Device Security
Database Access
```

### 57.29 Production Completion Criteria

Production Deployment is complete when:

```text
Approved Artifact Deployed
Database Migration Successful
Backend Healthy
API Healthy
Android Release Published
Smoke Tests Passing
Synchronization Healthy
Monitoring Active
No Release-Blocking Errors
```

### 57.30 Production Deployment Invariants

The following rules are mandatory:

- Production must use approved release artifacts.
- Production secrets must never be committed to source control.
- Production must remain isolated from non-production environments.
- Database migrations must be reviewed before execution.
- Required backups must be completed before risky migrations.
- Backend health must be verified after deployment.
- Critical synchronization flows must be verified after release.
- Production monitoring must be active during deployment.
- Critical failures must trigger controlled rollout pause or recovery.
- Database rollback must never be assumed without verifying migration safety.


## 58. Monitoring and Observability

### 58.1 Purpose

This section defines the implementation of Monitoring and Observability for SplitSync V1.

Observability must allow the system to answer:

```text
Is the System Healthy?
What Failed?
Where Did It Fail?
Why Did It Fail?
```

### 58.2 Observability Components

Monitoring should cover:

```text
Application
Backend
Database
API
Synchronization
P2P
Security
Infrastructure
```

### 58.3 Health Checks

The Backend should expose appropriate health information.

Conceptually:

```text
Health
Readiness
Dependencies
```

### 58.4 Backend Health

Monitor:

```text
Availability
Response Time
Error Rate
Throughput
```

### 58.5 Database Health

Monitor:

```text
Connectivity
Query Latency
Connection Usage
Storage
Errors
```

### 58.6 API Monitoring

Monitor:

```text
Request Count
Latency
HTTP Errors
Authentication Errors
Authorization Errors
```

### 58.7 Android Monitoring

Where appropriate, monitor:

```text
Crash Rate
ANR Rate
Startup Performance
Sync Failures
```

### 58.8 Synchronization Monitoring

Monitor:

```text
Sync Attempts
Successful Syncs
Failed Syncs
Pending Operations
Conflicts
Retries
Sync Duration
```

### 58.9 Push Monitoring

Track:

```text
Operations Pushed
Push Success
Push Failure
Push Retry
```

### 58.10 Pull Monitoring

Track:

```text
Operations Pulled
Pull Success
Pull Failure
Cursor Progress
```

### 58.11 Cursor Monitoring

Monitor abnormal conditions such as:

```text
Cursor Not Advancing
Cursor Regression
Repeated Batch
```

### 58.12 Conflict Monitoring

Track:

```text
Conflict Count
Conflict Rate
Open Conflicts
Resolution Time
```

### 58.13 P2P Monitoring

Track:

```text
Discovery Attempts
Peer Connections
Handshake Failures
P2P Sync Failures
Connection Loss
P2P Retry
```

### 58.14 Security Monitoring

Monitor security-relevant events such as:

```text
Authentication Failures
Authorization Failures
Device Revocations
Peer Rejections
Replay Rejections
```

### 58.15 Logging Integration

Logs should include correlation information:

```text
Request ID
Sync ID
Operation ID
Session ID
```

where applicable.

### 58.16 Metrics

Metrics should be designed around measurable system behavior.

### 58.17 Alerting

Alerts should exist for critical conditions such as:

```text
Backend Unavailable
High Error Rate
High Sync Failure Rate
Database Failure
Authentication Failure Spike
P2P Failure Spike
```

### 58.18 Alert Thresholds

Thresholds must be defined based on expected system behavior rather than arbitrary values.

### 58.19 Alert Noise

Alerts must be tuned to avoid excessive false positives.

### 58.20 Dashboard

Operational dashboards should expose:

```text
System Health
API Health
Database Health
Sync Health
P2P Health
Security Events
```

### 58.21 Synchronization Dashboard

A synchronization dashboard may include:

```text
Pending Operations
Sync Success Rate
Sync Failure Rate
Conflict Count
Retry Count
Average Sync Duration
```

### 58.22 P2P Dashboard

A P2P dashboard may include:

```text
Peer Connections
Handshake Success Rate
P2P Sync Success Rate
Connection Failures
Transfer Duration
```

### 58.23 Traceability

A synchronization failure should be traceable across:

```text
Android
 ↓
API / P2P
 ↓
Backend
 ↓
Database
```

using correlation identifiers where applicable.

### 58.24 Privacy

Monitoring must minimize collection of:

```text
Personal Data
Financial Payloads
Authentication Secrets
Cryptographic Material
```

### 58.25 Production Monitoring

Production monitoring must be active before or at the time of Production deployment.

### 58.26 Monitoring Failure

Monitoring infrastructure failure must not corrupt application or financial state.

### 58.27 Observability Testing

Monitoring must be tested by generating controlled:

```text
Success
Failure
Authentication Error
Sync Failure
P2P Failure
Database Error
```

### 58.28 Observability Completion Criteria

Monitoring and Observability is complete when:

```text
Health Checks Implemented
Application Monitoring Implemented
Backend Monitoring Implemented
Database Monitoring Implemented
API Monitoring Implemented
Sync Monitoring Implemented
P2P Monitoring Implemented
Security Monitoring Implemented
Metrics Implemented
Dashboards Implemented
Alerts Implemented
Correlation IDs Implemented
Privacy Controls Implemented
Monitoring Tests Implemented
```

### 58.29 Monitoring and Observability Invariants

The following rules are mandatory:

- Critical system health must be observable.
- Synchronization health must be observable.
- P2P failures must be observable.
- Security failures must be observable.
- Correlation IDs should allow failures to be traced across system boundaries.
- Monitoring must not expose sensitive financial or security data.
- Monitoring failures must not corrupt application state.
- Alerts must represent actionable conditions.
- Production monitoring must be active before relying on the release.


## 59. Backup and Recovery

### 59.1 Purpose

This section defines the implementation of Backup and Recovery for SplitSync V1.

Backup and Recovery protects Backend data against:

```text
Database Failure
Data Corruption
Infrastructure Failure
Operational Error
Deployment Failure
```

### 59.2 Backup Principle

Backups must be:

```text
Reliable
Protected
Recoverable
Tested
```

### 59.3 Backup Scope

Backend backups must protect critical persistent data including:

```text
Users
Devices
Groups
Memberships
Expenses
ExpenseSplits
Settlements
Synchronization Metadata
Conflicts
```

where stored by the Backend.

### 59.4 Backup Types

The implementation may use:

```text
Full Backup
Incremental Backup
Point-in-Time Recovery
```

according to the selected infrastructure.

### 59.5 Backup Frequency

Backup frequency must be defined according to the required recovery objectives.

### 59.6 Recovery Point Objective

The project must define the maximum acceptable amount of data that may be lost after a catastrophic failure.

### 59.7 Recovery Time Objective

The project must define the target time required to restore service after a catastrophic failure.

### 59.8 Backup Storage

Backups must be stored separately from the primary database where practical.

### 59.9 Backup Security

Backups must be protected using appropriate:

```text
Access Control
Encryption
Credential Protection
```

### 59.10 Backup Access

Only authorized operators/services should have access to production backups.

### 59.11 Backup Retention

Backup retention must follow the operational and security requirements.

### 59.12 Backup Verification

A successful backup job must not be assumed to mean a recoverable backup.

Backups must be verified.

### 59.13 Recovery Testing

Recovery tests must periodically restore backups into an isolated environment.

```text
Backup
 ↓
Restore
 ↓
Verify
```

### 59.14 Database Recovery

Recovery must verify:

```text
Schema
Data
Constraints
Indexes
```

### 59.15 Financial Data Recovery

After recovery, verify:

```text
Expenses
ExpenseSplits
Settlements
Balances
```

remain consistent.

### 59.16 Synchronization Data Recovery

Recovery must preserve:

```text
SyncOperations
SyncState
Cursors
Conflicts
```

where stored by the Backend.

### 59.17 Cursor Recovery

Recovered synchronization cursors must remain consistent with the recovered synchronization data.

### 59.18 Conflict Recovery

Recovered conflicts must remain resolvable.

### 59.19 Backup Before Migration

Risky Production migrations should have a verified backup before execution.

### 59.20 Backup Before Deployment

Deployment procedures should define when a backup is required.

### 59.21 Disaster Recovery

The project should define recovery procedures for:

```text
Database Loss
Backend Infrastructure Loss
Storage Loss
Configuration Loss
```

### 59.22 Recovery Environment

Recovery must use an isolated recovery environment before Production restoration where practical.

### 59.23 Recovery Validation

After recovery:

```text
Database Healthy
+
Backend Healthy
+
API Healthy
+
Synchronization Healthy
```

must be verified.

### 59.24 Recovery and Android Clients

Android clients must safely recover from temporary Backend unavailability.

Local offline state must not be discarded merely because the Backend is being restored.

### 59.25 Recovery and P2P

P2P data exchange may help Devices retain/propagate valid state while Backend recovery occurs, subject to the synchronization model.

### 59.26 Recovery Logging

Recovery operations must be auditable.

### 59.27 Recovery Security

Restored environments must not expose production data to unauthorized environments.

### 59.28 Recovery Testing Frequency

Recovery tests should be performed periodically according to operational requirements.

### 59.29 Backup Monitoring

Monitor:

```text
Backup Success
Backup Failure
Backup Age
Storage Usage
Restore Test Result
```

### 59.30 Backup Failure

A failed backup must generate an operational alert.

### 59.31 Recovery Failure

A failed restore must trigger investigation and remediation.

### 59.32 Backup Completion Criteria

Backup and Recovery is complete when:

```text
Backup Strategy Implemented
Backup Scheduling Implemented
Backup Storage Secured
Backup Retention Defined
Backup Monitoring Implemented
Restore Procedure Implemented
Recovery Testing Implemented
RPO Defined
RTO Defined
Financial Data Verification Implemented
Synchronization Data Verification Implemented
Disaster Recovery Procedure Defined
```

### 59.33 Backup and Recovery Invariants

The following rules are mandatory:

- Critical Backend data must be backed up.
- Backups must be protected from unauthorized access.
- Backup success must be verifiable.
- Restore procedures must be tested.
- Financial data must remain consistent after recovery.
- Synchronization metadata must remain consistent after recovery.
- Cursors must not become inconsistent with recovered synchronization data.
- Conflicts must remain resolvable after recovery.
- Production migrations requiring backup must not proceed without the required verified backup.
- Backup failures must be observable.
- Recovery procedures must be documented and tested.


## 60. Release Management

### 60.1 Purpose

This section defines the Release Management implementation for SplitSync V1.

Release Management controls how validated changes move from development to Production.

```text
Development
      ↓
Test
      ↓
Staging
      ↓
Production
```

### 60.2 Release Principle

Every release must be:

```text
Versioned
Validated
Approved
Traceable
Deployable
Recoverable
```

### 60.3 Release Artifact

A release consists of the validated artifacts required for the system.

Examples:

```text
Android APK/AAB
Backend Artifact
Database Migration
Configuration
Release Metadata
```

### 60.4 Release Version

Each release must have a unique version.

### 60.5 Build Number

Android builds must have a unique build number.

### 60.6 API Version

The release must identify the API version supported by the client/backend.

### 60.7 Database Version

The release must identify the expected database schema version.

### 60.8 Release Candidate

A Release Candidate is an artifact that has passed the required Test/Staging validation.

```text
Validated Build
      ↓
Release Candidate
```

### 60.9 Release Checklist

Before Production release:

```text
Build Passing
Unit Tests Passing
Integration Tests Passing
E2E Tests Passing
Security Tests Passing
Synchronization Tests Passing
P2P Tests Passing
Migration Tests Passing
Performance Validation Completed
Staging Validation Completed
Monitoring Ready
Backup Ready
Rollback Plan Ready
```

### 60.10 Release Approval

Production release requires explicit approval according to the project's governance process.

### 60.11 Release Notes

Every release should document:

```text
Version
Changes
Bug Fixes
Database Changes
API Changes
Synchronization Changes
Security Changes
Known Issues
```

### 60.12 Database Changes

Release notes must clearly identify database migration requirements.

### 60.13 API Changes

Release notes must identify:

```text
New Endpoints
Changed Endpoints
Deprecated Endpoints
API Version
```

where applicable.

### 60.14 Synchronization Changes

Synchronization changes must document:

```text
Sync Protocol Changes
SyncOperation Changes
SyncState Changes
Conflict Changes
Migration Requirements
```

### 60.15 P2P Changes

P2P releases must document:

```text
Discovery Changes
Handshake Changes
Protocol Changes
Compatibility Changes
```

### 60.16 Compatibility

Before release, verify compatibility between:

```text
Android
Backend
Database
API
Sync Protocol
P2P Protocol
```

### 60.17 Backward Compatibility

The release must not break supported older clients unless the release explicitly introduces a planned breaking change.

### 60.18 Release Branch

If release branches are used, the release branch must contain only approved release changes.

### 60.19 Release Tag

Production releases should be tagged in source control.

### 60.20 Artifact Traceability

Every Production artifact must be traceable to:

```text
Source Commit
Build
Version
Release
```

### 60.21 Release Deployment

Production deployment must follow the Production Deployment procedure.

### 60.22 Rollout Strategy

Where supported, release rollout may be gradual.

```text
Small Percentage
      ↓
Monitor
      ↓
Increase
      ↓
Complete
```

### 60.23 Rollout Pause

If critical problems are detected:

```text
Detect
 ↓
Pause Rollout
 ↓
Investigate
```

### 60.24 Rollback

If required:

```text
Pause
 ↓
Rollback / Hotfix
 ↓
Validate
 ↓
Resume
```

### 60.25 Hotfix

Critical Production defects may be addressed through a controlled hotfix process.

### 60.26 Hotfix Validation

Hotfixes must still pass the minimum required:

```text
Unit Tests
Regression Tests
Security Checks
Deployment Validation
```

before Production deployment.

### 60.27 Release Failure

Failed releases must be documented and investigated.

### 60.28 Release Monitoring

After release:

```text
Monitor Errors
Monitor Crashes
Monitor Sync
Monitor API
Monitor Database
Monitor P2P
```

### 60.29 Post-Release Validation

Perform:

```text
Smoke Tests
Health Checks
Synchronization Checks
Critical User Workflow Checks
```

### 60.30 Release Closure

A release may be considered complete after:

```text
Deployment Successful
+
Monitoring Stable
+
Post-Release Validation Passed
```

### 60.31 Release Documentation

The release record should contain:

```text
Release Version
Source Commit
Build Artifacts
Deployment Time
Migration Version
Approval
Validation Results
Known Issues
```

### 60.32 Release Metrics

Useful metrics include:

```text
Deployment Frequency
Deployment Success Rate
Rollback Rate
Release Failure Rate
Mean Time to Recovery
Post-Release Defects
```

### 60.33 Release Completion Criteria

Release Management is complete when:

```text
Versioning Implemented
Release Candidate Process Implemented
Release Checklist Defined
Approval Process Defined
Release Notes Implemented
Artifact Traceability Implemented
Compatibility Validation Implemented
Production Rollout Implemented
Rollback Process Defined
Hotfix Process Defined
Post-Release Validation Implemented
Release Monitoring Implemented
Release Documentation Implemented
```

### 60.34 Release Management Invariants

The following rules are mandatory:

- Every Production release must be uniquely versioned.
- Production artifacts must be traceable to source code.
- Only validated artifacts may be promoted to Production.
- Required tests must pass before Production release.
- Database migrations must be explicitly reviewed.
- API compatibility must be verified.
- Synchronization compatibility must be verified.
- P2P protocol compatibility must be verified.
- Production deployment must have an approved rollback/recovery strategy.
- Critical releases must support controlled rollout where available.
- Hotfixes must receive appropriate regression and security validation.
- Post-release monitoring must be performed.
- Release documentation must be retained.

## 61. Implementation Validation

### 61.1 Purpose

This section defines the validation process used to confirm that the SplitSync V1 implementation conforms to the approved Technical Design and Implementation Plan.

Implementation validation must verify:

```text
Architecture
+
Domain Rules
+
Data Model
+
Offline Behavior
+
Synchronization
+
Security
+
P2P
+
Testing
+
Deployment
```

### 61.2 Validation Principle

Implementation must be validated against the documented design rather than only against whether the application builds successfully.

```text
Technical Design
      ↓
Implementation
      ↓
Validation
      ↓
V1 Completion
```

### 61.3 Architecture Validation

Verify that the implemented architecture matches:

```text
Android Architecture
Backend Architecture
Package Structure
Module Boundaries
Layer Boundaries
```

### 61.4 Domain Validation

Verify that the implementation correctly represents:

```text
Domain Entities
Value Objects
Business Rules
Domain Validation
Domain Invariants
```

### 61.5 Database Validation

Verify:

```text
Local Schema
Backend Schema
Relationships
Constraints
Indexes
Transactions
Migrations
```

### 61.6 Data Consistency Validation

Verify consistency between:

```text
Expense
ExpenseSplits
Settlement
Balance
Group
Membership
```

### 61.7 Offline Validation

Verify:

```text
Offline Read
Offline Mutation
Local Transaction
Pending SyncOperation
Application Restart
Network Recovery
```

### 61.8 Synchronization Validation

Verify:

```text
Push
Pull
SyncState
Cursor
Retry
Recovery
Idempotency
```

### 61.9 Conflict Validation

Verify:

```text
Conflict Detection
Conflict Persistence
Conflict Resolution
Resolution Authorization
Convergence
```

### 61.10 P2P Validation

Verify:

```text
Discovery
Handshake
Authentication
Authorization
Protocol Negotiation
P2P Push
P2P Pull
Recovery
```

### 61.11 Security Validation

Verify:

```text
Authentication
Authorization
Device Identity
Device Revocation
Transport Security
Secret Handling
Replay Protection
```

### 61.12 API Validation

Verify:

```text
API Contract
Request Validation
Response Contract
Error Contract
API Version
Backward Compatibility
```

### 61.13 Background Processing Validation

Verify:

```text
Worker Scheduling
Retry
Backoff
Cancellation
Process Death
Recovery
```

### 61.14 Testing Validation

Verify that required tests exist and pass:

```text
Unit
Integration
Synchronization
Conflict
P2P
End-to-End
Security
Performance
```

### 61.15 Deployment Validation

Verify:

```text
Development
Test
Staging
Production
```

deployment procedures are implemented according to the defined architecture.

### 61.16 Observability Validation

Verify:

```text
Logging
Metrics
Health Checks
Monitoring
Alerts
Correlation IDs
```

### 61.17 Backup Validation

Verify:

```text
Backup
Restore
Recovery
Financial Data Integrity
Synchronization Metadata Integrity
```

### 61.18 Implementation Traceability

Each major implementation item should be traceable to its corresponding design requirement.

```text
Requirement
   ↓
Technical Design
   ↓
Implementation
   ↓
Test
   ↓
Validation
```

### 61.19 Defect Classification

Implementation defects should be classified as:

```text
Domain Defect
Data Defect
API Defect
Synchronization Defect
Security Defect
P2P Defect
UI Defect
Infrastructure Defect
Deployment Defect
```

### 61.20 Validation Failure

If validation fails:

```text
Validation Failure
      ↓
Identify Defect
      ↓
Fix Implementation
      ↓
Run Relevant Tests
      ↓
Revalidate
```

### 61.21 Validation Completion Criteria

Implementation Validation is complete when:

```text
Architecture Validated
Domain Validated
Database Validated
Offline Behavior Validated
Synchronization Validated
Conflict Handling Validated
P2P Validated
Security Validated
API Validated
Background Processing Validated
Testing Validated
Deployment Validated
Observability Validated
Backup and Recovery Validated
```

### 61.22 Implementation Validation Invariants

The following rules are mandatory:

- Implementation must conform to the approved architecture.
- Domain invariants must be preserved.
- Financial data must remain consistent.
- Offline operations must remain durable.
- Synchronization must remain idempotent.
- Conflicts must not be silently overwritten.
- P2P synchronization must follow the security model.
- Required tests must pass.
- Deployment configuration must remain environment-specific.
- Production readiness must be validated before release.


## 62. Implementation Checklist

### 62.1 Purpose

This section provides the master implementation checklist for SplitSync V1.

### 62.2 Project Setup

```text
[ ] Repository Structure Created
[ ] Android Project Created
[ ] Backend Project Created
[ ] Build Configuration Created
[ ] Development Environment Configured
[ ] Test Environment Configured
```

### 62.3 Domain

```text
[ ] Domain Entities Implemented
[ ] Value Objects Implemented
[ ] Common Types Implemented
[ ] Domain Validation Implemented
[ ] Domain Invariants Tested
```

### 62.4 Database

```text
[ ] Local Database Implemented
[ ] Backend Database Implemented
[ ] Entity Relationships Implemented
[ ] Constraints Implemented
[ ] Indexes Implemented
[ ] Transactions Implemented
[ ] Database Migrations Implemented
```

### 62.5 Expense Management

```text
[ ] Expense Creation Implemented
[ ] Expense Update Implemented
[ ] Expense Deletion Implemented
[ ] Expense Splits Implemented
[ ] Split Validation Implemented
[ ] Settlement Implemented
[ ] Balance Calculation Implemented
```

### 62.6 Groups and Membership

```text
[ ] Group Creation Implemented
[ ] Group Management Implemented
[ ] Membership Implemented
[ ] Membership Validation Implemented
[ ] Membership Authorization Implemented
```

### 62.7 Authentication and Authorization

```text
[ ] Authentication Implemented
[ ] Session Handling Implemented
[ ] Device Identity Implemented
[ ] Authorization Implemented
[ ] Device Revocation Implemented
```

### 62.8 Backend API

```text
[ ] API Endpoints Implemented
[ ] Request DTOs Implemented
[ ] Response DTOs Implemented
[ ] Validation Implemented
[ ] Error Handling Implemented
[ ] API Versioning Implemented
```

### 62.9 Android

```text
[ ] Data Layer Implemented
[ ] Repository Layer Implemented
[ ] Application Services Integrated
[ ] Presentation Layer Implemented
[ ] Offline-First Behavior Implemented
[ ] Background Processing Implemented
```

### 62.10 Synchronization

```text
[ ] SyncOperation Implemented
[ ] SyncState Implemented
[ ] Sync Engine Implemented
[ ] Push Implemented
[ ] Pull Implemented
[ ] Cursor Handling Implemented
[ ] Retry Implemented
[ ] Recovery Implemented
[ ] Idempotency Implemented
```

### 62.11 Conflict Handling

```text
[ ] Conflict Detection Implemented
[ ] Conflict Persistence Implemented
[ ] Conflict Resolution Implemented
[ ] Resolution Validation Implemented
[ ] Resolution Authorization Implemented
[ ] Conflict Convergence Tested
```

### 62.12 P2P

```text
[ ] P2P Discovery Implemented
[ ] P2P Connection Implemented
[ ] Handshake Implemented
[ ] Protocol Negotiation Implemented
[ ] Peer Authentication Implemented
[ ] Peer Authorization Implemented
[ ] P2P Push Implemented
[ ] P2P Pull Implemented
[ ] P2P Recovery Implemented
```

### 62.13 Security

```text
[ ] Authentication Security Tested
[ ] Authorization Security Tested
[ ] Device Identity Security Tested
[ ] Device Revocation Tested
[ ] Transport Security Verified
[ ] Secret Management Verified
[ ] Replay Protection Tested
[ ] Sensitive Logging Prevented
```

### 62.14 Testing

```text
[ ] Unit Tests Implemented
[ ] Integration Tests Implemented
[ ] Synchronization Tests Implemented
[ ] Conflict Tests Implemented
[ ] P2P Tests Implemented
[ ] End-to-End Tests Implemented
[ ] Security Tests Implemented
[ ] Performance Tests Implemented
[ ] Regression Tests Implemented
```

### 62.15 CI/CD

```text
[ ] CI Pipeline Implemented
[ ] Build Validation Implemented
[ ] Static Analysis Implemented
[ ] Automated Tests Integrated
[ ] Security Checks Integrated
[ ] Artifact Generation Implemented
[ ] Deployment Pipeline Implemented
```

### 62.16 Deployment

```text
[ ] Development Deployment Implemented
[ ] Test Deployment Implemented
[ ] Staging Deployment Implemented
[ ] Production Deployment Implemented
[ ] Migration Deployment Process Implemented
[ ] Rollback Strategy Defined
```

### 62.17 Monitoring

```text
[ ] Health Checks Implemented
[ ] Application Monitoring Implemented
[ ] Backend Monitoring Implemented
[ ] Database Monitoring Implemented
[ ] Sync Monitoring Implemented
[ ] P2P Monitoring Implemented
[ ] Security Monitoring Implemented
[ ] Alerts Implemented
```

### 62.18 Backup and Recovery

```text
[ ] Backup Strategy Implemented
[ ] Backup Scheduling Implemented
[ ] Backup Monitoring Implemented
[ ] Restore Procedure Implemented
[ ] Recovery Procedure Implemented
[ ] Recovery Testing Completed
[ ] RPO Defined
[ ] RTO Defined
```

### 62.19 Release

```text
[ ] Release Version Defined
[ ] Release Candidate Process Implemented
[ ] Release Checklist Completed
[ ] Release Notes Prepared
[ ] Compatibility Validated
[ ] Production Approval Obtained
[ ] Post-Release Validation Defined
```

### 62.20 Final Checklist

```text
[ ] All V1 Features Implemented
[ ] All Critical Business Rules Implemented
[ ] All Critical Tests Passing
[ ] Offline-First Behavior Verified
[ ] Backend Synchronization Verified
[ ] P2P Synchronization Verified
[ ] Conflict Handling Verified
[ ] Security Validation Completed
[ ] Performance Validation Completed
[ ] Migration Validation Completed
[ ] Staging Validation Completed
[ ] Monitoring Active
[ ] Backup Verified
[ ] Recovery Tested
[ ] Production Deployment Ready
```


## 63. V1 Completion Criteria

### 63.1 Purpose

This section defines the criteria that must be satisfied before SplitSync V1 is considered implementation-complete.

### 63.2 Functional Completion

All V1-defined functionality must be implemented and validated.

```text
Groups
Memberships
Expenses
Expense Splits
Settlements
Balances
Authentication
Authorization
Offline Operations
Synchronization
Conflict Handling
P2P Synchronization
```

### 63.3 Domain Completion

The Domain must satisfy all documented:

```text
Entities
Value Objects
Business Rules
Validation Rules
Invariants
```

### 63.4 Data Completion

Both local and Backend persistence must be implemented and validated.

```text
Schema
Relationships
Constraints
Transactions
Migrations
```

### 63.5 Offline-First Completion

The application must support:

```text
Local Reads
Local Writes
Offline Mutations
Pending SyncOperations
Application Restart
Network Recovery
```

without requiring continuous Backend connectivity.

### 63.6 Synchronization Completion

Synchronization must support:

```text
Push
Pull
SyncState
Cursor
Retry
Recovery
Idempotency
```

### 63.7 Conflict Completion

The system must support:

```text
Conflict Detection
Conflict Persistence
Conflict Resolution
Resolution Validation
Resolution Authorization
Convergence
```

### 63.8 P2P Completion

P2P functionality must support the V1-defined:

```text
Discovery
Connection
Handshake
Authentication
Authorization
Synchronization
Recovery
```

### 63.9 Security Completion

Security requirements must be validated for:

```text
Authentication
Authorization
Device Identity
Device Revocation
Transport
Secrets
Replay Protection
```

### 63.10 Testing Completion

Required automated tests must pass.

```text
Unit
Integration
Synchronization
Conflict
P2P
End-to-End
Security
Performance
```

### 63.11 Deployment Completion

The system must be deployable through:

```text
Development
Test
Staging
Production
```

using documented procedures.

### 63.12 Monitoring Completion

Production monitoring must be capable of detecting critical:

```text
Application Failures
Backend Failures
Database Failures
Synchronization Failures
P2P Failures
Security Failures
```

### 63.13 Recovery Completion

Backup and recovery procedures must be implemented and tested.

### 63.14 Documentation Completion

Required implementation documentation must be complete, including:

```text
Technical Design
Implementation Plan
API Documentation
Database Documentation
Deployment Documentation
Testing Documentation
Release Documentation
```

### 63.15 Compatibility Completion

The following must be compatible:

```text
Android
Backend
Database
API
Sync Protocol
P2P Protocol
```

### 63.16 Release Readiness

A release candidate must successfully pass:

```text
Test Environment
Staging Environment
Regression Suite
Security Validation
Synchronization Validation
Production Readiness Validation
```

### 63.17 V1 Sign-Off

V1 may be marked complete only after the required stakeholders approve:

```text
Functional Readiness
Technical Readiness
Security Readiness
Operational Readiness
Release Readiness
```

### 63.18 V1 Completion State

The final state is:

```text
Implementation
      ↓
Validation
      ↓
Testing
      ↓
Staging
      ↓
Approval
      ↓
V1 Ready
```

### 63.19 V1 Completion Criteria

V1 is complete when:

```text
[ ] Functional Requirements Implemented
[ ] Domain Rules Implemented
[ ] Database Implemented
[ ] Android Implementation Complete
[ ] Backend Implementation Complete
[ ] Offline-First Implementation Complete
[ ] Synchronization Complete
[ ] Conflict Handling Complete
[ ] P2P Complete
[ ] Security Complete
[ ] Testing Complete
[ ] CI/CD Complete
[ ] Deployment Complete
[ ] Monitoring Complete
[ ] Backup and Recovery Complete
[ ] Documentation Complete
[ ] Staging Validation Passed
[ ] Release Candidate Approved
[ ] Production Readiness Confirmed
```


## 64. Future Implementation Extensions

### 64.1 Purpose

This section identifies implementation areas that may be added after SplitSync V1.

Future extensions must not compromise the V1 architecture or existing financial/synchronization correctness.

### 64.2 Extension Principle

Future features should build on the existing architecture rather than introducing unrelated parallel implementations.

```text
V1 Foundation
      ↓
Extension
      ↓
New Capability
```

### 64.3 Advanced Synchronization

Future versions may extend synchronization with:

```text
More Efficient Batching
Advanced Delta Synchronization
Improved Conflict Strategies
Synchronization Optimization
```

### 64.4 Advanced Conflict Resolution

Future versions may support:

```text
More Resolution Strategies
User-Assisted Resolution
Conflict History
Advanced Merge Rules
```

### 64.5 Enhanced P2P

Future P2P extensions may include:

```text
Improved Discovery
Multi-Peer Synchronization
Peer Relay
Advanced Peer Management
Improved Background P2P
```

### 64.6 Multi-Device Improvements

Future versions may improve:

```text
Device Management
Device Trust
Device Revocation
Device Synchronization
```

### 64.7 Performance Extensions

Future optimization areas may include:

```text
Database Optimization
Sync Batching Optimization
Memory Optimization
Network Optimization
P2P Transfer Optimization
```

### 64.8 Scalability Extensions

Future versions may support larger:

```text
Groups
Memberships
Expense Histories
Synchronization Queues
Device Counts
```

### 64.9 Backend Scaling

Potential future infrastructure extensions include:

```text
Horizontal Scaling
Database Scaling
Caching
Queue-Based Processing
Load Distribution
```

### 64.10 Advanced Observability

Future versions may add:

```text
Advanced Tracing
Detailed Sync Diagnostics
Performance Analytics
Operational Dashboards
Automated Anomaly Detection
```

### 64.11 Advanced Security

Potential future security extensions include:

```text
Stronger Device Trust
Additional Authentication Factors
Advanced Key Rotation
Enhanced Threat Detection
```

### 64.12 Data Export

Future versions may support:

```text
Expense Export
Settlement Export
Balance Export
Group Data Export
```

### 64.13 Data Import

Future versions may support importing data from compatible external formats.

### 64.14 Reporting Extensions

Future versions may introduce:

```text
Expense Reports
Group Reports
Settlement Reports
Spending Analytics
```

### 64.15 UI Extensions

Future UI improvements may include:

```text
Advanced Filtering
Search
Sorting
Visualization
Improved Expense Entry
Improved Settlement Workflows
```

### 64.16 Platform Extensions

The architecture may later support additional clients/platforms where required.

```text
Android
   ↓
Additional Client
```

Such extensions must continue to use the defined Backend/API/Synchronization contracts.

### 64.17 API Extensions

Future API versions may introduce new capabilities while preserving supported V1 compatibility where required.

### 64.18 Database Extensions

Future schema changes must continue to use explicit versioned migrations.

### 64.19 Synchronization Protocol Extensions

Future protocol changes must use explicit protocol versioning.

```text
V1 Protocol
      ↓
Future Protocol Version
```

### 64.20 Backward Compatibility

Future extensions must define compatibility behavior for existing:

```text
Android Clients
Backend
Database
Sync Protocol
P2P Peers
```

### 64.21 Feature Flags

Future functionality may be introduced using feature flags where appropriate.

Feature flags must not bypass:

```text
Authorization
Validation
Synchronization Integrity
Security
```

### 64.22 Migration Strategy

Future extensions requiring schema changes must provide:

```text
Migration
Validation
Rollback/Recovery Strategy
Compatibility Plan
```

### 64.23 Testing Future Extensions

Every future extension must add appropriate:

```text
Unit Tests
Integration Tests
Synchronization Tests
Security Tests
End-to-End Tests
```

where applicable.

### 64.24 Future Extension Approval

A future extension should be evaluated for:

```text
Business Value
Architecture Impact
Security Impact
Data Impact
Synchronization Impact
Performance Impact
Operational Impact
```

### 64.25 Future Extension Completion

A future feature must not be considered complete until:

```text
Implementation
+
Validation
+
Testing
+
Documentation
+
Deployment Readiness
```

are completed.

### 64.26 V1 Boundary

Future extensions must remain outside the V1 scope unless explicitly approved as part of V1.

### 64.27 Future Extension Invariants

The following rules are mandatory:

- Future extensions must preserve V1 financial correctness.
- Future extensions must preserve existing Domain invariants.
- Future extensions must not silently break supported API clients.
- Database changes must use explicit migrations.
- Synchronization protocol changes must be versioned.
- P2P protocol changes must support explicit compatibility handling.
- Security controls must not be bypassed by new features.
- New synchronization behavior must preserve idempotency and convergence.
- Future extensions must include appropriate automated tests.
- V1 behavior must remain stable unless a deliberate breaking change is approved.