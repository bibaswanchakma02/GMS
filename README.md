# Gym Management System

## Overview

This project is a Gym Management System that allows users to register, login, view and edit their profiles, renew memberships, request trainers, and manage membership details. It includes backend functionality with Spring Boot and MongoDB, frontend integration with React.js, and payment handling through Razorpay. The front end using Reactjs is in a seperate repository - https://github.com/bibaswanchakma02/GMS-frontend

## Features

- User Registration and Login
- Profile Management (View and Edit)
- Membership Management
  - View Membership Details
  - Renew Membership
  - Payment Integration with Razorpay
- Trainer Management
  - Request for a Trainer
  - Admin Assignment of Trainers
- Admin functionalities for user and trainer management

## Technology Stack

- **Backend**: Spring Boot, MongoDB
- **Frontend**: React.js(still under development)
- **Payment Gateway**: Razorpay

## Prerequisites

- Java 11 or higher
- MongoDB
- Maven
- Razorpay account for payment integration

## Setup and Installation

### Backend Setup

1. **Clone the Repository**

   ```sh
   git clone https://github.com/bibaswanchakma02/GMS.git
   cd GMS
2. **Set up environment variables**
   Set up your own environment variables to use in the application.properties file. If you are using IntelliJ follow these steps -
   - Go to "Run" tab.
   - Go to Edit Conigurations
   - You will find option to add environment variables.
   
