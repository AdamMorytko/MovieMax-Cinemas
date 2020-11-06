package pl.morytko.moviemax.movies;

import java.util.List;

public interface MovieService {
    List<Movie> getMovies();
    void addMovie(Movie movie);
    void updateMovie(Movie movie);
    void deleteMovie(long id);
}
