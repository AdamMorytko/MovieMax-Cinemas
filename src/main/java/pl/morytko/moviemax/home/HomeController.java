package pl.morytko.moviemax.home;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.morytko.moviemax.cinemas.Cinema;
import pl.morytko.moviemax.cinemas.CinemaService;
import pl.morytko.moviemax.movies.Movie;
import pl.morytko.moviemax.screenings.Screening;
import pl.morytko.moviemax.screenings.ScreeningService;
import pl.morytko.moviemax.utils.DateUtil;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class HomeController {
    private final CinemaService cinemaService;
    private final ScreeningService screeningService;

    @GetMapping("/")
    public String showCinemaChooseForm(Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        Object cinemaIdAttribute = session.getAttribute("cinemaId");
        if (cinemaIdAttribute == null) {
            List<Cinema> cinemas = cinemaService.getCinemas();
            if (cinemas.size() > 0) {
                model.addAttribute("cinemas", cinemas);
            } else {
                model.addAttribute("cinemasEmpty", true);
            }
        } else {
            return "redirect:/dates";
        }
        return "main/cinemaChooseForm";
    }

    @GetMapping("/dates")
    public String showDateChooseForm(@RequestParam Map<String, String> allRequestParams, Model model) {
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        Object cinemaIdAttribute = session.getAttribute("cinemaId");
        if (cinemaIdAttribute == null) {
            if (allRequestParams.get("cinemaId") == null) {
                return "redirect:/";
            } else {
                long cinemaId = Long.parseLong(allRequestParams.get("cinemaId"));
                return addCinemaIdToSessionAndReturnDateChooseForm(model, session, cinemaId);
            }
        } else {
            long cinemaId = (long) cinemaIdAttribute;
            return addCinemaIdToSessionAndReturnDateChooseForm(model, session, cinemaId);
        }
    }

    private String addCinemaIdToSessionAndReturnDateChooseForm(Model model, HttpSession session, long cinemaId) {
        Optional<Cinema> cinemaOptional = cinemaService.getCinemaById(cinemaId);
        if (cinemaOptional.isPresent()) {
            session.setAttribute("cinemaId", cinemaId);
            model.addAttribute("cinema", cinemaOptional.get());
            model.addAttribute("dates", DateUtil.getTwoWeeks());
            return "main/dateChooseForm";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/clearCinemaId")
    public String clearCinemaIdFromSession(){
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.removeAttribute("cinemaId");
        return "redirect:/";
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
        Multimap<Movie, Screening> screeningMultimap = Multimaps.newSetMultimap(
                new TreeMap<>(),
                LinkedHashSet::new);
        List<Screening> futureScreenings = new ArrayList<>();
        if (screeningDate.isEqual(LocalDate.now())) {
            futureScreenings = screeningList
                    .stream()
                    .filter(s -> (s.getScreeningTime().isAfter(LocalTime.now()) ||
                            s.getScreeningTime().equals(LocalTime.now())))
                    .collect(Collectors.toList());
        } else if (screeningDate.isAfter(LocalDate.now())) {
            futureScreenings = screeningList;
        }
        futureScreenings = futureScreenings.stream()
                .sorted(Comparator.comparing(Screening::getScreeningTime))
                .collect(Collectors.toList());
        futureScreenings.forEach(s -> screeningMultimap.put(s.getMovie(), s));
        model.addAttribute("screenings", screeningMultimap);
        model.addAttribute("cinemaId", cinemaId);
        model.addAttribute("dates", DateUtil.getTwoWeeks());
        model.addAttribute("cinemas", cinemaService.getCinemas());
        return "main/screenings";
    }


}
