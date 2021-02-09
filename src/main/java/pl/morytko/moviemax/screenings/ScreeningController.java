package pl.morytko.moviemax.screenings;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.morytko.moviemax.auditoriums.Auditorium;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.cinemas.Cinema;
import pl.morytko.moviemax.cinemas.CinemaService;
import pl.morytko.moviemax.movies.MovieService;
import pl.morytko.moviemax.utils.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/screenings")
@AllArgsConstructor
public class ScreeningController {
    private final ScreeningService screeningService;
    private final AuditoriumService auditoriumService;
    private final MovieService movieService;
    private final CinemaService cinemaService;

    @GetMapping("/types")
    public String showTypesList(Model model) {
        model.addAttribute("cinemas", cinemaService.getCinemas());
        return "admin/screenings/screeningsTypesList";
    }

    @GetMapping("/all")
    public String showAllAuditoriumsScreenings(Model model) {
        model.addAttribute("screenings", screeningService.getScreenings());
        return "admin/screenings/screeningsAllList";
    }

    @GetMapping("/cinema/all/{cinemaId}")
    public String showAllCinemaScreenings(@PathVariable long cinemaId, Model model) {
        model.addAttribute("screenings", screeningService.getScreeningsByCinema(cinemaId));
        System.out.println(screeningService.getScreeningsByCinema(cinemaId).size());
        return "admin/screenings/screeningsAllCinemaScreenings";
    }

    @GetMapping("/cinema/future/{cinemaId}")
    public String showAllFutureCinemaScreenings(@PathVariable long cinemaId, Model model) {
        List<Screening> screeningList = screeningService.getScreeningsByCinema(cinemaId);
        System.out.println(screeningList.size());
        List<Screening> futureScreenings = new ArrayList<>();
        screeningList.forEach(screening -> {
            if (screening.getScreeningDate().isAfter(LocalDate.now())) {
                futureScreenings.add(screening);
            } else if (screening.getScreeningTime().isAfter(LocalTime.now()) ||
                    screening.getScreeningTime().equals(LocalTime.now())) {
                futureScreenings.add(screening);
            }
        });
        model.addAttribute("screenings", futureScreenings);
        return "admin/screenings/screeningsAllCinemaFutureScreenings";
    }

    @GetMapping("/all/future")
    public String showAllAuditoriumsFutureScreenings(Model model) {
        List<Screening> screeningList = screeningService.getScreenings();
        List<Screening> futureScreenings = new ArrayList<>();
        screeningList.forEach(screening -> {
            if (screening.getScreeningDate().isAfter(LocalDate.now())) {
                futureScreenings.add(screening);
            } else if (screening.getScreeningTime().isAfter(LocalTime.now()) ||
                    screening.getScreeningTime().equals(LocalTime.now())) {
                futureScreenings.add(screening);
            }
        });
        model.addAttribute("screenings", futureScreenings);
        return "admin/screenings/screeningsAllFutureList";
    }

    @GetMapping("/auditoriums/all/{auditoriumId}")
    public String showAllAuditoriumScreenings(@PathVariable long auditoriumId, Model model) {
        Optional<Auditorium> optionalAuditorium = auditoriumService.getAuditoriumById(auditoriumId);
        if (optionalAuditorium.isPresent()) {
            model.addAttribute("screenings", screeningService.getScreeningsByAuditoriumId(auditoriumId));
            model.addAttribute("auditoriumId", auditoriumId);
            return "admin/screenings/screeningsAllAuditoriumScreenings";
        } else {
            return "redirect:/admin/screenings/types";
        }
    }

    @GetMapping("/auditoriums/future/{auditoriumId}")
    public String showAllAuditoriumFutureScreenings(@PathVariable long auditoriumId, Model model) {
        Optional<Auditorium> optionalAuditorium = auditoriumService.getAuditoriumById(auditoriumId);
        if (optionalAuditorium.isPresent()) {
            List<Screening> screeningList = screeningService.getScreeningsByAuditoriumId(auditoriumId);
            List<Screening> futureScreenings = new ArrayList<>();
            screeningList.forEach(screening -> {
                if (screening.getScreeningDate().isAfter(LocalDate.now())) {
                    futureScreenings.add(screening);
                } else if (screening.getScreeningTime().isAfter(LocalTime.now()) ||
                        screening.getScreeningTime().equals(LocalTime.now())) {
                    futureScreenings.add(screening);
                }
            });
            model.addAttribute("screenings", futureScreenings);
            model.addAttribute("auditoriumId", auditoriumId);
            return "admin/screenings/screeningsAllAuditoriumFutureScreenings";
        } else {
            return "redirect:/admin/screenings/types";
        }
    }

