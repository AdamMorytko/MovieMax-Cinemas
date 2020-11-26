package pl.morytko.moviemax.screenings;

import java.util.List;

public interface ScreeningService {
    List<Screening> getScreeningsByAuditoriumId(long auditoriumId);
    void addScreening(Screening screening);

}
