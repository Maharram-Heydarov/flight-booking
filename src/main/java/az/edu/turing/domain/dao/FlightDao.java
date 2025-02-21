package az.edu.turing.domain.dao;

import az.edu.turing.domain.entities.FlightEntity;

import java.util.List;

public abstract class FlightDao implements Dao<FlightEntity, Long> {
    public abstract FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount);

    public abstract List<FlightEntity> getAllFlightsWithin24Hours();
}
