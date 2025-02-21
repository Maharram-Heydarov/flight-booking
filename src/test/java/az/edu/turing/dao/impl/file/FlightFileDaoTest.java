package az.edu.turing.dao.impl.file;

import az.edu.turing.domain.dao.impl.file.FlightFileDao;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.exception.FlightNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FlightFileDaoTest {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static Path tempDirectory;
    private FlightFileDao flightFileDao;

    @BeforeAll
    static void setUpClass() throws Exception {
        tempDirectory = Files.createTempDirectory("flights_test");
    }

    @BeforeEach
    void setUp() {
        System.setProperty("FLIGHTS_RESOURCE_PATH", tempDirectory.toString());
        flightFileDao = new FlightFileDao(objectMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        for (var file : tempDirectory.toFile().listFiles()) {
            file.delete();
        }
    }

    @AfterAll
    static void tearDownClass() throws Exception {
        Files.delete(tempDirectory);
    }

    @Test
    void testSaveAndGetById() {
        FlightEntity flight = new FlightEntity(1L, "Paris", "London",
                LocalDateTime.now().plusHours(5), 100);

        flightFileDao.save(flight);
        Optional<FlightEntity> retrievedFlight = flightFileDao.getById(1L);

        assertTrue(retrievedFlight.isPresent());
        assertEquals(flight, retrievedFlight.get());
    }

    @Test
    void testGetById_NotFound() {
        Optional<FlightEntity> retrievedFlight = flightFileDao.getById(999L);

        assertTrue(retrievedFlight.isEmpty());
    }

    @Test
    void testGetAll() {
        FlightEntity flight1 = new FlightEntity(1L, "Paris", "London",
                LocalDateTime.now().plusHours(5), 100);
        FlightEntity flight2 = new FlightEntity(2L, "Berlin", "Madrid",
                LocalDateTime.now().plusHours(10), 200);

        flightFileDao.save(flight1);
        flightFileDao.save(flight2);

        List<FlightEntity> allFlights = flightFileDao.getAll();

        assertEquals(3, allFlights.size());
        assertTrue(allFlights.contains(flight1));
        assertTrue(allFlights.contains(flight2));
    }

    @Test
    void testDeleteById() {
        FlightEntity flight = new FlightEntity(1L, "Paris", "London",
                LocalDateTime.now().plusHours(5), 100);
        flightFileDao.save(flight);

        boolean deleted = flightFileDao.deleteById(1L);

        assertTrue(deleted);
        assertTrue(flightFileDao.getById(1L).isEmpty());
    }

    @Test
    void testDeleteById_NotFound() {
        assertThrows(FlightNotFoundException.class, () -> flightFileDao.deleteById(999L));
    }

    @Test
    void testUpdateAvailableSeats() {
        FlightEntity flight = new FlightEntity(1L, "Paris", "London",
                LocalDateTime.now().plusHours(5), 100);
        flightFileDao.save(flight);

        FlightEntity updatedFlight = flightFileDao.updateAvailableSeats(1L, 80);

        assertEquals(80, updatedFlight.getAvailableSeats());
    }

    @Test
    void testUpdateAvailableSeats_FlightNotFound() {
        assertThrows(FlightNotFoundException.class, () -> flightFileDao.updateAvailableSeats(999L,
                80));
    }

    @Test
    void testGetAllFlightsWithin24Hours() {
        // Arrange
        FlightEntity flight1 = new FlightEntity(1L, "Paris", "London",
                LocalDateTime.now().plusHours(5), 100);
        FlightEntity flight2 = new FlightEntity(2L, "Berlin", "Madrid",
                LocalDateTime.now().plusDays(2), 200);
        FlightEntity flight3 = new FlightEntity(3L, "Rome", "Athens",
                LocalDateTime.now().minusHours(3), 150);

        flightFileDao.save(flight1);
        flightFileDao.save(flight2);
        flightFileDao.save(flight3);

        List<FlightEntity> flightsWithin24Hours = flightFileDao.getAllFlightsWithin24Hours();

        assertEquals(2, flightsWithin24Hours.size());
        assertTrue(flightsWithin24Hours.contains(flight1));
        assertTrue(flightsWithin24Hours.contains(flight3));
    }
}
