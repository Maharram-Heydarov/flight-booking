package az.edu.turing.controller;

import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.service.FlightService;

import java.time.LocalDate;
import java.util.List;

public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    public FlightDto getFlightById(long flightId) {
        return flightService.getFlightById(flightId);
    }

    public List<FlightDto> getAllFlights() {
        return flightService.getAllFlights();
    }

    public List<FlightDto> getAllFlightsWithin24Hours() {
        return flightService.getAllFlightsWithin24Hours();
    }


    public List<FlightDto> findFlights(String destination, LocalDate date, int numberOfPeople) {
        return flightService.findFlights(destination, date, numberOfPeople);
    }
}
