package az.edu.turing.domain.dao.impl.database;

import az.edu.turing.domain.dao.FlightDao;
import az.edu.turing.domain.entities.FlightEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static az.edu.turing.config.DatabaseConfig.getConnection;

public class FlightDatabaseDao extends FlightDao {

    public FlightDatabaseDao() {
        createFlightTableIfNotExists();
        insertMockFlightDataIfDataNotExists();
    }

    @Override
    public List<FlightEntity> getAll() {
        List<FlightEntity> flights = new ArrayList<>();
        String query = "SELECT * FROM flights";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                FlightEntity flight = mapRowToFlightEntity(resultSet);
                flights.add(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }

    @Override
    public Optional<FlightEntity> getById(Long id) {
        String query = "SELECT * FROM flights WHERE flight_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    FlightEntity flight = mapRowToFlightEntity(resultSet);
                    return Optional.of(flight);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public FlightEntity save(FlightEntity object) {
        String insertFlightQuery = "INSERT INTO flights (destination, from_location, departure_time, available_seats) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(insertFlightQuery)) {

            statement.setString(1, object.getDestination());
            statement.setString(2, object.getFrom());
            statement.setTimestamp(3, Timestamp.valueOf(object.getDepartureTime()));
            statement.setInt(4, object.getAvailableSeats());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return object;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "DELETE FROM flights WHERE flight_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private FlightEntity mapRowToFlightEntity(ResultSet resultSet) throws SQLException {
        long flightId = resultSet.getLong("flight_id");
        String destination = resultSet.getString("destination");
        String fromLocation = resultSet.getString("from_location");
        LocalDateTime departureTime = resultSet.getTimestamp("departure_time").toLocalDateTime();
        int availableSeats = resultSet.getInt("available_seats");

        return new FlightEntity(flightId, destination, fromLocation, departureTime, availableSeats);
    }

    @Override
    public FlightEntity updateAvailableSeats(long flightId, int newAvailableSeatCount) {
        String updateSeatsQuery = "UPDATE flights SET available_seats = ? WHERE flight_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(updateSeatsQuery)) {

            statement.setInt(1, newAvailableSeatCount);
            statement.setLong(2, flightId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return getById(flightId).orElse(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<FlightEntity> getAllFlightsWithin24Hours() {
        List<FlightEntity> flights = new ArrayList<>();
        String query = """
                SELECT flight_id, destination, from_location, departure_time, available_seats 
                FROM flights 
                WHERE departure_time BETWEEN NOW() AND NOW() + INTERVAL '1 DAY'
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                FlightEntity flight = mapRowToFlightEntity(resultSet);
                flights.add(flight);
            }

        } catch (SQLException e) {
            System.err.println("Database error while fetching flights within 24 hours: " + e.getMessage());
        }

        return flights;
    }


    private boolean createFlightTableIfNotExists() {
        String query = """
                CREATE TABLE IF NOT EXISTS flights (
                    flight_id SERIAL PRIMARY KEY,
                    destination VARCHAR(255) NOT NULL,
                    from_location VARCHAR(255) NOT NULL,
                    departure_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                    available_seats INTEGER NOT NULL CHECK (available_seats >= 0)
                );
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            int result = statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void insertMockFlightDataIfDataNotExists() {
        String checkFlightQuery = "SELECT COUNT(*) FROM flights;";
        try (Connection connection = getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkFlightQuery)) {
            ResultSet resultSet = checkStatement.executeQuery();
            if (!(resultSet.next() && resultSet.getInt(1) > 0)) {
                String query = """
                        INSERT INTO flights (destination, from_location, departure_time, available_seats)
                        VALUES ('New York', 'Kiev', NOW() + INTERVAL '1 hour', 150);
                        """;
                try (Statement statement = connection.createStatement()) {
                    statement.execute(query);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
