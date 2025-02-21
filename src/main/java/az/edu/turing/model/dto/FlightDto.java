package az.edu.turing.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class FlightDto implements Serializable {
    private final long flightId;
    private final String destination;
    private final String from;
    private final LocalDateTime departureTime;
    private final int availableSeats;


    public FlightDto(long flightId, String destination, String from, LocalDateTime departureTime, int availableSeats) {
        this.flightId = flightId;
        this.destination = destination;
        this.from = from;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }

    public long getFlightId() {
        return flightId;
    }

    public String getDestination() {
        return destination;
    }

    public String getFrom() {
        return from;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    @Override
    public String toString() {
        return "Flight ID=%d, Destination='%s', From='%s', Departure Time=%s, Available Seats=%d"
                .formatted(flightId, destination, from, departureTime, availableSeats);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightDto flightDto = (FlightDto) o;
        return flightId == flightDto.flightId &&
                availableSeats == flightDto.availableSeats &&
                Objects.equals(destination, flightDto.destination) &&
                Objects.equals(from, flightDto.from) &&
                Objects.equals(departureTime.toLocalDate(), flightDto.departureTime.toLocalDate()) &&
                Objects.equals(departureTime.getHour(), flightDto.departureTime.getHour()) &&
                Objects.equals(departureTime.getMinute(), flightDto.departureTime.getMinute()) &&
                Objects.equals(departureTime.getSecond(), flightDto.departureTime.getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, destination, from, departureTime, availableSeats);
    }
}