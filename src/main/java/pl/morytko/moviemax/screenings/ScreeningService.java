package pl.morytko.moviemax.screenings;

import java.time.LocalDate;
import java.util.List;

public interface ScreeningService {
    List<Screening> getScreeningsByAuditoriumId(long auditoriumId);
    List<Screening> getScreeningsByCinemaAndDate(long cinemaId, LocalDate screeningDate);
    void addScreening(Screening screening);

}
