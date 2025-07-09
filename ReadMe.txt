# Faculty’s Office Hours Manager

A user-friendly **JavaFX desktop application** designed to help faculty efficiently schedule and manage office hours using an SQLite database. Built with the **Model-View-Controller (MVC)** architecture, this project enables defining semesters, managing time slots, setting up courses, and booking appointments.

---

## 📌 Objective

This application serves as a tool for faculty to manage their office hours by offering a streamlined UI built with **FXML** and **JavaFX**, storing data persistently in an **SQLite** database. The program leverages **MVC architecture**, ensuring a clean separation between UI, business logic, and data.

---

## 🗂️ Technologies Used

- **JavaFX** for GUI  
- **FXML** for UI layout  
- **SQLite** for local database  
- **JDBC** for database connectivity  
- **UML Diagrams** for design representation

---

## 🧠 System Architecture

### 📁 Packages
- `model` – Contains data classes and database models  
- `view` – FXML layouts and UI-related files  
- `controller` – Logic for handling user actions and data flow

### 🧱 Design Patterns
- **MVC (Model-View-Controller)** – For modular and maintainable structure  
- **Polymorphism** – Applied across entity handling

---

## 🛢️ Database

**Database File**: `semester_office_hours.db`  
**Database Type**: SQLite

### 🧾 Entities

1. `CourseRecord`  
2. `OfficeHoursScheduleRecord`  
3. `TimeSlotRecord`  
4. `DatabaseUtils`

---

## 🖼️ UML Diagrams

- **Class Diagram** – Overview of the class structure  
- **Polymorphism Diagram** – Demonstrates polymorphic design  
- **Sequence Diagrams** – Illustrate user workflows such as:
  - Booking office hours
  - Viewing time slots
  - Creating new courses

---

## 📚 References

- CS 151 Lecture Notes on Object-Oriented Design  
- Problem Statement v1 by Ahmad Yazdankhah  
- [JavaFX Documentation (Oracle)](https://openjfx.io/)  
- [SQLite JDBC Driver](https://github.com/xerial/sqlite-jdbc)  
- JavaFX Properties API Documentation

---

## 📌 Acronyms

| Abbreviation | Meaning                          |
|--------------|----------------------------------|
| UML          | Unified Modeling Language        |
| FXML         | XML-based UI Layout for JavaFX   |
| MVC          | Model-View-Controller            |
| SQL          | Structured Query Language        |
| JDBC         | Java Database Connectivity       |

---

## 🚀 Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/your-username/faculty-office-hours-manager.git
```  
2. Open in an IDE with JavaFX support (like IntelliJ or Eclipse)  
3. Run `Main.java`

