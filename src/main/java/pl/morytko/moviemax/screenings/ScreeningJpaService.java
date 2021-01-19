package pl.morytko.moviemax.screenings;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
@AllArgsConstructor
public class ScreeningJpaService implements ScreeningService {
    private final ScreeningRepository screeningRepository;

    @Override
    public List<Screening> getScreeningsByAuditoriumId(long auditoriumId) {
        return screeningRepository.findAllByAuditoriumId(auditoriumId);
    }

    @Override
    public List<Screening> getScreeningsByCinemaAndDate(long cinemaId, LocalDate screeningDate) {
        return screeningRepository.findAllByAuditorium_Cinema_IdAndScreeningDate(cinemaId, screeningDate);
    }

    @Override
    public List<Screening> getScreeningsByAuditoriumAndDate(long auditoriumId, LocalDate screeningDate) {
        return screeningRepository.findAllByAuditorium_IdAndScreeningDate(auditoriumId, screeningDate);
    }

    @Override
    public void saveAll(List<Screening> screenings) {
        screeningRepository.saveAll(screenings);
    }

    @Override
    public void addScreening(Screening screening) {
        screeningRepository.save(screening);
    }

    @Override
    public Optional<Screening> getScreeningById(long id) {
        return screeningRepository.findById(id);
    }

    @Override
    public boolean checkOverlapping(Screening screeningToCheck) {
        long auditoriumId = screeningToCheck.getAuditorium().getId();
        List<Screening> sameDayScreenings = screeningRepository.findAllByAuditorium_IdAndScreeningDate(auditoriumId, screeningToCheck.getScreeningDate());
        AtomicBoolean available = new AtomicBoolean(false);
        sameDayScreenings.forEach(screening -> {
            LocalDateTime screeningStart = LocalDateTime.of
                    (screening.getScreeningDate(),
                            screening.getScreeningTime());
            LocalDateTime screeningEnd = LocalDateTime.of
                    (screening.getScreeningDate(),
                            screening.getScreeningTime().plusMinutes(screening.getMovie().getDuration()));
            LocalDateTime checkedScreeningStart = LocalDateTime.of
                    (screeningToCheck.getScreeningDate(),
                            screeningToCheck.getScreeningTime());
            LocalDateTime checkedScreeningEnd = LocalDateTime.of
                    (screeningToCheck.getScreeningDate(),
                            screeningToCheck.getScreeningTime().plusMinutes(screeningToCheck.getMovie().getDuration()));
            if (checkedScreeningStart.isBefore(screeningEnd) && checkedScreeningEnd.isAfter(screeningStart)){
                available.set(true);
            }   
        });
        return available.get();
    }


}
