package az.edu.turing.controller;

import az.edu.turing.domain.dao.impl.memory.FlightInMemoryDao;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.mapper.FlightMapper;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightControllerTest {
    private FlightController flightController;
    private FlightServiceImpl flightService;
    private FlightInMemoryDao flightInMemoryDao;
    private FlightMapper flightMapper;

    @BeforeEach
    void setUp() {
        flightInMemoryDao = new FlightInMemoryDao();
        flightMapper = new FlightMapper();
        flightService = new FlightServiceImpl(flightInMemoryDao, flightMapper);
        flightController = new FlightController(flightService);

        FlightEntity flight = new FlightEntity(
                101,
                "New York",
                "Kiev",
                LocalDateTime.now().plusHours(2),
                150
        );

        FlightEntity flight2 = new FlightEntity(
                102,
                "Turkey",
                "Kiev",
                LocalDateTime.now().plusHours(2),
                50
        );

        flightInMemoryDao.save(flight);
        flightInMemoryDao.save(flight2);
    }

    @AfterEach
    void tearDown() {
        flightInMemoryDao = null;
        flightMapper = null;
        flightService = null;
        flightController = null;
    }

    @Test
    void getFlightById() {
        FlightDto flightById = flightController.getFlightById(101);
        LocalDateTime expected = LocalDateTime.now().plusHours(2);
        LocalDateTime actual = flightById.getDepartureTime();
        assertNotNull(flightById);
        assertEquals(101, flightById.getFlightId());
        assertEquals("Kiev", flightById.getFrom());
        assertEquals("New York", flightById.getDestination());
        assertEquals(expected.getYear(), actual.getYear());
        assertEquals(expected.getMonthValue(), actual.getMonthValue());
        assertEquals(expected.getDayOfMonth(), actual.getDayOfMonth());
        assertEquals(expected.getHour(), actual.getHour());
        assertEquals(expected.getMinute(), actual.getMinute());
        assertEquals(expected.getSecond(), actual.getSecond());
        assertEquals(150, flightById.getAvailableSeats());
    }

    @Test
    void getAllFlights() {
        List<FlightDto> allFlights = flightController.getAllFlights();
        List<FlightDto> flightDtos = List.of(new FlightDto(101, "New York", "Kiev", LocalDateTime.now().plusHours(2), 150),
                new FlightDto(102, "Turkey", "Kiev", LocalDateTime.now().plusHours(2), 50)
        );

        assertNotNull(allFlights);
        assertEquals(flightDtos, allFlights);
    }

    @Test
    void findFlights() {
        List<FlightDto> flights = flightController.findFlights("New York", LocalDate.now(), 2);
        FlightDto flightById = flightController.getFlightById(101);

        assertNotNull(flights);
        assertEquals(flights.get(0), flightById);
        assertEquals(flights, List.of(flightById));
    }

    @Test
    void getAllFlightsWithin24Hours() {
        List<FlightDto> flights = flightService.getAllFlightsWithin24Hours();
        assertNotNull(flights);
        assertEquals(2, flights.size());
        assertTrue(flights.stream().anyMatch(flight -> flight.getFlightId() == 101));
        assertTrue(flights.stream().anyMatch(flight -> flight.getFlightId() == 102));
    }
}