package az.edu.turing.mapper;

import az.edu.turing.domain.entities.FlightEntity;
import az.edu.turing.model.dto.FlightDto;

public class FlightMapper implements EntityMapper<FlightEntity, FlightDto> {

    @Override
    public FlightEntity toEntity(FlightDto flightDto) {
        return new FlightEntity(
                flightDto.getFlightId(),
                flightDto.getDestination(),
                flightDto.getFrom(),
                flightDto.getDepartureTime(),
                flightDto.getAvailableSeats()
        );
    }

    @Override
    public FlightDto toDto(FlightEntity flightEntity) {
        return new FlightDto(
                flightEntity.getFlightId(),
                flightEntity.getDestination(),
                flightEntity.getFrom(),
                flightEntity.getDepartureTime(),
                flightEntity.getAvailableSeats()
        );
    }
}
