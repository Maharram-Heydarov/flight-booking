package az.edu.turing.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class BookingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicLong atomicCounter = new AtomicLong(0);

    private long bookingId;
    private String bookerName;
    private String bookerSurname;
    private FlightEntity flight;

    public BookingEntity() {
    }

    public BookingEntity(long bookingId, String bookerName, String bookerSurname, FlightEntity flight) {
        this.bookingId = bookingId;
        this.bookerName = bookerName;
        this.bookerSurname = bookerSurname;
        this.flight = flight;
    }

    public BookingEntity(String bookerName, String bookerSurname, FlightEntity flight) {
        this.bookingId = atomicCounter.incrementAndGet();
        this.bookerName = bookerName;
        this.bookerSurname = bookerSurname;
        this.flight = flight;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public FlightEntity getFlight() {
        return flight;
    }

    public void setFlight(FlightEntity flight) {
        this.flight = flight;
    }

    public String getBookerName() {
        return bookerName;
    }

    public void setBookerName(String bookerName) {
        this.bookerName = bookerName;
    }

    public String getBookerSurname() {
        return bookerSurname;
    }

    public void setBookerSurname(String bookerSurname) {
        this.bookerSurname = bookerSurname;
    }

    @JsonIgnore
    public String getFullName() {
        return bookerName + " " + bookerSurname;
    }

    @Override
    public String toString() {
        return "BookingEntity{" +
                "bookingId=" + bookingId +
                ", flight=" + flight +
                ", bookerName='" + bookerName + '\'' +
                ", bookerSurname='" + bookerSurname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return bookingId == that.bookingId &&
                Objects.equals(flight, that.flight) &&
                Objects.equals(bookerName, that.bookerName) &&
                Objects.equals(bookerSurname, that.bookerSurname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, flight, bookerName, bookerSurname);
    }
}
