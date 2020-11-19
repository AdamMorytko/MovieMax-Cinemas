package pl.morytko.moviemax.seats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findAllByAuditorium_Id(long id);
    Optional<Seat> findByRowAndNumber(int row, int number);
}
