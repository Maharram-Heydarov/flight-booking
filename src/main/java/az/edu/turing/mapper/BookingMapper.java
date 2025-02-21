package az.edu.turing.mapper;

import az.edu.turing.domain.entities.BookingEntity;
import az.edu.turing.model.dto.BookingDto;

public class BookingMapper implements EntityMapper<BookingEntity, BookingDto> {
    @Override
    public BookingEntity toEntity(BookingDto bookingDto) {
        return new BookingEntity(
                bookingDto.getBookingId(),
                bookingDto.getBookerName(),
                bookingDto.getBookerSurname(),
                bookingDto.getFlight()
        );
    }

    @Override
    public BookingDto toDto(BookingEntity bookingEntity) {
        return new BookingDto(
                bookingEntity.getBookingId(),
                bookingEntity.getBookerName(),
                bookingEntity.getBookerSurname(),
                bookingEntity.getFlight()
        );
    }
}
