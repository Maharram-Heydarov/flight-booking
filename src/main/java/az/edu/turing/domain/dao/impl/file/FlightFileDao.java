package az.edu.turing.domain.dao.impl.file;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.exception.FlightNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightFileDao extends FlightDao {

    private static final String FLIGHTS_RESOURCE_PATH = System.getenv("FLIGHTS_RESOURCE_PATH");
    private final ObjectMapper objectMapper;

    public FlightFileDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        ensureFolderExists();
        createMockFlight();
    }

    @Override
    public List<FlightEntity> getAll() {
        List<FlightEntity> flights = new ArrayList<>();
        File directory = new File(FLIGHTS_RESOURCE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try {
                    FlightEntity object = objectMapper.readValue(file, FlightEntity.class);
                    flights.add(object);
                } catch (IOException e) {
                    System.err.println("error reading files in FlightFileDao.getAll() method");
                    throw new RuntimeException(e);
                }
            }
        }

        return flights;
    }

    public List<FlightEntity> getAllFlightsWithin24Hours() {
        List<FlightEntity> flightsWithin24Hours = new ArrayList<>();
        File directory = new File(FLIGHTS_RESOURCE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try {
                    FlightEntity object = objectMapper.readValue(file, FlightEntity.class);
                    if (isFlightWithin24Hours(object)) {
                        flightsWithin24Hours.add(object);
                    }
                } catch (IOException e) {
                    System.err.println("error reading files in FlightFileDao.getAll() method");
                    throw new RuntimeException(e);
                }
            }
        }

        return flightsWithin24Hours;
    }

    @Override
    public Optional<FlightEntity> getById(Long id) {
        File file = new File(FLIGHTS_RESOURCE_PATH, id + ".json");
        if (!file.exists()) {
            return Optional.empty();
        }

        try {
            FlightEntity flightById = objectMapper.readValue(file, FlightEntity.class);
            return Optional.of(flightById);
        } catch (IOException e) {
            System.err.println("error reading file in FlightFileDao.getById() method");
            throw new RuntimeException(e);
        }
    }

    @Override
    public FlightEntity save(FlightEntity flightEntity) {
        File file = new File(FLIGHTS_RESOURCE_PATH, flightEntity.getFlightId() + ".json");
        try {
            objectMapper.writeValue(file, flightEntity);
        } catch (IOException e) {
            System.err.println("save method of FlightFileDao has faced an error.");
            throw new RuntimeException(e);
        }
        return getById(flightEntity.getFlightId()).orElse(null);
    }

    @Override
    public boolean deleteById(Long id) {
        File file = new File(FLIGHTS_RESOURCE_PATH, id + ".json");
        if (!file.exists()) {
            throw new FlightNotFoundException("Couldn't find flight with id: " + id);
        }
        return file.delete();
    }

    @Override
    public FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount) {
        FlightEntity flightById = getById(flightId)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        flightById.setAvailableSeats(newAvailableSeatCount);
        return save(flightById);
    }

    private void ensureFolderExists() {
        if (FLIGHTS_RESOURCE_PATH == null || FLIGHTS_RESOURCE_PATH.isBlank()) {
            throw new IllegalArgumentException("FLIGHTS_RESOURCE_PATH environment variable is not set or is empty.");
        }

        Path folderPath = Paths.get(FLIGHTS_RESOURCE_PATH);

        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                System.err.println("error creating folder in ensureFolderExists method of FlightFileDao");
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isFlightWithin24Hours(FlightEntity flight) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime flightDateTime = flight.getDepartureTime();

        if (currentTime.getYear() != flightDateTime.getYear()) {
            return false;
        }

        if (currentTime.getMonthValue() != flightDateTime.getMonthValue()) {
            return false;
        }

        long hoursDifference = Math.abs(ChronoUnit.HOURS.between(currentTime, flightDateTime));

        return (hoursDifference >= 0 && hoursDifference <= 24);
    }

    public void createMockFlight() {
        FlightEntity mockFlight = new FlightEntity(
                1L,
                "New York",
                "Kiev",
                LocalDateTime.now().plusHours(4),
                150
        );
        FlightEntity saved = save(mockFlight);
        if (saved == null) {
            throw new RuntimeException("error creating mock flight");
        }
    }
}
