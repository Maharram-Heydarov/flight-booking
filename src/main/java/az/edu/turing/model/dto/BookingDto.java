package az.edu.turing.model.dto;

import az.edu.turing.domain.entities.FlightEntity;

import java.io.Serializable;
import java.util.Objects;

public class BookingDto implements Serializable {

    private final long bookingId;
    private final String bookerName;
    private final String bookerSurname;
    private final FlightEntity flight;

    public BookingDto(long bookingId, String bookerName, String bookerSurname, FlightEntity flight) {
        this.bookingId = bookingId;
        this.bookerName = bookerName;
        this.bookerSurname = bookerSurname;
        this.flight = flight;
    }

    public long getBookingId() {
        return bookingId;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public String getFullName() {
        return bookerName + " " + bookerSurname;
    }

    public String getBookerName() {
        return bookerName;
    }

    public String getBookerSurname() {
        return bookerSurname;
    }


    @Override
    public String toString() {
        return "Booking ID=%d, Booker's Name='%s', Booker's Surname='%s', Flight=%d}"
                .formatted(bookingId, bookerName, bookerSurname, flight.getFlightId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDto that = (BookingDto) o;
        return bookingId == that.bookingId &&
                Objects.equals(bookerName, that.bookerName) &&
                Objects.equals(bookerSurname, that.bookerSurname) &&
                Objects.equals(flight, that.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, bookerName, bookerSurname, flight);
    }
}