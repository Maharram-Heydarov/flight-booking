package az.edu.turing.service;

import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.request.CreateBookingRequest;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(CreateBookingRequest createBookingRequest);

    boolean cancelBooking(long bookingId);

    List<BookingDto> findAllBookingByPassenger(String fullName);

    BookingDto getBookingDetails(long bookingId);

    List<BookingDto> getAllBookings();
}
