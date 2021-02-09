package pl.morytko.moviemax.cinemas;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.morytko.moviemax.auditoriums.AuditoriumService;


import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/cinemas")
@AllArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;
    private final AuditoriumService auditoriumService;


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cinema", new Cinema());
        return "admin/cinemas/cinemaAddForm";
    }

    @PostMapping("/add")
    public String addCinema(@Valid Cinema cinema, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/cinemas/cinemaAddForm";
        }
        cinemaService.addCinema(cinema);
        return "redirect:/admin/cinemas/list";
    }

    @GetMapping("/details/{id}")
    public String showCinemaDetails(@PathVariable long id, Model model) {
        Optional<Cinema> cinemaOptional = cinemaService.getCinemaById(id);
        if (cinemaOptional.isPresent()) {
            model.addAttribute("cinema", cinemaOptional.get());
            model.addAttribute("auditoriumsIds",auditoriumService.getListOfAuditoriumIdOfCinema(id));
        }
        return "admin/cinemas/cinemaDetails";
    }

    @GetMapping("/list")
    public String showCinemasList(Model model) {
        model.addAttribute("cinemas", cinemaService.getCinemas());
        return "admin/cinemas/cinemaList";
    }

    @GetMapping("/delete/{cinemaId}")
    public String showDeleteConfirmation(@PathVariable("cinemaId") String cinemaIdParam, Model model) throws NumberFormatException{
        long cinemaId;
        if (cinemaIdParam.isEmpty()){
            return "redirect:/admin/cinemas/list";
        }else{
            try {
                cinemaId = Long.parseLong(cinemaIdParam);
            }catch (NumberFormatException nfe){
                throw new NumberFormatException();
            }
        }
        Optional<Cinema> cinemaOptional = cinemaService.getCinemaById(cinemaId);
        if (cinemaOptional.isPresent()){
            model.addAttribute("cinema",cinemaOptional.get());
            return "admin/cinemas/cinemaDeleteConfirmation";
        }
        return "redirect:/admin/cinemas/list";
    }

    @PostMapping("/delete")
    public String deleteCinema(@RequestParam("cinemaId") String cinemaIdParam) throws NumberFormatException{
        long cinemaId;
        if (cinemaIdParam.isEmpty()){
            return "redirect:/admin/cinemas/list";
        }else{
            try {
                cinemaId = Long.parseLong(cinemaIdParam);
            }catch (NumberFormatException nfe){
                throw new NumberFormatException();
            }
        }
        Optional<Cinema> cinemaOptional = cinemaService.getCinemaById(cinemaId);
        if (cinemaOptional.isPresent()){
            cinemaService.deleteCinema(cinemaId);
        }
        return "redirect:/admin/cinemas/list";
    }

    @GetMapping("/edit/{cinemaId}")
    public String showEditForm(@PathVariable("cinemaId") String cinemaIdParam, Model model) throws NumberFormatException{
        long cinemaId;
        if (cinemaIdParam.isEmpty()){
            return "redirect:/admin/cinemas/list";
        }else{
            try {
                cinemaId = Long.parseLong(cinemaIdParam);
            }catch (NumberFormatException nfe){
                throw new NumberFormatException();
            }
        }
        Optional<Cinema> cinemaOptional = cinemaService.getCinemaById(cinemaId);
        if (cinemaOptional.isPresent()){
            model.addAttribute("cinema",cinemaOptional.get());
            return "admin/cinemas/cinemaEditForm";
        }
        return "redirect:/admin/cinemas/list";
    }
}
