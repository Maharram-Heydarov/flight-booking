package az.edu.turing.dao.impl.file;

import az.edu.turing.domain.dao.impl.file.BookingFileDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.exception.BookingNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BookingFileDaoTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Path tempDirectory;
    private BookingFileDao bookingFileDao;

    @BeforeAll
    static void setUpClass() throws Exception {
        tempDirectory = Files.createTempDirectory("bookings_test");
    }

    @BeforeEach
    void setUp() {
        System.setProperty("BOOKINGS_RESOURCE_PATH", tempDirectory.toString());
        bookingFileDao = new BookingFileDao(objectMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        for (File file : tempDirectory.toFile().listFiles()) {
            file.delete();
        }
    }

    @AfterAll
    static void tearDownClass() throws Exception {
        Files.delete(tempDirectory);
    }

    @Test
    void testSaveAndGetById() {
        BookingEntity booking = new BookingEntity("John", "Doe", new FlightEntity());

        bookingFileDao.save(booking);
        Optional<BookingEntity> retrievedBooking = bookingFileDao.getById(1L);

        assertTrue(retrievedBooking.isPresent());
        assertEquals(booking, retrievedBooking.get());
    }

    @Test
    void testGetById_NotFound() {
        Optional<BookingEntity> retrievedBooking = bookingFileDao.getById(999L);

        assertTrue(retrievedBooking.isEmpty());
    }

    @Test
    void testGetAll() {
        BookingEntity booking1 = new BookingEntity("Harry", "Potter", new FlightEntity());
        BookingEntity booking2 = new BookingEntity("Ron", "Weasley", new FlightEntity());

        bookingFileDao.save(booking1);
        bookingFileDao.save(booking2);

        List<BookingEntity> allBookings = bookingFileDao.getAll();

        assertEquals(3, allBookings.size());
        assertTrue(allBookings.contains(booking1));
        assertTrue(allBookings.contains(booking2));
    }

    @Test
    void testDeleteById() {
        BookingEntity booking = new BookingEntity("John", "Doe", new FlightEntity());
        bookingFileDao.save(booking);

        boolean deleted = bookingFileDao.deleteById(1L);

        assertTrue(deleted);
        assertTrue(bookingFileDao.getById(1L).isEmpty());
    }

    @Test
    void testDeleteById_NotFound() {
        assertThrows(BookingNotFoundException.class, () -> bookingFileDao.deleteById(999L));
    }
}
