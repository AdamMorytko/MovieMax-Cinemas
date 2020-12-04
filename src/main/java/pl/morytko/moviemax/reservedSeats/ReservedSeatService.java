package pl.morytko.moviemax.reservedSeats;

public interface ReservedSeatService {
    void addReservedSeat(ReservedSeat reservedSeat);
    void getReservedSeatById(long id);
}
