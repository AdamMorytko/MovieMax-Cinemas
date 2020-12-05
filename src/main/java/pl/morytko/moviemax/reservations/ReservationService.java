package pl.morytko.moviemax.reservations;


import java.util.Optional;

public interface ReservationService {
    Reservation addReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    Optional<Reservation> getReservation(long id);
}
