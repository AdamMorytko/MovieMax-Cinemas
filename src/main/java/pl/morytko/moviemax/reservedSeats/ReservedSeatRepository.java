package pl.morytko.moviemax.reservedSeats;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {
}
