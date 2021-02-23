package pl.morytko.moviemax.reservedSeats;

import java.util.List;

public interface ReservedSeatService {
    void addReservedSeat(ReservedSeat reservedSeat);
    void addReservedSeats(List<ReservedSeat> reservedSeats);
    void getReservedSeatById(long id);
    List<ReservedSeat> getReservedSeatsByScreeningId(long screeningId);
}
