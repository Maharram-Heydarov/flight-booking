package az.edu.turing.domain.dao.impl.file;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.exception.BookingNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingFileDao extends BookingDao {

    private static final String BOOKINGS_RESOURCE_PATH = System.getenv("BOOKINGS_RESOURCE_PATH");
    private final ObjectMapper objectMapper;

    public BookingFileDao(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        ensureFolderExists();
    }

    @Override
    public List<BookingEntity> getAll() {
        List<BookingEntity> bookings = new ArrayList<>();
        File directory = new File(BOOKINGS_RESOURCE_PATH);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try {
                    BookingEntity object = objectMapper.readValue(file, BookingEntity.class);
                    bookings.add(object);
                } catch (IOException e) {
                    System.err.println("error reading files in BookingFileDao.getAll() method");
                    throw new RuntimeException(e);
                }
            }
        }

        return bookings;
    }

    @Override
    public Optional<BookingEntity> getById(Long id) {
        File file = new File(BOOKINGS_RESOURCE_PATH, id + ".json");
        if (!file.exists()) {
            return Optional.empty();
        }

        try {
            BookingEntity bookingById = objectMapper.readValue(file, BookingEntity.class);
            return Optional.of(bookingById);
        } catch (IOException e) {
            System.err.println("error reading file in BookingFileDao.getById() method");
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookingEntity save(BookingEntity bookingEntity) {
        File file = new File(BOOKINGS_RESOURCE_PATH, bookingEntity.getBookingId() + ".json");
        try {
            objectMapper.writeValue(file, bookingEntity);
        } catch (IOException e) {
            System.err.println("save method of BookingFileDao has faced an error.");
            throw new RuntimeException(e);
        }
        return getById(bookingEntity.getBookingId()).orElse(null);
    }

    @Override
    public boolean deleteById(Long id) {
        File file = new File(BOOKINGS_RESOURCE_PATH, id + ".json");
        if (!file.exists()) {
            throw new BookingNotFoundException("Couldn't find booking with id: " + id);
        }
        return file.delete();
    }

    private void ensureFolderExists() {
        if (BOOKINGS_RESOURCE_PATH == null || BOOKINGS_RESOURCE_PATH.isBlank()) {
            throw new IllegalArgumentException("FLIGHTS_RESOURCE_PATH environment variable is not set or is empty.");
        }

        Path folderPath = Paths.get(BOOKINGS_RESOURCE_PATH);

        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                System.err.println("error creating folder in ensureFolderExists method of BookingFileDao");
                throw new RuntimeException(e);
            }
        }
    }
}
