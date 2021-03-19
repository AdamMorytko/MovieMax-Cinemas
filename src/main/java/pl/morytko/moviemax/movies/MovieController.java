package pl.morytko.moviemax.movies;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.morytko.moviemax.utils.Filmweb;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping("/list")
    public String showMovieList(Model model){
        model.addAttribute("movies", movieService.getMovies());
        return "admin/movies/movieList";
    }

    @GetMapping("/details/{id}")
    public String showMovieDetails(@PathVariable long id, Model model){
        model.addAttribute("movie", movieService.getMovieById(id).get());
        return "admin/movies/movieDetails";
    }

    @GetMapping("/add")
    public String showAddForm(Model model){
        model.addAttribute("movie",new Movie());
        return "admin/movies/movieAddForm";
    }

    @PostMapping("/add")
    public String addMovie(@Valid Movie movie, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "admin/movies/movieAddForm";
        }
        movieService.addMovie(movie);
        return "redirect:/admin/movies/list";
    }

    @RequestMapping("/filmweb")
    public String fillAddForm(@RequestParam String filmwebUrl, Model model) {
        Movie movie = new Movie();
        Filmweb filmweb;
        Map<String, String> errors = new HashMap<>();
        try {
            filmweb = new Filmweb(filmwebUrl);
        } catch(IOException | IllegalArgumentException exception){
            model.addAttribute("errorLoadingFilmweb","true");
            model.addAttribute("movie", movie);
            return "admin/movies/movieAddForm";
        }

        Optional<String> title = filmweb.getTitle();
        if (title.isPresent()){
            movie.setTitle(title.get());
        }else{
            errors.put("titleError", "tytuł");
        }

        Optional<String> director = filmweb.getDirector();
        if (director.isPresent()){
            movie.setDirector(director.get());
        }else{
            errors.put("directorError", "reżyser");
        }

        Optional<String> countryOfOrigin = filmweb.getCountryOfOrigin();
        if (countryOfOrigin.isPresent()){
            movie.setCountryOrigin(countryOfOrigin.get());
        }else{
            errors.put("countryOfOriginError", "kraj produkcji");
        }

        Optional<Integer> duration = filmweb.getDuration();
        if (duration.isPresent()){
            movie.setDuration(duration.get());
        }else{
            errors.put("durationError", "czas trwania");
        }

        Optional<String> description = filmweb.getDescription();
        if (description.isPresent()){
            movie.setDescription(description.get());
        }else{
            errors.put("descriptionError", "opis");
        }

        Optional<String> genre = filmweb.getGenre();
        if (genre.isPresent()){
            movie.setGenre(genre.get());
        }else{
            errors.put("genreError", "gatunek");
        }

        Optional<String> posterUrl = filmweb.getPosterUrl();
        if (posterUrl.isPresent()){
            movie.setPosterUrl(posterUrl.get());
        }else{
            errors.put("posterUrlError", "link do plakatu");
        }
        model.addAttribute("movie",movie);
        if (errors.size() > 0){
            model.addAttribute("errors",errors);
        }
        return "admin/movies/movieAddForm";
    }

    @GetMapping("/delete/{movieId}")
    public String showDeleteConfirmation(@PathVariable("movieId") String movieIdParam, Model model) throws NumberFormatException{
        long movieId;
        if (movieIdParam.isEmpty()){
            return "redirect:/admin/movies/list";
        }else{
            try {
                movieId = Long.parseLong(movieIdParam);
            }catch (NumberFormatException nfe){
                throw new NumberFormatException();
            }
        }
        Optional<Movie> movieOptional = movieService.getMovieById(movieId);
        if (movieOptional.isPresent()){
            model.addAttribute("movie",movieOptional.get());
            return "admin/movies/movieDeleteConfirmation";
        }
        return "redirect:/admin/movies/list";
    }

    @GetMapping("/edit/{movieId}")
    public String showEditForm(@PathVariable("movieId") String movieIdParam, Model model) throws NumberFormatException{
        long movieId;
        if (movieIdParam.isEmpty()){
            return "redirect:/admin/movies/list";
        }else{
            try {
                movieId = Long.parseLong(movieIdParam);
            }catch (NumberFormatException nfe){
                throw new NumberFormatException();
            }
        }
        Optional<Movie> movieOptional = movieService.getMovieById(movieId);
        if (movieOptional.isPresent()){
            model.addAttribute("movie",movieOptional.get());
            return "admin/movies/movieEditForm";
        }
        return "redirect:/admin/movies/list";
    }

    @PostMapping("/delete")
    public String deleteMovie(@RequestParam("movieId") String movieIdParam) throws NumberFormatException{
        long movieId;
        if (movieIdParam.isEmpty()){
            return "redirect:/admin/movies/list";
        }else{
            try {
                movieId = Long.parseLong(movieIdParam);
            }catch (NumberFormatException nfe){
                throw new NumberFormatException();
            }
        }
        Optional<Movie> movieOptional = movieService.getMovieById(movieId);
        if (movieOptional.isPresent()){
            movieService.deleteMovie(movieId);
        }
        return "redirect:/admin/movies/list";
    }


}
