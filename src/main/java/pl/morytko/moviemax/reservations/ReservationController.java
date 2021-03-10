package pl.morytko.moviemax.reservations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.morytko.moviemax.auditoriums.Auditorium;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.email.EmailService;
import pl.morytko.moviemax.reservedSeats.ReservedSeat;
import pl.morytko.moviemax.reservedSeats.ReservedSeatService;
import pl.morytko.moviemax.screenings.Screening;
import pl.morytko.moviemax.screenings.ScreeningService;
import pl.morytko.moviemax.seats.SeatService;
import pl.morytko.moviemax.users.User;
import pl.morytko.moviemax.users.UserService;
import pl.morytko.moviemax.utils.Counter;
import pl.morytko.moviemax.utils.HttpUtil;
import pl.morytko.moviemax.utils.ReservedSeatUtil;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservedSeatService reservedSeatService;
    private final ScreeningService screeningService;
    private final AuditoriumService auditoriumService;
    private final SeatService seatService;
    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/zero")
    public String showGuestOrLoginCheckForm(@RequestParam("screeningId") String screeningIdParam, Principal principal) {
        long screeningId = 0;
        if (screeningIdParam.isBlank()) {
            throw new IllegalArgumentException("Nieprawidłowy parametr.");
        } else {
            screeningId = Long.parseLong(screeningIdParam);
        }
        Optional<Screening> screeningOptional = screeningService.getScreeningById(screeningId);
        if (screeningOptional.isPresent()) {
            Screening screening = screeningOptional.get();
            LocalDateTime screeningDateTime = LocalDateTime
                    .of(screening.getScreeningDate(), screening.getScreeningTime());
            if (screeningDateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Seans już miał miejsce.");
            } else {
                HttpSession session = HttpUtil.getHttpSession();
                session.setAttribute("screeningId", screeningId);
                if (principal == null) {
                    return "main/reservations/zeroGuestOrLogin";
                } else {
                    return "main/reservations/firstFormSeatNumber";
                }
            }
        } else {
            throw new IllegalArgumentException("Nieprawidłowy parametr.");
        }
    }

    @PostMapping("/guest")
    public String showFirstFormSeatNumber() {
        return "main/reservations/firstFormSeatNumber";
    }

    @PostMapping("/second")
    public String showSecondForm(@RequestParam("reservedSeatNumber") String reservedSeatNumberParam, Model model) {
        HttpSession session = HttpUtil.getHttpSession();
        long screeningId = (long) session.getAttribute("screeningId");
        int reservedSeatNumber = Integer.parseInt(reservedSeatNumberParam);
        if (reservedSeatNumber < 1 || reservedSeatNumber > 10) {
            throw new IllegalArgumentException("Nieprawidłowa liczba biletów.");
        }
        Optional<Screening> screeningOptional = screeningService.getScreeningById(screeningId);
        if (screeningOptional.isPresent()) {
            Optional<Auditorium> auditorium = auditoriumService.getAuditoriumById(screeningOptional.get().getAuditorium().getId());
            if (auditorium.isPresent()) {
                session.setAttribute("reservedSeatNumber", reservedSeatNumber);
                model.addAttribute("reservedSeatNumber", reservedSeatNumber);
                model.addAttribute("auditorium", auditorium.get());
                model.addAttribute("cinema", auditorium.get().getCinema());
                model.addAttribute("seats", seatService.getSeatsByAuditoriumId(auditorium.get().getId()));
                model.addAttribute("reservedSeats", reservedSeatService.getReservedSeatsByScreeningId(screeningId));
                model.addAttribute("counter", new Counter());
                model.addAttribute("rsUtil", new ReservedSeatUtil());
            } else {
                throw new IllegalArgumentException("Nieprawidłowy parametr.");
            }
            return "main/reservations/chooseSeatsForm";
        } else {
            throw new IllegalArgumentException("Nieprawidłowy parametr.");
        }
    }

    @PostMapping("/third")
    public String showThirdForm(@RequestParam Map<String, String> allRequestParams, Model model, Principal principal) {
        HttpSession session = HttpUtil.getHttpSession();
        int reservedSeatNumber = (int) session.getAttribute("reservedSeatNumber");
        List<ReservedSeat> reservedSeats = new ArrayList<>();
        for (int i = 0; i < reservedSeatNumber; i++) {
            ReservedSeat reservedSeat = new ReservedSeat();
            String[] inputValue = allRequestParams.get("reservedSeat" + i).split("n");
            int row = Integer.parseInt(inputValue[0]);
            int number = Integer.parseInt(inputValue[1]);
            reservedSeat.setRow(row);
            reservedSeat.setNumber(number);
            reservedSeats.add(reservedSeat);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonReservedSeats = "";
        try {
            jsonReservedSeats = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservedSeats);
        } catch (JsonProcessingException e) {
            //to be replaced with logger and proper error display to user
            e.printStackTrace();
            return "redirect:/";
        }
        session.setAttribute("reservedSeats", jsonReservedSeats);
        if (principal == null) {
            return "main/reservations/userDetailsForm";
        } else {
            User user = (User) userService.loadUserByUsername(principal.getName());
            long screeningId = Long.parseLong(session.getAttribute("screeningId").toString());
            model.addAttribute("screening", screeningService.getScreeningById(screeningId).get());
            model.addAttribute("reservedSeatNumber", session.getAttribute("reservedSeatNumber"));
            model.addAttribute("reservedSeats", reservedSeats);
            model.addAttribute("userName", user.getName());
            model.addAttribute("userSurname", user.getSurname());
            model.addAttribute("userEmail", user.getUsername());
            return "main/reservations/reservationSummary";
        }
    }

    @PostMapping("/fourth")
    public String showSummary(@RequestParam Map<String, String> allRequestParams, Model model) {
        HttpSession session = HttpUtil.getHttpSession();
        String reservedSeatsString = session.getAttribute("reservedSeats").toString();
        long screeningId = Long.parseLong(session.getAttribute("screeningId").toString());
        Object reservedSeatNumber = session.getAttribute("reservedSeatNumber");
        ObjectMapper objectMapper = new ObjectMapper();
        List<ReservedSeat> reservedSeats = new ArrayList<>();
        try {
            reservedSeats = objectMapper.readValue(reservedSeatsString, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String userEmail = allRequestParams.get("userEmail");
        session.setAttribute("userEmail",userEmail);
        model.addAttribute("screening", screeningService.getScreeningById(screeningId).get());
        model.addAttribute("reservedSeatNumber", reservedSeatNumber);
        model.addAttribute("reservedSeats", reservedSeats);
        model.addAttribute("userEmail", userEmail);
        return "main/reservations/reservationSummary";
    }

    @PostMapping("/fifth")
    public String showReservation(@RequestParam Map<String, String> allRequestParams, Model model, Principal principal) {
        HttpSession session = HttpUtil.getHttpSession();
        int reservedSeatNumber = Integer.parseInt(session.getAttribute("reservedSeatNumber").toString());
        long screeningId = Long.parseLong(session.getAttribute("screeningId").toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String reservedSeatsString = session.getAttribute("reservedSeats").toString();
        List<ReservedSeat> reservedSeats = new ArrayList<>();
        try {
            reservedSeats = objectMapper.readValue(reservedSeatsString, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Reservation reservation = new Reservation();
        User user = new User();
        String recipientEmail;
        if (principal != null){
            user = (User) userService.loadUserByUsername(principal.getName());
            recipientEmail = user.getUsername();
        }else{
            // creating disabled user with "password" password
            // in a real application this should be replaced with a proper logic for guests buying tickets
            // field "name" will contain email to track the reservation later and connect to a person
            String userEmail = session.getAttribute("userEmail").toString();
            user.setName(userEmail);
            user.setUsername("guest@guest.guest");
            user.setSurname("gosc");
            user.setPassword("$2y$12$NzVOdFPSOtI/d3vsZbHGveTGO77r2Y/ERx0iJCaPkdwohRZIptzyi");
            user.setEnabled(false);
            userService.addUser(user);
            recipientEmail = userEmail;
        }
        reservation.setUser(user);
        reservation.setReservedSeatNumber(reservedSeatNumber);
        Screening screening = screeningService.getScreeningById(screeningId).get();
        reservation.setScreening(screening);
        Reservation savedReservation = reservationService.addReservation(reservation);
        List<ReservedSeat> alreadyReservedScreeningSeats = reservedSeatService.getReservedSeatsByScreeningId(screeningId);
        reservedSeats.forEach(reservedSeat -> {
            ReservedSeatUtil reservedSeatUtil = new ReservedSeatUtil();
            if (reservedSeatUtil.isReserved(reservedSeat.getNumber(),reservedSeat.getRow(),alreadyReservedScreeningSeats)){
                throw new IllegalArgumentException("Miejsce już jest zajęte.");
            }
            reservedSeat.setReservation(savedReservation);
        });
        savedReservation.setReservedSeats(reservedSeats);
        reservedSeatService.addReservedSeats(reservedSeats);
        long savedReservationId = savedReservation.getId();
        emailService.sendReservationDetails(recipientEmail, savedReservation);
        Optional<Reservation> reservationOptional = reservationService.getReservation(savedReservationId);
        model.addAttribute("reservation", reservationOptional.get());
        return "main/reservations/displayReservation";
    }

}
