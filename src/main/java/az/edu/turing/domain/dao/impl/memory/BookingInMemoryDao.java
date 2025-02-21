package az.edu.turing.domain.dao.impl.memory;

import az.edu.turing.domain.dao.BookingDao;
import az.edu.turing.domain.entities.BookingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class BookingInMemoryDao extends BookingDao {
    private static final Map<Long, BookingEntity> BOOKINGS = new HashMap<>();

    @Override
    public List<BookingEntity> getAll() {
        return List.copyOf(BOOKINGS.values());
    }

    @Override
    public Optional<BookingEntity> getById(Long id) {
        return Optional.ofNullable(BOOKINGS.get(id));

    }

    @Override
    public BookingEntity save(final BookingEntity object) {
        BOOKINGS.put(object.getBookingId(), object);
        return BOOKINGS.get(object.getBookingId());
    }

    @Override
    public boolean deleteById(Long id) {
        return BOOKINGS.remove(id) != null;
    }
}
