package az.edu.turing.impl;

import az.edu.turing.controller.BookingController;
import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.dao.impl.memory.BookingInMemoryDao;
import az.edu.turing.domain.dao.impl.memory.FlightInMemoryDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.mapper.BookingMapper;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.dto.request.CreateBookingRequest;
import az.edu.turing.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingServiceImplTest {

    private BookingServiceImpl bookingService;
    private BookingDao bookingDao;
    private FlightDao flightDao;
    private BookingMapper bookingMapper;
    private FlightInMemoryDao flightInMemoryDao;
    private BookingInMemoryDao bookingInMemoryDao;
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        bookingInMemoryDao = new BookingInMemoryDao();
        flightInMemoryDao = new FlightInMemoryDao();
        bookingMapper = new BookingMapper();
        bookingService = new BookingServiceImpl(bookingInMemoryDao, flightInMemoryDao, bookingMapper);
        bookingController = new BookingController(bookingService);
        flightInMemoryDao.save(new FlightEntity(101, "New York", "Kiev", LocalDateTime.now().plusHours(5), 150));
        FlightEntity flight = new FlightEntity(102, "Moscow", "Kiev", LocalDateTime.now().plusHours(10), 150);
        flightInMemoryDao.save(flight);
        BookingEntity booking = new BookingEntity("Jane", "Doe", flight);
        bookingInMemoryDao.save(booking);

    }

    @AfterEach
    void tearDown() {
        bookingInMemoryDao = null;
        flightInMemoryDao = null;
        bookingMapper = null;
        bookingService = null;
        bookingController = null;
    }

    @Test
    void testCreateBooking_Success() {
        CreateBookingRequest request = new CreateBookingRequest(101, "Ron", "Weasley");
        BookingDto result = bookingService.createBooking(request);
        assertNotNull(result);
        assertEquals("Ron", result.getBookerName());
        assertEquals("Weasley", result.getBookerSurname());
        assertNotNull(result.getFlight());
        assertEquals(101, result.getFlight().getFlightId());
    }


    @Test
    void testCancelBooking_Success() {
        long bookingId = 1;
        FlightEntity flightBeforeCancellation = flightInMemoryDao.getById(101L).orElseThrow();
        int availableSeatsBefore = flightBeforeCancellation.getAvailableSeats();
        boolean isCancelled = bookingService.cancelBooking(bookingId);
        FlightEntity flightAfterCancellation = flightInMemoryDao.getById(101L).orElseThrow();
        int availableSeatsAfter = flightAfterCancellation.getAvailableSeats();
        assertTrue(isCancelled);
        assertEquals(availableSeatsBefore, availableSeatsAfter);
        assertFalse(bookingInMemoryDao.getById(bookingId).isPresent());
    }


    @Test
    void testFindAllBookingByPassenger() {
        String fullName = "Tom Sawyer";
        BookingEntity booking = new BookingEntity("Tom", "Sawyer", new FlightEntity());
        bookingInMemoryDao.save(booking);
        List<BookingDto> bookings = bookingService.findAllBookingByPassenger(fullName);
        assertEquals(1, bookings.size());
        assertEquals(fullName, bookings.get(0).getFullName());
    }

    @Test
    void getBookingDetails() {
        long bookingId = 1;
        BookingDto bookingDto = bookingService.getBookingDetails(bookingId);
        assertNotNull(bookingDto);
        assertEquals("Jane Doe", bookingDto.getFullName());
        assertEquals(1, bookingDto.getBookingId());
    }

    @Test
    void getAllBookings() {
        List<BookingDto> bookings = bookingService.getAllBookings();
        assertNotNull(bookings);
        assertEquals(6, bookings.size());
        assertEquals("Jane Doe", bookings.get(0).getFullName());

    }
}
