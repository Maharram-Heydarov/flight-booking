# Plane Ticket Booking Console Application

## 📌 Project Overview
This is a Java console application for booking plane tickets from Kiev. The application allows users to search, book, and manage flight reservations in an interactive console-based interface. The system follows a structured three-layer architecture (Controller, Service, DAO) and ensures data persistence through file storage.

## 🚀 Features
- **Online-board**: View available flights from Kiev within the next 24 hours.
- **Flight Information**: Retrieve details of a specific flight by entering its ID.
- **Search & Book a Flight**: Find flights based on destination, date, and number of passengers, then proceed with booking.
- **Cancel Booking**: Cancel an existing flight reservation by entering the booking ID.
- **My Flights**: Retrieve all bookings associated with a passenger's full name.
- **Exit**: Close the application while saving all changes.

## 🏷️ Architecture
The project follows the standard three-layer architecture:
- **Controller Layer**: Handles user input and directs requests to the service layer.
- **Service Layer**: Implements business logic and interacts with the DAO layer.
- **DAO Layer**: Manages data persistence using text/binary files.

## 💁‍♂️ Project Structure
```
flight-booking/
├── src/
│   ├── main/
│   │   ├── java/az/edu/turing/
│   │   │   ├── config/           # Konfiqurasiya sinifləri
│   │   │   ├── controller/       # REST API kontrollerləri
│   │   │   ├── domain/           # Entity sinifləri
│   │   │   ├── exception/        # Custom istisnalar (exceptions)
│   │   │   ├── mapper/           # Entity-DTO çevirmə sinifləri
│   │   │   ├── model/dto/        # Data Transfer Object (DTO) modelləri
│   │   │   ├── service/          # Business logic sinifləri
│   │   │   ├── util/             # Utility funksiyalar
│   │   │   ├── BookingApp.java   # Application giriş nöqtəsi
│   │   ├── resources/Flights/    # Uçuşlarla bağlı məlumatlar
│   ├── test/
│   │   ├── java/az/edu/turing/   # Unit və integration testlər
├── .gitignore                    # Git üçün ignore qaydaları
├── README.md                      # Layihə haqqında sənəd
├── docker-compose.yml             # Docker konfiqurasiya faylı
├── pom.xml                        # Maven bağımlılıqları və konfiqurasiyası
```

## ⚙️ Technologies Used
- **Java 8+**
- **JUnit** (for unit testing)
- **Git/GitHub** (for version control)
- **Text/Binary Files** (for data persistence)

## 🔧 Setup & Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/plane-ticket-booking.git
   ```
2. Open the project in IntelliJ IDEA or any Java IDE.
3. Compile and run the application:
   ```sh
   javac -d bin src/main/java/*.java
   java -cp bin Main
   ```

## 🛠️ Usage Instructions
1. Run the application and choose from the available menu options.
2. Follow the prompts to view flights, book tickets, or manage reservations.
3. All data is automatically saved when exiting the application.

## 📄 Contribution Guidelines
- Use a separate branch for every feature or bug fix.
- Follow the pull request workflow before merging into `master`.
- Ensure proper code review before merging any changes.
- Write unit tests for new features.

## 📝 License
This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

