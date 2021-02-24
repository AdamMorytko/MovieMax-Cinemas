package pl.morytko.moviemax.reservations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.morytko.moviemax.reservedSeats.ReservedSeatService;
import pl.morytko.moviemax.seats.SeatService;
import pl.morytko.moviemax.utils.Counter;
import pl.morytko.moviemax.utils.ReservedSeatUtil;

import java.util.Optional;

@Controller
@RequestMapping("/admin/reservations")
@AllArgsConstructor
public class ReservationAdminController {

    private final ReservationService reservationService;
    private final SeatService seatService;
    private final ReservedSeatService reservedSeatService;

    @GetMapping("/list")
    public String showReservationList(Model model){
        model.addAttribute("reservations",reservationService.getReservations());
        return "admin/reservations/reservationList";
    }

    @GetMapping("/details/{reservationId}")
    public String showReservationDetails(@PathVariable long reservationId, Model model){
        Optional<Reservation> reservationOptional = reservationService.getReservation(reservationId);
        if (reservationOptional.isPresent()){
            model.addAttribute("reservation",reservationOptional.get());
            return "admin/reservations/reservationDetails";
        }else{
            throw new IllegalArgumentException("Rezerwacja o podanym id nie istnieje.");
        }
    }

    @GetMapping("/edit/{reservationId}")
    public String showEditForm(@PathVariable long reservationId, Model model){
        Optional<Reservation> reservationOptional = reservationService.getReservation(reservationId);
        if (reservationOptional.isPresent()){
            Reservation reservation = reservationOptional.get();
            model.addAttribute("reservationReservedSeats",reservation.getReservedSeats());
            model.addAttribute("reservedSeatNumber", reservation.getReservedSeatNumber());
            model.addAttribute("auditorium", reservation.getScreening().getAuditorium());
            model.addAttribute("seats", seatService.getSeatsByAuditoriumId(reservation.getScreening().getAuditorium().getId()));
            model.addAttribute("reservedSeats", reservedSeatService.getReservedSeatsByScreeningId(reservation.getScreening().getId()));
            model.addAttribute("counter", new Counter());
            model.addAttribute("rsUtil", new ReservedSeatUtil());
            //finished here
            return "admin/reservations/reservationEditForm";
        }else{
            throw new IllegalArgumentException("Rezerwacja o podanym id nie istnieje.");
        }
    }
}
