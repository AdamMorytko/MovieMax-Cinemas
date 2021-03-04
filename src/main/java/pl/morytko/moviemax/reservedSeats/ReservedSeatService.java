package pl.morytko.moviemax.reservedSeats;

import pl.morytko.moviemax.reservations.Reservation;

import java.util.List;

public interface ReservedSeatService {
    void addReservedSeat(ReservedSeat reservedSeat);
    void updateReservedSeats(List<ReservedSeat> reservedSeats, Reservation reservation);
    void addReservedSeats(List<ReservedSeat> reservedSeats);
    void getReservedSeatById(long id);
    List<ReservedSeat> getReservedSeatsByScreeningId(long screeningId);
}
