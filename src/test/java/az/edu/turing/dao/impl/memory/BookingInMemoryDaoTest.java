package az.edu.turing.dao.impl.memory;

import az.edu.turing.domain.dao.impl.memory.BookingInMemoryDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.FlightEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingInMemoryDaoTest {
    private BookingInMemoryDao bookingDao;

    @BeforeEach
    public void setUp() {
        bookingDao = new BookingInMemoryDao();
        BookingEntity booking2 = new BookingEntity(2L, "Anna", "Johnson", new FlightEntity());
        bookingDao.save(booking2);
    }

    @Test
    public void testSaveBooking() {
        BookingEntity booking = new BookingEntity(1L, "Tom", "Johnson", new FlightEntity(1, "Turkey", "Kiev", LocalDateTime.of(2024, 11, 13, 5, 41), 50));
        BookingEntity result = bookingDao.save(booking);
        assertTrue(bookingDao.getById(1L).isPresent());
    }

    @Test
    public void testGetAllBookings() {
        assertEquals(2, bookingDao.getAll().size());
    }

    @Test
    public void testGetById() {
        Optional<BookingEntity> retrievedBooking = bookingDao.getById(2L);
        assertTrue(retrievedBooking.isPresent());
        assertEquals(2L, retrievedBooking.get().getBookingId());
    }

    @Test
    public void testDeleteById() {
        boolean deleted = bookingDao.deleteById(2L);
        assertEquals(1, bookingDao.getAll().size());
    }

}
