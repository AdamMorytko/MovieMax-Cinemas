package pl.morytko.moviemax.screenings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findAllByAuditoriumId(long auditoriumId);
    List<Screening> findAllByAuditorium_Cinema_IdAndScreeningDate(long cinemaId, LocalDate screeningDate);
}
