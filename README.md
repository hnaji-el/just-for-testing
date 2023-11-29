# Software Requirements Specification (SRS) for Laboratory Information Management System (LIMS)

## Introduction

The Laboratory Information Management System (LIMS) is a web-based platform designed to streamline sample management, communication, and resource allocation within laboratory environments. It integrates features such as sample tracking, reservation management, user access control, and progress monitoring for efficient laboratory operations. The system employs QR codes to encode basic sample information for easy identification and tracking.

## Functional Requirements

1. **User Authentication and Access Control:**
    - The system should support user authentication for administrators, platform managers, operators, and clients.
    - Different user roles should have distinct access privileges as defined by the administrator.
  
2. **Reservation Management:**
    - Clients should be able to make reservations through the web interface.
    - Once a reservation time is 8 hours or less from the current time, modification of the reservation should be restricted.

3. **Resource Management:**
    - The system should display available resources, including equipment and operations, for users.
    - A calendar view should indicate the free time of resources for scheduling purposes.
  
4. **Sample Management:**
    - Each sample should be associated with a QR code containing basic information.
    - Operators should be able to register new samples and track their progress within the system.

5. **Progress Monitoring:**
    - A progress bar should display the estimated time remaining for each operation on a sample.
    - Sound notifications should alert the operator if the estimated time for an operation has elapsed.

6. **Alerts and Notifications:**
    - Platform managers should be notified of any overdue operations or samples after a predefined time limit.
    - Clients should receive notifications regarding the status of their samples operations results and reservations.

7. **User Management:**
    - Administrators should have the authority to create and manage user accounts for all roles.
    - Platform managers should be able to create and assign resources and operators within their divisions.

## Non-Functional Requirements

1. **Scalability:**
    - The system should be scalable to accommodate a growing number of users and samples.

2. **Security:**
    - Robust security measures should be implemented to safeguard sensitive data and prevent unauthorized access.

3. **Performance:**
    - The system should be responsive and capable of handling simultaneous user requests without significant delays.

4. **User Interface:**
    - The user interface should be intuitive and user-friendly, providing easy navigation for all user roles.

5. **Customization:**
    - The system should allow for customization of notification settings, time limits, and other configurable parameters.

## Constraints

- The system should be compatible with modern web browsers and accessible across different devices.
- Data backup and recovery mechanisms should be in place to prevent data loss or corruption.

## Assumptions

- Users have a basic understanding of operating web-based systems.

## Glossary

- LIMS: Laboratory Information Management System
- QR Code: Quick Response Code, a type of matrix barcode for storing information
