package pl.morytko.moviemax.auditoriums;

import java.util.List;
import java.util.Optional;

public interface AuditoriumService {
    List<Auditorium> getAuditoriums();
    List<Auditorium> getCinemaAuditoriums(long cinemaId);
    Optional<Auditorium> getAuditoriumById(long auditoriumId);
    Auditorium addAuditorium(Auditorium auditorium);
    void updateAuditorium(Auditorium auditorium);
    void deleteAuditorium(long id);
    List<Long> getListOfAuditoriumIdOfCinema(long cinemaId);
}
