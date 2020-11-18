package pl.morytko.moviemax.auditoriums;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AuditoriumJpaService implements AuditoriumService {

    private final AuditoriumRepository auditoriumRepository;

    @Override
    public List<Auditorium> getCinemaAuditoriums(long cinemaId) {
        return auditoriumRepository.getAllByCinema_Id(cinemaId);
    }

    @Override
    public Optional<Auditorium> getAuditoriumOfCinemaById(long auditoriumId, long cinemaId) {
        return auditoriumRepository.getByIdAndCinema_Id(auditoriumId, cinemaId);
    }

    @Override
    public void addAuditorium(Auditorium auditorium) {
        auditoriumRepository.save(auditorium);
    }

    @Override
    public void updateAuditorium(Auditorium auditorium) {
        auditoriumRepository.save(auditorium);
    }

    @Override
    public void deleteAuditorium(long id) {
        auditoriumRepository.deleteById(id);
    }
}
