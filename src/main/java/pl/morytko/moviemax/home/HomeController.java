package pl.morytko.moviemax.home;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.morytko.moviemax.cinemas.CinemaService;
import pl.morytko.moviemax.movies.Movie;
import pl.morytko.moviemax.movies.MovieService;
import pl.morytko.moviemax.screenings.Screening;
import pl.morytko.moviemax.screenings.ScreeningService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@SessionAttributes("cinemaId")
public class HomeController {
    private final MovieService movieService;
    private final CinemaService cinemaService;
    private final ScreeningService screeningService;

    @RequestMapping
    public String getCinema(Model model) {
        model.addAttribute("cinemas", cinemaService.getCinemas());
        return "main/getCinemaId";

    }

    @GetMapping("/screenings")
    public String showScreenings(@RequestParam Map<String, String> allRequestParams,
                                 Model model) {
        if (allRequestParams.get("cinemaId") == null) {
            return "redirect:/";
        }
        long cinemaId = Long.parseLong(allRequestParams.get("cinemaId"));
        String screeningDateParam = allRequestParams.get("screeningDate");
        LocalDate screeningDate;
        if (screeningDateParam == null) {
            screeningDate = LocalDate.now();
        } else {
            screeningDate = LocalDate.parse(screeningDateParam);
        }
        List<Screening> screeningList = screeningService.getScreeningsByCinemaAndDate(cinemaId, screeningDate);
        Multimap<Movie,Screening> screeningMultimap = ArrayListMultimap.create();
        List<Screening> futureScreenings = screeningList
                .stream()
                .filter(s -> s.getScreeningTime().isAfter(LocalTime.now()))
                .collect(Collectors.toList());
        futureScreenings.forEach(s->screeningMultimap.put(s.getMovie(),s));
        model.addAttribute("screenings",screeningMultimap);
        return "main/screenings";

    }

}
