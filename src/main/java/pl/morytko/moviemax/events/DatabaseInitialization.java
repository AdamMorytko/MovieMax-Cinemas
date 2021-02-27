package pl.morytko.moviemax.events;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.morytko.moviemax.auditoriums.Auditorium;
import pl.morytko.moviemax.auditoriums.AuditoriumService;
import pl.morytko.moviemax.cinemas.Cinema;
import pl.morytko.moviemax.cinemas.CinemaService;
import pl.morytko.moviemax.movies.Movie;
import pl.morytko.moviemax.movies.MovieService;
import pl.morytko.moviemax.reservations.Reservation;
import pl.morytko.moviemax.reservations.ReservationService;
import pl.morytko.moviemax.reservedSeats.ReservedSeat;
import pl.morytko.moviemax.reservedSeats.ReservedSeatService;
import pl.morytko.moviemax.screenings.Screening;
import pl.morytko.moviemax.screenings.ScreeningService;
import pl.morytko.moviemax.seats.Seat;
import pl.morytko.moviemax.seats.SeatRepository;
import pl.morytko.moviemax.users.User;
import pl.morytko.moviemax.users.UserRole;
import pl.morytko.moviemax.users.UserService;
import pl.morytko.moviemax.utils.DateUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static pl.morytko.moviemax.users.UserRole.ADMIN;
import static pl.morytko.moviemax.users.UserRole.USER;

@Component
@AllArgsConstructor
public class DatabaseInitialization {

    private final CinemaService cinemaService;
    private final MovieService movieService;
    private final AuditoriumService auditoriumService;
    private final SeatRepository seatRepository;
    private final ScreeningService screeningService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ReservationService reservationService;
    private final ReservedSeatService reservedSeatService;
    private final Random random = new Random();


