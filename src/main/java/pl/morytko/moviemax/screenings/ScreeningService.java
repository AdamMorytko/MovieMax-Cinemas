package pl.morytko.moviemax.screenings;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScreeningService {
    List<Screening> getScreenings();
    List<Screening> getScreeningsByAuditoriumId(long auditoriumId);
    List<Screening> getScreeningsByCinemaAndDate(long cinemaId, LocalDate screeningDate);
    List<Screening> getScreeningsByCinema(long cinemaId);
    void addScreening(Screening screening);
    void deleteScreening(long screeningId);
    Optional<Screening> getScreeningById(long id);
    boolean checkOverlapping(Screening screeningToCheck);
    boolean checkOverlapping(Screening screeningToCheck, long screeningIdToIgnore);
    List<Screening> getScreeningsByAuditoriumAndDate(long auditoriumId, LocalDate screeningDate);
    void saveAll(List<Screening> screenings);

}
