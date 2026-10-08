# 🚆 Train Ticket Booking System (Spring Boot REST API)

A backend application where you can **view trains, book a seat, look up your bookings, and cancel a ticket with an automatic refund**. It is built with Java, Spring Boot and MySQL, and you test it with Postman (no website or app screen needed).

This is the Spring Boot version of my earlier **core Java console project**. The same idea (trains, seats, refunds) is rebuilt as a real REST API with a database.

---

## 💡 What can it do?

| Feature | What it means |
|---|---|
| **View trains** | See all trains or one train by id |
| **Book a ticket** | Pick a train, date and seat. The server copies the real fare from the train, so the price can't be faked |
| **No double booking** | If the same seat on the same train and date is already booked, you get an error |
| **See bookings** | List all bookings, one booking, or all bookings of one phone number |
| **Cancel a ticket** | Cancel with your booking id and phone number, and the refund is calculated automatically |

**Refund rule:** if the travel date is **2 or more days away → 75% refund**, otherwise **50% refund**. (₹650 ticket → ₹487 or ₹325. Decimals are rounded down.)

---

## 🛠️ Tech stack

- **Java 17**
- **Spring Boot 4.1.1** (Spring Web, Spring Data JPA)
- **Hibernate** (turns Java classes into MySQL tables)
- **MySQL 8**
- **Maven**
- **Postman** (for testing)

---

## 🔄 How it works (the flow)

Every request follows the same path, like an office with different desks:

```
 Postman  ──►  Controller  ──►  Service  ──►  Repository  ──►  MySQL
 (you)        (receptionist)    (the brain)    (database clerk)   (storage)
   ▲                                                                 │
   └─────────────  JSON response + status code  ◄───────────────────┘
```

| Layer | Job | Example |
|---|---|---|
| **Controller** | Receives the request, decides the HTTP status (200, 201, 404, 409) | `BookingController` |
| **Service** | Business rules: duplicate check, fare, refund calculation | `BookingService` |
| **Repository** | Talks to MySQL without writing SQL | `BookingRepository` |
| **Entity** | A Java class that becomes a table | `Train`, `Booking` |

### Example: cancelling a ticket
`POST /cancelbooking/1` with `{ "phone": "9876543210" }`

1. The **Controller** takes `1` from the URL and the phone from the body.
2. The **Service** finds booking 1, checks the phone matches, and checks it isn't already cancelled.
3. It counts the days left, picks 75% or 50%, sets the status to `CANCELLED` and stores the refund.
4. The **Repository** saves it (an UPDATE in MySQL).
5. You get back `200 OK` with the updated booking.

---

## 📁 Project structure

```
src/main/java/com/example/
├── Train.java                       → Train table (number, name, route, seats, fare)
├── TrainRepository.java
├── TrainService.java
├── TrainController.java             → /gettrains
├── DataSeeder.java                  → inserts the 3 trains when the app starts
├── Booking.java                     → Booking table (linked to a Train)
├── BookingRepository.java
├── BookingService.java              → duplicate check, fare, refund logic
├── BookingController.java           → /createbooking, /getbookings, /cancelbooking
└── TrainTicketBookingApplication.java
```

---

## ▶️ How to run it

**You need:** JDK 17, MySQL running on port 3306, and Maven (or the included `mvnw`).

1. **Clone the project**
```
   git clone https://github.com/sumitkumar93041/train-ticket-booking-springboot.git
```
2. **Set up the config file.** Go to `src/main/resources/`, copy `application.properties.example` and rename the copy to `application.properties`. Then put your own MySQL password in it:
```
   spring.datasource.password=your_password_here
```
   (The real file is git-ignored on purpose, so passwords never get uploaded.)
3. **Create the database** in MySQL:
```sql
   CREATE DATABASE trainbooking;
```
4. **Run** `TrainTicketBookingApplication` from your IDE, or use `./mvnw spring-boot:run`.

On startup Hibernate creates the tables automatically, and 3 trains are added (Karnataka Express, Mysuru Shatabdi, Coastal Superfast). The app runs at `http://localhost:8080`.

---

## 🔌 API endpoints

| Action | Method | URL |
|---|---|---|
| All trains | GET | `/gettrains` |
| One train | GET | `/gettrains/{id}` |
| Book a ticket | POST | `/createbooking` |
| All bookings | GET | `/getbookings` |
| Bookings by phone | GET | `/getbookings?phone=9876543210` |
| One booking | GET | `/getbookings/{id}` |
| Cancel a booking | POST | `/cancelbooking/{id}` |

### Book a ticket: `POST /createbooking`
```json
{
  "train": {
    "id": 1,
    "trainNumber": "T101",
    "name": "Karnataka Express",
    "source": "Bengaluru",
    "destination": "Mangaluru",
    "totalSeats": 20,
    "fare": 650
  },
  "travelDate": "2026-10-10",
  "seatNumber": 5,
  "passengerName": "Ravi Kumar",
  "age": 21,
  "phone": "9876543210"
}
```
Only `train.id` is actually used. The server looks up the real train in the database and copies its real fare.
**Response:** `201 Created` with `"status": "BOOKED"` and `"fare": 650`.

### Cancel a ticket: `POST /cancelbooking/1`
```json
{ "phone": "9876543210" }
```
**Response:** `200 OK` with `"status": "CANCELLED"` and `"refundAmount": 487`.

### What the API says when something goes wrong

| Situation | Status | Message |
|---|---|---|
| Seat already booked for that train and date | **409** | seat already booked |
| Train id doesn't exist | **409** | Train not found |
| Cancel with wrong booking id **or** wrong phone | **404** | Booking not found |
| Cancel a booking that is already cancelled | **409** | Booking is already cancelled |

The wrong id and the wrong phone give the **same** message on purpose, so nobody can guess which booking ids exist.

---

## 🧪 How I tested it

I tested every endpoint in Postman and checked the data in MySQL Workbench:
- Booked a seat, then booked the same seat again → `409`
- Booked the same seat on a different date → allowed
- Cancelled with more than 2 days left → refund **487** (75%)
- Cancelled with less than 2 days left → refund **325** (50%)
- Cancelled twice → `409`; wrong phone or fake id → `404`
- After a cancellation, the seat can be booked again

---

## 🧠 Design decisions

- **Cancelled bookings are kept, not deleted.** They stay as history, and the seat check only looks at `BOOKED` rows, so a cancelled seat becomes free again.
- **The server decides the fare**, not the client.
- **`ResponseStatusException`** carries both the status code and the message from the Service up to the Controller.

---

## 🚧 Known limitations and future work

- **Hold → Pay → Confirm flow** is not built yet (my core Java version already has it).
- The seat check happens before saving and is not atomic, so two requests at the exact same moment could both pass. A database-level constraint or a transaction is the proper fix.
- No login or Spring Security. The phone number is checked manually.
- `GET /gettrains/{id}` and `GET /getbookings/{id}` return an empty `200` for an unknown id instead of `404`.
- Booking status is a plain String; an enum would be safer.

---

## 👤 Author

**Sumit Kumar**, B.E. Computer Science & Engineering student, SJB Institute of Technology
GitHub: [sumitkumar93041](https://github.com/sumitkumar93041)
