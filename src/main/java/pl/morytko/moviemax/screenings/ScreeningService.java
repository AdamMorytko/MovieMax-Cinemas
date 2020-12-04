package pl.morytko.moviemax.screenings;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScreeningService {
    List<Screening> getScreeningsByAuditoriumId(long auditoriumId);
    List<Screening> getScreeningsByCinemaAndDate(long cinemaId, LocalDate screeningDate);
    void addScreening(Screening screening);
    Optional<Screening> getScreeningById(long id);

}
