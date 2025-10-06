# Pizza Management System

## Overview

A full-stack database application simulating pizza store operations with role-based access control. Built with Java JDBC and PostgreSQL to demonstrate enterprise database design and transaction management. 

## Features

### Customer Features

* User authentication and profile management
* Browse menu with filtering (by type, price)
* Place orders with multiple items
* View order history and order details

### Driver Features

* Update order delivery status
* View customer order information

### Manager Features

* Full menu management (CRUD operations)
* User role management
* System-wide order visibility

## Technical Architecture 

**Backend:** Java with JDBC

**Database:** PostgreSQL

**Key Concepts:** SQL queries, transactions, prepared statements, role-based access control

### Database Schema

* Users (authentication, roles)
* Items (menu inventory)
* Store (location)
* FoodOrder (order tracking)
* ItemsInOrder (order line items)

### Prerequisites

* Java 17+
* PostgreSQL 12+
* PostgreSQL JDBC Driver (postgresql-42.7.1.jar)

### Installation

```

bash
# Clone repository
git clone repo 

# Set up database 
psql -U postgres -p 5433
CREATE DATABASE pizzamanagemnt
\q 

psql -h localhost -p 5433 -U postgres -d pizzamanagement -f PizzaManagementSys/sql/src/create_tables.sql 

# Replace data path in the load_tables file
psql -h localhost -p 5433 -U postgres -d pizzamanagement -f PizzaManagementSys/sql/src/load_tables.sql 

psql -h localhost -p 5433 -U postgres -d pizzamanagement -f PizzaManagementSys/sql/src/create_indexes.sql

# Compile and run 
cd java/scripts
chmod +x compile.sh
./compile.sh 

```
