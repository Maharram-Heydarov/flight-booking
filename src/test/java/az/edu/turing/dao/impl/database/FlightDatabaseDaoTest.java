package az.edu.turing.dao.impl.database;


import az.edu.turing.config.DatabaseConfig;
import az.edu.turing.domain.dao.impl.database.FlightDatabaseDao;
import az.edu.turing.domain.entities.FlightEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static az.edu.turing.config.DatabaseConfig.getConnection;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FlightDatabaseDaoTest {
    private static FlightDatabaseDao flightDao;

    @BeforeAll
    static void setupDatabase() {
        flightDao = new FlightDatabaseDao();
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
    void testGetAll() throws SQLException {
        try(Connection connection= DatabaseConfig.getConnection()){
            Statement statement=connection.createStatement();
            statement.executeUpdate("INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) " +
                    "VALUES (1, 'New York', 'London', NOW(), 100);");
            statement.executeUpdate("INSERT INTO bookings (booker_name, booker_surname, flight_id) " +
                    "VALUES ('John', 'Doe', 1);");
        }
        List<FlightEntity> flights = flightDao.getAll();
        assertNotNull(flights);
        assertFalse(flights.isEmpty());
    }

    @Test
    void testGetById() throws SQLException {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) " +
                    "VALUES (2, 'Paris', 'Berlin', NOW(), 100);");
            statement.executeUpdate("INSERT INTO bookings (booker_name, booker_surname, flight_id) " +
                    "VALUES ('Alice', 'Smith', 2);");
        }
        Optional<FlightEntity> flight = flightDao.getById(2L);

        assertTrue(flight.isPresent());
        assertEquals("Paris", flight.get().getDestination());
    }

    @Test
    void testSave() {
        FlightEntity newFlight = new FlightEntity(1L, "Paris", "Berlin",
                LocalDateTime.now().plusDays(1), 100);
        FlightEntity savedFlight = flightDao.save(newFlight);

        assertNotNull(savedFlight);
        assertEquals("Paris", savedFlight.getDestination());
    }

    @Test
    void testDeleteById() throws SQLException {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) " +
                    "VALUES (3, 'Berlin', 'New York', NOW(), 100);");
            statement.executeUpdate("INSERT INTO bookings (booker_name, booker_surname, flight_id) " +
                    "VALUES ('Maria', 'Garcia', 3);");
        }
        boolean isDeleted = flightDao.deleteById(3L);
        assertTrue(isDeleted);
        Optional<FlightEntity> flight = flightDao.getById(3L);
        assertFalse(flight.isPresent());
    }

    @Test
    void testUpdateAvailableSeats() throws SQLException {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) " +
                    "VALUES (4, 'Berlin', 'New York', NOW(), 100);");
            statement.executeUpdate("INSERT INTO bookings (booker_name, booker_surname, flight_id) " +
                    "VALUES ('Maria', 'Garcia', 4);");
        }
        FlightEntity updatedFlight = flightDao.updateAvailableSeats(4L, 120);

        assertNotNull(updatedFlight);
        assertEquals(120, updatedFlight.getAvailableSeats());
    }

    @Test
    void testGetAllFlightsWithin24Hours() throws SQLException {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO flights (flight_id, destination, from_location, departure_time, available_seats) " +
                    "VALUES (3, 'Berlin', 'New York', NOW() + INTERVAL '1 hour', 100);");
            statement.executeUpdate("INSERT INTO bookings (booker_name, booker_surname, flight_id) " +
                    "VALUES ('Maria', 'Garcia', 3);");
        }
        List<FlightEntity> flights = flightDao.getAllFlightsWithin24Hours();
        assertNotNull(flights);
        assertFalse(flights.isEmpty());
    }
}
