# SkillBridge – Job Application Portal

## Overview

SkillBridge is a web-based job application platform developed with Spring Boot and Thymeleaf. The application connects job candidates with recruiters through a simple and intuitive interface.

Candidates can browse available job offers, apply for positions, manage their applications, and maintain a personal skill portfolio.

Recruiters can create and manage job offers, review incoming applications, and evaluate candidates.

The project was developed as an Individual Project Assignment for the Spring Fundamentals Course at SoftUni.

---

# Technology Stack

## Backend

* Java 17
* Spring Boot 3.4.0
* Spring MVC
* Spring Data JPA
* Thymeleaf
* Hibernate

## Database

* Relational Database (MySQL)

## Frontend

* HTML5
* CSS3
* Bootstrap 5
* Thymeleaf Templates

## Build Tool

* Maven

## Version Control

* Git
* GitHub

---

# User Roles

The application supports two user roles.

## Candidate

Candidates can:

* Register and log into the platform
* Browse available job offers
* View job details
* Submit job applications
* Edit motivation letters
* Withdraw applications
* Manage personal skills
* View profile information

## Recruiter

Recruiters can:

* Register and log into the platform
* Create job offers
* Edit job offers
* Delete job offers
* View all created job offers
* Review incoming applications
* Accept applications
* Reject applications
* View candidate information

---

# Database Model

The system contains four main domain entities.

## User

Represents a registered system user.

### Properties

* UUID id
* String username
* String email
* String password
* String firstName
* String lastName
* UserRole role
* LocalDateTime createdAt

### Relationships

* One User can create many Job Offers
* One User can submit many Applications
* One User can have many Skills

---

## JobOffer

Represents a published job opportunity.

### Properties

* UUID id
* String title
* String company
* String location
* String description
* BigDecimal salary
* EmploymentType employmentType
* LocalDate deadline
* LocalDateTime createdAt

### Relationships

* Many Job Offers belong to one Recruiter
* One Job Offer can receive many Applications

---

## JobApplication

Represents a candidate's application for a job offer.

### Properties

* UUID id
* String motivationLetter
* ApplicationStatus status
* LocalDateTime appliedAt

### Relationships

* Many Applications belong to one Candidate
* Many Applications belong to one Job Offer

---

## Skill

Represents a professional skill owned by a candidate.

### Properties

* UUID id
* String name
* ProficiencyLevel proficiencyLevel

### Relationships

* Many Skills belong to one User

---

# Entity Relationship Diagram

User
│
├── JobOffer
│       │
│       └── JobApplication
│
├── JobApplication
│
└── Skill

---

# Enumerations

## UserRole

* CANDIDATE
* RECRUITER

## EmploymentType

* FULL_TIME
* PART_TIME
* CONTRACT
* INTERNSHIP
* REMOTE

## ApplicationStatus

* PENDING
* ACCEPTED
* REJECTED
* WITHDRAWN

## ProficiencyLevel

* BEGINNER
* INTERMEDIATE
* ADVANCED
* EXPERT

---

# Functionalities

The application implements four primary domain functionalities.

---

## Functionality 1: Job Offer Management

### Accessible by

Recruiters

### Description

Recruiters can manage job postings.

### Operations

* Create Job Offer
* View Job Offer
* Edit Job Offer
* Delete Job Offer

### Result

The created or updated job offer becomes visible in the job listings page.

---

## Functionality 2: Job Application Management

### Accessible by

Candidates

### Description

Candidates can apply for available jobs.

### Operations

* Create Application
* View Application
* Edit Motivation Letter
* Withdraw Application

### Result

Applications are stored in the system and become visible to recruiters.

---

## Functionality 3: Application Review

### Accessible by

Recruiters

### Description

Recruiters can review incoming applications.

### Operations

* View Application Details
* Accept Application
* Reject Application
* Change Application Status

### Result

Candidates receive updated application status information.

---

## Functionality 4: Skill Management

### Accessible by

Candidates

### Description

Candidates can maintain their personal skill portfolio.

### Operations

* Create Skill
* View Skill
* Edit Skill
* Delete Skill

### Result

Skills become visible in the user's profile and application details.

---

# Pages

## Public Pages

### Home Page

Landing page presenting the platform and its features.

### Register Page

Allows new users to create accounts.

### Login Page

Allows existing users to authenticate.

---

## Protected Pages

### Dashboard

Displays role-specific statistics and information.

### Job Listings

Displays all available job offers.

### Job Details

Displays detailed information about a selected job.

### Create Job Offer

Form used by recruiters to create new jobs.

### Edit Job Offer

Form used by recruiters to modify existing jobs.

### Applications

Displays user applications or recruiter reviews.

### Application Details

Displays detailed information about an application.

### Skills

Displays all skills associated with a candidate.

### Profile

Displays user profile information.

### Error Page

Displays application errors.

---

# Validation

The application performs server-side validation for all forms.

## Registration Validation

### Username

* Required
* Minimum 3 characters
* Maximum 20 characters

### Email

* Required
* Valid email format

### Password

* Required
* Minimum 6 characters
* Maximum 50 characters

### First Name

* Required
* Minimum 2 characters
* Maximum 30 characters

### Last Name

* Required
* Minimum 2 characters
* Maximum 30 characters

---

## Job Offer Validation

### Title

* Required
* 5–100 characters

### Company

* Required
* 2–100 characters

### Location

* Required
* 2–100 characters

### Description

* Required
* 50–5000 characters

### Salary

* Positive value only

### Deadline

* Must be a future date

---

## Application Validation

### Motivation Letter

* Required
* 50–1000 characters

---

## Skill Validation

### Skill Name

* Required
* 2–50 characters

---

# Security

The application implements session-based authentication.

## Access Control

### Guests

Can access:

* Home Page
* Register Page
* Login Page

### Authenticated Users

Can access:

* Dashboard
* Profile
* Jobs
* Applications
* Skills

### Role Restrictions

Candidates:

* Apply for jobs
* Manage skills
* Manage applications

Recruiters:

* Create jobs
* Edit jobs
* Delete jobs
* Review applications

---

# Project Structure

app

├── config

├── controller

├── service

├── repository

├── model

│   ├── entity

│   ├── dto

│   └── enums

├── validation

├── exception

├── security

└── util

---

# Future Improvements

Possible future enhancements include:

* Email notifications
* Advanced job search filters
* Resume uploads
* Profile pictures
* Job categories
* Candidate recommendations
* Recruiter company profiles

---

# Author

Developed as a Spring Fundamentals Course Project at SoftUni.
