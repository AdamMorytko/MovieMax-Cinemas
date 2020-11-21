package pl.morytko.moviemax.auditoriums;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {
    List<Auditorium> getAllByCinema_Id(long id);

    @Query("SELECT a.id FROM Auditorium a WHERE a.cinema.id = ?1")
    List<Long> getIdsByCinema_Id(long cinemaId);
}
