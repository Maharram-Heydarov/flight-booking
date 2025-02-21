package az.edu.turing.domain.dao.impl.memory;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class FlightInMemoryDao extends FlightDao {
    private static final Map<Long, FlightEntity> FLIGHTS = new HashMap<>();

    public FlightInMemoryDao() {
        loadMockFlight();
    }

    @Override
    public List<FlightEntity> getAll() {
        return List.copyOf(FLIGHTS.values());
    }

    @Override
    public Optional<FlightEntity> getById(Long id) {
        return Optional.ofNullable(FLIGHTS.get(id));
    }

    @Override
    public FlightEntity save(final FlightEntity object) {
        FLIGHTS.put(object.getFlightId(), object);
        return FLIGHTS.get(object.getFlightId());
    }

    @Override
    public boolean deleteById(Long id) {
        return FLIGHTS.remove(id) != null;
    }

    @Override
    public FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount) {
        var flightEntity = FLIGHTS.get(flightId);
        if (flightEntity != null) {
            flightEntity.setAvailableSeats(newAvailableSeatCount);
        }
        return flightEntity;
    }

    @Override
    public List<FlightEntity> getAllFlightsWithin24Hours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursLater = now.plusHours(24);

        return FLIGHTS.values().stream()
                .filter(flight -> {
                    LocalDateTime departureTime = flight.getDepartureTime();
                    return departureTime.isAfter(now) && departureTime.isBefore(twentyFourHoursLater);
                })
                .collect(Collectors.toList());
    }


    public void loadMockFlight() {
        FlightEntity flight = new FlightEntity(
                101,
                "New York",
                "Kiev",
                LocalDateTime.now().plusHours(2),
                150
        );
        FLIGHTS.put(flight.getFlightId(), flight);
    }
}
