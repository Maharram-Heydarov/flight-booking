package az.edu.turing.domain.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FlightEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicLong atomicCounter = new AtomicLong(0);

    private long flightId;
    private String destination;
    private String from;
    private LocalDateTime departureTime;
    private int availableSeats;

    public FlightEntity() {
    }

    public FlightEntity(String destination, String from, LocalDateTime departureTime, int availableSeats) {
        this.flightId = atomicCounter.incrementAndGet();
        this.destination = destination;
        this.from = from;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }


    public FlightEntity(long flightId, String destination, String from, LocalDateTime departureTime, int availableSeats) {
        this.flightId = flightId;
        this.destination = destination;
        this.from = from;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
    }


    public Long getFlightId() {
        return flightId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "FlightEntity{" +
                "flightId=" + flightId +
                ", destination='" + destination + '\'' +
                ", from='" + from + '\'' +
                ", departureTime=" + departureTime +
                ", availableSeats=" + availableSeats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightEntity that = (FlightEntity) o;
        return flightId == that.flightId &&
                availableSeats == that.availableSeats &&
                Objects.equals(destination, that.destination) &&
                Objects.equals(from, that.from) &&
                Objects.equals(departureTime, that.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, destination, from, departureTime, availableSeats);
    }
}
