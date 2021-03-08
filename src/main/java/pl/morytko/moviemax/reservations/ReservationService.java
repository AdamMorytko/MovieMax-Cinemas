package pl.morytko.moviemax.reservations;


import java.util.List;
import java.util.Optional;

public interface ReservationService {
    Reservation addReservation(Reservation reservation);
    void addReservations(List<Reservation> reservationList);
    void updateReservation(Reservation reservation);
    Optional<Reservation> getReservation(long id);
    List<Reservation> getReservations();
    List<Reservation> getReservationsByScreening(long screeningId);
}
