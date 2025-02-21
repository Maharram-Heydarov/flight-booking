package az.edu.turing.model.dto.request;

public class CreateBookingRequest {
    private long flightId;
    private String bookerName;
    private String bookerSurname;

    public CreateBookingRequest(long flightId, String bookerName, String bookerSurname) {
        this.flightId = flightId;
        this.bookerName = bookerName;
        this.bookerSurname = bookerSurname;
    }

    public long getFlightId() {
        return flightId;
    }

    public String getBookerName() {
        return bookerName;
    }

    public void setBookerName(String bookerName) {
        this.bookerName = bookerName;
    }

    public String getBookerSurname() {
        return bookerSurname;
    }

    public void setBookerSurName(String bookerSurname) {
        this.bookerSurname = bookerSurname;
    }
}
