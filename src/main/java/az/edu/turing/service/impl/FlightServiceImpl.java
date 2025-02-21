package az.edu.turing.service.impl;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.exception.FlightNotFoundException;
import az.edu.turing.mapper.FlightMapper;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.service.FlightService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FlightServiceImpl implements FlightService {

    private final FlightDao flightDao;
    private final FlightMapper mapper;

    public FlightServiceImpl(FlightDao flightDao, FlightMapper mapper) {
        this.flightDao = flightDao;
        this.mapper = mapper;
    }

    @Override
    public FlightDto getFlightById(long flightId) {
        return flightDao.getById(flightId)
                .map(mapper::toDto)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + flightId));
    }

    @Override
    public List<FlightDto> getAllFlights() {
        return flightDao.getAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightDto> getAllFlightsWithin24Hours() {
        return flightDao
                .getAllFlightsWithin24Hours().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<FlightDto> findFlights(String destination, LocalDate date, int numberOfPeople) {
        return flightDao.getAll().stream()
                .filter(f -> f.getDestination().equalsIgnoreCase(destination))
                .filter(f -> f.getDepartureTime().toLocalDate().equals(date))
                .filter(f -> f.getAvailableSeats() >= numberOfPeople)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

