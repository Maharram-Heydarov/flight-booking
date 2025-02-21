package az.edu.turing.dao.impl.memory;

import az.edu.turing.domain.dao.impl.memory.FlightInMemoryDao;
import az.edu.turing.domain.entities.FlightEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlightInMemoryDaoTest {
    private FlightInMemoryDao flightDao;

    @BeforeEach
    void setup() {
        flightDao = new FlightInMemoryDao();
        flightDao.save(new FlightEntity(1L, "Azerbaijan", "NYC", LocalDateTime.now().plusHours(5), 100));
        flightDao.save(new FlightEntity(2L, "Turkey", "London", LocalDateTime.now().plusHours(10), 150));
        flightDao.save(new FlightEntity(3L, "France", "Berlin", LocalDateTime.now().plusDays(2), 120));
    }

    @Test
    public void testSaveFlights() {
        assertTrue(flightDao.getById(3L).isPresent());
    }

    @Test
    public void testGetById() {
        Optional<FlightEntity> result = flightDao.getById(2L);
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getFlightId());
    }

    @Test
    public void testGetAll() {
        assertEquals(4, flightDao.getAll().size());
    }

    @Test
    public void testDeleteById() {
        boolean deleted = flightDao.deleteById(2L);
        assertEquals(3, flightDao.getAll().size());
    }

    @Test
    public void testUpdateAvailableSeats() {
        FlightEntity updatedFlight = flightDao.updateAvailableSeats(2L, 40);
        assertEquals(40, updatedFlight.getAvailableSeats());
    }

    @Test
    public void testGetAllFlightsWithin24Hours() {
        List<FlightEntity> flightsWithin24Hours = flightDao.getAllFlightsWithin24Hours();
        assertEquals(3, flightsWithin24Hours.size());
        LocalDateTime now = LocalDateTime.now();
        for (FlightEntity flight : flightsWithin24Hours) {
            LocalDateTime departureTime = flight.getDepartureTime();
            assertTrue(departureTime.isAfter(now) && departureTime.isBefore(now.plusHours(24)));
        }
    }

}