    @EventListener(ApplicationReadyEvent.class)
    public void appStart() throws InterruptedException {
        // initial test users created here
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user@test.com");
        user1.setPassword(passwordEncoder.encode("usertest"));
        user1.setName("Jan");
        user1.setSurname("Kowalski");
        Set<UserRole> roles1 = new HashSet<>();
        roles1.add(USER);
        user1.setRoles(roles1);
        user1.setEnabled(true);
        users.add(user1);

        User user2 = new User();
        user2.setUsername("admin@test.com");
        user2.setPassword(passwordEncoder.encode("admintest"));
        user2.setName("Anna");
        user2.setSurname("Nowak");
        Set<UserRole> roles2 = new HashSet<>();
        roles2.add(ADMIN);
        user2.setRoles(roles2);
        user2.setEnabled(true);
        users.add(user2);

        userService.addUserList(users);

        // movies created here
        List<String> movie1 = Arrays.asList("W deszczowy dzień w Nowym Jorku", "Woody Allen", "USA", "Gdy dwoje młodych ludzi przyjeżdża na weekend do Nowego Jorku, miasto wita ich niepogodą i serią przygód.", "Komedia", "92", "https://fwcdn.pl/fpo/41/94/794194/7889452.6.jpg");
        List<String> movie2 = Arrays.asList("1917", "Sam Mendes", "Hiszpania Indie Kanada USA Wielka Brytania", "Dwóch młodych żołnierzy - Schofield i Blake dostają rozkaz przedostania się za linie wroga, aby dostarczyć tajną wiadomość, która pozwoli brytyjskim żołnierzy uniknąć wpadnięcia w śmiertelną pułapkę.", "Wojenny", "119", "https://fwcdn.pl/fpo/69/80/816980/7913530.6.jpg");
        List<String> movie3 = Arrays.asList("Joker", "Todd Phillips", "Kanada USA", "Strudzony życiem komik popada w obłęd i staje się psychopatycznym mordercą.", "Dramat / Kryminał", "122", "https://fwcdn.pl/fpo/01/67/810167/7905225.6.jpg");
        List<String> movie4 = Arrays.asList("Saint Maud", "Rose Glass", "Wielka Brytania", "Maud przyjeżdża do luksusowej posiadłości ekscentrycznej Amandy. Wkrótce dziewczyna przekonuje się o swojej ważnej misji, która zamieni się w niebezpieczną obsesję...", "Horror", "83", "https://fwcdn.pl/fpo/09/45/840945/7933376.6.jpg");
        List<String> movie5 = Arrays.asList("Irlandczyk", "Martin Scorsese", "USA", "Płatny zabójca Frank Sheeran powraca do sekretów, których strzegł jako lojalny członek rodziny przestępczej Bufalino.", "Biograficzny / Gangsterski", "210", "https://fwcdn.pl/fpo/37/94/513794/7902504.6.jpg");
        List<String> movie6 = Arrays.asList("365 dni", "Barbara Białowąs / Tomasz Mandes", "Polska", "Laura, aby ratować rozpadający się związek, wyjeżdża na Sycylię, gdzie poznaje Massimo. Niebezpieczny mężczyzna, szef rodziny mafijnej, porywa ją i daje 365 dni na pokochanie go.", "Erotyczny", "116", "https://fwcdn.pl/fpo/24/52/842452/7909947.6.jpg");
        List<String> movie7 = Arrays.asList("Tenet", "Christopher Nolan", "Kanada USA Wielka Brytania", "Uzbrojony tylko w jedno słowo — Tenet — bohater przenika w mroczny świat międzynarodowych szpiegów, próbując ocalić ludzkość. Do tego jednak nieodzowne okazuje się skorzystanie ze zjawiska wykraczającego poza czas realny.", "Thriller / Akcja / Sci-Fi", "150", "https://fwcdn.pl/fpo/16/53/831653/7926046.6.jpg");
        List<String> movie8 = Arrays.asList("Sala samobójców. Hejter", "Jan Komasa", "Polska", "Wydalony z Uniwersytetu Warszawskiego student prawa, zostaje przyłapany na plagiacie. Postanawia jednak ukrywać ten fakt przed światem i nadal pobiera pomoc finansową od państwa Krasuckich.", "Thriller", "130", "https://fwcdn.pl/fpo/80/17/818017/7914782.6.jpg");
        List<String> movie9 = Arrays.asList("Mulan", "Niki Caro", "USA", "Kobieta o imieniu Hua Mulan przebrana za mężczyznę zaciąga się do cesarskiej armii, aby pomóc swojemu schorowanemu ojcu.", "Fantasy / Przygodowy", "115", "https://fwcdn.pl/fpo/00/66/740066/7929880.6.jpg");
        List<String> movie10 = Arrays.asList("Psy 3. W imię zasad", "Władysław Pasikowski", "Polska", "Po 25 latach odsiadki Franz Maurer wkracza w nową Polskę, gdzie nic nie jest takie, jak zapamiętał. Wkrótce ponownie spotyka Nowego.", "Sensacyjny", "126", "https://fwcdn.pl/fpo/58/16/835816/7908750.6.jpg");
        List<String> movie11 = Arrays.asList("25 lat niewinności. Sprawa Tomka Komendy", "Jan Holoubek", "Polska", "Tomasz Komenda miał 23 lata, kiedy jego normalne życie zostało brutalnie przerwane. Z dnia na dzień został zatrzymany...", "Dramat / Sensacyjny", "116", "https://fwcdn.pl/fpo/43/42/844342/7930471.6.jpg");
        List<String> movie12 = Arrays.asList("Naprzód", "Dan Scanlon", "21 lutego 2020 (świat)", "Dwóch braci próbuje sprowadzić na ziemię drugą połowę ciała swojego ojca, gdyż nieudane czary powodują, że pojawia się tylko od pasa w dół.", "Animacja", "102", "https://fwcdn.pl/fpo/21/78/832178/7890671.6.jpg");
        List<String> movie13 = Arrays.asList("Proces Siódemki z Chicago", "Aaron Sorkin", "USA", "Pokojowy protest przerodził się w brutalne starcie z policją, a jego organizatorzy stanęli przed sądem. Tak rozpoczął się jeden z najgłośniejszych procesów w historii.", "Dramat sądowy", "129", "https://fwcdn.pl/fpo/12/41/451241/7931788.6.jpg");
        List<String> movie14 = Arrays.asList("Zenek", "Jan Hryniak", "Polska", "Historia chłopaka z podlaskiej wsi, który realizuje swoje wielkie marzenie, by śpiewać i bawić tłumy. Dzięki uporowi, ciężkiej pracy i wielu wyrzeczeniom osiągnął niebywały sukces.", "Biograficzny / Muzyczny", "120", "https://fwcdn.pl/fpo/92/25/829225/7911651.6.jpg");
        List<String> movie15 = Arrays.asList("Dylemat społeczny", "Jeff Orlowski", "USA", "Ukazanie groźnych skutków ludzkiego wpływu na sieci społecznościowe. Ten problem najlepiej zauważają ich twórcy — eksperci ds. technologii.", "Dokumentalny", "89", "https://fwcdn.pl/fpo/95/86/859586/7930217.6.jpg");
        List<String> movie16 = Arrays.asList("Pewnego razu... w Hollywood", "Quentin Tarantino", "USA Wielka Brytania", "Aktor Rick Dalton i jego przyjaciel kaskader powracają do Hollywood. Mężczyźni próbują odnaleźć się w przemyśle filmowym, który ewoluował podczas ich nieobecności.", "Dramat / Kryminał", "161", "https://fwcdn.pl/fpo/45/61/804561/7891889.6.jpg");
        List<String> movie17 = Arrays.asList("Parasite", "Joon-ho Bong", "Korea Południowa", "Kiedy Ki-woo dostaje pracę jako korepetytor córki zamożnego małżeństwa, wymyśla sposób na zapewnienie zatrudnienia również reszcie swojej rodziny.", "Dramat", "132", "https://fwcdn.pl/fpo/81/43/798143/7895390.6.jpg");
        List<String> movie18 = Arrays.asList("Green Book", "Peter Farrelly", "USA", "Drobny cwaniaczek z Bronksu zostaje szoferem ekstrawaganckiego muzyka z wyższych sfer i razem wyruszają na wielotygodniowe tournée.", "Dramat / Komedia", "130", "https://fwcdn.pl/fpo/96/30/809630/7873350.6.jpg");
        List<String> movie19 = Arrays.asList("Bohemian Rhapsody", "Bryan Singer", "USA Wielka Brytania", "Dzięki oryginalnemu brzmieniu Queen staje się jednym z najpopularniejszych zespołów w historii muzyki.", "Biograficzny / Dramat / Muzyczny", "134", "https://fwcdn.pl/fpo/92/01/619201/7863181.6.jpg");
        List<List<String>> movies = new ArrayList<>(Arrays.asList(movie1, movie2, movie3, movie4, movie5, movie6, movie7, movie8, movie9, movie10, movie11, movie12, movie13, movie14, movie15, movie16, movie17, movie18, movie19));
        List<Movie> moviePersistList = new ArrayList<>();
        for (List<String> movie :
                movies) {
            Movie newMovie = new Movie();
            newMovie.setTitle(movie.get(0));
            newMovie.setDirector(movie.get(1));
            newMovie.setCountryOrigin(movie.get(2));
            newMovie.setDescription(movie.get(3));
            newMovie.setGenre(movie.get(4));
            newMovie.setDuration(Integer.parseInt(movie.get(5)));
            newMovie.setPosterUrl(movie.get(6));
            moviePersistList.add(newMovie);
        }
        movieService.addMovies(moviePersistList);

        // cinemas created here
        List<String> cinema1 = Arrays.asList("Gliwice", "Zwycięstwa 13");
        List<String> cinema2 = Arrays.asList("Warszawa", "Polna 77");
        List<String> cinema3 = Arrays.asList("Zabrze", "Leśna 34");
        List<String> cinema4 = Arrays.asList("Sopot", "Słoneczna 19");
        List<String> cinema5 = Arrays.asList("Gdańsk", "Lipowa 93");
        List<List<String>> cinemas = new ArrayList<>(Arrays.asList(cinema1, cinema2, cinema3, cinema4, cinema5));
        List<Cinema> cinemaPersistList = new ArrayList<>();
        for (List<String> cinema :
                cinemas) {
            Cinema newCinema = new Cinema();
            newCinema.setCity(cinema.get(0));
            newCinema.setStreet(cinema.get(1));
            cinemaPersistList.add(newCinema);
        }
        cinemaService.addCinemas(cinemaPersistList);

        // cinemas -> auditoriums -> screenings created here
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        List<Cinema> cinemaList = cinemaService.getCinemas();
        List<Movie> movieList = movieService.getMovies();
        List<Screening> newScreenings = new ArrayList<>();
        List<LocalDate> twoWeeks = DateUtil.getTwoWeeks();
        cinemaList.forEach(cinema -> {
            for (int h = 0; h < 3; h++) {
                Auditorium newAuditorium = new Auditorium();
                int seatsInARow = random.nextInt(20 - 16) + 16;
                newAuditorium.setSeatNumberCount(seatsInARow);
                int rows = random.nextInt(13 - 5) + 5;
                newAuditorium.setSeatRowCount(rows);
                int screenStart = random.nextInt(4 - 2) + 2;
                newAuditorium.setScreenStart(screenStart);
                newAuditorium.setScreenEnd(seatsInARow - screenStart + 1);
                newAuditorium.setCinema(cinema);
                Auditorium savedAuditorium = auditoriumService.addAuditorium(newAuditorium);
                System.out.printf("INSERT INTO auditorium (seat_number_count, seat_row_count, screen_start, screen_end, cinema_id) VALUES (%d, %d, %d, %d, %d);%n",
                        newAuditorium.getSeatNumberCount(), newAuditorium.getSeatRowCount(), newAuditorium.getScreenStart(), newAuditorium.getScreenEnd(), newAuditorium.getCinema().getId());
                AtomicInteger atomicInteger = new AtomicInteger(0);
                twoWeeks.forEach(day -> {
                    List<LocalTime> timesList = new LinkedList<>(Arrays.asList(
                            LocalTime.of(11, 5),
                            LocalTime.of(16, 30),
                            LocalTime.of(21, 0)
                    ));
                    for (int i = 0; i < 3; i++) {
                        Movie movie = movieList.get(random.nextInt(movieList.size()));
                        Screening screening = new Screening();
                        LocalTime screeningTime;
                        screeningTime = timesList.get(i);
                        screening.setScreeningDate(day);
                        screening.setScreeningTime(screeningTime);
                        screening.setAuditorium(savedAuditorium);
                        screening.setMovie(movie);
                        System.out.printf("INSERT INTO screening (screening_date, screening_time, auditorium_id, movie_id) VALUES ((SELECT CURDATE()+INTERVAL %d DAY), '%s', %d, %d);%n",
                                atomicInteger.getAndIncrement(), screening.getScreeningTime().format(timeFormatter), screening.getAuditorium().getId(), screening.getMovie().getId());
                        newScreenings.add(screening);
                        if (atomicInteger.get() == 14) {
                            atomicInteger.set(0);
                        }
                    }
                });
                screeningService.saveAll(newScreenings);

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
                        System.out.printf("INSERT INTO seat (row, number, status, auditorium_id) VALUES (%d, %d, %d, %d);%n",
                                newSeat.getRow(), newSeat.getNumber(), newSeat.getStatus(), newSeat.getAuditorium().getId());
                        seatList.add(newSeat);
                    }
                }
                seatRepository.saveAll(seatList);
            }
        });
        for (int i = 1; i < 6; i++) {
            Reservation reservation = new Reservation();
            reservation.setUser(user1);
            reservation.setScreening(screeningService.getScreeningById(i).get());
            reservation.setReservedSeatNumber(2);
            List<ReservedSeat> reservedSeats = new ArrayList<>();
            ReservedSeat reservedSeat1 = new ReservedSeat();
            ReservedSeat reservedSeat2 = new ReservedSeat();
            reservedSeat1.setNumber(10);
            reservedSeat1.setRow(1);
            reservedSeat2.setNumber(11);
            reservedSeat2.setRow(1);
            reservedSeats.add(reservedSeat1);
            reservedSeats.add(reservedSeat2);
            reservation.setReservedSeats(reservedSeats);
            Reservation savedReservation = reservationService.addReservation(reservation);
            System.out.printf("INSERT INTO reservation (reserved_seat_number, screening_id, user_id) VALUES (%d, %d, %d);%n",
                    reservation.getReservedSeatNumber(), reservation.getScreening().getId(), reservation.getUser().getId());
            for (ReservedSeat reservedSeat : reservedSeats) {
                reservedSeat.setReservation(savedReservation);
            }
            reservedSeatService.addReservedSeats(reservedSeats);
            System.out.printf("INSERT INTO reserved_seat (number, row, reservation_id) VALUES (%d, %d, %d);%n",
                    reservedSeat1.getNumber(), reservedSeat1.getRow(), reservedSeat1.getReservation().getId());
            System.out.printf("INSERT INTO reserved_seat (number, row, reservation_id) VALUES (%d, %d, %d);%n",
                    reservedSeat1.getNumber(), reservedSeat1.getRow(), reservedSeat1.getReservation().getId());
        }
    }
}
