package pl.morytko.moviemax.reservedSeats;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.morytko.moviemax.reservations.Reservation;

import javax.transaction.Transactional;
import java.util.List;

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
    public void updateReservedSeats(List<ReservedSeat> editedReservedSeats, Reservation reservation) {
        List<ReservedSeat> oldReservedSeats = reservedSeatRepository.findAllByReservation_Id(reservation.getId());
        reservedSeatRepository.deleteAll(oldReservedSeats);
        reservedSeatRepository.saveAll(editedReservedSeats);
    }

    @Override
    public void addReservedSeats(List<ReservedSeat> reservedSeats) {
        reservedSeatRepository.saveAll(reservedSeats);
    }

    @Override
    public void getReservedSeatById(long id) {
        reservedSeatRepository.findById(id);
    }

    @Override
    public List<ReservedSeat> getReservedSeatsByScreeningId(long screeningId) {
        return  reservedSeatRepository.findAllByReservationScreeningId(screeningId);
    }
}
