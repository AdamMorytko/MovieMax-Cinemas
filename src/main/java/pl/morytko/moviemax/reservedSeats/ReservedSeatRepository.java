package pl.morytko.moviemax.reservedSeats;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {
    Optional<ReservedSeat> findByRowAndNumber(int row, int number);
    List<ReservedSeat> findAllByScreeningId(long screeningId);
}