    @GetMapping("/auditoriums/{cinemaId}")
    public String showCinemasAuditoriums(@PathVariable long cinemaId, Model model) {
        Optional<Cinema> optionalCinema = cinemaService.getCinemaById(cinemaId);
        if (optionalCinema.isPresent()) {
            model.addAttribute("cinemaName", optionalCinema.get().getCity() + " | " + optionalCinema.get().getStreet());
            model.addAttribute("auditoriums", auditoriumService.getCinemaAuditoriums(cinemaId));
            return "admin/screenings/screeningsChooseByAuditorium";
        } else {
            return "redirect:/admin/screenings/types";
        }
    }

    @GetMapping("/date")
    public String showChooseDate(Model model) {
        model.addAttribute("dates", DateUtil.getTwoWeeks());
        return "admin/screenings/screeningChooseDate";
    }

    @PostMapping("/date")
    public String showAddForm(Model model, @RequestParam Map<String,String> allParams) {
        model.addAttribute("auditoriums", auditoriumService.getAuditoriums());
        model.addAttribute("movies", movieService.getMovies());
        model.addAttribute("screening", new Screening());
        String chosenDate = allParams.get("chosenDate");
        if (chosenDate==null || chosenDate.isEmpty()) {
            return "redirect:/admin/screenings/date";
        }else{
            model.addAttribute("chosenDate", chosenDate);
        }
        return "admin/screenings/screeningAddForm";
    }

    @PostMapping("/add")
    public String addScreening(@RequestParam Map<String, String> allRequestParams, Model model) {
        long movieId = Long.parseLong(allRequestParams.get("movieId"));
        long auditoriumId = Long.parseLong(allRequestParams.get("auditoriumId"));
        String screeningDateParam = allRequestParams.get("screeningDate");
        LocalDate screeningDate = LocalDate.parse(screeningDateParam);
        String screeningTimeParam = allRequestParams.get("screeningTime");
        if (screeningTimeParam.isEmpty()) {
            model.addAttribute("timeEmpty", true);
            model.addAttribute("chosenDate", screeningDate);
            model.addAttribute("auditoriums", auditoriumService.getAuditoriums());
            model.addAttribute("movies", movieService.getMovies());
            model.addAttribute("screening", new Screening());
            return "admin/screenings/screeningAddForm";
        }

        LocalTime screeningTime = LocalTime.parse(screeningTimeParam);
        Screening screening = new Screening();
        screening.setAuditorium(auditoriumService.getAuditoriumById(auditoriumId).get());
        screening.setMovie(movieService.getMovieById(movieId).get());
        screening.setScreeningDate(screeningDate);
        screening.setScreeningTime(screeningTime);
        boolean overlapping = screeningService.checkOverlapping(screening);
        if (overlapping) {
            model.addAttribute("overlapping", true);
            model.addAttribute("chosenDate", screeningDate);
            model.addAttribute("auditoriums", auditoriumService.getAuditoriums());
            model.addAttribute("movies", movieService.getMovies());
            model.addAttribute("screening", new Screening());
            return "admin/screenings/screeningAddForm";
        }
        screeningService.addScreening(screening);
        return "redirect:/admin/screenings/date";
    }

    @GetMapping("/details/{screeningId}")
    public String showScreeningDetails(@PathVariable long screeningId, Model model) {
        Optional<Screening> optionalScreening = screeningService.getScreeningById(screeningId);
        if (optionalScreening.isPresent()) {
            Screening screening = optionalScreening.get();
            model.addAttribute("screening", screening);
            model.addAttribute("endingTime", screening.getScreeningTime().plusMinutes(screening.getMovie().getDuration()));
            return "admin/screenings/screeningDetails";
        } else {
            return "redirect:/admin/screenings/types";
        }
    }

    @GetMapping("/delete/{screeningId}")
    public String showDeleteConfirmation(@PathVariable long screeningId, Model model) {
        Optional<Screening> optionalScreening = screeningService.getScreeningById(screeningId);
        if (optionalScreening.isPresent()) {
            model.addAttribute("screening",optionalScreening.get());
            return "admin/screenings/screeningDeleteConfirmation";
        }
        return "redirect:/admin/screenings/types";
    }

