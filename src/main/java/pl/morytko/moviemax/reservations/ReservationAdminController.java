package pl.morytko.moviemax.reservations;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.morytko.moviemax.reservedSeats.ReservedSeat;
import pl.morytko.moviemax.reservedSeats.ReservedSeatService;
import pl.morytko.moviemax.seats.SeatService;
import pl.morytko.moviemax.utils.Counter;
import pl.morytko.moviemax.utils.ReservedSeatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/reservations")
@AllArgsConstructor
public class ReservationAdminController {

    private final ReservationService reservationService;
    private final SeatService seatService;
    private final ReservedSeatService reservedSeatService;

    @GetMapping("/list")
    public String showReservationList(Model model) {
        model.addAttribute("reservations", reservationService.getReservations());
        return "admin/reservations/reservationList";
    }

    @GetMapping("/details/{reservationId}")
    public String showReservationDetails(@PathVariable long reservationId, Model model) {
        Optional<Reservation> reservationOptional = reservationService.getReservation(reservationId);
        if (reservationOptional.isPresent()) {
            model.addAttribute("reservation", reservationOptional.get());
            return "admin/reservations/reservationDetails";
        } else {
            throw new IllegalArgumentException("Rezerwacja o podanym id nie istnieje.");
        }
    }

    @GetMapping("/edit/{reservationId}")
    public String showEditForm(@PathVariable long reservationId, Model model) {
        Optional<Reservation> reservationOptional = reservationService.getReservation(reservationId);
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            List<ReservedSeat> reservationReservedSeats = reservation.getReservedSeats();
            model.addAttribute("reservationReservedSeats", reservationReservedSeats);
            model.addAttribute("reservedSeatNumber", reservation.getReservedSeatNumber());
            model.addAttribute("auditorium", reservation.getScreening().getAuditorium());
            model.addAttribute("seats", seatService.getSeatsByAuditoriumId(reservation.getScreening().getAuditorium().getId()));
            List<ReservedSeat> reservedSeatsByScreeningId = reservedSeatService.getReservedSeatsByScreeningId(reservation.getScreening().getId());
            model.addAttribute("reservedSeats", reservedSeatsByScreeningId);
            model.addAttribute("counter", new Counter());
            model.addAttribute("rsUtil", new ReservedSeatUtil());
            return "admin/reservations/reservationEditForm";
        } else {
            throw new IllegalArgumentException("Rezerwacja o podanym id nie istnieje.");
        }
    }

    @PostMapping("/edit")
    public String editReservation(@RequestParam Map<String, String> allRequestParams) {
        String reservedSeatNumberParam = allRequestParams.get("reservedNumber");
        String reservationIdParam = allRequestParams.get("reservationId");
        if (reservedSeatNumberParam == null || reservedSeatNumberParam.isBlank()) {
            throw new IllegalArgumentException("Niepoprawny parametr rezerwacji.");
        }
        if (reservationIdParam == null || reservationIdParam.isBlank()){
            throw new IllegalArgumentException("Niepoprawny parametr id rezerwacji.");
        }
        Optional<Reservation> reservationOptional = reservationService.getReservation(Long.parseLong(reservationIdParam));
        Reservation reservation;
        if (reservationOptional.isPresent()){
            reservation = reservationOptional.get();
        }else{
            throw new IllegalArgumentException("Rezerwacja o podanym id nie istnieje.");
        }
        int reservedSeatNumber = Integer.parseInt(reservedSeatNumberParam);
        List<ReservedSeat> reservedSeats = new ArrayList<>();
        for (int i = 0; i < reservedSeatNumber; i++) {
            ReservedSeat reservedSeat = new ReservedSeat();
            String[] inputValue = allRequestParams.get("reservedSeat" + i).split("n");
            int row = Integer.parseInt(inputValue[0]);
            int number = Integer.parseInt(inputValue[1]);
            reservedSeat.setRow(row);
            reservedSeat.setNumber(number);
            reservedSeat.setReservation(reservation);
            reservedSeats.add(reservedSeat);
        }
        reservedSeatService.updateReservedSeats(reservedSeats, reservation);
        return "redirect:/admin/reservations/list";
    }
}
