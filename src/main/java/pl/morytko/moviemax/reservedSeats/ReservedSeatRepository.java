package pl.morytko.moviemax.reservedSeats;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.morytko.moviemax.reservations.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {
    Optional<ReservedSeat> findByRowAndNumber(int row, int number);
    List<ReservedSeat> findAllByReservationScreeningId(long screeningId);
    List<ReservedSeat> findAllByReservation_Id(long reservationId);
}
