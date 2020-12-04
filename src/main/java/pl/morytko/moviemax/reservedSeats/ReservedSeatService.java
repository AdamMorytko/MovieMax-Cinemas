package pl.morytko.moviemax.reservedSeats;

import pl.morytko.moviemax.seats.Seat;

import java.util.List;
import java.util.Optional;

public interface ReservedSeatService {
    void addReservedSeat(ReservedSeat reservedSeat);
    void getReservedSeatById(long id);
    List<ReservedSeat> getReservedSeatsByScreeningId(long screeningId);
}
