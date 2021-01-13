package pl.morytko.moviemax.events;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.morytko.moviemax.auditoriums.Auditorium;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.cinemas.Cinema;
import pl.morytko.moviemax.cinemas.CinemaService;
import pl.morytko.moviemax.movies.Movie;
import pl.morytko.moviemax.movies.MovieService;
import pl.morytko.moviemax.seats.Seat;
import pl.morytko.moviemax.seats.SeatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@AllArgsConstructor
public class DatabaseInitialization {

    private final CinemaService cinemaService;
    private final MovieService movieService;
    private final AuditoriumService auditoriumService;
    private final SeatRepository seatRepository;
    private final Random random = new Random();


    @EventListener(ApplicationReadyEvent.class)
    public void appStart() throws InterruptedException {
        List<Cinema> cinemaList = cinemaService.getCinemas();
        if (cinemaList.size() == 0) {
            Thread.sleep(1000);
            cinemaList = cinemaService.getCinemas();
            ;
        }
        cinemaList.forEach(cinema -> {
            for (int h = 0; h < 5; h++) {
                Auditorium newAuditorium = new Auditorium();
                int seatsInARow = random.nextInt(20 - 16) + 16;
                newAuditorium.setSeatNumberCount(seatsInARow);
                int rows = random.nextInt(13 - 5) + 5;
                newAuditorium.setSeatRowCount(rows);
                int screenStart = random.nextInt(4 - 2) + 2;
                System.out.println("start: "+screenStart);
                newAuditorium.setScreenStart(screenStart);
                newAuditorium.setScreenEnd(seatsInARow - screenStart + 1);
                newAuditorium.setCinema(cinema);
                Auditorium savedAuditorium = auditoriumService.addAuditorium(newAuditorium);

                List<Seat> seatList = new ArrayList<>();
                int firstStairs = 3;
                int secondStairs = seatsInARow - 2;
                for (int i = 1; i <= rows; i++) {
                    for (int j = 1; j <= seatsInARow; j++) {
                        Seat newSeat = new Seat();
                        newSeat.setRow(i);
                        newSeat.setNumber(j);
                        if ((j == firstStairs || j == secondStairs) && i != rows) {
                            newSeat.setStatus((short) 2);
                        } else {
                            newSeat.setStatus((short) 1);
                        }
                        newSeat.setAuditorium(savedAuditorium);
                        seatList.add(newSeat);
                    }
                }
                seatRepository.saveAll(seatList);
            }
        });
        List<Movie> movieList = movieService.getMovies();
        List<Integer> uniqueIndexList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int newIndex = random.nextInt(movieList.size()-1)+1;
        }

    }
}