    @PostMapping("/delete")
    public String deleteScreening(@RequestParam("screeningId") String screeningIdParam) throws NumberFormatException{
        long screeningId;
        if (screeningIdParam.isEmpty()){
            return "redirect:/admin/screenings/types";
        }else{
            try {
                screeningId = Long.parseLong(screeningIdParam);
            }catch (NumberFormatException nfe){
                throw new NumberFormatException();
            }
        }
        Optional<Screening> screeningOptional = screeningService.getScreeningById(screeningId);
        if (screeningOptional.isPresent()){
            screeningService.deleteScreening(screeningId);
        }
        return "redirect:/admin/screenings/types";
    }

    @GetMapping("/edit/{screeningId}")
    public String showEditForm(@PathVariable long screeningId, @RequestParam Map<String,String> allParams, Model model) {
        Optional<Screening> optionalScreening = screeningService.getScreeningById(screeningId);
        if (optionalScreening.isPresent()) {
            Screening screening = optionalScreening.get();
            LocalDateTime screeningDateTime = LocalDateTime.of(screening.getScreeningDate(),screening.getScreeningTime());
            if (screeningDateTime.isBefore(LocalDateTime.now())){
                return "admin/screenings/screeningPastError";
            }
            String chosenDate = allParams.get("chosenDate");
            if (chosenDate!=null && !chosenDate.isEmpty()){
                screening.setScreeningDate(LocalDate.parse(chosenDate));
            }
            model.addAttribute("screening",screening);
            model.addAttribute("movies",movieService.getMovies());
            model.addAttribute("auditoriums",auditoriumService.getAuditoriums());
            return "admin/screenings/screeningEditForm";
        }
        return "redirect:/admin/screenings/types";
    }

    @GetMapping("/edit/date/{screeningId}")
    public String showDateEditForm(@PathVariable long screeningId, Model model) {
        Optional<Screening> optionalScreening = screeningService.getScreeningById(screeningId);
        if (optionalScreening.isPresent()) {
            model.addAttribute("screening",optionalScreening.get());
            model.addAttribute("dates",DateUtil.getTwoWeeks());
            return "admin/screenings/screeningDateEdit";
        }
        return "redirect:/admin/screenings/types";
    }

    @PostMapping("/edit")
    public String editScreening(@RequestParam Map<String, String> allRequestParams, Model model) {
        long movieId = Long.parseLong(allRequestParams.get("movieId"));
        long auditoriumId = Long.parseLong(allRequestParams.get("auditoriumId"));
        String screeningDateParam = allRequestParams.get("screeningDate");
        LocalDate screeningDate = LocalDate.parse(screeningDateParam);
        String screeningTimeParam = allRequestParams.get("screeningTime");
        if (screeningTimeParam.isEmpty()) {
            model.addAttribute("timeEmpty", true);
            model.addAttribute("chosenDate", screeningDate);
            model.addAttribute("auditoriums", auditoriumService.getAuditoriums());
            model.addAttribute("movies", movieService.getMovies());
            model.addAttribute("screening", new Screening());
            return "admin/screenings/screeningEditForm";
        }
        LocalTime screeningTime = LocalTime.parse(screeningTimeParam);
        Screening screening = new Screening();
        String screeningIdParam = allRequestParams.get("screeningId");
        long screeningId = Long.parseLong(screeningIdParam);
        screening.setId(screeningId);
        screening.setAuditorium(auditoriumService.getAuditoriumById(auditoriumId).get());
        screening.setMovie(movieService.getMovieById(movieId).get());
        screening.setScreeningDate(screeningDate);
        screening.setScreeningTime(screeningTime);
        boolean overlapping = screeningService.checkOverlapping(screening, screeningId);
        if (overlapping) {
            model.addAttribute("overlapping", true);
            model.addAttribute("chosenDate", screeningDate);
            model.addAttribute("auditoriums", auditoriumService.getAuditoriums());
            model.addAttribute("movies", movieService.getMovies());
            model.addAttribute("screening", screening);
            return "admin/screenings/screeningEditForm";
        }
        screeningService.addScreening(screening);
        return "redirect:/admin/screenings/types";
    }
}
