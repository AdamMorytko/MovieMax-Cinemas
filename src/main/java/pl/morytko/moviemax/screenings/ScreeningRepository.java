package pl.morytko.moviemax.screenings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findAllByAuditoriumId(long auditoriumId);
    List<Screening> findAllByAuditorium_Cinema_IdAndScreeningDate(long cinemaId, LocalDate screeningDate);
    List<Screening> findAllByAuditorium_IdAndScreeningDate(long auditoriumId, LocalDate screeningDate);
    @Query("SELECT s FROM Screening s WHERE s.auditorium.cinema.id=:cinemaId")
    List<Screening> findAllByCinema(long cinemaId);
}
