# SmartPark-MS
A Smart City Parking Management System


## Setup 
### Backend: Database
1. Clone the repository
2. Install the dependencies
    - Java 23
    - MySQL
    Check if you have Java and MySQL installed by running the following commands:
    ```bash
    java --version
    ```
    ```bash
    mysql --version
    ```
3. Create a database in MySQL
    ```sql
    CREATE DATABASE parkdb;
    ```
4. Create the user for the database
    ```sql
    CREATE USER 'park_admin'@'localhost' IDENTIFIED BY 'Admin@123';
    ```
5. Grant the user all privileges on the database
    ```sql
    GRANT ALL PRIVILEGES ON parkdb.* TO 'park_admin'@'localhost';
    ```
6. Apply changes to the database
    ```sql
    FLUSH PRIVILEGES;
    ``` 
7. For maven 
    ```bash
    mvn clean install
    ```
