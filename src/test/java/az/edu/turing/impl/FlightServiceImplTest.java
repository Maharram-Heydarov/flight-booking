package az.edu.turing.impl;

import az.edu.turing.domain.dao.FlightDao;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class FlightServiceImplTest {

    private FlightServiceImpl flightService;
    private FlightDao flightDao;
    private FlightMapper mapper;


    @BeforeEach
    public void setUp() {

        flightDao = new FlightInMemoryDao();
        mapper = new FlightMapper();
        flightService = new FlightServiceImpl(flightDao, mapper);
        FlightEntity flight = new FlightEntity("Kiev", "NYC", LocalDateTime.of(2024, 12, 25, 10, 30), 100);
        flightDao.save(flight);
    }

    @AfterEach
    public void tearDown() {
        flightDao = null;
        mapper = null;
        flightService = null;
    }

    @Test
    void testGetFlightById_Success() {
        FlightDto flightDto = flightService.getFlightById(1L);
        assertNotNull(flightDto);
        assertEquals("NYC", flightDto.getFrom());
        assertEquals("Kiev", flightDto.getDestination());
    }


    @Test
    void testGetAllFlights() {

        List<FlightDto> flights = flightService.getAllFlights();
        assertEquals(2, flights.size());
    }

    @Test
    void testFindFlights() {
        List<FlightDto> results = flightService.findFlights("Kiev", LocalDate.of(2024, 12, 25), 1);
        assertEquals(2, results.size());
        assertEquals("Kiev", results.get(0).getDestination());
    }
}
