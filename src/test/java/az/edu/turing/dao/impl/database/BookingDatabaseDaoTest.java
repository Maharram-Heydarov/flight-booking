package az.edu.turing.dao.impl.database;

import az.edu.turing.domain.dao.impl.database.BookingDatabaseDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.FlightEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static az.edu.turing.config.DatabaseConfig.getConnection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class BookingDatabaseDaoTest {
    private static BookingDatabaseDao bookingDao;


    @BeforeAll
    static void setupDatabase() {
        bookingDao = new BookingDatabaseDao();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {


            statement.execute("""
                            CREATE TABLE IF NOT EXISTS flights (
                                flight_id SERIAL PRIMARY KEY,
                                destination VARCHAR(100),
                                from_location VARCHAR(100),
                                departure_time TIMESTAMP,
                                available_seats INTEGER
                            );
                    """);


            statement.execute("""
                            CREATE TABLE IF NOT EXISTS bookings (
                                booking_id SERIAL PRIMARY KEY,
                                booker_name VARCHAR(100) NOT NULL,
                                booker_surname VARCHAR(100) NOT NULL,
                                flight_id INTEGER NOT NULL,
                                CONSTRAINT bookings_flight_id_fkey FOREIGN KEY (flight_id)
                                    REFERENCES flights(flight_id) ON DELETE CASCADE
                            );
                    """);

        } catch (Exception e) {
            fail("Failed to set up database: " + e.getMessage());
        }
    }

    @BeforeEach
    public void setUp() throws SQLException {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM bookings;");
            statement.executeUpdate("DELETE FROM flights;");
        }
    }

    @Test
    public void testGetAll() throws SQLException {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) " +
                    "VALUES (1, 'New York', 'London', NOW(), 100);");
            statement.executeUpdate("INSERT INTO bookings (booker_name, booker_surname, flight_id) " +
                    "VALUES ('John', 'Doe', 1);");
        }
        var bookings = bookingDao.getAll();
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals("John", bookings.get(0).getBookerName());
        assertEquals("Doe", bookings.get(0).getBookerSurname());
    }

    @Test
    public void testGetById() throws SQLException {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) " +
                    "VALUES (2, 'Paris', 'Berlin', NOW(), 100);");
            statement.executeUpdate("INSERT INTO bookings (booker_name, booker_surname, flight_id) " +
                    "VALUES ('Alice', 'Smith', 2);");
        }
        Optional<BookingEntity> booking = bookingDao.getById(2L);

        assertTrue(booking.isPresent());
    }

    @Test
    public void testSave() throws SQLException {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) " +
                    "VALUES (3, 'Los Angeles', 'Tokyo', NOW(), 100);");
        }
        BookingEntity newBooking = new BookingEntity();
        newBooking.setBookerName("James");
        newBooking.setBookerSurname("Brown");
        newBooking.setFlight(new FlightEntity(3L, "Los Angeles", "Tokyo", null, 100));

        BookingEntity savedBooking = bookingDao.save(newBooking);
        assertNotNull(savedBooking);
        assertEquals("James", savedBooking.getBookerName());
        assertEquals("Brown", savedBooking.getBookerSurname());
    }

    @Test
    public void testDeleteById() throws SQLException {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) " +
                    "VALUES (4, 'Berlin', 'New York', NOW(), 100);");
            statement.executeUpdate("INSERT INTO bookings (booker_name, booker_surname, flight_id) " +
                    "VALUES ('Maria', 'Garcia', 4);");
        }
        boolean isDeleted = bookingDao.deleteById(4L);
        assertEquals(true, isDeleted);
        Optional<BookingEntity> booking = bookingDao.getById(4L);
        assertFalse(booking.isPresent());
    }
    }


