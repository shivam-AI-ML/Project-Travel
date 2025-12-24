Travel Management System (Java + JDBC)

A Java Swing based Travel Management System that allows users to manage trips, customers, and bookings using a MySQL database.
This project uses JDBC without Maven, making it suitable for beginners and college submissions.

📌 Features

Add new trips

View all trips

Add customers

View customers

Create bookings

View bookings

Seat availability management

GUI built using Java Swing

🛠️ Technologies Used

Java (JDK 8+)

Java Swing (GUI)

MySQL

JDBC

MySQL Connector/J (JAR)

📂 Project Structure
Project-Travel/
│
├── TravelManagementGUI.java
├── DBConnection.java
├── lib/
│   └── mysql-connector-j-8.4.0.jar
└── README.md

🗄️ Database Setup (MySQL)

Create database and tables using the following SQL:

CREATE DATABASE travel_db;
USE travel_db;

CREATE TABLE trips (
    id INT AUTO_INCREMENT PRIMARY KEY,
    destination VARCHAR(100),
    trip_date DATE,
    price DOUBLE,
    seats INT
);

CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(15)
);

CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    trip_id INT,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (trip_id) REFERENCES trips(id)
);

🔌 Database Configuration

Update credentials in DBConnection.java if required:

DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/travel_db?useSSL=false&serverTimezone=UTC",
    "root",
    "toor"
);

▶️ How to Compile & Run (Without Maven)
1️⃣ Compile
javac -cp ".;lib/mysql-connector-j-8.4.0.jar" TravelManagementGUI.java

2️⃣ Run
java -cp ".;lib/mysql-connector-j-8.4.0.jar" TravelManagementGUI


⚠️ Important (Windows users)
Use ; in classpath
Linux/Mac users should use : instead.

❌ Common Error & Fix
Error:
Could not find or load main class java

Reason:

Wrong command used.

✅ Correct Command:
java -cp ".;lib/mysql-connector-j-8.4.0.jar" TravelManagementGUI

🖥️ GUI Preview

Buttons for all operations

Data displayed in text area

Popup dialogs for input

📌 Future Enhancements

Login system

Delete / update records

Better UI design

Input validation

Report generation

👨‍💻 Author

Shivam
📌 GitHub: https://github.com/shivam-AI-ML/Project-Travel

Text area displays all output

Dialog boxes are used for data input

Grid layout used for buttons
