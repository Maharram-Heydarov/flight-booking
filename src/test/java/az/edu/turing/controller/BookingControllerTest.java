package az.edu.turing.controller;

import az.edu.turing.domain.dao.impl.memory.BookingInMemoryDao;
import az.edu.turing.domain.dao.impl.memory.FlightInMemoryDao;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingControllerTest {
    private BookingInMemoryDao bookingInMemoryDao;
    private FlightInMemoryDao flightInMemoryDao;
    private BookingMapper bookingMapper;
    private BookingServiceImpl bookingService;
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        bookingInMemoryDao = new BookingInMemoryDao();
        flightInMemoryDao = new FlightInMemoryDao();
        bookingMapper = new BookingMapper();
        bookingService = new BookingServiceImpl(bookingInMemoryDao, flightInMemoryDao, bookingMapper);
        bookingController = new BookingController(bookingService);

        FlightEntity flight = new FlightEntity(
                101,
                "New York",
                "Kiev",
                LocalDateTime.now().plusHours(2),
                150
        );

        flightInMemoryDao.save(flight);

        CreateBookingRequest createBookingRequest =
                new CreateBookingRequest(101, "John", "Doe");

        CreateBookingRequest createBookingRequest2 =
                new CreateBookingRequest(101, "Jessie", "Jonathan");

        bookingController.createBooking(createBookingRequest);
        bookingController.createBooking(createBookingRequest2);
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
    void createBooking() {
        CreateBookingRequest createBookingRequest =
                new CreateBookingRequest(101, "Walter", "White");

        BookingDto booking = bookingController.createBooking(createBookingRequest);

        FlightEntity flightEntity = flightInMemoryDao.getById(101L).orElse(null);

        assertNotNull(flightEntity);

        BookingDto expected = new BookingDto(3, "Walter", "White", flightEntity);

        assertNotNull(booking);
        assertEquals(expected, booking);
    }

    @Test
    void cancelBooking() {
        CreateBookingRequest createBookingRequest =
                new CreateBookingRequest(101, "John", "Doe");

        BookingDto booking = bookingController.createBooking(createBookingRequest);

        boolean result = bookingController.cancelBooking(1);
        assertTrue(result);
    }

    @Test
    void findAllBookingsByPassenger() {
        List<BookingDto> allBookingsByPassenger = bookingController.findAllBookingsByPassenger("John Doe");
        assertNotNull(allBookingsByPassenger);
        BookingDto expected = new BookingDto(1, "John", "Doe", flightInMemoryDao.getById(101L).orElse(null));
        assertEquals(expected, allBookingsByPassenger.get(0));
    }

    @Test
    void getBookingDetails() {
        BookingDto actual = bookingController.getBookingDetails(1);
        BookingDto expected = new BookingDto(1, "John", "Doe", flightInMemoryDao.getById(101L).orElse(null));

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getAllBookings() {
        List<BookingDto> allBookings = bookingController.getAllBookings();
        assertEquals(allBookings, List.of(
                new BookingDto(1, "John", "Doe", flightInMemoryDao.getById(101L).orElse(null)),
                new BookingDto(2, "Jessie", "Jonathan", flightInMemoryDao.getById(101L).orElse(null))
        ));
    }
}
