package pl.morytko.moviemax.cinemas;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CinemaJpaService implements CinemaService{

    private final CinemaRepository cinemaRepository;


    @Override
    public List<Cinema> getCinemas() {
        return cinemaRepository.findAll();
    }

    @Override
    public void addCinema(Cinema cinema) {
        cinemaRepository.save(cinema);
    }

    @Override
    public void updateCinema(Cinema cinema) {
        cinemaRepository.save(cinema);
    }

    @Override
    public void deleteCinema(long id) {
        cinemaRepository.deleteById(id);
    }
}
