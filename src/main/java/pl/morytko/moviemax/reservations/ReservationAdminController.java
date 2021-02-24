package pl.morytko.moviemax.reservations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin/reservations")
@AllArgsConstructor
public class ReservationAdminController {

    ReservationService reservationService;

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
}
