package pl.morytko.moviemax.seats;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SeatJpaService implements SeatService{

    private final SeatRepository seatRepository;

    @Override
    public List<Seat> getSeatsByAuditoriumId(long auditoriumId) {
        return seatRepository.findAllByAuditorium_Id(auditoriumId);
    }

    @Override
    public void addSeat(Seat seat) {
        seatRepository.save(seat);
    }

    @Override
    public void addSeats(List<Seat> seatList) {
        seatRepository.saveAll(seatList);
    }
}
