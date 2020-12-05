package pl.morytko.moviemax.utils;


import pl.morytko.moviemax.reservedSeats.ReservedSeat;

import java.util.List;

public class ReservedSeatUtil {

    public boolean isReserved(int number, int row, List<ReservedSeat> reservedSeats){
        ReservedSeat suspectedReservedSeat = new ReservedSeat();
        suspectedReservedSeat.setNumber(number);
        suspectedReservedSeat.setRow(row);
        boolean found = false;
        for (ReservedSeat reservedSeat:
             reservedSeats) {
            if (reservedSeat.equals(suspectedReservedSeat)) {
                found = true;
                break;
            }
        }
        return found;
    }
}
