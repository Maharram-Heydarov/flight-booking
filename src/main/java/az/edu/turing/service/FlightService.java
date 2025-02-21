package az.edu.turing.service;

import az.edu.turing.model.dto.FlightDto;

import java.time.LocalDate;
import java.util.List;


public interface FlightService {

    FlightDto getFlightById(long flightId);

    List<FlightDto> getAllFlights();

    List<FlightDto> getAllFlightsWithin24Hours();

    List<FlightDto> findFlights(String destination, LocalDate date, int numberOfPeople);
}
