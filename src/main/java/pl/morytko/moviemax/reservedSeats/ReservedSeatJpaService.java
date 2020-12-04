package pl.morytko.moviemax.reservedSeats;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ReservedSeatJpaService implements ReservedSeatService{
    private final ReservedSeatRepository reservedSeatRepository;

    @Override
    public void addReservedSeat(ReservedSeat reservedSeat) {
        reservedSeatRepository.save(reservedSeat);
    }

    @Override
    public void getReservedSeatById(long id) {
        reservedSeatRepository.findById(id);
    }
}
