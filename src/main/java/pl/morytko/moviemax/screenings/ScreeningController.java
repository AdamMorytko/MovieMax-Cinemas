package pl.morytko.moviemax.screenings;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.movies.Movie;
import pl.morytko.moviemax.movies.MovieService;
import pl.morytko.moviemax.utils.DateUtil;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/screenings")
@AllArgsConstructor
public class ScreeningController {
    private final ScreeningService screeningService;
    private final AuditoriumService auditoriumService;
    private final MovieService movieService;

    @GetMapping("/date")
    public String showChooseDate(Model model){
        model.addAttribute("dates", DateUtil.getTwoWeeks());
        return "admin/screenings/screeningChooseDate";
    }

    @PostMapping("/date")
    public String showAddForm(Model model, @RequestParam Map<String,String> allRequestParams){
        model.addAttribute("auditoriums",auditoriumService.getAuditoriums());
        model.addAttribute("movies",movieService.getMovies());
        model.addAttribute("screening",new Screening());
        model.addAttribute("time",LocalTime.now());
        String chosenDate = allRequestParams.get("chosenDate");
        if (!chosenDate.isEmpty()){
            model.addAttribute("chosenDate",chosenDate);
        }
        return "admin/screenings/screeningAddForm";
    }

    @PostMapping("/add")
    public String addScreening(@RequestParam Map<String,String> allRequestParams, Model model){
        long movieId = Long.parseLong(allRequestParams.get("movieId"));
        long auditoriumId = Long.parseLong(allRequestParams.get("auditoriumId"));
        System.out.println(allRequestParams.get("screeningDate"));
        System.out.println(allRequestParams.get("screeningTime"));
        LocalDate screeningDate = LocalDate.parse(allRequestParams.get("screeningDate"));
        LocalTime screeningTime = LocalTime.parse(allRequestParams.get("screeningTime"));
        System.out.println(screeningDate);
        System.out.println(screeningTime);
        Screening screening = new Screening();
        screening.setAuditorium(auditoriumService.getAuditoriumById(auditoriumId).get());
        screening.setMovie(movieService.getMovieById(movieId).get());
        screening.setScreeningDate(screeningDate);
        screening.setScreeningTime(screeningTime);
        screeningService.addScreening(screening);
        System.out.println(screeningService.getScreeningsByAuditoriumId(auditoriumId).get(0).getScreeningTime());
        return "redirect:/screenings/list";
    }

}
